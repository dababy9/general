// Generated from si413/spl/SPLParser.g4 by ANTLR 4.13.1
package si413.spl;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class SPLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NUM=1, BOOL=2, OPA=3, OPM=4, BOP=5, NOT=6, COMP=7, ASN=8, FUNARG=9, LP=10, 
		RP=11, LC=12, RC=13, STOP=14, IF=15, IFELSE=16, WHILE=17, READ=18, WRITE=19, 
		LAMBDA=20, CLASS=21, OBJ=22, NEW=23, ID=24, DEBUG=25, SPACE=26, COMMENT=27;
	public static final int
		RULE_prog = 0, RULE_stlist = 1, RULE_stmt = 2, RULE_block = 3, RULE_exp = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "stlist", "stmt", "block", "exp"
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

	@Override
	public String getGrammarFileName() { return "SPLParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SPLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public StlistContext stlist() {
			return getRuleContext(StlistContext.class,0);
		}
		public TerminalNode EOF() { return getToken(SPLParser.EOF, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			stlist(0);
			setState(11);
			match(EOF);
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
	public static class StlistContext extends ParserRuleContext {
		public StlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stlist; }
	 
		public StlistContext() { }
		public void copyFrom(StlistContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NullStmtContext extends StlistContext {
		public NullStmtContext(StlistContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterNullStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitNullStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitNullStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NormalStmtContext extends StlistContext {
		public StlistContext stlist() {
			return getRuleContext(StlistContext.class,0);
		}
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public NormalStmtContext(StlistContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterNormalStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitNormalStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitNormalStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StlistContext stlist() throws RecognitionException {
		return stlist(0);
	}

	private StlistContext stlist(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StlistContext _localctx = new StlistContext(_ctx, _parentState);
		StlistContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_stlist, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new NullStmtContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			}
			_ctx.stop = _input.LT(-1);
			setState(18);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NormalStmtContext(new StlistContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_stlist);
					setState(14);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(15);
					stmt();
					}
					} 
				}
				setState(20);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
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

	@SuppressWarnings("CheckReturnValue")
	public static class StmtContext extends ParserRuleContext {
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	 
		public StmtContext() { }
		public void copyFrom(StmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WriteContext extends StmtContext {
		public TerminalNode WRITE() { return getToken(SPLParser.WRITE, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode STOP() { return getToken(SPLParser.STOP, 0); }
		public WriteContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterWrite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitWrite(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitWrite(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ExpStmtContext extends StmtContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode STOP() { return getToken(SPLParser.STOP, 0); }
		public ExpStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterExpStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitExpStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitExpStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStmtContext extends StmtContext {
		public TerminalNode IF() { return getToken(SPLParser.IF, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public IfStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterIfStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitIfStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitIfStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfElseStmtContext extends StmtContext {
		public TerminalNode IFELSE() { return getToken(SPLParser.IFELSE, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}
		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class,i);
		}
		public IfElseStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterIfElseStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitIfElseStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitIfElseStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileStmtContext extends StmtContext {
		public TerminalNode WHILE() { return getToken(SPLParser.WHILE, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterWhileStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitWhileStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitWhileStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewVarContext extends StmtContext {
		public TerminalNode NEW() { return getToken(SPLParser.NEW, 0); }
		public TerminalNode ID() { return getToken(SPLParser.ID, 0); }
		public TerminalNode ASN() { return getToken(SPLParser.ASN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode STOP() { return getToken(SPLParser.STOP, 0); }
		public NewVarContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterNewVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitNewVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitNewVar(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockStmtContext extends StmtContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public BlockStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterBlockStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitBlockStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitBlockStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DebugStmtContext extends StmtContext {
		public TerminalNode DEBUG() { return getToken(SPLParser.DEBUG, 0); }
		public DebugStmtContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterDebugStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitDebugStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitDebugStmt(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AsnContext extends StmtContext {
		public TerminalNode ID() { return getToken(SPLParser.ID, 0); }
		public TerminalNode ASN() { return getToken(SPLParser.ASN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode STOP() { return getToken(SPLParser.STOP, 0); }
		public AsnContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterAsn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitAsn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitAsn(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_stmt);
		try {
			setState(54);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new NewVarContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(21);
				match(NEW);
				setState(22);
				match(ID);
				setState(23);
				match(ASN);
				setState(24);
				exp(0);
				setState(25);
				match(STOP);
				}
				break;
			case 2:
				_localctx = new AsnContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				match(ID);
				setState(28);
				match(ASN);
				setState(29);
				exp(0);
				setState(30);
				match(STOP);
				}
				break;
			case 3:
				_localctx = new WriteContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(32);
				match(WRITE);
				setState(33);
				exp(0);
				setState(34);
				match(STOP);
				}
				break;
			case 4:
				_localctx = new IfStmtContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(36);
				match(IF);
				setState(37);
				exp(0);
				setState(38);
				block();
				}
				break;
			case 5:
				_localctx = new IfElseStmtContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(40);
				match(IFELSE);
				setState(41);
				exp(0);
				setState(42);
				block();
				setState(43);
				block();
				}
				break;
			case 6:
				_localctx = new WhileStmtContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(45);
				match(WHILE);
				setState(46);
				exp(0);
				setState(47);
				block();
				}
				break;
			case 7:
				_localctx = new BlockStmtContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(49);
				block();
				}
				break;
			case 8:
				_localctx = new DebugStmtContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(50);
				match(DEBUG);
				}
				break;
			case 9:
				_localctx = new ExpStmtContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(51);
				exp(0);
				setState(52);
				match(STOP);
				}
				break;
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
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LC() { return getToken(SPLParser.LC, 0); }
		public StlistContext stlist() {
			return getRuleContext(StlistContext.class,0);
		}
		public TerminalNode RC() { return getToken(SPLParser.RC, 0); }
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(LC);
			setState(57);
			stlist(0);
			setState(58);
			match(RC);
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
	public static class ExpContext extends ParserRuleContext {
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
	 
		public ExpContext() { }
		public void copyFrom(ExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddOpContext extends ExpContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode OPA() { return getToken(SPLParser.OPA, 0); }
		public AddOpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterAddOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitAddOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitAddOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ClassRefContext extends ExpContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode OBJ() { return getToken(SPLParser.OBJ, 0); }
		public TerminalNode ID() { return getToken(SPLParser.ID, 0); }
		public ClassRefContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterClassRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitClassRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitClassRef(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParensContext extends ExpContext {
		public TerminalNode LP() { return getToken(SPLParser.LP, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RP() { return getToken(SPLParser.RP, 0); }
		public ParensContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterParens(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitParens(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitParens(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumContext extends ExpContext {
		public TerminalNode NUM() { return getToken(SPLParser.NUM, 0); }
		public NumContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterNum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitNum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitNum(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NegOpContext extends ExpContext {
		public TerminalNode OPA() { return getToken(SPLParser.OPA, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public NegOpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterNegOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitNegOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitNegOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotOpContext extends ExpContext {
		public TerminalNode NOT() { return getToken(SPLParser.NOT, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public NotOpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterNotOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitNotOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitNotOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReadContext extends ExpContext {
		public TerminalNode READ() { return getToken(SPLParser.READ, 0); }
		public ReadContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterRead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitRead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitRead(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SubClassDeclContext extends ExpContext {
		public TerminalNode CLASS() { return getToken(SPLParser.CLASS, 0); }
		public TerminalNode ID() { return getToken(SPLParser.ID, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public SubClassDeclContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterSubClassDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitSubClassDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitSubClassDecl(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ClassDeclContext extends ExpContext {
		public TerminalNode CLASS() { return getToken(SPLParser.CLASS, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ClassDeclContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterClassDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitClassDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitClassDecl(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunCallContext extends ExpContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode FUNARG() { return getToken(SPLParser.FUNARG, 0); }
		public FunCallContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterFunCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitFunCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitFunCall(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CompOpContext extends ExpContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode COMP() { return getToken(SPLParser.COMP, 0); }
		public CompOpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterCompOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitCompOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitCompOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolContext extends ExpContext {
		public TerminalNode BOOL() { return getToken(SPLParser.BOOL, 0); }
		public BoolContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitBool(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulOpContext extends ExpContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode OPM() { return getToken(SPLParser.OPM, 0); }
		public MulOpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterMulOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitMulOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitMulOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ExpContext {
		public TerminalNode ID() { return getToken(SPLParser.ID, 0); }
		public IdContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolOpContext extends ExpContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode BOP() { return getToken(SPLParser.BOP, 0); }
		public BoolOpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterBoolOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitBoolOp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitBoolOp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewClassContext extends ExpContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode OBJ() { return getToken(SPLParser.OBJ, 0); }
		public NewClassContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterNewClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitNewClass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitNewClass(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LambdaContext extends ExpContext {
		public TerminalNode LAMBDA() { return getToken(SPLParser.LAMBDA, 0); }
		public TerminalNode ID() { return getToken(SPLParser.ID, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public LambdaContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).enterLambda(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SPLParserListener ) ((SPLParserListener)listener).exitLambda(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SPLParserVisitor ) return ((SPLParserVisitor<? extends T>)visitor).visitLambda(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		return exp(0);
	}

	private ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState);
		ExpContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_exp, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				_localctx = new NumContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(61);
				match(NUM);
				}
				break;
			case 2:
				{
				_localctx = new BoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(62);
				match(BOOL);
				}
				break;
			case 3:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(63);
				match(ID);
				}
				break;
			case 4:
				{
				_localctx = new ParensContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64);
				match(LP);
				setState(65);
				exp(0);
				setState(66);
				match(RP);
				}
				break;
			case 5:
				{
				_localctx = new ReadContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(68);
				match(READ);
				}
				break;
			case 6:
				{
				_localctx = new LambdaContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(69);
				match(LAMBDA);
				setState(70);
				match(ID);
				setState(71);
				block();
				}
				break;
			case 7:
				{
				_localctx = new ClassDeclContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(72);
				match(CLASS);
				setState(73);
				block();
				}
				break;
			case 8:
				{
				_localctx = new SubClassDeclContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(74);
				match(CLASS);
				setState(75);
				match(ID);
				setState(76);
				block();
				}
				break;
			case 9:
				{
				_localctx = new NegOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(77);
				match(OPA);
				setState(78);
				exp(5);
				}
				break;
			case 10:
				{
				_localctx = new NotOpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(79);
				match(NOT);
				setState(80);
				exp(2);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(105);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(103);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
					case 1:
						{
						_localctx = new FunCallContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(83);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(84);
						match(FUNARG);
						setState(85);
						exp(8);
						}
						break;
					case 2:
						{
						_localctx = new MulOpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(86);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(87);
						match(OPM);
						setState(88);
						exp(7);
						}
						break;
					case 3:
						{
						_localctx = new AddOpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(89);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(90);
						match(OPA);
						setState(91);
						exp(5);
						}
						break;
					case 4:
						{
						_localctx = new CompOpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(92);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(93);
						match(COMP);
						setState(94);
						exp(4);
						}
						break;
					case 5:
						{
						_localctx = new BoolOpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(95);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(96);
						match(BOP);
						setState(97);
						exp(2);
						}
						break;
					case 6:
						{
						_localctx = new NewClassContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(98);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(99);
						match(OBJ);
						}
						break;
					case 7:
						{
						_localctx = new ClassRefContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(100);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(101);
						match(OBJ);
						setState(102);
						match(ID);
						}
						break;
					}
					} 
				}
				setState(107);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
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
			return stlist_sempred((StlistContext)_localctx, predIndex);
		case 4:
			return exp_sempred((ExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean stlist_sempred(StlistContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 1);
		case 6:
			return precpred(_ctx, 9);
		case 7:
			return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001bm\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0005"+
		"\u0001\u0011\b\u0001\n\u0001\f\u0001\u0014\t\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0003\u00027\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004R\b\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0005\u0004h\b\u0004\n\u0004\f\u0004k\t\u0004"+
		"\u0001\u0004\u0000\u0002\u0002\b\u0005\u0000\u0002\u0004\u0006\b\u0000"+
		"\u0000\u0080\u0000\n\u0001\u0000\u0000\u0000\u0002\r\u0001\u0000\u0000"+
		"\u0000\u00046\u0001\u0000\u0000\u0000\u00068\u0001\u0000\u0000\u0000\b"+
		"Q\u0001\u0000\u0000\u0000\n\u000b\u0003\u0002\u0001\u0000\u000b\f\u0005"+
		"\u0000\u0000\u0001\f\u0001\u0001\u0000\u0000\u0000\r\u0012\u0006\u0001"+
		"\uffff\uffff\u0000\u000e\u000f\n\u0002\u0000\u0000\u000f\u0011\u0003\u0004"+
		"\u0002\u0000\u0010\u000e\u0001\u0000\u0000\u0000\u0011\u0014\u0001\u0000"+
		"\u0000\u0000\u0012\u0010\u0001\u0000\u0000\u0000\u0012\u0013\u0001\u0000"+
		"\u0000\u0000\u0013\u0003\u0001\u0000\u0000\u0000\u0014\u0012\u0001\u0000"+
		"\u0000\u0000\u0015\u0016\u0005\u0017\u0000\u0000\u0016\u0017\u0005\u0018"+
		"\u0000\u0000\u0017\u0018\u0005\b\u0000\u0000\u0018\u0019\u0003\b\u0004"+
		"\u0000\u0019\u001a\u0005\u000e\u0000\u0000\u001a7\u0001\u0000\u0000\u0000"+
		"\u001b\u001c\u0005\u0018\u0000\u0000\u001c\u001d\u0005\b\u0000\u0000\u001d"+
		"\u001e\u0003\b\u0004\u0000\u001e\u001f\u0005\u000e\u0000\u0000\u001f7"+
		"\u0001\u0000\u0000\u0000 !\u0005\u0013\u0000\u0000!\"\u0003\b\u0004\u0000"+
		"\"#\u0005\u000e\u0000\u0000#7\u0001\u0000\u0000\u0000$%\u0005\u000f\u0000"+
		"\u0000%&\u0003\b\u0004\u0000&\'\u0003\u0006\u0003\u0000\'7\u0001\u0000"+
		"\u0000\u0000()\u0005\u0010\u0000\u0000)*\u0003\b\u0004\u0000*+\u0003\u0006"+
		"\u0003\u0000+,\u0003\u0006\u0003\u0000,7\u0001\u0000\u0000\u0000-.\u0005"+
		"\u0011\u0000\u0000./\u0003\b\u0004\u0000/0\u0003\u0006\u0003\u000007\u0001"+
		"\u0000\u0000\u000017\u0003\u0006\u0003\u000027\u0005\u0019\u0000\u0000"+
		"34\u0003\b\u0004\u000045\u0005\u000e\u0000\u000057\u0001\u0000\u0000\u0000"+
		"6\u0015\u0001\u0000\u0000\u00006\u001b\u0001\u0000\u0000\u00006 \u0001"+
		"\u0000\u0000\u00006$\u0001\u0000\u0000\u00006(\u0001\u0000\u0000\u0000"+
		"6-\u0001\u0000\u0000\u000061\u0001\u0000\u0000\u000062\u0001\u0000\u0000"+
		"\u000063\u0001\u0000\u0000\u00007\u0005\u0001\u0000\u0000\u000089\u0005"+
		"\f\u0000\u00009:\u0003\u0002\u0001\u0000:;\u0005\r\u0000\u0000;\u0007"+
		"\u0001\u0000\u0000\u0000<=\u0006\u0004\uffff\uffff\u0000=R\u0005\u0001"+
		"\u0000\u0000>R\u0005\u0002\u0000\u0000?R\u0005\u0018\u0000\u0000@A\u0005"+
		"\n\u0000\u0000AB\u0003\b\u0004\u0000BC\u0005\u000b\u0000\u0000CR\u0001"+
		"\u0000\u0000\u0000DR\u0005\u0012\u0000\u0000EF\u0005\u0014\u0000\u0000"+
		"FG\u0005\u0018\u0000\u0000GR\u0003\u0006\u0003\u0000HI\u0005\u0015\u0000"+
		"\u0000IR\u0003\u0006\u0003\u0000JK\u0005\u0015\u0000\u0000KL\u0005\u0018"+
		"\u0000\u0000LR\u0003\u0006\u0003\u0000MN\u0005\u0003\u0000\u0000NR\u0003"+
		"\b\u0004\u0005OP\u0005\u0006\u0000\u0000PR\u0003\b\u0004\u0002Q<\u0001"+
		"\u0000\u0000\u0000Q>\u0001\u0000\u0000\u0000Q?\u0001\u0000\u0000\u0000"+
		"Q@\u0001\u0000\u0000\u0000QD\u0001\u0000\u0000\u0000QE\u0001\u0000\u0000"+
		"\u0000QH\u0001\u0000\u0000\u0000QJ\u0001\u0000\u0000\u0000QM\u0001\u0000"+
		"\u0000\u0000QO\u0001\u0000\u0000\u0000Ri\u0001\u0000\u0000\u0000ST\n\u0007"+
		"\u0000\u0000TU\u0005\t\u0000\u0000Uh\u0003\b\u0004\bVW\n\u0006\u0000\u0000"+
		"WX\u0005\u0004\u0000\u0000Xh\u0003\b\u0004\u0007YZ\n\u0004\u0000\u0000"+
		"Z[\u0005\u0003\u0000\u0000[h\u0003\b\u0004\u0005\\]\n\u0003\u0000\u0000"+
		"]^\u0005\u0007\u0000\u0000^h\u0003\b\u0004\u0004_`\n\u0001\u0000\u0000"+
		"`a\u0005\u0005\u0000\u0000ah\u0003\b\u0004\u0002bc\n\t\u0000\u0000ch\u0005"+
		"\u0016\u0000\u0000de\n\b\u0000\u0000ef\u0005\u0016\u0000\u0000fh\u0005"+
		"\u0018\u0000\u0000gS\u0001\u0000\u0000\u0000gV\u0001\u0000\u0000\u0000"+
		"gY\u0001\u0000\u0000\u0000g\\\u0001\u0000\u0000\u0000g_\u0001\u0000\u0000"+
		"\u0000gb\u0001\u0000\u0000\u0000gd\u0001\u0000\u0000\u0000hk\u0001\u0000"+
		"\u0000\u0000ig\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000\u0000j\t\u0001"+
		"\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000\u0005\u00126Qgi";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}