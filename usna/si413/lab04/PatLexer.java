import java.io.*;
import java.util.*;
import java.util.regex.*;

/** Pat language scanner/lexer which reads raw source and produces a stream of tokens. */
public class PatLexer {
    /** Pairing of token types to regular expression patterns. */
    private static final Map<PatToken.Type, Pattern> specs = Map.of(
        PatToken.Type.SYM,    Pattern.compile("[a-z][a-zA-Z0-9]*") ,
        PatToken.Type.FOLD,   Pattern.compile("\\*")               ,
        PatToken.Type.STOP,   Pattern.compile(";")                 ,
        PatToken.Type.COLON,  Pattern.compile(":")                 ,
        PatToken.Type.NAME,   Pattern.compile("[A-Z][a-zA-Z0-9]*") ,
        PatToken.Type.REV,    Pattern.compile("_r")                ,
        PatToken.Type.LB,     Pattern.compile("\\[")               ,
        PatToken.Type.RB,     Pattern.compile("\\]")
    );

    private static final Pattern whitespace = Pattern.compile("\\s*");

    private Lines source;
    private Matcher curLine = null;
    private PatToken nextTok = null;

    /** Creates a PatLexer to read from standard in. */
    public PatLexer() {
        source = Lines.fromStdin();
    }

    /** Reads, moves past, and returns the next complete token from the input source.
     *
     * This works by first trying to get one more token on the current line, and
     * then reading in more line(s) as necessary.
     *
     * @return The next complete token or an EOF token if there are no more tokens
     * of input.
     *
     * @throws PatError if the input cannot match any token or whitespace.
     */
    private PatToken readToken() throws PatError {
        while (true) {
            if (curLine == null) {
                // no current line, so read a new one
                String line = source.readLine("pat> ");
                // check for end of input
                if (line == null)
                    return new PatToken(PatToken.Type.EOF, "");
                curLine = whitespace.matcher(line);
            }
            // skip any whitespace to start the next token
            if (curLine.usePattern(whitespace).lookingAt())
                curLine.region(curLine.end(), curLine.regionEnd());
            // stop the loop if there is anything left on the line besides whitespace
            if (curLine.regionStart() < curLine.regionEnd()) break;
            else curLine = null;
        }
        // use maximal munch to select the longest matching token
        PatToken candidate = null;
        for (Map.Entry<PatToken.Type, Pattern> spec : specs.entrySet()) {
            if (curLine.usePattern(spec.getValue()).lookingAt()) {
                // this token could be a match - check if it is the longest
                String text = curLine.group();
                if (candidate == null || text.length() > candidate.getText().length())
                    candidate = new PatToken(spec.getKey(), text);
            }
        }
        if (candidate == null)
            throw new PatError("Unrecognized token starting at column %d"
                .formatted(curLine.regionStart()));
        // skip past the matched token on the current line
        curLine.region(curLine.regionStart() + candidate.getText().length(), curLine.regionEnd());
        return candidate;
    }

    /** Gets but does not consume the next token of input.
     * @return A non-null PatToken object for the next input token.
     * @throws PatError if a scanner error occurs.
     */
    public PatToken peek() throws PatError {
        if (nextTok == null) nextTok = readToken();
        return nextTok;
    }

    /** Gets and consumes (moves past) the next token of input.
     * @return A non-null PatToken object for the next input token.
     * @throws PatError if a scanner error occurs.
     */
    public PatToken match() throws PatError {
        PatToken result = peek();
        nextTok = null;
        return result;
    }

    /** Consumes and returns the next input token if it has the expected type.
     * @param expected the token type which the next token must have.
     * @return A non-null PatToken matching the expected type.
     * @throws PatError If the next token does not match expected type.
     */
    public PatToken match(PatToken.Type expected) throws PatError {
        PatToken.Type nextType = peek().getType();
        if (nextType != expected)
            throw new PatError("PatLexer mismatch: expected %s, got %s"
                .formatted(expected, peek()));
        return match();
    }

    /** Program to read input lines and print the resulting Pat tokens.
     * @param args ignored.
     */
    public static void main(String[] args) {
        if (System.console() != null) {
            // show help if connected to a live terminal
            System.out.println("Lexer test program - will break input into tokens.");
            System.out.println("Enter Pat language input below, followed by Ctrl-D.");
        }
        PatLexer lex = new PatLexer();
        PatToken lastTok = null;
        do {
            try {
                lastTok = lex.match();
                System.out.println(lastTok);
            }
            catch (PatError e) {
                System.err.println(e);
                System.exit(5);
            }
        } while (lastTok.getType() != PatToken.Type.EOF);
        if (System.console() != null)
            System.out.println("goodbye");
    }
}
