package org.hwer.gui;


import org.hwer.api.HandwrittenEquationsRecognizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;


public class MainView implements WindowListener, ActionListener{
    public static void main(String[] args){
        new MainView();
    }

    private static final HandwrittenEquationsRecognizer hwer_;
    private static final DrawingPanel drawingPanel_;
    private static final JTextField outputField_;
    private static final JButton clearButton_;
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

        clearButton_ = new JButton("clear");
    }

    public MainView(){
        JFrame jFrame = new JFrame("Handwritten Equations Recognizer");
        jFrame.addWindowListener(this);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        JPanel outputPanel = new JPanel(new GridLayout(1, 3));
        outputPanel.setPreferredSize(new Dimension(100, 40));
        outputPanel.add(outputField_);
        outputPanel.add(clearButton_);
        clearButton_.addActionListener(this);

        JPanel inputPanel = new JPanel(new GridLayout(1, 1));
        inputPanel.add(drawingPanel_);

        mainPanel.add(outputPanel);
        mainPanel.add(inputPanel);

        jFrame.add(mainPanel);

        jFrame.pack();
        jFrame.setVisible(true);
    }

    public void windowOpened (WindowEvent windowEvent) {}
    public void windowClosing (WindowEvent windowEvent) {
        drawingPanel_.terminate();

        System.exit(0);
    }
    public void windowClosed (WindowEvent windowEvent) {}
    public void windowIconified (WindowEvent windowEvent) {}
    public void windowDeiconified (WindowEvent windowEvent) {}
    public void windowActivated (WindowEvent windowEvent) {}
    public void windowDeactivated (WindowEvent windowEvent) {}

    public void actionPerformed (ActionEvent actionEvent) {
        drawingPanel_.reset();
    }

}
