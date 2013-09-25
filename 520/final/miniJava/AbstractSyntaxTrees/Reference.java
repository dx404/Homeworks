/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.ContextualAnalyzer.IdentificationTable;
import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class Reference extends AST
{
	public Reference(SourcePosition posn){
		super(posn);
	}
	
	//pa3 added for get type of Reference;
	public abstract Declaration getDecl();

	public abstract Type getType();
	
	public ClassDecl getTypeDecl(){
		return ((ClassType)getType()).getDecl();
	}
	
	//final added
	public IdentificationTable fetchIDT(){
		return getType().getIDT();
	}
	
	/**
	 * All reference fetch ID table via Type
	 * @return
	 */
	public IdentificationTable fetchIDTviaType(){
		return getType().getIDT();
	}

	public abstract RunTimeEntity getDeclRTE();
	
	public abstract Integer getDeclRTEoffset();
	
}
