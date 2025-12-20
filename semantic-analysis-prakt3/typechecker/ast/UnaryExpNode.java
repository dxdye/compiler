import java.io.*;
import java.util.*;

public abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        return myExp.namecheck(st);
    }

    // one child
    protected ExpNode myExp;
}
