package si413.spl.ast;

import si413.spl.*;

/** A while loop in SPL.
 * Example: while x != 10 { x := x + 1; }
 */
public class WhileStmt extends Statement {
    private Expression condExp;
    private Statement body;

    public WhileStmt(Expression condExp, Statement body) {
        this.condExp = condExp;
        this.body = body;
    }

    @Override
    public void compile(Frame env, Context ctx){
        ctx.comment("While Statement");
        // Write conditional block
        String condLabel = ctx.freshLabel();
        ctx.code("br label %%%s".formatted(condLabel));
        ctx.label(condLabel);
        String condNum = condExp.compile(env, ctx);
        String condBool = ctx.freshRegister();
        ctx.code("%s = trunc i64 %s to i1".formatted(condBool, condNum));
        // Create two labels: loop and afterloop
        String loopLabel = ctx.freshLabel();
        String afterLabel = ctx.freshLabel();
        // Conditional branch
        ctx.code("br i1 %s, label %%%s, label %%%s".formatted(condBool, loopLabel, afterLabel));
        // Write loop statement (and jump back to conditional block)
        ctx.label(loopLabel);
        body.compile(new Frame(env), ctx);
        ctx.code("br label %%%s".formatted(condLabel));
        // Write afterloop label to continue on loop completion
        ctx.label(afterLabel);
    }
}
