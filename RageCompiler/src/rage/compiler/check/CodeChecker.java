package rage.compiler.check;

import rage.compiler.base.code.CodeObject;
import rage.compiler.base.code.CodePackage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Check code for errors
 */

public class CodeChecker {
    public CodeChecker() {}

    public boolean check(CodePackage codePackage) {
        return checkPackage(codePackage);
    }

    protected boolean checkPackage(CodePackage codePackage) {
        Set<String> codeNames = new HashSet<String>();
        for(CodeObject o : codePackage.getCodeObjects()) {
            if(codeNames.contains(o.getName())) {
                System.out.println("\tError: code object with name \"" + o.getName() + "\" already exists!");
                return false;
            } else {
                codeNames.add(o.getName());
            }
        }
        return true;
    }
}
