package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * final project added
 * for the AST of the condtion
 * @author duozhao
 *
 */
public class ForStmt extends Statement{
	public ForStmt(StatementList init, Expression cond, StatementList update, Statement body, SourcePosition posn) {
		super(posn);
		this.init = init;
		this.cond = cond;
		this.update = update;
		this.forBody = body;
	}
	public StatementList init;
	public Expression cond;
	public StatementList update;
	public Statement forBody;

	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitForStmt(this, o);
	}

}
