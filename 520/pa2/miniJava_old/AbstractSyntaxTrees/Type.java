/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

abstract public class Type extends AST {
    //why abstract ? 
    public Type(SourcePosition posn){
        super(posn);
    }
    
    public TypeKind typeKind;
}

//class testing{
//	Type m = null;
//	void foo(){
//		m.typeKind = TypeKind.ARRAY;
//	}
//}

        