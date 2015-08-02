package main.utilities.traces;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Class implementing a group of traces as a list of traces.
 *
 * @author Georgios Ouzounis
 *
 */
public class TraceGroup{

  public TraceGroup(){
    traces_ = new ArrayList<Trace>();
  }

  public TraceGroup(TraceGroup traceGroup){
    traces_ = new ArrayList<Trace>();

    for(int i = 0;i < traceGroup.size();i++){
      this.add(traceGroup.get(i));
    }
  }

  public TraceGroup add(Trace trace){
    traces_.add(new Trace(trace));

    return this;
  }

  public TraceGroup add(TraceGroup traceGroup){
    for(int i = 0;i < traceGroup.size();i++){
      this.add(traceGroup.get(i));
    }

    return this;
  }

  public Trace get(int index){
    return traces_.get(index);
  }

  public int size(){
    return traces_.size();
  }

  public TraceGroup subTraceGroup(int[] tracesIndeces){
    TraceGroup traceGroup = new TraceGroup();

    for(int i = 0;i < tracesIndeces.length;i++){
      traceGroup.add(traces_.get(tracesIndeces[i]));
    }

    return traceGroup;
  }

  public TraceGroup multiplyBy(double factor){
    for(int i = 0;i < traces_.size();i++){
      traces_.get(i).multiplyBy(factor);
    }

    return this;
  }

  public TraceGroup subtract(Point point){
    for(int i = 0;i < traces_.size();i++){
      traces_.get(i).subtract(point);
    }

    return this;
  }

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

  public double getWidth(){
    return (bottomRightCorner_.x_ - topLeftCorner_.x_);
  }

  public double getHeight(){
    return (topLeftCorner_.y_ - bottomRightCorner_.y_);
  }

  public Mat print(Size size){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    // Work on a copy of this trace group.
    TraceGroup traceGroup = new TraceGroup(this);

    // Data provided by GeoGebra will have 2 decimal digits, that is why the
    // multiplication is done with 100.
    traceGroup.multiplyBy(100);

    traceGroup.calculateCorners();

    traceGroup.subtract(new Point(traceGroup.getTopLeftCorner().x_, traceGroup.getBottomRightCorner().y_));

    double width = traceGroup.getWidth();
    if(width < 100){
      width = 100;
    }
    double height = traceGroup.getHeight();
    if(height < 100){
      height = 100;
    }

    Mat image = Mat.zeros(new Size(width, height), CvType.CV_32F);

    int thickness = (int)((height + width) / 2 * 30 / 1000);
    if(thickness <= 0){
      thickness = 1;
    }
    else if(thickness > 255){
      thickness = 255;
    }

    for(int i = 0;i < traceGroup.size();i++){
      traceGroup.get(i).print(image, thickness);
    }

    Imgproc.resize(image, image, new Size(1000, 1000));

    Imgproc.copyMakeBorder(image, image, 500, 500, 500, 500, Imgproc.BORDER_CONSTANT, new Scalar(0, 0, 0));

    Imgproc.blur(image, image, new Size(200, 200));

    Imgproc.resize(image, image, size);

    double meanValue = 0;
    for(int i = 0;i < size.height;i++){
      for(int j = 0;j < size.width;j++){
        meanValue += image.get(i, j)[0];
      }
    }
    meanValue /= (size.height * size.width);

    double value;
    for(int i = 0;i < size.height;i++){
      for(int j = 0;j < size.width;j++){
        value = image.get(i, j)[0];
        if(value > meanValue){
          image.put(i, j, 255);
        }
        else{
          image.put(i, j, 0);
        }
      }
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

    int numberOfPoints = 0;
    for(Trace trace : traces_){
      centerOfMass.add(trace.getCenterOfMass().multiplyBy(trace.size()));

      numberOfPoints += trace.size();
    }
    centerOfMass.divideBy(numberOfPoints);

    return centerOfMass;
  }

  public double getArea(){
    return (this.getWidth() * this.getHeight());
  }

  public static double minimumDinstance(TraceGroup traceGroup1, TraceGroup traceGroup2){
    Point[] closestPoints = TraceGroup.closestPoints(traceGroup1, traceGroup2);

    return (Point.distance(closestPoints[0], closestPoints[1]));
  }

  public static Trace[] closestTraces(TraceGroup traceGroup1, TraceGroup traceGroup2){
    double minimumDinstance = Trace.minimumDistance(traceGroup1.get(0), traceGroup2.get(0));
    int index1 = 0;
    int index2 = 0;

    int size1 = traceGroup1.size();
    int size2 = traceGroup2.size();
    for(int i = 0;i < size1;i++){
      for(int j = 0;j < size2;j++){
        double distance = Trace.minimumDistance(traceGroup1.get(i), traceGroup2.get(j));

        if(distance < minimumDinstance){
          minimumDinstance = distance;
          index1 = i;
          index2 = j;
        }
      }
    }

    return (new Trace[] {traceGroup1.get(index1), traceGroup2.get(index2)});
  }

  public static Point[] closestPoints(TraceGroup traceGroup1, TraceGroup traceGroup2){
    Trace[] closestTraces = TraceGroup.closestTraces(traceGroup1, traceGroup2);

    return (Trace.closestPoints(closestTraces[0], closestTraces[1]));
  }

  private ArrayList<Trace> traces_;

  private Point topLeftCorner_;
  private Point bottomRightCorner_;

}
