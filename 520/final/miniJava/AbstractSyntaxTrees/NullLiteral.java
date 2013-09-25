/**
 * miniJava Abstract Syntax Tree classes
 * @author duozhao final added
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class NullLiteral extends Literal {

	public NullLiteral(SourcePosition posn) {
		super("null", posn);
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitNullLiteral(this, o);
	}

	private static Identifier nullID = 
			new Identifier("null", SourcePosition.dummyPos);
	public static final Type nullTypeSample = 
			new ClassType(nullID, TypeKind.NULL, SourcePosition.dummyPos);
	
	@Override
	public Type getType() {
		return nullTypeSample;
	}
}