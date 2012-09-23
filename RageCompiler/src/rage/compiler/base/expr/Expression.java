package rage.compiler.base.expr;

import java.util.Set;

/**
 * Expression base class
 */

public interface Expression {
    public void getVarNames(Set<String> varNames); // get names of vars in expression
    public String toString();
}
