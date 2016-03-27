package org.hwer.engine.utilities.traces;


import org.hwer.engine.utilities.image_processing.drawing.Drawer;
import org.hwer.engine.utilities.image_processing.image.Image;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * @class Trace
 * @brief Implements a trace of ink as a series of two-dimensional points
 *        An ink trace created on a drawing surface is a series of two-dimensional points which
 *        represent the places where the pen has contacted the drawing surface.
 */
public class Trace implements Iterable<Point> {
    /**
     * @brief Default constructor
     */
    public Trace () {
        points_ = new ArrayList<Point>();

        calculateAll();
    }

    /**
     * @brief Constructor
     *        This constructor can be used to copy a Trace. The new Trace will have the same points
     *        with the Trace given but different Point objects.
     *
     * @param trace
     *     The Trace to be copied
     */
    public Trace (Trace trace) {
        points_ = new ArrayList<Point>();

        for (Point point : trace) {
            points_.add(new Point(point));
        }

        calculateAll();
    }

    /**
     * @brief Returns an iterator over the Points of this Trace
     *
     * @return An iterator over the Points of this Trace
     */
    public Iterator<Point> iterator () {
        return points_.iterator();
    }

    /**
     * @brief Appends a Point to this Trace
     *
     * @param point
     *     The Point to be appended to this Trace
     *
     * @return This Trace so that chain commands are possible (e.g. tr.add(p1).add(p2))
     */
    public Trace add (Point point) {
        points_.add(point);

        calculateAll();

        return this;
    }

    /**
     * @brief Returns the Point at the specified position of this Trace
     *
     * @param index
     *     The position of the Point to be returned
     *
     * @return The Point at the specified position in this Trace
     */
    public Point get (int index) {
        return points_.get(index);
    }

    /**
     * @brief Returns the number of Points in this Trace
     *
     * @return The number of Points in this Trace
     */
    public int size () {
        return points_.size();
    }

    /**
     * @brief Multiplies this Trace with a factor
     *        The multiplication of a Trace with a factor is defined as the multiplication of every
     *        Point inside the Trace with the factor.
     *
     * @param factor
     *     The number to multiply this Trace with
     *
     * @return This Trace so that chain commands are possible (e.g. tr1.multiplyBy(3).multiplyBy(2))
     */
    public Trace multiplyBy (double factor) {
        for (Point point_ : points_) {
            point_.multiplyBy(factor);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Multiplies this Trace with a Point
     *        The multiplication of a Trace with a Point is defined as the element-wise
     *        multiplication of every Point inside the Trace with the Point.
     *
     * @param point
     *     The Point to multiply this Trace with
     *
     * @return This Trace so that chain commands are possible (e.g. tr1.multiplyBy(p1).add(p2))
     */
    public Trace multiplyBy (Point point) {
        for (Point point_ : points_) {
            point_.multiplyBy(point);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Subtracts a Point from this Trace
     *        The subtraction of a Point from a Trace is defined as the subtraction of the Point
     *        from every Point inside the Trace.
     *
     * @param point
     *     The Point to be subtracted from this Trace
     *
     * @return This Trace in order for chain commands to be possible (e.g. tr1.subtract(p1).add(p2))
     */
    public Trace subtract (Point point) {
        for (Point point_ : points_) {
            point_.subtract(point);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Calculates the four corners of this Trace
     *        The corners of a Trace are defined as follows:
     *        Top Left Corner    : minimum abscissa(x), minimum ordinate(y)
     *        Top Right Corner   : maximum abscissa(x), minimum ordinate(y)
     *        Bottom Left Corner : minimum abscissa(x), maximum ordinate(y)
     *        Bottom Right Corner: maximum abscissa(x), maximum ordinate(y)
     */
    private void calculateCorners () {
        if (! calculateCorners_) {
            return;
        }

        double minX = points_.get(0).x_;
        double maxX = points_.get(0).x_;
        double minY = points_.get(0).y_;
        double maxY = points_.get(0).y_;

        for (Point point_ : points_) {
            if (point_.x_ > maxX) {
                maxX = point_.x_;
            }

            if (point_.x_ < minX) {
                minX = point_.x_;
            }

            if (point_.y_ > maxY) {
                maxY = point_.y_;
            }

            if (point_.y_ < minY) {
                minY = point_.y_;
            }
        }

        topLeftCorner_ = new Point(minX, maxY);
        topRightCorner_ = new Point(maxX, maxY);
        bottomLeftCorner_ = new Point(minX, minY);
        bottomRightCorner_ = new Point(maxX, minY);

        calculateCorners_ = false;
    }

    /**
     * @brief Returns the topLeftCorner of this Trace
     *        The Point returned is not the actual Point object in this Trace but a copy of it
     *
     * @return The topLeftCorner of this Trace
     */
    public Point getTopLeftCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(topLeftCorner_));
    }

    /**
     * @brief Returns the bottomRightCorner of this Trace
     *        The Point returned is not the actual Point object in this Trace but a copy of it
     *
     * @return The bottomRightCorner of this Trace
     */
    public Point getBottomRightCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(bottomRightCorner_));
    }

    /**
     * @brief Returns the bottomLeftCorner of this Trace
     *        The Point returned is not the actual Point object in this Trace but a copy of it
     *
     * @return The bottomLeftCorner of this Trace
     */
    public Point getBottomLeftCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(bottomLeftCorner_));
    }

    /**
     * @brief Returns the topRightCorner of this Trace
     *        The Point returned is not the actual Point object in this Trace but a copy of it
     *
     * @return The topRightCorner of this Trace
     */
    public Point getTopRightCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(topRightCorner_));
    }

    /**
     * @brief Returns the width of this Trace
     *        The width of a Trace is defined as the horizontal distance between the outermost left
     *        and the outermost right Point of the Trace
     *
     * @return The width of this Trace
     */
    public double getWidth () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (bottomRightCorner_.x_ - topLeftCorner_.x_);
    }

    /**
     * @brief Returns the height of this Trace
     *        The height of a Trace is defined as the vertical distance between the outermost top
     *        and the outermost bottom Point of the Trace
     *
     * @return The height of this Trace
     */
    public double getHeight () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (topLeftCorner_.y_ - bottomRightCorner_.y_);
    }

    /**
     * @brief Returns the centroid of this Trace
     *        The centroid of a Trace is the center of the rectangle formed by the four corners of
     *        the Trace
     *
     * @return The centroid of this Trace
     */
    public Point getCentroid () {
        if (calculateCorners_) {
            calculateCorners();
        }

        double centroidX = topLeftCorner_.x_ + getWidth() / 2;
        double centroidY = bottomRightCorner_.y_ + getHeight() / 2;

        return (new Point(centroidX, centroidY));
    }

    /**
     * @brief Returns the outermost left Point of this Trace
     *        The outermost left Point of a Trace is the Point with the minimum abscissa. The Point
     *        returned is not the actual Point object in this Trace but a copy of it.
     *
     * @return The outermost left Point of this Trace
     */
    public Point getOuterLeftPoint () {
        if (calculateOuterLeftPoint_) {
            calculateOuterLeftPoint();
        }

        return new Point(outerLeftPoint_);
    }

    /**
     * @brief Calculates the outermost left Point of this Trace
     */
    private void calculateOuterLeftPoint () {
        if (! calculateOuterLeftPoint_) {
            return;
        }

        outerLeftPoint_ = points_.get(0);
        double minX = outerLeftPoint_.x_;
        for (Point point_ : points_) {
            if (point_.x_ < minX) {
                minX = point_.x_;
                outerLeftPoint_ = point_;
            }
        }

        calculateOuterLeftPoint_ = false;
    }

    /**
     * @brief Returns the outermost right Point of this Trace
     *        The outermost right Point of a Trace is the Point with the maximum abscissa. The Point
     *        returned is not the actual Point object in this Trace but a copy of it.
     *
     * @return The outermost right Point of this Trace
     */
    public Point getOuterRightPoint () {
        if (calculateOuterRightPoint_) {
            calculateOuterRightPoint();
        }

        return new Point(outerRightPoint_);
    }

    /**
     * @brief Calculates the outermost right Point of this Trace
     */
    private void calculateOuterRightPoint () {
        if (! calculateOuterRightPoint_) {
            return;
        }

        outerRightPoint_ = points_.get(0);
        double maxX = outerRightPoint_.x_;
        for (Point point_ : points_) {
            if (point_.x_ > maxX) {
                maxX = point_.x_;
                outerRightPoint_ = point_;
            }
        }

        calculateOuterRightPoint_ = false;
    }

    /**
     * @brief Returns the centerOfMass of this Trace
     *        The centerOfMass of a Trace is defined as the Point with abscissa equal to the average
     *        abscissa and ordinate equal to the average ordinate of the Points of the Trace. The
     *        Point returned is not the actual Point object in this Trace but a copy of it.
     *
     * @return The centerOfMass of this Trace
     */
    public Point getCenterOfMass () {
        if (calculateCenterOfMass_) {
            calculateCenterOfMass();
        }

        return new Point(centerOfMass_);
    }

    /**
     * @brief Calculates the centerOfMass of this Trace
     */
    private void calculateCenterOfMass () {
        if (! calculateCenterOfMass_) {
            return;
        }

        centerOfMass_ = new Point(0, 0);
        for (Point point_ : points_) {
            centerOfMass_.add(point_);
        }
        centerOfMass_.divideBy(this.size());

        calculateCenterOfMass_ = false;
    }

    /**
     * @brief Returns an Image with this Trace printed on it
     *        The Image returned is not the actual Image object in this Trace but a copy of it.
     *
     * @param width
     *     The width of the Image
     * @param height
     *     The height of the Image
     *
     * @return An Image with this Trace printed on it
     */
    public Image print (int width, int height) {
        if (drawImage_) {
            drawImage(width, height);
        }

        return new Image(image_);
    }

    /**
     * @brief Draws the Image of this Trace
     *
     * @param width
     *     The width of the Image
     * @param height
     *     The height of the Image
     */
    private void drawImage (int width, int height) {
        if (! drawImage_ && image_.getWidth() == width && image_.getHeight() == height) {
            return;
        }

        image_ = new Image(width, height);

        for (int i = 0, n = points_.size() - 1; i < n; i++) {
            Drawer.drawLine(image_, (int) points_.get(i).x_, (int) (points_.get(i).y_),
                (int) (points_.get(i + 1).x_), (int) (points_.get(i + 1).y_));
        }

        drawImage_ = false;
    }

    /**
     * @brief Returns the InkML representation of this Trace
     *        More information regarding the InkML at https://www.w3.org/TR/InkML/
     *
     * @return The InkML representation of this Trace
     */
    public String toInkMLFormat () {
        if (calculateInkML_) {
            calculateInkML();
        }

        return inkML_;
    }

    /**
     * @brief Calculates the InkML representation of this Trace
     */
    private void calculateInkML () {
        if (! calculateInkML_) {
            return;
        }

        inkML_ = "<trace>";
        for (Point point_ : points_) {
            inkML_ += point_.x_ + " " + point_.y_ + ", ";
        }
        // Remove last comma and the following space.
        inkML_ = inkML_.substring(0, inkML_.length() - 2);
        inkML_ += "</trace>";

        calculateInkML_ = false;
    }

    /**
     * @brief Returns the closest Point of this Trace to the given Point
     *        The Point returned is not the actual Point object in this Trace but a copy of it.
     *
     * @param point
     *     The Point to find the closest to
     */
    public Point closestPoint (Point point) {
        Point closestPoint = points_.get(0);
        double minDistance = Point.distance(closestPoint, point);
        double distance;
        for (Point point_ : points_) {
            distance = Point.distance(point_, point);

            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point_;
            }
        }

        return (new Point(closestPoint));
    }

    /**
     * @brief Returns true if the two given Traces are overlapped
     *        Two Traces are overlapped if the corresponding ink traces are overlapped.
     *
     * @param trace1
     *     The first Trace
     * @param trace2
     *     The second Trace
     *
     * @return True if these two Trace objects are overlapped, false otherwise
     */
    public static boolean areOverlapped (Trace trace1, Trace trace2) {
        Trace trace1Copy = new Trace(trace1);
        Trace trace2Copy = new Trace(trace2);

        TraceGroup traceGroup = new TraceGroup();
        traceGroup.add(trace1Copy);
        traceGroup.add(trace2Copy);

        traceGroup.subtract(new Point(traceGroup.getTopLeftCorner().x_,
            traceGroup.getBottomRightCorner().y_));

        int width = (int) traceGroup.getWidth();
        int height = (int) traceGroup.getHeight();

        Image image1;
        if (trace1Copy.size() == 1) {
            image1 = new Image(width, height);
            Drawer.drawCircle(image1, (int) trace1Copy.get(0).x_,
                (int) trace1Copy.get(0).y_, 10, Drawer.WHITE);
        }
        else {
            image1 = trace1Copy.print(width, height);
        }

        Image image2;
        if (trace2Copy.size() == 1) {
            image2 = new Image(width, height);
            Drawer.drawCircle(image2, (int) trace2Copy.get(0).x_,
                (int) trace2Copy.get(0).y_, 10, Drawer.WHITE);
        }
        else {
            image2 = trace2Copy.print(width, height);
        }

        int white = Drawer.WHITE;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (image1.getPixel(x, y) == white &&
                    ((x - 1 >= 0    && y - 1 >= 0     && image2.getPixel(x - 1, y - 1) == white) ||
                     (x - 1 >= 0                      && image2.getPixel(x - 1, y)     == white) ||
                     (x - 1 >= 0    && y + 1 < height && image2.getPixel(x - 1, y + 1) == white) ||
                     (                 y - 1 >= 0     && image2.getPixel(x, y - 1)     == white) ||
                     (                                   image2.getPixel(x, y)         == white) ||
                     (                 y + 1 < height && image2.getPixel(x, y + 1)     == white) ||
                     (x + 1 < width && y - 1 >= 0     && image2.getPixel(x + 1, y - 1) == white) ||
                     (x + 1 < width                   && image2.getPixel(x + 1, y)     == white) ||
                     (x + 1 < width && y + 1 < height && image2.getPixel(x + 1, y + 1) == white))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @brief Returns the minimum distance between two Traces
     *
     * @param trace1
     *     The first Trace
     * @param trace2
     *     The second Trace
     *
     * @return The minimum distance between two Traces
     */
    public static double minimumDistance (Trace trace1, Trace trace2) {
        Point[] closestPoints = Trace.closestPoints(trace1, trace2);

        return (Point.distance(closestPoints[0], closestPoints[1]));
    }

    /**
     * @brief Returns the two closest Points of Two Traces
     *        The Points returned are not the actual Point objects in the Traces but copies of them.
     *
     * @param trace1
     *     The first Trace
     * @param trace2
     *     The second Trace
     *
     * @return An array with the two closest Point object. The first Point belongs to the first
     *         Trace and the second Point belongs to the second Trace
     */
    public static Point[] closestPoints (Trace trace1, Trace trace2) {
        Point point1 = trace1.get(0);
        Point point2 = trace2.get(0);

        double minDistance = Point.distance(point1, point2);
        double distance;
        for (Point point1_ : trace1) {
            for (Point point2_ : trace2) {
                distance = Point.distance(point1_, point2_);

                if (distance < minDistance) {
                    minDistance = distance;
                    point1 = point1_;
                    point2 = point2_;
                }
            }
        }

        return (new Point[] {new Point(point1), new Point(point2)});
    }

    /**
     * @brief Sets all calculate* flags to true
     */
    private void calculateAll () {
        calculateCorners_ = true;

        calculateOuterLeftPoint_ = true;
        calculateOuterRightPoint_ = true;

        calculateCenterOfMass_ = true;

        drawImage_ = true;
        calculateInkML_ = true;
    }

    private ArrayList<Point> points_; //!< The Points of this Trace

    private Point topLeftCorner_; //!< The top left corner of this Trace
    private Point topRightCorner_; //!< The top right corner of this Trace
    private Point bottomLeftCorner_; //!< The bottom left corner of this Trace
    private Point bottomRightCorner_; //!< The bottom right corner of this Trace
    private boolean calculateCorners_; //!< Flag indicating that the corners should be recalculated

    private Point outerLeftPoint_; //!< The outermost left Point of this Trace
    private boolean calculateOuterLeftPoint_; //!< Flag indicating that the outermost left point
                                              //!< should be recalculated

    private Point outerRightPoint_; //!< The outermost right Point of this Trace
    private boolean calculateOuterRightPoint_; //!< Flag indicating that the outermost right point
                                               //!< should be recalculated

    private Point centerOfMass_; //!< The center of mass of this Trace
    private boolean calculateCenterOfMass_; //!< Flag indicating that the center of mass should be
                                            //!< recalculated

    private Image image_; //!< The image of this Trace
    private boolean drawImage_; //!< Flag indicating that the image should be redrawn

    private String inkML_; //!< The InkML representation of this Trace
    private boolean calculateInkML_; //!< Flag indicating that the InkML representation should be
                                     //!< recalculated

}
