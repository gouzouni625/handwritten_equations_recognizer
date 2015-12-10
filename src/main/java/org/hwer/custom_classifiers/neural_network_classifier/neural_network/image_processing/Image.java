package org.hwer.custom_classifiers.neural_network_classifier.neural_network.image_processing;


public interface Image {
  Image clone();

  double[] toVector(double min, double max);
}
