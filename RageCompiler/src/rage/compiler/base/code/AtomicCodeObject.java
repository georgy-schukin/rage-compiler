package rage.compiler.base.code;

import rage.compiler.base.code.items.help.ArgItem;

import java.util.List;

/**
 * Atomic code object
 */

public class AtomicCodeObject extends CodeObject {
    protected String codeName = "";

    public AtomicCodeObject(String name, List<ArgItem> inputArgs, List<ArgItem> outputArgs, String codeName) {
        super(name, inputArgs, outputArgs);
        this.codeName = codeName;
    }

    public final String getCodeName() {
        return codeName;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
