package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class SimpleRef extends Reference{
	public SimpleRef(Identifier id, SourcePosition posn) {
		super (posn);
		simpleId =id;
	}
	
	public Identifier simpleId;
	
	public Type getType(){
		return simpleId.type;
	}
	
	public Declaration getDecl(){
		return simpleId.declBinding;
	}
	
	public RunTimeEntity getDeclRTE(){
		return simpleId.rte = simpleId.declBinding.rte;
	}
}
