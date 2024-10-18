package si413.spl.ast;

import si413.spl.*;

/** A function call in SPL, as in an expression with the '@' symbol.
 */
public class FunCall extends Expression {
    private Expression funExp;
    private Expression argExp;

    public FunCall(Expression lhs, Expression rhs) {
        funExp = lhs;
        argExp = rhs;
    }

    @Override
    public int evaluate(Frame env){
        Closure c = Closure.fromId(funExp.evaluate(env));
        Frame f = new Frame(c.getFrame());
        Lambda l = c.getFunc();
        f.bind(l.getParam(), argExp.evaluate(env));
        f.bind("ret", 0);
        l.getBody().execute(f);
        return f.lookup("ret");
    }
}
