import java.io.*;
import java.util.*;

public class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int colNum, int intVal) {
        myLineNum = lineNum;
        myColNum = colNum;
        myIntVal = intVal;
        myType = DataType.INT;
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
        p.write(Long.toString(myIntVal));
    }

    @SuppressWarnings("unused")
    private int myLineNum;
    @SuppressWarnings("unused")
    private int myColNum;
    private int myIntVal;
}
