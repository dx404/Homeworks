/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class Identifier extends Terminal {

	public Identifier (String s, SourcePosition posn) {
		super (s,posn);
		declBinding = null; //not identified
		type = null;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitIdentifier(this, o);
	}

	//pa3 added
	public Declaration declBinding; //applied occurance reference to its declaration
	public Type type;
	// Either a Declaration or a FieldTypeDenoter


}
