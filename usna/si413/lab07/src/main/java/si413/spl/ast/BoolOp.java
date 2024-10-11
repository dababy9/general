package si413.spl.ast;

import si413.spl.Frame;

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
    public int evaluate() {
        boolean lhs = (lhsExp.evaluate() != 0);

        if(op.equals("and") && !lhs) return 0;
        if(op.equals("or") && lhs) return 1;
        
        boolean rhs = (rhsExp.evaluate() != 0);

        boolean result;
        if (op.equals("and")) result = lhs && rhs;
        else result = lhs || rhs;

        if (result) return 1;
        else return 0;
    }
}
