/**
 * COMP 520
 * Identification
 */
class pass08 { 	
    public static void main(String[] args) {
        A08 a08 = new A08();
        pass08 p08 = new pass08();
        p08.a = a08;
        a08.p = p08;
        int y = p08.a.p.a.x;
    } 
    
    public A08 a;
}

class A08 { 	
    public pass08 p;
    public int x;
}
