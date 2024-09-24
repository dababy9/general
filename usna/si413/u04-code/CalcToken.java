/** A single token in the calculator language.
 * This stores the token type as well as the literal text string
 * that matched. (We need that literal text string to, e.g.,
 * extract the numeric value of an NUM token.)
 */
public class CalcToken {
    /** Names of the different token types.
     * If you haven't seen enums in Java before, think of these
     * as fixed int constants. It's just more convenient and
     * clear to use CalcToken.Type.NUM, CalcToken.Type.OPA, etc.,
     * rather than making up our own constants like 1, 2, 3.
     */
    public enum Type {
        /** Plus/minus operator. */
        OPA,
        /** Mult/div operator. */
        OPM,
        /** An integer. */
        NUM,
        /** Left parenthesis. */
        LP,
        /** Right parenthesis. */
        RP,
        /** Semicolon. */
        STOP,
        /** End of the input. */
        EOF
    }

    private Type type;
    private String text;

    /** Creates a new token with the given type and text string.
     * Note that there is no actual checking here - that is up to
     * the actual Lexer class.
     */
    public CalcToken(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    /** Getter for the token type, e.g. OPA or NUM. */
    public Type getType() {
        return type;
    }

    /** Getter for the matching text, e.g. "+" or "42". */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "%s '%s'".formatted(type, text);
    }
}
