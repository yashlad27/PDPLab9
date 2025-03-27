package spreadsheet;

import java.util.Scanner;

/**
 * This class extends the SpreadSheetController to add support for macros.
 * It adds commands that use the command design pattern to execute macros.
 */
public class MacroSpreadSheetController extends SpreadSheetController {
  private final SpreadSheetWithMacro macroSheet;

  /**
   * Create a controller to work with the specified sheet (model),
   * readable (to take inputs) and appendable (to transmit output).
   *
   * @param sheet      the sheet to work with (must implement SpreadSheetWithMacro)
   * @param readable   the Readable object for inputs
   * @param appendable the Appendable objects to transmit any output
   */
  public MacroSpreadSheetController(SpreadSheet sheet, Readable readable, Appendable appendable)
          throws IllegalArgumentException {
    super(sheet, readable, appendable);

    if (!(sheet instanceof SpreadSheetWithMacro)) {
      throw new IllegalArgumentException("Sheet must implement SpreadSheetWithMacro");
    }

    this.macroSheet = (SpreadSheetWithMacro) sheet;
  }

  @Override
  protected void processCommand(String userInstruction, Scanner sc, SpreadSheet sheet) {
    int fromRow;
    int fromCol;
    int toRow;
    int toCol;
    double value;
    int destRow;
    int destCol;
    double startValue;
    double increment;

    switch (userInstruction) {
      case "bulk-assign-value":
        try {
          fromRow = getRowNum(sc.next());
          fromCol = sc.nextInt() - 1; // Convert to 0-indexed
          toRow = getRowNum(sc.next());
          toCol = sc.nextInt() - 1; // Convert to 0-indexed
          value = sc.nextDouble();

          writeMessage("Setting cells from (" + fromRow + "," + fromCol
                  + ") to (" + toRow + "," + toCol + ") to " + value + System.lineSeparator());

          BulkAssignMacro macro = new BulkAssignMacro(fromRow, fromCol, toRow, toCol, value);
          macroSheet.executeMacro(macro);
        } catch (IllegalArgumentException e) {
          writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        }
        break;

      case "average":
        try {
          fromRow = getRowNum(sc.next());
          fromCol = sc.nextInt() - 1; // Convert to 0-indexed
          toRow = getRowNum(sc.next());
          toCol = sc.nextInt() - 1; // Convert to 0-indexed
          destRow = getRowNum(sc.next());
          destCol = sc.nextInt() - 1; // Convert to 0-indexed

          writeMessage("Computing average of cells from (" + fromRow + "," + fromCol
                  + ") to (" + toRow + "," + toCol
                  + ") and storing in (" + destRow + "," + destCol + ")"
                  + System.lineSeparator());

          AverageMacro macro = new AverageMacro(fromRow, fromCol, toRow, toCol, destRow, destCol);
          macroSheet.executeMacro(macro);
        } catch (IllegalArgumentException e) {
          writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        }
        break;

      case "range-assign":
        try {
          fromRow = getRowNum(sc.next());
          fromCol = sc.nextInt() - 1; // Convert to 0-indexed
          toRow = getRowNum(sc.next());
          toCol = sc.nextInt() - 1; // Convert to 0-indexed
          startValue = sc.nextDouble();
          increment = sc.nextDouble();

          writeMessage("Assigning range of values starting at " + startValue
                  + " with increment " + increment
                  + " to cells from (" + fromRow + "," + fromCol
                  + ") to (" + toRow + "," + toCol + ")"
                  + System.lineSeparator());

          RangeAssignMacro macro = new RangeAssignMacro(fromRow, fromCol, toRow, toCol,
                  startValue, increment);
          macroSheet.executeMacro(macro);
        } catch (IllegalArgumentException e) {
          writeMessage("Error: " + e.getMessage() + System.lineSeparator());
        }
        break;

      default:
        // Delegate to parent class for other commands
        super.processCommand(userInstruction, sc, sheet);
    }
  }

  @Override
  protected void printMenu() {
    writeMessage("Supported user instructions are: " + System.lineSeparator());
    writeMessage("bulk-assign-value from-row-num from-col-num to-row-num to-col-num value "
            + "(set a range of cells to a value)" + System.lineSeparator());
    writeMessage("range-assign from-row-num from-col-num to-row-num to-col-num "
            + "start-value increment "
            + "(set a row or column of cells to a range of values starting at the given value "
            + "and advancing by the increment)" + System.lineSeparator());
    writeMessage("average from-row-num from-col-num to-row-num to-col-num dest-row-num dest-col-num "
            + "(compute the average of a range of cells and put it at the given location)"
            + System.lineSeparator());
    super.printMenu();
  }
}