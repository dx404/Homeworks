package miniJava.ContextualAnalyzer;

import miniJava.ErrorReporter;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token.OpName;

/**
 * The TypeChecker here specialize in type checking
 * It takes the decorated AST as its input, which is done by the IdChecker
 * @author duozhao
 *
 */
public class TypeChecker implements Visitor<Type, Type>{

	private SourcePosition dummyPos = SourcePosition.dummyPos;

	private AST decoratedAST; //from Id checker
	private ErrorReporter reporter;
	private int numOfErrors;

	private ClassDecl currentClass; //for the tracking of this object


	public TypeChecker (AST decoratedAST, ErrorReporter reporter) {
		this.reporter = reporter;
		this.decoratedAST = decoratedAST;
	}

	public void pushError(String msg, Identifier id){
		numOfErrors ++;

		String errClassSpelling = (currentClass == null || currentClass.id == null)? 
				"Out of class" : currentClass.id.spelling;

		System.out.println("***====Error: TypeChecker:" + numOfErrors + ": " 
				+ msg + " :Token (" + id.spelling +"), from class: (" + errClassSpelling + ")");
		System.exit(4);
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
		fd.type = fd.type.visit(this, arg);
		if (fd.type.typeKind == TypeKind.VOID){
			pushError("***====TypeCheker: visitParameterDecl: " +
					"VOID cannot be declared as a parameter", fd.id);
		}
		return fd.type;
	}

	boolean checkMain(MethodDecl md){
		if (md.isMain){
			if (md.type.typeKind != TypeKind.VOID){
				pushError("main method be of a void type", md.id);
			}
			if (md.encodedName.contentEquals("$main<String[]>")){
				if (((ArrayType)md.parameterDeclList.get(0).type).
						eltType.typeKind != TypeKind.UNSUPPORTED){
					pushError("***====TypeChecker: checkMain: " +
							"String in Para Decl is invalid " +
							"(" + md.encodedName + ")", md.id);
				}
			}
			else{
				pushError("***====TypeChecker: checkMain: " +
						"main method must have exactly String[] argument: " +
						"(" + md.encodedName + ")", md.id);
			}
		}
		return true;
	}
	@Override
	public Type visitMethodDecl(MethodDecl md, Type arg) {
		Type expectedReturnType = md.type;

		checkMain(md);

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
				pushError("***===Type Checker: visitMethodDecl: " +
						"non-void must have a return", md.id);
			}
			else{
				Type returnedType = md.returnExp.visit(this, arg);
				if (! returnedType.equals(expectedReturnType)){
					pushError("***===Type Checker: visitMethodDecl: " +
							"return type does not match with Decl", md.id);
				}
			}
		}
		return md.type;
	}

	@Override
	public Type visitMethodDeclHead(MethodDecl md, Type arg) {
		return null;
	}

	@Override
	public Type visitMethodDeclBody(MethodDecl md, Type arg) {
		return null;
	}

	@Override
	public Type visitParameterDecl(ParameterDecl pd, Type arg) {
		pd.type = pd.type.visit(this, arg);
		if (pd.type.typeKind == TypeKind.VOID){
			pushError("***====TypeCheker: visitParameterDecl: " +
					"VOID cannot be declared as a parameter", pd.id);
		}
		return pd.type;
	}

	@Override // does not apply
	public Type visitVarDecl(VarDecl vd, Type arg) {
		vd.id.visit(this, arg);
		vd.type = vd.type.visit(this, arg);

		if (vd.type.typeKind == TypeKind.VOID){
			pushError("***====TypeCheker: visitParameterDecl: " +
					"VOID cannot be declared as a Local variable", vd.id);
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
		if (type.typeKind == TypeKind.UNSUPPORTED){ //id checker marked
			return new UnsupportedType(type, dummyPos);
		}
		return type;
	}

	@Override
	public Type visitArrayType(ArrayType type, Type arg) {
		type.eltType = type.eltType.visit(this, arg);
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

	public Type visitAssignExpr(AssignExpr expr, Type arg) {
		Type refType = expr.ref.visit(this, arg);
		Type valType = expr.val.visit(this, arg);
		if (!valType.equals(refType)){
			pushError("***===Type Checker: visitAssignExpr: Assignment type incompatible: " +
					"(" + refType + " vs " + valType + ")", Identifier.dummyID);
		}
		return refType;
	}

	@Override
	public Type visitIfStmt(IfStmt stmt, Type arg) {
		Type condType = stmt.cond.visit(this, arg); //must be boolean type
		if (condType == null ||
				condType.typeKind != TypeKind.BOOLEAN ){
			pushError("Condition in if is not a boolean type", Identifier.dummyID);
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
			pushError("***===TypeChecker: visitWhileStmt: " +
					"condition is not a boolean type", Identifier.dummyID);
		}
		stmt.body.visit(this, arg);  //restriction for initialization statement
		return null;
	}

	@Override
	public Type visitUnaryExpr(UnaryExpr expr, Type arg) {
		Operator op = expr.operator;
		Type exprType = expr.expr.visit(this, arg);
		if (op.opKind == OpName.NEG){ //from Int to Int
			if (exprType.typeKind == TypeKind.INT){
				return expr.type = BaseType.IntTypeSample;
			}
			else{ //return exprType;
				pushError("***===TypeChecker: visitUnaryExpr: " +
						"after '-' is Not IntegerExpr. " , Identifier.dummyID);
				return expr.type = ErrorType.ErrorTypeSample;
			}
		}
		else if (op.opKind == OpName.NOT){
			if (exprType.typeKind == TypeKind.BOOLEAN){
				return expr.type = BaseType.BooleanTypeSample;
			}
			else{
				pushError("***===TypeChecker: visitUnaryExpr: " +
						"after '!' is Not a BooleanExpr ", Identifier.dummyID);
				return expr.type = ErrorType.ErrorTypeSample;
			}
		}
		else{
			pushError("***===TypeChecker: visitUnaryExpr: " +
					"Invalid UnaryExpr Operator: (" + op.spelling + ")" 
					+ op.opKind, Identifier.dummyID);
			return expr.type = ErrorType.ErrorTypeSample;
		}
	}

	@Override
	public Type visitBinaryExpr(BinaryExpr expr, Type arg) {
		Operator op = expr.operator;
		Type leftExpType =  expr.left.visit(this, null);
		Type rightExpType = expr.right.visit(this, null);

		expr.type = ErrorType.ErrorTypeSample;
		switch(expr.operator.opKind){
		case OR: case AND:
			if (leftExpType.typeKind == TypeKind.BOOLEAN &&
			rightExpType.typeKind == TypeKind.BOOLEAN){
				expr.type = BaseType.BooleanTypeSample;
			}
			break;

		case EQ: case NEQ: 
			if (leftExpType.equals(rightExpType)){ //require the same type
				expr.type = BaseType.BooleanTypeSample;
			}
			break;

		case LEQ: case LT: case GEQ: case GT: 
			if (leftExpType.typeKind == TypeKind.INT &&
			rightExpType.typeKind == TypeKind.INT){
				expr.type = BaseType.BooleanTypeSample;
			}
			break;

		case PLUS: case MINUS: case TIMES: case DIV:
			if (leftExpType.typeKind == TypeKind.INT &&
			rightExpType.typeKind == TypeKind.INT){
				expr.type = BaseType.IntTypeSample;
			}
			break;

		case INSTANCEOF:
			if (expr.left instanceof RefExpr && 
					expr.right instanceof TypeExpr){
				expr.type = BaseType.BooleanTypeSample;
			}
			break;

		default:
			expr.type = ErrorType.ErrorTypeSample;
			break;
		}

		if (expr.type.typeKind == TypeKind.ERROR){
			pushError("***====Type Cheker: visitBinaryExpr: (" +
					op.spelling + ") is undefined for " + 
					leftExpType + " by " + rightExpType, Identifier.dummyID);
		}

		return expr.type;
	}

	public Type visitRefExpr(RefExpr expr, Type arg) {
		return expr.type = expr.ref.visit(this, arg);
	}

	public Type visitIncrementExpr(IncrementExpr expr, Type arg) { //should be iniLiter
		Type exprType = expr.type = expr.ref.visit(this, arg);
		if (exprType.typeKind != TypeKind.INT){
			pushError("***====Type Checker: visitIncrementExpr: (" +
					expr.increOp.spelling + ") is undefined for: " + exprType, 
					currentClass.id);
		}
		return exprType;
	}

	public Type visitDecrementExpr(DecrementExpr expr, Type arg) {
		Type exprType = expr.type = expr.ref.visit(this, arg);
		if (exprType.typeKind != TypeKind.INT){
			pushError("***====Type Checker: visitIncrementExpr: (" +
					expr.decreOp.spelling + ") is undefined for: " + exprType, 
					currentClass.id);
		}
		return exprType;
	}

	@Override
	public Type visitCallExpr(CallExpr expr, Type arg) { //nothing left to check
		Reference fRef = expr.functionRef; //returns to its declaration. 
		return expr.type = fRef.getType();
	}

	@Override
	public Type visitLiteralExpr(LiteralExpr expr, Type arg) { //integer
		return expr.type = expr.literal.visit(this, arg);
	}

	@Override
	public Type visitNewObjectExpr(NewObjectExpr expr, Type arg) {
		return expr.type = expr.classtype;
	}

	@Override
	public Type visitNewArrayExpr(NewArrayExpr expr, Type arg) {
		return expr.type = new ArrayType(expr.eltType, dummyPos);
	}

	@Override
	public Type visitIndexedRef(IndexedRef iRef, Type arg) {
		return iRef.getType();
	}

	@Override
	public Type visitLocalRef(LocalRef ref, Type arg) {
		return ref.getType(); //no need to visit
	}

	@Override
	public Type visitClassRef(ClassRef ref, Type arg) {
		return ref.getType(); // cannot be a terminal reference
	}

	@Override
	public Type visitThisRef(ThisRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitIdentifier(Identifier id, Type arg) {
		return id.getType(); //should not be here
	}

	@Override
	public Type visitOperator(Operator op, Type arg) {
		return null;
	}

	@Override
	public Type visitIntLiteral(IntLiteral num, Type arg) {
		return BaseType.IntTypeSample;
	}

	@Override
	public Type visitBooleanLiteral(BooleanLiteral bool, Type arg) {
		return BaseType.BooleanTypeSample;
	}

	@Override
	public Type visitSimpleRef(SimpleRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitVoidType(VoidType type, Type arg) {
		return VoidType.VoidTypeSample;
	}

	@Override
	public Type visitExprStmt(ExprStmt stmt, Type arg) {
		stmt.expr.visit(this, arg);
		return null;
	}

	@Override
	public Type visitForStmt(ForStmt stmt, Type arg) {
		for (Statement init : stmt.init){
			init.visit(this, arg);
		}
		Type condType = stmt.cond.visit(this, arg);
		if (condType.typeKind != TypeKind.BOOLEAN){
			pushError("***====TypeChecker: visitForStmt: " +
					"forLoop condition ("  + condType + ") " +
					"cannot converted to a boolean type: ", currentClass.id);
		}
		stmt.forBody.visit(this, arg);
		for (Statement update : stmt.update){
			update.visit(this, arg);
		}
		return null;
	}

	public Type visitVarDeclListStmt(VarDeclListStmt stmt, Type arg) {
		for (int i = 0; i < stmt.vdList.size(); i++){
			Type typeDecl= stmt.vdList.get(i).visit(this, arg);
			Type typeInit = stmt.initList.get(i) == null? 
					null: stmt.initList.get(i).visit(this, arg);
			if (!( typeInit == null || typeInit.equals(typeDecl))){
				pushError("***====TypeChecker: visitVarDeclListStmt: ", stmt.vdList.get(i).id);
			}
		}
		return null;
	}


	public Type visitTypeExpr(TypeExpr expr, Type arg) {
		return expr.type = expr.type.visit(this, arg);
	}

	public Type visitNullLiteral(NullLiteral nul, Type arg) {
		Type nullType = new ClassType(new Identifier("null", dummyPos), dummyPos);
		nullType.typeKind = TypeKind.NULL;
		return nullType;
	}

	public Type visitNullStmt(NullStmt stmt, Type arg) {
		return null; //ignore null Statement, doing nothing
	}

	@Override
	public Type visitArrayLengthRef(ArrayLengthRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitFieldRef(FieldRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitMethodRef(MethodRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitSubFieldRef(SubFieldRef ref, Type arg) {
		return ref.getType();
	}

	@Override
	public Type visitSubMethodRef(SubMethodRef ref, Type arg) {

		return ref.getType();
	}

	@Override
	public Type visitExprRef(ExprRef ref, Type arg) {
		return ref.getType();
	}

}
