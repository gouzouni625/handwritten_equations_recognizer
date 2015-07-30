package main.utilities.traces;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/**
 * Class that implements a trace as a list of two dimensional points.
 *
 * @author Georgios Ouzounis.
 *
 */
public class Trace{

  public Trace(){
    points_ = new ArrayList<Point>();
  }

  // Constructor that will be called to create an identical Trace.
  public Trace(Trace trace){
    points_ = new ArrayList<Point>();

    for(int i = 0;i < trace.size();i++){
      this.add(trace.get(i));
    }
  }

  public void add(Point point){
    points_.add(new Point(point));
  }

  public Point get(int index){
    return points_.get(index);
  }

  public int size(){
    return points_.size();
  }

  public Trace multiplyBy(double factor){
    for(int i = 0;i < points_.size();i++){
      points_.get(i).multiplyBy(factor);
    }

    return this;
  }

  public Trace subtract(Point point){
    for(int i = 0;i < points_.size();i++){
      points_.get(i).subtract(point);
    }

    return this;
  }

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

  public Point getTopLeftCorner(){
    return (new Point(topLeftCorner_));
  }

  public Point getBottomRightCorner(){
    return (new Point(bottomRightCorner_));
  }

  public Point getBottomLeftCorner(){
    return (new Point(topLeftCorner_.x_, bottomRightCorner_.y_));
  }

  public Point getTopRightCorner(){
    return (new Point(bottomRightCorner_.x_, topLeftCorner_.y_));
  }

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

  public double getWidth(){
    return (bottomRightCorner_.x_ - topLeftCorner_.x_);
  }

  public double getHeight(){
    return (topLeftCorner_.y_ - bottomRightCorner_.y_);
  }

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

  public Point getCentroid(){
    this.calculateCorners();

    double centroidX = topLeftCorner_.x_ + this.getWidth() / 2;
    double centroidY = bottomRightCorner_.y_ + this.getHeight() / 2;

    return (new Point(centroidX, centroidY));
  }

  public Point getCenterOfMass(){
    Point centerOfMass = new Point(0, 0);

    for(Point point : points_){
      centerOfMass.add(point);
    }
    centerOfMass.divideBy(this.size());

    return centerOfMass;
  }

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
        if(image1.get(i, j)[0] > 0 && ((i - 1 >= 0 &&           j - 1 >= 0              && image2.get(i - 1, j - 1)[0] > 0) ||
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

  // TODO
  // write tests.
  public static double minimumDistance(Trace trace1, Trace trace2){
    double min = Point.distance(trace1.get(0), trace2.get(0));

    int size1 = trace1.size();
    int size2 = trace2.size();

    for(int i = 0;i < size1;i++){
      for(int j = 0;j < size2;j++){
        double distance = Point.distance(trace1.get(i), trace2.get(j));

        if(distance < min){
          min = distance;
        }
      }
    }

    return min;
  }

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

  private ArrayList<Point> points_;

  private Point topLeftCorner_;
  private Point bottomRightCorner_;

  public static double COMPARISON_THRESHOLD = 0.5;

  public static int NUMBER_OF_DECIMAL_DIGITS = 2;

}
