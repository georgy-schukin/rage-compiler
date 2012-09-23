package rage.compiler.base.code.items.data;

import rage.compiler.base.code.items.GenericCodeItem;
import rage.compiler.base.expr.Expression;

/**
 * Data variable
 */

public class DataItem extends GenericCodeItem {
    protected String name;
    protected Expression arrayExpr;

    public DataItem(String name) {
        this.name = name;
    }

    public DataItem(String name, Expression arrayExpr) {
        this.name = name;
        this.arrayExpr = arrayExpr;
    }

    public final String getName() {
        return name;
    }

    public final Expression getArrayExpr() {
        return arrayExpr;
    }

    @Override
    public Type getType() {
        return Type.DATA;
    }
}
