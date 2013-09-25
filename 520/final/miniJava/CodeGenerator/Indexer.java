package miniJava.CodeGenerator;

import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
/**
 * to set RTE index
 * @author duozhao
 *
 */
public class Indexer implements Visitor<Integer, Integer>{
	
	public AST astToIndex;
	
	public Indexer(AST ast){
		astToIndex = ast;
	}
	
	public AST indexIt(){
		astToIndex.visit(this, 0);
		return astToIndex;
	}

	@Override
	public Integer visitPackage(Package prog, Integer index) {
		ClassDeclList cdl = prog.classDeclList;
		for (int i = 0; i < cdl.size(); i++){
			cdl.get(i).visit(this, i); // i is classIndex
		}
		return null;
	}

	@Override
	public Integer visitClassDecl(ClassDecl cd, Integer classIndex) {
		cd.setIndex(classIndex);
		
		FieldDeclList fdl = cd.fieldDeclList;
		MethodDeclList mdl = cd.methodDeclList;
		
		for (int i = 0; i < fdl.size(); i++){
			fdl.get(i).classIndex = classIndex;
		}
		
		for (int i = 0; i < mdl.size(); i++){
			mdl.get(i).classIndex = classIndex;
			mdl.get(i).visit(this, i); //MethodIndex
		}
		return null;
	}

	@Override
	public Integer visitFieldDecl(FieldDecl fd, Integer index) {
		return null;
	}

	@Override
	public Integer visitMethodDecl(MethodDecl md, Integer methodIndex) {
		ParameterDeclList pdl = md.parameterDeclList;
		StatementList sl = md.statementList; 
		
		for (int i = 0; i < pdl.size(); i++){
			pdl.get(i).setIndex(i - pdl.size()); //(-2, -1) //Local Index
		}
		
		int stmt_natural_index = 0, next_VarDecl_index = 0 ;
		for (Statement stmt: sl){
			stmt.setIndex(stmt_natural_index++); //one by one
			next_VarDecl_index += stmt.visit(this, next_VarDecl_index); //return # of Decl
		}
		
		md.rte.setVarDeclCounter(next_VarDecl_index);
		return null;
	}

	@Override
	public Integer visitMethodDeclHead(MethodDecl md, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitMethodDeclBody(MethodDecl md, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitParameterDecl(ParameterDecl pd, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitVarDecl(VarDecl decl, Integer index) {
		decl.setIndex(index); 
		decl.rte.setVarDeclCounter(1);
		return 1; //for now it's one
	}

	@Override
	public Integer visitBaseType(BaseType type, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitClassType(ClassType type, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitArrayType(ArrayType type, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitErrorType(ErrorType type, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitUnsupportedType(UnsupportedType type, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitVoidType(VoidType type, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitBlockStmt(BlockStmt blstmt, Integer block_start_index) {
		StatementList sl = blstmt.sl;
		
		int stmt_natural_index = 0;
		int varDecl_index = block_start_index;
		for (Statement stmt : sl){
			stmt.setIndex(stmt_natural_index++);
			varDecl_index += stmt.visit(this, varDecl_index);
		}
		blstmt.rte.setVarDeclCounter(varDecl_index - block_start_index);
		return 0; //itself is 0
	}

	@Override
	public Integer visitIfStmt(IfStmt stmt, Integer index) {
		stmt.thenStmt.visit(this, index);
		if (stmt.elseStmt != null){
			stmt.elseStmt.visit(this, index);
		}
		return 0;
	}

	@Override
	public Integer visitWhileStmt(WhileStmt stmt, Integer index) {
		stmt.body.visit(this, index);
		return 0;
	}

	@Override
	public Integer visitExprStmt(ExprStmt stmt, Integer index) {
		return 0;
	}

	@Override
	public Integer visitForStmt(ForStmt stmt, Integer for_start_index) {
		
		int VarDecl_index = for_start_index;
		int stmt_natural_index = 0;
		
		for (Statement init : stmt.init){
			init.setIndex(stmt_natural_index++);
			VarDecl_index += init.visit(this, VarDecl_index);
		}
		stmt.forBody.setIndex(stmt_natural_index++);
		VarDecl_index += stmt.forBody.visit(this, VarDecl_index);
		for (Statement update: stmt.update){
			update.setIndex(stmt_natural_index++);
			VarDecl_index += update.visit(this, VarDecl_index);
		}
		
		stmt.rte.setVarDeclCounter(VarDecl_index - for_start_index);
		return 0;
	}

	@Override
	public Integer visitVarDeclListStmt(VarDeclListStmt stmt, Integer vdl_index) {
		int index = vdl_index;
		for (VarDecl vd : stmt.vdList){
			index += vd.visit(this, index);
		}
		
		return stmt.rte.VarDeclCounter = index - vdl_index;
	}

	@Override
	public Integer visitNullStmt(NullStmt stmt, Integer index) {
		return 0;
	}

	@Override
	public Integer visitUnaryExpr(UnaryExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitBinaryExpr(BinaryExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitRefExpr(RefExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitCallExpr(CallExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitLiteralExpr(LiteralExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitNewObjectExpr(NewObjectExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitNewArrayExpr(NewArrayExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitIncrementExpr(IncrementExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitDecrementExpr(DecrementExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitAssignExpr(AssignExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitTypeExpr(TypeExpr expr, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitIndexedRef(IndexedRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitArrayLengthRef(ArrayLengthRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitSimpleRef(SimpleRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitLocalRef(LocalRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitClassRef(ClassRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitThisRef(ThisRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitFieldRef(FieldRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitMethodRef(MethodRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitSubFieldRef(SubFieldRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitSubMethodRef(SubMethodRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitExprRef(ExprRef ref, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitIdentifier(Identifier id, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitOperator(Operator op, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitIntLiteral(IntLiteral num, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitBooleanLiteral(BooleanLiteral bool, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitNullLiteral(NullLiteral nul, Integer index) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
