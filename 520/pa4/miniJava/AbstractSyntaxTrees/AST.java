/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import mJAM.Machine.Reg;
import miniJava.CodeGenerator.RunTimeEntity;
import miniJava.SyntacticAnalyzer.SourcePosition;

public abstract class AST {

	public AST (SourcePosition posn) {
		this.posn = posn;
		//pa4 added
		rte = new RunTimeEntity(Reg.SB, -1, -1, -1);
	}

	public String toString() {
		String fullClassName = this.getClass().getName();
		String cn = fullClassName.substring(1 + fullClassName.lastIndexOf('.'));
		if (ASTDisplay.showPosition)
			cn = cn + " " + posn.toString();
		return cn;
	}

	public abstract <A,R> R visit(Visitor<A,R> v, A o);

	public SourcePosition posn;
	
	public RunTimeEntity rte; //pa4
	
	//pa4 added relative to upper level. here is the abstract offset, a relative one

	
	public void setIndex(Integer i){
		rte.index = i;
	}
	
	//pa4 added use to create and RTE for declaration and its enclosing Identifer
	public void setRTE(Reg reg, Integer offset, Integer size, Integer index){
		this.rte.address.reg = reg; 
		this.rte.address.offset = offset;
		this.rte.size = size;
		this.rte.index = index;
	}
	
	public void setRTE(Reg reg, Integer offset, Integer size){
		this.rte.address.reg = reg; 
		this.rte.address.offset = offset;
		this.rte.size = size;
	}
	public void setRTE(Reg reg, Integer offset){
		this.rte.address.reg = reg; 
		this.rte.address.offset = offset;
		this.rte.size = 1; //the default size is 1
	}
	
	public Integer getRTEIndex(){
		return rte.index;
	}
	
	public Integer getRTEoffset(){
		return rte.address.offset;
	}
	
	public Integer setRTEsize(Integer n){
		return rte.size = n;
	}
	
	
}
