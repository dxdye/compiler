package ast.decl;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        
        String paramName = myId.getNameOfId();
        DataType dataType = DataType.fromString(myType.getTypeName());
        
        SymbolTable.SymbolInfo paramInfo = new SymbolTable.SymbolInfo(
            paramName, dataType, SymbolType.ARGUMENT,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(paramName, paramInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate parameter name: " + paramName);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Formal parameters don't have complex type checking
        return true;
    }

    public TypeNode getTypeNode() {
        return myType;
    }
    
    public IdNode getId() {
        return myId;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        myType.decompile(p, indent);
        p.write(" ");
        myId.decompile(p, indent);
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}
