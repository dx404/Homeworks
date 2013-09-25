package miniJava.CodeGenerator;

import java.util.ArrayList;

import mJAM.Machine;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;
import miniJava.SyntacticAnalyzer.Token.OpName;
import miniJava.stdEnv.StdEnv;

import mJAM.Machine.Op;
import mJAM.Machine.Reg;
import mJAM.Machine.Prim;

public class Encoder implements Visitor<Integer, Integer>{
	public AST decoratedAST;
	public Integer main_offset;
	ArrayList<PatchLine> memPatchList;
	public int classObjectSB_resevered = 2;
	public int formalPara_LB_resevered = 0;
	public int LocalVar_LB_resevered = 3;
	
	public boolean enableShortCircult = true;
	public ShortCircuitEvaluator short_circuit_evaluator;

	public void pushError(String errMsg){
		System.out.println("*** ==Error== Encoder:" + errMsg);
		System.exit(4);
	}
	
	public void encodeIt(String fileName){
		Machine.initCodeGen();
		decoratedAST.visit(this, null);
	}

	public Encoder(AST decoratedAST){
		this.decoratedAST = decoratedAST;
		memPatchList = new ArrayList<PatchLine>();
	}
	
	public void setShortCircuitEvaluation(boolean value){
		enableShortCircult = value;
	}

	public Integer visitPackage(Package prog, Integer arg) {
		Integer begin_CB_Label = Machine.nextInstrAddr();
		prog.setRTE(Reg.CB, begin_CB_Label);

		int classObject_offset_SB = 0;
		int classObject_size_SB = 0;
		ClassDeclList cdl = prog.classDeclList;
		
		for (ClassDecl cd: cdl){ //pre-allocate for class object
			classObject_size_SB = cd.methodDeclList.size() + classObjectSB_resevered;
			cd.rte.setRTE(Reg.SB, classObject_offset_SB, classObject_size_SB);
			classObject_offset_SB += classObject_size_SB; //preview
		}

		for (ClassDecl cd: cdl){
			cd.visit(this, -1); //no super class
		}

		Machine.emit(Op.LOADL,-1); //load address of static call
		Machine.emit(Op.CALL, Reg.CB, main_offset);
		//Machine.emit(Op.HALT, 4, 0, 0); //snapshot
		Machine.emit(Op.HALT, 0, 0, 0);

		for (PatchLine pl : memPatchList){
			Integer method_offset_CB = 
					cdl.get(pl.classIndex).methodDeclList.get(pl.info).getRTEoffset();
			Machine.patch(pl.line, method_offset_CB);
		}

		Integer end_CB_Label = Machine.nextInstrAddr();
		prog.setRTEsize(end_CB_Label - begin_CB_Label);
		return end_CB_Label - begin_CB_Label;
	}

	//processing methods and static fields
	public Integer visitClassDecl(ClassDecl cd, Integer arg) {
		MethodDeclList mdl = cd.methodDeclList; 

		Integer fwd_jump_label = Machine.nextInstrAddr(); //on code base
		Machine.emit(Op.JUMP, Reg.CB, 0); //0 is subject to patch

		ArrayList<Integer> mdl_offset_CB = new ArrayList<Integer>(); // list of method CB
		for (MethodDecl md : mdl) {
			mdl_offset_CB.add(Machine.nextInstrAddr()); //The beginning of a method
			md.visit(this, arg); 
		}

		Integer post_mdl_label = Machine.nextInstrAddr();
		Machine.patch(fwd_jump_label, post_mdl_label); // to patch offset

		/* Establish the class object after methods */
		Machine.emit(Op.LOADL,-1); //no super class
		Machine.emit(Op.LOADL, mdl.size()); // # of methods
		for (Integer md_start_label : mdl_offset_CB){
			Machine.emit(Op.LOADA, Reg.CB, md_start_label);
		}

		return cd.rte.size;
	}

	@Override 
	public Integer visitFieldDecl(FieldDecl fd, Integer arg) { 
		return null;
	}

	public Integer visitMethodDecl(MethodDecl md, Integer arg) { //RTE in CB
		Integer begin_md = Machine.nextInstrAddr();
		if (md.isMain){
			main_offset = begin_md;
		}
		
		ParameterDeclList pdl = md.parameterDeclList;
		StatementList sl = md.statementList;

		md.setRTE(Reg.CB, begin_md); //for now size = 1;

		for (ParameterDecl pd : pdl){ 
			pd.visit(this, 0); //no need to push
		}
		
		for (Statement sm : sl){
			sm.visit(this, Machine.nextInstrAddr()); //CB
		}

		if (md.returnExp == null){
			Machine.emit(Op.RETURN, 0, 0, pdl.size());
		}
		else {//void
			md.returnExp.visit(this, 1);
			Machine.emit(Op.RETURN, 1, 0, pdl.size());
		}

		Integer end_md = Machine.nextInstrAddr();
		md.rte.setRTE(Reg.CB, begin_md, end_md - begin_md);
		return md.rte.size;
	}

	@Override
	public Integer visitMethodDeclHead(MethodDecl md, Integer arg) {
		// TODO Does not apply here
		return null;
	}

	@Override 
	public Integer visitMethodDeclBody(MethodDecl md, Integer arg) {
		// TODO Does not apply here
		return null;
	}

	@Override // update later, copy (value/reference accordingly) from caller
	public Integer visitParameterDecl(ParameterDecl pd, Integer pushSpace) { 
		int pd_offset_LB = pd.getRTEIndex() - formalPara_LB_resevered; //set via indexer
		pd.setRTE(Reg.LB, pd_offset_LB);
		return 0; //pass by CallExpr
	}

	public Integer visitVarDecl(VarDecl decl, Integer pushSpace) { //which visit which first
		int va_offset_LB = decl.getRTEIndex() + LocalVar_LB_resevered;
		decl.setRTE(Reg.LB, va_offset_LB);
		if ( pushSpace > 0 ){
			Machine.emit(Op.PUSH, pushSpace); // reserve space for local var "a" at 3[LB]
		}
		return pushSpace;
	}

	//Types...Generally not use in coder
	@Override
	public Integer visitBaseType(BaseType type, Integer arg) {
		return null;
	}

	@Override
	public Integer visitClassType(ClassType type, Integer arg) {
		return null;
	}

	@Override
	public Integer visitArrayType(ArrayType type, Integer arg) {
		return null;
	}

	@Override
	public Integer visitErrorType(ErrorType type, Integer arg) {
		return null;
	}

	@Override
	public Integer visitUnsupportedType(UnsupportedType type, Integer arg) {
		return null;
	}
	
	@Override
	public Integer visitVoidType(VoidType type, Integer arg) {
		return null;
	}

	
	//Statements: 
	@Override
	public Integer visitExprStmt(ExprStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);


		stmt.expr.visit(this, 0); //work for callExpr

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return stmt.rte.size;
	}
	
	@Override
	public Integer visitBlockStmt(BlockStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		for (Statement sm : stmt.sl){
			sm.visit(this, null);
		}

		Integer PopNum = stmt.rte.VarDeclCounter;
		if (PopNum > 0){
			Machine.emit(Op.POP, PopNum);
		}

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return null;
	}

	@Override
	public Integer visitIfStmt(IfStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		stmt.cond.visit(this, 1);
		Integer post_cond_label = Machine.nextInstrAddr();
		Machine.emit(Op.JUMPIF, 0, Reg.CB, 0); //to be pached //1 ???

		stmt.thenStmt.visit(this, null);
		Integer post_then_label = Machine.nextInstrAddr();
		if (stmt.elseStmt == null){
			Machine.patch(post_cond_label, post_then_label);
		}
		else {
			Machine.emit(Op.JUMP, Reg.CB, 0); //end_then_label[CB]

			Integer begin_else_label = Machine.nextInstrAddr();
			Machine.patch(post_cond_label, begin_else_label);
			stmt.elseStmt.visit(this, null);

			Integer post_else_label = Machine.nextInstrAddr();
			Machine.patch(post_then_label, post_else_label);
		}

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return null;
	}

	@Override
	public Integer visitWhileStmt(WhileStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		Machine.emit(Op.JUMP, Reg.CB, 0); //Forward Jump, offset to patch
		Integer pre_body_label = Machine.nextInstrAddr();
		stmt.body.visit(this, null);
		Integer pre_cond_label = Machine.nextInstrAddr();
		Machine.patch(begin_stmt, pre_cond_label);
		stmt.cond.visit(this, 1);
		Machine.emit(Op.JUMPIF, 1, Reg.CB, pre_body_label);

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return end_stmt - begin_stmt;
	}
	
	@Override
	public Integer visitForStmt(ForStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);
		for (Statement initStmt : stmt.init){
			initStmt.visit(this, null);
		}

		Integer begin_loop = Machine.nextInstrAddr();
		Machine.emit(Op.JUMP, Reg.CB, 0); //to patch
		Integer pre_body_label = Machine.nextInstrAddr();
		stmt.forBody.visit(this, null);
		for (Statement update : stmt.update){
			update.visit(this, null);
		}
		Integer pre_cond_label = Machine.nextInstrAddr();
		Machine.patch(begin_loop, pre_cond_label);
		stmt.cond.visit(this, 1);
		Machine.emit(Op.JUMPIF, 1, Reg.CB, pre_body_label);

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return end_stmt - begin_stmt;
	}

	/**
	 * arg is the starting index for VarDecl
	 */
	@Override
	public Integer visitVarDeclListStmt(VarDeclListStmt stmt, Integer startIndex) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		ArrayList<VarDecl> varDeclList = stmt.vdList;
		int numOfDecls = varDeclList.size();
		ExprList eprl = stmt.initList;
		for (int i = 0; i < numOfDecls; i++){
			if (eprl.get(i) == null){
				varDeclList.get(i).visit(this, 1); //Set RTE & push one
			}
			else {
				varDeclList.get(i).visit(this, 0); //Set RTE & no push 
				eprl.get(i).visit(this, 1);  // reserve the result on stack
			}
		}

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return end_stmt - begin_stmt;
	}

	@Override
	public Integer visitNullStmt(NullStmt stmt, Integer arg) {
		return 0;
	}

	//Expressions
	@Override
	public Integer visitUnaryExpr(UnaryExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		Operator op = expr.operator;
		expr.expr.visit(this, 1);
		
		switch(op.opKind){
		case NOT:
			Machine.emit(Prim.not);
			break;
			
		case NEG:
			Machine.emit(Prim.neg);
			break;
			
		default:
			pushError("visitUnaryExpr: No such an operator:");
			break;
		}
		
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return end - begin;
	}
	
	@Override
	public Integer visitBinaryExpr(BinaryExpr expr, Integer arg) {
		Operator op = expr.operator;
		//Below are the short-circuit treatment of short-circuit evaluation
		if (enableShortCircult 
				&& (op.opKind == OpName.OR ||
				op.opKind == OpName.AND)){
			short_circuit_evaluator = new ShortCircuitEvaluator(this);
			short_circuit_evaluator.emitShortCircuitCode(expr);
			return expr.rte.size;
		}
		
		int begin = Machine.nextInstrAddr();
		expr.left.visit(this, 1);
		expr.right.visit(this, 1);

		switch(op.opKind){
		case OR:
			Machine.emit(Prim.or);
			break;
		case AND:
			Machine.emit(Prim.and);
			break;
		case EQ:
			Machine.emit(Prim.eq);
			break;
		case NEG: 
			Machine.emit(Prim.ne);
			break;
		case LEQ: 			
			Machine.emit(Prim.le);
			break;
		case LT: 
			Machine.emit(Prim.lt);
			break;
		case GEQ: 
			Machine.emit(Prim.ge);
			break;
		case GT:
			Machine.emit(Prim.gt);
			break;
		case PLUS: 
			Machine.emit(Prim.add);
			break;
		case MINUS:
			Machine.emit(Prim.sub);
			break;
		case TIMES:
			Machine.emit(Prim.mult);
			break;
		case DIV:
			Machine.emit(Prim.div);
			break;
		default: 
			pushError("***==Encoder: ==: visitBinaryExpr Invalid operator:");
			break;
		}
		
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return end - begin;
	}

	@Override
	public Integer visitRefExpr(RefExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		expr.ref.visit(this, 1);
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return end - begin; //just to evaluate
	}

	@Override
	public Integer visitCallExpr(CallExpr expr, Integer keepValue) { //0 not keep, 1 try to keep it
		int begin = Machine.nextInstrAddr();
		expr.functionRef.visit(this, 1); 
		
		if (keepValue == 0 && 
				expr.peekType().typeKind != TypeKind.VOID ){
			Machine.emit(Op.POP, 1);
		}
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return end - begin;
	}

	@Override
	public Integer visitLiteralExpr(LiteralExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		
		expr.literal.visit(this, arg);
		
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return end - begin;
	}

	@Override
	public Integer visitNewObjectExpr(NewObjectExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		
		ClassDecl classObject = expr.classtype.getDecl();
		Integer temp_offset = classObject.getRTEoffset();
		Machine.emit(Op.LOADA, Reg.SB, temp_offset); //
		Machine.emit(Op.LOADL, classObject.fieldDeclList.size()); 
		Machine.emit(Prim.newobj); 

		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return null;
	}

	@Override
	public Integer visitNewArrayExpr(NewArrayExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		
		expr.sizeExpr.visit(this, 1); //push value on its stack
		Machine.emit(Prim.newarr);
		
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return null;
	}
	
	@Override
	public Integer visitIncrementExpr(IncrementExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		// TODO To be implemented
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return null;
	}

	@Override
	public Integer visitDecrementExpr(DecrementExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		// TODO To be implemented
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return null;
	}

	/**
	 * arg == 0 for AssignStmt, arg == 0 for reserve the value
	 */
	@Override
	public Integer visitAssignExpr(AssignExpr expr, Integer arg) { 
		int begin = Machine.nextInstrAddr();
		Reference ref = expr.ref;
		Expression val = expr.val;
		
		if (ref instanceof LocalRef){
			val.visit(this, 1); //put value of an evaluated expr on stack
			Machine.emit(Op.STORE, Reg.LB, ref.getDeclRTEoffset());
		}
		else if (ref instanceof FieldRef){
			Machine.emit(Op.LOADA, Reg.OB, 0); 
			Machine.emit(Op.LOADL, ref.getDeclRTE().index);
			val.visit(this, 1);			//put value on stack
			Machine.emit(Prim.fieldupd);
		}
		else if (ref instanceof IndexedRef){//array update
			ref.visit(this, 0); // 0 or 1 ??
			val.visit(this, 1);
			Machine.emit(Prim.arrayupd);
		}
		else if (ref instanceof SubFieldRef){ //layered update
			ref.visit(this, 0); //push its address, in the whole picture only the last one left unevaluaed
			val.visit(this, 1);
			Machine.emit(Prim.fieldupd);
		}
		else if (ref instanceof ThisRef){
			pushError("visitAssignExpr: Cannot assign to 'this' ");
		}
		else{
			pushError("visitAssignExpr: Invalid Reference to be assigned");
		}
		
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return end;
	}
	
	@Override
	public Integer visitTypeExpr(TypeExpr expr, Integer arg) {
		int begin = Machine.nextInstrAddr();
		// TODO To be implemented
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return null;
	}

	@Override
	public Integer visitIdentifier(Identifier id, Integer arg) { 
		// TODO Does not apply here 
		return null;
	}

	@Override
	public Integer visitOperator(Operator op, Integer arg) {
		// TODO Does not apply here 
		return null;
	}

	//Literals
	@Override
	public Integer visitIntLiteral(IntLiteral num, Integer arg) {
		Machine.emit(Op.LOADL, Integer.parseInt(num.spelling)); 
		return 1;
	}

	@Override
	public Integer visitBooleanLiteral(BooleanLiteral bool, Integer arg) {
		int boolValue = bool.spells("true") ? 1 : 0;
		Machine.emit(Op.LOADL, boolValue);
		return 1;
	}
	
	@Override
	public Integer visitNullLiteral(NullLiteral nul, Integer arg) {
		Machine.emit(Op.LOADL, 0); // the value of null literal is one
		return 1;
	}

	/**
	 * The Major Referencea
	 * (1) LocalRef (2)ThisRef    (3)ClassRef
	 * (4) FieldRef (5) MethodRef (6) SubFieldRef (7) SubMethodRef
	 * (8)IndexRef (9)ArrayLengthRef (10)ExprRef
	 *  For a general rule during the following visiting, 
	 *  
	 */
	@Override
	public Integer visitSimpleRef(SimpleRef ref, Integer toVal) {
		/**
		 *  Does not apply here, SimpleRef has been refined into 
		 *  (1) ~ (5)
		 */
		return null;
	}
	
	@Override
	public Integer visitLocalRef(LocalRef ref, Integer toVal) { 
		RunTimeEntity declRTE = ref.getDeclRTE();
		
		if (toVal == 0){ //here return an address
			Machine.emit(Op.LOADA, Reg.LB, declRTE.address.offset);
		}
		else { //here return the corresponding value
			Machine.emit(Op.LOAD, Reg.LB, declRTE.address.offset);
		}
		return null;
	}
	
	@Override
	public Integer visitThisRef(ThisRef ref, Integer toVal) {//Cannot evaluate to a value
		Machine.emit(Op.LOADA, Reg.OB, 0); //LOADA vs LOAD
		return null;
	}
	
	@Override
	public Integer visitClassRef(ClassRef ref, Integer toVal) {//Cannot be assigned
		RunTimeEntity declRTE = ref.getDeclRTE();
		Machine.emit(Op.LOADA, Reg.SB, declRTE.address.offset);
		return null;
	}
	
	@Override
	public Integer visitFieldRef(FieldRef ref, Integer toVal) {
		RunTimeEntity declRTE = ref.getDeclRTE();
		if (toVal == 0){
			Machine.emit(Op.LOADA, Reg.OB, declRTE.index); 
		}
		else {
			Machine.emit(Op.LOAD, Reg.OB, declRTE.index);
		}
		return null;
	}
	
	@Override
	public Integer visitMethodRef(MethodRef ref, Integer toVal) {
		RunTimeEntity declRTE = ref.getDeclRTE();
		for (Expression argExpr : ref.argList){
			argExpr.visit(this, 1); //put value on the stack
		}
		Machine.emit(Op.LOADA, Reg.OB, 0);

		Integer offset = declRTE.address.offset;
		if (offset < 0){
			memPatchList.add(new PatchLine(Machine.nextInstrAddr(), ref.getDecl().classIndex, declRTE.index));
		}
		Machine.emit(Op.CALL, Reg.CB, offset); 
		return null;
	}

	@Override
	public Integer visitSubFieldRef(SubFieldRef ref, Integer toVal) {
		ref.preRef.visit(this, 1); //The value on stack is the address on heap  
		Integer idIndex = ref.getDecl().getRTEIndex();
		Machine.emit(Op.LOADL, idIndex);
		
		if (toVal != 0){ //to retrieve its value
			Machine.emit(Prim.fieldref);
		}
		return null;
	}

	@Override
	public Integer visitSubMethodRef(SubMethodRef ref, Integer toVal) {
		for (Expression argExpr : ref.argList){
			argExpr.visit(this, 1); //put value on the stack
		}

		if (ref.subID.declBinding == StdEnv.printlnMethod){
			Machine.emit(Prim.putint);
			Machine.emit(Prim.puteol);
			return null;
		}
		if (ref.subID.declBinding == StdEnv.printMethod){
			Machine.emit(Prim.putint);
			return null;
		}

		ref.preRef.visit(this, 1); //The value on stack is the address on heap 
		MethodDecl subIdDecl = ref.getDecl();
		int potential_offset = subIdDecl.getRTEoffset();
		if (potential_offset < 0){
			int patchline = Machine.nextInstrAddr();
			int classIndex = subIdDecl.classIndex;
			int mdIndex = subIdDecl.getRTEIndex();
			memPatchList.add(new PatchLine(patchline, classIndex, mdIndex));
		}
		Machine.emit(Op.CALL, Reg.CB, potential_offset);
		return null;
	}
	
	@Override
	public Integer visitIndexedRef(IndexedRef ref, Integer toVal) {
		ref.preRef.visit(this, 1); //The value on stack is the address on heap
		ref.indexExpr.visit(this, 1); //put value on stack
		if (toVal != 0){
			Machine.emit(Prim.arrayref);
		}
		return null;
	}

	@Override //Array length is stored before the first element in heap
	public Integer visitArrayLengthRef(ArrayLengthRef ref, Integer toValue) {
		ref.preRef.visit(this, 1);
		Machine.emit(Prim.pred); 
		Machine.emit(Op.LOADI);
		return null;
	}

	@Override
	public Integer visitExprRef(ExprRef ref, Integer arg) {
		// TODO To be implemented in the future
		// The definition of ExprRef is defined in 
		//package miniJava.AbstractSyntaxTrees.ExprRef
		return null;
	}
}
