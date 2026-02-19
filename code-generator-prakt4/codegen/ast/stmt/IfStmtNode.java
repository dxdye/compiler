package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, StmtListNode slist) {
        myExp = exp;
        myStmtList = slist;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myExp.namecheck(st) && noErrors;
        
        // Create new scope for if body
        SymbolTable ifScope = new SymbolTable(st);
        noErrors = myStmtList.namecheck(ifScope) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myExp.typecheck() && noErrors;
        noErrors = myStmtList.typecheck() && noErrors;
        
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
            
            // Generate unique label for end of if using the label generator
            String endLabel = accGen.getLabelGenerator().newLabel("if_end");
            
            // Generate code for condition (result in $a0)
            myExp.codegen(codeGen);
            
            // Branch if false (condition == 0) - accumulator version
            codeGen.emit("beq " + support.AccumulatorCodeGenerator.ACCUMULATOR + ", $zero, " + endLabel, "If condition false, skip body");
            
            // Generate code for if body
            myStmtList.codegen(codeGen);
            
            // End label
            codeGen.emitLabel(endLabel);
        } else {
            // Original register-based approach
            support.LabelGenerator labelGen = codeGen.getLabelGenerator();
            
            // Generate labels for if statement
            String[] labels = labelGen.newIfLabels();
            String endLabel = labels[1];
            
            // Generate code for condition
            String condReg = myExp.codegen(codeGen);
            
            // Branch if false (condition == 0)
            codeGen.emit("beq " + condReg + ", $zero, " + endLabel, "If condition false, skip body");
            
            // Free condition register
            codeGen.getRegisterAllocator().freeRegister(condReg);
            
            // Generate code for if body
            myStmtList.codegen(codeGen);
            
            // End label
            codeGen.emitLabel(endLabel);
        }
        
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("if ( ");
        myExp.decompile(p, indent);
        p.write(" ) {\n");
        myStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("}");
    }

    // 2 kids
    private ExpNode myExp;
    private StmtListNode myStmtList;
}
