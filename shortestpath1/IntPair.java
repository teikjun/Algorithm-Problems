class IntPair implements Comparable<IntPair> {
    int first;
    int second;

    public IntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(IntPair o) {
        if (!(this.first() == o.first())) {
            return this.first() - o.first();
        } else {
            return this.second() - o.second();
        }
    }
    
    public int first() {
        return first;
    }
    public int second() {
        return second;
    }
}