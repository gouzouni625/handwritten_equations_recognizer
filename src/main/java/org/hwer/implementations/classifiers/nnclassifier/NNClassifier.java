package org.hwer.implementations.classifiers.nnclassifier;

import org.hwer.engine.symbols.Symbol;
import org.hwer.implementations.classifiers.nnclassifier.neural_network.NeuralNetwork;
import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;


/** @class NeuralNetworkClassifier
 *
 *  @brief Implements a Classifier using a main.java.base.NeuralNetwork as a classification
 *         algorithm.
 */
public class NNClassifier extends Classifier {
  /**
   *  @brief Constructor.
   *
   */
  public NNClassifier (NeuralNetwork cascadeNeuralNetwork, NeuralNetwork[] neuralNetworks){
    cascadeNeuralNetwork_ = cascadeNeuralNetwork;
    neuralNetworks_ = neuralNetworks;
  }

  public NNClassifier (NeuralNetwork cascadeNeuralNetwork, NeuralNetwork[] neuralNetworks,
                       Labels[][] symbolLabels){
    cascadeNeuralNetwork_ = cascadeNeuralNetwork;
    neuralNetworks_ = neuralNetworks;

    symbolLabels_ = symbolLabels;
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
    double[] cascadeNeuralNetworkOutput = cascadeNeuralNetwork_.evaluate(symbol, 0);

    int classLabel = Utilities.indexOfMax(cascadeNeuralNetworkOutput);

    double[] neuralNetworkOutput = neuralNetworks_[classLabel].evaluate(symbol, 0);

    int classificationLabel = Utilities.indexOfMax(neuralNetworkOutput);

    Symbol symbolObject = null;
    try {
      symbolObject = symbolFactory_.create(symbolLabels_[classLabel][classificationLabel], symbol);

      symbolObject.setConfidence(0.6 * cascadeNeuralNetworkOutput[classLabel] + 0.4 * neuralNetworkOutput[classificationLabel]);
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

  public void setSymbolLabels(Labels[][] symbolLabels){
    symbolLabels_ = symbolLabels;
  }

  public Labels[][] getSymbolLabels(){
    return symbolLabels_;
  }

  private SymbolFactory symbolFactory_ = SymbolFactory.getInstance();

  private NeuralNetwork cascadeNeuralNetwork_ = null;
  private NeuralNetwork[] neuralNetworks_ = null;

  private Labels[][] symbolLabels_ = new Labels[][] {};

  private boolean silent_ = true; //!< Flag for the silent mode of this NeuralNetworkClassifier.

}
