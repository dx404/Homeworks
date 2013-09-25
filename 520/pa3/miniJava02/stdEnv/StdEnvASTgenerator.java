package miniJava.stdEnv;

import java.io.File;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.Scanner;
import miniJava.SyntacticAnalyzer.SourceFile;

public class StdEnvASTgenerator {
	private static Scanner scannerEnv;
	private static Parser parserEnv;
	private static ErrorReporter reporterEnv;
	
	public Package astEnv; 
	
	StdEnvASTgenerator(){
		String stdEnvSourceFileName = "miniJava/stdEnv/stdEnvSource.mjava";
		SourceFile stdEnvSourceFile = new SourceFile(stdEnvSourceFileName);
		//subject to test in differnt source address. 
//		if (stdEnvSourceFile == null) {
//			System.out.println("Can't access Standard source file " + stdEnvSourceFileName);
//			System.exit(1);
//		}
		
		scannerEnv  = new Scanner(stdEnvSourceFile, true);
		reporterEnv = new ErrorReporter();
		parserEnv   = new Parser(scannerEnv, reporterEnv);

		astEnv = parserEnv.parsePackage();	
		
	}
	public static void main(String[] args){
		StdEnvASTgenerator envAST = new StdEnvASTgenerator();
		System.out.println("from source");
		new ASTDisplay().showTree(envAST.astEnv);
	}
	
}
