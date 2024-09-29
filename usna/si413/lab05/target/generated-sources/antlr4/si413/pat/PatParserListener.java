// Generated from si413/pat/PatParser.g4 by ANTLR 4.13.1
package si413.pat;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PatParser}.
 */
public interface PatParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code NonemptyProg}
	 * labeled alternative in {@link PatParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterNonemptyProg(PatParser.NonemptyProgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NonemptyProg}
	 * labeled alternative in {@link PatParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitNonemptyProg(PatParser.NonemptyProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EmptyProg}
	 * labeled alternative in {@link PatParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterEmptyProg(PatParser.EmptyProgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EmptyProg}
	 * labeled alternative in {@link PatParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitEmptyProg(PatParser.EmptyProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Concat}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterConcat(PatParser.ConcatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Concat}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitConcat(PatParser.ConcatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Fold}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterFold(PatParser.FoldContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Fold}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitFold(PatParser.FoldContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterVariable(PatParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variable}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitVariable(PatParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Braces}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterBraces(PatParser.BracesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Braces}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitBraces(PatParser.BracesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Reverse}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterReverse(PatParser.ReverseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Reverse}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitReverse(PatParser.ReverseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Symbol}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterSymbol(PatParser.SymbolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Symbol}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitSymbol(PatParser.SymbolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarAssign}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void enterVarAssign(PatParser.VarAssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarAssign}
	 * labeled alternative in {@link PatParser#seq}.
	 * @param ctx the parse tree
	 */
	void exitVarAssign(PatParser.VarAssignContext ctx);
}