package miniJava.stdEnv;

import java.io.File;
import java.util.HashMap;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class StdEnv implements Cloneable{ //level 0
	public Package envAST;
	public IdentificationTable envIdTable; //for current use only one level

	private SourcePosition dummyPos = new SourcePosition();

	public StdEnv(){
		envIdTable = new IdentificationTable(new HashMap<String, Declaration>());
		establishBuidInStdEnv();
	}
	
	public static MethodDecl printMethod;
	public static MethodDecl printlnMethod;

	public StdEnv(File sourceStdEnv){
		envIdTable = new IdentificationTable(new HashMap<String, Declaration>());
		establishStdEnvFromSource(sourceStdEnv);
	}

	private void establishStdEnvFromSource(File sourceStdEnv){
		//implemented later
	}

	private void establishBuidInStdEnv() { 
		//pure side effect to establish level 0
		MemberDecl mdPrint = new FieldDecl(
				false, false, 
				new BaseType(TypeKind.VOID, dummyPos), 
				new Identifier("print", dummyPos), 
				dummyPos);
		ParameterDecl pdPrint = new ParameterDecl(
				new BaseType(TypeKind.INT, dummyPos), 
				new Identifier("n", dummyPos), 
				dummyPos);
		ParameterDeclList pdlPrint = new ParameterDeclList();
		pdlPrint.add(pdPrint);
		MethodDecl methodPrint = new MethodDecl(
				mdPrint, pdlPrint, new StatementList(), null, dummyPos);

		MemberDecl mdPrintln = new FieldDecl(
				false, false, 
				new BaseType(TypeKind.VOID, dummyPos), 
				new Identifier("println", dummyPos), 
				dummyPos);
		ParameterDecl pdPrintln = new ParameterDecl(
				new BaseType(TypeKind.INT, dummyPos), 
				new Identifier("n", dummyPos), 
				dummyPos);
		ParameterDeclList pdlPrintln = new ParameterDeclList();
		pdlPrintln.add(pdPrintln);
		MethodDecl methodPrintln = new MethodDecl(
				mdPrintln, pdlPrintln, new StatementList(), null, dummyPos);
		
		MemberDecl mdPrintlnBool = new FieldDecl(
				false, false, 
				new BaseType(TypeKind.VOID, dummyPos), 
				new Identifier("println", dummyPos), 
				dummyPos);
		ParameterDecl pdPrintlnBool = new ParameterDecl(
				new BaseType(TypeKind.BOOLEAN, dummyPos), 
				new Identifier("b", dummyPos), 
				dummyPos);
		ParameterDeclList pdlPrintlnBool = new ParameterDeclList();
		pdlPrintln.add(pdPrintlnBool);
		MethodDecl methodPrintlnBool = new MethodDecl(
				mdPrintlnBool, pdlPrintlnBool, new StatementList(), null, dummyPos);

		MethodDeclList mdlForPrintStream = new MethodDeclList();
		mdlForPrintStream.add(methodPrint);
		mdlForPrintStream.add(methodPrintln);
		mdlForPrintStream.add(methodPrintlnBool);

		ClassDecl miniJavaPrintStream = new ClassDecl(
				new Identifier("_PrintStream", dummyPos), 
				new FieldDeclList(), 
				mdlForPrintStream, 
				dummyPos); 


		miniJavaPrintStream.id.declBinding = miniJavaPrintStream;
		miniJavaPrintStream.establishClassIDT();
		//two fields add to the class declaration mapping. 

		//********************************************************************
		
		Identifier miniJavaOutId = new Identifier("out", dummyPos);
		FieldDecl miniJavaOut = 
				new FieldDecl(false, true, 
						new ClassType(new Identifier(
								"_PrintStream", dummyPos), dummyPos), //to resolve?
								miniJavaOutId, //binding?
								dummyPos);
		miniJavaOutId.declBinding = miniJavaOut;
		FieldDeclList fdlminiJavaSystem = new FieldDeclList();
		fdlminiJavaSystem.add(miniJavaOut);
		ClassDecl miniJavaSystem = new ClassDecl(
				new Identifier("System", dummyPos), 
				fdlminiJavaSystem, new MethodDeclList(),
				dummyPos);

		miniJavaSystem.id.declBinding = miniJavaSystem;
		miniJavaSystem.establishClassIDT();

		/**
		 * for standard Environment package
		 */
		ClassDeclList cl = new ClassDeclList();
		cl.add(miniJavaPrintStream);
		cl.add(miniJavaSystem);
		envAST = new Package(cl, dummyPos);

//		envIdTable.topLevelMap.put("_PrintStream", miniJavaPrintStream);
//		envIdTable.topLevelMap.put("System", miniJavaSystem);
		envIdTable.enter("_PrintStream", miniJavaPrintStream);
		envIdTable.enter("System", miniJavaSystem);
		
		
		printMethod = methodPrint;
		printlnMethod = methodPrintln;

	}


}
