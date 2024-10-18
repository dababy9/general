package si413.spl.ast;

import si413.spl.*;

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

    public int evaluate(Frame env) {
        int lhsVal = lhsExp.evaluate(env);
        int rhsVal = rhsExp.evaluate(env);
        boolean result;
        switch (op) {
            case '<':
                result = lhsVal < rhsVal;
                break;
            case '≤':
                result = lhsVal <= rhsVal;
                break;
            case '=':
                result = lhsVal == rhsVal;
                break;
            case '>':
                result = lhsVal > rhsVal;
                break;
            case '≥':
                result = lhsVal >= rhsVal;
                break;
            case '≠':
                result = lhsVal != rhsVal;
                break;
            default:
                throw new Error("Invalid CompOp operator '%c'".formatted(op));
        }
        if (result) return 1;
        else return 0;
    }
}
