package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class DeRef extends SimpleRef{

	public DeRef(Reference ref, Identifier id, SourcePosition posn) {
		super(id, posn);
		// TODO Auto-generated constructor stub
		currRef = ref;
		subId = id;
		refId = id;
	}
	
	
//	public Identifier localId; //only one id for the Local Reference 
//	public LocalRef(Identifier id, SourcePosition posn){
//		super(posn);
//		localId = id;
//	}
//	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitDeRef(this, o);
	}

	public Reference currRef;
	public Identifier subId;
}
