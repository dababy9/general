package si413.spl;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.function.*;
import si413.spl.ast.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jline.reader.*;
import org.jline.terminal.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.ListTokenSource;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.gui.TreeViewer;
import org.jline.utils.AttributedStyle;
import org.jline.utils.AttributedStringBuilder;
import net.harawata.appdirs.AppDirsFactory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/** A very fancy interpreter for SPL language statements.
 * This works the same ultimately as BasicInterpreter,
 * except that it supports a much nicer command-line interface,
 * with line editing, colors, immediate execution, and error recovery.
 *
 * You should not need to make any changes to this class.
 */
public class FancyInterpreter extends Interpreter {
    public static void main(String[] args) throws IOException {
        ArgumentParser parser = ArgumentParsers.newFor("FancyInterpreter")
            .build()
            .defaultHelp(true)
            .description("SPL interpreter with colors and line editing");
        parser.addArgument("-t", "--tree")
            .action(Arguments.storeTrue())
            .help("Display the AST of each statement before executing");
        parser.addArgument("-p", "--parsetree")
            .action(Arguments.storeTrue())
            .help("Show parse tree for each statement before executing");
        parser.addArgument("-d", "--dryrun")
            .action(Arguments.storeTrue())
            .help("Don't actually execute any statements");
        parser.addArgument("-n", "--nohistory")
            .action(Arguments.storeTrue())
            .help("Don't try to load or save line history");
        parser.addArgument("file")
            .nargs("?")
            .help("Source file of SPL code to run");
        Namespace ns = null;
        try { ns = parser.parseArgs(args); }
        catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        if (ns.getBoolean("nohistory")) saveHist = false;
        String filename = ns.getString("file");
        File sourceFile = null;
        if (filename != null) {
            sourceFile = Paths.get(filename).toFile();
            try { new FileInputStream(sourceFile).close(); }
            catch (IOException e) {
                parser.handleError(new ArgumentParserException(
                    "cannot read file:\n  %s".formatted(filename),
                    parser));
                System.exit(1);
            }
        }
        FancyInterpreter interp =
            (filename == null)
            ? new FancyInterpreter()
            : new FancyInterpreter(sourceFile);
        if (ns.getBoolean("tree")) interp.showAst = true;
        if (ns.getBoolean("parsetree")) interp.showParseTree = true;
        if (ns.getBoolean("dryrun")) interp.dryRun = true;
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
        <T> Optional<T> getObject(String prompt, Function<CharSequence, Optional<T>> complete);

        /** Returns true if end of file has been reached. */
        boolean eof();

        /** Reads lines of input and returns them only when complete.
         * @param prompt Text to display to a console user before reading the next line.
         * @param complete Function to test whether input is complete and should be returned.
         * @return The first sequence of read lines which are complete, or Optional.empty()
         *         if EOF is reached before finding completion.
         */
        default Optional<String> getLine(String prompt, Predicate<CharSequence> complete) {
            return getObject(prompt, (s -> (complete.test(s) ? Optional.of(s.toString()) : Optional.empty())));
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
        private class CompleteLine implements CompletingParsedLine {
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
        private class Getter<T> implements Predicate<CharSequence> {
            /** The underlying processing function to apply to a complete chunk of lines. */
            private Function<CharSequence, Optional<T>> maybe;
            /** The saved result of applying process() to input lines. */
            private T item = null;

            /** Creates a new instance using the specified function to determine
             * line completeness and process a result.
             */
            public Getter(Function<CharSequence, Optional<T>> maybe) {
                this.maybe = maybe;
            }

            /** Determines whether the given lines should be complete.
             * If so, the result of process() is saved.
             * This funciton is called from within JLine to determine whether more input
             * is needed from the console.
             */
            @Override
            public boolean test(CharSequence s) {
                Optional<T> got = maybe.apply(s);
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
        private CompletingParsedLine blankLine = new CompleteLine("");
        /** The underlying source of input lines. */
        private LineReader reader;
        /** Current check to apply in JLine to determine whether a line is complete. */
        private Predicate<CharSequence> parseCheck = (x -> true);
        private boolean gotEof = false;

        /** Attempts to create a console instance to support the Lines interface.
         * This is where saving and restoring the history in JLine happens as well.
         */
        private ConsoleLines() {
            LineReaderBuilder builder = LineReaderBuilder.builder()
                .terminal(getTerminal())
                .parser(this)
                .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%P.> ");
            if (saveHist) {
                builder.variable(LineReader.HISTORY_FILE,
                    Paths.get(AppDirsFactory.getInstance()
                        .getUserDataDir("spl", "0.1", "si413"), "history").toString());
            }
            reader = builder.build();
            if (saveHist) {
                try { reader.getHistory().load(); }
                catch (IOException e) { }
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try { reader.getHistory().save(); }
                    catch (IOException e) { }
                }));
            }
        }

        /** Specified by the Lines interface. */
        @Override
        public <T> Optional<T> getObject(String prompt, Function<CharSequence, Optional<T>> complete) {
            Getter<T> box = new Getter<>(complete);
            parseCheck = box;
            inline = true;
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
            finally {
                inline = false;
            }
            if (errorState) {
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
        implements Iterator<SPLParser.ProgContext>,
                   Function<CharSequence, Optional<SPLParser.ProgContext>>
    {
        /** The underlying source of input lines. */
        private Lines source;
        /** Remembers when EOF is reached. */
        private boolean eof = false;
        /** Saves the most recent complete parse tree. */
        private SPLParser.ProgContext saved = null;

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
            while (!eof && saved == null) {
                Optional<SPLParser.ProgContext> got = source.getObject("spl", this);
                if (errorState) {
                    // clear syntax error and try again
                    errorState = false;
                    continue;
                }
                else if (got.isPresent()) saved = got.get();
                else if (source.eof()) eof = true;
            }
            return saved != null;
        }

        /** Supports the Iterator interface to actually retrieve the next statement.
         */
        @Override
        public SPLParser.ProgContext next() {
            hasNext();
            SPLParser.ProgContext result = saved;
            saved = null;
            return result;
        }

        /** Supports the Function interface to check whether the next line is complete,
         * and if so, to return its parse tree.
         */
        @Override
        public Optional<SPLParser.ProgContext> apply(CharSequence source) {
            return getProg(CharStreams.fromString(source.toString(), "console"));
        }
    }

    private static SPLParser.ProgContext emptyProg =
        new SPLParser(new BufferedTokenStream(new ListTokenSource(List.of()))).prog();
    private static Terminal terminal = null;
    private ConsoleLines console = null;
    private static boolean gotTerminal = false;
    private boolean errorState = false;
    private Iterator<SPLParser.ProgContext> prog;
    private Lines readSource;
    private boolean interactive;
    private boolean showAst = false;
    private boolean showParseTree = false;
    private boolean dryRun = false;
    private boolean inline = false;
    private static boolean saveHist = true;

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
            prog = progFromStream(System.in);
            readSource = null;
            interactive = false;
        }
        else {
            prog = new ConsoleProg();
            readSource = getConsole();
            interactive = true;
        }
    }

    public FancyInterpreter(File sourceFile) throws IOException {
        prog = progFromStream(new FileInputStream(sourceFile));
        readSource = (getTerminal() == null) ? new ReaderLines(System.in) : getConsole();
        interactive = false;
    }

    private Iterator<SPLParser.ProgContext> progFromStream(InputStream source) throws IOException {
        return List.of(getProg(CharStreams.fromStream(source)).get()).iterator();
    }

    private Optional<SPLParser.ProgContext> getProg(CharStream source) {
        ErrorCatcher err = new ErrorCatcher(interactive);
        SPLLexer lexer = new SPLLexer(source);
        err.attach(lexer);
        TokenStream tokens = new BufferedTokenStream(lexer);
        SPLParser parser = new SPLParser(tokens);
        err.attach(parser);
        SPLParser.ProgContext tree = parser.prog();
        if (interactive && err.eofError()) return Optional.empty();
        else if (err.gotError()) return Optional.of(emptyProg);
        else return Optional.of(tree);
    }

    @Override
    public int read() {
        if (errorState) return 0;
        if (readSource == null) {
            error("Cannot read BOTH code and input from stdin unless it is a terminal");
            return 0;
        }
        Optional<String> got = readSource.getLine("read");
        if (!got.isPresent()) {
            error("EOF reached on read expression");
            return 0;
        }
        try {
            return Integer.parseInt(got.get());
        }
        catch (NumberFormatException e) {
            error("Invalid number given to read expression");
            return 0;
        }
    }

    @Override
    public void write(Object value) {
        if (errorState) return;
        Terminal term = getTerminal();
        if (term != null) {
            term.writer().println(new AttributedStringBuilder()
                .style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
                .append(value.toString())
                .style(AttributedStyle.DEFAULT)
                .toAnsi());
            term.flush();
        }
        else System.out.println(value);
    }

    @Override
    public void error(String message) {
        if (errorState) return;
        errorState = true;
        Terminal term = getTerminal();
        String toPrint = "ERROR: " + message;
        if (term != null) {
            if (inline) term.writer().println();
            term.writer().print(new AttributedStringBuilder()
                .style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
                .append(toPrint)
                .style(AttributedStyle.DEFAULT)
                .toAnsi());
            if (!inline) term.writer().println();
            term.flush();
        }
        else System.err.println(toPrint);
    }

    @Override
    public void run() {
        if (errorState) return;
        if (getTerminal() != null) {
            getTerminal().writer().println("Fancy SPL Interpreter");
            getTerminal().writer().flush();
        }
        if (interactive) {
            getTerminal().writer().println("Enter SPL statements. Ctrl-D to exit gracefully.");
            getTerminal().writer().flush();
        }
        StlistBuilder builder = new StlistBuilder();
        while (prog.hasNext()) {
            SPLParser.ProgContext tree = prog.next();
            if (showParseTree) System.out.println(tree.toStringTree(Arrays.asList(SPLParser.ruleNames)));
            if (showParseTree) {
                JFrame frame = new JFrame("SPL Parse Tree");
                JPanel panel = new JPanel();
                TreeViewer viewer = new TreeViewer(Arrays.asList(SPLParser.ruleNames), tree);
                double maxdim = Math.max(viewer.getPreferredSize().getHeight(),
                                        viewer.getPreferredSize().getWidth());
                if (maxdim <= 300) viewer.setScale(2.0);
                else if (maxdim <= 500) viewer.setScale(1.5);
                panel.add(viewer);
                frame.add(new JScrollPane(panel));
                frame.pack();
                frame.setVisible(true);
            }
            for (Statement stmt : builder.visit(tree)) {
                if (showAst) stmt.printTree();
                if (!dryRun) stmt.execute();
                if (errorState) {
                    if (interactive) {
                        errorState = false;
                        break;
                    }
                    else System.exit(5);
                }
            }
        }
        if (interactive) {
            getTerminal().writer().println("goodbye");
            getTerminal().writer().flush();
        }
    }
}
