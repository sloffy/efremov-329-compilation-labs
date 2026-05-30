package ast;

public class IfNode extends StatementNode {

    private final ExpressionNode condition;
    private final BlockNode thenBlock;
    private final BlockNode elseBlock;

    public IfNode(ExpressionNode condition,
                  BlockNode thenBlock,
                  BlockNode elseBlock) {

        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public String toTree(String indent) {

        StringBuilder sb = new StringBuilder();

        sb.append(indent)
                .append("If\n");

        sb.append(indent)
                .append("├── condition\n");

        sb.append(condition.toTree(
                indent + "│   "
        ));

        sb.append(indent)
                .append("├── then\n");

        sb.append(thenBlock.toTree(
                indent + "│   "
        ));

        if (elseBlock != null) {

            sb.append(indent)
                    .append("└── else\n");

            sb.append(elseBlock.toTree(
                    indent + "    "
            ));
        }

        return sb.toString();
    }
}