// Generated from si413/spl/SPLLexer.g4 by ANTLR 4.13.1
package si413.spl;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class SPLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NUM=1, BOOL=2, OPA=3, OPM=4, BOP=5, NOT=6, COMP=7, ASN=8, FUNARG=9, LP=10, 
		RP=11, LC=12, RC=13, STOP=14, IF=15, IFELSE=16, WHILE=17, READ=18, WRITE=19, 
		LAMBDA=20, NEW=21, ID=22, DEBUG=23, SPACE=24, COMMENT=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"NUM", "BOOL", "OPA", "OPM", "BOP", "NOT", "COMP", "ASN", "FUNARG", "LP", 
			"RP", "LC", "RC", "STOP", "IF", "IFELSE", "WHILE", "READ", "WRITE", "LAMBDA", 
			"NEW", "ID", "DEBUG", "SPACE", "COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'not'", null, "':='", "'@'", "'('", 
			"')'", "'{'", "'}'", "';'", "'if'", "'ifelse'", "'while'", "'read'", 
			"'write'", "'lambda'", "'new'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUM", "BOOL", "OPA", "OPM", "BOP", "NOT", "COMP", "ASN", "FUNARG", 
			"LP", "RP", "LC", "RC", "STOP", "IF", "IFELSE", "WHILE", "READ", "WRITE", 
			"LAMBDA", "NEW", "ID", "DEBUG", "SPACE", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SPLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SPLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0019\u00ac\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0001\u0000\u0004\u00005\b\u0000\u000b\u0000"+
		"\f\u00006\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001B\b\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004M\b\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006V\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0005"+
		"\u0015\u008f\b\u0015\n\u0015\f\u0015\u0092\t\u0015\u0001\u0016\u0001\u0016"+
		"\u0005\u0016\u0096\b\u0016\n\u0016\f\u0016\u0099\t\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0017\u0004\u0017\u009e\b\u0017\u000b\u0017\f\u0017\u009f"+
		"\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0005\u0018\u00a6\b\u0018"+
		"\n\u0018\f\u0018\u00a9\t\u0018\u0001\u0018\u0001\u0018\u0000\u0000\u0019"+
		"\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r"+
		"\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"+
		"\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017"+
		"/\u00181\u0019\u0001\u0000\n\u0001\u000009\u0002\u0000++--\u0002\u0000"+
		"**//\u0001\u0000<>\u0003\u0000!!<<>>\u0003\u0000AZ__az\u0004\u000009A"+
		"Z__az\u0001\u0000\"\"\u0003\u0000\t\n\r\r  \u0002\u0000\n\n\r\r\u00b3"+
		"\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000"+
		"\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000"+
		"\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000"+
		"\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011"+
		"\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015"+
		"\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019"+
		"\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d"+
		"\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001"+
		"\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000"+
		"\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000"+
		"\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000\u0000/"+
		"\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u00014\u0001\u0000"+
		"\u0000\u0000\u0003A\u0001\u0000\u0000\u0000\u0005C\u0001\u0000\u0000\u0000"+
		"\u0007E\u0001\u0000\u0000\u0000\tL\u0001\u0000\u0000\u0000\u000bN\u0001"+
		"\u0000\u0000\u0000\rU\u0001\u0000\u0000\u0000\u000fW\u0001\u0000\u0000"+
		"\u0000\u0011Z\u0001\u0000\u0000\u0000\u0013\\\u0001\u0000\u0000\u0000"+
		"\u0015^\u0001\u0000\u0000\u0000\u0017`\u0001\u0000\u0000\u0000\u0019b"+
		"\u0001\u0000\u0000\u0000\u001bd\u0001\u0000\u0000\u0000\u001df\u0001\u0000"+
		"\u0000\u0000\u001fi\u0001\u0000\u0000\u0000!p\u0001\u0000\u0000\u0000"+
		"#v\u0001\u0000\u0000\u0000%{\u0001\u0000\u0000\u0000\'\u0081\u0001\u0000"+
		"\u0000\u0000)\u0088\u0001\u0000\u0000\u0000+\u008c\u0001\u0000\u0000\u0000"+
		"-\u0093\u0001\u0000\u0000\u0000/\u009d\u0001\u0000\u0000\u00001\u00a3"+
		"\u0001\u0000\u0000\u000035\u0007\u0000\u0000\u000043\u0001\u0000\u0000"+
		"\u000056\u0001\u0000\u0000\u000064\u0001\u0000\u0000\u000067\u0001\u0000"+
		"\u0000\u00007\u0002\u0001\u0000\u0000\u000089\u0005t\u0000\u00009:\u0005"+
		"r\u0000\u0000:;\u0005u\u0000\u0000;B\u0005e\u0000\u0000<=\u0005f\u0000"+
		"\u0000=>\u0005a\u0000\u0000>?\u0005l\u0000\u0000?@\u0005s\u0000\u0000"+
		"@B\u0005e\u0000\u0000A8\u0001\u0000\u0000\u0000A<\u0001\u0000\u0000\u0000"+
		"B\u0004\u0001\u0000\u0000\u0000CD\u0007\u0001\u0000\u0000D\u0006\u0001"+
		"\u0000\u0000\u0000EF\u0007\u0002\u0000\u0000F\b\u0001\u0000\u0000\u0000"+
		"GH\u0005a\u0000\u0000HI\u0005n\u0000\u0000IM\u0005d\u0000\u0000JK\u0005"+
		"o\u0000\u0000KM\u0005r\u0000\u0000LG\u0001\u0000\u0000\u0000LJ\u0001\u0000"+
		"\u0000\u0000M\n\u0001\u0000\u0000\u0000NO\u0005n\u0000\u0000OP\u0005o"+
		"\u0000\u0000PQ\u0005t\u0000\u0000Q\f\u0001\u0000\u0000\u0000RV\u0007\u0003"+
		"\u0000\u0000ST\u0007\u0004\u0000\u0000TV\u0005=\u0000\u0000UR\u0001\u0000"+
		"\u0000\u0000US\u0001\u0000\u0000\u0000V\u000e\u0001\u0000\u0000\u0000"+
		"WX\u0005:\u0000\u0000XY\u0005=\u0000\u0000Y\u0010\u0001\u0000\u0000\u0000"+
		"Z[\u0005@\u0000\u0000[\u0012\u0001\u0000\u0000\u0000\\]\u0005(\u0000\u0000"+
		"]\u0014\u0001\u0000\u0000\u0000^_\u0005)\u0000\u0000_\u0016\u0001\u0000"+
		"\u0000\u0000`a\u0005{\u0000\u0000a\u0018\u0001\u0000\u0000\u0000bc\u0005"+
		"}\u0000\u0000c\u001a\u0001\u0000\u0000\u0000de\u0005;\u0000\u0000e\u001c"+
		"\u0001\u0000\u0000\u0000fg\u0005i\u0000\u0000gh\u0005f\u0000\u0000h\u001e"+
		"\u0001\u0000\u0000\u0000ij\u0005i\u0000\u0000jk\u0005f\u0000\u0000kl\u0005"+
		"e\u0000\u0000lm\u0005l\u0000\u0000mn\u0005s\u0000\u0000no\u0005e\u0000"+
		"\u0000o \u0001\u0000\u0000\u0000pq\u0005w\u0000\u0000qr\u0005h\u0000\u0000"+
		"rs\u0005i\u0000\u0000st\u0005l\u0000\u0000tu\u0005e\u0000\u0000u\"\u0001"+
		"\u0000\u0000\u0000vw\u0005r\u0000\u0000wx\u0005e\u0000\u0000xy\u0005a"+
		"\u0000\u0000yz\u0005d\u0000\u0000z$\u0001\u0000\u0000\u0000{|\u0005w\u0000"+
		"\u0000|}\u0005r\u0000\u0000}~\u0005i\u0000\u0000~\u007f\u0005t\u0000\u0000"+
		"\u007f\u0080\u0005e\u0000\u0000\u0080&\u0001\u0000\u0000\u0000\u0081\u0082"+
		"\u0005l\u0000\u0000\u0082\u0083\u0005a\u0000\u0000\u0083\u0084\u0005m"+
		"\u0000\u0000\u0084\u0085\u0005b\u0000\u0000\u0085\u0086\u0005d\u0000\u0000"+
		"\u0086\u0087\u0005a\u0000\u0000\u0087(\u0001\u0000\u0000\u0000\u0088\u0089"+
		"\u0005n\u0000\u0000\u0089\u008a\u0005e\u0000\u0000\u008a\u008b\u0005w"+
		"\u0000\u0000\u008b*\u0001\u0000\u0000\u0000\u008c\u0090\u0007\u0005\u0000"+
		"\u0000\u008d\u008f\u0007\u0006\u0000\u0000\u008e\u008d\u0001\u0000\u0000"+
		"\u0000\u008f\u0092\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000"+
		"\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091,\u0001\u0000\u0000\u0000"+
		"\u0092\u0090\u0001\u0000\u0000\u0000\u0093\u0097\u0005\"\u0000\u0000\u0094"+
		"\u0096\b\u0007\u0000\u0000\u0095\u0094\u0001\u0000\u0000\u0000\u0096\u0099"+
		"\u0001\u0000\u0000\u0000\u0097\u0095\u0001\u0000\u0000\u0000\u0097\u0098"+
		"\u0001\u0000\u0000\u0000\u0098\u009a\u0001\u0000\u0000\u0000\u0099\u0097"+
		"\u0001\u0000\u0000\u0000\u009a\u009b\u0005\"\u0000\u0000\u009b.\u0001"+
		"\u0000\u0000\u0000\u009c\u009e\u0007\b\u0000\u0000\u009d\u009c\u0001\u0000"+
		"\u0000\u0000\u009e\u009f\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000"+
		"\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000"+
		"\u0000\u0000\u00a1\u00a2\u0006\u0017\u0000\u0000\u00a20\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a7\u0005#\u0000\u0000\u00a4\u00a6\b\t\u0000\u0000\u00a5"+
		"\u00a4\u0001\u0000\u0000\u0000\u00a6\u00a9\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a5\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8"+
		"\u00aa\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00aa"+
		"\u00ab\u0006\u0018\u0000\u0000\u00ab2\u0001\u0000\u0000\u0000\t\u0000"+
		"6ALU\u0090\u0097\u009f\u00a7\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}