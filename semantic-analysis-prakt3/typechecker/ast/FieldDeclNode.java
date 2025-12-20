import java.io.*;
import java.util.*;

public class FieldDeclNode extends DeclNode {
    public FieldDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        
        String fieldName = myId.getNameOfId();
        DataType dataType = DataType.fromString(myType.getTypeName());
        
        SymbolTable.SymbolInfo fieldInfo = new SymbolTable.SymbolInfo(
            fieldName, dataType, SymbolType.FIELD,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(fieldName, fieldInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate field declaration: " + fieldName);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Fields don't have complex type checking beyond declaration
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("static ");
        myType.decompile(p, indent);
        p.print(" ");
        myId.decompile(p, indent);
        p.print(";");
    }

    public TypeNode getTypeNode() {
        return myType;
    }

    public IdNode getId() {
        return myId;
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}
