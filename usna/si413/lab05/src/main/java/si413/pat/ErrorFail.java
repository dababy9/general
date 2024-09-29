package si413.pat;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

/** A specialized error handler for ANTLR syntax errors.
 *
 * The default in ANTLR is to "try" to ignore syntax errors and
 * automatically recover from them. That might be nice for the Pat
 * programmer but not for us as scanner/grammar/interpreter writers!
 *
 * Instead, this class immediately throws an instance of the PatError
 * class on any encountered syntax error.
 *
 * You should not need to make any changes to this class.
 */
public class ErrorFail extends BaseErrorListener {
    /** This is the exception that will be thrown if an EOF occurs.
     * Having this possibility is useful for the "fancy" interpreter
     * where we need to allow the user to keep typing across multiple
     * lines.
     * If this is null, then there is no special handling of EOFs.
     */
    private RuntimeException eofError = null;

    /** Creates a new ANTLR error handler with no special EOF handling.
     * This version will immediately throw a PatError for any syntax
     * error encountered, including EOF.
     */
    public ErrorFail() { }

    /** Creates a new ANTLR error handler that handles EOFs speially.
     * This version throws the given error when an EOF is encountered,
     * and otherwise throws an instance of PatError on any other kind
     * of syntax error.
     * @param eofError The exception instance to throw when an EOF is encountered.
     */
    public ErrorFail(RuntimeException eofError) {
        this.eofError = eofError;
    }

    /** Called by ANTLR scanner or parser when a syntax error occurs. */
    @Override
    public void syntaxError(Recognizer<?,?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e)
    {
        if (eofError != null && (offendingSymbol instanceof Token)
            && (((Token)offendingSymbol).getType() == Token.EOF))
        {
            throw eofError;
        }
        else throw new PatError("ERROR in %s on line %d column %d: %s%n".formatted(
            recognizer.getClass().getSimpleName(),
            line,
            charPositionInLine,
            msg));
    }

    /** Adds this instance as the sole error handler for the given scanner or parser.
     * @param recognizer an ANTLR parser or scanner instance.
     */
    public void attach(Recognizer<?,?> recognizer) {
        recognizer.removeErrorListeners();
        recognizer.addErrorListener(this);
    }
}
