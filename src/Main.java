import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.ConfigManager;
import models.UtilCalcSingelton;
import org.hibernate.SessionFactory;

import java.util.Locale;
import java.util.Properties;

public class Main extends Application {

    private static SessionFactory sf;
    private String path;
    private Locale locale = Locale.getDefault();
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    public static void main(String[] args) {
        launch(args);
        //UpdateSalaryRV up = new UpdateSalaryRV();
        // up.DuplicateRenglonVariante();

    }

    public void start(Stage primaryStage) throws Exception {
        if (locale.getDisplayCountry().trim().equals("Mexico") || locale.getDisplayCountry().trim().equals("México")) {

            ConfigManager configManager = ConfigManager.getInstance();
            String sDirectory = System.getProperty("user.dir");
            // String path = sDirectory.replace("\\src", "\\resources\\config.properties");
            String path = sDirectory + "\\resources\\config.properties";
            Properties properties = configManager.getConnectionProperties(path);

            if (properties.getProperty("calprec.db.type") != null) {
                Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
                primaryStage.setScene(new Scene(root, 357.0, 264.0));

                primaryStage.initStyle(StageStyle.TRANSPARENT);
                primaryStage.show();

            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("views/DatabaseConfig.fxml"));
                    Parent proot = loader.load();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();
                } catch (Exception ei) {
                    ei.printStackTrace();
                }
            }

        } else {
            String mssage = "Configure Español(México) \n" + " como formato de idioma predeterminado del \n" + " Sistema Operativo";
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Incompatibilidad de Configuración detectada");
            alert.setContentText(mssage);
            alert.showAndWait();

            Platform.exit();
            System.exit(0);
        }
    }

}
