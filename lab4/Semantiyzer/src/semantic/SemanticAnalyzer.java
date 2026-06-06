package semantic;

import ast.*;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private final SymbolTable symbols = new SymbolTable();
    private final TriadGenerator triads = new TriadGenerator();
    private final String scopeName = "Main";

    public SemanticResult analyze(ProgramNode program) {
        for (StatementNode stmt : program.getStatements()) {
            visitStatement(stmt);
        }

        return new SemanticResult(
                new ArrayList<>(symbols.all()),
                triads.getTriads()
        );
    }

    private void visitStatement(StatementNode stmt) {
        if (stmt instanceof VariableDeclarationNode v) {
            visitVariableDeclaration(v);
        } else if (stmt instanceof AssignmentNode a) {
            visitAssignment(a);
        } else if (stmt instanceof IncrementNode inc) {
            visitIncrement(inc);
        } else if (stmt instanceof PrintNode p) {
            visitPrint(p);
        } else if (stmt instanceof IfNode i) {
            visitIf(i);
        } else if (stmt instanceof WhileNode w) {
            visitWhile(w);
        } else if (stmt instanceof BlockNode b) {
            visitBlock(b);
        } else {
            throw new SemanticException("Семантическая ошибка: неизвестный узел AST");
        }
    }

    private void visitBlock(BlockNode block) {
        for (StatementNode stmt : block.getStatements()) {
            visitStatement(stmt);
        }
    }

    private void visitVariableDeclaration(VariableDeclarationNode node) {
        String name = node.getName();
        String type = node.getType();

        SymbolInfo symbol = symbols.declare(name, type, scopeName, false);

        ExprResult init = evaluate(node.getValue());
        ensureAssignable(type, init.type, "инициализация переменной '" + name + "'");

        symbol.setInitialized(true);
        triads.emit(":=", name, init.place);
    }

    private void visitAssignment(AssignmentNode node) {
        SymbolInfo symbol = symbols.resolve(node.getIdentifier());
        if (symbol == null) {
            throw new SemanticException(
                    "Семантическая ошибка: использование необъявленной переменной '" +
                            node.getIdentifier() + "'"
            );
        }

        ExprResult value = evaluate(node.getExpression());
        ensureAssignable(symbol.getType(), value.type,
                "присваивание переменной '" + node.getIdentifier() + "'");

        symbol.setInitialized(true);
        triads.emit(":=", node.getIdentifier(), value.place);
    }

    private void visitIncrement(IncrementNode node) {
        SymbolInfo symbol = symbols.resolve(node.getIdentifier());
        if (symbol == null) {
            throw new SemanticException(
                    "Семантическая ошибка: использование необъявленной переменной '" +
                            node.getIdentifier() + "'"
            );
        }

        if (!isNumeric(symbol.getType())) {
            throw new SemanticException(
                    "Семантическая ошибка: оператор ++ применим только к числовым переменным ('"
                            + node.getIdentifier() + "')"
            );
        }

        symbol.setInitialized(true);
        String tmp = triads.emit("+", node.getIdentifier(), "1");
        triads.emit(":=", node.getIdentifier(), tmp);
    }

    private void visitPrint(PrintNode node) {
        ExprResult value = evaluate(node.getExpression());
        triads.emit("print", value.place);
    }

    private void visitIf(IfNode node) {
        ExprResult cond = evaluate(node.getCondition());
        ensureBoolean(cond.type, "условие if");

        String elseLabel = triads.newLabel();
        String endLabel = triads.newLabel();

        triads.emit("jz", cond.place, elseLabel);

        visitBlock(node.getThenBlock());

        triads.emit("jmp", endLabel);
        triads.emitLabel(elseLabel);

        if (node.getElseBlock() != null) {
            visitBlock(node.getElseBlock());
        }

        triads.emitLabel(endLabel);
    }

    private void visitWhile(WhileNode node) {
        String startLabel = triads.newLabel();
        String endLabel = triads.newLabel();

        triads.emitLabel(startLabel);

        ExprResult cond = evaluate(node.getCondition());
        ensureBoolean(cond.type, "условие while");

        triads.emit("jz", cond.place, endLabel);

        visitBlock(node.getBody());

        triads.emit("jmp", startLabel);
        triads.emitLabel(endLabel);
    }

    private ExprResult evaluate(ExpressionNode expr) {
        if (expr instanceof LiteralNode l) {
            return new ExprResult(inferLiteralType(l.getValue()), l.getValue());
        }

        if (expr instanceof IdentifierNode id) {
            SymbolInfo symbol = symbols.resolve(id.getName());
            if (symbol == null) {
                throw new SemanticException(
                        "Семантическая ошибка: использование необъявленной переменной '" +
                                id.getName() + "'"
                );
            }

            if (!symbol.isInitialized()) {
                throw new SemanticException(
                        "Семантическая ошибка: использование неинициализированной переменной '" +
                                id.getName() + "'"
                );
            }

            return new ExprResult(symbol.getType(), id.getName());
        }

        if (expr instanceof BinaryExpressionNode b) {
            ExprResult left = evaluate(b.getLeft());
            ExprResult right = evaluate(b.getRight());
            String op = b.getOperator();

            switch (op) {
                case "+", "-", "*", "/" -> {
                    ensureNumeric(left.type, "левый операнд операции '" + op + "'");
                    ensureNumeric(right.type, "правый операнд операции '" + op + "'");

                    String resultType = (left.type.equals("double") || right.type.equals("double"))
                            ? "double"
                            : "int";

                    String place = triads.emit(op, left.place, right.place);
                    return new ExprResult(resultType, place);
                }
                case ">", "<", ">=", "<=" -> {
                    ensureNumeric(left.type, "левый операнд операции '" + op + "'");
                    ensureNumeric(right.type, "правый операнд операции '" + op + "'");

                    String place = triads.emit(op, left.place, right.place);
                    return new ExprResult("bool", place);
                }
                case "==", "!=" -> {
                    if (!isComparable(left.type, right.type)) {
                        throw new SemanticException(
                                "Семантическая ошибка: несовместимые типы в операции '" + op + "'"
                        );
                    }

                    String place = triads.emit(op, left.place, right.place);
                    return new ExprResult("bool", place);
                }
                default -> throw new SemanticException(
                        "Семантическая ошибка: неизвестный оператор '" + op + "'"
                );
            }
        }

        throw new SemanticException("Семантическая ошибка: неизвестное выражение AST");
    }

    private String inferLiteralType(String value) {
        if ("true".equals(value) || "false".equals(value)) {
            return "bool";
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return "string";
        }
        if (value.contains(".")) {
            return "double";
        }
        return "int";
    }

    private void ensureAssignable(String leftType, String rightType, String context) {
        if (!leftType.equals(rightType)) {
            throw new SemanticException(
                    "Семантическая ошибка: несоответствие типов в " + context +
                            " (ожидался " + leftType + ", найден " + rightType + ")"
            );
        }
    }

    private void ensureNumeric(String type, String context) {
        if (!isNumeric(type)) {
            throw new SemanticException(
                    "Семантическая ошибка: в " + context +
                            " ожидается числовой тип, найден " + type
            );
        }
    }

    private void ensureBoolean(String type, String context) {
        if (!"bool".equals(type)) {
            throw new SemanticException(
                    "Семантическая ошибка: в " + context +
                            " ожидается bool, найден " + type
            );
        }
    }

    private boolean isNumeric(String type) {
        return "int".equals(type) || "double".equals(type);
    }

    private boolean isComparable(String leftType, String rightType) {
        if (leftType.equals(rightType)) {
            return true;
        }
        return isNumeric(leftType) && isNumeric(rightType);
    }

    private static final class ExprResult {
        final String type;
        final String place;

        ExprResult(String type, String place) {
            this.type = type;
            this.place = place;
        }
    }
}