class Main {
    static int[] typeOneArr;
    static int[] typeTwoArr;
    static int typeOneCount;
    static int typeTwoCount;
    public static void main(String[] args) {
        Kattio io = new Kattio(System.in, System.out);
        int totalTime = io.getInt();
        typeOneCount = io.getInt();
        typeOneArr = new int[typeOneCount * 2]; 
        for (int i = 0; i < typeOneCount; i++) {
            typeOneArr[2 * i] = io.getInt(); //Ak
            typeOneArr[2 * i + 1] = io.getInt(); //Bk
        }
        typeTwoCount = io.getInt();
        typeTwoArr = new int[typeTwoCount * 2]; 
        for (int j = 0; j < typeTwoCount; j++) {
            typeTwoArr[2 * j] = io.getInt(); //Ck
            typeTwoArr[2 * j + 1] = io.getInt(); //Dk
        }

        /*
           1. Set low = 0, high = totalTime
           2. mid = (low + high) / 2
           3. Guess that t1 = totalTime/2, t2 = totalTime/2
           4. Count the number of coconuts picked by typeOne
           5. Calculate the time taken t3 to open the coconuts typeTwo
           6. If t3 > t2 
         */
        int high = totalTime; 
        int low = 0;
        while (high >= low) {
            //we guess mid for time taken for t1
            int guessT1 = (high - low) / 2 + low;
            int guessT2 = totalTime - guessT1;
            //check if the guess is low or high
            int timeTaken = 0;
            int typeOneNum = typeOneNuts(guessT1);
            int typeTwoNum = typeTwoNuts(guessT2);
            //based on guess time for t2, calculate number of coconuts opened 
            if (typeTwoNuts(totalTime - guessT1 - 1) <= typeOneNum && typeOneNum <= typeTwoNum) {
                System.out.println(guessT1);
                break;
            }

            if (typeOneNum < typeTwoNum || typeTwoNum == 0) {
                // guess a smaller number for t1
                low = guessT1 + 1;
            } 
            if (typeOneNum > typeTwoNum) {
                // guess a larger number for t1
                high = guessT1 - 1;
            }
        }

    }

    static int typeOneNuts(int guessT1) {
        //based on guess time for t1, calculate number of coconuts picked
        int pickCount = 0;
        for (int i = 0; i < typeOneCount; i++) { 
            int Ak = typeOneArr[2 * i];
            int Bk = typeOneArr[2 * i + 1];
            int temp = guessT1 - Ak;
            if (temp >= 0) {
                int addedPickCount = (temp / Bk) + 1;
                pickCount += addedPickCount; 
            }
        }
        return pickCount; 
    }
    static int typeTwoNuts(int guessT2) {
        int openCount = 0;
        for (int j = 0; j < typeTwoCount; j++) {
            int Ck = typeTwoArr[2 * j];
            int Dk = typeTwoArr[2 * j + 1];
            int temp = guessT2 - Ck;
            if (temp >= 0) {
                int addedOpenCount = (temp) / Dk + 1;
                openCount += addedOpenCount;
            }
        }
        return openCount;
    }
}
