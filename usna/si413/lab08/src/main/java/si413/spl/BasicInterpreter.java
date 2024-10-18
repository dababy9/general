package si413.spl;

import si413.spl.ast.Statement;
import si413.spl.ast.Num;
import si413.spl.ast.Bool;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.BufferedTokenStream;

/** A no-frills SPL interpreter.
 * This is the class that will be used to test your code, and which you
 * should fully be able to read and understand.
 * Look how the steps of the run() method exactly match our high-level
 * understanding of the major steps of any interpreter or compiler!
 *
 * You should not need to modify this class.
 */
public class BasicInterpreter extends Interpreter {
    /** Main method to start the interpreter.
     * The one optional argument should be a filename to read SPL
     * commands from (default is stdin).
     */
    public static void main(String[] args) throws IOException {
        CharStream source;
        InputStream input;
        if (args.length == 0) {
            System.out.println("Basic SPL interpreter reading from standard in");
            source = CharStreams.fromStream(System.in);
            input = null;
        }
        else if (args.length == 1) {
            System.out.format("Basic SPL interpreter reading from '%s'%n", args[0]);
            source = CharStreams.fromFileName(args[0]);
            input = System.in;
        }
        else throw new RuntimeException("BasicInterpreter needs 0 or 1 command-line arguments");
        new BasicInterpreter(source, input, System.out).run();
    }

    private CharStream source;
    private Scanner input;
    private PrintStream output;

    /** Crates a new interpreter with the code source, the read input, and the output destination. */
    BasicInterpreter(CharStream source, InputStream input, PrintStream output) {
        this.source = source;
        if (input == null) this.input = null;
        else this.input = new Scanner(input);
        this.output = output;
    }

    /** Reads in a single number.
     * This method should be used by Read nodes in the AST.
     * Specified by the Interpreter interface.
     */
    @Override
    public int read() {
        if (input == null)
            error("Cannot use standard in for both source and read input");
        if (!input.hasNext())
            error("EOF reached on read expression");
        String tok = input.next();
        try {
            return Integer.parseInt(tok);
        }
        catch (NumberFormatException e) {
            error("Invalid input token: '%s'".formatted(tok));
            return 0;
        }
    }

    /** Prints a single value.
     * This method should be used by Write nodes in the AST.
     * Specified by the Interpreter interface.
     */
    @Override
    public void write(Object value) {
        output.println(value);
    }

    /** Prints an error message and immediately halts the interpreter.
     * Specified by the Interpreter interface.
     */
    @Override
    public void error(String message) {
        System.err.format("ERROR: %s%n", message);
        System.exit(5);
    }

    /** Actually runs the interpreter from its source code.
     * Specified by the Runnable interface.
     */
    @Override
    public void run() {
        ErrorCatcher err = new ErrorCatcher();
        SPLLexer lexer = new SPLLexer(source);
        err.attach(lexer);
        TokenStream tokens = new BufferedTokenStream(lexer);
        SPLParser parser = new SPLParser(tokens);
        err.attach(parser);
        SPLParser.ProgContext tree = parser.prog();
        for (Statement stmt : new StlistBuilder().visit(tree)) {
            stmt.execute(Interpreter.current().getGlobal());
        }
    }
}
