package miniJava.SyntacticAnalyzer;

import java.util.ArrayList;

import miniJava.AbstractSyntaxTrees.Identifier;
import miniJava.AbstractSyntaxTrees.MethodDecl;
import miniJava.AbstractSyntaxTrees.ParameterDecl;
import miniJava.AbstractSyntaxTrees.Type;

/**
 * This is for generate mangled name
 * @author duozhao
 *
 */
public class FuncSigContainer {
	public String returnType;
	public String funcName;
	public ArrayList<String> paraTypeList = new ArrayList<String>();
	public FuncSigContainer(String rt, String fn, ArrayList<String> pl){
		returnType = rt;
		funcName = fn;
		paraTypeList = pl;
	}
	
	public FuncSigContainer(String fn, ArrayList<String> pl){
		returnType = null; //no return type;
		funcName = fn;
		paraTypeList = pl;
	}
	public FuncSigContainer(String fn){
		returnType = null; //no return type;
		funcName = fn;
		paraTypeList = new ArrayList<String>();
	}
	public FuncSigContainer(Identifier fn){
		returnType = null; //no return type;
		funcName = fn.spelling;
		paraTypeList = new ArrayList<String>();
	}
	public FuncSigContainer(MethodDecl md){
		returnType = md.type.toName();
		funcName = md.id.spelling;
		for (ParameterDecl pd : md.parameterDeclList){
			paraTypeList.add(pd.type.toName());
		}
	}
	public void setReturnType(String rt){
		returnType = rt;
	}
	public void setReturnType(Type rt){
		returnType = rt.toName();
	}
	public void setFuncName(String name){
		funcName = name;
	}
	public void setFuncName(Identifier id){
		funcName = id.spelling;
	}
	public void addParaType(String paraType){
		paraTypeList.add(paraType);
	}
	public void addParaType(Type paraType){
		paraTypeList.add(paraType.toName());
	}
	public String toEncodedName(){ //no bother with return type
		StringBuilder strbul = new StringBuilder();
		strbul.append("$" + funcName);
		for (String paraType : paraTypeList){
			strbul.append('<' + paraType + '>');
		}
		return strbul.toString();
	}
}
