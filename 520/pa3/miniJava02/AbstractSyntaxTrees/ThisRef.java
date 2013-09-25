package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ThisRef extends SimpleRef{
	public ThisRef(Identifier id, SourcePosition posn){
		super(id, posn);
		dummyId = id; //try to identify what is "this"
		refId = id;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitThisRef(this, o);
	}
	
	public Identifier resolvedThisId;
	public Identifier dummyId;
	
}