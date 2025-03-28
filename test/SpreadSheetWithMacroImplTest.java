import org.junit.Before;
import org.junit.Test;

import spreadsheet.AverageMacro;
import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
import spreadsheet.SpreadSheetMacro;
import spreadsheet.SpreadSheetWithMacro;
import spreadsheet.SpreadSheetWithMacroImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for SpreadSheetWithMacroImpl.
 */
public class SpreadSheetWithMacroImplTest {
  private SpreadSheetWithMacro sheet;

  @Before
  public void setup() {
    SpreadSheet baseSheet = new SparseSpreadSheet();
    sheet = new SpreadSheetWithMacroImpl(baseSheet);
  }

  @Test
  public void testIsEmptyDelegation() {
    assertTrue(sheet.isEmpty(0, 0));

    sheet.set(0, 0, 42.0);
    assertFalse(sheet.isEmpty(0, 0));

    sheet.set(1, 1, 0.0);
    assertFalse(sheet.isEmpty(1, 1));
  }

  @Test
  public void testGetDelegation() {
    assertEquals(0.0, sheet.get(0, 0), 0.001);

    sheet.set(0, 0, 42.0);
    assertEquals(42.0, sheet.get(0, 0), 0.001);

    sheet.set(1, 1, 0.0);
    assertEquals(0.0, sheet.get(1, 1), 0.001);
  }

  @Test
  public void testWidthHeightDelegation() {
    assertEquals(0, sheet.getWidth());
    assertEquals(0, sheet.getHeight());

    sheet.set(5, 3, 42.0);
    assertEquals(4, sheet.getWidth());
    assertEquals(6, sheet.getHeight());

    sheet.set(7, 8, 42.0);
    assertEquals(9, sheet.getWidth());
    assertEquals(8, sheet.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNull() {
    new SpreadSheetWithMacroImpl(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsEmptyWithNegativeRow() {
    sheet.isEmpty(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsEmptyWithNegativeCol() {
    sheet.isEmpty(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNegativeRow() {
    sheet.get(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNegativeCol() {
    sheet.get(0, -1);
  }

  @Test
  public void testMacroExecution() {
    sheet.set(0, 0, 10.0);
    sheet.set(0, 1, 20.0);
    sheet.set(1, 0, 30.0);
    sheet.set(1, 1, 40.0);

    SpreadSheetMacro macro = new AverageMacro(0, 0, 1, 1, 2, 2);
    sheet.executeMacro(macro);

    assertEquals(25.0, sheet.get(2, 2), 0.001);
  }

  @Test
  public void testMultipleMacroExecutions() {
    sheet.set(0, 0, 10.0);
    sheet.set(0, 1, 20.0);

    SpreadSheetMacro macro1 = new AverageMacro(0, 0, 0, 1, 1, 0);
    sheet.executeMacro(macro1);
    assertEquals(15.0, sheet.get(1, 0), 0.001);

    sheet.set(1, 1, 30.0);
    SpreadSheetMacro macro2 = new AverageMacro(1, 0, 1, 1, 2, 0);
    sheet.executeMacro(macro2);
    assertEquals(22.5, sheet.get(2, 0), 0.001);
  }

  @Test
  public void testMacroStatePreservation() {
    sheet.set(0, 0, 10.0);
    sheet.set(0, 1, 20.0);

    SpreadSheetMacro macro = new AverageMacro(0, 0, 0, 1, 1, 0);
    sheet.executeMacro(macro);

    // Original values should be preserved
    assertEquals(10.0, sheet.get(0, 0), 0.001);
    assertEquals(20.0, sheet.get(0, 1), 0.001);
    assertEquals(15.0, sheet.get(1, 0), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMacroWithOverlappingRanges() {
    sheet.set(0, 0, 10.0);
    sheet.set(0, 1, 20.0);

    // Create a macro where source and destination ranges overlap
    SpreadSheetMacro macro = new AverageMacro(0, 0, 1, 1, 0, 0);
    sheet.executeMacro(macro);
  }

  @Test
  public void testMacroWithEmptyRange() {
    SpreadSheetMacro macro = new AverageMacro(0, 0, 1, 1, 2, 2);
    sheet.executeMacro(macro);
    assertEquals(0.0, sheet.get(2, 2), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMacroWithInvalidRange() {
    SpreadSheetMacro macro = new AverageMacro(2, 2, 1, 1, 3, 3);
    sheet.executeMacro(macro);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMacroWithNullMacro() {
    sheet.executeMacro(null);
  }

  @Test
  public void testMacroWithZeroValues() {
    sheet.set(0, 0, 0.0);
    sheet.set(0, 1, 0.0);

    SpreadSheetMacro macro = new AverageMacro(0, 0, 0, 1, 1, 0);
    sheet.executeMacro(macro);
    assertEquals(0.0, sheet.get(1, 0), 0.001);
  }
} 