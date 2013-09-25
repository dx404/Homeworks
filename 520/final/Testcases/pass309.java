/**
 * COMP 520
 * Identification
 */
class A09 { 	
    public static void main(String[] args) {
        F03 c = new F03();
        c.next.mynext.x = 3;
    }

    private F03 mynext;  // normally no access, but ok if dereferenced within A09
}

class F03 {
    public A09 next;
    public int x;
}
