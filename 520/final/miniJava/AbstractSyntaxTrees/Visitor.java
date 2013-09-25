/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;


/**
 * An implementation of the Visitor interface provides a method visitX
 * for each non-abstract AST class X.  
 */
public interface Visitor<ArgType,ResultType> {

	// Package
	public ResultType visitPackage(Package prog, ArgType arg);

	// Declarations
	public ResultType visitClassDecl(ClassDecl cd, ArgType arg);
	public ResultType visitFieldDecl(FieldDecl fd, ArgType arg);
	public ResultType visitMethodDecl(MethodDecl md, ArgType arg);
	public ResultType visitMethodDeclHead(MethodDecl md, ArgType arg); 
	//pa3 added return type function name
	public ResultType visitMethodDeclBody(MethodDecl md, ArgType arg); 
	//pa3 added parameterlist and boday
	public ResultType visitParameterDecl(ParameterDecl pd, ArgType arg);
	public ResultType visitVarDecl(VarDecl decl, ArgType arg);

	// Types
	public ResultType visitBaseType(BaseType type, ArgType arg);
	public ResultType visitClassType(ClassType type, ArgType arg);
	public ResultType visitArrayType(ArrayType type, ArgType arg);
	public ResultType visitErrorType(ErrorType type, ArgType arg); //pa3 added
	public ResultType visitUnsupportedType(UnsupportedType type, ArgType arg); //pa3 added
	public ResultType visitVoidType(VoidType type, ArgType arg); //final added

	// Statements
	public ResultType visitBlockStmt(BlockStmt stmt, ArgType arg);
	public ResultType visitIfStmt(IfStmt stmt, ArgType arg);
	public ResultType visitWhileStmt(WhileStmt stmt, ArgType arg);
	public ResultType visitExprStmt(ExprStmt stmt, ArgType arg);//final added
	public ResultType visitForStmt(ForStmt stmt, ArgType arg); //final added
	public ResultType visitVarDeclListStmt(VarDeclListStmt stmt, ArgType arg); //final added
	public ResultType visitNullStmt(NullStmt stmt, ArgType arg); //final added

	// Expressions
	public ResultType visitUnaryExpr(UnaryExpr expr, ArgType arg);
	public ResultType visitBinaryExpr(BinaryExpr expr, ArgType arg);
	public ResultType visitRefExpr(RefExpr expr, ArgType arg);
	public ResultType visitCallExpr(CallExpr expr, ArgType arg);
	public ResultType visitLiteralExpr(LiteralExpr expr, ArgType arg);
	public ResultType visitNewObjectExpr(NewObjectExpr expr, ArgType arg);
	public ResultType visitNewArrayExpr(NewArrayExpr expr, ArgType arg);
	public ResultType visitIncrementExpr(IncrementExpr expr, ArgType arg);//final added
	public ResultType visitDecrementExpr(DecrementExpr expr, ArgType arg); //final added
	public ResultType visitAssignExpr(AssignExpr expr, ArgType arg); //final added
	public ResultType visitTypeExpr(TypeExpr expr, ArgType arg);//final added

	// References
	public ResultType visitIndexedRef(IndexedRef ref, ArgType arg);
	public ResultType visitArrayLengthRef(ArrayLengthRef ref, ArgType arg);
	//\begin{pa3 added}
	
	public ResultType visitSimpleRef(SimpleRef ref, ArgType arg);
	public ResultType visitLocalRef(LocalRef ref, ArgType arg);
	public ResultType visitClassRef(ClassRef ref, ArgType arg);
	public ResultType visitThisRef(ThisRef ref, ArgType arg);
	public ResultType visitFieldRef(FieldRef ref, ArgType arg);
	public ResultType visitMethodRef(MethodRef ref, ArgType arg);
	public ResultType visitSubFieldRef(SubFieldRef ref, ArgType arg);
	public ResultType visitSubMethodRef(SubMethodRef ref, ArgType arg);
	public ResultType visitExprRef(ExprRef ref, ArgType arg);
	//end{pa3 added}

	// Terminals
	public ResultType visitIdentifier(Identifier id, ArgType arg);
	public ResultType visitOperator(Operator op, ArgType arg);
	public ResultType visitIntLiteral(IntLiteral num, ArgType arg);
	public ResultType visitBooleanLiteral(BooleanLiteral bool, ArgType arg);
	public ResultType visitNullLiteral(NullLiteral nul, ArgType arg);
	

}
