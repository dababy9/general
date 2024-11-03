package si413.spl.ast;

import si413.spl.*;

/** A negation operation in SPL.
 * Example: -x
 */
public class NegOp extends Expression {
    private Expression rhs;

    public NegOp(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public Value evaluate(Frame env) {
        int rhsVal = rhs.evaluate(env).getNum();
        return Value.fromNum(-rhsVal);
    }
}
