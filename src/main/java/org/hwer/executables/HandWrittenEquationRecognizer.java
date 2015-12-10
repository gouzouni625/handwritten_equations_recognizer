package org.hwer.executables;

import java.io.FileInputStream;
import java.io.IOException;

import org.hwer.custom_classifiers.neural_network_classifier.NeuralNetworkClassifier;
import org.hwer.custom_classifiers.neural_network_classifier.neural_network.CustomNeuralNetwork;
import org.hwer.custom_classifiers.neural_network_classifier.neural_network.image_processing.ImageDistorter;
import org.hwer.custom_classifiers.neural_network_classifier.neural_network.image_processing.ImageProcessor;
import org.hwer.engine.partitioners.Partitioner;
import org.hwer.evaluators.SimpleEvaluator;

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
    CustomNeuralNetwork neuralNetwork = new CustomNeuralNetwork(new ImageProcessor(), new ImageDistorter());
    neuralNetwork.loadFromInputStream(new FileInputStream(args[0]));

    NeuralNetworkClassifier neuralNetworkClassifier = new NeuralNetworkClassifier(neuralNetwork,
        Partitioner.MAX_TRACES_IN_SYMBOL);

    SimpleEvaluator evaluator = new SimpleEvaluator(neuralNetworkClassifier);

    evaluator.evaluate(args[1]);

    System.out.println(evaluator.toString());
  }

}
