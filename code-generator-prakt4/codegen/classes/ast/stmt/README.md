# AST Statement Nodes

This package contains AST nodes for all statement types in the Simple language.

## Classes

### Abstract Base
- **`StmtNode.java`** - Abstract base class for all statements
  - Provides common statement functionality
  - Inherited by all specific statement node types

### Concrete Statement Types

#### Control Flow
- **`IfStmtNode.java`** - If statement (without else)
  - Example: `if (condition) { ... }`
  
- **`IfElseStmtNode.java`** - If-else statement
  - Example: `if (condition) { ... } else { ... }`
  
- **`WhileStmtNode.java`** - While loop
  - Example: `while (condition) { ... }`
  
- **`SwitchStmtNode.java`** - Switch statement
  - Example: `switch (expr) { case 1: ... }`
  
- **`CaseStmtNode.java`** - Case statement within switch
  - Example: `case 1: statements; break;`

#### Method-Related
- **`MethodBodyNode.java`** - Method body container
  - Contains variable declarations and statements
  
- **`CallStmtNode.java`** - Method call statement
  - Example: `foo(x, y);`
  
- **`ReturnStmtNode.java`** - Return statement (void)
  - Example: `return;`
  
- **`ReturnExprStmtNode.java`** - Return statement with expression
  - Example: `return x + 1;`

#### Assignment & I/O
- **`AssignStmtNode.java`** - Assignment statement
  - Example: `x = 5;`
  
- **`PrintStmtNode.java`** - Print statement
  - Example: `System.out.println(x);`

## Hierarchy
```
StmtNode (abstract)
├── MethodBodyNode
├── PrintStmtNode
├── AssignStmtNode
├── IfStmtNode
├── IfElseStmtNode
├── WhileStmtNode
├── CallStmtNode
├── SwitchStmtNode
├── ReturnStmtNode
├── ReturnExprStmtNode
└── CaseStmtNode
```

## Package Structure
```
ast.stmt (12 classes)
├── StmtNode.java
├── MethodBodyNode.java
├── PrintStmtNode.java
├── AssignStmtNode.java
├── IfStmtNode.java
├── IfElseStmtNode.java
├── WhileStmtNode.java
├── CallStmtNode.java
├── SwitchStmtNode.java
├── ReturnStmtNode.java
├── ReturnExprStmtNode.java
└── CaseStmtNode.java
```

## Dependencies
- **Extends**: `ast.base.ASTnode`
- **Imports**: `support.*`, `ast.base.*`, `ast.expr.*`
