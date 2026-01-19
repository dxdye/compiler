# AST Package Structure

This directory contains the Abstract Syntax Tree (AST) implementation for the Simple language compiler, organized into semantic subpackages.

## Package Organization

### `ast.base/` - Core Infrastructure (8 classes)
Foundation classes and container nodes:
- `ASTnode` - Abstract base for all AST nodes
- `ProgramNode` - Root of the AST
- `ClassBodyNode` - Class body container
- List nodes: `DeclListNode`, `FormalsListNode`, `StmtListNode`, `ExpListNode`, `SwitchGroupListNode`

**[See detailed documentation →](base/README.md)**

### `ast.decl/` - Declaration Nodes (5 classes)
All declaration types in the language:
- `DeclNode` - Abstract base for declarations
- `FieldDeclNode` - Field declarations
- `VarDeclNode` - Variable declarations
- `MethodDeclNode` - Method declarations
- `FormalDeclNode` - Parameter declarations

**[See detailed documentation →](decl/README.md)**

### `ast.type/` - Type Nodes (5 classes)
Type representation nodes:
- `TypeNode` - Abstract base for types
- `VoidNode`, `IntNode`, `BooleanNode`, `StringNode`

**[See detailed documentation →](type/README.md)**

### `ast.stmt/` - Statement Nodes (12 classes)
All statement types:
- Control flow: `IfStmtNode`, `IfElseStmtNode`, `WhileStmtNode`, `SwitchStmtNode`
- Method-related: `MethodBodyNode`, `CallStmtNode`, `ReturnStmtNode`, `ReturnExprStmtNode`
- Other: `AssignStmtNode`, `PrintStmtNode`, `CaseStmtNode`

**[See detailed documentation →](stmt/README.md)**

### `ast.expr/` - Expression Nodes (25 classes)
All expression types:
- Literals: `IntLiteralExpNode`, `BoolLiteralExpNode`, `StringLiteralExpNode`
- Operators: Unary (`UnaryMinusExpNode`, `NotExpNode`)
- Binary: Arithmetic (`+`, `-`, `*`, `/`), Logical (`&&`, `||`), Comparison (`<`, `>`, `<=`, `>=`), Equality (`==`, `!=`)
- Other: `IdExpNode`, `CallExprNode`, `ParenthesizedExpNode`

**[See detailed documentation →](expr/README.md)**

## Design Overview

### Class Hierarchy
```
ASTnode (ast.base)
├── ProgramNode
├── ClassBodyNode
├── *ListNode (containers)
├── DeclNode (ast.decl)
│   ├── FieldDeclNode
│   ├── VarDeclNode
│   ├── MethodDeclNode
│   └── FormalDeclNode
├── TypeNode (ast.type)
│   ├── VoidNode
│   ├── IntNode
│   ├── BooleanNode
│   └── StringNode
├── StmtNode (ast.stmt)
│   ├── MethodBodyNode
│   ├── IfStmtNode, IfElseStmtNode, WhileStmtNode
│   ├── AssignStmtNode, PrintStmtNode
│   └── ... (see stmt/README.md)
└── ExpNode (ast.expr)
    ├── Literals (Int, Bool, String)
    ├── UnaryExpNode → UnaryMinusExpNode, NotExpNode
    ├── BinaryExpNode → Arithmetic, Logical, Comparison, Equality
    └── ... (see expr/README.md)
```

### Common Functionality
All AST nodes inherit from `ASTnode` and provide:
- **Unparsing** - Converting AST back to source code (`decompile()`)
- **Name Checking** - Verifying identifier declarations and uses (`namecheck()`)
- **Type Checking** - Ensuring type correctness (`typecheck()`)

### Dependencies
- **Core support**: `support.*` package provides `SymbolTable`, `DataType`, `Types`, `Errors`, etc.
- **Inter-package**: Nodes import from other AST packages as needed

## Statistics
- **Total Classes**: 55 (8 base + 5 decl + 5 type + 12 stmt + 25 expr)
- **Total Packages**: 5 subpackages + 1 parent package
- **Lines of Code**: ~1,950 (originally in single `ast.java` file)

## Usage

### In Parser (simple.cup)
```java
import ast.base.*;
import ast.decl.*;
import ast.type.*;
import ast.stmt.*;
import ast.expr.*;
```

### In Main Program (P4.java)
```java
import ast.base.*;  // For ProgramNode, ASTnode
import support.*;   // For symbol table, types, errors
```

## Build System
All AST files are compiled to the `classes/` directory:
```
classes/
├── ast/
│   ├── base/
│   ├── decl/
│   ├── type/
│   ├── stmt/
│   └── expr/
└── support/
```

See `Makefile` for build configuration.

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
