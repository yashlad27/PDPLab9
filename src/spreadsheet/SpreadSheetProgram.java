package spreadsheet;

import java.io.InputStreamReader;

/**
 * The driver of this application.
 */
public class SpreadSheetProgram {
  /**
   * main method of the program.
   *
   * @param args any command line arguments
   */
  public static void main(String[] args) {
    // Create base spreadsheet
    SpreadSheet baseModel = new SparseSpreadSheet();

    // Wrap it with macro support
    SpreadSheetWithMacro model = new SpreadSheetWithMacroImpl(baseModel);

    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;

    // Use the enhanced controller
    MacroSpreadSheetController controller = new MacroSpreadSheetController(model, rd, ap);
    controller.control();
  }
}