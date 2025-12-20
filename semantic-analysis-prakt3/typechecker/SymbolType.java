// **********************************************************************
// SymbolType enum
// 
// Represents the kind of symbol (class, method, field, argument, variable)
// **********************************************************************

public enum SymbolType {
    CLASS("class"),
    METHOD("method"),
    FIELD("field"),
    ARGUMENT("arg"),
    VARIABLE("var");

    private final String string;

    SymbolType(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }
}
