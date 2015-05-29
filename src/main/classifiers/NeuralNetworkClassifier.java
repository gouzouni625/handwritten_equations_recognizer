package main.classifiers;

import java.io.IOException;

import main.utilities.TraceGroup;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import base.NeuralNetwork;

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
    double[] output = neuralNetwork_.feedForward(this.imageToVector(symbol.toImage(new Size(28, 28)), -1, 1));

    double max = output[0];
    int index = 0;
    for(int i = 1;i < output.length;i++){
      if(output[i] > max){
        max = output[i];
        index = i;
      }
    }
    classificationLabel_ = index;

    return max;
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
