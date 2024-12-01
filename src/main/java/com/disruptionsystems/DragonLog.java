package com.disruptionsystems;

import com.disruptionsystems.logging.DisruptionLogger;
import com.disruptionsystems.logging.LogLevel;
import java.io.*;
import java.util.Properties;

public class DragonLog extends DisruptionLogger{

    public DragonLog(){
        main(new String[]{""});
    }

    public static String logLocation;
    public static void main(String[] args){
        Properties props = new Properties();
        File configFile = new File("config.chorus");
        if (!configFile.exists()){
            try {
                createBaseFileStructure();
                createDefaultFileLayout(props, configFile);
            }
            catch (IOException e){
                System.out.println("Could not create or read file: config.chorus. Reason:\n" + e.getMessage());
                System.exit(2);
            }
        }
        try {
            props.load(new FileInputStream(configFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logLocation = props.getProperty("logLocation");
        DisruptionLogger logger = new DisruptionLogger();
        logger.printToLog(LogLevel.INFORMATION, "System Startup complete. Awaiting Input");
    }
    public static void createBaseFileStructure(){
        File LogFolderFile = new File("data/log/");
        if (!LogFolderFile.exists()){
            LogFolderFile.mkdirs();
        }
    }
    public static String getLogLocation(){
        return logLocation;
    }

    public static void createDefaultFileLayout(Properties props, File configFile) throws IOException {
        props.setProperty("logLocation", "data/log/log.chorus");
        props.store(new FileWriter(configFile), null);
    }
}
