import java.util.List; 
import java.util.LinkedList;
import java.util.Arrays;

class Main {
    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int fullCount = io.getInt(); 
        int remainCount = io.getInt();
        int missingCount = fullCount - remainCount;

        //create an array of size fullCount + 1 containing 0s, the 0 index is not used
        int[] freqArr = new int[fullCount + 1]; 
        int[] remainArr = new int[remainCount];
        int[] missingArr = new int[missingCount];
        List<Integer> fullList = new LinkedList<Integer>();
        
        //fill remainArr with input ints 
        for (int i = 0; i < remainCount; i++) {
            remainArr[i] = io.getInt();
        }

        //recreate the original sequence in fullList
        /*
        1. increment the elements in the freqArr corresponding to the numbers in remainArr O(n)
        2. iterate through freqArr, storing the indexes of the 0 elements in a missingArr O(n)
        3. create a new LinkedList fullList, compare the first values in remainArr and missingArr,
            add the larger of the two. Repeat until arrays are empty. (Similar to merge)  O(n)
        time complexity: O(n) 
        */

        //iterate through the remainCount elements of remainArr, increment the freqArr elements 
        //at index corresponding to the element's value in remainArr
        for (int j = 0; j < remainCount; j++) {
            //value is either 0 or 1
            int value = remainArr[j];
            freqArr[value] = freqArr[value] + 1; 
        }

        //iterate through the fullCount elements of freqArr,
        //if the element is 1, add the index of that element to missingArr
        for (int k = 1, m = 0; k < fullCount + 1; k++) {
            if (freqArr[k] == 0) {
                missingArr[m] = k;
                m++;
            }
        }  
        
        //merge missingArr and remainArr into fullList 
        //compare first elements of remainArr and missingArr, add the smaller of the two into fullList
        int m = 0;
        int r = 0;
        while (m < missingCount && r < remainCount) {
            int missingElem = missingArr[m];
            int remainElem = remainArr[r];
            if (missingElem < remainElem) {
                fullList.add(missingElem);
                m++;
            } else {
                fullList.add(remainElem);
                r++;
            }
        }
        if (m == missingCount) {
            for (; r < remainCount; r++) {
                int remainElem = remainArr[r];
                fullList.add(remainElem);
            }
        } else {
            for (; m < missingCount; m++) {
                int missingElem = missingArr[m]; 
                fullList.add(missingElem);
            }
        }

        for (int i : fullList) {
            System.out.println(i);
        }

    }
}

