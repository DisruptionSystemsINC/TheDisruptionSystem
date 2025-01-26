package com.disruptionsystems.dragonlog;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class DragonLog {

    private String logLocation;
    private Properties props;
    private String configLocation;
    private File logFile;

    public DragonLog(String configLocation){
        this.configLocation = configLocation;
        init();
    }


    private void init(){
        props = new Properties();
        File configFile = new File(configLocation);
        if (!configFile.exists()){
            try {
                createDefaultFileLayout(props, configFile);
                try {
                    props.load(new FileInputStream(configFile));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                logLocation = props.getProperty("logLocation");
                this.createBaseFileStructure();
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
        logFile = new File(logLocation + props.getProperty("logName"));
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

    public String getLogFileName(){
        return logFile.getName();
    }

    public String getLogLevel(){
        return (String) props.get("logLevel");
    }

    public static void createDefaultFileLayout(Properties props, File configFile) throws IOException {
        props.setProperty("logLocation", "data/log/");
        props.setProperty("logName", "log.chorus");
        props.setProperty("logLevel", "INFORMATION");
        props.store(new FileWriter(configFile), null);
    }

    public List<LogLevel> isBelowLevel(String level){
        return switch (level) {
            case "INFORMATION" ->
                    new ArrayList<LogLevel>(List.of(new LogLevel[]{LogLevel.INFORMATION, LogLevel.WARNING, LogLevel.ERROR, LogLevel.CRITICAL, LogLevel.CRASH}));
            case "WARNING" ->
                    new ArrayList<LogLevel>(List.of(new LogLevel[]{LogLevel.WARNING, LogLevel.ERROR, LogLevel.CRITICAL, LogLevel.CRASH}));
            case "ERROR" ->
                    new ArrayList<LogLevel>(List.of(new LogLevel[]{LogLevel.ERROR, LogLevel.CRITICAL, LogLevel.CRASH}));
            case "CRITICAL" ->
                    new ArrayList<LogLevel>(List.of(new LogLevel[]{LogLevel.CRITICAL, LogLevel.CRASH}));
            case "CRASH" ->
                    new ArrayList<LogLevel>(List.of(new LogLevel[]{LogLevel.CRASH}));
            default -> null;
        };
    }

    public void printToLog(LogLevel level, String msg) {
        if (isBelowLevel(getLogLevel()).contains(level)) {
            logFile = new File(this.getLogFileName());
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
