package si413.spl.ast;

import si413.spl.*;

/** Base class for SPL expressions.
 * Every Expression must evalueate to a single value,
 * which means overriding the evaluate(env) method.
 */
public abstract class Expression extends ASTNode {
    public String compile(Frame env, Context ctx) {
        throw new UnsupportedOperationException(
            "compile(env,ctx) method not yet written for %s Expression class"
            .formatted(getClass().getSimpleName()));
    }
}
