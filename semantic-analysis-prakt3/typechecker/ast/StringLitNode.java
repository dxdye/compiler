import java.io.*;
import java.util.*;

public class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int colNum, String strVal) {
        myLineNum = lineNum;
        myColNum = colNum;
        myStrVal = strVal;
        myType = DataType.STRING;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        // Literals don't need name checking
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Type is set in constructor
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("\"");
        p.write(myStrVal);
        p.write("\"");
    }

    @SuppressWarnings("unused")
    private int myLineNum;
    @SuppressWarnings("unused")
    private int myColNum;
    private String myStrVal;
}
