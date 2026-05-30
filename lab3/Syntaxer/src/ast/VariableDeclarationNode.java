package ast;

public class VariableDeclarationNode extends StatementNode {

    private final String type;
    private final String name;
    private final ExpressionNode value;

    public VariableDeclarationNode(String type,
                                   String name,
                                   ExpressionNode value) {

        this.type = type;
        this.name = name;
        this.value = value;
    }

    @Override
    public String toTree(String indent) {

        StringBuilder sb = new StringBuilder();

        sb.append(indent)
                .append("VariableDeclaration\n");

        sb.append(indent)
                .append("├── type: ")
                .append(type)
                .append("\n");

        sb.append(indent)
                .append("├── name: ")
                .append(name)
                .append("\n");

        sb.append(indent)
                .append("└── value\n");

        sb.append(value.toTree(
                indent + "    "
        ));

        return sb.toString();
    }
}