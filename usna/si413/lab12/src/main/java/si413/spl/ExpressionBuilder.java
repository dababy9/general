package si413.spl;

import si413.spl.ast.*;

/** Parse Tree to AST conversion functions for nodes that represent expressions.
 * Each visitXXX(SPLParser.XXXContext) method here should return an
 * AST node that is a subclass of Expression, from the corresponding parse
 * tree node.
 */
public class ExpressionBuilder extends SPLParserBaseVisitor<Expression> {
    StatementBuilder stmtBuilder;

    public ExpressionBuilder(StatementBuilder sb) {
        stmtBuilder = sb;
    }

    @Override
    public Expression visitBool(SPLParser.BoolContext ctx) {
        String literal = ctx.BOOL().getText();
        if (literal.equals("true")) return new Bool(true);
        else return new Bool(false);
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
        Expression rhs = visit(ctx.exp());
        return new NotOp(rhs);
    }

    @Override
    public Expression visitNum(SPLParser.NumContext ctx) {
        int val = Integer.valueOf(ctx.NUM().getText());
        return new Num(val);
    }

    @Override
    public Expression visitId(SPLParser.IdContext ctx) {
        String varName = ctx.ID().getText();
        return new Id(varName);
    }

    @Override
    public Expression visitParens(SPLParser.ParensContext ctx) {
        Expression inner = visit(ctx.exp());
        return inner;
    }

    @Override
    public Expression visitRead(SPLParser.ReadContext ctx) {
        return new Read();
    }

    @Override
    public Expression visitMulOp(SPLParser.MulOpContext ctx) {
        Expression lhs = visit(ctx.exp(0));
        String op = ctx.OPM().getText();
        Expression rhs = visit(ctx.exp(1));
        return new ArithOp(lhs, op, rhs);
    }

    @Override
    public Expression visitNegOp(SPLParser.NegOpContext ctx) {
        String op = ctx.OPA().getText();
        Expression rhs = visit(ctx.exp());
        if (op.equals("-")) return new NegOp(rhs);
        else return rhs;
    }

    @Override
    public Expression visitAddOp(SPLParser.AddOpContext ctx) {
        Expression lhs = visit(ctx.exp(0));
        String op = ctx.OPA().getText();
        Expression rhs = visit(ctx.exp(1));
        return new ArithOp(lhs, op, rhs);
    }

    @Override
    public Expression visitCompOp(SPLParser.CompOpContext ctx) {
        Expression lhs = visit(ctx.exp(0));
        String op = ctx.COMP().getText();
        Expression rhs = visit(ctx.exp(1));
        return new CompOp(lhs, op, rhs);
    }

    @Override
    public Expression visitLambda(SPLParser.LambdaContext ctx) {
        String varName = ctx.ID().getText();
        Statement body = stmtBuilder.visit(ctx.block());
        return new Lambda(varName, body);
    }

    @Override
    public Expression visitClassDecl(SPLParser.ClassDeclContext ctx) {
        Statement body = stmtBuilder.visit(ctx.block());
        return new ClassDecl(body);
    }

    @Override
    public Expression visitFunCall(SPLParser.FunCallContext ctx) {
        Expression funExp = visit(ctx.exp(0));
        Expression argExp = visit(ctx.exp(1));
        return new FunCall(funExp, argExp);
    }

    @Override
    public Expression visitNewClass(SPLParser.NewClassContext ctx) {
        Expression lhs = visit(ctx.exp());
        return new NewClass(lhs);
    }

    @Override
    public Expression visitClassRef(SPLParser.ClassRefContext ctx) {
        Expression lhs = visit(ctx.exp());
        String name = ctx.ID().getText();
        return new ClassRef(lhs, name);
    }
}
