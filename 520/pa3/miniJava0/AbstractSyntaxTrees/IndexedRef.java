/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class IndexedRef extends Reference {
	
	public IndexedRef(Reference ref, Expression e, SourcePosition posn){
		super(posn);
		this.ref = ref;
		this.indexExpr = e;
	}

	public <A,R> R visit(Visitor<A,R> v, A o){
		return v.visitIndexedRef(this, o);
	}
	
	public Reference ref;
	public Expression indexExpr;
	
	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		Type refType = ref.getType();
		if (refType instanceof ArrayType){
			ArrayType refArrayType = (ArrayType) refType;
			return refArrayType.eltType;
		}
		else{
			System.out.println("index referenced to an non-array type");
			System.exit(4);
			return new ErrorType(posn);
		}
	}
}
