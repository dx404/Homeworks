/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.FuncSigContainer;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class MethodDecl extends MemberDecl {

	public MethodDecl(MemberDecl md, ParameterDeclList pl, StatementList sl, Expression e, SourcePosition posn){
		super(md,posn);
		parameterDeclList = pl;
		statementList = sl;
		returnExp = e;
		setupLookUpID();
	}

	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitMethodDecl(this, o);
	}
	
	public <A, R> R visitHead(Visitor<A, R> v, A o) {//pa3 added
		return v.visitMethodDeclHead(this, o);
	}
	public <A, R> R visitBody(Visitor<A, R> v, A o) {//pa3 added
		return v.visitMethodDeclBody(this, o);
	}

	public ParameterDeclList parameterDeclList;
	public StatementList statementList;
	public Expression returnExp;
	public String encodedName; 
	
	//pa 4 add
	public Boolean isMain = null;
	public boolean isMain(){
		return isMain = this.id.spells("main");
	}
	
	public Type getTypeInRef(){
		return type;
	}
	
	public Type getReturnType(){
		return type;
	}
	
	@Override
	public String toLookUpID(){ 
		return encodedName; //default
	}
	
	private void setupLookUpID(){ 
		FuncSigContainer fsc = new FuncSigContainer(this);
		encodedName = fsc.toEncodedName(); //default
	}
	
}
