package miniJava;

import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.SourceFile;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.CodeGenerator.Encoder;
import miniJava.ContextualAnalyzer.*;
import miniJava.ErrorReporter;

public class Compiler {

	static String objectName = "obj.mjava"; //"obj.tam";

	private static Scanner scanner;
	private static Parser parser;
	private static ErrorReporter reporter;
	private static AST parsedAST;
	private static AST decoratedAST;
	private static IdChecker idChecker;
	private static TypeChecker typeChecker;
	private static Encoder enCoder;

	static boolean compileProgram (String sourceName, String objectName, 
			boolean showingAST, boolean showingTable) throws SyntaxError {
		
		//System.out.println("Source File Checking Get Started...");

		SourceFile source = new SourceFile(sourceName);
		
		if (!source.isSourceExist()) {
			System.out.println("Can't access source file " + sourceName);
			System.exit(1);
		}

		System.out.println("Syntactic Analysis Get Started...");

		scanner  = new Scanner(source);
		reporter = new ErrorReporter();
		parser   = new Parser(scanner, reporter);
		parsedAST = parser.parsePackage();	//driver's function
		//new ASTDisplay().showTree(parsedAST);

		System.out.println ("Contextual Analysis ...");

		idChecker = new IdChecker(parsedAST, reporter);
		decoratedAST = idChecker.check();
		typeChecker = new TypeChecker(decoratedAST, reporter);
		typeChecker.check();
		
		System.out.println ("Code generation ...");
		enCoder = new Encoder(decoratedAST, sourceName);
		
		return true;
	}
	

	
	public static void main(String[] args){
		if (args.length < 1) {
			System.out.println("Usage: source filename must be specified: ");
			System.exit(1);
		}
		String sourceName = args[0];
		//System.out.println("File:  " + args[0]);

		try {
			boolean compiledOK = 
					compileProgram(sourceName, objectName, false, false);

			//System.out.println("compiledOK?: " + compiledOK);
			System.exit(0);
		} catch (SyntaxError e) {
			System.out.println("===Commpiler::System.exit(4)===");
			e.printStackTrace();
			System.exit(4);
		}

	}

}
