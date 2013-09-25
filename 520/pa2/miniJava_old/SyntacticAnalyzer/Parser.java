/* Parser written by Duo Zhao*/
package miniJava.SyntacticAnalyzer;

import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package; //explicitly import to avoid ambiguity 

public class Parser {

	private Scanner lexicalAnalyser;
	private ErrorReporter errorReporter;
	private Token currentToken;
	private SourcePosition previousTokenPosition;

	public Parser(Scanner lexer, ErrorReporter reporter) {
		lexicalAnalyser = lexer;
		errorReporter = reporter;
		previousTokenPosition = new SourcePosition();
	}

	// accept checks whether the current token matches tokenExpected.
	// If so, fetches the next token.
	// If not, reports a syntactic error.

	void accept (int tokenExpected) throws SyntaxError {
		if (currentToken.kind == tokenExpected) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyser.scan();
		} else {
			syntacticError("\"%\" expected here", Token.spell(tokenExpected));
		}
	}

	void acceptIt() {
		previousTokenPosition = currentToken.position;
		currentToken = lexicalAnalyser.scan();
	}

	// start records the position of the start of a phrase.
	// This is defined to be the position of the first
	// character of the first token of the phrase.

	void start(SourcePosition position) {
		position.start = currentToken.position.start;
	}

	// finish records the position of the end of a phrase.
	// This is defined to be the position of the last
	// character of the last token of the phrase.

	void finish(SourcePosition position) {
		position.finish = previousTokenPosition.finish;
	}

	void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
		SourcePosition pos = currentToken.position;
		System.out.println("===========ERROR: System.exit(4);=================");	
		errorReporter.reportError(messageTemplate, tokenQuoted, pos);		
//		System.exit(4);
		throw(new SyntaxError());	
	}

	///////////////////////////////////////////////////////////////////////////////
	//
	// PROGRAMS
	//
	///////////////////////////////////////////////////////////////////////////////

	public Package parsePackage(){
		//return root node, a package type. 
		//from parseProgram to parsePackage ? (changed)
		
		Package packageAST = null;  //root node
		ClassDeclList classDeclListAST = new ClassDeclList();
		ClassDecl classDeclAST = null;
		//Package packageAST2 = new Package(); is undefined.
		
		System.out.println("parsePackage()");

		previousTokenPosition.start = 0;
		previousTokenPosition.finish = 0;
		currentToken = lexicalAnalyser.scan();

		try {
			while(currentToken.kind == Token.CLASS){
				classDeclAST = parseClassDeclaration();
				classDeclListAST.add(classDeclAST); 
			}
			
			packageAST = new Package(classDeclListAST, previousTokenPosition);
			//why not start(), finish() ?
			if (currentToken.kind != Token.EOT) {
				syntacticError("\"%\" not expected after end of program",
						currentToken.spelling);
			}
		}
		catch (SyntaxError s) { return null; }
		return packageAST;
		
	}
	

//	public ClassDeclList parseClassDeclList() throws SyntaxError{
//		ClassDeclList classDeclListAST = new ClassDeclList(); 
//		//cannot be initialized as null
//		
//		while(currentToken.kind == Token.CLASS){
//			ClassDecl classDeclAST = parseClassDeclaration();
//			classDeclListAST.add(classDeclAST); 
//			//new equivalent part
//		}
//		//classDeclListAST = new ClassDeclList()// no source position?	
//		return classDeclListAST;
//	}

	public ClassDecl parseClassDeclaration() throws SyntaxError{
		ClassDecl classDeclAST = null;
		
		Identifier idAST = null;
		FieldDeclList fieldDeclListAST = new FieldDeclList();
		MethodDeclList methodDeclListAST = new MethodDeclList();
		
		FieldDecl fieldDecl = null; //for temp
		MethodDecl methodDecl = null; //for temp
		MemberDecl memberDecl = null;
		
		boolean isPrivate = true; //default value?
		boolean isStatic = false; //default value
		Type typeDecltor = null;
		Identifier idDecltor = null;
		
		ParameterDeclList parameterDeclList = new ParameterDeclList();
		StatementList statementList = new StatementList();
		Statement statComponent = null;
		Expression returnExp = null;
		
		SourcePosition classDeclarationPos = new SourcePosition();
		start(classDeclarationPos);
		
		accept(Token.CLASS);
		idAST = parseIdentifier();
		accept(Token.LCURLY);
		while(currentToken.kind != Token.RCURLY){
			{
				//parseDeclarators();
				isPrivate = true; //default value?
				isStatic = false; //default value.
				typeDecltor = null;
				idDecltor = null;
				
				parameterDeclList = new ParameterDeclList();
				statementList = new StatementList();
				statComponent = null;
				returnExp = null;
								
				switch (currentToken.kind){
				case Token.PUBLIC:
					acceptIt();
					isPrivate = false;
					break;
				case Token.PRIVATE:
					acceptIt();
					isPrivate = true;
					break;
				default:
					break;
				}
								
				if (currentToken.kind == Token.STATIC){
					acceptIt();
					isStatic = true;
				}
				
				typeDecltor = parseType();
				
				idDecltor = parseIdentifier();
				
				memberDecl = new FieldDecl(isPrivate, isStatic, typeDecltor, idDecltor, classDeclarationPos);
			
			}
			switch(currentToken.kind){
				case Token.SEMICOLON: // ';' parsing filedDecl
					acceptIt();
					finish(classDeclarationPos);
					fieldDecl = new FieldDecl(isPrivate, isStatic, typeDecltor, idDecltor, classDeclarationPos);
					fieldDeclListAST.add(fieldDecl);
					break;
				
				case Token.LPAREN:  {
					acceptIt();
					if(currentToken.kind != Token.RPAREN){
						parameterDeclList = parseParameterList();
					}
					accept(Token.RPAREN);
					accept(Token.LCURLY);
					while(currentToken.kind != Token.RCURLY &&
							currentToken.kind != Token.RETURN){
						statComponent = parseStatement();
						statementList.add(statComponent);
					}
					if(currentToken.kind == Token.RETURN){
						acceptIt();
						returnExp = parseExpression();
						accept(Token.SEMICOLON);
					}
					accept(Token.RCURLY);
					
					methodDecl = new MethodDecl(memberDecl, parameterDeclList, statementList, returnExp, classDeclarationPos);
					methodDeclListAST.add(methodDecl);
				}
				break;
				
				default: 
					syntacticError("Token not expected, Parse classDeclaration", currentToken.spelling);
					break;
			}
		}		
		accept(Token.RCURLY);
		finish(classDeclarationPos);
		classDeclAST = new ClassDecl(idAST, fieldDeclListAST, methodDeclListAST, classDeclarationPos);
		
		return classDeclAST;
	}
	
//	public void parseDeclarators() throws SyntaxError{
//		System.out.println("parseDeclarators");
//		
//		if(currentToken.kind == Token.PUBLIC || currentToken.kind == Token.PRIVATE)
//			acceptIt();
//		if(currentToken.kind == Token.STATIC )
//			acceptIt();
//		parseType();
//	} //need to integrate back. 
	
	
	
	public ParameterDeclList parseParameterList() throws SyntaxError{
		ParameterDeclList parameterDeclList = new ParameterDeclList();
		
		Type typeAST = null;
		Identifier idAST = null;
		ParameterDecl parameterDecl = null;
		
		SourcePosition parseParameterListPos = new SourcePosition();
		start(parseParameterListPos);
		
		System.out.println("parseParameterList");
		
		typeAST = parseType();
		idAST = parseIdentifier();
		finish(parseParameterListPos);
		parameterDecl = new ParameterDecl(typeAST, idAST, parseParameterListPos);
		parameterDeclList.add(parameterDecl);
		
		while(currentToken.kind == Token.COMMA){
			acceptIt();
			
			typeAST = parseType();
			idAST = parseIdentifier();
			finish(parseParameterListPos);
			parameterDecl = new ParameterDecl(typeAST, idAST, parseParameterListPos);
			parameterDeclList.add(parameterDecl);
		}
		
		return parameterDeclList;
	}
	
	public ExprList parseArgumentList() throws SyntaxError{
		ExprList exprListAST = new ExprList();
		
		Expression exprAST = null;
		
		SourcePosition parseArgumentListPos = new SourcePosition();
		start(parseArgumentListPos);
		
		System.out.println("parseArgumentList");
		
		exprAST = parseExpression();
		finish(parseArgumentListPos);
		exprListAST.add(exprAST);
		
		while(currentToken.kind == Token.COMMA){
			acceptIt();
			
			exprAST = parseExpression();
			finish(parseArgumentListPos);
			exprListAST.add(exprAST);
		}
		
		return exprListAST;
	}
	
	public Reference parseReference() throws SyntaxError{
		Reference referenceAST = null;
		
		SourcePosition referencePos = new SourcePosition();
	    start(referencePos);
	    
		referenceAST = parseQualifiedRef();
		
		if(currentToken.kind == Token.LBRACKET){ //parsing Indexded reference
			acceptIt();
			Expression exprAST = parseExpression();
			accept(Token.RBRACKET);
			finish(referencePos);
			referenceAST = new IndexedRef(referenceAST, exprAST, referencePos);
		}
		
		return referenceAST;
	}
	
	public QualifiedRef parseQualifiedRef() throws SyntaxError{
		
		QualifiedRef refAST = null; //for return tracking		
		boolean thisRelative = false; //to be determined
		Identifier id = null; //for temporary use assignment
		IdentifierList idList = new IdentifierList(); //null?
				
		SourcePosition referencePos = new SourcePosition();
	    start(referencePos);
		
		System.out.println("parseQualifiedRef(): ");
		
		if(currentToken.kind == Token.THIS){
			thisRelative = true;
			acceptIt();		
		}
		else{
			thisRelative = false;
			id = parseIdentifier();
			idList.add(id);
		}
		
		while(currentToken.kind == Token.DOT){
			acceptIt();
			id = parseIdentifier();
			idList.add(id);
		}
		finish(referencePos);
		refAST = new QualifiedRef(thisRelative, idList, referencePos);
		
		return refAST;
	}
	
	public Statement parseStatement() throws SyntaxError{
		System.out.println("parseStatement");

		Statement stmtAST = null;
		
		AssignStmt assignStmtAST = null;
		BlockStmt blockStmtAST = null;
		CallStmt callStmtAST = null;
		IfStmt ifStmtAST = null;
		VarDeclStmt varDeclStmtAST = null;
		WhileStmt whileStmtAST = null;
		
		//Expression expr = null;
		//Statement stat = null;
		
		SourcePosition stmtPos = new SourcePosition();
	    start(stmtPos);
		
		
		switch(currentToken.kind){
		case Token.IF:
			Expression ifCond = null;
			Statement thenStmt = null;
			Statement elseStmt = null;
			
			acceptIt();
			accept(Token.LPAREN);
			ifCond = parseExpression();
			accept(Token.RPAREN);
			thenStmt = parseStatement();
			if(currentToken.kind == Token.ELSE){
				acceptIt();
				elseStmt = parseStatement();
			}
			
			finish(stmtPos);
			ifStmtAST = new IfStmt(ifCond, thenStmt, elseStmt, stmtPos);
			stmtAST = ifStmtAST;
			break;
			
		case Token.WHILE:
			Expression whileCond;
			Statement whileBody;
			
			acceptIt();
			accept(Token.LPAREN);
			whileCond = parseExpression();
			accept(Token.RPAREN);
			whileBody = parseStatement();
			
			finish(stmtPos);
			whileStmtAST = new WhileStmt(whileCond, whileBody, stmtPos);
			stmtAST = whileStmtAST;
			break;
			
		case Token.LCURLY: //AcceptIt Block statment
			StatementList stmtList = new StatementList();
			Statement blockStmtElmt = null;
			
			acceptIt();
			while(currentToken.kind != Token.RCURLY){
				blockStmtElmt = parseStatement();
				stmtList.add(blockStmtElmt);
			}
			
			accept(Token.RCURLY);
			finish(stmtPos);
			blockStmtAST = new BlockStmt(stmtList, stmtPos);	
			stmtAST = blockStmtAST;
			break;
			
			//************ from here *********
		case Token.IDENTIFIER:
			//may port to varDeclStmt, assignStmt, or callStmt
			Identifier idHead = parseIdentifier(); //candidate for Type or reference. 
			
			if(currentToken.kind == Token.LBRACKET){ // assignStmt
				acceptIt();
				if(currentToken.kind == Token.RBRACKET){ // The Type form: id[] ArrayType
					//Here is VarDeclStmt
					VarDecl varDeclAST;
					Expression initExp;
					
					Type typeInVarDecl = null;
					Identifier idInVarDecl = null;
					
					acceptIt(); //right bracket 
					finish(stmtPos);
					typeInVarDecl = new ArrayType(new ClassType(idHead, stmtPos), stmtPos);
					
					idInVarDecl = parseIdentifier();
					finish(stmtPos); //may not be necessary
					varDeclAST = new VarDecl(typeInVarDecl, idInVarDecl, stmtPos);
					
					accept(Token.BECOMES);
					initExp = parseExpression();
					accept(Token.SEMICOLON);
					
					finish(stmtPos);
					varDeclStmtAST = new VarDeclStmt(varDeclAST, initExp, stmtPos);
					stmtAST = varDeclStmtAST;
				}
				else{ //Here is AssignmentStat IndexedRef
					Reference refInAssignStat; //supposed to an indexed reference
				    Expression exprInAssignStat;
				    
				    Reference refAtIndexedRef;
				    Expression exprAtIndexedRef;
				    
				    finish(stmtPos);
				    refAtIndexedRef = new QualifiedRef(idHead);
				    exprAtIndexedRef = parseExpression();
					accept(Token.RBRACKET);
					
					finish(stmtPos);
					refInAssignStat = new IndexedRef(refAtIndexedRef, exprAtIndexedRef, stmtPos);
					
					accept(Token.BECOMES);
					exprInAssignStat = parseExpression();
					accept(Token.SEMICOLON);
					
					finish(stmtPos);
					assignStmtAST = new AssignStmt(refInAssignStat, exprInAssignStat, stmtPos);
					stmtAST = assignStmtAST;
				}
			}
			else if(currentToken.kind == Token.DOT){ //Reference Qualified AssignStat or CallStat
				Identifier idInIdList;
				IdentifierList idListInRef = new IdentifierList();
				finish(stmtPos);
				QualifiedRef ref = new QualifiedRef(false, idListInRef, stmtPos);
				
				idListInRef.add(idHead);
				do{
					acceptIt();
					idInIdList = parseIdentifier();
					idListInRef.add(idInIdList);
				}
				while(currentToken.kind == Token.DOT); //parsing reference id.id.id.id 
				//code for [ ( =
				if(currentToken.kind == Token.LPAREN){ // CallStat
					Reference methodRefInCallStat = ref; //may not needed
				    ExprList argListInCallStat = new ExprList(); //may be null
					
					acceptIt();
					if(currentToken.kind != Token.RPAREN){
						argListInCallStat = parseArgumentList();
					}
					accept(Token.RPAREN);
					accept(Token.SEMICOLON);
					
					finish(stmtPos);
					callStmtAST = new CallStmt(methodRefInCallStat, argListInCallStat, stmtPos);
					stmtAST = callStmtAST;
				}
				else{ //AssignStat id.id.id.id = 8; id.id.id.id [expr] = 8;
					Reference refInAssignStat = ref; //supposed to an indexed reference
				    Expression exprInAssignStat;
				    								
					if(currentToken.kind == Token.LBRACKET){ // id.id.id.id [expr] = 8;
						Reference refAtIndexedRef = ref;
						Expression exprAtIndexedRef;
						
						acceptIt();
						exprAtIndexedRef = parseExpression();
						accept(Token.RBRACKET);
						
						finish(stmtPos);
						refInAssignStat = new IndexedRef(refAtIndexedRef, exprAtIndexedRef, stmtPos);
					}
					accept(Token.BECOMES);
					exprInAssignStat = parseExpression();
					accept(Token.SEMICOLON);
					finish(stmtPos);
					assignStmtAST = new AssignStmt(refInAssignStat, exprInAssignStat, stmtPos);
					stmtAST = assignStmtAST;
				}
			}
			else if(currentToken.kind == Token.LPAREN){ //CallStat id ()
				Reference methodRefInCall = new QualifiedRef(idHead);
				ExprList argListInCall = new ExprList();

				acceptIt();
				if(currentToken.kind != Token.RPAREN){
					argListInCall = parseArgumentList();
				}
				accept(Token.RPAREN);
				accept(Token.SEMICOLON);
				
				finish(stmtPos);
				callStmtAST = new CallStmt(methodRefInCall, argListInCall, stmtPos);
				stmtAST = callStmtAST;
			}
			else if(currentToken.kind == Token.BECOMES){ //AssignStat
				Reference refInAssignStat = new QualifiedRef(idHead); //supposed to an indexed reference
			    Expression exprInAssignStat;
				
				acceptIt();
				exprInAssignStat = parseExpression();
				accept(Token.SEMICOLON);
				
				finish(stmtPos);
				assignStmtAST = new AssignStmt(refInAssignStat, exprInAssignStat, stmtPos);
				stmtAST = assignStmtAST;
			}
			else{ // id id = expr //VarDeclStat 
				VarDecl varDeclAST;
				Expression initExp;
				
				finish(stmtPos);
				Type typeInVarDecl = new ClassType(idHead, stmtPos);
				Identifier idInVarDecl = parseIdentifier();
				varDeclAST = new VarDecl(typeInVarDecl, idInVarDecl, stmtPos);
				
				accept(Token.BECOMES);
				initExp = parseExpression();
				accept(Token.SEMICOLON);
				
				finish(stmtPos);
				varDeclStmtAST = new VarDeclStmt(varDeclAST, initExp, stmtPos);
				stmtAST = varDeclStmtAST;
			}
			break;
			
		case Token.THIS:
			//Reference thisRef;
			QualifiedRef thisQualifiedRef;
			IdentifierList idListInThisRef = new IdentifierList();
			Identifier idTraversal;
			
			acceptIt();
			while(currentToken.kind == Token.DOT){
				acceptIt();
				idTraversal = parseIdentifier();
				idListInThisRef.add(idTraversal);
			}// get this.id.id.id
			
			finish(stmtPos);
			thisQualifiedRef = new QualifiedRef(true, idListInThisRef, stmtPos);
			
			if(currentToken.kind == Token.LPAREN){ //CallStat
				Reference methodRefInCall = thisQualifiedRef;
				ExprList argListInCall = new ExprList();
				
				acceptIt();
				if(currentToken.kind != Token.RPAREN){
					argListInCall = parseArgumentList();
				}
				accept(Token.RPAREN);
				accept(Token.SEMICOLON);
				
				finish(stmtPos);
				callStmtAST = new CallStmt(methodRefInCall, argListInCall, stmtPos);
				stmtAST = callStmtAST;
			}
			else{//Assign
				Reference refInAssignStat = thisQualifiedRef; //supposed to an indexed reference
			    Expression exprInAssignStat;
				
				if(currentToken.kind == Token.LBRACKET){
					Reference refAtIndexedRef = thisQualifiedRef;
				    Expression exprAtIndexedRef;
					
					acceptIt();
					exprAtIndexedRef = parseExpression();
					accept(Token.RBRACKET);
					
					finish(stmtPos);
					refInAssignStat = new IndexedRef(refAtIndexedRef, exprAtIndexedRef, stmtPos);
				}
				accept(Token.BECOMES);
				exprInAssignStat = parseExpression();
				accept(Token.SEMICOLON);
				
				finish(stmtPos);
				assignStmtAST = new AssignStmt(refInAssignStat, exprInAssignStat, stmtPos);
				stmtAST = assignStmtAST;
			}
			break;
			
		case Token.INT: case Token.BOOLEAN: case Token.VOID: 
			//VarDecl;
			VarDecl varDeclAST;		
			Type typeInVarDecl = parseType();
			Identifier idInVarDecl = parseIdentifier();
			finish(stmtPos);
			varDeclAST = new VarDecl(typeInVarDecl, idInVarDecl, stmtPos);
			accept(Token.BECOMES);
			Expression exprInvarDeclStmt = parseExpression();
			accept(Token.SEMICOLON);
			finish(stmtPos);
			varDeclStmtAST = new VarDeclStmt(varDeclAST, exprInvarDeclStmt, stmtPos);
			stmtAST = varDeclStmtAST;
			break;
			
		default:
			syntacticError("No expected token found", currentToken.spelling);
			break;//shouldn't be here
		}	
		
		return stmtAST;
	}
	
	public Expression parseExpression() throws SyntaxError{
//		Expression exprAST = null;
//		
//		SourcePosition exprPos = new SourcePosition();
//	    start(exprPos);
//		
//		System.out.println("parseExpression");
//		
//		exprAST = parseDisj();
//		
//		while(currentToken.kind == Token.DISJUNCTION){
//			Operator op = parseOperator(); 
//			Expression exprNext = parseDisj();	
//			finish(exprPos);
//			exprAST = new BinaryExpr(op, exprAST, exprNext, exprPos);
//		}
//		
//		return exprAST;
		
		return parseDisj();
	}
	
	public Expression parseDisj() throws SyntaxError{
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		System.out.println("parseDisj");

		exprAST = parseConj();
		while(currentToken.kind == Token.DISJUNCTION){
			Operator op = parseOperator(); 
			Expression exprNext = parseConj();	
			finish(exprPos);
			exprAST = new BinaryExpr(op, exprAST, exprNext, exprPos);
		}
		return exprAST;	
	}
	
	public Expression parseConj() throws SyntaxError{
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		System.out.println("parseConj");

		exprAST = parseEqua();
		while(currentToken.kind == Token.CONJUNCTION){
			Operator op = parseOperator(); 
			Expression exprNext = parseEqua();	
			finish(exprPos);
			exprAST = new BinaryExpr(op, exprAST, exprNext, exprPos);
		}
		return exprAST;		
	}
	
	public Expression parseEqua() throws SyntaxError{
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		System.out.println("parseEqua"); //current name

		exprAST = parseRela(); //next name
		while(currentToken.kind == Token.EQUALITY){ //current name
			Operator op = parseOperator(); 
			Expression exprNext = parseEqua();	//next name
			finish(exprPos);
			exprAST = new BinaryExpr(op, exprAST, exprNext, exprPos);
		}
		return exprAST;		
	}
	
	public Expression parseRela() throws SyntaxError{
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		System.out.println("parseRela"); //current name

		exprAST = parseAddi(); //next name
		while(currentToken.kind == Token.RELATIONAL){ //current name
			Operator op = parseOperator(); 
			Expression exprNext = parseAddi();	//next name
			finish(exprPos);
			exprAST = new BinaryExpr(op, exprAST, exprNext, exprPos);
		}
		return exprAST;			
	}
	
	public Expression parseAddi() throws SyntaxError{
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		System.out.println("parseAddi"); //current name

		exprAST = parseMult(); //next name
		while(currentToken.kind == Token.ADDITIVE){ //current name
			Operator op = parseOperator(); 
			Expression exprNext = parseMult();	//next name
			finish(exprPos);
			exprAST = new BinaryExpr(op, exprAST, exprNext, exprPos);
		}
		return exprAST;		
	}
	
	public Expression parseMult() throws SyntaxError{
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		System.out.println("parseMult"); //current name

		exprAST = parseUnary(); //next name
		while(currentToken.kind == Token.MULTIPLICATIVE){ //current
			Operator op = parseOperator(); 
			Expression exprNext = parseUnary();	//next name
			finish(exprPos);
			exprAST = new BinaryExpr(op, exprAST, exprNext, exprPos);
		}
		return exprAST;		
	}
	
	public Expression parseUnary() throws SyntaxError{
		Expression exprAST = null;

		SourcePosition exprPos = new SourcePosition();
		start(exprPos);

		System.out.println("parseUnary"); //current name

		if(currentToken.isUnary()){ //next name
			Operator op = parseOperator(); 
			Expression exprNext = parseExpression();	//next name
			finish(exprPos);
			exprAST = new UnaryExpr(op, exprNext, exprPos);
		}
		else{
			exprAST = parsePrimaryExpression();
		}
		return exprAST;		
	}
	
	
	public Expression parsePrimaryExpression() throws SyntaxError{
		Expression exprAST = null;
		
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);
		
		System.out.println("parsePrimaryExpression");
		
		switch(currentToken.kind){
		case Token.TRUE: case Token.FALSE:
			finish(exprPos);
			exprAST = new LiteralExpr(parseBooleanLiteral(), exprPos);
			break;
		
		case Token.NUM:
			finish(exprPos);
			exprAST = new LiteralExpr(parseNum(), exprPos);
			break;
			
		case Token.NEW:
			exprAST = parseNewExpr();
			break;
			
		case Token.LPAREN:
			acceptIt();
			exprAST = parseExpression();
			accept(Token.RPAREN);
			break;
			
//		case Token.OPERATOR:
//			parseOperator();
//			parsePrimaryExpression();
//			break;
			
		default:
			QualifiedRef refInExpr = parseQualifiedRef();
			if(currentToken.kind == Token.LPAREN){ //CallExpr
				acceptIt();
				ExprList exprlistInExpr = new ExprList();
				if(currentToken.kind != Token.RPAREN){
					exprlistInExpr = parseArgumentList();
				}
				accept(Token.RPAREN);
				
				finish(exprPos);
				exprAST =  new CallExpr(refInExpr, exprlistInExpr, exprPos);
			}
			else if(currentToken.kind == Token.LBRACKET){ //another indexedRef
				acceptIt();
				Expression exprInRefExpr = parseExpression();
				accept(Token.RBRACKET);
				
				finish(exprPos);
				exprAST = new RefExpr(new IndexedRef(refInExpr, exprInRefExpr, exprPos), exprPos);
			}
			else{
				System.out.println("===Next Token is expected ===");
				exprAST = new RefExpr(refInExpr, exprPos);
				//syntacticError("No expected token found", currentToken.spelling);
				//shouldn't be here ????
			}
			break;
		}
		
		return exprAST;
	}
	
	public Expression parseNewExpr() throws SyntaxError{	//new ...
		Expression exprAST = null;
		
		SourcePosition exprPos = new SourcePosition();
		start(exprPos);
		
		accept(Token.NEW);
		if(currentToken.kind == Token.INT){ //new array expression			
			//acceptIt();// change to parseType
			////Type typeInNewArrayExpr = parseType(); not OK for int[]
			acceptIt();
			finish(exprPos);
			Type typeInNewArrayExpr = new BaseType(TypeKind.INT, exprPos);
			accept(Token.LBRACKET);
			Expression exprInNewArrayExpr = parseExpression();
			accept(Token.RBRACKET);
			
			finish(exprPos);
			exprAST = new NewArrayExpr(typeInNewArrayExpr, exprInNewArrayExpr, exprPos);
		}
		else{
			Identifier idInNew = parseIdentifier();
			ClassType classTypeOfid = new ClassType(idInNew, exprPos);
			
			if(currentToken.kind == Token.LPAREN){ //newObjectExpr
				acceptIt();
				accept(Token.RPAREN);
				
				finish(exprPos);
				exprAST = new NewObjectExpr(classTypeOfid, exprPos);
			}
			else{ //new Array type
				accept(Token.LBRACKET);
				Expression ExprInNewArrayType = parseExpression();
				accept(Token.RBRACKET);
				
				finish(exprPos);
				exprAST = new NewArrayExpr(classTypeOfid, ExprInNewArrayType, exprPos);
			}
		}
		
		return exprAST;
	}

	
	
/*********Terminal parsing methods starting here ***********/	
	public Identifier parseIdentifier() throws SyntaxError {
		Identifier identifier = null;
		
		System.out.println("parseIdentifier");
		
		if (currentToken.kind == Token.IDENTIFIER) {
			previousTokenPosition = currentToken.position;
			String spelling = currentToken.spelling;
			identifier = new Identifier(spelling, previousTokenPosition);
			currentToken = lexicalAnalyser.scan();
		} else {
			identifier = null;
			syntacticError("identifier expected here", currentToken.spelling);
		}
		
		return identifier;
	}
	
	public BooleanLiteral parseBooleanLiteral() throws SyntaxError{
		BooleanLiteral booleanLiteral = null;
		
		System.out.println("parseBooleanLiteral - BooleanLiteral for now");

		if (currentToken.kind == Token.TRUE||
				currentToken.kind == Token.FALSE) {
			previousTokenPosition = currentToken.position;
			String spelling = currentToken.spelling;
			booleanLiteral = new BooleanLiteral(spelling, previousTokenPosition);
			currentToken = lexicalAnalyser.scan();
		} else {
			booleanLiteral = null;
			syntacticError("integer literal expected here", currentToken.spelling);
		}

		return booleanLiteral;
	}
	
	public IntLiteral parseNum() throws SyntaxError {
		IntLiteral intLiteral = null;
		
		System.out.println("parseNum - IntLiteral for now");
		
		if (currentToken.kind == Token.NUM) {
			previousTokenPosition = currentToken.position;
			String spelling = currentToken.spelling;
			intLiteral = new IntLiteral(spelling, previousTokenPosition);
			currentToken = lexicalAnalyser.scan();
		} else {
			intLiteral = null;
			syntacticError("integer literal expected here", currentToken.spelling);
		}
		
		return intLiteral;
	}
	
	public Operator parseOperator() throws SyntaxError {
		Operator operator = null;
		
		System.out.println("parseOperator");
		
		if (currentToken.kind == Token.OPERATOR || //reserved for future operator
				currentToken.kind == Token.DISJUNCTION ||
				currentToken.kind == Token.CONJUNCTION ||
				currentToken.kind == Token.EQUALITY ||
				currentToken.kind == Token.RELATIONAL ||
				currentToken.kind == Token.ADDITIVE ||
				currentToken.kind == Token.MULTIPLICATIVE ||
				currentToken.kind == Token.UNARY) {
			previousTokenPosition = currentToken.position;
			String spelling = currentToken.spelling;
			operator = new Operator(spelling, previousTokenPosition);
			currentToken = lexicalAnalyser.scan();
		} else {
			operator = null;
			syntacticError("operator expected here", currentToken.spelling);
		}
		
		return operator;
	}

	
	public Type parseType() throws SyntaxError{
		//cannot parse an abstract type. 
		Type typeAST = null;
		SourcePosition typePos = new SourcePosition();
		
		System.out.println("parseType");
		
		start(typePos);
		
		switch(currentToken.kind){
		
		case Token.BOOLEAN: 
			acceptIt();
			finish(typePos);
			typeAST = new BaseType(TypeKind.BOOLEAN, typePos);
			break;
		case Token.VOID: 
			acceptIt();
			finish(typePos);
			typeAST = new BaseType(TypeKind.VOID, typePos);
			break;
		case Token.INT:
			acceptIt();
			finish(typePos);
			BaseType baseAST = new BaseType(TypeKind.INT, typePos);
			if(currentToken.kind == Token.LBRACKET){
				acceptIt();
				accept(Token.RBRACKET);	
				finish(typePos);
				typeAST = new ArrayType(baseAST, typePos);
			}else{
				typeAST = baseAST;
			}
			break;
			
		case Token.IDENTIFIER:
			Identifier identifierAST = parseIdentifier();
			finish(typePos);
			ClassType classTypeAST = new ClassType(identifierAST, typePos);
			if(currentToken.kind == Token.LBRACKET){
				acceptIt();
				accept(Token.RBRACKET);
				finish(typePos);
				typeAST = new ArrayType(classTypeAST, typePos);
			}else{
				typeAST = classTypeAST;
			}
			break;
			
		default:
			syntacticError("Token not expected, parseType()", currentToken.spelling);
			break;
		}
		
		return typeAST;
	}
	
	public ArrayType parseArrayType() throws SyntaxError{
		ArrayType arrayType = null;
		
		return arrayType;
	}
	
}