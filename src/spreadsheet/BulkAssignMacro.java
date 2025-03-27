/**
 * A macro that assigns a specific value to a range of cells in a spreadsheet.
 * This is using CommandDesignPattern.
 */
public class BulkAssignMacro implements SpreadSheetMacro {

  private final int fromRow;
  private final int fromCol;
  private final int toRow;
  private final int toCol;
  private final double value;

  public BulkAssignMacro(int fromRow, int fromCol, int toRow, int toCol, double value)
          throws IllegalArgumentException {
    if (fromRow < 0 || fromCol < 0 || toRow < 0 || toCol < 0) {
      throw new IllegalArgumentException("Row and column index cannot be negative");
    }

    if (fromRow > toRow || fromCol > toCol) {
      throw new IllegalArgumentException("Invalid range: starting index must be less than "
              + "or equal to ending index");
    }

    this.fromRow = fromRow;
    this.fromCol = fromCol;
    this.toRow = toRow;
    this.toCol = toCol;
    this.value = value;
  }

  @Override
  public void execute(SpreadSheet sheet) {
    if(sheet == null) {
      throw new IllegalArgumentException("Sheet cannot be null");
    }

    for(int row = fromRow; row<=toRow; row++) {
      for(int col = fromCol; col<=toCol; col++){
        sheet.set(row, col, value);
      }
    }
  }
}