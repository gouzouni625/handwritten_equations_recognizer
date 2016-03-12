package org.hwer.implementations.classifiers.nnclassifier;

import org.hwer.engine.parsers.symbols.Symbol;
import org.hwer.implementations.classifiers.nnclassifier.neural_network.NeuralNetwork;
import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.utilities.Utilities;
import org.hwer.implementations.classifiers.nnclassifier.symbols.SymbolFactory;


/** @class NeuralNetworkClassifier
 *
 *  @brief Implements a Classifier using a main.java.base.NeuralNetwork as a classification
 *         algorithm.
 */
public class NNClassifier extends Classifier {
  /**
   *  @brief Constructor.
   *
   *  @param neuralNetwork The main.java.base.NeuralNetwork to be used.
   */
  public NNClassifier (NeuralNetwork neuralNetwork){
    neuralNetwork_ = neuralNetwork;
  }

  public void registerSymbol(Class<?> clazz){
    symbolFactory_.registerSymbol(clazz);
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
  @Override
  public Symbol classify(TraceGroup symbol, TraceGroup context, boolean subSymbolCheck, boolean subContextCheck){
    double[] neuralNetworkOutput = neuralNetwork_.evaluate(symbol, 1);

    int classificationLabel = Utilities.indexOfMax(neuralNetworkOutput);

    Symbol symbolObject = null;
    try {
      symbolObject = symbolFactory_.createByLabel(symbol, classificationLabel);

      symbolObject.setConfidence(neuralNetworkOutput[classificationLabel]);
    }
    catch (Exception exception){
      exception.printStackTrace();
    }

    return symbolObject;
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


  private SymbolFactory symbolFactory_ = SymbolFactory.getInstance();

  private NeuralNetwork neuralNetwork_ = null; //!< The NeuralNetwork of this NeuralNetworkClassifier.

  private boolean silent_ = true; //!< Flag for the silent mode of this NeuralNetworkClassifier.

}
