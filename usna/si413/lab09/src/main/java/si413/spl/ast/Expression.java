package si413.spl.ast;

import si413.spl.*;

/** Base class for SPL expressions.
 * Every Expression must evalueate to a single value,
 * which means overriding the evaluate(env) method.
 */
public abstract class Expression extends ASTNode {
    public Value evaluate(Frame env) {
        throw new UnsupportedOperationException(
            "evaluate(env) method not yet written for %s class"
            .formatted(getClass().getSimpleName()));
    }
}
