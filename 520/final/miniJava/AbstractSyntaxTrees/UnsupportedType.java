package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * pa3 added
 * @author duozhao
 *
 */
public class UnsupportedType extends ClassType{
	public UnsupportedType(Identifier id, SourcePosition posn){
		super(id, posn);
		typeKind = TypeKind.UNSUPPORTED;
	}
	public UnsupportedType(ClassType classType, SourcePosition posn){
		super(classType.name, posn);
		typeKind = TypeKind.UNSUPPORTED;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitUnsupportedType(this, o);
	}

	@Override
	public boolean equals(Type obj) {
		return false; //according to the 2.2 Type checking requirement
	}

}
