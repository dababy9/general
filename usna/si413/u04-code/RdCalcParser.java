import java.io.IOException;

public class RdCalcParser implements CalcParser {
    private CalcLexer lexer;

    public RdCalcParser() {
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
        exp();
        lexer.match(CalcToken.Type.STOP);
    }

    void exp() throws IOException {
        term();
        exptail();
    }

    void exptail() throws IOException {
        switch (lexer.peek().getType()) {
            case OPA:
                lexer.match(CalcToken.Type.OPA);
                term();
                exptail();
                break;
            case RP:
            case STOP:
                break;
            default:
                error("exptail");
        }
    }

    void term() throws IOException {
        factor();
        termtail();
    }

    void termtail() throws IOException {
        switch (lexer.peek().getType()) {
            case OPM:
                lexer.match(CalcToken.Type.OPM);
                factor();
                termtail();
                break;
            case RP:
            case STOP:
            case OPA:
                break;
            default:
                error("termtail");
        }
    }

    void factor() throws IOException {
        switch (lexer.peek().getType()) {
            case NUM:
                lexer.match(CalcToken.Type.NUM);
                break;
            case LP:
                lexer.match(CalcToken.Type.LP);
                exp();
                lexer.match(CalcToken.Type.RP);
                break;
            default:
                error("factor");
        }
    }
}
