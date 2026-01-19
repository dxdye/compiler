package ast.expr;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class IdNode extends ExpNode {

    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public String getNameOfId() {
        return myStrVal;
    }
    
    public int getLineNum() {
        return myLineNum;
    }
    
    public int getCharNum() {
        return myCharNum;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        // Look up the identifier in the symbol table
        SymbolTable.SymbolInfo info = st.lookup(myStrVal);
        if (info == null) {
            Errors.fatal(myLineNum, myCharNum, "Undeclared identifier: " + myStrVal);
            hasErrors = true;
            myType = DataType.ERROR;
            return false;
        }
        // Set the type based on the symbol info
        myType = info.getDataType();
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Type is already determined during namecheck
        return !hasErrors;
    }
    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        support.RegisterAllocator regAlloc = codeGen.getRegisterAllocator();
        String reg = regAlloc.allocateTemp();
        
        // Check if it's a local variable or static field
        Integer localOffset = codeGen.getLocalOffset(myStrVal);
        
        if (localOffset != null) {
            // Local variable - load from stack
            codeGen.emit("lw " + reg + ", " + localOffset + "($fp)", 
                        "Load local " + myStrVal);
        } else {
            // Static field - load from data segment
            String label = codeGen.getStaticVarLabel(myStrVal);
            codeGen.emit("lw " + reg + ", " + label, 
                        "Load static " + myStrVal);
        }
        
        return reg; // Return register containing the value
    }

    public void decompile(PrintWriter p, int indent) {
        p.print(myStrVal);
        System.out.println(myCharNum + "  " + myLineNum);
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}
