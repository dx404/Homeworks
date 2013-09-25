package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class SubFieldRef extends SubMemberRef{

	public SubFieldRef(Reference ref, Identifier id, SourcePosition posn) {
		super(ref, id, posn);
	}


	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitSubFieldRef(this, o);
	}


	@Override
	public FieldDecl getDecl() {
		if (subID.declBinding instanceof FieldDecl){
			return (FieldDecl) subID.declBinding;
		}
		else{
			System.out.println("***AST: SubFieldRef: Ref to a non-field");
			return null;
		}
	}


	@Override
	public Integer getDeclRTEoffset() {
		return getDecl().getRTEoffset();
	}

}
