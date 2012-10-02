package rage.compiler.base.expr.items;

import rage.compiler.base.expr.ExpressionItem;

import java.util.Set;

/**
 * Constant in expression (int/float/string, etc)
 */

public class ExpressionConstItem extends ExpressionItem {
    protected Object constItem;

    public ExpressionConstItem(Object constItem) {
        this.constItem = constItem;
    }

    @Override
    public Type getType() {
        return Type.CONST;
    }

    @Override
    public void getVarNames(Set<String> varNames) {} // no vars here
}
