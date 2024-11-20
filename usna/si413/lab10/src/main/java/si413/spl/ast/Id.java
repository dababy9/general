package si413.spl.ast;

import si413.spl.*;

/** A variable name used in an Expression context.
 */
public class Id extends Expression {
    private String varname;

    public Id(String varname) {
        this.varname = varname;
    }

    @Override
    public String astInfo() {
        return varname;
    }

    @Override
    public String compile(Frame env, Context ctx){
        // Get register name from frame and load memory into new register
        String num = env.lookup(varname);
        String ptr = ctx.freshRegister();
        ctx.code("%s = inttoptr i64 %s to ptr".formatted(ptr, num));
        String resultInt = ctx.freshRegister();
        ctx.code("%s = load i64, ptr %s".formatted(resultInt, ptr));
        return resultInt;
    }
}
