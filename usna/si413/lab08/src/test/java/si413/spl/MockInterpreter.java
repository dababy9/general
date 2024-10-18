package si413.spl;

import si413.spl.ast.*;
import java.util.*;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;

/** An Interpreter implementation that runs SPL code for testing.
 * Instead of reading from files/stdin and writing to stdout,
 * the class reads and writes from/to strings, which is convenient for
 * unit testing.
 */
class MockInterpreter extends Interpreter {
    /** Error class to be thrown (only!) when an SPL error is properly triggered.
     */
    public static class Error extends RuntimeException {
        private static final long serialVersionUID = 3774597810024146142L;
        public Error(String msg) {
            super(msg);
        }
    }

    /** Error handler class for the lexer and parser. */
    private class Handler extends BaseErrorListener {
        @Override
        public void syntaxError(
            Recognizer<?,?> r,
            Object o,
            int l,
            int c,
            String m,
            RecognitionException e)
        {
            throw new Error("Syntax error parsing '%s': %s".formatted(source, m));
        }

        <T extends Recognizer<?,?>> T attach(T recog) {
            recog.removeErrorListeners();
            recog.addErrorListener(this);
            return recog;
        }
    }

    private String source;
    private Iterator<Integer> inputs;
    private List<String> outputs = new ArrayList<>();

    /** Creates a new MockInterpreter with given source code and read inputs.
     * The interpreter will scan, parse and execute the source code.
     * Whenever read() is called, the next value from the given list of inputs
     * is returned.
     */
    public MockInterpreter(String source, Iterable<Integer> inputs) {
        this.source = source;
        this.inputs = inputs.iterator();
    }

    /** Retrieve all lines of normal output after running. */
    public List<String> getOutputs() {
        return outputs;
    }

    @Override
    public int read() {
        if (inputs.hasNext()) return inputs.next();
        else throw new Error("too many read's or too few inputs");
    }

    @Override
    public void write(Object value) {
        outputs.add(value.toString());
    }

    @Override
    public void error(String message) {
        throw new Error(message);
    }

    public List<Statement> getStmts() {
        Handler err = new Handler();
        SPLLexer lexer = new SPLLexer(CharStreams.fromString(source));
        err.attach(lexer);
        SPLParser parser = new SPLParser(new BufferedTokenStream(lexer));
        err.attach(parser);
        return new StlistBuilder().visit(parser.prog());
    }

    @Override
    public void run() {
        Handler err = new Handler();
        for (Statement stmt : getStmts()) {
            stmt.execute(Interpreter.current().getGlobal());
        }
    }
}
