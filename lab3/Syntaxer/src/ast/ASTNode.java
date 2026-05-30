package ast;

public abstract class ASTNode {

    public abstract String toTree(String indent);

    @Override
    public String toString() {
        return toTree("");
    }
}