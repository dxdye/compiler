import java.io.*;
import java.util.*;

public class StmtListNode extends ASTnode {
    public StmtListNode(Sequence S) {
        myStmts = S;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myStmts.start(); myStmts.isCurrent(); myStmts.advance()) {
                StmtNode stmt = (StmtNode) myStmts.getCurrent();
                noErrors = stmt.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in StmtListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myStmts.start(); myStmts.isCurrent(); myStmts.advance()) {
                StmtNode stmt = (StmtNode) myStmts.getCurrent();
                noErrors = stmt.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in StmtListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        for (myStmts.start(); myStmts.isCurrent();) {
            try {
                doIndent(p, indent);
                ((StmtNode) myStmts.getCurrent()).decompile(p, indent);
                myStmts.advance();
                p.write("\n");
            } catch (NoCurrentException ex) {
                System.err.println("unexpected NoCurrentException in StmtListNode.print");
                System.exit(-1);
            }
        }
    }

    // sequence of kids (StmtNodes)
    private Sequence myStmts;
}
