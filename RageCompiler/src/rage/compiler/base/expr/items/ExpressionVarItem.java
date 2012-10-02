package rage.compiler.base.expr.items;

import rage.compiler.base.code.items.var.VarItem;
import rage.compiler.base.expr.ExpressionItem;

import java.util.Set;

/**
 * Variable in expression
 */

public class ExpressionVarItem extends ExpressionItem {
    protected VarItem varItem;

    public ExpressionVarItem(VarItem varItem) {
        this.varItem = varItem;
    }

    public VarItem getVarItem() {
        return varItem;
    }

    @Override
    public Type getType() {
        return Type.VAR;
    }

    @Override
    public void getVarNames(Set<String> varNames) {
        varNames.add(varItem.getName());
        if(varItem.getArrayExpr() != null)
            varItem.getArrayExpr().getVarNames(varNames);
    }
}
