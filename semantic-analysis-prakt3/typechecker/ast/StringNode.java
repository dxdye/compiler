import java.io.*;
import java.util.*;

public class StringNode extends TypeNode {
    public StringNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("String");
    }

    public String getTypeName() {
        return "String";
    }
}
