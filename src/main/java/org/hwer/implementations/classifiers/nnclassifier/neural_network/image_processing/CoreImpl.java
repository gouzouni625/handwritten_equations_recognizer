package org.hwer.implementations.classifiers.nnclassifier.neural_network.image_processing;


import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.implementations.classifiers.nnclassifier.neural_network.NeuralNetworkImpl.Core;
import org.hwer.implementations.classifiers.nnclassifier.neural_network.NeuralNetworkImpl.Image;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


/**
 * @class CoreImpl
 * @brief Implementation of the Core API from NeuralNetworkImpl using the java AWT package
 */
public class CoreImpl implements Core {
    /**
     * @brief Transforms a Trace to a java.awt.image.BufferedImage
     *
     * @param trace
     *     The Trace to be transformed
     * @param image
     *     The java.awt.image.BufferedImage
     * @param thickness
     *     The thickness of lines on the java.awt.image.BufferedImage
     *
     * @return The java.awt.image.BufferedImage created by the given Trace
     */
    private BufferedImage printTrace (Trace trace, BufferedImage image, int thickness) {
        int numberOfLines = trace.size() - 1;

        Graphics2D graphics2D = image.createGraphics();

        graphics2D.setStroke(new BasicStroke(thickness));

        for (int i = 0; i < numberOfLines; i++) {
            graphics2D.draw(new Line2D.Double((int) trace.get(i).x_, (int) (trace.get(i).y_),
                (int) (trace.get(i + 1).x_), (int) (trace.get(i + 1).y_)));
        }

        graphics2D.dispose();

        return image;
    }

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
    public Image printTraceGroup (TraceGroup traceGroup, int width, int height, int thickness) {
        // Work on a copy of this trace group.
        TraceGroup traceGroupCopy = new TraceGroup(traceGroup);

        double traceGroupWidth = traceGroupCopy.getWidth();
        double traceGroupHeight = traceGroupCopy.getHeight();

        traceGroupCopy.subtract(new Point(traceGroupCopy.getTopLeftCorner().x_,
            traceGroupCopy.getBottomRightCorner().y_));

        double divisionFactor = Math.max(traceGroupWidth, traceGroupHeight);

        traceGroupCopy.multiplyBy(new Point(0.6 * width / divisionFactor,
            0.6 * height / divisionFactor));

        Point traceGroupCentroid = traceGroupCopy.getCentroid();

        traceGroupCopy.subtract(new Point(- (width / 2 - traceGroupCentroid.x_),
            - (height / 2 - traceGroupCentroid.y_)));

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 0; i < traceGroupCopy.size(); i++) {
            printTrace(traceGroupCopy.get(i), image, thickness);
        }

        // Flip image.
        AffineTransform affineTransform = AffineTransform.getScaleInstance(1, - 1);
        affineTransform.translate(0, - image.getHeight());
        image = new AffineTransformOp(affineTransform,
            AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(image, null);

        return (new ImageImpl(image));
    }

}
