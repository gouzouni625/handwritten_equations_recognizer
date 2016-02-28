package org.hwer.implemented_classifiers.neural_network_classifier.neural_network;

import org.hwer.implemented_classifiers.neural_network_classifier.neural_network.image_processing.Core;
import org.hwer.implemented_classifiers.neural_network_classifier.neural_network.image_processing.Distorter;
import org.hwer.implemented_classifiers.neural_network_classifier.neural_network.image_processing.Image;
import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

/** @class NeuralNetwork
 *
 *  @brief Implements a feed forward NeuralNetwork.
 *
 *  The implementation includes the NeuralNetwork parameters, training and evaluating methods.
 */
public class NeuralNetworkImpl implements NeuralNetwork{

  public NeuralNetworkImpl (Core imageProcessor, Distorter imageDistorter){
    imageProcessor_ = imageProcessor;
    imageDistorter_ = imageDistorter;
  }

  /**
   *  @brief Applies an input to this NeuralNetwork and returns its output.
   *
   *  @param input The input to this NeuralNetwork. The length of the input must be equal to sizesOfLayers_[0].
   *
   *  @return Returns the output of this NeuralNetwork for the given input. The length of the output is equal to
   *          sizesOfLayers_[sizesOfLayers_.length - 1].
   */
  public double[] feedForward(double[] input){
    double[] inputBuffer = input.clone();

    double[] output = new double[sizesOfLayers_[1]];
    for(int i = 0;i < numberOfLayers_ - 1;i++){
      for(int j = 0;j < sizesOfLayers_[i + 1];j++){
        double sum = 0;
        for(int k = 0;k < sizesOfLayers_[i];k++){
          sum += weights_[i][j][k] * inputBuffer[k];
        }

        output[j] = this.activationFunction(sum + biases_[i][j]);
      }

      if(i + 2 < numberOfLayers_){
        inputBuffer = output.clone();
        output = new double[sizesOfLayers_[i + 2]];
      }
    }

    return output;
  }

  /**
   *  @brief Getter function for the number of this NeuralNetwork layers.
   *
   *  \return The number of this NeuralNetwork layers.
   */
  public int getNumberOfLayers(){
    return numberOfLayers_;
  }

  public void loadFromInputStream(InputStream inputStream) throws IOException {
    DataInputStream dataInputStream = new DataInputStream(inputStream);

    numberOfLayers_ = dataInputStream.readInt();
    sizesOfLayers_ = new int[numberOfLayers_];
    for(int i = 0;i < numberOfLayers_;i++){
      sizesOfLayers_[i] = dataInputStream.readInt();
    }

    // Allocate the needed memory for the weights and the biases.
    weights_ = new double[numberOfLayers_ - 1][][];
    biases_ = new double[numberOfLayers_ - 1][];

    for(int i = 0;i < numberOfLayers_ - 1;i++){
      weights_[i] = new double[sizesOfLayers_[i + 1]][];
      biases_[i] = new double[sizesOfLayers_[i + 1]];

      for(int j = 0;j < sizesOfLayers_[i + 1];j++){
        weights_[i][j] = new double[sizesOfLayers_[i]];
      }
    }

    for(int i = 0;i < numberOfLayers_ - 1;i++){
      for(int j = 0;j < sizesOfLayers_[i + 1];j++){
        biases_[i][j] = dataInputStream.readDouble();

        for(int k = 0;k < sizesOfLayers_[i];k++){
          weights_[i][j][k] = dataInputStream.readDouble();
        }
      }
    }

    dataInputStream.close();

    imageSide_ = (int)Math.sqrt(sizesOfLayers_[0]);
  }

  /**
   *  @brief The activation function for each neuron.
   *
   *  @param z The independent variable.
   *
   *  @return The value of the activation function at z.
   */
  private double activationFunction(double z){
    return (sigmoid(z));
  }

  /**
   *  @brief Implements the sigmoid function.
   *
   *  This function is used as an activation function for the neurons.
   *
   *  @param z The independent variab.e
   *
   *  @return Returns the value of the sigmoid function on z.
   */
  private double sigmoid(double z){
    return (1 / (1 + Math.exp(-z)));
  }

  /**
   *  @brief Getter method for the sizes of layers of this independent.
   *
   *  @return Returns the sizes of the layers of this independent.
   */
  public int[] getSizesOfLayers(){
    return sizesOfLayers_;
  }

  /**
   *  @brief Feeds the given vector to the main.java.base.NeuralNetwork.
   *
   *  The vector is subject to random distortions using an main.java.distorters.ImageDistorter and
   *  fed again and again to the main.java.base.NeuralNetwork. The final confidence of the
   *  main.java.base.NeuralNetwork is the average value of all these classifications.
   *
   *  @param symbol The vector to be fed to the main.java.base.NeuralNetwork.
   *
   *  @return Returns the confidence of the main.java.base.NeuralNetwork for the classification.
   */
  public double[] evaluate (TraceGroup symbol, int times) {
    Image image = imageProcessor_.printTraceGroup(symbol, imageSide_, imageSide_, 1);

    Image imageClone;

    double[] neuralNetworkOutput = feedForward(image.toVector(- 1, 1));

    if(imageDistorter_ != null){
      for(int i = 0;i < times;i++){
        imageClone = image.clone();

        imageClone = imageDistorter_.distort(imageClone);

        double[] currentOutput = feedForward(imageClone.toVector(- 1, 1));
        for(int j = 0;j < neuralNetworkOutput.length;j++){
          neuralNetworkOutput[j] += currentOutput[j];
        }

      }

      if(times > 0){
        for(int i = 0;i < neuralNetworkOutput.length;i++){
          neuralNetworkOutput[i] /= times;
        }
      }
    }

    neuralNetworkOutput = Utilities.normalizeArray(neuralNetworkOutput);

    return neuralNetworkOutput;
  }

  public void setImageSide(int imageSide){
    imageSide_ = imageSide;
  }

  private int numberOfLayers_; //!< The number of layers of this NeuralNetwork.
  private int[] sizesOfLayers_; //!< The number of neurons in each layer.

  private double[][][] weights_; //!< The weight parameters of this NeuralNetwork. dimensions are: layer, neuron, weight.
  private double[][] biases_; //!< The bias parameters of this NeuralNetwork. dimensions are: layer, neuron.

  private Core imageProcessor_ = null;

  private Distorter imageDistorter_ = null;

  private int imageSide_;
}
