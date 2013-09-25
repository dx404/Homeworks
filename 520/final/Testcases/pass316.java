/**
 * COMP 520
 * Type Checking
 */
class pass16 { 	
	public static void main(String[] args) {
		pass16 a = new pass16();
		boolean c = a.b() && a.p() == 5;			
	}
	
	int p() {return 5;}
	
	boolean b() {return true == false;}
}

