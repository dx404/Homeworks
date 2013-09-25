/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

abstract public class Terminal extends AST {

	public Terminal (String s, SourcePosition posn) {
		super(posn);
		spelling = s;
	}

	public String spelling;

	//pa4 added
	public boolean spells(String content){
		return spelling.contentEquals(content);
	}
}
