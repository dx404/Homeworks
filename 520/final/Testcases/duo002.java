class shortCircuit{
    public static void main(String[] args){
        boolean a = false|| false || false || true;
        if (a){
            System.out.println(1);
        }
    }

}