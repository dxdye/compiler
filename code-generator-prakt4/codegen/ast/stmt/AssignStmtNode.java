package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class AssignStmtNode extends StmtNode {
    public AssignStmtNode(IdNode id, ExpNode exp) {
        myId = id;
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myId.namecheck(st) && noErrors;
        noErrors = myExp.namecheck(st) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myId.typecheck() && noErrors;
        noErrors = myExp.typecheck() && noErrors;
        
        // Check type compatibility
        DataType idType = myId.getType();
        DataType expType = myExp.getType();
        
        if (idType != expType && idType != DataType.ERROR && expType != DataType.ERROR) {
            // Type mismatch in assignment
            noErrors = false;
        }
        
        return noErrors;
    }
    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        codeGen.emitComment("Assignment: " + myId.getNameOfId());
        
        // Generate code for the expression (result in a register)
        String expReg = myExp.codegen(codeGen);
        
        // Get the location of the variable
        String varName = myId.getNameOfId();
        Integer localOffset = codeGen.getLocalOffset(varName);
        
        if (localOffset != null) {
            // Local variable - store on stack
            codeGen.emit("sw " + expReg + ", " + localOffset + "($fp)", 
                        "Store to local " + varName);
        } else {
            // Static field - store in data segment
            String label = codeGen.getStaticVarLabel(varName);
            codeGen.emit("sw " + expReg + ", " + label, 
                        "Store to static " + varName);
        }
        
        // Free the expression register
        codeGen.getRegisterAllocator().freeRegister(expReg);
        
        return null;
    }

    public void decompile(PrintWriter p, int indent) {
        myId.decompile(p, indent);
        p.write(" = ");
        myExp.decompile(p, indent);
        p.write(";");
    }

    // 2 kids
    private IdNode myId;
    private ExpNode myExp;
}
