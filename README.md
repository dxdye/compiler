# Description 

This is an implementation of a compiler for the so-called 'simple'-language.

It implements a:
- frontend, lexer: it identifies tokens via Regex
- frontend, parser: it will create an AST from the lexed tokens input stream - it's able to decompile the original into (bottom-up parser)
- froetend, semantic analysis: checks for type and name errors 
- backend, code generator: for MIPS with ABI support

# Structure

The project consists of sub-projects, which store redundant 
deps and .jlex and .jcup-specifications. For the reviewer of this project, 
the progress of this compiler build will be recognizable. 


# Lexer 

It identifies tokens via regular expressions. It is specified via the `simple.jlex` file, 
which is a java implementation of `lex`.
It is part of the frontend.

# Parser 

It builds a so-called abstract syntax tree, which supports a context free grammar. 
The grammar rules are simplified via the rule set in the simple.cup (critical utilization pattern), 
which builds and executes a Push-Down Automata two parse the tree. While the grammer is fitted onto 
the lexed tokens, the Node type and accurate decompile function are matched and created. 

# Semantic Analysis

It extends the implemented (generated parser) check for name errors and type errors.
The semantic check is implemented directly into the node and there was major refactoring ongoing, 
so the classes in `ast,java` now would have their own `.java` file, since the file ast.java got really huge.

Example (type-error): 
IF the expression is `a >= b` and we have `a `as string and `b` as int, then the types for a and b 
may be incompatible in the simple language. The compiler needs to recognize that. 

Example (name-error): 
```c
// in simple language..
int a; 
a = 5
int b; 
a = b < a; // b was not defined before
```

In this example we will have a name error, since b is used before it was defined.


# Code Generation

It will extend the leafs of the AST with a `codegen` method, now specifically for MIPS. 
Also it has a support class called `CodeGenerator.java`, which abstracts away most of the MIPS specs; names the registers and so on..


# Summary 

Have fun! Create some MIPS code!

