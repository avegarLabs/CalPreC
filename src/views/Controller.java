package views;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import org.controlsfx.dialog.ProgressDialog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    javafx.concurrent.Task tarea;
    @FXML
    private BorderPane mainpane;
    @FXML
    private JFXButton btn_suministros;
    @FXML
    private JFXButton btn_mano;
    @FXML
    private JFXButton btn_equipos;
    @FXML
    private JFXButton btn_config;
    @FXML
    private JFXButton btn_juego;
    @FXML
    private JFXButton btn_semielaborados;
    @FXML
    private JFXButton btn_renglon;
    @FXML
    private JFXButton btn_inversionista;
    @FXML
    private JFXButton btn_entidad;
    @FXML
    private JFXButton btn_empresa;
    @FXML
    private JFXButton btn_brigada;
    @FXML
    private JFXButton btn_grupo;
    @FXML
    private JFXButton btn_cuadrilla;
    @FXML
    private JFXButton btn_ejecucion;
    @FXML
    private Label labelUser;
    private Usuarios usuario;
    @FXML
    private AnchorPane centerPane;
    private Runtime garbache;
    @FXML
    private JFXButton btn_close;
    private UsuariosDAO usuariosDAO;
    @FXML
    private MenuItem restaura;
    @FXML
    private MenuItem importar;
    @FXML
    private MenuItem salva;

    @FXML
    private MenuItem opt1;

    @FXML
    private JFXButton btn_notification;

    private String[] updateData;

    private String stableComp = "2022.1.3";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        usuariosDAO = UsuariosDAO.getInstance();
        if (usuariosDAO.usuario.getGruposId() == 6) {
            restaura.setDisable(true);
            opt1.setDisable(true);
            salva.setDisable(true);
            importar.setDisable(true);

        }


        btn_suministros.setOnAction(event -> {
            Parent proot = null;
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("Suministros.fxml"));

                proot = loader.load();
                SuministrosController controller = (SuministrosController) loader.getController();
                controller.getCheckUser(usuario);
                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                //stage.setResizable(false);
                stage.show();

                // mainpane.setCenter(proot);

            } catch (IOException e) {
                e.printStackTrace();
            }

            garbache = Runtime.getRuntime();
            garbache.gc();


        });

        btn_mano.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Mano de Obra.fxml"));
                proot = loader.load();

                ManoObraController controller = loader.getController();
                controller.checkUser(usuario);


                mainpane.setCenter(proot);

            } catch (IOException e) {
                e.printStackTrace();
            }

            garbache = Runtime.getRuntime();
            garbache.gc();

        });

        btn_equipos.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Equipos.fxml"));
                proot = loader.load();

                EquiposController controller = loader.getController();
                controller.checkUser(usuario);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);
        });

        btn_inversionista.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Inversionistas.fxml"));
                proot = loader.load();

                InversionistaController controller = loader.getController();
                controller.chckUser(usuario);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();
        });

        btn_entidad.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Entidad.fxml"));
                proot = loader.load();

                EntidadController controller = loader.getController();
                controller.checkUser(usuario);


            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);
            garbache = Runtime.getRuntime();
            garbache.gc();
        });

        btn_empresa.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EmpresaConstructora.fxml"));
                proot = loader.load();

                EmpresaConstructoraController controller = loader.getController();
                controller.checkUser(usuario);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();
        });


        btn_brigada.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("BrigadaConstructora.fxml"));
                proot = loader.load();

                BrigadaConstructoraController controller = loader.getController();
                controller.checkUser(usuario);


            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();
        });


        btn_grupo.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GrupodeConstruccion.fxml"));
                proot = loader.load();

                GrupoConstruccionController controller = loader.getController();
                controller.checkUsuario(usuario);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();
        });


        btn_cuadrilla.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CuadrillaConstructora.fxml"));
                proot = loader.load();

                CuadrillaContructoraController controller = loader.getController();
                controller.checkUser(usuario);


            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();
        });

        btn_ejecucion.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GrupoEjecucion.fxml"));
                proot = loader.load();

                GrupoEjecucionController controller = loader.getController();
                controller.checkUsuario(usuario);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();
        });


        btn_juego.setOnAction(event -> {
            Parent proot = null;
            try {
                proot = FXMLLoader.load(getClass().getResource("JuegodeProductos.fxml"));


            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);
        });

        btn_semielaborados.setOnAction(event -> {
            Parent proot = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministrosSemielaborados.fxml"));
                proot = loader.load();

                SuministrosSemielaboradosController controller = loader.getController();
                controller.checkUsuario(usuario);

            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();

        });

        btn_renglon.setOnAction(event -> {
            Parent proot = null;
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonesVariantes.fxml"));
                proot = loader.load();


                RenglonesController controller = loader.getController();
                controller.checkUsuario(usuario);

                // proot = FXMLLoader.load(getClass().getResource("RenglonesVariantes.fxml"));


            } catch (IOException e) {
                e.printStackTrace();
            }
            mainpane.setCenter(proot);

            garbache = Runtime.getRuntime();
            garbache.gc();
        });

        //loadDataJson();

    }

    public void handleNuevoPesupustoObra(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Obras.fxml"));
            Parent proot = loader.load();

            garbache = Runtime.getRuntime();
            garbache.gc();
            mainpane.setCenter(proot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handlePlanObras(ActionEvent event) {
        try {
            AnchorPane plan = FXMLLoader.load(getClass().getResource("PlanObras.fxml"));
            mainpane.setCenter(plan);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void handleCertificacion(ActionEvent event) {
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("CertificacionObras.fxml"));
            mainpane.setCenter(anchorPane);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    /**
     * Presupuesto como renglon variante
     */
    public void handleNuevoPesupustoRenglon(ActionEvent event) {

        try {
            VBox boxObrasRV = FXMLLoader.load(getClass().getResource("ProsupustoRenglonVariante.fxml"));

            mainpane.setCenter(boxObrasRV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void handlePlanRenglonVariante(ActionEvent event) {

        try {
            VBox boxSalarioRV = FXMLLoader.load(getClass().getResource("PlanificarProsupustoRenglonVariante.fxml"));
            mainpane.setCenter(boxSalarioRV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void handleCertificacionRenglonVariante(ActionEvent event) {

        try {
            VBox boxSalarioRV = FXMLLoader.load(getClass().getResource("CertificarProsupustoRenglonVariante.fxml"));
            mainpane.setCenter(boxSalarioRV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void handleControlPresupuesto(ActionEvent event) {

        try {
            AnchorPane control = FXMLLoader.load(getClass().getResource("ControlPresupuesto.fxml"));
            mainpane.setCenter(control);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void handleReportesdeImpresion(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reports/ReportesImpresion.fxml"));
            Parent proot = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void handleReportesdelProject(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MProject/ExportacionProject.fxml"));
            Parent proot = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        garbache = Runtime.getRuntime();
        garbache.gc();
    }

    public void sendUser() {
        usuariosDAO = UsuariosDAO.getInstance();
        usuario = usuariosDAO.usuario;
        labelUser.setText(usuario.getUsuario());

        if (usuario.getGruposId() != 1) {
            btn_config.setVisible(false);
        }
    }

    public void handleConfiguraciones(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Configuracion.fxml"));
            Parent proot = loader.load();

            garbache = Runtime.getRuntime();
            garbache.gc();

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleAjustes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearPlandeMesesAnteriores.fxml"));
            Parent proot = loader.load();

            garbache = Runtime.getRuntime();
            garbache.gc();

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleHojaCalcToMain(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HojadeCalculoToMain.fxml"));
            Parent proot = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        garbache = Runtime.getRuntime();
        garbache.gc();

    }

    public void handleCartaLimiteShow(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CartasLimites.fxml"));
            Parent proot = loader.load();

            CartaLimiteController controller = loader.getController();
            controller.checkUsuario(usuario);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        garbache = Runtime.getRuntime();
        garbache.gc();

    }

    public void handleCloseProgram(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }


    ConfigManager configManager = ConfigManager.getInstance();

    public void handleBackupAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione la ubicación del fichero");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichero (*.backup)", "*.backup");
            fileChooser.getExtensionFilters().add(extFilter);

            String sDirectory = System.getProperty("user.dir");
            //String path = sDirectory.replace("\\src", "\\resources\\config.properties");
            String path = sDirectory + "\\resources\\config.properties";
            Properties properties = configManager.getConnectionProperties(path);


            Runtime r = Runtime.getRuntime();
            //PostgreSQL variables
            String user = "srv.calprec";
            String dbase = properties.getProperty("calprec.db.database");
            String password = "0PG57ONu8N";
            String[] hostPath = properties.getProperty("calprec.db.url").split("//");
            String server = hostPath[1];
            String port = properties.getProperty("calprec.db.port");
            Process p;
            ProcessBuilder pb;

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                /**
                 * Ejecucion del proceso de respaldo
                 */
                File f = new File("Util/runtime/pg_dump.exe");
                r = Runtime.getRuntime();
                pb = new ProcessBuilder(f.getAbsolutePath(), "--host=" + server, "--port=" + port, "-f", file.getAbsolutePath(), "-U", user, "--format=custom", dbase);
                pb.environment().put("PGPASSWORD", password);
                pb.redirectErrorStream(true);
                p = pb.start();
                p.waitFor();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setContentText("Fichero de respaldo creado satisfactoriamente!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Ha ocurrido un error al tratar de crear el fichero de respaldo: " + e.getMessage());

        }

    }

    public javafx.concurrent.Task createTimeMesage() {
        return new javafx.concurrent.Task() {
            @Override
            protected Object call() throws Exception {

                //for (int i = 0; i < ; i++) {
                Thread.sleep(1000);
                //  updateProgress(i + 1, val);
                //}
                return true;
            }
        };
    }


    public void handleRestoreAction(ActionEvent event) {

        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleccione archivo de respaldo");
            File file = fileChooser.showOpenDialog(null);
            Runtime r = Runtime.getRuntime();
            String sDirectory = System.getProperty("user.dir");
            String path = sDirectory.replace("\\src", "\\resources\\config.properties");
            //  String path = sDirectory + "\\resources\\config.properties";
            Properties properties = configManager.getConnectionProperties(path);


            //PostgreSQL variables
            String user = "srv.calprec";
            String dbase = properties.getProperty("calprec.db.database");
            String password = "0PG57ONu8N";
            String[] hostPath = properties.getProperty("calprec.db.url").split("//");
            String server = hostPath[1];
            String port = properties.getProperty("calprec.db.port");
            Process p;
            ProcessBuilder pb;

            if (file != null) {

                /**
                 * Ejecucion del proceso de respaldo
                 */
                tarea = createTimeMesage();
                ProgressDialog dialog = new ProgressDialog(tarea);
                dialog.setContentText("Procesando fichero de respaldo...");
                dialog.setTitle("Espere...");
                new Thread(tarea).start();
                dialog.showAndWait();

                //Realiza la construccion del comando
                r = Runtime.getRuntime();
                ProcessBuilder pbuilder;

                File f = new File("Util/runtime/pg_restore.exe");

                pbuilder = new ProcessBuilder(f.getAbsolutePath(), "--host=" + server, "--port=" + port, "-U", user, "-d", dbase, "-c", file.getAbsolutePath());
                //Se ingresa el valor del password a la variable de entorno de postgres
                pbuilder.environment().put("PGPASSWORD", password);
                pbuilder.redirectErrorStream(true);
                p = pbuilder.start();
                InputStream is = p.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String ll;
                while ((ll = br.readLine()) != null) {
                    System.out.println(ll);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setContentText("Datos restaurados satisfactoriamente!");
                alert.showAndWait();


            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Ha ocurrido un error al tratar de restaturar: " + e.getMessage());
            alert.showAndWait();

        }

    }

    public void handleImportBDAccessFileAction(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccessMigration/ImportMDB.fxml"));
            Parent proot = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        garbache = Runtime.getRuntime();
        garbache.gc();


    }

    public void runCalculadoraAction(ActionEvent event) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("calc");
            p.waitFor();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }


    public void handleIndicadoresView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IndicadoresCalc.fxml"));
            Parent proot = loader.load();


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            //stage.setResizable(false);
            stage.setTitle("Vista para el Cálculo de los Indicadores");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleInformacionView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Información.fxml"));
            Parent proot = loader.load();

            InformacionController controller = (InformacionController) loader.getController();
            controller.pasarDatos(updateData);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            //stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.showAndWait();
            stage.toFront();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ReportProjectStructureSingelton reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private Obra obraToImport;
    private int idZona;
    private List<Nivel> list;

    private static JSONArray filterObjetosArray(int idZona, JSONArray objetosArray) {
        JSONArray tempArray = new JSONArray();
        for (Object o : objetosArray) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("idz").toString()) == idZona) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private static JSONArray filterNivelArray(int idOb, JSONArray objetosArray) {
        JSONArray tempArray = new JSONArray();
        for (Object o : objetosArray) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("ido").toString()) == idOb) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private static JSONArray filterRenglonesArray(int idUo, JSONArray renglonesArray) {
        JSONArray tempArray = new JSONArray();
        for (Object o : renglonesArray) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("uoId").toString()) == idUo) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private static JSONArray filterSuministrosArray(int idUo, JSONArray suministrosArray) {
        JSONArray tempArray = new JSONArray();
        for (Object o : suministrosArray) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("unidadobraId").toString()) == idUo) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private static JSONArray filterPlanesArray(int idUo, JSONArray planes) {
        JSONArray tempArray = new JSONArray();
        for (Object o : planes) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("unidadobraId").toString()) == idUo) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private static JSONArray filterCertificacionesArray(int idUo, JSONArray certificaciones) {
        JSONArray tempArray = new JSONArray();
        for (Object o : certificaciones) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("unidadobraId").toString()) == idUo) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private static JSONArray filterCertificacionesRecArray(int idCertif, JSONArray certificacionesrec) {
        JSONArray tempArray = new JSONArray();
        for (Object o : certificacionesrec) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("certificacionId").toString()) == idCertif) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private static JSONArray filterPlanificacionesRecRecArray(int idCertif, JSONArray planificacionesRec) {
        JSONArray tempArray = new JSONArray();
        for (Object o : planificacionesRec) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("planId").toString()) == idCertif) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    public void handleSaveObra(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SalvaObra.fxml"));
            Parent proot = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleImportObra(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el archivo .json");
        File file = fileChooser.showOpenDialog(null);

        if (file != null && file.getAbsolutePath().endsWith(".json")) {

            JSONParser parser = new JSONParser();
            try {
                FileReader fileReader = new FileReader(file.getAbsolutePath());
                Object object = parser.parse(fileReader);

                JSONObject jsonObject = (JSONObject) object;
                createObraUO(jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Información");
            alert.setContentText("Fichero incorrecto, cargue un fichero .json");
            alert.showAndWait();
        }
    }

    private Obra getObra(int idObra, String code) {
        Obra obra = reportProjectStructureSingelton.getObra(idObra);
        if (obra == null) {
            obra = null;
        } else if (!obra.getCodigo().trim().equals(code.trim())) {
            obra = null;
        }
        return obra;
    }

    private ObservableList<SuministrosView> suministrosViewObservableList;

    private void createObraUO(JSONObject jsonObject) {
        int idObra = 0;
        if (getObra(Integer.parseInt(jsonObject.get("id").toString()), (String) jsonObject.get("code")) == null) {
            Obra newObra = new Obra();
            newObra.setCodigo((String) jsonObject.get("code"));
            newObra.setDescripion((String) jsonObject.get("descrip"));
            newObra.setTipo(jsonObject.get("tipo").toString());
            newObra.setEntidadId(utilCalcSingelton.getEntidad((String) jsonObject.get("entidad")).getId());
            newObra.setInversionistaId(utilCalcSingelton.getInversionista((String) jsonObject.get("inversionista")).getId());
            newObra.setTipoobraId(Integer.parseInt(jsonObject.get("tipoobra").toString()));
            newObra.setSalarioId(Integer.parseInt(jsonObject.get("salario").toString()));
            newObra.setTarifaId(Integer.parseInt(jsonObject.get("tarifa").toString()));
            newObra.setDefcostmat(0);
            idObra = utilCalcSingelton.addNuevaObra(newObra);

            Obra ob1 = reportProjectStructureSingelton.getObra(idObra);

            suministrosViewObservableList = FXCollections.observableArrayList();
            suministrosViewObservableList = utilCalcSingelton.getSuministrosViewObservableList(ob1.getSalarioBySalarioId().getTag());

            JSONArray especialidades = (JSONArray) jsonObject.get("especialidades");
            JSONArray subespecialidades = (JSONArray) jsonObject.get("subespecialidades");

            JSONArray brigadas = (JSONArray) jsonObject.get("brigadas");
            JSONArray grupos = (JSONArray) jsonObject.get("grupos");
            JSONArray cuadrillas = (JSONArray) jsonObject.get("cuadrillas");

            JSONArray suministrosBajo = (JSONArray) jsonObject.get("suministrosBajo");
            JSONArray suministros = (JSONArray) jsonObject.get("suministros");

            createEstructuraEmpresasConstructoras(brigadas, grupos, cuadrillas);

            updateEstructureBaseDatos(especialidades, subespecialidades);

            createListOfSuministrosByObra(suministrosBajo, idObra);

            JSONArray empresaObra = (JSONArray) jsonObject.get("obraEmpresas");
            for (Object o : empresaObra) {
                createEmpresaObra(idObra, (String) o, newObra.getSalarioId());
            }

            JSONArray empresaObraCoeficientes = (JSONArray) jsonObject.get("coeficientes");
            createEmpresaCoeficienteObra(empresaObraCoeficientes, idObra);

            JSONArray zonasArray = (JSONArray) jsonObject.get("zonas");
            JSONArray objetosArray = (JSONArray) jsonObject.get("objetos");
            JSONArray nivelesArray = (JSONArray) jsonObject.get("niveles");
            createEstructuraObra(idObra, zonasArray, objetosArray, nivelesArray);

            obraToImport = reportProjectStructureSingelton.getObra(idObra);

            JSONArray unidadesObra = (JSONArray) jsonObject.get("unidades");
            JSONArray renglones = (JSONArray) jsonObject.get("renglones");
            JSONArray plan = (JSONArray) jsonObject.get("plan");
            JSONArray certificacion = (JSONArray) jsonObject.get("certificacion");
            JSONArray planRec = (JSONArray) jsonObject.get("planRec");
            JSONArray certificacionrec = (JSONArray) jsonObject.get("certificaRec");

            utilCalcSingelton.getAllRenglones();
            utilCalcSingelton.listToSugestionSuministros(Integer.parseInt(jsonObject.get("salario").toString()));

            if (obraToImport.getTipo().trim().equals("UO")) {
                try {
                    createObraDatosCostos(idObra, unidadesObra, renglones, suministros, plan, certificacion, planRec, certificacionrec);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Obra Importada Satisfactoriamente!!");
                    alert.showAndWait();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Información");
                    alert.setContentText("Error al importar la obra");
                    alert.showAndWait();
                }
            } else if (obraToImport.getTipo().trim().equals("RV")) {
                try {
                    createObraDatosCostosRV(idObra, unidadesObra, renglones, suministros, plan, certificacion, planRec, certificacionrec);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Obra Importada Satisfactoriamente!!");
                    alert.showAndWait();
                } catch (Exception exception) {
                    //exception.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Información");
                    alert.setContentText("Error al importar la obra");
                    alert.showAndWait();


                }

            }


        } else {
            System.out.println("ya esta");
        }


    }


    private void createObraDatosCostosRV(int idObra, JSONArray unidadesObra, JSONArray renglones, JSONArray suministros, JSONArray plan, JSONArray certificacion, JSONArray planRec, JSONArray certificacionrec) {
        for (Object o : unidadesObra) {
            JSONObject object = (JSONObject) o;
            Empresaconstructora empresaconstructora = utilCalcSingelton.getEmpresaconstructora(object.get("empresaconstructoraId").toString());
            Nivelespecifico unidadobra = new Nivelespecifico();
            unidadobra = createNewNivelNewObra(object, idObra);
            int idUO = utilCalcSingelton.saveNivelEspecifico(unidadobra);
            Obra obraget = reportProjectStructureSingelton.getObra(idObra);
            List<Empresaobratarifa> empresaobratarifaList = new ArrayList<>();
            empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(idObra, empresaconstructora.getId(), obraget.getTarifaId());
            empresaobratarifaList.size();

            List<Renglonnivelespecifico> listRenglones = new ArrayList<>();
            listRenglones.addAll(createListRenglonesNivel(unidadobra, idUO, filterRenglonesArray(Integer.parseInt(object.get("id").toString()), renglones)));
            utilCalcSingelton.persistNivelRenglones(listRenglones);

            List<Bajoespecificacionrv> listBajoespecificacions = new ArrayList<>();
            listBajoespecificacions.addAll(createListBajoespecificacionrvs(idUO, filterSuministrosArray(Integer.parseInt(object.get("id").toString()), suministros), unidadobra.getObraId()));
            utilCalcSingelton.persistBajoespecificacionRenglon(listBajoespecificacions);

            if (plan != null) {
                createListPlanificacionRev(idUO, filterPlanesArray(Integer.parseInt(object.get("id").toString()), plan), planRec, empresaconstructora, unidadobra.getObraId());
            }
            if (certificacion != null) {
                createListCertificacionRenglon(idUO, filterCertificacionesArray(Integer.parseInt(object.get("id").toString()), certificacion), certificacionrec, empresaconstructora, unidadobra.getObraId());
            }


        }
    }


    private void createListOfSuministrosByObra(JSONArray suministros, int idObra) {
        List<Recursos> recursosList = new ArrayList<>();
        List<Suministrossemielaborados> suministrossemielaboradosList = new ArrayList<>();
        List<Juegoproducto> juegoproductoList = new ArrayList<>();
        for (Object sumini : suministros) {
            JSONObject object = (JSONObject) sumini;
            if (!object.get("pertenece").toString().trim().equals("I") && object.get("tipo").toString().trim().equals("1")) {
                if (createRecursosList(object, idObra) != null) {
                    recursosList.add(createRecursosList(object, idObra));
                }
            } else if (!object.get("pertenece").toString().trim().equals("I") && object.get("tipo").toString().trim().equals("S")) {
                if (createRecursosList(object, idObra) != null) {
                    suministrossemielaboradosList.add(createRecursosSemielList(object, idObra));
                }
            } else if (!object.get("pertenece").toString().trim().equals("I") && object.get("tipo").toString().trim().equals("J")) {
                if (createRecursosList(object, idObra) != null) {
                    juegoproductoList.add(createJuegoProducto(object, idObra));
                }
            }
        }
        utilCalcSingelton.saverecursosList(recursosList);
        utilCalcSingelton.saveSemielaboradosList(suministrossemielaboradosList);
        utilCalcSingelton.savejuegoList(juegoproductoList);
    }

    private Juegoproducto createJuegoProducto(JSONObject object, int idO) {
        Juegoproducto sumuni = new Juegoproducto();
        if (getRecursosByCode(object.get("code").toString().trim()) == null) {
            sumuni.setCodigo(object.get("code").toString().trim());
            sumuni.setDescripcion(object.get("descrip").toString().trim());
            sumuni.setUm(object.get("um").toString().trim());
            sumuni.setPeso(0.0);
            sumuni.setPreciomn(Double.parseDouble(object.get("precio").toString().trim()));
            sumuni.setPreciomlc(0.0);
            sumuni.setMermaprod(0.0);
            sumuni.setMermatrans(0.0);
            sumuni.setMermamanip(0.0);
            sumuni.setOtrasmermas(0.0);
            sumuni.setPertenece(String.valueOf(idO));
            sumuni.setCostomat(0.0);
            sumuni.setCostmano(0.0);
            sumuni.setCostequip(0.0);
            sumuni.setHa(0.0);
            sumuni.setHe(0.0);
            sumuni.setHo(0.0);
            sumuni.setCemento(0.0);
            sumuni.setArido(0.0);
            sumuni.setAsfalto(0.0);
            sumuni.setCarga(0.0);
            sumuni.setPrefab(0.0);
        } else {
            sumuni = null;
        }
        return sumuni;
    }

    private Suministrossemielaborados createRecursosSemielList(JSONObject object, int idO) {
        Suministrossemielaborados sumuni = new Suministrossemielaborados();
        if (getRecursosByCode(object.get("code").toString().trim()) == null) {
            sumuni.setCodigo(object.get("code").toString().trim());
            sumuni.setDescripcion(object.get("descrip").toString().trim());
            sumuni.setUm(object.get("um").toString().trim());
            sumuni.setPeso(0.0);
            sumuni.setPreciomn(Double.parseDouble(object.get("precio").toString().trim()));
            sumuni.setPreciomlc(0.0);
            sumuni.setMermaprod(0.0);
            sumuni.setMermatrans(0.0);
            sumuni.setMermamanip(0.0);
            sumuni.setOtrasmermas(0.0);
            sumuni.setPertenec(String.valueOf(idO));
            sumuni.setCostomat(0.0);
            sumuni.setCostmano(0.0);
            sumuni.setCostequip(0.0);
            sumuni.setHa(0.0);
            sumuni.setHe(0.0);
            sumuni.setHo(0.0);
            sumuni.setCemento(0.0);
            sumuni.setArido(0.0);
            sumuni.setAsfalto(0.0);
            sumuni.setCarga(0.0);
            sumuni.setPrefab(0.0);
        } else {
            sumuni = null;
        }
        return sumuni;
    }

    private SuministrosView getRecursosByCode(String code) {
        return suministrosViewObservableList.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private Recursos createRecursosList(JSONObject object, int idO) {
        Recursos sumuni = new Recursos();
        if (getRecursosByCode(object.get("code").toString().trim()) == null) {
            sumuni.setCodigo(object.get("code").toString().trim());
            sumuni.setDescripcion(object.get("descrip").toString().trim());
            sumuni.setUm(object.get("um").toString().trim());
            sumuni.setPeso(0.0);
            sumuni.setPreciomn(Double.parseDouble(object.get("precio").toString().trim()));
            sumuni.setPreciomlc(0.0);
            sumuni.setMermaprod(0.0);
            sumuni.setMermatrans(0.0);
            sumuni.setMermamanip(0.0);
            sumuni.setOtrasmermas(0.0);
            sumuni.setPertenece(String.valueOf(idO));
            sumuni.setTipo("1");
            sumuni.setGrupoescala("I");
            sumuni.setCostomat(0.0);
            sumuni.setCostmano(0.0);
            sumuni.setCostequip(0.0);
            sumuni.setHa(0.0);
            sumuni.setHe(0.0);
            sumuni.setHo(0.0);
            sumuni.setCemento(0.0);
            sumuni.setArido(0.0);
            sumuni.setAsfalto(0.0);
            sumuni.setCarga(0.0);
            sumuni.setPrefab(0.0);
            sumuni.setCet(0.0);
            sumuni.setCpo(0.0);
            sumuni.setCpe(0.0);
            sumuni.setOtra(0.0);
        } else {
            sumuni = null;
        }
        return sumuni;

    }

    private static Brigadaconstruccion getBrigada(List<Brigadaconstruccion> brigadaconstruccions, String code) {
        return brigadaconstruccions.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private void createEstructuraEmpresasConstructoras(JSONArray brigadas, JSONArray grupos, JSONArray cuadrillas) {
        for (Object brigada : brigadas) {
            JSONObject object = (JSONObject) brigada;
            Empresaconstructora empresaobra = utilCalcSingelton.getEmpresaconstructora(object.get("idE").toString().trim());
            if (getBrigada(empresaobra.getBrigadaconstruccionsById().stream().collect(Collectors.toList()), object.get("code").toString()) == null) {
                int idBri = utilCalcSingelton.createBrigada(object, empresaobra.getId());
                creategrupoList(idBri, filterGrupos(object.get("code").toString(), grupos), cuadrillas);
            } else {
                Brigadaconstruccion brig = getBrigada(empresaobra.getBrigadaconstruccionsById().stream().collect(Collectors.toList()), object.get("code").toString());
                checkGrupos(brig, filterGrupos(object.get("code").toString(), grupos), cuadrillas);
            }
        }
    }

    private void checkGrupos(Brigadaconstruccion brigada, JSONArray gruposList, JSONArray cuadrillas) {
        JSONArray tocreateSub = new JSONArray();
        for (Object o : gruposList) {
            JSONObject object = (JSONObject) o;
            Grupoconstruccion grup = brigada.getGrupoconstruccionsById().parallelStream().filter(item -> item.getCodigo().trim().equals(String.valueOf(object.get("code").toString().trim()))).findFirst().orElse(null);
            if (grup == null) {
                tocreateSub.add(object);
            } else {
                checkCuadrillas(grup, filterCuadrilla(grup.getCodigo(), cuadrillas));
            }
        }
        creategrupoList(brigada.getId(), tocreateSub, cuadrillas);
    }

    private void checkCuadrillas(Grupoconstruccion grup, JSONArray filterCuadrilla) {
        JSONArray tocreateSub = new JSONArray();
        for (Object o : filterCuadrilla) {
            JSONObject object = (JSONObject) o;
            Cuadrillaconstruccion cuadrilla = grup.getCuadrillaconstruccionsById().parallelStream().filter(item -> item.getCodigo().trim().equals(String.valueOf(object.get("code").toString().trim()))).findFirst().orElse(null);
            if (cuadrilla == null) {
                tocreateSub.add(object);
            }
        }
        createCuadrillasList(grup.getId(), tocreateSub);
    }

    private void creategrupoList(int idBri, JSONArray grupoConsts, JSONArray cuadrillas) {
        for (Object o : grupoConsts) {
            JSONObject object = (JSONObject) o;
            Grupoconstruccion grupos = new Grupoconstruccion();
            grupos.setCodigo(String.valueOf(object.get("code").toString()));
            grupos.setDescripcion(String.valueOf(object.get("descrip").toString()));
            grupos.setBrigadaconstruccionId(idBri);
            int idGrupo = utilCalcSingelton.createGrupo(grupos);
            createCuadrillasList(idGrupo, filterCuadrilla(object.get("code").toString(), cuadrillas));
        }
    }

    private void createCuadrillasList(int idGrupo, JSONArray cuadrillasConst) {
        List<Cuadrillaconstruccion> cuadrillaconstruccions = new ArrayList<>();
        for (Object o : cuadrillasConst) {
            JSONObject object = (JSONObject) o;
            Cuadrillaconstruccion sub = new Cuadrillaconstruccion();
            sub.setCodigo(String.valueOf(object.get("code").toString()));
            sub.setDescripcion(String.valueOf(object.get("descrip").toString()));
            sub.setGrupoconstruccionId(idGrupo);
            cuadrillaconstruccions.add(sub);
        }
        utilCalcSingelton.cuadrillaConstruccion(cuadrillaconstruccions);
    }

    private void updateEstructureBaseDatos(JSONArray especialidades, JSONArray subespecialidades) {
        for (Object especialidade : especialidades) {
            JSONObject object = (JSONObject) especialidade;
            Especialidades esp = getEspecialidad(String.valueOf(object.get("code").toString()));
            if (esp == null) {
                int idEsp = utilCalcSingelton.createEspecialidad(object);
                createSubespecialidades(idEsp, filterSubespecialiades(Integer.parseInt(object.get("id").toString()), subespecialidades));
            } else {
                checkSubespecialidedes(esp, filterSubespecialiades(Integer.parseInt(object.get("id").toString()), subespecialidades));
            }
        }
    }

    private void checkSubespecialidedes(Especialidades esp, JSONArray subJsonArray) {
        JSONArray tocreateSub = new JSONArray();
        for (Object o : subJsonArray) {
            JSONObject object = (JSONObject) o;
            Subespecialidades sub = esp.getSubespecialidadesById().parallelStream().filter(item -> item.getCodigo().trim().equals(String.valueOf(object.get("code").toString().trim()))).findFirst().orElse(null);
            if (sub == null) {
                tocreateSub.add(object);
            }
        }
        createSubespecialidades(esp.getId(), tocreateSub);
    }

    private void createSubespecialidades(int idEsp, JSONArray subJsonArray) {
        List<Subespecialidades> subespecialidadesList = new ArrayList<>();
        for (Object o : subJsonArray) {
            JSONObject object = (JSONObject) o;
            Subespecialidades sub = new Subespecialidades();
            sub.setCodigo(String.valueOf(object.get("code").toString()));
            sub.setDescripcion(String.valueOf(object.get("descrip").toString()));
            sub.setEspecialidadesId(idEsp);
            subespecialidadesList.add(sub);
        }
        utilCalcSingelton.sabelistSubespecialidades(subespecialidadesList);
    }

    private JSONArray filterSubespecialiades(int id, JSONArray subespecialidades) {
        JSONArray tempArray = new JSONArray();
        for (Object o : subespecialidades) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(object.get("idE").toString()) == id) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private JSONArray filterGrupos(String code, JSONArray gruposConstrucion) {
        JSONArray tempArray = new JSONArray();
        for (Object o : gruposConstrucion) {
            JSONObject object = (JSONObject) o;
            if (object.get("idE").toString().trim().equals(code.trim())) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private JSONArray filterCuadrilla(String code, JSONArray cuadrillas) {
        JSONArray tempArray = new JSONArray();
        for (Object o : cuadrillas) {
            JSONObject object = (JSONObject) o;
            if (object.get("idE").toString().trim().equals(code.trim())) {
                tempArray.add(object);
            }
        }
        return tempArray;
    }

    private void createObraDatosCostos(int idObra, JSONArray unidadesObra, JSONArray renglones, JSONArray suministros, JSONArray plan, JSONArray certificacion, JSONArray planRec, JSONArray certificacionrec) {
        for (Object o : unidadesObra) {
            JSONObject object = (JSONObject) o;
            if (!object.isEmpty()) {
                Empresaconstructora empresaconstructora = utilCalcSingelton.getEmpresaconstructora(object.get("empresaconstructoraId").toString());
                Unidadobra unidadobra = new Unidadobra();
                unidadobra = createNewUONewObra(object, idObra);
                int idUO = utilCalcSingelton.saveUnidadobra(unidadobra);
                Obra obraget = reportProjectStructureSingelton.getObra(idObra);
                List<Empresaobratarifa> empresaobratarifaList = new ArrayList<>();
                empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(idObra, empresaconstructora.getId(), obraget.getTarifaId());
                empresaobratarifaList.size();
                List<Unidadobrarenglon> listRenglones = new ArrayList<>();
                listRenglones.addAll(createListUnidadRenglones(unidadobra, idUO, filterRenglonesArray(Integer.parseInt(object.get("id").toString()), renglones)));
                utilCalcSingelton.persistUnidadesObraRenglones(listRenglones);

                List<Bajoespecificacion> listBajoespecificacions = new ArrayList<>();
                listBajoespecificacions.addAll(createListBajoEspecificacion(idUO, filterSuministrosArray(Integer.parseInt(object.get("id").toString()), suministros), unidadobra.getObraId()));
                utilCalcSingelton.persistBajoespecificacion(listBajoespecificacions);

                if (plan != null) {
                    createListPlanificacion(idUO, filterPlanesArray(Integer.parseInt(object.get("id").toString()), plan), planRec, empresaconstructora, unidadobra.getObraId());
                }
                if (certificacion != null) {
                    createListCertificacion(idUO, filterCertificacionesArray(Integer.parseInt(object.get("id").toString()), certificacion), certificacionrec, empresaconstructora, unidadobra.getObraId());
                }
            }
        }
    }

    private Brigadaconstruccion getBrigadaCosnt(int idEmpresa, String code) {
        return utilCalcSingelton.getBrigadabyEmpresa(idEmpresa).parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private static Grupoconstruccion getGrupoConst(Brigadaconstruccion brigadaconstruccion, String code) {
        return brigadaconstruccion.getGrupoconstruccionsById().parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private static Cuadrillaconstruccion getCuadrilla(Grupoconstruccion cuadrillaconstruccion, String code) {
        return cuadrillaconstruccion.getCuadrillaconstruccionsById().parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private void createListPlanificacion(int idUO, JSONArray planes, JSONArray planesRec, Empresaconstructora empresaconstructora, int idO) {
        for (Object o : planes) {
            JSONObject object = (JSONObject) o;
            Planificacion plan = new Planificacion();
            plan.setUnidadobraId(idUO);
            Brigadaconstruccion brigadaconstruccion = getBrigadaCosnt(empresaconstructora.getId(), object.get("brigadaconstruccionId").toString());
            plan.setBrigadaconstruccionId(brigadaconstruccion.getId());

            Grupoconstruccion grupoconstruccion = getGrupoConst(brigadaconstruccion, object.get("grupoconstruccionId").toString());
            plan.setGrupoconstruccionId(grupoconstruccion.getId());

            Cuadrillaconstruccion cuadrillaconstruccion = getCuadrilla(grupoconstruccion, object.get("cuadrillaconstruccionId").toString());
            plan.setCuadrillaconstruccionId(cuadrillaconstruccion.getId());

            plan.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            plan.setCostomaterial(Double.parseDouble(object.get("costomaterial").toString()));
            plan.setCostoequipo(Double.parseDouble(object.get("costoequipo").toString()));
            plan.setCostomano(Double.parseDouble(object.get("costomano").toString()));
            plan.setCostosalario(Double.parseDouble(object.get("costosalario").toString()));
            plan.setDesde(Date.valueOf(object.get("desde").toString()));
            plan.setHasta(Date.valueOf(object.get("hasta").toString()));
            plan.setCostsalariocuc(0.0);

            int idPlan = utilCalcSingelton.addNuevoPlan(plan);
            createPlanificacionRec(idUO, idPlan, filterPlanificacionesRecRecArray(Integer.parseInt(object.get("id").toString()), planesRec), idO);
        }
    }

    private void createListPlanificacionRev(int idUO, JSONArray planes, JSONArray planesRec, Empresaconstructora empresaconstructora, int idO) {
        for (Object o : planes) {
            JSONObject object = (JSONObject) o;
            Planrenglonvariante plan = new Planrenglonvariante();
            plan.setNivelespecificoId(idUO);
            plan.setRenglonvarianteId(Integer.parseInt(object.get("idRenglon").toString()));
            Brigadaconstruccion brigadaconstruccion = getBrigadaCosnt(empresaconstructora.getId(), object.get("brigadaconstruccionId").toString());
            plan.setBrigadaconstruccionId(brigadaconstruccion.getId());

            Grupoconstruccion grupoconstruccion = getGrupoConst(brigadaconstruccion, object.get("grupoconstruccionId").toString());
            plan.setGrupoconstruccionId(grupoconstruccion.getId());

            Cuadrillaconstruccion cuadrillaconstruccion = getCuadrilla(grupoconstruccion, object.get("cuadrillaconstruccionId").toString());
            plan.setCuadrillaconstruccionId(cuadrillaconstruccion.getId());

            plan.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            plan.setCostomaterial(Double.parseDouble(object.get("costomaterial").toString()));
            plan.setCostoequipo(Double.parseDouble(object.get("costoequipo").toString()));
            plan.setCostomano(Double.parseDouble(object.get("costomano").toString()));
            plan.setSalario(Double.parseDouble(object.get("costosalario").toString()));
            plan.setDesde(Date.valueOf(object.get("desde").toString()));
            plan.setHasta(Date.valueOf(object.get("hasta").toString()));
            plan.setSalariocuc(0.0);


            int idPlan = utilCalcSingelton.addNuevoPlanRenglon(plan);
            createPlanificacionRecRenglon(idUO, idPlan, filterPlanificacionesRecRecArray(Integer.parseInt(object.get("id").toString()), planesRec), idO);

        }


    }

    private void createPlanificacionRec(int idUO, int idPlan, JSONArray planesrec, int idO) {
        List<Planrecuo> list = new ArrayList<>();
        for (Object o : planesrec) {
            JSONObject object = (JSONObject) o;
            Planrecuo planrecuo = new Planrecuo();
            planrecuo.setUnidadobraId(idUO);
            planrecuo.setPlanId(idPlan);
            planrecuo.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            planrecuo.setCosto(Double.parseDouble(object.get("costo").toString()));
            planrecuo.setFini(Date.valueOf(object.get("fini").toString()));
            planrecuo.setFfin(Date.valueOf(object.get("ffin").toString()));
            planrecuo.setCmateriales(Double.parseDouble(object.get("cmateriales").toString()));
            planrecuo.setCmano(Double.parseDouble(object.get("cmano").toString()));
            planrecuo.setCequipo(Double.parseDouble(object.get("cequipo").toString()));
            planrecuo.setSalario(Double.parseDouble(object.get("salario").toString()));
            planrecuo.setSalariocuc(0.0);
            if (!String.valueOf(object.get("recursoId").toString()).trim().equals("0")) {
                SuministrosView suministrosView = getSuministroToView(object.get("recursoId").toString(), object.get("tipo").toString(), idO);
                planrecuo.setRecursoId(suministrosView.getId());
                planrecuo.setTipo(suministrosView.getTipo());

            } else {
                planrecuo.setRecursoId(0);
                planrecuo.setTipo(String.valueOf(object.get("tipo").toString()));
            }
            if (!String.valueOf(object.get("renglonId").toString()).trim().equals("0")) {
                planrecuo.setRenglonId(Integer.parseInt(object.get("renglonId").toString()));
            }
            list.add(planrecuo);
        }
        utilCalcSingelton.createListPlarec(list);
    }

    private void createPlanificacionRecRenglon(int idUO, int idPlan, JSONArray planesrec, int idO) {
        List<Planrecrv> list = new ArrayList<>();
        for (Object o : planesrec) {
            JSONObject object = (JSONObject) o;
            Planrecrv planrecuo = new Planrecrv();
            planrecuo.setNivelespId(idUO);
            planrecuo.setPlanId(idPlan);
            planrecuo.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            planrecuo.setCosto(Double.parseDouble(object.get("costo").toString()));
            planrecuo.setFini(Date.valueOf(object.get("fini").toString()));
            planrecuo.setFfin(Date.valueOf(object.get("ffin").toString()));
            planrecuo.setCmateriales(Double.parseDouble(object.get("cmateriales").toString()));
            planrecuo.setCmano(Double.parseDouble(object.get("cmano").toString()));
            planrecuo.setCequipo(Double.parseDouble(object.get("cequipo").toString()));
            planrecuo.setSalario(Double.parseDouble(object.get("salario").toString()));
            planrecuo.setSalariocuc(0.0);
            if (!String.valueOf(object.get("recursoId").toString()).trim().equals("0")) {
                SuministrosView suministrosView = getSuministroToView(object.get("recursoId").toString(), object.get("tipo").toString(), idO);
                planrecuo.setRecursoId(suministrosView.getId());
                planrecuo.setTipo(suministrosView.getTipo());

            } else {
                planrecuo.setRecursoId(0);
                planrecuo.setTipo(String.valueOf(object.get("tipo").toString()));
            }
            if (!String.valueOf(object.get("renglonId").toString()).trim().equals("0")) {
                planrecuo.setRenglonId(Integer.parseInt(object.get("renglonId").toString()));
            }
            list.add(planrecuo);
        }
        utilCalcSingelton.createListPlarecRenglon(list);
    }

    private void createListCertificacion(int idUO, JSONArray certificacions, JSONArray certificaionRec, Empresaconstructora empresaconstructora, int idO) {
        for (Object o : certificacions) {
            JSONObject object = (JSONObject) o;
            Certificacion plan = new Certificacion();
            plan.setUnidadobraId(idUO);

            Brigadaconstruccion brigadaconstruccion = getBrigadaCosnt(empresaconstructora.getId(), object.get("brigadaconstruccionId").toString());
            plan.setBrigadaconstruccionId(brigadaconstruccion.getId());

            Grupoconstruccion grupoconstruccion = getGrupoConst(brigadaconstruccion, object.get("grupoconstruccionId").toString());
            plan.setGrupoconstruccionId(grupoconstruccion.getId());

            Cuadrillaconstruccion cuadrillaconstruccion = getCuadrilla(grupoconstruccion, object.get("cuadrillaconstruccionId").toString());
            plan.setCuadrillaconstruccionId(cuadrillaconstruccion.getId());

            plan.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            plan.setCostmaterial(Double.parseDouble(object.get("costomaterial").toString()));
            plan.setCostequipo(Double.parseDouble(object.get("costoequipo").toString()));
            plan.setCostmano(Double.parseDouble(object.get("costomano").toString()));
            plan.setSalario(Double.parseDouble(object.get("costosalario").toString()));
            plan.setDesde(Date.valueOf(object.get("desde").toString()));
            plan.setHasta(Date.valueOf(object.get("hasta").toString()));
            plan.setCucsalario(0.0);

            int idPlan = utilCalcSingelton.addNuevaCertificacion(plan);
            createCertificcaionRec(idUO, idPlan, filterCertificacionesRecArray(Integer.parseInt(object.get("id").toString()), certificaionRec), idO);

        }
    }

    private void createListCertificacionRenglon(int idUO, JSONArray certificacions, JSONArray certificaionRec, Empresaconstructora empresaconstructora, int idO) {
        for (Object o : certificacions) {
            JSONObject object = (JSONObject) o;
            CertificacionRenglonVariante plan = new CertificacionRenglonVariante();
            plan.setNivelespecificoId(idUO);
            plan.setRenglonvarianteId(Integer.parseInt(object.get("idRenglon").toString()));
            Brigadaconstruccion brigadaconstruccion = getBrigadaCosnt(empresaconstructora.getId(), object.get("brigadaconstruccionId").toString());
            plan.setBrigadaconstruccionId(brigadaconstruccion.getId());

            Grupoconstruccion grupoconstruccion = getGrupoConst(brigadaconstruccion, object.get("grupoconstruccionId").toString());
            plan.setGrupoconstruccionId(grupoconstruccion.getId());

            Cuadrillaconstruccion cuadrillaconstruccion = getCuadrilla(grupoconstruccion, object.get("cuadrillaconstruccionId").toString());
            plan.setCuadrillaconstruccionId(cuadrillaconstruccion.getId());

            plan.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            plan.setCostomaterial(Double.parseDouble(object.get("costomaterial").toString()));
            plan.setCostoequipo(Double.parseDouble(object.get("costoequipo").toString()));
            plan.setCostomano(Double.parseDouble(object.get("costomano").toString()));
            plan.setSalario(Double.parseDouble(object.get("costosalario").toString()));
            plan.setDesde(Date.valueOf(object.get("desde").toString()));
            plan.setHasta(Date.valueOf(object.get("hasta").toString()));
            plan.setSalariocuc(0.0);

            int idPlan = utilCalcSingelton.addNuevaCertificacionRenglon(plan);
            createCertificcaionRecRenglon(idUO, idPlan, filterCertificacionesRecArray(Integer.parseInt(object.get("id").toString()), certificaionRec), idO);

        }
    }

    private void createCertificcaionRec(int idUO, int idPlan, JSONArray planesrec, int idO) {
        List<Certificacionrecuo> list = new ArrayList<>();
        for (Object o : planesrec) {
            JSONObject object = (JSONObject) o;
            Certificacionrecuo planrecuo = new Certificacionrecuo();
            planrecuo.setUnidadobraId(idUO);
            planrecuo.setCertificacionId(idPlan);
            planrecuo.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            planrecuo.setCosto(Double.parseDouble(object.get("costo").toString()));
            planrecuo.setFini(Date.valueOf(object.get("fini").toString()));
            planrecuo.setFfin(Date.valueOf(object.get("ffin").toString()));
            planrecuo.setCmateriales(Double.parseDouble(object.get("cmateriales").toString()));
            planrecuo.setCmano(Double.parseDouble(object.get("cmano").toString()));
            planrecuo.setCequipo(Double.parseDouble(object.get("cequipo").toString()));
            planrecuo.setSalario(Double.parseDouble(object.get("salario").toString()));
            planrecuo.setSalariocuc(0.0);
            if (!String.valueOf(object.get("recursoId").toString()).trim().equals("0")) {
                SuministrosView suministrosView = getSuministroToView(object.get("recursoId").toString(), object.get("tipo").toString(), idO);
                if (suministrosView == null) {
                    System.out.println(suministrosView.getCodigo());
                }
                planrecuo.setRecursoId(suministrosView.getId());
                planrecuo.setTipo(suministrosView.getTipo());

            } else {
                planrecuo.setRecursoId(0);
                planrecuo.setTipo(String.valueOf(object.get("tipo").toString()));
            }
            if (!String.valueOf(object.get("renglonId").toString()).trim().equals("0")) {
                planrecuo.setRenglonId(Integer.parseInt(object.get("renglonId").toString()));
            }
            list.add(planrecuo);

        }
        utilCalcSingelton.createListCertific(list);
    }

    private void createCertificcaionRecRenglon(int idUO, int idPlan, JSONArray planesrec, int idO) {
        List<Certificacionrecrv> list = new ArrayList<>();
        for (Object o : planesrec) {
            JSONObject object = (JSONObject) o;
            Certificacionrecrv planrecuo = new Certificacionrecrv();
            planrecuo.setNivelespId(idUO);
            planrecuo.setCertificacionId(idPlan);
            planrecuo.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
            planrecuo.setCosto(Double.parseDouble(object.get("costo").toString()));
            planrecuo.setFini(Date.valueOf(object.get("fini").toString()));
            planrecuo.setFfin(Date.valueOf(object.get("ffin").toString()));
            planrecuo.setCmateriales(Double.parseDouble(object.get("cmateriales").toString()));
            planrecuo.setCmano(Double.parseDouble(object.get("cmano").toString()));
            planrecuo.setCequipo(Double.parseDouble(object.get("cequipo").toString()));
            planrecuo.setSalario(Double.parseDouble(object.get("salario").toString()));
            planrecuo.setSalariocuc(0.0);
            if (!String.valueOf(object.get("recursoId").toString()).trim().equals("0")) {
                SuministrosView suministrosView = getSuministroToView(object.get("recursoId").toString(), object.get("tipo").toString(), idO);
                if (suministrosView == null) {
                    System.out.println(suministrosView.getCodigo());
                }
                planrecuo.setRecursoId(suministrosView.getId());
                planrecuo.setTipo(suministrosView.getTipo());

            } else {
                planrecuo.setRecursoId(0);
                planrecuo.setTipo(String.valueOf(object.get("tipo").toString()));
            }
            if (!String.valueOf(object.get("renglonId").toString()).trim().equals("0")) {
                planrecuo.setRenglonId(Integer.parseInt(object.get("renglonId").toString()));
            }
            list.add(planrecuo);

        }
        utilCalcSingelton.createListCertificaRecRenglon(list);
    }

    private SuministrosView getSuministroToView(String code, String tipo, int idO) {
        SuministrosView suministrosView = null;
        List<SuministrosView> list = new ArrayList<>();
        if (utilCalcSingelton.listSuministros.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim()) && item.getTipo().trim().equals(tipo.trim()) && !item.getPertene().trim().equals("I") && item.getPertene() != null).collect(Collectors.toList()) == null) {
            System.out.println(code);
        } else {
            list = utilCalcSingelton.listSuministros.parallelStream().filter(item -> item.getCodigo().trim().equals(code.trim()) && item.getTipo().trim().equals(tipo.trim()) && !item.getPertene().trim().equals("I")).collect(Collectors.toList());
        }
        if (list.size() > 0) {
            suministrosView = list.get(0);
        }

        return suministrosView;
    }

    private List<Bajoespecificacion> createListBajoEspecificacion(int idUO, JSONArray suminstros, int idO) {
        List<Bajoespecificacion> bajoespecificacionsToRecalc = new ArrayList<>();
        for (Object o : suminstros) {
            JSONObject object = (JSONObject) o;
            SuministrosView suministrosView = getSuministroToView(object.get("idSuministro").toString(), object.get("tipo").toString(), idO);
            if (suministrosView != null) {
                Bajoespecificacion bajo = new Bajoespecificacion();
                double cant = Double.parseDouble(object.get("cantidad").toString());
                bajo.setCantidad(cant);
                bajo.setCosto(cant * suministrosView.getPreciomn());
                bajo.setIdSuministro(suministrosView.getId());
                bajo.setTipo(suministrosView.getTipo());
                bajo.setSumrenglon(0);
                bajo.setUnidadobraId(idUO);
                bajoespecificacionsToRecalc.add(bajo);
            } else {
                System.out.println("NULO: " + object.get("idSuministro").toString());
            }

        }

        return bajoespecificacionsToRecalc;
    }

    private List<Bajoespecificacionrv> createListBajoespecificacionrvs(int idUO, JSONArray suminstros, int idO) {
        List<Bajoespecificacionrv> bajoespecificacionsToRecalc = new ArrayList<>();
        for (Object o : suminstros) {
            JSONObject object = (JSONObject) o;
            SuministrosView suministrosView = getSuministroToView(object.get("idSuministro").toString(), object.get("tipo").toString(), idO);
            if (suministrosView != null) {
                Bajoespecificacionrv bajo = new Bajoespecificacionrv();
                double cant = Double.parseDouble(object.get("cantidad").toString());
                bajo.setCantidad(cant);
                bajo.setRenglonvarianteId(Integer.parseInt(object.get("idRenglon").toString()));
                bajo.setCosto(cant * suministrosView.getPreciomn());
                bajo.setIdsuministro(suministrosView.getId());
                bajo.setTipo(suministrosView.getTipo());
                bajo.setNivelespecificoId(idUO);
                bajoespecificacionsToRecalc.add(bajo);
            } else {
                System.out.println(suministrosView.getCodigo());
            }

        }
        bajoespecificacionsToRecalc.size();
        return bajoespecificacionsToRecalc;
    }

    private Renglonvariante getRenglonvarianteR266(String codeRV, int tag) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglonvariante -> renglonvariante.getCodigo().trim().equals(codeRV.trim()) & renglonvariante.getRs() == tag).findFirst().orElse(null);
    }

    private List<Unidadobrarenglon> createListUnidadRenglones(Unidadobra unidad, int idUO, JSONArray unidadObraRenglon) {
        List<Unidadobrarenglon> unidadobrarengTorecalc = new ArrayList<>();
        for (Object o : unidadObraRenglon) {
            JSONObject object = (JSONObject) o;
            Obra obra = reportProjectStructureSingelton.getObra(unidad.getObraId());
            Renglonvariante renglonvariante = getRenglonvarianteR266((String) object.get("rvId"), obra.getSalarioId());
            if (renglonvariante != null) {
                renglonvariante.getRenglonrecursosById().size();
                renglonvariante.getRenglonsemielaboradosById().size();
                renglonvariante.getRenglonjuegosById().size();
                double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvariante);
                double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMater = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMaterSemi = renglonvariante.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn()).reduce(0.0, Double::sum);
                double costMaterJueg = renglonvariante.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn()).reduce(0.0, Double::sum);
                double cant = Double.parseDouble(object.get("cantRv").toString());
                Unidadobrarenglon newUORV = new Unidadobrarenglon();
                newUORV.setRenglonvarianteId(renglonvariante.getId());
                newUORV.setCantRv(cant);
                newUORV.setCostMano(cant * costoMano);
                newUORV.setCostEquip(cant * costoEq);
                if (String.valueOf(object.get("conMat")).trim().equals("1")) {
                    double mat = costMater + costMaterSemi + costMaterJueg;
                    newUORV.setCostMat(cant * mat);
                    newUORV.setConMat("1");
                } else if (String.valueOf(object.get("conMat")).trim().equals("0")) {
                    newUORV.setCostMat(0.0);
                    newUORV.setConMat("0");
                }
                newUORV.setSalariomn(cant * utilCalcSingelton.calcSalarioRV(renglonvariante));
                newUORV.setSalariocuc(0.0);
                newUORV.setUnidadobraId(idUO);
                unidadobrarengTorecalc.add(newUORV);
            } else {
                System.out.println(object.get("rvId"));
            }
        }

        return unidadobrarengTorecalc;
    }

    private List<Renglonnivelespecifico> createListRenglonesNivel(Nivelespecifico unidad, int idUO, JSONArray unidadObraRenglon) {
        List<Renglonnivelespecifico> unidadobrarengTorecalc = new ArrayList<>();
        for (Object o : unidadObraRenglon) {
            JSONObject object = (JSONObject) o;
            Obra obra = reportProjectStructureSingelton.getObra(unidad.getObraId());
            Renglonvariante renglonvariante = getRenglonvarianteR266((String) object.get("rvId"), obra.getSalarioId());
            renglonvariante.getCodigo();
            renglonvariante.getRenglonrecursosById().size();
            renglonvariante.getRenglonsemielaboradosById().size();
            renglonvariante.getRenglonjuegosById().size();
            double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvariante);
            double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMater = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterSemi = renglonvariante.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterJueg = renglonvariante.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double cant = Double.parseDouble(object.get("cantRv").toString());
            Renglonnivelespecifico newUORV = new Renglonnivelespecifico();
            newUORV.setRenglonvarianteId(renglonvariante.getId());
            newUORV.setCantidad(cant);
            newUORV.setCostmano(cant * costoMano);
            newUORV.setCostequipo(cant * costoEq);
            if (String.valueOf(object.get("conMat")).trim().equals("1")) {
                double mat = costMater + costMaterSemi + costMaterJueg;
                newUORV.setCostmaterial(cant * mat);
                newUORV.setConmat("1");
            } else if (String.valueOf(object.get("conMat")).trim().equals("0")) {
                newUORV.setCostmaterial(0.0);
                newUORV.setConmat("0");
            }
            newUORV.setSalario(cant * utilCalcSingelton.calcSalarioRV(renglonvariante));
            newUORV.setSalariocuc(0.0);
            newUORV.setNivelespecificoId(idUO);
            unidadobrarengTorecalc.add(newUORV);
        }

        return unidadobrarengTorecalc;
    }

    private int getIdZona(String code) {
        return obraToImport.getZonasById().parallelStream().filter(zonas -> zonas.getCodigo().trim().equals(code.trim())).findFirst().map(Zonas::getId).orElse(0);
    }

    private int getObjecto(String code, int idZona) {

        return utilCalcSingelton.getObjetosbyZona(idZona).parallelStream().filter(objetos -> objetos.getCodigo().trim().equals(code) && objetos.getZonasId() == idZona).findFirst().map(Objetos::getId).orElse(0);
    }

    private int getIdNiv(String code, int iOb) {
        return utilCalcSingelton.getNivelbyOb(iOb).parallelStream().filter(nivel -> nivel.getObjetosId() == iOb && nivel.getCodigo().equals(code)).findFirst().map(Nivel::getId).orElse(0);
    }

    private Especialidades getEspecialidad(String code) {
        return utilCalcSingelton.getAllEspecialidades().parallelStream().filter(esp -> esp.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private Unidadobra createNewUONewObra(JSONObject object, int idObra) {
        Unidadobra newUnidad = new Unidadobra();
        newUnidad.setObraId(idObra);
        newUnidad.setEmpresaconstructoraId(utilCalcSingelton.getEmpresaconstructora(object.get("empresaconstructoraId").toString()).getId());
        int idZ = getIdZona((String) object.get("idzona"));
        newUnidad.setZonasId(idZ);
        int idObj = getObjecto((String) object.get("objetosId"), idZ);
        newUnidad.setObjetosId(idObj);
        int idNiv = getIdNiv((String) object.get("nivelId"), idObj);
        newUnidad.setNivelId(idNiv);
        Especialidades especialidades = getEspecialidad(object.get("especialidadesId").toString());
        newUnidad.setEspecialidadesId(especialidades.getId());
        Subespecialidades sub = especialidades.getSubespecialidadesById().parallelStream().filter(item -> item.getCodigo().trim().equals(object.get("subespecialidadesId").toString())).findFirst().orElse(null);
        newUnidad.setSubespecialidadesId(sub.getId());
        newUnidad.setDescripcion((String) object.get("descripcion"));
        newUnidad.setCodigo((String) object.get("codigo"));
        newUnidad.setUm((String) object.get("um"));
        newUnidad.setCantidad(Double.parseDouble(object.get("cantidad").toString()));
        newUnidad.setRenglonbase((String) object.get("renglonbase"));
        newUnidad.setSalariocuc(0.0);
        newUnidad.setIdResolucion(Integer.parseInt(object.get("idResolucion").toString()));
        newUnidad.setGrupoejecucionId(Integer.parseInt(object.get("grupoejecucionId").toString()));
        newUnidad.setCostoMaterial(Double.parseDouble(object.get("costoMaterial").toString()));
        newUnidad.setCostomano(Double.parseDouble(object.get("costomano").toString()));
        newUnidad.setCostoequipo(Double.parseDouble(object.get("costoequipo").toString()));
        newUnidad.setCostototal(Double.parseDouble(object.get("costototal").toString()));
        newUnidad.setCostounitario(Double.parseDouble(object.get("costounitario").toString()));
        newUnidad.setSalario(Double.parseDouble(object.get("salario").toString()));
        return newUnidad;
    }

    private Nivelespecifico createNewNivelNewObra(JSONObject object, int idObra) {
        Nivelespecifico newUnidad = new Nivelespecifico();
        newUnidad.setObraId(idObra);
        newUnidad.setEmpresaconstructoraId(utilCalcSingelton.getEmpresaconstructora(object.get("empresaconstructoraId").toString()).getId());
        int idZ = getIdZona((String) object.get("idzona"));
        newUnidad.setZonasId(idZ);
        int idObj = getObjecto((String) object.get("objetosId"), idZ);
        newUnidad.setObjetosId(idObj);
        int idNiv = getIdNiv((String) object.get("nivelId"), idObj);
        newUnidad.setNivelId(idNiv);
        Especialidades especialidades = getEspecialidad(object.get("especialidadesId").toString());
        newUnidad.setEspecialidadesId(especialidades.getId());
        Subespecialidades sub = especialidades.getSubespecialidadesById().parallelStream().filter(item -> item.getCodigo().trim().equals(object.get("subespecialidadesId").toString())).findFirst().orElse(null);
        newUnidad.setSubespecialidadesId(sub.getId());
        newUnidad.setDescripcion((String) object.get("descripcion"));
        newUnidad.setCodigo((String) object.get("codigo"));
        newUnidad.setCostomaterial(Double.parseDouble(object.get("costoMaterial").toString()));
        newUnidad.setCostomano(Double.parseDouble(object.get("costomano").toString()));
        newUnidad.setCostoequipo(Double.parseDouble(object.get("costoequipo").toString()));
        newUnidad.setSalario(Double.parseDouble(object.get("salario").toString()));
        return newUnidad;
    }

    private void createEstructuraObra(int idObra, JSONArray zonasArray, JSONArray objetosArray, JSONArray nivelesArray) {
        for (Object o : zonasArray) {
            Zonas zonas = new Zonas();
            JSONObject object = (JSONObject) o;
            zonas.setObraId(idObra);
            zonas.setCodigo((String) object.get("code"));
            zonas.setDesripcion((String) object.get("desc"));
            idZona = utilCalcSingelton.createZona(zonas);
            createObjetosByZona(idZona, filterObjetosArray(Integer.parseInt(object.get("id").toString()), objetosArray), nivelesArray);
        }
    }

    private void createObjetosByZona(int idZona, JSONArray jsonArray, JSONArray nivelArray) {
        for (Object o : jsonArray) {
            Objetos objetos = new Objetos();
            JSONObject object = (JSONObject) o;
            objetos.setZonasId(idZona);
            objetos.setCodigo((String) object.get("code"));
            objetos.setDescripcion((String) object.get("desc"));
            int idObj = utilCalcSingelton.createObjetos(objetos);
            createNivelesByObjetos(filterNivelArray(Integer.parseInt(object.get("id").toString()), nivelArray), idObj);

        }

    }

    private void createNivelesByObjetos(JSONArray nivelList, int idObj) {
        list = new ArrayList<>();
        for (Object o : nivelList) {
            Nivel nivel = new Nivel();
            JSONObject object = (JSONObject) o;
            nivel.setObjetosId(idObj);
            nivel.setCodigo((String) object.get("code"));
            nivel.setDescripcion((String) object.get("desc"));
            list.add(nivel);
        }
        utilCalcSingelton.createNiveles(list);
    }


    private void createEmpresaObra(int idObra, String o, int idSalario) {
        List<Empresaobra> empresaobras = new ArrayList<>();
        Empresaobra eo = new Empresaobra();
        eo.setObraId(idObra);
        eo.setEmpresaconstructoraId(utilCalcSingelton.getEmpresaconstructora(o).getId());
        empresaobras.add(eo);
        utilCalcSingelton.caddNuevaObraEmpresa(empresaobras);

        Obra obraGet = reportProjectStructureSingelton.getObra(idObra);
        addNewEmpresaObraTarifa(idObra, utilCalcSingelton.getEmpresaconstructora(o).getId(), obraGet.getTarifaSalarialByTarifa());
        /*
        List<Empresaobrasalario> empresaobrasalarioList = new ArrayList<>();
        empresaobrasalarioList = createEmpresaObraSalarioNewObra(empresaobras, idSalario);
        utilCalcSingelton.createEmpresaObraSalarios(empresaobrasalarioList);
        */


    }

    private void addNewEmpresaObraTarifa(int idObra, int idEmpresa, TarifaSalarial tarifa) {
        List<Empresaobratarifa> empresaobratarifaList = new ArrayList<>();
        for (GruposEscalas gruposEscalas : tarifa.getGruposEscalasCollection()) {
            Empresaobratarifa empresaobratarifa = new Empresaobratarifa();
            empresaobratarifa.setEmpresaconstructoraId(idEmpresa);
            empresaobratarifa.setObraId(idObra);
            empresaobratarifa.setTarifaId(tarifa.getId());
            empresaobratarifa.setGrupo(gruposEscalas.getGrupo());
            empresaobratarifa.setEscala(gruposEscalas.getValorBase());
            empresaobratarifa.setTarifa(gruposEscalas.getValor());
            // empresaobratarifa.setTarifaInc(0.0);
            empresaobratarifaList.add(empresaobratarifa);
        }
        utilCalcSingelton.createEmpresaobratarifa(empresaobratarifaList);
    }

    /*
    private List<Empresaobrasalario> createEmpresaObraSalarioNewObra(List<Empresaobra> empresaobras, int salario) {
        Salario entidad3 = utilCalcSingelton.getSalarios().parallelStream().filter(sal -> sal.getId() == salario).findFirst().get();
        List<Empresaobrasalario> empresaobrasalarios = new ArrayList<>();
        for (Empresaobra empresaobra : empresaobras) {
            Empresaobrasalario empresaobrasalario = new Empresaobrasalario();
            empresaobrasalario.setObraId(empresaobra.getObraId());
            empresaobrasalario.setEmpresaconstructoraId(empresaobra.getEmpresaconstructoraId());
            empresaobrasalario.setSalarioId(entidad3.getId());
            empresaobrasalario.setIi(entidad3.getIi());
            empresaobrasalario.setIii(entidad3.getIii());
            empresaobrasalario.setIv(entidad3.getIv());
            empresaobrasalario.setV(entidad3.getV());
            empresaobrasalario.setVi(entidad3.getVi());
            empresaobrasalario.setVii(entidad3.getVii());
            empresaobrasalario.setViii(entidad3.getViii());
            empresaobrasalario.setIx(entidad3.getIx());
            empresaobrasalario.setX(entidad3.getX());
            empresaobrasalario.setXi(entidad3.getXi());
            empresaobrasalario.setXii(entidad3.getXii());
            empresaobrasalario.setXiii(entidad3.getXiii());
            empresaobrasalario.setXiv(entidad3.getXiv());
            empresaobrasalario.setXv(entidad3.getXv());
            empresaobrasalario.setXvi(entidad3.getXvi());

            empresaobrasalario.setAutoesp(entidad3.getAutesp());
            empresaobrasalario.setAntig(entidad3.getAntiguedad());
            empresaobrasalario.setVacaiones(entidad3.getVacaciones());
            empresaobrasalario.setNomina(entidad3.getNomina());
            empresaobrasalario.setSeguro(entidad3.getSeguro());

            empresaobrasalario.setGtii(entidad3.getGtii());
            empresaobrasalario.setGtiii(entidad3.getGtiii());
            empresaobrasalario.setGtiv(entidad3.getGtiv());
            empresaobrasalario.setGtv(entidad3.getGtv());
            empresaobrasalario.setGtvi(entidad3.getGtvi());
            empresaobrasalario.setGtvii(entidad3.getGtvii());
            empresaobrasalario.setGtviii(entidad3.getGtviii());
            empresaobrasalario.setGtix(entidad3.getGtix());
            empresaobrasalario.setGtx(entidad3.getGtx());
            empresaobrasalario.setGtxi(entidad3.getGtxi());
            empresaobrasalario.setGtxii(entidad3.getGtxii());
            empresaobrasalario.setGtxiii(entidad3.getGtxiii());
            empresaobrasalario.setGtxiv(entidad3.getGtxiv());
            empresaobrasalario.setGtxv(entidad3.getGtxv());
            empresaobrasalario.setGtxvi(entidad3.getGtxvi());
            empresaobrasalarios.add(empresaobrasalario);
        }
        return empresaobrasalarios;
    }
*/
    private void createEmpresaCoeficienteObra(JSONArray coeficientesequiposById, Integer id) {
        List<Coeficientesequipos> coeficientesequipos = new ArrayList<>();
        for (Object o : coeficientesequiposById) {
            Coeficientesequipos coeficienteseq = new Coeficientesequipos();
            JSONObject object = (JSONObject) o;
            coeficienteseq.setObraId(id);
            coeficienteseq.setEmpresaconstructoraId(utilCalcSingelton.getEmpresaconstructora((String) object.get("empresaconstructoraId")).getId());
            coeficienteseq.setRecursosId(Integer.parseInt(object.get("recursosId").toString()));
            coeficienteseq.setCpo(Double.parseDouble(object.get("cpo").toString()));
            coeficienteseq.setCpe(Double.parseDouble(object.get("cpe").toString()));
            coeficienteseq.setCet(Double.parseDouble(object.get("cet").toString()));
            coeficienteseq.setOtra(Double.parseDouble(object.get("otra").toString()));
            coeficienteseq.setSalario(Double.parseDouble(object.get("salario").toString()));
            coeficientesequipos.add(coeficienteseq);
        }
        utilCalcSingelton.caddNuevoCoeficienteEquipo(coeficientesequipos);
    }

    public void handleMantenimientoView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarCatalogo.fxml"));
            Parent proot = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadDataJson() {
        JSONParser parser = new JSONParser();
        try {
            File file = new File("D:\\test\\updates.json");
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            Object object = parser.parse(fileReader);

            JSONObject jsonObject = (JSONObject) object;
            JSONArray versionesList = (JSONArray) jsonObject.get("compilations");
            var lastVersion = versionesList.stream().skip(versionesList.size() - 1)
                    .reduce((first, second) -> second)
                    .orElse(null);
            JSONObject obj = (JSONObject) lastVersion;
            String vers = "Actualización: " + (String) obj.get("version");
            String descrip = (String) obj.get("description");
            String path = (String) obj.get("path");
            if (!vers.equals(stableComp)) {
                updateData = new String[3];
                updateData[0] = vers;
                updateData[1] = descrip;
                updateData[2] = path;
                ActionEvent e = new ActionEvent();
                handleInformacionView(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}






