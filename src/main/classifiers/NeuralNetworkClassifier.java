package main.classifiers;

import java.io.IOException;

import main.base.NeuralNetwork;
import main.utilities.Utilities;
import main.utilities.traces.TraceGroup;
import main.distorters.ImageDistorter;

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

  public double classify(TraceGroup symbol, TraceGroup context){
    int symbolSize = symbol.size();
    int contextSize = context.size();

    if(symbolSize > Utilities.MAX_TRACES_IN_SYMBOL){
      return Utilities.MINIMUM_RATE;
    }

    double[] neuralNetworkOutput = this.feedForward(this.imageToVector(symbol.print(new Size(28, 28)), -1, 1));

    classificationLabel_ = Utilities.indexOfMax(neuralNetworkOutput);
    double symbolRate = neuralNetworkOutput[classificationLabel_];

    boolean[][] connections = new boolean[symbolSize][symbolSize];
    for(int i = 0;i < symbolSize;i++){
      for(int j = 0;j < symbolSize;j++){
        connections[i][j] = true;
      }
    }
    int[][] symbolSubPaths = Utilities.findUniquePaths(connections, Utilities.MAX_TRACES_IN_SYMBOL);
    int numberOfSubPaths = symbolSubPaths.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: symbol sub-paths... ===== Start =====");

      System.out.println("number of symbol sub-paths: " + numberOfSubPaths);
      for(int i = 0;i < numberOfSubPaths;i++){
        System.out.print("sub-path: " + i + " ");
        for(int j = 0;j < symbolSubPaths[i].length;j++){
          System.out.print(symbolSubPaths[i][j] + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: symbol sub-paths... ===== End =======");
    }
    /* ===== Logs ===== */

    double finalRate = symbolRate;
    TraceGroup subSymbol;
    for(int i = 0;i < numberOfSubPaths;i++){
      subSymbol = new TraceGroup(symbol.subTraceGroup(symbolSubPaths[i]));

      neuralNetworkOutput = this.feedForward(this.imageToVector(subSymbol.print(new Size(28, 28)), -1, 1));

      double rate = Utilities.maxValue(neuralNetworkOutput);

      /* ===== Logs ===== */
      if(!silent_){
        System.out.println("Log: symbol sub-paths' rates... ===== Start =====");

        System.out.println("sub-path " + i + " rate: " + rate);

        System.out.println("Log: symbol sub-paths' rates... ===== End =======");
      }
      /* ===== Logs ===== */

      if(rate < (Utilities.MAXIMUM_RATE - Utilities.MINIMUM_RATE) / 2){
        continue;
      }

      finalRate -= 0.33 * rate;
    }

    // Process context.
    if(symbolSize == Utilities.MAX_TRACES_IN_SYMBOL || contextSize == 0){
      return finalRate;
    }

    connections = new boolean[contextSize][contextSize];
    for(int i = 0;i < contextSize;i++){
      for(int j = 0;j < contextSize;j++){
        connections[i][j] = true;
      }
    }
    int[][] contextPaths = Utilities.findUniquePaths(connections, Utilities.MAX_TRACES_IN_SYMBOL - symbolSize);
    int numberOfContextPaths = contextPaths.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: context paths... ===== Start =====");

      System.out.println("number of context paths: " + numberOfSubPaths);
      for(int i = 0;i < numberOfSubPaths;i++){
        System.out.print("path: " + i + " ");
        for(int j = 0;j < symbolSubPaths[i].length;j++){
          System.out.print(symbolSubPaths[i][j] + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: context paths... ===== End =======");
    }
    /* ===== Logs ===== */

    TraceGroup symbolWithContext;
    for(int i = 0;i < numberOfContextPaths;i++){
      symbolWithContext = new TraceGroup(symbol);
      symbolWithContext.add(context.subTraceGroup(contextPaths[i]));

      neuralNetworkOutput = this.feedForward(this.imageToVector(symbolWithContext.print(new Size(28, 28)), -1, 1));

      double rate = Utilities.maxValue(neuralNetworkOutput);

      /* ===== Logs ===== */
      if(!silent_){
        System.out.println("Log: context paths' rates... ===== Start =====");

        System.out.println("path " + i + " rate: " + rate);

        System.out.println("Log: context paths' rates... ===== End =======");
      }
      /* ===== Logs ===== */

      if(rate < (Utilities.MAXIMUM_RATE - Utilities.MINIMUM_RATE) / 2){
        continue;
      }

      finalRate -= 33 / 100 * rate;
    }

    return finalRate;
  }

  private double[] feedForward(double[] imageVector){
    int numberOfDistortions = 5;

    double[] neuralNetworkOutput = neuralNetwork_.feedForward(imageVector);

    if(imageDistorter_ != null){
      double[][] dataToDistort = new double[1][];

      for(int i = 0;i < numberOfDistortions;i++){
        dataToDistort[0] = imageVector.clone();
        imageDistorter_.distort(dataToDistort);

        double[] currentOutput = neuralNetwork_.feedForward(dataToDistort[0]);
        for(int j = 0;j < neuralNetworkOutput.length;j++){
          neuralNetworkOutput[j] += currentOutput[j];
        }

      }

      if(numberOfDistortions > 0){
        for(int i = 0;i < neuralNetworkOutput.length;i++){
          neuralNetworkOutput[i] /= numberOfDistortions;
        }
      }
    }

    neuralNetworkOutput = Utilities.relativeValues(neuralNetworkOutput);

    return neuralNetworkOutput;
  }

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

  public void setSilent(boolean silent){
    silent_ = silent;
  }

  public boolean getSilent(){
    return silent_;
  }

  public void setImageDistorter(ImageDistorter imageDistorter){
    imageDistorter_ = imageDistorter;
  }

  public ImageDistorter getImageDistorter(){
    return imageDistorter_;
  }

  private NeuralNetwork neuralNetwork_;

  private int classificationLabel_;

  private boolean silent_ = true;

  private ImageDistorter imageDistorter_ = null;

}
