package main.partitioners;

import java.io.IOException;

import main.classifiers.NeuralNetworkClassifier;

/* NN = Neural Network, describes the classifier.
 * MST = Minimum Spanning Tree, describes the partitioning algorithm.
 */
public class NNMSTPartitioner extends MSTPartitioner{
  public NNMSTPartitioner(int[] sizesOfLayers){
    classifier_ = new NeuralNetworkClassifier(sizesOfLayers);
  }

  public NNMSTPartitioner(int[] sizesOfLayers, String path) throws IOException{
    classifier_ = new NeuralNetworkClassifier(sizesOfLayers);
    ((NeuralNetworkClassifier)(classifier_)).loadNeuralNetwork(path);
  }

}
