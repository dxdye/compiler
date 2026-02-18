package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
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
        // Check if this is an accumulator-based code generator
        if (codeGen instanceof support.AccumulatorCodeGenerator) {
            support.AccumulatorCodeGenerator accGen = (support.AccumulatorCodeGenerator) codeGen;
            
            // Generate code for left operand (result in $a0)
            myExp1.codegen(codeGen);
            
            // Push left operand result to stack
            accGen.pushAccumulator("Push left operand");
            
            // Generate code for right operand (result in $a0)
            myExp2.codegen(codeGen);
            
            // Pop left operand from stack to $t1
            accGen.popToTemp();
            
            // Perform multiplication: $a0 = $t1 * $a0
            accGen.binaryOperation("mul", "Multiply: " + support.AccumulatorCodeGenerator.TEMP_RESULT + " * " + support.AccumulatorCodeGenerator.ACCUMULATOR);
            
            // Result is in accumulator
            return support.AccumulatorCodeGenerator.ACCUMULATOR;
        } else {
            // Fallback to original register-based approach
            support.RegisterAllocator regAlloc = codeGen.getRegisterAllocator();
            
            // Generate code for left operand
            String reg1 = myExp1.codegen(codeGen);
            
            // Generate code for right operand
            String reg2 = myExp2.codegen(codeGen);
            
            // Perform multiplication
            codeGen.emit("mul " + reg1 + ", " + reg1 + ", " + reg2, "Multiply");
            
            // Free the second register
            regAlloc.freeRegister(reg2);
            
            // Result is in reg1
            return reg1;
        }
    }



    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" * ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}
