package rage.compiler.base.expr;

import java.util.Set;

/**
 * Binary expression
 */

public class BinaryExpression implements Expression {
    protected String operation = "";
    protected Expression first;
    protected Expression second;

    public BinaryExpression(String operation, Expression first, Expression second) {
        this.operation = operation;
        this.first = first;
        this.second = second;
    }

    public final void setOperation(String operation) {
        this.operation = operation;
    }

    public final String getOperation() {
        return operation;
    }

    public final Expression getFirstMember() {
        return first;
    }

    public final Expression getSecondMember() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + operation + second.toString() + ")";
    }

    @Override
    public void getVarNames(Set<String> varNames) {
        first.getVarNames(varNames);
        second.getVarNames(varNames);
    }
}
