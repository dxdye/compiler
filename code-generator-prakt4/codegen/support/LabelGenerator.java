package support;

/**
 * Label generator for MIPS assembly.
 * Generates unique labels for control flow and data.
 */
public class LabelGenerator {
    private int labelCounter = 0;
    private int stringCounter = 0;
    private int ifCounter = 0;
    private int whileCounter = 0;
    private int switchCounter = 0;
    private int orCounter = 0;
    private int andCounter = 0;
    
    /**
     * Generate a new generic label
     */
    public String newLabel() {
        return "L" + (labelCounter++);
    }
    
    /**
     * Generate a new label with prefix
     */
    public String newLabel(String prefix) {
        return prefix + "_" + (labelCounter++);
    }
    
    /**
     * Generate a label for string literals
     */
    public String newStringLabel() {
        return "_str" + (stringCounter++);
    }
    
    /**
     * Generate labels for if statement
     */
    public String[] newIfLabels() {
        int id = ifCounter++;
        return new String[] {
            "if_else_" + id,
            "if_end_" + id
        };
    }
    
    /**
     * Generate labels for if-else statement
     */
    public String[] newIfElseLabels() {
        int id = ifCounter++;
        return new String[] {
            "if_else_" + id,
            "if_end_" + id
        };
    }
    
    /**
     * Generate labels for while loop
     */
    public String[] newWhileLabels() {
        int id = whileCounter++;
        return new String[] {
            "while_start_" + id,
            "while_end_" + id
        };
    }
    
    /**
     * Generate labels for do-while loop
     */
    public String[] newDoWhileLabels() {
        int id = whileCounter++;
        return new String[] {
            "do_start_" + id,
            "do_cond_" + id,
            "do_end_" + id
        };
    }
    
    /**
     * Generate labels for switch statement
     */
    public String[] newSwitchLabels(int numCases) {
        int id = switchCounter++;
        String[] labels = new String[numCases + 2]; // cases + default + end
        for (int i = 0; i < numCases; i++) {
            labels[i] = "switch_case_" + id + "_" + i;
        }
        labels[numCases] = "switch_default_" + id;
        labels[numCases + 1] = "switch_end_" + id;
        return labels;
    }
    
    /**
     * Generate labels for logical OR (short-circuit)
     */
    public String[] newOrLabels() {
        int id = orCounter++;
        return new String[] {
            "or_true_" + id,
            "or_end_" + id
        };
    }
    
    /**
     * Generate labels for logical AND (short-circuit)
     */
    public String[] newAndLabels() {
        int id = andCounter++;
        return new String[] {
            "and_false_" + id,
            "and_end_" + id
        };
    }
    
    /**
     * Generate method label
     */
    public String getMethodLabel(String methodName) {
        if (methodName.equals("main")) {
            return "main";
        }
        return "_method_" + methodName;
    }
    
    /**
     * Reset all counters
     */
    public void reset() {
        labelCounter = 0;
        stringCounter = 0;
        ifCounter = 0;
        whileCounter = 0;
        switchCounter = 0;
        orCounter = 0;
        andCounter = 0;
    }
}
