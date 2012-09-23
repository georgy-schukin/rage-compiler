package rage.compiler.parser;

import goldengine.java.Reduction;
import rage.compiler.base.code.CodeObject;
import rage.compiler.base.code.items.*;
import rage.compiler.base.code.items.comp.*;
import rage.compiler.base.code.items.data.DataDeclItem;
import rage.compiler.base.code.items.data.DataItem;
import rage.compiler.base.code.items.data.DataTypeItem;
import rage.compiler.base.code.items.help.OrderItem;
import rage.compiler.base.code.items.help.RangeItem;
import rage.compiler.base.expr.BinaryExpression;
import rage.compiler.base.expr.Expression;
import rage.compiler.base.expr.ExpressionItem;
import rage.compiler.base.expr.UnaryExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyze parse tree and build code from it
 */

public class RageTreeParser {

    private interface RuleConstants
    {
        final int RULE_DATA_ID                                           =  0;  // <Data> ::= Id
        final int RULE_DATA_ID_LBRACKET_RBRACKET                         =  1;  // <Data> ::= Id '[' <Expr> ']'
        final int RULE_DATATYPE_ID                                       =  2;  // <Data type> ::= Id
        final int RULE_DATATYPE_ID_LBRACKET_RBRACKET                     =  3;  // <Data type> ::= Id '[' <Expr> ']'
        final int RULE_DATATYPE_ID_LBRACKET_RBRACKET2                    =  4;  // <Data type> ::= Id '[' ']'
        final int RULE_DATADECL                                          =  5;  // <Data decl> ::= <Data type> <Data>
        final int RULE_ORDER_SEQ                                         =  6;  // <Order> ::= seq
        final int RULE_ORDER_PAR                                         =  7;  // <Order> ::= par
        final int RULE_ORDER                                             =  8;  // <Order> ::=
        final int RULE_PROGRAM                                           =  9;  // <Program> ::= <Decl list>
        final int RULE_DECLLIST                                          = 10;  // <Decl list> ::= <Decl> <Decl list>
        final int RULE_DECLLIST2                                         = 11;  // <Decl list> ::=
        final int RULE_DECL                                              = 12;  // <Decl> ::= <Package decl>
        final int RULE_DECL2                                             = 13;  // <Decl> ::= <Code decl>
        final int RULE_PACKAGEDECL_PACKAGE_ID                            = 14;  // <Package decl> ::= package Id
        final int RULE_CODEDECL                                          = 15;  // <Code decl> ::= <Atomic code decl>
        final int RULE_CODEDECL2                                         = 16;  // <Code decl> ::= <Struct code decl>
        final int RULE_ATOMICCODEDECL_ATOMIC_ID_LPARAN_RPARAN_CODESTRING = 17;  // <Atomic code decl> ::= atomic Id '(' <Code formal arg list> ')' CodeString
        final int RULE_STRUCTCODEDECL_ID_LPARAN_RPARAN                   = 18;  // <Struct code decl> ::= Id '(' <Code formal arg list> ')' <Statement>
        final int RULE_CODEFORMALARGLIST                                 = 19;  // <Code formal arg list> ::= <Data decl>
        final int RULE_CODEFORMALARGLIST_COMMA                           = 20;  // <Code formal arg list> ::= <Data decl> ',' <Code formal arg list>
        final int RULE_CODEFORMALARGLIST2                                = 21;  // <Code formal arg list> ::=
        final int RULE_CODEARGLIST                                       = 22;  // <Code arg list> ::= <Expr>
        final int RULE_CODEARGLIST_COMMA                                 = 23;  // <Code arg list> ::= <Expr> ',' <Code arg list>
        final int RULE_CODEARGLIST2                                      = 24;  // <Code arg list> ::=
        final int RULE_STATEMENT                                         = 25;  // <Statement> ::= <Data statement>
        final int RULE_STATEMENT2                                        = 26;  // <Statement> ::= <Block statement>
        final int RULE_STATEMENT3                                        = 27;  // <Statement> ::= <Call statement>
        final int RULE_STATEMENT4                                        = 28;  // <Statement> ::= <Cond statement>
        final int RULE_STATEMENT5                                        = 29;  // <Statement> ::= <For statement>
        final int RULE_STATEMENT6                                        = 30;  // <Statement> ::= <While statement>
        final int RULE_STATEMENTLIST                                     = 31;  // <Statement list> ::= <Statement> <Statement list>
        final int RULE_STATEMENTLIST2                                    = 32;  // <Statement list> ::=
        final int RULE_DATASTATEMENT_SEMI                                = 33;  // <Data statement> ::= <Data decl> ';'
        final int RULE_BLOCKSTATEMENT_LBRACE_RBRACE                      = 34;  // <Block statement> ::= <Order> '{' <Statement list> '}'
        final int RULE_CALLSTATEMENT_ID_LPARAN_RPARAN_SEMI               = 35;  // <Call statement> ::= Id '(' <Code arg list> ')' ';'
        final int RULE_CONDSTATEMENT_IF_FI                               = 36;  // <Cond statement> ::= if <Expr> <Statement> fi
        final int RULE_CONDSTATEMENT_IF_ELSE_FI                          = 37;  // <Cond statement> ::= if <Expr> <Statement> else <Statement> fi
        final int RULE_FORSTATEMENT_FOR_ID_EQ                            = 38;  // <For statement> ::= <Order> for Id '=' <Range expr> <Statement>
        final int RULE_RANGEEXPR_DOTDOT                                  = 39;  // <Range expr> ::= <Expr> '..' <Expr>
        final int RULE_RANGEEXPR_DOTDOT_COLON                            = 40;  // <Range expr> ::= <Expr> '..' <Expr> ':' <Expr>
        final int RULE_WHILESTATEMENT_WHILE                              = 41;  // <While statement> ::= while <Expr> <Statement>
        final int RULE_EXPR                                              = 42;  // <Expr> ::= <Op or>
        final int RULE_OPOR_PIPEPIPE                                     = 43;  // <Op or> ::= <Op or> '||' <Op and>
        final int RULE_OPOR                                              = 44;  // <Op or> ::= <Op and>
        final int RULE_OPAND_AMPAMP                                      = 45;  // <Op and> ::= <Op and> '&&' <Op equate>
        final int RULE_OPAND                                             = 46;  // <Op and> ::= <Op equate>
        final int RULE_OPEQUATE_EQEQ                                     = 47;  // <Op equate> ::= <Op equate> '==' <Op compare>
        final int RULE_OPEQUATE_EXCLAMEQ                                 = 48;  // <Op equate> ::= <Op equate> '!=' <Op compare>
        final int RULE_OPEQUATE                                          = 49;  // <Op equate> ::= <Op compare>
        final int RULE_OPCOMPARE_LT                                      = 50;  // <Op compare> ::= <Op compare> '<' <Op add>
        final int RULE_OPCOMPARE_GT                                      = 51;  // <Op compare> ::= <Op compare> '>' <Op add>
        final int RULE_OPCOMPARE_LTEQ                                    = 52;  // <Op compare> ::= <Op compare> '<=' <Op add>
        final int RULE_OPCOMPARE_GTEQ                                    = 53;  // <Op compare> ::= <Op compare> '>=' <Op add>
        final int RULE_OPCOMPARE                                         = 54;  // <Op compare> ::= <Op add>
        final int RULE_OPADD_PLUS                                        = 55;  // <Op add> ::= <Op add> '+' <Op mult>
        final int RULE_OPADD_MINUS                                       = 56;  // <Op add> ::= <Op add> '-' <Op mult>
        final int RULE_OPADD                                             = 57;  // <Op add> ::= <Op mult>
        final int RULE_OPMULT_TIMES                                      = 58;  // <Op mult> ::= <Op mult> '*' <Op unary>
        final int RULE_OPMULT_DIV                                        = 59;  // <Op mult> ::= <Op mult> '/' <Op unary>
        final int RULE_OPMULT                                            = 60;  // <Op mult> ::= <Op unary>
        final int RULE_OPUNARY_EXCLAM                                    = 61;  // <Op unary> ::= '!' <Op unary>
        final int RULE_OPUNARY_MINUS                                     = 62;  // <Op unary> ::= '-' <Op unary>
        final int RULE_OPUNARY                                           = 63;  // <Op unary> ::= <Value>
        final int RULE_VALUE_DECLITERAL                                  = 64;  // <Value> ::= DecLiteral
        final int RULE_VALUE                                             = 65;  // <Value> ::= <Data>
        final int RULE_VALUE_LPARAN_RPARAN                               = 66;  // <Value> ::= '(' <Expr> ')'
    }

    //protected CodePackage codePackage;
    protected String packageName = "";
    List<CodeObject> codeObjects = new ArrayList<CodeObject>();

    public RageTreeParser() {
        //this.codePackage = codePackage;
    }

    public final String getPackageName() {
        return packageName;
    }

    public final List<CodeObject> getCodeObjects() {
        return codeObjects;
    }

    public void parseProgram(Reduction root) throws Exception {
        parseItem(root);
    }

    protected String getToken(Reduction item, int index) { // get text data of token
        return item.getToken(index).getData().toString();
    }

    protected Reduction getReduction(Reduction item, int index) throws Exception { // get text data of token
        Object child = item.getToken(index).getData();
        if(child instanceof Reduction)
            return (Reduction)child;
        else
            throw new Exception("Access to non reduction token: " + child.toString());
    }

    /*protected static String parseString(Reduction item) throws Exception {
        return item.toString();
      */

    protected Expression parseExpression(Reduction item) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_EXPR:   //<Expr> ::= <Op or>
            case RuleConstants.RULE_OPOR:   //<Op or> ::= <Op and>
            case RuleConstants.RULE_OPAND:  //<Op and> ::= <Op equate>
            case RuleConstants.RULE_OPEQUATE: //<Op equate> ::= <Op compare>
            case RuleConstants.RULE_OPCOMPARE: //<Op compare> ::= <Op add>
            case RuleConstants.RULE_OPADD: //<Op add> ::= <Op mult>
            case RuleConstants.RULE_OPMULT: //<Op mult> ::= <Op unary>
            case RuleConstants.RULE_OPUNARY: //<Op unary> ::= <Value>
                return parseExpression(getReduction(item, 0)); // continue to parse

            case RuleConstants.RULE_OPOR_PIPEPIPE:  //<Op or> ::= <Op or> '||' <Op and>
            case RuleConstants.RULE_OPAND_AMPAMP:   //<Op and> ::= <Op and> '&&' <Op equate>
            case RuleConstants.RULE_OPEQUATE_EQEQ:  //<Op equate> ::= <Op equate> '==' <Op compare>
            case RuleConstants.RULE_OPEQUATE_EXCLAMEQ: //<Op equate> ::= <Op equate> '!=' <Op compare>
            case RuleConstants.RULE_OPCOMPARE_LT:   //<Op compare> ::= <Op compare> '<' <Op add>
            case RuleConstants.RULE_OPCOMPARE_GT:   //<Op compare> ::= <Op compare> '>' <Op add>
            case RuleConstants.RULE_OPCOMPARE_LTEQ: //<Op compare> ::= <Op compare> '<=' <Op add>
            case RuleConstants.RULE_OPCOMPARE_GTEQ: //<Op compare> ::= <Op compare> '>=' <Op add>
            case RuleConstants.RULE_OPADD_PLUS:     //<Op add> ::= <Op add> '+' <Op mult>
            case RuleConstants.RULE_OPADD_MINUS:    //<Op add> ::= <Op add> '-' <Op mult>
            case RuleConstants.RULE_OPMULT_TIMES:   //<Op mult> ::= <Op mult> '*' <Op unary>
            case RuleConstants.RULE_OPMULT_DIV:     //<Op mult> ::= <Op mult> '/' <Op unary>
                return new BinaryExpression(getToken(item, 1),
                        parseExpression(getReduction(item, 0)),
                        parseExpression(getReduction(item, 2))); // parse binary op

            case RuleConstants.RULE_OPUNARY_EXCLAM: //<Op unary> ::= '!' <Op unary>
            case RuleConstants.RULE_OPUNARY_MINUS: //<Op unary> ::= '-' <Op unary>
                return new UnaryExpression(getToken(item, 0),
                        parseExpression(getReduction(item, 1))); // parse unary op

            case RuleConstants.RULE_VALUE_DECLITERAL: //<Value> ::= DecLiteral
                return new ExpressionItem(getToken(item, 0)); // save item as string

            case RuleConstants.RULE_VALUE: //<Value> ::= <Data>
                return new ExpressionItem(parseItem(getReduction(item, 0))); // save item as code item

            case RuleConstants.RULE_VALUE_LPARAN_RPARAN: //<Value> ::= '(' <Expr> ')'
                return parseExpression(getReduction(item, 1)); // continue to parse

            default:
                throw new Exception("Unrecognized expression rule: " + item.getParentRule().toString());
        }
    }

    protected List<CodeDeclItem> parseDeclItemList(Reduction item, List<CodeDeclItem> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_DECLLIST: //<Decl list> ::= <Decl> <Decl list>
                CodeItem rootItem = parseItem(getReduction(item, 0));
                if(rootItem != null) {
                    if(!(rootItem instanceof CodeDeclItem))
                        throw new Exception("Wrong item returned, need CodeDeclItem");
                    lst.add((CodeDeclItem)rootItem);
                }
                return parseDeclItemList(getReduction(item, 1), lst);

            case RuleConstants.RULE_DECLLIST2: //<Decl list> ::=
                return lst;
            default:
                throw new Exception("Unrecognized item list rule: " + item.getParentRule().toString());
        }
    }

    protected List<DataDeclItem> parseFormalArgItemList(Reduction item, List<DataDeclItem> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_CODEFORMALARGLIST: //<Code formal arg list> ::= <Data decl>
                lst.add((DataDeclItem)parseItem(getReduction(item, 0)));
                return lst;

            case RuleConstants.RULE_CODEFORMALARGLIST_COMMA: //<Code formal arg list> ::= <Data decl> ',' <Code formal arg list>
                lst.add((DataDeclItem)parseItem(getReduction(item, 0)));
                return parseFormalArgItemList(getReduction(item, 2), lst);

            case RuleConstants.RULE_CODEFORMALARGLIST2: //<Code formal arg list> ::=
                return lst;
            default:
                throw new Exception("Unrecognized item list rule: " + item.getParentRule().toString());
        }
    }

    protected List<Expression> parseArgItemList(Reduction item, List<Expression> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_CODEARGLIST: //<Code arg list> ::= <Expr>
                lst.add(parseExpression(getReduction(item, 0)));
                return lst;

            case RuleConstants.RULE_CODEARGLIST_COMMA: //<Code arg list> ::= <Expr> ',' <Code arg list>
                lst.add(parseExpression(getReduction(item, 0)));
                return parseArgItemList(getReduction(item, 2), lst);

            case RuleConstants.RULE_CODEARGLIST2: //<Code arg list> ::=
                return lst;
            default:
                throw new Exception("Unrecognized item list rule: " + item.getParentRule().toString());
        }
    }

    protected List<CodeItem> parseStatementItemList(Reduction item, List<CodeItem> lst) throws Exception {
        switch (item.getParentRule().getTableIndex()) {
            case RuleConstants.RULE_STATEMENTLIST: //<Statement list> ::= <Statement> <Statement list>
                lst.add(parseItem(getReduction(item, 0)));
                return parseStatementItemList(getReduction(item, 1), lst);

            case RuleConstants.RULE_STATEMENTLIST2: //<Statement list> ::=
                return lst;
            default:
                throw new Exception("Unrecognized item list rule: " + item.getParentRule().toString());
        }
    }

    protected CodeItem parseItem(Reduction item) throws Exception {
        switch (item.getParentRule().getTableIndex()) {

            case RuleConstants.RULE_DATA_ID: //<Data> ::= Id
                return new DataItem(getToken(item, 0)); // scalar data

            case RuleConstants.RULE_DATA_ID_LBRACKET_RBRACKET: //<Data> ::= Id '[' <Expr> ']'
                return new DataItem(getToken(item, 0), parseExpression(getReduction(item, 2))); // array elem

            case RuleConstants.RULE_DATATYPE_ID: //<Data type> ::= Id
                return new DataTypeItem(getToken(item, 0), false); // scalar data type

            case RuleConstants.RULE_DATATYPE_ID_LBRACKET_RBRACKET: //<Data type> ::= Id '[' <Expr> ']'
                return new DataTypeItem(getToken(item, 0), parseExpression(getReduction(item, 2))); // array data type

            case RuleConstants.RULE_DATATYPE_ID_LBRACKET_RBRACKET2: //<Data type> ::= Id '[' ']'
                return new DataTypeItem(getToken(item, 0), true); // empty array data type

            case RuleConstants.RULE_DATADECL:
                //<Data decl> ::= <Data type> <Data>
                return new DataDeclItem((DataTypeItem)parseItem(getReduction(item, 0)),
                        (DataItem)parseItem(getReduction(item ,1)));

            case RuleConstants.RULE_ORDER_SEQ: //<Order> ::= seq
                return new OrderItem(OrderItem.OrderType.SEQ); // sequental order

            case RuleConstants.RULE_ORDER_PAR: //<Order> ::= par
                return new OrderItem(OrderItem.OrderType.PAR); // parallel order

            case RuleConstants.RULE_ORDER: //<Order> ::=
                return new OrderItem(OrderItem.OrderType.SEQ); // seq by default

            case RuleConstants.RULE_PROGRAM:  //<Program> ::= <Decl list>
                List<CodeDeclItem> decls = new ArrayList<CodeDeclItem>();
                parseDeclItemList(getReduction(item, 0), decls); // parse all decls
                for(CodeDeclItem d : decls) {
                    if(d != null)
                        codeObjects.add(new CodeObject(d)); // create code tree for each decl
                }
                return null;

            case RuleConstants.RULE_DECL: //<Decl> ::= <Package decl>
            case RuleConstants.RULE_DECL2: //<Decl> ::= <Code decl>
            case RuleConstants.RULE_CODEDECL: //<Code decl> ::= <Atomic code decl>
            case RuleConstants.RULE_CODEDECL2: //<Code decl> ::= <Struct code decl>
                return parseItem(getReduction(item, 0));

            case RuleConstants.RULE_PACKAGEDECL_PACKAGE_ID: //<Package decl> ::= package Id
                packageName = getToken(item, 1);
                return null;

            case RuleConstants.RULE_ATOMICCODEDECL_ATOMIC_ID_LPARAN_RPARAN_CODESTRING:
                //<Atomic code decl> ::= atomic Id '(' <Code formal arg list> ')' CodeString
                return new CodeDeclItem(getToken(item, 1),
                        parseFormalArgItemList(getReduction(item, 3), new ArrayList<DataDeclItem>()),
                        getToken(item, 5));

            case RuleConstants.RULE_STRUCTCODEDECL_ID_LPARAN_RPARAN:
                //<Struct code decl> ::= Id '(' <Code formal arg list> ')' <Statement>
                return new CodeDeclItem(getToken(item, 0),
                        parseFormalArgItemList(getReduction(item, 2), new ArrayList<DataDeclItem>()),
                        parseItem(getReduction(item, 4)));

            case RuleConstants.RULE_STATEMENT: //<Statement> ::= <Data statement>
            case RuleConstants.RULE_STATEMENT2: //<Statement> ::= <Block statement>
            case RuleConstants.RULE_STATEMENT3: //<Statement> ::= <Call statement>
            case RuleConstants.RULE_STATEMENT4: //<Statement> ::= <Cond statement>
            case RuleConstants.RULE_STATEMENT5: //<Statement> ::= <For statement>
            case RuleConstants.RULE_STATEMENT6: //<Statement> ::= <While statement>
                return parseItem(getReduction(item, 0));

            case RuleConstants.RULE_DATASTATEMENT_SEMI: //<Data statement> ::= <Data decl> ';'
                return parseItem(getReduction(item, 0));

            case RuleConstants.RULE_BLOCKSTATEMENT_LBRACE_RBRACE: //<Block statement> ::= <Order> '{' <Statement list> '}'
                return new BlockItem(parseStatementItemList(getReduction(item, 2), new ArrayList<CodeItem>()),
                        (OrderItem)parseItem(getReduction(item, 0)));

            case RuleConstants.RULE_CALLSTATEMENT_ID_LPARAN_RPARAN_SEMI: //<Call statement> ::= Id '(' <Code arg list> ')' ';'
                return new CallItem(getToken(item, 0),
                        parseArgItemList(getReduction(item, 2), new ArrayList<Expression>()));

            case RuleConstants.RULE_CONDSTATEMENT_IF_FI: //<Cond statement> ::= if <Expr> <Statement> fi
                return new CondItem(parseExpression(getReduction(item, 1)),
                        parseItem(getReduction(item, 2)));

            case RuleConstants.RULE_CONDSTATEMENT_IF_ELSE_FI: //<Cond statement> ::= if <Expr> <Statement> else <Statement> fi
                return new CondItem(parseExpression(getReduction(item, 1)),
                        parseItem(getReduction(item, 2)),
                        parseItem(getReduction(item, 4)));

            case RuleConstants.RULE_FORSTATEMENT_FOR_ID_EQ: //<For statement> ::= <Order> for Id '=' <Range expr> <Statement>
                return new ForItem(getToken(item, 2), (RangeItem)parseItem(getReduction(item, 4)),
                        parseItem(getReduction(item, 5)),
                        (OrderItem)parseItem(getReduction(item, 0)));

            case RuleConstants.RULE_RANGEEXPR_DOTDOT: //<Range expr> ::= <Expr> '..' <Expr>
                return new RangeItem(parseExpression(getReduction(item, 0)),
                        parseExpression(getReduction(item, 2)),
                        new ExpressionItem("1")); // step=1 by default

            case RuleConstants.RULE_RANGEEXPR_DOTDOT_COLON: //<Range expr> ::= <Expr> '..' <Expr> ':' <Expr>
                return new RangeItem(parseExpression(getReduction(item, 0)),
                        parseExpression(getReduction(item, 2)),
                        parseExpression(getReduction(item, 4)));

            case RuleConstants.RULE_WHILESTATEMENT_WHILE: //<While statement> ::= while <Expr> <Statement>
                return new WhileItem(parseExpression(getReduction(item, 1)),
                        parseItem(getReduction(item, 2)));
            default:
                throw new Exception("Unrecognized item rule: " + item.getParentRule().toString());
        }
    }
}
