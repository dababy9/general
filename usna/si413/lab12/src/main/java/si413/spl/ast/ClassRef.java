package si413.spl.ast;

import si413.spl.*;

/** A reference to a class field or method.
 * Example: SomeObject ! field;
 */
public class ClassRef extends Expression {
    private Expression lhs;
    private String name;

    public ClassRef(Expression lhs, String name) {
        this.lhs = lhs;
        this.name = name;
    }

    @Override
    public Value evaluate(Frame env) {
        Instance obj = lhs.evaluate(env).getObj();
        Frame objFrame = obj.getEnv();
        return objFrame.lookup(name);
    }
}
