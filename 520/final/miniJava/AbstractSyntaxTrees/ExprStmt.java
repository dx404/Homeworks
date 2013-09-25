/**
 * miniJava Abstract Syntax Tree classes
 * @author Duo Zhao
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;
/**
 * Here is my additional statement: Expression Statement
 * The Expression(epxr) can be 
 * (1) AssignExpr 
 * (2) increment/decrement 
 * (3) CallExpr 
 * (4) NewObjectExpr
 * @author duozhao
 */
public class ExprStmt extends Statement
{
	public ExprStmt(Expression e, SourcePosition posn){
		super(posn);
		expr = e;
	}
	public ExprStmt(Expression e){
		super(e.posn);
		expr = e;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitExprStmt(this, o);
	}

	public Expression expr; 
	
}
