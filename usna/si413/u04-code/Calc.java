import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.Tree;
import org.antlr.v4.gui.TreeViewer;

public class Calc {
    static void usage() {
        System.err.println("Usage: java Calc COMMAND [CLASS]");
        System.err.println("  where COMMAND is one of: scan parse interpret");
        System.err.println("  optional argument CLASS is a class name for that command");
        System.err.println();
        System.err.println("for scan, CLASS can be DFA (default), Regex, or Antlr");
        System.err.println("for parse, CLASS can be Rd (default) or Antlr");
        System.err.println("for interpret, CLASS can be Rd (default) or Antlr");
        System.exit(1);
    }

    static <T> T instantiate(Class<T> clz, String cname) {
        try {
            return clz.cast(Class.forName(cname).getDeclaredConstructor().newInstance());
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException("ERROR: could not instantiate %s from %s"
                .formatted(clz.getName(), cname));
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) usage();
        if (args[0].equals("scan")) {
            String cname;
            if (args.length < 2) cname = "DFACalcLexer";
            else if (args[1].equals("Antlr")) cname = "Calc$AntlrLexer";
            else cname = args[1] + "CalcLexer";
            CalcLexer scanner = instantiate(CalcLexer.class, cname);
            CalcToken tok = null;
            do {
                try { tok = scanner.readToken(); }
                catch (IOException e) {
                    System.out.format("Scanner error: %s%n", e.getMessage());
                    System.exit(2);
                }
                System.out.format("Read token: %s%n", tok);
            } while (tok.getType() != CalcToken.Type.EOF);
        }
        else if (args[0].equals("parse")) {
            String cname;
            if (args.length < 2) cname = "RdCalcParser";
            else if (args[1].equals("Antlr")) cname = "Calc$AntlrParser";
            else cname = args[1] + "CalcParser";
            CalcParser parser = instantiate(CalcParser.class, cname);
            try { parser.prog(); }
            catch (IOException e) {
                System.out.format("Parse error: %s%n", e.getMessage());
                System.exit(2);
            }
            System.out.println("Parse OK");
        }
        else if (args[0].equals("interpret")) {
            String cname;
            if (args.length < 2) cname = "RdCalcInterpreter";
            else if (args[1].equals("Antlr")) cname = "Calc$AntlrInterpreter";
            else cname = args[1] + "CalcInterpreter";
            CalcParser parser = instantiate(CalcParser.class, cname);
            try { parser.prog(); }
            catch (IOException e) {
                System.out.format("Parse error: %s%n", e.getMessage());
                System.exit(2);
            }
        }
        else usage();
    }

    static class AntlrErrorHandler extends BaseErrorListener {
        IOException error = null;

        @Override
        public void syntaxError(Recognizer<?,?> r, Object offending, int line,
            int column, String msg, RecognitionException e)
        {
            if (error == null)
                error = new IOException(msg);
        }

        public <T extends Recognizer<?,?>> T attach(T recog) {
            recog.removeErrorListeners();
            recog.addErrorListener(this);
            return recog;
        }

        public void throwIfError() throws IOException {
            if (error != null) throw error;
        }
    }

    static class AntlrLexer extends CalcLexer {
        private ACalcLexer inner;
        private AntlrErrorHandler handler = new AntlrErrorHandler();

        public AntlrLexer() throws IOException {
            inner = new ACalcLexer(CharStreams.fromStream(System.in));
            handler.attach(inner);
        }

        @Override
        public CalcToken readToken() throws IOException {
            org.antlr.v4.runtime.Token tok = inner.nextToken();
            CalcToken result = new CalcToken(
                CalcToken.Type.valueOf(ACalcLexer.VOCABULARY.getSymbolicName(tok.getType())),
                tok.getText());
            handler.throwIfError();
            return result;
        }
    }

    static class AntlrParser implements CalcParser {
        private ACalcParser inner;
        private AntlrErrorHandler handler = new AntlrErrorHandler();

        public AntlrParser() throws IOException {
            inner = new ACalcParser(
                new BufferedTokenStream(
                    handler.attach(new ACalcLexer(
                        CharStreams.fromStream(
                            System.in)))));
            handler.attach(inner);
        }

        protected ACalcParser.ProgContext getTree() throws IOException {
            ACalcParser.ProgContext result = inner.prog();
            handler.throwIfError();
            return result;
        }

        @Override
        public void prog() throws IOException {
            getTree();
        }
    }

    static class AntlrInterpreter extends AntlrParser {
        public AntlrInterpreter() throws IOException {
            super();
        }

        @Override
        public void prog() throws IOException {
            ACalcParser.ProgContext tree = getTree();
            AntlrCalcEvaluator.evaluate(tree);
        }
    }
}
