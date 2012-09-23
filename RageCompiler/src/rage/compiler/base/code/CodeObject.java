package rage.compiler.base.code;

import rage.compiler.base.code.items.comp.CallItem;
import rage.compiler.base.code.items.comp.CodeDeclItem;
import rage.compiler.base.code.items.CodeItem;

import java.util.HashSet;
import java.util.Set;

/**
 * Code object (procedure)
 */

public class CodeObject {
    protected CodeDeclItem codeDecl;
    protected Set<String> callNames = new HashSet<String>();

    public CodeObject(CodeDeclItem codeDecl) {
        this.codeDecl = codeDecl;
        genCallNames(codeDecl, callNames);
    }

    public final CodeDeclItem getCodeDecl() {
        return codeDecl;
    }

    public final String getName() {
        return codeDecl.getName();
    }

    public final Set<String> getCallNames() {
        return callNames;
    }

    public final boolean isAtomic() {
        return codeDecl.isAtomic();
    }

    protected void genCallNames(CodeItem item, Set<String> names) {
        if(item.getType() == CodeItem.Type.CALL)
            names.add(((CallItem)item).getName());
        for(CodeItem i : item.getChildren())
            genCallNames(i, names);
    }
}
