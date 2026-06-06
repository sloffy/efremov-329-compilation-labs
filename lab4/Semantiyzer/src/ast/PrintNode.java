package ast;

public class PrintNode extends StatementNode {

    private final ExpressionNode expression;

    public PrintNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    @Override
    public String toTree(String indent) {

        return indent +
                "Print\n" +
                expression.toTree(indent + "    ");
    }
}