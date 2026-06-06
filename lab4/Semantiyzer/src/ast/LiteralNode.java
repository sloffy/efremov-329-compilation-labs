package ast;

public class LiteralNode extends ExpressionNode {

    private final String value;

    public LiteralNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toTree(String indent) {
        return indent + "Literal: " + value + "\n";
    }
}