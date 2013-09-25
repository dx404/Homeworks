/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassType extends Type
{
	public ClassType(Identifier id, SourcePosition posn){
		super(posn);
		typeKind = TypeKind.CLASS;
		name = id;
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
			System.out.println("***===237482x===Inconsistent ClassType --> ClassDecl");
			return null;
		}
	}
	
	//public HashMap<String, Declaration> classInstanceMemDeclMap; //pa3 added need a clone copy

	@Override
	public boolean equals(Object obj) { //pa3 added according to the String name
		// TODO Auto-generated method stub
		if (obj == null || obj instanceof UnsupportedType){
			return false;
		}
		else if (obj instanceof ErrorType){
			return true;
		}
		else if (obj instanceof ClassType){
			String objSpelling = ((ClassType)obj).name.spelling;
			String thisSpelling = this.name.spelling;
			return objSpelling.equals(thisSpelling);
		}
		else{
			return false;
		}
	}
}
/*
public boolean equals(Object obj) {
	// TODO Auto-generated method stub
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

*/