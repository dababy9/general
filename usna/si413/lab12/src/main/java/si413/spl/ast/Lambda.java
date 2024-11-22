package si413.spl.ast;

import si413.spl.*;

/** A lambda expression, representing an unnamed function
 */
public class Lambda extends Expression {
    private String parameter;
    private Statement body;

    public Lambda(String parameter, Statement body) {
        this.parameter = parameter;
        this.body = body;
    }

    public String getParameter() {
        return parameter;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public String astInfo() {
        return parameter;
    }

    @Override
    public Value evaluate(Frame env) {
        return Value.fromFun(new Closure(this, env));
    }
}
