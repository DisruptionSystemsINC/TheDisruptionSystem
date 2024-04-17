package com.disruptionsystems;

import com.disruptionsystems.WindowManager.WindowManager;
import com.disruptionsystems.logging.DisruptionLogger;
import com.disruptionsystems.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class SystemLauncher {
    public static String logLocation;

    public static void main(String[] args){
        readConfig();
        createBaseFileStructure();
        DisruptionLogger.printToLog(LogLevel.INFORMATION, "System Startup complete. Awaiting Input");

        WindowManager man = new WindowManager();
        JButton button1 = new JButton();
        JButton button2 = new JButton();
        JButton button3 = new JButton();
        JButton button4 = new JButton();
        JButton button5 = new JButton();
        JButton button6 = new JButton();


        button1.setText("Nuclear1");
        button2.setText("Nuclear2");
        button3.setText("Nuclear3");
        button4.setText("Nuclear4");
        button5.setText("Nuclear5");
        button6.setText("Nuclear6");
        button1.setBounds(55, 55, 30, 30);

        button1.setVisible(true);
        button2.setVisible(true);
        button3.setVisible(true);
        button4.setVisible(true);
        button5.setVisible(true);
        button6.setVisible(true);

        List<JComponent> buttons = new ArrayList<>(List.of(button1, button2, button3, button4, button5, button6));


        man.createNewFrame("Window One").addComponents(buttons);
        man.createNewFrame("Window Two").addComponents(buttons);
        man.createNewFrame("Window Three").addComponents(buttons);
    }



    public static void readConfig(){
        File configFile = new File("config.chorus");
        if (!configFile.exists()){
            try {
                createDefaultFileLayout(configFile);
            }
            catch (IOException e){
                DisruptionLogger.printToLog(LogLevel.CRASH, "Could not create or read file: config.chorus. Reason:\n" + e.getMessage());
                System.exit(2);
            }
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(configFile);
            logLocation = node.get("logFile").asText();
        }
        catch (IOException e){
            DisruptionLogger.printToLog(LogLevel.CRASH, "Invalid Config File Structure detected... Aborting Startup");
            System.exit(3);
        }
    }
    public static void createBaseFileStructure(){
        File LogFolderFile = new File(logLocation);
        if (!LogFolderFile.exists()){
            LogFolderFile.mkdirs();
        }
    }

    public static void createDefaultFileLayout(File file) throws IOException {
        file.createNewFile();
        String s = File.separator;
        PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        w.append("{\n");
        w.append("  \"logFile\":\"data" + s + "logging" + s + "\"\n");
        w.append("}");
        w.close();
    }
}
