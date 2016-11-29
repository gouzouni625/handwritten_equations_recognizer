package org.hwer.image.processing;


import org.hwer.engine.classifiers.neural_network.NeuralNetworkImpl.Image;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;


/**
 * @class ImageImpl
 * @brief Implementation of the Image API from NeuralNetworkImpl using the java AWT package
 */
public class ImageImpl implements Image {
    /**
     * @brief Constructor
     *
     * @param bufferedImage
     *     The java.awt.image.BufferedImage to be used by this ImageImpl
     */
    public ImageImpl (BufferedImage bufferedImage) {
        bufferedImage_ = bufferedImage;
    }

    /**
     * @brief Returns a clone of this Image
     *
     * @return A clone of this Image
     */
    public Image clone () {
        BufferedImage bufferedImage = new BufferedImage(bufferedImage_.getWidth(),
            bufferedImage_.getHeight(), bufferedImage_.getType());

        bufferedImage.setData(bufferedImage_.getData());

        return new ImageImpl(bufferedImage);
    }

    /**
     * @brief Transforms this Image to a vector
     *
     * @param min
     *     The minimum value of the vector values
     * @param max
     *     The maximum value of the vector values
     *
     * @return The vector created by this Image
     */
    public double[] toVector (double min, double max) {
        byte[] pixels = ((DataBufferByte) bufferedImage_.getRaster().getDataBuffer()).getData();

        int numberOfPixels = pixels.length;

        double[] vector = new double[numberOfPixels];

        for (int i = 0; i < numberOfPixels; i++) {
            vector[i] = (pixels[i] & 0xFF) * (max - min) / 255 + min;
        }

        return vector;
    }

    /**
     * @brief Returns the java.awt.image.BufferedImage used by this ImageImpl
     *        This method is to be used by the CoreImpl and DistorterImpl and doesn't need to be
     *        known by the rest of the program
     *
     * @return The java.awt.image.BufferedImage used by this ImageImpl
     */
    public BufferedImage getImplementation () {
        return bufferedImage_;
    }

    private BufferedImage bufferedImage_; //!< The java.awt.image.BufferedImage used by this
                                          //!< ImageImpl

}
