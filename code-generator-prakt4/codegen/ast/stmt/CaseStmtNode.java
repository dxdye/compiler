package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class CaseStmtNode extends StmtNode {
    private ExpNode myCaseExpr;
    private StmtListNode myStmtList;

    public CaseStmtNode(ExpNode caseValue, StmtListNode stmtList) {
        myCaseExpr = caseValue;
        myStmtList = stmtList;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        if (myCaseExpr == null) {
            doIndent(p, indent);
            p.write("default");
            p.write(",\n");
            myStmtList.decompile(p, indent + 2);
            doIndent(p, indent + 2);
            p.println();

        } else {
            doIndent(p, indent);
            p.write("case ");
            myCaseExpr.decompile(p, indent);
            p.write(",\n");
            myStmtList.decompile(p, indent + 2);
            doIndent(p, indent + 2);
            p.println();

        }

    }
}
