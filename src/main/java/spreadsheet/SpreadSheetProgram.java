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
    SpreadSheet baseModel = new SparseSpreadSheet();

    SpreadSheetWithMacro model = new SpreadSheetWithMacroImpl(baseModel);

    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;

    MacroSpreadSheetController controller = new MacroSpreadSheetController(model, rd, ap);
    controller.control();
  }
}