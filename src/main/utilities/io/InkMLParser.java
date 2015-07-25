package main.utilities.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.utilities.traces.Point;
import main.utilities.traces.TraceGroup;
import main.utilities.traces.Trace;

public class InkMLParser{
  public InkMLParser(){
    xmlData_ = "";

    traceGroup_ = new TraceGroup();

    equation_ = "";
  }

  public InkMLParser(String xmlData){
    xmlData_ = xmlData;

    traceGroup_ = new TraceGroup();

    equation_ = "";
  }

  public static String readInkMLFile(File file) throws FileNotFoundException{
    Scanner scanner = new Scanner(file);

    String xmlData = new String("");
    while(scanner.hasNextLine()){
      xmlData += scanner.nextLine();
    }
    scanner.close();

    return xmlData;
  }

  public void parse(File file) throws FileNotFoundException{
    this.reset();

    xmlData_ = readInkMLFile(file);

    parse();
  }

  public void parse(){
    this.reset();

    String xmlData = new String(xmlData_);

    int startOfAnnotation = xmlData.indexOf("<annotation type=\"equationInTeX\">");
    int endOfAnnotation = xmlData.indexOf("</annotation>");

    equation_ = xmlData.substring(startOfAnnotation + ("<annotation type=\"equationInTeX\">").length(), endOfAnnotation);

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

  public void setXMLData(String xmlData){
    this.reset();

    xmlData_ = xmlData;
  }

  public String getEquation(){
    return equation_;
  }

  private void reset(){
    traceGroup_ = new TraceGroup();
  }

  private String xmlData_;

  private String equation_;

  public TraceGroup traceGroup_;

}

