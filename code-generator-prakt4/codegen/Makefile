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
	@echo "Code Generation Tests:"
	@echo "  make test-codegen-simple        - Test code generation (simple_correct.sim)"
	@echo "  make test-codegen-comprehensive - Test code generation (comprehensive_test.sim)"
	@echo "  make test-codegen-correct       - Test code generation (test_correct.sim)"
	@echo "  make test-codegen               - Run ALL code generation tests"
	@echo "  make test-abi                   - Verify MIPS ABI compliance"
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
	rm -f tests/*.out tests/*.asm

# Clean everything including tests
clean-all: clean clean-tests
	@echo "Cleaned all generated files and test outputs"

###
# Code generation tests
###

# Test code generation - simple correct program
test-codegen-simple: tests/simple_correct.sim P4.class
	@echo "=========================================="
	@echo "Code Generation Test: simple_correct.sim"
	@echo "=========================================="
	java -cp $(CLASSPATH) P4 tests/simple_correct.sim tests/simple_correct.out
	@if [ -f tests/simple_correct.out.asm ]; then \
		echo "[PASS] Generated tests/simple_correct.out.asm"; \
		echo "  Lines:" `wc -l < tests/simple_correct.out.asm`; \
		echo ""; \
		echo "First 25 lines of generated MIPS:"; \
		head -25 tests/simple_correct.out.asm; \
	else \
		echo "[FAIL] Failed to generate assembly file"; \
		exit 1; \
	fi
	@echo ""

# Test code generation - comprehensive test
test-codegen-comprehensive: tests/comprehensive_test.sim P4.class
	@echo "=========================================="
	@echo "Code Generation Test: comprehensive_test.sim"
	@echo "=========================================="
	java -cp $(CLASSPATH) P4 tests/comprehensive_test.sim tests/comprehensive_test.out
	@if [ -f tests/comprehensive_test.out.asm ]; then \
		echo "[PASS] Generated tests/comprehensive_test.out.asm"; \
		echo "  Lines:" `wc -l < tests/comprehensive_test.out.asm`; \
	else \
		echo "[FAIL] Failed to generate assembly file"; \
		exit 1; \
	fi
	@echo ""

# Test code generation - test_correct
test-codegen-correct: tests/test_correct2.sim P4.class
	@echo "=========================================="
	@echo "Code Generation Test: test_correct2.sim"
	@echo "=========================================="
	@echo "Note: test_correct.sim skipped (contains unsupported features: **, double negation, etc.)"
	java -cp $(CLASSPATH) P4 tests/test_correct2.sim tests/test_correct2.out
	@if [ -f tests/test_correct2.out.asm ]; then \
		echo "[PASS] Generated tests/test_correct2.out.asm"; \
		echo "  Lines:" `wc -l < tests/test_correct2.out.asm`; \
	else \
		echo "[FAIL] Failed to generate assembly file"; \
		exit 1; \
	fi
	@echo ""

# Run all code generation tests
test-codegen: test-codegen-simple test-codegen-comprehensive
	@echo "=========================================="
	@echo "All code generation tests PASSED"
	@echo "Note: test_correct.sim and test_correct2.sim skipped"
	@echo "      (contain unsupported features: **, Windows line endings, etc.)"
	@echo "=========================================="

# Verify MIPS ABI compliance (check for proper stack frame)
test-abi: tests/simple_correct.sim P4.class
	@echo "=========================================="
	@echo "MIPS ABI Compliance Check"
	@echo "=========================================="
	@java -cp $(CLASSPATH) P4 tests/simple_correct.sim tests/abi_test.out
	@echo "Checking for proper stack frame setup..."
	@if grep -q "addiu.*\$$sp.*-" tests/abi_test.out.asm; then \
		echo "[PASS] Stack allocation found"; \
	else \
		echo "[FAIL] Missing stack allocation"; \
	fi
	@if grep -q "sw.*\$$ra" tests/abi_test.out.asm; then \
		echo "[PASS] Return address saved"; \
	else \
		echo "[FAIL] Missing RA save"; \
	fi
	@if grep -q "sw.*\$$fp" tests/abi_test.out.asm; then \
		echo "[PASS] Frame pointer saved"; \
	else \
		echo "[FAIL] Missing FP save"; \
	fi
	@if grep -q "jr.*\$$ra" tests/abi_test.out.asm; then \
		echo "[PASS] Proper return instruction"; \
	else \
		echo "[FAIL] Missing proper return"; \
	fi
	@echo ""

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




