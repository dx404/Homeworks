/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class BaseType extends Type
{
	public BaseType(TypeKind t, SourcePosition posn){
		super(posn);
		typeKind = t;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitBaseType(this, o);
	}

	public boolean equals(Type obj) {
		if (obj == null || obj instanceof UnsupportedType){
			return false;
		}
		else if (obj instanceof ErrorType){
			return true;
		}
		else if (obj instanceof BaseType &&
				this.typeKind == ((BaseType)obj).typeKind){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static final BaseType IntTypeSample = new BaseType(TypeKind.INT, SourcePosition.dummyPos);
	public static final BaseType BooleanTypeSample = new BaseType(TypeKind.BOOLEAN, SourcePosition.dummyPos);

	public IdentificationTable getIDT() {
		return IdentificationTable.EmptyIDT;
	}

	public boolean isOfType(Type type) {
		if (type == null || 
				type.typeKind == TypeKind.UNSUPPORTED ||
				type.typeKind == TypeKind.NULL){
			return false;
		}
		else if (type.typeKind == TypeKind.ERROR){
			return true;
		}
		else if (type.typeKind == TypeKind.INT||
				type.typeKind == TypeKind.BOOLEAN){
			return this.typeKind == type.typeKind;
		}
		else{
			return false;
		}
	}

	@Override
	public String toName() {
		if (typeKind == TypeKind.INT){
			return "int";
		}
		else if (typeKind == TypeKind.BOOLEAN){
			return "boolean";
		}
		else if ((typeKind == TypeKind.VOID)){
			return "void";
		}
		return null;
	}
}

