import java.io.*;
import java.util.*;

public class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        DataType expType = myExp.getType();
        if (expType == DataType.BOOLEAN) {
            myType = DataType.BOOLEAN;
        } else if (expType != DataType.ERROR) {
            // Type error: not requires boolean
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("!");
        this.myExp.decompile(p, indent);
    }
}
