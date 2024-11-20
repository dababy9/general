package si413.spl.ast;

import si413.spl.*;

/** A function call in SPL, as in an expression with the '@' symbol.
 */
public class FunCall extends Expression {
    private Expression funExp;
    private Expression argExp;
    public static String retName = "ret";

    public FunCall(Expression lhs, Expression rhs) {
        funExp = lhs;
        argExp = rhs;
    }

    public String compile(Frame env, Context ctx){
        // Get pointer to function and save in register
        String ptr = ctx.freshRegister();
        String num = funExp.compile(env, ctx);
        ctx.code("%s = inttoptr i64 %s to ptr".formatted(ptr, num));
        // Call function and return the result
        String result = ctx.freshRegister();
        String arg = argExp.compile(env, ctx);
        ctx.code("%s = call i64 %s(i64 %s)".formatted(result, ptr, arg));
        return result;
    }
}
