import java.io.*;
import java.util.*;

public class AssignStmtNode extends StmtNode {
    public AssignStmtNode(IdNode id, ExpNode exp) {
        myId = id;
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myId.namecheck(st) && noErrors;
        noErrors = myExp.namecheck(st) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myId.typecheck() && noErrors;
        noErrors = myExp.typecheck() && noErrors;
        
        // Check type compatibility
        DataType idType = myId.getType();
        DataType expType = myExp.getType();
        
        if (idType != expType && idType != DataType.ERROR && expType != DataType.ERROR) {
            // Type mismatch in assignment
            noErrors = false;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        myId.decompile(p, indent);
        p.write(" = ");
        myExp.decompile(p, indent);
        p.write(";");
    }

    // 2 kids
    private IdNode myId;
    private ExpNode myExp;
}
