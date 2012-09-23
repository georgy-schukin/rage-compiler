#include "code.h"
using namespace rage;
namespace test {

//Code objects
//init : atomic code object
class init : public AtomicCodeObject {
protected:
void init(CodeLibrary *lib) {
	addArg("p");
	addArg("i");
}
public:
void exec(const CodeArgs& args) {
args["p"] = args["i"];
}
}

//inc : atomic code object
class inc : public AtomicCodeObject {
protected:
void init(CodeLibrary *lib) {
	addArg("p");
}
public:
void exec(const CodeArgs& args) {
args["p"] += 3;
}
}

//print : atomic code object
class print : public AtomicCodeObject {
protected:
void init(CodeLibrary *lib) {
	addArg("p");
}
public:
void exec(const CodeArgs& args) {
printf("%d\n", args["p"]);
}
}

//main : structured code object
class main : public StructuredCodeObject {
protected:
void init(CodeLibrary *lib) {
	lib->add<inc>("test.inc");
	lib->add<init>("test.init");
	lib->add<print>("test.print");
}
void buildTree(CodeTree *tree, CodeLibrary *lib) {

	BlockVertex *vertex_0 = new BlockVertex(CompVertex::SEQ);

	DataVertex *vertex_1 = new DataVertex("a", DataVertex::BLOCK, sizeof(int), new ExpressionConstItem(20));
		vertex_0->addChild(vertex_1);

	IndexedVertex *vertex_2 = new IndexedVertex("i", CompVertex::PAR, new ExpressionConstItem(1), new ExpressionConstItem(20), new ExpressionConstItem(1));

	BlockVertex *vertex_3 = new BlockVertex(CompVertex::SEQ);

	CallVertex *vertex_4 = new CallVertex(lib->get("test.init"));
		vertex_4->addArg(new ExpressionDataItem("a", new ExpressionDataItem("i")));
		vertex_4->addArg(new BinaryExpression(BinaryOperation::MULT, new ExpressionDataItem("i"), new ExpressionConstItem(2)));
		vertex_4->addDataReq("a");
		vertex_4->addDataReq("i");
		vertex_3->addChild(vertex_4);

	CallVertex *vertex_5 = new CallVertex(lib->get("test.inc"));
		vertex_5->addArg(new ExpressionDataItem("a", new ExpressionDataItem("i")));
		vertex_5->addDataReq("a");
		vertex_5->addDataReq("i");
		vertex_3->addChild(vertex_5);

	CondVertex *vertex_6 = new CondVertex(new BinaryExpression(BinaryOperation::GT, new ExpressionDataItem("a", new ExpressionDataItem("i")), new ExpressionConstItem(10)));

	CallVertex *vertex_7 = new CallVertex(lib->get("test.print"));
		vertex_7->addArg(new ExpressionDataItem("a", new ExpressionDataItem("i")));
		vertex_7->addDataReq("a");
		vertex_7->addDataReq("i");
		vertex_6->addChild(vertex_7);
		vertex_3->addChild(vertex_6);
		vertex_2->addChild(vertex_3);
		vertex_0->addChild(vertex_2);
	tree->setRoot(vertex_0);
}
}

}
