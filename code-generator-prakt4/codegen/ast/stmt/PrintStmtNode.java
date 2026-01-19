package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class PrintStmtNode extends StmtNode {
    public PrintStmtNode(ExpNode exp) {
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        return myExp.namecheck(st);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        // Print can handle INT, BOOLEAN, or STRING
        DataType expType = myExp.getType();
        if (expType != DataType.INT && expType != DataType.BOOLEAN && 
            expType != DataType.STRING && expType != DataType.ERROR) {
            // Could add error reporting here if needed
            noErrors = false;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("System.out.println(");
        myExp.decompile(p, indent);
        p.write(");");
    }

    // 1 kid
    private ExpNode myExp;
}
