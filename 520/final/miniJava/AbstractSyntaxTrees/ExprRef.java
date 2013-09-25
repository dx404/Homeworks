package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * only new Expression are allowed here
 * such as in the statement <new A().start();>
 * The new A() part is a ExprRef
 */
public class ExprRef extends Reference{
	/**
	 * @param e should be RefExpr, newObjeExpr, newArrayExpr, CallExpr
	 */
	public ExprRef(Expression e, SourcePosition posn) {
		super(posn);
		expr = e;
	}
	public ExprRef(Expression e) {
		super(e.posn);
		expr = e;
	}

	public Expression expr;
	
	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitExprRef(this, o);
	}
	
	public Declaration getDecl() {
		// TODO To be implemented
		return null;
	}

	@Override
	public Type getType() {
		// TODO To be implemented
		return null;
	}

	@Override
	public RunTimeEntity getDeclRTE() {
		// TODO To be implemented
		return null;
	}

	@Override
	public Integer getDeclRTEoffset() {
		// TODO To be implemented
		return null;
	}

}
