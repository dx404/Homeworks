package miniJava.CodeGenerator;

import mJAM.Machine;
import mJAM.Machine.*;
import miniJava.AbstractSyntaxTrees.BinaryExpr;
import miniJava.SyntacticAnalyzer.Token.OpName;

public class ShortCircuitEvaluator {
	Encoder encoder;
	BinaryExpr binExpr; //must be root node

	public ShortCircuitEvaluator(BinaryExpr be, Encoder ed){
		binExpr = be;
		encoder = ed;
	}
	public ShortCircuitEvaluator(Encoder ed){
		binExpr = null;
		encoder = ed;
	}

	public void emitShortCircuitCode(){
		binExpr.isRoot = true;
		FindJumpNode(binExpr, binExpr);
		ShortCircutEmit(binExpr, 0); //0 is for root node
		patchShortCutJump(binExpr, 0);//0 is for root node
	}

	public void emitShortCircuitCode(BinaryExpr binExpr){
		binExpr.isRoot = true;
		FindJumpNode(binExpr, binExpr);
		ShortCircutEmit(binExpr, 0); //0 is for root node
		patchShortCutJump(binExpr, 0);//0 is for root node
	}

	public void pushError(String errMsg){
		System.out.println("*** ==Error== ShortCircuitEvaluator:" + errMsg);
		System.exit(4);
	}

	Integer FindJumpNode(BinaryExpr currExpr, BinaryExpr upper){
		if (currExpr.operator.opKind == upper.operator.opKind){
			currExpr.superShortNode = upper;
		}
		else {
			currExpr.superShortNode = currExpr;
		}
		if (currExpr.left instanceof BinaryExpr){
			FindJumpNode((BinaryExpr) currExpr.left, currExpr.superShortNode);
		}
		if (currExpr.right instanceof BinaryExpr){
			FindJumpNode((BinaryExpr) currExpr.right, currExpr.superShortNode);
		}
		return 0;
	}
	Integer ShortCircutEmit(BinaryExpr expr, Integer arg){
		int begin = Machine.nextInstrAddr();
		if (expr.left instanceof BinaryExpr && 
				(((BinaryExpr)expr.left).operator.opKind == OpName.AND ||
				((BinaryExpr)expr.left).operator.opKind == OpName.OR )){
			ShortCircutEmit((BinaryExpr) expr.left, 1);
		}
		else {
			expr.left.visit(encoder, 1);
		}

		switch(expr.operator.opKind){
		case OR:
			Machine.emit(Op.JUMPIF, 1, Reg.CB, -1); //keep value
			break;
		case AND:
			Machine.emit(Op.JUMPIF, 0, Reg.CB, -1); //keep value
			break;
		default:
			break;
		}

		if (expr.right instanceof BinaryExpr && 
				(((BinaryExpr)expr.right).operator.opKind == OpName.AND ||
				((BinaryExpr)expr.right).operator.opKind == OpName.OR )){
			ShortCircutEmit((BinaryExpr) expr.right, 1);
		}
		else {
			expr.right.visit(encoder, 1);
		}

		if (arg == 0){//root post set-up
			switch(expr.operator.opKind){
			case OR:
				Machine.emit(Op.JUMP, Reg.CB, -1);
				Machine.emit(Op.LOADL, 1); //return true value
				break;
			case AND:
				Machine.emit(Op.JUMP, Reg.CB, -1);
				Machine.emit(Op.LOADL, 0); //return false value
				break;
			default:
				break;
			}
		}
		int end = Machine.nextInstrAddr();
		expr.setRTE(Reg.CB, begin, end - begin);
		return 0;
	}

	Integer patchShortCutJump(BinaryExpr expr, Integer arg){
		int jumpline = expr.right.rte.address.offset - 1;
		int endOfSuperNode = expr.superShortNode.rte.getEnd();
		if (expr.left instanceof BinaryExpr&& 
				(((BinaryExpr)expr.left).operator.opKind == OpName.AND ||
				((BinaryExpr)expr.left).operator.opKind == OpName.OR )){
			patchShortCutJump((BinaryExpr)expr.left, 1);
		}
		if (expr.right instanceof BinaryExpr&& 
				(((BinaryExpr)expr.right).operator.opKind == OpName.AND ||
				((BinaryExpr)expr.right).operator.opKind == OpName.OR )){
			patchShortCutJump((BinaryExpr)expr.right, 1);
		}
		if (expr.superShortNode.isRoot == true){
			Machine.patch(jumpline, endOfSuperNode -1);
			Machine.patch(endOfSuperNode-2, endOfSuperNode);
		}else{
			Machine.patch(jumpline, endOfSuperNode + 1);
		}
		return null;
	}

}
