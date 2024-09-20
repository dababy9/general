/** Exception class for syntax and run-time errors in the Pat language.
 * Throw an instance this class for scanner errors, parser errors, and undefined
 * variables in the interpreter.
 */
class PatError extends Exception {
    private static final long serialVersionUID = 2816560820728592125L;

    public PatError(String message) {
        super(message);
    }
}
