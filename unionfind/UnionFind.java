import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class UnionFind {
    
    /////////////////////////////////////
    //  Why are you looking at this?   //
    //  This isn't for you to modify.  //
    // You only have to run this file. //
    //       Go away. Get lost.        //
    /////////////////////////////////////

    private static final byte EOF_BYTE = -1;
    private static final int BUFFER_SIZE = (1 << 16);

    private PrintWriter outputWriter;
    private boolean isEofReached;
    private byte[] byteBuffer;
    private int bufferIndex;
    private int bufferLimit;

    public UnionFind() {
        outputWriter = new PrintWriter(new BufferedOutputStream(System.out));
        byteBuffer = new byte[BUFFER_SIZE];
        bufferIndex = 0;
        bufferLimit = 0;
        isEofReached = false;
    }

    public static void main(String[] args) {
        try {
            UnionFind runner = new UnionFind();
            runner.run();
            runner.onProgramEnd();
        } catch (IOException e) {
            System.err.println("Well, this is interesting.");
        }
    }

    public void run() throws IOException {
        int numVertices = getNextInt();
        int numQueries = getNextInt();

        UnionFindDisjointSet solver = new UnionFindDisjointSet(numVertices);

        for (int i = 0; i < numQueries; ++i) {
            byte operation = getNextChar();
            int u = getNextInt();
            int v = getNextInt();

            if (operation == '?') {
                if (solver.isSameSet(u, v)) {
                    outputWriter.println("yes");
                } else {
                    outputWriter.println("no");
                }
            } else {
                solver.union(u, v);
            }
        }
    }

    private byte getNextByte() throws IOException {
        if (isEofReached) {
            return EOF_BYTE;
        }

        if (bufferIndex >= bufferLimit) {
            reloadBuffer();
            if (isEofReached) {
                return EOF_BYTE;
            }
        }

        byte currByte = byteBuffer[bufferIndex];
        ++bufferIndex;

        return currByte;
    }

    private boolean reloadBuffer() throws IOException {
        int numBytesRead = System.in.read(byteBuffer);
        if (numBytesRead == 0) {
            isEofReached = true;
            return false;
        }

        bufferLimit = numBytesRead;
        bufferIndex = 0;
        return true;
    }

    private byte getNextChar() throws IOException {
        while (true) {
            // Repeatedly get the next byte until we encounter a non-whitespace character.
            byte nextByte = getNextByte();
            if (nextByte == EOF_BYTE) {
                return EOF_BYTE;
            } else if (isWhitespace(nextByte)) {
                continue;
            }

            return nextByte;
        }
    }

    private int getNextInt() throws IOException {
        // Start from the first non-whitespacce character.
        byte firstDigit = getNextChar();
        if (firstDigit == EOF_BYTE) {
            return -1;
        }

        int output = (firstDigit - '0');

        // Repeatedly get the next digit until we encounter a whitespace character (or EOF).
        while (true) {
            byte nextDigit = getNextByte();
            if (nextDigit == EOF_BYTE || isWhitespace(nextDigit)) {
                break;
            }

            output = (output * 10) + (nextDigit - '0');
        }

        return output;
    }

    private boolean isWhitespace(byte b) {
        return b == ' ' || b == '\r' || b == '\n';
    }

    public void onProgramEnd() {
        outputWriter.close();
    }
}