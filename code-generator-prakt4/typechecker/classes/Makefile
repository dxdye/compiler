###
# This Makefile can be used to make a parser for the Simple language
# (Yylex.class) and to make a program (P4.class) that tests the parser and
# the unparse methods in the AST classes (organized in ast/ package).
#
# The default makes both the parser and the test program.
#
# make clean removes all generated files.
#
###
CLASSPATH = .:classes:../jars/java-cup-11b-runtime.jar
JARFILE = ../jars/java-cup-11b.jar
FLAGS = -g -cp $(CLASSPATH) -d classes

# Default target
all: P4.class

# Help target
help:
	@echo "Available targets:"
	@echo "  make              - Build the compiler (default)"
	@echo "  make all          - Build the compiler"
	@echo "  make clean        - Remove generated .class and .java files"
	@echo "  make clean-tests  - Remove test output files"
	@echo "  make clean-all    - Remove all generated files"
	@echo ""
	@echo "Test targets:"
	@echo "  make test              - Run basic test"
	@echo "  make test-simple       - Test simple correct program"
	@echo "  make test-comprehensive - Test comprehensive program"
	@echo "  make test-name-errors  - Test name error detection"
	@echo "  make test-type-errors  - Test type error detection"
	@echo "  make test-correct      - Run all correct program tests"
	@echo "  make test-errors       - Run all error detection tests"
	@echo "  make test-all          - Run ALL tests"
	@echo ""
	@echo "Other targets:"
	@echo "  make submit       - Create submission zip"
	@echo "  make handout      - Create handout zip"

P4.class: P4.java parser.class Yylex.class ASTnode.class IO.class
	javac $(FLAGS) P4.java

parser.java: simple.cup
	java -jar $(JARFILE) < simple.cup

parser.class: parser.java ASTnode.class Yylex.class Errors.class
	javac $(FLAGS) parser.java

Yylex.class: simple.jlex.java sym.class Errors.class ASTnode.class
	javac $(FLAGS) simple.jlex.java

# Compile all AST packages and support package
ASTnode.class: ast/base/*.java ast/decl/*.java ast/type/*.java ast/stmt/*.java ast/expr/*.java support/*.java
	javac $(FLAGS) support/*.java ast/base/*.java ast/decl/*.java ast/type/*.java ast/stmt/*.java ast/expr/*.java

DataType.class: support/DataType.java
	javac $(FLAGS) support/DataType.java

SymbolType.class: support/SymbolType.java
	javac $(FLAGS) support/SymbolType.java

SymbolTable.class: support/SymbolTable.java
	javac $(FLAGS) support/SymbolTable.java

Types.class: support/Types.java
	javac $(FLAGS) support/Types.java

Sequence.class: support/Sequence.java
	javac $(FLAGS) support/Sequence.java

NoCurrentException.class: support/NoCurrentException.java
	javac $(FLAGS) support/NoCurrentException.java

Errors.class: support/Errors.java
	javac $(FLAGS) support/Errors.java


simple.jlex.java: simple.jlex
	@echo "IMPORTANT You must replace the simple.jlex file with your scanner"
	@echo "The provided simple.jlex is incomplete and just has what"
	@echo "Is needed so that the incompled parser compiles"
	jflex simple.jlex # use jflex it is more flexiblej
	mv Yylex.java simple.jlex.java # rename the file produced by jflex


sym.class: sym.java
	javac $(FLAGS) sym.java

sym.java: simple.cup
	java -cp $(CLASSPATH) java_cup.Main < simple.cup

IO.class: IO.java
	javac $(FLAGS) IO.java

###
# clean
###
clean:
	rm -f *~ parser.java simple.jlex.java
	rm -rf classes/*

###
# test targets
###

# Single test target (existing)
test:	tests/test.sim P4.class 
	@echo "If you get an error below your Parser does not work yet!"
	@echo "Modify the simple.cup specification to implement the language!"
	java -cp $(CLASSPATH) P4 tests/test.sim tests/test.out

# Test with simple correct program
test-simple: tests/simple_correct.sim P4.class
	@echo "=========================================="
	@echo "Testing: simple_correct.sim (should pass)"
	@echo "=========================================="
	java -cp $(CLASSPATH) P4 tests/simple_correct.sim tests/simple_correct.out
	@echo ""

# Test with comprehensive program
test-comprehensive: tests/comprehensive_test.sim P4.class
	@echo "=========================================="
	@echo "Testing: comprehensive_test.sim (should pass)"
	@echo "=========================================="
	java -cp $(CLASSPATH) P4 tests/comprehensive_test.sim tests/comprehensive_test.out
	@echo ""

# Test name errors (should fail name checking)
test-name-errors: tests/test_name_error.sim P4.class
	@echo "=========================================="
	@echo "Testing: test_name_error.sim (should fail name checking)"
	@echo "=========================================="
	-java -cp $(CLASSPATH) P4 tests/test_name_error.sim tests/test_name_error.out
	@echo ""

# Test type errors (should fail type checking)
test-type-errors: tests/test_type_error.sim P4.class
	@echo "=========================================="
	@echo "Testing: test_type_error.sim (should fail type checking)"
	@echo "=========================================="
	-java -cp $(CLASSPATH) P4 tests/test_type_error.sim tests/test_type_error.out
	@echo ""

# Test with type example (has errors)
test-type-example: tests/type_example.sim P4.class
	@echo "=========================================="
	@echo "Testing: type_example.sim (should fail type checking)"
	@echo "=========================================="
	-java -cp $(CLASSPATH) P4 tests/type_example.sim tests/type_example.out
	@echo ""

# Test all correct programs
test-correct: test-simple test-comprehensive
	@echo "=========================================="
	@echo "All correct tests completed"
	@echo "=========================================="

# Test all error-detecting programs
test-errors: test-name-errors test-type-errors test-type-example
	@echo "=========================================="
	@echo "All error detection tests completed"
	@echo "=========================================="

# Run all tests
test-all: test-correct test-errors
	@echo "=========================================="
	@echo "ALL TESTS COMPLETED"
	@echo "=========================================="

# Clean test output files
clean-tests:
	rm -f tests/*.out

# Clean everything including tests
clean-all: clean clean-tests
	@echo "Cleaned all generated files and test outputs"

###
# submit
###

submit:
	zip submit.zip support/*.java ast/*/*.java P4.java IO.java tests/test.sim Makefile simple.jlex simple.cup

###
# handout
###

handout:
	zip handout.zip tests/test.sim Makefile simple.grammar support/*.java ast/*/*.java P4.java IO.java simple.cup simple.grammar




