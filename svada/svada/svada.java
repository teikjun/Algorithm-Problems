//https://open.kattis.com/problems/svada
//Author: Love Almgren
import java.util.*;

public class svada {

    static int T;

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        T = reader.nextInt();
        int N = reader.nextInt();
        treeApes = new Ape[N];

        for (int i = 0;i<N; i++) {
            treeApes[i] = new Ape(reader.nextInt(),reader.nextInt());
        }

        int M = reader.nextInt();
        groundApes = new Ape[M];

        for (int i = 0;i<M; i++) {
            groundApes[i] = new Ape(reader.nextInt(),reader.nextInt());
        }
        //
        // System.out.println(howManyTreeNutsAtS(13));
        // System.out.println(howManyGroundNutsAtS(0));
        System.out.println(binarySearch());
    }

    static int binarySearch() {
        int lower = 0;
        int higher = T;
        while (lower <= higher) {
            int middle = lower + (higher-lower)/2;
            int tree = howManyTreeNutsAtS(middle);
            int ground = howManyGroundNutsAtS(T-middle);

            if (howManyGroundNutsAtS(T-middle-1) <= tree && tree <= ground) {
                return middle;
            }

            if( tree < ground || ground == 0 ) {
                lower = middle+1;
            }
            if( tree > ground ) {
                higher = middle-1;
            }
        }
        return -1;
    }



    static Ape[] treeApes, groundApes;

    static int howManyTreeNutsAtS(int s) {
        int nuts = 0;
        for (Ape ape : treeApes) {
            if (ape.t1 <= s ) {
                nuts += 1+(int)Math.floor((s-ape.t1)/ape.t2);
            }
        }
        return nuts;
    }
    static int howManyGroundNutsAtS(int s) {
        int nuts = 0;

        for (Ape ape : groundApes) {
            if (ape.t1 <= s ) {
                nuts += 1+(int)Math.floor((s-ape.t1)/ape.t2);
            }
        }
        return nuts;
    }
}
class Ape {
    public int t1, t2;
    Ape(int t1, int t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

}
