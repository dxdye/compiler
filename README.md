# Description 

This is an implementation of a compiler for the so-called 'simple'-language.

It implements a:
> frontend, lexer: it identifies tokens via Regex
> frontend, parser: it will build an AST from the lexed tokens - it's able to decompile the original into 

ToDo: 
> frontend: name- and type- checking 
> backend: assembler code generation

# Structure

The project consists of sub-projects, which store redundant 
deps and .jlex and jcup-specs. For the reviewer of this project, 
the progress of this compiler build will be recognizable. 


# Lexer 

It identifies tokens via regular expressions. It is specified via the `simple.jlex` file, 
which is a java implementation of `lex`.
It is part of the frontend.

# Parser 

It builds a so-called abstract syntax tree, which supports a context free grammar. 
The grammar rules are simplified via the ruleset in the simple.cup (critical utilization pattern), 
which builds and executes a Push-Down Automata two parse the tree. While the grammer is fitted onto 
the lexed tokens, the Node type and accurate decompile function are matched and created. 


