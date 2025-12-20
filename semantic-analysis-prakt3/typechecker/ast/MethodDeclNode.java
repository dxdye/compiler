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
