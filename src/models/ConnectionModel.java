package models;

import javafx.scene.control.Alert;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.Properties;

public class ConnectionModel {

    public static ConnectionModel instancia = null;
    public static ConfigManager configManager = ConfigManager.getInstance();
    public static SessionFactory sf;
    public static String host;
    public static String datbase;
    public static String userName = "srv.calprec";
    public static String pass = "0PG57ONu8N";
    public static Properties properties;
    public static String port;

    private ConnectionModel() {
    }

    public static SessionFactory createAppConnection() {
        CreateLogFile logFile = new CreateLogFile();
        if (sf == null) {
            try {
                String sDirectory = System.getProperty("user.dir");
                //String path = sDirectory.replace("\\src", "\\resources\\config.properties");
                String path = sDirectory + "\\resources\\config.properties";
                properties = configManager.getConnectionProperties(path);

                File file = new File("config/hibernate.cfg.xml");
                Configuration cfg = new Configuration();
                cfg.configure(file); //hibernate config xml file name

                if (properties.getProperty("calprec.db.type").equals("PostgreSQL")) {
                    cfg.setProperty("hibernate.connection.url", properties.getProperty("calprec.db.url") + ":" + properties.getProperty("calprec.db.port") + "/" + properties.getProperty("calprec.db.database"));
                    cfg.setProperty("hibernate.connection.driver_class", properties.getProperty("calprec.db.driver"));
                    cfg.setProperty("hibernate.dialect", properties.getProperty("calprec.db.dialect"));
                    cfg.setProperty("hibernate.connection.username", userName);
                    cfg.setProperty("hibernate.connection.password", pass);
                    sf = cfg.buildSessionFactory();
                }
                datbase = properties.getProperty("calprec.db.database");
                port = properties.getProperty("calprec.db.port");
                host = properties.getProperty("calprec.db.url");

            } catch (Exception ex) {
                ex.printStackTrace();
                logFile.createLogMessage(ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Error consulte el archivo log en CalPreC/log/calprec.log");
                alert.showAndWait();
            }
        }
        return sf;
    }
}
