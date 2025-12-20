import java.io.*;
import java.util.*;

public abstract class ASTnode {
    protected SymbolTable symbolTable; // Symbol table for this node's scope
    protected DataType myType = DataType.VOID; // Type of this node (for expressions)
    protected boolean hasErrors = false; // Track if errors occurred
    
    // Set the symbol table for this node
    public void setSymbolTable(SymbolTable st) {
        this.symbolTable = st;
    }
    
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
    
    public DataType getType() {
        return myType;
    }
    
    public void setType(DataType type) {
        this.myType = type;
    }
    
    // Name checking: build symbol table and check for name errors
    // Returns true if no errors, false otherwise
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        return true; // default: no errors
    }
    
    // Type checking: verify type correctness
    // Returns true if no errors, false otherwise
    public boolean typecheck() {
        return true; // default: no errors
    }
    
    // Decompile operation (existing method)
    abstract public void decompile(PrintWriter p, int indent);

    // Helper method for indenting
    protected void doIndent(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++)
            p.print(" ");
    }
}
