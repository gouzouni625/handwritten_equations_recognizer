package org.hwer.implementations.classifiers.nnclassifier;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;


/**
 * @class NeuralNetworkClassifier
 * @brief Implements a Classifier that uses neural networks to classify TraceGroups
 *        This classifier uses a two-level cascade architecture. Concretely, on the first level,
 *        a neural network is used to classify the symbol into categories (e.g. number, letter,
 *        etc...) and on the second level, another neural network (different for each category) is
 *        used to identify the symbol (e.g. if the chosen category is number, the second neural
 *        network will try to classify between zero, one, two, ..., nine).
 */
public class NNClassifier extends Classifier {
    /**
     * @brief Constructor
     * @param cascadeNeuralNetwork
     *     The first-level neural network
     * @param neuralNetworks
     *     The second-level neural networks
     */
    public NNClassifier (NeuralNetwork cascadeNeuralNetwork, NeuralNetwork[] neuralNetworks) {
        cascadeNeuralNetwork_ = cascadeNeuralNetwork;
        neuralNetworks_ = neuralNetworks;
    }

    /**
     * @brief Constructor
     * @param cascadeNeuralNetwork
     *     The first-level neural network
     * @param neuralNetworks
     *     The second-level neural networks
     * @param symbolLabels
     *     The Symbols that each neural network label is mapped to
     */
    public NNClassifier (NeuralNetwork cascadeNeuralNetwork, NeuralNetwork[] neuralNetworks,
                         Labels[][] symbolLabels) {
        cascadeNeuralNetwork_ = cascadeNeuralNetwork;
        neuralNetworks_ = neuralNetworks;

        symbolLabels_ = symbolLabels;
    }

    /**
     * @brief Classifies a given TraceGroup
     *        The context of a TraceGroup contains all the Traces that are near but not a part of
     *        the TraceGroup.
     *
     * @param traceGroup
     *     The TraceGroup to classify
     * @param context
     *     The context of the given TraceGroup
     * @param subSymbolCheck
     *     Check sub-groups of the given TraceGroup
     * @param subContextCheck
     *     Check sub-groups of the given context
     *
     * @return The Symbol that the given TraceGroup was classified
     */
    @Override
    public Symbol classify (TraceGroup traceGroup, TraceGroup context, boolean subSymbolCheck,
                            boolean subContextCheck) {
        double[] cascadeNeuralNetworkOutput = cascadeNeuralNetwork_.evaluate(traceGroup, 0);

        int classLabel = Utilities.indexOfMax(cascadeNeuralNetworkOutput);

        double[] neuralNetworkOutput = neuralNetworks_[classLabel].evaluate(traceGroup, 0);

        int classificationLabel = Utilities.indexOfMax(neuralNetworkOutput);

        Symbol symbol = null;
        try {
            symbol = symbolFactory_.create(symbolLabels_[classLabel][classificationLabel],
                traceGroup);

            symbol.setConfidence(cascadeNeuralNetworkOutput[classLabel] *
                neuralNetworkOutput[classificationLabel]);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return symbol;
    }

    /**
     * @interface NeuralNetwork
     * @brief Defines the API that every neural network used by this classifier should implement
     */
    public interface NeuralNetwork {
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
        double[] evaluate (TraceGroup traceGroup, int times);
    }

    /**
     * @brief Setter method for the Symbols that each neural network label is mapped to
     *
     * @param symbolLabels
     *     The Symbol labels
     */
    public void setSymbolLabels (Labels[][] symbolLabels) {
        symbolLabels_ = symbolLabels;
    }

    /**
     * @brief Getter method for the Symbols that each neural network label is mapped to
     *
     * @return The Symbol labels
     */
    public Labels[][] getSymbolLabels () {
        return symbolLabels_;
    }

    private SymbolFactory symbolFactory_ = SymbolFactory.getInstance(); //!< The factory used to
                                                                        //!< create Symbols

    private NeuralNetwork cascadeNeuralNetwork_ = null; //!< The first-level neural network
    private NeuralNetwork[] neuralNetworks_ = null; //!< The second-level neural networks

    private Labels[][] symbolLabels_ = new Labels[][] {}; //!< The Symbols that each neural network
                                                          //!< label is mapped to

}
