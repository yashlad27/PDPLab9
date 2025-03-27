import org.junit.Before;
import org.junit.Test;
import spreadsheet.BulkAssignMacro;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetMacro;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;
import spreadsheet.SparseSpreadSheet;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the BulkAssignMacro class.
 */
public class BulkAssignMacroTest {
  private SpreadSheetWithMacro sheet;

  @Before
  public void setup() {
    SpreadSheet baseSheet = new SparseSpreadSheet();
    sheet = new SpreadSheetWithMacroImpl(baseSheet);
  }

  @Test
  public void testValidBulkAssign() {
    // Create a macro to set a 2x3 range to the value 42
    SpreadSheetMacro macro = new BulkAssignMacro(0, 0, 1, 2, 42.0);

    // Execute the macro
    sheet.executeMacro(macro);

    // Verify the cells have the expected values
    for (int row = 0; row <= 1; row++) {
      for (int col = 0; col <= 2; col++) {
        assertEquals(42.0, sheet.get(row, col), 0.001);
      }
    }

    // Verify surrounding cells are still 0
    assertEquals(0.0, sheet.get(0, 3), 0.001);
    assertEquals(0.0, sheet.get(2, 0), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRange_NegativeRow() {
    new BulkAssignMacro(-1, 0, 1, 1, 10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRange_NegativeColumn() {
    new BulkAssignMacro(0, -1, 1, 1, 10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRange_StartRowGreaterThanEndRow() {
    new BulkAssignMacro(2, 0, 1, 1, 10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRange_StartColGreaterThanEndCol() {
    new BulkAssignMacro(0, 2, 1, 1, 10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExecuteWithNullSheet() {
    SpreadSheetMacro macro = new BulkAssignMacro(0, 0, 1, 1, 10.0);
    macro.execute(null);
  }
}