/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * modifed to extends coninutedRef
 * @author duozhao
 *
 */
public class IndexedRef extends ContinuedRef {
	
	public IndexedRef(Reference ref, Expression e, SourcePosition posn){
		super(ref, posn);
		this.indexExpr = e;
	}

	public <A,R> R visit(Visitor<A,R> v, A o){
		return v.visitIndexedRef(this, o);
	}
	
	public Expression indexExpr;
	
	public Type getType() {
		Type refType = preRef.getType();
		if (refType instanceof ArrayType){
			ArrayType refArrayType = (ArrayType) refType;
			return refArrayType.eltType;
		}
		else{
			System.out.println("***index referenced to an non-array type");
			return new ErrorType(posn);
		}
	}

	@Override
	public Declaration getDecl() {
		return preRef.getDecl(); //the same declaration its header
	}

	@Override
	public RunTimeEntity getDeclRTE() {
		return preRef.getDeclRTE();
	}

	@Override
	public Integer getDeclRTEoffset() {
		return preRef.getDeclRTE().address.offset;
	}
	
}
