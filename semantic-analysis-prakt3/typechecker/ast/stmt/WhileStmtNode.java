package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, StmtListNode slist) {
        myExp = exp;
        myStmtList = slist;
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
