package org.hwer.partitioners;

import org.hwer.classifiers.Classifier;
import org.hwer.utilities.traces.TraceGroup;

/** @class Partitioner
 *
 *  @brief A Partitioner splits a set of traces to groups. Each group of these traces represents a
 *         symbol.
 */
public abstract class Partitioner{
  /**
   *  @brief Partitions a group of ink traces of an equation.
   *
   *   The result is an array of groups of ink traces each one of which represents a symbol of the
   *   equation.
   *
   *  @param expression The main.java.utilities.traces.TraceGroup with the ink traces of the
   *                    equation.
   *
   *  @return Returns an array of main.java.utilities.traces.TraceGroup each one of which contains
   *          the traces of a single symbol.
   */
  public abstract TraceGroup[] partition(TraceGroup expression);

  /**
   *  @brief Setter method for the silent mode of this Partitioner.
   *
   *  @param silent The value for the silent mode of this Partitioner.
   */
  public void setSilent(boolean silent){
    silent_ = silent;
  }

  /**
   *  @brief Getter method for the silent mode of this Partitioner.
   *
   *  @return Returns true if this Partitioner is in silent mode.
   */
  public boolean isSilent(){
    return silent_;
  }

  protected boolean silent_ = true; //!< Flag defining the silent mode of this Partitioner.

  protected Classifier classifier_; //!< The Classifier used by this Partitioner to partition the
                                    //!< given equation.

  public static int MAX_TRACES_IN_SYMBOL = 3; //!< The maximum number of traces allowed in a symbol.

}
