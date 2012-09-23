package rage.compiler.base.code;

import rage.compiler.base.code.items.CodeItem;

/**
 * Tree of code (build by parser)
 */

public class CodeTree {
    protected CodeItem root;

    public CodeTree(CodeItem root) {
        this.root = root;
    }

    public final CodeItem getRoot() {
        return root;
    }
}
