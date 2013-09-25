package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * pa3 added
 * @author duozhao
 *
 */
public class ErrorType extends Type{
	public ErrorType(SourcePosition posn){
		super(posn);
		typeKind = TypeKind.ERROR;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitErrorType(this, o);
	}

	//equivalent to any type.
	public boolean equals(Type type) {
		return true;  
	}

	public IdentificationTable getIDT() {
		return IdentificationTable.EmptyIDT;
	}

	public boolean isOfType(Type type) {
		return true;
	}
	
	public static final ErrorType ErrorTypeSample = new ErrorType(SourcePosition.dummyPos);

	@Override
	public String toName() {
		return "%error";
	}
}
