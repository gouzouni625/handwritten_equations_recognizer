package org.hwer.implementations.classifiers.nnclassifier.neural_network.image_processing;


public interface Image {
  Image clone();

  double[] toVector(double min, double max);
}
