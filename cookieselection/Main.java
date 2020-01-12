import java.util.PriorityQueue;
import java.util.Collections;

class Main {
    /*     
    Pseudo code: (Using Heap data structure, PQ ADT)
    1. Store the smaller half of the cookies in leftMaxQueue and larger half of the cookies in a rightMinQueue
    2. When # is encountered, poll an element e from the rightMinQueue. Rebalance.
    3. When cookie is added, if smaller than e, put in leftMaxQueue     O(log N) insertion (swim new cookie).
        else, put in rightMaxQueue. Either case, Rebalance.             O(log N) insertion.
    Rebalance: Since the queues are initially balanced, and we rebalance after each addition,
               only one cookie needs to be moved at most
    A. When rightMinQueue has fewer elements than leftMaxQueue,         O(1) to check size
    poll an element from leftMaxQueue and add into rightMinQueue.       O(log N) insertion
    B. When rightMinQueue has >1 elements more than leftMaxQueue, 
    poll an element from rightMinQueue and add into leftMaxQueue.       O(log N) insertion.

    Overall time complexity for N inputs: O(N log N)
    
    Invariances:
    1. Smaller half of the cookies in leftMaxQueue
       Larger half of the cookies in rightMinQueue
    2. rightMinQueue should have 0 or 1 element more than the letMaxQueue.  
    */ 

    static PriorityQueue<Integer> leftMaxQueue = new PriorityQueue<Integer>(Collections.reverseOrder());
    static PriorityQueue<Integer> rightMinQueue = new PriorityQueue<Integer>();
    
    public static void main(String[] args) {
        // scan input
        Kattio io = new Kattio(System.in, System.out);
        while (io.hasMoreTokens()) {
            String input = io.getWord();
            if (input.equals("#")) {
                // poll and print a cookie
                Integer cookieToPrint = rightMinQueue.poll();
                System.out.println(cookieToPrint);
                // rebalance
                Main.rebalance();
            } else {
                // check the diameter and add the cookie
                Integer cookieToAdd = Integer.parseInt(input);
                Integer medianCookie = rightMinQueue.peek();
                if (medianCookie == null || cookieToAdd > medianCookie) {
                    rightMinQueue.add(cookieToAdd);
                } else {
                    leftMaxQueue.add(cookieToAdd);
                }
                // rebalance
                Main.rebalance();
            }
        }
    }

    public static void rebalance() {
        Integer leftSize = leftMaxQueue.size();
        Integer rightSize = rightMinQueue.size();
        if (leftSize > rightSize) {
            Integer cookieToMove = leftMaxQueue.poll();
            rightMinQueue.add(cookieToMove);
        } else if (rightSize > leftSize + 1) {
            Integer cookieToMove = rightMinQueue.poll();
            leftMaxQueue.add(cookieToMove);
        } 
        // else leftSize == rightSize, or leftSize + 1 == rightSize. do nothing
    }
    
}
