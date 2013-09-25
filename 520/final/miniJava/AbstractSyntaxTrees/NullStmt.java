package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * Final added for nothing between ;;
 */
public class NullStmt extends Statement{
	public NullStmt(SourcePosition posn) {
		super(posn);
	}

	public <A, R> R visit(Visitor<A, R> v, A o) {
		// TODO Auto-generated method stub
		return v.visitNullStmt(this, o);
	}

}
