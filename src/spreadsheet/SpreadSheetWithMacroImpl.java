package spreadsheet;

/**
 * Implementation wraps an existing Spreadsheet.
 * This class is using composition to reuse functionality from original sheet.
 */
public class SpreadSheetWithMacroImpl implements SpreadSheetWithMacro {

  private final SpreadSheet sheet;

  /**
   * Constructs a new SpreadSheetWithMacroImpl with a default SparseSpreadSheet.
   */
  public SpreadSheetWithMacroImpl() {
    this.sheet = new SparseSpreadSheet();
  }

  /**
   * Constructs a new SpreadSheetWithMacroImpl that wraps an existing spreadsheet.
   * This implementation uses composition to add macro functionality to
   * any SpreadSheet implementation.
   *
   * @param sheet the base spreadsheet to be enhanced with macro capabilities
   * @throws IllegalArgumentException if the provided sheet is null
   */
  public SpreadSheetWithMacroImpl(SpreadSheet sheet) throws IllegalArgumentException {
    if (sheet == null) {
      throw new IllegalArgumentException("Sheet cannot be null");
    }
    this.sheet = sheet;
  }

  @Override
  public void executeMacro(SpreadSheetMacro macro) {
    if (macro == null) {
      throw new IllegalArgumentException("Macro cannot be null");
    }
    macro.execute(this);
  }

  @Override
  public double get(int row, int col) throws IllegalArgumentException {
    return sheet.get(row, col);
  }

  @Override
  public void set(int row, int col, double value) throws IllegalArgumentException {
    sheet.set(row, col, value);
  }

  @Override
  public boolean isEmpty(int row, int col) throws IllegalArgumentException {
    return sheet.isEmpty(row, col);
  }

  @Override
  public int getWidth() {
    return sheet.getWidth();
  }

  @Override
  public int getHeight() {
    return sheet.getHeight();
  }
}
