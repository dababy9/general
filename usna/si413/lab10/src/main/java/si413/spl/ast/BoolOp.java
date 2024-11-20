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
        // Break into block immediately (for phi function later)
        String startLabel = ctx.freshLabel();
        ctx.code("br label %%%s".formatted(startLabel));
        ctx.label(startLabel);
        // Recursively evaluate lhs and convert to i1
        String lhsInt = lhsExp.compile(env, ctx);
        String lhsBool = ctx.freshRegister();
        ctx.code("%s = trunc i64 %s to i1".formatted(lhsBool, lhsInt));
        // Break to a label depending on lhs value and bool op
        String noSCLabel = ctx.freshLabel();
        String afterLabel = ctx.freshLabel();
        boolean isOr = op.equals("or");
        ctx.code("br i1 %s, label %%%s, label %%%s".formatted(lhsBool, (isOr ? afterLabel : noSCLabel), (isOr ? noSCLabel : afterLabel)));
        // Code NO short-circuit section (evaluate rhs)
        ctx.label(noSCLabel);
        // Evaluate rhs
        String rhsInt = rhsExp.compile(env, ctx);
        String rhsBool = ctx.freshRegister();
        ctx.code("%s = trunc i64 %s to i1".formatted(rhsBool, rhsInt));
        // Perform actual boolean operation
        String resultBool = ctx.freshRegister();
        ctx.code("%s = %s i1 %s, %s".formatted(resultBool, (isOr ? "or" : "and"), lhsBool, rhsBool));
        // Break to next block
        ctx.code("br label %%%s".formatted(afterLabel));
        // Code after label
        ctx.label(afterLabel);
        // Get either just lhs or full result with phi function
        String phi = ctx.freshRegister();
        ctx.code("%s = phi i1 [%s, %%%s], [%s, %%%s]".formatted(phi, lhsBool, startLabel, resultBool, noSCLabel));
        // Turn the result back into an i64
        String resultInt = ctx.freshRegister();
        ctx.code("%s = zext i1 %s to i64".formatted(resultInt, phi));
        return resultInt;
    }
}
