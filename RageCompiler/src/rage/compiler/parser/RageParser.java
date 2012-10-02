package rage.compiler.parser;

import goldengine.java.*;
import rage.compiler.base.code.CodePackage;
import rage.compiler.check.CodeChecker;
import rage.compiler.codegen.CodeGenerator;
import rage.compiler.codegen.cpp.CPPCodeGenerator;

/*
 * Rage parser
 */

public class RageParser implements GPMessageConstants
{
    private static final String grammarFile = "Rage.cgt";

    private interface SymbolConstants
    {
        final int SYMBOL_EOF             =  0;  // (EOF)
        final int SYMBOL_ERROR           =  1;  // (Error)
        final int SYMBOL_WHITESPACE      =  2;  // (Whitespace)
        final int SYMBOL_COMMENTEND      =  3;  // (Comment End)
        final int SYMBOL_COMMENTLINE     =  4;  // (Comment Line)
        final int SYMBOL_COMMENTSTART    =  5;  // (Comment Start)
        final int SYMBOL_MINUS           =  6;  // '-'
        final int SYMBOL_EXCLAM          =  7;  // '!'
        final int SYMBOL_EXCLAMEQ        =  8;  // '!='
        final int SYMBOL_PERCENT         =  9;  // '%'
        final int SYMBOL_AMPAMP          = 10;  // '&&'
        final int SYMBOL_LPARAN          = 11;  // '('
        final int SYMBOL_RPARAN          = 12;  // ')'
        final int SYMBOL_TIMES           = 13;  // '*'
        final int SYMBOL_COMMA           = 14;  // ','
        final int SYMBOL_DOT             = 15;  // '.'
        final int SYMBOL_DOTDOT          = 16;  // '..'
        final int SYMBOL_DIV             = 17;  // '/'
        final int SYMBOL_COLON           = 18;  // ':'
        final int SYMBOL_SEMI            = 19;  // ';'
        final int SYMBOL_AT              = 20;  // '@'
        final int SYMBOL_LBRACKET        = 21;  // '['
        final int SYMBOL_RBRACKET        = 22;  // ']'
        final int SYMBOL_LBRACE          = 23;  // '{'
        final int SYMBOL_PIPEPIPE        = 24;  // '||'
        final int SYMBOL_RBRACE          = 25;  // '}'
        final int SYMBOL_PLUS            = 26;  // '+'
        final int SYMBOL_LT              = 27;  // '<'
        final int SYMBOL_LTEQ            = 28;  // '<='
        final int SYMBOL_EQ              = 29;  // '='
        final int SYMBOL_EQEQ            = 30;  // '=='
        final int SYMBOL_GT              = 31;  // '>'
        final int SYMBOL_GTEQ            = 32;  // '>='
        final int SYMBOL_AND             = 33;  // and
        final int SYMBOL_ATOMIC          = 34;  // atomic
        final int SYMBOL_CODESTRING      = 35;  // CodeString
        final int SYMBOL_DECLITERAL      = 36;  // DecLiteral
        final int SYMBOL_ELSE            = 37;  // else
        final int SYMBOL_FLOATLITERAL    = 38;  // FloatLiteral
        final int SYMBOL_FOR             = 39;  // for
        final int SYMBOL_FUNC            = 40;  // func
        final int SYMBOL_ID              = 41;  // Id
        final int SYMBOL_IF              = 42;  // if
        final int SYMBOL_IN              = 43;  // in
        final int SYMBOL_NOT             = 44;  // not
        final int SYMBOL_OR              = 45;  // or
        final int SYMBOL_PACKAGE         = 46;  // package
        final int SYMBOL_PAR             = 47;  // par
        final int SYMBOL_REDUCE          = 48;  // reduce
        final int SYMBOL_RETURN          = 49;  // return
        final int SYMBOL_SEQ             = 50;  // seq
        final int SYMBOL_STRINGLITERAL   = 51;  // StringLiteral
        final int SYMBOL_USE             = 52;  // use
        final int SYMBOL_WHILE           = 53;  // while
        final int SYMBOL_ARG             = 54;  // <Arg>
        final int SYMBOL_ARGLIST         = 55;  // <Arg list>
        final int SYMBOL_ATOMICCODEDECL  = 56;  // <Atomic code decl>
        final int SYMBOL_BLOCKSTATEMENT  = 57;  // <Block statement>
        final int SYMBOL_CODEDECL        = 58;  // <Code decl>
        final int SYMBOL_DECL            = 59;  // <Decl>
        final int SYMBOL_DECLLIST        = 60;  // <Decl list>
        final int SYMBOL_EXPR            = 61;  // <Expr>
        final int SYMBOL_EXPRLIST        = 62;  // <Expr list>
        final int SYMBOL_NORMALSTATEMENT = 63;  // <Normal statement>
        final int SYMBOL_OPADD           = 64;  // <Op add>
        final int SYMBOL_OPAND           = 65;  // <Op and>
        final int SYMBOL_OPCOMPARE       = 66;  // <Op compare>
        final int SYMBOL_OPEQUATE        = 67;  // <Op equate>
        final int SYMBOL_OPMULT          = 68;  // <Op mult>
        final int SYMBOL_OPOR            = 69;  // <Op or>
        final int SYMBOL_OPUNARY         = 70;  // <Op unary>
        final int SYMBOL_ORDER           = 71;  // <Order>
        final int SYMBOL_PACKAGEDECL     = 72;  // <Package decl>
        final int SYMBOL_PROGRAM         = 73;  // <Program>
        final int SYMBOL_RANGEEXPR       = 74;  // <Range expr>
        final int SYMBOL_STATEMENT       = 75;  // <Statement>
        final int SYMBOL_STATEMENTLIST   = 76;  // <Statement list>
        final int SYMBOL_STRUCTCODEDECL  = 77;  // <Struct code decl>
        final int SYMBOL_THENSTATEMENT   = 78;  // <Then statement>
        final int SYMBOL_USEDECL         = 79;  // <Use decl>
        final int SYMBOL_VALUE           = 80;  // <Value>
        final int SYMBOL_VARIABLE        = 81;  // <Variable>
    }

    protected GOLDParser parser;

    public RageParser() throws Exception {
        try {
            parser = new GOLDParser();
            parser.setTrimReductions(false);
            parser.loadCompiledGrammar(grammarFile);
        }
        catch(ParserException e) {
            System.out.println("**PARSER ERROR**\n" + e.toString());
            throw e;
        }
    }

    public boolean parse(String input, String output, String directory) throws Exception {
        try {
            parser.reset();
            parser.openFile(input);
        }
        catch(ParserException e) {
            System.out.println("**PARSER ERROR**\n" + e.toString());
            throw e;
        }

        boolean done = false;
        int response = -1;

        while(!done) {
            try
            {
                response = parser.parse();
            }
            catch(ParserException e) {
                System.out.println("**PARSER ERROR**\n" + e.toString());
                throw e;
                //System.exit(1);
            }

            switch(response)
            {
                case gpMsgTokenRead:
                    /* A token was read by the parser. The Token Object can be accessed
              through the CurrentToken() property:  Parser.CurrentToken */
                    System.out.println("Token: " + parser.currentToken().getData().toString());
                    break;

                case gpMsgReduction:
                    /* This message is returned when a rule was reduced by the parse engine.
              The CurrentReduction property is assigned a Reduction object
              containing the rule and its related tokens. You can reassign this
              property to your own customized class. If this is not the case,
              this message can be ignored and the Reduction object will be used
              to store the parse tree.  */

                    //Parser.Reduction = //Object you created to store the rule
                    //Reduction myRed = parser.currentReduction();

                    //System.out.println(myRed.getParentRule().getText());
                    /*for(int i = 0;i < myRed.getTokenCount();i++) {
                        System.out.print(myRed.getToken(i).getName() + ": " +
                                "\"" + myRed.getToken(i).getData().toString() + "\" ");
                        //System.out.println(myRed.getToken(i).getText());
                    } */
                    //System.out.println("\n");
                    break;

                case gpMsgAccept:
                    //System.out.println("OK");
                    done = true;
                    break;

                case gpMsgLexicalError:
                    /* Place code here to handle a illegal or unrecognized token
                           To recover, pop the token from the stack: Parser.PopInputToken */

                    // ************************************** log file
                    System.out.println("gpMsgLexicalError");
                    // ************************************** end log

                    parser.popInputToken();

                    break;

                case gpMsgNotLoadedError:
                    /* Load the Compiled Grammar Table file first. */

                    // ************************************** log file
                    System.out.println("gpMsgNotLoadedError");
                    // ************************************** end log

                    done = true;

                    break;

                case gpMsgSyntaxError:
                    /* This is a syntax error: the source has produced a token that was
                           not expected by the LALR State Machine. The expected tokens are stored
                           into the Tokens() list. To recover, push one of the
                              expected tokens onto the parser's input queue (the first in this case):
                           You should limit the number of times this type of recovery can take
                           place. */

                    done = true;

                    Token theTok = parser.currentToken();
                    throw new Exception("Token not expected: " + (String)theTok.getData());

                    // ************************************** log file
                    //System.out.println("gpMsgSyntaxError");
                    // ************************************** end log

                    //break;

                case gpMsgCommentError:
                    /* The end of the input was reached while reading a comment.
                             This is caused by a comment that was not terminated */

                    // ************************************** log file
                    System.out.println("gpMsgCommentError");
                    // ************************************** end log

                    done = true;

                    break;

                case gpMsgInternalError:
                    /* Something horrid happened inside the parser. You cannot recover */

                    // ************************************** log file
                    System.out.println("gpMsgInternalError");
                    // ************************************** end log

                    done = true;

                    break;
            }
        }
        try
        {
            parser.closeFile();
        }
        catch(ParserException e)
        {
            System.out.println("**PARSER ERROR**\n" + e.toString());
            throw e;
        }

        try {
            System.out.println("Parsing " + input);

            CodePackage codePackage = new RageTreeParser().parseProgram(parser.currentReduction());

            //CodePackage codePackage = new CodePackage(treeParser.getPackageName(), treeParser.getCodeObjects());

            /*if(!(new CodeChecker().check(codePackage))) {
                System.out.println("There are some error(s) in " + input);
                return false;
            } */

            /*CodeGenerator codeGenerator = new CPPCodeGenerator();
                System.out.println("Generating code for " + input);
                codeGenerator.generate(codePackage, output, directory);*/
        }
        catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        return true;
    }
}

