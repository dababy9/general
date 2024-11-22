package si413.spl.ast;

import si413.spl.*;

/** A class declaration for OOP in SPL.
 */
public class ClassDecl extends Expression {
    private Statement body;

    public ClassDecl(Statement body) {
        this.body = body;
    }

    public Block getBody() {
        return (Block)body;
    }

    @Override
    public Value evaluate(Frame env) {
        return Value.fromCC(new ClassClosure(this, env));
    }
}
