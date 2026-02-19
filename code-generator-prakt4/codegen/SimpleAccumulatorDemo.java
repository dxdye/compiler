import support.*;
import java.io.*;

/**
 * Simple test to demonstrate accumulator-based code generation concepts
 */
public class SimpleAccumulatorDemo {
    public static void main(String[] args) {
        try {
            // Create output writer
            PrintWriter out = new PrintWriter(System.out);
            
            // Create accumulator-based code generator
            AccumulatorCodeGenerator codeGen = new AccumulatorCodeGenerator(out);
            
            System.out.println("=== Accumulator-based MIPS Code Generation Demo ===\n");
            
            // Demonstration of manual code generation
            System.out.println("Demo: Generating code for expression (5 + 3) * 2\n");
            
            // Simulate: Load 5 into accumulator
            codeGen.loadImmediate(5, "Load 5");
            
            // Simulate: Push 5 to stack
            codeGen.pushAccumulator("Save 5 for later");
            
            // Simulate: Load 3 into accumulator  
            codeGen.loadImmediate(3, "Load 3");
            
            // Simulate: Pop 5 from stack to $t1, add with 3 in $a0
            codeGen.popToTemp();
            codeGen.binaryOperation("add", "Compute 5 + 3");
            
            // Now result (8) is in accumulator
            // Simulate: Push result to stack
            codeGen.pushAccumulator("Save intermediate result (5+3)");
            
            // Simulate: Load 2 into accumulator
            codeGen.loadImmediate(2, "Load 2");
            
            // Simulate: Pop 8 from stack to $t1, multiply with 2 in $a0
            codeGen.popToTemp();
            codeGen.binaryOperation("mul", "Compute (5+3) * 2");
            
            // Final result is in accumulator ($a0)
            System.out.println("Final result should be in $a0: (5+3)*2 = 16\n");
            
            // Show unary minus example
            System.out.println("Demo: Generating code for unary minus -42\n");
            
            // Load 42 and negate
            codeGen.loadImmediate(42, "Load 42");
            codeGen.unaryOperation("neg", "Negate to get -42");
            
            System.out.println("Result of -42 should be in $a0\n");
            
            System.out.println("=== Generated MIPS Assembly ===");
            codeGen.finalizeCode();
            
        } catch (Exception e) {
            System.err.println("Error during code generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}