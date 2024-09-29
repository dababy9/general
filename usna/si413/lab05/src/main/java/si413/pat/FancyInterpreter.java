package si413.pat;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jline.reader.Parser;
import org.jline.reader.CompletingParsedLine;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.EOFError;
import org.jline.reader.EndOfFileException;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.jline.utils.AttributedStyle;
import org.jline.utils.AttributedStringBuilder;
import net.harawata.appdirs.AppDirsFactory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** A very fancy interpreter for Pat language statements.
 * This works the same ultimately as BasicInterpreter,
 * except that it supports a much nicer command-line interface,
 * with line editing, colors, immediate execution, and error recovery.
 *
 * You should not need to make any changes to this class.
 */
public class FancyInterpreter {
    /** Main method to process command-line args and start the interpreter.
     * Uses the argparse4j library to handle command-line arguments
     * and usage messages.
     */
    public static void main(String[] args) throws IOException {
        ArgumentParser parser = ArgumentParsers.newFor("FancyInterpreter")
            .build()
            .defaultHelp(true)
            .description("Interpreter for the Pat language");
        parser.addArgument("-p", "--parsetree")
            .action(Arguments.storeTrue())
            .help("Show parse tree for each statement before executing");
        parser.addArgument("file").nargs("?")
            .help("Source file to read commands from");
        Namespace ns = null;
        try { ns = parser.parseArgs(args); }
        catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        String filename = ns.getString("file");
        FancyInterpreter interp =
            (filename == null)
            ? new FancyInterpreter()
            : new FancyInterpreter(Paths.get(filename).toFile());
        if (ns.getBoolean("parsetree")) interp.showTree = true;
        interp.run();
    }

    /** An abstraction of lines of input.
     * The lines could come from e.g. a file or ther terminal (console).
     */
    private interface Lines {
        /** Reads and processes a complete chunk of input.
         * It will try to repeatedly read more and more text and keep calling
         * process() on the complete input until process() returns something non-empty
         * or EOF is reached.
         * @param prompt Text to display to a console user before reading the next line.
         * @param process A function to process the input before returning.
         * @return The result of successfully calling process(), or Optional.empty() if EOF is reached.
         */
        <T> Optional<T> getObject(String prompt, Function<CharSequence, Optional<T>> process);

        /** Returns true if end of file has been reached. */
        boolean eof();

        /** Reads lines of input and returns them only when complete.
         * @param prompt Text to display to a console user before reading the next line.
         * @param complete Function to test whether input is complete and should be returned.
         * @return The first sequence of read lines which are complete, or Optional.empty()
         *         if EOF is reached before finding completion.
         */
        default Optional<String> getLine(String prompt, Predicate<CharSequence> complete) {
            return getObject(prompt,
                (s -> (complete.test(s)
                       ? Optional.of(s.toString())
                       : Optional.empty())));
        }

        /** Reads a line of input and returns it.
         * @return Either the line read in or Optional.empty() if there is no more input.
         */
        default Optional<String> getLine(String prompt) {
            return getLine(prompt, (s -> true));
        }
    }

    /** Implementation of the Lines interface backed by an input stream.
     * This is to be used for non-interactive inputs such as a file.
     */
    private static class ReaderLines implements Lines {
        private BufferedReader source;
        private boolean gotEof = false;

        /** Creates a new ReaderLines instance backed by the given input stream. */
        public ReaderLines(InputStream in) {
            source = new BufferedReader(new InputStreamReader(in));
        }

        @Override
        public <T> Optional<T> getObject(String prompt, Function<CharSequence, Optional<T>> complete) {
            StringBuilder received = new StringBuilder();
            Optional<T> processed;
            do {
                String line = null;
                try { line = source.readLine(); }
                catch (IOException e) { throw new RuntimeException(e); }
                if (line == null) {
                    gotEof = true;
                    return Optional.empty();
                }
                if (!received.isEmpty()) received.append('\n');
                received.append(line);
            } while (!(processed = complete.apply(received)).isPresent());
            return processed;
        }

        @Override
        public boolean eof() {
            return gotEof;
        }
    }

    /** Implementation of the Lines interface backed by an interactive terminal.
     * This uses the JLine library to handle interactive text input.
     */
    private class ConsoleLines implements Lines, Parser {
        /** Dummy class to hold a complete line of JLine console input.
         * Needed to indicate that we want a "smart" console, without actually
         * implementing any tab completion.
         */
        private static class CompleteLine implements CompletingParsedLine {
            private String contents;

            public CompleteLine(String contents) {
                this.contents = contents;
            }

            @Override
            public int cursor() { return contents.length(); }

            @Override
            public String line() { return contents; }

            @Override
            public String word() { return ""; }

            @Override
            public int wordCursor() { return 0; }

            @Override
            public int wordIndex() { return 0; }

            @Override
            public List<String> words() { return List.of(); }

            @Override
            public CharSequence escape(CharSequence candidate, boolean complete) { return candidate; }

            @Override
            public int rawWordCursor() { return wordCursor(); }

            @Override
            public int rawWordLength() { return contents.length(); }
        }

        /** Helper class to indicate when input is complete while saving the result.
         *
         * This is used in the completingParsedLine method of the Jline Parser interface
         * to indicate whether JLine should return or ask for more input.
         *
         * It is needed to avoid extra calls to process() which will attempt to parse
         * the input - once that returns successfully, the resulting parse tree is
         * saved and can be retrieved later outside of JLine.
         */
        private static class Getter<T> implements Predicate<CharSequence> {
            /** The underlying processing function to apply to a complete chunk of lines. */
            private Function<CharSequence, Optional<T>> process;
            /** The saved result of applying process() to input lines. */
            private T item = null;

            /** Creates a new instance using the specified function to determine
             * line completeness and process a result.
             */
            public Getter(Function<CharSequence, Optional<T>> process) {
                this.process = process;
            }

            /** Determines whether the given lines should be complete.
             * If so, the result of process() is saved.
             * This funciton is called from within JLine to determine whether more input
             * is needed from the console.
             */
            @Override
            public boolean test(CharSequence s) {
                Optional<T> got = process.apply(s);
                if (got.isPresent()) {
                    item = got.get();
                    return true;
                }
                else return false;
            }

            /** Returns the saved result of process(). */
            public T get() {
                return item;
            }
        }

        /** Dummy value to return for tab completion attempts in JLine. */
        private static CompletingParsedLine blankLine = new CompleteLine("");
        /** The underlying source of input lines. */
        private LineReader reader;
        /** Current check to apply in JLine to determine whether a line is complete. */
        private Predicate<CharSequence> parseCheck = (x -> true);
        private boolean gotEof = false;

        /** Attempts to create a console instance to support the Lines interface.
         * This is where saving and restoring the history in JLine happens as well.
         */
        private ConsoleLines() {
            reader = LineReaderBuilder.builder()
                .terminal(getTerminal())
                .parser(this)
                .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%P.> ")
                .variable(LineReader.HISTORY_FILE,
                    Paths.get(AppDirsFactory.getInstance().getUserDataDir("pat", "0.1", "si413"), "history").toString())
                .build();
            try { reader.getHistory().load(); }
            catch (IOException e) { }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try { reader.getHistory().save(); }
                catch (IOException e) { }
            }));
        }

        /** Specified by the Lines interface. */
        @Override
        public <T> Optional<T> getObject(String prompt, Function<CharSequence, Optional<T>> complete) {
            Getter<T> box = new Getter<>(complete);
            parseCheck = box;
            try {
                reader.readLine(new AttributedStringBuilder()
                    .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
                    .append(prompt)
                    .style(AttributedStyle.DEFAULT)
                    .append("> ")
                    .toAnsi()
                );
            }
            catch (EndOfFileException e) {
                gotEof = true;
                return Optional.empty();
            }
            catch (UserInterruptException e) {
                getTerminal().writer().println("Press Ctrl-D to exit");
                return Optional.empty();
            }
            return Optional.of(box.get());
        }

        /** Specified by the Lines interface. */
        @Override
        public boolean eof() {
            return gotEof;
        }

        /** Checks within JLine whether the input so far is complete.
         * Specified by the JLine Parser interface.
         */
        @Override
        public CompletingParsedLine parse(String line, int cursor, Parser.ParseContext context)
            throws EOFError
        {
            if (context != Parser.ParseContext.ACCEPT_LINE) return blankLine;
            if (parseCheck.test(line)) return new CompleteLine(line);
            else throw new EOFError(0, cursor, "incomplete line");
        }
    }

    /** Helper class that acts as an iterator over input lines.
     * Each line of input is parsed and returned as a ProgContext
     * parse tree.
     */
    private class ConsoleProg
        implements Iterator<PatParser.ProgContext>,
                   Function<CharSequence, Optional<PatParser.ProgContext>>
    {
        /** The underlying source of input lines. */
        private Lines source;
        /** Remembers when EOF is reached. */
        private boolean eof = false;
        /** Saves the most recent complete parse tree. */
        private PatParser.ProgContext saved = null;
        /** Remembers the most recent syntax error. */
        private PatError error = null;

        /** Creates a new interactive console-based interpreter.
         * @throws UnsupportedOperationException if the JVM is not connected to a terminal.
         */
        public ConsoleProg() throws UnsupportedOperationException {
            this.source = getConsole();
        }

        /** Supports the Iterator interface to check whether there is another statement.
         */
        @Override
        public boolean hasNext() {
            while (!eof && error == null && saved == null) {
                Optional<PatParser.ProgContext> got;
                try {
                    got = source.getObject("pat", this);
                }
                catch (PatError e) {
                    error = e;
                    return true;
                }
                if (got.isPresent()) saved = got.get();
                else if (source.eof()) eof = true;
                else saved = null;
            }
            return saved != null;
        }

        /** Supports the Iterator interface to actually retrieve the next statement.
         */
        @Override
        public PatParser.ProgContext next() {
            hasNext();
            if (error != null) {
                PatError temp = error;
                error = null;
                throw temp;
            }
            PatParser.ProgContext result = saved;
            saved = null;
            return result;
        }

        /** Supports the Function interface to check whether the next line is complete,
         * and if so, to return its parse tree.
         */
        @Override
        public Optional<PatParser.ProgContext> apply(CharSequence source) {
            return getProg(CharStreams.fromString(source.toString(), "console"));
        }
    }

    private static Terminal terminal = null;
    private ConsoleLines console = null;
    private static boolean gotTerminal = false;
    private Iterator<PatParser.ProgContext> statements;
    private boolean interactive;
    private boolean showTree = false;

    /** Try to get a JLine terminal connected to the current JVM.
     * @return the connected terminal instance, or null.
     */
    private static Terminal getTerminal() {
        if (!gotTerminal) {
            if (System.console() == null) terminal = null;
            else {
                try { terminal = TerminalBuilder.builder().build(); }
                catch (IOException e) { terminal = null; }
            }
            gotTerminal = true;
        }
        return terminal;
    }

    /** Try get a ConsoleLines input for the current JVM.
     * Crucially, if this has already been called, it uses the saved object
     * rather than creating a new one.
     * @return the ConsoleLines object for this instance, or null.
     */
    private ConsoleLines getConsole() throws UnsupportedOperationException {
        if (getTerminal() == null) throw new UnsupportedOperationException("not connected to a terminal");
        if (console == null) console = new ConsoleLines();
        return console;
    }

    /** Creates a FancyInterpreter reading from standard in.
     */
    public FancyInterpreter() throws IOException {
        if (getTerminal() == null) {
            statements = progFromStream(System.in);
            interactive = false;
        }
        else {
            statements = new ConsoleProg();
            interactive = true;
        }
    }

    /** Creates a FancyInterpreter reading from the given file.
     */
    public FancyInterpreter(File sourceFile) throws IOException {
        statements = progFromStream(new FileInputStream(sourceFile));
        interactive = false;
    }

    /** Helper function to produce a single-element iterator for the complete
     * parse tree found in the given source.
     */
    private Iterator<PatParser.ProgContext> progFromStream(InputStream source) throws IOException {
        return List.of(getProg(CharStreams.fromStream(source)).get()).iterator();
    }

    /** Crucial helper function to scan and parse a given input source.
     */
    private Optional<PatParser.ProgContext> getProg(CharStream source) {
        ErrorFail err;
        if (interactive) err = new ErrorFail(new EOFError(0,0,"EOF"));
        else err = new ErrorFail();
        PatLexer lexer = new PatLexer(source);
        err.attach(lexer);
        TokenStream tokens = new BufferedTokenStream(lexer);
        PatParser parser = new PatParser(tokens);
        err.attach(parser);
        try {
            return Optional.of(parser.prog());
        }
        catch (EOFError e) {
            return Optional.empty();
        }
    }

    /** Actually run the interpreter. */
    public void run() {
        if (interactive) {
            getTerminal().writer().println("Enter Pat statements. Ctrl-D to exit gracefully.");
            getTerminal().writer().flush();
        }
        PatEvaluator eval = new PatEvaluator();
        while (statements.hasNext()) {
            PatParser.ProgContext prog = null;
            try { prog = statements.next(); }
            catch (PatError e) {
                if (interactive) {
                    System.err.format("ERROR: %s%n", e.getMessage());
                    continue;
                }
                else {
                    e.printStackTrace();
                    System.exit(5);
                }
            }
            if (showTree) {
                System.out.println(prog.toStringTree(Arrays.asList(PatParser.ruleNames)));
            }
            try { eval.visit(prog); }
            catch (PatError e) {
                if (interactive) {
                    System.err.format("ERROR: %s%n", e.getMessage());
                }
                else {
                    e.printStackTrace();
                    System.exit(5);
                }
            }
        }
        if (interactive) {
            getTerminal().writer().println("goodbye");
            getTerminal().writer().flush();
        }
    }
}
