package spreadsheet;

/**
 * A macro that computes the average of a range of cells and stores it in a destination cell.
 * This implements the Command Design Pattern.
 */
public class AverageMacro implements SpreadSheetMacro {
  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final int destRow;
  private final int destCol;

  /**
   * Construct a new AverageMacro with the specified range and destination cell.
   *
   * @param fromRow the starting row (inclusive)
   * @param fromCol the starting column (inclusive)
   * @param toRow   the ending row (inclusive)
   * @param toCol   the ending column (inclusive)
   * @param destRow the row of the destination cell
   * @param destCol the column of the destination cell
   * @throws IllegalArgumentException if any indices are negative or if the range is invalid
   */
  public AverageMacro(int fromRow, int fromCol, int toRow, int toCol, int destRow, int destCol)
          throws IllegalArgumentException {
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0 || destRow < 0 || destCol < 0) {
      throw new IllegalArgumentException("Row and column indices cannot be negative");
    }

    if (fromRow > toRow || fromCol > toCol) {
      throw new IllegalArgumentException("Invalid range: starting indices must "
              + "be <= ending indices");
    }

    // Check if destination cell overlaps with source range
    if (destRow >= fromRow && destRow <= toRow && destCol >= fromCol && destCol <= toCol) {
      throw new IllegalArgumentException("Destination cell cannot overlap with source range");
    }

    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.destRow = destRow;
    this.destCol = destCol;
  }

  @Override
  public void execute(SpreadSheet sheet) {
    if (sheet == null) {
      throw new IllegalArgumentException("Sheet cannot be null");
    }

    double sum = 0.0;
    int count = 0;

    for (int row = fromRow; row <= toRow; row++) {
      for (int col = fromCol; col <= toCol; col++) {
        sum += sheet.get(row, col);
        count++;
      }
    }

    double average = count > 0 ? sum / count : 0.0;
    sheet.set(destRow, destCol, average);
  }
}