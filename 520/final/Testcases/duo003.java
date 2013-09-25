//null value

class MainClass {
    public static void main (String [] args) {
    	MainClass mc = new MainClass();
    	mc = null;
    	if (mc == null){
    		System.out.println(1);
    	}
    	else{
    		System.out.println(0);
    	}
    }
}

