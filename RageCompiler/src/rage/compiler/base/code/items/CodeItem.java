package rage.compiler.base.code.items;

import java.util.List;

/**
 * Code item interface
 */

public interface CodeItem {
    public enum Type {
        DATA,
        DATA_DECL,
        DATA_TYPE,
        ORDER,
        RANGE,
        CODE_DECL,
        CALL,
        BLOCK,
        FOR,
        COND,
        WHILE
    }

    public List<CodeItem> getChildren();
    public Type getType();
}
