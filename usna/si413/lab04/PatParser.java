/** Parser to recognize valid Pat language statements. */
public class PatParser {
    private PatLexer lexer;

    /** Creates a new PatParser from the given token stream.
     * YOU SHOULD NOT NEED TO MODIFY THIS METHOD.
     */
    public PatParser(PatLexer lexer) {
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

    /** Attempt to parse input as a series of Pat language statements.
     * Note, for PatParser, this function (and any recursive sub-functions)
     * just need to print "Parse OK" for each successfully-parsed statement,
     * or call error() if any syntax error occurs.
     */
    public void prog() throws PatError {
        switch (lexer.peek().getType()) {
            case EOF:
                break;
            case STOP:
                System.out.pritnln("Parse OK");
            default:
                error("prog");
        }
    }

    private void seq() throws PatError {
        switch (lexer.peek().getType()) {
            case :
                break;
            default:
                error("seq");
        }
    }

    private void seqtail() throws PatError {
        switch (lexer.peek().getType()) {
            case FOLD:
                lexer.match(PatToken.Type.FOLD);
                catseq();
                seqtail();
                break;
            case STOP: case RB:
                break;
            default:
                error("seqtail");
        }
    }

    private void catseq() throws PatError {
        switch (lexer.peek().getType()) {
            case :
                break;
            default:
                error("catseq");
        }
    }

    private void cattail() throws PatError {
        switch (lexer.peek().getType()) {
            case :
                break;
            default:
                error("cattail");
        }
    }

    private void opseq() throws PatError {
        switch (lexer.peek().getType()) {
            case :
                break;
            default:
                error("opseq");
        }
    }

    private void optail() throws PatError {
        switch (lexer.peek().getType()) {
            case :
                break;
            default:
                error("optail");
        }
    }

    private void atom() throws PatError {
        switch (lexer.peek().getType()) {
            case :
                break;
            default:
                error("atom");
        }
    }


    /** Test program that reads lines and tries to parse them in the Pat language.
     * YOU SHOULD NOT NEED TO MODIFY THIS METHOD.
     */
    public static void main(String[] args) {
        if (System.console() != null) {
            // show help if connected to a live terminal
            System.out.println("Parser test program - will parse and recognize complete statements.");
            System.out.println("Enter Pat language input below, followed by Ctrl-D.");
        }
        PatLexer lexer = new PatLexer();
        PatParser parser = new PatParser(lexer);
        try { parser.prog(); }
        catch (PatError e) {
            e.printStackTrace();
            System.exit(5);
        }
        if (System.console() != null)
            System.out.println("goodbye");
    }
}
