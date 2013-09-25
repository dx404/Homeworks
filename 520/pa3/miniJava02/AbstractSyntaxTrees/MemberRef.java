package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class MemberRef extends SimpleRef{
	
	public Identifier memberId;

	public MemberRef(Identifier id, SourcePosition posn) {
		super(id, posn);
		memberId = id;
		refId = id;
		// TODO Auto-generated constructor stub
	}

	@Override
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitMemberRef(this, o);
	}

}
