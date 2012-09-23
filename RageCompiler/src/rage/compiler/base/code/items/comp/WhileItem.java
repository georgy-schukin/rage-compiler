package rage.compiler.base.code.items.comp;

import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.expr.Expression;

/**
 * While cycle
 */

public class WhileItem extends BlockItem {
    protected Expression loopExpr;

    public WhileItem(Expression expr, CodeItem body) {
        this.loopExpr = expr;
        addChild(body);
    }

    public final Expression getCondExpression() {
        return loopExpr;
    }

    public final CodeItem getLoopBody() {
        return getChildren().listIterator(0).next();
    }

    @Override
    public Type getType() {
        return Type.WHILE;
    }
}
