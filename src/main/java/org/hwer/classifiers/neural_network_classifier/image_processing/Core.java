package org.hwer.classifiers.neural_network_classifier.image_processing;

import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.awt.image.BufferedImage;


public interface Core {
  BufferedImage printTrace(Trace trace, BufferedImage image, int thickness);
  BufferedImage printTraceGroup(TraceGroup traceGroup, int width, int height, int thickness);
}
