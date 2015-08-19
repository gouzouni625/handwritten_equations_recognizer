package main.java.utilities.traces;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/** @class Trace
 *
 *  @brief Implements a Trace of Ink.
 *
 *  A Trace of Ink is a sequence of Point objects. They represent the places where a pen has contacted the drawing
 *  surface.
 */
public class Trace{
  /**
   *  @brief Default constructor.
   */
  public Trace(){
    points_ = new ArrayList<Point>();
  }

  /**
   *  @brief Constructor.
   *
   *  This constructor is used to create an identical copy of a Trace.
   *
   *  @param trace The Trace to be copied.
   */
  public Trace(Trace trace){
    points_ = new ArrayList<Point>();

    for(int i = 0;i < trace.size();i++){
      this.add(trace.get(i));
    }
  }

  /**
   *  @brief Adds a Point to this Trace.
   *
   *  It is not the actual Point that is added but a copy of it. That is, if the given as input Point changes, the Point
   *  inside the Trace will not change.
   *
   *  @param point The Point to be added to this Trace.
   *
   *  @return Returns this Trace in order for chain commands to be possible(e.g. tr.add(p1).add(p2);).
   */
  public Trace add(Point point){
    points_.add(new Point(point));

    return this;
  }

  /**
   *  @brief Returns the Point at a specific position in this Trace.
   *
   *  The Point returned is the actual Point that exists inside this Trace. That is, if the returned Point changes, then,
   *  the Point inside the Trace will also change.
   *
   *  @param index The position of the Point to be returned.
   *
   *  @return Returns the Point at the specified position in this Trace.
   */
  public Point get(int index){
    return points_.get(index);
  }

  /**
   *  @brief Returns the number of Point objects in this Trace.
   *
   *  @return Returns the number of Point objects in this Trace.
   */
  public int size(){
    return points_.size();
  }

  /**
   *  @brief Multiplies this Trace with a double.
   *
   *  Multiplying a Trace with a double it to multiply each Point in the Trace with this double.
   *
   *  @param factor The double that this Trace should be multiplied with.
   *
   *  @return Returns this Trace in order for chain commands to be possible(e.g. tr1.multiplyBy(3).multiplyBy(2);).
   */
  public Trace multiplyBy(double factor){
    for(int i = 0;i < points_.size();i++){
      points_.get(i).multiplyBy(factor);
    }

    return this;
  }

  /**
   *  @brief Subtracts a Point from this Trace.
   *
   *  Subtracting a Point from a Trace is to subtract the Point from each Point in the Trace.
   *
   *  @param point The Point to be subtracted from this Trace.
   *
   *  @return Returns this Trace in order for chain commands to be possible(e.g. tr1.multiplyBy(3).subtract(p1);).
   */
  public Trace subtract(Point point){
    for(int i = 0;i < points_.size();i++){
      points_.get(i).subtract(point);
    }

    return this;
  }

  /**
   *  @brief Calculates the corners of this Trace.
   *
   *  Each Trace has four(4) corners.
   *  Top Left Corner    : minimum abscissa(x), minimum ordinate(y).
   *  Top Right Corner   : maximum abscissa(x), minimum ordinate(y).
   *  Bottom Left Corner : minimum abscissa(x), maximum ordinate(y).
   *  Bottom Right Corner: maximum abscissa(x), maximum ordinate(y).
   */
  public void calculateCorners(){
    double minX = points_.get(0).x_;
    double maxX = points_.get(0).x_;
    double minY = points_.get(0).y_;
    double maxY = points_.get(0).y_;

    for(int i = 0;i < points_.size();i++){
      Point point = points_.get(i);

      if(point.x_ > maxX){
        maxX = point.x_;
      }

      if(point.x_ < minX){
        minX = point.x_;
      }

      if(point.y_ > maxY){
        maxY = point.y_;
      }

      if(point.y_ < minY){
        minY = point.y_;
      }
    }

    topLeftCorner_ = new Point(minX, maxY);
    bottomRightCorner_ = new Point(maxX, minY);
  }

  /**
   *  @brief Getter method for topLeftCorner.
   *
   *  The Point returned is not the actual topLeftCorner but a copy of it. That is, if the returned Point is changed,
   *  topLeftCorner will not be changed.
   *
   *  @return Returns the top left corner of this Trace.
   */
  public Point getTopLeftCorner(){
    return (new Point(topLeftCorner_));
  }

  /**
   *  @brief Getter method for bottomRightCorner.
   *
   *  The Point returned is not the actual bottomRightCorner but a copy of it. That is, if the returned Point is changed,
   *  bottomRightCorner will not be changed.
   *
   *  @return Returns the bottom right corner of this Trace.
   */
  public Point getBottomRightCorner(){
    return (new Point(bottomRightCorner_));
  }

  /**
   *  @brief Getter method for bottomLeftCorner.
   *
   *  The Point returned is not the actual bottomLeftCorner but a copy of it. That is, if the returned Point is changed,
   *  bottomLeftCorner will not be changed.
   *
   *  @return Returns the bottom left corner of this Trace.
   */
  public Point getBottomLeftCorner(){
    return (new Point(topLeftCorner_.x_, bottomRightCorner_.y_));
  }

  /**
   *  @brief Getter method for top right corner.
   *
   *  The Point returned is not the actual topRightCorner but a copy of it. That is, if the returned Point is changed,
   *  topRightCorner will not be changed.
   *
   *  @return Returns the top right corner of this Trace.
   */
  public Point getTopRightCorner(){
    return (new Point(bottomRightCorner_.x_, topLeftCorner_.y_));
  }

  /**
   *  @brief Calculates and returns the Point of this Trace with the minimum abscissa(x).
   *
   *  The Point returned is not the actual Point with the minimum abscissa(x) but a copy of it. That is, if the Point
   *  returned is changed, the Point inside the Trace will not be changed.
   *
   *  @return Returns the Point inside this Trace with the minimum abscissa(x).
   */
  public Point getOutterLeftPoint(){
    double minX = points_.get(0).x_;
    int index = 0;
    for(int i = 0;i < points_.size();i++){
      if(points_.get(i).x_ < minX){
        minX = points_.get(i).x_;
        index = i;
      }
    }

    return (new Point(points_.get(index)));
  }

  /**
   *  @brief Calculates and returns the Point of this Trace with the maximum abscissa(x).
   *
   *  The Point returned is not the actual Point with the maximum abscissa(x) but a copy of it. That is, if the Point
   *  returned is changed, the Point inside the Trace will not be changed.
   *
   *  @return Returns the Point inside this Trace with the maximum abscissa(x).
   */
  public Point getOutterRightPoint(){
    double maxX = points_.get(0).x_;
    int index = 0;
    for(int i = 0;i < points_.size();i++){
      if(points_.get(i).x_ > maxX){
        maxX = points_.get(i).x_;
        index = i;
      }
    }

    return (new Point(points_.get(index)));
  }

  /**
   *  @brief Calculates and returns the width of this Trace.
   *
   *  The width is calculated as width = bottomRightCorner.x - topLeftCorner.x .
   *
   *  @return Returns the width of this Trace.
   */
  public double getWidth(){
    return (bottomRightCorner_.x_ - topLeftCorner_.x_);
  }

  /**
   *  @brief Calculates and returns the height of this Trace.
   *
   *  The height is calculated as height = topLeftCorner.y - bottomRightCorner.y .
   *
   *  @return Returns the height of this Trace.
   */
  public double getHeight(){
    return (topLeftCorner_.y_ - bottomRightCorner_.y_);
  }

  /**
   *  @brief Prints this Trace to an image.
   *
   *  Using OpenCV Core.line method, draws this Trace on an image.
   *
   *  @param image The OpenCV Mat that is used as an image.
   *  @param thickness The thickness of the line during the drawing.
   *
   *  @return Returns the image so that the method can be used in chain commands
   *  (e.g. tr2.print(tr1.print(image, thickness1), thickness2);).
   */
  public Mat print(Mat image, int thickness){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    int height = image.rows();

    for(int i = 0;i < points_.size() - 1;i++){
      Core.line(image, new org.opencv.core.Point(points_.get(i).x_, height - points_.get(i).y_),
                       new org.opencv.core.Point(points_.get(i + 1).x_, height - points_.get(i + 1).y_),
                       new Scalar(255, 255, 255), thickness);
    }

    return image;
  }

  /**
   *  @brief Calculates and returns the center of this Trace.
   *
   *  The center is a Point calculated as:
   *  center.x = topLeftCorner.x + traceWidth / 2;
   *  center.y = bottomRightCorner.y + traceHeight / 2;
   *
   *  @return Returns the center of this Trace.
   */
  public Point getCentroid(){
    this.calculateCorners();

    double centroidX = topLeftCorner_.x_ + this.getWidth() / 2;
    double centroidY = bottomRightCorner_.y_ + this.getHeight() / 2;

    return (new Point(centroidX, centroidY));
  }

  /**
   *  @brief Calculates and returns the center of mass of this Trace.
   *
   *  The center of mass is calculated as:
   *  centerOfMass = sum{pointsInTrace} / traceSize;
   *
   *  @return Returns the center of mass of this Trace.
   */
  public Point getCenterOfMass(){
    Point centerOfMass = new Point(0, 0);

    for(Point point : points_){
      centerOfMass.add(point);
    }
    centerOfMass.divideBy(this.size());

    return centerOfMass;
  }

  /**
   *  @brief Checks if two Trace objects are overlapped.
   *
   *  Two Trace object are overlapped if, when printed, their curves are overlapped.
   *
   *  @param trace1 The first Trace.
   *  @param trace2 The second Trace.
   *
   *  @return Returns true if these two Trace objects are overlapped.
   */
  public static boolean areOverlapped(Trace trace1, Trace trace2){
    Trace trace1Copy = new Trace(trace1);
    Trace trace2Copy = new Trace(trace2);
    trace1Copy.multiplyBy(100).calculateCorners();
    trace2Copy.multiplyBy(100).calculateCorners();

    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add(trace1Copy);
    traceGroup.add(trace2Copy);
    traceGroup.calculateCorners();

    trace1Copy.subtract(new Point(traceGroup.getTopLeftCorner().x_, traceGroup.getBottomRightCorner().y_));
    trace2Copy.subtract(new Point(traceGroup.getTopLeftCorner().x_, traceGroup.getBottomRightCorner().y_));
    traceGroup.subtract(new Point(traceGroup.getTopLeftCorner().x_, traceGroup.getBottomRightCorner().y_));

    double width = traceGroup.getWidth();
    if(width < 100){
      width = 100;
    }
    double height = traceGroup.getHeight();
    if(height < 100){
      height = 100;
    }

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    Mat image1 = Mat.zeros(new Size(width, height), CvType.CV_32F);
    Mat image2 = Mat.zeros(new Size(width, height), CvType.CV_32F);

    trace1Copy.print(image1, 1);
    trace2Copy.print(image2, 1);

    int numberOfRows = image1.rows();
    int numberOfColumns = image1.cols();

    for(int i = 0;i < numberOfRows;i++){
      for(int j = 0;j < numberOfColumns;j++){
        if(image1.get(i, j)[0] > 0 &&
           ((i - 1 >= 0 &&           j - 1 >= 0              && image2.get(i - 1, j - 1)[0] > 0) ||
            (i - 1 >= 0 &&                                      image2.get(i - 1, j    )[0] > 0) ||
            (i - 1 >= 0 &&           j + 1 < numberOfColumns && image2.get(i - 1, j + 1)[0] > 0) ||
            (                        j - 1 >= 0              && image2.get(i    , j - 1)[0] > 0) ||
            (                                                   image2.get(i    , j    )[0] > 0) ||
            (                        j + 1 < numberOfColumns && image2.get(i    , j + 1)[0] > 0) ||
            (i + 1 < numberOfRows && j - 1 >= 0              && image2.get(i + 1, j - 1)[0] > 0) ||
            (i + 1 < numberOfRows &&                            image2.get(i + 1, j    )[0] > 0) ||
            (i + 1 < numberOfRows && j + 1 < numberOfColumns && image2.get(i + 1, j + 1)[0] > 0))){
          return true;
        }
      }
    }

    return false;
  }

  /**
   *  @brief Calculates and returns the minimum distance between two Trace objects.
   *
   *  The minimum distance between two Trace objects is calculated as the distance between the closest Point objects of
   *  these Traces.
   *
   *  @param trace1 The first Trace.
   *  @param trace2 The second Trace.
   *
   *  @return Returns the minimum distance between the two Trace objects.
   */
  public static double minimumDistance(Trace trace1, Trace trace2){
    Point[] closestPoints = Trace.closestPoints(trace1, trace2);

    return (Point.distance(closestPoints[0], closestPoints[1]));
  }

  /**
   *  @brief Calculates and returns the two closest Point objects from these two Trace objects.
   *
   *  The Point objects returned are the actual Point objects that are inside the Trace objects. That is, if the returned
   *  Point objects are changed, then, the actual Point objects will also be changed.
   *
   *  @param trace1 The first Trace.
   *  @param trace2 The second Trace.
   *
   *  @return Returns an array with the two closest Point object. The first Point belongs to the first Trace and the
   *  second Point belongs to the second Trace.
   */
  public static Point[] closestPoints(Trace trace1, Trace trace2){
    double min = Point.distance(trace1.get(0), trace2.get(0));
    int index1 = 0;
    int index2 = 0;

    int size1 = trace1.size();
    int size2 = trace2.size();

    for(int i = 0;i < size1;i++){
      for(int j = 0;j < size2;j++){
        double distance = Point.distance(trace1.get(i), trace2.get(j));

        if(distance < min){
          min = distance;
          index1 = i;
          index2 = j;
        }
      }
    }

    return (new Point[] {trace1.get(index1), trace2.get(index2)});
  }

  /**
   *  @brief Calculates and returns the closest point from this Trace to a specific Point.
   *
   *  @param point The Point to find the closest to.
   *
   *  @return Returns the Point of this Trace that is the closest to the specified Point. The Point returned is the actual
   *  Point. That is, if the returned Point is changed, then, the actual Point inside the Trace will also be changed.
   */
  public Point closestPoint(Point point){
    double minDistance = Point.distance(points_.get(0), point);
    int minIndex = 0;
    for(int i = 0;i < points_.size();i++){
      double distance = Point.distance(points_.get(i), point);

      if(distance < minDistance){
        minDistance = distance;
        minIndex = i;
      }
    }

    return (points_.get(minIndex));
  }

  /**
   *  @brief Constructs an InkML representation of this Trace.
   *
   *  @return Returns the InkML representation of this Trace.
   */
  public String toInkMLFormat(){
    String inkMLRepresentation = new String("<trace>");

    for(Point point : points_){
      inkMLRepresentation += point.x_ + " " + point.y_ + ", ";
    }
    // Remove last comma and the following space.
    inkMLRepresentation = inkMLRepresentation.substring(0, inkMLRepresentation.length() - 2);

    inkMLRepresentation += "</trace>";

    return inkMLRepresentation;
  }

  private ArrayList<Point> points_; //!< The Point objects of this Trace.

  private Point topLeftCorner_; //!< The top left corner of this Trace.
  private Point bottomRightCorner_; //!< The bottom right corner of this Trace.

}
