import java.io.IOException;

public class RdCalcInterpreter implements CalcParser {
    private CalcLexer lexer;

    public RdCalcInterpreter() {
        this.lexer = new DFACalcLexer();
    }

    private void error(String rule) throws IOException {
        throw new IOException("Parse error in %s on token %s"
            .formatted(rule, lexer.peek()));
    }

    @Override
    public void prog() throws IOException {
        switch (lexer.peek().getType()) {
            case NUM:
            case LP:
                stmt();
                prog();
                break;
            case EOF:
                break;
            default:
                error("prog");
        }
    }

    void stmt() throws IOException {
        int result = exp();
        System.out.println(result);
        lexer.match(CalcToken.Type.STOP);
    }

    int exp() throws IOException {
        int lhs = term();
        return exptail(lhs);
    }

    int exptail(int accumulate) throws IOException {
        switch (lexer.peek().getType()) {
            case OPA:
                CalcToken oper = lexer.match(CalcToken.Type.OPA);
                int rhs = term();
                if (oper.getText().equals("+")) accumulate += rhs;
                else accumulate -= rhs;
                return exptail(accumulate);
            case RP:
            case STOP:
                return accumulate;
            default:
                error("exptail");
                return -1;
        }
    }

    int term() throws IOException {
        int lhs = factor();
        return termtail(lhs);
    }

    int termtail(int accumulate) throws IOException {
        switch (lexer.peek().getType()) {
            case OPM:
                CalcToken oper = lexer.match(CalcToken.Type.OPM);
                int rhs = factor();
                if (oper.getText().equals("*")) accumulate *= rhs;
                else accumulate /= rhs;
                return termtail(accumulate);
            case RP:
            case STOP:
            case OPA:
                return accumulate;
            default:
                error("termtail");
                return -1;
        }
    }

    int factor() throws IOException {
        switch (lexer.peek().getType()) {
            case NUM:
                CalcToken tok = lexer.match(CalcToken.Type.NUM);
                return Integer.valueOf(tok.getText());
            case LP:
                lexer.match(CalcToken.Type.LP);
                int result = exp();
                lexer.match(CalcToken.Type.RP);
                return result;
            default:
                error("factor");
                return -1;
        }
    }
}
