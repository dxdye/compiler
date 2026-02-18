package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int colNum, int intVal) {
        myLineNum = lineNum;
        myColNum = colNum;
        myIntVal = intVal;
        myType = DataType.INT;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        // Literals don't need name checking
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Type is set in constructor
        return true;
    }
    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // Check if this is an accumulator-based code generator
        if (codeGen instanceof support.AccumulatorCodeGenerator) {
            support.AccumulatorCodeGenerator accGen = (support.AccumulatorCodeGenerator) codeGen;
            // Load immediate value into accumulator ($a0)
            accGen.loadImmediate(myIntVal, "Load integer literal " + myIntVal);
            return support.AccumulatorCodeGenerator.ACCUMULATOR;
        } else {
            // Fallback to original register-based approach
            support.RegisterAllocator regAlloc = codeGen.getRegisterAllocator();
            String reg = regAlloc.allocateTemp();
            
            // Load immediate value into register
            codeGen.emit("li " + reg + ", " + myIntVal, "Load integer literal " + myIntVal);
            
            return reg; // Return register containing the value
        }
    }

    public void decompile(PrintWriter p, int indent) {
        p.write(Long.toString(myIntVal));
    }

    @SuppressWarnings("unused")
    private int myLineNum;
    @SuppressWarnings("unused")
    private int myColNum;
    private int myIntVal;
}
