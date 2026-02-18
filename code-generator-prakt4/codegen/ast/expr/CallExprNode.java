package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class CallExprNode extends ExpNode {
    public CallExprNode(IdNode id, ExpListNode elist) {
        myId = id;
        myExpList = elist;
    }

    public CallExprNode(IdNode id) {
        myId = id;
        myExpList = new ExpListNode(new Sequence());
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        if (codeGen instanceof support.AccumulatorCodeGenerator) {
            support.AccumulatorCodeGenerator accGen = (support.AccumulatorCodeGenerator) codeGen;
            String methodName = myId.getNameOfId();
            
            // Get number of arguments
            int numArgs = myExpList.getNumArgs();
            Sequence argSequence = myExpList.getSequence();
            
            // Save caller-saved registers if needed
            codeGen.emitComment("Prepare method call to " + methodName);
            
            // Evaluate all arguments and store temporarily on stack
            try {
                for (argSequence.start(); argSequence.isCurrent(); argSequence.advance()) {
                    ExpNode arg = (ExpNode) argSequence.getCurrent();
                    arg.codegen(codeGen);
                    
                    // Push argument result to stack for temporary storage
                    codeGen.emit("addi $sp, $sp, -4", "Save argument");
                    codeGen.emit("sw " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", 0($sp)", "Store argument");
                }
            } catch (support.NoCurrentException ex) {
                System.err.println("unexpected NoCurrentException in CallExprNode.codegen");
                System.exit(-1);
            }
            
            // Now pop arguments and place in correct registers/stack positions
            // Pop in reverse order to get correct argument order
            for (int i = numArgs - 1; i >= 0; i--) {
                codeGen.emit("lw $t0, 0($sp)", "Load argument " + i);
                codeGen.emit("addi $sp, $sp, 4", "Deallocate temp space");
                
                if (i < 4) {
                    // Place in argument register $a0-$a3
                    codeGen.emit("move $a" + i + ", $t0", "Argument " + i + " to $a" + i);
                } else {
                    // Place on stack (beyond $a0-$a3)
                    codeGen.emit("addi $sp, $sp, -4", "Allocate space for arg " + i);
                    codeGen.emit("sw $t0, 0($sp)", "Push argument " + i + " to stack");
                }
            }
            
            // Generate method call
            String methodLabel = codeGen.getLabelGenerator().getMethodLabel(methodName);
            codeGen.emit("jal " + methodLabel, "Call method " + methodName);
            
            // Clean up stack arguments (if any)
            if (numArgs > 4) {
                int stackArgs = numArgs - 4;
                codeGen.emit("addi $sp, $sp, " + (stackArgs * 4), "Clean up " + stackArgs + " stack arguments");
            }
            
            // Result is in $v0, move to accumulator for consistency
            codeGen.emit("move " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", $v0", "Move return value to accumulator");
            
            return support.AccumulatorCodeGenerator.ACCUMULATOR;
        } else {
            // Fallback to basic implementation
            return null;
        }
    }

    public boolean namecheck(SymbolTable st) {
        boolean noErrors = true;
        
        // For method calls, we don't namecheck the method name like a variable
        // The method name will be resolved during code generation
        // We only namecheck the arguments
        if (myExpList != null) {
            noErrors = myExpList.namecheck(st) && noErrors;
        }
        
        return noErrors;
    }
    
    public boolean typecheck() {
        boolean noErrors = true;
        
        // Check if method exists in global symbol table
        String methodName = myId.getNameOfId();
        
        // For now, we'll do basic checking - method should exist in global scope
        // In a full implementation, we'd check method signatures, parameter types, etc.
        
        // Check arguments
        if (myExpList != null) {
            noErrors = myExpList.typecheck() && noErrors;
        }
        
        // For now, we assume all static methods are valid
        // TODO: Add proper method lookup in symbol table
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        myId.decompile(p, indent);
        myExpList.decompile(p, indent);
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;
}
