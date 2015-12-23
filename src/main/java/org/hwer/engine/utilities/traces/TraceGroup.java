package org.hwer.engine.utilities.traces;

import java.util.ArrayList;


/** @class TraceGroup
 *
 *  @brief Implements a group of Trace objects.
 *
 *  A group of Trace objects is a sequence of Trace objects.
 */
public class TraceGroup{
  /**
   *  @brief Default constructor.
   */
  public TraceGroup(){
    traces_ = new ArrayList<Trace>();
  }

  /**
   *  @brief Constructor.
   *
   *  This constructor is used to create an identical copy of a TraceGroup.
   *
   *  @param traceGroup The TraceGroup to be copied.
   */
  public TraceGroup(TraceGroup traceGroup){
    traces_ = new ArrayList<Trace>();

    for(int i = 0;i < traceGroup.size();i++){
      this.add(traceGroup.get(i));
    }
  }

  /**
   *  @brief Adds a Trace to this TraceGroup.
   *
   *  It is not the actual Trace that is added but a copy of it. That is, if the given as input
   *  Trace changes, the Trace inside this TraceGroup will not change.
   *
   *  @param trace The Trace to be added to this TraceGroup.
   *
   *  @return Returns this TraceGroup in order for chain commands to be
   *          possible(e.g. tg.add(tr1).add(tr2);).
   */
  public TraceGroup add(Trace trace){
    traces_.add(new Trace(trace));

    return this;
  }

  /**
   *  @brief Adds a TraceGroup to this TraceGroup.
   *
   *  Each Trace of the given TraceGroup is added to this TraceGroup.
   *
   *  @param traceGroup The TraceGroup to be added to this TraceGroup.
   *
   *  @return Returns this TraceGroup in order for chain commands to be
   *          possible(e.g. tg1.add(tg2).add(tg3);).
   */
  public TraceGroup add(TraceGroup traceGroup){
    for(int i = 0;i < traceGroup.size();i++){
      this.add(traceGroup.get(i));
    }

    return this;
  }

  /**
   *  @brief Returns the Trace at a specific position in this Trace.
   *
   *  The Trace returned is the actual Trace that exists inside this TraceGroup. That is,
   *  if the returned Trace is changed, then, the Trace inside this TraceGroup, will also change.
   *
   *  @param index The position of the Trace to be returned.
   *
   *  @return Returns the Trace at the specified position in this TraceGroup.
   */
  public Trace get(int index){
    return traces_.get(index);
  }

  /**
   *  @brief Returns the number of Trace objects in this TraceGroup.
   *
   * @return Returns the number of Trace objects in this TraceGroup.
   */
  public int size(){
    return traces_.size();
  }

  /**
   *  @brief Returns a sub-group of Trace objects from this TraceGroup.
   *
   *  It is the user's responsibility to correctly enter the indices of the Trace objects to be
   *  returned. That is, if tracesIndices contains an index that is less than zero of greater than
   *  the size - 1 of this TraceGroup, the program will throw an exception. The Trace objects that
   *  are returned are not the actual Trace objects that exist inside this TraceGroup but a copy of
   *  them. That is, if the returned Trace objects change, the Trace objects inside this TraceGroup
   *  will not change.
   *
   *  @param tracesIndices The indices of the Trace objects from this TraceGroup to be returned.
   *
   *  @return A TraceGroup that contains the Trace objects specified.
   */
  public TraceGroup subTraceGroup(int[] tracesIndices){
    TraceGroup traceGroup = new TraceGroup();

    for(int i = 0;i < tracesIndices.length;i++){
      traceGroup.add(traces_.get(tracesIndices[i]));
    }

    return traceGroup;
  }

  /**
   *  @brief Multiplies this TraceGroup with a double.
   *
   *  Multiplying a TraceGroup with a double is to multiply each Trace in the TraceGroup with this
   *  double.
   *
   *  @param factor The double that this TraceGroup should be multiplied with.
   *
   *  @return Returns this TraceGroup in order for chain commands to be
   *  possible(e.g. tg1.multiplyBy(3).multiplyBy(4);).
   */
  public TraceGroup multiplyBy(double factor){
    for(int i = 0;i < traces_.size();i++){
      traces_.get(i).multiplyBy(factor);
    }

    return this;
  }

  /**
   *  @brief Subtracts a Point from this TraceGroup.
   *
   *  Subtracting a Point from a TraceGroup is to subtract the Point from each Trace in the
   *  TraceGroup.
   *
   *  @param point The Point to be subtracted from this TraceGroup.
   *
   *  @return Returns this TraceGroup in order for chain commands to be
   *          possible(e.g. tg.subtract(p1).multiplyBy(3);).
   */
  public TraceGroup subtract(Point point){
    for(int i = 0;i < traces_.size();i++){
      traces_.get(i).subtract(point);
    }

    return this;
  }

  /**
   *  @brief Calculates the corners of this TraceGroup.
   *
   *  Each TraceGroup has four(4) corners.
   *  Top Left Corner    : minimum abscissa(x), minimum ordinate(y).
   *  Top Right Corner   : maximum abscissa(x), minimum ordinate(y).
   *  Bottom Left Corner : minimum abscissa(x), maximum ordinate(y).
   *  Bottom Right Corner: maximum abscissa(x), maximum ordinate(y).
   */
  public void calculateCorners(){
    if(traces_ == null || traces_.size() == 0){
      return;
    }

    traces_.get(0).calculateCorners();

    double minX = traces_.get(0).getTopLeftCorner().x_;
    double maxX = traces_.get(0).getBottomRightCorner().x_;
    double minY = traces_.get(0).getBottomRightCorner().y_;
    double maxY = traces_.get(0).getTopLeftCorner().y_;

    for(int i = 0;i < traces_.size();i++){
      traces_.get(i).calculateCorners();

      Point topLeftCorner = traces_.get(i).getTopLeftCorner();
      Point bottomRightCorner = traces_.get(i).getBottomRightCorner();

      if(topLeftCorner.x_ < minX){
        minX = topLeftCorner.x_;
      }

      if(bottomRightCorner.x_ > maxX){
        maxX = bottomRightCorner.x_;
      }

      if(bottomRightCorner.y_ < minY){
        minY = bottomRightCorner.y_;
      }

      if(topLeftCorner.y_ > maxY){
        maxY = topLeftCorner.y_;
      }
    }

    topLeftCorner_ = new Point(minX, maxY);
    bottomRightCorner_ = new Point(maxX, minY);
  }

  /**
   *  @brief Getter method for topLeftCorner.
   *
   *  The Point returned is not the actual topLeftCorner but a copy of it. That is, if the returned
   *  Point is changed, topLeftCorner will not be changed.
   *
   *  @return Returns the top left corner of this TraceGroup.
   */
  public Point getTopLeftCorner(){
    return (new Point(topLeftCorner_));
  }

  /**
   *  @brief Getter method for bottomRightCorner.
   *
   *  The Point returned is not the actual bottomRightCorner but a copy of it. That is, if the
   *  returned Point is changed, bottomRightCorner will not be changed.
   *
   *  @return Returns the bottom right corner of this TraceGroup.
   */
  public Point getBottomRightCorner(){
    return (new Point(bottomRightCorner_));
  }

  /**
   *  @brief Getter method for bottomLeftCorner.
   *
   *  The Point returned is not the actual bottomLeftCorner but a copy of it. That is, if the
   *  returned Point is changed, bottomLeftCorner will not be changed.
   *
   *  @return Returns the bottom left corner of this TraceGroup.
   */
  public Point getBottomLeftCorner(){
    return (new Point(topLeftCorner_.x_, bottomRightCorner_.y_));
  }

  /**
   *  @brief Getter method for top right corner.
   *
   *  The Point returned is not the actual topRightCorner but a copy of it. That is, if the
   *  returned Point is changed, topRightCorner will not be changed.
   *
   *  @return Returns the top right corner of this TraceGroup.
   */
  public Point getTopRightCorner(){
    return (new Point(bottomRightCorner_.x_, topLeftCorner_.y_));
  }

  /**
   *  @brief Calculates and returns the width of this TraceGroup.
   *
   *  The width is calculated as width = bottomRightCorner.x - topLeftCorner.x .
   *
   *  @return Returns the width of this TraceGroup.
   */
  public double getWidth(){
    return (bottomRightCorner_.x_ - topLeftCorner_.x_);
  }

  /**
   *  @brief Calculates and returns the height of this TraceGroup.
   *
   *  The height is calculated as height = topLeftCorner.y - bottomRightCorner.y .
   *
   *  @return Returns the height of this TraceGroup.
   */
  public double getHeight(){
    return (topLeftCorner_.y_ - bottomRightCorner_.y_);
  }

  /**
   *  @brief Calculates and returns the center of this TraceGroup.
   *
   *  The center is a Point calculated as:
   *  center.x = topLeftCorner.x + traceWidth / 2;
   *  center.y = bottomRightCorner.y + traceHeight / 2;
   *
   *  @return Returns the center of this TraceGroup.
   */
  public Point getCentroid(){
    this.calculateCorners();

    double centroidX = topLeftCorner_.x_ + this.getWidth() / 2;
    double centroidY = bottomRightCorner_.y_ + this.getHeight() / 2;

    return (new Point(centroidX, centroidY));
  }

  /**
   *  @brief Calculates and returns the center of mass of this TraceGroup.
   *
   *  The center of mass is calculated as:
   *  centerOfMass = sum{pointsInTrace} / traceSize;
   *
   *  @return Returns the center of mass of this TraceGroup.
   */
  public Point getCenterOfMass(){
    Point centerOfMass = new Point(0, 0);

    int numberOfPoints = 0;
    for(Trace trace : traces_){
      centerOfMass.add(trace.getCenterOfMass().multiplyBy(trace.size()));

      numberOfPoints += trace.size();
    }
    centerOfMass.divideBy(numberOfPoints);

    return centerOfMass;
  }

  /**
   *  @brief Calculates and returns the area covered by this TraceGroup.
   *
   *  The area is calculated as: area = width * height .
   * @return
   */
  public double getArea(){
    return (this.getWidth() * this.getHeight());
  }

  /**
   *  @brief Calculates and returns the minimum distance between two TraceGroup objects.
   *
   *  The minimum distance between two TraceGroup objects is calculated as the distance between the
   *  closest Point objects of these TraceGroup objects.
   *
   *  @param traceGroup1 The first TraceGroup.
   *  @param traceGroup2 The second TraceGroup.
   *
   *  @return Returns the minimum distance between the two Trace objects.
   */
  public static double minimumDinstance(TraceGroup traceGroup1, TraceGroup traceGroup2){
    Point[] closestPoints = TraceGroup.closestPoints(traceGroup1, traceGroup2);

    return (Point.distance(closestPoints[0], closestPoints[1]));
  }

  /**
   *  @brief Calculates and returns the two closests Trace objects from these two TraceGroup objects.
   *
   *  The Trace objects returned are the actual Trace objects that are inside the TraceGroup
   *  objects. That is, if the returned Trace objects are changed, then, the actual Trace objects
   *  will also be changed.
   *
   *  @param traceGroup1 The first TraceGroup.
   *  @param traceGroup2 The second TraceGroup.
   *
   *  @return Returns and array with the two closest Trace objects. The first Trace belongs to the
   *  first TraceGroup and the second Trace belongs to the second TraceGroup.
   */
  public static Trace[] closestTraces(TraceGroup traceGroup1, TraceGroup traceGroup2){
    double minimumDistance = Trace.minimumDistance(traceGroup1.get(0), traceGroup2.get(0));
    int index1 = 0;
    int index2 = 0;

    int size1 = traceGroup1.size();
    int size2 = traceGroup2.size();
    for(int i = 0;i < size1;i++){
      for(int j = 0;j < size2;j++){
        double distance = Trace.minimumDistance(traceGroup1.get(i), traceGroup2.get(j));

        if(distance < minimumDistance){
          minimumDistance = distance;
          index1 = i;
          index2 = j;
        }
      }
    }

    return (new Trace[] {traceGroup1.get(index1), traceGroup2.get(index2)});
  }

  /**
   *  @brief Calculated and returns the two closest Point objects from these two TraceGroup objects.
   *
   *  The Point objects returned are the actual Point objects that are inside the TraceGroup
   *  objects. That is, if the returned Point objects are changed, then, the actual Point objects
   *  will also be changed.
   *
   *  @param traceGroup1 The first TraceGroup.
   *  @param traceGroup2 The second TraceGroup.
   *
   *  @return Returns an array with the two closest Point objects. The first Point belongs to the
   *  first TraceGroup and the second Point belongs to the second TraceGroup.
   */
  public static Point[] closestPoints(TraceGroup traceGroup1, TraceGroup traceGroup2){
    Trace[] closestTraces = TraceGroup.closestTraces(traceGroup1, traceGroup2);

    return (Trace.closestPoints(closestTraces[0], closestTraces[1]));
  }

  public void reset(){
    traces_.clear();
    topLeftCorner_ = null;
    bottomRightCorner_ = null;
  }

  public boolean isOverlappedBy(Trace trace){
    int numberOfTraces = traces_.size();

    for(int i = 0;i < numberOfTraces;i++){
      if(Trace.areOverlapped(traces_.get(i), trace)){
        return true;
      }
    }

    return false;
  }

  private ArrayList<Trace> traces_; //!< The Trace objects of this TraceGroup.

  private Point topLeftCorner_; //!< The top left corner of this TraceGroup.
  private Point bottomRightCorner_; //!< The bottom right corner of this TraceGroup.

}
