package spreadsheet;

import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

/**
 * This class extends the SpreadSheetController to add support for macros.
 * It adds commands that use the command design pattern to execute macros.
 */
public class MacroSpreadSheetController extends SpreadSheetController {
  private final SpreadSheetWithMacro macroSheet;
  private final Readable input;
  private final Appendable output;
  private final Scanner scanner;

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
    this.input = readable;
    this.output = appendable;
    this.scanner = new Scanner(readable);
  }

  /**
   * Starts the controller and processes user input until the user quits.
   */
  public void control() {
    try {
      output.append("Welcome to the spreadsheet program!\n");
      displayMenu();
      Scanner scanner = new Scanner(input);

      while (scanner.hasNextLine()) {
        output.append("Type instruction: ");
        String command = scanner.nextLine().trim();
        
        if (command.isEmpty()) {
          continue;
        }

        String[] tokens = command.split("\\s+");
        String originalCommand = tokens[0];
        String commandLower = originalCommand.toLowerCase();

        if (commandLower.equals("quit")) {
          output.append("Thank you for using this program!\n");
          break;
        }

        try {
          switch (commandLower) {
            case "assign-value":
              if (!originalCommand.equals("assign-value")) {
                output.append("Undefined instruction: " + originalCommand + "\n");
                continue;
              }
              handleAssignValue(tokens);
              break;
            case "print-value":
              if (!originalCommand.equals("print-value")) {
                output.append("Undefined instruction: " + originalCommand + "\n");
                continue;
              }
              handlePrintValue(tokens);
              break;
            case "bulk-assign-value":
              if (!originalCommand.equals("bulk-assign-value")) {
                output.append("Undefined instruction: " + originalCommand + "\n");
                continue;
              }
              handleBulkAssignValue(tokens);
              break;
            case "range-assign":
              if (!originalCommand.equals("range-assign")) {
                output.append("Undefined instruction: " + originalCommand + "\n");
                continue;
              }
              handleRangeAssign(tokens);
              break;
            case "average":
              if (!originalCommand.equals("average")) {
                output.append("Undefined instruction: " + originalCommand + "\n");
                continue;
              }
              handleAverage(tokens);
              break;
            default:
              output.append("Undefined instruction: " + originalCommand + "\n");
          }
        } catch (IllegalArgumentException e) {
          output.append(e.getMessage() + "\n");
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write output", e);
    }
  }

  private void displayMenu() throws IOException {
    output.append("Supported user instructions are:\n");
    output.append("  assign-value ROW COL VALUE (set a cell to a value)\n");
    output.append("  print-value ROW COL (print the value at the given location)\n");
    output.append("  bulk-assign-value FROM_ROW FROM_COL TO_ROW TO_COL VALUE (set a range of cells to a value)\n");
    output.append("  range-assign FROM_ROW FROM_COL TO_ROW TO_COL START_VALUE INCREMENT (set a range of cells with incrementing values)\n");
    output.append("  average FROM_ROW FROM_COL TO_ROW TO_COL DEST_ROW DEST_COL (compute average of cells)\n");
    output.append("  menu (show this menu)\n");
    output.append("  quit (exit the program)\n");
  }

  private void handleAssignValue(String[] tokens) throws IOException {
    if (tokens.length != 4) {
      throw new IllegalArgumentException("Invalid parameters for assign-value command");
    }

    try {
      String row = tokens[1];
      int col = Integer.parseInt(tokens[2]) - 1;
      double value = Double.parseDouble(tokens[3]);

      if (col < 0) {
        throw new IllegalArgumentException("Error: Column number must be positive");
      }

      int rowIndex = convertRowToIndex(row);
      macroSheet.set(rowIndex, col, value);
      output.append(String.format("Setting cell (%d,%d) to %.1f\n", rowIndex, col, value));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Error: Invalid number format");
    }
  }

  private void handlePrintValue(String[] tokens) throws IOException {
    if (tokens.length != 3) {
      throw new IllegalArgumentException("Invalid parameters for print-value command");
    }

    try {
      String row = tokens[1];
      int col = Integer.parseInt(tokens[2]) - 1;

      if (col < 0) {
        throw new IllegalArgumentException("Error: Column number must be positive");
      }

      int rowIndex = convertRowToIndex(row);
      double value = macroSheet.get(rowIndex, col);
      output.append(String.format("Value: %.1f\n", value));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Error: Invalid number format");
    }
  }

  private void handleBulkAssignValue(String[] tokens) throws IOException {
    if (tokens.length != 6) {
      throw new IllegalArgumentException("Invalid parameters for bulk-assign-value command");
    }

    try {
      String fromRow = tokens[1];
      int fromCol = Integer.parseInt(tokens[2]) - 1;
      String toRow = tokens[3];
      int toCol = Integer.parseInt(tokens[4]) - 1;
      double value = Double.parseDouble(tokens[5]);

      if (fromCol < 0 || toCol < 0) {
        throw new IllegalArgumentException("Error: Column numbers must be positive");
      }

      int fromRowIndex = convertRowToIndex(fromRow);
      int toRowIndex = convertRowToIndex(toRow);

      BulkAssignMacro macro = new BulkAssignMacro(fromRowIndex, fromCol, toRowIndex, toCol, value);
      macro.execute(macroSheet);

      output.append(String.format("Setting cells from (%d,%d) to (%d,%d) to %.1f\n", 
          fromRowIndex, fromCol, toRowIndex, toCol, value));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Error: Invalid number format");
    }
  }

  private void handleRangeAssign(String[] tokens) throws IOException {
    if (tokens.length != 7) {
      throw new IllegalArgumentException("Invalid command format. Use: range-assign FROM_ROW FROM_COL TO_ROW TO_COL START_VALUE INCREMENT");
    }
    
    try {
      int fromRow = convertRowToIndex(tokens[1]);
      int fromCol = Integer.parseInt(tokens[2]) - 1;
      int toRow = convertRowToIndex(tokens[3]);
      int toCol = Integer.parseInt(tokens[4]) - 1;
      double startValue = Double.parseDouble(tokens[5]);
      double increment = Double.parseDouble(tokens[6]);
      
      if (fromRow < 0 || toRow < 0) {
        throw new IllegalArgumentException("Invalid row reference");
      }
      if (fromCol < 0 || toCol < 0) {
        throw new IllegalArgumentException("Invalid column reference");
      }
      
      RangeAssignMacro macro = new RangeAssignMacro(fromRow, fromCol, toRow, toCol, startValue, increment);
      macroSheet.executeMacro(macro);
      output.append("Setting cells from (" + fromRow + "," + fromCol + ") to (" + 
                    toRow + "," + toCol + ") starting at " + startValue + 
                    " with increment " + increment + "\n");
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid number format");
    }
  }

  private void handleAverage(String[] tokens) throws IOException {
    if (tokens.length != 7) {
      throw new IllegalArgumentException("Invalid command format. Use: average FROM_ROW FROM_COL TO_ROW TO_COL DEST_ROW DEST_COL");
    }
    
    try {
      int fromRow = convertRowToIndex(tokens[1]);
      int fromCol = Integer.parseInt(tokens[2]) - 1;
      int toRow = convertRowToIndex(tokens[3]);
      int toCol = Integer.parseInt(tokens[4]) - 1;
      int destRow = convertRowToIndex(tokens[5]);
      int destCol = Integer.parseInt(tokens[6]) - 1;
      
      if (fromRow < 0 || toRow < 0 || destRow < 0) {
        throw new IllegalArgumentException("Invalid row reference");
      }
      if (fromCol < 0 || toCol < 0 || destCol < 0) {
        throw new IllegalArgumentException("Invalid column reference");
      }
      
      AverageMacro macro = new AverageMacro(fromRow, fromCol, toRow, toCol, destRow, destCol);
      macroSheet.executeMacro(macro);
      output.append("Computing average of cells from (" + fromRow + "," + fromCol + ") to (" + 
                    toRow + "," + toCol + ") and storing in (" + 
                    destRow + "," + destCol + ")\n");
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid number format");
    }
  }

  private int convertRowToIndex(String row) {
    if (row == null || row.isEmpty() || !row.matches("[A-Za-z]")) {
      throw new IllegalArgumentException("Error: Invalid row");
    }
    return Character.toUpperCase(row.charAt(0)) - 'A';
  }
}