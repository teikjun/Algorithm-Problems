import java.util.Comparator;

class WeightComparator implements Comparator<IntTriple> {
    public int compare(IntTriple a, IntTriple b) {
        if (a.third() != b.third()) {
            return a.third() - b.third();
        } else if (a.first() != b.first()) {
            return a.first() - b.first();
        } else {
            return a.second() - b.second();
        }
    }
} 