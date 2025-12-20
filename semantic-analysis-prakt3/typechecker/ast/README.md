# AST Node Classes

This directory contains all Abstract Syntax Tree (AST) node classes for the Simple language compiler.

## Organization

Each AST node is defined in its own file. All classes are in the default (unnamed) package to maintain compatibility with the rest of the codebase.

## Class Hierarchy

```
ASTnode (abstract base)
├── ProgramNode
├── ClassBodyNode
├── DeclListNode
├── FormalsListNode
├── StmtListNode
├── ExpListNode
├── SwitchGroupListNode
│
├── DeclNode (abstract)
│   ├── FieldDeclNode
│   ├── VarDeclNode
│   ├── MethodDeclNode
│   └── FormalDeclNode
│
├── TypeNode (abstract)
│   ├── VoidNode
│   ├── IntNode
│   ├── BooleanNode
│   └── StringNode
│
├── StmtNode (abstract)
│   ├── MethodBodyNode
│   ├── PrintStmtNode
│   ├── AssignStmtNode
│   ├── IfStmtNode
│   ├── IfElseStmtNode
│   ├── WhileStmtNode
│   ├── CallStmtNode
│   ├── SwitchStmtNode
│   ├── ReturnStmtNode
│   ├── ReturnExprStmtNode
│   └── CaseStmtNode
│
└── ExpNode (abstract)
    ├── IntLitNode
    ├── StringLitNode
    ├── TrueNode
    ├── FalseNode
    ├── IdNode
    ├── EmptyExprNode
    ├── CallExprNode
    ├── ParenthesizedExpNode
    │
    ├── UnaryExpNode (abstract)
    │   ├── UnaryMinusNode
    │   └── NotNode
    │
    └── BinaryExpNode (abstract)
        ├── PlusNode
        ├── MinusNode
        ├── TimesNode
        ├── DivideNode
        ├── AndNode
        ├── OrNode
        ├── EqualsNode
        ├── NotEqualsNode
        ├── LessNode
        ├── GreaterNode
        ├── LessEqNode
        └── GreaterEqNode
```

## Key Methods

All AST nodes inherit from `ASTnode` and implement:

- `namecheck(SymbolTable st)` - Performs name analysis, checking that all identifiers are declared before use
- `typecheck()` - Performs type checking, ensuring type compatibility in expressions and statements  
- `decompile(PrintWriter pw, int indent)` - Outputs a formatted representation of the AST

## Dependencies

These classes depend on:
- `SymbolTable` - Symbol table for name resolution
- `DataType` - Enum representing types (INT, BOOLEAN, STRING, VOID, ERROR)
- `SymbolType` - Enum representing symbol kinds (CLASS, METHOD, FIELD, ARGUMENT, VARIABLE)
- `Types` - Type compatibility checking utilities
- `Errors` - Error reporting utilities
- `Sequence` - Generic sequence container
- `NoCurrentException` - Exception for sequence operations

## Compilation

Class files are compiled from this directory but placed in the parent directory to maintain the default package structure:

```bash
javac -d .. *.java
```

This is handled automatically by the Makefile.

## Adding New Nodes

To add a new AST node:

1. Create a new .java file in this directory
2. Extend the appropriate base class (ASTnode, ExpNode, StmtNode, etc.)
3. Make the class `public`
4. Do NOT add a package declaration
5. Include standard imports (java.io.*, java.util.*)
6. Implement required methods (namecheck, typecheck, decompile)
7. The Makefile will automatically compile it

## File Naming Convention

Each file is named exactly after the class it contains:
- Class name: `ProgramNode` → File name: `ProgramNode.java`
- Class name: `BinaryExpNode` → File name: `BinaryExpNode.java`

This makes navigation intuitive and follows Java conventions.
