package si413.spl.ast;

import si413.spl.*;

/** An expression statement in SPL.
 * Example: 20;
 */
public class ExpressionStatement extends Statement {
    private Expression exp;

    public ExpressionStatement(Expression exp) {
        this.exp = exp;
    }

    @Override
    public void compile(Frame env, Context ctx){
        // Recursively evaluate right-hand side
        String expResult = exp.compile(env, ctx);
    }
}