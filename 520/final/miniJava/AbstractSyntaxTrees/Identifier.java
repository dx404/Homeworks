/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class Identifier extends Terminal {

	public Identifier (String s, SourcePosition posn) {
		super (s,posn);
		declBinding = null; //not identified
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitIdentifier(this, o);
	}
	
	//final added
	public int resolve_and_bind(IdentificationTable idt){
		return idt.retrieve_and_bind(this);
	}
	
	public int resolve_and_bind(IdentificationTable idt, String lookUpName){
		return idt.retrieve_and_bind(this, lookUpName);
	}

	//pa3 added
	public Declaration declBinding; //applied occurance reference to its declaration
	
	public Type getType(){
		return (declBinding == null) ? null : declBinding.getTypeInRef();
	}
	
	public boolean isDeclPrivate(){ //isPrivate ?
		if (declBinding == null){
			System.out.println("*** Cannot find id declaration");
		}
		else if (declBinding instanceof MemberDecl){
			return ((MemberDecl)declBinding).isPrivate;
		}
		else {
			System.out.println("*** Not declared as a member of a class");
		}
		
		return false;
	}
	
	public boolean isDeclStatic(){ //isPrivate ?
		if (declBinding == null){
			System.out.println("*** Cannot find id declaration");
		}
		else if (declBinding instanceof MemberDecl){
			return ((MemberDecl)declBinding).isStatic;
		}
		else {
			System.out.println("*** Not declared as a member of a class");
		}
		
		return false;
	}
	
	public static final Identifier dummyID = new Identifier("_dummyID", SourcePosition.dummyPos);
	
	public void print(){
		System.out.println("=== ID printer:  " + 
				"id(" + spelling + ") " + 
				"Decl: (" + declBinding +") " +
				"Type: (" + ((declBinding == null)?null:declBinding.getTypeInRef()) + ") ");
	}

}
