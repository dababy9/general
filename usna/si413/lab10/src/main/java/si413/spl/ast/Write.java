package si413.spl.ast;

import si413.spl.*;

/** A write statement in SPL.
 * Example: write 15;
 */
public class Write extends Statement {
    private Expression exp;

    public Write(Expression exp) {
        this.exp = exp;
    }

    @Override
    public void compile(Frame env, Context ctx) {
        // evaluate child
        String childVal = exp.compile(env, ctx);
        ctx.comment("write statement");
        // call printf
        ctx.code("call i32 @printf(ptr @printLong, i64 %s)".formatted(childVal));
    }
}
