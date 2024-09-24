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
        switch (lexer.peek().getType()) {
            case NAME: case SYM: case LB:
                List<String> result = seq();
                lexer.match(PatToken.Type.STOP);
                System.out.println(String.join(" ", result));
                prog();
                break;
            case EOF:
                lexer.match(PatToken.Type.EOF);
                break;
            default:
                error("prog");
        }
    }

    private List<String> seq() throws PatError {
        switch (lexer.peek().getType()) {
            case NAME: case SYM: case LB:
                return seqtail(catseq());
            default:
                error("seq");
                return null;
        }
    }

    private List<String> seqtail(List<String> lhs) throws PatError {
        switch (lexer.peek().getType()) {
            case FOLD:
                lexer.match(PatToken.Type.FOLD);
                return seqtail(fold(lhs, catseq()));
            case STOP: case RB:
                return lhs;
            default:
                error("seqtail");
                return null;
        }
    }

    private List<String> catseq() throws PatError {
        switch (lexer.peek().getType()) {
            case NAME: case SYM: case LB:
                return cattail(opseq());
            default:
                error("catseq");
                return null;
        }
    }

    private List<String> cattail(List<String> lhs) throws PatError {
        switch (lexer.peek().getType()) {
            case NAME: case SYM: case LB:
                ArrayList<String> cat = new ArrayList<>(lhs);
                cat.addAll(opseq());
                return cattail(cat);
            case STOP: case FOLD: case RB:
                return lhs;
            default:
                error("cattail");
                return null;
        }
    }

    private List<String> opseq() throws PatError {
        switch (lexer.peek().getType()) {
            case NAME: case SYM: case LB:
                return optail(atom());
            default:
                error("opseq");
                return null;
        }
    }

    private List<String> optail(List<String> lhs) throws PatError {
        List<String> result;
        switch (lexer.peek().getType()) {
            case COLON:
                lexer.match(PatToken.Type.COLON);
                String name = lexer.peek().getText();
                lexer.match(PatToken.Type.NAME);
                result = optail(lhs);
                symbolTable.put(name, lhs);
                return result;
            case REV:
                lexer.match(PatToken.Type.REV);
                result = new ArrayList<String>(lhs);
                Collections.reverse(result);
                return optail(result);
            case STOP: case FOLD: case NAME: case SYM: case LB: case RB:
                return lhs;
            default:
                error("optail");
                return null;
        }
    }

    private List<String> atom() throws PatError {
        String text;
        List<String> result;
        switch (lexer.peek().getType()) {
            case SYM:
                text = lexer.peek().getText();
                lexer.match(PatToken.Type.SYM);
                return List.of(text);
            case NAME:
                text = lexer.peek().getText();
                lexer.match(PatToken.Type.NAME);
                result = symbolTable.get(text);
                if (result == null) error("atom");
                return result;
            case LB:
                lexer.match(PatToken.Type.LB);
                result = seq();
                lexer.match(PatToken.Type.RB);
                return result;
            default:
                error("atom");
                return null;
        }
    }


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
