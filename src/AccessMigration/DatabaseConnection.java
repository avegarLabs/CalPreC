package AccessMigration;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static DatabaseConnection instancia = null;
    private static Connection connection;

    private DatabaseConnection() {

    }

    public static DatabaseConnection getInstance() {

        if (instancia == null) {
            instancia = new DatabaseConnection();
        }

        return instancia;
    }

    public static Connection accessConnection(String path) {

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection("jdbc:ucanaccess:" + path, " ", "mnb32575mb54");

            if (connection == null) {
                System.out.println("Error");
            } else {

                return connection;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;

    }


    public static Connection accessConnectionRVDB(String rvPath) {

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection("jdbc:ucanaccess:" + rvPath, " ", "mnb32575mb54");

            if (connection == null) {
                System.out.println("Error");
            } else {

                return connection;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;

    }

}
