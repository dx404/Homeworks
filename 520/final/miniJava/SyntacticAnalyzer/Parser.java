/* Parser written by Duo Zhao*/
package miniJava.SyntacticAnalyzer;


import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package; //explicitly import to avoid ambiguity 

import java.nio.Buffer;
import java.util.ArrayList;

public class Parser {

	private Scanner lexicalAnalyser;
	private ErrorReporter errorReporter;
	private Token currentToken, peekToken;
	private SourcePosition preTokenPosn, prePeekPosn;

	private ArrayList<Token> tokenBuffer = new ArrayList<Token>();

	public Parser(Scanner lexer, ErrorReporter reporter) {
		lexicalAnalyser = lexer;
		errorReporter = reporter;
		preTokenPosn = new SourcePosition();
		preTokenPosn = new SourcePosition();
	}

	void consumeOne() {
		preTokenPosn = currentToken.position;
		if (tokenBuffer.size() == 0){
			currentToken = lexicalAnalyser.scan();
		}
		else{
			currentToken = tokenBuffer.remove(0);
		}
	}

	void consumeOne(int tokenExpected) throws SyntaxError { 
		if (currentToken.kind == tokenExpected){
			preTokenPosn = currentToken.position;
			if (tokenBuffer.size() == 0){
				currentToken = lexicalAnalyser.scan();
			}
			else{
				currentToken = tokenBuffer.remove(0);
			}
		}
		else{
			syntacticError("\"%\" expected here", Token.spell(tokenExpected));
		}
	}

	boolean tryConsumeOne(int tokenExpected){
		if (currentToken.kind == tokenExpected){
			preTokenPosn = currentToken.position;
			currentToken = tokenBuffer.size() == 0 ? 
					lexicalAnalyser.scan() :
						tokenBuffer.remove(0) ;
					return true;
		}
		else{
			return false;
		}
	}

	void bufferOne(){ //unconditionally buffer the current token and shift a token in source file
		prePeekPosn = (tokenBuffer.size() == 0) ? 
				currentToken.position: 
					peekToken.position;
		peekToken = lexicalAnalyser.scan(); //update peekToken
		tokenBuffer.add(peekToken);
	}
	
	int peekNextToken(){
		if (tokenBuffer.size() == 0){
			bufferOne();
		}
		return tokenBuffer.get(0).kind;
	}

	void start(SourcePosition position) {
		position.start = currentToken.position.start;
	}

	SourcePosition finish(SourcePosition position) {
		position.finish = preTokenPosn.finish;
		return position;
	}

	void syntacticError(String messageTemplate, String tokenQuoted)
			throws SyntaxError {
		SourcePosition pos = currentToken.position;
		System.out
		.println("===========ERROR: System.exit(4);=================");
		errorReporter.reportError(messageTemplate, tokenQuoted, pos);
		// System.exit(4);
		throw (new SyntaxError());
	}


	/**
	 * Following are the parser
	 * @return
	 */
	public Package parsePackage() {
		Package packageAST = null; // Root node, a Package Type. (Abstract)

		ClassDeclList classDeclListAST = new ClassDeclList();

		preTokenPosn.start = 0;
		preTokenPosn.finish = 0;

		currentToken = lexicalAnalyser.scan();

		try {
			while (currentToken.kind == Token.CLASS) {
				ClassDecl classDeclAST = parseClassDeclaration();
				classDeclListAST.add(classDeclAST);
			}

			packageAST = new Package(classDeclListAST, preTokenPosn);

			if (currentToken.kind != Token.EOT) {
				syntacticError("\"%\" not expected after end of program",
						currentToken.spelling);
			}
		} catch (SyntaxError s) {
			return null;
		}

		return packageAST;
	}

	public ClassDecl parseClassDeclaration() throws SyntaxError {
		ClassDecl classDeclAST = null;

		FieldDeclList fieldDeclListAST = new FieldDeclList();
		MethodDeclList methodDeclListAST = new MethodDeclList();

		Reference extendsRef = null; // final added

		MemberDecl memberDecl = null;
		FieldDecl fieldDecl = null; // for temp
		MethodDecl methodDecl = null; // for temp

		SourcePosition classDeclarationPos = new SourcePosition();
		start(classDeclarationPos);

		consumeOne(Token.CLASS);
		Identifier idAST = parseIdentifier();
		/****** final project added ******************/
		if (currentToken.kind == Token.EXTENDS) {
			consumeOne();
			extendsRef = new ClassRef(parseIdentifier(), finish(classDeclarationPos));
		}
		/*******************************************/
		consumeOne(Token.LCURLY);
		while (currentToken.kind != Token.RCURLY) {
			boolean isPrivate = false; // default value
			boolean isStatic = false; // default value.
			Type typeDecltor = null;
			Identifier idDecltor = null;

			switch (currentToken.kind) {
			case Token.PUBLIC:
				consumeOne();
				isPrivate = false;
				break;
			case Token.PRIVATE:
				consumeOne();
				isPrivate = true;
				break;
			default: // default value is private
				break;
			}

			if (currentToken.kind == Token.STATIC) {
				consumeOne();
				isStatic = true;
			}

			typeDecltor = parseType();

			idDecltor = parseIdentifier();

			finish(classDeclarationPos);
			memberDecl = new FieldDecl(isPrivate, isStatic, typeDecltor,
					idDecltor, classDeclarationPos);

			switch (currentToken.kind) {
			case Token.SEMICOLON: // ';' parsing filedDecl
				consumeOne();
				finish(classDeclarationPos);
				fieldDecl = new FieldDecl(memberDecl, classDeclarationPos);
				fieldDeclListAST.add(fieldDecl);
				break;

			case Token.LPAREN: { // '(' parsing MethodDecl
				ParameterDeclList parameterDeclList = new ParameterDeclList();
				StatementList statementList = new StatementList();
				Expression returnExp = null;

				consumeOne();
				if (currentToken.kind != Token.RPAREN) {
					parameterDeclList = parseParameterList();
				}
				consumeOne(Token.RPAREN);
				consumeOne(Token.LCURLY);
				while (currentToken.kind != Token.RCURLY
						&& currentToken.kind != Token.RETURN) {
					Statement statComponent = parseStatement();
					statementList.add(statComponent);
				}
				if (tryConsumeOne(Token.RETURN)) {
					if (currentToken.kind != Token.SEMICOLON){
						returnExp = parseExpression();
					}
					consumeOne(Token.SEMICOLON);
				}
				consumeOne(Token.RCURLY);

				finish(classDeclarationPos);
				methodDecl = new MethodDecl(memberDecl, parameterDeclList,
						statementList, returnExp, classDeclarationPos);
				methodDeclListAST.add(methodDecl);
			}
			break;

			default:
				syntacticError("\"%\" cannot start classDeclaration",
						currentToken.spelling);
				break;
			}
		}
		consumeOne(Token.RCURLY);

		finish(classDeclarationPos);
		classDeclAST = new ClassDecl(idAST, fieldDeclListAST,
				methodDeclListAST, classDeclarationPos);
		classDeclAST.superClassRef = extendsRef; // final project added

		return classDeclAST;
	}

	public ParameterDeclList parseParameterList() throws SyntaxError {
		ParameterDeclList parameterDeclList = new ParameterDeclList();

		SourcePosition parseParameterListPos = new SourcePosition();
		start(parseParameterListPos);

		Type typeAST = parseType();
		Identifier idAST = parseIdentifier();

		finish(parseParameterListPos);
		ParameterDecl parameterDecl = new ParameterDecl(typeAST, idAST, parseParameterListPos);
		parameterDeclList.add(parameterDecl);

		while (currentToken.kind == Token.COMMA) {
			consumeOne();

			typeAST = parseType();
			idAST = parseIdentifier();

			parameterDecl = new ParameterDecl(typeAST, idAST, finish(parseParameterListPos));
			parameterDeclList.add(parameterDecl);
		}

		return parameterDeclList;
	}


	/**
	 * for now support increment/decrement and new object
	 * later should also support callExpr and Assignment Expr
	 * @return
	 * @throws SyntaxError
	 */
	public ExprStmt parseExprStmt(int endToken) throws SyntaxError {
		ExprStmt exprStmt = null;

		SourcePosition esPos = new SourcePosition();
		start(esPos);

		switch (currentToken.kind) {
		case Token.INCREMENT: // pre-increment
			consumeOne();
			Reference increRef = parseReference();
			IncrementExpr preIncreExpr = new IncrementExpr(increRef, false, finish(esPos));
			exprStmt = new ExprStmt(preIncreExpr, finish(esPos));
			break;

		case Token.DECREMENT: // pre-decrement
			consumeOne();
			Reference preDecreRef = parseReference();
			DecrementExpr preDecreExpr = new DecrementExpr(preDecreRef, false, finish(esPos));
			exprStmt = new ExprStmt(preDecreExpr, finish(esPos));
			break;

		case Token.NEW:
			NewObjectExpr newExpr = parseNewObjExpr();
			exprStmt = new ExprStmt(newExpr, finish(esPos));
			break;

		default:
			Reference refHeader = parseReference(); //not an array of method may be ExprRef
			if (refHeader instanceof MethodRef){
				exprStmt = new ExprStmt(new CallExpr((MethodRef)refHeader));
			}
			else if (refHeader instanceof SubMethodRef){
				exprStmt = new ExprStmt(new CallExpr((SubMethodRef)refHeader));
			}
			else if (currentToken.kind == Token.INCREMENT) { // post-increment a, a[i]++;
				consumeOne();
				IncrementExpr increExpr = new IncrementExpr(refHeader, true, finish(esPos));
				exprStmt = new ExprStmt(increExpr, finish(esPos));
			} 
			else if (currentToken.kind == Token.DECREMENT) { // post-decrement a, a[i]--;
				consumeOne();
				DecrementExpr decreExpr = new DecrementExpr(refHeader, true, finish(esPos));
				exprStmt = new ExprStmt(decreExpr, finish(esPos));
			} 
			else if (currentToken.kind == Token.BECOMES){ //AssignmentExpr Stmt a, a[i] = 5;
				consumeOne();
				Expression newValueExpr = parseExpression();
				Expression assignExpr = new AssignExpr(refHeader, newValueExpr, finish(esPos));
				exprStmt = new ExprStmt(assignExpr, finish(esPos));
			}
			else { // no postfix RefExpr Cannot server as a statement
				finish(esPos);
				syntacticError("\"%\" parse ExprStmt failed",
						currentToken.spelling);
			}
			break;
		}

		if (endToken > 0){
			consumeOne(endToken);
		}
		return exprStmt;
	}

	public VarDeclListStmt parseVarDeclListStmt(int endToken) throws SyntaxError{
		VarDeclListStmt vdlStmt = null;//new VarDeclListStmt(t, idList, initList, posn);
		Type typeHead = null; 
		IdentifierList idList = new IdentifierList();
		ExprList exprList = new ExprList();

		SourcePosition vdlPosn = new SourcePosition();
		start(vdlPosn);
		if (currentToken.isVarType()){
			typeHead = parseType();
			Identifier id; //for add
			Expression expr; //for add
			do {
				id = parseIdentifier();
				expr = tryConsumeOne(Token.BECOMES) ? parseExpression() : null;
				idList.add(id);
				exprList.add(expr);
			}
			while (tryConsumeOne(Token.COMMA));
			if (endToken > 0){
				consumeOne(endToken);
			}
		}
		else{
			syntacticError("***\"%\" parse VarDeclListStmt failed",
					currentToken.spelling);
		}
		finish(vdlPosn);
		vdlStmt = new VarDeclListStmt(typeHead, idList, exprList, vdlPosn);
		return vdlStmt;
	}


	public Reference parseReference() throws SyntaxError {
		Reference referenceAST = null;
		SourcePosition refPos = new SourcePosition();
		start(refPos);

		Expression exprToRef = null;
		ExprList argList = null;
		if (currentToken.kind == Token.THIS){
			consumeOne();
			referenceAST = new ThisRef(currentToken.position);
		}
		else if (currentToken.kind == Token.IDENTIFIER){
			Identifier headID = parseIdentifier();
			if (currentToken.kind == Token.LPAREN){
				consumeOne(); 
				argList = new ExprList();
				if (currentToken.kind != Token.RPAREN){
					do argList.add(parseExpression());
					while (tryConsumeOne(Token.COMMA));
				}
				consumeOne(Token.RPAREN);
				referenceAST = new MethodRef(headID, argList, finish(refPos));
			}
			else {
				referenceAST = new SimpleRef(headID, finish(refPos));
			}
		}
		else if (currentToken.kind == Token.NEW){
			exprToRef = parseNewExpr();
			referenceAST = new ExprRef(exprToRef, finish(refPos));
		}
		else if (currentToken.kind == Token.LPAREN){
			exprToRef = parsePrimaryExpression();
			if (exprToRef instanceof RefExpr){
				referenceAST = ((RefExpr)exprToRef).ref ;
			}
			else if (exprToRef instanceof CallExpr){
				referenceAST = ((CallExpr)exprToRef).functionRef;
			}
			else{
				syntacticError("***===Cannot start a valid Reference"  +
						"(" + currentToken.spelling + ")", currentToken.spelling);
			}
		}
		else{
			syntacticError("***===Cannot start a valid Reference(" 
		+ currentToken.spelling + ")" , currentToken.spelling);
		}

		while (currentToken.kind == Token.DOT || 
				currentToken.kind == Token.LBRACKET ){
			referenceAST = stepToNextRef(referenceAST);
		}
		return referenceAST;
	}

	Reference stepToNextRef(Reference preRef) throws SyntaxError{ //culmulative
		Reference referenceAST = preRef;
		SourcePosition refPos = new SourcePosition();
		start(refPos);
		//refPos.start = preRef.posn.start;

		Identifier subID = null;
		ExprList argList = null;
		Expression indexExpr = null;
		boolean isGoIndex = false;
		if (currentToken.kind == Token.DOT ||
				(isGoIndex = currentToken.kind == Token.LBRACKET)){
			consumeOne();
			if (isGoIndex){
				indexExpr = parseExpression();
				consumeOne(Token.RBRACKET);
				referenceAST = new IndexedRef(referenceAST, indexExpr, finish(refPos));
			}
			else {
				subID = parseIdentifier(); //Array LengthRef not applicable
				if (currentToken.kind == Token.LPAREN){
					consumeOne();
					argList = new ExprList();
					if (currentToken.kind != Token.RPAREN){
						do argList.add(parseExpression());
						while (tryConsumeOne(Token.COMMA));
					}
					consumeOne(Token.RPAREN);
					referenceAST = new SubMethodRef(referenceAST, subID, argList, finish(refPos));
				}
				else {
					referenceAST = new SubFieldRef(referenceAST, subID, finish(refPos));
				}
			}
		}
		else{
			syntacticError("***No more to accumulate", currentToken.spelling);
		}
		return referenceAST;
	}

	//	public Reference parseInitialRef(){
	//		Reference referenceAST = null;
	//		SourcePosition refPos = new SourcePosition();
	//		start(refPos);
	//		
	//	}

	//	public QualifiedRef parseQualifiedRef() throws SyntaxError { //abandon
	//		QualifiedRef refAST = null; 
	//		boolean thisRelative = false; 
	//		IdentifierList idList = new IdentifierList();
	//
	//		SourcePosition refPos = new SourcePosition();
	//		start(refPos);
	//
	//		if (tryConsumeOne(Token.THIS)) {
	//			thisRelative = true;
	//		}
	//		else {
	//			thisRelative = false;
	//			idList.add(parseIdentifier());
	//		}
	//
	//		while (tryConsumeOne(Token.DOT)) {
	//			idList.add(parseIdentifier());
	//		}
	//
	//		refAST = new QualifiedRef(thisRelative, idList, finish(refPos));
	//		return refAST;
	//	}

	public IfStmt parseIfStmt() throws SyntaxError {
		IfStmt ifStmtAST = null;
		SourcePosition stmtPos = new SourcePosition();
		start(stmtPos);

		Expression ifCond = null;
		Statement thenStmt = null;
		Statement elseStmt = null;

		consumeOne(Token.IF);
		consumeOne(Token.LPAREN);
		ifCond = parseExpression();
		consumeOne(Token.RPAREN);
		thenStmt = parseStatement();
		if (currentToken.kind == Token.ELSE) {
			consumeOne();
			elseStmt = parseStatement();
		}

		ifStmtAST = new IfStmt(ifCond, thenStmt, elseStmt, finish(stmtPos));
		return ifStmtAST;
	}

	public WhileStmt parseWhileStmt() throws SyntaxError{
		WhileStmt whileStmtAST = null;
		SourcePosition stmtPos = new SourcePosition();
		start(stmtPos);

		Expression whileCond;
		Statement whileBody;

		consumeOne(Token.WHILE);
		consumeOne(Token.LPAREN);
		whileCond = parseExpression();
		consumeOne(Token.RPAREN);
		whileBody = parseStatement();

		whileStmtAST = new WhileStmt(whileCond, whileBody, finish(stmtPos));
		return whileStmtAST;
	}

	public NullStmt parseNullStmt(int endToken) throws SyntaxError{
		SourcePosition nullPons = new SourcePosition();
		start(nullPons);
		if (endToken > 0){
			consumeOne(endToken);
		}
		return new NullStmt(finish(nullPons));
	}

	public ForStmt parseForStmt() throws SyntaxError{
		ForStmt forStmt = null; 
		SourcePosition stmtPos = new SourcePosition();
		start(stmtPos);

		StatementList initList = new StatementList();
		Expression cond = null;
		StatementList update = new StatementList();
		Statement forBody = null;

		consumeOne(); // Token.FOR for
		consumeOne(Token.LPAREN); //for(
		if (currentToken.kind == Token.SEMICOLON){
			initList.add(parseNullStmt(Token.EMPTY));
		}
		else {
			if (currentToken.kind == Token.INT 
					||currentToken.kind == Token.BOOLEAN){ //int, boolean, id
				initList.add(parseVarDeclListStmt(Token.EMPTY));
			}
			else if (currentToken.kind == Token.IDENTIFIER){
				bufferOne();
				if(peekToken.kind == Token.IDENTIFIER){
					initList.add(parseVarDeclListStmt(Token.EMPTY));
				}
				else{
					do initList.add(parseExprStmt(Token.EMPTY));
					while (tryConsumeOne(Token.COMMA));
				}
			}
			else {
				do initList.add(parseExprStmt(Token.EMPTY));
				while(tryConsumeOne(Token.COMMA));
			}
		}
		consumeOne(Token.SEMICOLON); //for(;
		if (currentToken.kind == Token.SEMICOLON){
			BooleanLiteral trueLiteral = new BooleanLiteral("true", currentToken.position);
			LiteralExpr trueExpr = new LiteralExpr(trueLiteral, currentToken.position);
			cond = trueExpr; //(;true;) //if empty 
		}
		else{
			cond = parseExpression();
		}
		consumeOne(Token.SEMICOLON); //for(;;
		if (currentToken.kind == Token.RPAREN){
			update.add(parseNullStmt(Token.EMPTY));
		}
		else {
			do update.add(parseExprStmt(Token.EMPTY));
			while(tryConsumeOne(Token.COMMA));
		}
		consumeOne(Token.RPAREN);//for(;;)
		forBody = parseStatement();

		forStmt = new ForStmt(initList, cond, update, forBody, finish(stmtPos));
		return forStmt;
	}

	public BlockStmt parseBlockStmt() throws SyntaxError{
		BlockStmt blockStmtAST = null;
		SourcePosition stmtPos = new SourcePosition();
		start(stmtPos);

		StatementList stmtList = new StatementList();
		Statement blockStmtElmt = null;

		consumeOne(Token.LCURLY);
		while (currentToken.kind != Token.RCURLY) {
			blockStmtElmt = parseStatement();
			stmtList.add(blockStmtElmt);
		}
		consumeOne(Token.RCURLY);

		blockStmtAST = new BlockStmt(stmtList, finish(stmtPos));
		return blockStmtAST;
	}

	public Statement parseStatement() throws SyntaxError {
		Statement stmtAST = null;
		SourcePosition stmtPos = new SourcePosition();
		start(stmtPos);

		switch (currentToken.kind) {
		case Token.IF:
			stmtAST = parseIfStmt();
			break;

		case Token.WHILE:
			stmtAST = parseWhileStmt();
			break;

		case Token.FOR: // final project added
			stmtAST = parseForStmt();
			break;

		case Token.LCURLY: 
			stmtAST = parseBlockStmt();
			break;

		case Token.INT:
		case Token.BOOLEAN:
		case Token.VOID: // VarDecl;  ???
			stmtAST = parseVarDeclListStmt(Token.SEMICOLON);
			break;

		case Token.SEMICOLON:
			stmtAST = parseNullStmt(Token.SEMICOLON); //final add parse null statment
			break;

		case Token.IDENTIFIER: // may port to varDeclStmt, assignStmt, or callStmt
			bufferOne();
			if (peekToken.kind == Token.IDENTIFIER ){ // id id 
				stmtAST = parseVarDeclListStmt(Token.SEMICOLON);
			}
			else if(peekToken.kind == Token.LBRACKET){
				bufferOne();
				if (peekToken.kind == Token.RBRACKET){
					stmtAST = parseVarDeclListStmt(Token.SEMICOLON);
				}
				else {
					stmtAST = parseExprStmt(Token.SEMICOLON);
				}
			}
			else {
				stmtAST = parseExprStmt(Token.SEMICOLON);
			}
			break; 

		default: //case Token.THIS: case Token.INCREMENT:case Token.DECREMENT: case Token.NEW:
			stmtAST = parseExprStmt(Token.SEMICOLON);
			break; 
		}
		return stmtAST;
	}

	/**
	 * Following are parsing Expression
	 * @return
	 * @throws SyntaxError
	 */
	public Expression parseExpression() throws SyntaxError {
		Expression exprAST = parseAssign();
		return exprAST;
	}

	public Expression parseAssign() throws SyntaxError{
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		exprAST = parseDisj();
		if (currentToken.kind == Token.BECOMES) {
			if (exprAST instanceof RefExpr){
				consumeOne();
				Expression newValueExpr = parseExpression();
				exprAST = new AssignExpr((RefExpr)exprAST, newValueExpr, finish(exprPos));
			}
			else {
				syntacticError("***\"%\" The Left hand side cannot be assigned",
						currentToken.spelling);
			}
		}
		return exprAST;

	}

	public Expression parseDisj() throws SyntaxError {
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		exprAST = parseConj();
		while (currentToken.kind == Token.DISJUNCTION) {
			Operator op = parseOperator();
			Expression exprNext = parseConj();
			exprAST = new BinaryExpr(op, exprAST, exprNext, finish(exprPos));
		}
		return exprAST;
	}

	public Expression parseConj() throws SyntaxError {
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		exprAST = parseEqua();
		while (currentToken.kind == Token.CONJUNCTION) {
			Operator op = parseOperator();
			Expression exprNext = parseEqua();
			exprAST = new BinaryExpr(op, exprAST, exprNext, finish(exprPos));
		}

		return exprAST;
	}

	public Expression parseEqua() throws SyntaxError {
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		exprAST = parseRela(); // next name
		while (currentToken.kind == Token.EQUALITY) { // current name
			Operator op = parseOperator();
			Expression exprNext = parseEqua(); // next name
			exprAST = new BinaryExpr(op, exprAST, exprNext, finish(exprPos));
		}
		return exprAST;
	}

	public Expression parseRela() throws SyntaxError {
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		Operator RelaOp = null;
		Expression exprNext = null;

		boolean isInstanceof = false;

		exprAST = parseAddi(); 
		while (currentToken.kind == Token.RELATIONAL
				|| (isInstanceof = currentToken.kind == Token.INSTANCEOF)) { 
			RelaOp = parseOperator();
			exprNext = (isInstanceof)? 
					new TypeExpr(parseType(), finish(exprPos)):
						parseAddi(); // next name
					exprAST = new BinaryExpr(RelaOp, exprAST, exprNext, finish(exprPos));
		}
		return exprAST;
	}

	public Expression parseAddi() throws SyntaxError {
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		exprAST = parseMult(); // next name
		while (currentToken.kind == Token.ADDITIVE) { // current name
			Operator op = parseOperator();
			Expression exprNext = parseMult(); // next name
			exprAST = new BinaryExpr(op, exprAST, exprNext, finish(exprPos));
		}
		return exprAST;
	}

	public Expression parseMult() throws SyntaxError {
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		exprAST = parseUnary(); // next name
		while (currentToken.kind == Token.MULTIPLICATIVE) { // current
			Operator op = parseOperator();
			Expression exprNext = parseUnary(); // next name
			exprAST = new BinaryExpr(op, exprAST, exprNext, finish(exprPos));
		}
		return exprAST;
	}

	public Expression parseUnary() throws SyntaxError { // pre-Unary and
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		if (currentToken.kind == Token.INCREMENT) {
			consumeOne();
			exprAST = new IncrementExpr(parseReference(), false, finish(exprPos));
		} 
		else if (currentToken.kind == Token.DECREMENT) {
			consumeOne();
			exprAST = new DecrementExpr(parseReference(), false, finish(exprPos));
		} 
		else if (currentToken.isUnary()) { // here is '-' and '!' left
			Operator op = parseOperator();
			Expression exprNext = parseUnary();// next name neither
			exprAST = new UnaryExpr(op, exprNext, finish(exprPos));
		} 
		else {
			exprAST = parsePrimaryExpression(); // not the type of ++(i)
		}
		return exprAST;
	}

	public Expression parsePrimaryExpression() throws SyntaxError {
		Expression exprAST = null;
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		switch (currentToken.kind) {
		case Token.NULL:
			return new LiteralExpr(parseNullLiteral(), finish(exprPos));

		case Token.TRUE:
		case Token.FALSE:
			return new LiteralExpr(parseBooleanLiteral(), finish(exprPos));

		case Token.INTLITERAL:
			return  new LiteralExpr(parseIntLiteral(), finish(exprPos));

		case Token.NEW:
			exprAST = parseNewExpr();
			break;

		case Token.LPAREN:
			consumeOne();
			exprAST = parseExpression();
			consumeOne(Token.RPAREN);
			break;

		default:
			exprAST = new RefExpr(parseReference(), finish(exprPos));
			break;
		}

		boolean isContRef = false;
		Reference enclosedRef = null;
		if (exprAST instanceof RefExpr){ //in case of (RefExpr)
			enclosedRef = ((RefExpr)exprAST).ref;
			while (currentToken.kind == Token.DOT ||
					currentToken.kind == Token.LBRACKET){
				isContRef = true;
				enclosedRef = stepToNextRef(enclosedRef);
			}
			if (currentToken.kind == Token.INCREMENT) { // post-increment
				consumeOne();
				return new IncrementExpr(enclosedRef, true, finish(exprPos));
			} 
			else if (currentToken.kind == Token.DECREMENT) { // post-decrement
				consumeOne();
				return new DecrementExpr(enclosedRef, true, finish(exprPos));
			}
		}
		else if (exprAST instanceof NewExpr){
			enclosedRef = new ExprRef(exprAST);
			while (currentToken.kind == Token.DOT ||
					currentToken.kind == Token.LBRACKET){
				isContRef = true;
				enclosedRef = stepToNextRef(enclosedRef);
			}
		}
		
		if (enclosedRef instanceof MethodRef){
			exprAST = new CallExpr((MethodRef)enclosedRef);
		}
		else if (enclosedRef instanceof SubMethodRef){
			exprAST = new CallExpr((SubMethodRef)enclosedRef);
		}
		else if (isContRef){
			exprAST = new RefExpr(enclosedRef, finish(exprPos));
		}

		return exprAST;
	}

	public Expression parseNewExpr() throws SyntaxError { // new object or new array
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		bufferOne(); //int, consumeOne(Token.NEW);
		bufferOne(); //int, id, so on
		switch(peekToken.kind){
		case Token.LPAREN:
			exprAST = parseNewObjExpr();
			break;

		case Token.LBRACKET:
			exprAST= parseNewArrayExpr();
			break;

		default:
			syntacticError("***Invalid NewExpr, '(' or '[' is expected", currentToken.spelling);
			break;
		}

		return exprAST;
	}

	public Expression parseNewArrayExpr() throws SyntaxError { // new object or new array
		Expression exprAST = null;
		Type eltType = null;
		Expression sizeExpr = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		consumeOne(Token.NEW);
		eltType = parseType();
		consumeOne(Token.LBRACKET);
		sizeExpr = parseExpression();
		consumeOne(Token.RBRACKET);
		exprAST = new NewArrayExpr(eltType, sizeExpr, finish(exprPos));

		return exprAST;
	}

	public NewObjectExpr parseNewObjExpr() throws SyntaxError{
		NewObjectExpr exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		ExprList argList = new ExprList();

		consumeOne(Token.NEW);
		ClassType objectType = new ClassType(parseIdentifier(), finish(exprPos));
		consumeOne(Token.LPAREN);
		if (currentToken.kind != Token.RPAREN) {
			do{
				argList.add(parseExpression());
			}
			while (tryConsumeOne(Token.COMMA));
		}
		consumeOne(Token.RPAREN);
		exprAST = new NewObjectExpr(objectType, argList, finish(exprPos));

		return exprAST;
	}

	public CallExpr parseCallExpr() throws SyntaxError{
		CallExpr callExpr = null;

		Reference parsedRef = parseReference();
		if (parsedRef instanceof MethodRef){
			callExpr = new CallExpr((MethodRef) parsedRef);
		}
		else if (parsedRef instanceof SubMethodRef){
			callExpr = new CallExpr((SubMethodRef) parsedRef);
		}
		else {
			syntacticError("*** parseCallExpr() failed", currentToken.spelling);
		}
		return callExpr;
	}


	public Type parseType() throws SyntaxError { //consume only one id
		Type typeAST = null;

		SourcePosition typePons = new SourcePosition();
		start(typePons);

		switch (currentToken.kind) {
		case Token.VOID:
			consumeOne();
			typeAST = new VoidType(finish(typePons)); //final modified
			break;

		case Token.BOOLEAN:
			consumeOne();
			typeAST = new BaseType(TypeKind.BOOLEAN, finish(typePons));
			break;

		case Token.INT:
			consumeOne();
			typeAST = new BaseType(TypeKind.INT, finish(typePons));
			break;

		case Token.IDENTIFIER:
			Identifier identifierAST = parseIdentifier();
			typeAST = new ClassType(identifierAST, finish(typePons));
			break;

		default:
			syntacticError("Token not expected, parseType()",
					currentToken.spelling);
			break;
		}
		
		if (currentToken.kind == Token.LBRACKET
				&& peekNextToken() == Token.RBRACKET){
			consumeOne();
			consumeOne();
			typeAST = new ArrayType(typeAST, finish(typePons));
		}

		return typeAST;
	}


	/**
	 * Below are the parsing methods of four terminals in miniJava
	 * (1)BooleanLiter (2) IntLiteral (3) Identifier (4) Operator
	 * @return the four corresponding AST (1)(2)(3)(4)
	 * @throws SyntaxError
	 */
	public BooleanLiteral parseBooleanLiteral() throws SyntaxError {
		BooleanLiteral booleanLiteral = null;
		if (currentToken.kind == Token.TRUE) {
			booleanLiteral = new BooleanLiteral("true", currentToken.position);
			consumeOne();
		} 
		else if (currentToken.kind == Token.FALSE) {
			booleanLiteral = new BooleanLiteral("false", currentToken.position);
			consumeOne();
		} 
		else {
			syntacticError("BooleanLiteral literal expected here", " ");
		}
		return booleanLiteral;
	}

	public IntLiteral parseIntLiteral() throws SyntaxError {
		IntLiteral intLiteral = null;
		if (currentToken.kind == Token.INTLITERAL) {
			intLiteral = new IntLiteral(currentToken.spelling, currentToken.position);
			consumeOne();
		} 
		else {
			syntacticError("integer literal expected here", " ");
		}
		return intLiteral;
	}

	public NullLiteral parseNullLiteral() throws SyntaxError{
		NullLiteral nullLiteral = null;
		if (tryConsumeOne(Token.NULL)){
			nullLiteral = new NullLiteral(currentToken.position);
		}
		else {
			syntacticError("null literal expected here", " ");
		}
		return nullLiteral;
	}

	public Identifier parseIdentifier() throws SyntaxError {
		Identifier identifier = null;
		if (currentToken.kind == Token.IDENTIFIER) {
			identifier = new Identifier(currentToken.spelling, currentToken.position);
			consumeOne();
		} else {
			syntacticError("identifier expected here", " ");
		}
		return identifier;
	}

	public Operator parseOperator() throws SyntaxError {
		
		Operator operator = null;
		if (currentToken.isOperator()) {
			operator = new Operator(currentToken.spelling, currentToken.position);
			consumeOne();
		} 
		else {
			syntacticError("operator expected here", " ");
		}
		return operator;
	}
}