package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        DataType expType = myExp.getType();
        if (expType == DataType.INT) {
            myType = DataType.INT;
        } else if (expType != DataType.ERROR) {
            // Type error: unary minus requires int
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
            
            // Generate code for operand (result in $a0)
            myExp.codegen(codeGen);
            
            // Negate the accumulator using unaryOperation (which handles neg properly)
            accGen.unaryOperation("neg", "Negate accumulator");
            
            // Result is in accumulator
            return support.AccumulatorCodeGenerator.ACCUMULATOR;
        } else {
            // Fallback to original register-based approach
            // Generate code for operand
            String reg = myExp.codegen(codeGen);
            
            // Negate the register using sub from $zero (no pseudoinstructions)
            codeGen.emit("sub " + reg + ", $zero, " + reg, "Negate");
            
            // Result is in same register
            return reg;
        }
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("-");
        this.myExp.decompile(p, indent);
    }
}
