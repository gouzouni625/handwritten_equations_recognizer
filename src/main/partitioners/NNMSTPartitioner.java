package main.partitioners;

import java.io.IOException;

import main.classifiers.NeuralNetworkClassifier;
import main.distorters.ImageDistorter;

/* NN = Neural Network, describes the classifier.
 * MST = Minimum Spanning Tree, describes the partitioning algorithm.
 */
public class NNMSTPartitioner extends MSTPartitioner{

  public NNMSTPartitioner(int[] sizesOfLayers){
    classifier_ = new NeuralNetworkClassifier(sizesOfLayers, Partitioner.MAX_TRACES_IN_SYMBOL);
  }

  public NNMSTPartitioner(int[] sizesOfLayers, String path) throws IOException{
    classifier_ = new NeuralNetworkClassifier(sizesOfLayers, Partitioner.MAX_TRACES_IN_SYMBOL);
    ((NeuralNetworkClassifier)(classifier_)).loadNeuralNetwork(path);
  }

  public NNMSTPartitioner(int[] sizesOfLayers, String path, ImageDistorter imageDistorter) throws IOException{
    classifier_ = new NeuralNetworkClassifier(sizesOfLayers, Partitioner.MAX_TRACES_IN_SYMBOL);
    ((NeuralNetworkClassifier)(classifier_)).loadNeuralNetwork(path);

    ((NeuralNetworkClassifier)(classifier_)).setImageDistorter(imageDistorter);

    ((NeuralNetworkClassifier)(classifier_)).setImageNumberOfRows(imageDistorter.getSampleRows());
    ((NeuralNetworkClassifier)(classifier_)).setImageNumberOfColumns(imageDistorter.getSampleColumns());
  }

}
