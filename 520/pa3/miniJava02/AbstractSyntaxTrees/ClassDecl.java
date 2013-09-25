/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import java.util.HashMap;

import  miniJava.SyntacticAnalyzer.SourcePosition;

public class ClassDecl extends Declaration {

	public ClassDecl(Identifier id, FieldDeclList fdl, MethodDeclList mdl, SourcePosition posn) {
		super(null, id,posn);
		//fieldDeclList = fdl;
		//methodDeclList = mdl;
		
		//pa3 modified
		fieldDeclList = (fdl == null)? new FieldDeclList() : fdl;
		methodDeclList = (mdl == null)? new MethodDeclList() : mdl;

		classMemDeclMap = new HashMap<String, Declaration>(); //not enabled

	}

	public <A,R> R visit(Visitor<A, R> v, A o) {
		return v.visitClassDecl(this, o);
	}

	public FieldDeclList fieldDeclList;
	public MethodDeclList methodDeclList;

	protected HashMap<String, Declaration> classMemDeclMap; //pa3 added
	
	public HashMap<String, Declaration> getHashMap(){
		return classMemDeclMap;
	}
	
	public int level; //if system, the level is 0, otherwise (user defined, the level is 1.

	public HashMap<String, Declaration> enableClassMemDeclMap(){
		return enableClassMemDeclMap(1); //default at level 1;
	}

	public HashMap<String, Declaration> enableClassMemDeclMap(int setlevel){ //0 or 1
		classMemDeclMap = new HashMap<String, Declaration>(); 
		//here there is decoration, but with type resolving. in form
		level = setlevel;

		this.id.type = null; //here is not correct Class A{ } the type of A is null, no need to resolve
		this.id.declBinding = this;

		for(FieldDecl fd : fieldDeclList){
			if(fd.isStatic && level > 0){
				System.out.println("Non-static field");
				System.exit(4);
			}
//			fd.id.type = fd.type; //here type is not resolved
//			fd.id.declBinding = fd;
			fd.setId2Declbindings();
			classMemDeclMap.put(fd.id.spelling, fd);
		}

		for(MethodDecl md: methodDeclList){
			if(md.isStatic && !md.id.spelling.equals("main")){
				System.out.println("Non-static method other than main()");
				System.exit(4);
			}
//			md.id.type = md.type;
//			md.id.declBinding = md; //enable binding. 
			md.setId2Declbindings();
			classMemDeclMap.put(md.id.spelling, md);
		}

		return classMemDeclMap;
	}

}
