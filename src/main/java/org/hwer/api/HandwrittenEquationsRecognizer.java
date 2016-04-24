package org.hwer.api;


import org.hwer.api.concurrency.Consumer;
import org.hwer.api.concurrency.tasks.EvaluateTask;
import org.hwer.api.concurrency.tasks.RemoveTask;
import org.hwer.api.concurrency.tasks.ResetParserTask;
import org.hwer.engine.classifiers.NNClassifier;
import org.hwer.engine.classifiers.NNClassifier.NeuralNetwork;
import org.hwer.engine.classifiers.neural_network.NeuralNetworkImpl;
import org.hwer.engine.parsers.GrammarParser;
import org.hwer.engine.parsers.grammars.GeometricalGrammar;
import org.hwer.engine.partitioners.MSTPartitioner;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.image_processing.CoreImpl;
import org.hwer.image_processing.DistorterImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


/**
 * @class HandwrittenEquationsRecognizer
 * @brief Wraps the whole engine in a minimum API.
 */
public class HandwrittenEquationsRecognizer {
    /**
     * @brief Default constructor
     *
     * @throws IOException If any of the resources is not found
     */
    public HandwrittenEquationsRecognizer () throws IOException {
        CoreImpl core = new CoreImpl();
        DistorterImpl distorter = new DistorterImpl();

        NeuralNetworkImpl cascadeNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        cascadeNeuralNetwork.loadFromInputStream(
            getResource("/neural_networks/cascade_neural_network.bin")
        );

        NeuralNetworkImpl numbersNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        numbersNeuralNetwork.loadFromInputStream(
            getResource("/neural_networks/numbers_neural_network.bin")
        );

        NeuralNetworkImpl variablesNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        variablesNeuralNetwork.loadFromInputStream(
            getResource("/neural_networks/variables_neural_network.bin")
        );

        NeuralNetworkImpl operatorsNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        operatorsNeuralNetwork.loadFromInputStream(
            getResource("/neural_networks/operators_neural_network.bin")
        );

        NeuralNetworkImpl lettersNeuralNetwork = new NeuralNetworkImpl(core, distorter);
        lettersNeuralNetwork.loadFromInputStream(
            getResource("/neural_networks/letters_neural_network.bin")
        );

        NNClassifier neuralNetworkClassifier = new NNClassifier(
            cascadeNeuralNetwork,
            new NeuralNetwork[] {
                numbersNeuralNetwork,
                variablesNeuralNetwork,
                operatorsNeuralNetwork,
                lettersNeuralNetwork
            },
            new Labels[][] {
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

        tasksQueue_ = new ArrayBlockingQueue<>(5);
        consumer_ = new Consumer(tasksQueue_);
        new Thread(consumer_).start();
    }

    /**
     * @brief Returns an InputStream on a resource given its name
     *
     * @param resourceName
     *     The resource name
     *
     * @return An InputStream on a resource given its name
     */
    private InputStream getResource (String resourceName) {
        return HandwrittenEquationsRecognizer.class.getResourceAsStream(resourceName);
    }

    /**
     * @brief Set the logging Level of this HandwrittenEquationsRecognizer
     *
     * @param level
     *     The new logging Level
     */
    public void setLogLevel(Level level){
        partitioner_.logger_.setLevel(level);
        parser_.logger_.setLevel(level);
    }

    /**
     * @brief Terminates this HandwrittenEquationsRecognizer
     */
    public void terminate () {
        consumer_.setRunning(false);
    }

    /**
     * @brief Returns the String representation of the equation recognized
     *
     * @return The String representation of the equation recognized
     */
    public String getEquation () {
        if (tasksQueue_.size() != 0) {
            return "recognizing...";
        }

        String equation = parser_.getEquation();
        if (equation.contains("AMBIGUOUS")) {
            equation = "AMBIGUOUS";
        }

        return equation;
    }

    /**
     * @brief Resets this HandwrittenEquationsRecognizer
     *        To reset a HandwrittenEquationsRecognizer is to bring it to the state it was when
     *        it was instantiated.
     */
    public void reset () {
        tasksQueue_.clear();

        resetParser();
    }

    /**
     * @brief Schedules the Parser to be reset
     *        The scheduling is done by adding a ResetParserTask to the tasksQueue.
     *
     * @return True if the tasksQueue has accepted the new task
     */
    private boolean resetParser () {
        return tasksQueueOffer(new ResetParserTask(parser_));
    }

    /**
     * @brief Schedules the given TraceGroup to be appended
     *        The scheduling is done by adding an EvaluateTask to the tasksQueue.
     *
     * @param traceGroup
     *     The TraceGroup to be appended
     *
     * @return True if the tasksQueue has accepted the new task
     */
    public boolean append (TraceGroup traceGroup) {
        return traceGroup != null && traceGroup.size() != 0 &&
            tasksQueueOffer(new EvaluateTask(partitioner_, parser_, traceGroup));
    }

    /**
     * @brief Schedules the given TraceGroup to be removed
     *        The scheduling is done by adding a RemoveTask to the tasksQueue.

     * @param traceGroup
     *     The TraceGroup to be removed
     *
     * @return True if the tasksQueue has accepted the new task
     */
    public boolean remove (TraceGroup traceGroup) {
        return traceGroup != null && traceGroup.size() != 0 &&
            tasksQueueOffer(new RemoveTask(partitioner_, parser_, traceGroup));
    }

    /**
     * @brief Offers a tasks to the tasksQueue
     *
     * @param task
     *     The task
     *
     * @return True if the tasksQueue accepts the task
     */
    private boolean tasksQueueOffer (Runnable task) {
        try {
            return tasksQueue_.offer(task, TASKS_QUEUE_OFFER_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    private MSTPartitioner partitioner_; //!< The partitioner of this HandwrittenEquationsRecognizer
    private GrammarParser parser_; //!< The parser of this HandwrittenEquationsRecognizer

    private Consumer consumer_; //!< The consumer that executes the tasks from the tasks queue

    private ArrayBlockingQueue<Runnable> tasksQueue_; //!< The queue where the tasks are sent

    private static final long TASKS_QUEUE_OFFER_TIMEOUT = 100; //!< The time to wait on the
                                                               //!< tasksQueue to accept a task in
                                                               //!< milliseconds

}
