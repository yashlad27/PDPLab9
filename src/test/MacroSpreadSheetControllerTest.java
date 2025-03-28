import org.junit.Test;

import java.io.StringReader;

import spreadsheet.MacroSpreadSheetController;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the MacroSpreadSheetController class.
 */
public class MacroSpreadSheetControllerTest {

  @Test
  public void testBulkAssignValue() {
    // Create input
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value A 1 B 3 42.5\n"); // Set cells A1:B3 to 42.5
    input.append("print-value A 1\n");                // Check A1
    input.append("print-value B 3\n");                // Check B3
    input.append("print-value C 1\n");                // Check C1 (outside range)
    input.append("quit\n");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro model = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(model, in, out);

    controller.control();

    // Verify output contains expected values
    String output = out.toString();
    assertTrue(output.contains("Setting cells from (0,0) to (1,2) to 42.5"));
    assertTrue(output.contains("Value: 42.5")); // A1 and B3 should both be 42.5
    assertTrue(output.contains("Value: 0.0")); // C1 should be 0.0 (outside range)
  }

  @Test
  public void testInvalidCommand() {
    // Create input with invalid command
    StringBuilder input = new StringBuilder();
    input.append("invalid-command\n");
    input.append("quit\n");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro model = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(model, in, out);

    controller.control();

    // Verify error message
    String output = out.toString();
    assertTrue(output.contains("Undefined instruction: invalid-command"));
  }

  @Test
  public void testInvalidParameters() {
    // Create input with invalid parameters
    StringBuilder input = new StringBuilder();
    input.append("bulk-assign-value A -1 B 2 10\n"); // Negative column number
    input.append("quit\n");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro model = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(model, in, out);

    controller.control();

    // Verify error message
    String output = out.toString();
    assertTrue(output.contains("Error:"));
  }
}