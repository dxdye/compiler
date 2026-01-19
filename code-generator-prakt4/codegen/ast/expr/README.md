# AST Expression Nodes

This package contains AST nodes for all expression types in the Simple language.

## Classes

### Abstract Base Classes
- **`ExpNode.java`** - Abstract base class for all expressions
  - Root of the expression hierarchy
  
- **`UnaryExpNode.java`** - Abstract base for unary expressions
  - Expressions with one operand
  
- **`BinaryExpNode.java`** - Abstract base for binary expressions
  - Expressions with two operands

### Literal Expressions
- **`IntLiteralExpNode.java`** - Integer literals
  - Example: `42`, `-17`
  
- **`BoolLiteralExpNode.java`** - Boolean literals
  - Example: `true`, `false`
  
- **`StringLiteralExpNode.java`** - String literals
  - Example: `"hello"`

### Identifiers
- **`IdExpNode.java`** - Variable/field references
  - Example: `x`, `count`

### Unary Operators
- **`UnaryMinusExpNode.java`** - Negation
  - Example: `-x`
  
- **`NotExpNode.java`** - Logical NOT
  - Example: `!flag`

### Arithmetic Operators (Binary)
- **`PlusExpNode.java`** - Addition
  - Example: `x + y`
  
- **`MinusExpNode.java`** - Subtraction
  - Example: `x - y`
  
- **`TimesExpNode.java`** - Multiplication
  - Example: `x * y`
  
- **`DivideExpNode.java`** - Division
  - Example: `x / y`

### Logical Operators (Binary)
- **`AndExpNode.java`** - Logical AND
  - Example: `a && b`
  
- **`OrExpNode.java`** - Logical OR
  - Example: `a || b`

### Comparison Operators (Binary)
- **`LessExpNode.java`** - Less than
  - Example: `x < y`
  
- **`GreaterExpNode.java`** - Greater than
  - Example: `x > y`
  
- **`LessEqExpNode.java`** - Less than or equal
  - Example: `x <= y`
  
- **`GreaterEqExpNode.java`** - Greater than or equal
  - Example: `x >= y`

### Equality Operators (Binary)
- **`EqualsExpNode.java`** - Equality
  - Example: `x == y`
  
- **`NotEqualsExpNode.java`** - Inequality
  - Example: `x != y`

### Other Expressions
- **`CallExprNode.java`** - Method call expression
  - Example: `foo(x, y)`
  
- **`ParenthesizedExpNode.java`** - Parenthesized expression
  - Example: `(x + y)`

## Hierarchy
```
ExpNode (abstract)
├── Literals
│   ├── IntLiteralExpNode
│   ├── BoolLiteralExpNode
│   └── StringLiteralExpNode
├── IdExpNode
├── UnaryExpNode (abstract)
│   ├── UnaryMinusExpNode
│   └── NotExpNode
├── BinaryExpNode (abstract)
│   ├── Arithmetic
│   │   ├── PlusExpNode
│   │   ├── MinusExpNode
│   │   ├── TimesExpNode
│   │   └── DivideExpNode
│   ├── Logical
│   │   ├── AndExpNode
│   │   └── OrExpNode
│   ├── Comparison
│   │   ├── LessExpNode
│   │   ├── GreaterExpNode
│   │   ├── LessEqExpNode
│   │   └── GreaterEqExpNode
│   └── Equality
│       ├── EqualsExpNode
│       └── NotEqualsExpNode
├── CallExprNode
└── ParenthesizedExpNode
```

## Package Structure
```
ast.expr (25 classes)
├── ExpNode.java
├── UnaryExpNode.java
├── BinaryExpNode.java
├── IntLiteralExpNode.java
├── BoolLiteralExpNode.java
├── StringLiteralExpNode.java
├── IdExpNode.java
├── UnaryMinusExpNode.java
├── NotExpNode.java
├── PlusExpNode.java
├── MinusExpNode.java
├── TimesExpNode.java
├── DivideExpNode.java
├── AndExpNode.java
├── OrExpNode.java
├── LessExpNode.java
├── GreaterExpNode.java
├── LessEqExpNode.java
├── GreaterEqExpNode.java
├── EqualsExpNode.java
├── NotEqualsExpNode.java
├── CallExprNode.java
└── ParenthesizedExpNode.java
```

## Dependencies
- **Extends**: `ast.base.ASTnode`
- **Imports**: `support.*`, `ast.base.*`
