import java.io.IOException;

/** A lexer (a.k.a. scanner) for the calculator language.
 * The job of the lexer is to read in some source code
 * and give access to a stream of resulting tokens.
 */
public abstract class CalcLexer {
    /** Reads and returns the next token from the source.  */
    protected abstract CalcToken readToken() throws IOException;

    /** The saved next token, or null if not yet read. */
    protected CalcToken saved = null;

    /** Returns the next token without consuming it. */
    public CalcToken peek() throws IOException {
        if (saved == null) saved = readToken();
        return saved;
    }

    /** Returns and consumes (moves past) the next token. */
    public CalcToken match() throws IOException {
        CalcToken result = peek();
        saved = null;
        return result;
    }

    /** Returns and consumes (moves past) the next token,
     * but only if it has the given type.
     * @param expected The expected type the next token must have.
     * @throws IOException if the next token does not match expected type.
     */
    public CalcToken match(CalcToken.Type expected) throws IOException {
        CalcToken.Type nextType = peek().getType();
        if (nextType.equals(expected)) return match();
        else throw new IOException(
            "CalcLexer mismatch: expected %s, got %s%n"
            .formatted(expected, nextType));
    }
}
