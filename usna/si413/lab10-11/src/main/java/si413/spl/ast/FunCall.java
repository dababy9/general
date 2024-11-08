package si413.spl.ast;

import si413.spl.*;

/** A function call in SPL, as in an expression with the '@' symbol.
 */
public class FunCall extends Expression {
    private Expression funExp;
    private Expression argExp;
    public static String retName = "ret";

    public FunCall(Expression lhs, Expression rhs) {
        funExp = lhs;
        argExp = rhs;
    }
}
