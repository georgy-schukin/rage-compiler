package rage.compiler.base.code.items;

import java.util.Collections;
import java.util.List;

/**
 * Generic
 */

public abstract class GenericCodeItem implements CodeItem {
    @Override
    public List<CodeItem> getChildren() {
        return Collections.emptyList();
    }
}
