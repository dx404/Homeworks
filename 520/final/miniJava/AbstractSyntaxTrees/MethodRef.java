package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class MethodRef extends MemberRef{
	public MethodRef(Identifier methodID, ExprList el, SourcePosition posn) {
		super(methodID, posn);
		argList = el;
	}
	
	public ExprList argList;
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitMethodRef(this, o);
	}
	
	@Override
	public MethodDecl getDecl(){
		return (MethodDecl) simpleId.declBinding;
	}

}
