import com.disruptionsystems.dragonlog.DragonLog;
import com.disruptionsystems.dragonlog.LogLevel;

import java.io.File;

public class main {
    public static void main(String[] args) {
        DragonLog logger = new DragonLog("log", LogLevel.INFORMATION, "dragonlog.chorus");
        logger.printToLog(LogLevel.WARNING, "no");
    }
}
