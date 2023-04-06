package views;

import com.jfoenix.controls.JFXCheckBox;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DetalleCertificarUnidadOController implements Initializable {

    private static SessionFactory sf;
    private static double globalCertif;
    private static int val;
    public Double cantPresupuestada;
    public Double cantCertificada;
    public Double cantDisponible;
    public Integer idUnidad;
    public Integer idRecurso;
    public String detalle;
    @FXML
    private Label labelTitle;
    @FXML
    private TableView<DetalleCertificaUOView> tablePlanificacionMateriales;
    @FXML
    private TableColumn<DetalleCertificaUOView, JFXCheckBox> codigoNateriales;
    @FXML
    private TableColumn<DetalleCertificaUOView, StringProperty> descr;
    @FXML
    private TableColumn<DetalleCertificaUOView, StringProperty> um;
    @FXML
    private TableColumn<DetalleCertificaUOView, DoubleProperty> cant;
    @FXML
    private TableColumn<DetalleCertificaUOView, DoubleProperty> cost;
    @FXML
    private TableColumn<DetalleCertificaUOView, DoubleProperty> disp;
    @FXML
    private TableColumn<DetalleCertificaUOView, StringProperty> finic;
    @FXML
    private TableColumn<DetalleCertificaUOView, StringProperty> ffi;
    private ObservableList<DetalleCertificaUOView> detalleCertificaViewObservableList;
    private DetalleCertificaUOView detalleCertificaView;
    private JFXCheckBox checkBox;
    private boolean check;
    private Unidadobra unidadobra;

    public static double getCantidadCertificasa(Integer idUO, Integer rec, int idCer) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            globalCertif = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecuo where unidadobraId =: idu AND recursoId =: sumId AND certificacionId <: idC").setParameter("idu", idUO).setParameter("sumId", rec).setParameter("idC", idCer);

            if (query.getResultList().get(0) != null) {
                globalCertif = (Double) query.getResultList().get(0);
            }

            tx.commit();
            session.close();
            return globalCertif;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return globalCertif;
    }

    private static int stadoSum(int idUn, int idRec) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            val = (Integer) session.createQuery(" SELECT sumrenglon FROM Bajoespecificacion WHERE unidadobraId =: idU AND idSuministro =: simId").setParameter("idU", idUn).setParameter("simId", idRec).getSingleResult();

            tx.commit();
            session.close();
            return val;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return val;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public boolean getIsChange(int idUnid, int idSumin) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            check = false;
            Bajoespecificacion bajoespecificacion = (Bajoespecificacion) session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idU AND idSuministro =: idS").setParameter("idU", idUnid).setParameter("idS", idSumin).getSingleResult();
            if (bajoespecificacion.getSumrenglon() == 1) {
                check = true;
            }
            tx.commit();
            session.close();
            return check;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return check;

    }

    public Double getCantidadPresupuestada(Integer idUO, Integer rec) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cantPresupuestada = 0.0;
            Query query = session.createQuery("SELECT cantidad FROM Bajoespecificacion where unidadobraId =: idu AND idSuministro =: sumId").setParameter("idu", idUO).setParameter("sumId", rec);

            if (query.getResultList().get(0) != null) {
                cantPresupuestada = (Double) query.getResultList().get(0);
            }

            tx.commit();
            session.close();
            return cantPresupuestada;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantPresupuestada;
    }

    public Double getCantidadCertificada(Integer idUO, Integer rec, Integer idCert) {


        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cantCertificada = 0.0;
            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacionrecuo where unidadobraId =: idu AND recursoId =: sumId AND certificacionId =: idCert").setParameter("idu", idUO).setParameter("sumId", rec).setParameter("idCert", idCert);

            if (query.getResultList().get(0) != null) {
                cantCertificada = (Double) query.getResultList().get(0);
                System.out.println(cantCertificada);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantCertificada;
    }

    public void loadData() {
        codigoNateriales.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, JFXCheckBox>("code"));
        descr.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, StringProperty>("um"));
        cant.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, DoubleProperty>("cantidad"));
        cost.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, DoubleProperty>("costo"));
        disp.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, DoubleProperty>("disp"));
        finic.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, StringProperty>("fini"));
        ffi.setCellValueFactory(new PropertyValueFactory<DetalleCertificaUOView, StringProperty>("ffin"));
    }

    public ObservableList<DetalleCertificaUOView> getDetalleCertificacionRecursos(Integer niv, Integer uni) {
        detalleCertificaViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cantPresupuestada = 0.0;
            cantCertificada = 0.0;
            cantDisponible = 0.0;
            List<Object[]> listDatos = session.createQuery(" SELECT plrv.recursoId, plrv.tipo, plrv.cantidad, plrv.costo, plrv.fini, plrv.ffin FROM Certificacionrecuo plrv WHERE plrv.certificacionId =: idNiv AND  plrv.unidadobraId =: idU").setParameter("idNiv", niv).setParameter("idU", uni).getResultList();
            for (Object[] row : listDatos) {
                if (row[1].toString().contains("1")) {
                    Recursos recursos = session.get(Recursos.class, Integer.parseInt(row[0].toString()));
                    checkBox = new JFXCheckBox();
                    checkBox.setText(recursos.getCodigo());
                    if (stadoSum(uni, recursos.getId()) != 2) {
                        checkBox.setSelected(true);
                    } else {
                        checkBox.setSelected(false);
                    }

                    double sumCert = getCantidadCertificasa(uni, recursos.getId(), niv);
                    cantPresupuestada = getCantidadPresupuestada(uni, recursos.getId()) - sumCert;
                    cantCertificada = getCantidadCertificada(uni, recursos.getId(), niv);
                    cantDisponible = cantPresupuestada - cantCertificada;

                    detalleCertificaViewObservableList.add(new DetalleCertificaUOView(recursos.getId(), checkBox, recursos.getDescripcion(), recursos.getUm(), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), Math.round(cantDisponible * 10000d) / 10000d, row[4].toString(), row[5].toString()));

                } else if (row[1].toString().contains("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, Integer.parseInt(row[0].toString()));
                    checkBox = new JFXCheckBox();
                    checkBox.setText(recursos.getCodigo());
                    if (stadoSum(uni, recursos.getId()) != 2) {
                        checkBox.setSelected(true);
                    } else {
                        checkBox.setSelected(false);
                    }

                    cantPresupuestada = getCantidadPresupuestada(uni, recursos.getId());
                    cantCertificada = getCantidadCertificada(uni, recursos.getId(), niv);
                    cantDisponible = cantPresupuestada - cantCertificada;

                    detalleCertificaViewObservableList.add(new DetalleCertificaUOView(recursos.getId(), checkBox, recursos.getDescripcion(), recursos.getUm(), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), cantDisponible, row[4].toString(), row[5].toString()));

                } else if (row[1].toString().contains("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, Integer.parseInt(row[0].toString()));
                    checkBox = new JFXCheckBox();
                    checkBox.setText(recursos.getCodigo());
                    if (stadoSum(uni, recursos.getId()) != 2) {
                        checkBox.setSelected(true);
                    } else {
                        checkBox.setSelected(false);
                    }
                    cantPresupuestada = getCantidadPresupuestada(uni, recursos.getId());
                    cantCertificada = getCantidadCertificada(uni, recursos.getId(), niv);
                    cantDisponible = cantPresupuestada - cantCertificada;

                    detalleCertificaViewObservableList.add(new DetalleCertificaUOView(recursos.getId(), checkBox, recursos.getDescripcion(), recursos.getUm(), Double.parseDouble(row[2].toString()), Double.parseDouble(row[3].toString()), cantDisponible, row[4].toString(), row[5].toString()));

                }
            }


            tx.commit();
            session.close();
            return detalleCertificaViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void cargarDatos(Integer Niv, Unidadobra unida) {

        labelTitle.setText("Listado de Recursos Certificados");
        idUnidad = unida.getId();
        unidadobra = unida;

        loadData();
        detalleCertificaViewObservableList = FXCollections.observableArrayList();
        detalleCertificaViewObservableList = getDetalleCertificacionRecursos(Niv, unida.getId());
        tablePlanificacionMateriales.getItems().setAll(detalleCertificaViewObservableList);


    }

    public void handleCambiarSuministro(ActionEvent event) {

        detalleCertificaView = tablePlanificacionMateriales.getSelectionModel().getSelectedItem();

        if (detalleCertificaView.getDisp() == 0.0) {
            //if (detalleCertificaView.getDisp() == 0.0 || detalleCertificaView.getCodigoRV().isSelected() == false) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("El suministro seleccionado no esta disponible");
            alert.showAndWait();

        } else {

            /*
            if (combo_Brigada.getValue() == null || combogrupo.getValue() == null || dateini.getValue() == null || datehasta.getValue() == null) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Información");
                alert.setContentText("Complete los valores de formulario antes de Certificar!");
                alert.showAndWait();

            } else {
*/


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CambioSuministroCertificacionUO.fxml"));
                Parent proot = loader.load();
                CambioSuministroCertificacionUOController controller = loader.getController();
                controller.cargarDatostoChange(unidadobra, 0, detalleCertificaView.getId(), detalleCertificaView.getDisp());


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    private String getDetalleCambio(int nivelespecificoId, int idRec) {


        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            detalle = null;
            Query queryCambios = session.createQuery("SELECT motivo FROM Cambioscertificacionesuo  WHERE unidadId =: idNiv  AND recursoId =: idRec");
            queryCambios.setParameter("idNiv", nivelespecificoId);
            queryCambios.setParameter("idRec", idRec);

            if (!queryCambios.getResultList().isEmpty()) {
                detalle = queryCambios.getResultList().get(0).toString();
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return detalle;
    }

    public void handleShowMotivoCambio(ActionEvent event) {

        detalleCertificaView = tablePlanificacionMateriales.getSelectionModel().getSelectedItem();

        detalle = null;
        detalle = getDetalleCambio(idUnidad, detalleCertificaView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Motivo del Cambio");
        alert.setContentText(detalle);
        alert.showAndWait();
    }
}











