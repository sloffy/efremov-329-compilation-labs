package ast;

public class BinaryExpressionNode extends ExpressionNode {

    private final String operator;
    private final ExpressionNode left;
    private final ExpressionNode right;

    public BinaryExpressionNode(String operator,
                                ExpressionNode left,
                                ExpressionNode right) {

        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toTree(String indent) {

        StringBuilder sb = new StringBuilder();

        sb.append(indent)
                .append("BinaryExpression\n");

        sb.append(indent)
                .append("├── operator: ")
                .append(operator)
                .append("\n");

        sb.append(indent)
                .append("├── left\n");

        sb.append(left.toTree(
                indent + "│   "
        ));

        sb.append(indent)
                .append("└── right\n");

        sb.append(right.toTree(
                indent + "    "
        ));

        return sb.toString();
    }
}