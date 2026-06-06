package ast;

import java.util.List;

public class BlockNode extends StatementNode {

    private final List<StatementNode> statements;

    public BlockNode(List<StatementNode> statements) {
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toTree(String indent) {

        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("Block\n");

        for (StatementNode stmt : statements) {
            sb.append(stmt.toTree(indent + "    "));
        }

        return sb.toString();
    }
}