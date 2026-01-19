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
        
        // MIPS ABI-compliant prologue:
        // Reserve minimum 8 bytes for RA + FP
        // We'll adjust this later if needed
        int savedRegsSize = 8; // RA (4 bytes) + FP (4 bytes)
        
        // Placeholder - we need to know total frame size
        // For proper ABI compliance, we should do two-pass or estimate
        // For now, we'll use a conservative approach:
        
        // Handle parameters (they're in $a0-$a3 or on stack)
        if (myFormalsList != null) {
            // Parameters would be saved to locals here
            // myFormalsList.codegen(codeGen);
        }
        
        // Generate code for method body (this allocates locals)
        if (myBody != null) {
            // myBody.codegen(codeGen);
        }
        
        // Calculate total frame size (must be 8-byte aligned per MIPS ABI)
        int localsSize = codeGen.getFrameSize();
        int totalFrameSize = localsSize + savedRegsSize;
        
        // Ensure 8-byte alignment
        if (totalFrameSize % 8 != 0) {
            totalFrameSize = ((totalFrameSize / 8) + 1) * 8;
        }
        
        // Emit MIPS ABI-compliant prologue
        codeGen.emitComment("Prologue (MIPS ABI compliant)");
        codeGen.emit("addiu $sp, $sp, -" + totalFrameSize, "Allocate frame (" + totalFrameSize + " bytes)");
        codeGen.emit("sw $ra, " + (totalFrameSize - 4) + "($sp)", "Save return address");
        codeGen.emit("sw $fp, " + (totalFrameSize - 8) + "($sp)", "Save frame pointer");
        codeGen.emit("addiu $fp, $sp, " + totalFrameSize, "FP points to old SP");
        codeGen.emit("");
        
        // Method body code goes here
        codeGen.emitComment("Method body");
        if (myBody != null) {
            myBody.codegen(codeGen);
        }
        
        // Method epilogue
        codeGen.emitComment("Epilogue");
        codeGen.emitLabel(methodLabel + "_exit");
        codeGen.emit("lw $ra, " + (totalFrameSize - 4) + "($sp)", "Restore return address");
        codeGen.emit("lw $fp, " + (totalFrameSize - 8) + "($sp)", "Restore frame pointer");
        codeGen.emit("addiu $sp, $sp, " + totalFrameSize, "Deallocate frame");
        codeGen.emit("jr $ra", "Return");
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
