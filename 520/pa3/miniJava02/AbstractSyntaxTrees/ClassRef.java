package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassRef extends SimpleRef{
	public Identifier classId; //only one id for the Local Reference 
	public ClassRef(Identifier id, SourcePosition posn){
		super(id, posn);
		classId = id;
		refId = id;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitClassRef(this, o);
	}
	
	//getType is null
}