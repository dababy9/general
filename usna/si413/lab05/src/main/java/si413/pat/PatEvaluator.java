package si413.pat;

import java.util.*;

/** Evaluator for the Pat language following the visitor pattern.
 *
 * CALEB WALKER
 */
public class PatEvaluator extends PatParserBaseVisitor<List<String>> {

    private static List<String> fold(List<String> lhs, List<String> rhs) {
        List<String> result = new ArrayList<>();
        Iterator<String> lit = lhs.iterator();
        Iterator<String> rit = rhs.iterator();
        while (lit.hasNext() || rit.hasNext()) {
            if (lit.hasNext()) result.add(lit.next());
            if (rit.hasNext()) result.add(rit.next());
        }
        return result;
    }

    HashMap<String, List<String>> symTable = new HashMap<>();

    @Override
    public List<String> visitNonemptyProg(PatParser.NonemptyProgContext ctx) {
        // recursively call visit() on the child sequence node
        List<String> sequence = visit(ctx.seq());
        // for now, just indicate that parsing worked.
        System.out.println(String.join(" ", sequence));
        // recursively continue on to the next statement
        visit(ctx.prog());
        return null;
    }

    @Override
    public List<String> visitSymbol(PatParser.SymbolContext ctx) {
        // get the literal text for this symbol
        String symbol = ctx.SYM().getText();
        // return a size-one list just containing that one symbol
        return List.of(symbol);
    }

    @Override
    public List<String> visitVariable(PatParser.VariableContext ctx) {
        // get the literal text for this variable name
        String var = ctx.NAME().getText();
        // retrieve the value of that variable
        List<String> val = symTable.get(var);
        // check if the variable doesn't exist (value is null)
        if(val == null)
            throw new PatError("Undefined Variable");
        // return the value of the variable
        return val;
    }

    @Override
    public List<String> visitBraces(PatParser.BracesContext ctx) {
        // return the sequence inside of the braces
        return visit(ctx.seq());
    }

    @Override
    public List<String> visitReverse(PatParser.ReverseContext ctx) {
        // get the sequence before the REV operator
        List<String> seq = visit(ctx.seq());
        // make a mutable copy
        ArrayList<String> rev = new ArrayList<>(seq);
        // reverse the copy
        Collections.reverse(rev);
        // return reversed copy
        return rev;
    }

    @Override
    public List<String> visitVarAssign(PatParser.VarAssignContext ctx) {
        // get the literal text for this variable name
        String var = ctx.NAME().getText();
        // get the sequence before the COLON
        List<String> seq = visit(ctx.seq());
        // map the variable name to the sequence
        symTable.put(var, seq);
        // return the sequence
        return seq;
    }

    @Override
    public List<String> visitConcat(PatParser.ConcatContext ctx) {
        // get the first sequence
        List<String> seq1 = visit(ctx.seq(0));
        // make a mutable copy of the first sequence
        ArrayList<String> concat = new ArrayList<>(seq1);
        // concatenate first sequence with second sequence using addAll() method
        concat.addAll(visit(ctx.seq(1)));
        // return concatenated sequence
        return concat;
    }

    @Override
    public List<String> visitFold(PatParser.FoldContext ctx) {
        // return the folded sequence of the left and right sequences
        return fold(visit(ctx.seq(0)), visit(ctx.seq(1)));
    }
}
