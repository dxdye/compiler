import java.io.*;
import java.util.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a "Simple" program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a sequence (for nodes that may have a variable number of children)
// or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         IdNode, ClassBodyNode
//     ClassBodyNode       DeclListNode
//     DeclListNode        sequence of DeclNode
//     FormalsListNode     sequence of FormalDeclNode
//     MethodBodyNode      DeclListNode, StmtListNode
//     StmtListNode        sequence of StmtNode
//     ExpListNode         sequence of ExpNode
//
//     DeclNode:
//       FieldDeclNode     TypeNode, IdNode
//       VarDeclNode       TypeNode, IdNode
//       MethodDeclNode    IdNode, FormalsListNode, MethodBodyNode
//       FormalDeclNode    TypeNode, IdNode
//
//     TypeNode:
//       IntNode             -- none --
//       BooleanNode         -- none --
//       StringNode          -- none --
//
//     StmtNode:
//       PrintStmtNode       ExpNode
//       AssignStmtNode      IdNode, ExpNode
//       IfStmtNode          ExpNode, StmtListNode
//       IfElseStmtNode      ExpNode, StmtListNode, StmtListNode
//       WhileStmtNode       ExpNode, StmtListNode
//       CallStmtNode        IdNode, ExpListNode
//       ReturnStmtNode      -- none --
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode     
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with sequences of kids, or internal
// nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode, BooleanNode, StringNode, IntLitNode,
//	  StrLitNode, TrueNode, FalseNode, IdNode, ReturnStmtNode
//
// (2) Internal nodes with (possibly empty) sequences of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//        ProgramNode,    ClassBodyNode, MethodBodyNode,
//        FieldDeclNode,  VarDeclNode,   MethodDeclNode, FormalDeclNode,
//        PrintStmtNode,  AssignStmtNode,IfStmtNode,     IfElseStmtNode,
//        WhileStmtNode,  CallStmtNode,  UnaryExpNode,   BinaryExpNode,
//        UnaryMinusNode, NotNode,       PlusNode,       MinusNode,
//        TimesNode,      DivideNode,    AndNode,        OrNode,
//        EqualsNode,     NotEqualsNode, LessNode,       GreaterNode,
//        LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode {
    protected SymbolTable symbolTable; // Symbol table for this node's scope
    protected DataType myType = DataType.VOID; // Type of this node (for expressions)
    protected boolean hasErrors = false; // Track if errors occurred
    
    // Set the symbol table for this node
    public void setSymbolTable(SymbolTable st) {
        this.symbolTable = st;
    }
    
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
    
    public DataType getType() {
        return myType;
    }
    
    public void setType(DataType type) {
        this.myType = type;
    }
    
    // Name checking: build symbol table and check for name errors
    // Returns true if no errors, false otherwise
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        return true; // default: no errors
    }
    
    // Type checking: verify type correctness
    // Returns true if no errors, false otherwise
    public boolean typecheck() {
        return true; // default: no errors
    }
    
    // Decompile operation (existing method)
    abstract public void decompile(PrintWriter p, int indent);

    // Helper method for indenting
    protected void doIndent(PrintWriter p, int indent) {
        for (int k = 0; k < indent; k++)
            p.print(" ");
    }
}


// **********************************************************************
// ProgramNode, ClassBodyNode, DeclListNode, FormalsListNode,
// MethodBodyNode, StmtListNode, ExpListNode
// **********************************************************************
class ProgramNode extends ASTnode {
    public ProgramNode(IdNode id, ClassBodyNode classBody) {
        myId = id;
        myClassBody = classBody;
    }

    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        // Add class name to symbol table
        String className = myId.getNameOfId();
        SymbolTable.SymbolInfo classInfo = new SymbolTable.SymbolInfo(
            className, myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(className, classInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate class name: " + className);
            noErrors = false;
        }
        
        // Name check the class body with the same symbol table
        noErrors = myClassBody.namecheck(st) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        // Type check the class body
        return myClassBody.typecheck();
    }

    public void decompile(PrintWriter p, int indent) {
        System.out.println("ProgramNode write");
        p.print("public class ");
        myId.decompile(p, 0);
        p.println(" {");
        myClassBody.decompile(p, 0);
        p.println("}");
    }

    // 2 kids
    private IdNode myId;
    private ClassBodyNode myClassBody;
}

class ClassBodyNode extends ASTnode {
    public ClassBodyNode(DeclListNode declList) {
        myDeclList = declList;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        // Pass the symbol table to declarations
        return myDeclList.namecheck(st);
    }
    
    @Override
    public boolean typecheck() {
        return myDeclList.typecheck();
    }

    public void decompile(PrintWriter p, int indent) {
        myDeclList.decompile(p, indent + 2);
    }

    // 1 kid
    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
    public DeclListNode(Sequence S) {
        myDecls = S;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myDecls.start(); myDecls.isCurrent(); myDecls.advance()) {
                DeclNode decl = (DeclNode) myDecls.getCurrent();
                noErrors = decl.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in DeclListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myDecls.start(); myDecls.isCurrent(); myDecls.advance()) {
                DeclNode decl = (DeclNode) myDecls.getCurrent();
                noErrors = decl.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in DeclListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        try {
            for (myDecls.start(); myDecls.isCurrent(); myDecls.advance()) {
                doIndent(p, indent);
                DeclNode currentDecl = (DeclNode) myDecls.getCurrent();
                currentDecl.decompile(p, indent);
                p.write("\n");
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in DeclListNode.print");
            System.exit(-1);
        }
    }

    // sequence of kids (DeclNodes)
    public Sequence myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(Sequence S) {
        myFormals = S;
    }
    
    public List<DataType> getParamTypes() {
        List<DataType> paramTypes = new ArrayList<>();
        try {
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                FormalDeclNode formal = (FormalDeclNode) myFormals.getCurrent();
                TypeNode typeNode = formal.getTypeNode();
                DataType dataType = DataType.fromString(typeNode.getTypeName());
                paramTypes.add(dataType);
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.getParamTypes");
            System.exit(-1);
        }
        return paramTypes;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                FormalDeclNode formal = (FormalDeclNode) myFormals.getCurrent();
                noErrors = formal.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                FormalDeclNode formal = (FormalDeclNode) myFormals.getCurrent();
                noErrors = formal.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        if (myFormals == null) {
            System.out.println("unexpected null myFormals in FormalsListNode.print");
            p.print("()");
            return;
        }
        p.print("(");
        try {
            int i = 0;
            for (myFormals.start(); myFormals.isCurrent(); myFormals.advance()) {
                ((FormalDeclNode) myFormals.getCurrent()).decompile(p, indent);
                if (myFormals.isCurrent() && i < myFormals.length() - 1) {
                    p.print(", ");
                }
                ++i;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in FormalsListNode.print");
            System.exit(-1);
        }
        p.print(")");
    }

    // sequence of kids (FormalDeclNodes)
    private Sequence myFormals;
}

class MethodBodyNode extends StmtNode {
    public MethodBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        // Name check local variable declarations
        noErrors = myDeclList.namecheck(st) && noErrors;
        // Name check statements
        noErrors = myStmtList.namecheck(st) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        // Type check declarations and statements
        noErrors = myDeclList.typecheck() && noErrors;
        noErrors = myStmtList.typecheck() && noErrors;
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("\n");
        myDeclList.decompile(p, indent + 2);
        p.write("\n");
        myStmtList.decompile(p, indent + 2);
    }

    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(Sequence S) {
        myStmts = S;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myStmts.start(); myStmts.isCurrent(); myStmts.advance()) {
                StmtNode stmt = (StmtNode) myStmts.getCurrent();
                noErrors = stmt.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in StmtListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myStmts.start(); myStmts.isCurrent(); myStmts.advance()) {
                StmtNode stmt = (StmtNode) myStmts.getCurrent();
                noErrors = stmt.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in StmtListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        for (myStmts.start(); myStmts.isCurrent();) {
            try {
                doIndent(p, indent);
                ((StmtNode) myStmts.getCurrent()).decompile(p, indent);
                myStmts.advance();
                p.write("\n");
            } catch (NoCurrentException ex) {
                System.err.println("unexpected NoCurrentException in StmtListNode.print");
                System.exit(-1);
            }
        }
    }

    // sequence of kids (StmtNodes)
    private Sequence myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(Sequence S) {
        myExps = S;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        try {
            for (myExps.start(); myExps.isCurrent(); myExps.advance()) {
                ExpNode exp = (ExpNode) myExps.getCurrent();
                noErrors = exp.namecheck(st) && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in ExpListNode.namecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        try {
            for (myExps.start(); myExps.isCurrent(); myExps.advance()) {
                ExpNode exp = (ExpNode) myExps.getCurrent();
                noErrors = exp.typecheck() && noErrors;
            }
        } catch (NoCurrentException ex) {
            System.err.println("unexpected NoCurrentException in ExpListNode.typecheck");
            System.exit(-1);
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        for (myExps.start(); myExps.isCurrent();) {
            try {
                ((ExpNode) myExps.getCurrent()).decompile(p, indent);
                myExps.advance();
                if (myExps.isCurrent()) {
                    p.write(", ");
                }
            } catch (NoCurrentException ex) {
                System.err.println("unexpected NoCurrentException in ExpListNode.print");
                System.exit(-1);
            }
        }
        p.write(")");
    }

    // sequence of kids (ExpNodes)
    private Sequence myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************
abstract class DeclNode extends ASTnode {
}

class FieldDeclNode extends DeclNode {
    public FieldDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        
        String fieldName = myId.getNameOfId();
        DataType dataType = DataType.fromString(myType.getTypeName());
        
        SymbolTable.SymbolInfo fieldInfo = new SymbolTable.SymbolInfo(
            fieldName, dataType, SymbolType.FIELD,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(fieldName, fieldInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate field declaration: " + fieldName);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Fields don't have complex type checking beyond declaration
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("static ");
        myType.decompile(p, indent);
        p.print(" ");
        myId.decompile(p, indent);
        p.print(";");
    }

    public TypeNode getTypeNode() {
        return myType;
    }

    public IdNode getId() {
        return myId;
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        
        String varName = myId.getNameOfId();
        DataType dataType = DataType.fromString(myType.getTypeName());
        
        SymbolTable.SymbolInfo varInfo = new SymbolTable.SymbolInfo(
            varName, dataType, SymbolType.VARIABLE,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(varName, varInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate variable declaration: " + varName);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Variables don't have complex type checking beyond declaration
        return true;
    }

    public void decompile(PrintWriter p, int indent) {
        myType.decompile(p, indent);
        p.print(" ");
        myId.decompile(p, indent);
        p.write("; ");
    }

    public TypeNode getTypeNode() {
        return myType;
    }

    public IdNode getId() {
        return myId;
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class MethodDeclNode extends DeclNode {
    public MethodDeclNode(TypeNode type, IdNode id, FormalsListNode formalList,
            MethodBodyNode body) {
        myReturnType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        String methodName = myId.getNameOfId();
        DataType returnType = DataType.fromString(myReturnType.getTypeName());
        
        // Get parameter types from formals list
        List<DataType> paramTypes = new ArrayList<>();
        if (myFormalsList != null) {
            paramTypes = myFormalsList.getParamTypes();
        }
        
        SymbolTable.SymbolInfo methodInfo = new SymbolTable.SymbolInfo(
            methodName, returnType, paramTypes,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(methodName, methodInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate method declaration: " + methodName);
            noErrors = false;
        }
        
        // Create a new scope for the method body
        SymbolTable methodScope = new SymbolTable(st);
        
        // Add formal parameters to the method scope
        if (myFormalsList != null) {
            noErrors = myFormalsList.namecheck(methodScope) && noErrors;
        }
        
        // Name check the method body
        noErrors = myBody.namecheck(methodScope) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        // Type check formals and body
        if (myFormalsList != null) {
            noErrors = myFormalsList.typecheck() && noErrors;
        }
        noErrors = myBody.typecheck() && noErrors;
        
        return noErrors;
    }

    public IdNode getId() {
        return myId;
    }
    
    public TypeNode getReturnTypeNode() {
        return myReturnType;
    }
    
    public FormalsListNode getFormalsList() {
        return myFormalsList;
    }
    
    public void decompile(PrintWriter p, int indent) {
        p.print("public ");
        p.print("static ");
        myReturnType.decompile(p, indent);
        p.print(" ");
        myId.decompile(p, indent);
        p.print(" ");
        if (this.myFormalsList == null) {
            p.print("()");
        } else {
            this.myFormalsList.decompile(p, indent);
        }
        p.print(" {");
        this.myBody.decompile(p, indent);
        doIndent(p, indent);
        p.print("}");
    }

    // 4 kids
    private TypeNode myReturnType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private MethodBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        
        String paramName = myId.getNameOfId();
        DataType dataType = DataType.fromString(myType.getTypeName());
        
        SymbolTable.SymbolInfo paramInfo = new SymbolTable.SymbolInfo(
            paramName, dataType, SymbolType.ARGUMENT,
            myId.getLineNum(), myId.getCharNum()
        );
        
        if (!st.addSymbol(paramName, paramInfo)) {
            Errors.fatal(myId.getLineNum(), myId.getCharNum(),
                "Duplicate parameter name: " + paramName);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Formal parameters don't have complex type checking
        return true;
    }

    public TypeNode getTypeNode() {
        return myType;
    }
    
    public IdNode getId() {
        return myId;
    }

    public void decompile(PrintWriter p, int indent) {
        myType.decompile(p, indent);
        p.write(" ");
        myId.decompile(p, indent);
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************
abstract class TypeNode extends ASTnode {
    public abstract String getTypeName();
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public String getTypeName() {
        return "void";
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("void");
    }
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("int");
    }

    public String getTypeName() {
        return "int";
    }
}

class BooleanNode extends TypeNode {
    public BooleanNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("boolean");
    }

    public String getTypeName() {
        return "boolean";
    }
}

class StringNode extends TypeNode {
    public StringNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.print("String");
    }

    public String getTypeName() {
        return "String";
    }
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
}

class PrintStmtNode extends StmtNode {
    public PrintStmtNode(ExpNode exp) {
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        return myExp.namecheck(st);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        // Print can handle INT, BOOLEAN, or STRING
        DataType expType = myExp.getType();
        if (expType != DataType.INT && expType != DataType.BOOLEAN && 
            expType != DataType.STRING && expType != DataType.ERROR) {
            // Could add error reporting here if needed
            noErrors = false;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("System.out.println(");
        myExp.decompile(p, indent);
        p.write(");");
    }

    // 1 kid
    private ExpNode myExp;
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(IdNode id, ExpNode exp) {
        myId = id;
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myId.namecheck(st) && noErrors;
        noErrors = myExp.namecheck(st) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myId.typecheck() && noErrors;
        noErrors = myExp.typecheck() && noErrors;
        
        // Check type compatibility
        DataType idType = myId.getType();
        DataType expType = myExp.getType();
        
        if (idType != expType && idType != DataType.ERROR && expType != DataType.ERROR) {
            // Type mismatch in assignment
            noErrors = false;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        myId.decompile(p, indent);
        p.write(" = ");
        myExp.decompile(p, indent);
        p.write(";");
    }

    // 2 kids
    private IdNode myId;
    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, StmtListNode slist) {
        myExp = exp;
        myStmtList = slist;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myExp.namecheck(st) && noErrors;
        
        // Create new scope for if body
        SymbolTable ifScope = new SymbolTable(st);
        noErrors = myStmtList.namecheck(ifScope) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myExp.typecheck() && noErrors;
        noErrors = myStmtList.typecheck() && noErrors;
        
        // Condition must be boolean
        if (myExp.getType() != DataType.BOOLEAN && myExp.getType() != DataType.ERROR) {
            noErrors = false;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("if ( ");
        myExp.decompile(p, indent);
        p.write(" ) {\n");
        myStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("}");
    }

    // 2 kids
    private ExpNode myExp;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, StmtListNode slist1,
            StmtListNode slist2) {
        myExp = exp;
        myThenStmtList = slist1;
        myElseStmtList = slist2;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        
        noErrors = myExp.namecheck(st) && noErrors;
        
        // Create new scope for then branch
        SymbolTable thenScope = new SymbolTable(st);
        noErrors = myThenStmtList.namecheck(thenScope) && noErrors;
        
        // Create new scope for else branch
        SymbolTable elseScope = new SymbolTable(st);
        noErrors = myElseStmtList.namecheck(elseScope) && noErrors;
        
        return noErrors;
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        
        noErrors = myExp.typecheck() && noErrors;
        noErrors = myThenStmtList.typecheck() && noErrors;
        noErrors = myElseStmtList.typecheck() && noErrors;
        
        // Condition must be boolean
        if (myExp.getType() != DataType.BOOLEAN && myExp.getType() != DataType.ERROR) {
            noErrors = false;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("if ( ");
        myExp.decompile(p, indent);
        p.write(" ) {\n");
        myThenStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("} else {\n");
        myElseStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("}");
    }

    // 3 kids
    private ExpNode myExp;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, StmtListNode slist) {
        myExp = exp;
        myStmtList = slist;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("do {\n");
        myStmtList.decompile(p, indent + 2);
        doIndent(p, indent);
        p.write("} while (");
        myExp.decompile(p, indent);
        p.write(");");
    }

    // 2 kids
    private ExpNode myExp;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(IdNode id, ExpListNode elist) {
        myId = id;
        myExpList = elist;
    }

    public CallStmtNode(IdNode id) {
        myId = id;
        myExpList = new ExpListNode(new Sequence());
    }

    public void decompile(PrintWriter p, int indent) {
        myId.decompile(p, indent);
        myExpList.decompile(p, indent);
        p.write(";");
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;
}

class SwitchStmtNode extends StmtNode {
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

class SwitchGroupListNode extends ASTnode {
    public SwitchGroupListNode(Sequence S) {
        mySwitchGroups = S;
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

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("return");
    }
}

class ReturnExprStmtNode extends StmtNode { // added class
    ExpNode myExp;

    public ReturnExprStmtNode(
            ExpNode exp) {
        myExp = exp;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("return ");
        this.myExp.decompile(p, indent);
        p.write(";");
    }
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
}

class IntLitNode extends ExpNode {
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

class StringLitNode extends ExpNode {
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

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int colNum) {
        myLineNum = lineNum;
        myColNum = colNum;
        myType = DataType.BOOLEAN;
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
        p.write("true");
    }

    @SuppressWarnings("unused")
    private int myLineNum;
    @SuppressWarnings("unused")
    private int myColNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int colNum) {
        myLineNum = lineNum;
        myColNum = colNum;
        myType = DataType.BOOLEAN;
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
        p.write("false");
    }

    @SuppressWarnings("unused")
    private int myLineNum;
    @SuppressWarnings("unused")
    private int myColNum;
}

class IdNode extends ExpNode {

    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public String getNameOfId() {
        return myStrVal;
    }
    
    public int getLineNum() {
        return myLineNum;
    }
    
    public int getCharNum() {
        return myCharNum;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        // Look up the identifier in the symbol table
        SymbolTable.SymbolInfo info = st.lookup(myStrVal);
        if (info == null) {
            Errors.fatal(myLineNum, myCharNum, "Undeclared identifier: " + myStrVal);
            hasErrors = true;
            myType = DataType.ERROR;
            return false;
        }
        // Set the type based on the symbol info
        myType = info.getDataType();
        return true;
    }
    
    @Override
    public boolean typecheck() {
        // Type is already determined during namecheck
        return !hasErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.print(myStrVal);
        System.out.println(myCharNum + "  " + myLineNum);
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        return myExp.namecheck(st);
    }

    // one child
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        boolean noErrors = true;
        noErrors = myExp1.namecheck(st) && noErrors;
        noErrors = myExp2.namecheck(st) && noErrors;
        return noErrors;
    }

    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        DataType expType = myExp.getType();
        if (expType == DataType.INT) {
            myType = DataType.INT;
        } else if (expType != DataType.ERROR) {
            // Type error: unary minus requires int
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("-");
        this.myExp.decompile(p, indent);
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        
        DataType expType = myExp.getType();
        if (expType == DataType.BOOLEAN) {
            myType = DataType.BOOLEAN;
        } else if (expType != DataType.ERROR) {
            // Type error: not requires boolean
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("!");
        this.myExp.decompile(p, indent);
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.INT;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" + ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.INT;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" - ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.INT;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" * ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.INT;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" / ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.BOOLEAN && type2 == DataType.BOOLEAN) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write(" (");
        this.myExp1.decompile(p, indent);
        p.write(" && ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.BOOLEAN && type2 == DataType.BOOLEAN) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" || ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        // Both operands must have the same type
        if (type1 == type2 && type1 != DataType.ERROR) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" == ");
        this.myExp2.decompile(p, indent);
        p.write(") ");

    }
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        // Both operands must have the same type
        if (type1 == type2 && type1 != DataType.ERROR) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" != ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" < ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" > ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" <= ");
        this.myExp2.decompile(p, indent);
        p.write(") ");
    }
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = true;
        noErrors = myExp1.typecheck() && noErrors;
        noErrors = myExp2.typecheck() && noErrors;
        
        DataType type1 = myExp1.getType();
        DataType type2 = myExp2.getType();
        
        if (type1 == DataType.INT && type2 == DataType.INT) {
            myType = DataType.BOOLEAN;
        } else if (type1 != DataType.ERROR && type2 != DataType.ERROR) {
            myType = DataType.ERROR;
            noErrors = false;
        } else {
            myType = DataType.ERROR;
        }
        
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        this.myExp1.decompile(p, indent);
        p.write(" >= ");
        this.myExp2.decompile(p, indent);
        p.write(")");
    }
}

class EmptyExprNode extends ExpNode {
    public EmptyExprNode() {
    }

    public void decompile(PrintWriter p, int indent) {
        // do nothing -- represents an empty expression
    }
}

class CallExprNode extends ExpNode {
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

class ParenthesizedExpNode extends ExpNode {
    public ParenthesizedExpNode(ExpNode exp) {
        myExp = exp;
    }
    
    @Override
    public boolean namecheck(SymbolTable st) {
        this.symbolTable = st;
        return myExp.namecheck(st);
    }
    
    @Override
    public boolean typecheck() {
        boolean noErrors = myExp.typecheck();
        myType = myExp.getType();
        return noErrors;
    }

    public void decompile(PrintWriter p, int indent) {
        p.write("(");
        myExp.decompile(p, indent);
        p.write(")");
    }

    // 1 kid
    private ExpNode myExp;
}

class CaseStmtNode extends StmtNode {
    private ExpNode myCaseExpr;
    private StmtListNode myStmtList;

    public CaseStmtNode(ExpNode caseValue, StmtListNode stmtList) {
        myCaseExpr = caseValue;
        myStmtList = stmtList;
    }

    public void decompile(PrintWriter p, int indent) {
        if (myCaseExpr == null) {
            doIndent(p, indent);
            p.write("default");
            p.write(",\n");
            myStmtList.decompile(p, indent + 2);
            doIndent(p, indent + 2);
            p.println();

        } else {
            doIndent(p, indent);
            p.write("case ");
            myCaseExpr.decompile(p, indent);
            p.write(",\n");
            myStmtList.decompile(p, indent + 2);
            doIndent(p, indent + 2);
            p.println();

        }

    }
}
