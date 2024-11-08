package si413.spl.ast;

import si413.spl.*;

/** Base class for SPL statements.
 * Statements can be executed but have no return value.
 */
public abstract class Statement extends ASTNode {
    public void compile(Frame env, Context ctx) {
        throw new UnsupportedOperationException(
            "compile(env,ctx) method not yet written for %s Statement class"
            .formatted(getClass().getSimpleName()));
    }
}
