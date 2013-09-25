package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class LocalRef extends SimpleRef{
	public Identifier localId; //only one id for the Local Reference 
	public LocalRef(Identifier id, SourcePosition posn){
		super(id, posn);
		localId = id;
		refId = id;
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitLocalRef(this, o);
	}
}
/*
 public class RefExpr extends Expression
{
    public RefExpr(Reference r, SourcePosition posn){
        super(posn);
        ref = r;
    }

    public <A,R> R visit(Visitor<A,R> v, A o) {
        return v.visitRefExpr(this, o);
    }

    public Reference ref;
}
 *
 */
