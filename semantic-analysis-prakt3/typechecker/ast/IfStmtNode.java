import java.io.*;
import java.util.*;

public class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, StmtListNode slist) {
        myExp = exp;
        myStmtList = slist;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myExp.namecheck(st) && noErrors;
        
        // Create new scope for if body
        SymbolTable ifScope = new SymbolTable(st);
        noErrors = myStmtList.namecheck(ifScope) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myExp.typecheck() && noErrors;
        noErrors = myStmtList.typecheck() && noErrors;
        
        // Condition must be boolean
        if (myExp.getType() != DataType.BOOLEAN && myExp.getType() != DataType.ERROR) {
            noErrors = false;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("if ( ");
        myExp.decompile(p, indent);
        p.write(" ) {\n");
        myStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("}");
    }

    // 2 kids
    private ExpNode myExp;
    private StmtListNode myStmtList;
}
