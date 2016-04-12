package org.hwer.gui;


import org.hwer.api.HandwrittenEquationsRecognizer;
import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.image_processing.CoreImpl;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class DrawingPanel extends JPanel implements MouseInputListener{
    public DrawingPanel(HandwrittenEquationsRecognizer hwer){
        this(hwer, 1024, 768);
    }

    public DrawingPanel(HandwrittenEquationsRecognizer hwer, int width, int height){
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.lightGray);

        core_ = new CoreImpl();

        hwer_ = hwer;

        traceGroup_ = new TraceGroup();
        currentTrace_ = new Trace();
        dimension_ = new Dimension(width, height);

        timer_ = new Timer();
        timer_.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                repaint();
            }
        }, new Date(System.currentTimeMillis()), 1);

        basicStroke_ = new BasicStroke(10);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    public Dimension getPreferredSize(){
        return dimension_;
    }

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        ((Graphics2D) graphics).setStroke(basicStroke_);

        for(Trace trace : traceGroup_){
            drawTrace(graphics, trace);
        }

        drawTrace(graphics, currentTrace_);
    }

    private void drawTrace(Graphics graphics, Trace trace){
        int numberOfPoints = trace.size();

        Point currentPoint;
        Point nextPoint;
        for(int i = 0;i < numberOfPoints - 1;i++){
            currentPoint = trace.get(i);
            nextPoint = trace.get(i + 1);

            graphics.drawLine((int)currentPoint.x_, (int)currentPoint.y_,
                (int)nextPoint.x_, (int)nextPoint.y_);
        }
    }

    private TraceGroup traceGroup_;
    private Trace currentTrace_;

    private int previousX_;
    private int previousY_;

    private Dimension dimension_;

    private final CoreImpl core_;

    private final int TOUCH_TOLERANCE = 4;

    private final HandwrittenEquationsRecognizer hwer_;

    private final Timer timer_;

    private final BasicStroke basicStroke_;

    public void mousePressed (MouseEvent mouseEvent) {
        int currentX = mouseEvent.getX();
        int currentY = mouseEvent.getY();

        currentTrace_.add(new Point(currentX, currentY));

        previousX_ = currentX;
        previousY_ = currentY;
    }

    public void mouseReleased (MouseEvent mouseEvent) {
        currentTrace_.add(new Point(mouseEvent.getX(), mouseEvent.getY()));

        traceGroup_.add(currentTrace_);

        currentTrace_ = new Trace();
    }

    public void mouseDragged (MouseEvent mouseEvent) {
        int currentX = mouseEvent.getX();
        int currentY = mouseEvent.getY();

        int dx = currentX - previousX_;
        int dy = currentY - previousY_;

        if(Math.abs(dx) >= TOUCH_TOLERANCE || Math.abs(dy) >= TOUCH_TOLERANCE){
            currentTrace_.add(new Point(currentX, currentY));

            previousX_ = currentX;
            previousY_ = currentY;
        }
    }

    public void mouseClicked (MouseEvent mouseEvent) {}
    public void mouseEntered (MouseEvent mouseEvent) {}
    public void mouseExited (MouseEvent mouseEvent) {}
    public void mouseMoved (MouseEvent mouseEvent) {}

}
