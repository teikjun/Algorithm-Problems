class IntegerPair implements Comparable<IntegerPair> {
    Integer first;
    Integer second;

    public IntegerPair(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(IntegerPair o) {
        if (!this.first().equals(o.first())) {
            return this.first() - o.first();
        } else {
            return this.second() - o.second();
        }
    }
    
    public Integer first() {
        return first;
    }
    public Integer second() {
        return second;
    }
}