import java.io.*;
import java.util.*;

public class ReturnExprStmtNode extends StmtNode { // added class
    ExpNode myExp;

    public ReturnExprStmtNode(
            ExpNode exp) {
        myExp = exp;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("return ");
        this.myExp.decompile(p, indent);
        p.write(";");
    }
}
