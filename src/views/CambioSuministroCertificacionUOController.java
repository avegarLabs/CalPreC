package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ResourceBundle;

public class CambioSuministroCertificacionUOController implements Initializable {

    private static SessionFactory sf;
    public Integer idObra;
    public Obra obraToChenge;
    CambioSuministroCertificacionUOController cambioSuministroCertificacionController;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXTextField fieldPrecio;
    @FXML
    private javafx.scene.control.TextArea textDedscripcion;
    @FXML
    private Label labelValue;
    @FXML
    private JFXButton btn_exit;
    @FXML
    private JFXTextField cantDispon;
    @FXML
    private JFXTextField cantertificar;
    @FXML
    private JFXTextArea motivo;
    private Integer idNEsp;
    private Integer idRenglon;
    private Integer idRecToChan;
    private Double cantDisponible;
    private Integer idTransaccion;
    private Integer idnewRec;
    private Double precio;
    private ObservableList<RecursosToChangeObserve> recursosToChangeObserveObservableList;
    private ModelsImportViewSingelton modelsImportViewSingelton;
    private Unidadobra uo;
    private String tipo;
    private RecursosToChangeObserve recursosToChangeObserve;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cambioSuministroCertificacionController = this;
    }

    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }

    public Integer saveCambiosMatrial(Bajoespecificacion cambioscertificacionesrv) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        Integer idCambio = null;
        try {
            tx = session.beginTransaction();
            idCambio = (Integer) session.save(cambioscertificacionesrv);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idCambio;
    }

    public void updateSuministroCahnge(int idUnid, int idSumin, double disponible) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Bajoespecificacion bajoespecificacion = (Bajoespecificacion) session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idU AND idSuministro =: idS").setParameter("idU", idUnid).setParameter("idS", idSumin).getSingleResult();
            if (bajoespecificacion != null) {
                double cantReal = bajoespecificacion.getCantidad() - disponible;
                bajoespecificacion.setCantidad(cantReal);
                double costoReal = cantReal * getRecursoToChangeId(bajoespecificacion.getIdSuministro()).getPrecio();
                bajoespecificacion.setCosto(costoReal);
                bajoespecificacion.setSumrenglon(2);
                session.update(bajoespecificacion);
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void cargarDatostoChange(Unidadobra unidadobra, int renVa, int Rec, Double disponible) {
        idNEsp = unidadobra.getId();
        uo = unidadobra;
        obraToChenge = uo.getObraByObraId();
        idRenglon = renVa;
        idRecToChan = Rec;
        cantDisponible = disponible;
        cantDispon.setText(cantDisponible.toString());

        recursosToChangeObserveObservableList = FXCollections.observableArrayList();
        modelsImportViewSingelton = ModelsImportViewSingelton.getInstance();
        recursosToChangeObserveObservableList = modelsImportViewSingelton.listToSugestionSuministros(uo.getObraId());
    }

    private RecursosToChangeObserve getRecursoToChangeCode(String code) {
        return recursosToChangeObserveObservableList.parallelStream().filter(recursosToChangeObserve -> recursosToChangeObserve.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private RecursosToChangeObserve getRecursoToChangeId(int id) {
        return recursosToChangeObserveObservableList.parallelStream().filter(recursosToChangeObserve -> recursosToChangeObserve.getId() == id).findFirst().orElse(null);
    }

    public void handleGetResources(ActionEvent event) {
        tipo = null;
        if (field_codigo.getText() != null) {
            recursosToChangeObserve = getRecursoToChangeCode(field_codigo.getText().trim());
            if (recursosToChangeObserve != null) {
                idnewRec = recursosToChangeObserve.getId();
                precio = recursosToChangeObserve.getPrecio();
                fieldPrecio.setText(String.valueOf(recursosToChangeObserve.getPrecio()));
                labelValue.setText(recursosToChangeObserve.getUm());
                textDedscripcion.setText(recursosToChangeObserve.getDescrip());
                tipo = recursosToChangeObserve.getTipo();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Información");
                alert.setContentText("Suministro no encontrado");
                alert.showAndWait();

                handleBajoAddSum(event);
            }
        }
    }

    public void handleSaveChange(ActionEvent event) {

        if (motivo.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Debe indicar los motivos para el cambio del suministro");
            alert.showAndWait();

        } else {

            Bajoespecificacion bajoespecificacion = new Bajoespecificacion();
            bajoespecificacion.setUnidadobraId(idNEsp);
            bajoespecificacion.setIdSuministro(idnewRec);
            bajoespecificacion.setCantidad(Double.parseDouble(cantDispon.getText()));
            double val = precio * cantDisponible;
            bajoespecificacion.setCosto(val);
            bajoespecificacion.setTipo(tipo);
            bajoespecificacion.setRenglonvarianteId(0);
            bajoespecificacion.setSumrenglon(0);

            idTransaccion = saveCambiosMatrial(bajoespecificacion);
            updateSuministroCahnge(uo.getId(), idRecToChan, cantDisponible);

            Stage stage = (Stage) btn_exit.getScene().getWindow();
            stage.close();
        }
    }

    public void searchView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhSuministroToChangeUO.fxml"));
            Parent proot = loader.load();
            SearchSuminitrosToChangeUOController controller = loader.getController();
            controller.pasController(cambioSuministroCertificacionController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.UTILITY);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void getSuminitroResult(SuministrosView suministrosView) {
        idnewRec = suministrosView.getId();
        precio = suministrosView.getPreciomn();
        fieldPrecio.setText(String.valueOf(suministrosView.getPreciomn()));
        labelValue.setText(suministrosView.getUm());
        textDedscripcion.setText(suministrosView.getDescripcion());
        field_codigo.setText(suministrosView.getCodigo());
        tipo = suministrosView.getTipo();
    }

    public void handleBajoAddSum(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevoSuminitroFromBajoToChange.fxml"));
            Parent proot = loader.load();
            NuevoSuministroFromBajoToChangeController controller = loader.getController();
            //controller.pasarParametros(cambioSuministroCertificacionController, field_codigo.getText());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.UTILITY);
            stage.showAndWait();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
