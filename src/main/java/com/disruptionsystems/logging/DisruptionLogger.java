package com.disruptionsystems.logging;

import com.disruptionsystems.DragonLog;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class DisruptionLogger {

    public void printToLog(LogLevel level, String msg) {
        File logFile = new File(DragonLog.getLogLocation());
        String messgae = getAppropriateEscapeChar(level)+"[" + LocalDate.now() + "    " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS) + "]     \n[" + level + "] " + msg + "\n";
        System.out.println(messgae);
        if (!logFile.exists()){
            try {
                logFile.createNewFile();
                Writer writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
                writer.append(messgae);
                writer.close();
            }
            catch (IOException e){
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
