package rage.compiler.codegen.cpp;

import rage.compiler.base.code.items.var.DataItem;
import rage.compiler.base.expr.BinaryExpression;
import rage.compiler.base.expr.Expression;
import rage.compiler.base.expr.ExpressionItem;
import rage.compiler.base.expr.UnaryExpression;

/**
 * Generate expressions for C++ runtime
 */

public class CPPExpressionGenerator {
    public static String convertBinaryOp(String op) throws Exception {
        if(op.equals("+")) return "BinaryOperation::PLUS";
        else if(op.equals("-")) return "BinaryOperation::MINUS";
        else if(op.equals("*")) return "BinaryOperation::MULT";
        else if(op.equals("/")) return "BinaryOperation::DIV";
        else if(op.equals("<")) return "BinaryOperation::LT";
        else if(op.equals("<=")) return "BinaryOperation::LE";
        else if(op.equals(">")) return "BinaryOperation::GT";
        else if(op.equals(">=")) return "BinaryOperation::GE";
        else if(op.equals("&&")) return "BinaryOperation::AND";
        else if(op.equals("||")) return "BinaryOperation::OR";
        else
            throw new Exception("Unknown binary operation: " + op);
    }

    public static String convertUnaryOp(String op) throws Exception {
        if(op.equals("-")) return "UnaryOperation::MINUS";
        else if(op.equals("!")) return "UnaryOperation::NOT";
        else
            throw new Exception("Unknown unary operation: " + op);
    }

    public static String generateExpression(Expression expr) throws Exception {
        if(expr instanceof BinaryExpression) {
            BinaryExpression bExpr = (BinaryExpression)expr;
            return "new BinaryExpression(" +
                    convertBinaryOp(bExpr.getOperation()) + ", " +
                    generateExpression(bExpr.getFirstMember()) + ", " +
                    generateExpression(bExpr.getSecondMember()) + ")";
        } else if (expr instanceof UnaryExpression) {
            UnaryExpression uExpr = (UnaryExpression)expr;
            return "new UnaryExpression(" +
                    convertUnaryOp(uExpr.getOperation()) + ", " +
                    generateExpression(uExpr.getMember()) + ")";

        } /*else if(expr instanceof ExpressionItem) {
            ExpressionItem eItem = (ExpressionItem)expr;
            if(eItem.isData()) {
                DataItem dataItem = (DataItem)eItem.getItem();
                return "new ExpressionDataItem(\"" + dataItem.getName() + "\"" +
                        ((dataItem.getArrayExpr() != null) ? ", " + generateExpression(dataItem.getArrayExpr()) : "") + ")";
            } else {
                return "new ExpressionConstItem(" + eItem.getItem().toString() + ")";
            }
        }*/ else throw new Exception("Unknown expression type");
    }
}
