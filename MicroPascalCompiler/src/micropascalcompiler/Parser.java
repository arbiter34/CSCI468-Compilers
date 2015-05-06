package micropascalcompiler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import micropascalcompiler.symboltable.*;
import micropascalcompiler.labelmaker.*;
import micropascalcompiler.semanticanalyzer.SemanticAnalyzer;

public class Parser {
	private static final boolean DEBUG = false;
	private final PrintWriter outFile;
	private TokenContainer lookAhead;
	private TokenContainer lookAhead2;
	private final Scanner scanner;
	private final SymbolTableStack symbolTableStack = new SymbolTableStack();
	private final SemanticAnalyzer analyzer;

	public Parser(Scanner scanner, PrintWriter outFile, String fileName)
			throws IOException {
		this.outFile = outFile;
		this.scanner = scanner;
		this.analyzer = new SemanticAnalyzer(symbolTableStack,
				fixFileName(fileName) + ".asm");

		getNextToken();
		getNextToken();
		systemGoal();
		printSymbolTables();
		this.outFile.close();
		System.out.println("Successfully parsed the program.");
	}

	private String fixFileName(String fileName) {
		return fileName.replace("\\.\\w+$", "");
	}

	private void printNode(int rule, boolean newLine) {
		this.outFile.flush();
		if (newLine) {
			this.outFile.append(Integer.toString(rule) + "\n");
		} else {
			this.outFile.append(Integer.toString(rule) + ", ");
		}
	}

	private void printBranch() {
		// this.outFile.print(" branch ");
	}

	private void getNextToken() {
		lookAhead = lookAhead2;
		do {
			lookAhead2 = scanner.getNextToken();
		} while (lookAhead2.getToken() == TokenType.MP_COMMENT);

	}

	public void match(TokenType tokenInput) {
		if (lookAhead.getToken() == tokenInput) {
			if (DEBUG) {
				System.out.println("Matched token: " + lookAhead.getToken()
						+ " with " + tokenInput);
			}
			// get next lookahead
			getNextToken();

		} else {
			matchError(tokenInput.toString());
		}
	}

	public void matchError(String expectedToken) {
		System.out.println("Match error found on line " + lookAhead.getRow()
				+ ", column " + lookAhead.getCol() + ": expected '"
				+ expectedToken + "', but found '" + lookAhead.getLexeme()
				+ "'");
	}

	public void syntaxError(String expectedToken) {
		System.out.println("Syntax error found on line " + lookAhead.getRow()
				+ ", column " + lookAhead.getCol()
				+ ": expected one of the following tokens {" + expectedToken
				+ "}, but found '" + lookAhead.getLexeme() + "'");
		if (DEBUG) {
			System.out.println("Current lookahead token: "
					+ lookAhead.toString());
		}
		System.exit(1);
	}

	public void semanticError(String errorMessage) {
		System.out.println("Semantic Error: " + errorMessage);
		System.out.println("Line: " + (lookAhead.getRow() - 1));
		System.exit(1);
	}

	private boolean addSymbolTable(String scopeName, String branchLabel) {
		if (!symbolTableStack.scopeExists(scopeName)) {
			symbolTableStack.generateSymbolTable(scopeName, branchLabel);
			return true;
		} else {
			semanticError("Symbol table with name " + scopeName
					+ " already exists");
			return false;
		}
	}

	private void removeSymbolTable() {
		symbolTableStack.printCurrentTable();
		symbolTableStack.removeSymbolTable();
	}

	public void printSymbolTables() {
		symbolTableStack.print();
	}

	public void debug() {
		if (DEBUG) {
			System.out.println("Expanding non-terminal: "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "() and current lookahead: " + lookAhead.getToken());
		}
	}

	public void lambda() {
		if (DEBUG) {
			System.out.println("\tExpanding lambda rule in "
					+ Thread.currentThread().getStackTrace()[2].getMethodName()
					+ "()");
		}
	}

	public void systemGoal() {
		debug();
		switch (lookAhead.getToken()) {
		case MP_PROGRAM: // 1 SystemGoal -> Program mp_eof
			printNode(1, false);
			printBranch();
			program();
			match(TokenType.MP_EOF);
			break;
		default:
			syntaxError("program");
		}
	}

	public void program() {

		switch (lookAhead.getToken()) {
		case MP_PROGRAM: // 2 Program -> Programheading
							// #create_symbol_table(prog_id_rec)
			printNode(2, false);
			printBranch();
			String scopeName = programHeading();
			String branchLabel = LabelMaker.getCurrentLabel();
			addSymbolTable(scopeName, branchLabel);
			match(TokenType.MP_SCOLON);
			printBranch();
			analyzer.gen_branch_unconditional(branchLabel);
			block(branchLabel);
			analyzer.gen_halt();
			match(TokenType.MP_PERIOD);

			break;
		default:
			syntaxError("program");
		}
	}

	public String programHeading() {
		String name = "";

		switch (lookAhead.getToken()) {
		case MP_PROGRAM: // 3 ProgramHeading -> mp_program ProgramIdentifier
			printNode(3, false);
			match(TokenType.MP_PROGRAM);
			printBranch();
			name = programIdentifier();
			break;
		default:
			syntaxError("program");
		}
		return name;
	}

	public void block(String scopeLabel) {
		switch (lookAhead.getToken()) {
		case MP_BEGIN:
		case MP_FUNCTION:
		case MP_PROCEDURE:
		case MP_VAR: // 4 Block -> VariableDeclarationPart
						// ProcedureAndFunctionDeclarationPart StatementPart
			printNode(4, false);

			printBranch();
			variableDeclarationPart();

			printBranch();
			procedureAndFunctionDeclarationPart();
			analyzer.gen_label(scopeLabel);
			analyzer.gen_activation_rec();
			printBranch();
			statementPart();
			analyzer.gen_deactivation_rec();
			break;
		default:
			syntaxError("var, begin, function, procedure");
		}
	}

	public void variableDeclarationPart() {
		switch (lookAhead.getToken()) {
		case MP_VAR: // 5 VariableDeclarationPart -> mp_var VariableDeclaration
						// mp_scolon VariableDeclarationTail
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
		case MP_PROCEDURE: // 107 VariableDeclarationPart -> lambda
			printNode(107, true);
			lambda();
			break;
		default:
			syntaxError("var, begin, function, procedure");
		}
	}

	public void variableDeclarationTail() {
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 6 VariableDeclarationTail -> VariableDeclaration
							// mp_scolon VariableDeclarationTail
			printNode(6, false);
			printBranch();
			variableDeclaration();

			match(TokenType.MP_SCOLON);
			printBranch();
			variableDeclarationTail();
			break;
		case MP_BEGIN:
		case MP_PROCEDURE:
		case MP_FUNCTION: // 7 VariableDeclarationTail -> lambda
			printNode(7, true);
			lambda();
			break;
		default:
			syntaxError("identifier, begin, procedure, function");
		}
	}

	public void variableDeclaration() {
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 8 VariableDeclaration -> IdentifierList mp_colon
			printNode(8, false);
			printBranch();
			List<String> ids = identifierList();

			match(TokenType.MP_COLON);
			printBranch();
			RecordType t = type();
			for (String id : ids) {
				symbolTableStack.insertSymbolInScope(new SymbolTableRecord(id,
						t, RecordKind.VARIABLE, null, null));
			}
			break;
		default:
			syntaxError("identifier");
		}
	}

	public RecordType type() {
		RecordType t = null;
		switch (lookAhead.getToken()) {
		case MP_INTEGER: // 9 Type -> mp_integer
			printNode(9, true);
			t = RecordType.INTEGER;
			match(TokenType.MP_INTEGER);
			break;
		case MP_BOOLEAN: // 110 Type -> mp_boolean
			printNode(110, true);
			t = RecordType.BOOLEAN;
			match(TokenType.MP_BOOLEAN);
			break;
		case MP_FLOAT: // 108 Type -> mp_float
			printNode(108, true);
			t = RecordType.FLOAT;
			match(TokenType.MP_FLOAT);
			break;
		case MP_STRING: // 109 Type -> mp_string
			printNode(109, true);
			t = RecordType.STRING;
			match(TokenType.MP_STRING);
			break;
		default:
			syntaxError("Integer, Float, Boolean, String");
		}
		return t;
	}

	public void procedureAndFunctionDeclarationPart() {
		switch (lookAhead.getToken()) {
		case MP_PROCEDURE: // 10 ProcedureAndFunctionDeclarationPart ->
							// ProcedureDeclaration
							// ProcedureAndFunctionDeclarationPart

			printNode(110, false);
			printBranch();
			procedureDeclaration();

			printBranch();
			procedureAndFunctionDeclarationPart();
			break;
		case MP_FUNCTION: // 11 ProcedureAndFunctionDeclarationPart ->
							// FunctionDeclaration

			printNode(11, false);
			printBranch();
			functionDeclaration();

			printBranch();
			procedureAndFunctionDeclarationPart();
			break;
		case MP_BEGIN: // 12 ProcedureAndFunctionDeclarationPart -> lambda
			printNode(12, true);
			break;
		default:
			syntaxError("procedure, function, begin");
		}
	}

	public void procedureDeclaration() {

		switch (lookAhead.getToken()) {
		case MP_PROCEDURE: // 13 ProcedureDeclaration -> ProcedureHeading
							// mp_scolon Block mp_scolon
			printNode(13, false);
			printBranch();
			procedureHeading();
			match(TokenType.MP_SCOLON);

			printBranch();

			String branchLabel = LabelMaker.getCurrentLabel();
			block(branchLabel);

			match(TokenType.MP_SCOLON);
			removeSymbolTable();
			break;
		default:
			syntaxError("procedure");
		}
	}

	public void functionDeclaration() {
		switch (lookAhead.getToken()) {
		case MP_FUNCTION: // 14 FunctionDeclaration -> FunctionHeading mp_scolon
							// Block mp_scolon #Destroy
			printNode(14, false);
			printBranch();
			functionHeading();
			match(TokenType.MP_SCOLON);

			printBranch();
			String branchLabel = LabelMaker.getCurrentLabel();
			block(branchLabel);
			match(TokenType.MP_SCOLON);
			removeSymbolTable();
			break;
		default:
			syntaxError("function");
		}
	}

	public String procedureHeading() {
		String procId = null;
		ArrayList<RecordParameter> params = null;

		switch (lookAhead.getToken()) {
		case MP_PROCEDURE: // 15 ProcedureHeading -> mp_procedure
							// ProcedureIdentifier #create
							// OptionalFormalParameterList #insert
			printNode(15, false);
			match(TokenType.MP_PROCEDURE);

			printBranch();
			procId = procedureIdentifier();
			printBranch();
			params = optionalFormalParameterList();

			symbolTableStack.insertSymbolInScope(new SymbolTableRecord(procId,
					null, RecordKind.PROCEDURE, null, params));
			symbolTableStack.getCurrentTable().getRecord(procId).setLabel(
					LabelMaker.getNextLabel());
			addSymbolTable(procId, LabelMaker.getCurrentLabel());
			symbolTableStack.insertSymbolInScope(new SymbolTableRecord(procId
					+ "reg", null, RecordKind.REG_OR_RA, null, null));
			if (params != null) {
				for (RecordParameter p : params) {
					symbolTableStack.insertSymbolInScope(new SymbolTableRecord(
							p.getLexeme(), p.getType(), RecordKind.VARIABLE, p
									.getMode(), null));
				}
			}
			symbolTableStack.insertSymbolInScope(new SymbolTableRecord(procId
					+ "ra", null, RecordKind.REG_OR_RA, null, null));

			break;
		default:
			syntaxError("procedure");
		}
		return procId;
	}

	public String functionHeading() {
		String funcId = null;
		ArrayList<RecordParameter> params = null;

		switch (lookAhead.getToken()) {
		case MP_FUNCTION: // 16 FunctionHeading -> mp_function
							// FunctionIdentifier OptionalFormalParameterList
							// mp_colon Type
			printNode(16, false);
			match(TokenType.MP_FUNCTION);

			printBranch();
			funcId = functionIdentifier();

			printBranch();
			params = optionalFormalParameterList();

			match(TokenType.MP_COLON);
			printBranch();
			RecordType t = type();
			symbolTableStack.insertSymbolInScope(new SymbolTableRecord(funcId,
					t, RecordKind.FUNCTION, null, params));
			symbolTableStack.getCurrentTable().getRecord(funcId).setLabel(
					LabelMaker.getNextLabel());
			addSymbolTable(funcId, LabelMaker.getCurrentLabel());
			symbolTableStack.insertSymbolInScope(new SymbolTableRecord(funcId
					+ "reg", null, RecordKind.REG_OR_RA, null, null));
			if (params != null) {
				for (RecordParameter p : params) {
					symbolTableStack.insertSymbolInScope(new SymbolTableRecord(
							p.getLexeme(), p.getType(), RecordKind.VARIABLE, p
									.getMode(), null));
				}
			}
			symbolTableStack.insertSymbolInScope(new SymbolTableRecord(funcId
					+ "ra", null, RecordKind.REG_OR_RA, null, null));
			break;
		default:
			syntaxError("function");
		}
		return funcId;
	}

	public ArrayList<RecordParameter> optionalFormalParameterList() {
		ArrayList<RecordParameter> params = null;
		switch (lookAhead.getToken()) {
		case MP_LPAREN: // 17 OptionalFormalParameterList -> mp_lparen
						// FormalParameterSection FormalParameterSectionTail
						// mp_rparen
			printNode(17, false);
			printBranch();
			match(TokenType.MP_LPAREN);
			params = formalParameterSection();

			printBranch();
			formalParameterSectionTail(params);
			match(TokenType.MP_RPAREN);
			break;
		case MP_SCOLON:
		case MP_COLON: // 18 OptionalFormalParameterList -> lambda
			printNode(18, true);
			lambda();
			break;
		default:
			syntaxError("(, ;, :");
		}
		return params;
	}

	public void ifStatement() {
		switch (lookAhead.getToken()) {
		case MP_IF: // 51 IfStatement -> mp_if BooleanExpression mp_then
					// #gen_branch_false Statement OptionalElsePart
			printNode(51, false);
			match(TokenType.MP_IF);
			printBranch();
			booleanExpression();
			String elseStartLabel = LabelMaker.getNextLabel();
			analyzer.gen_branch_false(elseStartLabel);
			printBranch();
			match(TokenType.MP_THEN);
			statement();
			String elseEndLabel = LabelMaker.getNextLabel();
			analyzer.gen_branch_unconditional(elseEndLabel);
			printBranch();
			analyzer.gen_label(elseStartLabel);
			optionalElsePart();
			analyzer.gen_label(elseEndLabel);
			break;
		default:
			syntaxError("if");
		}
	}

	public void optionalElsePart() {

		switch (lookAhead.getToken()) {
		case MP_ELSE: // 52 OptionalElsePart -> mp_else Statement
			printNode(52, false);
			printBranch();
			match(TokenType.MP_ELSE);
			statement();
			break;
		case MP_UNTIL:
		case MP_SCOLON:
		case MP_END: // 53 OptionalElsePart -> lambda
			printNode(53, true);
			break;
		default:
			syntaxError("else, until, ;, end");
		}
	}

	public void repeatStatement() {
		switch (lookAhead.getToken()) {
		case MP_REPEAT: // 54 RepeatStatement -> mp_repeat StatementSequence
						// mp_until BooleanExpression
			printNode(54, false);
			printBranch();

			match(TokenType.MP_REPEAT);
			String repeatStartLabel = LabelMaker.getNextLabel();

			analyzer.gen_label(repeatStartLabel);

			statementSequence();

			printBranch();
			match(TokenType.MP_UNTIL);
			booleanExpression();
			analyzer.gen_branch_false(repeatStartLabel);
			break;
		default:
			syntaxError("repeat");
		}
	}

	public void whileStatement() {
		switch (lookAhead.getToken()) {
		case MP_WHILE: // 55 WhileStatement -> mp_while BooleanExpression mp_do
						// Statement
			printNode(55, false);
			printBranch();
			match(TokenType.MP_WHILE);

			String whileStartLabel = LabelMaker.getNextLabel();

			analyzer.gen_label(whileStartLabel);
			booleanExpression();

			String endWhileLabel = LabelMaker.getNextLabel();
			analyzer.gen_branch_false(endWhileLabel);

			printBranch();
			match(TokenType.MP_DO);
			statement();
			analyzer.gen_branch_unconditional(whileStartLabel);
			analyzer.gen_label(endWhileLabel);
			break;
		default:
			syntaxError("while");
		}
	}

	public void forStatement() {
		switch (lookAhead.getToken()) {
		case MP_FOR: // 56 ForStatement -> mp_for ControlVariable mp_assign
						// InitialValue StepValue FinalValue mp_do Statement
			printNode(56, false);
			printBranch();
			match(TokenType.MP_FOR);
			String lexeme = controlVariable();

			SymbolTableRecord record = symbolTableStack
					.getSymbolInScope(lexeme);
			int nestingLevel = symbolTableStack.getPreviousRecordNestingLevel();
			long offset = record.getOffset();

			printBranch();
			match(TokenType.MP_ASSIGN);

			analyzer.gen_comment("For control statement start");
			printBranch();

			RecordType r = initialValue();

			if (record.getType() != r) {
				semanticError("Invalid implicit cast from " + r + " to "
						+ record.getType()
						+ ". Invalid initialValue in for loop.");
			}

			String forStartLabel = LabelMaker.getNextLabel();

			String forEndLabel = LabelMaker.getNextLabel();

			analyzer.gen_comment("Initialize for control variable");
			analyzer.gen_id_pop(offset, nestingLevel); // pop initial value into
														// control var

			TokenType forDirection = stepValue();

			r = finalValue();

			analyzer.gen_label(forStartLabel);
			analyzer.gen_id_push(offset, nestingLevel);

			int topNestingLevel = symbolTableStack.getCurrentTable()
					.getNestingLevel();
			int tableSize = symbolTableStack.getCurrentTable().getSize();
			analyzer.gen_id_push(tableSize, topNestingLevel);

			if (forDirection == TokenType.MP_TO) {
				analyzer.gen_bool_expr(TokenType.MP_LEQUAL, r);
			} else if (forDirection == TokenType.MP_DOWNTO) {
				analyzer.gen_bool_expr(TokenType.MP_GEQUAL, r);
			}
			analyzer.gen_branch_false(forEndLabel);
			match(TokenType.MP_DO);

			if (r != record.getType()) {
				semanticError("Invalid implicit cast from " + r + " to "
						+ record.getType()
						+ ". Invalid finalValue in for loop.");
			}

			printBranch();
			statement();
			if (forDirection == TokenType.MP_TO) {
				analyzer.gen_id_increment(r, offset, nestingLevel);
			} else if (forDirection == TokenType.MP_DOWNTO) {
				analyzer.gen_id_decrement(r, offset, nestingLevel);
			}
			analyzer.gen_branch_unconditional(forStartLabel);
			analyzer.gen_label(forEndLabel);
			break;
		default:
			syntaxError("for");
		}
	}

	public String controlVariable() {
		String id = "";

		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 57 ControlVariable -> VariableIdentifier
			printNode(57, true);
			id = variableIdentifier();
			break;
		default:
			syntaxError("identifier");
		}
		return id;
	}

	public RecordType initialValue() {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS: // 58 InitialValue -> OrdinalExpression
			printNode(58, false);
			printBranch();
			r = ordinalExpression(null);
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return r;
	}

	public TokenType stepValue() {
		TokenType t = null;
		switch (lookAhead.getToken()) {
		case MP_TO: // 59 StepValue -> mp_to
			t = TokenType.MP_TO;
			printNode(59, true);
			match(TokenType.MP_TO);
			break;
		case MP_DOWNTO: // 60 StepValue -> mp_downto
			t = TokenType.MP_DOWNTO;
			printNode(60, true);
			match(TokenType.MP_DOWNTO);
			break;
		default:
			syntaxError("to, downto");
		}
		return t;
	}

	public RecordType finalValue() {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS: // 61 FinalValue -> OrdinalExpression
			printNode(61, false);
			printBranch();
			r = ordinalExpression(null);
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return r;
	}

	public void procedureStatement() {
		String procId = null;

		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 62 ProcedureStatement -> ProcedureIdentifier
							// OptionalActualParameterList
			printNode(62, false);
			printBranch();
			procId = procedureIdentifier();

			SymbolTableRecord record = symbolTableStack
					.getSymbolInScope(procId);
			int nestingLevel = symbolTableStack.getPreviousRecordNestingLevel();
			int numParams = record.getParameters() == null ? 0 : record
					.getParameters().size();

			if (record == null) {
				semanticError("Procedure '" + procId
						+ "' not declared in this scope.");
			}
			long offset = record.getOffset();

			if (record.getKind() != RecordKind.PROCEDURE) {
				semanticError("'" + procId
						+ "' is not a valid procedure symbol.");
			}

			printBranch();
			analyzer.gen_sp_increment(1); // Leave space for old register value
			optionalActualParameterList(record);
			String procedureLabel = record.getLabel();

			if (procedureLabel == null) {
				semanticError("Unable to find label for scope '" + procId
						+ "'.");
			}

			analyzer.gen_call(procedureLabel);
			analyzer.gen_sp_decrement(numParams);
			analyzer.gen_sp_decrement(1); // Remove space for old register value

			break;
		default:
			syntaxError("identifier");
			break;
		}
	}

	public void optionalActualParameterList(SymbolTableRecord record) {
		ArrayList<RecordType> actualParameterList = new ArrayList<>();
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
		case MP_END: // 64 OptionalActualParameterList -> lambda
			printNode(64, true);
			if (record.getParameters() != null
					&& record.getParameters().size() > 0) {
				int paramsRequired = record.getParameters() == null ? 0
						: record.getParameters().size();
				semanticError("'" + record.getLexeme() + "' requires "
						+ paramsRequired + " parameters, 0 given.");
			}
			lambda();
			break;
		case MP_LPAREN: // 63 OptionalActualParameterList -> mp_lparen
						// ActualParameter ActualParameterTail mp_rparen
			printNode(63, false);
			printBranch();
			match(TokenType.MP_LPAREN);
			if (record.getParameters() != null) {
				actualParameterList.add(actualParameter(record.getParameters()
						.get(0).getMode()));
				actualParameterTail(actualParameterList, record, 1);
			} else {
				actualParameterList.add(actualParameter(null));
				actualParameterTail(actualParameterList, null, 0);
			}

			printBranch();

			int i = 0;
			if (actualParameterList.size() != record.getParameters().size()) {
				semanticError("'" + record.getLexeme() + "' requires "
						+ record.getParameters().size() + " parameters, "
						+ actualParameterList.size() + " given.");
			}
			for (RecordType r : actualParameterList) {
				if (r != record.getParameters().get(i).getType()) {
					semanticError("Invalid parameter for '"
							+ record.getLexeme() + "'. " + r + " given, "
							+ record.getParameters().get(i).getType()
							+ " expected.");
				}
				i++;
			}
			match(TokenType.MP_RPAREN);
			break;
		default:
			syntaxError("',', ), and, mod, div, / , *, 'or', -, +, <>, >=, <=, <, >, =,"
					+ " downto, to, do, until, else, then, ;, end");
		}
	}

	public void actualParameterTail(ArrayList<RecordType> actualParameterList,
			SymbolTableRecord record, int currentIndex) {
		switch (lookAhead.getToken()) {
		case MP_COMMA: // 65 ActualParameterTail -> mp_comma ActualParameter
						// ActualParameterTail
			printNode(65, false);
			printBranch();
			match(TokenType.MP_COMMA);
			if (record != null) {
				actualParameterList.add(actualParameter(record.getParameters()
						.get(currentIndex).getMode()));
			} else {
				actualParameterList.add(actualParameter(null));
			}
			printBranch();
			actualParameterTail(actualParameterList, record, currentIndex + 1);
			break;
		case MP_RPAREN: // 66 ActualParameterTail -> lambda
			printNode(66, true);
			lambda();
			break;
		default:
			syntaxError("',', )");
		}
	}

	public RecordType actualParameter(RecordMode mode) {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS: // 67 ActualParameter -> OrdinalExpression
			printNode(67, false);
			printBranch();
			r = ordinalExpression(mode);

			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return r;
	}

	/**
     * 
     */
	public RecordType expression(RecordType previousRecordType, RecordMode mode) {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS: // 68 Expression -> SimpleExpression
						// OptionalRelationalPart
			printNode(68, false);
			printBranch();
			r = simpleExpression(previousRecordType, mode);

			printBranch();
			RecordType bool = optionalRelationalPart(r);
                        r = bool == null ? r : bool;
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return r;
	}

	/**
     * 
     */
	public  RecordType optionalRelationalPart(RecordType r) {
		RecordType rr = null;
                TokenType t = null;
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
		case MP_END: // 70 OptionalRelationalPart -> lambda
			printNode(70, true);
			lambda();
			break;
		case MP_NEQUAL:
		case MP_GEQUAL:
		case MP_LEQUAL:
		case MP_GTHAN:
		case MP_LTHAN:
		case MP_EQUAL: // 69 OptionalRelationalPart -> RelationalOperator
						// SimpleExpression
			printNode(69, false);
			printBranch();
			t = relationalOperator();
			printBranch();
			simpleExpression(r, null);
			analyzer.gen_bool_expr(t, r);
                        rr = RecordType.BOOLEAN;
			break;
		default:
			syntaxError("',', ), downto, to, do, until, else, then, ;, end, <>, >=, <=, >, <, =");
		}
		return rr;
	}

	/**
	 * 
	 * @return SemanticRec RecordType.REL_OP
	 */
	public TokenType relationalOperator() {
		TokenType t = null;

		switch (lookAhead.getToken()) {
		case MP_NEQUAL: // 76 RelationalOperator -> mp_nequal
			printNode(76, true);
			t = TokenType.MP_NEQUAL;
			match(TokenType.MP_NEQUAL);
			break;
		case MP_GEQUAL: // 75 RelationalOperator -> mp_gequal
			t = TokenType.MP_GEQUAL;
			printNode(75, true);
			match(TokenType.MP_GEQUAL);
			break;
		case MP_LEQUAL: // 74 RelationalOperator -> mp_lequal
			t = TokenType.MP_LEQUAL;
			printNode(74, true);
			match(TokenType.MP_LEQUAL);
			break;
		case MP_GTHAN: // 73 RelationalOperator -> mp_gthan
			t = TokenType.MP_GTHAN;
			printNode(73, true);
			match(TokenType.MP_GTHAN);
			break;
		case MP_LTHAN: // 72 RelationalOperator -> mp_lthan
			t = TokenType.MP_LTHAN;
			printNode(72, true);
			match(TokenType.MP_LTHAN);
			break;
		case MP_EQUAL: // 71 RelationalOperator -> mp_equal
			t = TokenType.MP_EQUAL;
			printNode(71, true);
			match(TokenType.MP_EQUAL);
			break;
		default:
			syntaxError("<>, >=, <= , >, <, =");
		}
		return t;
	}

	/**
	 * 
	 * @param formalParam
	 * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
	 */
	public RecordType simpleExpression(RecordType previousRecordType,
			RecordMode mode) {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS: // 77 SimpleExpression -> OptionalSign Term TermTail
			printNode(77, false);
			printBranch();
			TokenType t = optionalSign();

			printBranch();
			r = term(previousRecordType, mode);

			if (r == RecordType.FLOAT && t == TokenType.MP_MINUS) {
				analyzer.gen_negate_float();
			} else if (r == RecordType.INTEGER && t == TokenType.MP_MINUS) {
				analyzer.gen_negate_int();
			}

			printBranch();
			RecordType tailType = termTail(r);
			r = tailType == null ? r : tailType;
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return r;
	}

	/**
	 * 
	 * @param left
	 * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
	 */
	public RecordType termTail(RecordType previousRecordType) {
		RecordType r = null;
		TokenType t = null;

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
		case MP_END: // 79 TermTail -> lambda
			printNode(79, true);
			lambda();
			break;
		case MP_OR:
		case MP_MINUS:
		case MP_PLUS: // 78 TermTail -> AddingOperator Term TermTail
			printNode(78, false);
			printBranch();
			String addOp = lookAhead.getLexeme();
			TokenType currentOp = lookAhead.getToken();
			t = addingOperator();

			printBranch();
			r = term(previousRecordType, null);

			if ((currentOp != TokenType.MP_OR)
					&& (r != null && previousRecordType != null)
					&& ((r == RecordType.STRING || r == RecordType.BOOLEAN) || 
							(previousRecordType == RecordType.STRING || 
							previousRecordType == RecordType.BOOLEAN))) {
				semanticError("Invalid operation. '" + addOp
						+ "' does not apply to " + previousRecordType + " and "
						+ r + ".");
			}
			analyzer.gen_add_op(t, r);
			printBranch();
			RecordType tailType = termTail(r);
			r = tailType == null ? r : tailType;
			break;
		default:
			syntaxError("',', ), <>, >=, <=, >, <, =, downto, to, do, until, else, then, ;, end, or, -, +");
		}
		return r == null ? previousRecordType : r;
	}

	/**
	 * 
	 * @return SemanticRec RecordType.OPT_SIGN
	 */
	public TokenType optionalSign() {
		TokenType t = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT: // 82 OptionalSign -> lambda
			printNode(82, true);
			lambda();
			break;
		case MP_MINUS: // 81 OptionalSign -> mp_minus
			t = TokenType.MP_MINUS;
			printNode(81, true);
			match(TokenType.MP_MINUS);
			break;
		case MP_PLUS: // 80 OptionalSign -> mp_plus
			t = TokenType.MP_PLUS;
			printNode(80, true);
			match(TokenType.MP_PLUS);
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return t;
	}

	public TokenType addingOperator() {
		TokenType t = null;
		switch (lookAhead.getToken()) {
		case MP_OR: // 85 AddingOperator -> mp_or
			printNode(85, true);
			t = TokenType.MP_OR;
			match(TokenType.MP_OR);
			break;
		case MP_MINUS: // 84 AddingOperator -> mp_minus
			printNode(84, true);
			t = TokenType.MP_MINUS;
			match(TokenType.MP_MINUS);
			break;
		case MP_PLUS: // 83 AddingOperator -> mp_plus
			printNode(83, true);
			t = TokenType.MP_PLUS;
			match(TokenType.MP_PLUS);
			break;
		default:
			syntaxError("or, -, +");
		}
		return t;
	}

	/**
	 * 
	 * @param formalParam
	 * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
	 */
	public RecordType term(RecordType previousRecordType, RecordMode mode) {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT: // 86 Term -> Factor FactorTail
			printNode(86, false);
			printBranch();

			r = factor(previousRecordType, mode);

			printBranch();
			RecordType tailType = factorTail(r);
			if (r == RecordType.FLOAT || tailType == RecordType.FLOAT) {
				r = RecordType.FLOAT;
			}
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer");
		}
		return r;
	}

	/**
	 * 
	 * @param left
	 * @return SemanticRec RecordType.LITERAL or RecordType.IDENTIFIER
	 */
	public RecordType factorTail(RecordType previousRecordType) {
		RecordType r = null;
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
		case MP_END: // 88 FactorTail -> lambda
			printNode(88, true);
			lambda();
			break;
		case MP_AND:
		case MP_MOD:
		case MP_DIV:
		case MP_FLOAT_DIVIDE: // added for / vs div division
		case MP_TIMES: // 87 FactorTail -> MultiplyingOperator Factor FactorTail
			printNode(87, false);

			printBranch();
			String multOp = lookAhead.getLexeme();
			TokenType currentOp = lookAhead.getToken();
			TokenType t = multiplyingOperator();

			printBranch();
			r = factor(previousRecordType, null);
			if ((currentOp != TokenType.MP_AND)
					&& (r != null && previousRecordType != null)
					&& ((r == RecordType.STRING || r == RecordType.BOOLEAN) || 
							(previousRecordType == RecordType.STRING ||
							previousRecordType == RecordType.BOOLEAN))) {
				semanticError("Invalid operation. '" + multOp
						+ "' does not apply to " + previousRecordType + " and "
						+ r + ".");
			}
			if ((r != null && previousRecordType != null)
					&& (currentOp == TokenType.MP_AND)
					&& (r != RecordType.BOOLEAN || previousRecordType != RecordType.BOOLEAN)) {
				semanticError("Invalid operation. '" + multOp
						+ "' does not apply to " + previousRecordType + " and "
						+ r + ".");
			}
			analyzer.gen_mul_op(t, r);
			printBranch();
			RecordType tailType = factorTail(r);
			if (r == RecordType.FLOAT || tailType == RecordType.FLOAT) {
				r = RecordType.FLOAT;
			}
			break;
		default:
			syntaxError("',', ), or, -, +, <>, >=, <=, >, <, =, downto, to, do, until, "
					+ "else, then, ;, end, and, mod, div, / , *");
		}
		return r == null ? previousRecordType : r;
	}

	/**
	 * 
	 * @return SemanticRec RecordType.MUL_OP
	 */
	public TokenType multiplyingOperator() {
		TokenType t = null;
		switch (lookAhead.getToken()) {
		case MP_AND: // 92 MultiplyingOperator -> mp_and
			printNode(92, true);
			t = TokenType.MP_AND;
			match(TokenType.MP_AND);
			break;
		case MP_MOD: // 91 MultiplyingOperator -> mp_mod
			printNode(91, true);
			t = TokenType.MP_MOD;
			match(TokenType.MP_MOD);
			break;
		case MP_FLOAT_DIVIDE: // 112 MultiplyingOperator -> mp_float_divide "/"
			printNode(112, true);
			t = TokenType.MP_FLOAT_DIVIDE;
			match(TokenType.MP_FLOAT_DIVIDE);
			break;
		case MP_DIV: // 90 MultiplyingOperator -> mp_div "div"
			printNode(90, true);
			t = TokenType.MP_DIV;
			match(TokenType.MP_DIV);
			break;
		case MP_TIMES: // 89 MultiplyingOperator -> mp_times
			printNode(89, true);
			t = TokenType.MP_TIMES;
			match(TokenType.MP_TIMES);
			break;
		default:
			syntaxError("and, mod, div, / , *");
		}
		return t;
	}

	/**
	 * 
	 * @param formalParam
	 * @return SemanticRec RecordType.IDENTIFIER or RecordType.LITERAL
	 */
	public RecordType factor(RecordType previousRecordType, RecordMode mode) {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:
			printNode(106, false);

			printBranch();
			String lexeme = variableIdentifier();
			SymbolTableRecord record = symbolTableStack
					.getSymbolInScope((lexeme));
			if (record == null) {
				semanticError("Identifier '" + lexeme + "' not declared");
			}
			int nestingLevel = symbolTableStack.getPreviousRecordNestingLevel();
			if (previousRecordType == RecordType.INTEGER
					&& record.getType() == RecordType.FLOAT) {
				analyzer.gen_cast_op(record.getType());
			}
			if (record.getKind() == RecordKind.VARIABLE) {
				if (mode == RecordMode.VARIABLE) {
					analyzer.gen_id_addr_push(record.getOffset(), nestingLevel);
				} else if (record.getMode() == RecordMode.VARIABLE) {
					analyzer.gen_id_deref_push(record.getOffset(), nestingLevel);
				} else {
					analyzer.gen_id_push(record.getOffset(), nestingLevel);
				}
			} else if (record.getKind() == RecordKind.FUNCTION) {

				long offset = record.getOffset();
				int numParams = record.getParameters() == null ? 0 : record
						.getParameters().size();
				if (record == null) {
					semanticError("Symbol '" + lexeme
							+ "' not declared in this scope.");
				}

				if (record.getKind() != RecordKind.FUNCTION) {
					semanticError("'" + lexeme
							+ "' is not a valid function symbol.");
				}

				printBranch();
				analyzer.gen_sp_increment(1);
				optionalActualParameterList(record);
				String procedureLabel = record.getLabel();

				if (procedureLabel == null) {
					semanticError("Unable to find label for scope '" + lexeme
							+ "'.");
				}

				analyzer.gen_call(procedureLabel);
				analyzer.gen_sp_decrement(numParams);
				analyzer.gen_sp_decrement(1);

				// If function, push return value
				if (record.getKind() == RecordKind.FUNCTION) {
					analyzer.gen_id_push(offset, nestingLevel);
				}
			} else {
				semanticError("Invalid factor");
			}

			r = record.getType();

			if (previousRecordType == RecordType.FLOAT
					&& record.getType() == RecordType.INTEGER) {
				analyzer.gen_cast_op(previousRecordType);
				r = RecordType.FLOAT;
			} else if (previousRecordType != null && record.getType() != null
					&& previousRecordType != record.getType()) {
				semanticError("Incompatible types " + previousRecordType
						+ " and " + record.getType() + ".");
			}

			printBranch();

			break;
		case MP_LPAREN: // 96 Factor -> mp_lparen Expression mp_rparen
			printNode(96, false);
			match(TokenType.MP_LPAREN);
			printBranch();
			r = expression(previousRecordType, null);

			match(TokenType.MP_RPAREN);
			break;
		case MP_NOT: // 95 Factor -> mp_not Factor
			printNode(95, false);
			printBranch();
			match(TokenType.MP_NOT);
			r = factor(previousRecordType, null);
			if (r != RecordType.BOOLEAN) {
				semanticError("Invalid operation. " + r
						+ " is not compatible with 'not'.");
			}
			analyzer.gen_not_op();
			break;
		case MP_INTEGER_LIT: // 93 Factor -> mp_integer_lit
			printNode(93, true);
			analyzer.gen_lit_push(lookAhead.getLexeme());
			r = RecordType.INTEGER;
			if (previousRecordType == RecordType.FLOAT) {
				analyzer.gen_cast_op(previousRecordType);
				r = RecordType.FLOAT;
			}
			match(TokenType.MP_INTEGER_LIT);
			break;
		case MP_FALSE: // 116 Factor -> mp_false
			printNode(116, true);
			analyzer.gen_lit_push("0");
			match(TokenType.MP_FALSE);
			r = RecordType.BOOLEAN;
			break;
		case MP_TRUE: // 115 Factor -> mp_true
			printNode(115, true);
			analyzer.gen_lit_push("1");
			match(TokenType.MP_TRUE);
			r = RecordType.BOOLEAN;
			break;
		case MP_STRING_LIT: // 114 Factor -> mp_string_lit
			printNode(114, true);
			lookAhead.setLexeme(lookAhead.getLexeme().replaceAll("'", ""));
			analyzer.gen_lit_push("\"" + lookAhead.getLexeme() + "\"");
			match(TokenType.MP_STRING_LIT);
			r = RecordType.STRING;
			break;
		case MP_FIXED_LIT:
			printNode(113, true);
			r = RecordType.FLOAT;
			if (previousRecordType == RecordType.INTEGER) {
				analyzer.gen_cast_op(RecordType.FLOAT);
				r = RecordType.FLOAT;
			}
			analyzer.gen_lit_push(lookAhead.getLexeme());
			match(TokenType.MP_FIXED_LIT);
			break;
		case MP_FLOAT_LIT: // 113 Factor -> mp_float_lit
			printNode(113, true);
			if (previousRecordType == RecordType.INTEGER) {
				analyzer.gen_cast_op(RecordType.FLOAT);
			}
			analyzer.gen_lit_push(lookAhead.getLexeme());
			match(TokenType.MP_FLOAT_LIT);
			r = RecordType.FLOAT;
			break;
		default:
			syntaxError("identifier, (, not, Integer, false, true, String, Float");
		}
		return r;
	}

	public String programIdentifier() {
		String name = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 98 ProgramIdentifier -> mp_identifier
			printNode(98, true);
			name = lookAhead.getLexeme();
			match(TokenType.MP_IDENTIFIER);
			break;
		default:
			syntaxError("identifier");
		}
		return name;
	}

	public String variableIdentifier() {
		String name = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 99 VariableIdentifier -> mp_identifier
			printNode(99, true);
			name = lookAhead.getLexeme();
			match(TokenType.MP_IDENTIFIER);
			break;
		default:
			syntaxError("identifier");
			break;
		}
		return name;
	}

	public String procedureIdentifier() {
		String name = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 100 ProcedureIdentifier -> mp_identifier
			printNode(100, true);
			name = lookAhead.getLexeme();
			match(TokenType.MP_IDENTIFIER);
			break;
		default:
			syntaxError("identifier");
		}
		return name;
	}

	public void formalParameterSectionTail(List<RecordParameter> params) {
		switch (lookAhead.getToken()) {
		case MP_SCOLON: // 19 FormalParameterSectionTail -> mp_scolon
						// FormalParameterSection FormalParameterSectionTail
			printNode(19, false);
			printBranch();
			match(TokenType.MP_SCOLON);
			params.addAll(formalParameterSection());

			printBranch();
			formalParameterSectionTail(params);
			break;
		case MP_RPAREN: // 20 FormalParameterSectionTail -> &epsilon
			printNode(20, true);
			break;
		default:
			syntaxError("function, )");
		}
	}

	public ArrayList<RecordParameter> formalParameterSection() {
		ArrayList<RecordParameter> params = new ArrayList<>();
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 21 FormalParameterSection ->
							// ValueParameterSection #insert
			printNode(21, false);
			printBranch();
			params = valueParameterSection();
			break;
		case MP_VAR: // 22 FormalParameterSection -> VariableParameterSection
						// #insert
			printNode(22, false);
			printBranch();
			params = variableParameterSection();
			break;
		default:
			syntaxError("identifier, var");
		}
		return params;
	}

	public ArrayList<RecordParameter> valueParameterSection() {
		ArrayList<RecordParameter> params = new ArrayList<>();
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 23 ValueParameterSection -> IdentifierList
							// mp_colon Type
			printNode(23, false);
			printBranch();
			List<String> ids = identifierList();
			match(TokenType.MP_COLON);

			printBranch();
			RecordType t = type();
			for (String id : ids) {
				params.add(new RecordParameter(id, RecordMode.VALUE, t));
			}
			break;
		default:
			syntaxError("identifier");
		}
		return params;
	}

	public ArrayList<RecordParameter> variableParameterSection() {
		ArrayList<RecordParameter> params = new ArrayList<>();
		switch (lookAhead.getToken()) {
		case MP_VAR: // 24 VariableParameterSection -> mp_var IdentifierList
						// mp_colon Type
			printNode(24, false);
			printBranch();
			match(TokenType.MP_VAR);
			List<String> ids = identifierList();

			printBranch();
			match(TokenType.MP_COLON);

			RecordType t = type();
			for (String id : ids) {
				params.add(new RecordParameter(id, RecordMode.VARIABLE, t));
			}
			break;
		default:
			syntaxError("var");
		}
		return params;
	}

	public void statementPart() {
		switch (lookAhead.getToken()) {
		case MP_BEGIN: // 25 StatementPart -> CompoundStatement
			printNode(25, false);
			printBranch();
			compoundStatement();
			break;
		default:
			syntaxError("begin");
		}
	}

	public void compoundStatement() {
		switch (lookAhead.getToken()) {
		case MP_BEGIN: // 26 CompoundStatement -> mp_begin StatementSequence
						// mp_end
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

	public void statementSequence() {
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 27 StatementSequence -> Statement StatementTail
		case MP_FOR:
		case MP_WHILE:
		case MP_UNTIL:
		case MP_REPEAT:
		case MP_IF:
		case MP_WRITELN: // added writeln
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

	public void statementTail() {
		switch (lookAhead.getToken()) {
		case MP_SCOLON: // 28 StatementTail -> mp_scolon Statement StatementTail
			printNode(28, false);
			match(TokenType.MP_SCOLON);
			printBranch();
			statement();

			printBranch();
			statementTail();
			break;
		case MP_UNTIL: // 29 StatementTail -> &epsilon
		case MP_END:
			printNode(29, true);
			lambda();
			break;
		default:
			syntaxError(";, until, end");
		}
	}

	public void statement() {
		switch (lookAhead.getToken()) {
		case MP_UNTIL: // 30 Statement -> EmptyStatement
		case MP_ELSE:
		case MP_SCOLON:
		case MP_END:
			printNode(30, false);
			printBranch();
			emptyStatement();
			break;
		case MP_BEGIN: // 31 Statement -> CompoundStatement
			printNode(31, false);
			printBranch();
			compoundStatement();
			break;
		case MP_READ: // 32 Statement -> ReadStatement
			printNode(32, false);
			printBranch();
			readStatement();
			break;
		case MP_WRITELN:
		case MP_WRITE: // 33 Statement -> WriteStatement
			printNode(33, false);
			printBranch();
			writeStatement();
			break;
		case MP_IDENTIFIER:
			if (lookAhead2.getToken() == TokenType.MP_ASSIGN) {
				printNode(34, false);
				printBranch();
				assignmentStatement(); // 34 Statement -> AssigmentStatement
			} else {
				printNode(39, false);
				printBranch();
				procedureStatement();// 39 Statement -> ProcedureStatement
			}
			break;
		case MP_IF:
			printNode(35, false);
			printBranch();
			ifStatement(); // 35 Statement -> IfStatement
			break;
		case MP_WHILE:
			printNode(36, false);
			printBranch();
			whileStatement(); // 36 Statement -> WhileStatement
			break;
		case MP_REPEAT:
			printNode(37, false);
			printBranch();
			repeatStatement(); // 37 Statement -> RepeatStatement
			break;
		case MP_FOR:
			printNode(38, false);
			printBranch();
			forStatement(); // 38 Statement -> ForStatement
			break;
		default:
			syntaxError("until, else, ;, end, begin, Read, Write, Writeln, identifier, if, while, repeat, for");
		}
	}

	public void emptyStatement() {
		switch (lookAhead.getToken()) {
		case MP_UNTIL: // 40 EmptyStatement -> &epsilon
		case MP_ELSE:
		case MP_SCOLON:
		case MP_END:
			printNode(40, true);
			lambda();
			break;
		default:
			syntaxError("until, else, ;, end");
		}
	}

	public void readStatement() {
		switch (lookAhead.getToken()) {
		case MP_READ: // 41 ReadStatement -> mp_read mp_lparen ReadParameter
						// ReadParameterTail mp_rparen
			printNode(41, false);
			match(TokenType.MP_READ);
			match(TokenType.MP_LPAREN);
			printBranch();
			readParameter();

			readParameterTail();
			match(TokenType.MP_RPAREN);
			break;
		default:
			syntaxError("Read");
		}
	}

	public void readParameterTail() {
		switch (lookAhead.getToken()) {
		case MP_COMMA: // 42 ReadParameterTail -> mp_comma ReadParameter
						// ReadParameterTail
			printNode(42, false);
			match(TokenType.MP_COMMA);
			printBranch();
			readParameter();

			printBranch();
			readParameterTail();
			break;
		case MP_RPAREN: // 43 ReadParameterTail -> &epsilon
			printNode(43, true);
			lambda();
			break;
		default:
			syntaxError("Read, )");
		}
	}

	public String readParameter() {
		String lexeme = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 44 ReadParameter -> VariableIdentifier
			printNode(44, false);
			printBranch();
			lexeme = variableIdentifier();
			SymbolTableRecord record = symbolTableStack
					.getSymbolInScope((lexeme));

			if (record == null) {
				semanticError("Variable '" + lexeme
						+ "' undeclared in this scope.");
			}
			int nestingLevel = symbolTableStack.getPreviousRecordNestingLevel();
			long offset = record.getOffset();

			RecordType r = record.getType();

			analyzer.gen_read_op(r, offset, nestingLevel);
			printBranch();

			break;
		default:
			syntaxError("identifier");
		}
		return lexeme;
	}

	public void writeStatement() {
		switch (lookAhead.getToken()) {
		case MP_WRITE: // 45 WriteStatement -> mp_write mp_lparen WriteParameter
						// WriteParameterTail mp_rparen
			printNode(45, false);
			match(TokenType.MP_WRITE);
			match(TokenType.MP_LPAREN);
			printBranch();
			writeParameter();

			printBranch();
			writeParameterTail();

			match(TokenType.MP_RPAREN);
			break;
		case MP_WRITELN: // 111 WriteStatement -> mp_writeln mp_lparen
							// WriteParameter WriteParameterTail mp_rparen.
			printNode(111, false);
			match(TokenType.MP_WRITELN);
			match(TokenType.MP_LPAREN);
			printBranch();
			writeParameter();

			printBranch();
			writeParameterTail();
			analyzer.gen_lit_push("\"\\n\"");
			analyzer.gen_write_op();
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
	public void writeParameterTail() {
		switch (lookAhead.getToken()) {
		case MP_COMMA: // 46 WriteParameterTail -> mp_comma WriteParameter
						// WriteParameterTail
			printNode(46, false);
			match(TokenType.MP_COMMA);
			printBranch();
			writeParameter();

			printBranch();
			writeParameterTail();
			break;
		case MP_RPAREN: // 47 WriteParameterTail -> &epsilon
			printNode(47, false);
			lambda();
			break;
		default:
			syntaxError("',', )");
		}
	}

	/**
     * 
     * 
     */
	public void writeParameter() {
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 48 WriteParameter -> OrdinalExpression
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS:
			printNode(48, false);
			printBranch();
			ordinalExpression(null);
			analyzer.gen_write_op();
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
	}

	public void assignmentStatement() {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER:

			String lexeme = variableIdentifier();

			SymbolTableRecord record = symbolTableStack
					.getSymbolInScope(lexeme);
			if (record == null) {
				semanticError("Variable '" + lexeme + "' not declared");
			}
			int nestingLevel = symbolTableStack.getPreviousRecordNestingLevel();

			match(TokenType.MP_ASSIGN);
			r = expression(null, null);
			if (record.getType() == RecordType.FLOAT && r == RecordType.INTEGER) {
				analyzer.gen_cast_op(record.getType());
			} else if (r != record.getType()) {
				semanticError("Unable to do implicit cast from " + r + " to "
						+ record.getType() + ".");
			}
			if (record.getMode() == RecordMode.VARIABLE) {
				analyzer.gen_id_deref_pop(record.getOffset(), nestingLevel);
			} else {
				analyzer.gen_id_pop(record.getOffset(), nestingLevel);
			}
			// functionIdentifier();
			// match(TokenType.MP_ASSIGN);
			// expression(null)
			break;

		default:
			syntaxError("identifier");
		}
	}

	// 103
	public String functionIdentifier() {
		String id = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 101 FunctionIdentifier -> mp_identifier
			printNode(101, false);
			id = lookAhead.getLexeme();
			match(TokenType.MP_IDENTIFIER);
			break;
		default:
			syntaxError("identifier");
		}
		return id;
	}

	public RecordType booleanExpression() {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 102 BooleanExpression -> Expression
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS:
			printNode(102, false);
			printBranch();
			r = expression(null, null);
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return r;
	}

	public RecordType ordinalExpression(RecordMode mode) {
		RecordType r = null;
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 103 OrdinalExpression -> Expression
		case MP_FALSE:
		case MP_TRUE:
		case MP_STRING_LIT:
		case MP_FIXED_LIT:
		case MP_FLOAT_LIT: // added boolean values, string, float
		case MP_LPAREN:
		case MP_NOT:
		case MP_INTEGER_LIT:
		case MP_MINUS:
		case MP_PLUS:
			printNode(103, false);
			printBranch();
			r = expression(null, mode);
			break;
		default:
			syntaxError("identifier, false, true, String, Float, (, not, Integer, -, +");
		}
		return r;
	}

	public List<String> identifierList() {
		List<String> ids = new ArrayList<>();
		switch (lookAhead.getToken()) {
		case MP_IDENTIFIER: // 104 IdentifierList -> mp_identifier
							// IdentifierTail
			printNode(104, false);
			ids.add(lookAhead.getLexeme());
			match(TokenType.MP_IDENTIFIER);
			identifierTail(ids);
			break;
		default:
			syntaxError("identifier");
		}
		return ids;
	}

	public void identifierTail(List<String> ids) {
		switch (lookAhead.getToken()) {
		case MP_COMMA: // 105 IdentifierTail -> mp_comma mp_identifier
						// IdentifierTail
			printNode(105, false);
			printBranch();
			match(TokenType.MP_COMMA);
			ids.add(lookAhead.getLexeme());
			match(TokenType.MP_IDENTIFIER);
			identifierTail(ids);
			break;
		case MP_COLON: // 106 IdentifierTail -> &epsilon
			printNode(106, true);
			break;
		default:
			syntaxError("',', :");
		}
	}
}