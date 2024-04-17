package com.disruptionsystems.WindowManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class WindowManager extends WindowComponentManager {
    List<JFrame> frames = new ArrayList<>();

    public WindowManager(){
        List<JFrame> frames = this.frames;
    }

    public DisruptionWindow createNewFrame(String title) {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setVisible(true);
        this.frames.add(frame);
        return new DisruptionWindow(frame);
    }

    public void removeFrame(int index){
        this.frames.remove(index);
    }

    public List<JFrame> getFrames(){
        return this.frames;
    }
}
