package rage.compiler.base.code.items.comp;

import rage.compiler.base.code.items.GenericCodeItem;
import rage.compiler.base.expr.Expression;

import java.util.*;

/**
 * Call item
 */

public class CallItem extends GenericCodeItem {
    protected String name;
    protected List<Expression> args = new ArrayList<Expression>();
    protected Set<String> varNames = new HashSet<String>();

    public CallItem(String name, List<Expression> args) {
        this.name = name;
        this.args = args;
        for(Expression e : args)
            e.getVarNames(varNames);
    }

    public final String getName() {
        return name;
    }

    public final List<Expression> getArgs() {
        return args;
    }

    public final Set<String> getVarNames() {
        return varNames;
    }

    @Override
    public Type getType() {
        return Type.CALL;
    }
}
