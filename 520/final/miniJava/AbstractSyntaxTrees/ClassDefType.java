package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassDefType extends Type{ //for class A{} the type of A

	public ClassDefType(SourcePosition posn) {
		super(posn);
		typeKind = TypeKind.CLASS_DEF;
	}

	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ClassDefType sampleClassDefType 
		= new ClassDefType(SourcePosition.dummyPos);
	
	@Override
	public boolean equals(Type type) {
		return false; //why true
	}

	@Override
	public IdentificationTable getIDT() {
		return IdentificationTable.EmptyIDT;
	}

	@Override
	public boolean isOfType(Type type) {
		return true;
	}

	@Override
	public String toName() {
		return "class";
	}

}
