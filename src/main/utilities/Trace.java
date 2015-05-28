package main.utilities;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

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

  // TODO
  public Mat toImage(Size size){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    // Work on a copy of the original points in order not to change them.
    @SuppressWarnings("unchecked")
    ArrayList<Point> points = (ArrayList<Point>)points_.clone();

    // Multiply data by 100. ===================================================
    // Data provided by GeoGebra will have 2 decimal digits, that is why the
    // multiplication is done by 100.
    for(int i = 0;i < points_.size();i++){
      points.get(i).multiplyBy(100);
    }

    // Find a bounding box around the trace. ====================================
    int minX = (int)(points.get(0).x_);
    int maxX = (int)(points.get(0).x_);
    int minY = (int)(points.get(0).y_);
    int maxY = (int)(points.get(0).y_);
    for(int i = 0;i < points.size();i++){
      if(points.get(i).x_ < minX){
        minX = (int)(points.get(i).x_);
      }
      if(points.get(i).x_ > maxX){
        maxX = (int)(points.get(i).x_);
      }

      if(points.get(i).y_ < minY){
        minY = (int)(points.get(i).y_);
      }

      if(points.get(i).y_ > maxY){
        maxY = (int)(points.get(i).y_);
      }
    }
    int width = maxX - minX;
    if(width < 100){
      width = 100;
    }
    int height = maxY - minY;
    if(height < 100){
      height = 100;
    }

    // Translate points around the beginning of the axes. =======================
    Point translation = new Point(minX, minY);
    for(int i = 0;i < points.size();i++){
      points.get(i).subtract(translation);
    }

    // Create the image. =======================================================
    Mat image = Mat.zeros(height, width, CvType.CV_32F);

    // Wanted thickness at 1000 x 1000 pixels = 30.
    int thickness = (width + height) / 2 * 30 / 1000;
    // Check that 0 <= thickness <= 255 (constraint made by OpenCV).
    if(thickness <= 0){
      thickness = 1;
    }
    else if(thickness > 255){
      thickness = 255;
    }

    // Notice that we use OpenCV points inside Core.line function.
    for(int i = 0;i < points.size() - 1;i++){
      Core.line(image,
         new org.opencv.core.Point(points.get(i).x_, height - points.get(i).y_),
         new org.opencv.core.Point(points.get(i + 1).x_,
              height - points.get(i + 1).y_),
              new Scalar(255, 255, 255), thickness);
    }

    Imgproc.resize(image, image, new Size(1000, 1000));

    Imgproc.copyMakeBorder(image, image, 500, 500, 500, 500,
        Imgproc.BORDER_CONSTANT, new Scalar(0, 0, 0));

    Imgproc.blur(image, image, new Size(200, 200));

    Imgproc.resize(image, image, size);

    int meanValue = 0;
    for(int i = 0;i < size.height;i++){
      for(int j = 0;j < size.width;j++){
        meanValue += image.get(i, j)[0];
      }
    }
    meanValue /= (size.height * size.width);

    int value;
    for(int i = 0;i < size.height;i++){
      for(int j = 0;j < size.width;j++){
        value = (int)(image.get(i, j)[0]);
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

  private ArrayList<Point> points_;
}
