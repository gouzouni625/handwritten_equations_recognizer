package org.hwer.custom_classifiers.neural_network_classifier.neural_network.image_processing;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageDistorter implements Distorter {
  public ImageDistorter(){

  }

  public Image distort (Image image) {
    Random random = new Random();
    double distortionType = random.nextDouble();

    BufferedImage bufferedImage = ((CustomImage) image).getImplementation();

    BufferedImage transformedImage = new BufferedImage(bufferedImage.getWidth(),
        bufferedImage.getHeight(), bufferedImage.getType());

    if (distortionType < 0.25) { // Rotating. [-pi/12, pi/12).
      double parameter = ((2 * random.nextDouble() - 1) / 12) * Math.PI; // Angle.

      new AffineTransformOp(AffineTransform.getRotateInstance(parameter,
          bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2),
          AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
    } else if (distortionType < 0.5) {  // Scaling. [0.85, 1.15).
      // Volume for horizontal axis.
      double parameter = ((2 * random.nextDouble() - 1) * 15 / 100) + 1;

      new AffineTransformOp(AffineTransform.getScaleInstance(parameter, parameter),
          AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
    } else if (distortionType < 0.75) {  // Shearing. [-0.15, 0.15).
      double parameter = ((2 * random.nextDouble() - 1) * 15 / 100);

      new AffineTransformOp(AffineTransform.getShearInstance(parameter, parameter),
          AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
    } else { // translating [-5, 5).
      double parameterX = (2 * random.nextDouble() - 1) * 5;
      double parameterY = (2 * random.nextDouble() - 1) * 5;

      new AffineTransformOp(AffineTransform.getTranslateInstance(parameterX, parameterY),
          AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
    }

    return (new CustomImage(transformedImage));
  }

}
