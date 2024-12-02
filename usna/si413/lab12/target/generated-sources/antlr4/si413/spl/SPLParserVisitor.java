// Generated from si413/spl/SPLParser.g4 by ANTLR 4.13.1
package si413.spl;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SPLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SPLParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SPLParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(SPLParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullStmt}
	 * labeled alternative in {@link SPLParser#stlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullStmt(SPLParser.NullStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NormalStmt}
	 * labeled alternative in {@link SPLParser#stlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormalStmt(SPLParser.NormalStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewVar}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewVar(SPLParser.NewVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Asn}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsn(SPLParser.AsnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Write}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrite(SPLParser.WriteContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStmt(SPLParser.IfStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfElseStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseStmt(SPLParser.IfElseStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStmt(SPLParser.WhileStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStmt(SPLParser.BlockStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DebugStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDebugStmt(SPLParser.DebugStmtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExpStmt}
	 * labeled alternative in {@link SPLParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpStmt(SPLParser.ExpStmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SPLParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(SPLParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOp(SPLParser.AddOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ClassRef}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassRef(SPLParser.ClassRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parens}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(SPLParser.ParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Num}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNum(SPLParser.NumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegOp(SPLParser.NegOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotOp(SPLParser.NotOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Read}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRead(SPLParser.ReadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubClassDecl}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubClassDecl(SPLParser.SubClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ClassDecl}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDecl(SPLParser.ClassDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunCall}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunCall(SPLParser.FunCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompOp(SPLParser.CompOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Bool}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(SPLParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulOp(SPLParser.MulOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(SPLParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolOp}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolOp(SPLParser.BoolOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewClass}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewClass(SPLParser.NewClassContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Lambda}
	 * labeled alternative in {@link SPLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambda(SPLParser.LambdaContext ctx);
}