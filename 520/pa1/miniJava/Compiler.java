package miniJava;

import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.SourceFile;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.ErrorReporter;

public class Compiler {

	static String objectName = "obj.mjava"; //"obj.tam";

	private static Scanner scanner;
	private static Parser parser;
	private static ErrorReporter reporter;

	static boolean compileProgram (String sourceName, String objectName, 
			boolean showingAST, boolean showingTable) throws SyntaxError {

		System.out.println("********** " +
				"Duo's miniJava Compiler (Version 1.0)" +
				" **********");

		System.out.println("Syntactic Analysis Get Started...");
		SourceFile source = new SourceFile(sourceName);

		if (source == null) {
			System.out.println("Can't access source file " + sourceName);
			System.exit(1);
		}

		scanner  = new Scanner(source);
		parser   = new Parser(scanner, reporter);

		parser.parseProgram();				// 1st pass

		
		boolean successful = true;
		/*
		boolean successful = (reporter.numErrors == 0);
		if (successful) {
			System.out.println("Compilation was successful.");
		} else {
			System.out.println("Compilation was unsuccessful.");
		}
		*/
		return successful;
	}

	public static void main(String[] args){
		boolean compiledOK;

		if (args.length != 1) {
			System.out.println("Usage: tc filename");
			System.exit(1);
		}

		String sourceName = args[0];
		try {
			compiledOK = compileProgram(sourceName, objectName, false, false);
			System.exit(0);
		} catch (SyntaxError e) {
			System.out.println("System.exit(4)");
			e.printStackTrace();
			System.exit(4);
		}

	}

}
