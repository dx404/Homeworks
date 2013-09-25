package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;


/**
 * No Field in ArrayLengthRef
 * @author duozhao
 *
 */
public class ArrayLengthRef extends ContinuedRef{

	public ArrayLengthRef(Reference preRef, SourcePosition posn) {
		super(preRef, posn);
	}

	public Declaration getDecl() {
		return ArrayType.lengthFieldDecl; //pseudo-decl
	}

	@Override
	public Type getType() {
		return ArrayType.lengthType;
	}

	@Override
	public RunTimeEntity getDeclRTE() {
		return preRef.getDeclRTE();
	}

	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitArrayLengthRef(this, o);
	}

	@Override
	public Integer getDeclRTEoffset() {
		return preRef.getDeclRTE().address.offset;
	}

}
