package si413.spl.ast;

import si413.spl.*;

/** Base class for built-in functions.
 * Implementing classes just need to write the execute(Frame) method
 * and then make sure to call bindTo with the global frame.
 */
public abstract class Builtin extends Statement {
    protected static final String argName = "argument";
    protected String name;

    protected Builtin(String name) {
        this.name = name;
    }

    /** Creates a new binding in the given frame for this built-in.
     * Works by creating a mini AST with a NewStmt and Lambda node.
     */
    public void bindTo(Frame env) {
        Lambda lambda = new Lambda(argName, this);
        NewStmt assign = new NewStmt(name, lambda);
        assign.execute(env);
    }
}
