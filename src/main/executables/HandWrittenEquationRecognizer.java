package main.executables;

import java.io.IOException;

import main.base.NeuralNetwork;
import main.distorters.ImageDistorter;
import main.evaluators.SimpleEvaluator;

/** @class HandWrittenEquationRecognizer
 *
 *  @brief Implements a recognizer of hand-written equations.
 */
public class HandWrittenEquationRecognizer{
  /**
   *  @brief main function.
   *
   *  @param args Accepts 2 String arguments. The first, is the full or the relative path of the file where the
   *              main.base.NeuralNetwork to be used is located. Note that this file should end with .binary or .xml.
   *              The second, is the equation's InkML data.
   *
   *  @throws IOException When the main.evaluators.SimpleEvaluator.SimpleEvaluator throws an exception.
   */
  public static void main(String[] args) throws IOException{
    String extention = args[0].substring(args[0].lastIndexOf('.'));

    NeuralNetwork neuralNetwork;
    if(extention.equals("xml")){
      neuralNetwork = NeuralNetwork.createFromXML(args[0]);
    }
    else{
      neuralNetwork = NeuralNetwork.createFromBinary(args[0]);
    }

    ImageDistorter imageDistorter = new ImageDistorter();
    int sampleDimentionSize = (int)Math.sqrt(neuralNetwork.getSizesOfLayers()[0]);
    imageDistorter.setSampleRows(sampleDimentionSize);
    imageDistorter.setSampleColumns(sampleDimentionSize);

    SimpleEvaluator evaluator = new SimpleEvaluator(neuralNetwork, imageDistorter);

    evaluator.evaluate(args[1]);

    System.out.println(evaluator.toString());
  }

}
