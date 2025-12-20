import java.io.*;
import java.util.*;

public class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.INT;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" * ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}
