package miniJava;

import mJAM.Disassembler;
import mJAM.Interpreter;
import mJAM.ObjectFile;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.SourceFile;
import miniJava.SyntacticAnalyzer.SyntaxError;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.CodeGenerator.Encoder;
import miniJava.CodeGenerator.Indexer;
import miniJava.ContextualAnalyzer.*;
import miniJava.ErrorReporter;

public class Compiler {
	private Scanner scanner;
	private Parser parser;
	private ErrorReporter reporter;
	private AST parsedAST;
	private AST decoratedAST;
	private IdChecker idChecker;
	private TypeChecker typeChecker;
	private Encoder enCoder;

	private String sourceName;
	private String objectName;

	public Compiler(String s, String o){
		sourceName = s;
		objectName = o;
	}

	boolean compileProgram (boolean showingAST, boolean showingTable) throws SyntaxError {
		//System.out.println("Source File Checking Get Started...");
		SourceFile source = new SourceFile(sourceName);
		if (!source.isSourceExist()) {
			System.out.println("Can't access source file " + sourceName);
			System.exit(1);
		}
		//System.out.println("Syntactic Analysis Get Started...");
		scanner  = new Scanner(source);
		reporter = new ErrorReporter();
		parser   = new Parser(scanner, reporter);
		parsedAST = parser.parsePackage();	//driver's function
		//System.out.println ("Contextual Analysis ...");
		idChecker = new IdChecker(parsedAST, reporter);
		decoratedAST = idChecker.check();
		typeChecker = new TypeChecker(decoratedAST, reporter);
		typeChecker.check();
		//System.out.println ("Code generation ...");		
		Indexer indexer = new Indexer(decoratedAST);
		indexer.indexIt();
		enCoder = new Encoder(decoratedAST);
		enCoder.setShortCircuitEvaluation(true);
		enCoder.encodeIt(sourceName);

		writeToFileAndRun(objectName); //modify file name later

		return true;
	}



	public static void main(String[] args){
		if (args.length < 1) {
			System.out.println("Usage: source filename must be specified: ");
			System.exit(4);
		}
		try {
			String source = args[0];
			String object = source.split("[.]")[0] + ".mJAM";
			
			Compiler compiler = new Compiler(source, object);
			boolean compiledOK = compiler.compileProgram(false, false);
			if (compiledOK){
				System.out.println("compiledOK?: " + compiledOK);
			}
			System.exit(0);
		} 
		catch (SyntaxError e) {
			System.out.println("===Commpiler::System.exit(4)===");
			e.printStackTrace();
			System.exit(4);
		} 
		catch (Exception e) {
			System.exit(4);
		}

	}

	public void writeToFileAndRun(String fileName){
		String objectCodeFileName = fileName;
		ObjectFile objF = new ObjectFile(objectCodeFileName);
		System.out.print("===Writing object code file " + objectCodeFileName + " ... ");
		if (objF.write()) {
			System.out.println("FAILED!");
			return;
		}
		else{
			System.out.println("===SUCCEEDED");	
			/* create asm file using disassembler */
		}
		System.out.print("===Writing assembly file ... ");
		Disassembler d = new Disassembler(objectCodeFileName);
		if (d.disassemble()) {
			System.out.println("===FAILED!");
			return;
		}
		else
			System.out.println("SUCCEEDED");

		/* run code */
		System.out.println("===Running code ... ");
		//Interpreter.interpret(objectCodeFileName);
		System.out.println("*** mJAM execution completed");
	}

}
