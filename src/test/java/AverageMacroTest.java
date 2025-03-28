import org.junit.Before;
import org.junit.Test;

import spreadsheet.AverageMacro;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetMacro;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the AverageMacro class.
 */
public class AverageMacroTest {
  private SpreadSheetWithMacro sheet;

  @Before
  public void setup() {
    SpreadSheet baseSheet = new SparseSpreadSheet();
    sheet = new SpreadSheetWithMacroImpl(baseSheet);
  }

  @Test
  public void testBasicAverage() {
    // First, set up some values
    sheet.set(1, 1, 10.0);
    sheet.set(1, 2, 20.0);
    sheet.set(2, 1, 30.0);
    sheet.set(2, 2, 40.0);

    SpreadSheetMacro macro = new AverageMacro(1, 1, 2, 2, 3, 3);
    sheet.executeMacro(macro);

    assertEquals(25.0, sheet.get(3, 3), 0.001);
  }

  @Test
  public void testAverageWithEmptyCells() {
    sheet.set(1, 1, 10.0);
    sheet.set(2, 2, 30.0);

    SpreadSheetMacro macro = new AverageMacro(1, 1, 2, 2, 3, 3);
    sheet.executeMacro(macro);

    assertEquals(10.0, sheet.get(3, 3), 0.001);
  }

  @Test
  public void testAverageWithEmptyRange() {
    SpreadSheetMacro macro = new AverageMacro(1, 1, 2, 2, 3, 3);
    sheet.executeMacro(macro);

    assertEquals(0.0, sheet.get(3, 3), 0.001);
  }

  @Test
  public void testAverageSingleCell() {
    sheet.set(1, 1, 42.0);

    SpreadSheetMacro macro = new AverageMacro(1, 1, 1, 1, 2, 2);
    sheet.executeMacro(macro);

    assertEquals(42.0, sheet.get(2, 2), 0.001);
  }

  @Test
  public void testAverageNegativeValues() {
    sheet.set(1, 1, -10.0);
    sheet.set(1, 2, -20.0);
    sheet.set(2, 1, 15.0);
    sheet.set(2, 2, 25.0);

    SpreadSheetMacro macro = new AverageMacro(1, 1, 2, 2, 3, 3);
    sheet.executeMacro(macro);

    // Verify the average was computed correctly (-10-20+15+25)/4 = 2.5
    assertEquals(2.5, sheet.get(3, 3), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeFromRow() {
    new AverageMacro(-1, 0, 2, 2, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeFromCol() {
    new AverageMacro(0, -1, 2, 2, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeToRow() {
    new AverageMacro(0, 0, -1, 2, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeToCol() {
    new AverageMacro(0, 0, 2, -1, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeDestRow() {
    new AverageMacro(0, 0, 2, 2, -1, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeDestCol() {
    new AverageMacro(0, 0, 2, 2, 3, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromRowGreaterThanToRow() {
    new AverageMacro(3, 0, 2, 2, 4, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromColGreaterThanToCol() {
    new AverageMacro(0, 3, 2, 2, 4, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSheet() {
    SpreadSheetMacro macro = new AverageMacro(0, 0, 1, 1, 2, 2);
    macro.execute(null);
  }
}