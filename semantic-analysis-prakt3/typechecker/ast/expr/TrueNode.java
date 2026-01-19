package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int colNum) {
        myLineNum = lineNum;
        myColNum = colNum;
        myType = DataType.BOOLEAN;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        // Literals don't need name checking
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Type is set in constructor
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("true");
    }

    @SuppressWarnings("unused")
    private int myLineNum;
    @SuppressWarnings("unused")
    private int myColNum;
}
