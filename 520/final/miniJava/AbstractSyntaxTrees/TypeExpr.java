/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * final added
 * @author duozhao
 *
 */
public class TypeExpr extends Expression
{
	public TypeExpr(Type t, SourcePosition posn){
		super(posn);
		type = t;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitTypeExpr(this, o);
	}

	public Type type;

	@Override
	public Type peekType() {
		return type;
	}
	
}
