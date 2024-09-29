// Generated from si413/pat/PatParser.g4 by ANTLR 4.13.1
package si413.pat;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class PatParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SYM=1, FOLD=2, STOP=3, COLON=4, NAME=5, REV=6, LB=7, RB=8, WHITE=9;
	public static final int
		RULE_prog = 0, RULE_seq = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "seq"
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

	@Override
	public String getGrammarFileName() { return "PatParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PatParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
	 
		public ProgContext() { }
		public void copyFrom(ProgContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NonemptyProgContext extends ProgContext {
		public SeqContext seq() {
			return getRuleContext(SeqContext.class,0);
		}
		public TerminalNode STOP() { return getToken(PatParser.STOP, 0); }
		public ProgContext prog() {
			return getRuleContext(ProgContext.class,0);
		}
		public NonemptyProgContext(ProgContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterNonemptyProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitNonemptyProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitNonemptyProg(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyProgContext extends ProgContext {
		public TerminalNode EOF() { return getToken(PatParser.EOF, 0); }
		public EmptyProgContext(ProgContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterEmptyProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitEmptyProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitEmptyProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			setState(9);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SYM:
			case NAME:
			case LB:
				_localctx = new NonemptyProgContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(4);
				seq(0);
				setState(5);
				match(STOP);
				setState(6);
				prog();
				}
				break;
			case EOF:
				_localctx = new EmptyProgContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(8);
				match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SeqContext extends ParserRuleContext {
		public SeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_seq; }
	 
		public SeqContext() { }
		public void copyFrom(SeqContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConcatContext extends SeqContext {
		public List<SeqContext> seq() {
			return getRuleContexts(SeqContext.class);
		}
		public SeqContext seq(int i) {
			return getRuleContext(SeqContext.class,i);
		}
		public ConcatContext(SeqContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterConcat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitConcat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitConcat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FoldContext extends SeqContext {
		public List<SeqContext> seq() {
			return getRuleContexts(SeqContext.class);
		}
		public SeqContext seq(int i) {
			return getRuleContext(SeqContext.class,i);
		}
		public TerminalNode FOLD() { return getToken(PatParser.FOLD, 0); }
		public FoldContext(SeqContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterFold(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitFold(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitFold(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VariableContext extends SeqContext {
		public TerminalNode NAME() { return getToken(PatParser.NAME, 0); }
		public VariableContext(SeqContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BracesContext extends SeqContext {
		public TerminalNode LB() { return getToken(PatParser.LB, 0); }
		public SeqContext seq() {
			return getRuleContext(SeqContext.class,0);
		}
		public TerminalNode RB() { return getToken(PatParser.RB, 0); }
		public BracesContext(SeqContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterBraces(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitBraces(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitBraces(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReverseContext extends SeqContext {
		public SeqContext seq() {
			return getRuleContext(SeqContext.class,0);
		}
		public TerminalNode REV() { return getToken(PatParser.REV, 0); }
		public ReverseContext(SeqContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterReverse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitReverse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitReverse(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SymbolContext extends SeqContext {
		public TerminalNode SYM() { return getToken(PatParser.SYM, 0); }
		public SymbolContext(SeqContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitSymbol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitSymbol(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarAssignContext extends SeqContext {
		public SeqContext seq() {
			return getRuleContext(SeqContext.class,0);
		}
		public TerminalNode COLON() { return getToken(PatParser.COLON, 0); }
		public TerminalNode NAME() { return getToken(PatParser.NAME, 0); }
		public VarAssignContext(SeqContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).enterVarAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PatParserListener ) ((PatParserListener)listener).exitVarAssign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PatParserVisitor ) return ((PatParserVisitor<? extends T>)visitor).visitVarAssign(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SeqContext seq() throws RecognitionException {
		return seq(0);
	}

	private SeqContext seq(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		SeqContext _localctx = new SeqContext(_ctx, _parentState);
		SeqContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_seq, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SYM:
				{
				_localctx = new SymbolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(12);
				match(SYM);
				}
				break;
			case NAME:
				{
				_localctx = new VariableContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(13);
				match(NAME);
				}
				break;
			case LB:
				{
				_localctx = new BracesContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(14);
				match(LB);
				setState(15);
				seq(0);
				setState(16);
				match(RB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(32);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(30);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new ConcatContext(new SeqContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_seq);
						setState(20);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(21);
						seq(3);
						}
						break;
					case 2:
						{
						_localctx = new FoldContext(new SeqContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_seq);
						setState(22);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(23);
						match(FOLD);
						setState(24);
						seq(2);
						}
						break;
					case 3:
						{
						_localctx = new ReverseContext(new SeqContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_seq);
						setState(25);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(26);
						match(REV);
						}
						break;
					case 4:
						{
						_localctx = new VarAssignContext(new SeqContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_seq);
						setState(27);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(28);
						match(COLON);
						setState(29);
						match(NAME);
						}
						break;
					}
					} 
				}
				setState(34);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return seq_sempred((SeqContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean seq_sempred(SeqContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 1);
		case 2:
			return precpred(_ctx, 4);
		case 3:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\t$\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\n\b"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0003\u0001\u0013\b\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0005\u0001\u001f\b\u0001\n\u0001\f\u0001\"\t\u0001"+
		"\u0001\u0001\u0000\u0001\u0002\u0002\u0000\u0002\u0000\u0000(\u0000\t"+
		"\u0001\u0000\u0000\u0000\u0002\u0012\u0001\u0000\u0000\u0000\u0004\u0005"+
		"\u0003\u0002\u0001\u0000\u0005\u0006\u0005\u0003\u0000\u0000\u0006\u0007"+
		"\u0003\u0000\u0000\u0000\u0007\n\u0001\u0000\u0000\u0000\b\n\u0005\u0000"+
		"\u0000\u0001\t\u0004\u0001\u0000\u0000\u0000\t\b\u0001\u0000\u0000\u0000"+
		"\n\u0001\u0001\u0000\u0000\u0000\u000b\f\u0006\u0001\uffff\uffff\u0000"+
		"\f\u0013\u0005\u0001\u0000\u0000\r\u0013\u0005\u0005\u0000\u0000\u000e"+
		"\u000f\u0005\u0007\u0000\u0000\u000f\u0010\u0003\u0002\u0001\u0000\u0010"+
		"\u0011\u0005\b\u0000\u0000\u0011\u0013\u0001\u0000\u0000\u0000\u0012\u000b"+
		"\u0001\u0000\u0000\u0000\u0012\r\u0001\u0000\u0000\u0000\u0012\u000e\u0001"+
		"\u0000\u0000\u0000\u0013 \u0001\u0000\u0000\u0000\u0014\u0015\n\u0002"+
		"\u0000\u0000\u0015\u001f\u0003\u0002\u0001\u0003\u0016\u0017\n\u0001\u0000"+
		"\u0000\u0017\u0018\u0005\u0002\u0000\u0000\u0018\u001f\u0003\u0002\u0001"+
		"\u0002\u0019\u001a\n\u0004\u0000\u0000\u001a\u001f\u0005\u0006\u0000\u0000"+
		"\u001b\u001c\n\u0003\u0000\u0000\u001c\u001d\u0005\u0004\u0000\u0000\u001d"+
		"\u001f\u0005\u0005\u0000\u0000\u001e\u0014\u0001\u0000\u0000\u0000\u001e"+
		"\u0016\u0001\u0000\u0000\u0000\u001e\u0019\u0001\u0000\u0000\u0000\u001e"+
		"\u001b\u0001\u0000\u0000\u0000\u001f\"\u0001\u0000\u0000\u0000 \u001e"+
		"\u0001\u0000\u0000\u0000 !\u0001\u0000\u0000\u0000!\u0003\u0001\u0000"+
		"\u0000\u0000\" \u0001\u0000\u0000\u0000\u0004\t\u0012\u001e ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}