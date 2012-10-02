package rage.compiler.base.expr.items;

import rage.compiler.base.expr.Expression;
import rage.compiler.base.expr.ExpressionItem;

import java.util.List;
import java.util.Set;

/**
 * List/array of expressions : [e1, e2, ..., en]
 */

public class ExpressionListItem extends ExpressionItem {
    protected String typeName; // name of type (if there is typed list)
    protected List<Expression> expressions; // expressions in list

    public ExpressionListItem(List<Expression> expressions) {
        this.typeName = null;
        this.expressions = expressions;
    }

    public ExpressionListItem(String typeName, List<Expression> expressions) {
        this.typeName = typeName;
        this.expressions = expressions;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public Type getType() {
        return Type.LIST;
    }

    @Override
    public void getVarNames(Set<String> varNames) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
