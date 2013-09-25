package miniJava.SyntacticAnalyzer;

import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.ErrorReporter;

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
		System.out.println("===========ERROR=================");
		System.exit(4);
		errorReporter.reportError(messageTemplate, tokenQuoted, pos);	
		throw(new SyntaxError());	
	}

	///////////////////////////////////////////////////////////////////////////////
	//
	// PROGRAMS
	//
	///////////////////////////////////////////////////////////////////////////////

	public void parseProgram() throws SyntaxError{
		
		System.out.println("parseProgram()");

		previousTokenPosition.start = 0;
		previousTokenPosition.finish = 0;
		currentToken = lexicalAnalyser.scan();

		try {
			while(currentToken.kind == Token.CLASS){
				parseClassDeclaration();
			}

			if (currentToken.kind != Token.EOT) {
				syntacticError("\"%\" not expected after end of program",
						currentToken.spelling);
			}
		}
		catch (SyntaxError s) { return; }
	}

	public void parseClassDeclaration() throws SyntaxError{
		System.out.println("parseClassDeclaration"); 
		
		accept(Token.CLASS);
		parseIdentifier();
		accept(Token.LCURLY);
		while(currentToken.kind != Token.RCURLY){
			parseDeclarators();
			parseIdentifier();
			switch(currentToken.kind){
				case Token.SEMICOLON: // ';'
					acceptIt();
				break;
				
				case Token.LPAREN:  {
					acceptIt();
					if(currentToken.kind != Token.RPAREN){
						parseParameterList();
					}
					accept(Token.RPAREN);
					accept(Token.LCURLY);
					while(currentToken.kind != Token.RCURLY &&
							currentToken.kind != Token.RETURN){
						parseStatement();
					}
					if(currentToken.kind == Token.RETURN){
						acceptIt();
						parseExpression();
						accept(Token.SEMICOLON);
					}
					accept(Token.RCURLY);
				}
				break;
				
				default: 
					syntacticError("Token not expected, Parse classDeclaration", currentToken.spelling);
					break;
			}
		}		
		accept(Token.RCURLY);
	}
	
	public void parseDeclarators() throws SyntaxError{
		System.out.println("parseDeclarators");
		
		if(currentToken.kind == Token.PUBLIC || currentToken.kind == Token.PRIVATE)
			acceptIt();
		if(currentToken.kind == Token.STATIC )
			acceptIt();
		parseType();
	}
	
	public void parseType() throws SyntaxError{
		System.out.println("parseType");
		switch(currentToken.kind){
		case Token.BOOLEAN: 
			acceptIt();
			break;
		case Token.VOID: 
			acceptIt();
			break;
		case Token.INT:
			acceptIt();
			if(currentToken.kind == Token.LBRACKET){
				acceptIt();
				accept(Token.RBRACKET);	
			}
			break;
		case Token.IDENTIFIER:
			parseIdentifier();
			if(currentToken.kind == Token.LBRACKET){
				acceptIt();
				accept(Token.RBRACKET);	
			}
			break;
		default:
			syntacticError("Token not expected, parseType()", currentToken.spelling);
			break;
		}
	}
	
	public void parseParameterList() throws SyntaxError{
		System.out.println("parseParameterList");
		
		parseType();
		parseIdentifier();
		while(currentToken.kind == Token.COMMA){
			acceptIt();
			parseType();
			parseIdentifier();
		}
	}
	
	public void parseArgumentList() throws SyntaxError{
		System.out.println("parseArgumentList");
		
		parseExpression();
		while(currentToken.kind == Token.COMMA){
			acceptIt();
			parseExpression();
		}
	}
	
	public void parseReference() throws SyntaxError{
		System.out.println("parseReference");
		
		if(currentToken.kind == Token.THIS)
			acceptIt();
		else
			parseIdentifier();
		while(currentToken.kind == Token.DOT){
			acceptIt();
			parseIdentifier();
		}
	}
	
	public void parseStatement() throws SyntaxError{
		System.out.println("parseStatement");
		/*gdfgsfdg8/*ghdfhfg*/
		switch(currentToken.kind){
		case Token.IF:
			acceptIt();
			accept(Token.LPAREN);
			parseExpression();
			accept(Token.RPAREN);
			parseStatement();
			if(currentToken.kind == Token.ELSE){
				acceptIt();
				parseStatement();
			}
			break;
		case Token.WHILE:
			acceptIt();
			accept(Token.LPAREN);
			parseExpression();
			accept(Token.RPAREN);
			parseStatement();
			break;
		case Token.LCURLY: //AcceptIt
			acceptIt();
			while(currentToken.kind != Token.RCURLY){
				parseStatement();
			}
			accept(Token.RCURLY);
			break;
		case Token.IDENTIFIER:
			parseIdentifier();
			if(currentToken.kind == Token.LBRACKET){
				acceptIt();
				if(currentToken.kind == Token.RBRACKET){
					acceptIt();
					parseIdentifier();
					accept(Token.BECOMES);
					parseExpression();
					accept(Token.SEMICOLON);
				}
				else{
					parseExpression();
					accept(Token.RBRACKET);
					accept(Token.BECOMES);
					parseExpression();
					accept(Token.SEMICOLON);
				}
			}
			else if(currentToken.kind == Token.DOT){
				do{
					acceptIt();
					parseIdentifier();
				}
				while(currentToken.kind == Token.DOT); //parsing reference
				//code for [ ( =
				if(currentToken.kind == Token.LPAREN){
					acceptIt();
					if(currentToken.kind != Token.RPAREN){
						parseArgumentList();
					}
					accept(Token.RPAREN);
					accept(Token.SEMICOLON);
				}
				else{
					if(currentToken.kind == Token.LBRACKET){
						acceptIt();
						parseExpression();
						accept(Token.RBRACKET);
					}
					accept(Token.BECOMES);
					parseExpression();
					accept(Token.SEMICOLON);
				}
			}
			else if(currentToken.kind == Token.LPAREN){
				acceptIt();
				if(currentToken.kind != Token.RPAREN){
					parseArgumentList();
				}
				accept(Token.RPAREN);
				accept(Token.SEMICOLON);
			}
			else if(currentToken.kind == Token.BECOMES){
				acceptIt();
				parseExpression();
				accept(Token.SEMICOLON);
			}
			else{
				parseIdentifier();
				accept(Token.BECOMES);
				parseExpression();
				accept(Token.SEMICOLON);
			}
			break;
			
		case Token.THIS:
			acceptIt();
			while(currentToken.kind == Token.DOT){
				acceptIt();
				parseIdentifier();
			}
			if(currentToken.kind == Token.LPAREN){
				acceptIt();
				if(currentToken.kind != Token.RPAREN){
					parseArgumentList();
				}
				accept(Token.RPAREN);
				accept(Token.SEMICOLON);
			}
			else{
				if(currentToken.kind == Token.LBRACKET){
					acceptIt();
					parseExpression();
					accept(Token.RBRACKET);
				}
				accept(Token.BECOMES);
				parseExpression();
				accept(Token.SEMICOLON);
			}
			break;
			
		case Token.INT: case Token.BOOLEAN: case Token.VOID: 
			parseType();
			parseIdentifier();
			accept(Token.BECOMES);
			parseExpression();
			accept(Token.SEMICOLON);
			break;
			
		default:
			syntacticError("No expected token found", currentToken.spelling);
			break;//shouldn't be here
		}	
	}
	
	public void parseExpression() throws SyntaxError{
		System.out.println("parseExpression");
		
		parsePrimaryExpression();
		while(currentToken.kind == Token.OPERATOR){
			parseBinaryOperator(); 
			//parseOperator();
			parsePrimaryExpression();
		}
	}
	
	public void parsePrimaryExpression() throws SyntaxError{
		System.out.println("parsePrimaryExpression");
		
		switch(currentToken.kind){
		case Token.TURE:
			acceptIt();
			break;
		case Token.FALSE:
			acceptIt();
			break;
		case Token.NUM:
			parseNum();
			break;
		case Token.NEW:
			acceptIt();
			if(currentToken.kind == Token.INT){
				acceptIt();
				accept(Token.LBRACKET);
				parseExpression();
				accept(Token.RBRACKET);
			}
			else{
				parseIdentifier();
				if(currentToken.kind == Token.LPAREN){
					acceptIt();
					accept(Token.RPAREN);
				}
				else{
					accept(Token.LBRACKET);
					parseExpression();
					accept(Token.RBRACKET);
				}
			}
			break;
			
		case Token.LPAREN:
			acceptIt();
			parseExpression();
			accept(Token.RPAREN);
			break;
			
		case Token.OPERATOR:
			parseUnitaryOperator();
			parsePrimaryExpression();
			break;
			
		default:
			parseReference();
			if(currentToken.kind == Token.LPAREN){
				acceptIt();
				if(currentToken.kind != Token.RPAREN){
					parseArgumentList();
				}
				accept(Token.RPAREN);
			}
			else{
				if(currentToken.kind == Token.LBRACKET){
					acceptIt();
					parseExpression();
					accept(Token.RBRACKET);
				}
			}
			break;
		}
	}
	
	public void parseIdentifier() throws SyntaxError {
		System.out.println("parseIdentifier");
		
		if (currentToken.kind == Token.IDENTIFIER) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyser.scan();
		} else {
			syntacticError("identifier expected here", currentToken.spelling);
		}
	}
	
	public void parseNum() throws SyntaxError {
		System.out.println("parseNum");
		
		if (currentToken.kind == Token.NUM) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyser.scan();
		} else {
			syntacticError("integer literal expected here", currentToken.spelling);
		}
	}
	
	public void parseOperator() throws SyntaxError {
		System.out.println("parseOperator");
		
		if (currentToken.kind == Token.OPERATOR) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyser.scan();
		} else {
			syntacticError("operator expected here", currentToken.spelling);
		}
	}
	
	public void parseUnitaryOperator() throws SyntaxError{
		System.out.println("parseUnitaryOperator()");

		if (currentToken.kind == Token.OPERATOR &&
				Token.isUnitaryOperator(currentToken.spelling)) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyser.scan();
		} else
			syntacticError("UnitaryOperator expected here", currentToken.spelling);
	}

	public void parseBinaryOperator() throws SyntaxError{
		System.out.println("parseBinaryOperator()");

		if (currentToken.kind == Token.OPERATOR &&
				Token.isBinaryOperator(currentToken.spelling)) {
			previousTokenPosition = currentToken.position;
			currentToken = lexicalAnalyser.scan();
		} else
			syntacticError("BinaryOperator expected here", currentToken.spelling);
	}
}