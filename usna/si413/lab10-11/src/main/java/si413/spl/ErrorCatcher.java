package si413.spl;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Lexer;

/** Helper class to identify and properly handle SPL scanner and parser errors.
 */
public class ErrorCatcher extends BaseErrorListener {
    private boolean error = false;
    private boolean eof = false;
    private boolean ignoreEof;

    /** Creates a new error handler.
     * @param ignoreEof if true, then EOF errors (only) are ignored.
     * Ignoring EOF errors makes it possible to handle multi-line input
     * in the FancyInterpreter.
     */
    public ErrorCatcher(boolean ignoreEof) {
        this.ignoreEof = ignoreEof;
    }

    /** Creates a new error handler which does not ignore any errors. */
    public ErrorCatcher() {
        this(false);
    }

    /** Handles an actual syntax error from ANTLR during scanning or parsing.
     */
    @Override
    public void syntaxError(
        Recognizer<?, ?> recognizer,
        Object offendingSymbol,
        int line,
        int charPositionInLine,
        String msg,
        RecognitionException e)
    {
        if (!error) {
            error = true;
            if (offendingSymbol instanceof Token
                && ((Token)offendingSymbol).getType() == Token.EOF)
            {
                eof = true;
            }
            if (!ignoreEof || !eof) {
                String phase = (recognizer instanceof Lexer) ? "LEXER" : "PARSER";
                throw new SPLError(
                    "%s syntax error line %d column %d:%n  %s".formatted(
                    phase, line, charPositionInLine, msg));
            }
        }
    }

    /** Associates this error handler with the given lexer or parser. */
    void attach(Recognizer<?,?> recog) {
        recog.removeErrorListeners();
        recog.addErrorListener(this);
    }

    /** Indicates whether an error occured during scanning/parsing. */
    public boolean gotError() {
        return error;
    }

    /** Indicates whether an EOR error specifically occured during parsing. */
    public boolean eofError() {
        return eof;
    }
}
