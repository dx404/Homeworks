/**
 * miniJava Abstract Syntax Tree classes
 * @author prins
 * @version COMP 520 (v2.2)
 */
package miniJava.AbstractSyntaxTrees;

import java.util.*;

/**
 * ExprStmtList is primarily for for loop
 * initExpr, or updateExpr. 
 * @author duozhao
 *
 */
public class ExprStmtList implements Iterable<ExprStmt>
{
	public ExprStmtList() {
		slist = new ArrayList<ExprStmt>();
	}

	public void add(ExprStmt s){
		slist.add(s);
	}

	public ExprStmt get(int i){
		return slist.get(i);
	}

	public int size() {
		return slist.size();
	}

	public Iterator<ExprStmt> iterator() {
		return slist.iterator();
	}

	private List<ExprStmt> slist;
}
