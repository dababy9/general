// Generated from si413/pat/PatLexer.g4 by ANTLR 4.13.1
package si413.pat;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class PatLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SYM=1, FOLD=2, STOP=3, COLON=4, NAME=5, REV=6, LB=7, RB=8, WHITE=9;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"SYM", "FOLD", "STOP", "COLON", "NAME", "REV", "LB", "RB", "WHITE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'*'", "';'", "':'", null, "'_r'", "'['", "']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "SYM", "FOLD", "STOP", "COLON", "NAME", "REV", "LB", "RB", "WHITE"
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


	public PatLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PatLexer.g4"; }

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
		"\u0004\u0000\t5\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0001\u0000\u0001\u0000\u0005\u0000\u0016"+
		"\b\u0000\n\u0000\f\u0000\u0019\t\u0000\u0001\u0001\u0001\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0005\u0004"+
		"#\b\u0004\n\u0004\f\u0004&\t\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0004\b0\b\b"+
		"\u000b\b\f\b1\u0001\b\u0001\b\u0000\u0000\t\u0001\u0001\u0003\u0002\u0005"+
		"\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0001\u0000"+
		"\u0004\u0001\u0000az\u0003\u000009AZaz\u0001\u0000AZ\u0003\u0000\t\n\r"+
		"\r  7\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000"+
		"\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000"+
		"\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000"+
		"\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000"+
		"\u0011\u0001\u0000\u0000\u0000\u0001\u0013\u0001\u0000\u0000\u0000\u0003"+
		"\u001a\u0001\u0000\u0000\u0000\u0005\u001c\u0001\u0000\u0000\u0000\u0007"+
		"\u001e\u0001\u0000\u0000\u0000\t \u0001\u0000\u0000\u0000\u000b\'\u0001"+
		"\u0000\u0000\u0000\r*\u0001\u0000\u0000\u0000\u000f,\u0001\u0000\u0000"+
		"\u0000\u0011/\u0001\u0000\u0000\u0000\u0013\u0017\u0007\u0000\u0000\u0000"+
		"\u0014\u0016\u0007\u0001\u0000\u0000\u0015\u0014\u0001\u0000\u0000\u0000"+
		"\u0016\u0019\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000"+
		"\u0017\u0018\u0001\u0000\u0000\u0000\u0018\u0002\u0001\u0000\u0000\u0000"+
		"\u0019\u0017\u0001\u0000\u0000\u0000\u001a\u001b\u0005*\u0000\u0000\u001b"+
		"\u0004\u0001\u0000\u0000\u0000\u001c\u001d\u0005;\u0000\u0000\u001d\u0006"+
		"\u0001\u0000\u0000\u0000\u001e\u001f\u0005:\u0000\u0000\u001f\b\u0001"+
		"\u0000\u0000\u0000 $\u0007\u0002\u0000\u0000!#\u0007\u0001\u0000\u0000"+
		"\"!\u0001\u0000\u0000\u0000#&\u0001\u0000\u0000\u0000$\"\u0001\u0000\u0000"+
		"\u0000$%\u0001\u0000\u0000\u0000%\n\u0001\u0000\u0000\u0000&$\u0001\u0000"+
		"\u0000\u0000\'(\u0005_\u0000\u0000()\u0005r\u0000\u0000)\f\u0001\u0000"+
		"\u0000\u0000*+\u0005[\u0000\u0000+\u000e\u0001\u0000\u0000\u0000,-\u0005"+
		"]\u0000\u0000-\u0010\u0001\u0000\u0000\u0000.0\u0007\u0003\u0000\u0000"+
		"/.\u0001\u0000\u0000\u000001\u0001\u0000\u0000\u00001/\u0001\u0000\u0000"+
		"\u000012\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u000034\u0006\b\u0000"+
		"\u00004\u0012\u0001\u0000\u0000\u0000\u0004\u0000\u0017$1\u0001\u0006"+
		"\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}