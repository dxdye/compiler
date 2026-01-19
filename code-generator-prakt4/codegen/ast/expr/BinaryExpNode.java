package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        noErrors = myExp1.namecheck(st) && noErrors;
        noErrors = myExp2.namecheck(st) && noErrors;
        return noErrors;
    }

    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}
