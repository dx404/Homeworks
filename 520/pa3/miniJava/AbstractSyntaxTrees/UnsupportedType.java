package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * pa3 added
 * @author duozhao
 *
 */
public class UnsupportedType extends Type{
	public UnsupportedType(Identifier id, SourcePosition posn){
		super(posn);
		typeKind = TypeKind.UNSUPPORTED;
		name = id;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitUnsupportedType(this, o);
	}
	
	Identifier name;

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false; //according to the 2.2 Type checking requirement
	}
	
	

}
