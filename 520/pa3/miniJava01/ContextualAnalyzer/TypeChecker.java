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

	private Package decoratedAST; //from Id checker
	private ErrorReporter reporter;

	public TypeChecker (Package decoratedAST, ErrorReporter reporter) {
		this.reporter = reporter;
		this.decoratedAST = decoratedAST;
	}


	public void check() { //driver
		decoratedAST.visit(this, null);

		System.out.println("Type Checking is over, When you see this message, it succeed. ");
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
		fd.type.visit(this, arg);
		fd.id.visit(this,arg);
		return fd.type;
	}

	@Override
	public Type visitMethodDecl(MethodDecl md, Type arg) {
		md.type.visit(this, arg);
		md.id.visit(this, arg);
		for (ParameterDecl pd: md.parameterDeclList) {
			pd.visit(this, arg);
		}
		for (Statement s: md.statementList) {
			s.visit(this, arg);
		}
		if (md.returnExp != null) {
			md.returnExp.visit(this, arg);
		}

		//check return Expression with md.type??
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
		pd.type.visit(this, arg);
		pd.id.visit(this, arg);
		return pd.type;
	}

	@Override
	public Type visitVarDecl(VarDecl vd, Type arg) {
		// TODO Auto-generated method stub
		vd.type.visit(this, arg);
		vd.id.visit(this, arg);

		return vd.type; //is declaration referred to type??
	}

	@Override
	public Type visitBaseType(BaseType type, Type arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visitClassType(ClassType type, Type arg) {
		// TODO Auto-generated method stub
		type.name.visit(this, arg);
		return null;
	}

	@Override
	public Type visitArrayType(ArrayType type, Type arg) {
		// TODO Auto-generated method stub
		type.eltType.visit(this, arg);
		return null;
	}

	@Override
	public Type visitErrorType(ErrorType type, Type arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visitUnsupportedType(UnsupportedType type, Type arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visitBlockStmt(BlockStmt stmt, Type arg) {
		// TODO Auto-generated method stub
		for (Statement s: stmt.sl) {
			s.visit(this, arg);
		}
		return null;
	}

	@Override
	public Type visitVardeclStmt(VarDeclStmt stmt, Type arg) {
		// TODO Auto-generated method stub
		Type declaredType = stmt.varDecl.visit(this, arg);	
		if (stmt.initExp != null){
			Type initType = stmt.initExp.visit(this, arg);

			//checking compatibility.
			if (!declaredType.equals(initType)){
				System.out.print("===2342x===: visitVardeclStmt Uncompatible ");
				System.exit(4);
			}
		}

		return null;
	}

	@Override
	public Type visitAssignStmt(AssignStmt stmt, Type arg) {
		// TODO Auto-generated method stub
		Type refType = stmt.ref.visit(this, arg);
		Type valType = stmt.val.visit(this, arg);
		if (!refType.equals(valType)){
			System.out.println("===2343x===: visitVardeclStmt Uncompatible ");
			System.out.println("===2343x===: left Type: " + refType.typeKind +" : " + refType);
			System.out.println("===2343x===: right Type: " + valType.typeKind +" : " + valType);
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
			Declaration mRefDecl = def.refId.declBinding;
			if (mRefDecl instanceof MethodDecl){
				MethodDecl mdl = (MethodDecl) mRefDecl;
				ParameterDeclList fpl = mdl.parameterDeclList; //formal parameter list

				if (fpl.size() != al.size()){
					System.out.println("different size between arguments and formal parameter");
					System.exit(4);
				}
				for (int i = 0; i < al.size(); i++){
					Type argType = al.get(i).visit(this, arg);
					Type fpType = fpl.get(i).type;
					if (!argType.equals(fpType)){
						System.out.println("Incompatible between arguments and parameter at " + i);
						System.exit(4);
					}
				}
			}
			else {
				System.out.println("===342x===call to a non-method");
				System.exit(4);
			}
		}
		return null;
	}

	@Override
	public Type visitIfStmt(IfStmt stmt, Type arg) {
		Type condType = stmt.cond.visit(this, arg); //must be boolean type
		if (condType == null ||
				condType.typeKind != TypeKind.BOOLEAN ){
			System.out.println("condition is not a boolean type");
			System.exit(4);
		}

		if (stmt.thenStmt instanceof VarDeclStmt){
			System.out.println("Can not declare in a branch");
			System.exit(4);
		}
		stmt.thenStmt.visit(this, arg); //not initialzation statement

		if (stmt.elseStmt != null){
			if (stmt.elseStmt instanceof VarDeclStmt){
				System.out.println("Can not declare in a branch");
				System.exit(4);
			}
			stmt.elseStmt.visit(this, arg);
		}

		return null;
	}

	@Override
	public Type visitWhileStmt(WhileStmt stmt, Type arg) {
		// TODO Auto-generated method stub
		Type stmtType = stmt.cond.visit(this, arg);  //boolean type
		if (stmtType == null ||
				stmtType.typeKind != TypeKind.BOOLEAN){
			System.out.println("while condition is not a boolean type");
			System.exit(4);
		}

		if( stmt.body instanceof VarDeclStmt){ //Questions about     ================
			System.out.println("Can not declare in a while branch");
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
				System.out.println("after '-' is Not Integer");
				System.exit(4);
				return new ErrorType(dummyPos);
			}
		}
		else if (op.contentEquals("!")){
			if (exprType.typeKind == TypeKind.BOOLEAN){
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				//return exprType;
				System.out.println("===98234x===: after '!' is Not a Boolean");
				System.exit(4); //change to ErrorType later. 
				return new ErrorType(dummyPos);
			}
		}
		else{
			System.out.println("shoud not be here, check parser, not - or !");
			return new ErrorType(dummyPos);
		}
	}

	@Override
	public Type visitBinaryExpr(BinaryExpr expr, Type arg) {
		// TODO Auto-generated method stub
		String op = expr.operator.spelling;
		Type leftExpType = expr.left.visit(this, null);
		Type rightExpType = expr.right.visit(this, null);
		if (op.contentEquals("||") || op.contentEquals("&&")){
			if (leftExpType.typeKind == TypeKind.BOOLEAN &&
					rightExpType.typeKind == TypeKind.BOOLEAN){
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				System.out.println("|| or && with non-boolean type");
				System.exit(4);
				return new ErrorType(dummyPos);
			}

		}
		else if (op.contentEquals("==") || op.contentEquals("!=")){ 
			//both boolean and Integer or even Class type Array type
			if (leftExpType.equals(rightExpType)){ //require the same type
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				System.out.println("== or != with non-same type RHS and RHS");
				System.exit(4);
				return new ErrorType(dummyPos);
			}
		}
		else if (op == "<=" || op == ">=" || 
				op == "<" || op == ">"){
			if (leftExpType.typeKind == TypeKind.INT &&
					rightExpType.typeKind == TypeKind.INT){
				return new BaseType(TypeKind.BOOLEAN, dummyPos);
			}
			else{
				System.out.println(">= <= > < Int type");
				System.exit(4);
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
				System.out.println("===68823x== + - * / not Int by Int Type");
				System.exit(4);
				return new ErrorType(dummyPos);
			}
		}else{
			System.out.println("Undefined opeator......");
			System.exit(4);
			return new ErrorType(dummyPos);
		}
	}

	@Override
	public Type visitRefExpr(RefExpr expr, Type arg) {
		// TODO Auto-generated method stub
		return expr.ref.visit(this, arg);
	}

	@Override
	public Type visitCallExpr(CallExpr expr, Type arg) {
		// TODO Auto-generated method stub
		Reference fRef = expr.functionRef; //returns to its declaration. 
		ExprList al = expr.argList;
		if (fRef instanceof SimpleRef){
			SimpleRef def = (SimpleRef) fRef;
			Declaration mRefDecl = def.refId.declBinding;
			if (mRefDecl instanceof MethodDecl){
				MethodDecl mdl = (MethodDecl) mRefDecl;
				ParameterDeclList fpl = mdl.parameterDeclList; //formal parameter list

				if (fpl.size() != al.size()){
					System.out.println("different size between arguments and formal parameter");
					System.exit(4);
				}
				for (int i = 0; i < al.size(); i++){
					Type argType = al.get(i).visit(this, arg);
					Type fpType = fpl.get(i).type;
					if (!argType.equals(fpType)){
						System.out.println("Incompatible between arguments and parameter at " + i);
						System.exit(4);
					}
				}
			}
			else {
				System.out.println("===342x===call to a non-method");
				System.exit(4);
			}
		}
		return null;
	}

	@Override
	public Type visitLiteralExpr(LiteralExpr expr, Type arg) { //integer
		// TODO Auto-generated method stub
		return expr.literal.visit(this, arg);
	}

	@Override
	public Type visitNewObjectExpr(NewObjectExpr expr, Type arg) {
		// TODO Auto-generated method stub
		expr.type = expr.classtype;
		return expr.classtype; //what's in new expression ?
	}

	@Override
	public Type visitNewArrayExpr(NewArrayExpr expr, Type arg) {
		// TODO Auto-generated method stub
		expr.type = new ArrayType(expr.eltType, dummyPos);
		return expr.type;
	}

	@Override
	public Type visitQualifiedRef(QualifiedRef ref, Type arg) {
		// TODO Auto-generated method stub
		System.out.println("Not ready for type checking as qualified reference");
		return null;
	}//no use here

	@Override
	public Type visitIndexedRef(IndexedRef iRef, Type arg) {
		// TODO Auto-generated method stub
		return iRef.getType();
	}

	@Override
	public Type visitLocalRef(LocalRef ref, Type arg) {
		// TODO Auto-generated method stub
		return ref.getType(); //no need to visit
	}

	@Override
	public Type visitMemberRef(MemberRef ref, Type arg) {
		// TODO Auto-generated method stub
		return ref.getType();
	}

	@Override
	public Type visitClassRef(ClassRef ref, Type arg) {
		// TODO Auto-generated method stub
		return null; // cannot be a terminal reference
	}

	@Override
	public Type visitThisRef(ThisRef ref, Type arg) {
		// TODO Auto-generated method stub
		return ref.getType();
	}

	@Override
	public Type visitDeRef(DeRef ref, Type arg) {
		// TODO Auto-generated method stub
		return ref.getType();
	}

	@Override
	public Type visitIdentifier(Identifier id, Type arg) {
		// TODO Auto-generated method stub 
		return id.type; //should not be here
	}

	@Override
	public Type visitOperator(Operator op, Type arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visitIntLiteral(IntLiteral num, Type arg) {
		// TODO Auto-generated method stub
		return new BaseType(TypeKind.INT, dummyPos);
	}

	@Override
	public Type visitBooleanLiteral(BooleanLiteral bool, Type arg) {
		// TODO Auto-generated method stub
		return new BaseType(TypeKind.BOOLEAN, dummyPos);
	}

}
