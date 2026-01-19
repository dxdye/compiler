package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        DataType expType = myExp.getType();
        if (expType == DataType.INT) {
            myType = DataType.INT;
        } else if (expType != DataType.ERROR) {
            // Type error: unary minus requires int
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("-");
        this.myExp.decompile(p, indent);
    }
}
