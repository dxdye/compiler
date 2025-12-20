import java.io.*;
import java.util.*;

public class SwitchStmtNode extends StmtNode {
    private ExpNode myExp;
    private SwitchGroupListNode myCaseList;

    public SwitchStmtNode(ExpNode exp, SwitchGroupListNode caseList) {
        myExp = exp;
        myCaseList = caseList;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("switch (");
        myExp.decompile(p, indent);
        p.write(") {\n");
        myCaseList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("}");
    }
}
