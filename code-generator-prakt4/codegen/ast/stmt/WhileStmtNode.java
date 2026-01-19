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
        support.LabelGenerator labelGen = codeGen.getLabelGenerator();
        
        // Generate labels for do-while loop
        String[] labels = labelGen.newDoWhileLabels();
        String startLabel = labels[0];
        String condLabel = labels[1];
        String endLabel = labels[2];
        
        // Start of loop body
        codeGen.emitLabel(startLabel);
        
        // Generate code for loop body
        myStmtList.codegen(codeGen);
        
        // Condition label
        codeGen.emitLabel(condLabel);
        
        // Generate code for condition
        String condReg = myExp.codegen(codeGen);
        
        // Branch if true (condition != 0), loop back
        codeGen.emit("bne " + condReg + ", $zero, " + startLabel, "If condition true, repeat");
        
        // Free condition register
        codeGen.getRegisterAllocator().freeRegister(condReg);
        
        // End label
        codeGen.emitLabel(endLabel);
        
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
