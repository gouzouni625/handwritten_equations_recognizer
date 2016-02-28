package org.hwer.implemented_classifiers.neural_network_classifier.neural_network.image_processing;

import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class CoreImpl implements Core{
  public CoreImpl () {
  }

  public BufferedImage printTrace (Trace trace, BufferedImage image, int thickness) {
    int numberOfLines = trace.size() - 1;

    Graphics2D graphics2D = image.createGraphics();

    graphics2D.setStroke(new BasicStroke(thickness));

    for(int i = 0;i < numberOfLines;i++){
      graphics2D.draw(new Line2D.Double((int) trace.get(i).x_, (int) (trace.get(i).y_),
          (int) (trace.get(i + 1).x_), (int) (trace.get(i + 1).y_)));
    }

    graphics2D.dispose();

    return image;
  }

  public Image printTraceGroup (TraceGroup traceGroup, int width, int height, int thickness) {
    // Work on a copy of this trace group.
    TraceGroup traceGroupCopy = new TraceGroup(traceGroup);

    traceGroupCopy.calculateCorners();

    double traceGroupWidth = traceGroupCopy.getWidth();
    double traceGroupHeight = traceGroupCopy.getHeight();

    traceGroupCopy.subtract(new Point(traceGroupCopy.getTopLeftCorner().x_,
        traceGroupCopy.getBottomRightCorner().y_));

    double divisionFactor = Math.max(traceGroupWidth, traceGroupHeight);

    traceGroupCopy.multiplyBy(new Point(0.6 * width / divisionFactor, 0.6 * height / divisionFactor));

    traceGroupCopy.calculateCorners();

    Point traceGroupCentroid = traceGroupCopy.getCentroid();

    traceGroupCopy.subtract(new Point(-(width / 2 - traceGroupCentroid.x_), -(height / 2 - traceGroupCentroid.y_)));

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

    for(int i = 0;i < traceGroupCopy.size();i++){
      printTrace(traceGroupCopy.get(i), image, thickness);
    }

    // Flip image.
    AffineTransform affineTransform = AffineTransform.getScaleInstance(1, -1);
    affineTransform.translate(0, -image.getHeight());
    image = new AffineTransformOp(affineTransform,
        AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(image, null);

    return (new ImageImpl(image));
  }
}
