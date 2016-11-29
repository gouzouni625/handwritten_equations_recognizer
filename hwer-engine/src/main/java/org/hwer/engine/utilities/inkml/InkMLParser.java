package org.hwer.engine.utilities.inkml;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class InkMLParser
 * @brief Implements a parser for InkML files
 *        For more information on InkML files see http://www.w3.org/TR/InkML/ .
 */
public class InkMLParser {
    /**
     * @brief Default constructor
     */
    public InkMLParser () {
        xmlData_ = "";
        equation_ = "";

        traceGroup_ = new TraceGroup();
    }

    /**
     * @brief Constructor
     *
     * @param xmlData
     *     Initial value for the xml data of the InkMLParser
     */
    public InkMLParser (String xmlData) {
        xmlData_ = xmlData;
        equation_ = "";

        traceGroup_ = new TraceGroup();
    }

    /**
     * @brief Reads an InkML file
     *
     * @param file
     *     The File object
     *
     * @return The xml data loaded from the file
     *
     * @throws FileNotFoundException If the file cannot be found
     */
    public static String readInkMLFile (File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        scanner.close();

        return stringBuilder.toString();
    }

    /**
     * @brief Parses an InkML file
     *
     * @param file
     *     The File object
     *
     * @throws FileNotFoundException If the file cannot be found
     */
    public void parse (File file) throws FileNotFoundException {
        this.reset();

        xmlData_ = readInkMLFile(file);

        parse();
    }

    /**
     * @brief Parses the xml data already loaded
     *        At the current moment, the InkMLParser can detect a single annotation with
     *        type = "equationInTeX". Also, the InkMLParser can detect a single TraceGroup element.
     */
    public void parse () {
        String xmlData = xmlData_;

        int startOfAnnotation = xmlData.indexOf("<annotation type=\"equationInTeX\">");
        int endOfAnnotation = xmlData.indexOf("</annotation>");

        if (startOfAnnotation != - 1) {
            equation_ = xmlData.substring(startOfAnnotation +
                ("<annotation type=\"equationInTeX\">").length(), endOfAnnotation);
        }

        int startOfTrace = xmlData.indexOf("<trace>");
        int endOfTrace = xmlData.indexOf("</trace>");
        while (startOfTrace != - 1) {
            String[] traceData = xmlData.substring(startOfTrace + 7, endOfTrace).
                split(", "); // ("<trace>").length = 7.

            Trace trace = new Trace();
            for (String aTraceData : traceData) {
                double x = Double.parseDouble(aTraceData.split(" ")[0]);
                double y = Double.parseDouble(aTraceData.split(" ")[1]);
                trace.add(new Point(x, y));
            }
            if (trace.size() > 1) {
                traceGroup_.add(trace);
            }

            xmlData = xmlData.substring(endOfTrace + 8); // ("</trace>").length = 8.
            startOfTrace = xmlData.indexOf("<trace>");
            endOfTrace = xmlData.indexOf("</trace>");
        }
    }

    /**
     * @brief Setter method for xmlData
     *
     * @param xmlData
     *     The new value for xmlData
     */
    public void setXMLData (String xmlData) {
        this.reset();

        xmlData_ = xmlData;
    }

    /**
     * @brief Getter method for xmlData
     *
     * @return The xmlData of this parser
     */
    public String getXMLData () {
        return xmlData_;
    }

    /**
     * @brief Getter method for the equation of this parser
     *        The equation is the annotation found under the equationInTeX type.
     *
     * @return The equation of the current xmlData
     */
    public String getEquation () {
        return equation_;
    }

    /**
     * @brief Resets the InkMLParser
     *        By reseting the InkMLParser, the xmlData, the TraceGroup and the equation stored, are
     *        erased.
     */
    public void reset () {
        xmlData_ = "";
        equation_ = "";

        traceGroup_ = new TraceGroup();
    }

    private String xmlData_; //!< The xml data of the InkMLParser

    private String equation_; //!< The equation found as annotation under the equationInTeX type

    public TraceGroup traceGroup_; //!< The TraceGroup parsed from the xml data

}
