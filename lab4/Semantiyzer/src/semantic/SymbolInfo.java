package semantic;

public class SymbolInfo {

    private final String name;
    private final String type;
    private final String scope;
    private final boolean declared;
    private boolean initialized;

    public SymbolInfo(String name, String type, String scope, boolean initialized) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.declared = true;
        this.initialized = initialized;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public boolean isDeclared() {
        return declared;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}