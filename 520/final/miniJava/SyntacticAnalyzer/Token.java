package miniJava.SyntacticAnalyzer;

import miniJava.SyntacticAnalyzer.SourcePosition;

public final class Token extends Object {

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
	
	public boolean isUnary(){
		return (spelling.contentEquals("-") 
				|| spelling.contentEquals("!") 
				|| spelling.contentEquals("++") 
				|| spelling.contentEquals("--") 
				) ? true: false;
	}
	
	public boolean isPostfix(){
		return (spelling.contentEquals("++") 
				|| spelling.contentEquals("--") 
				) ? true: false;
	}
	public boolean isVarType(){
		return (kind == Token.INT ||
				kind == Token.BOOLEAN ||
				kind == Token.IDENTIFIER)? true : false; 
		//void cannot serve as Var Type.
	}
	
	public boolean isOperator(){
		return (kind == Token.OPERATOR
				|| kind == Token.DISJUNCTION
				|| kind == Token.CONJUNCTION
				|| kind == Token.EQUALITY
				|| kind == Token.RELATIONAL
				|| kind == Token.ADDITIVE
				|| kind == Token.MULTIPLICATIVE
				|| kind == Token.UNARY
				|| kind == Token.INCREMENT
				|| kind == Token.DECREMENT
				|| kind == Token.INSTANCEOF)? true : false;
	}
	public boolean spells(String str){
		return spelling.contentEquals(str);
	}
	
	// Token classes...

	public static final int
	EMPTY = -1,
	IDENTIFIER = 0, //"<id>",
	INTLITERAL = 1, //"<IntLiteral>",  //integer literals
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
	FOR = 18,
	TRUE = 19, //"true",
	FALSE = 20, //"false",
	NULL = 21, 
	INSTANCEOF = 22, // instanceof is categoried as Relational operator
	SUPER = 23, 
	EXTENDS = 24,
	IMPLEMENTS = 25, 
	
	BECOMES = 26,
	DOT = 27, //".",
	COLON = 28, //":",
	SEMICOLON = 29, //";",
	COMMA = 30, //",",
	LPAREN = 31, //"(",
	RPAREN = 32, //")",
	LBRACKET = 33, //"[",
	RBRACKET = 34, //"]",
	LCURLY = 35, //"{",
	RCURLY = 36, //"}",
	
	EOT = 37, //"",  //"<eot>"	
	ERROR = 38, //"<error>";
	
	DISJUNCTION = 39,
	CONJUNCTION = 40,
	EQUALITY = 41,
	RELATIONAL = 42,
	ADDITIVE = 43,
	MULTIPLICATIVE = 44,
	UNARY = 45,
	INCREMENT = 46,
	DECREMENT = 47; 
	
	public static String[] tokenTable = new String[] {
		"<id>", "<intliteral>", "<operator>", "<binop>", "<unop>",
		"class", "new", "return", "public", "private", "static",
		"int", "boolean", "void", "this", 
		"if", "else", "while", "for",
		"true", "false", "null", 
		"instanceof", "super", "extends", "implements", 
		"=",
		".", ":", ";", ",", "(", ")", "[", "]",
		"{", "}",
		"",  //"<eot>"	
		"<error>",
		"disjunction", "conjunction", "equality", 
		"relational", "additive", "multiplicative", "unary", "++", "--"
	};
	
	public enum OpName{
		OR,
		AND,
		EQ,
		NEQ,
		LT,
		LEQ,
		GT,
		GEQ,
		INSTANCEOF,
		PLUS,
		MINUS,
		TIMES,
		DIV,
		NEG,
		NOT,
		PREINCRE,
		PREDECRE,
		POSTINCRE,
		POSTDECRE
	}

	private final static int firstReservedWord = Token.CLASS,
			lastReservedWord  = Token.IMPLEMENTS;
	
	public final static String[] unitaryOperator = 
			{"!", "-", "++", "--"}; //finall added increment/decrement operator
	
	public final static String[] binaryOperator = 
		{">", "<", "==", "<=", ">=", "!=", "&&", "||", "+", "-", "*", "/", "instanceof"}; 
	//final introduce instanceof operator
	
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