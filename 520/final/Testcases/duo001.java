/**
 * 1 + 2 + ... + 100 = 5050
 * 2 + 4 + ... + 200 = 10100
 */

class MainClass {
    public static void main (String [] args) {
    	int sum_1 = 0, sum_2 = 0;
        for (int i = 1,j = 2; i<=100; i = i + 1, j = j + 2){
        	sum_1 = sum_1 + i;
        	sum_2 = sum_2 + j;
        }
        System.out.println(sum_1);
        System.out.println(sum_2);
    }
}

