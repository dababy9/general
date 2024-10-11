package si413.spl.ast;

import si413.spl.Frame;

/** A negation operation in SPL.
 * Example: -x
 */
public class NegOp extends Expression {
    private Expression rhs;

    public NegOp(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public int evaluate() {
        return rhs.evaluate() * -1;
    }
}
