package ast.base;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class ExpListNode extends ASTnode {
    public ExpListNode(Sequence S) {
        myExps = S;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myExps.start(); myExps.isCurrent(); myExps.advance()) {
                ExpNode exp = (ExpNode) myExps.getCurrent();
                noErrors = exp.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in ExpListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myExps.start(); myExps.isCurrent(); myExps.advance()) {
                ExpNode exp = (ExpNode) myExps.getCurrent();
                noErrors = exp.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in ExpListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        for (myExps.start(); myExps.isCurrent();) {
            try {
                ((ExpNode) myExps.getCurrent()).decompile(p, indent);
                myExps.advance();
                if (myExps.isCurrent()) {
                    p.write(", ");
                }
            } catch (NoCurrentException ex) {
                System.err.println("unexpected NoCurrentException in ExpListNode.print");
                System.exit(-1);
            }
        }
        p.write(")");
    }

    // sequence of kids (ExpNodes)
    private Sequence myExps;
}
