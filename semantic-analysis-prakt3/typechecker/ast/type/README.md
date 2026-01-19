# AST Type Nodes

This package contains AST nodes representing type information in the Simple language.

## Classes

### Abstract Base
- **`TypeNode.java`** - Abstract base class for all type nodes
  - Provides common type functionality
  - Inherited by all specific type node types

### Concrete Type Nodes
- **`VoidNode.java`** - Represents the `void` type
  - Used for methods that don't return a value
  - Example: `void main() { ... }`

- **`IntNode.java`** - Represents the `int` type
  - Integer type for numeric values
  - Example: `int count;`

- **`BooleanNode.java`** - Represents the `boolean` type
  - Boolean type for true/false values
  - Example: `boolean flag;`

- **`StringNode.java`** - Represents the `String` type
  - String type for text values
  - Example: `String message;`

## Hierarchy
```
TypeNode (abstract)
├── VoidNode
├── IntNode
├── BooleanNode
└── StringNode
```

## Package Structure
```
ast.type
├── TypeNode.java
├── VoidNode.java
├── IntNode.java
├── BooleanNode.java
└── StringNode.java
```

## Type System
Each type node corresponds to a value in the `support.DataType` enum:
- `VoidNode` → `DataType.VOID`
- `IntNode` → `DataType.INT`
- `BooleanNode` → `DataType.BOOLEAN`
- `StringNode` → `DataType.STRING`

## Dependencies
- **Extends**: `ast.base.ASTnode`
- **Imports**: `support.*` (for DataType, Types, etc.)
