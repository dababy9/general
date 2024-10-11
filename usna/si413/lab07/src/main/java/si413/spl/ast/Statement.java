package si413.spl.ast;

import si413.spl.Frame;

/** Base class for SPL statements.
 * Statements can be executed but have no return value.
 */
public abstract class Statement extends ASTNode {
    public void execute() {
        throw new UnsupportedOperationException(
            "execute() method not yet written for %s class"
            .formatted(getClass().getSimpleName()));
    }
}
