package si413.pat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStreams;

@Timeout(value = 300, unit = TimeUnit.MILLISECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class Part1Test {
    /** Helper function to confirm a string of source code does parse successfully. */
    void checkParses(String source) {
        getParse(source, false, null);
    }

    /** Helper function to confirm a string of source code is rejected by the parser. */
    void checkDoesntParse(String source) {
        getParse(source, true, null);
    }

    @Test
    void oneSym() {
        checkParses("hello;");
    }

    @Test
    void concatSyms() {
        checkParses("programming is so much fun;");
    }

    @Test
    void empty() {
        checkDoesntParse(";");
    }

    @Test
    void oneName() {
        checkParses("ThisIsAVariable;");
    }

    @Test
    void brackets1() {
        checkParses("[ok];");
    }

    @Test
    void brackets2() {
        checkParses("[it works] [or [does it]];");
    }

    @Test
    void brackets3() {
        checkDoesntParse("closing ] before you open [ is bad;");
    }

    @Test
    void multiStatements() {
        checkParses("a b c; d e;\nf g h i;");
    }

    @Test
    void rev() {
        checkParses("something_r;");
        checkParses("[two things]_r;");
    }

    @Test
    void assign1() {
        checkParses("X:X;");
        checkDoesntParse("bad : idea;");
    }

    @Test
    void assign2() {
        checkParses("[a b c] : Alphabet;");
        checkDoesntParse(":Nothing;");
    }

    @Test
    void fold1() {
        checkParses("left * right;");
    }

    @Test
    void fold2() {
        checkParses("do re mi * fa * sol la;");
    }

    @Test
    void fold3() {
        checkDoesntParse("* whoops;");
        checkDoesntParse("[bad idea] *");
    }

    @Test
    void all1() {
        checkParses("a [b c]_r:X_r * d:Y X * X Y X;");
    }

    @Test
    void all2() {
        checkParses("[ uno dos : TWO [ tres ] ] : SIX _r * quatro cinco * SIX ; SIX TWO ; seis ;");
    }

    /** Helper class to recognize and remember syntax errors during scanning and parsing.
     */
    static class ErrorCatch extends BaseErrorListener {
        boolean hadError = false;

        @Override
        public void syntaxError(
            Recognizer<?,?> r,
            Object o, int l, int c, String m, RecognitionException e)
        {
            hadError = true;
        }

        void attach(Recognizer<?,?> recog) {
            recog.removeErrorListeners();
            recog.addErrorListener(this);
        }
    }

    /** Helper function to try parsing a string of Pat source code.
     * @param source Pat program source code.
     * @param shouldErr True iff this source code is expected to cause a syntax error.
     * @param expectedTree If non-null, indicates what the parse tree should look like.
     * @return A completed parse of the source code on success.
     */
    static PatParser.ProgContext getParse(String source, boolean shouldErr, String expectedTree) {
        ErrorCatch err = new ErrorCatch();
        PatLexer lexer = new PatLexer(CharStreams.fromString(source));
        err.attach(lexer);
        PatParser parser = new PatParser(new BufferedTokenStream(lexer));
        err.attach(parser);
        PatParser.ProgContext result = parser.prog();
        if (shouldErr) {
            assertTrue(err.hadError,
                "source '%s' should not have parsed but did".formatted(source));
            return null;
        }
        assertFalse(err.hadError,
            "source '%s' should have parsed but didn't".formatted(source));
        if (expectedTree != null) {
            assertEquals(expectedTree, result.toStringTree(parser),
                "parse tree mismatch");
        }
        return result;
    }
}
