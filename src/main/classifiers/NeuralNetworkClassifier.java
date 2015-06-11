package main.classifiers;

import java.io.IOException;

import main.base.NeuralNetwork;
import main.utilities.TraceGroup;
import main.utilities.Utilities;

import org.opencv.core.Mat;
import org.opencv.core.Size;

public class NeuralNetworkClassifier implements Classifier{
  public NeuralNetworkClassifier(int[] sizesOfLayers){
    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);
  }

  public void loadNeuralNetwork(String path) throws IOException{
    neuralNetwork_.loadNetwork(path);
  }

  public void newNeuralNetwork(int[] sizesOfLayers, String path) throws IOException{
    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);
    neuralNetwork_.loadNetwork(path);
  }

  /** \brief Checks if the traces in symbol trace group constitute a valid
   *         symbol given the context and returns the certainty of the decision.
   *
   *  \symbol: The group of traces that possibly constitute a valid symbol.
   *  \context: The group of traces that constitute the context of the symbol
   *            traces.
   *  \return: The possibility that the symbol group of traces constitutes a
   *           valid symbol. The values lies inside [0, 1].
   */
  public double classify(TraceGroup symbol, TraceGroup context){
    int symbolSize = symbol.size();
    int contextSize = context.size();

    if(symbolSize > Utilities.MAX_TRACES_IN_SYMBOL){
      return Utilities.MINIMUM_RATE;
    }

    double[] neuralNetworkOutput = neuralNetwork_.feedForward(this.imageToVector(symbol.print(new Size(28, 28)), -1, 1));
    neuralNetworkOutput = Utilities.relativeValues(neuralNetworkOutput);

    classificationLabel_ = Utilities.indexOfMax(neuralNetworkOutput);
    double symbolRate = neuralNetworkOutput[classificationLabel_];

    // Process context.
    if(symbolSize == Utilities.MAX_TRACES_IN_SYMBOL || contextSize == 0){
      return symbolRate;
    }

    boolean[][] connections = new boolean[contextSize][contextSize];
    for(int i = 0;i < contextSize;i++){
      for(int j = 0;j < contextSize;j++){
        connections[i][j] = true;
      }
    }
    int[][] contextPaths = Utilities.findUniquePaths(connections, Utilities.MAX_TRACES_IN_SYMBOL - symbolSize);
    int numberOfContextPaths = contextPaths.length;

    double finalRate = symbolRate;
    TraceGroup symbolWithContext;
    for(int i = 0;i < numberOfContextPaths;i++){
      symbolWithContext = new TraceGroup(symbol);
      symbolWithContext.add(context.subTraceGroup(contextPaths[i]));

      neuralNetworkOutput = neuralNetwork_.feedForward(this.imageToVector(symbolWithContext.print(new Size(28, 28)), -1, 1));
      neuralNetworkOutput = Utilities.relativeValues(neuralNetworkOutput);

      double rate = Utilities.maxValue(neuralNetworkOutput);
      if(rate < (Utilities.MAXIMUM_RATE - Utilities.MINIMUM_RATE) / 2){
        continue;
      }

      finalRate -= 33 / 100 * rate;
    }

    /* ===== Print symbol rate ===== */
    //System.out.println("symbolRate = " + symbolRate + " symbolLabel = " + classificationLabel_);
    /* ===== Print symbol rate ===== */

    /* ===== Print context paths ===== */
    /*System.out.println(" ===== Print context paths =====");
    for(int i = 0;i < contextPaths.length;i++){
      for(int j = 0;j < contextPaths[i].length;j++){
        System.out.print(contextPaths[i][j] + ", ");
      }

      System.out.println();
    }
    /* ===== Print context paths ===== */

    /*TraceGroup symbolWithContext;
    for(int i = 0;i < numberOfContextPaths;i++){
      symbolWithContext = new TraceGroup(symbol);
      symbolWithContext.add(context.subTraceGroup(contextPaths[i]));

      neuralNetworkOutput = neuralNetwork_.feedForward(this.imageToVector(symbolWithContext.print(new Size(28, 28)), -1, 1));
      neuralNetworkOutput = Utilities.relativeValues(neuralNetworkOutput);
      int label = Utilities.indexOfMax(neuralNetworkOutput);
      double rate = neuralNetworkOutput[label];

      /* ===== Print symbol with context rate ===== */
      //System.out.println("rate = " + rate + " label = " + label);
      /* ===== Print symbol with context rate ===== */

      /*if(Math.abs(rate - symbolRate) < (Utilities.MAXIMUM_RATE - Utilities.MINIMUM_RATE) / 2 && label != classificationLabel_){
        classificationLabel_ = -1;
        return Utilities.UNKNOWN_LABEL;
      }
    }*/

    return finalRate;
  }

  /* TODO
   * One can get the possibility that a trace group constitutes a symbol by
   * calling classify(). One can also get the symbol by calling getSymbol()
   * function. This function must work with utilities class and use the
   * enumerations that lie inside there.
   */
  public int getClassificationLabel(){
    return classificationLabel_;
  }

  private double[] imageToVector(Mat image, double min, double max){
    // Find the minimum and the maximum values of the image.
    double minValue = image.get(0, 0)[0];
    double maxValue = image.get(0, 0)[0];
    for(int i = 0;i < image.rows();i++){
      for(int j = 0;j < image.cols();j++){
        if(image.get(i, j)[0] > maxValue){
          maxValue = image.get(i, j)[0];
        }

        if(image.get(i, j)[0] < minValue){
          minValue = image.get(i, j)[0];
        }
      }
    }

    double[] vector = new double[image.rows() * image.cols()];

    for(int i = 0;i < image.rows();i++){
      for(int j = 0;j < image.cols();j++){
        vector[i * image.cols() + j] = (image.get(i, j)[0] - minValue) * (max - min) / (maxValue - minValue) + min;
      }
    }

    return vector;
  }

  private NeuralNetwork neuralNetwork_;

  private int classificationLabel_;
}
