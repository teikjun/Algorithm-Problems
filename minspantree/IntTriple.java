
class IntTriple implements Comparable<IntTriple> {
    int first; // u
    int second; // v
    int third; // w

    public IntTriple(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /* sort the set of edges E in non-decreasing weight 
       and if ties, by increasing smaller vertex number of the edge, 
       and if still ties, by increasing larger vertex number of the edge. */
    public int compareTo(IntTriple other) {
        //System.out.println("self: " + this + " other: " + other);
        if (this.third() != other.third()) {
            return this.third() - other.third();
        } else if (this.first() != other.first()) {
            return this.first() - other.first();
        } else {
            return this.second() - other.second();
        }
    }

    public int first() {
        return this.first;
    }

    public int second() { 
        return this.second;
    }

    public int third() {
        return this.third;
    }

    public String toString() {
        return first() + " " + second();
    }
} 