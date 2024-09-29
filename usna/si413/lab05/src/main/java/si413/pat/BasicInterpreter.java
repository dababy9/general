package si413.pat;

import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.TokenStream;

/** Runnable class to read in, scan, parse, and evaluate a Pat program.
 *
 * The main method below uses the ANTLR scanner and grammar specifications
 * in PatLexer.g4 and PatParser.g4 to scan and parse any given input.
 * Then it uses the PatEvaluator class on the resulting parse tree to
 * actually run the Pat program.
 *
 * You should not need to make any changes to this class.
 */
public class BasicInterpreter {
    public static void main(String[] args) throws IOException {
        // Read from standard in
        System.out.println("Enter Pat statements, ending with Ctrl-D");
        CharStream source = CharStreams.fromStream(System.in);

        // Set up the scanner and parser
        ErrorFail err = new ErrorFail();
        PatLexer lexer = new PatLexer(source);
        err.attach(lexer);
        TokenStream tokens = new BufferedTokenStream(lexer);
        PatParser parser = new PatParser(tokens);
        err.attach(parser);

        try {
            // Scan and parse
            PatParser.ProgContext tree = parser.prog();
            // Evaluate the tree
            new PatEvaluator().visit(tree);
        }
        catch (PatError e) {
            e.printStackTrace();
            System.exit(5);
        }
    }
}
