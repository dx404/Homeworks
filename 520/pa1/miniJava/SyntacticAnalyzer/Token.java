
package miniJava.SyntacticAnalyzer;

import miniJava.SyntacticAnalyzer.SourcePosition;

final class Token extends Object {

	protected int kind;
	protected String spelling;
	protected SourcePosition position;

	public Token(int kind, String spelling, SourcePosition position) {
		this.kind = kind;
		this.spelling = spelling;
		this.position = position;
		
		if (kind == Token.IDENTIFIER) {
			for(int k = firstReservedWord; k <= lastReservedWord; k++){
				if(spelling.equals(tokenTable[k])){
					this.kind = k;
					break;
				}
			}
		}
	}

	public static String spell (int kind) {
		return tokenTable[kind];
	}

	public String toString() {
		return "Kind=" + kind + ", spelling=" + spelling +
				", position=" + position;
	}

	// Token classes...

	public static final int
	IDENTIFIER = 0, //"<id>",
	NUM = 1, //"<num>",  //integer literals
	OPERATOR = 2,
	BINOP = 3, //"<binop>", 
	UNOP = 4, //"<unop>",
	CLASS = 5, //"class",
	NEW = 6, //"new",
	RETURN = 7, //"return",
	PUBLIC = 8, //"public",
	PRIVATE = 9, //"private",
	STATIC = 10, //"static",
	INT = 11, //"int",
	BOOLEAN = 12, //"boolean",
	VOID = 13, //"void",
	THIS = 14, //"this",
	IF = 15, //"if",
	ELSE = 16, //"else",
	WHILE = 17, //"while",
	TURE = 18, //"ture",
	FALSE = 19, //"false",
	BECOMES = 20,
	DOT = 21, //".",
	COLON = 22, //":",
	SEMICOLON = 23, //";",
	COMMA = 24, //",",
	LPAREN = 25, //"(",
	RPAREN = 26, //")",
	LBRACKET	 = 27, //"[",
	RBRACKET = 28, //"]",
	LCURLY = 29, //"{",
	RCURLY = 30, //"}",
	EOT = 31, //"",  //"<eot>"	
	ERROR = 32; //"<error>";

	public static String[] tokenTable = new String[] {
		"<id>", "<num>", "<operator>", "<binop>", "<unop>",
		"class", "new", "return", "public", "private", "static",
		"int", "boolean", "void", "this", "if", "else", "while", "ture",
		"false",
		"=",
		".", ":", ";", ",", "(", ")", "[", "]",
		"{", "}",
		"",  //"<eot>"	
		"<error>"
	};

	private final static int	firstReservedWord = Token.CLASS,
			lastReservedWord  = Token.FALSE;
	
	public final static String[] unitaryOperator = 
			{"!", "-"};
	
	public final static String[] binaryOperator = 
		{">", "<", "==", "<=", ">=", "!=", "&&", "||", "+", "-", "*", "/"};
	
	public static boolean isUnitaryOperator(String op){
		for(int i = 0; i < unitaryOperator.length; i++)
			if(unitaryOperator[i].equals(op))
				return true;
		return false;
	}
	
	public static boolean isBinaryOperator(String op){
		for(int i = 0; i < binaryOperator.length; i++)
			if(binaryOperator[i].equals(op))
				return true;
		return false;
	}
	
	
}