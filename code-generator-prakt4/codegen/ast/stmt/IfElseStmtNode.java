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
