package org.hwer.image.processing;


import org.hwer.engine.classifiers.neural_network.NeuralNetworkImpl.Distorter;
import org.hwer.engine.classifiers.neural_network.NeuralNetworkImpl.Image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * @class DistorterImpl
 * @brief Implementation of the Distorter API from NeuralNetworkImpl using the java AWT package
 */
public class DistorterImpl implements Distorter {
    /**
     * @brief Distorts an Image so that in can be re evaluated by a neural network
     *
     * @param image
     *     The Image to be distorted
     *
     * @return The distorted Image
     */
    public Image distort (Image image) {
        Random random = new Random();
        double distortionType = random.nextDouble();

        BufferedImage bufferedImage = ((ImageImpl) image).getImplementation();

        BufferedImage transformedImage = new BufferedImage(bufferedImage.getWidth(),
            bufferedImage.getHeight(), bufferedImage.getType());

        if (distortionType < 0.25) { // Rotating. [-pi/12, pi/12).
            double parameter = ((2 * random.nextDouble() - 1) / 12) * Math.PI; // Angle.

            new AffineTransformOp(AffineTransform.getRotateInstance(parameter,
                bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2),
                AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
        }
        else if (distortionType < 0.5) {  // Scaling. [0.85, 1.15).
            // Volume for horizontal axis.
            double parameter = ((2 * random.nextDouble() - 1) * 15 / 100) + 1;

            new AffineTransformOp(AffineTransform.getScaleInstance(parameter, parameter),
                AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
        }
        else if (distortionType < 0.75) {  // Shearing. [-0.15, 0.15).
            double parameter = ((2 * random.nextDouble() - 1) * 15 / 100);

            new AffineTransformOp(AffineTransform.getShearInstance(parameter, parameter),
                AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
        }
        else { // translating [-5, 5).
            double parameterX = (2 * random.nextDouble() - 1) * 5;
            double parameterY = (2 * random.nextDouble() - 1) * 5;

            new AffineTransformOp(AffineTransform.getTranslateInstance(parameterX, parameterY),
                AffineTransformOp.TYPE_BILINEAR).filter(bufferedImage, transformedImage);
        }

        return (new ImageImpl(transformedImage));
    }

}
