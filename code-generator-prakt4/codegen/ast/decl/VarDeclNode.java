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
    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // Local variables are allocated on the stack
        // This is handled during method code generation
        // We just allocate space here
        String varName = myId.getNameOfId();
        int offset = codeGen.allocateLocal(varName, 4); // 4 bytes for all types
        
        codeGen.emitComment("Local variable " + varName + " at offset " + offset);
        
        return null;
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
