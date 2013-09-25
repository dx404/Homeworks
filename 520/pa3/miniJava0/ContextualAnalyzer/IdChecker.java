package miniJava.ContextualAnalyzer;

/**
 * This part of program is for the identification phase only
 * It takes the parsedAST as its input
 * It generates the decorated AST without type checking. 
 * @author duozhao
 *
 */

import java.util.ArrayList;
import java.util.HashMap;

import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.stdEnv.StdEnv;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.ErrorReporter;
import miniJava.ContextualAnalyzer.IdentificationTable; 
//same package import needed?

public class IdChecker implements Visitor<IdentificationTable, Reference>{
	//Object can be Type or declaration
	/*
	 * 	public interface Visitor<ArgType,ResultType> {
		public ResultType visitPackage(Package prog, ArgType arg);
	 */
	private IdentificationTable idTable;  //private Hash
	private SourcePosition dummyPos = new SourcePosition();
	private ErrorReporter reporter;

	private Package sourceAST;
	private Package envAST;
	public StdEnv stdEnv;

	boolean isInStaticScheme;
	ClassDecl currentClass;

	public IdChecker (Package sourceAST, ErrorReporter reporter) {
		this.reporter = reporter;
		this.idTable = new IdentificationTable();
		this.sourceAST = sourceAST;
		this.isInStaticScheme = false;
		currentClass = null;

		stdEnv = new StdEnv();
		this.envAST = stdEnv.envAST; //for checking only, also need identification
		//this.idTable.set(0, stdEnv.envIdTable.getMapAtLevel(0)); 
	}


	public Package check() { //driver
		envAST.visit(this, idTable);

		idTable.openScope();
		{
			sourceAST.visit(this, idTable);
		}
		idTable.closeScope();

		System.out.println("Check is over, When you see this message, it succeed. ");
		
		return sourceAST;
	}


	// Package
	public Reference visitPackage(Package prog, IdentificationTable idt){
		int startlevel = idt.getTopLevel();
		System.out.println("==1312x===" + startlevel);


		ClassDeclList cl = prog.classDeclList;
		for (ClassDecl c: cl){ 
			c.enableClassMemDeclMap(startlevel); // can type be resovled? 
			idt.enter(c.id.spelling, c);
		} 

		for (ClassDecl c: cl){ //for each another pass
			for(FieldDecl f : c.fieldDeclList){
				f.visit(this, idt); //resolve field type
			}
			for(MethodDecl m : c.methodDeclList){
				m.visitHead(this, idt); //resolve method head (return type and method name)
			}
		}

		for (ClassDecl c: cl){ //for each another pass
			idt.openScope();
			c.visit(this, idt);  //de facto call visitClassDecl();
			idt.closeScope(); 
		}

		return null;
	}

	// Declarations
	public Reference visitClassDecl(ClassDecl clas, IdentificationTable idt){ 
		currentClass = clas;
		//idt.set(2, clas.getHashMap());
		idt.append(clas.getHashMap());

		for (MethodDecl m: clas.methodDeclList){ //binding declaration name
			idt.openScope();
			m.visitBody(this, idt);
			idt.closeScope(); //for level level 3
		}
		return null;
	}

	public Reference visitFieldDecl(FieldDecl f, IdentificationTable idt){ //still at level 2
		f.type.visit(this, idt);
		return null; //not used here
	}

	public Reference visitMethodDeclHead(MethodDecl m, IdentificationTable idt){
		if (m.id.spelling.contentEquals("main")){
			if (m.isPrivate){
				System.out.println("main method must be public");
				System.exit(4);
			}
			else if (!m.isStatic){
				System.out.println("main method must be static");
				System.exit(4);
			}
			else if (m.type.typeKind != TypeKind.VOID){
				System.out.println("main method must be void");
				System.exit(4);
			}

		}
		m.type.visit(this, idt);
		return null;
	}

	public Reference visitMethodDeclBody(MethodDecl m, IdentificationTable idt){
		if (m.id.spelling.contentEquals("main")){
			if (m.parameterDeclList.size() != 1){
				System.out.println("main method must have exactly one parameter");
				System.exit(4);
			}
			else{
				ParameterDecl decl = m.parameterDeclList.get(0);
				decl.type.visit(this, idt);
				Type type = decl.type;
				if (!(type instanceof ArrayType)){
					System.out.println("main method must have exactly String[] args: not an array");
					System.exit(4);
				}
				else if (((ArrayType)type).eltType.typeKind != TypeKind.UNSUPPORTED ){
					System.out.println("main method must have exactly String[] args: not String");
					System.exit(4);
				}

			}

		}



		isInStaticScheme = m.isStatic;
		//boolean isPrivate = m.isPrivate;

		for (ParameterDecl pd: m.parameterDeclList) {
			pd.visit(this, idt);
		}

		idt.openScope();
		for (Statement s: m.statementList) {
			s.visit(this, idt);
		}
		if (m.returnExp != null) {
			m.returnExp.visit(this, idt);
		}
		idt.closeScope(); //for level 4
		isInStaticScheme = false;

		return null;
	}

	public Reference visitMethodDecl(MethodDecl m, IdentificationTable idt){ //only left for level 3+
		//not used here
		return null;
	}

	public Reference visitParameterDecl(ParameterDecl pd, IdentificationTable idt){
		pd.type.visit(this, idt);
		pd.id.type = pd.type;
		pd.id.declBinding = pd;
		idt.enter(pd.id.spelling, pd);
		//pd.id.visit(this, idt); //not applied occurance
		return null;
	} 

	public Reference visitVarDecl(VarDecl vd, IdentificationTable idt){
		vd.type.visit(this, idt);
		vd.id.type = vd.type;
		vd.id.declBinding = vd;
		idt.enter(vd.id.spelling, vd);
		return null;
	}

	// Statements
	public Reference visitBlockStmt(BlockStmt stmt, IdentificationTable idt){
		idt.openScope();
		for (Statement s: stmt.sl) {
			s.visit(this, idt);
		}
		idt.closeScope();
		return null;
	}

	public Reference visitVardeclStmt(VarDeclStmt stmt, IdentificationTable idt){
		stmt.varDecl.visit(this, idt);	
		stmt.initExp.visit(this, idt);
		return null;
	}

	public Reference visitAssignStmt(AssignStmt stmt, IdentificationTable idt){
		stmt.ref = stmt.ref.visit(this, idt); 
		stmt.val.visit(this, idt); //Expression
		return null;
	}

	public Reference visitCallStmt(CallStmt stmt, IdentificationTable idt){
		stmt.methodRef = stmt.methodRef.visit(this, idt); //returns to its declaration. 
		for (Expression e: stmt.argList) { //does not work here
			e.visit(this, idt); 
		}
		return null;
	}

	public Reference visitIfStmt(IfStmt stmt, IdentificationTable idt){
		stmt.cond.visit(this, idt); //must be boolean type

		if (stmt.thenStmt instanceof VarDeclStmt){
			System.out.println("Can not declare in a branch");
			System.exit(4);
		} //put into type checking, maybe

		stmt.thenStmt.visit(this, idt); //not initialzation statement

		if (stmt.elseStmt != null){
			if (stmt.elseStmt instanceof VarDeclStmt){
				System.out.println("Can not declare in a branch");
				System.exit(4);
			}
			stmt.elseStmt.visit(this, idt);
		}

		return null;
	}

	public Reference visitWhileStmt(WhileStmt stmt, IdentificationTable idt){
		stmt.cond.visit(this, idt);  //boolean type

		if (stmt.body instanceof VarDeclStmt){ //Questions about     ================
			System.out.println("Can not declare in a while branch");
			System.exit(4);
		}

		stmt.body.visit(this, idt);  //restriction for initialization statement

		return null;
	}

	// Types
	public Reference visitBaseType(BaseType type, IdentificationTable idt){
		//return null; // for base type nothing to resolve. 
		return null; //why return typekind
	}

	public Reference visitClassType(ClassType type, IdentificationTable idt){
		if (type.name.spelling.contentEquals("String")){
			type.typeKind = TypeKind.UNSUPPORTED; //mark as unsupported, still class type
			return null; //nothing is left to resolve
		}
		type.name.visit(this, idt); //resolve id visit identifier
		return null;
	}

	public Reference visitArrayType(ArrayType type, IdentificationTable idt){
		type.eltType.visit(this, idt); //resolve eltType[]
		return null;
	}

	public Reference visitErrorType(ErrorType type, IdentificationTable idt) {
		// TODO Auto-generated method stub
		return null;
	}

	public Reference visitUnsupportedType(UnsupportedType type, IdentificationTable idt) {
		// TODO Auto-generated method stub
		return null;
	}


	// Expressions
	public Reference visitUnaryExpr(UnaryExpr expr, IdentificationTable idt){
		expr.expr.visit(this, idt);
		return null;
	}

	public Reference visitBinaryExpr(BinaryExpr expr, IdentificationTable idt){
		expr.left.visit(this, idt);
		expr.right.visit(this, idt);
		return null;
	}

	public Reference visitRefExpr(RefExpr expr, IdentificationTable idt){ //the critial step to make a change
		expr.ref = expr.ref.visit(this, idt); //subject to change here. 
		//returns a reference, edit its fields rather create a new one, why?
		
		//expr.ref.visit(this, idt);
		return null;
	}

	public Reference visitCallExpr(CallExpr expr, IdentificationTable idt){
		expr.functionRef = expr.functionRef.visit(this, idt);
		for(Expression e: expr.argList){
			e.visit(this, idt);
		}
		return null;
	}

	public Reference visitLiteralExpr(LiteralExpr expr, IdentificationTable idt){ 
		expr.literal.visit(this, idt);
		return null;
	}

	public Reference visitNewArrayExpr(NewArrayExpr expr, IdentificationTable idt){
		expr.eltType.visit(this, idt);
		expr.sizeExpr.visit(this, idt);
		return null;
	}

	public Reference visitNewObjectExpr(NewObjectExpr expr, IdentificationTable idt){
		expr.classtype.visit(this, idt); //return a type; 
		return null;
	}

	// Terminals

	public Reference visitOperator(Operator op, IdentificationTable idt){
		return null; //must refine and re-distinguish Operating returns spelling ??????
	}

	public Reference visitIntLiteral(IntLiteral num, IdentificationTable idt){
		return null;
	}

	public Reference visitBooleanLiteral(BooleanLiteral bool, IdentificationTable idt){
		return null;
	}
	//\begin{pa3 added}
	public Reference visitIdentifier(Identifier id, IdentificationTable idt){
		Declaration binding = idt.retrieve(id.spelling); 
		//leave 3+ has been considered in idt class
		if (binding != null){
			id.declBinding = binding;
			id.type = binding.type; //may be null for class name. 
			if (binding instanceof LocalDecl){
				return new LocalRef(id, dummyPos);
			}
			else if (binding instanceof MemberDecl){
				MemberDecl memDecl = (MemberDecl) binding;
				if (isInStaticScheme && !memDecl.isStatic){
					System.out.println("Reference a non-static member(" + id.spelling +") in a static method");
					System.exit(4);
				}

				return new MemberRef(id, dummyPos);//change to a new IdT
			}
			else if (binding instanceof ClassDecl){//Class name, no type}
				return new ClassRef(id, dummyPos);
			}
			else {
				System.out.println("===789234x===: Should not arrive here");
			}
		}
		else{
			System.out.println("id(" + id.spelling + ") id cannot be resolved");
			idt.printTable();
			System.exit(4);
		}
		return null; //return to the corresponding declaration
	}

	// References /******* all references return to their declarations

	public Reference visitIndexedRef(IndexedRef ir, IdentificationTable idt) {
		ir.ref = ir.ref.visit(this, idt); 
		ir.indexExpr.visit(this, idt);
		return ir;
	}

	public Reference visitQualifiedRef(QualifiedRef qr, IdentificationTable idt) { 
		//modify to make a list of references
		Reference modifiedRef; //for return
		Reference refHead;
		IdentifierList ql = qr.qualifierList;
		boolean isCapableRef = false;

		IdentificationTable jumpIdt = new IdentificationTable();
		jumpIdt.set(0, idt.getMapAtLevel(0)); //copy environment

		if (idt.getTopLevel() > 0){
			jumpIdt.openScope();
			jumpIdt.set(1, idt.getMapAtLevel(1)); //load all class names 
		}

		jumpIdt.openScope();
		if (qr.thisRelative){
			isCapableRef = true;
			Identifier dummyId4This = new Identifier("_dummyThis", dummyPos);
			//****** subject to change with 
			dummyId4This.declBinding = currentClass; //need to find
			dummyId4This.type = currentClass.type;
			//****** subject to change with more subtle reference

			refHead = new ThisRef(dummyId4This, dummyPos); //may introduce a dummy identifier
			jumpIdt.set(2, idt.getMapAtLevel(2));
			
			System.out.println("==428403x=== Here is");
			jumpIdt.printTable();
			System.out.println("==428404x=== Here is over");
			
		}
		else {
			Identifier headId = ql.get(0);
			refHead = headId.visit(this, idt); //visit identifier return to its reference expr
			if(headId.type == null){	//a class name
				isCapableRef = true;
				isInStaticScheme = true;
				
				ClassDecl headClassDecl = (ClassDecl) headId.declBinding;
				jumpIdt.set(2, headClassDecl.getHashMap()); //load class members to level 2, must be static 
				
				System.out.println("Here it is");
			}
			else if (headId.type.typeKind == TypeKind.UNSUPPORTED){ //for string
				//processing unsupported type
				System.out.println("unsupported type");
				isCapableRef = false;
			}
			else if(headId.type instanceof ClassType){
				isCapableRef = true;
				isInStaticScheme = false;
				ClassType headIdDeclType = (ClassType) headId.type;
				ClassDecl headIdToClass = (ClassDecl) headIdDeclType.name.declBinding;
				jumpIdt.set(2, headIdToClass.getHashMap());
			}
			else{
				System.out.println("===340958x==: Cannot continue with reference:");
				isCapableRef = false;
			}

		}
		modifiedRef = refHead;
		int startIndex = qr.thisRelative? 0 : 1;
		for (int i = startIndex; i < ql.size(); i++) { //local, only needs class member mappings
			
			IdentificationTable transferIdt = new IdentificationTable();
			transferIdt.openScope();
			transferIdt.openScope();
			transferIdt.set(2, jumpIdt.getMapAtLevel(2));
			jumpIdt = transferIdt;
			
			if (!isCapableRef){
				System.out.println("===2342x===(" + refHead.toString() + ")cannot be reffed");
				System.exit(4);
			}
			
			
			Identifier subitrId = ql.get(i);
			System.out.println("---79324812x--: i=" + i + "      id: = "+ subitrId.spelling);
			jumpIdt.printTable();
			subitrId.visit(this, jumpIdt);
			
			modifiedRef = new DeRef(modifiedRef, subitrId, dummyPos);
			jumpIdt.closeScope();
			/*
			 * 0 predefined
			 * 1 class name
			 * 2 member
			 * 3 method parameter
			 * 4+ local 
			 */
			
			jumpIdt.openScope();
			System.out.println("---738472x--: " + jumpIdt.getTopLevel());
			if (subitrId.declBinding instanceof MemberDecl){
				MemberDecl memDecl = (MemberDecl) subitrId.declBinding;
				if (memDecl.isStatic 
						&& subitrId.declBinding instanceof MethodDecl){
					System.out.println("Cannot reference to a non-static method:   ");
					System.exit(4);
				}
			}
			else {
				System.out.println("Not a member declaration");
				System.exit(4);
			}
			
			if (subitrId.type instanceof ClassType){
				isInStaticScheme = false;
				ClassType subitrIdDeclType = (ClassType) subitrId.type;
				ClassDecl subitrIdToClass = (ClassDecl) subitrIdDeclType.name.declBinding;

				System.out.println("==9211x==: " + (subitrIdDeclType.name.spelling));
				System.out.println("==9212x==: " + (subitrIdToClass==null));
				jumpIdt.set(2, subitrIdToClass.getHashMap());

				jumpIdt.printTable();
			}
			else if (i != ql.size() - 1){
				System.out.println("==9212x==: Invalid type");
				System.exit(4);
			}

		}
		//Here is without resolved all id's but the first one hasn't been resolved 
		return modifiedRef;
	}

	public Reference visitLocalRef(LocalRef ref, IdentificationTable idt) { // looking for at 3, 4+
		// TODO Auto-generated method stub
		return ref;
	}

	public Reference visitThisRef(ThisRef ref, IdentificationTable idt) { //looking for at Level 2 exactly
		// TODO Auto-generated method stub
		return ref;
	}

	public Reference visitMemberRef(MemberRef ref, IdentificationTable idt) { // located at level 2
		// TODO Auto-generated method stub
		return ref;
	}

	public Reference visitClassRef(ClassRef ref, IdentificationTable idt) { //located at level 1
		// TODO Auto-generated method stub
		return ref;
	}

	public Reference visitDeRef(DeRef ref, IdentificationTable idt) { //
		// TODO Auto-generated method stub
		return ref;
	}
	//\end{pa3 added}
}