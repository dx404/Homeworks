package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ThisRef extends SimpleRef{
	public ThisRef(Identifier id, SourcePosition posn){
		super(id, posn); // here should create a dummy Id for resolve type and decl
	}
	
	public ThisRef(SourcePosition posn){
		super(new Identifier("_this", posn), posn); 
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitThisRef(this, o);
	}
	
}