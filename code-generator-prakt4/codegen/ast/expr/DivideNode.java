package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.INT;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
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
        
        // Generate code for left operand
        String reg1 = myExp1.codegen(codeGen);
        
        // Generate code for right operand
        String reg2 = myExp2.codegen(codeGen);
        
        // Perform division
        codeGen.emit("div " + reg1 + ", " + reg2, "Divide");
        codeGen.emit("mflo " + reg1, "Get quotient");
        
        // Free the second register
        regAlloc.freeRegister(reg2);
        
        // Result is in reg1
        return reg1;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" / ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}
