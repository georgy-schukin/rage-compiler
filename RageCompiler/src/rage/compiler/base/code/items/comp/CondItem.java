package rage.compiler.base.code.items.comp;

import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.expr.Expression;

public class CondItem extends BlockItem {
    protected Expression condExpr;

    public CondItem(Expression condExpr, CodeItem first) {
        this.condExpr = condExpr;
        addChild(first);
    }

    public CondItem(Expression condExpr, CodeItem first, CodeItem second) {
        this.condExpr = condExpr;
        addChild(first);
        addChild(second);
    }

    public final Expression getCondExpression() {
        return condExpr;
    }

    public final CodeItem getFirst() {
        return getChildren().listIterator(0).next();
    }

    public final CodeItem getSecond() {
        return (getChildren().size() > 1) ? getChildren().listIterator(1).next() : null;
    }

    @Override
    public Type getType() {
        return Type.COND;
    }
}
