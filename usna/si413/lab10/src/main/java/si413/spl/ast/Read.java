package si413.spl.ast;

import si413.spl.*;

/** A read expression in an SPL progarm.
 * Example: read
 */
public class Read extends Expression {

    @Override
    public String compile(Frame env, Context ctx){
        ctx.comment("Begin Read");
        // Allocate memory and get pointer to it
        String memoryAddr = ctx.freshRegister();
        ctx.code("%s = alloca i64".formatted(memoryAddr));
        // Use scanf to read input into memory
        ctx.code("call i32 @scanf(ptr @scanLong, ptr %s)".formatted(memoryAddr));
        // Load memory into register
        String resultInt = ctx.freshRegister();
        ctx.code("%s = load i64, ptr %s".formatted(resultInt, memoryAddr));
        ctx.comment("End Read");
        return resultInt;
    }
}
