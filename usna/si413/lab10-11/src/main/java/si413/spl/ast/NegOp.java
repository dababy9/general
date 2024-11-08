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
    public String compile(Frame env, Context ctx){
        // Recursively evaluate right-hand side
        String rhsNum = rhs.compile(env, ctx);
        ctx.comment("NegOp");
        // Perform multiplication by -1
        String resultNum = ctx.freshRegister();
        ctx.code("%s = mul i64 %s, -1".formatted(resultNum, rhsNum));
        return resultNum;
    }
}
