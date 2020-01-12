public class Main {
    /*
       1. set low = 1, high = 1000
       2. mid = (low + high) / 2
       3. guess mid
       4. if std.in = lower, high = mid 
       else if std.in = higher, low = mid + 1 
       else exit
       5. repeat step 2 -4
     */
    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int low = 1;
        int high = 1000;
        int mid;
        while (true) {
            mid = (low + high) / 2;
            io.println(mid);
            io.flush();
            String word = io.getWord();
            if (word.equals("lower")) { 
                high = mid;
            } else if (word.equals("higher")) {
                low = mid + 1;
            } else {
                break;
            } 
        } 
    }
}

