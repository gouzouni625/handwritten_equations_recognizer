package org.hwer.implementations.classifiers.nnclassifier.neural_network;


import org.hwer.implementations.classifiers.nnclassifier.NNClassifier.NeuralNetwork;
import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;


/**
 * @class NeuralNetwork
 * @brief Implements a feed forward NeuralNetwork
 */
public class NeuralNetworkImpl implements NeuralNetwork {
    /**
     * @brief Constructor
     *
     * @param imageProcessor
     *     The Core implementation
     * @param imageDistorter
     *     The Distorter implementation
     */
    public NeuralNetworkImpl (Core imageProcessor, Distorter imageDistorter) {
        imageProcessor_ = imageProcessor;
        imageDistorter_ = imageDistorter;
    }

    /**
     * @brief Returns the values of the output layer of the neural network when evaluated on the
     *        given input
     * @param input
     *     The input to evaluate this neural network on
     *
     * @return The values of the output layer of the neural network
     */
    public double[] feedForward (double[] input) {
        double[] inputBuffer = input.clone();

        double[] output = new double[sizesOfLayers_[1]];
        for (int i = 0; i < numberOfLayers_ - 1; i++) {
            for (int j = 0; j < sizesOfLayers_[i + 1]; j++) {
                double sum = 0;
                for (int k = 0; k < sizesOfLayers_[i]; k++) {
                    sum += weights_[i][j][k] * inputBuffer[k];
                }

                output[j] = this.activationFunction(sum + biases_[i][j]);
            }

            if (i + 2 < numberOfLayers_) {
                inputBuffer = output.clone();
                output = new double[sizesOfLayers_[i + 2]];
            }
        }

        return output;
    }

    /**
     * @brief Getter method for the number of layers of this neural network
     *
     * @return The number of layers of this neural network
     */
    public int getNumberOfLayers () {
        return numberOfLayers_;
    }

    /**
     * @brief Loads the parameters for this neural network from an InputStream
     *
     * @param inputStream
     *     The InputStream
     *
     * @throws IOException If an I/O error occurs
     */
    public void loadFromInputStream (InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        numberOfLayers_ = dataInputStream.readInt();
        sizesOfLayers_ = new int[numberOfLayers_];
        for (int i = 0; i < numberOfLayers_; i++) {
            sizesOfLayers_[i] = dataInputStream.readInt();
        }

        weights_ = new double[numberOfLayers_ - 1][][];
        biases_ = new double[numberOfLayers_ - 1][];

        for (int i = 0; i < numberOfLayers_ - 1; i++) {
            weights_[i] = new double[sizesOfLayers_[i + 1]][];
            biases_[i] = new double[sizesOfLayers_[i + 1]];

            for (int j = 0; j < sizesOfLayers_[i + 1]; j++) {
                weights_[i][j] = new double[sizesOfLayers_[i]];
            }
        }

        for (int i = 0; i < numberOfLayers_ - 1; i++) {
            for (int j = 0; j < sizesOfLayers_[i + 1]; j++) {
                biases_[i][j] = dataInputStream.readDouble();

                for (int k = 0; k < sizesOfLayers_[i]; k++) {
                    weights_[i][j][k] = dataInputStream.readDouble();
                }
            }
        }

        dataInputStream.close();

        imageSide_ = (int) Math.sqrt(sizesOfLayers_[0]);
    }

    /**
     * @brief The activation function of the neurons of this neural network
     *
     * @param z
     *     The independent variable
     *
     * @return The value of the activation function at z
     */
    private double activationFunction (double z) {
        return (relu(z));
    }

    /**
     * @brief The sigmoid function
     *
     * @param z
     *     The independent variable
     *
     * @return The value of the sigmoid function on z
     */
    private double sigmoid (double z) {
        return (1 / (1 + Math.exp(- z)));
    }

    /**
     * @brief The rectifier function
     *
     * @param z
     *     The independent variable
     *
     * @return The value of the rectifier function on z
     */
    private double relu (double z) {
        return Math.max(0, z);
    }

    /**
     * @brief Getter method for the sizesOfLayers of this neural network
     *
     * @return The sizesOfLayers of this neural network
     */
    public int[] getSizesOfLayers () {
        return sizesOfLayers_;
    }

    /**
     * @brief Returns the values of the output layer of the neural network when evaluated on the
     *        given traceGroup
     *        The evaluation can be done many times for the case that the neural network uses a
     *        seed of randomness in each evaluation.
     *
     * @param traceGroup
     *     The TraceGroup to evaluate the neural network on
     * @param times
     *     The number of times that the evaluation should be done
     *
     * @return The values of the output layer of the neural network
     */
    public double[] evaluate (TraceGroup traceGroup, int times) {
        Image image = imageProcessor_.printTraceGroup(traceGroup, imageSide_, imageSide_, 1);

        Image imageClone;

        double[] neuralNetworkOutput = feedForward(image.toVector(- 1, 1));

        if (imageDistorter_ != null) {
            for (int i = 0; i < times; i++) {
                imageClone = image.clone();

                imageClone = imageDistorter_.distort(imageClone);

                double[] currentOutput = feedForward(imageClone.toVector(- 1, 1));
                for (int j = 0; j < neuralNetworkOutput.length; j++) {
                    neuralNetworkOutput[j] += currentOutput[j];
                }

            }

            if (times > 0) {
                for (int i = 0; i < neuralNetworkOutput.length; i++) {
                    neuralNetworkOutput[i] /= times;
                }
            }
        }

        neuralNetworkOutput = Utilities.normalizeArray(neuralNetworkOutput);

        return neuralNetworkOutput;
    }

    /**
     * @interface Core
     * @brief Defines the API that should be implemented by an image processor so that TraceGroups
     *        can be transformed to images
     */
    public interface Core {
        /**
         * @brief Transforms a TraceGroup to an Image
         *
         * @param traceGroup
         *     The TraceGroup to be transformed
         * @param width
         *     The width of the Image
         * @param height
         *     The height of the Image
         * @param thickness
         *     The thickness of lines on the image
         *
         * @return The Image created by the given TraceGroup
         */
        Image printTraceGroup (TraceGroup traceGroup, int width, int height, int thickness);
    }

    /**
     * @interface Distorter
     * @brief Defines the API that should be implemented by an image distorter
     */
    public interface Distorter {
        /**
         * @brief Distorts an Image so that in can be re evaluated by a neural network
         *
         * @param image
         *     The Image to be distorted
         *
         * @return The distorted Image
         */
        Image distort (Image image);
    }

    /**
     * @interface Image
     * @brief Defines the API that should be implemented to be used with a Core and a Distorter
     */
    public interface Image {
        /**
         * @brief Returns a clone of this Image
         *
         * @return A clone of this Image
         */
        Image clone ();

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
        double[] toVector (double min, double max);
    }

    private int numberOfLayers_; //!< The number of layers of this neural network
    private int[] sizesOfLayers_; //!< The number of neurons in each layer

    private double[][][] weights_; //!< The weight parameters of this NeuralNetwork
                                   //!< Dimensions are: [layer, neuron, weight]
    private double[][] biases_; //!< The bias parameters of this NeuralNetwork
                                //!< dimensions are: [layer, neuron]

    private Core imageProcessor_ = null; //!< The image processor of this neural network

    private Distorter imageDistorter_ = null; //!< The image distorter of this neural network

    private int imageSide_; //!< The number of pixels in a row or a column of the Images that this
                            //!< neural network accepts as input

}
