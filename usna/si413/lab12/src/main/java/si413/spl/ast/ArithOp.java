package si413.spl.ast;

import si413.spl.*;

/** Any math operation between two numbers that returns a number.
 * Example: x + 17;
 * Addition, subtraction, multiplication, and division.
 */
public class ArithOp extends Expression {
    private Expression lhsExp;
    private Expression rhsExp;
    private char op;

    public ArithOp(Expression lhsExp, String op, Expression rhsExp) {
        this.lhsExp = lhsExp;
        this.rhsExp = rhsExp;
        this.op = op.charAt(0);
    }

    @Override
    public String astInfo() {
        return String.valueOf(op);
    }

    @Override
    public Value evaluate(Frame env) {
        int lhsVal = lhsExp.evaluate(env).getNum();
        int rhsVal = rhsExp.evaluate(env).getNum();
        int result;
        switch (op) {
            case '+':
                result = lhsVal + rhsVal;
                break;
            case '-':
                result = lhsVal - rhsVal;
                break;
            case '*':
                result = lhsVal * rhsVal;
                break;
            case '/':
                if (rhsVal == 0) {
                    Interpreter.current().error("Divide by 0");
                    result = 0;
                }
                else result = lhsVal / rhsVal;
                break;
            default:
                throw new Error("Invalid ArithOp operator '%c'".formatted(op));
        }
        return Value.fromNum(result);
    }
}
