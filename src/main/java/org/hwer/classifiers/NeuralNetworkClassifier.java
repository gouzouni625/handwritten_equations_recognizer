package org.hwer.classifiers;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.nn.base.NeuralNetwork;
import org.nn.distorters.ImageDistorter;
import org.hwer.utilities.Utilities;
import org.hwer.utilities.traces.TraceGroup;


/** @class NeuralNetworkClassifier
 *
 *  @brief Implements a Classifier using a main.java.base.NeuralNetwork as a classification
 *         algorithm.
 */
public class NeuralNetworkClassifier extends Classifier{
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

    BufferedImage image = symbol.print(imageDistorter_.getSampleColumns(),
        imageDistorter_.getSampleRows(), 30);

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

    double[] neuralNetworkOutput = neuralNetwork_.feedForward(this.imageToVector(image, -1, 1));

    if(imageDistorter_ != null){
      for(int i = 0;i < numberOfDistortions;i++){
        imageClone.setData(image.getRaster());

        imageClone = imageDistorter_.distort(imageClone);

        double[] currentOutput = neuralNetwork_.feedForward(this.imageToVector(imageClone, -1, 1));
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
   *  @brief Converts an image to a vector of doubles.
   *
   *  @param image The OpenCV Mat object that represents the image.
   *  @param min The minimum value that the vector should have.
   *  @param max The maximum value that the vector should have.
   *
   *  @return Returns the image conversion to a vector.
   */
  private double[] imageToVector(BufferedImage image, double min, double max){
    // Find the minimum and the maximum values of the image.
    double minValue = image.getRGB(0, 0) & 0xFF;
    double maxValue = image.getRGB(0, 0) & 0xFF;

    int width = image.getWidth();
    int height = image.getHeight();
    for(int x = 0;x < width;x++){
      for(int y = 0;y < height;y++){
        if((image.getRGB(x, y) & 0xFF) > maxValue){
          maxValue = image.getRGB(x, y) & 0xFF;
        }

        if((image.getRGB(x, y) & 0xFF) < minValue){
          minValue = image.getRGB(x, y) & 0xFF;
        }
      }
    }

    double[] vector = new double[width * height];

    for(int y = 0;y < height;y++){
      for(int x = 0;x < width;x++){
        vector[y * width + x] = ((image.getRGB(x, height - y - 1) & 0xFF) - minValue) *
            (max - min) / (maxValue - minValue) + min;
        /*if(vector[y * width + x] == - 1){
          System.out.print(0 + " ");
        }
        else {
          System.out.print(1 + " ");
          vector[y * width + x] = 1;
        }*/
      }
      // System.out.println();
    }

    return vector;
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
  public void setImageDistorter(ImageDistorter imageDistorter){
    imageDistorter_ = imageDistorter;
  }

  /**
   *  @brief Getter method for the main.java.distorters.ImageDistorter.
   *
   *  @return Returns the current main.java.distorters.ImageDistorter.
   */
  public ImageDistorter getImageDistorter(){
    return imageDistorter_;
  }

  private NeuralNetwork neuralNetwork_; //!< The NeuralNetwork of this NeuralNetworkClassifier.

  private int classificationLabel_; //!< The label chosen by this NeuralNetworkClassifier during
                                    //   the classification.

  private boolean silent_ = true; //!< Flag for the silent mode of this NeuralNetworkClassifier.

  private ImageDistorter imageDistorter_ = null; //!< The ImageDistorter of this
                                                 //   NeuralNetworkClassifier.

}
