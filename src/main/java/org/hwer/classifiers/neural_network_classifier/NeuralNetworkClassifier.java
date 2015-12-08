package org.hwer.classifiers.neural_network_classifier;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.hwer.classifiers.Classifier;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.utilities.Utilities;
import org.hwer.classifiers.neural_network_classifier.image_processing.Core;
import org.hwer.classifiers.neural_network_classifier.image_processing.Distorter;


/** @class NeuralNetworkClassifier
 *
 *  @brief Implements a Classifier using a main.java.base.NeuralNetwork as a classification
 *         algorithm.
 */
public class NeuralNetworkClassifier extends Classifier {
  /** @class NeuralNetwork
   *
   *  @brief Implements a feed forward NeuralNetwork.
   *
   *  The implementation includes the NeuralNetwork parameters, training and evaluating methods.
   */
  public static class NeuralNetwork{

    public NeuralNetwork(){
    }

    /**
     *  @brief Constructor
     *
     *  Randomly initializes the NeuralNetwork parameters with values in [-0.25, 0.25).
     *
     *  @param sizesOfLayers The number of neurons in each layer including the input and output layers. The number of layers
     *         is equal to sizesOfLayers.length.
     */
    public NeuralNetwork(int[] sizesOfLayers){
      sizesOfLayers_ = sizesOfLayers;
      numberOfLayers_ = sizesOfLayers_.length;

      weights_ = new double[numberOfLayers_ - 1][][];
      biases_ = new double[numberOfLayers_ - 1][];

      for(int i = 0;i < numberOfLayers_ - 1;i++){
        weights_[i] = new double[sizesOfLayers_[i + 1]][];
        biases_[i] = new double[sizesOfLayers_[i + 1]];

        for(int j = 0;j < sizesOfLayers_[i + 1];j++){
          weights_[i][j] = new double[sizesOfLayers_[i]];

          for(int k = 0;k < sizesOfLayers_[i];k++){
            weights_[i][j][k] = ((double)Math.random()) * 0.5 - 0.25;
          }
          biases_[i][j] = ((double)Math.random()) * 0.5 - 0.25;
        }
      }
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

    /**
     *  @brief Saves the parameters of this NeuralNetwork to a binary file.
     *
     *  @param path The absolute, or relative path of the file where the parameters will be saved.
     *
     *  @throws IOException When an exception occurs while writing on the file.
     */
    public void saveToBinary(String path) throws IOException{
      FileOutputStream fileOutputStream = new FileOutputStream(path);
      DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

      dataOutputStream.writeInt(numberOfLayers_);
      for(int i = 0;i < numberOfLayers_;i++){
        dataOutputStream.writeInt(sizesOfLayers_[i]);
      }

      for(int i = 0;i < numberOfLayers_ - 1;i++){
        for(int j = 0;j < sizesOfLayers_[i + 1];j++){
          dataOutputStream.writeDouble(biases_[i][j]);

          for(int k = 0;k < sizesOfLayers_[i];k++){
            dataOutputStream.writeDouble(weights_[i][j][k]);
          }
        }
      }

      dataOutputStream.close();
    }

    /**
     *  @brief Saves the parameters of this NeuralNetwork to an xml file.
     *
     *  @param path The absolute or relative path of the file where the parameters will be saved.
     *
     *  @throws IOException When an exception occurs while writing on the file.
     */
    public void saveToXML(String path) throws IOException{
      PrintWriter printWriter = new PrintWriter(path);

      printWriter.print("<sizes_of_layers>");
      for(int i = 0;i < numberOfLayers_ - 1;i++){
        printWriter.print(sizesOfLayers_[i] + " ");
      }
      printWriter.println(sizesOfLayers_[numberOfLayers_ - 1] + "</sizes_of_layers>");

      for(int i = 0;i < numberOfLayers_ - 1;i++){
        printWriter.println("<layer>");

        for(int j = 0;j < sizesOfLayers_[i + 1];j++){
          printWriter.print("  <neuron>\n    <bias>" + biases_[i][j] + "</bias>\n    <weights>");

          for(int k = 0;k < sizesOfLayers_[i] - 1;k++){
            printWriter.print(weights_[i][j][k] + " ");
          }
          printWriter.println(weights_[i][j][sizesOfLayers_[i] - 1] + "</weights>\n  </neuron>");
        }

        printWriter.println("</layer>");
      }

      printWriter.close();
    }

    /**
     *  @brief Loads the parameters for this NeuralNetwork from a binary file.
     *
     *  @param path The path of the file where the parameters are saved.
     *
     *  @throws IOException When an exception occurs while reading from the file.
     */
    public void loadFromBinary(String path) throws IOException{
      DataInputStream dataInputStream = new DataInputStream(new FileInputStream(path));

      loadFromDataInputStream(dataInputStream);
    }

    public void loadFromInputStream(InputStream inputStream) throws IOException {
      DataInputStream dataInputStream = new DataInputStream(inputStream);

      loadFromDataInputStream(dataInputStream);
    }

    public void loadFromDataInputStream(DataInputStream dataInputStream) throws IOException {
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
    }

    /**
     *  @brief Loads the parameters for this NeuralNetwork from an xml file.
     *
     *  @param path The path of the file where the parameters are saved.
     *
     *  @throws IOException When an exception occurs while reading from the file.
     */
    public void loadFromXML(String path) throws IOException{
      // Load all the data from the given file.
      String xmlData = new String(Files.readAllBytes(Paths.get(path)));

      // Parse the sizes of the layers.
      int startOfSizesOfLayers = xmlData.indexOf("<sizes_of_layers>");
      int endOfSizesOfLayers = xmlData.indexOf("</sizes_of_layers>");

      String[] sizesOfLayers = xmlData.substring(startOfSizesOfLayers + ("<sizes_of_layers>").length(), endOfSizesOfLayers)
          .split(" ");

      numberOfLayers_ = sizesOfLayers.length;
      sizesOfLayers_ = new int[numberOfLayers_];
      for(int i = 0;i < numberOfLayers_;i++){
        sizesOfLayers_[i] = Integer.parseInt(sizesOfLayers[i]);
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

      xmlData = xmlData.substring(endOfSizesOfLayers + ("</sizes_of_layers>").length());

      // Parse each layer.
      int startOfLayer = xmlData.indexOf("<layer>");
      int currentLayer = 0;
      while(startOfLayer != -1){

        // Parse each neuron in the current layer.
        int currentNeuron = 0;
        while(currentNeuron < sizesOfLayers_[currentLayer + 1]){

          int startOfBias = xmlData.indexOf("<bias>");
          int endOfBias = xmlData.indexOf("</bias>");

          biases_[currentLayer][currentNeuron] = Double.parseDouble(xmlData.substring(startOfBias + ("<bias>").length(),
              endOfBias));

          int startOfWeights = xmlData.indexOf("<weights>");
          int endOfWeights = xmlData.indexOf("</weights>");

          String[] weights = xmlData.substring(startOfWeights + ("<weights>").length(), endOfWeights).split(" ");
          for(int i = 0;i < weights.length;i++){
            weights_[currentLayer][currentNeuron][i] = Double.parseDouble(weights[i]);
          }

          xmlData = xmlData.substring(xmlData.indexOf("</neuron>") + ("</neuron>").length());
          currentNeuron++;
        }

        xmlData = xmlData.substring(xmlData.indexOf("</layer>") + ("</layer>").length());
        startOfLayer = xmlData.indexOf("<layer>");
        currentLayer++;
      }
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


    private int numberOfLayers_; //!< The number of layers of this NeuralNetwork.
    private int[] sizesOfLayers_; //!< The number of neurons in each layer.

    private double[][][] weights_; //!< The weight parameters of this NeuralNetwork. dimensions are: layer, neuron, weight.
    private double[][] biases_; //!< The bias parameters of this NeuralNetwork. dimensions are: layer, neuron.

  }


  /**
   *  @brief Constructor.
   *
   *  @param sizesOfLayers The sizes of the layers of the main.java.base.NeuralNetwork.
   *  @param maxTracesInSymbol The maximum number of main.java.utilities.traces.Trace objects in a
   *                           main.java.utilities.symbols.Symbol object.
   */
  public NeuralNetworkClassifier(int[] sizesOfLayers, int maxTracesInSymbol){
    super(maxTracesInSymbol);

    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);

    imageSide_ = (int) Math.sqrt(sizesOfLayers[0]);
  }

  /**
   *  @brief Constructor.
   *
   *  @param neuralNetwork The main.java.base.NeuralNetwork to be used.
   *  @param maxTracesInSymbol The maximum number of main.java.utilities.traces.Trace objects in a
   *                           main.java.utilities.symbols.Symbol object.
   */
  public NeuralNetworkClassifier(NeuralNetwork neuralNetwork, int maxTracesInSymbol){
    super(maxTracesInSymbol);

    neuralNetwork_ = neuralNetwork;

    imageSide_ = (int) Math.sqrt(neuralNetwork.getSizesOfLayers()[0]);
  }

  /**
   *  @brief Loads a main.java.base.NeuralNetwork from a given path.
   *
   *  @param path The path where the main.java.base.NeuralNetwork parameters' file is located.
   *
   *  @throws IOException When main.java.base.NeuralNetwork.loadNetwork throws an exception.
   */
  public void loadNeuralNetwork(String path) throws IOException{
    neuralNetwork_.loadFromBinary(path);

    imageSide_ = (int) Math.sqrt(neuralNetwork_.getSizesOfLayers()[0]);
  }

  /**
   *  @brief Loads a new main.java.base.NeuralNetwork to this NeuralNetworkClassifier.
   *
   *  @param sizesOfLayers The sizes of the layers of the new the main.java.base.NeuralNetwork.
   *  @param path The path where the new main.java.base.NeuralNetwork parameters' file is located.
   *
   *  @throws IOException When main.java.base.NeuralNetwork.loadNetwork throws an exception.
   */
  public void newNeuralNetwork(int[] sizesOfLayers, String path) throws IOException{
    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);
    neuralNetwork_.loadFromBinary(path);

    imageSide_ = (int) Math.sqrt(neuralNetwork_.getSizesOfLayers()[0]);
  }

  /**
   *  @brief Classifies a given main.java.utilities.symbols.Symbol.
   *
   *  Uses the main.java.base.NeuralNetwork to classify the given
   *  main.java.utilities.symbols.Symbol.
   *
   *  @param symbol The main.java.utilities.symbols.Symbol to classify.
   *  @param context The context of the given main.java.utilities.symbols.Symbol. The context of a
   *                 main.java.utilities.symbols.Symbol contains all the
   *                 main.java.utilities.traces.Trace objects that are near the but not part of the
   *                 main.java.utilities.symbols.Symbol. The proximity measurement can be arbitrary.
   *  @param subSymbolCheck Check sub-groups of the main.java.utilities.traces.Trace objects of the
   *                        given main.java.utilities.symbols.Symbol
   *  @param subContextCheck Checks sub-groups of the main.java.utilities.traces.Trace objects of
   *                         the context of the given main.java.utilities.symbols.Symbol
   *
   *  @return Returns the confidence of this NeuralNetworkClassifier for the classification of the
   *          given main.java.utilities.symbols.Symbol.
   */
  public double classify(TraceGroup symbol, TraceGroup context, boolean subSymbolCheck,
                         boolean subContextCheck){
    int symbolSize = symbol.size();

    if(symbolSize > maxTracesInSymbol_){
      return MINIMUM_RATE;
    }

    BufferedImage image = imageProcessor_.printTraceGroup(symbol, imageSide_, imageSide_, 30);

    double[] neuralNetworkOutput = this.feedForward(image);

    classificationLabel_ = Utilities.indexOfMax(neuralNetworkOutput);
    double symbolRate = neuralNetworkOutput[classificationLabel_];

    return symbolRate;
  }

  /**
   *  @brief Feeds the given vector to the main.java.base.NeuralNetwork.
   *
   *  The vector is subject to random distortions using an main.java.distorters.ImageDistorter and
   *  fed again and again to the main.java.base.NeuralNetwork. The final confidence of the
   *  main.java.base.NeuralNetwork is the average value of all these classifications.
   *
   *  @param image The vector to be fed to the main.java.base.NeuralNetwork.
   *
   *  @return Returns the confidence of the main.java.base.NeuralNetwork for the classification.
   */
  private double[] feedForward(BufferedImage image){
    int numberOfDistortions = 100;

    BufferedImage imageClone = new BufferedImage(image.getWidth(), image.getHeight(),
        image.getType());

    double[] neuralNetworkOutput = neuralNetwork_.feedForward(
        imageDistorter_.bufferedImageToVector(image, -1, 1));

    if(imageDistorter_ != null){
      for(int i = 0;i < numberOfDistortions;i++){
        imageClone.setData(image.getRaster());

        imageClone = imageDistorter_.distort(imageClone);

        double[] currentOutput = neuralNetwork_.feedForward(
            imageDistorter_.bufferedImageToVector(imageClone, -1, 1));
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

    neuralNetworkOutput = Utilities.normalizeArray(neuralNetworkOutput);

    return neuralNetworkOutput;
  }

  /**
   *  @brief Getter method for the label chosen by this NeuralNetworkClassifier.
   *
   *  @return Returns the chosen label.
   */
  public int getClassificationLabel(){
    return classificationLabel_;
  }

  /**
   *  @brief Setter method for the silent mode of this NeuralNetworkClassifier.
   *
   *  @param silent The value for the silent mode.
   */
  public void setSilent(boolean silent){
    silent_ = silent;
  }

  /**
   *  @brief Getter method for the silent mode of this NeuralNetworkClassifier.
   *
   *  @return Returns the current state of the silent mode.
   */
  public boolean isSilent(){
    return silent_;
  }

  /**
   *  @brief Setter method for the main.java.distorters.ImageDistorter.
   *
   *  @param imageDistorter The main.java.distorters.ImageDistorter.
   */
  public void setImageDistorter(Distorter imageDistorter){
    imageDistorter_ = imageDistorter;
  }

  /**
   *  @brief Getter method for the main.java.distorters.ImageDistorter.
   *
   *  @return Returns the current main.java.distorters.ImageDistorter.
   */
  public Distorter getImageDistorter(){
    return imageDistorter_;
  }

  public void setImageProcessor (Core imageProcessor) {
    imageProcessor_ = imageProcessor;
  }

  public Core getImageProcessor(){
    return imageProcessor_;
  }

  private NeuralNetwork neuralNetwork_; //!< The NeuralNetwork of this NeuralNetworkClassifier.

  private int classificationLabel_; //!< The label chosen by this NeuralNetworkClassifier during
                                    //   the classification.

  private boolean silent_ = true; //!< Flag for the silent mode of this NeuralNetworkClassifier.

  private Distorter imageDistorter_ = null; //!< The ImageDistorter of this
                                                 //   NeuralNetworkClassifier.

  private Core imageProcessor_ = null;

  private int imageSide_;
}
