import com.disruptionsystems.logging.DisruptionLogger;
import com.disruptionsystems.logging.LogLevel;

public class TestLogging {
    public static void main(String[] args) {
        new DisruptionLogger("data/logging/log.chorus");
        DisruptionLogger.printToLog(LogLevel.WARNING, "THIS IS A TEST MESSAGE");
    }
}
