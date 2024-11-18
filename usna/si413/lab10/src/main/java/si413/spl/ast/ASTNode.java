package si413.spl.ast;

import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.lang.reflect.Field;
import org.antlr.v4.runtime.tree.Tree;
import java.util.*;
import java.util.stream.*;

/** An abstraction for any ASTNode, be it a statement or expression.
 * The methods in this class are mostly here to provide pretty-printing
 * of AST trees.
 * Two methods which sometimes need to be overridden in subclasses are
 * astInfo() and getChildren().
 *
 * You should not need to edit (or really worry about) this class.
 */
public abstract class ASTNode {
    /** Gets some extra info to be shown after a colon in the AST tree.
     * Use this for when your AST node has some static information that's not
     * part of its child nodes, like an operation or variable name.
     */
    protected String astInfo() {
        return null;
    }

    /** All of the private and public fields of this AST node, as a stream.
     * This implementation uses the java introspection library and probably
     * should work as-is in all cases.
     */
    protected Stream<Field> allFields() {
        return Stream.<Class<?>>iterate(getClass(), Objects::nonNull, Class::getSuperclass)
            .flatMap(clz -> Arrays.stream(clz.getDeclaredFields()));
    }

    /** Helper method to turn an introspection Field into the corresponding object stored there. */
    private Object getField(Field fld) {
        fld.setAccessible(true);
        try { return fld.get(this); }
        catch (IllegalAccessException e) {
            throw new RuntimeException("field not accessible but should be", e);
        }
    }

    /** Returns a list of all child nodes of this AST node.
     * The default implementation builds a list of all fields whose
     * declared type is a subclass of ASTNode, which should work in most cases.
     */
    protected List<? extends ASTNode> getChildren() {
        return allFields()
            .filter(fld -> ASTNode.class.isAssignableFrom(fld.getType()))
            .map(this::getField)
            .map(obj -> (ASTNode)obj)
            .toList();
    }

    /** Retuns a list of the Lisp-style representations of each root. */
    public static List<String> dumpTrees(Collection<? extends ASTNode> roots) {
        return roots.stream().map(ASTNode::dumpTree).toList();
    }

    /** Returns a Lisp-style representation of the AST. */
    public String dumpTree() {
        StringBuilder sb = new StringBuilder();
        dumpTree(sb);
        return sb.toString();
    }

    /** Helper method for dumpTree */
    private void dumpTree(StringBuilder sb) {
        List<? extends ASTNode> children = getChildren();
        sb.append('(');
        sb.append(nodeLabel());
        for (ASTNode child : children) {
            sb.append(' ');
            child.dumpTree(sb);
        }
        sb.append(')');
    }

    /** Displays a single ASTNode on the terminal. */
    public void printTree() {
        printTree(System.out, "", "");
    }

    /** Helper method for printTree to recursively show all nodes. */
    private void printTree(java.io.PrintStream out, String pre, String cpre) {
        // used https://stackoverflow.com/a/8948691/1008966
        out.print(pre);
        out.println(nodeLabel());
        Iterator<? extends ASTNode> it = getChildren().iterator();
        if (!it.hasNext()) return;
        ASTNode cur = it.next();
        for (; it.hasNext(); cur = it.next()) {
            cur.printTree(out, cpre + "├──", cpre + "│  ");
        }
        cur.printTree(out, cpre + "└──", cpre + "   ");
    }

    /** Helper method to get the label to show at each node in the AST.
     * The default implementation combines the class name with the result
     * from astInfo(), if any.
     */
    protected String nodeLabel() {
        String clname = getClass().getSimpleName();
        String info = astInfo();
        if (info == null) return clname;
        else return "%s:%s".formatted(clname, info);
    }

    /** Compares two ASTNodes for (deep) equality.
     * The default method is a generic deep equality check,
     * comparing the two types and then recursively calling .equals() on
     * all declared fields in the type.
     */
    @Override
    public boolean equals(Object o) {
        if (!getClass().equals(o.getClass())) return false;
        ASTNode onode = (ASTNode)o;
        return allFields().allMatch(fld -> {
            Object lhs = getField(fld), rhs = onode.getField(fld);
            return (lhs == null) ? (rhs == null) : lhs.equals(rhs);
        });
    }
}
