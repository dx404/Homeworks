package miniJava.ContextualAnalyzer;

/**
 * This part of program is for the identification phase only
 * It takes the parsedAST as its input
 * It generates the decorated AST without type checking. 
 * @author duozhao
 *
 */

import java.util.HashMap;

import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.stdEnv.StdEnv;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.ErrorReporter;
import miniJava.ContextualAnalyzer.IdentificationTable; 

public class IdChecker implements Visitor<IdentificationTable, Reference>{

	private IdentificationTable idTable;
	private SourcePosition dummyPos = new SourcePosition();
	private ErrorReporter reporter;

	private AST envAST;
	private AST processingAST; //must pass in a parsed AST

	public StdEnv stdEnv;

	private Boolean isInStaticMethod; //can only reference static fields/methods
	private Boolean isInStaticView;
	private ClassDecl currentClass; //for the tracking of the current context of class

	private int numOfErrors;

	public IdChecker (AST parsedAST, ErrorReporter reporter) { //should be the abstract AST
		this.reporter = reporter;
		this.idTable = new IdentificationTable();
		this.processingAST = parsedAST; //for copy, impossible to do
		this.isInStaticMethod = null;
		this.isInStaticView = false;
		this.currentClass = null; //hasn't enter any class

		stdEnv = new StdEnv();
		this.envAST = stdEnv.envAST; //for checking only, also need identification

		numOfErrors = 0;
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

		return processingAST; //decorated AST
	}

	public void pushError(String msg, Identifier id){
		numOfErrors ++;

		String errClassSpelling = (currentClass == null || currentClass.id == null)? 
				"Out of class" : currentClass.id.spelling;

		System.out.println("*** " + "Error " + numOfErrors + ": " 
				+ msg + " :id (" + id.spelling +"), from class: (" + errClassSpelling + ")");
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
			c.enableClassMemDeclMap(idt.getTopLevel()); // Type are not resolved here, distinguish level 0 or 1
			idt.enterDecl(c);
			c.setIndex(cl_index++);
		}	

		for (ClassDecl c: cl){ //2nd pass
			currentClass = c;
			idt.openScope(c.getHashMap());
			Integer fIndex = 0, mIndex = 0;
			for(FieldDecl f : c.fieldDeclList){
				f.visit(this, idt);
				f.setIndex(fIndex++);
			}
			for(MethodDecl m : c.methodDeclList){
				m.visitHead(this, idt); //resolve method head (return type and method name)
				m.setIndex(mIndex++);
			}
			idt.closeScope();
		}

		for (ClassDecl c: cl){ //3nd pass
			currentClass = c;
			idt.openScope(c.getHashMap());
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
		//f.id has been pre-entered
		f.type.visit(this, idt); 
		return null; 
	}

	public Reference visitMethodDeclHead(MethodDecl m, IdentificationTable idt){
		//m.id has been pre-entered
		if (m.id.spelling.contentEquals("main")){
			if (m.isPrivate){
				pushError("public modifier for main method is expected", m.id);
			}
			if (!m.isStatic){
				pushError("static modifier for main method is expected", m.id);
			}
			if (m.type.typeKind != TypeKind.VOID){
				pushError("void type is expected as a return type for main method", m.id);
			}

		}

		m.type.visit(this, idt);
		return null;
	}

	public Reference visitMethodDeclBody(MethodDecl m, IdentificationTable idt){
		//parameters + body
		m.isMain = m.id.spelling.contentEquals("main"); //pa4 added

		if (m.isMain && m.parameterDeclList.size() != 1){
			pushError("main method must have exactly one parameter", m.id);
		}

		isInStaticView = isInStaticMethod = m.isStatic;

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
		isInStaticView = false;

		return null;
	}

	public Reference visitParameterDecl(ParameterDecl pd, IdentificationTable idt){
		idt.enterDecl(pd);
		pd.type.visit(this, idt);
		pd.setId2Declbindings();

		return null;
	} 

	public Reference visitMethodDecl(MethodDecl m, IdentificationTable idt){ //only left for level 3+
		//not used here
		m.visitHead(this, idt);
		m.visitBody(this, idt);
		return null;
	}



	public Reference visitVarDecl(VarDecl vd, IdentificationTable idt){
		idt.enter(vd.id.spelling, vd);
		vd.type.visit(this, idt);
		vd.setId2Declbindings();
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
		stmt.initExp.visit(this, idt); //should be placed first like int m = m + 1; is not allowed here. 
		stmt.varDecl.visit(this, idt);	
		return null;
	}

	public Reference visitAssignStmt(AssignStmt stmt, IdentificationTable idt){
		stmt.val.visit(this, idt); //Expression
		stmt.ref = stmt.ref.visit(this, idt); 
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
		stmt.cond.visit(this, idt); //must be boolean type, checked in type checker

		if (stmt.thenStmt instanceof VarDeclStmt){
			VarDeclStmt thst = (VarDeclStmt) stmt.thenStmt;
			pushError("VarDeclStmt cannot exist as an entire single \"then\" branch", thst.varDecl.id);
		} 

		stmt.thenStmt.visit(this, idt); 

		if (stmt.elseStmt != null){
			if (stmt.elseStmt instanceof VarDeclStmt){
				VarDeclStmt elst = (VarDeclStmt) stmt.elseStmt;
				pushError("VarDeclStmt cannot exist as an entire single \"else\" branch", elst.varDecl.id);
			}
			stmt.elseStmt.visit(this, idt);
		}

		return null;
	}

	public Reference visitWhileStmt(WhileStmt stmt, IdentificationTable idt){
		stmt.cond.visit(this, idt);  //boolean type

		if (stmt.body instanceof VarDeclStmt){ //Questions about     ================
			VarDeclStmt bdst = (VarDeclStmt) stmt.body;
			pushError("VarDeclStmt cannot exist as an entire single \"while body\" branch", bdst.varDecl.id);
		}

		stmt.body.visit(this, idt);  //restriction for initialization statement

		return null;
	}

	// Types
	public Reference visitBaseType(BaseType type, IdentificationTable idt){
		return null; 
	}

	public Reference visitClassType(ClassType type, IdentificationTable idt){
		if (type.name.spelling.contentEquals("String")){
			type.typeKind = TypeKind.UNSUPPORTED; //mark as unsupported, still class type
			return null; //nothing is left to resolve
		}

		type.name.visit(this, idt); //resolve id visit identifier, should be a class name

		if (!(type.name.declBinding instanceof ClassDecl)) {//post type checking. 
			pushError("A Class Type is expected.", type.name);
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

	public Reference visitRefExpr(RefExpr expr, IdentificationTable idt){ //the critial step to make a change
		expr.ref = expr.ref.visit(this, idt); 
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
		return null; 
	}

	public Reference visitIntLiteral(IntLiteral num, IdentificationTable idt){
		return null;
	}

	public Reference visitBooleanLiteral(BooleanLiteral bool, IdentificationTable idt){
		return null;
	}


	//\begin{pa3 added}
	public Reference visitIdentifier(Identifier id, IdentificationTable idt){
		if (id.spelling.contentEquals("length")){
			return new ArrayLengthRef(null, dummyPos);
		}
		
		Declaration binding = idt.retrieve(id.spelling); 
		//leave 3+ has been considered in idt class
		if (binding != null){
			id.declBinding = binding;
			id.type = binding.type;    //may be null for class name. 

			if (binding instanceof LocalDecl){
				return new LocalRef(id, dummyPos);
			}
			else if (binding instanceof MemberDecl){
				MemberDecl memDecl = (MemberDecl) binding;
				if (isInStaticView != null && //if null, it is out side of any method. 
						isInStaticView &&
						!memDecl.isStatic){
					pushError("Unable to access a non-static member in a static method", id);
				}

				return new MemberRef(id, dummyPos);//change to a new IdT
			}
			else if (binding instanceof ClassDecl){//Class name, no type}
				return new ClassRef(id, dummyPos);
			}
			else {
				pushError("Undefined type of declaration, impossible type", id, true);
			}
		}
		else{
			pushError("id cannot be resolved, it may be undeclared", id, true);
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
		Reference modifiedRef; //for return

		Reference refHead; 

		IdentifierList ql = qr.qualifierList;
		boolean isCapableRef = false; //className or objects can continue ref
		boolean isDomestic = true; //for access private or public 
		isInStaticView = isInStaticMethod;

		ClassDecl runningClass = currentClass; //this is the class for record
		Identifier runningID = null;

		IdentificationTable jumpIdt = 
				new IdentificationTable(idt.getMapAtLevel(0));	

		jumpIdt.openScope(idt.getMapAtLevel(1)); //loading user defined class names

		jumpIdt.openScope();

		if (qr.thisRelative){
			isCapableRef = true; //this, Class Name, Object Name, Array Name can be referenced
			isDomestic = true;   // if may access private field ?
			isInStaticView = false; // cannot only access static member. 

			Identifier dummyId4This = new Identifier("_dummyThis", dummyPos);
			//****** subject to change with 
			dummyId4This.declBinding = runningClass; //need to find
			//****** subject to change with more subtle reference

			refHead = new ThisRef(dummyId4This, dummyPos); //may introduce a dummy identifier+

			jumpIdt.setEmptyTop(idt.getMapAtLevel(2)); //level 0 and 1 mute later
		}
		else {
			Identifier headId = ql.get(0);
			refHead = headId.visit(this, idt); //visit identifier return to its reference expr

			if (headId.declBinding == null){
				pushError("null head Id", headId);
				return null; //??
			}
			else if (headId.declBinding instanceof ClassDecl){ //if (headId.type == null){	//a class name, not reliable?
				isCapableRef = true;
				isDomestic = (headId.declBinding == runningClass)? true : false;
				isInStaticView = true; //isInStaticMethod = true;

				ClassDecl headClassDecl = (ClassDecl) headId.declBinding;
				jumpIdt.setEmptyTop(headClassDecl.getHashMap());
			}
			else if (headId.type.typeKind == TypeKind.UNSUPPORTED){ //for string
				isCapableRef = false;
			}
			else if(headId.type instanceof ClassType){
				isCapableRef = true;
				isInStaticView = false; //isInStaticMethod = false;

				ClassType headIdDeclType = (ClassType) headId.type;
				ClassDecl headIdToClass = (ClassDecl) headIdDeclType.name.declBinding;

				isDomestic = (headIdToClass.id.spelling.contentEquals(runningClass.id.spelling)) ? true : false;

				runningClass = headIdToClass;

				jumpIdt.setEmptyTop(headIdToClass.getHashMap()); //jumpIdt.set(2, headIdToClass.getHashMap());
			}
			else if (headId.type instanceof ArrayType){ //here may reference to length

			}

			else{ // including base type 
				//System.out.println("***===340958x==: Cannot continue with reference:");
				isCapableRef = false;
			}

		}
		modifiedRef = refHead;
		int startIndex = qr.thisRelative? 0 : 1;

		jumpIdt.set(0, new HashMap<String, Declaration>()); //mute it
		jumpIdt.set(1, new HashMap<String, Declaration>()); //mute it

		for (int i = startIndex; i < ql.size(); i++) { //local, only needs class member mappings

			Identifier subitrId = ql.get(i);
			if (!isCapableRef && !subitrId.spelling.contentEquals("length")){
				pushError("Last reference cannot continue with reference", subitrId);
				isInStaticView = isInStaticMethod;
				return modifiedRef;
			}
			Reference visitedIdRef = subitrId.visit(this, jumpIdt);
			if (visitedIdRef instanceof ArrayLengthRef){
				ArrayLengthRef arrlenRef = (ArrayLengthRef) visitedIdRef;
				arrlenRef.arrayRef = modifiedRef;
				return arrlenRef;
			}

			modifiedRef = new DeRef(modifiedRef, subitrId, dummyPos);
			jumpIdt.closeScope();
			//******************************
			jumpIdt.openScope();
			
			if (subitrId.declBinding != null && subitrId.declBinding instanceof MemberDecl){
				MemberDecl memDecl = (MemberDecl) subitrId.declBinding;
				if (isInStaticView && !memDecl.isStatic){
					pushError("cannot reference to a non-static member via ClassName", subitrId);
					isInStaticView = isInStaticMethod;
					return modifiedRef;
				}

				if (!isDomestic && memDecl.isPrivate){
					pushError("Cannot access a private member outside its class declaration", subitrId);
					isInStaticView = isInStaticMethod;
					return modifiedRef;
				}

				if (subitrId.type instanceof ClassType){
					isCapableRef = true;
					isInStaticView = false;

					ClassType subitrIdDeclType = (ClassType) subitrId.type;
					ClassDecl subitrIdToClass = (ClassDecl) subitrIdDeclType.name.declBinding;

					isDomestic = (subitrIdToClass.id.spelling.contentEquals(runningClass.id.spelling)) ? true : false;
					//impact the next
					runningClass = subitrIdToClass; //updating

					jumpIdt.setEmptyTop(subitrIdToClass.getHashMap()); // jumpIdt.set(2, subitrIdToClass.getHashMap());
				}
				else if (subitrId.declBinding!= null && subitrId.declBinding.type instanceof ArrayType){
					isCapableRef = true;
				}
				else {
					
					isCapableRef = false;
				}
			}
			else {
				pushError("Inpossible, resolved as a non-member declaration", subitrId);
			}


		}

		isInStaticView = isInStaticMethod;
		//Here is without resolved all id's but the first one hasn't been resolved 
		return modifiedRef;
	}


	public Reference visitQualifiedRef2(QualifiedRef qr, IdentificationTable idt) { 

		IdentifierList ql = qr.qualifierList;
		boolean isCapableRef = false; //className or objects can continue ref
		boolean isDomestic = true; //for access private or public 
		isInStaticView = isInStaticMethod;

		Reference culmulativeRef = null; //the refernence until the current point this is cumulative
		Identifier runningID = null; //the current processing identifier
		IdentificationTable runningIdt = idt; //based on the current identifier the IDTable

		if (qr.thisRelative){

			runningID = new Identifier("_dummyThis", dummyPos);
			runningID.declBinding = currentClass; //need to find
			runningID.type = currentClass.type;

			culmulativeRef = new ThisRef(runningID, dummyPos); //may introduce a dummy identifier+

			runningIdt = new  IdentificationTable(); // level 0
			runningIdt.openScope();  //leveo 0, 1
			runningIdt.openScope(idt.getMapAtLevel(2)); //level 0, 1, 2

			isCapableRef = true; //this, Class Name, Object Name, Array Name can be referenced
			isDomestic = true;   // if may access private field ?
			isInStaticView = false; // cannot only access static member. 
			//runningClass does not change
		}
		else if (ql.size() > 0){ //get the first
			runningID = ql.get(0); //reset 
			runningID.visit(this, runningIdt); 
			//check
			Declaration decl = runningID.declBinding;
			if (decl instanceof ClassDecl){ //class System{} System.out....
				culmulativeRef = new ClassRef(runningID, dummyPos); 

				isCapableRef = true; 
				isDomestic = (decl == currentClass)? true : false;
				isInStaticView = true;
			}
			else if (decl instanceof LocalDecl){ // no need to check
				culmulativeRef = new LocalRef(runningID, dummyPos); 

				Type localType = decl.type;
				if (localType.typeKind == TypeKind.CLASS){ // A a = new A(); a.b.c = 5;
					ClassType cType = (ClassType) localType;
					
					isCapableRef = true; 
					isDomestic = (cType.getDecl() == currentClass)? true : false;
					isInStaticView = false;
				}
				else if (localType.typeKind == TypeKind.ARRAY){ // int[] a = new int [5]; 
					isCapableRef = true; 
					isDomestic = true;
					isInStaticView = false;
				}
				else{ //base Type   // int x = 5;
					isCapableRef = false; 
					isDomestic = true;
					isInStaticView = false;
				}
			}
			else if (decl instanceof MemberDecl){
				MemberDecl md = (MemberDecl) decl;
				if (isInStaticView && !md.isStatic){
					pushError("cannot reference to a non-static member via ClassName", runningID);
					isInStaticView = isInStaticMethod;
					return culmulativeRef;
				}
				culmulativeRef = new MemberRef(runningID, dummyPos); 
			}
		}
		else{
			return null;
		}

		//		if (ql.size() > 0){
		//			runningIdt.set(0, new HashMap<String, Declaration>()); //mute it
		//			runningIdt.set(1, new HashMap<String, Declaration>()); //mute it
		//			
		//		}
		int start_index = qr.thisRelative ? 0 : 1;
		for (int i = start_index; i < ql.size(); i++) {
			if (!isCapableRef){
				pushError("Last reference cannot continue with reference", runningID);
				isInStaticView = isInStaticMethod;
				return culmulativeRef;
			}

			runningID = ql.get(i); //reset 
			runningID.visit(this, runningIdt); //return reference??
			//check
			Declaration decl = runningID.declBinding;

			if (decl instanceof MemberDecl){
				if (i == 0){
					culmulativeRef = new MemberRef(runningID, dummyPos);
				}
				MemberDecl md = (MemberDecl) decl;
				if (!isDomestic && md.isPrivate){
					pushError("Cannot access a private member outside its class declaration", runningID);
					isInStaticView = isInStaticMethod;
					return culmulativeRef;
				}
				if (isInStaticView && !md.isStatic){
					pushError("cannot reference to a non-static member via ClassName", runningID);
					isInStaticView = isInStaticMethod;
					return culmulativeRef;
				}
			}
			else{
				//no others not a member declaration
			}

			culmulativeRef = new DeRef(culmulativeRef, runningID, dummyPos); //null
			runningIdt.closeScope();
		}


		return culmulativeRef;
	}

	public Reference visitLocalRef(LocalRef ref, IdentificationTable idt) { // looking for at 3, 4+
		return ref;
	}

	public Reference visitThisRef(ThisRef ref, IdentificationTable idt) { //looking for at Level 2 exactly
		return ref;
	}

	public Reference visitMemberRef(MemberRef ref, IdentificationTable idt) { // located at level 2
		return ref;
	}

	public Reference visitClassRef(ClassRef ref, IdentificationTable idt) { //located at level 1
		return ref;
	}

	public Reference visitDeRef(DeRef ref, IdentificationTable idt) { //
		return ref;
	}


	@Override
	public Reference visitArrayLengthRef(ArrayLengthRef arrlenRef,
			IdentificationTable arg) {
		// TODO Auto-generated method stub
		return null;
	}
}