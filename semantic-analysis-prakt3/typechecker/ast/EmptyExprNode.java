import java.io.*;
import java.util.*;

public class EmptyExprNode extends ExpNode {
    public EmptyExprNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        // do nothing -- represents an empty expression
    }
}
