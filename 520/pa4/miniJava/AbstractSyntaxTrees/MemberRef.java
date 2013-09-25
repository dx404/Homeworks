package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class MemberRef extends SimpleRef{

	public MemberRef(Identifier id, SourcePosition posn) {
		super(id, posn);
		simpleId = id;
	}

	@Override
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitMemberRef(this, o);
	}
	
	public Type getType(){
		return simpleId.type;
	}

}
