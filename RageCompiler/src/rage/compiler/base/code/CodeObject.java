package rage.compiler.base.code;

import rage.compiler.base.code.items.comp.CallItem;
import rage.compiler.base.code.items.comp.CodeDeclItem;
import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.code.items.help.ArgItem;

import java.util.*;

/**
 * Code object (procedure)
 */

public abstract class CodeObject {
    protected String name;
    protected List<ArgItem> inputArgs = Collections.emptyList();
    protected List<ArgItem> outputArgs = Collections.emptyList();
    protected Set<String> callNames = new HashSet<String>();

    public CodeObject(String name, List<ArgItem> inputArgs, List<ArgItem> outputArgs) {
        this.name = name;
        this.inputArgs = inputArgs;
        this.outputArgs = outputArgs;
        //genCallNames(codeDecl, callNames);
    }

    public final String getName() {
        return name;
    }

    public final List<ArgItem> getInputArgs() {
        return inputArgs;
    }

    public final List<ArgItem> getOutputArgs() {
        return outputArgs;
    }

    public final Set<String> getCallNames() {
        return callNames;
    }

    public abstract boolean isAtomic();

    protected void genCallNames(CodeItem item, Set<String> names) {
        if(item.getType() == CodeItem.Type.CALL)
            names.add(((CallItem)item).getName());
        for(CodeItem i : item.getChildren())
            genCallNames(i, names);
    }
}
