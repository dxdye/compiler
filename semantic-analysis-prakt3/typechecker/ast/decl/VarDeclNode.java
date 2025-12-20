package ast.decl;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        
        String varName = myId.getNameOfId();
        DataType dataType = DataType.fromString(myType.getTypeName());
        
        SymbolTable.SymbolInfo varInfo = new SymbolTable.SymbolInfo(
            varName, dataType, SymbolType.VARIABLE,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(varName, varInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate variable declaration: " + varName);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Variables don't have complex type checking beyond declaration
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        myType.decompile(p, indent);
        p.print(" ");
        myId.decompile(p, indent);
        p.write("; ");
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
