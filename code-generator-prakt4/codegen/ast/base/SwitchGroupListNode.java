package ast.base;

import support.*;
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
import java.io.*;
import java.util.*;

public class SwitchGroupListNode extends ASTnode {
    public SwitchGroupListNode(Sequence S) {
        mySwitchGroups = S;
    }    
    @Override
    public String codegen(support.CodeGenerator codeGen) {
        return null;
    }



    public void decompile(PrintWriter p, int indent) {
        for (mySwitchGroups.start(); mySwitchGroups.isCurrent();) {
            try {
                ((CaseStmtNode) mySwitchGroups.getCurrent()).decompile(p, indent);
                mySwitchGroups.advance();
            } catch (NoCurrentException ex) {
                System.err.println("unexpected NoCurrentException in SwitchGroupListNode.print");
                System.exit(-1);
            }
        }

    }

    // sequence of kids (SwitchGroupNodes)
    private Sequence mySwitchGroups;
}
