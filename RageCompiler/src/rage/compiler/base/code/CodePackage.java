package rage.compiler.base.code;

import java.util.ArrayList;
import java.util.List;

/**
 * Package of code
 */

public class CodePackage {
    protected String name;
    protected List<CodeObject> codeObjects = new ArrayList<CodeObject>();

    public CodePackage(String name, List<CodeObject> codeObjects) {
        this.name = name;
        this.codeObjects = codeObjects;
    }

    public final String getName() {
        return name;
    }

    public final List<CodeObject> getCodeObjects() {
        return codeObjects;
    }

    public final CodeObject getCodeObject(String name) {
        for(CodeObject o : codeObjects)
            if(o.getName().equals(name)) return o;
        return null;
    }
}
