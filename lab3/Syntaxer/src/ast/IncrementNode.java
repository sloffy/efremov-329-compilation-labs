package ast;

public class IncrementNode extends StatementNode {

    private final String identifier;

    public IncrementNode(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toTree(String indent) {

        return indent +
                "Increment\n" +
                indent +
                "└── variable: " +
                identifier +
                "\n";
    }
}