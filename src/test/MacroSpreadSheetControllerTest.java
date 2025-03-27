import org.junit.Test;

import java.io.StringReader;

import spreadsheet.MacroSpreadSheetController;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;

import static org.junit.Assert.assertEquals;

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

    // Create expected output
    StringBuilder expected = new StringBuilder();
    expected.append("Welcome to the spreadsheet program!" + System.lineSeparator());
    expected.append("Supported user instructions are: " + System.lineSeparator());
    expected.append("bulk-assign-value from-row-num from-col-num to-row-num to-col-num value " +
            "(set a range of cells to a value)" + System.lineSeparator());
    expected.append("Supported user instructions are: " + System.lineSeparator());
    expected.append("assign-value row-num col-num value (set a cell to a value)"
            + System.lineSeparator());
    expected.append("print-value row-num col-num (print the value at a given cell)"
            + System.lineSeparator());
    expected.append("menu (Print supported instruction list)" + System.lineSeparator());
    expected.append("q or quit (quit the program) " + System.lineSeparator());

    expected.append("Type instruction: Setting cells from (0,0) to (1,2) to 42.5"
            + System.lineSeparator());
    expected.append("Type instruction: Value: 42.5" + System.lineSeparator());
    expected.append("Type instruction: Value: 42.5" + System.lineSeparator());
    expected.append("Type instruction: Value: 0.0" + System.lineSeparator());
    expected.append("Type instruction: Thank you for using this program!");

    // Set up controller and run test
    Readable in = new StringReader(input.toString());
    StringBuilder out = new StringBuilder();
    SpreadSheet baseSheet = new SparseSpreadSheet();
    SpreadSheetWithMacro sheet = new SpreadSheetWithMacroImpl(baseSheet);
    MacroSpreadSheetController controller = new MacroSpreadSheetController(sheet, in, out);

    controller.control();

    assertEquals(expected.toString(), out.toString());
  }
}