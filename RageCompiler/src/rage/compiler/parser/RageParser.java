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
        final int SYMBOL_EOF               =  0;  // (EOF)
        final int SYMBOL_ERROR             =  1;  // (Error)
        final int SYMBOL_WHITESPACE        =  2;  // (Whitespace)
        final int SYMBOL_COMMENTEND        =  3;  // (Comment End)
        final int SYMBOL_COMMENTLINE       =  4;  // (Comment Line)
        final int SYMBOL_COMMENTSTART      =  5;  // (Comment Start)
        final int SYMBOL_MINUS             =  6;  // '-'
        final int SYMBOL_EXCLAM            =  7;  // '!'
        final int SYMBOL_EXCLAMEQ          =  8;  // '!='
        final int SYMBOL_AMPAMP            =  9;  // '&&'
        final int SYMBOL_LPARAN            = 10;  // '('
        final int SYMBOL_RPARAN            = 11;  // ')'
        final int SYMBOL_TIMES             = 12;  // '*'
        final int SYMBOL_COMMA             = 13;  // ','
        final int SYMBOL_DOTDOT            = 14;  // '..'
        final int SYMBOL_DIV               = 15;  // '/'
        final int SYMBOL_COLON             = 16;  // ':'
        final int SYMBOL_SEMI              = 17;  // ';'
        final int SYMBOL_LBRACKET          = 18;  // '['
        final int SYMBOL_RBRACKET          = 19;  // ']'
        final int SYMBOL_LBRACE            = 20;  // '{'
        final int SYMBOL_PIPEPIPE          = 21;  // '||'
        final int SYMBOL_RBRACE            = 22;  // '}'
        final int SYMBOL_PLUS              = 23;  // '+'
        final int SYMBOL_LT                = 24;  // '<'
        final int SYMBOL_LTEQ              = 25;  // '<='
        final int SYMBOL_EQ                = 26;  // '='
        final int SYMBOL_EQEQ              = 27;  // '=='
        final int SYMBOL_GT                = 28;  // '>'
        final int SYMBOL_GTEQ              = 29;  // '>='
        final int SYMBOL_ATOMIC            = 30;  // atomic
        final int SYMBOL_CODESTRING        = 31;  // CodeString
        final int SYMBOL_DECLITERAL        = 32;  // DecLiteral
        final int SYMBOL_ELSE              = 33;  // else
        final int SYMBOL_FI                = 34;  // fi
        final int SYMBOL_FOR               = 35;  // for
        final int SYMBOL_ID                = 36;  // Id
        final int SYMBOL_IF                = 37;  // if
        final int SYMBOL_PACKAGE           = 38;  // package
        final int SYMBOL_PAR               = 39;  // par
        final int SYMBOL_SEQ               = 40;  // seq
        final int SYMBOL_WHILE             = 41;  // while
        final int SYMBOL_ATOMICCODEDECL    = 42;  // <Atomic code decl>
        final int SYMBOL_BLOCKSTATEMENT    = 43;  // <Block statement>
        final int SYMBOL_CALLSTATEMENT     = 44;  // <Call statement>
        final int SYMBOL_CODEARGLIST       = 45;  // <Code arg list>
        final int SYMBOL_CODEDECL          = 46;  // <Code decl>
        final int SYMBOL_CODEFORMALARGLIST = 47;  // <Code formal arg list>
        final int SYMBOL_CONDSTATEMENT     = 48;  // <Cond statement>
        final int SYMBOL_DATA              = 49;  // <Data>
        final int SYMBOL_DATADECL          = 50;  // <Data decl>
        final int SYMBOL_DATASTATEMENT     = 51;  // <Data statement>
        final int SYMBOL_DATATYPE          = 52;  // <Data type>
        final int SYMBOL_DECL              = 53;  // <Decl>
        final int SYMBOL_DECLLIST          = 54;  // <Decl list>
        final int SYMBOL_EXPR              = 55;  // <Expr>
        final int SYMBOL_FORSTATEMENT      = 56;  // <For statement>
        final int SYMBOL_OPADD             = 57;  // <Op add>
        final int SYMBOL_OPAND             = 58;  // <Op and>
        final int SYMBOL_OPCOMPARE         = 59;  // <Op compare>
        final int SYMBOL_OPEQUATE          = 60;  // <Op equate>
        final int SYMBOL_OPMULT            = 61;  // <Op mult>
        final int SYMBOL_OPOR              = 62;  // <Op or>
        final int SYMBOL_OPUNARY           = 63;  // <Op unary>
        final int SYMBOL_ORDER             = 64;  // <Order>
        final int SYMBOL_PACKAGEDECL       = 65;  // <Package decl>
        final int SYMBOL_PROGRAM           = 66;  // <Program>
        final int SYMBOL_RANGEEXPR         = 67;  // <Range expr>
        final int SYMBOL_STATEMENT         = 68;  // <Statement>
        final int SYMBOL_STATEMENTLIST     = 69;  // <Statement list>
        final int SYMBOL_STRUCTCODEDECL    = 70;  // <Struct code decl>
        final int SYMBOL_VALUE             = 71;  // <Value>
        final int SYMBOL_WHILESTATEMENT    = 72;  // <While statement>
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
                    //System.out.println("Token: " + parser.currentToken().getData().toString());
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
                    System.out.println("Token not expected: " + (String)theTok.getData());

                    // ************************************** log file
                    System.out.println("gpMsgSyntaxError");
                    // ************************************** end log

                    break;

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
            RageTreeParser treeParser = new RageTreeParser();
                System.out.println("Parsing " + input);
                treeParser.parseProgram(parser.currentReduction());

            CodePackage codePackage = new CodePackage(treeParser.getPackageName(), treeParser.getCodeObjects());

            CodeChecker codeChecker = new CodeChecker();
            if(!codeChecker.check(codePackage)) {
                System.out.println("There are some error(s) in " + input);
                return false;
            }

            CodeGenerator codeGenerator = new CPPCodeGenerator();
                System.out.println("Generating code for " + input);
                codeGenerator.generate(codePackage, output, directory);
        }
        catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        return true;
    }
}

