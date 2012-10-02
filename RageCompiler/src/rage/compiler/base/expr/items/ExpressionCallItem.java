package rage.compiler.base.expr.items;

import rage.compiler.base.expr.Expression;
import rage.compiler.base.expr.ExpressionItem;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Function call in expression
 */

public class ExpressionCallItem extends ExpressionItem {
    protected String name; // name of code object to call (or name of var - link to code object)
    protected List<Expression> args = Collections.emptyList();

    public ExpressionCallItem(String name) {
        this.name = name;
    }

    public ExpressionCallItem(String name, List<Expression> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public Type getType() {
        return Type.CALL;
    }

    @Override
    public void getVarNames(Set<String> varNames) {
        //TODO: track code name as variable?
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
