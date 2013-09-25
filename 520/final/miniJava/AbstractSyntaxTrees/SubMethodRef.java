package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class SubMethodRef extends SubMemberRef{

	public SubMethodRef(Reference ref, Identifier id, ExprList el, SourcePosition posn) {
		super(ref, id, posn);
		argList = el;
	}
	
	public ExprList argList;

	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitSubMethodRef(this, o);
	}
	
	@Override
	public MethodDecl getDecl(){
		if (subID.declBinding instanceof MethodDecl){
			return (MethodDecl) subID.declBinding;
		}
		else{
			System.out.println("***AST: SubMethodRef: Ref to a non-Method");
			return null;
		}
	}

	@Override
	public Integer getDeclRTEoffset() {
		return getDecl().getRTEoffset();
	}

}
