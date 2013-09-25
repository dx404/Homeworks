package miniJava.CodeGenerator;

import java.util.ArrayList;

import mJAM.Disassembler;
import mJAM.Interpreter;
import mJAM.Machine;
import mJAM.ObjectFile;
import miniJava.AbstractSyntaxTrees.*;
import miniJava.AbstractSyntaxTrees.Package;

import mJAM.Machine.Op;
import mJAM.Machine.Reg;
import mJAM.Machine.Prim;

public class Encoder implements Visitor<Integer, Integer>{
	public AST decoratedAST;
	public Integer main_offset;
	Integer codeLine; // not necessary, Machine.nextInstrAddr() automatically update
	ArrayList<PatchLine> memPatchList;


	public void pushError(String errMsg){
		System.out.println("***" + errMsg);
		System.exit(4);
	}

	public Encoder(AST decoratedAST, String fileName){
		this.decoratedAST = decoratedAST;
		memPatchList = new ArrayList<PatchLine>();
		Machine.initCodeGen();
		decoratedAST.visit(this, null);

		writeToFileAndRun(fileName.split("[.]")[0] + ".mJAM"); //modify file name later

	}


	public void writeToFileAndRun(String fileName){
		String objectCodeFileName = fileName;
		ObjectFile objF = new ObjectFile(objectCodeFileName);
		System.out.print("Writing object code file " + objectCodeFileName + " ... ");
		if (objF.write()) {
			System.out.println("FAILED!");
			return;
		}
		else
			System.out.println("SUCCEEDED");	

		/* create asm file using disassembler */
		System.out.print("Writing assembly file ... ");
		Disassembler d = new Disassembler(objectCodeFileName);
		if (d.disassemble()) {
			System.out.println("FAILED!");
			return;
		}
		else
			System.out.println("SUCCEEDED");

		/* run code */
//		System.out.println("Running code ... ");
//		Interpreter.interpret(objectCodeFileName);
//
//		System.out.println("*** mJAM execution completed");
	}

	public void emit_as_RTE_addr(Op op, RunTimeEntity rte){
		Machine.emit(op, rte.address.reg, rte.address.offset);
	}

	public void emit_Decl(Op op, Declaration decl){
		Machine.emit(op, decl.rte.address.reg, decl.rte.address.offset);
	}


	public Integer visitPackage(Package prog, Integer arg) {
		prog.rte = new RunTimeEntity(Reg.CB, 0, 0, 0); //the third is subject to patch

		Integer cl_index = 0, offset_SB = 0;
		ClassDeclList cdl = prog.classDeclList;
		for (ClassDecl cd: cdl){
			cd.rte.address.offset = offset_SB;
			
			Integer fd_Index = 0; 
			for (FieldDecl fd : cd.fieldDeclList){
				fd.setIndex(fd_Index++);
				fd.classIndex = cl_index;
			}

			Integer md_index = 0;
			for (MethodDecl md : cd.methodDeclList) {
				md.setIndex(md_index++);
				md.classIndex = cl_index;
			}
			cl_index ++;
			offset_SB += cd.methodDeclList.size() + 2; //preview
		}

		Integer classObjectIndex = 0, classObjectSBLocation = 0; //here is for offset
		for (ClassDecl cd: cdl){
			cd.setIndex(classObjectIndex++); // relative to package numbering
			classObjectSBLocation = cd.visit(this, classObjectSBLocation);
		}

		Machine.emit(Op.LOADL,-1); //load address of static call
		Machine.emit(Op.CALL, Reg.CB, main_offset);
		//Machine.emit(Op.HALT, 4, 0, 0); //snapshot
		Machine.emit(Op.HALT, 0, 0, 0);

		for (PatchLine pl : memPatchList){
			Machine.patch(pl.line, cdl.get(pl.classIndex).methodDeclList.get(pl.info).getRTEoffset());
		}

		Integer pkgEndLabel = Machine.nextInstrAddr();
		prog.rte.size = pkgEndLabel;
		return null;
	}

	public Integer visitClassDecl(ClassDecl cd, Integer arg) {
		FieldDeclList fdl = cd.fieldDeclList;
		MethodDeclList mdl = cd.methodDeclList;

		cd.setRTE(Reg.SB, arg, mdl.size() + 2); //here is for the class object (loading with methods)

		Integer fwd_jump_label = Machine.nextInstrAddr(); //on code base
		Machine.emit(Op.JUMP, Reg.CB, 0); //0 is subject to patch

		ArrayList<Integer> md_offsetListInCB = new ArrayList<Integer>(); // list of method CB

		for (MethodDecl md : mdl) {
			md_offsetListInCB.add(Machine.nextInstrAddr()); //mark at the beginning of a method
			md.visit(this, arg);
		}

		Integer label_postClassMethods = Machine.nextInstrAddr();
		Machine.patch(fwd_jump_label, label_postClassMethods); // to patch offset

		/*Here is to establish the class object after methods */
		Machine.emit(Op.LOADL,-1); //no super class
		Machine.emit(Op.LOADL, mdl.size()); // # of methods
		for (Integer md_start_label : md_offsetListInCB){
			Machine.emit(Op.LOADA, Reg.CB, md_start_label);
		}


		return arg + cd.rte.size;
	}

	@Override //subject to enhance....
	public Integer visitFieldDecl(FieldDecl fd, Integer arg) { 
		// update runtime entity 
		// visit field declaration list
		return null;
	}

	public Integer visitMethodDecl(MethodDecl md, Integer arg) {
		Integer begin_md = Machine.nextInstrAddr();
		if (md.isMain){
			main_offset = begin_md;
		}
		ParameterDeclList pdl = md.parameterDeclList;
		StatementList sl = md.statementList;

		md.setRTE(Reg.CB, begin_md); //for now size = 1;

		Integer fparaIndex = 0; //formal parameter
		for (ParameterDecl pd : pdl){ 
			pd.setIndex(fparaIndex++);
			pd.visit(this, pdl.size());
		}
		//pdl is not extended from an AST
		Integer stmtIndex = 0, varDeclIndexFilter = 0;
		for (Statement sm : sl){
			sm.rte.index = stmtIndex++;
			if (sm instanceof VarDeclStmt){ // statement enjoys natural indexing
				sm.visit(this, varDeclIndexFilter++);
			}
			else {
				sm.visit(this, varDeclIndexFilter);
			}
		}

		if (md.returnExp != null){
			md.returnExp.visit(this, null);
			Machine.emit(Op.RETURN, 1, 0, pdl.size());
		}
		else {//void
			Machine.emit(Op.RETURN, 0, 0, pdl.size());
		}

		Integer end_md = Machine.nextInstrAddr();
		md.rte.size = end_md - begin_md;
		return null;
	}

	@Override
	public Integer visitMethodDeclHead(MethodDecl md, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override //not used here
	public Integer visitMethodDeclBody(MethodDecl md, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // update later, copy (value/reference accordingly) from caller
	public Integer visitParameterDecl(ParameterDecl pd, Integer arg) { //arg be pdl.size()
		int pd_offset = pd.getRTEIndex() - arg;
		pd.setRTE(Reg.LB, pd_offset);
		return null;
	}

	public Integer visitVarDecl(VarDecl decl, Integer arg) { //which visit which first
		//here is the head of the declaration statement
		decl.setRTE(Reg.LB, arg + 3);
		Machine.emit(Op.PUSH, 1); // reserve space for local var "a" at 3[LB]
		return null;
	}

	@Override
	public Integer visitBaseType(BaseType type, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitClassType(ClassType type, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitArrayType(ArrayType type, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitErrorType(ErrorType type, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitUnsupportedType(UnsupportedType type, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer visitBlockStmt(BlockStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		Integer varDeclIndexFilter = 0, stmtIndex = 0; //in block
		for (Statement sm : stmt.sl){
			sm.rte.index = stmtIndex++;
			if (sm instanceof VarDeclStmt){  // statement enjoys natural indexing
				sm.visit(this, varDeclIndexFilter++);
			}
			else {
				sm.visit(this, varDeclIndexFilter);
			}
		}

		if (stmtIndex > 0){
			Machine.emit(Op.POP, varDeclIndexFilter);
		}

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return null;
	}

	public Integer visitVardeclStmt(VarDeclStmt stmt, Integer arg) { //push and update
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		stmt.varDecl.setIndex(arg); //the index is in charge by the upper layer
		stmt.varDecl.visit(this, arg); //push one entry on the local stack
		stmt.initExp.visit(this, null); //expression taking no arguments, expressions are supposed to have no index
		//Machine.emit(Op.STORE, Reg.LB, stmt.varDecl.rte.address.offset);  		// save its address in local var "a"
		emit_Decl(Op.STORE, stmt.varDecl);

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return null;
	}

	@Override
	public Integer visitAssignStmt(AssignStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		Reference ref = stmt.ref;
		Expression val = stmt.val;

		if (ref instanceof LocalRef){
			val.visit(this, null); //put value of an evaluated expr on stack
			emit_Decl(Op.STORE, ref.getDecl());
		}
		else if (ref instanceof MemberRef){
			RunTimeEntity declRTE = ref.getDeclRTE();

			Machine.emit(Op.LOADA, Reg.OB, 0); 
			Machine.emit(Op.LOADL, declRTE.index);
			val.visit(this, null);			//put value on stack
			Machine.emit(Prim.fieldupd);
		}
		else if (ref instanceof IndexedRef){//array update
			ref.visit(this, 0); //
			val.visit(this, null);
			Machine.emit(Prim.arrayupd);
		}
		else if (ref instanceof DeRef){ //layered update
			DeRef deRef = (DeRef) ref;

			deRef.preRef.visit(this, arg); //push its address
			Integer fieldIndex = deRef.getDeclRTE().index; 
			Machine.emit(Op.LOADL, fieldIndex);
			val.visit(this, null);
			Machine.emit(Prim.fieldupd);

		}
		else if (ref instanceof ThisRef){
			//cannot assign to this
			System.out.println("***==24451x==: Cannot assign to 'this' ");
		}
		else if (ref instanceof ArrayLengthRef){
			System.out.println("***==24451x==: Array length is a final field and cannot be assigned");
			System.exit(4);
			
		}
		else{
			System.out.println("***==24452x==: No such a ref" + ref);
		}

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return null;
	}


	@Override
	public Integer visitCallStmt(CallStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		for (Expression argExpr : stmt.argList){
			argExpr.visit(this, null); //put value on the stackx
		}

		int ifPOP = (stmt.methodRef.getType().typeKind == TypeKind.VOID) ? 0 : 1;

		stmt.methodRef.visit(this, ifPOP);

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return null;
	}

	@Override
	public Integer visitIfStmt(IfStmt stmt, Integer arg) {
		Integer begin_stmt = Machine.nextInstrAddr();
		stmt.setRTE(Reg.CB, begin_stmt);

		stmt.cond.visit(this, null);
		Integer post_cond_label = Machine.nextInstrAddr();
		Machine.emit(Op.JUMPIF, Reg.CB, 0); //to be pached //1 ???

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


		Machine.emit(Op.JUMP, Reg.CB, 0); // to patch

		Integer pre_body_label = Machine.nextInstrAddr();
		stmt.body.visit(this, null);
		Integer pre_cond_label = Machine.nextInstrAddr();
		Machine.patch(begin_stmt, pre_cond_label);
		stmt.cond.visit(this, null);
		Machine.emit(Op.JUMPIF, 1, Reg.CB, pre_body_label);

		Integer end_stmt = Machine.nextInstrAddr();
		stmt.setRTEsize(end_stmt - begin_stmt);
		return null;
	}

	@Override
	public Integer visitUnaryExpr(UnaryExpr expr, Integer arg) {
		expr.expr.visit(this, null);
		Operator op = expr.operator;
		String opSpelling = op.spelling;
		if (opSpelling.contentEquals("!")){
			Machine.emit(Prim.not);
		}
		else if (opSpelling.contentEquals("-")){
			Machine.emit(Prim.neg);
		}
		return null;
	}

	@Override
	public Integer visitBinaryExpr(BinaryExpr expr, Integer arg) {
		Operator op = expr.operator;
		String opSpelling = op.spelling;
		expr.left.visit(this, null);
		expr.right.visit(this, null);

		if (opSpelling.contentEquals("||")){
			Machine.emit(Prim.or);
		}
		else if (opSpelling.contentEquals("&&")){
			Machine.emit(Prim.and);
		}
		else if (opSpelling.contentEquals("==")){
			Machine.emit(Prim.eq);
		}
		else if (opSpelling.contentEquals("!=")){
			Machine.emit(Prim.ne);
		}
		else if (opSpelling.contentEquals("<=")){
			Machine.emit(Prim.le);
		}
		else if (opSpelling.contentEquals("<")){
			Machine.emit(Prim.lt);
		}
		else if (opSpelling.contentEquals(">=")){
			Machine.emit(Prim.ge);
		}
		else if (opSpelling.contentEquals(">")){
			Machine.emit(Prim.gt);
		}
		else if (opSpelling.contentEquals("+")){
			Machine.emit(Prim.add);
		}
		else if (opSpelling.contentEquals("-")){
			Machine.emit(Prim.sub);
		}
		else if (opSpelling.contentEquals("*")){
			Machine.emit(Prim.mult);
		}
		else if (opSpelling.contentEquals("/")){
			Machine.emit(Prim.div);
		}
		else {
			System.out.println("***==4523x==: Invalid operator:");
		}
		return null;
	}

	@Override
	public Integer visitRefExpr(RefExpr expr, Integer arg) {
		//visit Refernece get address, visit Reference expression push its value.
		return expr.ref.visit(this, 1); //just to evaluate
	}

	@Override
	public Integer visitCallExpr(CallExpr expr, Integer arg) {
		for (Expression argExpr : expr.argList){
			argExpr.visit(this, null); //put value on the stackx
		}
		Reference funcRef = expr.functionRef;
		funcRef.visit(this, 0); //Machine.emit(Op.LOAD, Reg.LB, 3); addr of object "a"  (will be OB in p_A)

		return null;
	}

	@Override
	public Integer visitLiteralExpr(LiteralExpr expr, Integer arg) {
		Literal ltr = expr.literal;
		String ltrSpelling = ltr.spelling;
		if (ltr instanceof IntLiteral){
			Machine.emit(Op.LOADL, Integer.parseInt(ltrSpelling)); 
		}
		else if (ltr instanceof BooleanLiteral){
			int boolValue = ltrSpelling.contentEquals("true")? 1 : 0;
			Machine.emit(Op.LOADL, boolValue);
		}
		return null;
	}

	@Override
	public Integer visitNewObjectExpr(NewObjectExpr expr, Integer arg) {

		ClassDecl classObject = expr.classtype.getDecl();

		Integer temp_offset = classObject.getRTEoffset();
		Machine.emit(Op.LOADA, Reg.SB, classObject.getRTEoffset()); //
		
		Machine.emit(Op.LOADL, classObject.fieldDeclList.size()); //create object in heap || what about 0 fields?
		Machine.emit(Prim.newobj); //its address is on the stack top

		return null;
	}

	@Override
	public Integer visitNewArrayExpr(NewArrayExpr expr, Integer arg) {
		expr.rte = new RunTimeEntity(Reg.ST, 0, 1, -1);

		// expr.eltType; Type in array is not being used here
		expr.sizeExpr.visit(this, null); //push value on its stack
		Machine.emit(Prim.newarr);
		return null;
	}

	@Override
	public Integer visitQualifiedRef(QualifiedRef ref, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitIndexedRef(IndexedRef ref, Integer arg) {
		ref.ref.visit(this, 1); //load address, here the address is the evaluated
		ref.indexExpr.visit(this, null);

		if (arg.intValue() != 0){
			Machine.emit(Prim.arrayref);
		}

		return null;
	}

	@Override
	public Integer visitLocalRef(LocalRef ref, Integer arg) { // use arg to distiguish visit as Ref or RefExp
		Declaration declRef = ref.getDecl();
		RunTimeEntity declRTE = declRef.rte;

		if (arg.intValue() == 0){ //here return an address
			emit_as_RTE_addr(Op.LOADA, declRTE);
		}
		else { //here return the corresponding value
			emit_as_RTE_addr(Op.LOAD, declRTE);
		}
		return null;
	}

	@Override
	public Integer visitMemberRef(MemberRef ref, Integer arg) { //may be a fieldRef or a methodRef
		MemberDecl declRef = (MemberDecl) ref.getDecl();
		RunTimeEntity declRTE = declRef.rte;

		//System.out.println("===231234x===" + declRef + "(" + declRef.id.spelling + ")" + declRef.getRTEoffset() + ", " + declRTE.address.offset + "(" + declRTE.index + ")");

		if (declRef instanceof FieldDecl){
			if (arg.intValue() == 0){
				Machine.emit(Op.LOADA, Reg.OB, declRTE.index); //emit_as_RTE_addr(Op.LOADA, declRTE);
			}
			else {
				//emit_as_RTE_addr(Op.LOAD, declRTE);
				Machine.emit(Op.LOAD, Reg.OB, declRTE.index);
			}
		}
		else if (declRef instanceof MethodDecl){
			Machine.emit(Op.LOADA, Reg.OB, 0); //emit_as_RTE_addr(Op.LOAD, declRTE);

			Integer offset = declRTE.address.offset;
			if (offset < 0){
				memPatchList.add(new PatchLine(Machine.nextInstrAddr(), declRef.classIndex, declRTE.index));
			}
			Machine.emit(Op.CALL, Reg.CB, offset); // for NOW is index, subject to convert to offset

			if (arg.intValue() != 0){
				Machine.emit(Op.POP, 1);
			}
		}
		return null;
	}

	@Override
	public Integer visitClassRef(ClassRef ref, Integer arg) {
		Declaration declRef = ref.getDecl();
		RunTimeEntity declRTE = declRef.rte;

		if (arg.intValue() == 0){
			emit_as_RTE_addr(Op.LOADA, declRTE);
		}
		else {
			emit_as_RTE_addr(Op.LOAD, declRTE);
		}

		return null;
	}

	@Override
	public Integer visitThisRef(ThisRef ref, Integer arg) {
		Declaration declRef = ref.getDecl();
		RunTimeEntity declRTE = declRef.rte;

		Machine.emit(Op.LOADA, Reg.OB, 0); //LOADA or LOAD ??
//		if (arg.intValue() == 0){
//		}
//		else {
//			Machine.emit(Op.LOAD, Reg.OB, 0);
//		}
		return null;
	}

	@Override
	public Integer visitDeRef(DeRef ref, Integer arg) {
		if (ref.subId.spelling.contains("println")){
			Machine.emit(Prim.putint);
			Machine.emit(Prim.puteol);
			return null;
		}


		ref.preRef.visit(this, 1); // wht 1 evaluated, if not the last one put its value. 
		Declaration subIdDecl = ref.getDecl();
		RunTimeEntity declRTE = subIdDecl.rte;
		if (subIdDecl instanceof FieldDecl){
			Integer idIndex = ref.getDecl().getRTEIndex();
			Machine.emit(Op.LOADL, idIndex);
			if (arg.intValue() != 0){ //address type
				Machine.emit(Prim.fieldref);
			}
		}
		else if (subIdDecl instanceof MethodDecl){
			int potential_offset = subIdDecl.getRTEoffset();
			if (potential_offset < 0){
				int patchline = Machine.nextInstrAddr();
				int classIndex = ((MethodDecl)subIdDecl).classIndex;
				int mdIndex = subIdDecl.getRTEIndex();
				memPatchList.add(new PatchLine(patchline, classIndex, mdIndex));
			}
			Machine.emit(Op.CALL, Reg.CB, potential_offset);
		}
		else {
			System.out.println("***==323234x==cannot be here");
		}
		return null;
	}


	@Override
	public Integer visitArrayLengthRef(ArrayLengthRef arrlenRef, Integer arg) { //arg == 1
		Reference ref = arrlenRef.arrayRef;
		ref.visit(this, 1);
		Machine.emit(Prim.pred);
		Machine.emit(Op.LOADI);
		return null;
	}



	@Override
	public Integer visitIdentifier(Identifier id, Integer arg) { 
		// TODO Auto-generated method stub 
		return null;
	}

	@Override
	public Integer visitOperator(Operator op, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitIntLiteral(IntLiteral num, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitBooleanLiteral(BooleanLiteral bool, Integer arg) {
		// TODO Auto-generated method stub
		return null;
	}





}
