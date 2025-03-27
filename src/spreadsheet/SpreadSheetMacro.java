/**
 * This interface represents a macro (command) that can be executed on a spreadsheet.
 * It follows the Command Design Pattern where each macro encapsulates a specific operation.
 */
public interface SpreadSheetMacro {

  /**
   * Execute this macro on the given spreadsheet.
   *
   * @param sheet the spreadsheet on which to execute this macro
   */
  void execute(SpreadSheet sheet);
}
