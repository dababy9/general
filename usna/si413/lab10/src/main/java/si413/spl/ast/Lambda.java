package si413.spl.ast;

import si413.spl.*;
import java.util.TreeMap;

/** A lambda expression, representing an unnamed function
 */
public class Lambda extends Expression {
    private String parameter;
    private Statement body;
    private Frame f;
    private int id;

    public static TreeMap<Integer, Lambda> lambdas = new TreeMap<>();
    private static int counter = 1;

    public Lambda(String parameter, Statement body) {
        this.parameter = parameter;
        this.body = body;
        this.id = counter++;
        lambdas.put(this.id, this);
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

    public int getId() {
        return id;
    }

    @Override
    public String astInfo() {
        return parameter;
    }

    @Override
    public String compile(Frame env, Context ctx) {
        f = new Frame(env);
        return ctx.addLambda(this);
    }

    public void retroCompile(Context ctx) {
        String argptr = ctx.freshRegister();
        ctx.code("%s = alloca i64".formatted(argptr));
        ctx.code("store i64 %%arg, ptr %s".formatted(argptr));
        String arg = ctx.freshRegister();
        ctx.code("%s = ptrtoint ptr %s to i64".formatted(arg, argptr));
        f.bind(parameter, arg);
        new NewStmt(FunCall.retName, new Num(0)).compile(f, ctx);
        body.compile(f, ctx);
        String retptr = ctx.freshRegister();
        String ret = ctx.freshRegister();
        ctx.code("%s = inttoptr i64 %s to ptr".formatted(retptr, f.lookup(FunCall.retName)));
        ctx.code("%s = load i64, ptr %s".formatted(ret, retptr));
        ctx.code("ret i64 %s".formatted(ret));
    }
}
