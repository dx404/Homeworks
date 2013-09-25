package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class SubMemberRef extends ContinuedRef{

	public SubMemberRef(Reference ref, Identifier id, SourcePosition posn) {
		super(ref, posn);
		subID = id;
	}
	
	public Identifier subID;
	
	public abstract Declaration getDecl();
	
	public Type getType() {
		return subID.getType();
	}
	
	public RunTimeEntity getDeclRTE() {
		return subID.rte = subID.declBinding.rte;
	}
}
