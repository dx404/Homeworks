/**
 * COMP 520
 * Identification
 */
class pass07 { 	
    public static void main(String[] args) {
        pass07 p07 = new pass07();
        p07.next = p07;
        p07.next.next.x = 3;
    } 
    
    public pass07 next;
    private int x;
}
