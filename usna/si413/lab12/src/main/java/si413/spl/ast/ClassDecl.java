package si413.spl.ast;

import si413.spl.*;

/** A class declaration for OOP in SPL.
 */
public class ClassDecl extends Expression {
    private Statement body;
    private String superName = null;

    public ClassDecl(Statement body) {
        this.body = body;
    }

    public ClassDecl(String superName, Statement body) {
        this.superName = superName;
        this.body = body;
    }

    public Block getBody() {
        return (Block)body;
    }

    @Override
    public Value evaluate(Frame env) {
        if(superName != null) {
            ClassClosure cc = env.lookup(superName).getCC();
            return Value.fromCC(new ClassClosure(this, cc.getCls(), env, cc.getEnv()));
        }
        return Value.fromCC(new ClassClosure(this, env));
    }
}
