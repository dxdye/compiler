# AST Declaration Nodes

This package contains AST nodes for all declaration types in the Simple language.

## Classes

### Abstract Base
- **`DeclNode.java`** - Abstract base class for all declarations
  - Provides common declaration functionality
  - Inherited by all specific declaration node types

### Concrete Declaration Types
- **`FieldDeclNode.java`** - Field declarations (class-level variables)
  - Example: `static int x;`
  - Can be static or instance fields

- **`VarDeclNode.java`** - Local variable declarations
  - Example: `int count;`
  - Declared within method bodies

- **`MethodDeclNode.java`** - Method declarations
  - Example: `public static void main() { ... }`
  - Contains return type, name, parameters, and body

- **`FormalDeclNode.java`** - Formal parameter declarations
  - Example: `int x` in `void foo(int x) { ... }`
  - Used in method parameter lists

## Hierarchy
```
DeclNode (abstract)
├── FieldDeclNode
├── VarDeclNode
├── MethodDeclNode
└── FormalDeclNode
```

## Package Structure
```
ast.decl
├── DeclNode.java
├── FieldDeclNode.java
├── VarDeclNode.java
├── MethodDeclNode.java
└── FormalDeclNode.java
```

## Dependencies
- **Extends**: `ast.base.ASTnode`
- **Imports**: `support.*`, `ast.base.*`, `ast.type.*`, `ast.stmt.*`
