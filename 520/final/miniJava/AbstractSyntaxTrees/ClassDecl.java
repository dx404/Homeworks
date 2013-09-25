/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import java.util.HashMap;

import miniJava.ContextualAnalyzer.IdentificationTable;
import  miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassDecl extends Declaration {

	public ClassDecl(Identifier id, FieldDeclList fdl, MethodDeclList mdl, SourcePosition posn) {
		super(ClassDefType.sampleClassDefType, id, posn);
		fieldDeclList = (fdl == null)? new FieldDeclList() : fdl;
		methodDeclList = (mdl == null)? new MethodDeclList() : mdl;

		classIDT = new IdentificationTable(new HashMap<String, Declaration>()); //only with Level 0
		classIDT.isClassIDT = true;
		sampleInstanceType = new ClassType(id, posn); //use to test equivalance
	}

	public <A,R> R visit(Visitor<A, R> v, A o) {
		return v.visitClassDecl(this, o);
	}

	public FieldDeclList fieldDeclList;
	public MethodDeclList methodDeclList;
	
	public int level; //System level is 0, User defined, the level is 1
	
	public IdentificationTable classIDT; //why protected
	
	public ClassType sampleInstanceType; //pa4 added
	
	public Reference superClassRef = null; //final added
	
	public IdentificationTable establishClassIDT(){ //one layer HashMap
		classIDT = new IdentificationTable(new HashMap<String, Declaration>());
		
		for (FieldDecl fd : fieldDeclList){
			classIDT.enter_and_bind(fd); //the type may not be solved
		}
		
		for (MethodDecl md : methodDeclList){
			classIDT.enter_and_bind(md);
		}
		
		return classIDT;
	}

	
	public boolean isSameClassDecl(ClassDecl cd){
		return id.spelling.contentEquals(cd.id.spelling);
	}
	
	//final added
	public boolean isSubOrEqualClassOf(ClassDecl cd){
		if (this == cd){
			return true;
		}
		Reference supRef = superClassRef;
		ClassDecl supClassDecl;
		while (supRef != null){
			supClassDecl = (ClassDecl) supRef.getDecl();
			if (supClassDecl == cd){
				return true;
			}
			supRef = supClassDecl.superClassRef;
		}
		return false;
	}

}
