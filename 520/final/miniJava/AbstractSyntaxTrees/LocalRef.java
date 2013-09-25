package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class LocalRef extends SimpleRef{ //May Ref to para Decl or LocalVar
	public LocalRef(Identifier id, SourcePosition posn){
		super (id, posn);
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitLocalRef(this, o);
	}
	
}

