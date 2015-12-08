package org.hwer.evaluators;

import java.io.IOException;

import org.nn.base.NeuralNetwork;
import org.nn.distorters.ImageDistorter;
import org.hwer.engine.parsers.GGParser;
import org.hwer.engine.partitioners.NNMSTPartitioner;
import org.hwer.engine.utilities.inkml.InkMLParser;
import org.hwer.engine.utilities.traces.TraceGroup;

/** @class SimpleEvaluator
 *
 *  @brief Combines a main.java.partitioners.NNMSTPartitioner and a main.java.parsers.GGParser to
 *         evaluate a given equation.
 */
public class SimpleEvaluator{
  /**
   *  @brief Constructor.
   *
   *  @param neuralNetwork The main.java.base.NeuralNetwork to be used by the
   *                       main.java.partitioners.NNMSTPartitioner.
   *  @param imageDistorter The main.java.distorters.ImageDistorter to be used by the
   *                        main.java.partitioners.NNMSTPartitioner.
   *
   *  @throws IOException When the main.java.partitioners.NNMSTPartitioner.NNMSTPartitioner
   *                      throws an exception.
   */
  public SimpleEvaluator(NeuralNetwork neuralNetwork, ImageDistorter imageDistorter)
      throws IOException{
    partitioner_ = new NNMSTPartitioner(neuralNetwork, imageDistorter);

    parser_ = new GGParser();
  }

  /**
   *  @brief Processes an equation.
   *
   *  @param xmlData The xmlData in InkML format of the given equation.
   *
   *  @return Returns the recognized equation in TeX format.
   */
  public String evaluate(String xmlData){
    InkMLParser inkMLParser = new InkMLParser(new String(xmlData));
    inkMLParser.parse();

    inkMLParser.traceGroup_.multiplyBy(100).calculateCorners();

    TraceGroup[] partition = partitioner_.partition(inkMLParser.traceGroup_);
    int[] labels = partitioner_.getLabels();

    parser_.parse(partition, labels);

    return (parser_.toString());
  }

  /**
   *  @brief Returns the recognized equation in TeX format.
   *
   *  @return Returns the recognized equation in TeX format.
   */
  public String toString(){
    return parser_.toString();
  }

  /**
   *  @brief Getter method for the silent mode of the main.java.partitioners.NNMSTPartitioner.
   *
   *  @return Returns true if the main.java.partitioners.NNMSTPartitioner is in silent mode.
   */
  public boolean isPartitionerSilent(){
    return (partitioner_.isSilent());
  }

  /**
   *  @brief Setter method for the silent mode of the main.java.partitioners.NNMSTPartitioner.
   *
   *  @param silent The value for the silent mode of the main.java.partitioners.NNMSTPartitioner.
   */
  public void setPartitionerSilent(boolean silent){
    partitioner_.setSilent(silent);
  }

  /**
   *  @brief Getter method for the silent mode of the main.java.parsers.GGParser.
   *
   *  @return Returns true if the main.java.parsers.GGParser is in silent mode.
   */
  public boolean isParserSilent(){
    return (parser_.isSilent());
  }

  /**
   *  @brief Setter method for the silent mode of the main.java.parsers.GGParser.
   *
   *  @param silent The value for the silent mode of the main.java.parsers.GGParser.
   */
  public void setParserSilent(boolean silent){
    parser_.setSilent(silent);
  }

  /**
   *  @brief Getter method for the silent mode of the
   *         main.java.utilities.grammars.GeometricalGrammar used by the main.java.parsers.GGParser.
   *
   *  @return Returns true if the main.java.utilities.grammars.GeometricalGrammar is in silent mode.
   */
  public boolean isGrammarSilent(){
    return (parser_.isGrammarSilent());
  }

  /**
   *  @brief Setter method for the main.java.utilities.grammars.GeometricalGrammar used by the
   *         main.java.parsers.GGParser.
   *
   *  @param silent The value for the silent mode of the
   *                main.java.utilities.grammars.GeometricalGrammar.
   */
  public void setGrammarSilent(boolean silent){
    parser_.setGrammarSilent(silent);
  }

  private NNMSTPartitioner partitioner_; //!< The main.java.partitioners.NNMSTPartitioner of this
                                         //!< SimpleEvaluator.

  private GGParser parser_;//!< The main.java.parsers.GGParser of this SimpleEvaluator.

}
