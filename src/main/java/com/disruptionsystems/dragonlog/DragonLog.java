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

    private final String logLocation;
    private File logFile;
    private final LogLevel logLevel;
    private final String logFileName;

    public DragonLog(String logLocation, LogLevel level, String logFileName){
        this.logLocation = logLocation;
        this.logLevel = level;
        this.logFileName = logFileName;
        createBaseFileStructure();
    }

    private void createBaseFileStructure(){
        File LogFolderFile = new File(logLocation);
        LogFolderFile.mkdirs();
        logFile = new File(LogFolderFile, logFileName);
    }
    public String getLogLocation(){
        return logLocation;
    }

    public String getLogFileName(){
        return logFile.getName();
    }

    public List<LogLevel> isBelowLevel(LogLevel level){
        return switch (level) {
            case INFORMATION ->
                    new ArrayList<>(List.of(new LogLevel[]{LogLevel.INFORMATION, LogLevel.WARNING, LogLevel.ERROR, LogLevel.CRITICAL, LogLevel.CRASH}));
            case WARNING ->
                    new ArrayList<>(List.of(new LogLevel[]{LogLevel.WARNING, LogLevel.ERROR, LogLevel.CRITICAL, LogLevel.CRASH}));
            case ERROR ->
                    new ArrayList<>(List.of(new LogLevel[]{LogLevel.ERROR, LogLevel.CRITICAL, LogLevel.CRASH}));
            case CRITICAL ->
                    new ArrayList<>(List.of(new LogLevel[]{LogLevel.CRITICAL, LogLevel.CRASH}));
            case CRASH ->
                    new ArrayList<>(List.of(new LogLevel[]{LogLevel.CRASH}));
            default -> null;
        };
    }

    public void printToLog(LogLevel level, String msg) {
        if (isBelowLevel(logLevel).contains(level)) {
            logFile = new File(this.getLogFileName());
            String message = getAppropriateEscapeChar(level) + "[" + LocalDate.now() + "    " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "]\n" + getAppropriateEscapeChar(level) + "[" + level + "] " + msg + "\n";
            System.out.println(message);
            try {
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
                Writer writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
                writer.append(message);
                writer.close();
            } catch (IOException e) {
                System.out.println("\033[36mWARNING: CRITICAL: SYSTEM CANNOT PROCEED: COULD NOT CREATE FILE");
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
