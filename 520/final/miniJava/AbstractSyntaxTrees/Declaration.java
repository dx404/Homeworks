/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class Declaration extends AST {
	public Declaration(Type type, Identifier id, SourcePosition posn) {
		super(posn);
		this.type = type;
		this.id = id;
		
		this.id.declBinding = this;
		
		this.id.rte = this.rte;
	}
	
	public Identifier id; 
	
	public Type type; //pa3 added
	
	public Type getTypeInRef(){
		return type;
	}
	
	public String toLookUpID(){ 
		return id.spelling; //default
	}

}
