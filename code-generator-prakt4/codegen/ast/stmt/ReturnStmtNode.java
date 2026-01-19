package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("return");
    }
}
