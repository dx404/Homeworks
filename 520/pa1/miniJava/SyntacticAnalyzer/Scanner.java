package miniJava.SyntacticAnalyzer;

import miniJava.SyntacticAnalyzer.SourceFile;
import miniJava.SyntacticAnalyzer.Token;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.ErrorReporter;

public final class Scanner {

	private SourceFile sourceFile;
	private boolean debug;

	private char currentChar;
	private StringBuffer currentSpelling;
	private boolean currentlyScanningToken;

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	private boolean isDigit(char c) {
		return (c >= '0' && c <= '9');
	}

	public Scanner(SourceFile source) {
		sourceFile = source;
		currentChar = sourceFile.getSource();
		debug = false;
	}

	public void enableDebugging() {
		debug = true;
	}


	private void takeIt() { //updating current character
		if (currentlyScanningToken)
			currentSpelling.append(currentChar);
		currentChar = sourceFile.getSource();
	}

	private int scanSeparator() {
		//int flag = -1; 
		/*1 for scan success, 0 for general failure, 
		2 for line comments
		3 for block comments
		4 for other while space
		-1 for '/' token.
		*/
		switch (currentChar) {
		case '/':{
			takeIt();
			switch(currentChar){
			case '/': { //Line Comments
				do takeIt();
				while(currentChar != '\n' && currentChar != '\u0000');
			}
			return 2;

			case '*': { //Block Commnets
				takeIt();
				while (true){
					if(currentChar == '\u0000'){
						System.out.println("===========ERROR=================");
						System.exit(4);
						return -2;
					}
					if(currentChar != '*'){
						takeIt();
						continue;
					}
					takeIt();
					if(currentChar == '/'){
						takeIt();
						break;
					}
						
				}
			}
			return 3;

			default:  //Token scanned instead
				return -1;
			}
		}
		
		case ' ': case '\n': case '\r': case '\t':{
			do{
				takeIt();
			}
			while(currentChar == ' ' ||
					currentChar == '\n' ||
					currentChar == '\r' ||
					currentChar == '\t');
			return 4;
		}
		default: 
			return 0;
		}
	}
	
	private int scanToken() {
		
		if(isLetter(currentChar)){
			takeIt();
			while (isLetter(currentChar) || 
					isDigit(currentChar) || 
					currentChar == '_')
				takeIt();
			//currentSpelling matches keywords. 
			return Token.IDENTIFIER;
		}
		
		if(isDigit(currentChar)){
			takeIt();
			while (isDigit(currentChar))
				takeIt();
			return Token.NUM;
		}
		
		switch (currentChar) {

		case '+':  case '-':  case '*':  case '/':
			takeIt();
			return Token.OPERATOR;
			
		case '>':  case '<':  case '!':
			takeIt();
			if(currentChar == '=')
				takeIt();
			return Token.OPERATOR;
			
		case '=': 
			takeIt();
			if(currentChar == '='){
				takeIt();
				return Token.OPERATOR;
			}
			else{
				return Token.BECOMES;
			}	
		case '&':
			takeIt();
			if(currentChar == '&'){
				takeIt();
				return Token.OPERATOR;
			}
			break;
			
		case '|':
			takeIt();
			if(currentChar == '|'){
				takeIt();
				return Token.OPERATOR;
			}
			break;
			
		case '.':
			takeIt();
			return Token.DOT;

		case ':':
			takeIt();
			return Token.COLON;

		case ';':
			takeIt();
			return Token.SEMICOLON;

		case ',':
			takeIt();
			return Token.COMMA;

		case '(':
			takeIt();
			return Token.LPAREN;

		case ')':
			takeIt();
			return Token.RPAREN;

		case '[':
			takeIt();
			return Token.LBRACKET;

		case ']':
			takeIt();
			return Token.RBRACKET;

		case '{':
			takeIt();
			return Token.LCURLY;

		case '}':
			takeIt();
			return Token.RCURLY;

		case SourceFile.eot:
			return Token.EOT;

		default:
			takeIt();
			return Token.ERROR;
		}
		return Token.ERROR;
	}

	public Token scan () {
		//System.out.println(currentChar);
		
		Token tok;
		SourcePosition pos;
		int kind;

		currentlyScanningToken = false;
		int scanSeparatorStatus = 1;
		while (	currentChar == ' ' ||
				currentChar == '\n' ||
				currentChar == '\r' ||
				currentChar == '\t' ||
				currentChar == '/'
				){
			scanSeparatorStatus = scanSeparator(); 
			if(scanSeparatorStatus == 2 || scanSeparatorStatus == 3)
				System.out.println("Comments ignored");
			
			if(scanSeparatorStatus == -1)
				break;
		}
		currentlyScanningToken = true;
		currentSpelling = new StringBuffer("");
		pos = new SourcePosition();
		pos.start = sourceFile.getCurrentLine();

		if(scanSeparatorStatus != -1)
			kind = scanToken();
		else{
			System.out.println("----- / ------here");
			
			kind = Token.OPERATOR;
			currentSpelling.append('/');
		}

		pos.finish = sourceFile.getCurrentLine();
		tok = new Token(kind, currentSpelling.toString(), pos);
		
		enableDebugging();
		if (debug)
			System.out.println(tok + "  --->  " + Token.tokenTable[tok.kind]);
		
		return tok;
	}

}
