/**
 * CS2040S Problem Set 3 - Speed Demon
 */ 

/*
A. Modify HashMap such that anagrams have the same HashCode 
B. Initialize a HashMap with HashCode as key, ArrayList of arrays[size_128] as value
C. For each line in file:
    1. Hash the entry, use the hash as key. Also, obtain a frequency array representation of the String
    2. Search(key). If it exists, for each freqArray in ArrayList, 
                    check if the new freqArray is equal for the 1...128 elements
                            If yes, freqArray[0] += 1, then count += freqArray[0], 
                            If none match, append the freqArray to the ArrayList
                    Otherwise, put(key, ArrayList(freqArray[size 128]))
D. Finally, return count
*/

import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class MySpeedDemon {
    // Your code
    static int[] currFreqArray;

    public static int speedMatch(String dbfilename) {
        // Your code
        HashMap<Integer, ArrayList<int[]>> hm = new HashMap<>();
        int hashCode = 0; 
        int count = 0;

        try {
            // Open file
            BufferedReader bufferedDataFile = new BufferedReader(new FileReader(dbfilename));

            // Read numOfEntries
            String line = bufferedDataFile.readLine();
            int numOfEntries = Integer.parseInt(line);
            // initialize outside loop for performance
            MyString entry = null;
            ArrayList<int[]> valueList = null;
            int j = 0;
            int[] storedFreqArray = null;
            int len;
            ArrayList<int[]> newValueList;

            // Read and process each entry
            for (int i = 0; i < numOfEntries; i++) {       
                entry = new MyString(bufferedDataFile.readLine());
                //hash entry, also set currFreqArray 
                hashCode = entry.hashCode();

                valueList = hm.get(hashCode);
                if (valueList != null) {
                    len = valueList.size();

                    // for each storedFreqArray in valueList
                    for (j = 0; j < len; j++) {
                        storedFreqArray = valueList.get(j);       
                        // check if it is equal to currentFreqList for 1...128 element, 0th element is the count
                        if (isFreqEqual(storedFreqArray, currFreqArray)) {
                            storedFreqArray[0]++;
                            count += storedFreqArray[0];
                            break;
                        }
                    }
                    // if none of the storedFreqArray are equal, then append the currFreqArray
                    if (j == len) {
                        valueList.add(currFreqArray);
                    }
                } else {
                    // put(hashCode, valueList containing the currFreqArray)
                    newValueList = new ArrayList<>();
                    newValueList.add(currFreqArray); 
                    hm.put(hashCode, newValueList);
                }
            } 
            // close the BufferedReader
            bufferedDataFile.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return count;
    }
    // returns true if the 1...128 elements are equal, false otherwise
    public static boolean isFreqEqual(int[] storedFreqArray, int[] currentFreqArray) {
        for (int i = 1; i < 129; i++) {
            if (storedFreqArray[i] != currentFreqArray[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String args[]) {
        if (args != null && args.length > 0) {
            int result = MySpeedDemon.speedMatch(args[0]);
            System.out.println(result);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
