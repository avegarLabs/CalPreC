package models;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CreateLogFile implements WriteInLog {

    Logger logger = Logger.getLogger(CreateLogFile.class.getName());
    private FileHandler fh;
    private String sDirectory;
    private SimpleFormatter simpleFormatter;
    private File file;


    @Override
    public void createLogMessage(String errorMessage) {
/*
        sDirectory = System.getProperty("user.dir");
        String newS = sDirectory.replace("\\src", "\\log\\calprec.log");
        file = new File(newS);
*/

/**
 * Habilitar esto par compilar
 */
        sDirectory = System.getProperty("user.dir");
        file = new File(sDirectory + "\\log\\calprec.log");

        try {
            fh = new FileHandler(file.getAbsolutePath(), true);
            logger.addHandler(fh);
            simpleFormatter = new SimpleFormatter();
            fh.setFormatter(simpleFormatter);
            logger.log(Level.SEVERE, "Información", errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
