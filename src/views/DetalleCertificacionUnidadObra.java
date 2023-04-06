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
import models.Certificacion;
import models.CertificacionUnidadObraView;
import models.ConnectionModel;
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

public class DetalleCertificacionUnidadObra implements Initializable {

    private static SessionFactory sf;
    @FXML
    private Label labelTitle;
    @FXML
    private TableView<CertificacionUnidadObraView> tablePlanificacion;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> cant;
    @FXML
    private TableColumn<CertificacionUnidadObraView, StringProperty> desde;
    @FXML
    private TableColumn<CertificacionUnidadObraView, StringProperty> hasta;
    @FXML
    private TableColumn<CertificacionUnidadObraView, StringProperty> brigada;
    @FXML
    private TableColumn<CertificacionUnidadObraView, StringProperty> grupo;
    @FXML
    private TableColumn<CertificacionUnidadObraView, StringProperty> cuadrilla;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> materiales;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> mano;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> equipo;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> salario;
    @FXML
    private TableColumn<CertificacionUnidadObraView, DoubleProperty> salariocuc;
    private ObservableList<CertificacionUnidadObraView> planificacionUnidadObraViewObservableList;
    private CertificacionUnidadObraView planificacionUnidadObraView;
    private Certificacion planificacion;
    private ArrayList<Certificacion> planificacionArrayList;
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


    public ObservableList<CertificacionUnidadObraView> getPlanificacionUnidadObra(String codeUO) {
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
                Query query1 = session.createQuery("FROM Certificacion WHERE unidadobraId =: id");
                query1.setParameter("id", unidadobra.getId());
                planificacionArrayList = (ArrayList<Certificacion>) ((org.hibernate.query.Query) query1).list();
                planificacionArrayList.forEach(certifica -> {
                    planificacionUnidadObraView = new CertificacionUnidadObraView(certifica.getId(), "", "", "", certifica.getCantidad(), certifica.getCostmaterial(), certifica.getCostmano(), certifica.getCostequipo(), certifica.getSalario(), certifica.getCucsalario(), certifica.getDesde().toString(), certifica.getHasta().toString(), cantidadPlan, certifica.getBrigadaconstruccionByBrigadaconstruccionId().getCodigo(), certifica.getGrupoconstruccionByGrupoconstruccionId().getCodigo(), certifica.getCuadrillaconstruccionByCuadrillaconstruccionId().getCodigo());
                    planificacionUnidadObraViewObservableList.add(planificacionUnidadObraView);
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

            planificacion = session.get(Certificacion.class, id);

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
        cant.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("cantidad"));
        desde.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, StringProperty>("desde"));
        hasta.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, StringProperty>("hasta"));
        brigada.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, StringProperty>("idBrigada"));
        grupo.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, StringProperty>("idGrupo"));
        cuadrilla.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, StringProperty>("idCuadrilla"));
        materiales.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("costmaterial"));
        mano.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("costmano"));
        equipo.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("costequipo"));
        salario.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("salario"));
        salariocuc.setCellValueFactory(new PropertyValueFactory<CertificacionUnidadObraView, DoubleProperty>("cucsalario"));
    }

    public void passData(String codeUo) {
        code = codeUo;

        labelTitle.setText("Certificaciones UO: " + codeUo);

        loadPlanesData();

        planificacionUnidadObraViewObservableList = FXCollections.observableArrayList();
        planificacionUnidadObraViewObservableList = getPlanificacionUnidadObra(codeUo);
        tablePlanificacion.getItems().setAll(planificacionUnidadObraViewObservableList);


    }

    public void modificarPlan(ActionEvent event) {
        planificacionUnidadObraView = tablePlanificacion.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarCertificion.fxml"));
            Parent proot = loader.load();
            ActualizarCertificacionController controller = loader.getController();
            controller.cargarCertificacion(planificacionUnidadObraView.getId(), code);


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


}
