package miniJava.ContextualAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;

import miniJava.AbstractSyntaxTrees.*;

//public final class IdentificationTable_Tiangle {
/*A final class cannot be extended. 
 * This is done for reasons of security and efficiency.
 */

public class IdentificationTable implements Cloneable{// a table of declarations

	private HashMap<String, Declaration> topLevelMap; //  private IdEntry latest;
	public ArrayList<HashMap<String, Declaration>> culumativeIdTable;

	public IdentificationTable() {
		topLevelMap = new HashMap<String, Declaration>(); //instead of null
		culumativeIdTable = new ArrayList<HashMap<String, Declaration>>();
		culumativeIdTable.add(topLevelMap);
	}

	int getTopLevel(){ //  private int level; //the topest index
		return culumativeIdTable.size() - 1;
	}

	// Opens a new level in the identification table, 1 higher than the
	// current topmost level.

	public void openScope () { //raise level by one
		topLevelMap = new HashMap<String, Declaration>();
		culumativeIdTable.add(topLevelMap);
		//level ++; //doing nothing here, automatically update
	}

	// Closes the topmost level in the identification table, discarding
	// all entries belonging to that level.

	public void closeScope () { //decrease level by one
		//topLevelMap.clear();
		culumativeIdTable.remove(getTopLevel());
	}

	// Makes a new entry in the identification table for the given identifier
	// and attribute. The new entry belongs to the current level.
	// duplicated is set to to true iff there is already an entry for the
	// same identifier at the current level.

	public int enter(String id, Declaration attr) { //set enter == 0 duplicate
		topLevelMap = getMapAtLevel(getTopLevel());
		if(topLevelMap.get(id) != null){
			System.out.println("===89892x===: duplicate Declaration:" + attr + "(" + id + ")");
			printTable();
			System.out.println(topLevelMap);
			System.exit(4);
			return 0;  //for duplicate
		}
		for(int i = getTopLevel() - 1; i >= 3; i--){
			if(getMapAtLevel(i).get(id) != null){
				System.out.println("===89893x===: duplicate Declaration:" + attr + "(" + id + ")");
				printTable();
				System.exit(4);
				return 0;  //for duplicate
				//System.exit(4);
			}
		}
		topLevelMap.put(id, attr);
		return 1;
	}

	public int set(int level, HashMap<String, Declaration> levelTable){
		//hard assignment, may result out of boundary error
		if (level < 0 || level > getTopLevel()){
			printTable();
			System.out.println("--12312xx--: level: " + level + "--Out of the level range of table size: " + getTopLevel());
			System.exit(65536);
		}
		else{
			if (level == getTopLevel()){
				topLevelMap = levelTable;
				culumativeIdTable.set(level, topLevelMap);
				System.out.println("--234234x---: " + (topLevelMap));
			}
			culumativeIdTable.set(level, levelTable);
		}
		
		return 1;
		
	}
	

	
	public HashMap<String, Declaration> getTopLevelTable(){
		return culumativeIdTable.get(0);
	}

	public int append(HashMap<String, Declaration> levelTable){
		if (topLevelMap.size() != 0){
			openScope();
		}
		else{
			System.out.println("==1123x==: scope has been opened" );
		}
		return set(getTopLevel(), topLevelMap = levelTable);
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
			System.out.println("L" + i + ": " + culumativeIdTable.get(i));
		}
		return 1;
	}

	public Declaration retrieve (String id) {
		Declaration attr = null;	
		int i;
		for(i = getTopLevel(); i >= 0; i--){
			attr = culumativeIdTable.get(i).get(id);
			if(attr != null){
				System.out.println("Get entry at Level: " + getTopLevel() + " (" + id + ")");
				return attr;
			}
		}

		System.out.println("Id(" + id + ") Not Found" 
				+ " Current Level: " + getTopLevel() + " End Level: " + i);

		for(i = 0; i <= getTopLevel(); i++){
			System.out.println("Level:" + i + "--> size: " + culumativeIdTable.get(i).size());

		}
		return null;
	}

	//	public int enterClassMap(HashMap<String, Declaration> classMemDeclMap){
	//		if(topLevelMap.size() == 0){
	//			topLevelMap = classMemDeclMap;
	//		}
	//		else{
	//			System.out.println("Not ready for load a class");
	//			System.exit(65536);
	//		}
	//		return 1;
	//	}

	public Object clone() { //need to copy
		try{
			return super.clone();
		}
		catch(Exception e){ return null; }
	}

}
