package org.hwer.engine.utilities.traces;


/**
 * @class Point
 * @brief Implements a two dimensional point
 *
 * Along with the point's data, some useful methods are implemented.
 */
public class Point {
    /**
     * @brief Default constructor
     *        Initializes Point's data to zero.
     */
    public Point () {
        x_ = 0;
        y_ = 0;
    }

    /**
     * @brief Constructor
     *
     * @param x
     *     Initial value of the Point's abscissa
     * @param y
     *     Initial value of the Point's ordinate
     */
    public Point (double x, double y) {
        x_ = x;
        y_ = y;
    }

    /**
     * @brief Constructor
     *        This constructor can be used to copy a Point. The new Point will have the same
     *        abscissa and ordinate with the Point given but will be a different object.
     *
     * @param point
     *     The Point to be copied
     */
    public Point (Point point) {
        x_ = point.x_;
        y_ = point.y_;
    }

    /**
     * @brief Adds a Point to this Point
     *        This method performs the mathematical addition of two two-dimensional points. That is,
     *        P3 = P1 + P2 = (x1 + x2, y1 + y2) where P1 = (x1, y1) and P2 = (x2, y2).
     *
     * @param point
     *     The Point to add to this point
     *
     * @return This Point so that chain commands are possible (e.g. p1.add(p2).add(p3))
     */
    public Point add (Point point) {
        x_ += point.x_;
        y_ += point.y_;

        return this;
    }

    /**
     * @brief Multiplies this Point with a factor
     *        This method performs the mathematical multiplication of a two-dimensional point with a
     *        floating point number. That is, P2 = a * P1 = (a * x1, a * y1) where P1 = (x1, y1) and
     *        a is the floating point number.
     *
     * @param factor
     *     The number to multiply this Point with
     *
     * @return This Point so that chain commands are possible (e.g. p1.multiplyBy(3).add(p2))
     */
    public Point multiplyBy (double factor) {
        x_ *= factor;
        y_ *= factor;

        return this;
    }

    /**
     * @brief Multiplies, element-wise, this Point with another Point
     *        This method performs the element-wise multiplication of two Points. That is,
     *        P3 = P1 .* P2 = (x1 * x2, y1 * y2) where P1 = (x1, y1) and P2 = (x2, y2).
     *
     * @param point
     *     The Point to multiply this Point with
     *
     * @return This Point so that chain command are possible (e.g. p1.multiplyBy(p2).add(p3))
     */
    public Point multiplyBy (Point point) {
        x_ *= point.x_;
        y_ *= point.y_;

        return this;
    }

    /**
     * @brief Divides this Point with by factor
     *        This method performs the mathematical division of a two-dimensional point by a
     *        floating point number. That is, P2 = P1 / a = (x1 / a, y1 / a) where P1 = (x1, y1) and
     *        a is a floating point number different from zero.
     *
     * @param factor
     *     The number to divide this Point by
     *
     * @return This Point so that chain commands are possible (e.g. p1.divideBy(3).add(p2))
     *
     * @throws ArithmeticException If the specified factor is equal to zero
     */
    public Point divideBy (double factor) {
        if (factor == 0) {
            throw (new ArithmeticException("/ by zero"));
        }

        x_ /= factor;
        y_ /= factor;

        return this;
    }

    /**
     * @brief Subtracts a Point from this Point
     *        This method performs the mathematical subtraction of two two-dimensional points. That
     *        is, P3 = P1 - P2 = (x1 - x2, y1 - y2) where P1 = (x1, y1) and P2 = (x2, y2).
     *
     * @param point
     *     The Point to be subtracted from this Point
     *
     * @return This Point so that chain commands are possible(e.g. p1.subtract(p2).add(p3))
     */
    public Point subtract (Point point) {
        x_ -= point.x_;
        y_ -= point.y_;

        return this;
    }

    /**
     * @brief Calculates the distance between two Points
     *        This method calculates the Euclidean distance between two two-dimensional points. That
     *        is, d = distance(P1, P2) = sqrt((x1 - x2) ^ 2 + (y1 - y2) ^ 2) where P1 = (x1, y1) and
     *        P2 = (x2, y2).
     *
     * @param point1
     *     The first Point
     * @param point2
     *     The second Point
     *
     * @return The Euclidean between the given Points
     */
    public static double distance (Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.x_ - point2.x_, 2) +
            Math.pow(point1.y_ - point2.y_, 2));
    }

    public double x_; //!< The abscissa of this Point
    public double y_; //!< The ordinate of this Point

}
