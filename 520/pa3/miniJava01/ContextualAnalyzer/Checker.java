//package miniJava.ContextualAnalyzer;
//
///**
// * here is the older part, for record only
// */
//import java.util.ArrayList;
//import miniJava.SyntacticAnalyzer.SourcePosition;
//import miniJava.AbstractSyntaxTrees.*;
//import miniJava.AbstractSyntaxTrees.Package;
//import miniJava.ErrorReporter;
//import miniJava.ContextualAnalyzer.IdentificationTable; 
////same package import needed?
//
//public class Checker implements Visitor<String, Object>{ 
//	//Object can be Type or declaration
//	//public class Checker implements Visitor<IdentificationTable, Reference>{
//	private IdentificationTable idTable;
//	//private Hash
//	
//	private SourcePosition dummyPos = new SourcePosition();
//	private ErrorReporter reporter;
//
//	private Package sourceAST;
//	private Package envAST;
//
//	public Checker (Package sourceAST, ErrorReporter reporter) {
//		this.reporter = reporter;
//		this.idTable = new IdentificationTable ();
//		this.sourceAST = sourceAST;
//		establishStdEnv();
//	}
//
//	public static void main(String[] args){
//		ErrorReporter rp = new ErrorReporter();
//		Checker ckr = new Checker(null, rp);
//		new ASTDisplay().showTree(ckr.envAST);
//
//		System.out.println(ckr.idTable.culumativeIdTable.size());
//	}
//
//	public void check() { //driver
//		sourceAST.visit(this, null);
//	}
//
//	private void establishStdEnv() { 
//		//pure side effect to establish level 0
//		MemberDecl mdPrint = new FieldDecl(
//				false, false, 
//				new BaseType(TypeKind.VOID, dummyPos), 
//				new Identifier("print", dummyPos), 
//				dummyPos);
//		ParameterDecl pdPrint = new ParameterDecl(
//				new BaseType(TypeKind.INT, dummyPos), 
//				new Identifier("n", dummyPos), 
//				dummyPos);
//		ParameterDeclList pdlPrint = new ParameterDeclList();
//		pdlPrint.add(pdPrint);
//		MethodDecl methodPrint = new MethodDecl(
//				mdPrint, pdlPrint, new StatementList(), null, dummyPos);
//
//		MemberDecl mdPrintln = new FieldDecl(
//				false, false, 
//				new BaseType(TypeKind.VOID, dummyPos), 
//				new Identifier("println", dummyPos), 
//				dummyPos);
//		ParameterDecl pdPrintln = new ParameterDecl(
//				new BaseType(TypeKind.INT, dummyPos), 
//				new Identifier("n", dummyPos), 
//				dummyPos);
//		ParameterDeclList pdlPrintln = new ParameterDeclList();
//		pdlPrintln.add(pdPrintln);
//		MethodDecl methodPrintln = new MethodDecl(
//				mdPrintln, pdlPrintln, new StatementList(), null, dummyPos);
//
//		MethodDeclList mdlForPrintStream = new MethodDeclList();
//		mdlForPrintStream.add(methodPrint);
//		mdlForPrintStream.add(methodPrintln);
//
//		ClassDecl miniJavaPrintStream = new ClassDecl(
//				new Identifier("_PrintSystem", dummyPos), 
//				new FieldDeclList(), 
//				mdlForPrintStream, 
//				dummyPos); 
//
//		FieldDecl miniJavaOut = 
//				new FieldDecl(false, true, 
//						new ClassType(new Identifier(
//								"_PrintStream", dummyPos), dummyPos), //to resolve?
//								new Identifier("out", dummyPos), 
//								dummyPos);
//		FieldDeclList fdlminiJavaSystem = new FieldDeclList();
//		fdlminiJavaSystem.add(miniJavaOut);
//		ClassDecl miniJavaSystem = new ClassDecl(
//				new Identifier("System", dummyPos), 
//				fdlminiJavaSystem, new MethodDeclList(),
//				dummyPos);
//
//		/**
//		 * for standard Environment package
//		 */
//		ClassDeclList cl = new ClassDeclList();
//		cl.add(miniJavaPrintStream);
//		cl.add(miniJavaSystem);
//		envAST = new Package(cl, dummyPos);
//
//		idTable.topLevelMap.put("_PrintStream", miniJavaPrintStream);
//		idTable.topLevelMap.put("System", miniJavaSystem);
//
//	}
//
//
//	// Package
//	public Object visitPackage(Package prog, String arg){
//		idTable.openScope();
//
//		ClassDeclList cl = prog.classDeclList;
//		for (ClassDecl c: cl){ //for each, at class name level-1 breath-first
//			c.id.type = new ClassType(c.id, dummyPos); //Associate with type or declaration
//			c.id.declBinding = c;  //one out of two. preferred
//			idTable.enter(c.id.spelling, c); //idTable in charge of duplicate and not found
//		}
//
//		for (ClassDecl c: cl){ //for each
//			c.visit(this, null);  //de facto call visitClassDecl();
//			//create level 2 identification table too...
//		}
//		
//		//**********new type   pre load; 
//		for (ClassDecl c: cl){
//			for (MethodDecl m: c.methodDeclList){
//				m.visit(this, null);
//			}
//		}
//
//		//idTable.closeScope(); ???
//		return null;
//	}
//
//	// Declarations
//	public Object visitClassDecl(ClassDecl clas, String arg){
//		//clas.id.visit(this, null); //visit Id may have resolve? No
//		//not necessary, already in the table and resolved
//
//		idTable.openScope(); //enter Level-2 also breath-first
//
//		//		clas.id.declBinding = clas;
//		//		clas.id.type = new ClassType(clas.id, dummyPos);
//		//		//The above two have no cross reference. 
//		//done in visitPackage
//
//		for (FieldDecl f: clas.fieldDeclList){
//			//f.visit(this, null);  //May have Class Type to resolve before enter.
//			f.type.visit(this, null); //resolve type
//			f.id.type = f.type;
//			f.id.declBinding = f;
//
//			if(f.isStatic && idTable.getTopLevel() > 0){
//				System.out.println("Non-static field");
//				System.exit(4);
//			}
//			idTable.enter(f.id.spelling, f);
//		}
//		for (MethodDecl m: clas.methodDeclList){ //binding declaration name
//			m.type.visit(this, null);
//			m.id.type = m.type;
//			m.id.declBinding = m;
//
//			if(m.isStatic && !m.id.spelling.equals("main")){
//				System.out.println("Non-static method other than main()");
//				System.exit(4);
//			}
//			idTable.enter(m.id.spelling, m);
//		}
//
//		//		for (FieldDecl f: clas.fieldDeclList){
//		//			f.visit(this, null); // fields may need to resolve
//		//		} //all fields are resolved
////		for (MethodDecl m: clas.methodDeclList){ //may have a lost to resolve 
////			m.visit(this, null); //resolve the remaining of methods declaration. 
////		} //should not been done here. 
//
//		//idTable.closeScope(); ???//
//		return null;
//	}
//
//	public Object visitFieldDecl(FieldDecl f, String arg){ //still at level 2
//		//nothing to do. all the work has been done by visitClassDecl
//
//		//f.type.visit(this, null); 
//		//Type fieldType = (Type) 
//		// what returns typekind.?? only need to resolve type
//		//Declaration fieldDecl = (Declaration) f.id.visit(this, null); //what returns
//		//		if(fieldDecl != null){
//		//			System.out.println("duplication declaration");//guaranted by table
//		//			System.exit(4);
//		//		}
//		//		else{
//		//			//f.id.declBinding = f; //this is for applied occurence, not for declaration
//		//			//idTable.enter(f.id.spelling, fieldDecl); //no need for enter already done in Filed list.
//		//		}
//
//		return null;
//	}
//
//	public Object visitMethodDecl(MethodDecl m, String arg){ //only left for level 3+
//		//m.type.visit(this, null); done by visitClassDecl
//		//m.id.visit(this, null); done by visitClassDecl
//
//		idTable.openScope();
//		ParameterDeclList pdl = m.parameterDeclList;
//		for (ParameterDecl pd: pdl) {
//			pd.visit(this, null);
//		}
//
//		idTable.openScope();
//		StatementList sl = m.statementList;
//		for (Statement s: sl) {
//			s.visit(this, null);
//		}
//		if (m.returnExp != null) {
//			m.returnExp.visit(this, null);
//		}
//
//		idTable.closeScope(); //for level 4
//		idTable.closeScope(); //for level level 3
//		return null;
//	}
//
//	public Object visitParameterDecl(ParameterDecl pd, String arg){
//		pd.type.visit(this, null);
//		pd.id.type = pd.type;
//		pd.id.declBinding = pd;
//		idTable.enter(pd.id.spelling, pd);
//		//pd.id.visit(this, null);
//		return null;
//	} 
//
//	public Object visitVarDecl(VarDecl vd, String arg){
//		vd.type.visit(this, null);
//		vd.id.type = vd.type;
//		vd.id.declBinding = vd;
//		idTable.enter(vd.id.spelling, vd);
//		//vd.id.visit(this, null);
//		return null;
//	}
//
//	// Statements
//	public Object visitBlockStmt(BlockStmt stmt, String arg){
//		idTable.openScope();
//		StatementList sl = stmt.sl;
//		for (Statement s: sl) {
//			s.visit(this, null);
//		}
//		idTable.closeScope();
//		return null;
//	}
//
//	public Object visitVardeclStmt(VarDeclStmt stmt, String arg){
//		stmt.varDecl.visit(this, null);	
//		if (stmt.initExp != null){
//			Type initExpType = (Type) stmt.initExp.visit(this, null);
//			if(!stmt.varDecl.type.equals(initExpType)){ //category instanceof ?
//				System.out.println("Type incompatible:.....visitVardeclStmt..");
//				System.exit(4);
//			}
//		} //need to check compatibility.
//
//		return null;
//	}
//
//	//	public boolean isTypeEquivalent(Type t1, Type t2){ 
//	//has been migrated to Type class
//	//		if(t1.typeKind != t2.typeKind){
//	//			return false;
//	//		}
//	//		
//	//		
//	//		return false;
//	//	}
//	///**********************define type equals and expression so on....done 
//	public Object visitAssignStmt(AssignStmt stmt, String arg){
//		Declaration stmtRefDecl = (Declaration) stmt.ref.visit(this, null); 
//		//Reference returns to its declaration
//		Type argType = (Type) stmt.val.visit(this, null); //Expression
//		//Triangle also checks about the if a variable vs const???
//		if (!stmtRefDecl.id.type.equals(argType)){ //id has type
//			System.out.println("Assignment failure....");
//			System.exit(4);
//		}		
//		return null;
//	}
//
//	public Object visitCallStmt(CallStmt stmt, String arg){
//		MethodDecl mRefDecl = (MethodDecl) stmt.methodRef.visit(this, null); //returns to its declaration. 
//		ParameterDeclList fpl = mRefDecl.parameterDeclList; //formal parameter list
//
//		ExprList al = stmt.argList;
//
//		//		for (Expression e: al) { //does not work here
//		//			e.visit(this, null); 
//		//			//here is needed to check actual parameter with formal parameter
//		//			//how to traverse two arrays?
//		//		}
//		if (fpl.size() != al.size()){
//			System.out.println("different size between arguments and formal parameter");
//			System.exit(4);
//		}
//		for (int i = 0; i < al.size(); i++){
//			Type argType = (Type) al.get(i).visit(this, null);
//			Type fpType = fpl.get(i).type;
//			if (!argType.equals(fpType)){
//				System.out.println("Incompatible between arguments and parameter at " + i);
//				System.exit(4);
//			}
//		}
//
//		return null;
//	}
//
//	public Object visitIfStmt(IfStmt stmt, String arg){
//		Type condType = (Type) stmt.cond.visit(this, null); //must be boolean type
//		if (condType == null ||
//				condType.typeKind != TypeKind.BOOLEAN ){
//			System.out.println("condition is not a boolean type");
//			System.exit(4);
//		}
//
//		if (stmt.thenStmt instanceof VarDeclStmt){
//			System.out.println("Can not declare in a branch");
//			System.exit(4);
//		}
//		stmt.thenStmt.visit(this, null); //not initialzation statement
//
//		if (stmt.elseStmt != null){
//			if (stmt.elseStmt instanceof VarDeclStmt){
//				System.out.println("Can not declare in a branch");
//				System.exit(4);
//			}
//			stmt.elseStmt.visit(this, null);
//		}
//
//		return null;
//	}
//
//	public Object visitWhileStmt(WhileStmt stmt, String arg){
//		Type stmtType = (Type) stmt.cond.visit(this, null);  //boolean type
//		if (stmtType == null ||
//				stmtType.typeKind != TypeKind.BOOLEAN){
//			System.out.println("while condition is not a boolean type");
//			System.exit(4);
//		}
//
//		if( stmt.body instanceof VarDeclStmt){ //Questions about     ================
//			System.out.println("Can not declare in a while branch");
//			System.exit(4);
//		}
//		stmt.body.visit(this, null);  //restriction for initialization statement
//		return null;
//	}
//
//	// Types
//	public Type visitBaseType(BaseType type, String arg){
//		//return null; // for base type nothing to resolve. 
//		return type; //why return typekind
//	}
//
//	public Type visitClassType(ClassType type, String arg){
//		Declaration classTypeNameDecl = 
//				(Declaration) type.name.visit(this, null); //resolve id visit identifier
//
//		//type.name.declBinding = classTypeNameDecl; done by visit identifier
//
//		if(classTypeNameDecl == null){
//			System.out.println("Class name cannot be resolved");
//			System.exit(4);
//		}
//
//		return type;
//	}
//
//	public Type visitArrayType(ArrayType type, String arg){
//		Declaration arrayTypeNameDecl = 
//				(Declaration) type.eltType.visit(this, null); //resolve eltType[]
//
//		if(arrayTypeNameDecl == null){
//			System.out.println("array element name cannot be resolved");
//			System.exit(4);
//		}
//
//		return type;
//	}
//
//	@Override
//	public Type visitErrorType(ErrorType type, String arg) {
//		// TODO Auto-generated method stub
//		return new ErrorType(dummyPos);
//	}
//
//	@Override
//	public Type visitUnsupportedType(UnsupportedType type, String arg) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	// Expressions
//	public Object visitUnaryExpr(UnaryExpr expr, String arg){
//		String op = (String) expr.operator.visit(this, null);
//		Type exprType = (Type) expr.expr.visit(this, null);
//		if (op == "-"){ //from Int to Int
//			if (exprType.typeKind == TypeKind.INT){
//				return new BaseType(TypeKind.INT, dummyPos);
//			}
//			else{
//				//return exprType;
//				System.out.println("after '-' is Not Integer");
//				System.exit(4);
//				return new ErrorType(dummyPos);
//			}
//		}
//		else if (op == "!"){
//			if (exprType.typeKind == TypeKind.BOOLEAN){
//				return new BaseType(TypeKind.BOOLEAN, dummyPos);
//			}
//			else{
//				//return exprType;
//				System.out.println("after '!' is Not Boolean");
//				System.exit(4); //change to ErrorType later. 
//				return new ErrorType(dummyPos);
//			}
//		}
//		else{
//			System.out.println("shoud not be here, check parser, not - or !");
//			return new ErrorType(dummyPos);
//		}
//
//	}
//
//	public Object visitBinaryExpr(BinaryExpr expr, String arg){
//		String op = (String) expr.operator.visit(this, null);
//		Type leftExpType = (Type) expr.left.visit(this, null);
//		Type rightExpType = (Type) expr.right.visit(this, null);
//		if (op == "||" || op == "&&"){
//			if (leftExpType.typeKind == TypeKind.BOOLEAN &&
//					rightExpType.typeKind == TypeKind.BOOLEAN){
//				return new BaseType(TypeKind.BOOLEAN, dummyPos);
//			}
//			else{
//				System.out.println("|| or && with non-boolean type");
//				System.exit(4);
//				return new ErrorType(dummyPos);
//			}
//
//		}
//		else if (op == "==" || op == "!=" ){ 
//			//both boolean and Integer or even Class type Array type
//			if (leftExpType.equals(rightExpType)){ //require the same type
//				return new BaseType(TypeKind.BOOLEAN, dummyPos);
//			}
//			else{
//				System.out.println("== or != with non-same type RHS and RHS");
//				System.exit(4);
//				return new ErrorType(dummyPos);
//			}
//		}
//		else if (op == "<=" || op == ">=" || 
//				op == "<" || op == ">"){
//			if (leftExpType.typeKind == TypeKind.INT &&
//					rightExpType.typeKind == TypeKind.INT){
//				return new BaseType(TypeKind.BOOLEAN, dummyPos);
//			}
//			else{
//				System.out.println(">= <= > < Int type");
//				System.exit(4);
//				return new ErrorType(dummyPos);
//			}
//		}
//		else if (op == "+" || op == "-" ||
//				op == "*" || op == "/"){
//			if (leftExpType.typeKind == TypeKind.INT &&
//					rightExpType.typeKind == TypeKind.INT){
//				return new BaseType(TypeKind.INT, dummyPos);
//			}
//			else{
//				System.out.println("+ - * / Int type");
//				System.exit(4);
//				return new ErrorType(dummyPos);
//			}
//		}else{
//			System.out.println("Undefined opeator......");
//			System.exit(4);
//			return new ErrorType(dummyPos);
//		}
//
//		//	return null;
//	}
//
//	public Type visitRefExpr(RefExpr expr, String arg){
//		return (Type) expr.ref.visit(this, null);
//	}
//
//	public Type visitCallExpr(CallExpr expr, String arg){
//		MethodDecl mRefDecl = (MethodDecl) expr.functionRef.visit(this, null);
//		ParameterDeclList fpl = mRefDecl.parameterDeclList;
//
//		ExprList al = expr.argList;
//		if (fpl.size() != al.size()){
//			System.out.println("different size between arguments and formal parameter");
//			System.exit(4);
//		}
//
//		for (int i = 0; i < al.size(); i++){
//			Type argType = (Type) al.get(i).visit(this, null);
//			Type fpType = fpl.get(i).type;
//			if (!argType.equals(fpType)){
//				System.out.println("Incompatible between arguments and parameter at " + i);
//				System.exit(4);
//			}
//		}
//
//		return mRefDecl.type; // similar to callStmt() except for return type
//		/*
//
//		public Object visitCallStmt(CallStmt stmt, String arg){
//		MethodDecl mRefDecl = (MethodDecl) stmt.methodRef.visit(this, null); //returns to its declaration. 
//		ParameterDeclList fpl = mRefDecl.parameterDeclList; //formal parameter list
//
//		ExprList al = stmt.argList;
//		if (fpl.size() != al.size()){
//			System.out.println("different size between arguments and formal parameter");
//			System.exit(4);
//		}
//		for (int i = 0; i < al.size(); i++){
//			Type argType = (Type) al.get(i).visit(this, null);
//			Type fpType = fpl.get(i).type;
//			if (!argType.equals(fpType)){
//				System.out.println("Incompatible between arguments and parameter at " + i);
//				System.exit(4);
//			}
//		}
//
//		return null;}
//		 */
//	}
//
//	public Type visitLiteralExpr(LiteralExpr expr, String arg){ 
//		//not terminal 
//		return (Type) expr.literal.visit(this, null);
//	}
//
//	public Type visitNewArrayExpr(NewArrayExpr expr, String arg){
//		Type elementType = (Type) expr.eltType.visit(this, null);
//		Type sizeExprType = (Type) expr.sizeExpr.visit(this, null);
//		if (sizeExprType.typeKind != TypeKind.INT){
//			System.out.println("non-int index in new array");
//			System.exit(4);
//		}
//
//		return new ArrayType(elementType, dummyPos);
//	}
//
//	public Type visitNewObjectExpr(NewObjectExpr expr, String arg){
//		return (Type) expr.classtype.visit(this, null); //return a type; 
//	}
//
//
//
//
//
//	// Terminals
//
//	public String visitOperator(Operator op, String arg){
//		return op.spelling; //must refine and re-distinguish Operating returns spelling ??????
//	}
//
//	public Type visitIntLiteral(IntLiteral num, String arg){
//		//return null;
//		return new BaseType(TypeKind.INT, dummyPos);
//	}
//
//	public Type visitBooleanLiteral(BooleanLiteral bool, String arg){
//		return new BaseType(TypeKind.BOOLEAN, dummyPos);
//	}
//	//\begin{pa3 added}
//	public Object visitIdentifier(Identifier id, String arg){
//		Declaration binding = idTable.retrieve(id.spelling); 
//		//leave 3+ has been considered in idTable class
//		if (binding != null){
//			id.declBinding = binding;
//		}
//		return binding; //return to the corresponding declaration
//	}
//
//	// References /******* all references return to their declarations
//
//	public Declaration visitIndexedRef(IndexedRef ir, String arg) {
//		Declaration refDecl = (Declaration) ir.ref.visit(this, null); 
//		
//		Type indexType = (Type) ir.indexExpr.visit(this, null);
//		if (indexType.typeKind != TypeKind.INT){
//			System.out.println("Invalid index in index Reference");
//			System.exit(4);
//		}
//		return refDecl;
//	}
//	
//	public Declaration visitQualifiedRef(QualifiedRef qr, String arg) { 
//		//modify to make a list of references
//		if(qr.thisRelative){
//			//ThisRef thisRef = new ThisRef(id, posn)
//		}
//		else{
//			
//		}
//		IdentifierList ql = qr.qualifierList;
//		if (ql.size() > 0)
//			ql.get(0).visit(this, null);
//		for (int i = 1; i < ql.size(); i++) {
//			ql.get(i).visit(this, null);
//		}
//		return null;
//	}
//
//	@Override //
//	public Declaration visitLocalRef(LocalRef ref, String arg) { // looking for at 3, 4+
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Declaration visitThisRef(ThisRef ref, String arg) { //looking for at Level 2 exactly
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public Declaration visitMemberRef(MemberRef ref, String arg) { // located at level 2
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Declaration visitClassRef(ClassRef ref, String arg) { //located at level 1
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Declaration visitDeRef(DeRef ref, String arg) { //
//		// TODO Auto-generated method stub
//		return null;
//	}
//	//\end{pa3 added}
//
//	@Override
//	public Object visitMethodDeclHead(MethodDecl md, String arg) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visitMethodDeclBody(MethodDecl md, String arg) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
