/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

abstract public class Type extends AST {
	public Type(SourcePosition posn){
		super(posn);
	}

	public TypeKind typeKind;
	
	public abstract boolean equals(Type type);//pa3 added
	
	public abstract boolean isOfType (Type type);
	
	public abstract IdentificationTable getIDT(); //final added
	
	public boolean hasIDT = false; //final added, if there is an id Table
	
	public abstract String toName();
	
}



