package si413.spl.ast;

import si413.spl.*;

/** A new instance of a class in SPL.
 * Example: SomeClass!;
 */

public class NewClass extends Expression {
    private Expression lhs;

    public NewClass(Expression lhs) {
        this.lhs = lhs;
    }

    @Override
    public Value evaluate(Frame env) {
        ClassClosure cc = lhs.evaluate(env).getCC();
        if(cc == null) return Value.unset();
        Frame objFrame = new Frame(cc.getEnv());
        for(Statement s : cc.getCls().getBody().getChildren()){
            s.execute(objFrame);
        }
        return Value.fromInstance(new Instance(objFrame));
    }
}
