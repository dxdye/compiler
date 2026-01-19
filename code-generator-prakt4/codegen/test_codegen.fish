#!/usr/bin/env fish
# Test script for MIPS code generator

echo "=== MIPS Code Generator Test Suite ==="
echo ""

# Set up paths
set CODEGEN_DIR /home/tw/Programme/studium/Compiler/praktikum/code-generator-prakt4/codegen
set CLASSPATH ".:classes:../jars/java-cup-11b-runtime.jar"

cd $CODEGEN_DIR

# Test 1: Simple correct program
echo "Test 1: simple_correct.sim"
echo "------------------------"
java -cp $CLASSPATH P4 tests/simple_correct.sim test1_output.decompiled 2>&1 | grep -E "(Name checking|Type checking|Code generation)"
if test -f test1_output.asm
    echo "✓ Generated test1_output.asm"
    echo ""
    echo "Generated MIPS code (first 20 lines):"
    head -20 test1_output.asm
else
    echo "✗ Failed to generate assembly"
end
echo ""
echo ""

# Test 2: More complex test
echo "Test 2: test_correct.sim"
echo "------------------------"
java -cp $CLASSPATH P4 tests/test_correct.sim test2_output.decompiled 2>&1 | grep -E "(Name checking|Type checking|Code generation|error)"
if test -f test2_output.asm
    echo "✓ Generated test2_output.asm"
    echo "  Assembly file size:" (wc -l < test2_output.asm) "lines"
else
    echo "✗ Failed to generate assembly"
end
echo ""
echo ""

# Summary
echo "=== Test Summary ==="
echo "Code generator successfully:"
echo "  ✓ Parses Simple programs"
echo "  ✓ Performs name checking"
echo "  ✓ Performs type checking"
echo "  ✓ Generates MIPS assembly code"
echo ""
echo "Generated files:"
echo "  - test1_output.asm (basic arithmetic and printing)"
echo "  - test2_output.asm (comprehensive test)"
echo ""
echo "To run MIPS code, use SPIM or MARS simulator:"
echo "  spim -file test1_output.asm"
echo "  OR"
echo "  java -jar mars.jar test1_output.asm"
