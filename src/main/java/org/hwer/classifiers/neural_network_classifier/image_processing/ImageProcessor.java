package org.hwer.classifiers.neural_network_classifier.image_processing;


import org.hwer.engine.utilities.traces.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageProcessor implements Core{
  public ImageProcessor() {
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

  public BufferedImage printTraceGroup (TraceGroup traceGroup, int width, int height, int thickness) {
    // Work on a copy of this trace group.
    TraceGroup traceGroupCopy = new TraceGroup(traceGroup);

    traceGroupCopy.calculateCorners();

    traceGroupCopy.subtract(new org.hwer.engine.utilities.traces.Point(traceGroupCopy.getTopLeftCorner().x_,
        traceGroupCopy.getBottomRightCorner().y_));

    int originalWidth = (int)traceGroupCopy.getWidth();
    if (originalWidth < 30) {
      originalWidth = 30;
    }
    int originalHeight = (int)traceGroupCopy.getHeight();
    if (originalHeight < 30) {
      originalHeight = 30;
    }

    int horizontalBorder = originalWidth >> 1;
    if (horizontalBorder < 50) {
      horizontalBorder = 50;
    }
    int verticalBorder = originalHeight >> 1;
    if (verticalBorder < 50) {
      verticalBorder = 50;
    }

    BufferedImage image = new BufferedImage((int)(originalWidth * 1.1), (int)(originalHeight * 1.1),
        BufferedImage.TYPE_BYTE_GRAY);

    for(int i = 0;i < traceGroupCopy.size();i++){
      printTrace(traceGroupCopy.get(i), image, thickness);
    }

    BufferedImage borderedImage = new BufferedImage(image.getWidth() + (horizontalBorder << 1),
        image.getHeight() + (verticalBorder << 1), image.getType());

    Graphics2D graphics2D = borderedImage.createGraphics();

    graphics2D.drawImage(image, horizontalBorder, verticalBorder,
        horizontalBorder + image.getWidth(), verticalBorder + image.getHeight(), 0, 0,
        image.getWidth(), image.getHeight(), null);

    graphics2D.dispose();

    /*BufferedImage largeImage = new BufferedImage(1000, 1000, borderedImage.getType());

    graphics2D = largeImage.createGraphics();

    graphics2D.drawImage(borderedImage, 0, 0, 1000, 1000, 0, 0, borderedImage.getWidth(),
        borderedImage.getHeight(), null);

    graphics2D.dispose();

    float[] matrix = new float[40000];
    for (int i = 0; i < 40000; i++) {
      matrix[i] = (float) 1 / 40000;
    }

    BufferedImageOp bufferedImageOp = new ConvolveOp(new Kernel(200, 200, matrix),
        ConvolveOp.EDGE_NO_OP, null);
    BufferedImage blurredImage = bufferedImageOp.filter(largeImage, null);*/

    BufferedImage resizedImage = new BufferedImage(width, height, borderedImage.getType());

    graphics2D = resizedImage.createGraphics();

    graphics2D.drawImage(borderedImage, 0, 0, width, height, 0, 0, borderedImage.getWidth(),
        borderedImage.getHeight(), null);

    graphics2D.dispose();

    // Flip image.
    AffineTransform affineTransform = AffineTransform.getScaleInstance(1, -1);
    affineTransform.translate(0, -resizedImage.getHeight());
    AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform,
        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    resizedImage = affineTransformOp.filter(resizedImage, null);

    return resizedImage;
  }
}
