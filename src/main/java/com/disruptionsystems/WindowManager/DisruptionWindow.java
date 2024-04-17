package com.disruptionsystems.WindowManager;

import javax.swing.*;
import java.util.List;

public class DisruptionWindow extends WindowManager{

    public JFrame frame;
    public DisruptionWindow(JFrame frame){
        this.frame = frame;
    }

    public JFrame getFrame() {
        return frame;
    }
}
