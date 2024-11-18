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
    public String compile(Frame env, Context ctx) {
        // recursively evaluate lhs and rhs
        String lhsInt = lhsExp.compile(env, ctx);
        String rhsInt = rhsExp.compile(env, ctx);
        ctx.comment("boolean '%s' operation".formatted(op));
        // convert lhs and rhs to i1 type
        String lhsBool = ctx.freshRegister();
        ctx.code("%s = trunc i64 %s to i1".formatted(lhsBool, lhsInt));
        String rhsBool = ctx.freshRegister();
        ctx.code("%s = trunc i64 %s to i1".formatted(rhsBool, rhsInt));
        // decide which LLVM command is needed
        String llvmCmd;
        if (op.equals("and")) llvmCmd = "and";
        else llvmCmd = "or";
        // do the actual command in LLVM
        String resultBool = ctx.freshRegister();
        ctx.code("%s = %s i1 %s, %s".formatted(resultBool, llvmCmd, lhsBool, rhsBool));
        // turn the result back into an i64
        String resultInt = ctx.freshRegister();
        ctx.code("%s = zext i1 %s to i64".formatted(resultInt, resultBool));
        return resultInt;
    }
}
