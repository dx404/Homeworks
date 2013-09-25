package miniJava.AbstractSyntaxTrees;

import java.util.ArrayList;

import miniJava.SyntacticAnalyzer.SourcePosition;

/**
 * Here is for the supportation of int x = 1, y, z = y;
 * @author duozhao
 *
 */
public class VarDeclListStmt extends Statement{
	public VarDeclListStmt(Type t, IdentifierList idList, ExprList initList, SourcePosition posn){
		super (posn);
		for (Identifier id : idList){
			vdList.add(new VarDecl(t, id, posn));
		}
		this.commonType = t;
		this.initList = initList;
	}
	
	public VarDeclListStmt(ArrayList<VarDecl> vdList, ExprList initList, SourcePosition posn){
		super (posn);
		this.vdList = vdList;
		this.commonType = vdList.get(0).getTypeInRef();
		this.initList = initList;
	}
	
	public Type commonType;
	public ArrayList<VarDecl> vdList = new ArrayList<VarDecl>();
	public ExprList initList; //same size;
	
	@Override
	public <A, R> R visit(Visitor<A, R> v, A o) {
		return v.visitVarDeclListStmt(this, o);
	};
	
}
