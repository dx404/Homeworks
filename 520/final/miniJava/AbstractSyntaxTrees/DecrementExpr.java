package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token.OpName;

public class DecrementExpr extends Expression{
	public DecrementExpr(Reference r, boolean isPostOp, SourcePosition posn){
		super (posn);
		ref = r;
		isPost = isPostOp;
		opKind = isPost? OpName.POSTDECRE : OpName.PREDECRE;
		decreOp = new Operator("--", opKind, posn);
	}
	public DecrementExpr(Reference r, SourcePosition posn){
		super (posn);
		ref = r;
		isPost = true;
		opKind = OpName.POSTDECRE;
		decreOp = new Operator("--", opKind, posn);
	}
	public DecrementExpr(RefExpr re, boolean isPostOp, SourcePosition posn){
		super (posn);
		ref = re.ref;
		isPost = isPostOp;
		opKind = isPost ? OpName.POSTDECRE : OpName.PREDECRE;
		decreOp = new Operator("--", opKind, posn);
	}
	public DecrementExpr(RefExpr re, SourcePosition posn){
		super (posn);
		ref = re.ref;
		isPost = true;
		opKind = OpName.POSTDECRE;
		decreOp = new Operator("--", opKind, posn);
	}
	
	public OpName opKind;
	public Operator decreOp;
	public Reference ref;
	public boolean isPost = true; //default to be post Decrement
	
	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitDecrementExpr(this, o);
	}
	@Override
	public Type peekType() {
		return BaseType.IntTypeSample;
	}
}
