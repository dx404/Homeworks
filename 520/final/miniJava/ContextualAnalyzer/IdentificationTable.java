package miniJava.ContextualAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

import miniJava.AbstractSyntaxTrees.*;

//public final class IdentificationTable_Tiangle {
/*A final class cannot be extended. 
 * This is done for reasons of security and efficiency.
 */

public class IdentificationTable {// a table of declarations

	public ArrayList<HashMap<String, Declaration>> culumativeIdTable;
	public boolean isClassIDT; //true there is only one layer

	public IdentificationTable() {
		culumativeIdTable = new ArrayList<HashMap<String, Declaration>>();
		culumativeIdTable.add(new HashMap<String, Declaration>());
	}
	
	public IdentificationTable(HashMap<String, Declaration> singleTable) {
		culumativeIdTable = new ArrayList<HashMap<String, Declaration>>();
		culumativeIdTable.add(singleTable);
	}
	
	public static final IdentificationTable EmptyIDT = new IdentificationTable();

	int size(){
		return culumativeIdTable.size();
	}

	// Opens a new level in the identification table, 1 higher than the
	// current topmost level.

	public boolean openScope () { //raise level by one
		return culumativeIdTable.add(new HashMap<String, Declaration>());
	}
	
	public boolean openScope (HashMap<String, Declaration> newLayerTable) { //raise level by one
		return culumativeIdTable.add(newLayerTable);
	}
	public boolean openScope (IdentificationTable idt) { //raise level by one
		return culumativeIdTable.add(idt.culumativeIdTable.get(0)); //only the first one
	}
	public boolean openScope (ClassDecl cd) { //raise level by one
		return culumativeIdTable.add(cd.classIDT.culumativeIdTable.get(0)); //only the first one
	}

	// Closes the topmost level in the identification table, discarding
	// all entries belonging to that level.

	public HashMap<String, Declaration> closeScope () { //decrease level by one
		int topIndex = culumativeIdTable.size() - 1;
		return culumativeIdTable.remove(topIndex);
	}
	

	public int enter(String spelling, Declaration attr) { //set enter == 0 duplicate
		int topIndex = culumativeIdTable.size() - 1;
		HashMap<String, Declaration> topLevelMap = culumativeIdTable.get(topIndex);
		if(topLevelMap.get(spelling) != null){ //check top level
			System.out.println("***===89892x===: (From IdentificationTable) duplicate Declaration:" + attr + "(" + spelling + ")");
			System.exit(4);
			return 0;  //for duplicate
		}
		for(int i = topIndex - 1; i >= 3; i--){
			if(culumativeIdTable.get(i).get(spelling) != null){
				System.out.println("***===89893x===: duplicate Declaration:" + attr + "(" + spelling + ")" + "level :" + i);
				System.exit(4);
				return 0;  //for duplicate
			}
		}
		topLevelMap.put(spelling, attr);
		return 1;
	}
	
	public int enter(Identifier id, Declaration attr) { 
		return enter(id.spelling, attr);
	}

	public int enter_and_bind(Declaration attr){
		//int status = enter(attr.id.spelling, attr);
		int status = enter(attr.toLookUpID(), attr);
		attr.id.declBinding = attr;
		return status;
	}

	public int set(int level, HashMap<String, Declaration> levelTable){
		HashMap<String, Declaration> topLevelMap;
		//hard assignment, may result out of boundary error
		if (level < 0 || level >= size() - 1){
			printTable();
			System.out.println("***--12312xx--: level: " + level + "--Out of the level range of table size: " + (size() - 1));
			System.exit(4);
		}
		else{
			if (level == size() - 1){
				topLevelMap = levelTable;
				culumativeIdTable.set(level, topLevelMap);
			}
			culumativeIdTable.set(level, levelTable);
		}
		
		return 1;
		
	}
	

	
	public HashMap<String, Declaration> getTopLevelTable(){
		return culumativeIdTable.get(0);
	}
	
	public int setEmptyTop(HashMap<String, Declaration> levelTable){
		if (getMapAtLevel(size() -1).size() != 0){
			System.out.println("***==923423x===, Top is not empty");
		}
		return set(size() -1, levelTable);
	}
	
	public int setEmptyTop(IdentificationTable idt){
		if (getMapAtLevel(size() -1).size() != 0){
			System.out.println("***==923423x===, Top is not empty");
		}
		return set(size() -1, idt.getMapAtLevel(0));
	}

	public HashMap<String, Declaration> getMapAtLevel(int level){
		return culumativeIdTable.get(level);
	}

	// Finds an entry for the given identifier in the identification table,
	// if any. If there are several entries for that identifier, finds the
	// entry at the highest level, in accordance with the scope rules.
	// Returns null iff no entry is found.
	// otherwise returns the attribute field of the entry found.

	public int printTable(){
		for (int i = 0 ; i < culumativeIdTable.size(); i++){
			System.out.println("=== L" + i + ": " + culumativeIdTable.get(i));
		}
		System.out.println("\r\n");
		return 1;
	}

	public Declaration retrieve (String id) {
		Declaration attr = null;	
		for(int i = size() -1; i >= 0; i--){
			attr = culumativeIdTable.get(i).get(id);
			if(attr != null){
				return attr;
			}
		}
		return null;
	}
	public Declaration retrieve (Identifier id) {
		return retrieve(id.spelling);
	}
	
	public int retrieve_and_bind(Identifier id) {
		Declaration attr = null;	
		int i;
		for(i = size() -1; i >= 0; i--){
			attr = culumativeIdTable.get(i).get(id.spelling);
			if(attr != null){
				id.declBinding = attr;
				return i;
			}
		}
		return i;
	}
	//if look-name differs from its id spelling
	public int retrieve_and_bind(Identifier id, String lookUpName) {
		Declaration attr = null;	
		int i;
		for(i = size() -1; i >= 0; i--){
			attr = culumativeIdTable.get(i).get(lookUpName);
			if(attr != null){
				id.declBinding = attr;
				return i;
			}
		}
		return i;
	}
	


}
