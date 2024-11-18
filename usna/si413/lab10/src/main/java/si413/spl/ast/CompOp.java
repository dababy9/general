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

    @Override
    public String compile(Frame env, Context ctx) {
        // recursively evaluate lhs and rhs
        String lhsNum = lhsExp.compile(env, ctx);
        String rhsNum = rhsExp.compile(env, ctx);
        ctx.comment("num '%c' operation".formatted(op));
        // decide which LLVM command is needed
        String llvmCmd;
        switch(op) {
            case '<': llvmCmd = "slt"; break;
            case '>': llvmCmd = "sgt"; break;
            case '≤': llvmCmd = "sle"; break;
            case '≥': llvmCmd = "sge"; break;
            case '≠': llvmCmd = "ne"; break;
            default: llvmCmd = "eq"; break;
        }
        // do the actual command in LLVM
        String resultBool = ctx.freshRegister();
        ctx.code("%s = icmp %s i64 %s, %s".formatted(resultBool, llvmCmd, lhsNum, rhsNum));
        // turn the result back into an i64
        String resultInt = ctx.freshRegister();
        ctx.code("%s = zext i1 %s to i64".formatted(resultInt, resultBool));
        return resultInt;
    }
}
