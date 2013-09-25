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
	}
	
	public Identifier id; 
	
	public Type type; //pa3 added
}
