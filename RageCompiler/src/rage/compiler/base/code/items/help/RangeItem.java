package rage.compiler.base.code.items.help;

import rage.compiler.base.code.items.GenericCodeItem;
import rage.compiler.base.expr.Expression;

/**
 * Range item
 */

public class RangeItem extends GenericCodeItem {
    protected Expression beginExpr, endExpr, stepExpr;

    public RangeItem(Expression beginExpr, Expression endExpr, Expression stepExpr) {
        this.beginExpr = beginExpr;
        this.endExpr = endExpr;
        this.stepExpr = stepExpr;
    }

    public final Expression getBeginExpression() {
        return beginExpr;
    }

    public final Expression getEndExpression() {
        return endExpr;
    }

    public final Expression getStepExpression() {
        return stepExpr;
    }

    @Override
    public Type getType() {
        return Type.RANGE;
    }
}
