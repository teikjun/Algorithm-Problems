public class Main {
    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int elementCount = io.getInt();
        int operationCount = io.getInt();
        UnionFindDisjointSet runner = new UnionFindDisjointSet(elementCount);
        for (int i = 0; i < operationCount; i++) {
            String operation = io.getWord();
            int firstOperand = io.getInt();
            int secondOperand = io.getInt();
            if (operation.equals("?")) {
                boolean isSame = runner.isSameSet(firstOperand, secondOperand);
                if (isSame) {
                    System.out.println("yes");
                } else {
                    System.out.println("no");
                }
            } else {
                assert(operation.equals("="));
                runner.union(firstOperand, secondOperand);
            }
        }
        io.close();
    }
}