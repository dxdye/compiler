package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.BOOLEAN && type2 == DataType.BOOLEAN) {
            myType = DataType.BOOLEAN;
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
        support.LabelGenerator labelGen = codeGen.getLabelGenerator();
        
        // Generate labels for short-circuit evaluation
        String[] labels = labelGen.newOrLabels();
        String trueLabel = labels[0];
        String endLabel = labels[1];
        
        String resultReg = regAlloc.allocateTemp();
        
        // Generate code for left operand
        String reg1 = myExp1.codegen(codeGen);
        
        // If left is true, short-circuit to true
        codeGen.emit("bne " + reg1 + ", $zero, " + trueLabel, "OR: short-circuit if left is true");
        
        // Free first register
        regAlloc.freeRegister(reg1);
        
        // Evaluate right operand
        String reg2 = myExp2.codegen(codeGen);
        
        // Result is value of right operand
        codeGen.emit("move " + resultReg + ", " + reg2, "OR: result from right");
        regAlloc.freeRegister(reg2);
        codeGen.emit("j " + endLabel, "Jump to end");
        
        // True label
        codeGen.emitLabel(trueLabel);
        codeGen.emit("li " + resultReg + ", 1", "OR: result is true");
        
        // End label
        codeGen.emitLabel(endLabel);
        
        return resultReg;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" || ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}
