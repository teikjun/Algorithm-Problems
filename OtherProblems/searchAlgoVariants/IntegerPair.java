class IntegerPair implements Comparable<IntegerPair> {
    Integer first;
    Integer second;

    public IntegerPair(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(IntegerPair o) {
        if (!this.getFirst().equals(o.getFirst())) {
            return this.getFirst() - o.getFirst();
        } else {
            return this.getSecond() - o.getSecond();
        }
    }
    
    public Integer getFirst() {
        return first;
    }
    public Integer getSecond() {
        return second;
    }
}