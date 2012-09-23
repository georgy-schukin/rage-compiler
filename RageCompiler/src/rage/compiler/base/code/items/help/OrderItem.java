package rage.compiler.base.code.items.help;

import rage.compiler.base.code.items.GenericCodeItem;

/**
 * Execution order (seq/par)
 */

public class OrderItem extends GenericCodeItem {

    public enum OrderType {
        SEQ,
        PAR
    }

    protected OrderType type;

    public OrderItem(OrderType type) {
        this.type = type;
    }

    public final OrderType getOrderType() {
        return type;
    }

    @Override
    public Type getType() {
        return Type.ORDER;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
