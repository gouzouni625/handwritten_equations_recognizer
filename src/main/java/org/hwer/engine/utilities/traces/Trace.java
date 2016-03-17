package org.hwer.engine.utilities.traces;

import org.hwer.engine.utilities.image_processing.drawing.Drawer;
import org.hwer.engine.utilities.image_processing.image.Image;

import java.util.ArrayList;

/**
 * @class Trace
 * @brief Implements a Trace of Ink.
 * <p>
 * A Trace of Ink is a sequence of Point objects. They represent the places where a pen has
 * contacted the drawing surface.
 */
public class Trace {
    /**
     * @brief Default constructor.
     */
    public Trace () {
        points_ = new ArrayList<Point>();

        calculateAll();
    }

    /**
     * @param trace The Trace to be copied.
     * @brief Constructor.
     * <p>
     * This constructor is used to create an identical copy of a Trace.
     */
    public Trace (Trace trace) {
        points_ = new ArrayList<Point>();

        for (int i = 0; i < trace.size(); i++) {
            points_.add(new Point(trace.get(i)));
        }

        calculateAll();
    }

    /**
     * @param point The Point to be added to this Trace.
     * @return Returns this Trace in order for chain commands to be
     * possible(e.g. tr.add(p1).add(p2);).
     * @brief Adds a Point to this Trace.
     * <p>
     * It is not the actual Point that is added but a copy of it. That is, if the given as
     * input Point changes, the Point inside the Trace will not change.
     */
    public Trace add (Point point) {
        points_.add(point);

        calculateAll();

        return this;
    }

    /**
     * @param index The position of the Point to be returned.
     * @return Returns the Point at the specified position in this Trace.
     * @brief Returns the Point at a specific position in this Trace.
     * <p>
     * The Point returned is the actual Point that exists inside this Trace.
     * That is, if the returned Point changes, then, the Point inside the Trace will also change.
     */
    public Point get (int index) {
        return points_.get(index);
    }

    /**
     * @return Returns the number of Point objects in this Trace.
     * @brief Returns the number of Point objects in this Trace.
     */
    public int size () {
        return points_.size();
    }

    /**
     * @param factor The double that this Trace should be multiplied with.
     * @return Returns this Trace in order for chain commands to be
     * possible(e.g. tr1.multiplyBy(3).multiplyBy(2);).
     * @brief Multiplies this Trace with a double.
     * <p>
     * Multiplying a Trace with a double it to multiply each Point in the Trace with this double.
     */
    public Trace multiplyBy (double factor) {
        for (int i = 0; i < points_.size(); i++) {
            points_.get(i).multiplyBy(factor);
        }

        calculateAll();

        return this;
    }

    public Trace multiplyBy (Point point) {
        for (int i = 0; i < points_.size(); i++) {
            points_.get(i).multiplyBy(point);
        }

        calculateAll();

        return this;
    }

    /**
     * @param point The Point to be subtracted from this Trace.
     * @return Returns this Trace in order for chain commands to be
     * possible(e.g. tr1.multiplyBy(3).subtract(p1);).
     * @brief Subtracts a Point from this Trace.
     * <p>
     * Subtracting a Point from a Trace is to subtract the Point from each Point in the Trace.
     */
    public Trace subtract (Point point) {
        for (int i = 0; i < points_.size(); i++) {
            points_.get(i).subtract(point);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Calculates the corners of this Trace.
     * <p>
     * Each Trace has four(4) corners.
     * Top Left Corner    : minimum abscissa(x), minimum ordinate(y).
     * Top Right Corner   : maximum abscissa(x), minimum ordinate(y).
     * Bottom Left Corner : minimum abscissa(x), maximum ordinate(y).
     * Bottom Right Corner: maximum abscissa(x), maximum ordinate(y).
     */
    private void calculateCorners () {
        if(! calculateCorners_){
            return;
        }

        double minX = points_.get(0).x_;
        double maxX = points_.get(0).x_;
        double minY = points_.get(0).y_;
        double maxY = points_.get(0).y_;

        for (int i = 0; i < points_.size(); i++) {
            Point point = points_.get(i);

            if (point.x_ > maxX) {
                maxX = point.x_;
            }

            if (point.x_ < minX) {
                minX = point.x_;
            }

            if (point.y_ > maxY) {
                maxY = point.y_;
            }

            if (point.y_ < minY) {
                minY = point.y_;
            }
        }

        topLeftCorner_ = new Point(minX, maxY);
        topRightCorner_ = new Point(maxX, maxY);
        bottomLeftCorner_ = new Point(minX, minY);
        bottomRightCorner_ = new Point(maxX, minY);

        calculateCorners_ = false;
    }

    /**
     * @return Returns the top left corner of this Trace.
     * @brief Getter method for topLeftCorner.
     * <p>
     * The Point returned is not the actual topLeftCorner but a copy of it. That is,
     * if the returned Point is changed, topLeftCorner will not be changed.
     */
    public Point getTopLeftCorner () {
        if (calculateCorners_){
            calculateCorners();
        }

        return (new Point(topLeftCorner_));
    }

    /**
     * @return Returns the bottom right corner of this Trace.
     * @brief Getter method for bottomRightCorner.
     * <p>
     * The Point returned is not the actual bottomRightCorner but a copy of it. That is,
     * if the returned Point is changed, bottomRightCorner will not be changed.
     */
    public Point getBottomRightCorner () {
        if (calculateCorners_){
            calculateCorners();
        }

        return (new Point(bottomRightCorner_));
    }

    /**
     * @return Returns the bottom left corner of this Trace.
     * @brief Getter method for bottomLeftCorner.
     * <p>
     * The Point returned is not the actual bottomLeftCorner but a copy of it. That is,
     * if the returned Point is changed, bottomLeftCorner will not be changed.
     */
    public Point getBottomLeftCorner () {
        if (calculateCorners_){
            calculateCorners();
        }

        return (new Point(bottomLeftCorner_));
    }

    /**
     * @return Returns the top right corner of this Trace.
     * @brief Getter method for top right corner.
     * <p>
     * The Point returned is not the actual topRightCorner but a copy of it. That is,
     * if the returned Point is changed, topRightCorner will not be changed.
     */
    public Point getTopRightCorner () {
        if (calculateCorners_){
            calculateCorners();
        }

        return (new Point(topRightCorner_));
    }

    /**
     * @return Returns the width of this Trace.
     * @brief Calculates and returns the width of this Trace.
     * <p>
     * The width is calculated as width = bottomRightCorner.x - topLeftCorner.x .
     */
    public double getWidth () {
        if(calculateCorners_){
            calculateCorners();
        }

        return (bottomRightCorner_.x_ - topLeftCorner_.x_);
    }

    /**
     * @return Returns the height of this Trace.
     * @brief Calculates and returns the height of this Trace.
     * <p>
     * The height is calculated as height = topLeftCorner.y - bottomRightCorner.y .
     */
    public double getHeight () {
        if(calculateCorners_){
            calculateCorners();
        }

        return (topLeftCorner_.y_ - bottomRightCorner_.y_);
    }

    /**
     * @return Returns the center of this Trace.
     * @brief Calculates and returns the center of this Trace.
     * <p>
     * The center is a Point calculated as:
     * center.x = topLeftCorner.x + traceWidth / 2;
     * center.y = bottomRightCorner.y + traceHeight / 2;
     */
    public Point getCentroid () {
        if(calculateCorners_){
            calculateCorners();
        }

        double centroidX = topLeftCorner_.x_ + getWidth() / 2;
        double centroidY = bottomRightCorner_.y_ + getHeight() / 2;

        return (new Point(centroidX, centroidY));
    }

    /**
     * @return Returns the Point inside this Trace with the minimum abscissa(x).
     * @brief Calculates and returns the Point of this Trace with the minimum abscissa(x).
     * <p>
     * The Point returned is not the actual Point with the minimum abscissa(x) but a copy of it.
     * That is, if the Point returned is changed, the Point inside the Trace will not be changed.
     */
    public Point getOuterLeftPoint () {
        if(calculateOuterLeftPoint_){
            calculateOuterLeftPoint();
        }

        return new Point(outerLeftPoint_);
    }

    private void calculateOuterLeftPoint(){
        if(! calculateOuterLeftPoint_){
            return;
        }

        double minX = points_.get(0).x_;
        int index = 0;
        for (int i = 0; i < points_.size(); i++) {
            if (points_.get(i).x_ < minX) {
                minX = points_.get(i).x_;
                index = i;
            }
        }

        outerLeftPoint_ = points_.get(index);

        calculateOuterLeftPoint_ = false;
    }

    /**
     * @return Returns the Point inside this Trace with the maximum abscissa(x).
     * @brief Calculates and returns the Point of this Trace with the maximum abscissa(x).
     * <p>
     * The Point returned is not the actual Point with the maximum abscissa(x) but a copy of it.
     * That is, if the Point returned is changed, the Point inside the Trace will not be changed.
     */
    public Point getOuterRightPoint () {
        if(calculateOuterRightPoint_){
            calculateOuterRightPoint();
        }

        return new Point(outerRightPoint_);
    }

    private void calculateOuterRightPoint(){
        if(! calculateOuterRightPoint_){
            return;
        }

        double maxX = points_.get(0).x_;
        int index = 0;
        for (int i = 0; i < points_.size(); i++) {
            if (points_.get(i).x_ > maxX) {
                maxX = points_.get(i).x_;
                index = i;
            }
        }

        outerRightPoint_ = points_.get(index);

        calculateOuterRightPoint_ = false;
    }

    /**
     * @return Returns the center of mass of this Trace.
     * @brief Calculates and returns the center of mass of this Trace.
     * <p>
     * The center of mass is calculated as:
     * centerOfMass = sum{pointsInTrace} / traceSize;
     */
    public Point getCenterOfMass () {
        if(calculateCenterOfMass_){
            calculateCenterOfMass();
        }

        return new Point(centerOfMass_);
    }

    private void calculateCenterOfMass(){
        if(! calculateCenterOfMass_){
            return;
        }

        centerOfMass_ = new Point(0, 0);
        for (Point point : points_) {
            centerOfMass_.add(point);
        }
        centerOfMass_.divideBy(this.size());

        calculateCenterOfMass_ = false;
    }

    /**
     *
     */
    public Image print (int width, int height) {
        if(drawImage_){
            drawImage(width, height);
        }

        return new Image(image_);
    }

    private void drawImage(int width, int height){
        if(!drawImage_ && image_.getWidth() == width && image_.getHeight() == height){
            return;
        }

        image_ = new Image(width, height);

        for (int i = 0, n = points_.size() - 1; i < n; i++) {
            Drawer.drawLine(image_, (int) points_.get(i).x_, (int) (height - points_.get(i).y_),
                    (int) (points_.get(i + 1).x_), (int) (height - points_.get(i + 1).y_));
        }

        drawImage_ = false;
    }

    /**
     * @return Returns the InkML representation of this Trace.
     * @brief Constructs an InkML representation of this Trace.
     */
    public String toInkMLFormat () {
        if(calculateInkML_){
            calculateInkML();
        }

        return inkML_;
    }

    private void calculateInkML(){
        if(! calculateInkML_){
            return;
        }

        inkML_ = "<trace>";
        for (Point point : points_) {
            inkML_ += point.x_ + " " + point.y_ + ", ";
        }
        // Remove last comma and the following space.
        inkML_ = inkML_.substring(0, inkML_.length() - 2);
        inkML_ += "</trace>";

        calculateInkML_ = false;
    }

    /**
     * @param point The Point to find the closest to.
     * @return Returns the Point of this Trace that is the closest to the specified Point.
     * The Point returned is the actual Point. That is, if the returned Point is changed, then, the
     * actual Point inside the Trace will also be changed.
     * @brief Calculates and returns the closest point from this Trace to a specific Point.
     */
    public Point closestPoint (Point point) {
        double minDistance = Point.distance(points_.get(0), point);
        int minIndex = 0;
        for (int i = 0; i < points_.size(); i++) {
            double distance = Point.distance(points_.get(i), point);

            if (distance < minDistance) {
                minDistance = distance;
                minIndex = i;
            }
        }

        return (new Point(points_.get(minIndex)));
    }

    /**
     * @param trace1 The first Trace.
     * @param trace2 The second Trace.
     * @return Returns true if these two Trace objects are overlapped.
     * @brief Checks if two Trace objects are overlapped.
     * <p>
     * Two Trace object are overlapped if, when printed, their curves are overlapped.
     */
    public static boolean areOverlapped (Trace trace1, Trace trace2) {
        Trace trace1Copy = new Trace(trace1);
        Trace trace2Copy = new Trace(trace2);

        TraceGroup traceGroup = new TraceGroup();
        traceGroup.add(trace1Copy);
        traceGroup.add(trace2Copy);

        trace1Copy.subtract(new Point(traceGroup.getTopLeftCorner().x_,
                traceGroup.getBottomRightCorner().y_));
        trace2Copy.subtract(new Point(traceGroup.getTopLeftCorner().x_,
                traceGroup.getBottomRightCorner().y_));
        traceGroup.subtract(new Point(traceGroup.getTopLeftCorner().x_,
                traceGroup.getBottomRightCorner().y_));

        double width = traceGroup.getWidth();
        double height = traceGroup.getHeight();

        Image image1 = trace1Copy.print((int)width, (int)height);
        Image image2 = trace2Copy.print((int)width, (int)height);

        int white = Drawer.WHITE;
        for (int x = 0; x < (int) width; x++) {
            for (int y = 0; y < (int) height; y++) {
                if (image1.getPixel(x, y) == white &&
                        ((x - 1 >= 0 && y - 1 >= 0 && image2.getPixel(x - 1, y - 1) == white) ||
                                (x - 1 >= 0 && image2.getPixel(x - 1, y) == white) ||
                                (x - 1 >= 0 && y + 1 < (int) height && image2.getPixel(x - 1, y + 1) == white) ||
                                (y - 1 >= 0 && image2.getPixel(x, y - 1) == white) ||
                                (image2.getPixel(x, y) == white) ||
                                (y + 1 < (int) height && image2.getPixel(x, y + 1) == white) ||
                                (x + 1 < (int) width && y - 1 >= 0 && image2.getPixel(x + 1, y - 1) == white) ||
                                (x + 1 < (int) width && image2.getPixel(x + 1, y) == white) ||
                                (x + 1 < (int) width && y + 1 < (int) height && image2.getPixel(x + 1, y + 1) == white))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param trace1 The first Trace.
     * @param trace2 The second Trace.
     * @return Returns the minimum distance between the two Trace objects.
     * @brief Calculates and returns the minimum distance between two Trace objects.
     * <p>
     * The minimum distance between two Trace objects is calculated as the distance between the
     * closest Point objects of these Traces.
     */
    public static double minimumDistance (Trace trace1, Trace trace2) {
        Point[] closestPoints = Trace.closestPoints(trace1, trace2);

        return (Point.distance(closestPoints[0], closestPoints[1]));
    }

    /**
     * @param trace1 The first Trace.
     * @param trace2 The second Trace.
     * @return Returns an array with the two closest Point object. The first Point belongs to the
     * first Trace and the second Point belongs to the second Trace.
     * @brief Calculates and returns the two closest Point objects from these two Trace objects.
     * <p>
     * The Point objects returned are the actual Point objects that are inside the Trace objects.
     * That is, if the returned Point objects are changed, then, the actual Point objects will also
     * be changed.
     */
    public static Point[] closestPoints (Trace trace1, Trace trace2) {
        double min = Point.distance(trace1.get(0), trace2.get(0));
        int index1 = 0;
        int index2 = 0;

        int size1 = trace1.size();
        int size2 = trace2.size();

        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                double distance = Point.distance(trace1.get(i), trace2.get(j));

                if (distance < min) {
                    min = distance;
                    index1 = i;
                    index2 = j;
                }
            }
        }

        return (new Point[]{new Point(trace1.get(index1)), new Point(trace2.get(index2))});
    }

    private void calculateAll (){
        calculateCorners_ = true;

        calculateOuterLeftPoint_ = true;
        calculateOuterRightPoint_ = true;

        calculateCenterOfMass_ = true;

        drawImage_ = true;
        calculateInkML_ = true;
    }

    private ArrayList<Point> points_; //!< The Point objects of this Trace.

    private Point topLeftCorner_; //!< The top left corner of this Trace.
    private Point topRightCorner_;
    private Point bottomLeftCorner_;
    private Point bottomRightCorner_; //!< The bottom right corner of this Trace.
    private boolean calculateCorners_;

    private Point outerLeftPoint_;
    private boolean calculateOuterLeftPoint_;

    private Point outerRightPoint_;
    private boolean calculateOuterRightPoint_;

    private Point centerOfMass_;
    private boolean calculateCenterOfMass_;

    private Image image_;
    private boolean drawImage_;

    private String inkML_;
    private boolean calculateInkML_;

}
