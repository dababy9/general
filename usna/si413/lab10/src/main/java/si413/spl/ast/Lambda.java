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
        id = counter++;
        lambdas.put(id, this);
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
        // Save new frame for lambda
        f = new Frame(env);
        // Save function pointer to register and return it
        String register = ctx.freshRegister();
        ctx.code("%s = ptrtoint ptr @fun%d to i64".formatted(register, id));
        return register;
    }

    public void retroCompile(Context ctx) {
        // Store argument value on stack like a variable
        String argptr = ctx.freshRegister();
        ctx.code("%s = alloca i64".formatted(argptr));
        ctx.code("store i64 %%arg, ptr %s".formatted(argptr));
        String arg = ctx.freshRegister();
        ctx.code("%s = ptrtoint ptr %s to i64".formatted(arg, argptr));
        // Bind the parameter name to the variable in the new frame
        f.bind(parameter, arg);
        // Just generate and compile a NewStmt for 'ret' variable
        new NewStmt(FunCall.retName, new Num(0)).compile(f, ctx);
        // Compile the body of the function within the new frame
        body.compile(f, ctx);
        // At the end, lookup the value in 'ret' and return it
        String retptr = ctx.freshRegister();
        String ret = ctx.freshRegister();
        ctx.code("%s = inttoptr i64 %s to ptr".formatted(retptr, f.lookup(FunCall.retName)));
        ctx.code("%s = load i64, ptr %s".formatted(ret, retptr));
        ctx.code("ret i64 %s".formatted(ret));
    }
}
