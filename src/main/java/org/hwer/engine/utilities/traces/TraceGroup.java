package org.hwer.engine.utilities.traces;


import java.util.ArrayList;
import java.util.Iterator;


/**
 * @class TraceGroup
 * @brief Implements a group of Traces
 */
public class TraceGroup implements Iterable<Trace> {
    /**
     * @brief Default constructor
     */
    public TraceGroup () {
        traces_ = new ArrayList<Trace>();

        calculateAll();
    }

    /**
     * @brief Constructor
     *        This constructor can be used to copy a TraceGroup. The new TraceGroup will have the
     *        same traces with the given TraceGroup but different Trace objects.
     *
     * @param traceGroup
     *     The TraceGroup to be copied
     */
    public TraceGroup (TraceGroup traceGroup) {
        traces_ = new ArrayList<Trace>();

        for (Trace trace : traceGroup) {
            traces_.add(new Trace(trace));
        }

        calculateAll();
    }

    /**
     * @brief Returns an iterator over the Traces of this TraceGroup
     *
     * @return An iterator over the Traces of this TraceGroup
     */
    public Iterator<Trace> iterator () {
        return traces_.iterator();
    }

    /**
     * @brief Appends a Trace to this TraceGroup
     *
     * @param trace
     *     The Trace to be appended to this TraceGroup
     *
     * @return This TraceGroup so that chain commands are possible (e.g. tg.add(tr1).add(tr2))
     */
    public TraceGroup add (Trace trace) {
        traces_.add(trace);

        calculateAll();

        return this;
    }

    /**
     * @brief Appends a TraceGroup to this TraceGroup
     *        A TraceGroup is appended on another TraceGroup by appending all of its Traces to the
     *        other TraceGroup
     *
     * @param traceGroup
     *     The TraceGroup to be appended to this TraceGroup
     *
     * @return This TraceGroup so that chain commands are possible (e.g. tg1.add(tg2).add(tg3))
     */
    public TraceGroup add (TraceGroup traceGroup) {
        for (Trace trace : traceGroup) {
            traces_.add(trace);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Returns the Trace at the specified position of this TraceGroup
     *
     * @param index
     *     The position of the Trace to be returned
     *
     * @return The Trace at the specified position in this TraceGroup
     */
    public Trace get (int index) {
        return traces_.get(index);
    }

    /**
     * @brief Removed the specified Trace from this TraceGroup
     *        Concretely, removes the first occurrence of the specified Trace from this TraceGroup
     *        if it is present
     *
     * @param trace
     *     The Trace to be removed from this TraceGroup, if present
     *
     * @return True if this TraceGroup contained the specified Trace
     */
    public boolean remove (Trace trace) {
        boolean removed = traces_.remove(trace);

        if (removed) {
            calculateAll();
        }

        return removed;
    }

    /**
     * @brief Returns the number of Traces in this TraceGroup
     *
     * @return The number of Traces in this TraceGroup
     */
    public int size () {
        return traces_.size();
    }

    /**
     * @brief Returns a sub-group of this TraceGroup
     *
     * @param tracesIndices
     *     The indices of the Traces from this TraceGroup to be returned
     *
     * @return A TraceGroup that contains the Traces specified
     */
    public TraceGroup subTraceGroup (int[] tracesIndices) {
        TraceGroup traceGroup = new TraceGroup();

        for (int tracesIndex : tracesIndices) {
            traceGroup.add(traces_.get(tracesIndex));
        }

        return traceGroup;
    }

    /**
     * @brief Multiplies this TraceGroup with a factor
     *        The multiplication of a TraceGroup with a factor is defined as the multiplication of
     *        every Trace inside the TraceGroup with the factor.
     *
     * @param factor
     *     The number to multiply this TraceGroup with
     *
     * @return This TraceGroup so that chain commands are possible (e.g. tg1.multiplyBy(3).add(tr1))
     */
    public TraceGroup multiplyBy (double factor) {
        for (Trace trace_ : traces_) {
            trace_.multiplyBy(factor);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Multiplies this TraceGroup with a Point
     *        The multiplication of a TraceGroup with a Point is define as the multiplication of
     *        every Trace inside the TraceGroup with the Point.
     *
     * @param point
     *     The Point to multiply this TraceGroup with
     *
     * @return This TraceGroup so that chain commands are possible (e.g. tg1.multiplyBy(p1).get(1))
     */
    public TraceGroup multiplyBy (Point point) {
        for (Trace trace_ : traces_) {
            trace_.multiplyBy(point);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Subtracts a Point from this TraceGroup
     *        The subtraction of a Point from a TraceGroup is defined as the subtraction of the
     *        Point from every Trace inside the TraceGroup
     *
     * @param point
     *     The Point to be subtracted from this TraceGroup
     *
     * @return This TraceGroup so that chain commands are possible (e.g. tg.subtract(p1).get(0))
     */
    public TraceGroup subtract (Point point) {
        for (Trace trace_ : traces_) {
            trace_.subtract(point);
        }

        calculateAll();

        return this;
    }

    /**
     * @brief Calculates the four corners of this TraceGroup
     *        The corners of a TraceGroup are defined as follows:
     *        Top Left Corner    : minimum abscissa(x), minimum ordinate(y)
     *        Top Right Corner   : maximum abscissa(x), minimum ordinate(y)
     *        Bottom Left Corner : minimum abscissa(x), maximum ordinate(y)
     *        Bottom Right Corner: maximum abscissa(x), maximum ordinate(y)
     */
    private void calculateCorners () {
        if (! calculateCorners_) {
            return;
        }

        double minX = traces_.get(0).getTopLeftCorner().x_;
        double maxX = traces_.get(0).getBottomRightCorner().x_;
        double minY = traces_.get(0).getBottomRightCorner().y_;
        double maxY = traces_.get(0).getTopLeftCorner().y_;

        for (Trace trace_ : traces_) {
            Point topLeftCorner = trace_.getTopLeftCorner();
            Point bottomRightCorner = trace_.getBottomRightCorner();

            if (topLeftCorner.x_ < minX) {
                minX = topLeftCorner.x_;
            }

            if (bottomRightCorner.x_ > maxX) {
                maxX = bottomRightCorner.x_;
            }

            if (bottomRightCorner.y_ < minY) {
                minY = bottomRightCorner.y_;
            }

            if (topLeftCorner.y_ > maxY) {
                maxY = topLeftCorner.y_;
            }
        }

        topLeftCorner_ = new Point(minX, maxY);
        topRightCorner_ = new Point(maxX, maxY);
        bottomLeftCorner_ = new Point(minX, minY);
        bottomRightCorner_ = new Point(maxX, minY);

        calculateCorners_ = false;
    }

    /**
     * @brief Returns the topLeftCorner of this TraceGroup
     *        The Point returned is not the actual Point object in this TraceGroup but a copy of it
     *
     * @return The topLeftCorner of this TraceGroup
     */
    public Point getTopLeftCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(topLeftCorner_));
    }

    /**
     * @brief Returns the bottomRightCorner of this TraceGroup
     *        The Point returned is not the actual Point object in this TraceGroup but a copy of it
     *
     * @return The bottomRightCorner of this TraceGroup
     */
    public Point getBottomRightCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(bottomRightCorner_));
    }

    /**
     * @brief Returns the bottomLeftCorner of this TraceGroup
     *        The Point returned is not the actual Point object in this TraceGroup but a copy of it
     *
     * @return The bottomLeftCorner of this TraceGroup
     */
    public Point getBottomLeftCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(bottomLeftCorner_));
    }

    /**
     * @brief Returns the topRightCorner of this TraceGroup
     *        The Point returned is not the actual Point object in this TraceGroup but a copy of it
     *
     * @return The topRightCorner of this TraceGroup
     */
    public Point getTopRightCorner () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (new Point(topRightCorner_));
    }

    /**
     * @brief Returns the width of this TraceGroup
     *        The width of a TraceGroup is defined as the horizontal distance between the outermost
     *        left and the outermost right Point of the TraceGroup
     *
     * @return The width of this TraceGroup
     */
    public double getWidth () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (bottomRightCorner_.x_ - topLeftCorner_.x_);
    }

    /**
     * @brief Returns the height of this TraceGroup
     *        The height of a TraceGroup is defined as the vertical distance between the outermost
     *        top and the outermost bottom Point of the TraceGroup
     *
     * @return The height of this TraceGroup
     */
    public double getHeight () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (topLeftCorner_.y_ - bottomRightCorner_.y_);
    }

    /**
     * @brief Returns the centroid of this TraceGroup
     *        The centroid of a TraceGroup is the center of the rectangle formed by the four corners
     *        of the TraceGroup
     *
     * @return The centroid of this TraceGroup
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
     * @brief Returns the area covered by this TraceGroup
     *
     * @return The area covered by this TraceGroup
     */
    public double getArea () {
        if (calculateCorners_) {
            calculateCorners();
        }

        return (getWidth() * getHeight());
    }

    /**
     * @brief Returns the centerOfMass of this TraceGroup
     *        The centerOfMass of a TraceGroup is defined as the Point with abscissa equal to the
     *        average abscissa and ordinate equal to the average ordinate of the Points in the
     *        TraceGroup. The Point returned is not the actual Point object in this TraceGroup but a
     *        copy of it.
     *
     * @return The centerOfMass of this TraceGroup
     */
    public Point getCenterOfMass () {
        if (calculateCenterOfMass_) {
            calculateCenterOfMass();
        }

        return new Point(centerOfMass_);
    }

    /**
     * @brief Calculates the centerOfMass of this TraceGroup
     */
    private void calculateCenterOfMass () {
        if (! calculateCenterOfMass_) {
            return;
        }

        centerOfMass_ = new Point(0, 0);
        int numberOfPoints = 0;
        for (Trace trace_ : traces_) {
            centerOfMass_.add(trace_.getCenterOfMass().multiplyBy(trace_.size()));

            numberOfPoints += trace_.size();
        }
        centerOfMass_.divideBy(numberOfPoints);

        calculateCenterOfMass_ = false;
    }

    /**
     * @brief Returns true if this TraceGroup is overlapped by the given Trace
     *
     * @param trace
     *     The given Trace
     *
     * @return True if this TraceGroup is overlapped by the given Trace
     */
    public boolean isOverlappedBy (Trace trace) {
        for (Trace trace_ : traces_) {
            if (Trace.areOverlapped(trace_, trace)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @brief Returns the two closest Traces of the two TraceGroups
     *        The Traces returned are not the actual Trace objects in the TraceGroups but copies of
     *        them.
     *
     * @param traceGroup1
     *     The first TraceGroup
     * @param traceGroup2
     *     The second TraceGroup
     *
     * @return An array with the two closest Traces objects. The first Trace belongs to the
     *         first TraceGroup and the second Trace belongs to the second TraceGroup
     */
    public static Trace[] closestTraces (TraceGroup traceGroup1, TraceGroup traceGroup2) {
        Trace trace1 = traceGroup1.get(0);
        Trace trace2 = traceGroup2.get(0);

        double minDistance = Trace.minimumDistance(trace1, trace2);
        double distance;
        for (Trace trace1_ : traceGroup1) {
            for (Trace trace2_ : traceGroup2) {
                distance = Trace.minimumDistance(trace1_, trace2_);

                if (distance < minDistance) {
                    minDistance = distance;
                    trace1 = trace1_;
                    trace2 = trace2_;
                }
            }
        }

        return (new Trace[] {new Trace(trace1), new Trace(trace2)});
    }

    /**
     * @brief Returns the two closest Points of two TraceGroups
     *        The Points returned are not the actual Point objects in the TraceGroups but copies of
     *        them
     *
     * @param traceGroup1
     *     The first TraceGroup
     * @param traceGroup2
     *     The second TraceGroup
     *
     * @return An array with the two closest Point objects. The first Point belongs to the
     *         first TraceGroup and the second Point belongs to the second TraceGroup
     */
    public static Point[] closestPoints (TraceGroup traceGroup1, TraceGroup traceGroup2) {
        Trace[] closestTraces = TraceGroup.closestTraces(traceGroup1, traceGroup2);

        return (Trace.closestPoints(closestTraces[0], closestTraces[1]));
    }

    /**
     * @brief Sets all calculate* flags to true
     */
    private void calculateAll () {
        calculateCorners_ = true;

        calculateCenterOfMass_ = true;
    }

    private ArrayList<Trace> traces_; //!< The Traces of this TraceGroup

    private Point topLeftCorner_; //!< The top left corner of this TraceGroup
    private Point topRightCorner_; //!< The top right corner of this TraceGroup
    private Point bottomLeftCorner_; //!< The bottom left corner of this TraceGroup
    private Point bottomRightCorner_; //!< The bottom right corner of this TraceGroup
    private boolean calculateCorners_; //!< Flag indicating that the corners should be recalculated

    private Point centerOfMass_; //!< The center of mass of this TraceGroup
    private boolean calculateCenterOfMass_; //!< Flag indicating that the center of mass should be
                                            //!< recalculated

}
