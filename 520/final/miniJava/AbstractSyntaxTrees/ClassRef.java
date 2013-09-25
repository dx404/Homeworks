package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public final class ClassRef extends SimpleRef{ 
	public ClassRef(Identifier id, SourcePosition posn){
		super(id, posn);
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitClassRef(this, o);
	}
	
	public IdentificationTable fetchIDT(){
		IdentificationTable staticIDT = null;
		if (simpleId.declBinding instanceof ClassDecl){
			ClassDecl cd = (ClassDecl) simpleId.declBinding;
			staticIDT = cd.classIDT;
		}
		else {
			System.out.println("***47928x=== ClassRef, inconsistent ClassRef with ClassDecl");
		}
		return staticIDT;
	}
	
	public ClassDecl getDecl(){
		return (ClassDecl) simpleId.declBinding;
	}
}