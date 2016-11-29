package org.hwer.gui;


import org.hwer.sdk.HandwrittenEquationsRecognizer;
import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.image.processing.CoreImpl;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @class DrawingPanel
 * @brief Implements a canvas where the used can draw on using the mouse
 *        The DrawingPanel uses a HandwrittenEquationsRecognizer to recognize the input equation
 *        and also a text field to output the result.
 */
class DrawingPanel extends JPanel implements MouseInputListener {
    /**
     * @brief Constructor
     *
     * @param hwer
     *     The HandwrittenEquationsRecognizer of this DrawingPanel
     * @param outputField
     *     The output field of this DrawingPanel
     */
    DrawingPanel (HandwrittenEquationsRecognizer hwer, JTextField outputField) {
        this(hwer, outputField, 1536, 480);
    }

    /**
     * @brief Constructor
     *
     * @param hwer
     *     The HandwrittenEquationsRecognizer of this DrawingPanel
     * @param outputField
     *     The output field of this DrawingPanel
     * @param width
     *     The width of this DrawingPanel
     * @param height
     *     The height of this DrawingPanel
     */
    public DrawingPanel (final HandwrittenEquationsRecognizer hwer, final JTextField outputField,
                         int width, int height) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.lightGray);

        core_ = new CoreImpl();

        hwer_ = hwer;
        outputField_ = outputField;

        currentTrace_ = new Trace();
        unAppendedTraceGroup_ = new TraceGroup();
        traceGroup_ = new TraceGroup();

        dimension_ = new Dimension(width, height);
        this.setPreferredSize(dimension_);

        timer_ = new Timer();
        timer_.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                repaint();
            }
        }, new Date(System.currentTimeMillis()), 10);
        timer_.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                String currentEquation = hwer_.getEquation();

                if (! currentEquation.equals(outputField_.getText())) {
                    outputField_.setText(hwer_.getEquation());
                }
            }
        }, new Date(System.currentTimeMillis()), 10);

        basicStroke_ = new BasicStroke(10);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * @brief Calls the UI delegate's paint method, if the UI delegate is non-null
     *
     * @param graphics
     *    The Graphics object
     */
    @Override
    protected void paintComponent (Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setStroke(basicStroke_);

        for (int i = 0, n = traceGroup_.size(); i < n; i++) {
            drawTrace(graphics2D, traceGroup_.get(i));
        }

        drawTrace(graphics2D, currentTrace_);
    }

    /**
     * @brief Draws a Trace on a Graphics object
     *
     * @param graphics
     *     The Graphics object to draw on
     * @param trace
     *     The Trace to be drawn
     */
    private void drawTrace (Graphics graphics, Trace trace) {
        int numberOfPoints = trace.size();

        if (numberOfPoints == 1) {
            Point point = trace.get(0);
            ((Graphics2D) graphics).fill(new Ellipse2D.Double(
                point.x_ - 5, dimension_.height - point.y_ - 5, 10, 10));

            return;
        }

        Point currentPoint;
        Point nextPoint;
        for (int i = 0; i < numberOfPoints - 1; i++) {
            currentPoint = trace.get(i);
            nextPoint = trace.get(i + 1);

            graphics.drawLine((int) currentPoint.x_, dimension_.height - (int) currentPoint.y_,
                (int) nextPoint.x_, dimension_.height - (int) nextPoint.y_);
        }
    }

    /**
     * @brief Invoked when a mouse button has been pressed on this DrawingPanel
     *
     * @param mouseEvent
     *     The MouseEvent raised
     */
    public void mousePressed (MouseEvent mouseEvent) {
        if (appendInputTask_ != null) {
            appendInputTask_.cancel();
            timer_.purge();
        }

        moved_ = false;

        int currentX = mouseEvent.getX();
        int currentY = mouseEvent.getY();

        currentTrace_ = new Trace();
        currentTrace_.add(new Point(currentX, dimension_.height - currentY));

        previousX_ = currentX;
        previousY_ = currentY;
    }

    /**
     * @brief Invoked when a mouse button has been released on this DrawingPanel
     *
     * @param mouseEvent
     *     The MouseEvent raised
     */
    public void mouseReleased (MouseEvent mouseEvent) {
        if (moved_) {
            currentTrace_.add(new Point(mouseEvent.getX(), dimension_.height - mouseEvent.getY()));
        }

        if (isEraseTrace(currentTrace_)) {
            TraceGroup toBeRemoved = new TraceGroup();
            for (int i = 0; i < traceGroup_.size(); i++) {
                if (Trace.areOverlapped(traceGroup_.get(i), currentTrace_)) {
                    toBeRemoved.add(traceGroup_.get(i));
                }
            }

            if (hwer_.remove(toBeRemoved)) {
                for (int i = 0, n = toBeRemoved.size(); i < n; i++) {
                    traceGroup_.remove(toBeRemoved.get(i));
                }
            }

            currentTrace_ = new Trace();
        }
        else {
            unAppendedTraceGroup_.add(currentTrace_);

            scheduleAppendInputTask();
        }
    }

    /**
     * @brief Invoked when a mouse button is pressed on this DrawingPanel and then dragged
     *
     * @param mouseEvent
     *     The MouseEvent raised
     */
    public void mouseDragged (MouseEvent mouseEvent) {
        int currentX = mouseEvent.getX();
        int currentY = mouseEvent.getY();

        int dx = currentX - previousX_;
        int dy = currentY - previousY_;

        if (Math.abs(dx) >= TOUCH_TOLERANCE || Math.abs(dy) >= TOUCH_TOLERANCE) {
            moved_ = true;

            currentTrace_.add(new Point(currentX, dimension_.height - currentY));

            previousX_ = currentX;
            previousY_ = currentY;
        }
    }

    /**
     * @brief Invoked when the mouse button has been clicked (pressed and released) on this
     *        DrawingPanel
     *
     * @param mouseEvent
     *     The MouseEvent raised
     */
    public void mouseClicked (MouseEvent mouseEvent) {
    }

    /**
     * @brief Invoked when the mouse enters this DrawingPanel
     *
     * @param mouseEvent
     *     The MouseEvent raised
     */
    public void mouseEntered (MouseEvent mouseEvent) {
    }

    /**
     * @brief Invoked when the mouse exits this DrawingPanel
     *
     * @param mouseEvent
     *     The MouseEvent raised
     */
    public void mouseExited (MouseEvent mouseEvent) {
    }

    /**
     * @brief Invoked when the mouse cursor has been moved onto this DrawingPanel but no buttons
     *        have been pushed
     *
     * @param mouseEvent
     *     The MouseEvent raised
     */
    public void mouseMoved (MouseEvent mouseEvent) {
    }

    /**
     * @brief Resets this DrawingPanel
     *        To reset a DrawingPanel is to bring it to the state it was when instantiated
     */
    public void reset () {
        currentTrace_ = new Trace();
        unAppendedTraceGroup_ = new TraceGroup();
        traceGroup_ = new TraceGroup();

        hwer_.reset();

        outputField_.setText("");
    }

    /**
     * @brief Terminates this DrawingPanel
     */
    public void terminate () {
        timer_.cancel();

        hwer_.terminate();
    }

    /**
     * @brief Schedules an AppendInputTask to the Timer of this InputSurfaceView
     */
    private void scheduleAppendInputTask () {
        appendInputTask_ = new AppendInputTask();
        timer_.schedule(appendInputTask_, new Date(System.currentTimeMillis() + 100));
    }

    /**
     * @class AppendInputTask
     * @brief Implements a TimerTask that will handle the newly added Traces
     */
    private class AppendInputTask extends TimerTask {
        @Override
        public void run () {
            if (hwer_.append(unAppendedTraceGroup_)) {
                traceGroup_.add(unAppendedTraceGroup_);
                unAppendedTraceGroup_ = new TraceGroup();
            }
            else if (unAppendedTraceGroup_.size() <= 5) {
                scheduleAppendInputTask();
            }
            else {
                currentTrace_ = new Trace();
                traceGroup_ = new TraceGroup();
                unAppendedTraceGroup_ = new TraceGroup();
                hwer_.reset();
            }
        }
    }

    /**
     * @brief Returns true if the given Trace is an erase Trace
     *        An erase Trace is used to remove some of the existing Traces. Concretely, the Traces
     *        that are overlapped by the erase Trace should be removed.
     *
     * @param trace
     *     The given Trace
     *
     * @return True if the given Trace is an erase Trace
     */
    private boolean isEraseTrace (Trace trace) {
        if (trace == null) {
            return false;
        }

        int numberOfPairs = trace.size() - 1;
        if (numberOfPairs <= 0) {
            return false;
        }

        double meanSlope = 0;

        int numberOfNonVerticalPairs = 0;
        for (int i = 0; i < numberOfPairs; i++) {
            double dx = trace.get(i + 1).x_ - trace.get(i).x_;
            double dy = trace.get(i + 1).y_ - trace.get(i).y_;

            if (dx > 0) {
                return false;
            }
            else if (dx < 0) {
                meanSlope += dy / dx;
                numberOfNonVerticalPairs++;
            }
        }

        if (numberOfNonVerticalPairs == 0) {
            // We have a vertical line.
            return false;
        }

        meanSlope /= numberOfNonVerticalPairs;

        double meanAngle = Math.atan(meanSlope);

        return ! ((meanAngle <= - Math.PI / 9) || (meanAngle >= Math.PI / 9));
    }

    private Trace currentTrace_; //!< The current Trace of this DrawingPanel
    private TraceGroup unAppendedTraceGroup_; //!< The un-appended traces of this DrawingPanel
    private TraceGroup traceGroup_; //!< The TraceGroup of this DrawingPanel

    private int previousX_; //!< The x coordinate of the last touch on this DrawingPanel
    private int previousY_; //!< The y coordinate of the last touch on this DrawingPanel
    private boolean moved_; //!< Flag indicating whether there has been a move action on this
                            //!< DrawingPanel

    private Dimension dimension_; //!< THe dimensions of this DrawingPanel
    private final BasicStroke basicStroke_; //!< The Stroke used by this DrawingPanel

    private final CoreImpl core_; //!< The image processing Core of this DrawingPanel

    private final int TOUCH_TOLERANCE = 4; //!< The sensitivity of this DrawingPanel as to whether a
                                           //!< move action event has occurred

    private final HandwrittenEquationsRecognizer hwer_; //!< The HandwrittenEquationsRecognizer of
                                                        //!< of this DrawingPanel
    private final JTextField outputField_; //!< The output field of this DrawingPanel

    private final Timer timer_; //!< The Timer of this DrawingPanel
    private TimerTask appendInputTask_; //!< The append input task of this DrawingPanel

}
