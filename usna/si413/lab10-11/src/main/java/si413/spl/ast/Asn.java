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
        // Recursively evaluate right-hand side
        String rhsNum = rhs.compile(env, ctx);
        ctx.comment("Assignment Statment");
        // Get register name from frame, and store new value
        String var = env.lookup(varname);
        ctx.code("store i64 %s, ptr %s".formatted(rhsNum, var));
    }
}
