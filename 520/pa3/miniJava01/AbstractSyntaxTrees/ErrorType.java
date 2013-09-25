package miniJava.AbstractSyntaxTrees;

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

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return true; //equivalent to any type. 
	}

}
