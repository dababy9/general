// Generated from si413/spl/SPLParser.g4 by ANTLR 4.13.1
package si413.spl;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SPLParser}.
 */
public interface SPLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SPLParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(SPLParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(SPLParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NullStmt}
	 * labeled alternative in {@link SPLParser#stlist}.
	 * @param ctx the parse tree
	 */
	void enterNullStmt(SPLParser.NullStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NullStmt}
	 * labeled alternative in {@link SPLParser#stlist}.
	 * @param ctx the parse tree
	 */
	void exitNullStmt(SPLParser.NullStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NormalStmt}
	 * labeled alternative in {@link SPLParser#stlist}.
	 * @param ctx the parse tree
	 */
	void enterNormalStmt(SPLParser.NormalStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NormalStmt}
	 * labeled alternative in {@link SPLParser#stlist}.
	 * @param ctx the parse tree
	 */
	void exitNormalStmt(SPLParser.NormalStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NewVar}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterNewVar(SPLParser.NewVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NewVar}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitNewVar(SPLParser.NewVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Asn}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterAsn(SPLParser.AsnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Asn}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitAsn(SPLParser.AsnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Write}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWrite(SPLParser.WriteContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Write}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWrite(SPLParser.WriteContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(SPLParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(SPLParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfElseStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfElseStmt(SPLParser.IfElseStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfElseStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfElseStmt(SPLParser.IfElseStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(SPLParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(SPLParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExpStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpStmt(SPLParser.ExpStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExpStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpStmt(SPLParser.ExpStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlockStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmt(SPLParser.BlockStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlockStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmt(SPLParser.BlockStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DebugStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterDebugStmt(SPLParser.DebugStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DebugStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitDebugStmt(SPLParser.DebugStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SPLParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(SPLParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SPLParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(SPLParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterAddOp(SPLParser.AddOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitAddOp(SPLParser.AddOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterParens(SPLParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitParens(SPLParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Num}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNum(SPLParser.NumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Num}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNum(SPLParser.NumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NegOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNegOp(SPLParser.NegOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NegOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNegOp(SPLParser.NegOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNotOp(SPLParser.NotOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNotOp(SPLParser.NotOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Read}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterRead(SPLParser.ReadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Read}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitRead(SPLParser.ReadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunCall}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterFunCall(SPLParser.FunCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunCall}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitFunCall(SPLParser.FunCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CompOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterCompOp(SPLParser.CompOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CompOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitCompOp(SPLParser.CompOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bool}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterBool(SPLParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bool}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitBool(SPLParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterMulOp(SPLParser.MulOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitMulOp(SPLParser.MulOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Id}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterId(SPLParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitId(SPLParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterBoolOp(SPLParser.BoolOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitBoolOp(SPLParser.BoolOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Lambda}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterLambda(SPLParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Lambda}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitLambda(SPLParser.LambdaContext ctx);
}