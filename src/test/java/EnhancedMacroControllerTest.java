import org.junit.Test;

import java.io.StringReader;

import spreadsheet.MacroSpreadSheetController;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;

/**
 * Tests for the enhanced MacroSpreadSheetController with all macros.
 */
public class EnhancedMacroControllerTest {

  @Test
  public void testRangeAssignCommand() {
    // Create input
    StringBuilder input = new StringBuilder();
    input.append("range-assign A 1 A 5 10 5\n");    // Set cells A1:A5 to 10,15,20,25,30
    input.append("print-value A 1\n");              // Check A1
    input.append("print-value A 3\n");              // Check A3
    input.append("print-value A 5\n");              // Check A5
    input.append("quit\n");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(sheet, in, out);

    controller.control();

    // Check if the output contains the expected values
    String output = out.toString();

    // We don't check the entire output string since menu formatting might change
    // Instead we check that the key values are present in the output
    assertTrue(output.contains("Value: 10.0"));
    assertTrue(output.contains("Value: 20.0"));
    assertTrue(output.contains("Value: 30.0"));
  }

  @Test
  public void testAverageCommand() {
    // Create input to set up some values and then compute their average
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value A 1 B 2 10\n");  // Set cells A1:B2 to 10
    input.append("range-assign C 1 C 2 20 10\n");    // Set cells C1:C2 to 20,30
    input.append("average A 1 C 2 D 1\n");           // Average A1:C2 into D1
    input.append("print-value D 1\n");               // Check D1
    input.append("quit\n");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(sheet, in, out);

    controller.control();

    // Check if the output contains the expected average value
    // (10+10+10+10+20+30)/6 = 15
    String output = out.toString();
    assertTrue(output.contains("Value: 15.0"));
  }

  @Test
  public void testInvalidCommandHandling() {
    // Create input with an invalid command
    StringBuilder input = new StringBuilder();
    input.append("invalid-command\n");  // An invalid command
    input.append("menu\n");             // Display the menu
    input.append("quit\n");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(sheet, in, out);

    controller.control();

    // Check if the output indicates an undefined instruction
    String output = out.toString();
    assertTrue(output.contains("Undefined instruction: invalid-command"));
  }

  @Test
  public void testInvalidParametersHandling() {
    // Create input with invalid parameters
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value A -1 B 2 10\n");  // Negative column
    input.append("quit\n");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(sheet, in, out);

    controller.control();

    // Check if the output indicates an error
    String output = out.toString();
    assertTrue(output.contains("Error:"));
  }

  // Helper method to check if a string contains the given substring
  private void assertTrue(boolean condition) {
    if (!condition) {
      throw new AssertionError("Assertion failed");
    }
  }
}