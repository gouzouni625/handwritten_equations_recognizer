package main.java.classifiers;

import main.java.utilities.traces.TraceGroup;

/** @class Classifier
 *
 *  @brief Implements an abstract Classifier.
 *
 *  A classifier uses a classfication algorithm to classify a given main.utilities.symbols.Symbol.
 */
public abstract class Classifier{
  /**
   *  @brief Constructor
   *
   *  @param maxTracesInSymbol The maximum number of main.utilities.traces.Trace objects in a
   *                           main.utilities.symbols.Symbol object.
   */
  public Classifier(int maxTracesInSymbol){
    maxTracesInSymbol_ = maxTracesInSymbol;
  }

  /**
   *  @brief Classifies a given main.utilities.symbols.Symbol.
   *
   *  Besides the classification algorithm, a Classifier can use additional techniques to increase the accuracy of the
   *  classification process. Checking the context of the given main.utilities.symbols.Symbol, or checking sub-groups
   *  of main.utilities.traces.Trace objects inside the main.utilities.symbols.Symbol are two of these techniques.
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
   *  @return Returns the confidence of the Classifier for the classification of the given main.utilities.symbols.Symbol.
   */
  public abstract double classify(TraceGroup symbol, TraceGroup context, boolean subSymbolCheck, boolean subContextCheck);

  /**
   *  @brief Getter method for the label chosen by the Classifier.
   *
   *  @return Returns the chosen label.
   */
  public abstract int getClassificationLabel();

  public static double MINIMUM_RATE = 0; //!< The minimum value of the confidence of the Classifier for a classification.
  public static double MAXIMUM_RATE = 100; //!< The maximum value of the confidence of the Classifier for a classification.

  protected int maxTracesInSymbol_; //!< The maximum number of main.utilities.traces.Trace objects in a
                                    //!< main.utilities.symbols.Symbol object.

}
