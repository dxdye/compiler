package support;

// **********************************************************************
// SymbolTable
//
// This class provides a symbol table implementation with proper type
// and scope management for the Simple language compiler.
// **********************************************************************

import java.util.*;

public class SymbolTable {
    
    // **********************************************************************
    // SymbolInfo class - represents information about a symbol
    // **********************************************************************
    public static class SymbolInfo {
        private String name;
        private DataType dataType;
        private SymbolType symbolType;
        private List<DataType> paramTypes; // for methods
        private int lineNum;
        private int charNum;
        
        // Constructor for variables, fields, and arguments
        public SymbolInfo(String name, DataType dataType, SymbolType symbolType, 
                         int lineNum, int charNum) {
            this.name = name;
            this.dataType = dataType;
            this.symbolType = symbolType;
            this.lineNum = lineNum;
            this.charNum = charNum;
            this.paramTypes = new ArrayList<>();
        }
        
        // Constructor for methods
        public SymbolInfo(String name, DataType returnType, List<DataType> paramTypes,
                         int lineNum, int charNum) {
            this.name = name;
            this.dataType = returnType;
            this.symbolType = SymbolType.METHOD;
            this.paramTypes = paramTypes;
            this.lineNum = lineNum;
            this.charNum = charNum;
        }
        
        // Constructor for classes
        public SymbolInfo(String name, int lineNum, int charNum) {
            this.name = name;
            this.dataType = DataType.VOID; // Classes don't have a data type
            this.symbolType = SymbolType.CLASS;
            this.paramTypes = new ArrayList<>();
            this.lineNum = lineNum;
            this.charNum = charNum;
        }
        
        public String getName() {
            return name;
        }
        
        public DataType getDataType() {
            return dataType;
        }
        
        public SymbolType getSymbolType() {
            return symbolType;
        }
        
        public List<DataType> getParamTypes() {
            return paramTypes;
        }
        
        public int getLineNum() {
            return lineNum;
        }
        
        public int getCharNum() {
            return charNum;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(symbolType.getString()).append(": ");
            sb.append(name);
            
            if (symbolType == SymbolType.METHOD) {
                sb.append("(");
                for (int i = 0; i < paramTypes.size(); i++) {
                    sb.append(paramTypes.get(i).getString());
                    if (i < paramTypes.size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(") -> ").append(dataType.getString());
            } else if (symbolType != SymbolType.CLASS) {
                sb.append(": ").append(dataType.getString());
            }
            
            return sb.toString();
        }
    }
    
    // **********************************************************************
    // SymbolTable fields and methods
    // **********************************************************************
    private Map<String, SymbolInfo> symbols;
    private SymbolTable parent; // for nested scopes
    
    // Constructor for root scope
    public SymbolTable() {
        this.symbols = new HashMap<>();
        this.parent = null;
    }
    
    // Constructor for nested scope
    public SymbolTable(SymbolTable parent) {
        this.symbols = new HashMap<>();
        this.parent = parent;
    }
    
    // Add a symbol to the current scope
    // Returns true if successful, false if duplicate
    public boolean addSymbol(String name, SymbolInfo info) {
        if (symbols.containsKey(name)) {
            return false; // duplicate in current scope
        }
        symbols.put(name, info);
        return true;
    }
    
    // Look up a symbol in current scope only
    public SymbolInfo lookupLocal(String name) {
        return symbols.get(name);
    }
    
    // Look up a symbol in current scope and parent scopes
    public SymbolInfo lookup(String name) {
        SymbolInfo info = symbols.get(name);
        if (info != null) {
            return info;
        }
        if (parent != null) {
            return parent.lookup(name);
        }
        return null;
    }
    
    // Check if symbol exists in current scope
    public boolean hasLocal(String name) {
        return symbols.containsKey(name);
    }
    
    // Check if symbol exists in current or parent scopes
    public boolean has(String name) {
        return lookup(name) != null;
    }
    
    public SymbolTable getParent() {
        return parent;
    }
    
    public Map<String, SymbolInfo> getSymbols() {
        return symbols;
    }
    
    // Print the symbol table (for debugging)
    public void print() {
        System.out.println("Symbol Table:");
        for (Map.Entry<String, SymbolInfo> entry : symbols.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}
