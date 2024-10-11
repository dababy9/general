package si413.spl.ast;

import si413.spl.Frame;
import si413.spl.Interpreter;

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
    public int evaluate() {
        int lhs = lhsExp.evaluate();
        int rhs = rhsExp.evaluate();
        switch(op) {
            case '+':
                return lhs + rhs;
            case '-':
                return lhs - rhs;
            case '*':
                return lhs * rhs;
            default:
                if(rhs == 0)
                    Interpreter.current().error("ERROR: Divide by zero");
                return lhs / rhs;
        }
    }
}
