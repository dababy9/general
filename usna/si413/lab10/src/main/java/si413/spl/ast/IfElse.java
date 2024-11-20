package si413.spl.ast;

import si413.spl.*;

/** An if/else statement in SPL.
 * Example: ifelse x = 10 { write x; } { write 0; }
 */
public class IfElse extends Statement {
    private Expression condExp;
    private Statement ifStmt;
    private Statement elseStmt;

    public IfElse(Expression condExp, Statement ifStmt, Statement elseStmt) {
        this.condExp = condExp;
        this.ifStmt = ifStmt;
        this.elseStmt = elseStmt;
    }

    @Override
    public void compile(Frame env, Context ctx){
        // Recursively evaluate conditional
        String condNum = condExp.compile(env, ctx);
        // Convert condition to i1 type
        String condBool = ctx.freshRegister();
        ctx.code("%s = trunc i64 %s to i1".formatted(condBool, condNum));
        // Create three labels: If, Else, Then
        String ifLabel = ctx.freshLabel();
        String elseLabel = ctx.freshLabel();
        String thenLabel = ctx.freshLabel();
        // Conditional branch
        ctx.code("br i1 %s, label %%%s, label %%%s".formatted(condBool, ifLabel, elseLabel));
        // Write if statement (and jump down to then label)
        ctx.label(ifLabel);
        ifStmt.compile(new Frame(env), ctx);
        ctx.code("br label %%%s".formatted(thenLabel));
        // Write else statement (and jump down to then label)
        ctx.label(elseLabel);
        elseStmt.compile(new Frame(env), ctx);
        ctx.code("br label %%%s".formatted(thenLabel));
        // Write then label to continue after this statement
        ctx.label(thenLabel);
    }
}
