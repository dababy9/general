package si413.spl;

import si413.spl.ast.*;
import java.util.List;
import java.util.ArrayList;

/** Parse Tree to AST conversion functions for nodes that represent statement lists.
 * Each visitXXX(SPLParser.XXXContext) method here should return an
 * list of AST Statement nodes, from the corresponding parse
 * tree node.
 */
public class StlistBuilder extends SPLParserBaseVisitor<List<Statement>> {
    private StatementBuilder stmtBuilder;

    public StlistBuilder() {
        stmtBuilder = new StatementBuilder(this);
    }

    @Override
    public List<Statement> visitProg(SPLParser.ProgContext ctx) {
        return visit(ctx.stlist());
    }

    @Override
    public List<Statement> visitNormalStmt(SPLParser.NormalStmtContext ctx) {
        List<Statement> stlist = visit(ctx.stlist());
        Statement last = stmtBuilder.visit(ctx.stmt());
        stlist.add(last);
        return stlist;
    }

    @Override
    public List<Statement> visitNullStmt(SPLParser.NullStmtContext ctx) {
        return new ArrayList<>();
    }
}
