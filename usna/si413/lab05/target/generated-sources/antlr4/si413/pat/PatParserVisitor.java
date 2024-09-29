// Generated from si413/pat/PatParser.g4 by ANTLR 4.13.1
package si413.pat;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PatParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PatParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code NonemptyProg}
	 * labeled alternative in {@link PatParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonemptyProg(PatParser.NonemptyProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EmptyProg}
	 * labeled alternative in {@link PatParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyProg(PatParser.EmptyProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Concat}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcat(PatParser.ConcatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Fold}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFold(PatParser.FoldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(PatParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Braces}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBraces(PatParser.BracesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Reverse}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReverse(PatParser.ReverseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Symbol}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbol(PatParser.SymbolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarAssign}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarAssign(PatParser.VarAssignContext ctx);
}