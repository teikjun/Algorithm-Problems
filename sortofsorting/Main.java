import java.util.Arrays; 

class Main { 
    public static void main(String[] args) { 
        //scan input into an array
        Kattio io = new Kattio(System.in, System.out); 
        int num; 
        while ((num = io.getInt()) != 0) { 
            String[] arr = new String[num]; 

            for (int i = 0; i < num; i++) {
                arr[i] = io.getWord();
            }
            //stable sort using first 2 letters of name
            Arrays.sort(arr, new NameComparator()); 
            for (String s : arr) {
                System.out.println(s);
            }
            System.out.println();
        }
        io.close();
    }
}
