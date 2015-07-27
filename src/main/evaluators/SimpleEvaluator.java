package main.evaluators;

import java.io.IOException;

import main.utilities.traces.Point;
import main.utilities.traces.Trace;
import main.utilities.traces.TraceGroup;
import main.distorters.ImageDistorter;
import main.partitioners.NNMSTPartitioner;
import main.parsers.GGParser;

public class SimpleEvaluator{

  public SimpleEvaluator(String inkML, String neuralNetworkPath, int[] sizesOfLayers, ImageDistorter imageDistorter) throws IOException{
    expression_ = new TraceGroup();

    int startOfTrace = inkML.indexOf("<trace>");
    int endOfTrace = inkML.indexOf("</trace>");
    while(startOfTrace != -1){
      String[] traceData = inkML.substring(startOfTrace + 7, endOfTrace).split(", "); // ("<trace>").length = 7.

      Trace trace = new Trace();
      for(int i = 0;i < traceData.length;i++){
        double x = Double.parseDouble(traceData[i].split(" ")[0]);
        double y = Double.parseDouble(traceData[i].split(" ")[1]);
        trace.add(new Point(x, y));
      }
      if(trace.size() > 1){
        expression_.add(trace);
      }

      inkML = inkML.substring(endOfTrace + 8); // ("</trace>").length = 8.
      startOfTrace = inkML.indexOf("<trace>");
      endOfTrace = inkML.indexOf("</trace>");
    }

    partitioner_ = new NNMSTPartitioner(sizesOfLayers, neuralNetworkPath, imageDistorter);

    parser_ = new GGParser();
  }

  public void evaluate(){
    TraceGroup[] partition = partitioner_.partition(expression_);
    int[] labels = partitioner_.getLabels();

    parser_.parse(partition, labels);
  }

  public String toString(){
    return parser_.toString();
  }

  private TraceGroup expression_;

  private NNMSTPartitioner partitioner_;

  private GGParser parser_;

}
