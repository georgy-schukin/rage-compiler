package rage.compiler.base.expr;

/**
 * Expression item (leaf)
 */

public abstract class ExpressionItem implements Expression {
    public enum Type {
        CONST,
        VAR,
        CALL,
        PROPERTY,
        LIST
    }

    public abstract Type getType();
    //protected Object item;

    /*public ExpressionItem(Object item) {
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
    }*/
}
