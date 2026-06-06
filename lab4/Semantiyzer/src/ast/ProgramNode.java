package ast;

import java.util.List;

public class ProgramNode extends ASTNode {

    private final String namespaceName;
    private final String className;
    private final List<StatementNode> statements;

    public ProgramNode(String namespaceName,
                       String className,
                       List<StatementNode> statements) {

        this.namespaceName = namespaceName;
        this.className = className;
        this.statements = statements;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public String getClassName() {
        return className;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toTree(String indent) {

        StringBuilder sb = new StringBuilder();

        sb.append("Program\n");
        sb.append("├── namespace: ").append(namespaceName).append("\n");
        sb.append("├── class: ").append(className).append("\n");
        sb.append("└── MainMethod\n");
        sb.append("    └── body\n");

        for (StatementNode stmt : statements) {
            sb.append(stmt.toTree("        "));
        }

        return sb.toString();
    }
}