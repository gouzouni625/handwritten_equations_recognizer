package org.hwer.custom_classifiers.neural_network_classifier.neural_network.image_processing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageImpl implements Image{
  public ImageImpl (BufferedImage bufferedImage){
    bufferedImage_ = bufferedImage;
  }

  public Image clone () {
    BufferedImage bufferedImage = new BufferedImage(bufferedImage_.getWidth(),
        bufferedImage_.getHeight(), bufferedImage_.getType());

    bufferedImage.setData(bufferedImage_.getData());

    return new ImageImpl(bufferedImage);
  }

  public double[] toVector (double min, double max) {
    byte[] pixels = ((DataBufferByte) bufferedImage_.getRaster().getDataBuffer()).getData();

    int numberOfPixels = pixels.length;

    double[] vector = new double[numberOfPixels];

    for (int i = 0; i < numberOfPixels; i++) {
      vector[i] = (pixels[i] & 0xFF) * (max - min) / 255 + min;
    }

    return vector;
  }

  public BufferedImage getImplementation(){
    return bufferedImage_;
  }

  private BufferedImage bufferedImage_;
}
