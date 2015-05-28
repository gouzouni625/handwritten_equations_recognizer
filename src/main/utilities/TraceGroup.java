package main.utilities;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

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

  public void add(Trace trace){
    traces_.add(new Trace(trace));
  }

  public Trace get(int index){
    return traces_.get(index);
  }

  public int size(){
    return traces_.size();
  }

  // TODO
  // Not a final version.
  public Mat toImage(Size size){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    Mat image = Mat.zeros(size, CvType.CV_32F);

    for(int i = 0;i < traces_.size();i++){
      Core.add(image, traces_.get(i).toImage(size), image);
    }

    return image;
  }

  public TraceGroup subTraceGroup(int[] tracesIndeces){
    TraceGroup traceGroup = new TraceGroup();

    for(int i = 0;i < tracesIndeces.length;i++){
      traceGroup.add(traces_.get(tracesIndeces[i]));
    }

    return traceGroup;
  }

  private ArrayList<Trace> traces_;
}
