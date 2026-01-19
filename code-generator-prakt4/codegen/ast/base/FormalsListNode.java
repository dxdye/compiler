package ast.base;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class FormalsListNode extends ASTnode {
    public FormalsListNode(Sequence S) {
        myFormals = S;
    }
    
    public List<DataType> getParamTypes() {
        List<DataType> paramTypes = new ArrayList<>();
        try {
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                FormalDeclNode formal = (FormalDeclNode) myFormals.getCurrent();
                TypeNode typeNode = formal.getTypeNode();
                DataType dataType = DataType.fromString(typeNode.getTypeName());
                paramTypes.add(dataType);
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.getParamTypes");
            System.exit(-1);
        }
        return paramTypes;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                FormalDeclNode formal = (FormalDeclNode) myFormals.getCurrent();
                noErrors = formal.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                FormalDeclNode formal = (FormalDeclNode) myFormals.getCurrent();
                noErrors = formal.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        if (myFormals == null) {
            System.out.println("unexpected null myFormals in FormalsListNode.print");
            p.print("()");
            return;
        }
        p.print("(");
        try {
            int i = 0;
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                ((FormalDeclNode) myFormals.getCurrent()).decompile(p, indent);
                if (myFormals.isCurrent() && i < myFormals.length() - 1) {
                    p.print(", ");
                }
                ++i;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.print");
            System.exit(-1);
        }
        p.print(")");
    }

    // sequence of kids (FormalDeclNodes)
    private Sequence myFormals;
}
