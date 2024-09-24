import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class AntlrCalcEvaluator extends ACalcParserBaseListener {
    private ParseTreeProperty<Integer> values = new ParseTreeProperty<>();

    public static void evaluate(ACalcParser.ProgContext tree) {
        AntlrCalcEvaluator evaluator = new AntlrCalcEvaluator();
        new ParseTreeWalker().walk(evaluator, tree);
    }

    @Override
    public void exitStmt(ACalcParser.StmtContext ctx) {
        int result = values.get(ctx.exp());
        System.out.println(result);
    }

    @Override
    public void exitSingleTerm(ACalcParser.SingleTermContext ctx) {
        values.put(ctx, values.get(ctx.term()));
    }

    @Override
    public void exitAddSub(ACalcParser.AddSubContext ctx) {
        int lhs = values.get(ctx.exp());
        int rhs = values.get(ctx.term());
        String oper = ctx.OPA().getSymbol().getText();
        if (oper.equals("+")) values.put(ctx, lhs + rhs);
        else values.put(ctx, lhs - rhs);
    }

    @Override
    public void exitMulDiv(ACalcParser.MulDivContext ctx) {
        int lhs = values.get(ctx.term());
        int rhs = values.get(ctx.factor());
        String oper = ctx.OPM().getSymbol().getText();
        if (oper.equals("*")) values.put(ctx, lhs * rhs);
        else values.put(ctx, lhs / rhs);
    }

    @Override
    public void exitSingleFactor(ACalcParser.SingleFactorContext ctx) {
        values.put(ctx, values.get(ctx.factor()));
    }

    @Override
    public void exitNum(ACalcParser.NumContext ctx) {
        values.put(ctx, Integer.valueOf(ctx.NUM().getSymbol().getText()));
    }

    @Override
    public void exitParens(ACalcParser.ParensContext ctx) {
        values.put(ctx, values.get(ctx.exp()));
    }
}
