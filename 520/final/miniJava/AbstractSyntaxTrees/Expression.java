/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import  miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class Expression extends AST {

	public Expression(SourcePosition posn) {
		super (posn);
	}
	
	public Type type; //pa3 added for visitor return
	
	public abstract Type peekType();//use for Identification Checking
	
		
}
