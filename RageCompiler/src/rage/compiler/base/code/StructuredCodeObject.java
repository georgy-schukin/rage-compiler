package rage.compiler.base.code;

import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.code.items.help.ArgItem;

import java.util.List;

/**
 * Structuredcode object
 */

public class StructuredCodeObject extends CodeObject {
    protected CodeItem rootItem;

    public StructuredCodeObject(String name, List<ArgItem> inputArgs, List<ArgItem> outputArgs, CodeItem rootItem) {
        super(name, inputArgs, outputArgs);
        this.rootItem = rootItem;
    }

    public final CodeItem getRootItem() {
        return rootItem;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }
}
