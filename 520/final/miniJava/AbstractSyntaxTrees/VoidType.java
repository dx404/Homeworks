package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * Void type as a special BaseType
 * only for method return
 * @author duozhao
 *
 */
public final class VoidType extends Type{
	public VoidType(SourcePosition posn){
		super(posn);
		typeKind = TypeKind.VOID;
	}
	
	public static final VoidType VoidTypeSample = new VoidType(new SourcePosition());

	@Override
	public boolean equals(Type type) {
		return false;
	}

	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitVoidType(this, o);
	}

	@Override
	public IdentificationTable getIDT() {
		return IdentificationTable.EmptyIDT;
	}

	@Override
	public boolean isOfType(Type type) {
		return false;
	}

	@Override
	public String toName() {
		return "void";
	}
}
