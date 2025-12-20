import java.io.*;
import java.util.*;

public class MethodBodyNode extends StmtNode {
    public MethodBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        // Name check local variable declarations
        noErrors = myDeclList.namecheck(st) && noErrors;
        // Name check statements
        noErrors = myStmtList.namecheck(st) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        // Type check declarations and statements
        noErrors = myDeclList.typecheck() && noErrors;
        noErrors = myStmtList.typecheck() && noErrors;
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("\n");
        myDeclList.decompile(p, indent + 2);
        p.write("\n");
        myStmtList.decompile(p, indent + 2);
    }

    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}
