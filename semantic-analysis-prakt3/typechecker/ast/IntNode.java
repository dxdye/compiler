import java.io.*;
import java.util.*;

public class IntNode extends TypeNode {
    public IntNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("int");
    }

    public String getTypeName() {
        return "int";
    }
}
