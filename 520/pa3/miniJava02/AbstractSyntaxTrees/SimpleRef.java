package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class SimpleRef extends Reference{
	public SimpleRef(Identifier id, SourcePosition posn) {
		super (posn);
		refId =id;
	}
	
	public Identifier refId;
	
	public Type getType(){
		return refId.type;
	}
	
	public Declaration getDecl(){
		return refId.declBinding;
	}
}
