package ast;

public class WhileNode extends StatementNode {

    private final ExpressionNode condition;
    private final BlockNode body;

    public WhileNode(ExpressionNode condition,
                     BlockNode body) {

        this.condition = condition;
        this.body = body;
    }

    @Override
    public String toTree(String indent) {

        StringBuilder sb = new StringBuilder();

        sb.append(indent)
                .append("While\n");

        sb.append(indent)
                .append("├── condition\n");

        sb.append(condition.toTree(
                indent + "│   "
        ));

        sb.append(indent)
                .append("└── body\n");

        sb.append(body.toTree(
                indent + "    "
        ));

        return sb.toString();
    }
}