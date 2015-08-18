/** 
 *  @brief Calculates the affine transformation matrix that maps the first two two-dimentional points to the last two.
 *
 *  @param affinePoints A string array containing the four two-dimentional points. Each string is has the format "x, y".
 *
 *  @return Returns a string containing the affine matrix (i.e. if the affine matrix is "M = [1, 2;3, 4]" then
 *          affineMatrix = "1 2, 3 4". 
 */
function affinePointsToAffineMatrix(affinePoints){
  var affineMatrix = "";
  var x = [[0, 0], [0, 0]]; //!< Abscissa of each point.
  var y = [[0, 0], [0, 0]]; //!< Ordinate of each point.
  var m = [[0, 0], [0, 0]]; //!< Affine matrix.

  for(var i = 0;i < 2;i++){
    for(var j = 0;j < 2;j++){
      var coordinates = affinePoints[i * 2 + j].split(", ");
      x[i][j] = parseFloat(coordinates[0]);
      y[i][j] = parseFloat(coordinates[1]);
    }
  }

  // The following equations are derived from the analytical solution of the
  // following system of equations:
  // x10 = m00 * x00 + m01 * y00
  // y10 = m10 * x00 + m11 * y00
  //
  // x11 = m00 * x01 + m01 * y01
  // y11 = m10 * x01 + m11 * y01
  //
  // where [x00, y00] and [x01, y01] are the two points that get mapped to
  // [x10, y10] and [x11, y11] respectively, through the affine transformation
  // described by matrix [m00, m01; m10, m11] and zero translation.
  var denominator = x[0][0] * y[0][1] - x[0][1] * y[0][0];
  m[0][0] = (x[1][0] * y[0][1] - x[1][1] * y[0][0]) / denominator;
  m[0][1] = (x[0][0] * x[1][1] - x[0][1] * x[1][0]) / denominator;
  m[1][0] = (y[0][1] * y[1][0] - y[0][0] * y[1][1]) / denominator;
  m[1][1] = (x[0][0] * y[1][1] - x[0][1] * y[1][0]) / denominator;

  // Keep only four decimal digits for each matrix entry.
  affineMatrix = m[0][0].toFixed(4) + " " + m[0][1].toFixed(4) + ", " +
                 m[1][0].toFixed(4) + " " + m[1][1].toFixed(4) + ",";

  return affineMatrix;
}

/** 
 *  @brief Resets the ggbApplet by calling its reset method.
 */
function resetGGBApplet(){
  window.ggbApplet.reset();

  // ggbOnInit function is not called after the reset method, so call it explicitly.
  window.ggbOnInit();

  // Remove focus from all the buttons to avoid pressing them by pressing enter to submit an equation.
  document.getElementById("reset_applet_button").blur();
  document.getElementById("submit_button").blur();
}

/** 
 *  @brief Gets the strokes' data from the ggbApplet, creates the inkML file and prints it to the output tag.
 */
function submitButtonClickListener(){
  var ggbApplet = window.ggbApplet;
  var equation = window.equation;

  if(ggbApplet.getObjectNumber() == 0){
    return; 
  }

  var inkMLFile = new InkMLFile();
  var strokeParser = new StrokeParser();

  var currentStroke = "";

  for(var i = 0;i < ggbApplet.getObjectNumber();i++){
    currentStroke = ggbApplet.getCommandString(ggbApplet.getObjectName(i));

    // Escape command letters from current stroke.
    currentStroke = strokeParser.escapeStroke(currentStroke);

    // Split current stroke to points.
    currentStroke = currentStroke.replace(/\(/g, "");
    currentStroke = currentStroke.replace(/\)/g, " ");
    var points = currentStroke.split(" , ");

    var affineMatrix = affinePointsToAffineMatrix(points.slice(points.length -
                                                  4, points.length));

    points = points.slice(0, points.length - 4);

    inkMLFile.addCanvasTransform(affineMatrix);
    inkMLFile.addTraceGroup(points);
  }

  inkMLFile.addAnnotation(equation);
  var finalInkMLFile = inkMLFile.getFileToString();

  document.getElementById("inkml_output").value = finalInkMLFile;
}

/** 
 *  @brief Processes the given input if the key "enter" is pressed.
 *
 *  @param event The event created by the key press.
 */
function ggbAppletKeyUpListener(event){
  if(event.keyCode == 13){ // key->enter, code->13
    submitButtonClickListener();
  }
}

