/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */

package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ArrayType extends Type {

	public ArrayType(Type eltType, SourcePosition posn){
		super(posn);
		typeKind = TypeKind.ARRAY;
		this.eltType = eltType;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitArrayType(this, o);
	}

	public Type eltType;
	
	//pa4 added
	public int length;

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null || obj instanceof UnsupportedType){
			return false;
		}
		else if (obj instanceof ErrorType){
			return true;
		}
		else if (obj instanceof ArrayType){
			Type elt = ((ArrayType) obj).eltType;
			if(elt instanceof BaseType){
				return ((BaseType)elt).equals(this.eltType);
			}
			else if (elt instanceof ClassType){
				return ((ClassType)elt).equals(this.eltType);
			}
			else{
				System.out.println("***Incorrect Array type....");
				return false;
			}
		}
		else{
			return false;
		}
	}
}
