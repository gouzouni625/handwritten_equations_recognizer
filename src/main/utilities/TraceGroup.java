package utilities;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class TraceGroup{
  public TraceGroup(){
    traces_ = new ArrayList<Trace>();
  }
  
  public TraceGroup(ArrayList<Trace> traces){
    traces_ = traces;
  }
  
  public void addTrace(Trace trace){
    traces_.add(trace);
  }
  
  public Trace get(int index){
    return traces_.get(index);
  }
  
  public int size(){
    return traces_.size();
  }
  
  public int indexOf(Trace trace){
    return traces_.indexOf(trace);
  }

  public Mat toImage(Size size){
    Mat image = Mat.zeros(size, CvType.CV_64F);
    
    for(int i = 0;i < traces_.size();i++){
      Core.add(image, traces_.get(i).toImage(size), image);
    }
    
    return image;
  }
  
  private ArrayList<Trace> traces_;
}