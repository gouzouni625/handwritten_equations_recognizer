package main.classifiers;

import java.io.IOException;

import main.base.NeuralNetwork;
import main.utilities.Utilities;
import main.utilities.traces.TraceGroup;
import main.distorters.ImageDistorter;

import org.opencv.core.Mat;
import org.opencv.core.Size;

public class NeuralNetworkClassifier extends Classifier{

  public NeuralNetworkClassifier(int[] sizesOfLayers, int maxTracesInSymbol){
    super(maxTracesInSymbol);

    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);
  }

  public void loadNeuralNetwork(String path) throws IOException{
    neuralNetwork_.loadNetwork(path);
  }

  public void newNeuralNetwork(int[] sizesOfLayers, String path) throws IOException{
    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);
    neuralNetwork_.loadNetwork(path);
  }

  public double classify(TraceGroup symbol, TraceGroup context, boolean subSymbolCheck, boolean subContextCheck){
    int symbolSize = symbol.size();
    int contextSize = context.size();

    if(symbolSize > maxTracesInSymbol_){
      return Classifier.MINIMUM_RATE;
    }

    double[] neuralNetworkOutput = this.feedForward(this.imageToVector(symbol.print(new Size(imageNumberOfRows_, imageNumberOfColumns_)), -1, 1));

    classificationLabel_ = Utilities.indexOfMax(neuralNetworkOutput);
    double symbolRate = neuralNetworkOutput[classificationLabel_];
    double finalRate = symbolRate;

    return finalRate;
  }

  private double[] feedForward(double[] imageVector){
    int numberOfDistortions = 100;

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

  public void setImageNumberOfRows(int imageNumberOfRows){
    imageNumberOfRows_ = imageNumberOfRows;
  }

  public int getImageNumberOfRows(){
    return imageNumberOfRows_;
  }

  public void setImageNumberOfColumns(int imageNumberOfColumns){
    imageNumberOfColumns_ = imageNumberOfColumns;
  }

  public int getImageNumberOfColumns(){
    return imageNumberOfColumns_;
  }

  public ImageDistorter getImageDistorter(){
    return imageDistorter_;
  }

  private NeuralNetwork neuralNetwork_;

  private int classificationLabel_;

  private boolean silent_ = true;

  private ImageDistorter imageDistorter_ = null;

  private int imageNumberOfRows_;
  private int imageNumberOfColumns_;

}
