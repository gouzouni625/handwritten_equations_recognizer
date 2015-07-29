package main.utilities.traces;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

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
    trace1 = new Trace(trace1).multiplyBy(Math.pow(10, NUMBER_OF_DECIMAL_DIGITS));
    trace2 = new Trace(trace2).multiplyBy(Math.pow(10, NUMBER_OF_DECIMAL_DIGITS));

    for(int i = 0;i < trace1.size() - 1;i++){
      Point p1 = trace1.get(i);
      Point p2 = trace1.get(i + 1);

      for(int j = 0;j < trace2.size() - 1;j++){
        Point p3 = trace2.get(j);
        Point p4 = trace2.get(j + 1);

        if(Math.abs(p2.x_ - p1.x_) < COMPARISON_THRESHOLD){
          // Line 1 is vertical.

          if(Math.abs(p4.x_ - p3.x_) < COMPARISON_THRESHOLD){
            // Line 1 and line 2 are vertical.

            if(Math.abs((p1.x_ + p2.x_) / 2 - (p3.x_ + p4.x_) / 2) < COMPARISON_THRESHOLD &&
               ((Math.max(p1.y_, p2.y_) <= Math.max(p3.y_, p4.y_) && Math.max(p1.y_, p2.y_) >= Math.min(p3.y_, p4.y_)) ||
                (Math.max(p3.y_, p4.y_) <= Math.max(p1.y_, p2.y_) && Math.max(p3.y_, p4.y_) >= Math.min(p1.y_, p2.y_)))){
              return true;
            }
            else{
              continue;
            }
          }
          else{
            // Line 1 is vertical but line 2 is not.
            // This means that there will definitely be an intersection point.
            double l34 = (p4.y_ - p3.y_) / (p4.x_ - p3.x_);

            Point intersection = new Point(0, 0);
            intersection.x_ = p1.x_;
            intersection.y_ = p4.y_ + l34 * (intersection.x_ - p4.x_);

            if(Math.min(p1.y_, p2.y_) <= intersection.y_ && intersection.y_ <= Math.max(p1.y_, p2.y_) &&
               Math.min(p3.x_, p4.x_) <= intersection.x_ && intersection.x_ <= Math.max(p3.x_, p4.x_) &&
               Math.min(p3.y_, p4.y_) <= intersection.y_ && intersection.y_ <= Math.max(p3.y_, p4.y_)){
              return true;
             }
          }
        }
        else{
          // Line 1 is not vertical

          if(Math.abs(p4.x_ - p3.x_) < COMPARISON_THRESHOLD){
            // Line 1 is not vertical but line 2 is.

            double l12 = (p2.y_ - p1.y_) / (p2.x_ - p1.x_);

            Point intersection = new Point(0, 0);
            intersection.x_ = p3.x_;
            intersection.y_ = p2.y_ + l12 * (intersection.x_ - p2.x_);

            if(Math.min(p1.x_, p2.x_) <= intersection.x_ && intersection.x_ <= Math.max(p1.x_, p2.x_) &&
               Math.min(p1.y_, p2.y_) <= intersection.y_ && intersection.y_ <= Math.max(p1.y_, p2.y_) &&
               Math.min(p3.y_, p4.y_) <= intersection.y_ && intersection.y_ <= Math.max(p3.y_, p4.y_)){
              return true;
             }
          }
          else{
            // Neither of line 1 or line 2 is vertical.

            double l12 = (p2.y_ - p1.y_) / (p2.x_ - p1.x_);
            double l34 = (p4.y_ - p3.y_) / (p4.x_ - p3.x_);

            if(Math.abs(l12 - l34) < COMPARISON_THRESHOLD){
              // The two lines are parallel so they do not overlap.
              continue;
            }

            Point intersection = new Point(0, 0);
            intersection.x_ = ((p4.y_ - l34 * p4.x_) - (p2.y_ - l12 * p2.x_)) / (l12 - l34);
            intersection.y_ = p2.y_ + l12 * (intersection.x_ - p2.x_);

            // If the intersection point belongs to both lines, the then lines overlap.
            if(Math.min(p1.x_, p2.x_) <= intersection.x_ && intersection.x_ <= Math.max(p1.x_, p2.x_) &&
               Math.min(p1.y_, p2.y_) <= intersection.y_ && intersection.y_ <= Math.max(p1.y_, p2.y_) &&
               Math.min(p3.x_, p4.x_) <= intersection.x_ && intersection.x_ <= Math.max(p3.x_, p4.x_) &&
               Math.min(p3.y_, p4.y_) <= intersection.y_ && intersection.y_ <= Math.max(p3.y_, p4.y_)){
              return true;
            }
          }
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
