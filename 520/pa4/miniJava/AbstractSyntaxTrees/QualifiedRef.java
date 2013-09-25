/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public class QualifiedRef extends Reference {
	
	public QualifiedRef(boolean thisRelative, IdentifierList ql, SourcePosition posn){
		super(posn);
		this.thisRelative = thisRelative;
		qualifierList = ql;
	}
	
	public QualifiedRef(Identifier id) {
		super(id.posn);
		thisRelative = false;
		qualifierList = new IdentifierList();
		qualifierList.add(id);
	}
	
	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitQualifiedRef(this, o);
	}

	public boolean thisRelative;
	public IdentifierList qualifierList;
	
	@Override
	public Type getType() {
		System.out.println("Not Ready for retrieve a type as a qualified Ref");
		return null;
	}

	@Override
	public Declaration getDecl() {
		// TODO Auto-generated method stub 
		return null;
	}


	@Override
	public RunTimeEntity getDeclRTE() {
		// TODO Auto-generated method stub
		return null;
	}
}
