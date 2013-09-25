package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class ArrayLengthRef extends Reference{
	public Reference arrayRef;
	public int length;
	
	public ArrayLengthRef(Reference arrayRef, SourcePosition posn){
		super (posn);
		this.arrayRef = arrayRef;
		this.length = 0;
	}

	@Override
	public Type getType() {
		return new BaseType(TypeKind.INT, posn);
	}

	@Override
	public Declaration getDecl() {
		return arrayRef.getDecl();
	}

	@Override
	public RunTimeEntity getDeclRTE() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		// TODO Auto-generated method stub
		return v.visitArrayLengthRef(this, o);
	}



}
