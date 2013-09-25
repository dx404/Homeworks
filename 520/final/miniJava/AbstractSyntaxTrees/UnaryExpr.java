/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;
import miniJava.SyntacticAnalyzer.Token.OpName;

public class UnaryExpr extends Expression
{
	public UnaryExpr(Operator o, Expression e, SourcePosition posn){
		super(posn);
		operator = o;
		expr = e;
		if (operator.spells("-")){
			operator.opKind = OpName.NEG;
		}
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitUnaryExpr(this, o);
	}

	public Operator operator;
	public Expression expr;
	
	
	@Override
	public Type peekType() {
		if (operator.opKind == OpName.NEG){
			return BaseType.IntTypeSample;
		}
		else if (operator.opKind == OpName.NOT){
			return BaseType.BooleanTypeSample;
		}
		else{
			System.out.println("===AST: UnaryExpr: peekType() failed. " +
					"Invalid operator in UnaryExpr (" + operator.spelling + ")");
		}
		return null;
	}
}