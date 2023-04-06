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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NivelesController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXComboBox<String> comboListObjetos;
    private ArrayList<Objetos> objetosArrayList;
    private ObservableList<String> listObjetos;
    private Objetos objetoModel;
    private int idObjeto;
    @FXML
    private TableView<NivelView> tableNivel;
    @FXML
    private TableColumn<NivelView, StringProperty> codeNivel;
    @FXML
    private TableColumn<NivelView, StringProperty> descripNivel;
    private ArrayList<Nivel> nivelArrayList;
    private ObservableList<NivelView> nivelViewObservableList;
    private NivelView nivelView;
    private Nivel newNivel;
    private Runtime runtime;
    private Empresaconstructora empresa;
    private Integer inputLength = 2;

    public ObservableList<String> getListObjetos(int Id) {


        listObjetos = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            objetosArrayList = (ArrayList<Objetos>) session.createQuery("FROM Objetos where  zonasId = :id ").setParameter("id", Id).getResultList();
            for (Objetos objetos : objetosArrayList) {
                listObjetos.add(objetos.getCodigo() + " - " + objetos.getDescripcion());
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return listObjetos;
    }

    public boolean checkNiveles(Nivel nivel) {
        return nivelViewObservableList.stream().filter(o -> o.getCodigo().equals(nivel.getCodigo())).findFirst().isPresent();

    }

    public Integer AddNivel(Nivel nivel) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer nivId = null;

        try {
            tx = session.beginTransaction();

            boolean res = checkNiveles(nivel);

            if (res == false) {
                nivId = (Integer) session.save(nivel);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código del nivel especificado ya existe!!! ");
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
        return nivId;
    }

    public ObservableList<NivelView> getNivelViewObservableList(int Id) {

        nivelViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            nivelArrayList = (ArrayList<Nivel>) session.createQuery("FROM Nivel where objetosId = :id ").setParameter("id", Id).getResultList();
            for (Nivel nivel : nivelArrayList) {
                nivelView = new NivelView(nivel.getId(), nivel.getCodigo(), nivel.getDescripcion());
                nivelViewObservableList.add(nivelView);
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return nivelViewObservableList;
    }

    private void editNivel(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            newNivel = session.get(Nivel.class, id);
            if (newNivel != null) {
                newNivel.setCodigo(field_codigo.getText());
                newNivel.setDescripcion(field_descripcion.getText());
                if (newNivel.getObjetosId() != idObjeto) {
                    newNivel.setObjetosId(objetoModel.getId());
                }
                session.update(newNivel);
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
            unidadobraList = session.createQuery("FROM Unidadobra WHERE nivelId =: idZ").setParameter("idZ", id).getResultList();
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
            unidadobraList = session.createQuery("FROM Nivelespecifico WHERE nivelId =: idZ").setParameter("idZ", id).getResultList();
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


    private void deleteNivel(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            newNivel = session.get(Nivel.class, id);

            if (newNivel.getObjetosByObjetosId().getZonasByZonasId().getObraByObraId().getTipo().equals("UO")) {
                deleteUnidadObra(newNivel.getId());
                session.delete(newNivel);
            } else if (newNivel.getObjetosByObjetosId().getZonasByZonasId().getObraByObraId().getTipo().equals("RV")) {
                deleteNivelEspecifico(newNivel.getId());
                session.delete(newNivel);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void datosNiveles() {

        codeNivel.setCellValueFactory(new PropertyValueFactory<NivelView, StringProperty>("codigo"));
        descripNivel.setCellValueFactory(new PropertyValueFactory<NivelView, StringProperty>("descripcion"));


        nivelViewObservableList = FXCollections.observableArrayList();
        nivelViewObservableList = getNivelViewObservableList(idObjeto);
        tableNivel.getItems().setAll(nivelViewObservableList);
    }

    public void pasarObjeto(Objetos objetos, Empresaconstructora empresaconstructora) {
        idObjeto = objetos.getId();
        empresa = empresaconstructora;
        comboListObjetos.setItems(getListObjetos(objetos.getZonasId()));
        String selectedObjeto = objetos.getCodigo() + " - " + objetos.getDescripcion();
        comboListObjetos.getSelectionModel().select(selectedObjeto);
        objetoModel = objetos;
        datosNiveles();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void addNewNivel(ActionEvent event) {

        newNivel = new Nivel();
        String code = comboListObjetos.getValue().substring(0, 2);

        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(code)) {
                objetoModel = objetos;
            }
        });
        if (objetoModel.getId() == idObjeto) {
            newNivel.setObjetosId(idObjeto);
        } else {
            newNivel.setObjetosId(objetoModel.getId());
        }

        if (btn_add.getText().contentEquals("Aceptar")) {

            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {

                newNivel.setCodigo(field_codigo.getText());
                newNivel.setDescripcion(field_descripcion.getText());


                Integer id = AddNivel(newNivel);

                if (id != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText("Objeto" + newNivel.getDescripcion() + " creado satisfactoriamente!");
                    alert.showAndWait();
                }

                runtime = Runtime.getRuntime();
                runtime.gc();

                field_codigo.clear();
                field_descripcion.clear();

                datosNiveles();
            } else if (field_codigo.getText().length() < 2 || field_descripcion.getText().length() < 3) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar el NivelPCW use el patrón: 'NivelPCW + No', para la descripción 2 digitos");
                alert.showAndWait();
            }
        }
        if (btn_add.getText().contentEquals("Editar")) {

            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {

                editNivel(nivelView.getId());


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos actualizados!");
                alert.showAndWait();

                runtime = Runtime.getRuntime();
                runtime.gc();

                field_codigo.clear();
                field_descripcion.clear();

                tableNivel.getItems().clear();
                datosNiveles();


            } else if (field_codigo.getText().length() < 2 || field_descripcion.getText().length() < 3) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar el NivelPCW use el patrón: 'NivelPCW + No', para la descripción 2 digitos");
                alert.showAndWait();
            }

        }
    }

    public void handleModificar(ActionEvent event) {

        nivelView = tableNivel.getSelectionModel().getSelectedItem();

        field_codigo.setText(nivelView.getCodigo());
        field_descripcion.setText(nivelView.getDescripcion());

        btn_add.setText("Editar");

    }

    public void handleEliminar(ActionEvent event) {

        nivelView = tableNivel.getSelectionModel().getSelectedItem();

        deleteNivel(nivelView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("NivelPCW Eliminado!");
        alert.showAndWait();

        tableNivel.getItems().clear();
        datosNiveles();

        runtime = Runtime.getRuntime();
        runtime.gc();
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }

    public void importarNivel(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarNiveles.fxml"));
            Parent proot = loader.load();
            ImportarNivelesController controller = loader.getController();
            controller.pasarDatos(objetoModel.getZonasByZonasId().getObraByObraId(), empresa, objetoModel.getZonasByZonasId(), objetoModel);


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

    public void duplicarNivel(ActionEvent event) {
        Nivel nivel = nivelArrayList.parallelStream().filter(nivel1 -> nivel1.getCodigo().trim().equals(tableNivel.getSelectionModel().getSelectedItem().getCodigo())).findFirst().orElse(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarNiveles.fxml"));
            Parent proot = loader.load();
            DuplicarNivelesController controller = loader.getController();
            controller.pasarDatos(objetoModel.getZonasByZonasId().getObraByObraId(), empresa, objetoModel.getZonasByZonasId(), objetoModel, nivel);


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
