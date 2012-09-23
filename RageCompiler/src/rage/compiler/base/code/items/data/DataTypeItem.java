package rage.compiler.base.code.items.data;

import rage.compiler.base.expr.Expression;

/**
 * Data type
 */

public class DataTypeItem extends DataItem {
    private boolean isArray = false;

    public DataTypeItem(String name, boolean isArray) {
        super(name);
        this.isArray = isArray;
    }

    public DataTypeItem(String name, Expression arrayExpr) {
        super(name, arrayExpr);
        this.isArray = true;
    }

    public final boolean isArray() {
        return isArray;
    }

    @Override
    public Type getType() {
        return Type.DATA_TYPE;
    }
}
