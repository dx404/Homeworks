package miniJava.stdEnv;

import java.io.File;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class StdEnv implements Cloneable{ //level 0
	public Package envAST;
	public IdentificationTable envIdTable; //for current use only one level

	private SourcePosition dummyPos = new SourcePosition();

	public StdEnv(){
		envIdTable = new IdentificationTable();
		establishBuidInStdEnv();
	}

	public StdEnv(File sourceStdEnv){
		envIdTable = new IdentificationTable();
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

		MethodDeclList mdlForPrintStream = new MethodDeclList();
		mdlForPrintStream.add(methodPrint);
		mdlForPrintStream.add(methodPrintln);

		ClassDecl miniJavaPrintStream = new ClassDecl(
				new Identifier("_PrintStream", dummyPos), 
				new FieldDeclList(), 
				mdlForPrintStream, 
				dummyPos); 


		miniJavaPrintStream.id.declBinding = miniJavaPrintStream;
		miniJavaPrintStream.id.type = new ClassType(miniJavaPrintStream.id, dummyPos);
		miniJavaPrintStream.enableClassMemDeclMap(0);
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
		miniJavaOutId.type = miniJavaOut.type;
		FieldDeclList fdlminiJavaSystem = new FieldDeclList();
		fdlminiJavaSystem.add(miniJavaOut);
		ClassDecl miniJavaSystem = new ClassDecl(
				new Identifier("System", dummyPos), 
				fdlminiJavaSystem, new MethodDeclList(),
				dummyPos);

		miniJavaSystem.id.declBinding = miniJavaSystem;
		miniJavaSystem.id.type = new ClassType(miniJavaSystem.id, dummyPos);
		miniJavaSystem.enableClassMemDeclMap(0);

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
		
		
		//envIdTable.set(0, envIdTable.topLevelMap);

	}

	public Object clone() { //need to copy
		try{
			return super.clone();
		}
		catch(Exception e){ return null; }
	}

}
