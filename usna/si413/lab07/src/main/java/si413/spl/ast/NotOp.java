package si413.spl.ast;

import si413.spl.Frame;

/** A boolean negation in SPL.
 * Example: not true
 */
public class NotOp extends Expression {
    private Expression rhs;

    public NotOp(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public int evaluate() {
        boolean rhsval = (rhs.evaluate() != 0);
        if (rhsval) return 0; // true becomes false
        else return 1; // false becomes true
    }
}
