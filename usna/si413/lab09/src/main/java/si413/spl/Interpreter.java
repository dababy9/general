package si413.spl;

/** Abstract base class for any SPL interpreter.
 * The role of this class is to serve as the common "environment" for
 * when an AST node has to interact with the user, like reading or
 * writing values to the screen.
 */
public abstract class Interpreter implements Runnable {
    private static Interpreter currentInterp = null;
    private Frame globalFrame = null;

    /** Base class constructor.
     * This constructor creates the global frame and
     * saves the current Interpreter instance. */
    protected Interpreter() {
        currentInterp = this;
        globalFrame = Frame.makeGlobal();
    }

    /** Returns the current running Interpreter instance. */
    public static Interpreter current() {
        return currentInterp;
    }

    /** Returns the global frame for this interpreter. */
    public Frame getGlobal() {
        return globalFrame;
    }

    /** Reads an SPL value (must be a number in fact) from the user. */
    public abstract int read();

    /** Writes an SPL value to the user in a running program. */
    public abstract void write(Object value);

    /** Indicates that an error has occurred.
     * Note, it is up to the interpreter to decide what to do.
     * For example, some interpreters may halt immediately, where
     * some might let the user continue trying to run new SPL statements.
     */
    public abstract void error(String message);
}
