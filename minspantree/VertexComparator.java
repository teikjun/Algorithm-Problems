import java.util.Comparator;

class VertexComparator implements Comparator<IntTriple> {
    public int compare(IntTriple a, IntTriple b) {
        if (a.first() != b.first()) {
            return a.first() - b.first();
        } else  {
            return a.second() - b.second();
        }
    }
} 