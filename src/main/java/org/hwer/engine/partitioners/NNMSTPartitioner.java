package org.hwer.engine.partitioners;

import org.hwer.image_processing.ImageProcessor;
import org.hwer.classifiers.NeuralNetworkClassifier;
import org.nn.base.NeuralNetwork;
import org.nn.distorters.ImageDistorter;

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
    classifier_ = new NeuralNetworkClassifier(neuralNetwork, MAX_TRACES_IN_SYMBOL);

    ((NeuralNetworkClassifier)(classifier_)).setImageDistorter(imageDistorter);
    ((NeuralNetworkClassifier)(classifier_)).setImageProcessor(new ImageProcessor());
  }

}
