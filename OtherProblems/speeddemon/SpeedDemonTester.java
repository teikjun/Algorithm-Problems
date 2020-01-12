import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

public class SpeedDemonTester {

    private String testCaseDirectory;

    public SpeedDemonTester() {
        this.testCaseDirectory = ".";
    }

    public static void main(String[] args) {
        // Set the directory of the test cases as "./tests/"
        SpeedDemonTester tester = new SpeedDemonTester("tests");
        tester.runTests(new String[] {
                /**
                 * You can add additional test cases of your own here. Make sure that each test
                 * case comes as pair of .in and .ans files for the input and expected output
                 * respectively. DO NOT INCLUDE THE FILE EXTENSION HERE!
                 */
                "TestDB_1",
		        "TestDB_2",
		        "TestDB_3",
		        "TestDB_4", 
		        "TestDB_5"
	    });
    }

    public SpeedDemonTester(String testCaseDirectory) {
        this.testCaseDirectory = testCaseDirectory;
    }

    public void runTests(String[] testFilenames) {
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

            TestOutcome outcome = testSpeedDemon(filename);
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
    }

    private RunOutcome runSpeedDemon(String filenameWithoutExtension) throws IOException {
        // Save the standard output stream to change later.
        PrintStream standardOutput = System.out;

        // Prepare the input and output files
        String inputFilename = getInputFilename(filenameWithoutExtension);
        String outputFilename = getOutputFilename(filenameWithoutExtension);

        System.out.println("Testing with " + inputFilename + "...");

        File outputFile = new File(outputFilename);
        outputFile.createNewFile();

        // Redirect standard output to the output file
        System.setOut(new PrintStream(outputFilename));
        Instant startTime = Instant.now();
        boolean isTerminatedSuccessfully = true;
        Exception caughtException = null;

        try {
            // Change MySpeedDemon to the class you want to run if required
            MySpeedDemon.main(new String[] { inputFilename });
        } catch (Exception e) {
            isTerminatedSuccessfully = false;
            caughtException = e;
        }

        // Time!
        Instant endTime = Instant.now();
        Duration executionTime = Duration.between(startTime, endTime);
        System.out.flush();

        // Reset standard output.
        System.setOut(standardOutput);

        if (isTerminatedSuccessfully) {
            return new RunOutcome(executionTime);
        } else {
            return new RunOutcome(executionTime, caughtException);
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

    private boolean compareOutputWithAnswer(String filenameWithoutExtension) throws IOException {
        String outputFilename = getOutputFilename(filenameWithoutExtension);
        String answerFilename = getAnswerFilename(filenameWithoutExtension);

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
    }

    private TestOutcome testSpeedDemon(String filenameWithoutExtension) {
        // Step 1: Ensure that the input file is present
        if (!isInputFilePresent(filenameWithoutExtension)) {
            String inputFilename = getInputFilename(filenameWithoutExtension);

            // Step 1.1: If the input file is not present, then the outcome is
            // "UNKNOWN_OUTCOME".
            System.err.println("The input file (\"" + inputFilename + "\") does not exist.");
            return new TestOutcome(TestOutcomeType.UNKNOWN_OUTCOME, new RunOutcome(Duration.ZERO));
        }

        // Step 2: Run the Speed Demon!
        RunOutcome runOutcome;
        try {
            runOutcome = runSpeedDemon(filenameWithoutExtension);
        } catch (IOException e) {
            System.err.println("An IOException has occurred.");
            e.printStackTrace();

            return new TestOutcome(TestOutcomeType.UNKNOWN_OUTCOME, new RunOutcome(Duration.ZERO));
        }

        if (!runOutcome.isRunSuccessfully()) {
            // Step 2.1: If an exception occurred while running, then the outcome is
            // "RUNTIME_ERROR".
            System.err.println("An exception was thrown while running.");
            runOutcome.getCaughtException().printStackTrace();

            return new TestOutcome(TestOutcomeType.RUNTIME_ERROR, runOutcome);
        }

        // Step 3: Ensure the answer file is present.
        if (!isAnswerFilePresent(filenameWithoutExtension)) {
            String answerFilename = getAnswerFilename(filenameWithoutExtension);

            // Step 3.1: If the answer file is not present, then the outcome is
            // "UNKNOWN_OUTCOME";
            System.err.println("The answer file (\"" + answerFilename + "\") does not exist.");
            return new TestOutcome(TestOutcomeType.UNKNOWN_OUTCOME, runOutcome);
        }

        // Step 4: Compare the output with the answer.
        try {
            if (compareOutputWithAnswer(filenameWithoutExtension)) {
                return new TestOutcome(TestOutcomeType.ACCEPTED, runOutcome);
            } else {
                return new TestOutcome(TestOutcomeType.WRONG_ANSWER, runOutcome);
            }
        } catch (IOException e) {
            System.err.println("An IOException occurred while comparing the output and answer files.");
            return new TestOutcome(TestOutcomeType.UNKNOWN_OUTCOME, runOutcome);
        }
    }

    private enum TestOutcomeType {
        ACCEPTED, WRONG_ANSWER, RUNTIME_ERROR, UNKNOWN_OUTCOME
    }

    private class RunOutcome {
        private Duration executionTime;
        private boolean isRunSuccessfully;
        private Exception caughtException;

        public RunOutcome(Duration executionTime) {
            this.executionTime = executionTime;
            this.isRunSuccessfully = true;
            this.caughtException = null;
        }

        public RunOutcome(Duration executionTime, Exception caughtException) {
            this.executionTime = executionTime;
            this.isRunSuccessfully = false;
            this.caughtException = caughtException;
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
    }

    private class TestOutcome {
        private TestOutcomeType testOutcomeType;
        private RunOutcome runOutcome;

        public TestOutcome(TestOutcomeType testOutcomeType, RunOutcome runOutcome) {
            this.testOutcomeType = testOutcomeType;
            this.runOutcome = runOutcome;
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