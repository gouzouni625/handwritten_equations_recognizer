package org.hwer.gui;


import org.hwer.api.HandwrittenEquationsRecognizer;

import javax.swing.JFrame;
import java.io.IOException;


public class MainView {

    private static final HandwrittenEquationsRecognizer hwer_;
    static {
        HandwrittenEquationsRecognizer hwer = null;
        try {
            hwer = new HandwrittenEquationsRecognizer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        hwer_ = hwer;
    }

    public static void main(String[] args){
        JFrame jFrame = new JFrame("Handwritten Equations Recognizer");
        jFrame.add(new DrawingPanel(hwer_));
        jFrame.pack();
        jFrame.setVisible(true);
    }

}
