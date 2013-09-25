package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class SimpleRef extends Reference{ //not abstract 
	public SimpleRef(Identifier id, SourcePosition posn) {
		super (posn);
		simpleId =id;
	}
	
	public Identifier simpleId;
	
	public Declaration getDecl(){
		return simpleId.declBinding;
	}
	public Type getType(){
		return simpleId.getType();
	}
	
	public RunTimeEntity getDeclRTE(){
		return simpleId.rte = simpleId.declBinding.rte;
	}

	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitSimpleRef(this, o);
	}
	@Override
	public Integer getDeclRTEoffset() {
		return getDeclRTE().address.offset;
	}

}
