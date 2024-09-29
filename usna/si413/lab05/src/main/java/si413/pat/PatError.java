package si413.pat;

/** Exception class for syntax and run-time errors in the Pat language.
 *
 * Throw an instance this class for scanner errors, parser errors, and undefined
 * variables in the interpreter.
 *
 * You should not need to modify this class.
 */
public class PatError extends RuntimeException {
    private static final long serialVersionUID = 2816560820728592125L;

    public PatError(String message) {
        super(message);
    }
}
