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
		LAMBDA=20, CLASS=21, OBJ=22, NEW=23, ID=24, DEBUG=25, SPACE=26, COMMENT=27;
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
			"CLASS", "OBJ", "NEW", "ID", "DEBUG", "SPACE", "COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, "'not'", null, "':='", "'@'", "'('", 
			"')'", "'{'", "'}'", "';'", "'if'", "'ifelse'", "'while'", "'read'", 
			"'write'", "'lambda'", "'class'", "'!'", "'new'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NUM", "BOOL", "OPA", "OPM", "BOP", "NOT", "COMP", "ASN", "FUNARG", 
			"LP", "RP", "LC", "RC", "STOP", "IF", "IFELSE", "WHILE", "READ", "WRITE", 
			"LAMBDA", "CLASS", "OBJ", "NEW", "ID", "DEBUG", "SPACE", "COMMENT"
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
		"\u0004\u0000\u001b\u00b8\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0001\u0000\u0004\u00009\b\u0000\u000b\u0000\f\u0000:\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0003\u0001F\b\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004Q\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006Z\b\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n"+
		"\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0005"+
		"\u0017\u009b\b\u0017\n\u0017\f\u0017\u009e\t\u0017\u0001\u0018\u0001\u0018"+
		"\u0005\u0018\u00a2\b\u0018\n\u0018\f\u0018\u00a5\t\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0019\u0004\u0019\u00aa\b\u0019\u000b\u0019\f\u0019\u00ab"+
		"\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0005\u001a\u00b2\b\u001a"+
		"\n\u001a\f\u001a\u00b5\t\u001a\u0001\u001a\u0001\u001a\u0000\u0000\u001b"+
		"\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r"+
		"\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"+
		"\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017"+
		"/\u00181\u00193\u001a5\u001b\u0001\u0000\n\u0001\u000009\u0002\u0000+"+
		"+--\u0002\u0000**//\u0001\u0000<>\u0003\u0000!!<<>>\u0003\u0000AZ__az"+
		"\u0004\u000009AZ__az\u0001\u0000\"\"\u0003\u0000\t\n\r\r  \u0002\u0000"+
		"\n\n\r\r\u00bf\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000"+
		"\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000"+
		"\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000"+
		"\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000"+
		"\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000"+
		"\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000"+
		"\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000"+
		"\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000"+
		"\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%"+
		"\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001"+
		"\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000"+
		"\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u0000"+
		"3\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00018\u0001"+
		"\u0000\u0000\u0000\u0003E\u0001\u0000\u0000\u0000\u0005G\u0001\u0000\u0000"+
		"\u0000\u0007I\u0001\u0000\u0000\u0000\tP\u0001\u0000\u0000\u0000\u000b"+
		"R\u0001\u0000\u0000\u0000\rY\u0001\u0000\u0000\u0000\u000f[\u0001\u0000"+
		"\u0000\u0000\u0011^\u0001\u0000\u0000\u0000\u0013`\u0001\u0000\u0000\u0000"+
		"\u0015b\u0001\u0000\u0000\u0000\u0017d\u0001\u0000\u0000\u0000\u0019f"+
		"\u0001\u0000\u0000\u0000\u001bh\u0001\u0000\u0000\u0000\u001dj\u0001\u0000"+
		"\u0000\u0000\u001fm\u0001\u0000\u0000\u0000!t\u0001\u0000\u0000\u0000"+
		"#z\u0001\u0000\u0000\u0000%\u007f\u0001\u0000\u0000\u0000\'\u0085\u0001"+
		"\u0000\u0000\u0000)\u008c\u0001\u0000\u0000\u0000+\u0092\u0001\u0000\u0000"+
		"\u0000-\u0094\u0001\u0000\u0000\u0000/\u0098\u0001\u0000\u0000\u00001"+
		"\u009f\u0001\u0000\u0000\u00003\u00a9\u0001\u0000\u0000\u00005\u00af\u0001"+
		"\u0000\u0000\u000079\u0007\u0000\u0000\u000087\u0001\u0000\u0000\u0000"+
		"9:\u0001\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000"+
		"\u0000;\u0002\u0001\u0000\u0000\u0000<=\u0005t\u0000\u0000=>\u0005r\u0000"+
		"\u0000>?\u0005u\u0000\u0000?F\u0005e\u0000\u0000@A\u0005f\u0000\u0000"+
		"AB\u0005a\u0000\u0000BC\u0005l\u0000\u0000CD\u0005s\u0000\u0000DF\u0005"+
		"e\u0000\u0000E<\u0001\u0000\u0000\u0000E@\u0001\u0000\u0000\u0000F\u0004"+
		"\u0001\u0000\u0000\u0000GH\u0007\u0001\u0000\u0000H\u0006\u0001\u0000"+
		"\u0000\u0000IJ\u0007\u0002\u0000\u0000J\b\u0001\u0000\u0000\u0000KL\u0005"+
		"a\u0000\u0000LM\u0005n\u0000\u0000MQ\u0005d\u0000\u0000NO\u0005o\u0000"+
		"\u0000OQ\u0005r\u0000\u0000PK\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000"+
		"\u0000Q\n\u0001\u0000\u0000\u0000RS\u0005n\u0000\u0000ST\u0005o\u0000"+
		"\u0000TU\u0005t\u0000\u0000U\f\u0001\u0000\u0000\u0000VZ\u0007\u0003\u0000"+
		"\u0000WX\u0007\u0004\u0000\u0000XZ\u0005=\u0000\u0000YV\u0001\u0000\u0000"+
		"\u0000YW\u0001\u0000\u0000\u0000Z\u000e\u0001\u0000\u0000\u0000[\\\u0005"+
		":\u0000\u0000\\]\u0005=\u0000\u0000]\u0010\u0001\u0000\u0000\u0000^_\u0005"+
		"@\u0000\u0000_\u0012\u0001\u0000\u0000\u0000`a\u0005(\u0000\u0000a\u0014"+
		"\u0001\u0000\u0000\u0000bc\u0005)\u0000\u0000c\u0016\u0001\u0000\u0000"+
		"\u0000de\u0005{\u0000\u0000e\u0018\u0001\u0000\u0000\u0000fg\u0005}\u0000"+
		"\u0000g\u001a\u0001\u0000\u0000\u0000hi\u0005;\u0000\u0000i\u001c\u0001"+
		"\u0000\u0000\u0000jk\u0005i\u0000\u0000kl\u0005f\u0000\u0000l\u001e\u0001"+
		"\u0000\u0000\u0000mn\u0005i\u0000\u0000no\u0005f\u0000\u0000op\u0005e"+
		"\u0000\u0000pq\u0005l\u0000\u0000qr\u0005s\u0000\u0000rs\u0005e\u0000"+
		"\u0000s \u0001\u0000\u0000\u0000tu\u0005w\u0000\u0000uv\u0005h\u0000\u0000"+
		"vw\u0005i\u0000\u0000wx\u0005l\u0000\u0000xy\u0005e\u0000\u0000y\"\u0001"+
		"\u0000\u0000\u0000z{\u0005r\u0000\u0000{|\u0005e\u0000\u0000|}\u0005a"+
		"\u0000\u0000}~\u0005d\u0000\u0000~$\u0001\u0000\u0000\u0000\u007f\u0080"+
		"\u0005w\u0000\u0000\u0080\u0081\u0005r\u0000\u0000\u0081\u0082\u0005i"+
		"\u0000\u0000\u0082\u0083\u0005t\u0000\u0000\u0083\u0084\u0005e\u0000\u0000"+
		"\u0084&\u0001\u0000\u0000\u0000\u0085\u0086\u0005l\u0000\u0000\u0086\u0087"+
		"\u0005a\u0000\u0000\u0087\u0088\u0005m\u0000\u0000\u0088\u0089\u0005b"+
		"\u0000\u0000\u0089\u008a\u0005d\u0000\u0000\u008a\u008b\u0005a\u0000\u0000"+
		"\u008b(\u0001\u0000\u0000\u0000\u008c\u008d\u0005c\u0000\u0000\u008d\u008e"+
		"\u0005l\u0000\u0000\u008e\u008f\u0005a\u0000\u0000\u008f\u0090\u0005s"+
		"\u0000\u0000\u0090\u0091\u0005s\u0000\u0000\u0091*\u0001\u0000\u0000\u0000"+
		"\u0092\u0093\u0005!\u0000\u0000\u0093,\u0001\u0000\u0000\u0000\u0094\u0095"+
		"\u0005n\u0000\u0000\u0095\u0096\u0005e\u0000\u0000\u0096\u0097\u0005w"+
		"\u0000\u0000\u0097.\u0001\u0000\u0000\u0000\u0098\u009c\u0007\u0005\u0000"+
		"\u0000\u0099\u009b\u0007\u0006\u0000\u0000\u009a\u0099\u0001\u0000\u0000"+
		"\u0000\u009b\u009e\u0001\u0000\u0000\u0000\u009c\u009a\u0001\u0000\u0000"+
		"\u0000\u009c\u009d\u0001\u0000\u0000\u0000\u009d0\u0001\u0000\u0000\u0000"+
		"\u009e\u009c\u0001\u0000\u0000\u0000\u009f\u00a3\u0005\"\u0000\u0000\u00a0"+
		"\u00a2\b\u0007\u0000\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a5"+
		"\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4"+
		"\u0001\u0000\u0000\u0000\u00a4\u00a6\u0001\u0000\u0000\u0000\u00a5\u00a3"+
		"\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005\"\u0000\u0000\u00a72\u0001"+
		"\u0000\u0000\u0000\u00a8\u00aa\u0007\b\u0000\u0000\u00a9\u00a8\u0001\u0000"+
		"\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000"+
		"\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u00ad\u0001\u0000"+
		"\u0000\u0000\u00ad\u00ae\u0006\u0019\u0000\u0000\u00ae4\u0001\u0000\u0000"+
		"\u0000\u00af\u00b3\u0005#\u0000\u0000\u00b0\u00b2\b\t\u0000\u0000\u00b1"+
		"\u00b0\u0001\u0000\u0000\u0000\u00b2\u00b5\u0001\u0000\u0000\u0000\u00b3"+
		"\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b4"+
		"\u00b6\u0001\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b6"+
		"\u00b7\u0006\u001a\u0000\u0000\u00b76\u0001\u0000\u0000\u0000\t\u0000"+
		":EPY\u009c\u00a3\u00ab\u00b3\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}