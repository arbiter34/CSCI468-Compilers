package micropascalcompiler;

import java.io.IOException;
import java.io.PrintWriter;

public class Parser {
    private static final boolean DEBUG = false;
    PrintWriter outFile;
    TokenContainer lookAhead;
    TokenContainer lookAhead2;
    Scanner scanner;

    public Parser(Scanner scanner, PrintWriter outFile) throws IOException {
        this.outFile = outFile;
        this.scanner = scanner;
        lookAhead = scanner.getNextToken();
        lookAhead2 = scanner.getNextToken();
        systemGoal();
        this.outFile.close();
        System.out.println("Successfully parsed the program.");
    }
    
    private void printNode(int rule, boolean newLine) {
        this.outFile.flush();
        if (newLine) {
            this.outFile.append(Integer.toString(rule) + "\n");
        } else {
            this.outFile.append(Integer.toString(rule)+ ", ") ;           
        }
    }
    
    private void printBranch() {
        //this.outFile.print(" branch ");
    }

    private void getNextToken() {
        lookAhead = lookAhead2;
        lookAhead2 = scanner.getNextToken();
    }

    public void match(TokenType tokenInput) {
        if (lookAhead.getToken() == tokenInput) {
            if (DEBUG) {
                System.out.println("Matched token: " + lookAhead.getToken() + " with " + tokenInput);
            }
            //get next lookahead
            getNextToken();
            
        } else {
            matchError(tokenInput.toString());
        }
    }

    public void matchError(String expectedToken) {
        System.out.println("Match error found on line " + lookAhead.getRow() + ", column "
                + lookAhead.getCol() + ": expected '" + expectedToken
                + "', but found '" + lookAhead.getLexeme() + "'");
    }

    public void syntaxError(String expectedToken) {
        System.out.println("Syntax error found on line " + lookAhead.getRow() + ", column "
                + lookAhead.getCol() + ": expected one of the following tokens {" + expectedToken
                + "}, but found '" + lookAhead.getLexeme() + "'");
        if (DEBUG) {
            System.out.println("Current lookahead token: " + lookAhead.toString());
        }
        System.exit(1);
    }


    public void systemGoal()
    {        
        switch (lookAhead.getToken()) {
            case MP_PROGRAM: //1 SystemGoal -> Program mp_eof
                printNode(1, false);
                printBranch();
                program();
                match(TokenType.MP_EOF);
                break;
            default:
                syntaxError("program");
        }
    }

    public void program()
    {
        
        switch (lookAhead.getToken()) {
        case MP_PROGRAM: //2 Program -> Programheading 
            printNode(2, false);
            printBranch();
            programHeading();
            match(TokenType.MP_SCOLON);
            printBranch();
            block(); //sends in the branch lbl and that it is a program block type
            
            match(TokenType.MP_PERIOD);
            
            break;
        default:
            syntaxError("program");
        }
    }

    public String programHeading()
    {
        String name = "";
        
        switch (lookAhead.getToken()) {
        case MP_PROGRAM: //3 ProgramHeading -> mp_program ProgramIdentifier
            printNode(3, false);
            match(TokenType.MP_PROGRAM);
            printBranch();
            programIdentifier();
            break;
        default:
            syntaxError("program");
        }
        return name;
    }

    public void block()
    {
        switch (lookAhead.getToken()) {
        case MP_BEGIN:
        case MP_FUNCTION:
        case MP_PROCEDURE:
        case MP_VAR: //4 Block -> VariableDeclarationPart ProcedureAndFunctionDeclarationPart StatementPart
            printNode(4, false);
            
            printBranch();
            variableDeclarationPart();
            
            printBranch();
            procedureAndFunctionDeclarationPart();
            
            printBranch();
            statementPart();
            break;
        default:
            syntaxError("var, begin, function, procedure");
        }
    }

    public void variableDeclarationPart()
    {
        switch (lookAhead.getToken()) {
        case MP_VAR: //5 VariableDeclarationPart -> mp_var VariableDeclaration mp_scolon VariableDeclarationTail
            printNode(5, false);
            match(TokenType.MP_VAR);
            
            printBranch();
            variableDeclaration();
            
            match(TokenType.MP_SCOLON);
            printBranch();
            variableDeclarationTail();
            break;
        case MP_BEGIN:
        case MP_FUNCTION: 
        case MP_PROCEDURE: //107 VariableDeclarationPart -> lambda
            printNode(107, true);
            break;
        default:
            syntaxError("var, begin, function, procedure");
        }
    }

    public void variableDeclarationTail()
    {
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER: //6 VariableDeclarationTail -> VariableDeclaration mp_scolon VariableDeclarationTail
            printNode(6, false);
            printBranch();
            variableDeclaration();
            
            match(TokenType.MP_SCOLON);
            printBranch();
            variableDeclarationTail();
            break;
        case MP_BEGIN:
        case MP_PROCEDURE:
        case MP_FUNCTION: //7 VariableDeclarationTail -> lambda
            printNode(7, true);
            break;
        default:
            syntaxError("identifier, begin, procedure, function");
        }
    }

    public void variableDeclaration()
    {
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER: //8 VariableDeclaration -> IdentifierList mp_colon 
            printNode(8, false);
            printBranch();
            identifierList();
            
            match(TokenType.MP_COLON);
            printBranch();
            type();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void type()
    {

        switch (lookAhead.getToken())
        {
        case MP_INTEGER: //9 Type -> mp_integer
            printNode(9, true);
            match(TokenType.MP_INTEGER);
            break;
        case MP_BOOLEAN: //110 Type -> mp_boolean
            printNode(110, true);
            match(TokenType.MP_BOOLEAN);
            break;
        case MP_FLOAT: //108 Type -> mp_float
            printNode(108, true);
            match(TokenType.MP_FLOAT);
            break;
        case MP_STRING: //109 Type -> mp_string
            printNode(109, true);
            match(TokenType.MP_STRING);
            break;
        default:
            syntaxError("Integer, Float, Boolean, String");
        }
    }

    public void procedureAndFunctionDeclarationPart()
    {
        switch (lookAhead.getToken()) {
        case MP_PROCEDURE: //10 ProcedureAndFunctionDeclarationPart -> ProcedureDeclaration ProcedureAndFunctionDeclarationPart
            
            printNode(110, false);
            printBranch();
            procedureDeclaration();
            
            printBranch();
            procedureAndFunctionDeclarationPart();
            break;
        case MP_FUNCTION: //11 ProcedureAndFunctionDeclarationPart -> FunctionDeclaration 
            
            printNode(11, false);
            printBranch();            
            functionDeclaration();
            
            printBranch();
            procedureAndFunctionDeclarationPart();
            break;
        case MP_BEGIN: //12 ProcedureAndFunctionDeclarationPart -> lambda
            printNode(12, true);
            break;
        default:
            syntaxError("procedure, function, begin");
        }
    }

    public void procedureDeclaration()
    {

        switch (lookAhead.getToken()) {
        case MP_PROCEDURE: //13 ProcedureDeclaration -> ProcedureHeading mp_scolon Block mp_scolon 
            printNode(13, false);
            printBranch();
            procedureHeading();
            match(TokenType.MP_SCOLON);
            
            printBranch();
            block();
            match(TokenType.MP_SCOLON);

            break;
        default:
            syntaxError("procedure");
        }
    }

    public void functionDeclaration()
    {
        switch (lookAhead.getToken()) {
        case MP_FUNCTION: //14 FunctionDeclaration -> FunctionHeading mp_scolon Block mp_scolon #Destroy
            printNode(14, false);
            printBranch();
            functionHeading();
            match(TokenType.MP_SCOLON);
            
            printBranch();
            block();
            match(TokenType.MP_SCOLON);

            break;
        default:
            syntaxError("function");
        }
    }

    public void procedureHeading()
    {

        switch (lookAhead.getToken()) {
        case MP_PROCEDURE: //15 ProcedureHeading -> mp_procedure ProcedureIdentifier #create OptionalFormalParameterList #insert
            printNode(15, false);
            match(TokenType.MP_PROCEDURE);
            
            printBranch();
            procedureIdentifier();
            
            printBranch();
            optionalFormalParameterList();

            break;
        default:
            syntaxError("procedure");
        }
    }

    public void functionHeading()
    {

        switch (lookAhead.getToken()) {
        case MP_FUNCTION: //16 FunctionHeading -> mp_function FunctionIdentifier OptionalFormalParameterList mp_colon Type
            printNode(16, false);
            match(TokenType.MP_FUNCTION);
            
            printBranch();
            functionIdentifier();
            
            printBranch();
            optionalFormalParameterList();

            match(TokenType.MP_COLON);
            printBranch();
            type();

            break;
        default:
            syntaxError("function");
        }
    }

    public void optionalFormalParameterList()
    {

        switch (lookAhead.getToken())
        {
        case MP_LPAREN: //17 OptionalFormalParameterList -> mp_lparen FormalParameterSection FormalParameterSectionTail mp_rparen
            printNode(17, false);
            printBranch();
            match(TokenType.MP_LPAREN);
            formalParameterSection();
            
            printBranch();
            formalParameterSectionTail();
            match(TokenType.MP_RPAREN);
            break;
        case MP_SCOLON:
        case MP_COLON: //18 OptionalFormalParameterList -> lambda
            printNode(18, true);
            break;
        default:
            syntaxError("(, ;, :");
        }
    }

    public void ifStatement()
    {
        switch (lookAhead.getToken())
        {
        case MP_IF: //51 IfStatement -> mp_if BooleanExpression mp_then #gen_branch_false Statement OptionalElsePart
            printNode(51, false);
            match(TokenType.MP_IF);
            printBranch();
            booleanExpression();
            
            printBranch();
            match(TokenType.MP_THEN);
            statement();
            
            printBranch();
            optionalElsePart();
            break;
        default:
            syntaxError("if");
        }
    }

    public void optionalElsePart()
    {
        
        switch (lookAhead.getToken())
        {
        case MP_ELSE: //52 OptionalElsePart -> mp_else Statement
            printNode(52, false);
            printBranch();
            match(TokenType.MP_ELSE);
            statement();
            break;
        case MP_UNTIL:
        case MP_SCOLON:
        case MP_END: //53 OptionalElsePart -> lambda
            printNode(53, true);
            break;
        default:
            syntaxError("else, until, ;, end");
        }
    }

    public void repeatStatement()
    {
        switch (lookAhead.getToken())
        {
        case MP_REPEAT: //54 RepeatStatement -> mp_repeat StatementSequence mp_until BooleanExpression
            printNode(54, false);
            printBranch();
            match(TokenType.MP_REPEAT);
            statementSequence();            
            
            printBranch();
            match(TokenType.MP_UNTIL);
            booleanExpression();
            break;
        default:
            syntaxError("repeat");
        }
    }

    public void whileStatement()
    {
        switch (lookAhead.getToken())
        {
        case MP_WHILE: //55 WhileStatement -> mp_while BooleanExpression mp_do Statement
            printNode(55, false);
            printBranch();
            match(TokenType.MP_WHILE);
            booleanExpression();
            
            printBranch();
            match(TokenType.MP_DO);
            statement();
            break;
        default:
            syntaxError("while");
        }
    }

    public void forStatement()
    {
        switch (lookAhead.getToken())
        {
        case MP_FOR: //56 ForStatement -> mp_for ControlVariable mp_assign InitialValue StepValue FinalValue mp_do Statement
            printNode(56, false);
            printBranch();
            match(TokenType.MP_FOR);
            controlVariable();            
            
            printBranch();
            match(TokenType.MP_ASSIGN);
            
            printBranch();
            initialValue();
            match(TokenType.MP_DO);
            
            printBranch();
            statement();
            break;
        default:
            syntaxError("for");
        }
    }

    public String controlVariable()
    {
        String id = "";
        
        switch (lookAhead.getToken())
        {
        case MP_IDENTIFIER: //57 ControlVariable -> VariableIdentifier
            printNode(57, true);            variableIdentifier();
            break;
        default:
            syntaxError("identifier");
        }
        return id;
    }

    public void initialValue()
    {
        switch (lookAhead.getToken())
        {
        case MP_IDENTIFIER:
        case MP_FALSE:
        case MP_TRUE:
        case MP_STRING_LIT:
        case MP_FLOAT_LIT: //added boolean values, string, float
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //58 InitialValue -> OrdinalExpression
            printNode(58, false);
            printBranch();
            ordinalExpression();
            break;
        default:
            syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    public void stepValue()
    {
        switch (lookAhead.getToken())
        {
        case MP_TO: //59 StepValue -> mp_to
            printNode(59, true);
            match(TokenType.MP_TO);
            break;
        case MP_DOWNTO: //60 StepValue -> mp_downto
            printNode(60, true);
            match(TokenType.MP_DOWNTO);
            break;
        default:
            syntaxError("to, downto");
        }
    }

    public void finalValue() {
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER:
        case MP_FALSE:
        case MP_TRUE:
        case MP_STRING_LIT:
        case MP_FLOAT_LIT: //added boolean values, string, float
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //61 FinalValue -> OrdinalExpression
            printNode(61, false);
            printBranch();
            ordinalExpression();
            break;
        default:
            syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    public void procedureStatement() {
        switch (lookAhead.getToken()) {
            case MP_IDENTIFIER: //62 ProcedureStatement -> ProcedureIdentifier OptionalActualParameterList
                printNode(62, false);
                printBranch();
                procedureIdentifier();

                printBranch();
                optionalActualParameterList();
                break;
            default:
                syntaxError("identifier");
                break;
        }
    }

    public void optionalActualParameterList() {
        switch (lookAhead.getToken()) {
        case MP_COMMA:
        case MP_RPAREN:
        case MP_AND:
        case MP_MOD:
        case MP_FLOAT_DIVIDE:
        case MP_DIV:
        case MP_TIMES:
        case MP_OR:
        case MP_MINUS:
        case MP_PLUS:
        case MP_NEQUAL:
        case MP_GEQUAL:
        case MP_LEQUAL:
        case MP_GTHAN:
        case MP_LTHAN:
        case MP_EQUAL:
        case MP_DOWNTO:
        case MP_TO:
        case MP_DO:
        case MP_UNTIL:
        case MP_ELSE:
        case MP_THEN:
        case MP_SCOLON:
        case MP_END: //64 OptionalActualParameterList -> lambda
            printNode(64, true);
            break;
        case MP_LPAREN: //63 OptionalActualParameterList -> mp_lparen ActualParameter ActualParameterTail mp_rparen
            printNode(63, false);
            printBranch();
            match(TokenType.MP_LPAREN);
            actualParameter();
            
            printBranch();
            actualParameterTail();

            match(TokenType.MP_RPAREN);
            break;
        default:
            syntaxError("',', ), and, mod, div, / , *, 'or', -, +, <>, >=, <=, <, >, =, downto, to, do, until, else, then, ;, end");
        }
    }

    public void actualParameterTail() {
        switch (lookAhead.getToken()) {
            case MP_COMMA: //65 ActualParameterTail -> mp_comma ActualParameter ActualParameterTail
                printNode(65, false);
                printBranch();
                match(TokenType.MP_COMMA);
                actualParameter();
                
                printBranch();
                actualParameterTail();
                break;
            case MP_RPAREN: //66 ActualParameterTail -> lambda
                printNode(66, true);
                break;
            default:
                syntaxError("',', )");
        }
    }

    public void actualParameter() {
        switch (lookAhead.getToken()) {
            case MP_IDENTIFIER:
            case MP_FALSE:
            case MP_TRUE:
            case MP_STRING_LIT:
            case MP_FLOAT_LIT: //added boolean values, string, float
            case MP_LPAREN:
            case MP_NOT:
            case MP_INTEGER_LIT:
            case MP_MINUS:
            case MP_PLUS: //67 ActualParameter -> OrdinalExpression
                printNode(67, false);
                printBranch();
                ordinalExpression();

                break;
            default:
                syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    /**
     * 
     */
    public void expression() {

        switch (lookAhead.getToken()) {
            case MP_IDENTIFIER:
            case MP_FALSE:
            case MP_TRUE:
            case MP_STRING_LIT:
            case MP_FLOAT_LIT: //added boolean values, string, float
            case MP_LPAREN:
            case MP_NOT:
            case MP_INTEGER_LIT:
            case MP_MINUS:
            case MP_PLUS: //68 Expression -> SimpleExpression OptionalRelationalPart
                printNode(68, false);
                printBranch();
                simpleExpression();
                        
                printBranch();
                optionalRelationalPart();
                break;
            default:
                syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    /**
     * 
     * @param left
     *            {@link SemanticRec} from {@link compiler.parser.Parser#simpleExpression()} with {@link RecordType#LITERAL} or
     *            {@link RecordType#IDENTIFIER}
     * @return {@link SemanticRec} either null or {@link RecordType#LITERAL}
     */
    public void optionalRelationalPart() {
        switch (lookAhead.getToken()) {
            case MP_COMMA:
            case MP_RPAREN:
            case MP_DOWNTO:
            case MP_TO:
            case MP_DO:
            case MP_UNTIL:
            case MP_ELSE:
            case MP_THEN:
            case MP_SCOLON:
            case MP_END: //70 OptionalRelationalPart -> lambda
                printNode(70, true);
                break;
            case MP_NEQUAL:
            case MP_GEQUAL:
            case MP_LEQUAL:
            case MP_GTHAN:
            case MP_LTHAN:
            case MP_EQUAL: //69 OptionalRelationalPart -> RelationalOperator SimpleExpression
                printNode(69, false);
                printBranch();
                relationalOperator();
                
                printBranch();
                simpleExpression();
                break;
            default:
                syntaxError("',', ), downto, to, do, until, else, then, ;, end, <>, >=, <=, >, <, =");
        }
    }

    /**
     * 
     * @return SemanticRec RecordType.REL_OP
     */
    public void relationalOperator() {
        switch (lookAhead.getToken()) {
        case MP_NEQUAL: //76 RelationalOperator -> mp_nequal
            printNode(76, true);
            match(TokenType.MP_NEQUAL);
            break;
        case MP_GEQUAL: //75 RelationalOperator -> mp_gequal
            printNode(75, true);
            match(TokenType.MP_GEQUAL);
            break;
        case MP_LEQUAL: //74 RelationalOperator -> mp_lequal
            printNode(74, true);
            match(TokenType.MP_LEQUAL);
            break;
        case MP_GTHAN: //73 RelationalOperator -> mp_gthan
            printNode(73, true);
            match(TokenType.MP_GTHAN);
            break;
        case MP_LTHAN: //72 RelationalOperator -> mp_lthan
            printNode(72, true);
            match(TokenType.MP_LTHAN);
            break;
        case MP_EQUAL: //71 RelationalOperator -> mp_equal
            printNode(71, true);
            match(TokenType.MP_EQUAL);
            break;
        default:
            syntaxError("<>, >=, <= , >, <, =");
        }
    }

    /**
     * 
     * @param formalParam
     * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
     */
    public void simpleExpression() {
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER:
        case MP_FALSE:
        case MP_TRUE:
        case MP_STRING_LIT:
        case MP_FLOAT_LIT: //added boolean values, string, float
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS: //77 SimpleExpression -> OptionalSign Term TermTail
            printNode(77, false);
            printBranch();
            optionalSign();
            
            printBranch();
            term();
            
            printBranch();
            termTail();
            
            break;
        default:
            syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    /**
     * 
     * @param left
     * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
     */
    public void termTail() {
        switch (lookAhead.getToken()) {
        case MP_COMMA:
        case MP_RPAREN:
        case MP_NEQUAL:
        case MP_GEQUAL:
        case MP_LEQUAL:
        case MP_GTHAN:
        case MP_LTHAN:
        case MP_EQUAL:
        case MP_DOWNTO:
        case MP_TO:
        case MP_DO:
        case MP_UNTIL:
        case MP_ELSE:
        case MP_THEN:
        case MP_SCOLON:
        case MP_END: //79 TermTail -> lambda
            printNode(79, true);
            break;
        case MP_OR:
        case MP_MINUS:
        case MP_PLUS: //78 TermTail -> AddingOperator Term TermTail
            printNode(78, false);
            printBranch();
            addingOperator();
            
            printBranch();
            term();
            
            printBranch();
            termTail();
            break;
        default:
            syntaxError("',', ), <>, >=, <=, >, <, =, downto, to, do, until, else, then, ;, end, or, -, +");
        }
    }

    /**
     * 
     * @return SemanticRec RecordType.OPT_SIGN
     */
    public void optionalSign() {
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER:
        case MP_FALSE:
        case MP_TRUE:
        case MP_STRING_LIT:
        case MP_FLOAT_LIT: //added boolean values, string, float
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT: //82 OptionalSign -> lambda
            printNode(82, true);
            break;
        case MP_MINUS: //81 OptionalSign -> mp_minus
            printNode(81, true);
            match(TokenType.MP_MINUS);
            break;
        case MP_PLUS: //80 OptionalSign -> mp_plus
            printNode(80, true);
            match(TokenType.MP_PLUS);
            break;
        default:
            syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    public void addingOperator() {
        switch (lookAhead.getToken()) {
        case MP_OR: //85 AddingOperator -> mp_or
            printNode(85, true);
            match(TokenType.MP_OR);
            break;
        case MP_MINUS: //84 AddingOperator -> mp_minus
            printNode(84, true);
            match(TokenType.MP_MINUS);
            break;
        case MP_PLUS: //83 AddingOperator -> mp_plus
            printNode(83, true);
            match(TokenType.MP_PLUS);
            break;
        default:
            syntaxError("or, -, +");
        }
    }

    /**
     * 
     * @param formalParam
     * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
     */
    public void term() {
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER:
        case MP_FALSE:
        case MP_TRUE:
        case MP_STRING_LIT:
        case MP_FLOAT_LIT: //added boolean values, string, float
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT: //86 Term -> Factor FactorTail
            printNode(86, false);
            printBranch();
            factor();
            
            printBranch();
            factorTail();
            break;
        default:
            syntaxError("identifier, false, true, String, Float, (, not, Integer");
        }
    }

    /**
     * 
     * @param left
     * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
     */
    public void factorTail() {
        switch (lookAhead.getToken()) {
            case MP_COMMA:
            case MP_RPAREN:
            case MP_OR:
            case MP_MINUS:
            case MP_PLUS:
            case MP_NEQUAL:
            case MP_GEQUAL:
            case MP_LEQUAL:
            case MP_GTHAN:
            case MP_LTHAN:
            case MP_EQUAL:
            case MP_DOWNTO:
            case MP_TO:
            case MP_DO:
            case MP_UNTIL:
            case MP_ELSE:
            case MP_THEN:
            case MP_SCOLON:
            case MP_END: //88 FactorTail -> lambda
                printNode(88, true);
                break;
            case MP_AND:
            case MP_MOD:
            case MP_DIV:
            case MP_FLOAT_DIVIDE: //added for / vs div division
            case MP_TIMES: //87 FactorTail -> MultiplyingOperator Factor FactorTail
                printNode(87, false);
                
                printBranch();
                multiplyingOperator();
                
                printBranch();
                factor();
                
                printBranch();
                factorTail();
                break;
            default:
                syntaxError("',', ), or, -, +, <>, >=, <=, >, <, =, downto, to, do, until, else, then, ;, end, and, mod, div, / , *");
        }
    }

    /**
     * 
     * @return SemanticRec RecordType.MUL_OP
     */
    public void multiplyingOperator() {
        switch (lookAhead.getToken()) {
        case MP_AND: //92 MultiplyingOperator -> mp_and
            printNode(92, true);
            match(TokenType.MP_AND);
            break;
        case MP_MOD: //91 MultiplyingOperator -> mp_mod
	printNode(91, true);
            match(TokenType.MP_MOD);
            break;
        case MP_FLOAT_DIVIDE: //112 MultiplyingOperator -> mp_float_divide "/"
	printNode(112, true);
            match(TokenType.MP_FLOAT_DIVIDE);
            break;
        case MP_DIV: //90 MultiplyingOperator -> mp_div "div"
	printNode(90, true);
            match(TokenType.MP_DIV);
            break;
        case MP_TIMES: //89 MultiplyingOperator -> mp_times
	printNode(89, true);
            match(TokenType.MP_TIMES);
            break;
        default:
            syntaxError("and, mod, div, / , *");
        }
    }

    /**
     * 
     * @param formalParam
     * @return SemanticRec RecordType.IDENTIFIER or RecordType.LITERAL
     */
    public void factor() {
        
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER:
            printNode(106, false);
                
            printBranch();
            variableIdentifier();
            //functionIdentifier();
            
            printBranch();
            optionalActualParameterList();

            break;
        case MP_LPAREN: //96 Factor -> mp_lparen Expression mp_rparen
            printNode(96, false);
            match(TokenType.MP_LPAREN);
            printBranch();
            expression();
            
            match(TokenType.MP_RPAREN);
            break;
        case MP_NOT: //95 Factor -> mp_not Factor
            printNode(95, false);
            printBranch();
            match(TokenType.MP_NOT);
            factor();
            break;
        case MP_INTEGER_LIT: //93 Factor -> mp_integer_lit
            printNode(93, true);
            match(TokenType.MP_INTEGER_LIT);
            break;
        case MP_FALSE: //116 Factor -> mp_false
            printNode(116, true);
            match(TokenType.MP_FALSE);
            break;
        case MP_TRUE: //115 Factor -> mp_true
            printNode(115, true);
            match(TokenType.MP_TRUE);
            break;
        case MP_STRING_LIT: //114 Factor -> mp_string_lit
            printNode(114, true);
            match(TokenType.MP_STRING_LIT);
            break;
        case MP_FLOAT_LIT: //113 Factor -> mp_float_lit
            printNode(113, true);
            match(TokenType.MP_FLOAT_LIT);
            break;
        default:
            syntaxError("identifier, (, not, Integer, false, true, String, Float");
        }
    }

    public void programIdentifier() {
        TokenType t = lookAhead.getToken();
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER: //98 ProgramIdentifier -> mp_identifier
            printNode(98, true);
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void variableIdentifier() {
        switch (lookAhead.getToken()) {
            case MP_IDENTIFIER: //99 VariableIdentifier -> mp_identifier
                printNode(99, true);
                match(TokenType.MP_IDENTIFIER);
                break;
            default:
                syntaxError("identifier");
                break;
        }
    }

    public void procedureIdentifier() {
        switch (lookAhead.getToken()) {
        case MP_IDENTIFIER: //100 ProcedureIdentifier -> mp_identifier
            printNode(100, true);
            match(TokenType.MP_IDENTIFIER);
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void formalParameterSectionTail()
    {
        switch (lookAhead.getToken())
        {
        case MP_SCOLON: //19 FormalParameterSectionTail -> mp_scolon FormalParameterSection FormalParameterSectionTail
            printNode(19, false);   
            printBranch();
            match(TokenType.MP_SCOLON);
            formalParameterSection();
            
            printBranch();
            formalParameterSectionTail();
            break;
        case MP_RPAREN: //20 FormalParameterSectionTail -> &epsilon
            printNode(20, true);
            break;
        default:
            syntaxError("function, )");
        }
    }

    public void formalParameterSection()
    {
        switch (lookAhead.getToken())
        {
            case MP_IDENTIFIER: //21 FormalParameterSection -> ValueParameterSection #insert
                printNode(21, false);
                printBranch();
                valueParameterSection();
                break;
            case MP_VAR: //22 FormalParameterSection -> VariableParameterSection #insert
                printNode(22, false);
                printBranch();
                variableParameterSection();
                break;
            default:
                syntaxError("identifier, var");
        }
    }

    public void valueParameterSection()
    {
        switch (lookAhead.getToken())
        {
        case MP_IDENTIFIER: //23 ValueParameterSection -> IdentifierList mp_colon Type
            printNode(23, false);
            printBranch();
            identifierList();
            match(TokenType.MP_COLON);
            
            printBranch();
            type();
            break;
        default:
            syntaxError("identifier");
        }
    }

    public void variableParameterSection()
    {
 
        switch (lookAhead.getToken())
        {
            case MP_VAR: //24 VariableParameterSection -> mp_var IdentifierList mp_colon Type
                printNode(24, false);
                printBranch();  
                match(TokenType.MP_VAR);
                identifierList();
                
                printBranch();
                match(TokenType.MP_COLON);
                type();

                break;
            default:
                syntaxError("var");
        }
    }

    public void statementPart()
    {
        switch (lookAhead.getToken())
        {
        case MP_BEGIN: //25 StatementPart -> CompoundStatement
            printNode(25, false);
            printBranch();
            compoundStatement();
            break;
        default:
            syntaxError("begin");
        }
    }

    public void compoundStatement()
    {
        switch (lookAhead.getToken())
        {
        case MP_BEGIN: //26 CompoundStatement -> mp_begin StatementSequence mp_end
            printNode(26, false);
            printBranch();
            match(TokenType.MP_BEGIN);
            statementSequence();
            
            match(TokenType.MP_END);
            break;
        default:
            syntaxError("begin");
        }
    }

    public void statementSequence()
    {
        switch (lookAhead.getToken())
        {
        case MP_IDENTIFIER: //27 StatementSequence -> Statement StatementTail
        case MP_FOR:
        case MP_WHILE:
        case MP_UNTIL:
        case MP_REPEAT:
        case MP_IF:
        case MP_WRITELN: //added writeln
        case MP_WRITE:
        case MP_READ:
        case MP_SCOLON:
        case MP_END:
        case MP_BEGIN:  
            printNode(27, false);
            printBranch();
            statement();
            
            printBranch();
            statementTail();
            break;
        default:
            syntaxError("identifier, for, while, until, repeat, if, write, writeln, read, ;, end, begin");
        }
    }

    public void statementTail()
    {
        switch (lookAhead.getToken())
        {
        case MP_SCOLON: //28 StatementTail -> mp_scolon Statement StatementTail
            printNode(28, false);
            match(TokenType.MP_SCOLON);
            printBranch();
            statement();
            
            printBranch();
            statementTail();
            break;
        case MP_UNTIL: //29 StatementTail -> &epsilon
        case MP_END:
            printNode(29, true);
            break;
        default:
            syntaxError(";, until, end");
        }
    }

    public void statement()
    {
        switch (lookAhead.getToken())
        {
        case MP_UNTIL: //30 Statement -> EmptyStatement
        case MP_ELSE:
        case MP_SCOLON:
        case MP_END:
            printNode(30, false);
            printBranch();
            emptyStatement();
            break;
        case MP_BEGIN: //31 Statement -> CompoundStatement
            printNode(31, false);
            printBranch();
            compoundStatement();
            break;
        case MP_READ: //32 Statement -> ReadStatement
            printNode(32, false);
            printBranch();
            readStatement();
            break;
        case MP_WRITELN:
        case MP_WRITE: //33 Statement -> WriteStatement
            printNode(33, false);
            printBranch();
            writeStatement();
            break;
        case MP_IDENTIFIER:
            if (lookAhead2.getToken() == TokenType.MP_ASSIGN) {
                printNode(34, false);
                printBranch();
                assignmentStatement(); //34 Statement  -> AssigmentStatement
            } else {
                printNode(39, false);
                printBranch();
                procedureStatement();//39 Statement  -> ProcedureStatement
            }
            break;
        case MP_IF:
            printNode(35, false);
            printBranch();
            ifStatement(); //35 Statement  -> IfStatement
            break;
        case MP_WHILE:
            printNode(36, false);
            printBranch();
            whileStatement(); //36 Statement  -> WhileStatement
            break;
        case MP_REPEAT:
            printNode(37, false);
            printBranch();
            repeatStatement(); //37 Statement  -> RepeatStatement
            break;
        case MP_FOR:
            printNode(38, false);
            printBranch();
            forStatement(); //38 Statement  -> ForStatement
            break;
        default:
            syntaxError("until, else, ;, end, begin, Read, Write, Writeln, identifier, if, while, repeat, for");
        }
    }

    public void emptyStatement()
    {
        switch (lookAhead.getToken())
        {
        case MP_UNTIL: //40 EmptyStatement -> &epsilon
        case MP_ELSE:
        case MP_SCOLON:
        case MP_END:
            printNode(40, true);
            break;
        default:
            syntaxError("until, else, ;, end");
        }
    }

    public void readStatement()
    {
        switch (lookAhead.getToken())
        {
        case MP_READ: //41 ReadStatement ->  mp_read mp_lparen ReadParameter ReadParameterTail mp_rparen
            printNode(41, false);
            match(TokenType.MP_READ);
            match(TokenType.MP_LPAREN);
            printBranch();
            readParameter();
            
            printBranch();
            readParameterTail();
            match(TokenType.MP_RPAREN);
            break;
        default:
            syntaxError("Read");
        }
    }

    public void readParameterTail()
    {
        switch (lookAhead.getToken())
        {
        case MP_COMMA: //42 ReadParameterTail -> mp_comma ReadParameter ReadParameterTail
            printNode(42, false);
            match(TokenType.MP_COMMA);
            printBranch();
            readParameter();
            
            printBranch();
            readParameterTail();
            break;
        case MP_RPAREN: //43 ReadParameterTail -> &epsilon
            printNode(43, true);
            break;
        default:
            syntaxError("Read, )");
        }
    }

    public void readParameter()
    {
        switch (lookAhead.getToken())
        {
            case MP_IDENTIFIER: //44 ReadParameter -> VariableIdentifier
                printNode(44, false);
                printBranch();
                variableIdentifier();

                break;
            default:
                syntaxError("identifier");
        }
    }

    public void writeStatement()
    {
        switch (lookAhead.getToken())
        {
            case MP_WRITE: //45 WriteStatement -> mp_write mp_lparen WriteParameter WriteParameterTail mp_rparen
                printNode(45, false);
                match(TokenType.MP_WRITE);
                match(TokenType.MP_LPAREN);
                printBranch();
                writeParameter();
                
                printBranch();
                writeParameterTail();

                match(TokenType.MP_RPAREN);
                break;
            case MP_WRITELN: //111 WriteStatement -> mp_writeln mp_lparen WriteParameter WriteParameterTail mp_rparen.
                printNode(111, false);
                match(TokenType.MP_WRITELN);
                match(TokenType.MP_LPAREN);
                printBranch();
                writeParameter();
                    
                printBranch();
                writeParameterTail();
                match(TokenType.MP_RPAREN);
                break;
            default:
                syntaxError("Write, WriteLn");
        }
    }

    /**
     * 
     * @param writeStmt
     */
    public void writeParameterTail()
    {
        switch (lookAhead.getToken())
        {
            case MP_COMMA: //46 WriteParameterTail -> mp_comma WriteParameter WriteParameterTail
                printNode(46, false);
                match(TokenType.MP_COMMA);
                printBranch();
                writeParameter();
                    
                printBranch();
                writeParameterTail();
                break;
            case MP_RPAREN: //47 WriteParameterTail -> &epsilon
	printNode(47, false);
                break;
            default:
                syntaxError("',', )");
        }
    }

    /**
     * 
     * 
     */
    public void writeParameter()
    {
        switch (lookAhead.getToken())
        {
            case MP_IDENTIFIER: //48 WriteParameter -> OrdinalExpression
            case MP_FALSE:
            case MP_TRUE:
            case MP_STRING_LIT:
            case MP_FLOAT_LIT: //added boolean values, string, float
            case MP_LPAREN:
            case MP_NOT:
            case MP_INTEGER_LIT:
            case MP_MINUS:
            case MP_PLUS:
                printNode(48, false);
                printBranch();
                ordinalExpression();
                break;
            default:
                syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    public void assignmentStatement()
    {
        switch (lookAhead.getToken())
        {
            case MP_IDENTIFIER: 
                
                variableIdentifier();
                match(TokenType.MP_ASSIGN);
                expression();
                //functionIdentifier();
                //match(TokenType.MP_ASSIGN);
                //expression(null)        
                break;

            default:
                syntaxError("identifier");
        }
    }

    //103
    public void functionIdentifier()
    {
        switch (lookAhead.getToken())
        {
            case MP_IDENTIFIER: //101 FunctionIdentifier -> mp_identifier
	printNode(101, false);
                match(TokenType.MP_IDENTIFIER);
                break;
            default:
                syntaxError("identifier");
        }
    }

    public void booleanExpression()
    {
        switch (lookAhead.getToken())
        {
        case MP_IDENTIFIER: //102 BooleanExpression ->  Expression
        case MP_FALSE:
        case MP_TRUE:
        case MP_STRING_LIT:
        case MP_FLOAT_LIT: //added boolean values, string, float
        case MP_LPAREN:
        case MP_NOT:
        case MP_INTEGER_LIT:
        case MP_MINUS:
        case MP_PLUS:
            printNode(102, false);
            printBranch();
            expression();
            break;
        default:
            syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    public void ordinalExpression()
    {
        switch (lookAhead.getToken())
        {
            case MP_IDENTIFIER: //103 OrdinalExpression ->  Expression
            case MP_FALSE:
            case MP_TRUE:
            case MP_STRING_LIT:
            case MP_FLOAT_LIT: //added boolean values, string, float
            case MP_LPAREN:
            case MP_NOT:
            case MP_INTEGER_LIT:
            case MP_MINUS:
            case MP_PLUS:
                printNode(103, false);
                printBranch();
                expression();
                break;
            default:
                syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
        }
    }

    public void identifierList()
    {
        switch (lookAhead.getToken())
        {
            case MP_IDENTIFIER: //104 IdentifierList -> mp_identifier IdentifierTail
	printNode(104, false);
                match(TokenType.MP_IDENTIFIER);
                identifierTail();
                break;
            default:
                syntaxError("identifier");
        }
    }

    public void identifierTail()
    {
        switch (lookAhead.getToken())
        {
            case MP_COMMA: //105 IdentifierTail -> mp_comma mp_identifier IdentifierTail
                printNode(105, false);
                printBranch();
                match(TokenType.MP_COMMA);
                match(TokenType.MP_IDENTIFIER);
                identifierTail();
                break;
            case MP_COLON: //106 IdentifierTail -> &epsilon
                printNode(106, true);
                break;
            default:
                syntaxError("',', :");
        }
    }
}