import java.util.Arrays; 

class Main { 
    public static long swapCount = 0;
    public static void main(String[] args) { 
        Kattio io = new Kattio(System.in, System.out);
        int num = io.getInt();
        int[] arr = new int[num];
        //change this to long later

        for (int i = 0; i < num; i++) {
            arr[i] = io.getInt(); 
        }
        
        /* Pseudocode
        Idea: divide and conquer. (swaps below refer to the legal consecutive swaps)
        countSwaps recursively calls countSwaps on 2 n/2 sized sorted arrays, adding the number of swaps 
        required to each time. base case is when there is one element. add 0 to count.   
        1. Split the array into two halves 
        2. Count the number of swaps required to sort first half, S1. (assume we can sort an array of n/2 size)
        3. Sort first half 
        4. Count the number of swaps required to sort second half, S2.
        5. Sort second half.
        6. Count the number of swaps required to sort two sorted arrays of n/2 size, S3
        7. Output S1 + S2 + S3
        time complexity: O(nlogn)
        */
        // right heavy array
        // e.g 2 - 1 / 2 = 0. arr left 0 to 0 right 1 to 1
        // e.g 3 - 1 / 2 = 1. arr left 0 to 1  right 2 to 2
        // e.g 4 - 1 / 2 = 1. arr left 0 to 1 right 2 to 3
        // length / 2 = c. arr left 0 to c right c + 1 to length - 1    
        int low = 0;
        int high = num - 1;
        countSwaps(arr, low, high);
        System.out.println(swapCount);
    }

    public static void countSwaps(int[] arr, int low, int high) {
        if (high - low > 0) {
            int mid = (high + low) / 2;
            //System.out.println(Arrays.toString(arr) + " low:" + low + " mid:" + mid + " high:" + high);
            countSwaps(arr, low, mid);
            Arrays.sort(arr, low, mid + 1);

            countSwaps(arr, mid + 1, high);
            //System.out.println(" low:" + low + " mid:" + mid + " high:" + high);
            Arrays.sort(arr, mid + 1, high + 1);

            mergeSortedArrays(arr, low, mid, high);
        } else { 
            //System.out.println(Arrays.toString(arr) + " low:" + low + " high:" + high + " - do nothing");
            assert(high - low == 0);     
        }
    }
    
    //merge the sorted arrays and increment the swapCount O(k. (n - k)) 
    public static void mergeSortedArrays(int[] arr, int low, int mid, int high) {
        //subarray left = arr[low...mid], subarray right = arr[mid + 1...high], both sorted
        //for each element in the left subarray, check how many elements are greater than it in the right subarray
        //System.out.println(Arrays.toString(arr) + " low:" + low + " mid:" + mid + " high:" + high + " - merging");
        for (int i = low; i <= mid; i++) {
            int current = arr[i];
            swapCount += binarySearchCount(arr, current, mid + 1, high);
        }

    }

    public static int binarySearchCount(int[] arr, int key, int start, int end) {
        int low = start;
        int high = end;
        while (low <= high) { // alternatively check (low < high) and set high = mid; 
            int mid = (high + low) / 2;
            if (key < arr[mid]) {
                high = mid - 1;
            } else {
                assert (key > arr[mid]);
                low = mid + 1;
            }
            System.out.println("low:" + low + " high:" + high);
        }
        return low - start;
    }

  /*int current = arr[i];
            for (int j = mid + 1; j <= high; j++) {
                int other = arr[j];
                if (current > other) {
                    swapCount++; 
                } 
            }*/
}
