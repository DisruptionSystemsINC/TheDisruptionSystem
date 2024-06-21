package com.disruptionsystems;

import com.disruptionsystems.logging.DisruptionLogger;
import com.disruptionsystems.logging.LogLevel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;

public class DragonLog extends DisruptionLogger{

    public DragonLog(){
        main(new String[]{""});
    }

    public static String logLocation;
    public static void main(String[] args){
        File configFile = new File("config.chorus");
        if (!configFile.exists()){
            try {
                createDefaultFileLayout(configFile);
                createBaseFileStructure();
            }
            catch (IOException e){
                System.out.println("Could not create or read file: config.chorus. Reason:\n" + e.getMessage());
                System.exit(2);
            }
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(configFile);
            logLocation = node.get("logFile").asText();
        }
        catch (IOException e){
            System.out.println("Invalid Config File Structure detected... Aborting Startup");
            System.exit(3);
        }
        DisruptionLogger logger = new DisruptionLogger();
        logger.printToLog(LogLevel.INFORMATION, "System Startup complete. Awaiting Input");
    }
    public static void createBaseFileStructure(){
        File LogFolderFile = new File("data/logs/");
        if (!LogFolderFile.exists()){
            LogFolderFile.mkdirs();
        }
    }
    public static String getLogLocation(){
        return logLocation;
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
