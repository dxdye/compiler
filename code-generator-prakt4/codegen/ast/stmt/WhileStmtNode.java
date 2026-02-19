package ast.stmt;

import ast.base.*;
import ast.expr.*;
import java.io.*;

public class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, StmtListNode slist) {
        myExp = exp;
        myStmtList = slist;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // Check if this is an accumulator-based code generator
        if (codeGen instanceof support.AccumulatorCodeGenerator) {
            support.AccumulatorCodeGenerator accGen = (support.AccumulatorCodeGenerator) codeGen;
            
            // Generate unique labels using the label generator
            String startLabel = accGen.getLabelGenerator().newLabel("while_start");
            String endLabel = accGen.getLabelGenerator().newLabel("while_end");
            
            // Start of loop - check condition first
            codeGen.emitLabel(startLabel);
            
            // Generate code for condition (result in $a0)
            myExp.codegen(codeGen);
            
            // Branch if false (condition == 0), exit loop - using accumulator
            codeGen.emit("beq " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", $zero, " + endLabel, "While condition false, exit loop");
            
            // Generate code for loop body
            myStmtList.codegen(codeGen);
            
            // Jump back to start
            codeGen.emit("j " + startLabel, "Repeat while loop");
            
            // End label
            codeGen.emitLabel(endLabel);
        } else {
            // Original register-based approach  
            support.LabelGenerator labelGen = codeGen.getLabelGenerator();
            
            // Generate labels for while loop
            String[] labels = labelGen.newWhileLabels();
            String startLabel = labels[0];
            String endLabel = labels[1];
            
            // Start of loop - check condition first
            codeGen.emitLabel(startLabel);
            
            // Generate code for condition
            String condReg = myExp.codegen(codeGen);
            
            // Branch if false (condition == 0), exit loop
            codeGen.emit("beq " + condReg + ", $zero, " + endLabel, "While condition false, exit loop");
            
            // Free condition register
            codeGen.getRegisterAllocator().freeRegister(condReg);
            
            // Generate code for loop body
            myStmtList.codegen(codeGen);
            
            // Jump back to start
            codeGen.emit("j " + startLabel, "Repeat while loop");
            
            // End label
            codeGen.emitLabel(endLabel);
        }
        
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("do {\n");
        myStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("} while (");
        myExp.decompile(p, indent);
        p.write(");");
    }

    // 2 kids
    private ExpNode myExp;
    private StmtListNode myStmtList;
}
