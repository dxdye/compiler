package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, StmtListNode slist1,
            StmtListNode slist2) {
        myExp = exp;
        myThenStmtList = slist1;
        myElseStmtList = slist2;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myExp.namecheck(st) && noErrors;
        
        // Create new scope for then branch
        SymbolTable thenScope = new SymbolTable(st);
        noErrors = myThenStmtList.namecheck(thenScope) && noErrors;
        
        // Create new scope for else branch
        SymbolTable elseScope = new SymbolTable(st);
        noErrors = myElseStmtList.namecheck(elseScope) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myExp.typecheck() && noErrors;
        noErrors = myThenStmtList.typecheck() && noErrors;
        noErrors = myElseStmtList.typecheck() && noErrors;
        
        // Condition must be boolean
        if (myExp.getType() != DataType.BOOLEAN && myExp.getType() != DataType.ERROR) {
            noErrors = false;
        }
        
        return noErrors;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // Check if this is an accumulator-based code generator
        if (codeGen instanceof support.AccumulatorCodeGenerator) {
            support.AccumulatorCodeGenerator accGen = (support.AccumulatorCodeGenerator) codeGen;
            
            // Generate unique labels using the label generator directly
            String elseLabel = accGen.getLabelGenerator().newLabel("if_else");
            String endLabel = accGen.getLabelGenerator().newLabel("if_end");
            
            // Generate code for condition (result in $a0)
            myExp.codegen(codeGen);
            
            // Branch if false (condition == 0) to else part - using accumulator
            codeGen.emit("beq " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", $zero, " + elseLabel, "If condition false, go to else");
            
            // Generate code for then body
            myThenStmtList.codegen(codeGen);
            
            // Jump to end (skip else part)
            codeGen.emit("j " + endLabel, "Skip else part");
            
            // Else label
            codeGen.emitLabel(elseLabel);
            
            // Generate code for else body
            myElseStmtList.codegen(codeGen);
            
            // End label
            codeGen.emitLabel(endLabel);
        } else {
            // Original register-based approach
            support.LabelGenerator labelGen = codeGen.getLabelGenerator();
            
            // Generate labels for if-else statement
            String[] labels = labelGen.newIfElseLabels();
            String elseLabel = labels[0];
            String endLabel = labels[1];
            
            // Generate code for condition
            String condReg = myExp.codegen(codeGen);
            
            // Branch if false (condition == 0) to else part
            codeGen.emit("beq " + condReg + ", $zero, " + elseLabel, "If condition false, go to else");
            
            // Free condition register
            codeGen.getRegisterAllocator().freeRegister(condReg);
            
            // Generate code for then body
            myThenStmtList.codegen(codeGen);
            
            // Jump to end (skip else part)
            codeGen.emit("j " + endLabel, "Skip else part");
            
            // Else label
            codeGen.emitLabel(elseLabel);
            
            // Generate code for else body
            myElseStmtList.codegen(codeGen);
            
            // End label
            codeGen.emitLabel(endLabel);
        }
        
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("if ( ");
        myExp.decompile(p, indent);
        p.write(" ) {\n");
        myThenStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("} else {\n");
        myElseStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("}");
    }

    // 3 kids
    private ExpNode myExp;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
}
