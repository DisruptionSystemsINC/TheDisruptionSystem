package com.disruptionsystems.dragonlog;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class DragonLog {

    public DragonLog(String configLocation){
        init();
        this.configLocation = configLocation;
    }

    private static String logLocation;
    private static Properties props;
    private static String configLocation;
    private static File logFile;


    private void init(){
        props = new Properties();
        File configFile = new File(configLocation);
        if (!configFile.exists()){
            try {
                this.createBaseFileStructure();
                createDefaultFileLayout(props, configFile);
            }
            catch (IOException e){
                System.out.println("Could not create or read file: "+configLocation+". Reason:\n" + e.getMessage());
                System.exit(2);
            }
        }
        try {
            props.load(new FileInputStream(configFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logLocation = props.getProperty("logLocation");
        this.printToLog(LogLevel.INFORMATION, "System Startup complete. Awaiting Input");
    }
    private void createBaseFileStructure(){
        File LogFolderFile = new File(logLocation);
        if (!LogFolderFile.exists()){
            LogFolderFile.mkdirs();
        }
    }
    public String getLogLocation(){
        return logLocation;
    }

    public LogLevel getLogLevel(){
        return (LogLevel) props.get("logLevel");
    }

    public static void createDefaultFileLayout(Properties props, File configFile) throws IOException {
        props.setProperty("logLocation", "data/log/");
        props.setProperty("logName", "log.chorus");
        props.setProperty("logLevel", "INFO");
        props.store(new FileWriter(configFile), null);
    }

    public void printToLog(LogLevel level, String msg) {
        if (level.name().equals(this.getLogLevel().name())) {
            logFile = new File(this.getLogLocation());
            String message = getAppropriateEscapeChar(level) + "[" + LocalDate.now() + "    " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "]     \n" + getAppropriateEscapeChar(level) + "[" + level + "] " + msg + "\n";
            System.out.println(message);
            try {
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                Writer writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
                writer.append(message);
                writer.close();
            } catch (IOException e) {
                System.out.println("\033[36mWARNING: CRITICAL: SYSTEM CANNOT PROCEED: COULD NOT CREATE FILE \"log.chorus\"");
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    public static String getAppropriateEscapeChar(LogLevel level){
        switch (level){
            case CRASH -> {
                return "\033[36m";
            }
            case CRITICAL -> {
                return "\033[35m";
            }
            case ERROR -> {
                return "\033[31m";
            }
            case WARNING -> {
                return "\033[33m";
            }
            case INFORMATION -> {
                return "\033[32m";
            }
        }
        return "";
    }

}
