import java.io.*;
import java.util.*;

public class ProgramNode extends ASTnode {
    public ProgramNode(IdNode id, ClassBodyNode classBody) {
        myId = id;
        myClassBody = classBody;
    }

    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        // Add class name to symbol table
        String className = myId.getNameOfId();
        SymbolTable.SymbolInfo classInfo = new SymbolTable.SymbolInfo(
            className, myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(className, classInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate class name: " + className);
            noErrors = false;
        }
        
        // Name check the class body with the same symbol table
        noErrors = myClassBody.namecheck(st) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        // Type check the class body
        return myClassBody.typecheck();
    }

    public void decompile(PrintWriter p, int indent) {
        System.out.println("ProgramNode write");
        p.print("public class ");
        myId.decompile(p, 0);
        p.println(" {");
        myClassBody.decompile(p, 0);
        p.println("}");
    }

    // 2 kids
    private IdNode myId;
    private ClassBodyNode myClassBody;
}
