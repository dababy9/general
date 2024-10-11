package si413.spl;

import si413.spl.ast.*;
import java.util.List;

/** Parse Tree to AST conversion functions for nodes that represent statements.
 * Each visitXXX(SPLParser.XXXContext) method here should return an
 * AST node that is a subclass of Statement, from the corresponding parse
 * tree node.
 */
public class StatementBuilder extends SPLParserBaseVisitor<Statement> {
    private StlistBuilder stlBuilder;
    private ExpressionBuilder expBuilder;

    public StatementBuilder(StlistBuilder stlBuilder) {
        this.stlBuilder = stlBuilder;
        this.expBuilder = new ExpressionBuilder();
    }

    @Override
    public Statement visitWrite(SPLParser.WriteContext ctx) {
        Expression rhs = expBuilder.visit(ctx.exp());
        return new Write(rhs);
    }

    @Override
    public Statement visitBlockStmt(SPLParser.BlockStmtContext ctx) {
        return visitBlock(ctx.block());
    }

    @Override
    public Statement visitBlock(SPLParser.BlockContext ctx) {
        List<Statement> statements = stlBuilder.visit(ctx.stlist());
        return new Block(statements);
    }

    @Override
    public Statement visitNewVar(SPLParser.NewVarContext ctx) {
        return new NewStmt(ctx.ID().getText(), expBuilder.visit(ctx.exp()));
    }

    @Override
    public Statement visitAsn(SPLParser.AsnContext ctx) {
        return new Asn(ctx.ID().getText(), expBuilder.visit(ctx.exp()));
    }

    @Override
    public Statement visitIfStmt(SPLParser.IfStmtContext ctx) {
        return new IfElse(expBuilder.visit(ctx.exp()), visitBlock(ctx.block()), new Block());
    }

    @Override
    public Statement visitIfElseStmt(SPLParser.IfElseStmtContext ctx) {
        return new IfElse(expBuilder.visit(ctx.exp()), visitBlock(ctx.block(0)), visitBlock(ctx.block(1)));
    }

    @Override
    public Statement visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        return new WhileStmt(expBuilder.visit(ctx.exp()), visitBlock(ctx.block()));
    }
}
