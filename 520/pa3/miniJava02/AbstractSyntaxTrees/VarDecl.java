/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class VarDecl extends LocalDecl {
	
	public VarDecl(Type t, Identifier id, SourcePosition posn) {
		super(t, id, posn);
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitVarDecl(this, o);
	}
}
