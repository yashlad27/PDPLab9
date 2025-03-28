import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import spreadsheet.SparseSpreadSheet;
import spreadsheet.SpreadSheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * This class is the tester for a sparse spreadsheet.
 */
public class SparseSpreadSheetTest {
  private SpreadSheet sheet;

  @Before
  public void setup() {
    sheet = new SparseSpreadSheet();
  }

  @Test
  public void testGetSet() {
    Random r = new Random(100);
    double[][] expectedSet = new double[100][100];
    for (int i = 0; i < 100; i = i + 1) {
      for (int j = 0; j < 100; j = j + 1) {
        double num = r.nextDouble();
        expectedSet[i][j] = num;
        assertTrue(sheet.isEmpty(i, j));
        assertEquals(0.0, sheet.get(i, j), 0.001);
        sheet.set(i, j, num);
        assertFalse(sheet.isEmpty(i, j));
      }
    }

    for (int i = 0; i < 100; i = i + 1) {
      for (int j = 0; j < 100; j = j + 1) {
        assertEquals(expectedSet[i][j], sheet.get(i, j), 0.01);
      }
    }
  }

  @Test
  public void testGetWidthHeight() {
    for (int i = 0; i < 100; i = i + 1) {
      for (int j = 0; j < 100; j = j + 1) {
        sheet.set(i, j, 0);
        assertEquals((i + 1), sheet.getHeight());
        if (i == 0) {
          assertEquals((j + 1), sheet.getWidth());
        } else {
          assertEquals(100, sheet.getWidth());
        }
      }
    }

    sheet.set(1000, 1000, 0);
    assertEquals(1001, sheet.getWidth());
    assertEquals(1001, sheet.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNegativeRow() {
    sheet.set(0, 0, 1);
    sheet.set(0, 1, 9);
    sheet.get(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNegativeColumn() {
    sheet.set(0, 0, 1);
    sheet.set(0, 1, 9);
    sheet.get(0, -1);
  }

  @Test
  public void testIsEmptyEdgeCases() {
    // Test isEmpty after setting and then getting a value
    sheet.set(0, 0, 42.0);
    assertFalse(sheet.isEmpty(0, 0));
    
    // Test isEmpty after setting a value to 0
    sheet.set(1, 1, 0.0);
    assertFalse(sheet.isEmpty(1, 1));
    
    // Test isEmpty for unset cell
    assertTrue(sheet.isEmpty(2, 2));
    
    // Test isEmpty after setting and overwriting with same value
    sheet.set(3, 3, 10.0);
    sheet.set(3, 3, 10.0);
    assertFalse(sheet.isEmpty(3, 3));
  }

  @Test
  public void testGetWidthHeightEdgeCases() {
    // Test initial width and height
    assertEquals(0, sheet.getWidth());
    assertEquals(0, sheet.getHeight());
    
    // Test width/height after setting non-zero value
    sheet.set(5, 5, 42.0);
    assertEquals(6, sheet.getWidth());
    assertEquals(6, sheet.getHeight());
    
    // Test width/height after setting zero value
    sheet.set(7, 7, 0.0);
    assertEquals(8, sheet.getWidth());
    assertEquals(8, sheet.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsEmptyWithNegativeCol() {
    sheet.isEmpty(0, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWithNegativeCol() {
    sheet.set(0, -1, 42.0);
  }
}