package main.classifiers;

import java.io.IOException;

import main.base.NeuralNetwork;
import main.utilities.Utilities;
import main.utilities.traces.TraceGroup;
import main.distorters.ImageDistorter;

import org.opencv.core.Mat;
import org.opencv.core.Size;

/** @class NeuralNetworkClassifier
 *
 *  @brief Implements a Classifier using a main.base.NeuralNetwork as a classification algorithm.
 */
public class NeuralNetworkClassifier extends Classifier{
  /**
   *  @brief Constructor.
   *
   *  @param sizesOfLayers The sizes of the layers of the main.base.NeuralNetwork.
   *  @param maxTracesInSymbol The maximum number of main.utilities.traces.Trace objects in a
   *                           main.utilities.symbols.Symbol object.
   */
  public NeuralNetworkClassifier(int[] sizesOfLayers, int maxTracesInSymbol){
    super(maxTracesInSymbol);

    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);
  }

  /**
   *  @brief Loads a main.base.NeuralNetwork from a given path.
   *
   *  @param path The path where the main.base.NeuralNetwork parameters' file is located.
   *
   *  @throws IOException When main.base.NeuralNetwork.loadNetwork throws an exception.
   */
  public void loadNeuralNetwork(String path) throws IOException{
    neuralNetwork_.loadNetwork(path);
  }

  /**
   *  @brief Loads a new main.base.NeuralNetwork to this NeuralNetworkClassifier.
   *
   *  @param sizesOfLayers The sizes of the layers of the new the main.base.NeuralNetwork.
   *  @param path The path where the new main.base.NeuralNetwork parameters' file is located.
   *
   *  @throws IOException When main.base.NeuralNetwork.loadNetwork throws an exception.
   */
  public void newNeuralNetwork(int[] sizesOfLayers, String path) throws IOException{
    neuralNetwork_ = new NeuralNetwork(sizesOfLayers);
    neuralNetwork_.loadNetwork(path);
  }

  /**
   *  @brief Classifies a given main.utilities.symbols.Symbol.
   *
   *  Uses the main.base.NeuralNetwork to classify the given main.utilities.symbols.Symbol.
   *
   *  @param symbol The main.utilities.symbols.Symbol to classify.
   *  @param context The context of the given main.utilities.symbols.Symbol. The context of a
   *         main.utilities.symbols.Symbol contains all the main.utilities.traces.Trace objects that are near the
   *         but not part of the main.utilities.symbols.Symbol. The proximity measurement can be arbitrary.
   *  @param subSymbolCheck Check sub-groups of the main.utilities.traces.Trace objects of the given
   *                        main.utilities.symbols.Symbol
   *  @param subContextCheck Checks sub-groups of the main.utilities.traces.Trace objects of the context of the
   *                         given main.utilities.symbols.Symbol
   *
   *  @return Returns the confidence of this NeuralNetworkClassifier for the classification of the given
   *          main.utilities.symbols.Symbol.
   */
  public double classify(TraceGroup symbol, TraceGroup context, boolean subSymbolCheck, boolean subContextCheck){
    int symbolSize = symbol.size();
    //int contextSize = context.size();

    if(symbolSize > maxTracesInSymbol_){
      return Classifier.MINIMUM_RATE;
    }

    double[] neuralNetworkOutput = this.feedForward(this.imageToVector(symbol.print(new Size(imageNumberOfRows_,
                                                                                             imageNumberOfColumns_)),
                                                                                    -1, 1));

    classificationLabel_ = Utilities.indexOfMax(neuralNetworkOutput);
    double symbolRate = neuralNetworkOutput[classificationLabel_];
    double finalRate = symbolRate;

    return finalRate;
  }

  /**
   *  @brief Feeds the given vector to the main.base.NeuralNetwork.
   *
   *  The vector is subject to random distortions using an main.distorters.ImageDistorter and fed again and again to the
   *  main.base.NeuralNetwork. The final confidence of the main.base.NeuralNetwork is the average value of all these
   *  classifications.
   *
   *  @param imageVector The vector to be fed to the main.base.NeuralNetwork.
   *
   *  @return Returns the confidence of the main.base.NeuralNetwork for the classification.
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
   *  @return Returns the image convertion to a vector.
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
   *  @brief Setter method for the main.distorters.ImageDistorter.
   *
   *  @param imageDistorter The main.distorters.ImageDistorter.
   */
  public void setImageDistorter(ImageDistorter imageDistorter){
    imageDistorter_ = imageDistorter;
  }

  /**
   *  @brief Getter method for the main.distorters.ImageDistorter.
   *
   *  @return Returns the current main.distorters.ImageDistorter.
   */
  public ImageDistorter getImageDistorter(){
    return imageDistorter_;
  }

  /**
   *  @brief Setter method for the number of rows in each image sample.
   *
   *  @param imageNumberOfRows The number of rows in each image sample.
   */
  public void setImageNumberOfRows(int imageNumberOfRows){
    imageNumberOfRows_ = imageNumberOfRows;
  }

  /**
   *  @brief Getter method for the number of rows in each image sample.
   *
   *  @return Returns the number of rows in each image sample.
   */
  public int getImageNumberOfRows(){
    return imageNumberOfRows_;
  }

  /**
   *  @brief Setter method for the number of columns in each image sample.
   *
   *  @param imageNumberOfColumns The number of columns in each image sample.
   */
  public void setImageNumberOfColumns(int imageNumberOfColumns){
    imageNumberOfColumns_ = imageNumberOfColumns;
  }

  /**
   *  @brief Getter method for the number of columns in each image sample.
   *
   *  @return Returns the number of columns in each image sample.
   */
  public int getImageNumberOfColumns(){
    return imageNumberOfColumns_;
  }

  private NeuralNetwork neuralNetwork_; //!< The main.base.NeuralNetwork of this NeuralNetworkClassifier.

  private int classificationLabel_; //!< The label chosen by this NeuralNetworkClassifier during the classification.

  private boolean silent_ = true; //!< Flag for the silent mode of this NeuralNetworkClassifier.

  private ImageDistorter imageDistorter_ = null; //!< The main.distorters.ImageDistorter of this NeuralNetworkClassifier.

  private int imageNumberOfRows_; //!< The number of rows in each image sample.
  private int imageNumberOfColumns_; //!< The number of columns in each image sample.

}
