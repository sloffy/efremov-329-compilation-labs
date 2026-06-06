package parser;

import lexer.*;
import ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {

        consume("namespace");

        String namespaceName = identifier();

        consume("{");

        consume("class");

        String className = identifier();

        consume("{");

        consume("static");
        consume("void");

        identifier(); // Main

        consume("(");

        while (!check(")")) {
            pos++;
        }

        consume(")");

        BlockNode body = parseBlock();

        consume("}");
        consume("}");

        if (pos != tokens.size()) {
            throw new SyntaxException(
                    "Лишние токены после завершения программы. Найдено: "
                            + current().getLexeme()
            );
        }

        return new ProgramNode(
                namespaceName,
                className,
                body.getStatements()
        );
    }

    private BlockNode parseBlock() {

        consume("{");

        List<StatementNode> statements =
                new ArrayList<>();

        while (!check("}")) {

            statements.add(
                    parseStatement()
            );
        }

        consume("}");

        return new BlockNode(statements);
    }

    private StatementNode parseStatement() {

        Token token = current();

        if (isType(token)) {
            return parseVariableDeclaration();
        }

        if (token.getLexeme().equals("if")) {
            return parseIf();
        }

        if (token.getLexeme().equals("while")) {
            return parseWhile();
        }

        if (token.getLexeme().equals("Console")) {
            return parsePrint();
        }

        if (token.getType() == TokenType.IDENTIFIER) {

            if (peek().getLexeme().equals("="))
                return parseAssignment();

            if (peek().getLexeme().equals("++"))
                return parseIncrement();
        }

        throw error(
                "Неожиданный токен: "
                        + token.getLexeme()
        );
    }

    private VariableDeclarationNode parseVariableDeclaration() {

        String type = current().getLexeme();
        pos++;

        String name = identifier();

        consume("=");

        ExpressionNode expr =
                parseExpression();

        consume(";");

        return new VariableDeclarationNode(
                type,
                name,
                expr
        );
    }

    private AssignmentNode parseAssignment() {

        String name = identifier();

        consume("=");

        ExpressionNode expr =
                parseExpression();

        consume(";");

        return new AssignmentNode(
                name,
                expr
        );
    }

    private IncrementNode parseIncrement() {

        String name = identifier();

        consume("++");
        consume(";");

        return new IncrementNode(name);
    }

    private PrintNode parsePrint() {

        consume("Console");
        consume(".");
        consume("WriteLine");

        consume("(");

        ExpressionNode expr =
                parseExpression();

        consume(")");

        consume(";");

        return new PrintNode(expr);
    }

    private IfNode parseIf() {

        consume("if");
        consume("(");

        ExpressionNode condition =
                parseExpression();

        consume(")");

        BlockNode thenBlock =
                parseBlock();

        BlockNode elseBlock = null;

        if (check("else")) {

            consume("else");

            elseBlock =
                    parseBlock();
        }

        return new IfNode(
                condition,
                thenBlock,
                elseBlock
        );
    }

    private WhileNode parseWhile() {

        consume("while");
        consume("(");

        ExpressionNode condition =
                parseExpression();

        consume(")");

        BlockNode body =
                parseBlock();

        return new WhileNode(
                condition,
                body
        );
    }

    private ExpressionNode parseExpression() {
        return parseComparison();
    }

    private ExpressionNode parseComparison() {

        ExpressionNode left =
                parseAdditive();

        while (match(
                ">", "<",
                ">=", "<=",
                "==", "!=")) {

            String op =
                    previous().getLexeme();

            ExpressionNode right =
                    parseAdditive();

            left =
                    new BinaryExpressionNode(
                            op,
                            left,
                            right
                    );
        }

        return left;
    }

    private ExpressionNode parseAdditive() {

        ExpressionNode left =
                parseMultiplicative();

        while (match("+", "-")) {

            String op =
                    previous().getLexeme();

            ExpressionNode right =
                    parseMultiplicative();

            left =
                    new BinaryExpressionNode(
                            op,
                            left,
                            right
                    );
        }

        return left;
    }

    private ExpressionNode parseMultiplicative() {

        ExpressionNode left =
                parsePrimary();

        while (match("*", "/")) {

            String op =
                    previous().getLexeme();

            ExpressionNode right =
                    parsePrimary();

            left =
                    new BinaryExpressionNode(
                            op,
                            left,
                            right
                    );
        }

        return left;
    }

    private ExpressionNode parsePrimary() {

        Token token = current();

        switch (token.getType()) {

            case IDENTIFIER:
                pos++;
                return new IdentifierNode(
                        token.getLexeme()
                );

            case CONSTANT_INT:
            case CONSTANT_DOUBLE:
            case CONSTANT_STRING:
            case CONSTANT_BOOL:
                pos++;
                return new LiteralNode(
                        token.getLexeme()
                );
        }

        throw error("Ожидалось выражение");
    }

    private boolean isType(Token token) {

        return token.getLexeme().equals("int")
                || token.getLexeme().equals("double")
                || token.getLexeme().equals("string")
                || token.getLexeme().equals("bool");
    }

    private boolean match(String... values) {

        for (String value : values) {

            if (check(value)) {

                pos++;
                return true;
            }
        }

        return false;
    }

    private String identifier() {

        Token token = current();

        if (token.getType() != TokenType.IDENTIFIER) {
            throw error(
                    "Ожидался идентификатор"
            );
        }

        pos++;

        return token.getLexeme();
    }

    private void consume(String lexeme) {

        if (!check(lexeme)) {

            throw error(
                    "Ожидалось '" +
                            lexeme +
                            "', найдено '" +
                            current().getLexeme()
                            + "'"
            );
        }

        pos++;
    }

    private boolean check(String lexeme) {

        return pos < tokens.size()
                && current()
                .getLexeme()
                .equals(lexeme);
    }

    private Token current() {
        return tokens.get(pos);
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }

    private Token peek() {
        return tokens.get(pos + 1);
    }

    private SyntaxException error(String text) {

        String currentLexeme =
                pos < tokens.size()
                        ? current().getLexeme()
                        : "EOF";

        return new SyntaxException(
                text +
                        " (позиция токена: " +
                        pos +
                        ", текущий токен: " +
                        currentLexeme +
                        ")"
        );
    }
}