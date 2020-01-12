import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UnweightedGraphTester {

    private String testCaseDirectory;

    public static void main(String[] args) {
        // 0: getNumConnectedComponents()
        // 1: hasCycle()
        // 2: isBipartiteGraph()
        // 3: getToposortOrder()
        //
        // For example, to test only "hasCycle()", invoke executeTester("NYNN");

        if (args != null && args.length >= 1 && args[0].length() >= 4) {
            executeTester(args[0]);
        } else {
            // Test all methods
            executeTester("YYYY");
        }
    }

    public static void executeTester(String testsToRunArgument) {
        // Set the directory of the test cases as "./tests/"
        UnweightedGraphTester tester = new UnweightedGraphTester("tests");
        char[] testsToRun = testsToRunArgument.toCharArray();

        if (testsToRun[0] == 'Y') {
            tester.testComponents(new String[] {
                "component1",
                "component2",
                "component3"
            });
        }

        if (testsToRun[1] == 'Y') {
        tester.testHasCycle(new String[] {
                "hasCycle1",
                "hasCycle2"
            });
        }

        if (testsToRun[2] == 'Y') {
        tester.testIsBipartite(new String[] {
                "bipartite1",
                "bipartite2",
                "bipartite3"
            });
        }

        if (testsToRun[3] == 'Y') {
            tester.testToposort(new String[] {
                "toposort"
            });
        }
    }

    public UnweightedGraphTester() {
        this.testCaseDirectory = ".";
    }

    public UnweightedGraphTester(String testCaseDirectory) {
        this.testCaseDirectory = testCaseDirectory;
    }

    public void testComponents(String[] testFilenames) {
        Function<String, Supplier<Integer>> methodToTestGenerator = filename -> () -> {
            UnweightedGraph graph = loadGraph(filename, false);
            return graph.getNumConnectedComponents();
        };

        Function<String, Predicate<Integer>> verifierGenerator = filename -> output -> compareOutputWithAnswer(filename);

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("            Testing 'getNumConnectedComponents()'");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        runTests(testFilenames, methodToTestGenerator, verifierGenerator);
    }

    public void testHasCycle(String[] testFilenames) {
        Function<String, Supplier<Boolean>> methodToTestGenerator = filename -> () -> {
            UnweightedGraph graph = loadGraph(filename, true);
            return graph.hasCycle();
        };

        Function<String, Predicate<Boolean>> verifierGenerator = filename -> output -> compareOutputWithAnswer(filename);

        
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("                     Testing 'hasCycle()'");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        runTests(testFilenames, methodToTestGenerator, verifierGenerator);
    }

    public void testIsBipartite(String[] testFilenames) {
        Function<String, Supplier<Boolean>> methodToTestGenerator = filename -> () -> {
            UnweightedGraph graph = loadGraph(filename, false);
            return graph.isBipartiteGraph();
        };

        Function<String, Predicate<Boolean>> verifierGenerator = filename -> output -> compareOutputWithAnswer(filename);

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("                 Testing 'isBipartiteGraph()'");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        runTests(testFilenames, methodToTestGenerator, verifierGenerator);
    }

    public void testToposort(String[] testFilenames) {
        Function<String, Supplier<List<Integer>>> methodToTestGenerator = filename -> () -> {
            UnweightedGraph graph = loadGraph(filename, true);
            return graph.getToposortOrder();
        };

        Function<String, Predicate<List<Integer>>> verifierGenerator = filename -> output -> verifyTopologicalOrder(filename, output);

        
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("                 Testing 'getToposortOrder()'");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        runTests(testFilenames, methodToTestGenerator, verifierGenerator);

    }

    public UnweightedGraph loadGraph(String filenameWithoutExtension, boolean isDirected) {
        String filename = getInputFilename(filenameWithoutExtension);
        try (BufferedReader inputReader = new BufferedReader(new FileReader(filename))) {
            String[] firstLineTokens = inputReader.readLine().split(" ");
            int numVertices = Integer.parseInt(firstLineTokens[0]);
            int numEdges = Integer.parseInt(firstLineTokens[1]);

            UnweightedGraph output = new UnweightedGraph(numVertices, isDirected);

            for (int i = 0; i < numEdges; ++i) {
                String[] edgeTokens = inputReader.readLine().split(" ");
                int u = Integer.parseInt(edgeTokens[0]);
                int v = Integer.parseInt(edgeTokens[1]);

                output.addEdge(u, v);
            }

            return output;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public <T> void runTests(String[] testFilenames, Function<String, Supplier<T>> methodToTestGenerator, Function<String, Predicate<T>> verifierGenerator) {
        HashMap<TestOutcomeType, Integer> outcomeFrequency = new HashMap<>();
        for (TestOutcomeType outcome : TestOutcomeType.values()) {
            outcomeFrequency.put(outcome, 0);
        }

        Duration totalExecutionTime = Duration.ZERO;
        for (int i = 0; i < testFilenames.length; ++i) {
            String filename = testFilenames[i];

            System.out.print("================== ");
            System.out.print("Test case " + (i + 1) + ": " + filename);
            System.out.println(" ==================");

            Supplier<T> methodToTest = methodToTestGenerator.apply(filename);
            Predicate<T> verifier = verifierGenerator.apply(filename);

            TestOutcome outcome = performTest(filename, methodToTest, verifier);
            double executionTime = (outcome.getExecutionTime().toNanos() / 1e9);
            TestOutcomeType outcomeType = outcome.getTestOutcomeType();

            System.out.println("Execution time: " + executionTime + " seconds");
            System.out.println("Outcome: " + outcomeType);
            System.out.println();
            outcomeFrequency.put(outcome.getTestOutcomeType(), outcomeFrequency.get(outcomeType) + 1);
            totalExecutionTime = totalExecutionTime.plus(outcome.getExecutionTime());
        }

        System.out.println("===========================================================");
        System.out.println();
        System.out.println("All Tests complete.");
        System.out.println("Total execution time: " + (totalExecutionTime.toNanos() / 1e9) + " seconds.");
        for (HashMap.Entry<TestOutcomeType, Integer> entry : outcomeFrequency.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
    }

    private <T> RunOutcome<T> runMethod(String filenameWithoutExtension, Supplier<T> methodToTest) throws IOException {
        // Prepare the input and output files
        String inputFilename = getInputFilename(filenameWithoutExtension);
        String outputFilename = getOutputFilename(filenameWithoutExtension);

        System.out.println("Testing with " + inputFilename + "...");

        File outputFile = new File(outputFilename);
        outputFile.createNewFile();

        // Prepare to obtain the output from the method.
        T output = null;

        Instant startTime = Instant.now();
        boolean isTerminatedSuccessfully = true;
        Exception caughtException = null;

        try {
            output = methodToTest.get();
        } catch (Exception e) {
            isTerminatedSuccessfully = false;
            caughtException = e;
        }

        // Time!
        Instant endTime = Instant.now();
        Duration executionTime = Duration.between(startTime, endTime);
        System.out.flush();

        // Write the output to the output file
        try (PrintWriter outputWriter = new PrintWriter(outputFilename)) {
            outputWriter.println(output);
        }

        if (isTerminatedSuccessfully) {
            return new RunOutcome<>(executionTime, output);
        } else {
            return RunOutcome.constructFailedRunOutcome(executionTime, caughtException);
        }
    }

    private String getInputFilename(String filenameWithoutExtension) {
        return testCaseDirectory + File.separator + filenameWithoutExtension + ".in";
    }

    private boolean isInputFilePresent(String filenameWithoutExtension) {
        return new File(getInputFilename(filenameWithoutExtension)).exists();
    }

    private String getOutputFilename(String filenameWithoutExtension) {
        return testCaseDirectory + File.separator + filenameWithoutExtension + ".out";
    }

    private String getAnswerFilename(String filenameWithoutExtension) {
        return testCaseDirectory + File.separator + filenameWithoutExtension + ".ans";
    }

    private boolean isAnswerFilePresent(String filenameWithoutExtension) {
        return new File(getAnswerFilename(filenameWithoutExtension)).exists();
    }

    private boolean compareOutputWithAnswer(String filenameWithoutExtension) {
        String outputFilename = getOutputFilename(filenameWithoutExtension);
        String answerFilename = getAnswerFilename(filenameWithoutExtension);

        if (!isAnswerFilePresent(filenameWithoutExtension)) {
            throw new RuntimeException("The answer file (\"" + answerFilename + "\") does not exist.");
        }

        try {
            String output = new String(Files.readAllBytes(Paths.get(outputFilename))).trim();
            String answer = new String(Files.readAllBytes(Paths.get(answerFilename))).trim();

            if (output.equals(answer)) {
                return true;
            } else {
                String abbreviatedOutput = output;
                if (output.length() > 20) {
                    abbreviatedOutput = output.substring(0, 20) + "...";
                }

                System.out.println("Actual output: \"" + abbreviatedOutput + "\"");
                System.out.println("Expected output: \"" + answer + "\"");

                return false;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean verifyTopologicalOrder(String filenameWithoutExtension, List<Integer> topologicalOrder) {
        if (topologicalOrder == null) {
            System.err.println("...What am I looking at? Nothing? (The returned list is null)");
            return false;
        }

        // Obtain information of the graph.
        String filename = getInputFilename(filenameWithoutExtension);
        try (BufferedReader inputReader = new BufferedReader(new FileReader(filename))) {
            String[] firstLineTokens = inputReader.readLine().split(" ");
            int numVertices = Integer.parseInt(firstLineTokens[0]);
            int numEdges = Integer.parseInt(firstLineTokens[1]);

            int[] indexInTopoOrder = new int[numVertices];
            for (int i = 0; i < numVertices; ++i) {
                indexInTopoOrder[i] = -1;
            }

            // Step 1: Ensure all the vertices are in the topological order.
            if (topologicalOrder.size() != numVertices) {
                System.err.println("The topological order is of the wrong size.");
                return false;
            }

            {
                int index = 0;
                for (int v : topologicalOrder) {
                    if (v < 0 || v >= numVertices) {
                        System.err.println("A wild vertex '" + v + "' has appeared!");
                        return false;
                    } else if (indexInTopoOrder[v] != -1) {
                        System.err.println("Vertex '" + v + "' appeared (at least) twice.");
                        return false;
                    }

                    indexInTopoOrder[v] = index;
                    ++index;
                }
            }

            // Step 2: Ensure no edge violates the topological order.
            for (int i = 0; i < numEdges; ++i) {
                String[] edgeTokens = inputReader.readLine().split(" ");
                int u = Integer.parseInt(edgeTokens[0]);
                int v = Integer.parseInt(edgeTokens[1]);

                if (indexInTopoOrder[u] > indexInTopoOrder[v]) {
                    System.err.println("The edge (" + u + "," + v + ") violates the topological order.");
                    return false;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return true;
    }

    private <T> TestOutcome performTest(String filenameWithoutExtension, Supplier<T> methodToTest, Predicate<T> verifier) {
        // Step 1: Ensure that the input file is present
        if (!isInputFilePresent(filenameWithoutExtension)) {
            String inputFilename = getInputFilename(filenameWithoutExtension);

            // Step 1.1: If the input file is not present, then the outcome is
            // "UNKNOWN_OUTCOME".
            System.err.println("The input file (\"" + inputFilename + "\") does not exist.");
            return TestOutcome.constructEmptyOutcome();
        }

        // Step 2: Run the test!
        RunOutcome<T> runOutcome;
        try {
            runOutcome = runMethod(filenameWithoutExtension, methodToTest);
        } catch (IOException e) {
            System.err.println("An IOException has occurred.");
            e.printStackTrace();

            return TestOutcome.constructEmptyOutcome();
        }

        if (!runOutcome.isRunSuccessfully()) {
            // Step 2.1: If an exception occurred while running, then the outcome is
            // "RUNTIME_ERROR".
            System.err.println("An exception was thrown while running.");
            runOutcome.getCaughtException().printStackTrace();

            return new TestOutcome<>(TestOutcomeType.RUNTIME_ERROR, runOutcome);
        }

        // Step 3: Check if the output is correct.
        try {
            if (verifier.test(runOutcome.getOutput())) {
                return new TestOutcome<>(TestOutcomeType.ACCEPTED, runOutcome);
            } else {
                return new TestOutcome<>(TestOutcomeType.WRONG_ANSWER, runOutcome);
            }
        } catch (Exception e) {
            System.err.println("An Exception occurred while verifying the answer.");
            e.printStackTrace();

            return TestOutcome.constructEmptyOutcome();
        }
    }

    private enum TestOutcomeType {
        ACCEPTED, WRONG_ANSWER, RUNTIME_ERROR, UNKNOWN_OUTCOME
    }

    private static class RunOutcome<T> {
        private Duration executionTime;
        private boolean isRunSuccessfully;
        private Exception caughtException;
        private T output;

        public RunOutcome(Duration executionTime, T output) {
            this(executionTime, true, null, output);
        }

        private RunOutcome(Duration executionTime, boolean isRunSuccessfully, Exception caughtException, T output) {
            this.executionTime = executionTime;
            this.isRunSuccessfully = isRunSuccessfully;
            this.caughtException = caughtException;
            this.output = output;
        }

        public static <T> RunOutcome<T> constructFailedRunOutcome(Duration executionTime, Exception caughtException) {
            return new RunOutcome<>(executionTime, false, caughtException, null);
        }

        public Duration getExecutionTime() {
            return executionTime;
        }

        public boolean isRunSuccessfully() {
            return isRunSuccessfully;
        }

        public Exception getCaughtException() {
            return caughtException;
        }

        public T getOutput() {
            return output;
        }
    }

    private static class TestOutcome<T> {
        private TestOutcomeType testOutcomeType;
        private RunOutcome<T> runOutcome;

        public TestOutcome(TestOutcomeType testOutcomeType, RunOutcome<T> runOutcome) {
            this.testOutcomeType = testOutcomeType;
            this.runOutcome = runOutcome;
        }

        public static <T> TestOutcome<T> constructEmptyOutcome() {
            return new TestOutcome<>(TestOutcomeType.UNKNOWN_OUTCOME, new RunOutcome<T>(Duration.ZERO, null));
        }

        public TestOutcomeType getTestOutcomeType() {
            return testOutcomeType;
        }

        public Duration getExecutionTime() {
            return runOutcome.getExecutionTime();
        }

        public RunOutcome getRunOutcome() {
            return runOutcome;
        }
    }
}