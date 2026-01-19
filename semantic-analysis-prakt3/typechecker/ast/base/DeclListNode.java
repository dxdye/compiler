package ast.base;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class DeclListNode extends ASTnode {
    public DeclListNode(Sequence S) {
        myDecls = S;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myDecls.start(); myDecls.isCurrent(); myDecls.advance()) {
                DeclNode decl = (DeclNode) myDecls.getCurrent();
                noErrors = decl.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in DeclListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myDecls.start(); myDecls.isCurrent(); myDecls.advance()) {
                DeclNode decl = (DeclNode) myDecls.getCurrent();
                noErrors = decl.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in DeclListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        try {
            for (myDecls.start(); myDecls.isCurrent(); myDecls.advance()) {
                doIndent(p, indent);
                DeclNode currentDecl = (DeclNode) myDecls.getCurrent();
                currentDecl.decompile(p, indent);
                p.write("\n");
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in DeclListNode.print");
            System.exit(-1);
        }
    }

    // sequence of kids (DeclNodes)
    public Sequence myDecls;
}
