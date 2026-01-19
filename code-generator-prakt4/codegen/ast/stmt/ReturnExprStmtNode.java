package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class ReturnExprStmtNode extends StmtNode { // added class
    ExpNode myExp;

    public ReturnExprStmtNode(
            ExpNode exp) {
        myExp = exp;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("return ");
        this.myExp.decompile(p, indent);
        p.write(";");
    }
}
