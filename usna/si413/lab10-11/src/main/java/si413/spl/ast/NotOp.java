package si413.spl.ast;

import si413.spl.*;

/** A boolean negation in SPL.
 * Example: not true
 */
public class NotOp extends Expression {
    private Expression rhs;

    public NotOp(Expression rhs) {
        this.rhs = rhs;
    }

    @Override
    public String compile(Frame env, Context ctx) {
        // Recursively evaluate right-hand side
        String rhsInt = rhs.compile(env, ctx);
        ctx.comment("NotOp");
        // Convert to bool
        String rhsBool = ctx.freshRegister();
        ctx.code("%s = trunc i64 %s to i1".formatted(rhsBool, rhsInt));
        // Perform logical not as xor(boolValue, true)
        String resultBool = ctx.freshRegister();
        ctx.code("%s = xor i1 %s, true".formatted(resultBool, rhsBool));
        // Convert result back to int
        String resultInt = ctx.freshRegister();
        ctx.code("%s = zext i1 %s to i64".formatted(resultInt, resultBool));
        return resultInt;
    }
}
