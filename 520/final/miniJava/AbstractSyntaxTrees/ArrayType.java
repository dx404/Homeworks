/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */

package miniJava.AbstractSyntaxTrees;

import java.util.HashMap;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class ArrayType extends Type {

	public ArrayType(Type eltType, SourcePosition posn){
		super(posn);
		typeKind = TypeKind.ARRAY;
		this.eltType = eltType;
		hasIDT = true;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitArrayType(this, o);
	}

	public Type eltType;


	@Override
	public boolean equals(Type obj) {
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
	
	public IdentificationTable getIDT() { //only length
		return arrayFieldIDT;
	}
	private static final SourcePosition LengthdummyPos = new SourcePosition();
	public static final Type lengthType = new BaseType(TypeKind.INT, LengthdummyPos);
	public static final Identifier lengthID = new Identifier("length", LengthdummyPos);
	public static final FieldDecl lengthFieldDecl = new FieldDecl(false, false, lengthType, lengthID, LengthdummyPos);
	public static final IdentificationTable arrayFieldIDT = establishArrayIDT();

	private static final IdentificationTable establishArrayIDT(){
		IdentificationTable arrfdIDT = 
				new IdentificationTable(
						new HashMap<String, Declaration>());
		arrfdIDT.enter_and_bind(lengthFieldDecl);
		return arrfdIDT;
	}

	@Override
	public boolean isOfType(Type type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toName() {
		return eltType.toName() + "[]";
	}
	
}
