import java.util.Arrays;

class Main {    
    static int pointCount;
    static int houseCount;
    static int[] houseArr;
    static int sol = -1;
    static Kattio io = new Kattio(System.in, System.out);
    
    public static void main(String[] args) {
        int numOfTestCases = io.getInt();
        for (int i = 0; i < numOfTestCases; i++) {
            solve();
        } 
    }      
    static void solve() {
        //scan input
        pointCount = io.getInt(); 
        houseCount = io.getInt();
        // houseArr contains house numbers
        houseArr = new int[houseCount];
        for (int j = 0; j < houseCount; j++) {
            //multiply all house numbers by two. This is because maxDist either ends with .0 or .5 because house numbers are integers 
            houseArr[j] = io.getInt() * 2;
        }
        //output minimum distance to each house
        /*
           1. Set low = 0, high = dist between first and last house
           2. mid = (high - low)/2 + low. we guess that mid is the maximum distance
           3. If possible, low = mid. else high = mid
           4. output low / 2.

           total time complexity O(nlogn)
           sort O(nlogn)
           calculatePointsNeeded O(n)
           binarySearch O(nlogn) 
         */
         Arrays.sort(houseArr);
         //pointCount is a positive integer. This accounts for the case where houseArr.size = 0.
         if (houseArr.length <= pointCount) {
            System.out.println(0.0); 
         } else {
            binarySearchMaxDist();
         }
    }              

    static void binarySearchMaxDist() {    
        int low = 0;
        // maxDist cannot be more than the distance between first and last house
        int high = houseArr[houseArr.length - 1] - houseArr[0]; 
        
        while (high > low) {
            int mid = (high - low) / 2 + low;
            int needed = calculatePointsNeeded(mid);
            if (needed > pointCount) {
                low = mid + 1;
            } else {
                sol = mid;
                high = mid;
            }
        }
        System.out.format("%.1f" + "\n", (double) sol / 2.0);
    }

    static int calculatePointsNeeded(int maxDist) {
        //initially 1 router needed. put router at point furthest away from the first house.
        int needed = 1; 
        int lastRouter = houseArr[0] + maxDist;
        for (int i = 1; i < houseArr.length; i++) {
            if (houseArr[i] - lastRouter > maxDist) {
                needed++;
                lastRouter = houseArr[i] + maxDist;
            }  
        }
        return needed;
    }


}


