package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class DeRef extends Reference{

	public DeRef(Reference ref, Identifier id, SourcePosition posn) {
		super(posn);
		preRef = ref;
		subId = id;
	}
	
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitDeRef(this, o);
	}

	public Reference preRef;
	public Identifier subId;
	
	public Type getType(){
		return subId.type;
	}

	@Override
	public Declaration getDecl() {
		return subId.declBinding;
	}


	@Override
	public RunTimeEntity getDeclRTE() {
		return subId.declBinding.rte;
	}
}
