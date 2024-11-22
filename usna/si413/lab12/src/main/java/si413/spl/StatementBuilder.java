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
        this.expBuilder = new ExpressionBuilder(this);
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
    public Block visitBlock(SPLParser.BlockContext ctx) {
        List<Statement> statements = stlBuilder.visit(ctx.stlist());
        return new Block(statements);
    }

    @Override
    public Statement visitNewVar(SPLParser.NewVarContext ctx) {
        String varName = ctx.ID().getText();
        Expression rhs = expBuilder.visit(ctx.exp());
        return new NewStmt(varName, rhs);
    }

    @Override
    public Statement visitAsn(SPLParser.AsnContext ctx) {
        String varName = ctx.ID().getText();
        Expression rhs = expBuilder.visit(ctx.exp());
        return new Asn(varName, rhs);
    }

    @Override
    public Statement visitIfStmt(SPLParser.IfStmtContext ctx) {
        Expression cond = expBuilder.visit(ctx.exp());
        Statement ifBlock = visit(ctx.block());
        return new IfElse(cond, ifBlock, new Block());
    }

    @Override
    public Statement visitIfElseStmt(SPLParser.IfElseStmtContext ctx) {
        Expression cond = expBuilder.visit(ctx.exp());
        Statement ifBlock = visit(ctx.block(0));
        Statement elseBlock = visit(ctx.block(1));
        return new IfElse(cond, ifBlock, elseBlock);
    }

    @Override
    public Statement visitWhileStmt(SPLParser.WhileStmtContext ctx) {
        Expression cond = expBuilder.visit(ctx.exp());
        Statement inner = visit(ctx.block());
        return new WhileStmt(cond, inner);
    }

    @Override
    public Statement visitDebugStmt(SPLParser.DebugStmtContext ctx) {
        String msgWithQuotes = ctx.DEBUG().getText();
        String msg = msgWithQuotes.substring(1, msgWithQuotes.length() - 1);
        return new Debug(msg);
    }

    @Override
    public Statement visitExpStmt(SPLParser.ExpStmtContext ctx) {
        Expression exp = expBuilder.visit(ctx.exp());
        return new ExpressionStatement(exp);
    }
}
