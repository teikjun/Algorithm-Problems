public class MyString { 
    String str;
    int[] currFreqArray = new int[129]; 

    public MyString(String str) {
        this.str = str;
    }

    @Override
    public int hashCode() {
        int hashResult = 0; 
        char currentChar = 0;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            currentChar = str.charAt(i);
            // calculate hash
            hashResult += currentChar; 
            // count frequency of each character
            currFreqArray[currentChar]++;
        }
        // sets the currFreqArray in MySpeedDemon
        MySpeedDemon.currFreqArray = currFreqArray;
        return Math.abs(hashResult % 100000007);
    }

}