package si413.spl.ast;

import si413.spl.*;

/** A boolean negation in SPL.
 * Example: not true
 */
public class NotOp extends Expression {
    private Expression rhs;

    public NotOp(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public Value evaluate(Frame env) {
        boolean rhsval = rhs.evaluate(env).getBool();
        return Value.fromBool(! rhsval);
    }
}
