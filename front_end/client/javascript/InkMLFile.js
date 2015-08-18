/** @class InkMLFile
 *
 *  @brier A class representing an InkML file.
 *
 *  For more information about InkML, please visit
 *  http://www.w3.org/TR/InkML/
 */

/** 
 *  @brief Default Constructor.
 */
function InkMLFile(){
  this.definitions = "<definitions>";
  this.annotations = "";
  this.mainBody = "";


  this.canvasTransformCounter = 0;
  this.contextCounter = 0;
  this.traceGroupCounter = 0;
}

/** 
 *  @brief Adds a canvasTransform tag to the file.
 *
 *  @param affineMatrix A string containing
 */
InkMLFile.prototype.addCanvasTransform = function(affineMatrix){
  this.canvasTransformCounter++;

  var temp = affineMatrix.split(",");
  affineTransform = temp[0] + " 0," + temp[1] + " 0,";

  this.definitions = this.definitions + this.canvasTransformText(affineTransform);
  this.addContext("canvasTransformRef=\"#canvasTransform" + this.canvasTransformCounter + "\"");
};

/** 
 *  @brief Creates the canvasTransform tag text.
 *
 *  The canvasTransform tag includes an affine transformation that accompanies
 *  each GeoGebra's pen stroke.
 *
 *  @param affineTransform A string containing the values of the affine transformation(affine matrix and translation
 *                         vector).
 */
InkMLFile.prototype.canvasTransformText = function(affineTransform){
  return "<canvasTransform xml:id=\"canvasTransform" +
         this.canvasTransformCounter + "\"><mapping type=\"affine\"><affine>" +
         affineTransform + "</affine></mapping></canvasTransform>";
};

/** 
 *  @brief Adds a context tag to the file.
 *
 *  @param parameters A string containing the canvasTransform that the context will reference.
 */
InkMLFile.prototype.addContext = function(parameters){
  this.contextCounter++;
  this.definitions = this.definitions + this.contextText(parameters);
};

/** 
 *  @brief Creates the context tag text.
 *
 *  The context tag includes references to a canvasTransform tag. Each traceGroup is assosiated with a context and
 *  thus with the respective canvasTransform.
 *
 *  @param parameters A string containing the canvasTransform reference.
 */
InkMLFile.prototype.contextText = function(parameters){
  return "<context xml:id=\"context" + this.contextCounter +
         "\" " + parameters + "/>"; 
};

/* 
 * @brief Adds a new trace group to the file.
 *
 * Each trace group corresponds to one GeoGebra's pen stroke.
 *
 * @param points An array of strings representing the two-dimentional points of a stroke.
 */
InkMLFile.prototype.addTraceGroup = function(points){
  this.traceGroupCounter++;
  var traceGroup = "<traceGroup xml:id=\"traceGroup" + this.traceGroupCounter + "\" contextRef=\"#context" +
                   this.contextCounter + "\">";

  var traceTag = false;

  for(i = 0;i < points.length;i++){
    if(points[i] == "?, ?"){
      traceGroup = traceGroup + "</trace>";
      traceTag = false;
      continue;
    }

    if(!traceTag){
      traceGroup = traceGroup + "<trace>";
      traceTag = true;
    }
    else{
      traceGroup = traceGroup + ", ";
    }

    var point = points[i].split(", ");
    traceGroup = traceGroup + point[0] + " " + point[1];
  }

  if(traceGroup.substring(traceGroup.length - ("</trace>").length,
                          traceGroup.length) != "</trace>"){
    traceGroup = traceGroup + "</trace>";
  }
  traceGroup = traceGroup + "</traceGroup>";
  this.mainBody = this.mainBody + traceGroup;
};

/** 
 *  @brief Adds an annotation to the file.
 *
 *  The ground truth symbols of each equation is stored in TeX format as an annotation. Each file represents one
 *  equation and thus has only one annotation.
 *
 *  \param equation A string containing the equation in TeX format.
 */
InkMLFile.prototype.addAnnotation = function(equation){
  this.annotations = this.annotations + "<annotation type=\"equationInTeX\">" +
                                       "$" + equation + "$" + "</annotation>";
};

/** 
 *  @brief Assembles all the parts to create the final inkML file.
 *
 *  @return A string containing the final inkML file.
 */
InkMLFile.prototype.getFileToString = function(){
  var result = "<ink xmlns=\"http://www.w3.org/2003/InkML\"/>";
  result = result + this.definitions + "</definitions>";
  result = result + this.annotations;
  result = result + this.mainBody;
  result = result + "</ink>";
  return result;
};

/** An inkML file example.
 *
 *  <ink xmlns="http://www.w3.org/2003/InkML">
 *    <definitions>
 *  
 *      <canvasTransform xml:id="canvasTransform1">
 *        <mapping type="affine">
 *          <affine>0 -1 0, 1 0 0,</affine>
 *        </mapping>
 *      </canvasTransform>
 *  
 *      <context xml:id="context1"
 *        canvasTransformRef="#canvasTransform1"/>
 *
 *      <canvasTransform xml:id="canvasTransform2">
 *        <mapping type="affine">
 *          <affine>2 -1 0, 1 4 0,</affine>
 *        </mapping>
 *      </canvasTransform>
 *  
 *      <context xml:id="context2"
 *        canvasTransformRef="#canvasTransform2"/>
 *  
 *    </definitions>
 *  
 *    <annotation type="equationInTeX">$\frac{\sqrt{a^2 + b^2}}{2}$</annotation>
 *  
 *    <traceGroup contextRef="#context1">
 *      <trace>1 2, 3 6, 44 55, 3 4, 5 4</trace>
 *      <trace>4 4, 6 7, 8, 2</trace>
 *    </traceGroup>
 * 
 *    <traceGroup contextRef="#context22">
 *      <trace>1 2, 3 4, 5 6, 13 5, 3 8, 7 8, 2 2</trace>
 *    </traceGroup>
 *  </ink>
 */
