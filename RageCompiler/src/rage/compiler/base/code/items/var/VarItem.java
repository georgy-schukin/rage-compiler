package rage.compiler.base.code.items.var;

import rage.compiler.base.code.items.GenericCodeItem;
import rage.compiler.base.expr.Expression;

/**
 * Variable - pointer to data/code object
 */

public class VarItem extends GenericCodeItem {
    protected String name;
    protected Expression arrayExpr;

    public VarItem(String name) {
        this.name = name;
    }

    public VarItem(String name, Expression arrayExpr) {
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
        return Type.VAR;
    }
}
