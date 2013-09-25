/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

/**
 * Display AST in text form
 * @author prins
 * @version COMP 520 V2.2
 * 
 * visitXXX( AST Node type XXX, prefix string for display of each line)
 */
public class ASTDisplay implements Visitor<String,Object> {

	public static boolean showPosition = false;

	public void showTree(AST ast){
		System.out.println("======= AST Display =========================");
		ast.visit(this, "");
		System.out.println("=============================================");
	}   

	// procedures to format output
	private void show(String arg, String text) {
		System.out.println(arg + text);
	}

	private void show(String arg, AST node) {
		System.out.println(arg + node.toString());
	}

	private String indent(String arg) {
		return arg + "  ";
	}

	// Package
	public Object visitPackage(Package prog, String arg){
		show(arg, prog);
		ClassDeclList cl = prog.classDeclList;
		show(arg,"  ClassDeclList [" + cl.size() + "]");
		String pfx = arg + "  . "; 
		for (ClassDecl c: prog.classDeclList){
			c.visit(this, pfx);
		}
		return null;
	}


	// Declarations
	public Object visitClassDecl(ClassDecl clas, String arg){
		show(arg, clas);
		clas.id.visit(this, indent(arg));
		if (clas.superClassRef != null){ //final added
			show(arg, "extends from");
			clas.superClassRef.visit(this, indent(arg));
		}
		show(arg,"  FieldDeclList [" + clas.fieldDeclList.size() + "]");
		String pfx = arg + "  . "; 
		for (FieldDecl f: clas.fieldDeclList)
			f.visit(this, pfx);
		show(arg,"  MethodDeclList [" + clas.methodDeclList.size() + "]");
		for (MethodDecl m: clas.methodDeclList)
			m.visit(this, pfx);
		return null;
	}

	public Object visitFieldDecl(FieldDecl f, String arg){
		show(arg, "(" + (f.isPrivate ? "private": "public") 
				+ (f.isStatic ? " static) " :") ") + f.toString());
		f.type.visit(this, indent(arg));
		f.id.visit(this,indent(arg));
		return null;
	}

	public Object visitMethodDecl(MethodDecl m, String arg){
		show(arg, "(" + (m.isPrivate ? "private": "public") 
				+ (m.isStatic ? " static) " :") ") + m.toString());
		m.type.visit(this, indent(arg));
		m.id.visit(this, indent(arg));
		show(indent(arg) + "  ", "Sig: " + m.encodedName);
		ParameterDeclList pdl = m.parameterDeclList;
		show(arg, "  ParameterDeclList [" + pdl.size() + "]");
		String pfx = ((String) arg) + "  . ";
		for (ParameterDecl pd: pdl) {
			pd.visit(this, pfx);
		}
		StatementList sl = m.statementList;
		show(arg, "  StmtList [" + sl.size() + "]");
		for (Statement s: sl) {
			s.visit(this, pfx);
		}
		if (m.returnExp != null) {
			m.returnExp.visit(this, indent(arg));
		}
		return null;
	}

	public Object visitParameterDecl(ParameterDecl pd, String arg){
		show(arg, pd);
		pd.type.visit(this, indent(arg));
		pd.id.visit(this, indent(arg));
		return null;
	} 

	public Object visitVarDecl(VarDecl vd, String arg){
		show(arg, vd);
		vd.type.visit(this, indent(arg));
		vd.id.visit(this, indent(arg));
		return null;
	}
	@Override
	public Object visitVarDeclListStmt(VarDeclListStmt stmt, String arg) {
		show(arg, stmt);
		stmt.commonType.visit(this, indent(arg));	
		arg = indent(arg);
		for (int i = 0; i < stmt.vdList.size(); i++){
			stmt.vdList.get(i).id.visit(this, arg);
			if (stmt.initList.get(i) != null){
				stmt.initList.get(i).visit(this, indent(arg));
			}
			else{
				show(indent(arg), "(uninitialized)");
			}
		}
		return null;
	}

	// Types
	public Object visitBaseType(BaseType type, String arg){
		show(arg, type.typeKind + " " + type.toString());
		return null;
	}
	
	public Object visitVoidType(VoidType type, String arg) {
		show(arg, TypeKind.VOID + " " + type.toString());
		return null;
	}

	public Object visitClassType(ClassType type, String arg){
		if (type.typeKind == TypeKind.UNSUPPORTED){
			show(arg, "UNSUPPORTED");
		}
		else{
			show(arg, type);
		}
		type.name.visit(this, indent(arg));
		return null;
	}

	public Object visitArrayType(ArrayType type, String arg){
		show(arg, type);
		type.eltType.visit(this, indent(arg));
		return null;
	}


	// Statements
	public Object visitBlockStmt(BlockStmt stmt, String arg){
		show(arg, stmt);
		StatementList sl = stmt.sl;
		show(arg,"  StatementList [" + sl.size() + "]");
		String pfx = arg + "  . ";
		for (Statement s: sl) {
			s.visit(this, pfx);
		}
		return null;
	}

	public Object visitIfStmt(IfStmt stmt, String arg){
		show(arg,stmt);
		stmt.cond.visit(this, indent(arg));
		stmt.thenStmt.visit(this, indent(arg));
		if (stmt.elseStmt != null)
			stmt.elseStmt.visit(this, indent(arg));
		return null;
	}

	public Object visitWhileStmt(WhileStmt stmt, String arg){
		show(arg, stmt);
		stmt.cond.visit(this, indent(arg));
		stmt.body.visit(this, indent(arg));
		return null;
	}


	// Expressions
	public Object visitUnaryExpr(UnaryExpr expr, String arg){
		show(arg, expr);
		expr.operator.visit(this, indent(arg));
		expr.expr.visit(this, indent(indent(arg)));
		return null;
	}

	public Object visitBinaryExpr(BinaryExpr expr, String arg){
		show(arg, expr);
		expr.operator.visit(this, indent(arg));
		expr.left.visit(this, indent(indent(arg)));
		expr.right.visit(this, indent(indent(arg)));
		return null;
	}
	
	@Override
	public Object visitAssignExpr(AssignExpr expr, String arg) {
		show(arg, expr);
		expr.ref.visit(this, indent(arg));
		expr.val.visit(this, indent(arg));
		return null;
	}

	public Object visitRefExpr(RefExpr expr, String arg){
		show(arg, expr);
		expr.ref.visit(this, indent(arg));
		return null;
	}

	public Object visitCallExpr(CallExpr expr, String arg){
		show(arg, expr);
		expr.functionRef.visit(this, indent(arg));
		ExprList al = expr.argList;
		show(arg,"  ExprList + [" + al.size() + "]");
		String pfx = arg + "  . ";
		for (Expression e: al) {
			e.visit(this, pfx);
		}
		return null;
	}

	public Object visitLiteralExpr(LiteralExpr expr, String arg){
		show(arg, expr);
		expr.literal.visit(this, indent(arg));
		return null;
	}

	public Object visitNewArrayExpr(NewArrayExpr expr, String arg){
		show(arg, expr);
		expr.eltType.visit(this, indent(arg));
		expr.sizeExpr.visit(this, indent(arg));
		return null;
	}

	public Object visitNewObjectExpr(NewObjectExpr expr, String arg){
		show(arg, expr);
		expr.classtype.visit(this, indent(arg));
		ExprList al = expr.argList;
		show(arg,"  ExprList + [" + al.size() + "]");
		String pfx = arg + "  . ";
		for (Expression e: al) {
			e.visit(this, pfx);
		}
		return null;
	}


	// References

	public Object visitIndexedRef(IndexedRef ir, String arg) {
		show(arg, ir);
		ir.preRef.visit(this, indent(arg));
		ir.indexExpr.visit(this, indent(arg));
		return null;
	}


	// Terminals
	public Object visitIdentifier(Identifier id, String arg){
		show(arg, "\"" + id.spelling + "\" " + id.toString());
		return null;
	}

	public Object visitOperator(Operator op, String arg){
		show(arg, "\"" + op.spelling + "\" " + op.toString());
		return null;
	}

	public Object visitIntLiteral(IntLiteral num, String arg){
		show(arg, "\"" + num.spelling + "\" " + num.toString());
		return null;
	}

	public Object visitBooleanLiteral(BooleanLiteral bool, String arg){
		show(arg, "\"" + bool.spelling + "\" " + bool.toString());
		return null;
	}
//\begin{pa3 added}
	@Override
	public Object visitLocalRef(LocalRef ref, String arg) {
		// TODO Auto-generated method stub
		show(arg, ref);
		ref.simpleId.visit(this, indent(arg));
		return null;
	}

	public Object visitClassRef(ClassRef ref, String arg) {
		// TODO Auto-generated method stub
		show(arg, ref);
		ref.simpleId.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitThisRef(ThisRef ref, String arg) {
		// TODO Auto-generated method stub
		show(arg, ref);
		show(arg, "  this");
		return null;
	}

	@Override
	public Object visitErrorType(ErrorType type, String arg) {
		// TODO Auto-generated method stub
		show(arg, type.typeKind + " " + type.toString());
		return null;
	}

	@Override
	public Object visitUnsupportedType(UnsupportedType type, String arg) {
		// TODO Auto-generated method stub
		show(arg, type.typeKind + " " + type.toString());
		return null;
	}
	//\end{pa3 added}

	@Override
	public Object visitMethodDeclHead(MethodDecl md, String arg) {
		// TODO Auto-generated method stub //not for show
		return null;
	}

	@Override
	public Object visitMethodDeclBody(MethodDecl md, String arg) {
		// TODO Auto-generated method stub //not for show
		return null;
	}

	@Override
	public Object visitSimpleRef(SimpleRef ref, String arg) {
		show(arg, ref);
		ref.simpleId.visit(this, indent(arg));
		return null;
	}

	@Override //final added
	public Object visitNullLiteral(NullLiteral nul, String arg) {
		show(arg, "\"" + nul.spelling + "\" " + nul.toString());
		return null;
	}

	@Override
	public Object visitExprStmt(ExprStmt stmt, String arg) {
		show(arg, stmt);
		stmt.expr.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitForStmt(ForStmt stmt, String arg) {
		show(arg, stmt);
		for (Statement init : stmt.init){
			init.visit(this, arg);
		}
		stmt.cond.visit(this, arg);
		for (Statement update : stmt.update){
			update.visit(this, arg);
		}
		stmt.forBody.visit(this, arg);
		return null;
	}

	@Override
	public Object visitIncrementExpr(IncrementExpr expr, String arg) {
		show(arg, expr);
		if (expr.isPost){
			expr.ref.visit(this, indent(indent(arg)));
			expr.increOp.visit(this, indent(arg));
		}
		else{
			expr.increOp.visit(this, indent(arg));
			expr.ref.visit(this, indent(indent(arg)));
		}
		return null;
	}

	@Override
	public Object visitDecrementExpr(DecrementExpr expr, String arg) {
		show(arg, expr);
		if (expr.isPost){
			expr.ref.visit(this, indent(indent(arg)));
			expr.decreOp.visit(this, indent(arg));
		}
		else{
			expr.decreOp.visit(this, indent(arg));
			expr.ref.visit(this, indent(indent(arg)));
		}
		return null;
	}

	@Override//final added for visit Type
	public Object visitTypeExpr(TypeExpr expr, String arg) {
		show(arg, expr);
		expr.type.visit(this, indent(arg));
		return null;
	}

	@Override //final added
	public Object visitNullStmt(NullStmt stmt, String arg) {
		show(arg,stmt);
		return null;
	}

	@Override
	public Object visitArrayLengthRef(ArrayLengthRef ref, String arg) {
		show(arg, ref);
		ref.preRef.visit(this, indent(arg));
		show(indent(arg), "(length)");
		return null;
	}

	@Override
	public Object visitSubFieldRef(SubFieldRef ref, String arg) {
		show(arg, ref);
		ref.preRef.visit(this, indent(arg));
		ref.subID.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitSubMethodRef(SubMethodRef ref, String arg) {
		show(arg, ref);
		ref.preRef.visit(this, indent(arg));
		ref.subID.visit(this, indent(arg));
		ExprList al = ref.argList;
		show(arg,"  ExprList + [" + al.size() + "]");
		String pfx = arg + "  . ";
		for (Expression e: al) {
			e.visit(this, pfx);
		}
		return null;
	}

	@Override
	public Object visitFieldRef(FieldRef ref, String arg) {
		show(arg, ref);
		ref.simpleId.visit(this, indent(arg));
		return null;
	}

	@Override
	public Object visitMethodRef(MethodRef ref, String arg) {
		show(arg, ref);
		ref.simpleId.visit(this, indent(arg));
		ExprList al = ref.argList;
		show(arg,"  ExprList [" + al.size() + "]");
		String pfx = arg + "  . ";
		for (Expression e: al) {
			e.visit(this, pfx);
		}
		return null;
	}

	@Override
	public Object visitExprRef(ExprRef ref, String arg) {
		show(arg, ref);
		ref.expr.visit(this, indent(arg));
		return null;
	}


}
