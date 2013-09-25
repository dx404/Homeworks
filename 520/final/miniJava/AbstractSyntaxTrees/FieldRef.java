package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class FieldRef extends MemberRef{
	public FieldRef(Identifier id, SourcePosition posn) {
		super(id, posn);
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitFieldRef(this, o);
	}

	public FieldDecl getDecl(){
		return (FieldDecl) simpleId.declBinding;
	}
}
