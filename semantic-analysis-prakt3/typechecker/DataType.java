// **********************************************************************
// DataType enum
// 
// Represents the data types available in the Simple language
// **********************************************************************

public enum DataType {
    INT("int"),
    BOOLEAN("boolean"),
    STRING("String"),
    VOID("void"),
    ERROR("error");

    private final String string;

    DataType(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

    // Parse a string into a DataType
    public static DataType fromString(String str) {
        if (str == null) {
            return ERROR;
        }
        switch (str.toLowerCase()) {
            case "int":
                return INT;
            case "boolean":
                return BOOLEAN;
            case "string":
                return STRING;
            case "void":
                return VOID;
            default:
                return ERROR;
        }
    }
}
