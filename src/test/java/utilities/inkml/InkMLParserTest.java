package test.java.utilities.inkml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import main.java.utilities.inkml.InkMLParser;

import org.junit.Test;

/** @class InkMLParserTest
 *
 *  @brief Class that contains tests for the main.utilities.inkml.InkMLParser class.
 *
 */
public class InkMLParserTest{
  /**
   *  @brief Tests the constructors, setXMLData, getXMLData, getEquation and reset methods of
   *         main.utilities.inkml.InkMLParser class.
   */
  @Test
  public void testInkMLParser(){
    /* Test default constructor. */
    InkMLParser inkMLParser = new InkMLParser();

    assertEquals("", inkMLParser.getXMLData());
    assertEquals("", inkMLParser.getEquation());
    assertEquals(0, inkMLParser.traceGroup_.size(), 0);

    /* Test second constructor. */
    String xmlData = "originalData";
    inkMLParser = new InkMLParser(xmlData);

    assertTrue(inkMLParser.getXMLData().equals(xmlData));

    assertFalse(xmlData == inkMLParser.getXMLData());

    /* Test setXMLData. */
    xmlData = "Hello World!";

    inkMLParser.setXMLData(xmlData);

    assertTrue(inkMLParser.getXMLData().equals(xmlData));

    assertFalse(xmlData == inkMLParser.getXMLData());

    /* Test reset. */
    inkMLParser.reset();

    assertTrue(inkMLParser.getXMLData().equals(""));
  }

  /**
   *  @brief Tests readInkMLFile method of main.utilities.inkml.InkMLParser class.
   *
   *  Example InkML file: \n\n
   *  <ink xmlns="http://www.w3.org/2003/InkML"/>
   *    <definitions>
   *      <canvasTransform xml:id="canvasTransform1">
   *        <mapping type="affine"><affine>80.7289 -35.1351 0, 45.0215 -101.4770 0,</affine></mapping>
   *      </canvasTransform>
   *      <context xml:id="context1" canvasTransformRef="#canvasTransform1"/>
   *    </definitions>
   *
   *    <annotation type="equationInTeX">$y=(3+x)(2+x)$</annotation>
   *
   *    <traceGroup xml:id="traceGroup1" contextRef="#context1">
   *      <trace>-2.02 3.42, -2.16 3.3, -2.22 3.12, -2.28 2.8, -2.24 2.4, -2.04 2.12, -1.76 2.1, -1.46 2.24, -1.28 2.56, -1.24 2.98, -1.24 3.26, -1.24 3.36, -1.3 3.34</trace>
   *      <trace>-1.72 2.12, -1.74 2, -1.74 1.68, -1.7 1.32, -1.66 1, -1.66 0.88</trace>
   *      <trace>-0.68 1.7, -0.56 1.7, -0.32 1.7, -0.08 1.74, 0.08 1.78</trace>
   *      <trace>-0.66 1.14, -0.42 1.18, -0.18 1.22, 0.08 1.24, 0.32 1.28</trace>
   *      <trace>1.64 3.12, 1.48 3.08, 1.2 2.84, 0.92 2.4, 0.8 1.88, 0.84 1.36, 1.06 0.98, 1.28 0.76, 1.52 0.68</trace>
   *      <trace>2 2.56, 2.02 2.66, 2.2 2.72, 2.42 2.72, 2.58 2.64, 2.6 2.42, 2.52 2.2, 2.4 2.1, 2.46 2.08, 2.6 2.02, 2.72 1.82, 2.74 1.56, 2.66 1.3, 2.46 1.18, 2.24 1.18, 2 1.38, 1.94 1.6</trace>
   *      <trace>3.44 1.86, 3.7 1.84, 3.94 1.84, 4.16 1.84, 4.34 1.82</trace>
   *      <trace>3.84 2.44, 3.78 2.4, 3.8 2.12, 3.9 1.78, 3.94 1.52, 3.96 1.42</trace>
   *      <trace>5.32 2.4, 5.24 2.26, 5.1 2.06, 4.84 1.76, 4.66 1.52, 4.54 1.38</trace>
   *      <trace>4.72 2.44, 4.7 2.34, 4.82 2.08, 5.1 1.76, 5.32 1.56, 5.46 1.48</trace>
   *      <trace>5.4 3.32, 5.64 3.02, 5.94 2.7, 6.2 2.44, 6.34 2.12, 6.38 1.72, 6.22 1.28, 5.94 1, 5.74 0.84</trace>
   *      <trace>7.46 3.32, 7.32 3.22, 7.14 2.94, 6.96 2.5, 6.88 2.04, 6.86 1.66, 6.96 1.32, 7.16 1.1, 7.36 0.96, 7.58 0.88</trace>
   *      <trace>7.7 2.3, 7.66 2.44, 7.76 2.74, 8.04 2.94, 8.34 2.98, 8.52 2.86, 8.56 2.44, 8.32 1.98, 8.04 1.74, 7.92 1.66, 8.22 1.64, 8.46 1.66, 8.7 1.66, 8.92 1.52</trace>
   *      <trace>9.38 2.08, 9.8 2.06, 10.16 2.06, 10.4 2.06, 10.5 2.06</trace>
   *      <trace>9.98 2.66, 9.98 2.5, 9.98 2.08, 9.98 1.74, 9.98 1.56, 9.98 1.46</trace>
   *      <trace>11.84 2.48, 11.72 2.4, 11.32 2.12, 11 1.74, 10.8 1.52, 10.68 1.42</trace>
   *      <trace>11.12 2.62, 11.1 2.48, 11.26 2.28, 11.54 1.94, 11.76 1.66, 11.88 1.5, 11.94 1.38</trace>
   *      <trace>12.22 3.48, 12.26 3.36, 12.44 3.16, 12.74 2.92, 13.04 2.62, 13.14 2.18, 13.14 1.7, 12.88 1.24, 12.58 0.96, 12.36 0.86, 12.22 0.78</trace>
   *    </traceGroup>
   *
   *  </ink>
   *
   *  \n\n Make sure to remove the next line characters before using the InkMLParser. The InkMLParser can currently parse
   *  only single line InkML files.
   *
   *  @throws FileNotFoundException In case readInkMLFile throws an exception.
   */
  @Test
  public void testReadInkMLFile() throws FileNotFoundException{
    String realData = "<ink xmlns=\"http://www.w3.org/2003/InkML\"/><definitions><canvasTransform xml:id=\"canvasTransform1\"><mapping type=\"affine\"><affine>80.7289 -35.1351 0, 45.0215 -101.4770 0,</affine></mapping></canvasTransform><context xml:id=\"context1\" canvasTransformRef=\"#canvasTransform1\"/></definitions><annotation type=\"equationInTeX\">$y=(3+x)(2+x)$</annotation><traceGroup xml:id=\"traceGroup1\" contextRef=\"#context1\"><trace>-2.02 3.42, -2.16 3.3, -2.22 3.12, -2.28 2.8, -2.24 2.4, -2.04 2.12, -1.76 2.1, -1.46 2.24, -1.28 2.56, -1.24 2.98, -1.24 3.26, -1.24 3.36, -1.3 3.34</trace><trace>-1.72 2.12, -1.74 2, -1.74 1.68, -1.7 1.32, -1.66 1, -1.66 0.88</trace><trace>-0.68 1.7, -0.56 1.7, -0.32 1.7, -0.08 1.74, 0.08 1.78</trace><trace>-0.66 1.14, -0.42 1.18, -0.18 1.22, 0.08 1.24, 0.32 1.28</trace><trace>1.64 3.12, 1.48 3.08, 1.2 2.84, 0.92 2.4, 0.8 1.88, 0.84 1.36, 1.06 0.98, 1.28 0.76, 1.52 0.68</trace><trace>2 2.56, 2.02 2.66, 2.2 2.72, 2.42 2.72, 2.58 2.64, 2.6 2.42, 2.52 2.2, 2.4 2.1, 2.46 2.08, 2.6 2.02, 2.72 1.82, 2.74 1.56, 2.66 1.3, 2.46 1.18, 2.24 1.18, 2 1.38, 1.94 1.6</trace><trace>3.44 1.86, 3.7 1.84, 3.94 1.84, 4.16 1.84, 4.34 1.82</trace><trace>3.84 2.44, 3.78 2.4, 3.8 2.12, 3.9 1.78, 3.94 1.52, 3.96 1.42</trace><trace>5.32 2.4, 5.24 2.26, 5.1 2.06, 4.84 1.76, 4.66 1.52, 4.54 1.38</trace><trace>4.72 2.44, 4.7 2.34, 4.82 2.08, 5.1 1.76, 5.32 1.56, 5.46 1.48</trace><trace>5.4 3.32, 5.64 3.02, 5.94 2.7, 6.2 2.44, 6.34 2.12, 6.38 1.72, 6.22 1.28, 5.94 1, 5.74 0.84</trace><trace>7.46 3.32, 7.32 3.22, 7.14 2.94, 6.96 2.5, 6.88 2.04, 6.86 1.66, 6.96 1.32, 7.16 1.1, 7.36 0.96, 7.58 0.88</trace><trace>7.7 2.3, 7.66 2.44, 7.76 2.74, 8.04 2.94, 8.34 2.98, 8.52 2.86, 8.56 2.44, 8.32 1.98, 8.04 1.74, 7.92 1.66, 8.22 1.64, 8.46 1.66, 8.7 1.66, 8.92 1.52</trace><trace>9.38 2.08, 9.8 2.06, 10.16 2.06, 10.4 2.06, 10.5 2.06</trace><trace>9.98 2.66, 9.98 2.5, 9.98 2.08, 9.98 1.74, 9.98 1.56, 9.98 1.46</trace><trace>11.84 2.48, 11.72 2.4, 11.32 2.12, 11 1.74, 10.8 1.52, 10.68 1.42</trace><trace>11.12 2.62, 11.1 2.48, 11.26 2.28, 11.54 1.94, 11.76 1.66, 11.88 1.5, 11.94 1.38</trace><trace>12.22 3.48, 12.26 3.36, 12.44 3.16, 12.74 2.92, 13.04 2.62, 13.14 2.18, 13.14 1.7, 12.88 1.24, 12.58 0.96, 12.36 0.86, 12.22 0.78</trace></traceGroup></ink>";

    String xmlData = InkMLParser.readInkMLFile(new File("data/tests/utilities/inkml/InkMLParser/testReadInkMLFile.xml"));

    assertTrue(xmlData.equals(realData));
  }

  /**
   *  @brief Tests parse methods of main.utilities.inkml.InkMLParser class.
   *
   *  @throws FileNotFoundException In case parse method throws an exception.
   */
  @Test
  public void testParse() throws FileNotFoundException{
    InkMLParser inkMLParser = new InkMLParser();

    inkMLParser.parse(new File("data/tests/utilities/inkml/InkMLParser/testReadInkMLFile.xml"));

    assertTrue(inkMLParser.getEquation().equals("$y=(3+x)(2+x)$"));
    assertEquals(18, inkMLParser.traceGroup_.size(), 0);
  }

}
