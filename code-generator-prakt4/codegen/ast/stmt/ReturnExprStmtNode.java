package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class ReturnExprStmtNode extends StmtNode { // added class
    ExpNode myExp;

    public ReturnExprStmtNode(
            ExpNode exp) {
        myExp = exp;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        if (codeGen instanceof support.AccumulatorCodeGenerator) {
            codeGen.emitComment("Return statement with value");
            
            // Evaluate the return expression
            myExp.codegen(codeGen);
            
            // Move result to return register $v0
            codeGen.emit("move $v0, " + support.AccumulatorCodeGenerator.ACCUMULATOR, "Move return value to $v0");
            
            // For now, use a simple method-specific exit pattern
            codeGen.emit("j main_exit", "Jump to method exit");
        }
        return null;
    }

    public boolean typecheck() {
        // Check the return expression
        if (myExp != null) {
            return myExp.typecheck();
        }
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("return ");
        this.myExp.decompile(p, indent);
        p.write(";");
    }
}
