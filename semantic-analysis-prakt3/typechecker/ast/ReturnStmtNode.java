import java.io.*;
import java.util.*;

public class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("return");
    }
}
