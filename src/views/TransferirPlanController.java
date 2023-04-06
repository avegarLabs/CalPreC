package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TransferirPlanController implements Initializable {

    private static SessionFactory sf;
    public PlanificarUnidaddeObraController planificarUnidaddeObraController;
    public PlanificarUnidaddeObraController controller;
    @FXML
    private JFXComboBox<String> combo_brigada;
    @FXML
    private JFXComboBox<String> combo_grupo;
    @FXML
    private JFXComboBox<String> combo_cuadrilla;
    private List<Brigadaconstruccion> brigadaconstruccionArrayList;
    private List<Grupoconstruccion> grupoconstruccionArrayList;
    private List<Cuadrillaconstruccion> cuadrillaconstruccionList;
    private ObservableList<String> brigadaList;
    private ObservableList<String> grupoList;
    private ObservableList<String> cuaObservableList;
    private Grupoconstruccion grupoconstruccion;
    private Planificacion planificacion;
    @FXML
    private JFXButton btnClose;
    private Brigadaconstruccion brigadaCont;
    private Grupoconstruccion grupo;

    public ObservableList<String> listOfGrupos(int idBrigada) {
        grupoList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Brigadaconstruccion brigadaconstruccion = session.get(Brigadaconstruccion.class, idBrigada);
            grupoconstruccionArrayList = (List<Grupoconstruccion>) brigadaconstruccion.getGrupoconstruccionsById();
            for (Grupoconstruccion grupoconstruccion : grupoconstruccionArrayList) {
                grupoList.add(grupoconstruccion.getCodigo() + " - " + grupoconstruccion.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return grupoList;
    }

    public ObservableList<String> listOfCuadrillas(int idBrigada) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cuaObservableList = FXCollections.observableArrayList();
            Grupoconstruccion brigadaconstruccion = session.get(Grupoconstruccion.class, idBrigada);
            cuadrillaconstruccionList = (List<Cuadrillaconstruccion>) brigadaconstruccion.getCuadrillaconstruccionsById();
            cuaObservableList.addAll(cuadrillaconstruccionList.parallelStream().map(item -> item.getCodigo() + " - " + item.getDescripcion()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return cuaObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return grupoList;
    }

    public void handleLlenaGrupoList(ActionEvent event) {
        String[] codeBriPart = combo_brigada.getValue().split(" - ");

        brigadaCont = brigadaconstruccionArrayList.stream().filter(b -> b.getCodigo().contentEquals(codeBriPart[0])).findFirst().orElse(null);
        grupoList = listOfGrupos(brigadaCont.getId());
        combo_grupo.setItems(grupoList);

    }

    public void handleLlenaCuadrillaList(ActionEvent event) {
        String[] codeGrupo = combo_grupo.getValue().split(" - ");

        grupoconstruccion = grupoconstruccionArrayList.stream().filter(b -> b.getCodigo().contentEquals(codeGrupo[0])).findFirst().orElse(null);
        combo_cuadrilla.setItems(listOfCuadrillas(grupoconstruccion.getId()));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void sentController(Planificacion plan, List<Brigadaconstruccion> brigadaconstruccionList, PlanificarUnidaddeObraController planificar) {
        controller = planificar;
        brigadaconstruccionArrayList = new ArrayList<>();
        brigadaconstruccionArrayList = brigadaconstruccionList;
        brigadaList = FXCollections.observableArrayList();
        brigadaList.addAll(brigadaconstruccionArrayList.parallelStream().map(brigadaconstruccion -> brigadaconstruccion.getCodigo() + " - " + brigadaconstruccion.getDescripcion()).collect(Collectors.toList()));
        combo_brigada.setItems(brigadaList);
        planificacion = plan;

    }


    public void handleClose(ActionEvent event) {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }


    public void handleTransferirAction(ActionEvent event) {
        String[] partCuad = combo_cuadrilla.getValue().split(" - ");
        Cuadrillaconstruccion cuadrillaconstruccion = cuadrillaconstruccionList.parallelStream().filter(grupo -> grupo.getCodigo().equals(partCuad[0])).findFirst().orElse(null);

        planificacion.setBrigadaconstruccionId(brigadaCont.getId());
        planificacion.setGrupoconstruccionId(grupoconstruccion.getId());
        planificacion.setCuadrillaconstruccionId(cuadrillaconstruccion.getId());

        Planificacion certificaionControl = controller.getCertfificacionDatosToCheCk(planificacion);
        if (certificaionControl == null) {
            trandferirPlan(planificacion);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Datos Correctos");
            alert.setContentText("Las operaciones del grupo seleccionados fueron asignados al: " + combo_cuadrilla.getValue());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" Ya existe una certificación con la misma relación de brigada, grupo y cuadrilla en este mes. Escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores ");

            ButtonType buttonAgregar = new ButtonType("Sumar");
            ButtonType buttonSobre = new ButtonType("Sobreescribir");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonAgregar) {
                controller.sumarCertificacion(certificaionControl, planificacion);
            } else if (result.get() == buttonSobre) {
                controller.sobreEscribirCertificacion(certificaionControl, planificacion);

            }
        }
    }

    private void trandferirPlan(Planificacion planificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.update(planificacion);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }
}
