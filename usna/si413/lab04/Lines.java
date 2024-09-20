import java.io.*;

/** Interface to represent a source of text input lines.
 * With this interface, we can effectively read from either
 * a Console object or BufferedReader seamlessly.
 */
public interface Lines {
    /** Reads a single line of input.
     * @param prompt The prompt which may be displayed if the input comes from a terminal.
     * @return a single line of input text without trailing newline.
     */
    public String readLine(String prompt);

    /** Gets a line reader from standard input.
     * @return An instance of this interface reading from standard in.
     */
    public static Lines fromStdin() {
        Console cons = System.console();
        if (cons != null) return new ConsoleLines(cons);
        else return new ReaderLines(new InputStreamReader(System.in));
    }

    /** A class to read lines from the console, with prompting. */
    static class ConsoleLines implements Lines {
        private Console console;

        /** Constructs an instance backed by the specified Console instance.
         * @param console the backing Console instance, must be non-null.
         */
        public ConsoleLines(Console console) {
            this.console = console;
        }

        @Override
        public String readLine(String prompt) {
            return console.readLine(prompt);
        }
    }

    /** A class to read lines from an arbitrary Reader, without prompting. */
    static class ReaderLines implements Lines {
        private BufferedReader source;

        /** Constructs an instance backed by the specified Reader instance.
         * @param source the backing Reader instance, must be valid.
         */
        public ReaderLines(Reader source) {
            this.source = new BufferedReader(source);
        }

        @Override
        public String readLine(String prompt) {
            try { return source.readLine(); }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
