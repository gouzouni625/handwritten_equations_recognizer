package org.hwer.api;


import org.hwer.engine.classifiers.NNClassifier;
import org.hwer.engine.classifiers.NNClassifier.NeuralNetwork;
import org.hwer.engine.classifiers.neural_network.NeuralNetworkImpl;
import org.hwer.engine.parsers.GrammarParser;
import org.hwer.engine.parsers.grammars.GeometricalGrammar;
import org.hwer.engine.partitioners.MSTPartitioner;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.image_processing.CoreImpl;
import org.hwer.image_processing.DistorterImpl;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.logging.Level;


public class HandwrittenEquationsRecognizer {
    public HandwrittenEquationsRecognizer() throws IOException {
        CoreImpl core = new CoreImpl();
        DistorterImpl distorter = new DistorterImpl();

        FileInputStream neuralNetworkInputStream = new FileInputStream(
            getResourcePath(
                "/neural_networks/cascade_neural_network.bin"
            )
        );
        NeuralNetworkImpl cascadeNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        cascadeNeuralNetwork.loadFromInputStream(neuralNetworkInputStream);

        neuralNetworkInputStream = new FileInputStream(
            getResourcePath(
                "/neural_networks/numbers_neural_network.bin"
            )
        );
        NeuralNetworkImpl numbersNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        numbersNeuralNetwork.loadFromInputStream(neuralNetworkInputStream);

        neuralNetworkInputStream = new FileInputStream(
            getResourcePath(
                "/neural_networks/variables_neural_network.bin"
            )
        );
        NeuralNetworkImpl variablesNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        variablesNeuralNetwork.loadFromInputStream(neuralNetworkInputStream);

        neuralNetworkInputStream = new FileInputStream(
            getResourcePath(
                "/neural_networks/operators_neural_network.bin"
            )
        );
        NeuralNetworkImpl operatorsNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        operatorsNeuralNetwork.loadFromInputStream(neuralNetworkInputStream);

        neuralNetworkInputStream = new FileInputStream(
            getResourcePath(
                "/neural_networks/letters_neural_network.bin"
            )
        );
        NeuralNetworkImpl lettersNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        lettersNeuralNetwork.loadFromInputStream(neuralNetworkInputStream);

        NNClassifier neuralNetworkClassifier = new NNClassifier(
            cascadeNeuralNetwork,
            new NeuralNetwork[] {
                numbersNeuralNetwork,
                variablesNeuralNetwork,
                operatorsNeuralNetwork,
                lettersNeuralNetwork
            },
            new Labels[][]{
                {
                    Labels.CIRCLE,
                    Labels.ONE,
                    Labels.TWO,
                    Labels.THREE,
                    Labels.FOUR,
                    Labels.S_LIKE,
                    Labels.SIX,
                    Labels.SEVEN,
                    Labels.EIGHT,
                    Labels.G_LIKE
                },
                {
                    Labels.LOWER_X,
                    Labels.LOWER_Y
                },
                {
                    Labels.PLUS,
                    Labels.EQUALS,
                    Labels.HORIZONTAL_LINE,
                    Labels.SQUARE_ROOT,
                    Labels.C_LIKE,
                    Labels.RIGHT_PARENTHESIS,
                    Labels.GREATER_THAN,
                    Labels.LESS_THAN,
                    Labels.VERTICAL_LINE
                },
                {
                    Labels.LOWER_A,
                    Labels.LOWER_E,
                    Labels.LOWER_I,
                    Labels.LOWER_L,
                    Labels.LOWER_N,
                    Labels.LOWER_T
                }
            }
        );

        partitioner_ = new MSTPartitioner(neuralNetworkClassifier);
        parser_ = new GrammarParser(new GeometricalGrammar());

        partitioner_.logger_.setLevel(Level.OFF);
    }

    private String getResourcePath(String resourceName){
        return HandwrittenEquationsRecognizer.class.getResource(resourceName).getPath();
    }

    private MSTPartitioner partitioner_;
    private GrammarParser parser_;
}
