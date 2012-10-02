package rage.compiler.base.expr.items;

import rage.compiler.base.code.items.var.VarItem;

/**
 * Property of object (referenced by var) in expression
 */

public class ExpressionVarPropertyItem extends ExpressionVarItem {
    protected String propertyName;

    public ExpressionVarPropertyItem(VarItem varItem, String propertyName) {
        super(varItem);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Type getType() {
        return Type.PROPERTY;
    }
}
