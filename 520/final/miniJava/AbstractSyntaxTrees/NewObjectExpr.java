/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class NewObjectExpr extends NewExpr
{
	public NewObjectExpr(ClassType ct, ExprList el, SourcePosition posn){
		super(posn);
		classtype = ct;
		type = ct;
		argList = el;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitNewObjectExpr(this, o);
	}

	public ClassType classtype;
	
	public ExprList argList; //final added

	@Override
	public Type peekType() {
		return classtype;
	}

}
