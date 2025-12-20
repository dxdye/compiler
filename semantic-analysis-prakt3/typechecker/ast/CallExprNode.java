import java.io.*;
import java.util.*;

public class CallExprNode extends ExpNode {
    public CallExprNode(IdNode id, ExpListNode elist) {
        myId = id;
        myExpList = elist;
    }

    public CallExprNode(IdNode id) {
        myId = id;
        myExpList = new ExpListNode(new Sequence());
    }

    public void decompile(PrintWriter p, int indent) {
        myId.decompile(p, indent);
        myExpList.decompile(p, indent);
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;
}
