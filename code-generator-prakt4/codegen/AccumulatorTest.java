import support.*;
import ast.expr.*;
import java.io.*;

/**
 * Test class to demonstrate accumulator-based code generation
 */
public class AccumulatorTest {
    public static void main(String[] args) {
        try {
            // Create output writer
            PrintWriter out = new PrintWriter(System.out);
            
            // Create accumulator-based code generator
            AccumulatorCodeGenerator codeGen = new AccumulatorCodeGenerator(out);
            
            System.out.println("=== Accumulator-based Code Generation Demo ===\n");
            
            // Test 1: Simple integer literal
            System.out.println("Test 1: Integer literal (42)");
            IntLitNode lit42 = new IntLitNode(1, 1, 42);
            lit42.codegen(codeGen);
            System.out.println("Generated code loads 42 into $a0\n");
            
            // Test 2: Simple addition (5 + 3)
            System.out.println("Test 2: Addition (5 + 3)");
            IntLitNode lit5 = new IntLitNode(1, 1, 5);
            IntLitNode lit3 = new IntLitNode(1, 5, 3);
            PlusNode addNode = new PlusNode(lit5, lit3);
            addNode.codegen(codeGen);
            System.out.println("Generated code for 5 + 3, result in $a0\n");
            
            // Test 3: Complex expression (10 - (4 * 2))
            System.out.println("Test 3: Complex expression (10 - (4 * 2))");
            IntLitNode lit10 = new IntLitNode(1, 1, 10);
            IntLitNode lit4 = new IntLitNode(1, 7, 4);
            IntLitNode lit2 = new IntLitNode(1, 11, 2);
            TimesNode mulNode = new TimesNode(lit4, lit2);
            ParenthesizedExpNode parenNode = new ParenthesizedExpNode(mulNode);
            MinusNode subNode = new MinusNode(lit10, parenNode);
            subNode.codegen(codeGen);
            System.out.println("Generated code for 10 - (4 * 2), result in $a0\n");
            
            // Test 4: Unary minus (-7)
            System.out.println("Test 4: Unary minus (-7)");
            IntLitNode lit7 = new IntLitNode(1, 2, 7);
            UnaryMinusNode negNode = new UnaryMinusNode(lit7);
            negNode.codegen(codeGen);
            System.out.println("Generated code for -7, result in $a0\n");
            
            // Test 5: Boolean literals
            System.out.println("Test 5: Boolean literals");
            TrueNode trueNode = new TrueNode(1, 1);
            FalseNode falseNode = new FalseNode(1, 6);
            trueNode.codegen(codeGen);
            falseNode.codegen(codeGen);
            System.out.println("Generated code for true and false\n");
            
            // Finalize and output the assembly code
            System.out.println("=== Generated MIPS Assembly ===");
            codeGen.finalizeCode();
            out.flush();
            
        } catch (Exception e) {
            System.err.println("Error during code generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}