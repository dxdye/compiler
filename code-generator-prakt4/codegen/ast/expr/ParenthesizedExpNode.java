package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class ParenthesizedExpNode extends ExpNode {
    public ParenthesizedExpNode(ExpNode exp) {
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
        myType = myExp.getType();
        return noErrors;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // Simply delegate to the wrapped expression
        return myExp.codegen(codeGen);
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        myExp.decompile(p, indent);
        p.write(")");
    }

    // 1 kid
    private ExpNode myExp;
}
