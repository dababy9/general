package si413.spl;

import java.io.PrintWriter;
import si413.spl.ast.*;

/** This represents the context of the current compilation.
 * Think of it as a place to share information such as counters or lists
 * that need to be shared across all AST nodes in the same program
 * for the compiler to work correctly.
 *
 * Feel free to add your own things to this class!
 */
public class Context {
    private PrintWriter codeOut;
    private int nextReg = 1;
    private int nextLabel = 1;

    public Context(PrintWriter codeOut) {
        this.codeOut = codeOut;
    }

    /** Returns the name of an as-yet-unused register.
     * Starts at %r001 and just counts up for there based on the
     * internal counter nextReg.
     * Notice that the returned string DOES include the % sign.
     */
    public String freshRegister() {
        return "%%r%03d".formatted(nextReg++);
    }

    /** Writes a line of code to the output file. */
    public void code(String line) {
        codeOut.format("  %s%n", line);
    }

    /** Writes a comment line to the output file. */
    public void comment(String line) {
        codeOut.format("    ; %s%n", line);
    }

    /** Returns the name of an as-yet-unused label.
     * Starts at b0 and just counts up from there based on the
     * internal counter nextLabel.
     */
    public String freshLabel() {
        return "b%d".formatted(nextLabel++);
    }

    /** Writes a label to the output file */
    public void label(String line) {
        codeOut.format("%s:%n", line);
    }

    public String addLambda(Lambda l){
        String funName = "@fun%d".formatted(l.getId());
        String register = freshRegister();
        code("%s = ptrtoint ptr %s to i64".formatted(register, funName));
        return register;
    }
}
