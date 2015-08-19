package main.java.classifiers;

import java.io.IOException;

import main.java.base.NeuralNetwork;
import main.java.distorters.ImageDistorter;
import main.java.utilities.Utilities;
import main.java.utilities.traces.TraceGroup;

import org.opencv.core.Mat;
import org.opencv.core.Size;

/** @class NeuralNetworkClassifier
 *
 *  @brief Implements a Classifier using a main.java.base.NeuralNetwork as a classification algorithm.
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
   *  Uses the main.java.base.NeuralNetwork to classify the given main.java.utilities.symbols.Symbol.
   *
   *  @param symbol The main.java.utilities.symbols.Symbol to classify.
   *  @param context The context of the given main.java.utilities.symbols.Symbol. The context of a
   *         main.java.utilities.symbols.Symbol contains all the main.java.utilities.traces.Trace objects that are near the
   *         but not part of the main.java.utilities.symbols.Symbol. The proximity measurement can be arbitrary.
   *  @param subSymbolCheck Check sub-groups of the main.java.utilities.traces.Trace objects of the given
   *                        main.java.utilities.symbols.Symbol
   *  @param subContextCheck Checks sub-groups of the main.java.utilities.traces.Trace objects of the context of the
   *                         given main.java.utilities.symbols.Symbol
   *
   *  @return Returns the confidence of this NeuralNetworkClassifier for the classification of the given
   *          main.java.utilities.symbols.Symbol.
   */
  public double classify(TraceGroup symbol, TraceGroup context, boolean subSymbolCheck, boolean subContextCheck){
    int symbolSize = symbol.size();
    //int contextSize = context.size();

    if(symbolSize > maxTracesInSymbol_){
      return Classifier.MINIMUM_RATE;
    }

    double[] neuralNetworkOutput = this.feedForward(this.imageToVector(symbol.print(
                                   new Size(imageDistorter_.getSampleRows(), imageDistorter_.getSampleColumns())), -1, 1));

    classificationLabel_ = Utilities.indexOfMax(neuralNetworkOutput);
    double symbolRate = neuralNetworkOutput[classificationLabel_];
    double finalRate = symbolRate;

    return finalRate;
  }

  /**
   *  @brief Feeds the given vector to the main.java.base.NeuralNetwork.
   *
   *  The vector is subject to random distortions using an main.java.distorters.ImageDistorter and fed again and again to
   *  the main.java.base.NeuralNetwork. The final confidence of the main.java.base.NeuralNetwork is the average value of
   *  all these classifications.
   *
   *  @param imageVector The vector to be fed to the main.java.base.NeuralNetwork.
   *
   *  @return Returns the confidence of the main.java.base.NeuralNetwork for the classification.
   */
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

  private NeuralNetwork neuralNetwork_; //!< The main.java.base.NeuralNetwork of this NeuralNetworkClassifier.

  private int classificationLabel_; //!< The label chosen by this NeuralNetworkClassifier during the classification.

  private boolean silent_ = true; //!< Flag for the silent mode of this NeuralNetworkClassifier.

  private ImageDistorter imageDistorter_ = null; //!< The ImageDistorter of this NeuralNetworkClassifier.

}
