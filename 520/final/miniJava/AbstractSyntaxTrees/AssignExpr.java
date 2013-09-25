/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class AssignExpr extends Expression
{
	public AssignExpr(Reference r, Expression e, SourcePosition posn){
		super(posn);
		ref = r;
		val = e;
	}
	
	public AssignExpr(RefExpr re, Expression e, SourcePosition posn){
		super(posn);
		ref = re.ref;
		val = e;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitAssignExpr(this, o);
	}

	public Reference ref;
	public Expression val;
	
	@Override
	public Type peekType() {
		return ref.getType();
	}
}