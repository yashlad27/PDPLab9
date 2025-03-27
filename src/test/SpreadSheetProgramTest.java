import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;

/**
 * Test class for SpreadSheetProgram.
 * Tests the main program functionality by simulating user input and capturing output.
 */
public class SpreadSheetProgramTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testBasicOperations() {
        String input = "assign-value A 1 42.5\n" +
                "print-value A 1\n" +
                "quit\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        spreadsheet.SpreadSheetProgram.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Welcome to the spreadsheet program!"));
        assertTrue(output.contains("Value: 42.5"));
        assertTrue(output.contains("Thank you for using this program!"));
    }

    @Test
    public void testBulkAssignOperation() {
        String input = "bulk-assign-value A 1 B 2 10.0\n" +
                "print-value A 1\n" +
                "print-value B 2\n" +
                "quit\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        spreadsheet.SpreadSheetProgram.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Setting cells from (0,0) to (1,1) to 10.0"));
        assertTrue(output.contains("Value: 10.0"));
    }

    @Test
    public void testAverageOperation() {
        String input = "assign-value A 1 10.0\n" +
                "assign-value A 2 20.0\n" +
                "average A 1 A 2 B 1\n" +
                "print-value B 1\n" +
                "quit\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        spreadsheet.SpreadSheetProgram.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Value: 15.0"));
    }

    @Test
    public void testRangeAssignOperation() {
        String input = "range-assign A 1 A 3 1.0 1.0\n" +
                "print-value A 1\n" +
                "print-value A 2\n" +
                "print-value A 3\n" +
                "quit\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        spreadsheet.SpreadSheetProgram.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Value: 1.0"));
        assertTrue(output.contains("Value: 2.0"));
        assertTrue(output.contains("Value: 3.0"));
    }

    @Test
    public void testInvalidCommand() {
        String input = "invalid-command\n" +
                "quit\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        spreadsheet.SpreadSheetProgram.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Undefined instruction: invalid-command"));
    }

    @Test
    public void testMenuCommand() {
        String input = "menu\n" +
                "quit\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        spreadsheet.SpreadSheetProgram.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Supported user instructions are:"));
        assertTrue(output.contains("bulk-assign-value"));
        assertTrue(output.contains("assign-value"));
        assertTrue(output.contains("print-value"));
    }

    @Test
    public void testInvalidCellReference() {
        String input = "assign-value 1A 1 42.5\n" +
                "quit\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        spreadsheet.SpreadSheetProgram.main(new String[]{});

        String output = outContent.toString();
        assertTrue(output.contains("Error: Invalid row"));
    }
} 