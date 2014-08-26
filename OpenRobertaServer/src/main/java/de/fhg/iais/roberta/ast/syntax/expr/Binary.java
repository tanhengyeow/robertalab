package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents all binary operations from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Op, Expr, Expr)}.<br>
 * The enumeration {@link Op} contains all allowed binary operations.
 * 
 * @author kcvejoski
 */
public final class Binary<V> extends Expr<V> {
    private final Op op;
    private final Expr<V> left;
    private final Expr<V> right;

    private Binary(Op op, Expr<V> left, Expr<V> right) {
        super(Phrase.Kind.BINARY);
        Assert.isTrue(op != null && left != null && right != null && left.isReadOnly() && right.isReadOnly());
        this.op = op;
        this.left = left;
        this.right = right;
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link Binary}. This instance is read only and can not be modified.
     * 
     * @param op operator
     * @param left expression on the left hand side,
     * @param right expression on the right hand side
     * @return Binary expression
     */
    public static <V> Binary<V> make(Op op, Expr<V> left, Expr<V> right) {
        return new Binary<V>(op, left, right);
    }

    /**
     * @return the operation in the binary expression. See enum {@link Op} for all possible operations.
     */
    public Op getOp() {
        return this.op;
    }

    /**
     * @return the expression on the left hand side. Returns subclass of {@link Expr}.
     */
    public Expr<V> getLeft() {
        return this.left;
    }

    /**
     * @return the expression on the right hand side. Returns subclass of {@link Expr}.
     */
    public Expr<V> getRight() {
        return this.right;
    }

    @Override
    public int getPrecedence() {
        return this.op.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.op.getAssoc();
    }

    @Override
    public String toString() {
        return "Binary [" + this.op + ", " + this.left + ", " + this.right + "]";
    }

    @Override
    protected V accept(Visitor<V> visitor) {
        return visitor.visitBinary(this);
    }

    /**
     * Operators for the binary expression.
     * 
     * @author kcvejoski
     */
    public static enum Op {
        ADD( 100, Assoc.LEFT, "+" ),
        MINUS( 100, Assoc.LEFT, "-" ),
        MULTIPLY( 200, Assoc.LEFT, "*" ),
        DIVIDE( 200, Assoc.LEFT, "/" ),
        MOD( 200, Assoc.NONE, "%" ),
        EQ( 80, Assoc.LEFT, "==" ),
        NEQ( 80, Assoc.LEFT, "!=", "<>" ),
        LT( 90, Assoc.LEFT, "<" ),
        LTE( 90, Assoc.LEFT, "<=" ),
        GT( 90, Assoc.LEFT, ">" ),
        GTE( 90, Assoc.LEFT, ">=" ),
        AND( 70, Assoc.LEFT, "&&" ),
        OR( 60, Assoc.LEFT, "||" ),
        MATH_CHANGE( 80, Assoc.NONE ),
        TEXT_APPEND( 1, Assoc.LEFT, "TEXTAPPEND" ),
        IN( 1, Assoc.LEFT, ":" ),
        ASSIGNMENT( 1, Assoc.RIGHT, "=" ),
        ADD_ASSIGNMENT( 1, Assoc.RIGHT, "+=" ),
        MINUS_ASSIGNMENT( 1, Assoc.RIGHT, "-=" ),
        MULTIPLY_ASSIGNMENT( 1, Assoc.RIGHT, "*=" ),
        DIVIDE_ASSIGNMENT( 1, Assoc.RIGHT, "/=" ),
        MOD_ASSIGNMENT( 1, Assoc.RIGHT, "%=" );

        private final String[] values;
        private final int precedence;
        private final Assoc assoc;

        private Op(int precedence, Assoc assoc, String... values) {
            this.precedence = precedence;
            this.assoc = assoc;
            this.values = values;
        }

        /**
         * @return mathematical symbol of the operation.
         */
        public String getOpSymbol() {
            if ( this.values.length == 0 ) {
                return this.toString();
            } else {
                return this.values[0];
            }
        }

        /**
         * @return precedence of the operator.
         */
        public int getPrecedence() {
            return this.precedence;
        }

        /**
         * @return association of the operator
         */
        public Assoc getAssoc() {
            return this.assoc;
        }

        /**
         * get operator from {@link Op} from string parameter. It is possible for one operator to have multiple string mappings.
         * Throws exception if the operator does not exists.
         * 
         * @param name of the operator
         * @return operator from the enum {@link Op}
         */
        public static Op get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Op op : Op.values() ) {
                if ( op.toString().equals(sUpper) ) {
                    return op;
                }
                for ( String value : op.values ) {
                    if ( sUpper.equals(value) ) {
                        return op;
                    }
                }
            }
            throw new DbcException("Invalid binary operator symbol: " + s);
        }
    }

}
