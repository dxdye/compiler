package ast.base;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class ClassBodyNode extends ASTnode {
    public ClassBodyNode(DeclListNode declList) {
        myDeclList = declList;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        // Pass the symbol table to declarations
        return myDeclList.namecheck(st);
    }
    
    @Override
    public boolean typecheck() {
        return myDeclList.typecheck();
    }
    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // Generate code for all declarations (fields and methods)
        myDeclList.codegen(codeGen);
        return null;
    }

    public void decompile(PrintWriter p, int indent) {
        myDeclList.decompile(p, indent + 2);
    }

    // 1 kid
    private DeclListNode myDeclList;
}
