package rage.compiler.codegen;

import rage.compiler.base.code.CodePackage;

import java.io.BufferedWriter;

/**
 * Generate code by code package for specific Rage runtime system platform
 */

public interface CodeGenerator {
    public void generate(CodePackage codePackage, String fileName, String directory) throws Exception;
}
