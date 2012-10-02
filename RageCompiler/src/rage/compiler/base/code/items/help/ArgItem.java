package rage.compiler.base.code.items.help;

import rage.compiler.base.code.items.GenericCodeItem;

/**
 * Formal argument for code object
 */

public class ArgItem extends GenericCodeItem {
    protected String name;

    public ArgItem(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return Type.ARG;
    }
}
