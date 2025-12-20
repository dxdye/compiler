import java.io.*;
import java.util.*;

public class BooleanNode extends TypeNode {
    public BooleanNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("boolean");
    }

    public String getTypeName() {
        return "boolean";
    }
}
