/** @class StrokeParser
 *
 *  @brief A class designed to manipuate pen strokes received from GeoGebra's applet.
 *
 *  A pen stroke is a sequence of two-dimentional points representing the points the pen went over.
 */

/** 
 *  @brief Defautl Constructor.
 */
function StrokeParser(){
}

/** 
 *  @brief Clears unneeded information in a stroke.
 *
 *  @param stroke A string returned from GeoGebra's getCommandString() function.
 *
 *  @return Returns a string containing only the two-dimentional points which describe the stroke.
 */
StrokeParser.prototype.escapeStroke = function(stroke){
  // Remove "AttachCopyToView[Polyline[" substring.
  // Does the same as:
  //   var start = (("AttachCopyToView[Polyline[").length;
  //   var end = stroke.length;
  //   currentStroke = currentStroke.substring(start, end);
  stroke = stroke.substring(stroke.indexOf("("), stroke.length);

  // Remove "true], 1, " substring.
  stroke = stroke.replace("true], 1, ", "");

  // Remove ']' at the end.
  stroke = stroke.substring(0, stroke.length - 1);

  return stroke;
};

