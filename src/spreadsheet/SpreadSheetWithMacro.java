/**
 * This interface extends the SpreadSheet interface to add support for macros.
 * It represents a spreadsheet that can execute macros.
 */
public interface SpreadSheetWithMacro extends SpreadSheet {

  /**
   * Execute the given macro on this spreadsheet.
   *
   * @param macro the macro to execute.
   */
  void executeMacro(SpreadSheetMacro macro);
}
