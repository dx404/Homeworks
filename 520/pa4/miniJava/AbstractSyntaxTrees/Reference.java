/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class Reference extends AST
{
	public Reference(SourcePosition posn){
		super(posn);
	}
	
	//pa3 added for get type of Reference;
	public abstract Type getType();
	
	public abstract Declaration getDecl();
	
	public abstract RunTimeEntity getDeclRTE();
}
