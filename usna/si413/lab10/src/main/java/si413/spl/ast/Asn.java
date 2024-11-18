package si413.spl.ast;

import si413.spl.*;

/** An re-assignment operation in SPL.
 * Example: x := 21;
 */
public class Asn extends Statement {
    private String varname;
    private Expression rhs;

    public Asn(String varname, Expression rhs) {
        this.varname = varname;
        this.rhs = rhs;
    }

    @Override
    public String astInfo() {
        return varname;
    }

    @Override
    public void compile(Frame env, Context ctx){
        ctx.comment("Begin Assignment Statment");
        // Recursively evaluate right-hand side
        String rhsNum = rhs.compile(env, ctx);
        // Get register name from frame, and store new value
        String var = env.lookup(varname);
        String ptr = ctx.freshRegister();
        ctx.code("%s = inttoptr i64 %s to ptr".formatted(ptr, var));
        ctx.code("store i64 %s, ptr %s".formatted(rhsNum, ptr));
        ctx.comment("End Assignment Statement");
    }
}
