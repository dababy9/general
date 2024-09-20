/** A single token in the Pat language. */
public class PatToken {
    /** Possible PAT language token types, including EOF. */
    public enum Type {
        /** A symbol, like 'x'. */
        SYM,
        /** A fold operation operator, '*'. */
        FOLD,
        /** Semicolon ';', used to terminate a statement. */
        STOP,
        /** Colon ':', used to assign variables. */
        COLON,
        /** Variable name such as 'X'. */
        NAME,
        /** Reversing postfix operator, '_r'. */
        REV,
        /** Left square bracket '['. */
        LB,
        /** Right square bracket ']'. */
        RB,
        /** End-of-file tokenn. */
        EOF
    }

    private Type type;
    private String text;

    /** Creates a token with the given type and matching literal source text.
     */
    public PatToken(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    /** Getter for the token type.
     */
    public Type getType() {
        return type;
    }

    /** Returns the literal source text that matched this token.
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "%s '%s'".formatted(type, text);
    }
}
