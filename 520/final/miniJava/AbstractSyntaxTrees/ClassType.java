/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassType extends Type
{
	public ClassType(Identifier id, SourcePosition posn){
		super(posn);
		typeKind = TypeKind.CLASS;
		name = id;
		hasIDT = true;
	}
	
	public ClassType(Identifier id, TypeKind tk, SourcePosition posn){
		super(posn);
		typeKind = tk;
		name = id;
		hasIDT = false;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitClassType(this, o);
	}

	public Identifier name;
	
	public ClassDecl getDecl(){
		Declaration decl = name.declBinding;
		if (decl instanceof ClassDecl){
			return (ClassDecl) decl;
		}
		else {
			System.out.println("***===237482x (ClassType) ===Inconsistent ClassType --> ClassDecl");
			return null;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public IdentificationTable getIDT(){
		Declaration decl = name.declBinding;
		if (decl instanceof ClassDecl){ // null instanceof X is always false;
			return ((ClassDecl) decl).classIDT;
		}
		else {
			System.out.println("***===237482x (ClassType) ===Inconsistent ClassType --> ClassDecl" + decl);
			return null;
		}
	}
	
	@Override
	public boolean equals(Type obj) { //pa3 added according to the String name
		if (obj == null || obj instanceof UnsupportedType){
			return false;
		}
		else if (obj instanceof ErrorType ){
			return true;
		}
		else if (obj instanceof ClassType){
			if (this.typeKind == TypeKind.NULL || 
					obj.typeKind == TypeKind.NULL){
				return true;
			}
			String objSpelling = ((ClassType)obj).name.spelling;
			String thisSpelling = this.name.spelling;
			return objSpelling.contentEquals(thisSpelling);
		}
		else{
			return false;
		}
	}

	public boolean isOfType(Type type) { //can assign
		if (type == null || 
				type.typeKind == TypeKind.UNSUPPORTED ||
				type.typeKind == TypeKind.NULL){
			return false;
		}
		else if (type.typeKind == TypeKind.ERROR){
			return true;
		}
		else if (type.typeKind == TypeKind.CLASS){
			ClassDecl passInDecl = ((ClassType)type).getDecl();
			return this.getDecl().isSubOrEqualClassOf(passInDecl);
		}
		else{
			return false;
		}
	}

	@Override
	public String toName() {
		return name.spelling;
	}
}
