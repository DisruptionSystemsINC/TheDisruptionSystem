import com.disruptionsystems.dragonlog.DragonLog;
import com.disruptionsystems.dragonlog.LogLevel;

public class main {
    public static void main(String[] args) {
        DragonLog logger = new DragonLog("data/config.chorus");
        logger.printToLog(LogLevel.INFORMATION, "no");
    }
}
