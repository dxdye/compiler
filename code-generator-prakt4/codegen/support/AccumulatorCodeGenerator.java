package support;

import java.io.*;

/**
 * Accumulator-based code generator for MIPS assembly generation.
 * Uses $a0 as accumulator register and manages stack-based computation.
 */
public class AccumulatorCodeGenerator extends CodeGenerator {
    // Accumulator register
    public static final String ACCUMULATOR = "$a0";
    // Temporary register for previous results
    public static final String TEMP_RESULT = "$t1";
    
    // Stack management
    private int stackSize = 0;
    
    public AccumulatorCodeGenerator(PrintWriter output) {
        super(output);
    }
    
    /**
     * Push accumulator value to stack
     */
    public void pushAccumulator() {
        pushAccumulator(null);
    }
    
    /**
     * Push accumulator value to stack with comment
     */
    public void pushAccumulator(String comment) {
        emit("addi $sp, $sp, -4", "Allocate stack space");
        emit("sw " + ACCUMULATOR + ", 0($sp)", comment != null ? comment : "Push accumulator to stack");
        stackSize += 4;
    }
    
    /**
     * Pop value from stack to given register
     */
    public void popToRegister(String targetReg) {
        popToRegister(targetReg, null);
    }
    
    /**
     * Pop value from stack to given register with comment
     */
    public void popToRegister(String targetReg, String comment) {
        emit("lw " + targetReg + ", 0($sp)", comment != null ? comment : "Pop from stack to " + targetReg);
        emit("addi $sp, $sp, 4", "Deallocate stack space");
        stackSize -= 4;
    }
    
    /**
     * Pop value from stack to temp register $t1
     */
    public void popToTemp() {
        popToRegister(TEMP_RESULT, "Pop previous result to temp");
    }
    
    /**
     * Load immediate value into accumulator (without pseudoinstructions)
     */
    public void loadImmediate(int value) {
        if (value >= -32768 && value <= 32767) {
            // Can use addiu with $zero
            emit("addiu " + ACCUMULATOR + ", $zero, " + value, "Load immediate " + value + " to accumulator");
        } else {
            // Need to use lui and ori for larger values
            int upper = (value >>> 16) & 0xFFFF;
            int lower = value & 0xFFFF;
            if (upper != 0) {
                emit("lui " + ACCUMULATOR + ", " + upper, "Load upper 16 bits");
                if (lower != 0) {
                    emit("ori " + ACCUMULATOR + ", " + ACCUMULATOR + ", " + lower, "Load lower 16 bits");
                }
            } else {
                emit("ori " + ACCUMULATOR + ", $zero, " + lower, "Load immediate " + value + " to accumulator");
            }
        }
    }
    
    /**
     * Load immediate value into accumulator with comment (without pseudoinstructions)
     */
    public void loadImmediate(int value, String comment) {
        if (value >= -32768 && value <= 32767) {
            // Can use addiu with $zero
            emit("addiu " + ACCUMULATOR + ", $zero, " + value, comment);
        } else {
            // Need to use lui and ori for larger values
            int upper = (value >>> 16) & 0xFFFF;
            int lower = value & 0xFFFF;
            if (upper != 0) {
                emit("lui " + ACCUMULATOR + ", " + upper, comment + " (upper bits)");
                if (lower != 0) {
                    emit("ori " + ACCUMULATOR + ", " + ACCUMULATOR + ", " + lower, comment + " (lower bits)");
                }
            } else {
                emit("ori " + ACCUMULATOR + ", $zero, " + lower, comment);
            }
        }
    }
    
    /**
     * Binary operation: performs operation between temp register and accumulator
     * Result is stored in accumulator
     */
    public void binaryOperation(String operation, String comment) {
        emit(operation + " " + ACCUMULATOR + ", " + TEMP_RESULT + ", " + ACCUMULATOR, comment);
    }
    
    /**
     * Unary operation: performs operation on accumulator (without pseudoinstructions)
     */
    public void unaryOperation(String operation, String comment) {
        if (operation.equals("neg")) {
            // Replace neg pseudoinstruction with sub from $zero
            emit("sub " + ACCUMULATOR + ", $zero, " + ACCUMULATOR, comment);
        } else {
            emit(operation + " " + ACCUMULATOR + ", " + ACCUMULATOR, comment);
        }
    }
    
    /**
     * Generate unique label for jumps and branches
     */
    public String genLabel(String prefix) {
        return getLabelGenerator().newLabel(prefix);
    }
    
    /**
     * Emit conditional branch based on accumulator value
     */
    public void branchIfZero(String label, String comment) {
        emit("beq " + ACCUMULATOR + ", $zero, " + label, comment);
    }
    
    /**
     * Emit conditional branch if accumulator is not zero
     */
    public void branchIfNotZero(String label, String comment) {
        emit("bne " + ACCUMULATOR + ", $zero, " + label, comment);
    }
    
    /**
     * Emit unconditional jump
     */
    public void jump(String label, String comment) {
        emit("j " + label, comment);
    }
    
    /**
     * Set up VTable for class (if needed)
     */
    public void setupVTable(String className) {
        // Add VTable setup code in data segment
        addDataSegment("# VTable for " + className + "\n");
        addDataSegment("_" + className + "_vtable:\n");
        // VTable entries would be added here based on class methods
    }
    
    /**
     * Set up ATag for class (if needed) 
     */
    public void setupATag(String className) {
        // Add ATag setup code in data segment
        addDataSegment("# ATag for " + className + "\n");
        addDataSegment("_" + className + "_atag:\n");
        addDataSegment("\t.word 0  # Class ID placeholder\n");
    }
    
    /**
     * Load immediate value into register without pseudoinstructions
     */
    public void loadImmediateToReg(String reg, int value, String comment) {
        if (value >= -32768 && value <= 32767) {
            // Can use addiu with $zero
            emit("addiu " + reg + ", $zero, " + value, comment);
        } else {
            // Need to use lui and ori for larger values
            int upper = (value >>> 16) & 0xFFFF;
            int lower = value & 0xFFFF;
            if (upper != 0) {
                emit("lui " + reg + ", " + upper, comment + " (upper bits)");
                if (lower != 0) {
                    emit("ori " + reg + ", " + reg + ", " + lower, comment + " (lower bits)");
                }
            } else {
                emit("ori " + reg + ", $zero, " + lower, comment);
            }
        }
    }
    
    /**
     * Get current stack size
     */
    public int getStackSize() {
        return stackSize;
    }
    
    /**
     * Reset stack size (for new function/scope)
     */
    public void resetStack() {
        stackSize = 0;
    }
}