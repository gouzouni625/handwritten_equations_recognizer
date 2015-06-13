package main.utilities;

/**
 * Class that implements a two dimensional point.
 *
 * @author Georgios Ouzounis
 *
 */
public class Point{

  public Point(){
    x_ = 0;
    y_ = 0;
  }

  public Point(double x, double y){
    x_ = x;
    y_ = y;
  }

  // Constructor that will be called to create an identical Point.
  public Point(Point point){
    x_ = point.x_;
    y_ = point.y_;
  }

  public Point multiplyBy(double factor){
    x_ *= factor;
    y_ *= factor;

    return this;
  }

  public Point subtract(Point point){
    x_ -= point.x_;
    y_ -= point.y_;

    return this;
  }

  public static double distance(Point point1, Point point2){
    double distance = Math.sqrt(Math.pow(point1.x_ - point2.x_, 2) + Math.pow(point1.y_ - point2.y_, 2));

    return distance;
  }

  public double x_;
  public double y_;

}
