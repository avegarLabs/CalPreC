package views;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Nivelespecifico;
import models.PlanMainViews;
import models.ReportProjectStructureSingelton;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CertificarProsupustoRenglonVarianteController implements Initializable {


    @FXML
    private Label label_title;

    @FXML
    private JFXComboBox<String> comboObras;

    @FXML
    private JFXComboBox<String> comboEmpresas;

    @FXML
    private JFXComboBox<String> comboZonas;

    @FXML
    private JFXComboBox<String> comboObjetos;

    @FXML
    private JFXComboBox<String> comboNivel;

    @FXML
    private JFXComboBox<String> comboEspecialidad;

    @FXML
    private JFXComboBox<String> comboSubespecialidad;

    @FXML
    private TableView<PlanMainViews> tableUnidades;

    @FXML
    private TableColumn<PlanMainViews, StringProperty> codeFirld;

    @FXML
    private TableColumn<PlanMainViews, StringProperty> descripField;

    private ReportProjectStructureSingelton reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();

    private ObservableList<PlanMainViews> planMainViewsObservableList;


    private ObservableList<PlanMainViews> getPlanMainViewsObservableList(int ob, int emp, int zon, int obj, int niv, int esp, int sub) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planMainViewsObservableList = FXCollections.observableArrayList();
            List<Nivelespecifico> unidadobraList = session.createQuery("FROM Nivelespecifico WHERE obraId =: idO and empresaconstructoraId =: idem and zonasId=:idz and objetosId=:idob and nivelId=: nii and especialidadesId=:ides and subespecialidadesId=:sub").setParameter("idO", ob).setParameter("idem", emp).setParameter("idz", zon).setParameter("idob", obj).setParameter("nii", niv).setParameter("ides", esp).setParameter("sub", sub).getResultList();
            planMainViewsObservableList.addAll(unidadobraList.parallelStream().map(unidadobra -> {
                PlanMainViews plan = new PlanMainViews(unidadobra.getId(), unidadobra.getCodigo(), unidadobra.getDescripcion(), " ", 0.0, "0.0", 0.0, 0.0);
                return plan;
            }).collect(Collectors.toList()));


            tx.commit();
            session.close();
            return planMainViewsObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboObras.getItems().setAll(reportProjectStructureSingelton.getObrasListRV());

    }

    public void handleZonasEmpresa(ActionEvent event) {
        int id = reportProjectStructureSingelton.getIdObraByToString(comboObras.getValue().trim().trim());
        if (comboZonas.getItems().size() > 0) {
            comboZonas.getItems().clear();
            comboZonas.getSelectionModel().clearSelection();
        }
        comboZonas.getItems().setAll(reportProjectStructureSingelton.getZonasList(id));

        if (comboEmpresas.getItems().size() > 0) {
            comboEmpresas.getItems().clear();
            comboEmpresas.getSelectionModel().clearSelection();
        }
        comboEmpresas.getItems().setAll(reportProjectStructureSingelton.getEmpresaList(id));

        if (comboObjetos.getItems().size() > 0) {
            comboObjetos.getItems().clear();
            comboObjetos.getSelectionModel().clearSelection();
        }
        if (comboNivel.getItems().size() > 0) {
            comboNivel.getItems().clear();
            comboNivel.getSelectionModel().clearSelection();
        }
        if (comboEspecialidad.getItems().size() > 0) {
            comboEspecialidad.getItems().clear();
            comboEspecialidad.getSelectionModel().clearSelection();
        }
        comboEspecialidad.getItems().setAll(reportProjectStructureSingelton.getEspecialidadesList().parallelStream().filter(st -> !st.equals("Todas")).collect(Collectors.toList()));

        if (comboSubespecialidad.getItems().size() > 0) {
            comboSubespecialidad.getItems().clear();
            comboSubespecialidad.getSelectionModel().clearSelection();
        }
    }

    public void handleObjetos(ActionEvent event) {
        int id = reportProjectStructureSingelton.getIdZonasByToString(comboZonas.getValue().trim());

        if (comboObjetos.getItems().size() > 0) {
            comboObjetos.getItems().clear();
            comboObjetos.getSelectionModel().clearSelection();
        }
        if (comboNivel.getItems().size() > 0) {
            comboNivel.getItems().clear();
            comboNivel.getSelectionModel().clearSelection();
        }
        if (comboEspecialidad.getItems().size() > 0) {

            comboEspecialidad.getSelectionModel().clearSelection();
        }
        if (comboSubespecialidad.getItems().size() > 0) {

            comboSubespecialidad.getSelectionModel().clearSelection();
        }
        comboObjetos.getItems().setAll(reportProjectStructureSingelton.getObjetosList(id).parallelStream().filter(st -> !st.equals("Todos")).collect(Collectors.toList()));

    }

    public void handleNivel(ActionEvent event) {
        int id = reportProjectStructureSingelton.getIdObjetosByToString(comboObjetos.getValue().trim());
        if (comboNivel.getItems().size() > 0) {
            comboNivel.getItems().clear();
            comboNivel.getSelectionModel().clearSelection();
        }
        if (comboEspecialidad.getItems().size() > 0) {
            comboEspecialidad.getSelectionModel().clearSelection();
        }
        if (comboSubespecialidad.getItems().size() > 0) {
            comboSubespecialidad.getSelectionModel().clearSelection();
        }

        comboNivel.getItems().setAll(reportProjectStructureSingelton.getNivelList(id).parallelStream().filter(st -> !st.equals("Todos")).collect(Collectors.toList()));

    }

    public void handleSubespecilaidad(ActionEvent event) {
        int id = reportProjectStructureSingelton.getIdEspecialidasByToString(comboEspecialidad.getValue().trim());
        comboSubespecialidad.getItems().clear();
        comboSubespecialidad.getSelectionModel().clearSelection();
        comboSubespecialidad.getItems().setAll(reportProjectStructureSingelton.getSubespecialidadesObservableList(id).parallelStream().filter(st -> !st.equals("Todas")).collect(Collectors.toList()));
    }


    public void handleCargarUnidades(ActionEvent event) {
        int parOb = reportProjectStructureSingelton.getIdObraByToString(comboObras.getValue().trim().trim());
        int parZon = reportProjectStructureSingelton.getIdZonasByToString(comboZonas.getValue().trim());
        int parEm = reportProjectStructureSingelton.getIdEmpresaByToString(comboEmpresas.getValue().trim());
        int parEsp = reportProjectStructureSingelton.getIdEspecialidasByToString(comboEspecialidad.getValue().trim());
        int parObj = reportProjectStructureSingelton.getIdObjetosByToString(comboObjetos.getValue().trim());
        int parNi = reportProjectStructureSingelton.getIdNivelByToString(comboNivel.getValue().trim());
        int parSub = reportProjectStructureSingelton.getIdSubespecialidadesByToString(comboSubespecialidad.getValue().trim());

        codeFirld.setCellValueFactory(new PropertyValueFactory<PlanMainViews, StringProperty>("code"));
        descripField.setCellValueFactory(new PropertyValueFactory<PlanMainViews, StringProperty>("descrip"));

        if (tableUnidades.getItems().size() > 0) {
            tableUnidades.getItems().clear();
        }

        tableUnidades.getItems().setAll(getPlanMainViewsObservableList(parOb, parEm, parZon, parObj, parNi, parEsp, parSub));

    }

    public void handleNewCertificacion(ActionEvent event) {
        int parOb = reportProjectStructureSingelton.getIdObraByToString(comboObras.getValue().trim().trim());
        int parZon = reportProjectStructureSingelton.getIdZonasByToString(comboZonas.getValue().trim());
        int parEm = reportProjectStructureSingelton.getIdEmpresaByToString(comboEmpresas.getValue().trim());
        int parEsp = reportProjectStructureSingelton.getIdEspecialidasByToString(comboEspecialidad.getValue().trim());
        int parObj = reportProjectStructureSingelton.getIdObjetosByToString(comboObjetos.getValue().trim());
        int parNi = reportProjectStructureSingelton.getIdNivelByToString(comboNivel.getValue().trim());
        int parSub = reportProjectStructureSingelton.getIdSubespecialidadesByToString(comboSubespecialidad.getValue().trim());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CertificarRenglonVariantes.fxml"));
            Parent proot = loader.load();
            CertificarRenglonVarianteController controller = (CertificarRenglonVarianteController) loader.getController();
            controller.cargarUnidadeObra(tableUnidades.getSelectionModel().getSelectedItem().getId(), parEm, parZon, parObj, parNi, parEsp, parSub, parOb);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
