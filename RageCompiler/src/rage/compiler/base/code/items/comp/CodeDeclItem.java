package rage.compiler.base.code.items.comp;

import rage.compiler.base.code.items.CodeItem;
import rage.compiler.base.code.items.var.DataDeclItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Code declaration
 */

public class CodeDeclItem extends BlockItem {
    protected String name;
    //protected List<DataDeclItem> args = new ArrayList<DataDeclItem>();
    protected List<String> args = new ArrayList<String>();
    protected String code = "";
    protected boolean isAtomic;

    public CodeDeclItem(String name, List<String> args, String code) {
        this.name = name;
        this.args = args;
        this.code = code;
        this.isAtomic = true; // atomic code declaration
    }

    public CodeDeclItem(String name, List<String> args, CodeItem code) {
        this.name = name;
        this.args = args;
        addChild(code);
        this.isAtomic = false; // struct code declaration
    }

    public final boolean isAtomic() {
        return isAtomic;
    }

    public final String getName() {
        return name;
    }

    public final List<String> getArgs() {
        return args;
    }

    public final String getCodeString() {
        return code;
    }

    public final CodeItem getBody() {
        return getChildren().listIterator(0).next();
    }

    @Override
    public Type getType() {
        return Type.CODE_DECL;
    }
}
