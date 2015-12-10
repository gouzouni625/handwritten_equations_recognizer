package org.hwer.custom_classifiers.neural_network_classifier.neural_network.image_processing;

public interface Distorter {
  Image distort (Image image);

  Image vectorToImage (double[] vector, int width, int height, double minValue,
                       double maxValue);
}
