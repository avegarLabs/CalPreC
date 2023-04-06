package views;

import javafx.beans.property.DoubleProperty;
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
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Planificacion;
import models.PlanificacionUnidadObraView;
import models.Unidadobra;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DetallePlanificacionUnidadObra implements Initializable {

    private static SessionFactory sf;
    @FXML
    private Label labelTitle;
    @FXML
    private TableView<PlanificacionUnidadObraView> tablePlanificacion;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> cant;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, StringProperty> desde;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, StringProperty> hasta;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, StringProperty> brigada;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, StringProperty> grupo;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, StringProperty> cuadrilla;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> materiales;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> mano;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> equipo;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> certificado;
    @FXML
    private TableColumn<PlanificacionUnidadObraView, DoubleProperty> pendientes;
    private ObservableList<PlanificacionUnidadObraView> planificacionUnidadObraViewObservableList;
    private PlanificacionUnidadObraView planificacionUnidadObraView;
    private Planificacion planificacion;
    private ArrayList<Planificacion> planificacionArrayList;
    private String code;
    private double cantidadPlan;

    /**
     * Para determinar la cantidad certificad
     */

    public Double getCantCertificada(int idUnidadObra, Date desde, Date hasta) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            cantidadPlan = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacion WHERE unidadobraId =: id AND desde >=: ini AND hasta <=: fin");
            query.setParameter("id", idUnidadObra);
            query.setParameter("ini", desde);
            query.setParameter("fin", hasta);

            if (query.getSingleResult() != null) {
                cantidadPlan = (Double) query.getSingleResult();
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantidadPlan;
    }


    public ObservableList<PlanificacionUnidadObraView> getPlanificacionUnidadObra(String codeUO) {
        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobra WHERE codigo =: code");
            query.setParameter("code", codeUO);

            Unidadobra unidadobra = (Unidadobra) query.getSingleResult();
            cantidadPlan = 0.0;
            if (unidadobra != null) {
                Query query1 = session.createQuery("FROM Planificacion WHERE unidadobraId =: id");
                query1.setParameter("id", unidadobra.getId());
                planificacionArrayList = (ArrayList<Planificacion>) ((org.hibernate.query.Query) query1).list();
                planificacionArrayList.forEach(certifica -> {
                    if (certifica.getCuadrillaconstruccionId() == null) {
                        cantidadPlan = getCantCertificada(unidadobra.getId(), certifica.getDesde(), certifica.getHasta());
                        planificacionUnidadObraView = new PlanificacionUnidadObraView(certifica.getId(), "", "", "", certifica.getCantidad(), certifica.getCostomaterial(), certifica.getCostomano(), certifica.getCostoequipo(), certifica.getCostosalario(), certifica.getCostsalariocuc(), certifica.getDesde().toString(), certifica.getHasta().toString(), cantidadPlan, certifica.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo(), certifica.getGrupoconstruccionByGrupoconstruccionId().getCodigo(), " ", certifica.getCantidad() - cantidadPlan);
                        planificacionUnidadObraViewObservableList.add(planificacionUnidadObraView);
                    } else if (certifica.getCuadrillaconstruccionId() != null) {
                        cantidadPlan = getCantCertificada(unidadobra.getId(), certifica.getDesde(), certifica.getHasta());
                        planificacionUnidadObraView = new PlanificacionUnidadObraView(certifica.getId(), "", "", "", certifica.getCantidad(), certifica.getCostomaterial(), certifica.getCostomano(), certifica.getCostoequipo(), certifica.getCostosalario(), certifica.getCostsalariocuc(), certifica.getDesde().toString(), certifica.getHasta().toString(), cantidadPlan, certifica.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo(), certifica.getGrupoconstruccionByGrupoconstruccionId().getCodigo(), certifica.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo(), certifica.getCantidad() - cantidadPlan);
                        planificacionUnidadObraViewObservableList.add(planificacionUnidadObraView);
                    }
                });
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return planificacionUnidadObraViewObservableList;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void deletePlan(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planificacion = session.get(Planificacion.class, id);

            if (planificacion != null) {
                session.delete(planificacion);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }


    public void loadPlanesData() {
        cant.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("cantidad"));
        desde.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, StringProperty>("desde"));
        hasta.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, StringProperty>("hasta"));
        brigada.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, StringProperty>("idBrigada"));
        grupo.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, StringProperty>("idGrupo"));
        cuadrilla.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, StringProperty>("idCuadrilla"));
        materiales.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("costmaterial"));
        mano.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("costmano"));
        equipo.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("costequipo"));
        pendientes.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("pendientes"));
        certificado.setCellValueFactory(new PropertyValueFactory<PlanificacionUnidadObraView, DoubleProperty>("disponible"));
    }

    public void passData(String codeUo) {
        code = codeUo;

        labelTitle.setText("Planificación UO: " + codeUo);

        loadPlanesData();

        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(codeUo);
        tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);


    }

    public void modificarPlan(ActionEvent event) {
        planificacionUnidadObraView = tablePlanificacion.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarPlan.fxml"));
            Parent proot = loader.load();
            ActualizarPlanController controller = loader.getController();
            controller.cargarUnidadesObra(planificacionUnidadObraView, code);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadPlanesData();

                planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
                planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(code);
                tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void deleteAction() {
        planificacionUnidadObraView = tablePlanificacion.getSelectionModel().getSelectedItem();

        deletePlan(planificacionUnidadObraView.getId());

        loadPlanesData();

        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(code);
        tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);

    }

    public void handleAjustarPlan(ActionEvent event) {
        planificacionUnidadObraView = tablePlanificacion.getSelectionModel().getSelectedItem();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AjustarPlan.fxml"));
            Parent proot = loader.load();
            AjustarPlanController controller = loader.getController();
            controller.cargarUnidadesObra(planificacionUnidadObraView, code, planificacionUnidadObraView.getPendientes());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadPlanesData();

                planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
                planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(code);
                tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


}
