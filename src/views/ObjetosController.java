package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.Label;
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

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ObjetosController implements Initializable {


    private static SessionFactory sf;
    protected Empresaconstructora empresaconstructora;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXComboBox<String> comboListZonas;
    private ArrayList<Zonas> zonasArrayList;
    private ObservableList<String> listZonas;
    private Zonas zonasModel;
    private int idZona;
    private ArrayList<Objetos> objetosArrayList;
    private ObservableList<ObjetosView> objetosViewObservableList;
    private ObjetosView objetosView;
    private Objetos newObjetos;
    @FXML
    private TableView<ObjetosView> tablaObjetosView;
    @FXML
    private TableColumn<ObjetosView, StringProperty> code;
    @FXML
    private TableColumn<ObjetosView, StringProperty> descripcion;
    private Runtime runtime;
    private Integer inputLength = 2;

    public ObservableList<String> getListZonas(int Id) {


        listZonas = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Zonas where obraId = :id ");
            query.setParameter("id", Id);
            zonasArrayList = (ArrayList<Zonas>) ((org.hibernate.query.Query) query).list();
            zonasArrayList.forEach(zonas -> {
                listZonas.add(zonas.getCodigo() + " - " + zonas.getDesripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listZonas;
    }

    public ObservableList<ObjetosView> getObjetosViewObservableList(int Id) {

        objetosViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            objetosArrayList = (ArrayList<Objetos>) session.createQuery("FROM Objetos where zonasId = :id ").setParameter("id", Id).getResultList();

            for (Objetos objetos : objetosArrayList) {
                objetosView = new ObjetosView(objetos.getId(), objetos.getCodigo(), objetos.getDescripcion());
                objetosViewObservableList.add(objetosView);
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return objetosViewObservableList;
    }

    private boolean checkObjeto(Objetos objetos) {

        return objetosViewObservableList.stream().filter(o -> o.getCodigo().equals(objetos.getCodigo())).findFirst().isPresent();
    }

    public Integer AddObjeto(Objetos objetos) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer objId = null;
        try {
            tx = session.beginTransaction();

            boolean res = checkObjeto(objetos);
            if (res == false) {
                objId = (Integer) session.save(objetos);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código del objeto especificado ya existe!!! ");
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
        return objId;
    }

    private void editObjetos(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            newObjetos = session.get(Objetos.class, id);
            if (newObjetos != null) {
                newObjetos.setCodigo(field_codigo.getText());
                newObjetos.setDescripcion(field_descripcion.getText());
                if (newObjetos.getZonasId() != idZona) {
                    newObjetos.setZonasId(zonasModel.getId());
                }
                session.update(newObjetos);
            }

            tx.commit();
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
            unidadobraList = session.createQuery("FROM Unidadobra WHERE objetosId =: idZ").setParameter("idZ", id).getResultList();
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

    private void deleteNivelEspecifico(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Nivelespecifico WHERE objetosId =: idZ").setParameter("idZ", id).getResultList();
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


    private void deleteObjeto(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            newObjetos = session.get(Objetos.class, id);
            if (newObjetos.getZonasByZonasId().getObraByObraId().getTipo().equals("UO")) {
                deleteUnidadObra(newObjetos.getId());
                int op1 = session.createQuery("DELETE FROM Nivel WHERE objetosId =: idOb").setParameter("idOb", newObjetos.getId()).executeUpdate();
                session.delete(newObjetos);
            } else if (newObjetos.getZonasByZonasId().getObraByObraId().getTipo().equals("RV")) {
                deleteNivelEspecifico(newObjetos.getId());
                int op1 = session.createQuery("DELETE FROM Nivel WHERE objetosId =: idOb").setParameter("idOb", newObjetos.getId()).executeUpdate();
                session.delete(newObjetos);
            }

            tx.commit();
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

    public void dataObjetos() {
        tablaObjetosView.getItems().clear();
        code.setCellValueFactory(new PropertyValueFactory<ObjetosView, StringProperty>("codigo"));
        descripcion.setCellValueFactory(new PropertyValueFactory<ObjetosView, StringProperty>("descripcion"));


        objetosViewObservableList = FXCollections.observableArrayList();
        objetosViewObservableList = getObjetosViewObservableList(idZona);
        tablaObjetosView.getItems().setAll(objetosViewObservableList);

    }

    public void pasarZona(Zonas zona, Empresaconstructora empresa) {
        empresaconstructora = empresa;
        idZona = zona.getId();
        zonasModel = zona;
        comboListZonas.setItems(getListZonas(zona.getObraId()));
        String selectedZona = zona.getCodigo() + " - " + zona.getDesripcion();
        comboListZonas.getSelectionModel().select(selectedZona);
        dataObjetos();

    }

    @FXML
    public void addNewObjetos(ActionEvent event) {

        newObjetos = new Objetos();
        String zonaCode = comboListZonas.getValue().substring(0, 3);

        zonasModel = zonasArrayList.stream().filter(z -> z.getCodigo().contentEquals(zonaCode)).findFirst().orElse(null);

        if (zonasModel.getId() == idZona) {
            newObjetos.setZonasId(idZona);
        } else {
            newObjetos.setZonasId(zonasModel.getId());
        }

        if (btn_add.getText().contentEquals("Aceptar")) {

            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {

                newObjetos.setCodigo(field_codigo.getText());
                newObjetos.setDescripcion(field_descripcion.getText());


                Integer id = AddObjeto(newObjetos);

                if (id != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText("Objeto " + newObjetos.getDescripcion() + " creado satisfactoriamente!");
                    alert.showAndWait();
                }

                runtime = Runtime.getRuntime();
                runtime.gc();

                field_codigo.clear();
                field_descripcion.clear();

                field_codigo.requestFocus();

                dataObjetos();


            } else if (field_codigo.getText().length() < 2 || field_descripcion.getText().length() < 3) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar el Objeto use el patrón: 'Objeto + No', para la descripción 2 digitos");
                alert.showAndWait();
            }

        }
        if (btn_add.getText().contentEquals("Editar")) {

            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {
                editObjetos(objetosView.getId());


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos actualizados!");
                alert.showAndWait();

                runtime = Runtime.getRuntime();
                runtime.gc();


                field_codigo.clear();
                field_descripcion.clear();

                tablaObjetosView.getItems().clear();
                dataObjetos();
            } else if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar el Objeto use el patrón: 'Objeto + No', para la descripción 2 digitos");
                alert.showAndWait();
            }

        }
    }

    public void handleModificar(ActionEvent event) {

        objetosView = tablaObjetosView.getSelectionModel().getSelectedItem();

        field_codigo.setText(objetosView.getCodigo());
        field_descripcion.setText(objetosView.getDescripcion());

        btn_add.setText("Editar");

    }

    public void handleEliminar(ActionEvent event) {

        objetosView = tablaObjetosView.getSelectionModel().getSelectedItem();
        deleteObjeto(objetosView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Zona Eliminada!");
        alert.showAndWait();

        tablaObjetosView.getItems().clear();
        dataObjetos();
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }

    public void importarObjetos(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarObjetos.fxml"));
            Parent proot = loader.load();
            ImportarObjetosController controller = loader.getController();
            controller.pasarDatos(zonasModel.getObraByObraId(), empresaconstructora, zonasModel);


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

    public void duplicarObjetos(ActionEvent event) {

        Objetos objetos = objetosArrayList.parallelStream().filter(objetos1 -> objetos1.getCodigo().trim().equals(tablaObjetosView.getSelectionModel().getSelectedItem().getCodigo().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarObjetos.fxml"));
            Parent proot = loader.load();
            DuplicarObjetosController controller = loader.getController();
            controller.pasarDatos(zonasModel.getObraByObraId(), empresaconstructora, zonasModel, objetos);


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
