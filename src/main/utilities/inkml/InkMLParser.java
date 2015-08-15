package main.utilities.inkml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.utilities.traces.Point;
import main.utilities.traces.TraceGroup;
import main.utilities.traces.Trace;

/** @class InkMLParser
 *
 *  @brief Implements a parser for InkML files.
 *
 *  For more information on InkML files see http://www.w3.org/TR/InkML/ .
 */
public class InkMLParser{
  /**
   *  @brief Default constructor.
   */
  public InkMLParser(){
    xmlData_ = new String();

    traceGroup_ = new TraceGroup();

    equation_ = new String();
  }

  /**
   *  @brief Constructor.
   *
   *  @param xmlData Initial value for the xml data of the InkMLParser.
   */
  public InkMLParser(String xmlData){
    xmlData_ = xmlData;

    traceGroup_ = new TraceGroup();

    equation_ = new String();
  }

  /**
   *  @brief Reads an InkML file.
   *
   *  @param file The File object.
   *
   *  @return Returns the xml data loaded from the file.
   *
   *  @throws FileNotFoundException If the file cannot be found.
   */
  public static String readInkMLFile(File file) throws FileNotFoundException{
    Scanner scanner = new Scanner(file);

    String xmlData = new String("");
    while(scanner.hasNextLine()){
      xmlData += scanner.nextLine();
    }
    scanner.close();

    return xmlData;
  }

  /**
   *  @brief Parses an InkML file.
   *
   *  @param file The File object.
   *
   *  @throws FileNotFoundException If the file cannot be found.
   */
  public void parse(File file) throws FileNotFoundException{
    this.reset();

    xmlData_ = readInkMLFile(file);

    parse();
  }

  /**
   *  @brief Parses the xml data already loaded.
   *
   *  At the current moment, the InkMLParser can detect a single annotation with type = "equationInTeX". Also, the
   *  InkMLParser can detect a single TraceGroup element.
   */
  public void parse(){
    // Work on a copy of the xml data in order not to ruin them.
    String xmlData = new String(xmlData_);

    int startOfAnnotation = xmlData.indexOf("<annotation type=\"equationInTeX\">");
    int endOfAnnotation = xmlData.indexOf("</annotation>");

    if(startOfAnnotation != -1){
      equation_ = xmlData.substring(startOfAnnotation + ("<annotation type=\"equationInTeX\">").length(), endOfAnnotation);
    }

    int startOfTrace = xmlData.indexOf("<trace>");
    int endOfTrace = xmlData.indexOf("</trace>");
    while(startOfTrace != -1){
      String[] traceData = xmlData.substring(startOfTrace + 7, endOfTrace).split(", "); // ("<trace>").length = 7.

      Trace trace = new Trace();
      for(int i = 0;i < traceData.length;i++){
        double x = Double.parseDouble(traceData[i].split(" ")[0]);
        double y = Double.parseDouble(traceData[i].split(" ")[1]);
        trace.add(new Point(x, y));
      }
      if(trace.size() > 1){
        traceGroup_.add(trace);
      }

      xmlData = xmlData.substring(endOfTrace + 8); // ("</trace>").length = 8.
      startOfTrace = xmlData.indexOf("<trace>");
      endOfTrace = xmlData.indexOf("</trace>");
    }
  }

  /**
   *  @brief Setter method for xml data.
   *
   *  The xml data of the InkMLParser is a copy ofthe given as input data. That is, if the given as input String changes,
   *  the xml data of the InkMLParser will not change.
   *
   *  @param xmlData The new value for xml data.
   */
  public void setXMLData(String xmlData){
    this.reset();

    xmlData_ = new String(xmlData);
  }

  /**
   *  @brief Getter method for xml data.
   *
   *  The xml data returned is a copy of the InkMLParser xml data. That is, if the returned String changes, the xml data
   *  of the InkMLParser will not change.
   *
   *  @return The xml data of the parser.
   */
  public String getXMLData(){
    return (new String(xmlData_));
  }

  /**
   *  @brief Getter method for the equation of the InkMLParser.
   *
   *  The equation is the annotation found under the equationInTeX type. The String returned is a copy of the equation
   *  of the InkMLParser. That is, if the returned String changes, the String inside the InkMLParser will not change.
   *
   *  @return Returns the equation of the current xml data.
   */
  public String getEquation(){
    return equation_;
  }

  /**
   *  @brief Resets the InkMLParser.
   *
   *  By reseting the InkMLParser, the xml data, the TraceGroup and the equation stored by the InkMLParser, are erased.
   */
  public void reset(){
    xmlData_ = new String();

    traceGroup_ = new TraceGroup();

    equation_ = new String();
  }

  private String xmlData_; //!< The xml data of the InkMLParser.

  private String equation_; //!< The equation found as annotation under the equationInTeX type.

  public TraceGroup traceGroup_; //!< The TraceGroup parsed from the xml data.

}
