package si413.spl.ast;

import si413.spl.*;

/** An operation between two booleans that returns another boolean.
 * Example: true | false;
 */
public class BoolOp extends Expression {
    private Expression lhsExp;
    private Expression rhsExp;
    private String op;

    public BoolOp(Expression lhsExp, String op, Expression rhsExp) {
        this.lhsExp = lhsExp;
        this.rhsExp = rhsExp;
        this.op = op;
    }

    @Override
    public String astInfo() {
        return op;
    }

    @Override
    public int evaluate(Frame env) {
        boolean lhs = (lhsExp.evaluate(env) != 0);

        boolean result;
        // short-circuit conditions
        if ((lhs && op.equals("or")) || (!lhs && op.equals("and"))) result = lhs;
        else {
            boolean rhs = (rhsExp.evaluate(env) != 0);
            result = rhs;
        }

        if (result) return 1;
        else return 0;
    }
}
