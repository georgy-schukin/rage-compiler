package rage.compiler.base.code.items.comp;

import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.expr.Expression;

import java.util.Collections;
import java.util.List;

/**
 * General item for operation "[output = ] input"
 */

public class AssignItem implements CodeItem {
    protected List<Expression> input = Collections.emptyList();
    protected List<Expression> output = Collections.emptyList();

    public AssignItem(List<Expression> input) {
        this.input = input;
    }

    public AssignItem(List<Expression> input, List<Expression> output) {
        this.input = input;
        this.output = output;
    }

    public final List<Expression> getInput() {
        return input;
    }

    public final List<Expression> getOutput() {
        return output;
    }

    @Override
    public List<CodeItem> getChildren() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Type getType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
