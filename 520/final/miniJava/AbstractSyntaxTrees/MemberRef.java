package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class MemberRef extends SimpleRef{

	public MemberRef(Identifier id, SourcePosition posn) {
		super(id, posn);
	}

}
