package org.hwer.implementations.classifiers.nnclassifier.neural_network;

import org.hwer.engine.utilities.traces.TraceGroup;

public interface NeuralNetwork {
  double[] evaluate(TraceGroup symbol, int times);
}
