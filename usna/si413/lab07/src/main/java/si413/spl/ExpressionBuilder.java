package si413.spl;

import si413.spl.ast.*;

/** Parse Tree to AST conversion functions for nodes that represent expressions.
 * Each visitXXX(SPLParser.XXXContext) method here should return an
 * AST node that is a subclass of Expression, from the corresponding parse
 * tree node.
 */
public class ExpressionBuilder extends SPLParserBaseVisitor<Expression> {

    @Override
    public Expression visitBool(SPLParser.BoolContext ctx) {
        String literal = ctx.BOOL().getText();
        return new Bool(literal.equals("true"));
    }

    @Override
    public Expression visitBoolOp(SPLParser.BoolOpContext ctx) {
        Expression lhs = visit(ctx.exp(0));
        Expression rhs = visit(ctx.exp(1));
        String op = ctx.BOP().getText();
        return new BoolOp(lhs, op, rhs);
    }

    @Override
    public Expression visitNotOp(SPLParser.NotOpContext ctx) {
        return new NotOp(visit(ctx.exp()));
    }

    @Override
    public Expression visitNum(SPLParser.NumContext ctx) {
        return new Num(Integer.parseInt(ctx.NUM().getText()));
    }

    @Override
    public Expression visitId(SPLParser.IdContext ctx) {
        return new Id(ctx.ID().getText());
    }

    @Override
    public Expression visitParens(SPLParser.ParensContext ctx) {
        return visit(ctx.exp());
    }

    @Override
    public Expression visitRead(SPLParser.ReadContext ctx) {
        return new Read();
    }

    @Override
    public Expression visitMulOp(SPLParser.MulOpContext ctx) {
        return new ArithOp(visit(ctx.exp(0)), ctx.OPM().getText(), visit(ctx.exp(1)));
    }

    @Override
    public Expression visitNegOp(SPLParser.NegOpContext ctx) {
        return new NegOp(visit(ctx.exp()));
    }

    @Override
    public Expression visitAddOp(SPLParser.AddOpContext ctx) {
        return new ArithOp(visit(ctx.exp(0)), ctx.OPA().getText(), visit(ctx.exp(1)));
    }

    @Override
    public Expression visitCompOp(SPLParser.CompOpContext ctx) {
        return new CompOp(visit(ctx.exp(0)), ctx.COMP().getText(), visit(ctx.exp(1)));
    }
}
