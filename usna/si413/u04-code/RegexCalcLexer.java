import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** Lexer (aka scanner) for the simple calculator language using regular expressions.
 */
public class RegexCalcLexer extends CalcLexer {
    /** Pairing of token type to regular expression. */
    private static Map<CalcToken.Type, Pattern> specs = Map.of(
        CalcToken.Type.OPA,  Pattern.compile("[+-]"),
        CalcToken.Type.OPM,  Pattern.compile("[*/]"),
        CalcToken.Type.NUM,  Pattern.compile("-?[0-9]+"),
        CalcToken.Type.LP,   Pattern.compile("\\("),
        CalcToken.Type.RP,   Pattern.compile("\\)"),
        CalcToken.Type.STOP, Pattern.compile(";")
    );

    private static Pattern whitespace = Pattern.compile("\\s*");

    private Matcher curLine = null;
    private BufferedReader source;

    public RegexCalcLexer() throws IOException {
	this.source = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    protected CalcToken readToken() throws IOException {
        while (true) {
            if (curLine == null) {
                String line = source.readLine();
                if (line == null) return new CalcToken(CalcToken.Type.EOF, "");
                curLine = whitespace.matcher(line);
            }
            if (curLine.usePattern(whitespace).lookingAt())
                curLine.region(curLine.end(), curLine.regionEnd());
            if (curLine.regionStart() < curLine.regionEnd()) break;
            else curLine = null;
        }
        CalcToken candidate = null;
        for (Map.Entry<CalcToken.Type, Pattern> spec : specs.entrySet()) {
            if (curLine.usePattern(spec.getValue()).lookingAt()) {
                String text = curLine.group();
                if (candidate == null || text.length() > candidate.getText().length())
                    candidate = new CalcToken(spec.getKey(), text);
            }
        }
        if (candidate == null)
            throw new IOException("Unrecognized token starting on column %d"
                .formatted(curLine.regionStart()));
        curLine.region(curLine.regionStart() + candidate.getText().length(), curLine.regionEnd());
        return candidate;
    }
}
