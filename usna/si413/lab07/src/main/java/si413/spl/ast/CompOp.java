package si413.spl.ast;

import si413.spl.Frame;

/** A comparison between two numbers that returns a boolean.
 * Example: x != 21
 */
public class CompOp extends Expression {
    private Expression lhsExp;
    private Expression rhsExp;
    private char op;

    public CompOp(Expression lhsExp, String op, Expression rhsExp) {
        this.lhsExp = lhsExp;
        this.rhsExp = rhsExp;
        if (op.length() == 1) this.op = op.charAt(0);
        else switch (op.charAt(0)) {
            case '!':
                this.op = '≠';
                break;
            case '<':
                this.op = '≤';
                break;
            case '>':
                this.op = '≥';
                break;
            default:
                throw new RuntimeException("illegal comparison operator: " + op);
        }
    }

    @Override
    public String astInfo() {
        switch (op) {
            case '≠': return "!=";
            case '≤': return "<=";
            case '≥': return ">=";
            default: return String.valueOf(op);
        }
    }

    @Override
    public int evaluate() {
        int lhs = lhsExp.evaluate();
        int rhs = rhsExp.evaluate();
        switch(op) {
            case '<':
                return (lhs < rhs ? 1 : 0);
            case '≤':
                return (lhs <= rhs ? 1 : 0);
            case '>':
                return (lhs > rhs ? 1 : 0);
            case '≥':
                return (lhs >= rhs ? 1 : 0);
            case '≠':
                return (lhs != rhs ? 1 : 0);
            default:
                return (lhs == rhs ? 1 : 0);
        }
    }
}
