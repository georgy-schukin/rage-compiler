package rage.compiler.codegen.cpp;

import rage.compiler.base.code.CodeObject;
import rage.compiler.base.code.CodePackage;
import rage.compiler.base.code.items.*;
import rage.compiler.base.code.items.comp.*;
import rage.compiler.base.code.items.var.DataDeclItem;
import rage.compiler.base.code.items.help.OrderItem;
import rage.compiler.base.expr.Expression;
import rage.compiler.codegen.CodeGenerator;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * Build code for Rage runtime system (C++)
 */

public abstract class CPPCodeGenerator implements CodeGenerator {

    protected interface Strings {
        final String EXTENSION = ".hpp";

        final String ORDER_SEQ = "CompVertex::SEQ";
        final String ORDER_PAR = "CompVertex::PAR";

        final String DATA_TYPE_BLOCK = "DataVertex::BLOCK";

        final String DATA_ITEM = "DataVertex";
        final String CALL_ITEM = "CallVertex";
        final String BLOCK_ITEM = "BlockVertex";
        final String FOR_ITEM = "IndexedVertex";
        final String COND_ITEM = "CondVertex";
        final String WHILE_ITEM = "WhileVertex";

        final String ATOMIC_CLASS = "AtomicCodeObject";
        final String STRUCTURED_CLASS = "StructuredCodeObject";

        final String FORMAT_ITEM = "\n\t{0} *{1} = new {2}({3});\n";
        final String FORMAT_CHILDREN = "\t\t{0}->addChild({1});\n";
        final String FORMAT_FACT_ARG = "\t\t{0}->addArg({1});\n";
        final String FORMAT_DATA_REQ = "\t\t{0}->addDataReq(\"{1}\");\n";
    }

    protected CodePackage codePackage;
    protected BufferedWriter writer;
    protected int cnt = 0;

    public CPPCodeGenerator() {}

    /*public void generate(CodePackage codePackage, String fileName, String directory) throws Exception {
        this.codePackage = codePackage;
        writer = new BufferedWriter(new FileWriter(directory + fileName + Strings.EXTENSION, false));
            genPackage(codePackage); // generate code
        writer.close();
    }

    protected void genPackage(CodePackage codePackage) throws Exception {
        writer.write("#include \"code.h\"\n");
        writer.write("using namespace rage;\n");

        if(!codePackage.getName().equals(""))
            writer.write("namespace " + codePackage.getName() + " {\n");

        writer.write("\n//Code objects\n");
        for(CodeObject o : codePackage.getCodeObjects()) { // for each code object in package
            if(o.isAtomic())
                genAtomicCodeDecl(o); // gen atomic code decl
            else
                genStructuredCodeDecl(o); // gen struct code decl
            writer.write("\n");
        }

        if(!codePackage.getName().equals(""))
            writer.write("}\n");
    }

    protected void genAtomicCodeDecl(CodeObject codeObject) throws Exception {
        CodeDeclItem codeDecl = codeObject.getCodeDecl();
        writer.write("//" + codeDecl.getName() + " : atomic code object\n");
        writer.write("class " + codeDecl.getName() + " : public " + Strings.ATOMIC_CLASS + " {\n"); // gen class
        writer.write("protected:\nvoid init(CodeLibrary *lib) {\n"); // gen init function
            genArgs(codeDecl.getArgs());
        writer.write("}\n");
        writer.write("public:\nvoid exec(const CodeArgs& args) {\n"); // gen exec function
        String code = codeDecl.getCodeString().replaceFirst("^\\{", "").replaceFirst("\\}$", "");  // strip { and } from code string
        for(DataDeclItem d : codeDecl.getArgs())
            code = code.replaceAll("\\b" + d.getData().getName() + "\\b",
                    "args[\"" + d.getData().getName() + "\"]");
        writer.write(code + "\n"); // TODO: prepare args for C++
        writer.write("}\n}\n");
    }

    protected void genStructuredCodeDecl(CodeObject codeObject) throws Exception {
        CodeDeclItem codeDecl = codeObject.getCodeDecl();
        writer.write("//" + codeDecl.getName() + " : structured code object\n");
        writer.write("class " + codeDecl.getName() + " : public " + Strings.STRUCTURED_CLASS + " {\n"); // gen class
        writer.write("protected:\nvoid init(CodeLibrary *lib) {\n"); // gen init function
            genArgs(codeDecl.getArgs());
            for(String codeName : codeObject.getCallNames()) {
                writer.write(MessageFormat.format("\tlib->add<{0}>(\"{1}\");\n",
                        codeName, codePackage.getName() + "." + codeName)); // init code in library
            }
        writer.write("}\n");
        writer.write("void buildTree(CodeTree *tree, CodeLibrary *lib) {\n"); // gen exec function
            String rootName = genItem(codeDecl.getBody());
        writer.write("\ttree->setRoot(" + rootName + ");\n");
        writer.write("}\n}\n");
    }

    protected void genArgs(List<DataDeclItem> args) throws Exception { // gen arg decls for "init" function
        for(DataDeclItem a : args) {
            writer.write("\taddArg(\"" + a.getData().getName() + "\");\n");
        }
    }

    protected String genItem(CodeItem item) throws Exception {
        String name;
        switch (item.getType()) {
            case DATA_DECL: name = genDataDeclItem((DataDeclItem) item); break;
            case BLOCK: name = genBlockItem((BlockItem)item); break;
            case CALL: name = genCallItem((CallItem) item); break;
            case FOR: name = genForItem((ForItem) item); break;
            case COND: name = genCondItem((CondItem) item); break;
            case WHILE: name = genWhileItem((WhileItem) item); break;
            default: throw new Exception("Unrecognized item type : " + item.getType().toString());
        }
        return name;
    }

    protected String getNewName() {
        return "vertex_" + Integer.toString(cnt++);
    }

    protected String getOrder(OrderItem item) {
        return (item.getOrderType() == OrderItem.OrderType.SEQ) ?
                Strings.ORDER_SEQ : Strings.ORDER_PAR;
    }

    protected String genDataDeclItem(DataDeclItem item) throws Exception {
        String name = getNewName();
            genItemCreate(Strings.DATA_ITEM, name,
                MessageFormat.format("\"{0}\", {1}, {2}{3}",
                    item.getData().getName(),
                    Strings.DATA_TYPE_BLOCK,
                    MessageFormat.format("sizeof({0}){1}",
                        item.getDataType().getName(),
                        (item.getDataType().getArrayExpr() != null ? "*" + item.getDataType().getArrayExpr().toString() : "")),
                    (item.getData().getArrayExpr() != null ?
                    ", " + CPPExpressionGenerator.generateExpression(item.getData().getArrayExpr()) : "")));
        return name;
    }

    protected String genBlockItem(BlockItem item) throws Exception {
        String name = getNewName();
            genItemCreate(Strings.BLOCK_ITEM, name, getOrder(item.getOrder()));
            genChildren(item, name);
        return name;
    }

    protected String genCallItem(CallItem item) throws Exception {
        String name = getNewName();
        genItemCreate(Strings.CALL_ITEM, name,
                MessageFormat.format("lib->get(\"{0}\")", codePackage.getName() + "." + item.getName()));
        for(Expression e : item.getArgs()) { // for each formal arg
            writer.write(MessageFormat.format(Strings.FORMAT_FACT_ARG,
                    name, CPPExpressionGenerator.generateExpression(e)));
        }
        //TODO: maybe should be replaced with auto runtime reqs handling
        for(String var : item.getVarNames()) { // gen var reqs for call
            writer.write(MessageFormat.format(Strings.FORMAT_DATA_REQ, name, var));
        }
        return name;
    }

    protected String genForItem(ForItem item) throws Exception {
        String name = getNewName();
            genItemCreate(Strings.FOR_ITEM, name,
                MessageFormat.format("\"{0}\", {1}, {2}, {3}, {4}",
                    item.getIndexName(),
                    getOrder(item.getOrder()),
                    CPPExpressionGenerator.generateExpression(item.getRange().getBeginExpression()),
                    CPPExpressionGenerator.generateExpression(item.getRange().getEndExpression()),
                    CPPExpressionGenerator.generateExpression(item.getRange().getStepExpression())));
            genChildren(item, name);
        return name;
    }

    protected String genCondItem(CondItem item) throws Exception {
        String name = getNewName();
            genItemCreate(Strings.COND_ITEM, name, CPPExpressionGenerator.generateExpression(item.getCondExpression()));
            genChildren(item, name);
        return name;
    }

    protected String genWhileItem(WhileItem item) throws Exception {
        String name = getNewName();
            genItemCreate(Strings.WHILE_ITEM, name, CPPExpressionGenerator.generateExpression(item.getCondExpression()));
            genChildren(item, name);
        return name;
    }

    protected void genItemCreate(String itemType, String name, String body) throws Exception {
        writer.write(MessageFormat.format(Strings.FORMAT_ITEM, itemType, name, itemType, body));
    }

    protected void genChildren(CodeItem item, String name) throws Exception {
        for(CodeItem i : item.getChildren()) {
            writer.write(MessageFormat.format(Strings.FORMAT_CHILDREN, name, genItem(i)));
        }
    } */
}
