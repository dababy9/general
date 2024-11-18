package si413.spl.ast;

import si413.spl.*;

/** A variable declaration (binding) and assignment.
 * Example: new x := 10;
 */
public class NewStmt extends Statement {
    private String varname;
    private Expression rhs;

    public NewStmt(String varname, Expression rhs) {
        this.varname = varname;
        this.rhs = rhs;
    }

    @Override
    public String astInfo() {
        return varname;
    }

    @Override
    public void compile(Frame env, Context ctx){
        ctx.comment("Begin New Statment");
        // Recursively evaluate right-hand side
        String rhsNum = rhs.compile(env, ctx);
        // Create new pointer and allocate memory for it
        String var = ctx.freshRegister();
        ctx.code("%s = alloca i64".formatted(var));
        ctx.code("store i64 %s, ptr %s".formatted(rhsNum, var));
        String num = ctx.freshRegister();
        ctx.code("%s = ptrtoint ptr %s to i64".formatted(num, var));
        ctx.comment("End New Statement");
        // Bind register name to frame
        env.bind(varname, num);
    }
}
