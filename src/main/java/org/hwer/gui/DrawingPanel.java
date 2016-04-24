package org.hwer.gui;


import org.hwer.api.HandwrittenEquationsRecognizer;
import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.image_processing.CoreImpl;

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


class DrawingPanel extends JPanel implements MouseInputListener{
    DrawingPanel (HandwrittenEquationsRecognizer hwer, JTextField outputField){
        this(hwer, outputField, 1536, 480);
    }

    public DrawingPanel(final HandwrittenEquationsRecognizer hwer, final JTextField outputField,
                        int width, int height){
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

                if(!currentEquation.equals(outputField_.getText())) {
                    outputField_.setText(hwer_.getEquation());
                }
            }
        }, new Date(System.currentTimeMillis()), 10);

        basicStroke_ = new BasicStroke(10);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setStroke(basicStroke_);

        for(int i = 0, n = traceGroup_.size();i < n;i++) {
            drawTrace(graphics2D, traceGroup_.get(i));
        }

        drawTrace(graphics2D, currentTrace_);
    }

    private void drawTrace(Graphics graphics, Trace trace){
        int numberOfPoints = trace.size();

        if(numberOfPoints == 1){
            Point point = trace.get(0);
            ((Graphics2D) graphics).fill(new Ellipse2D.Double(
                point.x_ - 5, dimension_.height - point.y_ - 5, 10, 10));

            return;
        }

        Point currentPoint;
        Point nextPoint;
        for(int i = 0;i < numberOfPoints - 1;i++){
            currentPoint = trace.get(i);
            nextPoint = trace.get(i + 1);

            graphics.drawLine((int)currentPoint.x_, dimension_.height - (int)currentPoint.y_,
                (int)nextPoint.x_, dimension_.height - (int)nextPoint.y_);
        }
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

    public void reset(){
        currentTrace_ = new Trace();
        unAppendedTraceGroup_ = new TraceGroup();
        traceGroup_ = new TraceGroup();

        hwer_.reset();

        outputField_.setText("");
    }

    private Trace currentTrace_;
    private TraceGroup unAppendedTraceGroup_; //!< The un-appended traces of this InputSurfaceView
    private TraceGroup traceGroup_;

    private int previousX_;
    private int previousY_;
    private boolean moved_; //!< Flag indicating whether there has been a move action on this
                            //!< DrawingPanel

    private Dimension dimension_;

    private final CoreImpl core_;

    private final int TOUCH_TOLERANCE = 4;

    private final HandwrittenEquationsRecognizer hwer_;
    private final JTextField outputField_;

    private final Timer timer_;
    private TimerTask appendInputTask_; //!< The append input task of this InputSurfaceView

    private final BasicStroke basicStroke_;

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

    public void mouseReleased (MouseEvent mouseEvent) {
        if(moved_) {
            currentTrace_.add(new Point(mouseEvent.getX(), dimension_.height - mouseEvent.getY()));
        }

        if(isEraseTrace(currentTrace_)){
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
        else{
            unAppendedTraceGroup_.add(currentTrace_);

            scheduleAppendInputTask();
        }
    }

    public void mouseDragged (MouseEvent mouseEvent) {
        int currentX = mouseEvent.getX();
        int currentY = mouseEvent.getY();

        int dx = currentX - previousX_;
        int dy = currentY - previousY_;

        if(Math.abs(dx) >= TOUCH_TOLERANCE || Math.abs(dy) >= TOUCH_TOLERANCE){
            moved_ = true;

            currentTrace_.add(new Point(currentX, dimension_.height - currentY));

            previousX_ = currentX;
            previousY_ = currentY;
        }
    }

    public void mouseClicked (MouseEvent mouseEvent) {}
    public void mouseEntered (MouseEvent mouseEvent) {}
    public void mouseExited (MouseEvent mouseEvent) {}
    public void mouseMoved (MouseEvent mouseEvent) {}

    public void terminate(){
        timer_.cancel();

        hwer_.terminate();
    }

}
