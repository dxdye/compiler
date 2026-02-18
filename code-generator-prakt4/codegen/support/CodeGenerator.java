package support;

import java.io.*;
import java.util.*;

/**
 * Main code generator class for MIPS assembly generation.
 * Manages the overall code generation process and provides utilities.
 */
public class CodeGenerator {
    private PrintWriter out;
    private RegisterAllocator regAlloc;
    private LabelGenerator labelGen;
    private SymbolTable globalSymbolTable;
    
    // Data segment for static variables
    private StringBuilder dataSegment;
    // Text segment for code
    private StringBuilder textSegment;
    
    // Stack frame management
    private int currentFrameSize = 0;
    private Map<String, Integer> localOffsets;
    
    public CodeGenerator(PrintWriter output) {
        this.out = output;
        this.regAlloc = new RegisterAllocator();
        this.labelGen = new LabelGenerator();
        this.dataSegment = new StringBuilder();
        this.textSegment = new StringBuilder();
        this.localOffsets = new HashMap<>();
    }
    
    public void setGlobalSymbolTable(SymbolTable st) {
        this.globalSymbolTable = st;
    }
    
    public RegisterAllocator getRegisterAllocator() {
        return regAlloc;
    }
    
    public LabelGenerator getLabelGenerator() {
        return labelGen;
    }
    
    public SymbolTable getGlobalSymbolTable() {
        return globalSymbolTable;
    }
    
    /**
     * Generate the complete MIPS program
     */
    public void finalizeCode() {
        // Write data segment
        out.println("# MIPS Assembly Code Generated");
        out.println("# Data Segment");
        out.println("\t.data");
        out.print(dataSegment.toString());
        
        // Write text segment
        out.println("\n# Text Segment");
        out.println("\t.text");
        out.println("\t.globl main");
        out.print(textSegment.toString());
        
        out.flush();
    }
    
    /**
     * Add code to the data segment
     */
    public void addDataSegment(String code) {
        dataSegment.append(code);
    }
    
    /**
     * Add code to the text segment
     */
    public void addTextSegment(String code) {
        textSegment.append(code);
    }
    
    /**
     * Emit a comment
     */
    public void emitComment(String comment) {
        textSegment.append("# ").append(comment).append("\n");
    }
    
    /**
     * Emit a label
     */
    public void emitLabel(String label) {
        textSegment.append(label).append(":\n");
    }
    
    /**
     * Emit an instruction
     */
    public void emit(String instruction) {
        textSegment.append("\t").append(instruction).append("\n");
    }
    
    /**
     * Emit an instruction with comment
     */
    public void emit(String instruction, String comment) {
        textSegment.append("\t").append(instruction);
        if (comment != null && !comment.isEmpty()) {
            textSegment.append("\t# ").append(comment);
        }
        textSegment.append("\n");
    }
    
    /**
     * Allocate space for a local variable
     */
    public int allocateLocal(String varName, int size) {
        currentFrameSize += size;
        int offset = -currentFrameSize;
        localOffsets.put(varName, offset);
        return offset;
    }
    
    /**
     * Get offset of a local variable
     */
    public Integer getLocalOffset(String varName) {
        return localOffsets.get(varName);
    }
    
    /**
     * Reset local variable tracking for new function
     */
    public void resetLocals() {
        localOffsets.clear();
        currentFrameSize = 0;
    }
    
    /**
     * Get current frame size
     */
    public int getFrameSize() {
        return currentFrameSize;
    }
    
    /**
     * Set frame size (for adjustments)
     */
    public void setFrameSize(int size) {
        this.currentFrameSize = size;
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
     * Add a static variable to data segment
     */
    public void addStaticVariable(String name, DataType type) {
        String label = getStaticVarLabel(name);
        switch (type) {
            case INT:
                dataSegment.append(label).append(":\t.word 0\t# static int ").append(name).append("\n");
                break;
            case BOOLEAN:
                dataSegment.append(label).append(":\t.word 0\t# static boolean ").append(name).append("\n");
                break;
            case STRING:
                // Strings are handled differently - they reference string literals
                dataSegment.append(label).append(":\t.word 0\t# static String ").append(name).append("\n");
                break;
            default:
                break;
        }
    }
    
    /**
     * Add a string literal to data segment
     */
    public String addStringLiteral(String value) {
        String label = labelGen.newStringLabel();
        // Escape special characters for MIPS
        String escaped = value.replace("\\", "\\\\")
                             .replace("\n", "\\n")
                             .replace("\t", "\\t")
                             .replace("\"", "\\\"");
        dataSegment.append(label).append(":\t.asciiz \"").append(escaped).append("\"\n");
        return label;
    }
    
    /**
     * Get the label for a static variable
     */
    public String getStaticVarLabel(String varName) {
        return "_static_" + varName;
    }
}
