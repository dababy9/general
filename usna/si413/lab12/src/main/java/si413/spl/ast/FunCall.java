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

    @Override
    public Value evaluate(Frame env) {
        Closure fun = funExp.evaluate(env).getFun();
        if (fun == null) return Value.unset();
        Value arg = argExp.evaluate(env);
        Frame funFrame = new Frame(fun.getEnv());
        Lambda lam = fun.getFunc();
        funFrame.bind(lam.getParameter(), arg);
        funFrame.bind(retName, Value.unset());
        fun.getFunc().getBody().execute(funFrame);
        return funFrame.lookup(retName);
    }
}
