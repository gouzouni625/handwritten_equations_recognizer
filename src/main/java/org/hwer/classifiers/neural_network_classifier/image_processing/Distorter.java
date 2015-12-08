package org.hwer.classifiers.neural_network_classifier.image_processing;

import java.awt.image.BufferedImage;

public interface Distorter {
  BufferedImage distort (BufferedImage image);

  BufferedImage vectorToBufferedImage (double[] vector, int width, int height, double minValue,
                                       double maxValue);

  double[] bufferedImageToVector (BufferedImage image, double min, double max);

}
