package support;

// Errors
//
// This class is used to generate warning and fatal error messages.

public class Errors {
    public static void fatal(int lineNum, int charNum, String msg) {
        System.err.println(lineNum + ":" + charNum + " **ERROR** " + msg);
    }

    public static void warn(int lineNum, int charNum, String msg) {
        System.err.println(lineNum + ":" + charNum + " **WARNING** " + msg);
    }
}
