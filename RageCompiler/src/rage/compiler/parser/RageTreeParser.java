package rage.compiler.parser;

import goldengine.java.Reduction;
import rage.compiler.base.code.AtomicCodeObject;
import rage.compiler.base.code.CodeObject;
import rage.compiler.base.code.CodePackage;
import rage.compiler.base.code.StructuredCodeObject;
import rage.compiler.base.code.items.*;
import rage.compiler.base.code.items.comp.*;
import rage.compiler.base.code.items.help.ArgItem;
import rage.compiler.base.code.items.help.OrderItem;
import rage.compiler.base.code.items.help.RangeItem;
import rage.compiler.base.code.items.var.VarItem;
import rage.compiler.base.expr.*;
import rage.compiler.base.expr.items.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyze parse tree and build code from it
 */

public class RageTreeParser {

    public interface RuleConstants
    {
        final int RULE_PROGRAM                                   =  0;  // <Program> ::= <Decl list>
        final int RULE_DECLLIST                                  =  1;  // <Decl list> ::= <Decl> <Decl list>
        final int RULE_DECLLIST2                                 =  2;  // <Decl list> ::= <Decl>
        final int RULE_DECL                                      =  3;  // <Decl> ::= <Package decl>
        final int RULE_DECL2                                     =  4;  // <Decl> ::= <Use decl>
        final int RULE_DECL3                                     =  5;  // <Decl> ::= <Code decl>
        final int RULE_PACKAGEDECL_PACKAGE_ID                    =  6;  // <Package decl> ::= package Id
        final int RULE_USEDECL_USE_ID                            =  7;  // <Use decl> ::= use Id
        final int RULE_CODEDECL                                  =  8;  // <Code decl> ::= <Atomic code decl>
        final int RULE_CODEDECL2                                 =  9;  // <Code decl> ::= <Struct code decl>
        final int RULE_ATOMICCODEDECL_ATOMIC_ID_LPARAN_RPARAN_ID = 10;  // <Atomic code decl> ::= atomic Id '(' <Arg list> ')' Id
        final int RULE_STRUCTCODEDECL_FUNC_ID_LPARAN_RPARAN      = 11;  // <Struct code decl> ::= <Arg list> func Id '(' <Arg list> ')' <Statement>
        final int RULE_ARGLIST_COMMA                             = 12;  // <Arg list> ::= <Arg> ',' <Arg list>
        final int RULE_ARGLIST                                   = 13;  // <Arg list> ::= <Arg>
        final int RULE_ARGLIST2                                  = 14;  // <Arg list> ::=
        final int RULE_ARG_AT_ID                                 = 15;  // <Arg> ::= '@' Id
        final int RULE_ARG_AT                                    = 16;  // <Arg> ::= '@'
        final int RULE_STATEMENT_IF_LPARAN_RPARAN                = 17;  // <Statement> ::= if '(' <Expr> ')' <Statement>
        final int RULE_STATEMENT_IF_LPARAN_RPARAN_ELSE           = 18;  // <Statement> ::= if '(' <Expr> ')' <Then statement> else <Statement>
        final int RULE_STATEMENT_WHILE_LPARAN_RPARAN             = 19;  // <Statement> ::= while '(' <Expr> ')' <Statement>
        final int RULE_STATEMENT_FOR_LPARAN_ID_EQ_RPARAN         = 20;  // <Statement> ::= <Order> for '(' Id '=' <Range expr> ')' <Statement>
        final int RULE_STATEMENT_FOR_LPARAN_ID_IN_RPARAN         = 21;  // <Statement> ::= <Order> for '(' Id in <Expr> ')' <Statement>
        final int RULE_STATEMENT_REDUCE                          = 22;  // <Statement> ::= reduce
        final int RULE_STATEMENT                                 = 23;  // <Statement> ::= <Normal statement>
        final int RULE_THENSTATEMENT_IF_LPARAN_RPARAN_ELSE       = 24;  // <Then statement> ::= if '(' <Expr> ')' <Then statement> else <Then statement>
        final int RULE_THENSTATEMENT_WHILE_LPARAN_RPARAN         = 25;  // <Then statement> ::= while '(' <Expr> ')' <Then statement>
        final int RULE_THENSTATEMENT_FOR_LPARAN_ID_EQ_RPARAN     = 26;  // <Then statement> ::= <Order> for '(' Id '=' <Range expr> ')' <Then statement>
        final int RULE_THENSTATEMENT_FOR_LPARAN_ID_IN_RPARAN     = 27;  // <Then statement> ::= <Order> for '(' Id in <Expr> ')' <Then statement>
        final int RULE_THENSTATEMENT_REDUCE                      = 28;  // <Then statement> ::= reduce
        final int RULE_THENSTATEMENT                             = 29;  // <Then statement> ::= <Normal statement>
        final int RULE_NORMALSTATEMENT                           = 30;  // <Normal statement> ::= <Block statement>
        final int RULE_NORMALSTATEMENT_EQ_SEMI                   = 31;  // <Normal statement> ::= <Expr list> '=' <Expr list> ';'
        final int RULE_NORMALSTATEMENT_SEMI                      = 32;  // <Normal statement> ::= <Expr list> ';'
        final int RULE_NORMALSTATEMENT_SEMI2                     = 33;  // <Normal statement> ::= ';'
        final int RULE_NORMALSTATEMENT_RETURN_SEMI               = 34;  // <Normal statement> ::= return <Expr list> ';'
        final int RULE_BLOCKSTATEMENT_LBRACE_RBRACE              = 35;  // <Block statement> ::= <Order> '{' <Statement list> '}'
        final int RULE_STATEMENTLIST                             = 36;  // <Statement list> ::= <Statement> <Statement list>
        final int RULE_STATEMENTLIST2                            = 37;  // <Statement list> ::=
        final int RULE_RANGEEXPR_DOTDOT                          = 38;  // <Range expr> ::= <Expr> '..' <Expr>
        final int RULE_RANGEEXPR_DOTDOT_COLON                    = 39;  // <Range expr> ::= <Expr> '..' <Expr> ':' <Expr>
        final int RULE_VARIABLE_ID                               = 40;  // <Variable> ::= Id
        final int RULE_VARIABLE_ID_LBRACKET_RBRACKET             = 41;  // <Variable> ::= Id '[' <Expr> ']'
        final int RULE_ORDER_PAR                                 = 42;  // <Order> ::= par
        final int RULE_ORDER_SEQ                                 = 43;  // <Order> ::= seq
        final int RULE_ORDER                                     = 44;  // <Order> ::=
        final int RULE_EXPR                                      = 45;  // <Expr> ::= <Op or>
        final int RULE_OPOR_PIPEPIPE                             = 46;  // <Op or> ::= <Op or> '||' <Op and>
        final int RULE_OPOR_OR                                   = 47;  // <Op or> ::= <Op or> or <Op and>
        final int RULE_OPOR                                      = 48;  // <Op or> ::= <Op and>
        final int RULE_OPAND_AMPAMP                              = 49;  // <Op and> ::= <Op and> '&&' <Op equate>
        final int RULE_OPAND_AND                                 = 50;  // <Op and> ::= <Op and> and <Op equate>
        final int RULE_OPAND                                     = 51;  // <Op and> ::= <Op equate>
        final int RULE_OPEQUATE_EQEQ                             = 52;  // <Op equate> ::= <Op equate> '==' <Op compare>
        final int RULE_OPEQUATE_EXCLAMEQ                         = 53;  // <Op equate> ::= <Op equate> '!=' <Op compare>
        final int RULE_OPEQUATE                                  = 54;  // <Op equate> ::= <Op compare>
        final int RULE_OPCOMPARE_LT                              = 55;  // <Op compare> ::= <Op compare> '<' <Op add>
        final int RULE_OPCOMPARE_GT                              = 56;  // <Op compare> ::= <Op compare> '>' <Op add>
        final int RULE_OPCOMPARE_LTEQ                            = 57;  // <Op compare> ::= <Op compare> '<=' <Op add>
        final int RULE_OPCOMPARE_GTEQ                            = 58;  // <Op compare> ::= <Op compare> '>=' <Op add>
        final int RULE_OPCOMPARE                                 = 59;  // <Op compare> ::= <Op add>
        final int RULE_OPADD_PLUS                                = 60;  // <Op add> ::= <Op add> '+' <Op mult>
        final int RULE_OPADD_MINUS                               = 61;  // <Op add> ::= <Op add> '-' <Op mult>
        final int RULE_OPADD                                     = 62;  // <Op add> ::= <Op mult>
        final int RULE_OPMULT_TIMES                              = 63;  // <Op mult> ::= <Op mult> '*' <Op unary>
        final int RULE_OPMULT_DIV                                = 64;  // <Op mult> ::= <Op mult> '/' <Op unary>
        final int RULE_OPMULT_PERCENT                            = 65;  // <Op mult> ::= <Op mult> '%' <Op unary>
        final int RULE_OPMULT                                    = 66;  // <Op mult> ::= <Op unary>
        final int RULE_OPUNARY_EXCLAM                            = 67;  // <Op unary> ::= '!' <Op unary>
        final int RULE_OPUNARY_NOT                               = 68;  // <Op unary> ::= not <Op unary>
        final int RULE_OPUNARY_MINUS                             = 69;  // <Op unary> ::= '-' <Op unary>
        final int RULE_OPUNARY                                   = 70;  // <Op unary> ::= <Value>
        final int RULE_VALUE_DECLITERAL                          = 71;  // <Value> ::= DecLiteral
        final int RULE_VALUE_FLOATLITERAL                        = 72;  // <Value> ::= FloatLiteral
        final int RULE_VALUE_STRINGLITERAL                       = 73;  // <Value> ::= StringLiteral
        final int RULE_VALUE                                     = 74;  // <Value> ::= <Variable>
        final int RULE_VALUE_DOT_ID                              = 75;  // <Value> ::= <Variable> '.' Id
        final int RULE_VALUE_ID_LPARAN_RPARAN                    = 76;  // <Value> ::= Id '(' <Expr list> ')'
        final int RULE_VALUE_ID_LPARAN_RPARAN2                   = 77;  // <Value> ::= Id '(' ')'
        final int RULE_VALUE_LPARAN_RPARAN                       = 78;  // <Value> ::= '(' <Expr> ')'
        final int RULE_VALUE_LBRACKET_RBRACKET                   = 79;  // <Value> ::= '[' <Expr list> ']'
        final int RULE_VALUE_LBRACKET_RBRACKET2                  = 80;  // <Value> ::= '[' ']'
        final int RULE_EXPRLIST                                  = 81;  // <Expr list> ::= <Expr>
        final int RULE_EXPRLIST_COMMA                            = 82;  // <Expr list> ::= <Expr> ',' <Expr list>
    }

    //protected CodePackage codePackage;
    protected String packageName = "";
    protected List<String> usedPackages = new ArrayList<String>();
    //List<CodeObject> codeObjects = new ArrayList<CodeObject>();

    public RageTreeParser() {
        //this.codePackage = codePackage;
    }

    /*
     * Parse output from Gold parser
     * Returns code package: list of code objects, each code object is a tree of code items
     * Root reduction should be "<Program> ::= <Decl list>" rule to work properly
     */
    public CodePackage parseProgram(Reduction root) throws Exception {
        if(root.getParentRule().getTableIndex() != RuleConstants.RULE_PROGRAM) // <Program> ::= <Decl list>
            throw new Exception("Incorrect root rule: " + root.getParentRule().getText());
        List<CodeObject> codeObjects = parseCodePackage(getReduction(root, 0), new ArrayList<CodeObject>());
        /*parseDeclItemList(getReduction(item, 0), decls); // parse all decls
        for(CodeDeclItem d : decls) {
            if(d != null)
                codeObjects.add(new CodeObject(d)); // create code tree for each decl
        }
        return null;

        parseItem(root);*/
        return new CodePackage(packageName, codeObjects);
    }

    /*
     * If [index] child of reduction item is a string (simple item) - get its value
     */
    protected String getToken(Reduction item, int index) throws Exception { // get text var of token
        Object child = item.getToken(index).getData();
        if(child instanceof Reduction)
            throw new Exception("String access to reduction token: " + child.toString() +
                    "\n\tin rule " + item.getParentRule().getText());
        return child.toString();
    }

    /*
     * If [index] child of reduction item is a reduction too - get it
     */
    protected Reduction getReduction(Reduction item, int index) throws Exception { // get text var of token
        Object child = item.getToken(index).getData();
        if(child instanceof Reduction)
            return (Reduction)child;
        else
            throw new Exception("Access to non reduction token \"" + child.toString() +  "\"" + " by index " + index +
                "\n\tin rule " + item.getParentRule().getText());
    }

    /*protected static String parseString(Reduction item) throws Exception {
        return item.toString();
      */

    /*
     * Parse reduction item in case it's an expression
     * Returns expression tree
     */
    protected Expression parseExpression(Reduction item) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_EXPR:           //<Expr> ::= <Op or>
            case RuleConstants.RULE_OPOR:           //<Op or> ::= <Op and>
            case RuleConstants.RULE_OPAND:          //<Op and> ::= <Op equate>
            case RuleConstants.RULE_OPEQUATE:       //<Op equate> ::= <Op compare>
            case RuleConstants.RULE_OPCOMPARE:      //<Op compare> ::= <Op add>
            case RuleConstants.RULE_OPADD:          //<Op add> ::= <Op mult>
            case RuleConstants.RULE_OPMULT:         //<Op mult> ::= <Op unary>
            case RuleConstants.RULE_OPUNARY:        //<Op unary> ::= <Value>
                return parseExpression(getReduction(item, 0)); // continue to parse

            case RuleConstants.RULE_OPOR_PIPEPIPE:      //<Op or> ::= <Op or> '||' <Op and>
            case RuleConstants.RULE_OPOR_OR:            //<Op or> ::= <Op or> or <Op and>
            case RuleConstants.RULE_OPAND_AMPAMP:       //<Op and> ::= <Op and> '&&' <Op equate>
            case RuleConstants.RULE_OPAND_AND:          //<Op and> ::= <Op and> and <Op equate>
            case RuleConstants.RULE_OPEQUATE_EQEQ:      //<Op equate> ::= <Op equate> '==' <Op compare>
            case RuleConstants.RULE_OPEQUATE_EXCLAMEQ:  //<Op equate> ::= <Op equate> '!=' <Op compare>
            case RuleConstants.RULE_OPCOMPARE_LT:       //<Op compare> ::= <Op compare> '<' <Op add>
            case RuleConstants.RULE_OPCOMPARE_GT:       //<Op compare> ::= <Op compare> '>' <Op add>
            case RuleConstants.RULE_OPCOMPARE_LTEQ:     //<Op compare> ::= <Op compare> '<=' <Op add>
            case RuleConstants.RULE_OPCOMPARE_GTEQ:     //<Op compare> ::= <Op compare> '>=' <Op add>
            case RuleConstants.RULE_OPADD_PLUS:         //<Op add> ::= <Op add> '+' <Op mult>
            case RuleConstants.RULE_OPADD_MINUS:        //<Op add> ::= <Op add> '-' <Op mult>
            case RuleConstants.RULE_OPMULT_TIMES:       //<Op mult> ::= <Op mult> '*' <Op unary>
            case RuleConstants.RULE_OPMULT_DIV:         //<Op mult> ::= <Op mult> '/' <Op unary>
            case RuleConstants.RULE_OPMULT_PERCENT:     //<Op mult> ::= <Op mult> '%' <Op unary>
                return new BinaryExpression(getToken(item, 1),
                        parseExpression(getReduction(item, 0)),
                        parseExpression(getReduction(item, 2))); // parse binary op

            case RuleConstants.RULE_OPUNARY_EXCLAM:     //<Op unary> ::= '!' <Op unary>
            case RuleConstants.RULE_OPUNARY_NOT:        //<Op unary> ::= not <Op unary>
            case RuleConstants.RULE_OPUNARY_MINUS:      //<Op unary> ::= '-' <Op unary>
                return new UnaryExpression(getToken(item, 0),
                        parseExpression(getReduction(item, 1))); // parse unary op

            case RuleConstants.RULE_VALUE_DECLITERAL:       //<Value> ::= DecLiteral
            case RuleConstants.RULE_VALUE_FLOATLITERAL:     //<Value> ::= FloatLiteral
            case RuleConstants.RULE_VALUE_STRINGLITERAL:    //<Value> ::= StringLiteral
                return new ExpressionConstItem(getToken(item, 0)); // save const item as string

            case RuleConstants.RULE_VALUE:                  //<Value> ::= <Variable>
                return new ExpressionVarItem((VarItem)parseItem(getReduction(item, 0))); // save item as var item

            case RuleConstants.RULE_VALUE_DOT_ID:           //<Value> ::= <Variable> '.' Id
                return new ExpressionVarPropertyItem((VarItem)parseItem(getReduction(item, 0)),
                        getToken(item, 2)); // save item as var property item

            case RuleConstants.RULE_VALUE_LPARAN_RPARAN:    //<Value> ::= '(' <Expr> ')'
                return parseExpression(getReduction(item, 1)); // continue to parse

            case RuleConstants.RULE_VALUE_ID_LPARAN_RPARAN:     //<Value> ::= Id '(' <Expr list> ')'
                return new ExpressionCallItem(getToken(item, 0),
                        parseExpressionList(getReduction(item, 2), new ArrayList<Expression>())); // func call item

            case RuleConstants.RULE_VALUE_ID_LPARAN_RPARAN2:    //<Value> ::= Id '(' ')'
                return new ExpressionCallItem(getToken(item, 0)); // func call item with no args

            case RuleConstants.RULE_VALUE_LBRACKET_RBRACKET:    //<Value> ::= '[' <Expr list> ']'
                return new ExpressionListItem(parseExpressionList(getReduction(item, 1), new ArrayList<Expression>()));

            case RuleConstants.RULE_VALUE_LBRACKET_RBRACKET2:   //<Value> ::= '[' ']'
                return new ExpressionListItem(new ArrayList<Expression>()); // empty list

            default:
                throw new Exception("Unrecognized expression rule: " + item.getParentRule().toString());
        }
    }

    /*
     * Parse code package "root" - list of code objects declarations
     */
    protected List<CodeObject> parseCodePackage(Reduction item, List<CodeObject> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_DECLLIST:   //<Decl list> ::= <Decl> <Decl list>
                parseCodePackage(getReduction(item, 0), lst);
                return parseCodePackage(getReduction(item, 1), lst);

            case RuleConstants.RULE_DECLLIST2:  //<Decl list> ::= <Decl>
                return parseCodePackage(getReduction(item, 0), lst);

            case RuleConstants.RULE_DECL:       //<Decl> ::= <Package decl>
            case RuleConstants.RULE_DECL2:      //<Decl> ::= <Use decl>
            case RuleConstants.RULE_DECL3:      //<Decl> ::= <Code decl>
            case RuleConstants.RULE_CODEDECL:   //<Code decl> ::= <Atomic code decl>
            case RuleConstants.RULE_CODEDECL2:  //<Code decl> ::= <Struct code decl>
                return parseCodePackage(getReduction(item, 0), lst);

            case RuleConstants.RULE_PACKAGEDECL_PACKAGE_ID: //<Package decl> ::= package Id
                packageName = getToken(item, 1);        // set package name
                return lst;

            case RuleConstants.RULE_USEDECL_USE_ID:         //<Use decl> ::= use Id
                usedPackages.add(getToken(item, 1));    // add package usage
                return lst;

            case RuleConstants.RULE_ATOMICCODEDECL_ATOMIC_ID_LPARAN_RPARAN_ID:
                // <Atomic code decl> ::= atomic Id '(' <Arg list> ')' Id
                lst.add(new AtomicCodeObject(getToken(item, 1),
                        parseArgList(getReduction(item, 3), new ArrayList<ArgItem>()), null,
                        getToken(item, 5))); // create atomic CO
                return lst;

            case RuleConstants.RULE_STRUCTCODEDECL_FUNC_ID_LPARAN_RPARAN:
                //<Struct code decl> ::= <Arg list> func Id '(' <Arg list> ')' <Statement>
                lst.add(new StructuredCodeObject(getToken(item, 2),
                        parseArgList(getReduction(item, 4), new ArrayList<ArgItem>()),
                        parseArgList(getReduction(item, 0), new ArrayList<ArgItem>()),
                        parseItem(getReduction(item, 6)))); // create structured CO
                return lst;
            default:
                throw new Exception("Unrecognized code package rule: " + item.getParentRule().toString());
        }
    }

    /*
     * Parse formal args list. Return ArgItem for each arg
     */
    protected List<ArgItem> parseArgList(Reduction item, List<ArgItem> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_ARGLIST_COMMA:  //<Arg list> ::= <Arg> ',' <Arg list>
                lst.add((ArgItem)parseItem(getReduction(item, 0)));
                return parseArgList(getReduction(item, 2), lst);

            case RuleConstants.RULE_ARGLIST:        //<Arg list> ::= <Arg>
                lst.add((ArgItem)parseItem(getReduction(item, 0)));
                return lst;

            case RuleConstants.RULE_ARGLIST2:       //<Arg list> ::=
                return lst;
            default:
                throw new Exception("Unrecognized arg list rule: " + item.getParentRule().toString());
        }
    }

    /*
     * Parse general list of expressions
     */
    protected List<Expression> parseExpressionList(Reduction item, List<Expression> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_EXPRLIST:       //<Expr list> ::= <Expr>
                lst.add(parseExpression(getReduction(item, 0)));
                return lst;
            case RuleConstants.RULE_EXPRLIST_COMMA: //<Expr list> ::= <Expr> ',' <Expr list>
                lst.add(parseExpression(getReduction(item, 0)));
                return parseExpressionList(getReduction(item, 2), lst);
            default:
                throw new Exception("Unrecognized expression list rule: " + item.getParentRule().toString());
        }
    }

    /*
     * Parse list of statements. Return CodeItem for each statement
     */
    protected List<CodeItem> parseStatementList(Reduction item, List<CodeItem> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_STATEMENTLIST: //<Statement list> ::= <Statement> <Statement list>
                lst.add(parseItem(getReduction(item, 0)));
                return parseStatementList(getReduction(item, 1), lst);

            case RuleConstants.RULE_STATEMENTLIST2: //<Statement list> ::=
                return lst;
            default:
                throw new Exception("Unrecognized statement list rule: " + item.getParentRule().toString());
        }
    }

    /*
     * Parse statement (reduction) and return CodeItem for it (to build code tree from these items)
     * Statement - anything that can be inside structured code object
     * Create special code items for statements as loops, blocks, etc.
     */
    protected CodeItem parseItem(Reduction item) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_ARG_AT_ID:      //<Arg> ::= '@' Id
                return new ArgItem(getToken(item, 1));

            case RuleConstants.RULE_ARG_AT:         //<Arg> ::= '@'
                return new ArgItem(""); // empty arg name

            case RuleConstants.RULE_VARIABLE_ID:    //<Variable> ::= Id
                return new VarItem(getToken(item, 0));

            case RuleConstants.RULE_VARIABLE_ID_LBRACKET_RBRACKET:  //<Variable> ::= Id '[' <Expr> ']'
                return new VarItem(getToken(item, 0),
                        parseExpression(getReduction(item, 2)));

            case RuleConstants.RULE_ORDER_SEQ: //<Order> ::= seq
                return new OrderItem(OrderItem.OrderType.SEQ); // sequental order

            case RuleConstants.RULE_ORDER_PAR: //<Order> ::= par
                return new OrderItem(OrderItem.OrderType.PAR); // parallel order

            case RuleConstants.RULE_ORDER: //<Order> ::=
                return new OrderItem(OrderItem.OrderType.SEQ); // seq by default

            case RuleConstants.RULE_STATEMENT_IF_LPARAN_RPARAN:
                //<Statement> ::= if '(' <Expr> ')' <Statement>
                return new CondItem(parseExpression(getReduction(item, 2)),
                        parseItem(getReduction(item, 4))); // one variant if

            case RuleConstants.RULE_STATEMENT_IF_LPARAN_RPARAN_ELSE:
                //<Statement> ::= if '(' <Expr> ')' <Then statement> else <Statement>
            case RuleConstants.RULE_THENSTATEMENT_IF_LPARAN_RPARAN_ELSE:
                //<Then statement> ::= if '(' <Expr> ')' <Then statement> else <Then statement>
                return new CondItem(parseExpression(getReduction(item, 2)),
                        parseItem(getReduction(item, 4)),
                        parseItem((getReduction(item, 6)))); // two variants if

            case RuleConstants.RULE_STATEMENT_WHILE_LPARAN_RPARAN:
                //<Statement> ::= while '(' <Expr> ')' <Statement>
            case RuleConstants.RULE_THENSTATEMENT_WHILE_LPARAN_RPARAN:
                //<Then statement> ::= while '(' <Expr> ')' <Then statement>
                return new WhileItem(parseExpression(getReduction(item, 2)),
                        parseItem(getReduction(item, 4)));

            case RuleConstants.RULE_STATEMENT_FOR_LPARAN_ID_EQ_RPARAN:
                //<Statement> ::= <Order> for '(' Id '=' <Range expr> ')' <Statement>
            case RuleConstants.RULE_THENSTATEMENT_FOR_LPARAN_ID_EQ_RPARAN:
                //<Then statement> ::= <Order> for '(' Id '=' <Range expr> ')' <Then statement>
                return new ForItem(getToken(item, 3), (RangeItem)parseItem(getReduction(item, 5)),
                        parseItem(getReduction(item, 7)), (OrderItem)parseItem(getReduction(item, 0)));

            case RuleConstants.RULE_STATEMENT: //<Statement> ::= <Normal statement>
            case RuleConstants.RULE_THENSTATEMENT: //<Then statement> ::= <Normal statement>
            case RuleConstants.RULE_NORMALSTATEMENT:    //<Normal statement> ::= <Block statement>
                return parseItem(getReduction(item, 0));

            case RuleConstants.RULE_NORMALSTATEMENT_EQ_SEMI:
                //<Normal statement> ::= <Expr list> '=' <Expr list> ';'
                return new AssignItem(parseExpressionList(getReduction(item, 2), new ArrayList<Expression>()),
                        parseExpressionList(getReduction(item, 0), new ArrayList<Expression>()));

            case RuleConstants.RULE_NORMALSTATEMENT_SEMI:
                //<Normal statement> ::= <Expr list> ';'
                return new AssignItem(parseExpressionList(getReduction(item, 0), new ArrayList<Expression>()));

            case RuleConstants.RULE_NORMALSTATEMENT_SEMI2: //<Normal statement> ::= ';'
                return null;

            case RuleConstants.RULE_BLOCKSTATEMENT_LBRACE_RBRACE: //<Block statement> ::= <Order> '{' <Statement list> '}'
                return new BlockItem(parseStatementList(getReduction(item, 2), new ArrayList<CodeItem>()),
                        (OrderItem)parseItem(getReduction(item, 0)));

            case RuleConstants.RULE_RANGEEXPR_DOTDOT: //<Range expr> ::= <Expr> '..' <Expr>
                return new RangeItem(parseExpression(getReduction(item, 0)),
                        parseExpression(getReduction(item, 2)),
                        new ExpressionConstItem("1")); // step=1 by default

            case RuleConstants.RULE_RANGEEXPR_DOTDOT_COLON: //<Range expr> ::= <Expr> '..' <Expr> ':' <Expr>
                return new RangeItem(parseExpression(getReduction(item, 0)),
                        parseExpression(getReduction(item, 2)),
                        parseExpression(getReduction(item, 4)));

            case RuleConstants.RULE_STATEMENT_FOR_LPARAN_ID_IN_RPARAN:
                //<Statement> ::= <Order> for '(' Id in <Expr> ')' <Statement>
            case RuleConstants.RULE_THENSTATEMENT_FOR_LPARAN_ID_IN_RPARAN:
                //<Then statement> ::= <Order> for '(' Id in <Expr> ')' <Then statement>
            case RuleConstants.RULE_STATEMENT_REDUCE:
                //<Statement> ::= reduce
            case RuleConstants.RULE_THENSTATEMENT_REDUCE:
                //<Then statement> ::= reduce
            case RuleConstants.RULE_NORMALSTATEMENT_RETURN_SEMI:
                //<Normal statement> ::= return <Expr list> ';'
                throw new Exception("Sorry, rule " + item.getParentRule().getText() + "isn't supported yet!");

            default:
                throw new Exception("Unrecognized item rule: " + item.getParentRule().toString());
        }
    }
}
