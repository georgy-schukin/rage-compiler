package rage.compiler.base.expr;

import java.util.Set;

/**
 * Unary expression
 */

public class UnaryExpression implements Expression {
    protected String operation = "";
    protected Expression expression;

    public UnaryExpression(String operation, Expression expression) {
        this.operation = operation;
        this.expression = expression;
    }

    public final void setOperation(String operation) {
        this.operation = operation;
    }

    public final String getOperation() {
        return operation;
    }

    public final Expression getMember() {
        return expression;
    }

    @Override
    public String toString() {
        return "(" + operation + expression.toString() + ")";
    }

    @Override
    public void getVarNames(Set<String> varNames) {
        expression.getVarNames(varNames);
    }
}
