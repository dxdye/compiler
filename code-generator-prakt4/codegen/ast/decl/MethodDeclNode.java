package ast.decl;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class MethodDeclNode extends DeclNode {
    public MethodDeclNode(TypeNode type, IdNode id, FormalsListNode formalList,
            MethodBodyNode body) {
        myReturnType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        String methodName = myId.getNameOfId();
        DataType returnType = DataType.fromString(myReturnType.getTypeName());
        
        // Get parameter types from formals list
        List<DataType> paramTypes = new ArrayList<>();
        if (myFormalsList != null) {
            paramTypes = myFormalsList.getParamTypes();
        }
        
        SymbolTable.SymbolInfo methodInfo = new SymbolTable.SymbolInfo(
            methodName, returnType, paramTypes,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(methodName, methodInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate method declaration: " + methodName);
            noErrors = false;
        }
        
        // Create a new scope for the method body
        SymbolTable methodScope = new SymbolTable(st);
        
        // Add formal parameters to the method scope
        if (myFormalsList != null) {
            noErrors = myFormalsList.namecheck(methodScope) && noErrors;
        }
        
        // Name check the method body
        noErrors = myBody.namecheck(methodScope) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        // Type check formals and body
        if (myFormalsList != null) {
            noErrors = myFormalsList.typecheck() && noErrors;
        }
        noErrors = myBody.typecheck() && noErrors;
        
        return noErrors;
    }
    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        String methodName = myId.getNameOfId();
        support.LabelGenerator labelGen = codeGen.getLabelGenerator();
        
        // Reset locals for new method
        codeGen.resetLocals();
        
        // Generate method label
        String methodLabel = labelGen.getMethodLabel(methodName);
        codeGen.emitLabel(methodLabel);
        codeGen.emitComment("Method: " + methodName);
        
        // Method prologue: save return address and set up stack frame
        codeGen.emit("sw $ra, 0($sp)", "Save return address");
        codeGen.emit("sw $fp, -4($sp)", "Save old frame pointer");
        codeGen.emit("move $fp, $sp", "Set up frame pointer");
        codeGen.emit("addiu $sp, $sp, -8", "Allocate space for RA and FP");
        
        // Handle parameters (they're in $a0-$a3 or on stack)
        int paramOffset = 8; // Parameters start after saved FP and RA
        if (myFormalsList != null) {
            // Parameters are handled in the formals list
            myFormalsList.codegen(codeGen);
        }
        
        // Generate code for method body
        myBody.codegen(codeGen);
        
        // Method epilogue (if no return statement at end)
        int frameSize = codeGen.getFrameSize() + 8; // Include RA and FP
        codeGen.emitLabel(methodLabel + "_exit");
        codeGen.emit("move $sp, $fp", "Restore stack pointer");
        codeGen.emit("lw $fp, -4($sp)", "Restore frame pointer");
        codeGen.emit("lw $ra, 0($sp)", "Restore return address");
        codeGen.emit("addiu $sp, $sp, " + frameSize, "Deallocate frame");
        codeGen.emit("jr $ra", "Return from method");
        codeGen.emit("");
        
        return null;
    }

    public IdNode getId() {
        return myId;
    }
    
    public TypeNode getReturnTypeNode() {
        return myReturnType;
    }
    
    public FormalsListNode getFormalsList() {
        return myFormalsList;
    }
    
    public void decompile(PrintWriter p, int indent) {
        p.print("public ");
        p.print("static ");
        myReturnType.decompile(p, indent);
        p.print(" ");
        myId.decompile(p, indent);
        p.print(" ");
        if (this.myFormalsList == null) {
            p.print("()");
        } else {
            this.myFormalsList.decompile(p, indent);
        }
        p.print(" {");
        this.myBody.decompile(p, indent);
        doIndent(p, indent);
        p.print("}");
    }

    // 4 kids
    private TypeNode myReturnType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private MethodBodyNode myBody;
}
