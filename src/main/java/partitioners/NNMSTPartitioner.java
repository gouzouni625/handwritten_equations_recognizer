package main.java.partitioners;

import main.java.base.NeuralNetwork;
import main.java.classifiers.NeuralNetworkClassifier;
import main.java.distorters.ImageDistorter;

/** @class NNMSTPartitioner
 *
 *  @brief Uses a neural network classifier as a classifier for a minimum spanning tree partitioner.
 */
public class NNMSTPartitioner extends MSTPartitioner{
  /**
   *  @brief Constructor.
   *
   *  @param neuralNetwork The neural network to be used by the main.java.classifiers.NeuralNetworkClassifier.
   *  @param imageDistorter The image distorter to be used by the main.java.classifiers.NeuralNetworkClassifier.
   */
  public NNMSTPartitioner(NeuralNetwork neuralNetwork, ImageDistorter imageDistorter){
    classifier_ = new NeuralNetworkClassifier(neuralNetwork, Partitioner.MAX_TRACES_IN_SYMBOL);

    ((NeuralNetworkClassifier)(classifier_)).setImageDistorter(imageDistorter);
  }

}
