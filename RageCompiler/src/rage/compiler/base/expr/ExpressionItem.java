package rage.compiler.base.expr;

import rage.compiler.base.code.items.data.DataItem;

import java.util.Set;

/**
 * Expression item (leaf)
 */

public class ExpressionItem implements Expression {
    protected Object item;

    public ExpressionItem(Object item) {
        this.item = item;
    }

    public final Object getItem() {
        return item;
    }

    public final boolean isData() {
        return (item instanceof DataItem);
    }

    @Override
    public String toString() {
        if(item instanceof DataItem) {
            DataItem dataItem = (DataItem)item;
            return dataItem.getName() +
                ((dataItem.getArrayExpr() != null) ? "[" + dataItem.getArrayExpr().toString() + "]" : "");
        }
        return item.toString();
    }

    @Override
    public void getVarNames(Set<String> varNames) {
        if(item instanceof DataItem) {
            DataItem dataItem = (DataItem)item;
            varNames.add(dataItem.getName());
            if(dataItem.getArrayExpr() != null)
                dataItem.getArrayExpr().getVarNames(varNames);
        }
    }
}
