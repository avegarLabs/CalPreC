package views;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ConnectionModel;
import models.DetalleCertificaView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DetalleCertificarRenglonVarianteRecursoController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private Label labelTitle;
    @FXML
    private TableView<DetalleCertificaView> tablePlanificacionMateriales;
    @FXML
    private TableColumn<DetalleCertificaView, StringProperty> codigoNateriales;
    @FXML
    private TableColumn<DetalleCertificaView, StringProperty> descr;
    @FXML
    private TableColumn<DetalleCertificaView, StringProperty> um;
    @FXML
    private TableColumn<DetalleCertificaView, DoubleProperty> cant;
    @FXML
    private TableColumn<DetalleCertificaView, DoubleProperty> cost;
    @FXML
    private TableColumn<DetalleCertificaView, StringProperty> finic;
    @FXML
    private TableColumn<DetalleCertificaView, StringProperty> ffi;
    private ObservableList<DetalleCertificaView> detalleCertificaViewObservableList;
    private DetalleCertificaView detalleCertificaView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public ObservableList<DetalleCertificaView> getDetalleCertificacionRecursos(Integer niv, Integer reng, Integer rec) {

        detalleCertificaViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Object[]> datosList = session.createQuery(" SELECT rec.id, rec.codigo, rec.descripcion, rec.um , plrv.cantidad, plrv.costo, plrv.fini, plrv.ffin FROM Certificacionrecrv plrv INNER JOIN Recursos rec ON plrv.recursoId = rec.id WHERE plrv.nivelespId =: idNiv AND plrv.renglonId =: idRev AND plrv.recursoId =: idR").setParameter("idNiv", niv).setParameter("idRev", reng).setParameter("idR", rec).getResultList();
            for (Object[] row : datosList) {

                Integer id = Integer.parseInt(row[0].toString());
                String code = row[1].toString();
                String descrip = row[2].toString();
                String um = row[3].toString();
                Double cant = Double.parseDouble(row[4].toString());
                Double costo = Double.parseDouble(row[5].toString());
                String fini = row[6].toString();
                String ffin = row[7].toString();

                detalleCertificaViewObservableList.add(new DetalleCertificaView(id, code, descrip, um, cant, costo, 0.0, fini, ffin));

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


    public void loadData() {
        codigoNateriales.setCellValueFactory(new PropertyValueFactory<DetalleCertificaView, StringProperty>("code"));
        descr.setCellValueFactory(new PropertyValueFactory<DetalleCertificaView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<DetalleCertificaView, StringProperty>("um"));
        cant.setCellValueFactory(new PropertyValueFactory<DetalleCertificaView, DoubleProperty>("cantidad"));
        cost.setCellValueFactory(new PropertyValueFactory<DetalleCertificaView, DoubleProperty>("costo"));
        finic.setCellValueFactory(new PropertyValueFactory<DetalleCertificaView, StringProperty>("fini"));
        ffi.setCellValueFactory(new PropertyValueFactory<DetalleCertificaView, StringProperty>("ffin"));

    }


    public void cargarDatos(Integer Niv, Integer Reng, Integer Rec) {

        labelTitle.setText("Listado de Certificaciones");

        loadData();
        detalleCertificaViewObservableList = FXCollections.observableArrayList();
        detalleCertificaViewObservableList = getDetalleCertificacionRecursos(Niv, Reng, Rec);

        tablePlanificacionMateriales.getItems().setAll(detalleCertificaViewObservableList);


    }
}
