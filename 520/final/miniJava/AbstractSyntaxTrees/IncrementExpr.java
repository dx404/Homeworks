package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token.OpName;

public class IncrementExpr extends Expression{
	public IncrementExpr(Reference r, boolean isPostOp, SourcePosition posn){
		super (posn);
		ref = r;
		isPost = isPostOp;
		opKind = isPost? OpName.POSTINCRE : OpName.PREINCRE;
		increOp = new Operator("++", opKind, posn);
	}
	public IncrementExpr(Reference r, SourcePosition posn){
		super (posn);
		ref = r;
		isPost = true;
		opKind = OpName.POSTINCRE;
		increOp = new Operator("++", opKind, posn);
	}
	public IncrementExpr(RefExpr re, boolean isPostOp, SourcePosition posn){
		super (posn);
		ref = re.ref;
		isPost = isPostOp;
		opKind = isPost ? OpName.POSTINCRE : OpName.PREINCRE;
		increOp = new Operator("++", opKind, posn);
	}
	public IncrementExpr(RefExpr re, SourcePosition posn){
		super (posn);
		ref = re.ref;
		isPost = true;
		opKind = OpName.POSTINCRE;
		increOp = new Operator("++", opKind, posn);
	}
	
	public OpName opKind;
	public Operator increOp;
	public Reference ref;
	public boolean isPost = true; //default to be post increment
	
	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitIncrementExpr(this, o);
	}
	@Override
	public Type peekType() {
		return BaseType.IntTypeSample;
	}
}
