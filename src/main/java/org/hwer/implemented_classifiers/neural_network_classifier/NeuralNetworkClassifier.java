package org.hwer.implemented_classifiers.neural_network_classifier;

import org.hwer.implemented_classifiers.neural_network_classifier.neural_network.NeuralNetwork;
import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.utilities.Utilities;


/** @class NeuralNetworkClassifier
 *
 *  @brief Implements a Classifier using a main.java.base.NeuralNetwork as a classification
 *         algorithm.
 */
public class NeuralNetworkClassifier extends Classifier {
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

    double[] neuralNetworkOutput = neuralNetwork_.evaluate(symbol, 1);

    classificationLabel_ = Utilities.indexOfMax(neuralNetworkOutput);
    double symbolRate = neuralNetworkOutput[classificationLabel_];

    return symbolRate;
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


  private NeuralNetwork neuralNetwork_; //!< The NeuralNetwork of this NeuralNetworkClassifier.

  private int classificationLabel_; //!< The label chosen by this NeuralNetworkClassifier during
                                    //   the classification.

  private boolean silent_ = true; //!< Flag for the silent mode of this NeuralNetworkClassifier.

}
