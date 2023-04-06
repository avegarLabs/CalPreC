package models;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    public static ConfigManager instance = null;
    public OutputStream outputStream;
    public InputStream inputStream;
    public Properties properties;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    boolean response = false;

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public boolean writeConfigFile(PgsqlConnection connection, String path) {
        try {
            outputStream = new FileOutputStream(path);
            properties = new Properties();
            properties.setProperty("calprec.db.type", connection.getType());
            properties.setProperty("calprec.db.url", connection.getUrl());
            properties.setProperty("calprec.db.database", connection.getDatabase());
            properties.setProperty("calprec.db.port", connection.getPort());
            properties.setProperty("calprec.db.driver", "org.postgresql.Driver");
            properties.setProperty("calprec.db.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
            properties.store(outputStream, "CalPreC Connections Parameters");
            response = true;

        } catch (IOException e) {
            response = false;
            utilCalcSingelton.showAlert("Error: " + e.getMessage(), 3);
        }
        return response;

    }

    public Properties getConnectionProperties(String path) {
        try {
            inputStream = new FileInputStream(path);
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
