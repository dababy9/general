package si413.spl.ast;

import si413.spl.*;

/** A single literal number in an SPL program.
 * Example: 21
 */
public class Num extends Expression {
    private int value;

    public Num(int value) {
        this.value = value;
    }

    @Override
    public String astInfo() {
        return String.valueOf(value);
    }

    @Override
    public Value evaluate(Frame env) {
        return Value.fromNum(value);
    }
}
