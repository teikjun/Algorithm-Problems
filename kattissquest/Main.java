import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.Collections;

class Main {
    /*
    Pseudocode - Using balanced BST (TreeMap) and max heap (PQ):
    1. In the TreeMap, key contains consumedEnergy, 
       value is a PriorityQueue containing gold of quests with that consumedEnergy
    A. If command is "add": 
        if get(consumedEnergy) == null,                                     O(log N) search +
            put key: consumedEnergy, value: PQ containing the element       O(log N) insertion into TreeMap
        if get(consumedEnergy) != null,                                     O(log N) search +
            there is a key with equal consumedEnergy 
            get(consumedEnergy) returns a PQ. add the gold value to the PQ  O(log N) insertion into PQ
    B. If command is "query"
        1. find floorKey = greatest value <= totalEnergy,                       O(log N) floor 
            i.e treeMap.floorKey(totalEnergy)    
        if floorKey != null:
        1. get(floorKey), this returns a PQ                                     O(log N) search
        2. poll first value of PQ and add gold to totalGoldEarned               O(1)                           
        3. peek first element from PQ,                                          O(1)
            if null, remove the consumedEnergy key value pair                   O(log N) removal 
        if floorKey == null:
            return totalGoldEarned
    Invariants:
    1. Each node in TreeMap contains a key of consumedEnergy, 
        and value of max PQ that contains >0 gold entries with the same consumedEnergy
    */ 

    static TreeMap<Integer, PriorityQueue<Integer>> energyTM = new TreeMap<>();

    public static void main(String[] args) {
        //scanning input
        Kattio io = new Kattio(System.in, System.out);
        int numOfCommands = io.getInt(); // not used
        while (io.hasMoreTokens()) {
            String command = io.getWord();
            if (command.equals("add")) {
                int consumedEnergy = io.getInt();
                int gold = io.getInt();
                // add to energyTM
                Main.add(consumedEnergy, gold);
            } else {
                assert(command.equals("query"));
                int totalEnergy = io.getInt();
                // query energyTM
                Main.query(totalEnergy);
            }
        }    
    }

    public static void add(int consumedEnergy, int gold) {
        PriorityQueue<Integer> valuePQ = energyTM.get(consumedEnergy);
        if (valuePQ == null) {
            // no such consumedEnergy key exists
            PriorityQueue<Integer> newPQ = new PriorityQueue<>(Collections.reverseOrder());
            newPQ.add(gold);
            energyTM.put(consumedEnergy, newPQ);
        } else {
            // consumedEnergy key already exists. valuePQ is a max PriorityQueue 
            valuePQ.add(gold);
        }
    }

    public static void query(int totalEnergy) { 
        // get floorKey
        long totalGoldEarned = 0;
        int remainingEnergy = totalEnergy;
        while (true) { 
            Integer floorKey = energyTM.floorKey(remainingEnergy); 
            //System.out.println(floorKey);
            if (floorKey != null) {
                //floorKey is consumedEnergy
                PriorityQueue<Integer> pq = energyTM.get(floorKey);
                int goldEarned = pq.poll();
                totalGoldEarned += (long) goldEarned; 
                remainingEnergy -= floorKey;
                Integer nextGoldEarned = pq.peek();
                if (nextGoldEarned == null) {
                    energyTM.remove(floorKey);
                }
            } else {
                assert(floorKey == null);
                System.out.println(totalGoldEarned);
                break;
            }
        }
    }


}