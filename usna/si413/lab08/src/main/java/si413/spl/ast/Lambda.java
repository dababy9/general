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

    @Override
    public String astInfo() {
        return parameter;
    }

    @Override
    public int evaluate(Frame env){
        return new Closure(new Frame(env), this).id();
    }

    public Statement getBody(){
        return body;
    }

    public String getParam(){
        return parameter;
    }
}
