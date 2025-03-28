import org.junit.Before;
import org.junit.Test;

import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;
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
    // Test empty cell
    assertTrue(sheet.isEmpty(0, 0));

    // Test non-empty cell
    sheet.set(0, 0, 42.0);
    assertFalse(sheet.isEmpty(0, 0));

    // Test cell set to zero
    sheet.set(1, 1, 0.0);
    assertFalse(sheet.isEmpty(1, 1));
  }

  @Test
  public void testGetDelegation() {
    // Test getting empty cell
    assertEquals(0.0, sheet.get(0, 0), 0.001);

    // Test getting set cell
    sheet.set(0, 0, 42.0);
    assertEquals(42.0, sheet.get(0, 0), 0.001);

    // Test getting cell set to zero
    sheet.set(1, 1, 0.0);
    assertEquals(0.0, sheet.get(1, 1), 0.001);
  }

  @Test
  public void testWidthHeightDelegation() {
    // Test initial dimensions
    assertEquals(0, sheet.getWidth());
    assertEquals(0, sheet.getHeight());

    // Test dimensions after setting values
    sheet.set(5, 3, 42.0);
    assertEquals(4, sheet.getWidth());
    assertEquals(6, sheet.getHeight());

    // Test dimensions after setting larger values
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
} 