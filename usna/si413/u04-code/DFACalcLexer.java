import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/** Hand-coded lexer (aka scanner) for the simple calculator language.
 * It is an implementation of the finite automaton.
 */
public class DFACalcLexer extends CalcLexer {
    /** The different states of the DFA. 
     * Many (but not all!!) of these have a direct correspondence to
     * token types, but I picked different enum names to make the
     * distinction clearer.
     */
    private enum State {
        START,
        INTEGER,
        MINUS,
        PLUS,
        MULDIV,
        LEFTPAR,
        RIGHTPAR,
        SEMICOLON,
        TRAP
    }

    /** The labels of accepting states. */
    private static Map<State, CalcToken.Type> labels = Map.of(
        State.INTEGER,   CalcToken.Type.NUM,
        State.MINUS,     CalcToken.Type.OPA,
        State.PLUS,      CalcToken.Type.OPA,
        State.MULDIV,    CalcToken.Type.OPM,
        State.LEFTPAR,   CalcToken.Type.LP,
        State.RIGHTPAR,  CalcToken.Type.RP,
        State.SEMICOLON, CalcToken.Type.STOP
    );

    /** Which DFA state are we currently in. */
    private State state = State.START;

    /** String of characters read since the last token. */
    private StringBuffer text = new StringBuffer();

    /** The underlying stream. */
    private InputStream source;

    public DFACalcLexer() {
        this.source = System.in;
    }

    private State transition(char c) {
        switch (state) {
            case START:
                if (Character.isWhitespace(c)) return State.START;
                else if (Character.isDigit(c)) return State.INTEGER;
                else if (c == '-') return State.MINUS;
                else if (c == '+') return State.PLUS;
                else if (c == '*' || c == '/') return State.MULDIV;
                else if (c == '(') return State.LEFTPAR;
                else if (c == ')') return State.RIGHTPAR;
                else if (c == ';') return State.SEMICOLON;
                else return State.TRAP;
            case INTEGER:
                if (Character.isDigit(c)) return State.INTEGER;
                else return State.TRAP;
            case MINUS:
                if (Character.isDigit(c)) return State.INTEGER;
                else return State.TRAP;
            default:
                return State.TRAP;
        }
    }

    @Override
    protected CalcToken readToken() throws IOException {
        if (state == State.TRAP) {
            // in trap state means EOF has been reached
            return new CalcToken(CalcToken.Type.EOF, "");
        }
        // read chars until transition from accepting to non-accepting
        while (true) {
            int nextFromSource = source.read();
            if (nextFromSource == -1) {
                CalcToken tok;
                // -1 means EOF
                if (state == State.START)
                    tok = new CalcToken(CalcToken.Type.EOF, "");
                else if (labels.containsKey(state))
                    tok = new CalcToken(labels.get(state), text.toString());
                else
                    throw new IOException(
                        "Incomplete token at EOF: '%s'".formatted(text));
                state = State.TRAP;
                return tok;
            }
            char nextChar = (char)nextFromSource;
            State nextState = transition(nextChar);
            if (!labels.containsKey(state) && nextState == State.TRAP) {
                // non-accepting to trap state, must be syntax error
                throw new IOException("Invalid token starting with %s%c"
                    .formatted(text, nextChar));
            }
            else if (labels.containsKey(state) && !labels.containsKey(nextState)) {
                // accepting to non-accepting transition
                CalcToken tok = new CalcToken(labels.get(state), text.toString());
                // now reset the state and text for the single char nextChar
                text.setLength(0);
                state = State.START;
                state = transition(nextChar);
                if (state != State.START) text.append(nextChar);
                return tok;
            }
            else if (nextState == State.START) {
                // transition from non-accepting to START, so reset text to ignore whitespace
                text.setLength(0);
            }
            else text.append(nextChar);
            state = nextState;
        }
    }
}
