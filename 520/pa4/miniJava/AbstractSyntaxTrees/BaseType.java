/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import miniJava.SyntacticAnalyzer.SourcePosition;

public class BaseType extends Type
{
	public BaseType(TypeKind t, SourcePosition posn){
		super(posn);
		typeKind = t;
	}

	public <A,R> R visit(Visitor<A,R> v, A o) {
		return v.visitBaseType(this, o);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null || obj instanceof UnsupportedType){
			return false;
		}
		else if (obj instanceof ErrorType){
			return true;
		}
		else if (obj instanceof BaseType &&
				this.typeKind == ((BaseType)obj).typeKind){
			return true;
		}
		else{
			return false;
		}
	}
}
/* refer to: 
public boolean equals (Object obj) {
    if (obj != null && obj instanceof ErrorTypeDenoter)
      return true;
    else
      return (obj != null && obj instanceof CharTypeDenoter);
  }
 */ 
