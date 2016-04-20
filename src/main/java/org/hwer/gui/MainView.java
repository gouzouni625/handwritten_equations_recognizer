package org.hwer.gui;


import org.hwer.api.HandwrittenEquationsRecognizer;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;


public class MainView implements WindowListener{
    public static void main(String[] args){
        new MainView();
    }

    private static final HandwrittenEquationsRecognizer hwer_;
    private static final DrawingPanel drawingPanel_;
    private static final JTextField outputField_;
    static {
        HandwrittenEquationsRecognizer hwer = null;
        try {
            hwer = new HandwrittenEquationsRecognizer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hwer_ = hwer;

        outputField_ = new JTextField();
        outputField_.setPreferredSize(new Dimension(1536, 120));

        drawingPanel_ = new DrawingPanel(hwer_, outputField_);
    }

    public MainView(){
        JFrame jFrame = new JFrame("Handwritten Equations Recognizer");
        jFrame.addWindowListener(this);

        GridLayout gridLayout = new GridLayout(2, 1);
        jFrame.setLayout(gridLayout);
        jFrame.add(drawingPanel_);
        jFrame.add(outputField_);

        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void windowOpened (WindowEvent windowEvent) {}
    public void windowClosing (WindowEvent windowEvent) {
        drawingPanel_.terminate();
        hwer_.terminate();

        System.exit(0);
    }
    public void windowClosed (WindowEvent windowEvent) {}
    public void windowIconified (WindowEvent windowEvent) {}
    public void windowDeiconified (WindowEvent windowEvent) {}
    public void windowActivated (WindowEvent windowEvent) {}
    public void windowDeactivated (WindowEvent windowEvent) {}

}
