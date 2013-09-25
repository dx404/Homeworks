package miniJava.ContextualAnalyzer;

/**
 * This part of program is for the identification phase only
 * It takes the parsedAST as its input
 * It generates the decorated AST without type checking. 
 * @author duozhao
 *
 */

import java.util.HashMap;

import miniJava.SyntacticAnalyzer.FuncSigContainer;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.stdEnv.StdEnv;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.ErrorReporter;
import miniJava.ContextualAnalyzer.IdentificationTable; 

public class IdChecker implements Visitor<IdentificationTable, Reference>{

	private IdentificationTable idTable;
	private SourcePosition dummyPos = SourcePosition.dummyPos;

	private ErrorReporter reporter;

	private AST envAST;
	private AST processingAST; //must pass in a parsed AST

	public StdEnv stdEnv;

	private Boolean isInStaticMethod; //can only reference static fields/methods
	private ClassDecl currentClass; //for the tracking of the current context of class

	private int numOfErrors;
	private int mainCounter;

	public IdChecker (AST parsedAST, ErrorReporter reporter) { //should be the abstract AST
		this.processingAST = parsedAST; //for copy, impossible to do
		stdEnv = new StdEnv();
		this.envAST = stdEnv.envAST; //for checking only, also need identification
		this.reporter = reporter;

		this.idTable = new IdentificationTable(new HashMap<String, Declaration>());
		idTable.isClassIDT = false;
		this.isInStaticMethod = null;
		this.currentClass = null; //hasn't enter any class


		numOfErrors = 0;
		mainCounter = 0;
	}


	public AST check() { //driver
		envAST.visit(this, idTable);

		idTable.openScope();
		processingAST.visit(this, idTable);
		idTable.closeScope();


		if (numOfErrors > 0) {
			System.out.println("***System.exit(4); ");
			System.exit(4);
		}
		if (mainCounter > 1){
			System.out.println("***More than one main method");
			System.exit(4);
		}
		else if (mainCounter < 1){
			System.out.println("***===IdChecker: AST check(): No main method");
			System.exit(4);
		}

		return processingAST; //decorated AST
	}

	public void pushError(String msg, Identifier id){
		numOfErrors ++;

		String errClassSpelling = (currentClass == null || currentClass.id == null)? 
				"Out of class" : currentClass.id.spelling;

		System.out.println("*** " + "Error " + numOfErrors + ": " 
				+ msg + " :id (" + id.spelling +"), from class: (" + errClassSpelling + ")");
		System.exit(4);
	}

	public void pushError(String msg, Identifier id, boolean ifTerminated){
		pushError(msg, id);
		if (ifTerminated){
			System.exit(4);
		}
	}

	// Package
	public Reference visitPackage(Package prog, IdentificationTable idt){

		ClassDeclList cl = prog.classDeclList; //used for up to 3 passes

		Integer cl_index = 0;
		for (ClassDecl c: cl){ //1st pass
			currentClass = c;
			c.establishClassIDT();
			idt.enter_and_bind(c);
			c.setIndex(cl_index++);
		}	
		for (ClassDecl c: cl){ //2nd pass
			currentClass = c;
			idt.openScope(c);
			Integer fIndex = 0, mIndex = 0;
			for(FieldDecl f : c.fieldDeclList){
				f.visit(this, idt);
				f.setIndex(fIndex++);
			}
			for(MethodDecl m : c.methodDeclList){
				if (m.isMain()){
					mainCounter++;
					prog.mainMethod = m;
				}
				m.visitHead(this, idt); //resolve method head (return type and method name)
				m.setIndex(mIndex++);
			}
			idt.closeScope();
		}

		for (ClassDecl c: cl){ //3nd pass
			currentClass = c;
			idt.openScope(c);
			c.visit(this, idt);  //call visitClassDecl();
			idt.closeScope(); 
		}

		return null;
	}

	// Declarations
	public Reference visitClassDecl(ClassDecl clas, IdentificationTable idt){ 
		//actually, only the method bodies are left to visit. 
		for (MethodDecl m: clas.methodDeclList){ //binding declaration name
			idt.openScope();
			m.visitBody(this, idt); //only left to visit the body of the method.
			idt.closeScope();  //for level level 3
		}
		return null;
	}

	public Reference visitFieldDecl(FieldDecl f, IdentificationTable idt){ //still at level 2
		f.type.visit(this, idt); 
		return null; 
	}

	public Reference visitMethodDeclHead(MethodDecl m, IdentificationTable idt){
		//m.id has been pre-entered
		if (m.isMain){
			if (m.isPrivate){
				pushError("public modifier for main method is expected", m.id);
			}
			if (!m.isStatic){
				pushError("static modifier for main method is expected", m.id);
			}
			if (m.getReturnType().typeKind != TypeKind.VOID){
				pushError("void type is expected as a return type for main method", m.id);
			}

		}
		m.type.visit(this, idt);
		return null;
	}

	public Reference visitMethodDeclBody(MethodDecl m, IdentificationTable idt){
		//parameters + body
		if (m.isMain && m.parameterDeclList.size() != 1){
			pushError("main method must have exactly one parameter", m.id);
		}
		isInStaticMethod = m.isStatic;

		for (ParameterDecl pd: m.parameterDeclList) { //open scope? opend in ClassDecl
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

		isInStaticMethod = null; //Boolean Type
		return null;
	}

	public Reference visitParameterDecl(ParameterDecl pd, IdentificationTable idt){
		idt.enter_and_bind(pd);
		pd.type.visit(this, idt);

		return null;
	} 

	public Reference visitMethodDecl(MethodDecl m, IdentificationTable idt){ //only left for level 3+
		//not used here
		m.visitHead(this, idt);
		m.visitBody(this, idt);
		return null;
	}

	public Reference visitVarDecl(VarDecl vd, IdentificationTable idt){
		idt.enter_and_bind(vd);
		vd.type.visit(this, idt);
		return null;
	}

	// Statements
	public Reference visitNullStmt(NullStmt stmt, IdentificationTable idt) {
		return null;
	}
	
	public Reference visitExprStmt(ExprStmt stmt, IdentificationTable idt) {
		stmt.expr.visit(this, idt);
		return null;
	}

	public Reference visitBlockStmt(BlockStmt stmt, IdentificationTable idt){
		idt.openScope();
		for (Statement s: stmt.sl) {
			s.visit(this, idt);
		}
		idt.closeScope();
		return null;
	}

	public Reference visitIfStmt(IfStmt stmt, IdentificationTable idt){
		stmt.cond.visit(this, idt); //must be boolean type, checked in type checker

		if (stmt.thenStmt instanceof VarDeclListStmt){
			pushError("VarDeclStmt cannot exist as an entire single \"then\" branch", currentClass.id);
		} 

		stmt.thenStmt.visit(this, idt); 

		if (stmt.elseStmt != null){
			if (stmt.elseStmt instanceof VarDeclListStmt){
				pushError("VarDeclStmt cannot exist as an entire single \"else\" branch", currentClass.id);
			}
			stmt.elseStmt.visit(this, idt);
		}

		return null;
	}

	public Reference visitWhileStmt(WhileStmt stmt, IdentificationTable idt){
		stmt.cond.visit(this, idt);  //boolean type
		if (stmt.body instanceof VarDeclListStmt){ //Questions about     ================
			pushError("VarDeclStmt cannot exist as an entire single \"while body\" branch", currentClass.id);
		}

		stmt.body.visit(this, idt);  //restriction for initialization statement

		return null;
	}

	public Reference visitForStmt(ForStmt stmt, IdentificationTable idt) {
		for (Statement init : stmt.init){
			init.visit(this, idt);
		}
		stmt.cond.visit(this, idt);
		stmt.forBody.visit(this, idt);
		if (stmt.forBody instanceof VarDeclListStmt){ //Questions about     ================
			pushError("***VarDeclStmt cannot exist as an entire single \"for body\" branch", currentClass.id);
		}
		for (Statement update : stmt.update){
			update.visit(this, idt);
		}
		return null;
	}

	// Types
	public Reference visitBaseType(BaseType type, IdentificationTable idt){
		return null; 
	}
	
	@Override
	public Reference visitVoidType(VoidType type, IdentificationTable idt) {
		return null;
	}

	public Reference visitClassType(ClassType type, IdentificationTable idt){ //here IDT is not a class IDT
		int tryVisitStatus = tryVisitIdentifier(type.name, idt);
		if (tryVisitStatus == 0 || tryVisitStatus == 1 ){
			return null;
		}
		else if (tryVisitStatus < 0 && type.name.spells("String")){ //should be 0 or 1
			type.typeKind = TypeKind.UNSUPPORTED;
			return null; //if (!(type.name.declBinding instanceof ClassDecl)) {//post type checking. 
		}
		else {
			pushError("*** A Class Type is expected.", type.name);
		}
		return null;
	}

	public Reference visitArrayType(ArrayType type, IdentificationTable idt){
		type.eltType.visit(this, idt); //resolve eltType[]
		return null;
	}

	public Reference visitErrorType(ErrorType type, IdentificationTable idt) {
		return null;
	}

	public Reference visitUnsupportedType(UnsupportedType type, IdentificationTable idt) {
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

	public Reference visitRefExpr(RefExpr expr, IdentificationTable idt){
		expr.ref = expr.ref.visit(this, idt); 
		Declaration refDecl = expr.ref.getDecl();
		if (refDecl instanceof VarDecl &&
				!((VarDecl)refDecl).isInitialized){
			pushError("***==23492xx== local var is not initialized", currentClass.id);
		}
		return null;
	}

	public Reference visitIncrementExpr(IncrementExpr expr, IdentificationTable idt) {
		expr.ref = expr.ref.visit(this, idt);
		return null;
	}

	public Reference visitDecrementExpr(DecrementExpr expr, IdentificationTable idt) {
		expr.ref = expr.ref.visit(this, idt);
		return null;
	}

	public Reference visitCallExpr(CallExpr expr, IdentificationTable idt){
		expr.functionRef = expr.functionRef.visit(this, idt);
		return null;
	}

	public Reference visitLiteralExpr(LiteralExpr expr, IdentificationTable idt){ 
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
		return null; 
	}

	public Reference visitIntLiteral(IntLiteral num, IdentificationTable idt){
		return null;
	}

	public Reference visitBooleanLiteral(BooleanLiteral bool, IdentificationTable idt){
		return null;
	}
	
	public Reference visitNullLiteral(NullLiteral nul, IdentificationTable idt) {
		return null;
	}


	//{pa3 added}
	public Reference visitIdentifier(Identifier id, IdentificationTable idt){
		int retrieve_status = idt.retrieve_and_bind(id); 

		if (retrieve_status < 0){
			pushError("id cannot be resolved, it may be undeclared", id, true);
		}

		return null; 
	}

	public int tryVisitIdentifier(Identifier id, IdentificationTable idt){
		return idt.retrieve_and_bind(id);
	}

	public int tryVisitIdentifier(Identifier id, IdentificationTable idt, String LookUpName){
		return idt.retrieve_and_bind(id, LookUpName);
	}


	@Override
	public Reference visitVarDeclListStmt(VarDeclListStmt stmt, IdentificationTable idt) {
		VarDecl varDeclTraversal = null;
		Expression initTraveral = null;
		for (int i = 0; i < stmt.vdList.size(); i++){
			varDeclTraversal = stmt.vdList.get(i);
			initTraveral = stmt.initList.get(i);
			
			varDeclTraversal.visit(this, idt);
			if (initTraveral == null){
				varDeclTraversal.isInitialized = false;
			}
			else {
				initTraveral.visit(this, idt);
				varDeclTraversal.isInitialized = true;
			}
		}
		return null;
	}


	public Reference visitAssignExpr(AssignExpr expr, IdentificationTable idt) {
		expr.val.visit(this, idt); 
		expr.ref = expr.ref.visit(this, idt); 
		
		Declaration refDecl = expr.ref.getDecl();
		if (refDecl instanceof VarDecl){
			((VarDecl)refDecl).isInitialized = true;
		}
		
		if (expr.ref instanceof ArrayLengthRef){
			pushError("*** The length of an array cannot be assigned", currentClass.id);
		}
		return null;
	}


	public Reference visitTypeExpr(TypeExpr expr, IdentificationTable idt) {
		return expr.type.visit(this, idt);
	}

	
	//Three initial Reference. ExprRef, SimpleRef, MethodRef..SimpleRef identifies to LocalRef
	@Override
	public Reference visitExprRef(ExprRef ref, IdentificationTable idt) {
		ref.expr.visit(this, idt);
		return ref;
	}
	
	@Override
	public Reference visitFieldRef(FieldRef ref, IdentificationTable idt) { 
		return ref; //generate from simpleRef
	}

	@Override
	public Reference visitMethodRef(MethodRef ref, IdentificationTable idt) {
		FuncSigContainer mdCallSigBuilder = new FuncSigContainer(ref.simpleId);
		String lookUpName = null;
		for(Expression e: ref.argList){
			e.visit(this, idt);
			mdCallSigBuilder.addParaType(e.peekType());
		}
		lookUpName = mdCallSigBuilder.toEncodedName();
		if (ref.simpleId.resolve_and_bind(idt, lookUpName) < 0){
			pushError("***===IdChekcer: visitMethodRef Falied, " +
					"ID canot be resolved: (" + lookUpName + ")", ref.simpleId);
			
		}
		StaticCheckWithDecl(ref.simpleId, isInStaticMethod);
		return ref;
		
	}
	
	/**
	 * Input: SimpleRef, 
	 * @return (1)ClassRef, (2) FieldRef (3) LocalRef  
	 */
	@Override
	public Reference visitSimpleRef(SimpleRef ref, IdentificationTable idt){
		Identifier simpleId = ref.simpleId;
		simpleId.visit(this, idt); //resolving ID //need to check static /private
		Declaration refDecl = simpleId.declBinding;
		
		if (refDecl instanceof ClassDecl){
			return new ClassRef(simpleId, dummyPos);
		}
		else if (refDecl instanceof FieldDecl){
			StaticCheckWithDecl(refDecl, isInStaticMethod);
			return new FieldRef(simpleId, dummyPos);
		}
		else if (refDecl instanceof ParameterDecl){
			return new LocalRef(simpleId, dummyPos);
		}
		else if (refDecl instanceof VarDecl){
			return new LocalRef(simpleId, dummyPos);
		}
		else{ //no others
			pushError("***===IdChecker: visitSimpleRef(): Resolve SimpleID falied", simpleId);
			return null;
		}
	}
	
	public boolean StaticCheckWithDecl(MemberDecl accessMemDecl, boolean isStaticContext){
		if (isStaticContext && !accessMemDecl.isStatic){
			pushError("***===IdChecker: StaticCheck(MemberDecl): " +
					"Cannot access a non-static member under a static context: ", accessMemDecl.id);
			return false;
		}
		return true;
	}
	
	public boolean StaticCheckWithDecl(Declaration accessMemDecl, boolean isStaticContext){
		if (isStaticContext && 
				accessMemDecl instanceof MemberDecl &&
					!((MemberDecl)accessMemDecl).isStatic){
			pushError("***===IdChecker: StaticCheck(Declaration): " +
					"Cannot access a non-static member under a static context: ", accessMemDecl.id);
			return false;
		}
		return true;
	}
	public boolean StaticCheckWithDecl(Identifier id, boolean isStaticContext){
		if (isStaticContext && 
				id.declBinding instanceof MemberDecl &&
				!((MemberDecl)id.declBinding).isStatic){
			pushError("***===IdChecker: StaticCheck(Declaration): " +
					"Cannot access a non-static member under a static context: ", id);
			return false;
		}
		return true;
	}

	public Reference visitLocalRef(LocalRef ref, IdentificationTable idt) { // looking for at 3, 4+
		return ref;
	}

	public Reference visitThisRef(ThisRef ref, IdentificationTable idt) { //looking for at Level 2 exactly
		ref.simpleId.declBinding = 
				new FieldDecl(false, false, currentClass.sampleInstanceType, ref.simpleId, dummyPos);
		return ref;
	}

	public Reference visitClassRef(ClassRef ref, IdentificationTable idt) { //located at level 1
		return ref;
	}
	//Reference


	@Override
	public Reference visitSubFieldRef(SubFieldRef ref, IdentificationTable idt) {
		ref.preRef = ref.preRef.visit(this, idt);

		Type preType = ref.preRef.getType();
		IdentificationTable preIDT = ref.preRef.fetchIDT();

		boolean is_preRef_domestic = preType.equals(currentClass.sampleInstanceType);
		boolean is_preRef_ClassRef = preType.typeKind == TypeKind.CLASS_DEF ;

		ref.subID.visit(this, preIDT); //with all key=String to Member Decl
		PrivateCheckWithDecl(ref.subID, is_preRef_domestic);
		StaticCheckWithDecl(ref.subID, is_preRef_ClassRef);
		
		if (preType instanceof ArrayType){
			return new ArrayLengthRef(ref.preRef, dummyPos);
		}

		return ref;
	}
	
	public boolean PrivateCheckWithDecl(MemberDecl accessMemDecl, boolean isDomesticContext){
		if (!isDomesticContext && accessMemDecl.isPrivate){
			pushError("***===IdChecker: PrivateCheckWithDecl(MemberDecl): " +
					"Cannot access a private member under outside a class: ", accessMemDecl.id);
			return false;
		}
		return true;
	}
	
	public boolean PrivateCheckWithDecl(Declaration accessMemDecl, boolean isDomesticContext){
		if (!isDomesticContext && 
				accessMemDecl instanceof MemberDecl &&
					((MemberDecl)accessMemDecl).isPrivate){
			pushError("***===IdChecker: PrivateCheckWithDecl(MemberDecl): " +
					"Cannot access a private member under outside a class: ", accessMemDecl.id);
			return false;
		}
		return true;
	}
	
	public boolean PrivateCheckWithDecl(Identifier id, boolean isDomesticContext){
		if (!isDomesticContext && 
				id.declBinding instanceof MemberDecl &&
				((MemberDecl)id.declBinding).isPrivate){
			pushError("***===IdChecker: PrivateCheckWithDecl(MemberDecl): " +
					"Cannot access a private member under outside a class: ", id);
			return false;
		}
		return true;
	}


	@Override
	public Reference visitSubMethodRef(SubMethodRef ref, IdentificationTable idt) {
		ref.preRef = ref.preRef.visit(this, idt);
		Type preType = ref.preRef.getType();
		IdentificationTable preIDT = ref.preRef.fetchIDT();
		
		boolean is_preRef_domestic = preType.equals(currentClass.sampleInstanceType);
		boolean is_preRef_ClassRef = preType.typeKind == TypeKind.CLASS_DEF ;

		FuncSigContainer mdCallSigBuilder = new FuncSigContainer(ref.subID);
		String lookUpName = null;

		for (Expression e: ref.argList) { //does not work here
			e.visit(this, idt); 
			mdCallSigBuilder.addParaType(e.peekType());
		}
		lookUpName = mdCallSigBuilder.toEncodedName();
		if (ref.subID.resolve_and_bind(preIDT, lookUpName) < 0){
			pushError("***===IdChekcer: visitSubMethodRef Falied, " +
					"ID canot be resolved: (" + lookUpName + ")", ref.subID);
		}

		PrivateCheckWithDecl(ref.subID, is_preRef_domestic);
		StaticCheckWithDecl(ref.subID, is_preRef_ClassRef);

		return ref;

	}
	
	@Override
	public Reference visitIndexedRef(IndexedRef ir, IdentificationTable idt) {
		ir.preRef = ir.preRef.visit(this, idt); 
		ir.indexExpr.visit(this, idt);
		return ir;
	}
	
	@Override
	public Reference visitArrayLengthRef(ArrayLengthRef ref, IdentificationTable idt) {
		return ref; //generate here
	}

}