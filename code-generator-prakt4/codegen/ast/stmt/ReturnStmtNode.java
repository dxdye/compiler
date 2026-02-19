package ast.stmt;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode() {
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // For void return, just jump to method exit
        if (codeGen instanceof support.AccumulatorCodeGenerator) {
            codeGen.emitComment("Return statement (void)");
            // For simplicity, we use a standard exit pattern
            codeGen.emit("j main_exit", "Jump to method exit");
        }
        return null;
    }

    public boolean typecheck() {
        // Void return statement - always valid
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("return");
    }
}
