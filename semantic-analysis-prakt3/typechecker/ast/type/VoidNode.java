package ast.type;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public String getTypeName() {
        return "void";
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("void");
    }
}
