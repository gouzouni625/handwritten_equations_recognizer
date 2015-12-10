package org.hwer.custom_classifiers.neural_network_classifier.neural_network;

import org.hwer.engine.utilities.traces.TraceGroup;

public interface NeuralNetwork {
  double[] evaluate(TraceGroup symbol, int times);
}
