import java.util.*;

/** Interpreter for the Pat language. */
public class PatInterpreter {
    /** Helper method to interleave two lists of strings.
     * YOU SHOULD NOT NEED TO MODIFY THIS METHOD.
     */
    private static List<String> fold(List<String> lhs, List<String> rhs) {
        List<String> result = new ArrayList<>();
        Iterator<String> lit = lhs.iterator();
        Iterator<String> rit = rhs.iterator();
        while (lit.hasNext() || rit.hasNext()) {
            if (lit.hasNext()) result.add(lit.next());
            if (rit.hasNext()) result.add(rit.next());
        }
        return result;
    }

    private PatLexer lexer;
    private Map<String, List<String>> symbolTable = new HashMap<>();

    /** Createss a new PatInterpreter reading tokens from the given scanner.
     * YOU SHOULD NOT NEED TO MODIFY THIS METHOD.
     */
    public PatInterpreter(PatLexer lexer) {
        this.lexer = lexer;
    }

    /** Call this function when your parser sees a token that
     * doesn't fit the current rule.
     * YOU SHOULD NOT NEED TO MODIFY THIS METHOD.
     * @param rule the name of the non-terminal such as 'factor' or 'seqtail'.
     * @throws PatError This function ALWAYS throws a new PatError with a
     *                  suitable message based on the next token and the rule name you give.
     */
    private void error(String rule) throws PatError {
        throw new PatError("Unexepected token %s when parsing %s"
            .formatted(lexer.peek(), rule));
    }

    /** Parses and interprets a complete Pat langauge program..
     * @throws PatError if any syntax or undefined variable occurs.
     */
    /** Attempt to parse and interpret input as a series of Pat language statements.
     * For PatInterpreter, this should print the actual pattern resulting from
     * each successfully-parsed statement, instead of just sayiing "Parse OK".
     */
    public void prog() throws PatError {
        // TODO YOU FILL THIS IN
    }

    // TODO You will write bunch of helper methods for each non-terminal in the
    // grammar. Unlike in PatParser where they all return void, most of these should
    // return a List<String> for the resulting pattern sequence.


    /** Runs the Pat interpreter from the command line.
     * YOU SHOULD NOT NEED TO MODIFY THIS METHOD.
     */
    public static void main(String[] args) {
        if (System.console() != null)
            // show help if connected to a live terminal
            System.out.println("Enter Pat language input below, followed by Ctrl-D.");
        PatLexer lexer = new PatLexer();
        PatInterpreter interp = new PatInterpreter(lexer);
        try { interp.prog(); }
        catch (PatError e) {
            e.printStackTrace();
            System.exit(5);
        }
        if (System.console() != null)
            System.out.println("goodbye");
    }
}
