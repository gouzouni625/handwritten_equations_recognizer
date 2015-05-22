package main.utilities;

public class Point{
  public Point(){
    x_ = 0;
    y_ = 0;
  }

  public Point(double x, double y){
    x_ = x;
    y_ = y;
  }

  public Point multiplyBy(double factor){
    x_ *= factor;
    y_ *= factor;

    return this;
  }

  public Point substract(double factor){
    x_ -= factor;
    y_ -= factor;

    // Return yourself so that expressions line
    // point.substract(1).substract(2)... are possible.
    return this;
  }

  public Point substract(Point point){
    x_ -= point.x_;
    y_ -= point.y_;

    return this;
  }

  public double x_;
  public double y_;
}