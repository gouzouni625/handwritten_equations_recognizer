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
import java.util.logging.Level;


/**
 * @class MainView
 * @brief Java Desktop graphics class that demonstrates the functionality of the Handwritten
 *        Equations Recognizer.
 */
public class MainView implements WindowListener, ActionListener {
    /**
     * @brief main method
     *
     * @param args
     *     arguments pass to main
     */
    public static void main (String[] args) {
        new MainView();
    }

    private static final HandwrittenEquationsRecognizer hwer_; //!< The HandwrittenEquationsRecognizer
                                                               //!< used by this MainView
    private static final DrawingPanel drawingPanel_; //!< The DrawingPanel used by this MainView
    private static final JTextField outputField_; //!< The text field where the recognized equation
                                                  //!< is displayed
    private static final JButton clearButton_; //!< The button to clear the DrawingPanel
    static {
        HandwrittenEquationsRecognizer hwer = null;
        try {
            hwer = new HandwrittenEquationsRecognizer();
            hwer.setLogLevel(Level.OFF);
        } catch (IOException e) {
            e.printStackTrace();

            System.exit(1);
        }
        hwer_ = hwer;

        outputField_ = new JTextField();
        outputField_.setPreferredSize(new Dimension(1536, 120));

        drawingPanel_ = new DrawingPanel(hwer_, outputField_);

        clearButton_ = new JButton("clear");
    }

    /**
     * @brief Default Constructor
     */
    public MainView () {
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

    /**
     * @brief Invoked the first time this MainView window is made visible
     *
     * @param windowEvent
     *     The windowEvent raised
     */
    public void windowOpened (WindowEvent windowEvent) {
    }

    /**
     * @brief Invoked when the user attempts to close this MainView window
     *
     * @param windowEvent
     *     The windowEvent raised
     */
    public void windowClosing (WindowEvent windowEvent) {
        drawingPanel_.terminate();

        System.exit(0);
    }

    /**
     * @brief Invoked when this MainView window has been closed
     *
     * @param windowEvent
     *     The windowEvent raised
     */
    public void windowClosed (WindowEvent windowEvent) {
    }

    /**
     * @brief Invoked when this MainView window is changed from a normal to a minimized state
     *
     * @param windowEvent
     *     The windowEvent raised
     */
    public void windowIconified (WindowEvent windowEvent) {
    }

    /**
     * @brief Invoked when this MainView window is changed from a minimized to a normal state
     *
     * @param windowEvent
     *     The windowEvent raised
     */
    public void windowDeiconified (WindowEvent windowEvent) {
    }

    /**
     * @brief Invoked when this MainView window is set to be the active window
     *
     * @param windowEvent
     *     The windowEvent raised
     */
    public void windowActivated (WindowEvent windowEvent) {
    }

    /**
     * @brief Invoked when this MainView window is no longer the active window
     *
     * @param windowEvent
     *     The windowEvent raised
     */
    public void windowDeactivated (WindowEvent windowEvent) {
    }

    /**
     * @brief Invoked when an action occurs
     *
     * @param actionEvent
     *     The ActionEvent raised
     */
    public void actionPerformed (ActionEvent actionEvent) {
        drawingPanel_.reset();
    }

}
