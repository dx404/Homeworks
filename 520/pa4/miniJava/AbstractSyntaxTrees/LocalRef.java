package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class LocalRef extends SimpleRef{
	public LocalRef(Identifier id, SourcePosition posn){
		super(id, posn);
		simpleId = id;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitLocalRef(this, o);
	}
	
	public Type getType(){
		return simpleId.type;
	}
}

