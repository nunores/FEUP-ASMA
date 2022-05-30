package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static Logger loggerInstance = null;
    private File log;

    public static Logger getInstance() throws IOException {
        if(loggerInstance == null){
            loggerInstance = new Logger();
        }

        return loggerInstance;
    }

    public Logger() throws IOException {
        Files.deleteIfExists(Paths.get("log.txt"));
        log = new File("log.txt");
    }

    public void writeToLog(String text) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        BufferedWriter logWriter = new BufferedWriter( new FileWriter("log.txt", true));
        logWriter.write("[" + formatter.format(date) + "] " + text + "\n");
        logWriter.close();
    }
}
