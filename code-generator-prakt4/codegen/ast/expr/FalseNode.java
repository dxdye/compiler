package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int colNum) {
        myLineNum = lineNum;
        myColNum = colNum;
        myType = DataType.BOOLEAN;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        // Literals don't need name checking
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Type is set in constructor
        return true;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        support.RegisterAllocator regAlloc = codeGen.getRegisterAllocator();
        String reg = regAlloc.allocateTemp();
        
        // Load false (0) into register
        codeGen.emit("li " + reg + ", 0", "Load boolean false");
        
        return reg;
    }



    public void decompile(PrintWriter p, int indent) {
        p.write("false");
    }

    @SuppressWarnings("unused")
    private int myLineNum;
    @SuppressWarnings("unused")
    private int myColNum;
}
