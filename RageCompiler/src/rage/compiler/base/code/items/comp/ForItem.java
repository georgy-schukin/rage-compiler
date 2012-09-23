package rage.compiler.base.code.items.comp;

import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.code.items.help.OrderItem;
import rage.compiler.base.code.items.help.RangeItem;

/**
 * For cycle
 */

public class ForItem extends BlockItem {
    protected String indexName;
    protected RangeItem rangeItem;

    public ForItem(String indexName, RangeItem rangeItem, CodeItem body, OrderItem order) {
        this.indexName = indexName;
        this.rangeItem = rangeItem;
        this.order = order;
        addChild(body);
    }

    public final String getIndexName() {
        return indexName;
    }

    public final RangeItem getRange() {
        return rangeItem;
    }

    public final CodeItem getLoopBody() {
        return getChildren().listIterator(0).next();
    }

    @Override
    public Type getType() {
        return Type.FOR;
    }
}
