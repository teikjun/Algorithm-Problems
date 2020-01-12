//remove this
import java.util.Arrays;

class Main { 
    public static void main(String[] args) { 
        //scan input into an array
        Kattio io = new Kattio(System.in, System.out);
        int totalNum = io.getInt(); 
        int remainingNum = io.getInt();
        int missingNum = totalNum - remainingNum;
        
        int[] remainingArr = new int[totalNum];
        //create an array of size totalNum + 1 containing 0s, the 0 index is not used
        int[] freqArr = new int[totalNum + 1]; 
        int[] missingArr = new int[missingNum];

        //fill remainingArr partially with remainingNum of ints
        for (int i = 0; i < remainingNum; i++) {
            remainingArr[i] = io.getInt();
        }
        
        //recreate the original sequence
        /*
        1. increment the elements in the freqArr corresponding to the numbers in remainingArr
        2. iterate through freqArr, storing the indexes of the 0 elements in a missingArr
        3. for each element e in the missingArr: 
            iterate through the remainingArr until we access an element that is greater than e.
            if such an element exists, place e before that element
            else place e at the end of remainingArr. 
        
        time complexity: O(n * k) ~ O(n^2) where n = totalNum, k = remainingNum
        3. create a new linkedList completeList, compare the first values in remainingArr and missingArr,
            add the larger of the two. repeat until arrays are empty. similar to merge. 
        time complexity: O(n) 
        */

        //iterate through the remainingNum elements of remainingArr, increment the freqArr elements 
        //at index corresponding to the element's value in remainingArr
        for (int j = 0; j < remainingNum; j++) {
            //value is either 0 or 1
            int value = remainingArr[j];
            //System.out.println(value);
            freqArr[value] = freqArr[value] + 1; 
        
            //System.out.println(Arrays.toString(freqArr));
        }

        //iterate through the totalNum elements of freqArr, if the element is 1, add the index 
        //of that element to missingArr
        for (int k = 1, m = 0; k < totalNum + 1; k++) {
            if (freqArr[k] == 0) {
                //System.out.println(k);
                missingArr[m] = k;
                m++;
            }
        //System.out.println(Arrays.toString(missingArr));
        }
        //System.out.println(Arrays.toString(remainingArr));
        
        // for each element in missingArr, iterate through remainingArr and put it in the right place, 
        // increment remainingNum each time
/*
        for (int p = 0; p < missingNum; p++) {
            int elem = missingArr[p];
            //iterate through remainingArr
            for (int q = 0; q < remainingNum; q++) {
                int other = remainingArr[q];
                //if other element in remainingArr is greater that elem, 
                //shift all remainingArr elements to the right by one index, place elem in that index 
                if (other > elem) {
                    //System.out.println(Arrays.toString(remainingArr));
                    int correctIdx = q;
                    //System.out.println(q);
                    for (int x = totalNum - 1; x > q; x--) {
                        //move all elements right by one
                        remainingArr[x] = remainingArr[x - 1];
                    }
                    remainingArr[correctIdx] = elem;
                    //System.out.println(Arrays.toString(remainingArr));
                    break;
                } else if (q == (remainingNum - 1)) {
                    remainingArr[remainingNum] = elem;
                }
            }
                remainingNum++;
        }
*/                  
        for (int r : remainingArr) {
            System.out.println(r);
        }
    }        
}

            

        
            

