package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        // Both operands must have the same type
        if (type1 == type2 && type1 != DataType.ERROR) {
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
            
            // Perform equality check: XOR and check if zero
            codeGen.emit("xor " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", " + support.AccumulatorCodeGenerator.TEMP_RESULT + ", " + support.AccumulatorCodeGenerator.ACCUMULATOR, "XOR for equality");
            codeGen.emit("sltiu " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", 1", "Set if equal (result is 0)");
            
            // Result is in accumulator
            return support.AccumulatorCodeGenerator.ACCUMULATOR;
        } else {
            // Fallback to original register-based approach
            support.RegisterAllocator regAlloc = codeGen.getRegisterAllocator();
            
            // Generate code for left operand
            String reg1 = myExp1.codegen(codeGen);
            
            // Generate code for right operand
            String reg2 = myExp2.codegen(codeGen);
            
            // Perform equality check: XOR and check if zero
            codeGen.emit("xor " + reg1 + ", " + reg1 + ", " + reg2, "XOR for equality");
            codeGen.emit("sltiu " + reg1 + ", " + reg1 + ", 1", "Set if equal (result is 0)");
            
            // Free the second register
            regAlloc.freeRegister(reg2);
            
            // Result (0 or 1) is in reg1
            return reg1;
        }
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" == ");
        this.myExp2.decompile(p, indent);
        p.write(") ");

    }
}
