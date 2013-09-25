package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * The TypeChecker here specialize in type checking
 * It takes the decorated AST as its input, which is done by the IdChecker
 * @author duozhao
 *
 */
public class TypeChecker implements Visitor<Type, Type>{

	private SourcePosition dummyPos = new SourcePosition();

	private AST decoratedAST; //from Id checker
	private ErrorReporter reporter;
	private int numOfErrors;

	private ClassDecl currentClass; //for the tracking of the current context of class


	public TypeChecker (AST decoratedAST, ErrorReporter reporter) {
		this.reporter = reporter;
		this.decoratedAST = decoratedAST;
	}

	public void pushError(String msg, Identifier id){
		numOfErrors ++;

		String errClassSpelling = (currentClass == null || currentClass.id == null)? 
				"Out of class" : currentClass.id.spelling;

		System.out.println("*** " + "Error " + numOfErrors + ": " 
				+ msg + " :Token (" + id.spelling +"), from class: (" + errClassSpelling + ")");
	}

	public void check() { //driver
		decoratedAST.visit(this, null);


		if (numOfErrors > 0){
			System.exit(4);
		}
	}

	@Override
	public Type visitPackage(Package prog, Type arg) {
		for (ClassDecl c: prog.classDeclList){
			c.visit(this, arg);
		}
		return null;
	}

	@Override
	public Type visitClassDecl(ClassDecl clas, Type arg) {
		currentClass = clas;
		clas.id.visit(this, arg);
		for (FieldDecl f: clas.fieldDeclList){
			f.visit(this, arg);
		}
		for (MethodDecl m: clas.methodDeclList){
			m.visit(this, arg);
		}
		return null;
	}

	@Override
	public Type visitFieldDecl(FieldDecl fd, Type arg) {
		fd.id.visit(this,arg);
		Type FieldType = fd.type.visit(this, arg);

		if (FieldType.typeKind == TypeKind.VOID){
			pushError("FieldDecl cannot have a void type", fd.id);
		}

		return fd.type;
	}

	@Override
	public Type visitMethodDecl(MethodDecl md, Type arg) {
		md.id.visit(this, arg);
		Type expectedReturnType = md.type;

		if (md.id.spelling.contentEquals("main")){
			ParameterDecl decl = md.parameterDeclList.get(0); //String[] args

			Type mainParaType = decl.type.visit(this, arg); //modify to an array of unsupported type //migrate to type checking 

			if (!(mainParaType instanceof ArrayType)){
				pushError("main method must have exactly String[] args: not an array", md.id);
			}
			else if (((ArrayType) mainParaType).eltType.typeKind != TypeKind.UNSUPPORTED ){
				pushError("main method must have exactly String[] args: not String", md.id);
			}

		}

		for (ParameterDecl pd: md.parameterDeclList) {
			pd.visit(this, arg);
		}
		for (Statement s: md.statementList) {
			s.visit(this, arg);
		}

		if (expectedReturnType.typeKind == TypeKind.VOID ){
			if (md.returnExp != null){
				pushError("void cannot return a type", md.id);
			}
		}
		else {
			if (md.returnExp == null){
				pushError("non-void must have a return", md.id);
			}
			else{
				Type returnedType = md.returnExp.visit(this, arg);
				//System.out.println("===342324x=== Return Type" + md.returnExp);
				if (! returnedType.equals(expectedReturnType)){
					pushError("return type does not match", md.id);
				}
			}
		}
		return md.type;
	}

	@Override
	public Type visitMethodDeclHead(MethodDecl md, Type arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visitMethodDeclBody(MethodDecl md, Type arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visitParameterDecl(ParameterDecl pd, Type arg) {
		// TODO Auto-generated method stub
		pd.id.visit(this, arg);
		pd.type.visit(this, arg);

		if (pd.type.typeKind == TypeKind.VOID){
			pushError("ParameterDecl cannot have a void type", pd.id);
		}

		return pd.type;
	}

	@Override
	public Type visitVarDecl(VarDecl vd, Type arg) {
		vd.id.visit(this, arg);
		Type localDeclType = vd.type.visit(this, arg);

		if (localDeclType.typeKind == TypeKind.VOID){
			pushError("Local variable cannot be declared as a void type", vd.id);
		}

		return vd.type; //is declaration referred to type??
	}

	@Override
	public Type visitBaseType(BaseType type, Type arg) {
		return type;
	}

	@Override
	public Type visitClassType(ClassType type, Type arg) {
		type.name.visit(this, arg);
		return type;
	}

	@Override
	public Type visitArrayType(ArrayType type, Type arg) {
		type.eltType.visit(this, arg);
		return type;
	}

	@Override
	public Type visitErrorType(ErrorType type, Type arg) {
		return type;
	}

	@Override
	public Type visitUnsupportedType(UnsupportedType type, Type arg) {
		return type;
	}

	@Override
	public Type visitBlockStmt(BlockStmt stmt, Type arg) {
		for (Statement s: stmt.sl) {
			s.visit(this, arg);
		}
		return null;
	}

	@Override
	public Type visitVardeclStmt(VarDeclStmt stmt, Type arg) {
		Type declaredType = stmt.varDecl.visit(this, arg);	
		if (stmt.initExp != null){
			Type initType = stmt.initExp.visit(this, arg);
			//checking compatibility.
			if (!declaredType.equals(initType)){
				pushError("visitVardeclStmt Uncompatible ", stmt.varDecl.id);
			}
		}

		return null;
	}

	@Override
	public Type visitAssignStmt(AssignStmt stmt, Type arg) {
		Type refType = stmt.ref.visit(this, arg);
		Type valType = stmt.val.visit(this, arg);
		if (!refType.equals(valType)){
			//			if (refType.typeKind == TypeKind.CLASS){
			//				System.out.println("Assignee (" + ((ClassType)refType).name.spelling + ")");
			//				System.out.println("Assigner (" + ((ClassType)valType).name.spelling + ")");
			//			}
			pushError("Assignment type incompatible: " + refType + "vs" + valType, new Identifier("_dummyId", dummyPos));
			System.exit(4);
		}
		return null;
	}

	@Override
	public Type visitCallStmt(CallStmt stmt, Type arg) {
		Reference mRef = stmt.methodRef; //returns to its declaration. 
		ExprList al = stmt.argList;
		if (mRef instanceof SimpleRef){
			SimpleRef def = (SimpleRef) mRef;
			Declaration mRefDecl = def.simpleId.declBinding;
			if (mRefDecl instanceof MethodDecl){
				MethodDecl mdl = (MethodDecl) mRefDecl;
				ParameterDeclList fpl = mdl.parameterDeclList; //formal parameter list

				if (fpl.size() != al.size()){
					pushError("different size between arguments and formal parameter", new Identifier("_dummyId", dummyPos));
				}
				for (int i = 0; i < al.size(); i++){
					Type argType = al.get(i).visit(this, arg);
					Type fpType = fpl.get(i).type;
					if (!argType.equals(fpType)){
						pushError("Incompatible between arguments and parameter at " + i, new Identifier("_dummyId", dummyPos));
					}
				}
			}
			else {
				pushError("call to a non-method", new Identifier("_dummyID", dummyPos));
			}
		}
		return null;
	}

	@Override
	public Type visitIfStmt(IfStmt stmt, Type arg) {
		Type condType = stmt.cond.visit(this, arg); //must be boolean type
		if (condType == null ||
				condType.typeKind != TypeKind.BOOLEAN ){
			pushError("Condition in if is not a boolean type", new Identifier("_dummyID", dummyPos));
			System.exit(4);
		}

		stmt.thenStmt.visit(this, arg); //not initialzation statement

		if (stmt.elseStmt != null){
			stmt.elseStmt.visit(this, arg);
		}

		return null;
	}

	@Override
	public Type visitWhileStmt(WhileStmt stmt, Type arg) {
		Type stmtType = stmt.cond.visit(this, arg);  //boolean type
		if (stmtType == null ||
				stmtType.typeKind != TypeKind.BOOLEAN){
			System.out.println("***while condition is not a boolean type");
			System.exit(4);
		}
		stmt.body.visit(this, arg);  //restriction for initialization statement
		return null;
	}

	@Override
	public Type visitUnaryExpr(UnaryExpr expr, Type arg) {
		// TODO Auto-generated method stub
		String op = expr.operator.spelling;
		Type exprType = expr.expr.visit(this, arg);
		if (op.contentEquals("-")){ //from Int to Int
			if (exprType.typeKind == TypeKind.INT){
				return new BaseType(TypeKind.INT, dummyPos);
			}
			else{
				//return exprType;
				pushError("after '-' is Not Integer: " + expr, new Identifier(op, dummyPos));
				return new ErrorType(dummyPos);
			}
		}
		else if (op.contentEquals("!")){
			if (exprType.typeKind == TypeKind.BOOLEAN){
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				//return exprType;
				pushError("after '!' is Not a Boolean " + expr, new Identifier(op, dummyPos));
				return new ErrorType(dummyPos);
			}
		}
		else{
			pushError("Impossible is here, check parser, not - or !", new Identifier(op, dummyPos));
			return new ErrorType(dummyPos);
		}
	}

	@Override
	public Type visitBinaryExpr(BinaryExpr expr, Type arg) {
		String op = expr.operator.spelling;
		Type leftExpType = expr.left.visit(this, null);
		Type rightExpType = expr.right.visit(this, null);
		if (op.contentEquals("||") || op.contentEquals("&&")){
			if (leftExpType.typeKind == TypeKind.BOOLEAN &&
					rightExpType.typeKind == TypeKind.BOOLEAN){
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				pushError("|| or && with non-boolean type", new Identifier(op, dummyPos));
				return new ErrorType(dummyPos);
			}

		}
		else if (op.contentEquals("==") || op.contentEquals("!=")){ 
			//both boolean and Integer or even Class type Array type
			if (leftExpType.equals(rightExpType)){ //require the same type
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				pushError("== or != with non-same type RHS and RHS", new Identifier(op, dummyPos));
				return new ErrorType(dummyPos);
			}
		}
		else if (op.contentEquals("<=") 
				|| op.contentEquals(">=")
				|| op.contentEquals(">")
				|| op.contentEquals("<")
				){
			if (leftExpType.typeKind == TypeKind.INT &&
					rightExpType.typeKind == TypeKind.INT){
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				pushError(">= <= > < Int by Int type", new Identifier(op, dummyPos));
				return new ErrorType(dummyPos);
			}
		}
		else if ( op.contentEquals("+")
				|| op.contentEquals("-")
				|| op.contentEquals("*")
				|| op.contentEquals("/")
				){
			if (leftExpType.typeKind == TypeKind.INT &&
					rightExpType.typeKind == TypeKind.INT){
				return new BaseType(TypeKind.INT, dummyPos);
			}
			else{
				pushError("+ - * / not Int by Int Type", new Identifier(op, dummyPos));
				return new ErrorType(dummyPos);
			}
		}else{
			pushError("Undefined opeator...", new Identifier(op, dummyPos));
			return new ErrorType(dummyPos);
		}
	}

	@Override
	public Type visitRefExpr(RefExpr expr, Type arg) {
		return expr.ref.visit(this, arg);
	}

	@Override
	public Type visitCallExpr(CallExpr expr, Type arg) {
		Reference fRef = expr.functionRef; //returns to its declaration. 
		ExprList al = expr.argList;
		if (fRef instanceof SimpleRef){
			SimpleRef def = (SimpleRef) fRef;
			Declaration mRefDecl = def.simpleId.declBinding;
			if (mRefDecl instanceof MethodDecl){
				MethodDecl mdl = (MethodDecl) mRefDecl;
				ParameterDeclList fpl = mdl.parameterDeclList; //formal parameter list

				if (fpl.size() != al.size()){
					pushError("different size between arguments and formal parameter", new Identifier("CallExpr", dummyPos));
				}
				for (int i = 0; i < al.size(); i++){
					Type argType = al.get(i).visit(this, arg);
					Type fpType = fpl.get(i).type;
					if (!argType.equals(fpType)){
						pushError("Incompatible between arguments and parameter at " + i, new Identifier("CallExpr", dummyPos));
					}
				}
			}
			else {
				pushError("call to a non-method", new Identifier("CallExpr", dummyPos));
				System.out.println("***====342x===call to a non-method");
				System.exit(4);
			}
		}
		return expr.getReturnType();
	}

	@Override
	public Type visitLiteralExpr(LiteralExpr expr, Type arg) { //integer
		return expr.literal.visit(this, arg);
	}

	@Override
	public Type visitNewObjectExpr(NewObjectExpr expr, Type arg) {
		expr.type = expr.classtype;
		return expr.classtype; //what's in new expression ?
	}

	@Override
	public Type visitNewArrayExpr(NewArrayExpr expr, Type arg) {
		expr.type = new ArrayType(expr.eltType, dummyPos);
		return expr.type;
	}

	@Override
	public Type visitQualifiedRef(QualifiedRef ref, Type arg) {
		System.out.println("***Not ready for type checking as qualified reference");
		return null;
	}//no use here

	@Override
	public Type visitIndexedRef(IndexedRef iRef, Type arg) {
		return iRef.getType();
	}

	@Override
	public Type visitLocalRef(LocalRef ref, Type arg) {
		return ref.getType(); //no need to visit
	}

	@Override
	public Type visitMemberRef(MemberRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitClassRef(ClassRef ref, Type arg) {
		return null; // cannot be a terminal reference
	}

	@Override
	public Type visitThisRef(ThisRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitDeRef(DeRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitIdentifier(Identifier id, Type arg) {
		return id.type; //should not be here
	}

	@Override
	public Type visitOperator(Operator op, Type arg) {
		return null;
	}

	@Override
	public Type visitIntLiteral(IntLiteral num, Type arg) {
		return new BaseType(TypeKind.INT, dummyPos);
	}

	@Override
	public Type visitBooleanLiteral(BooleanLiteral bool, Type arg) {
		return new BaseType(TypeKind.BOOLEAN, dummyPos);
	}

	@Override
	public Type visitArrayLengthRef(ArrayLengthRef arrlenRef, Type arg) {
		//pa4
		return new BaseType(TypeKind.INT, dummyPos);
	}

}
