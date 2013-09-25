/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;
//import miniJava.SyntacticAnalyzer.Token.OpName;

public class BinaryExpr extends Expression
{
	public BinaryExpr(Operator o, Expression e1, Expression e2, SourcePosition posn){
		super(posn);
		operator = o;
		left = e1;
		right = e2;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitBinaryExpr(this, o);
	}

	public Operator operator;
	public Expression left;
	public Expression right;
	
	public BinaryExpr superShortNode; //final added for
	public boolean isRoot = false;

	@Override
	public Type peekType() {
		Type peekType = null;
		switch (operator.opKind) {
		case OR: 
		case AND: 
		case EQ: 
		case NEQ: 
		case LT: 
		case LEQ: 
		case GT: 
		case GEQ: 
		case INSTANCEOF: 
		case NOT:
			peekType = BaseType.BooleanTypeSample;
			break; 
		
		case PLUS:
		case MINUS: 
		case TIMES:
		case DIV: 
		case NEG: 
		case PREINCRE: 
		case PREDECRE: 
		case POSTINCRE: 
		case POSTDECRE: 
			peekType = BaseType.IntTypeSample;
			break;

		default:
			System.out.println("====AST: BinaryExpr: PeekType() Falied: " +
					"No such an operator: " + operator.spelling);
			break;
		}
		
		return peekType;
	}
}