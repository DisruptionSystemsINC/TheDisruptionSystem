package com.disruptionsystems.WindowManager;

import javax.swing.*;
import java.util.List;

public class WindowComponentManager{
    public WindowComponentManager(JFrame frame) {
        super(frame);
    }

    public void addComponents(List<JComponent> components){
        for (JComponent comp : components) {
                getFrame().add(comp);
        }
    }

}
