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
    public String compile(Frame env, Context ctx){
        // Recursively evaluate lhs and rhs
        String lhsNum = lhsExp.compile(env, ctx);
        String rhsNum = rhsExp.compile(env, ctx);
        // Decide which LLVM command is needed
        String llvmCmd;
        switch(op) {
            case '+': llvmCmd = "add"; break;
            case '-': llvmCmd = "sub"; break;
            case '*': llvmCmd = "mul"; break;
            default: llvmCmd = "sdiv"; break;
        }
        // Do the actual command in LLVM
        String resultNum = ctx.freshRegister();
        ctx.code("%s = %s i64 %s, %s".formatted(resultNum, llvmCmd, lhsNum, rhsNum));
        return resultNum;
    }
}
