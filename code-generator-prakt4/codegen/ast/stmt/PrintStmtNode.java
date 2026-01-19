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
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        // Generate code for the expression
        String reg = myExp.codegen(codeGen);
        
        DataType expType = myExp.getType();
        
        if (expType == DataType.INT) {
            // Print integer using syscall 1
            codeGen.emit("move $a0, " + reg, "Prepare for print_int");
            codeGen.emit("li $v0, 1", "Syscall for print_int");
            codeGen.emit("syscall", "Print integer");
        } else if (expType == DataType.BOOLEAN) {
            // Print boolean as 0 or 1
            codeGen.emit("move $a0, " + reg, "Prepare for print_int");
            codeGen.emit("li $v0, 1", "Syscall for print_int");
            codeGen.emit("syscall", "Print boolean");
        } else if (expType == DataType.STRING) {
            // Print string using syscall 4
            codeGen.emit("move $a0, " + reg, "Prepare for print_string");
            codeGen.emit("li $v0, 4", "Syscall for print_string");
            codeGen.emit("syscall", "Print string");
        }
        
        // Print newline
        codeGen.emit("li $a0, 10", "Newline character");
        codeGen.emit("li $v0, 11", "Syscall for print_char");
        codeGen.emit("syscall", "Print newline");
        
        // Free the register
        codeGen.getRegisterAllocator().freeRegister(reg);
        
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("System.out.println(");
        myExp.decompile(p, indent);
        p.write(");");
    }

    // 1 kid
    private ExpNode myExp;
}
