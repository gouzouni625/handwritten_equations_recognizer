package org.hwer.executables;

import java.io.IOException;

import org.hwer.classifiers.neural_network_classifier.NeuralNetworkClassifier;
import org.hwer.classifiers.neural_network_classifier.image_processing.ImageDistorter;
import org.hwer.classifiers.neural_network_classifier.image_processing.ImageProcessor;
import org.hwer.engine.partitioners.Partitioner;
import org.hwer.evaluators.SimpleEvaluator;
import org.hwer.classifiers.neural_network_classifier.NeuralNetworkClassifier.NeuralNetwork;

/** @class HandWrittenEquationRecognizer
 *
 *  @brief Implements a recognizer of hand-written equations.
 */
public class HandWrittenEquationRecognizer{
  /**
   *  @brief main function.
   *
   *  @param args Accepts 2 String arguments. The first, is the full or the relative path of the
   *              file where the main.java.base.NeuralNetwork to be used is located. Note that this
   *              file should end with .binary or .xml. The second, is the equation's InkML data.
   *
   *  @throws IOException When the main.java.evaluators.SimpleEvaluator.SimpleEvaluator throws an
   *          exception.
   */
  public static void main(String[] args) throws IOException{
    String extension = args[0].substring(args[0].lastIndexOf('.'));

    NeuralNetwork neuralNetwork = new NeuralNetwork();
    if(extension.equals("xml")){
      neuralNetwork.loadFromXML(args[0]);
    }
    else{
      neuralNetwork.loadFromBinary(args[0]);
    }

    NeuralNetworkClassifier neuralNetworkClassifier = new NeuralNetworkClassifier(neuralNetwork,
        Partitioner.MAX_TRACES_IN_SYMBOL);

    ImageDistorter imageDistorter = new ImageDistorter();
    ImageProcessor imageProcessor = new ImageProcessor();

    neuralNetworkClassifier.setImageDistorter(imageDistorter);
    neuralNetworkClassifier.setImageProcessor(imageProcessor);

    SimpleEvaluator evaluator = new SimpleEvaluator(neuralNetworkClassifier);

    evaluator.evaluate(args[1]);

    System.out.println(evaluator.toString());
  }

}
