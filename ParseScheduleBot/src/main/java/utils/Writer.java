package utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class Writer {
    public static void printStackTrace(Logger logger, Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        logger.severe(stringWriter.toString());
    }
}
