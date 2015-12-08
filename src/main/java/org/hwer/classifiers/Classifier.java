package org.hwer.classifiers;

import org.hwer.engine.utilities.traces.TraceGroup;

/** @class Classifier
 *
 *  @brief Implements an abstract Classifier.
 *
 *  A classifier uses a classification algorithm to classify a given main.java.utilities.symbols.Symbol.
 */
public abstract class Classifier{
  /**
   *  @brief Constructor
   *
   *  @param maxTracesInSymbol The maximum number of main.java.utilities.traces.Trace objects in a
   *                           main.java.utilities.symbols.Symbol object.
   */
  public Classifier(int maxTracesInSymbol){
    maxTracesInSymbol_ = maxTracesInSymbol;
  }

  /**
   *  @brief Classifies a given main.java.utilities.symbols.Symbol.
   *
   *  Besides the classification algorithm, a Classifier can use additional techniques to increase the accuracy of the
   *  classification process. Checking the context of the given main.java.utilities.symbols.Symbol, or checking sub-groups
   *  of main.java.utilities.traces.Trace objects inside the
   *  main.java.utilities.symbols.Symbol are two of these techniques.
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
   *  @return Returns the confidence of the Classifier for the classification of the given
   *          main.java.utilities.symbols.Symbol.
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

  protected int maxTracesInSymbol_; //!< The maximum number of main.java.utilities.traces.Trace objects in a
                                    //!< main.java.utilities.symbols.Symbol object.

}
