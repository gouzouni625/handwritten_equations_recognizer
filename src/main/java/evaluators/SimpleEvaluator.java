package main.java.evaluators;

import java.io.IOException;

import main.java.base.NeuralNetwork;
import main.java.distorters.ImageDistorter;
import main.java.parsers.GGParser;
import main.java.partitioners.NNMSTPartitioner;
import main.java.utilities.inkml.InkMLParser;
import main.java.utilities.traces.TraceGroup;

/** @class SimpleEvaluator
 *
 *  @brief Combines a main.partitioners.NNMSTPartitioner and a main.parsers.GGParser to evaluate a given equation.
 */
public class SimpleEvaluator{
  /**
   *  @brief Constructor.
   *
   *  @param neuralNetwork The main.base.NeuralNetwork to be used by the main.partitioners.NNMSTPartitioner.
   *  @param imageDistorter The main.distorters.ImageDistorter to be used by the main.partitioners.NNMSTPartitioner.
   *
   *  @throws IOException When the main.partitioners.NNMSTPartitioner.NNMSTPartitioner throws an exception.
   */
  public SimpleEvaluator(NeuralNetwork neuralNetwork, ImageDistorter imageDistorter) throws IOException{
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
   *  @brief Getter method for the silent mode of the main.partitioners.NNMSTPartitioner.
   *
   *  @return Returns true if the main.partitioners.NNMSTPartitioner is in silent mode.
   */
  public boolean isPartitionerSilent(){
    return (partitioner_.isSilent());
  }

  /**
   *  @brief Setter method for the silent mode of the main.partitioners.NNMSTPartitioner.
   *
   *  @param silent The value for the silent mode of the main.partitioners.NNMSTPartitioner.
   */
  public void setPartitionerSilent(boolean silent){
    partitioner_.setSilent(silent);
  }

  /**
   *  @brief Getter method for the silent mode of the main.parsers.GGParser.
   *
   *  @return Returns true if the main.parsers.GGParser is in silent mode.
   */
  public boolean isParserSilent(){
    return (parser_.isSilent());
  }

  /**
   *  @brief Setter method for the silent mode of the main.parsers.GGParser.
   *
   *  @param silent The value for the silent mode of the main.parsers.GGParser.
   */
  public void setParserSilent(boolean silent){
    parser_.setSilent(silent);
  }

  /**
   *  @brief Getter method for the silent mode of the main.utilities.grammars.GeometricalGrammar used by the
   *         main.parsers.GGParser.
   *
   *  @return Returns true if the main.utilities.grammars.GeometricalGrammar is in silent mode.
   */
  public boolean isGrammarSilent(){
    return (parser_.isGrammarSilent());
  }

  /**
   *  @brief Setter method for the main.utilities.grammars.GeometricalGrammar used by the main.parsers.GGParser.
   *
   *  @param silent The value for the silent mode of the main.utilities.grammars.GeometricalGrammar.
   */
  public void setGrammarSilent(boolean silent){
    parser_.setGrammarSilent(silent);
  }

  private NNMSTPartitioner partitioner_; //!< The main.partitioners.NNMSTPartitioner of this SimpleEvaluator.

  private GGParser parser_;//!< The main.parsers.GGParser of this SimpleEvaluator.

}
