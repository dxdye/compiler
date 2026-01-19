package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        DataType expType = myExp.getType();
        if (expType == DataType.BOOLEAN) {
            myType = DataType.BOOLEAN;
        } else if (expType != DataType.ERROR) {
            // Type error: not requires boolean
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        support.RegisterAllocator regAlloc = codeGen.getRegisterAllocator();
        
        // Generate code for operand
        String reg = myExp.codegen(codeGen);
        
        // Negate boolean: XOR with 1
        codeGen.emit("xori " + reg + ", " + reg + ", 1", "Boolean NOT");
        
        return reg;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("!");
        this.myExp.decompile(p, indent);
    }
}
