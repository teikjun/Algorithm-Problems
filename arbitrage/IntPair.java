class Pair implements Comparable<Pair> {
    int first;
    double second;

    public Pair(int first, double second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(Pair o) {
        if (!(this.first() == o.first())) {
            return this.first() - o.first();
        } else {
            return (int) (this.second() - o.second());
        }
    }
    
    public int first() {
        return first;
    }
    public double second() {
        return second;
    }
}