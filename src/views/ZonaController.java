package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class ZonaController implements Initializable {


    private static SessionFactory sf;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private JFXButton btn_add;
    private Integer IdObra;

    @FXML
    private TableView<ZonasView> tablezonas;

    @FXML
    private TableColumn<ZonasView, StringProperty> codigo;

    @FXML
    private TableColumn<ZonasView, StringProperty> descripcion;

    private ArrayList<Zonas> zonasArrayList;
    private ObservableList<ZonasView> zonasViewObservableList;
    private ZonasView zonasView;
    private Zonas zonas;

    private Runtime runtime;
    private Obra obraModel;
    private Empresaconstructora empresaconst;
    private Integer inputLength = 3;

    public ObservableList<ZonasView> getListZonasViews(int Id) {


        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            zonasArrayList = (ArrayList<Zonas>) session.createQuery("FROM Zonas where obraId = :id ").setParameter("id", Id).getResultList();

            for (Zonas zonas : zonasArrayList) {
                zonasView = new ZonasView(zonas.getId(), zonas.getCodigo(), zonas.getDesripcion());
                zonasViewObservableList.add(zonasView);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return zonasViewObservableList;
    }

    public boolean checkZonas(Zonas zonas) {
        return zonasViewObservableList.stream().filter(o -> o.getCodigo().equals(zonas.getCodigo())).findFirst().isPresent();

    }

    private Integer AddZona(Zonas zonas) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer zonaId = null;
        try {
            tx = session.beginTransaction();

            boolean res = checkZonas(zonas);

            if (res == false) {
                zonaId = (Integer) session.save(zonas);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código de la zona especificada ya existe!!! ");
                alert.showAndWait();
            }


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return zonaId;
    }

    private void editZona(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            zonas = session.get(Zonas.class, id);
            if (zonas != null) {
                zonas.setCodigo(field_codigo.getText());
                zonas.setDesripcion(field_descripcion.getText());
                session.update(zonas);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void deleteZona(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            zonas = session.get(Zonas.class, id);
            if (zonas.getObraByObraId().getTipo().equals("UO")) {
                deleteUnidadObra(zonas.getId());
                deleteObjetos(zonas.getObjetosById());
                session.delete(zonas);
            } else if (zonas.getObraByObraId().getTipo().equals("RV")) {
                deleteNivelEspecifico(zonas.getId());
                deleteObjetos(zonas.getObjetosById());
                session.delete(zonas);
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

    private void deleteObjetos(Collection<Objetos> objetosList) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Objetos objetos : objetosList) {
                int op1 = session.createQuery("DELETE FROM Nivel WHERE objetosId =: id").setParameter("id", objetos.getId()).executeUpdate();
                int op2 = session.createQuery("DELETE FROM Objetos  WHERE id =: idO").setParameter("idO", objetos.getId()).executeUpdate();
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

    private void deleteNivelEspecifico(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Nivelespecifico WHERE zonasId =: idZ").setParameter("idZ", id).getResultList();
            if (unidadobraList.size() > 0) {
                for (Nivelespecifico unid : unidadobraList) {
                    int op1 = session.createQuery("DELETE FROM Renglonnivelespecifico WHERE nivelespecificoId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op2 = session.createQuery("DELETE FROM Bajoespecificacionrv WHERE nivelespecificoId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op3 = session.createQuery("DELETE FROM Planrenglonvariante WHERE nivelespecificoId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op4 = session.createQuery("DELETE FROM CertificacionRenglonVariante WHERE nivelespecificoId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op5 = session.createQuery("DELETE FROM Memoriadescriptivarv WHERE nivelespecificoId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op6 = session.createQuery("DELETE FROM Certificacionrecrv WHERE nivelespId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op7 = session.createQuery("DELETE FROM Planrecrv WHERE nivelespId =: idU").setParameter("idU", unid.getId()).executeUpdate();

                    session.delete(unid);
                }
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

    private void deleteUnidadObra(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Unidadobra> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Unidadobra WHERE zonasId =: idZ").setParameter("idZ", id).getResultList();
            if (unidadobraList.size() > 0) {
                for (Unidadobra unid : unidadobraList) {
                    int op1 = session.createQuery("DELETE FROM Unidadobrarenglon WHERE unidadobraId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op2 = session.createQuery("DELETE FROM Bajoespecificacion WHERE unidadobraId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op3 = session.createQuery("DELETE FROM Planificacion WHERE unidadobraId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op4 = session.createQuery("DELETE FROM Certificacion WHERE unidadobraId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op5 = session.createQuery("DELETE FROM Memoriadescriptiva WHERE unidadobraId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op6 = session.createQuery("DELETE FROM Certificacionrecuo WHERE unidadobraId =: idU").setParameter("idU", unid.getId()).executeUpdate();
                    int op7 = session.createQuery("DELETE FROM Planrecuo WHERE unidadobraId =: idU").setParameter("idU", unid.getId()).executeUpdate();

                    session.delete(unid);

                }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void addNewZona(ActionEvent event) {

        if (btn_add.getText().contentEquals("Aceptar")) {


            if (field_codigo.getText().length() < 3 || field_descripcion.getText().length() < 3) {


                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar la Zona use el patrón: 'Zona + No', para la descripción y 3 digitos para el código de la zona");
                alert.showAndWait();


            } else if (field_codigo.getText().length() == 3 || field_descripcion.getText().length() > 3) {

                Zonas zonas = new Zonas();
                zonas.setCodigo(field_codigo.getText());
                zonas.setDesripcion(field_descripcion.getText());
                zonas.setObraId(IdObra);


                Integer id = AddZona(zonas);

                if (id != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText("Zona " + zonas.getDesripcion() + " creada satisfactoriamente!");
                    alert.showAndWait();


                }
                field_codigo.clear();
                field_descripcion.clear();
                field_codigo.requestFocus();

                datosZonas();

            }
        }
        if (btn_add.getText().contentEquals("Editar")) {

            if (field_codigo.getText().length() < 3 || field_descripcion.getText().length() < 3) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar la Zona use el patrón: 'Zona + No', para la descripción y 3 digitos para el código de la zona");
                alert.showAndWait();


            } else if (field_codigo.getText().length() == 3 || field_descripcion.getText().length() > 3) {
                editZona(zonasView.getId());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos actualizados");
                alert.showAndWait();

                runtime = Runtime.getRuntime();
                runtime.gc();

                tablezonas.getItems().clear();
                field_codigo.clear();
                field_descripcion.clear();

                datosZonas();
            }
        }

    }

    public void datosZonas() {

        tablezonas.getItems().clear();
        codigo.setCellValueFactory(new PropertyValueFactory<ZonasView, StringProperty>("codigo"));
        descripcion.setCellValueFactory(new PropertyValueFactory<ZonasView, StringProperty>("desripcion"));

        zonasViewObservableList = FXCollections.observableArrayList();
        zonasViewObservableList = getListZonasViews(IdObra);
        tablezonas.getItems().addAll(zonasViewObservableList);
    }

    public void pasarZona(Obra obra, Empresaconstructora empresaconstructora) {
        IdObra = obra.getId();
        datosZonas();

        obraModel = obra;
        empresaconst = empresaconstructora;
    }

    public void handleModificar(ActionEvent event) {

        zonasView = tablezonas.getSelectionModel().getSelectedItem();

        field_codigo.setText(zonasView.getCodigo());
        field_descripcion.setText(zonasView.getDesripcion());

        btn_add.setText("Editar");
    }

    public void handleEliminar(ActionEvent event) {

        zonasView = tablezonas.getSelectionModel().getSelectedItem();

        deleteZona(zonasView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Zona Eliminada!");
        alert.showAndWait();

        tablezonas.getItems().clear();
        datosZonas();
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }

    public void importarZonas(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarZona.fxml"));
            Parent proot = loader.load();
            ImportarZonaController controller = loader.getController();
            controller.pasarDatos(obraModel, empresaconst);


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

    public void duplicarZonas(ActionEvent event) {

        Zonas zonas = zonasArrayList.parallelStream().filter(zon -> zon.getCodigo().trim().endsWith(tablezonas.getSelectionModel().getSelectedItem().getCodigo())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarZona.fxml"));
            Parent proot = loader.load();
            DuplicarZonaController controller = loader.getController();
            controller.pasarDatos(obraModel, empresaconst, zonas);


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
