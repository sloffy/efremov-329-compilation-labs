package ast;

public class AssignmentNode extends StatementNode {

    private final String identifier;
    private final ExpressionNode expression;

    public AssignmentNode(String identifier,
                          ExpressionNode expression) {

        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toTree(String indent) {

        StringBuilder sb = new StringBuilder();

        sb.append(indent)
                .append("Assignment\n");

        sb.append(indent)
                .append("├── variable: ")
                .append(identifier)
                .append("\n");

        sb.append(indent)
                .append("└── value\n");

        sb.append(expression.toTree(
                indent + "    "
        ));

        return sb.toString();
    }
}