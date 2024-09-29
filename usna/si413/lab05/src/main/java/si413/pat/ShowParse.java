package si413.pat;

import java.util.Arrays;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.Trees;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.gui.TreeViewer;

/**
 * A simple demo to show AST GUI with ANTLR
 * @see http://www.antlr.org/api/Java/org/antlr/v4/runtime/tree/gui/TreeViewer.html
 * @author wangdq
 * 2014-5-24
 *
 * Modified by Dr Roche for SI413
 *
 * You do not need to modify this class.
 */
public class ShowParse {
    public static void main(String[] args) throws IOException {
        // Read from standard in
        System.out.println("Enter Pat statements, ending with Ctrl-D");
        CharStream source = CharStreams.fromStream(System.in);

        // Make the program halt on error
        ErrorFail err = new ErrorFail();

        // Scan and parse
        PatLexer lexer = new PatLexer(source);
        err.attach(lexer);
        TokenStream tokens = new BufferedTokenStream(lexer);
        PatParser parser = new PatParser(tokens);
        err.attach(parser);
        ParseTree tree = parser.prog();

        // Show the tree in Lisp format on the terminal
        System.out.println();
        System.out.println("Lisp-style text representation of the parse tree:");
        System.out.println(tree.toStringTree(parser));

        // Show GUI version
        System.out.println();
        System.out.println("Attempting to show parse tree visually in a JFrame...");
        JFrame frame = new JFrame("Pat Parse Tree");
        JPanel panel = new JPanel();
        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        double maxdim = Math.max(viewer.getPreferredSize().getHeight(),
                                 viewer.getPreferredSize().getWidth());
        if (maxdim <= 300) viewer.setScale(2.0);
        else if (maxdim <= 500) viewer.setScale(1.5);
        panel.add(viewer);
        frame.add(new JScrollPane(panel));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
