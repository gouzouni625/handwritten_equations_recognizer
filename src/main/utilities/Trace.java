package main.utilities;

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

    topLeftCorner_ = new Point(minX, minY);
    bottomRightCorner_ = new Point(maxX, maxY);
  }

  public Point getTopLeftCorner(){
    return (new Point(topLeftCorner_));
  }

  public Point getBottomRightCorner(){
    return (new Point(bottomRightCorner_));
  }

  public double getWidth(){
    return (bottomRightCorner_.x_ - topLeftCorner_.x_);
  }

  public double getHeight(){
    return (bottomRightCorner_.y_ - topLeftCorner_.y_);
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

  private ArrayList<Point> points_;

  private Point topLeftCorner_;
  private Point bottomRightCorner_;

}
