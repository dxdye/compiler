package support;

import java.util.*;

/**
 * Register allocator for MIPS code generation.
 * Manages temporary registers ($t0-$t9) and saved registers ($s0-$s7).
 */
public class RegisterAllocator {
    // MIPS registers
    public static final String ZERO = "$zero";  // Always 0
    public static final String V0 = "$v0";      // Return value / syscall
    public static final String V1 = "$v1";      // Return value
    public static final String A0 = "$a0";      // Argument 1
    public static final String A1 = "$a1";      // Argument 2
    public static final String A2 = "$a2";      // Argument 3
    public static final String A3 = "$a3";      // Argument 4
    public static final String SP = "$sp";      // Stack pointer
    public static final String FP = "$fp";      // Frame pointer
    public static final String RA = "$ra";      // Return address
    
    // Temporary registers (caller-saved)
    private static final String[] TEMP_REGS = {
        "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"
    };
    
    // Saved registers (callee-saved)
    private static final String[] SAVED_REGS = {
        "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7"
    };
    
    private Stack<String> availableTemps;
    private Stack<String> availableSaved;
    private Stack<String> allocatedRegs;
    
    public RegisterAllocator() {
        availableTemps = new Stack<>();
        availableSaved = new Stack<>();
        allocatedRegs = new Stack<>();
        
        // Initialize with temporary registers (in reverse order for stack)
        for (int i = TEMP_REGS.length - 1; i >= 0; i--) {
            availableTemps.push(TEMP_REGS[i]);
        }
        
        // Initialize with saved registers
        for (int i = SAVED_REGS.length - 1; i >= 0; i--) {
            availableSaved.push(SAVED_REGS[i]);
        }
    }
    
    /**
     * Allocate a temporary register
     */
    public String allocateTemp() {
        if (availableTemps.isEmpty()) {
            // If no temps available, use a saved register
            if (availableSaved.isEmpty()) {
                throw new RuntimeException("No registers available! Register spilling needed.");
            }
            String reg = availableSaved.pop();
            allocatedRegs.push(reg);
            return reg;
        }
        String reg = availableTemps.pop();
        allocatedRegs.push(reg);
        return reg;
    }
    
    /**
     * Allocate a saved register
     */
    public String allocateSaved() {
        if (availableSaved.isEmpty()) {
            throw new RuntimeException("No saved registers available!");
        }
        String reg = availableSaved.pop();
        allocatedRegs.push(reg);
        return reg;
    }
    
    /**
     * Free a register
     */
    public void freeRegister(String reg) {
        if (reg == null) return;
        
        // Remove from allocated if present
        allocatedRegs.remove(reg);
        
        // Add back to appropriate pool
        if (isTempRegister(reg)) {
            if (!availableTemps.contains(reg)) {
                availableTemps.push(reg);
            }
        } else if (isSavedRegister(reg)) {
            if (!availableSaved.contains(reg)) {
                availableSaved.push(reg);
            }
        }
    }
    
    /**
     * Free the most recently allocated register
     */
    public void freeLastAllocated() {
        if (!allocatedRegs.isEmpty()) {
            freeRegister(allocatedRegs.pop());
        }
    }
    
    /**
     * Check if a register is a temporary register
     */
    public boolean isTempRegister(String reg) {
        for (String t : TEMP_REGS) {
            if (t.equals(reg)) return true;
        }
        return false;
    }
    
    /**
     * Check if a register is a saved register
     */
    public boolean isSavedRegister(String reg) {
        for (String s : SAVED_REGS) {
            if (s.equals(reg)) return true;
        }
        return false;
    }
    
    /**
     * Get a specific register (doesn't affect allocation)
     */
    public String getRegister(int index) {
        if (index >= 0 && index < TEMP_REGS.length) {
            return TEMP_REGS[index];
        }
        return null;
    }
    
    /**
     * Reset register allocation (for new function/scope)
     */
    public void reset() {
        availableTemps.clear();
        availableSaved.clear();
        allocatedRegs.clear();
        
        for (int i = TEMP_REGS.length - 1; i >= 0; i--) {
            availableTemps.push(TEMP_REGS[i]);
        }
        for (int i = SAVED_REGS.length - 1; i >= 0; i--) {
            availableSaved.push(SAVED_REGS[i]);
        }
    }
    
    /**
     * Get the number of available temporary registers
     */
    public int getAvailableTempCount() {
        return availableTemps.size();
    }
    
    /**
     * Get the number of allocated registers
     */
    public int getAllocatedCount() {
        return allocatedRegs.size();
    }
}
