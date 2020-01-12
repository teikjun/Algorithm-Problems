public class IntTriple implements Comparable<IntTriple> {
    int first;
    int second;
    int third;
    
    IntTriple(int first, int second, int third) {
        this.first = first; 
        this.second = second; 
        this.third = third; 
    }
    // sort by start times
    public int compareTo(IntTriple other) {
        if (this.second() != other.second()) {
            return this.second() - other.second();
        } else if (this.first() != other.first()) {
            return this.first() - other.first();
        } else {
            return this.third() - other.third();
        }
    }
    
    public int first() {
        return first;
    }
    public int second() {
        return second;
    }
    public int third() {
        return third;
    }
}