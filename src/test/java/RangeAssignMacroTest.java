import org.junit.Before;
import org.junit.Test;

import spreadsheet.RangeAssignMacro;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetMacro;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the RangeAssignMacro class.
 */
public class RangeAssignMacroTest {
  private SpreadSheetWithMacro sheet;

  @Before
  public void setup() {
    SpreadSheet baseSheet = new SparseSpreadSheet();
    sheet = new SpreadSheetWithMacroImpl(baseSheet);
  }

  @Test
  public void testHorizontalRange() {
    // Create a macro to assign values 1, 2, 3, 4, 5 to a row
    SpreadSheetMacro macro = new RangeAssignMacro(0, 0, 0, 4,
            1.0, 1.0);
    sheet.executeMacro(macro);

    // Verify the cells have the expected values
    for (int col = 0; col <= 4; col++) {
      assertEquals(col + 1.0, sheet.get(0, col), 0.001);
    }
  }

  @Test
  public void testVerticalRange() {
    // Create a macro to assign values 10, 20, 30 to a column
    SpreadSheetMacro macro = new RangeAssignMacro(0, 0, 2, 0,
            10.0, 10.0);
    sheet.executeMacro(macro);

    // Verify the cells have the expected values
    for (int row = 0; row <= 2; row++) {
      assertEquals((row + 1) * 10.0, sheet.get(row, 0), 0.001);
    }
  }

  @Test
  public void testGridRange() {
    // Create a macro to assign sequential values to a 2x3 grid
    // Values should be: 5, 6, 7
    //                  8, 9, 10
    SpreadSheetMacro macro = new RangeAssignMacro(0, 0, 1,
            2, 5.0, 1.0);
    sheet.executeMacro(macro);

    // Verify the first row
    assertEquals(5.0, sheet.get(0, 0), 0.001);
    assertEquals(6.0, sheet.get(0, 1), 0.001);
    assertEquals(7.0, sheet.get(0, 2), 0.001);

    // Verify the second row
    assertEquals(8.0, sheet.get(1, 0), 0.001);
    assertEquals(9.0, sheet.get(1, 1), 0.001);
    assertEquals(10.0, sheet.get(1, 2), 0.001);
  }

  @Test
  public void testNegativeIncrement() {
    // Create a macro to assign values 10, 8, 6, 4, 2 to a row
    SpreadSheetMacro macro = new RangeAssignMacro(0, 0, 0, 4,
            10.0, -2.0);
    sheet.executeMacro(macro);

    // Verify the cells have the expected values
    for (int col = 0; col <= 4; col++) {
      assertEquals(10.0 - (col * 2.0), sheet.get(0, col), 0.001);
    }
  }

  @Test
  public void testFloatingPointIncrement() {
    // Create a macro to assign values with a floating-point increment
    SpreadSheetMacro macro = new RangeAssignMacro(0, 0, 0, 4,
            1.0, 0.5);
    sheet.executeMacro(macro);

    // Verify the cells have the expected values
    for (int col = 0; col <= 4; col++) {
      assertEquals(1.0 + (col * 0.5), sheet.get(0, col), 0.001);
    }
  }

  @Test
  public void testSingleCell() {
    // Create a macro for a single cell
    SpreadSheetMacro macro = new RangeAssignMacro(1, 1, 1, 1,
            42.0, 5.0);
    sheet.executeMacro(macro);

    // Verify the cell has the expected value
    assertEquals(42.0, sheet.get(1, 1), 0.001);
  }

  @Test
  public void testZeroIncrement() {
    // Create a macro with zero increment (all cells get the same value)
    SpreadSheetMacro macro = new RangeAssignMacro(0, 0, 1, 1,
            7.0, 0.0);
    sheet.executeMacro(macro);

    // Verify all cells have the same value
    assertEquals(7.0, sheet.get(0, 0), 0.001);
    assertEquals(7.0, sheet.get(0, 1), 0.001);
    assertEquals(7.0, sheet.get(1, 0), 0.001);
    assertEquals(7.0, sheet.get(1, 1), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeFromRow() {
    new RangeAssignMacro(-1, 0, 1, 1, 1.0, 1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeFromCol() {
    new RangeAssignMacro(0, -1, 1, 1, 1.0, 1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeToRow() {
    new RangeAssignMacro(0, 0, -1, 1, 1.0, 1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeToCol() {
    new RangeAssignMacro(0, 0, 1, -1, 1.0, 1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromRowGreaterThanToRow() {
    new RangeAssignMacro(2, 0, 1, 1, 1.0, 1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromColGreaterThanToCol() {
    new RangeAssignMacro(0, 2, 1, 1, 1.0, 1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSheet() {
    SpreadSheetMacro macro = new RangeAssignMacro(0, 0, 1, 1,
            1.0, 1.0);
    macro.execute(null);
  }
}