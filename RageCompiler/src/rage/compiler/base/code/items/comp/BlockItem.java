package rage.compiler.base.code.items.comp;

import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.code.items.GenericCodeItem;
import rage.compiler.base.code.items.help.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Block
 */

public class BlockItem extends GenericCodeItem {
    protected List<CodeItem> children = new ArrayList<CodeItem>();
    protected OrderItem order;

    public BlockItem() {}

    public BlockItem(List<CodeItem> children, OrderItem order) {
        this.children = children;
        this.order = order;
    }

    protected void addChild(CodeItem item) {
        children.add(item);
    }

    public final OrderItem getOrder() {
        return order;
    }

    @Override
    public List<CodeItem> getChildren() {
        return children;
    }

    @Override
    public Type getType() {
        return Type.BLOCK;
    }
}
