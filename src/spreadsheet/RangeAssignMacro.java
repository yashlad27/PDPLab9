package spreadsheet;

/**
 * A macro that assigns a range of values to a range of cells in a spreadsheet.
 * Values start from a given value and increase by a given increment.
 * This implements the Command Design Pattern.
 */
public class RangeAssignMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final double startValue;
  private final double increment;

  /**
   * Construct a new RangeAssignMacro with the specified range, start value, and increment.
   *
   * @param fromRow    the starting row (inclusive)
   * @param fromCol    the starting column (inclusive)
   * @param toRow      the ending row (inclusive)
   * @param toCol      the ending column (inclusive)
   * @param startValue the initial value to assign
   * @param increment  the amount to increase each value by
   * @throws IllegalArgumentException if any indices are negative or if the range is invalid
   */
  public RangeAssignMacro(int fromRow, int fromCol, int toRow, int toCol,
                          double startValue, double increment) throws IllegalArgumentException {
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0) {
      throw new IllegalArgumentException("Row and column indices cannot be negative");
    }

    if (fromRow > toRow || fromCol > toCol) {
      throw new IllegalArgumentException("Invalid range: starting indices must be "
              + "<= ending indices");
    }

    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.startValue = startValue;
    this.increment = increment;
  }

  @Override
  public void execute(SpreadSheet sheet) {
    if (sheet == null) {
      throw new IllegalArgumentException("Sheet cannot be null");
    }

    double currentValue = startValue;

    for (int row = fromRow; row <= toRow; row++) {
      for (int col = fromCol; col <= toCol; col++) {
        sheet.set(row, col, currentValue);
        currentValue += increment;
      }
    }
  }
}