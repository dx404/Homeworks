package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * subMemberRef  += MemberID
 * --subFieldRef += (Field = MemberID)
 * --subMethodRef += ArgList
 * IndexRef    += Expression
 * ArrayLengthRef += (LengthID?)
 * @author duozhao
 *
 */
public abstract class ContinuedRef extends Reference{ //simulate DeRef

	public ContinuedRef(Reference ref, SourcePosition posn) {
		super(posn);
		preRef = ref;
	}
	
	public Reference preRef;

}
