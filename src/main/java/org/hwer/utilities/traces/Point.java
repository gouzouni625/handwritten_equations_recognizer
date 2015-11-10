package org.hwer.utilities.traces;

/** @class Point
 *
 *  @brief Implements a two dimensional Point.
 *
 *  Along with the Point's data, some useful methods are implemented.
 */
public class Point{
  /**
   *  @brief Default constructor.
   *
   *   Initializes point's data to zero.
   */
  public Point(){
    x_ = 0;
    y_ = 0;
  }

  /**
   *  @brief Constructor.
   *
   *  @param x Initial value of the point's abscissa.
   *  @param y Initial value of the point's ordinate.
   */
  public Point(double x, double y){
    x_ = x;
    y_ = y;
  }

  /**
   *  @brief Constructor.
   *
   *  This constructor is used to create an identical copy of a Point.
   *
   *  @param point The Point to be copied.
   */
  public Point(Point point){
    x_ = point.x_;
    y_ = point.y_;
  }

  /**
   *  @brief Adds a Point to this Point.
   *
   *  The addition is the typical mathematical addition. That is, if P1(x1, y1) and P2(x2, y2) are
   *  two points in mathematical notation, then, the result of adding these two, is a point
   *  P3(x1 + x2, y1 + y2).
   *
   *  @param point The Point to add to this point.
   *
   *  @return Returns this Point in order for chain commands to be
   *  possible(e.g. p1.add(p2).add(p3)).
   */
  public Point add(Point point){
    x_ += point.x_;
    y_ += point.y_;

    return this;
  }

  /**
   *  @brief Multiplies this Point with a double.
   *
   *  The multiplication is the typical mathematical multiplication. That is, if P1(x1, y1) is a
   *  point in mathematical notation and d is a double, then, the result of multiplying these two,
   *  is a point P2(d*x1, d*y1).
   *
   *  @param factor The double that this Point should be multiplied with.
   *
   *  @return Returns this Point in order for chain commands to be
   *  possible(e.g. p1.multiplyBy(3).add(p2);).
   */
  public Point multiplyBy(double factor){
    x_ *= factor;
    y_ *= factor;

    return this;
  }

  /**
   *  @brief Divides this Point by a double.
   *
   *  The division is the typical mathematical division. That is, if P1(x1, y1) is a point in
   *  mathematical notation and d is a double, then, the result of dividing P1 by d is a point
   *  P2(x1 /d, y1 / d). In case where d == 0 an ArithmeticException is thrown with the message
   *  "/ by zero.". by Java by throwing an appropriate exception.
   *
   *  @param factor The double that this Point should be divided by.
   *
   *  @return Returns this Point in order for chain commands to be
   *          possible(e.g. p1.divideBy(3).add(p2);).
   */
  public Point divideBy(double factor){
    if(factor == 0){
      throw (new ArithmeticException("/ by zero"));
    }

    x_ /= factor;
    y_ /= factor;

    return this;
  }

  /**
   *  @brief Subtracts a Point from this Point.
   *
   *  The subtraction is the typical mathematical point - point subtraction. That is, if
   *  P1(x1, y1) and P2(x2, y2) are two points in mathematical notation, then, the result of
   *  subtracting P2 from P1 is a point P3(x1 - x2, y1 - y2).
   *
   *  @param point The Point to be subtracted from this Point.
   *
   *  @return Returns this Point in order for chain commands to be
   *          possible(e.g. p1.subtract(p2).multiplyBy(5);).
   */
  public Point subtract(Point point){
    x_ -= point.x_;
    y_ -= point.y_;

    return this;
  }

  /**
   *  @brief Calculates the distance between two Points.
   *
   *  The distance is the Euclidean distance of the two Points. That is, if P1(x1, y1) and
   *  P2(x2, y2) are two points in mathematical notation, then, their Euclidean distance d,
   *  is calculated as d = ((x1 - x2) ^ 2 + (y1 - y2) ^ 2) ^ (1 / 2).
   *
   *  @param point1 The first Point.
   *  @param point2 The second Point.
   *
   *  @return Returns the distance of the two Points.
   */
  public static double distance(Point point1, Point point2){
    double distance = Math.sqrt(Math.pow(point1.x_ - point2.x_, 2) +
                                Math.pow(point1.y_ - point2.y_, 2));

    return distance;
  }

  public double x_; //!< The abscissa of this Point.
  public double y_; //!< The ordinate of this Point.

}
