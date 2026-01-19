# AST Base Classes

This package contains the core Abstract Syntax Tree infrastructure classes and list/container nodes.

## Classes

### Core Infrastructure
- **`ASTnode.java`** - Abstract base class for all AST nodes
  - Provides common functionality: unparsing, name checking, type checking
  - All AST nodes inherit from this class

- **`ProgramNode.java`** - Root node of the AST
  - Represents the entire Simple program
  - Contains a ClassBodyNode

- **`ClassBodyNode.java`** - Represents a class body
  - Contains a DeclListNode with field, variable, and method declarations

### List/Container Nodes
- **`DeclListNode.java`** - List of declarations (fields, variables, methods)
- **`FormalsListNode.java`** - List of formal parameters in method declarations
- **`StmtListNode.java`** - List of statements in a method body or block
- **`ExpListNode.java`** - List of expressions (e.g., method call arguments)
- **`SwitchGroupListNode.java`** - List of case statements in a switch block

## Package Structure
```
ast.base
├── ASTnode.java          (abstract base)
├── ProgramNode.java      (root)
├── ClassBodyNode.java    (class body)
└── *ListNode.java        (container nodes)
```

## Dependencies
- **Imports**: `support.*` (for SymbolTable, Types, Errors, etc.)
- **Used by**: All other AST packages (decl, type, stmt, expr)
