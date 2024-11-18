package si413.spl.ast;

import si413.spl.*;

/** A lambda expression, representing an unnamed function
 */
public class Lambda extends Expression {
    private String parameter;
    private Statement body;
    private Frame f;

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

    public Frame getFrame() {
        return f;
    }

    @Override
    public String astInfo() {
        return parameter;
    }

    @Override
    public String compile(Frame env, Context ctx){
        f = new Frame(env);
        return ctx.addLambda(this);
    }

    public void retroCompile(Context ctx){
        String argptr = ctx.freshRegister();
        String arg = ctx.freshRegister();
        ctx.code("%s = alloca i64".formatted(argptr));
        ctx.code("store i64 %%arg, ptr %s".formatted(argptr));
        ctx.code("%s = ptrtoint ptr %s to i64".formatted(arg, argptr));
        f.bind(parameter, arg);
        body.compile(f, ctx);
        ctx.code("ret i64 %s".formatted(f.lookup(FunCall.retName)));
    }
}
