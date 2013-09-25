/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token.OpName;

public class Operator extends Terminal {

	public Operator (String s, SourcePosition posn) {
		super (s,posn);
		setOpKind();
	}
	
	public Operator (String s, OpName userSetName, SourcePosition posn) {
		super (s,posn);
		opKind = userSetName;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitOperator(this, o);
	}
	
	public OpName opKind;
	
	private void setOpKind(){
		if (spells("||")){
			opKind = OpName.OR;
		}
		else if (spells("&&")){
			opKind = OpName.AND;
		}
		else if (spells("==")){
			opKind = OpName.EQ;
		}
		else if (spells("!=")){
			opKind = OpName.NEQ;
		}
		else if (spells("<")){
			opKind = OpName.LT;
		}
		else if (spells("<=")){
			opKind = OpName.LEQ;
		}
		else if (spells(">")){
			opKind = OpName.GT;
		}
		else if (spells(">=")){
			opKind = OpName.GEQ;
		}
		else if (spells("instanceof")){
			opKind = OpName.INSTANCEOF;
		}
		else if (spells("+")){
			opKind = OpName.PLUS;
		}
		else if (spells("-")){
			opKind = OpName.MINUS;
		}
		else if (spells("*")){
			opKind = OpName.TIMES;
		}
		else if (spells("/")){
			opKind = OpName.DIV;
		}
//		else if (spells("-")){
//			opKind = OpName.NEG;
//		}
		else if (spells("!")){
			opKind = OpName.NOT;
		} // default is binary -
		else if (spells("++")){
			opKind = OpName.POSTINCRE; 
		} //default is post
		else if (spells("--")){
			opKind = OpName.POSTDECRE;
		}
		else{
			System.out.println("====AST: Operator: setOpKind(): Invalid Operator: (" + spelling + ")");
		}
	
	}

}
