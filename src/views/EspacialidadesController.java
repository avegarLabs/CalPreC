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
import java.util.ResourceBundle;

public class EspacialidadesController implements Initializable {


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
    private JFXComboBox<String> comboListZonas;
    private ArrayList<Zonas> zonasArrayList;
    private ObservableList<String> listZonas;
    private Zonas zonasModel;
    private Empresaconstructora empresaconstructoraMod;
    private Objetos objetosMod;
    private Nivel nivelMod;
    private Obra obraM;
    private int idZona;
    private ArrayList<Especialidades> especialidadesArrayList;
    private ObservableList<EspecialidadesView> especialidadesViewObservableList;
    private EspecialidadesView especialidadesView;
    private Especialidades especialidades;
    @FXML
    private TableView<EspecialidadesView> tablaEspecialidades;
    @FXML
    private TableColumn<EspecialidadesView, StringProperty> code;
    @FXML
    private TableColumn<EspecialidadesView, StringProperty> descripcion;
    private Runtime runtime;
    private Integer inputLength = 2;

    public ObservableList<EspecialidadesView> getEspecialidadesViewObservableList() {

        especialidadesViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("FROM Especialidades ").list();
            for (Especialidades especialidades1 : especialidadesArrayList) {
                especialidadesView = new EspecialidadesView(especialidades1.getId(), especialidades1.getCodigo(), especialidades1.getDescripcion());
                especialidadesViewObservableList.add(especialidadesView);
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return especialidadesViewObservableList;
    }

    public boolean checkEsABoolean(Especialidades especialidades) {
        return especialidadesViewObservableList.stream().filter(o -> o.getCodigo().equals(especialidades.getCodigo())).findFirst().isPresent();

    }

    public Integer AddEspecialidad(Especialidades especialidades) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer objId = null;
        try {
            tx = session.beginTransaction();

            boolean res = checkEsABoolean(especialidades);

            if (res == false) {
                objId = (Integer) session.save(especialidades);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código de la especialidad especificada ya existe!!! ");
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

    private void editEspecialidad(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            especialidades = session.get(Especialidades.class, id);
            if (especialidades != null) {
                especialidades.setCodigo(field_codigo.getText());
                especialidades.setDescripcion(field_descripcion.getText());

                session.update(especialidades);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void deleteEspecialidad(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            especialidades = session.get(Especialidades.class, id);
            if (especialidades != null) {

                session.delete(especialidades);
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
        dataEspecilaidad();

    }

    public void dataEspecilaidad() {
        tablaEspecialidades.getItems().clear();

        code.setCellValueFactory(new PropertyValueFactory<EspecialidadesView, StringProperty>("codigo"));
        descripcion.setCellValueFactory(new PropertyValueFactory<EspecialidadesView, StringProperty>("descripcion"));


        especialidadesViewObservableList = FXCollections.observableArrayList();
        especialidadesViewObservableList = getEspecialidadesViewObservableList();
        tablaEspecialidades.getItems().setAll(especialidadesViewObservableList);

    }

    @FXML
    public void addNewObjetos(ActionEvent event) {

        especialidades = new Especialidades();

        if (btn_add.getText().contentEquals("Aceptar")) {

            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {

                especialidades.setCodigo(field_codigo.getText());
                especialidades.setDescripcion(field_descripcion.getText());


                Integer id = AddEspecialidad(especialidades);

                if (id != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText(especialidades.getDescripcion() + " creado satisfactoriamente!");
                    alert.showAndWait();
                }

                runtime = Runtime.getRuntime();
                runtime.gc();
                field_codigo.clear();
                field_descripcion.clear();
                dataEspecilaidad();
            } else if (field_codigo.getText().length() < 2 || field_descripcion.getText().length() < 3) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar la Especialidad use el patrón: 'Especialidad + No', para la descripción 2 digitos");
                alert.showAndWait();

            }
        }
        if (btn_add.getText().contentEquals("Editar")) {

            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {
                editEspecialidad(especialidadesView.getId());


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos actualizados!");
                alert.showAndWait();


                runtime = Runtime.getRuntime();
                runtime.gc();
                field_codigo.clear();
                field_descripcion.clear();

                tablaEspecialidades.getItems().clear();
                dataEspecilaidad();

            } else if (field_codigo.getText().length() < 2 || field_descripcion.getText().length() < 3) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar la Especialidad use el patrón: 'Especialidad + No', para la descripción 2 digitos");
                alert.showAndWait();

            }
        }
    }

    public void handleModificar(ActionEvent event) {

        especialidadesView = tablaEspecialidades.getSelectionModel().getSelectedItem();

        field_codigo.setText(especialidadesView.getCodigo());
        field_descripcion.setText(especialidadesView.getDescripcion());

        btn_add.setText("Editar");

    }

    public void handleEliminar(ActionEvent event) {

        especialidadesView = tablaEspecialidades.getSelectionModel().getSelectedItem();

        deleteEspecialidad(especialidadesView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Zona Eliminada!");
        alert.showAndWait();

        tablaEspecialidades.getItems().clear();
        dataEspecilaidad();
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }

    public void pasarDatos(Obra obra, Empresaconstructora empresaconstructora, Zonas zonas, Objetos objetos, Nivel nivel) {
        obraM = obra;
        empresaconstructoraMod = empresaconstructora;
        zonasModel = zonas;
        objetosMod = objetos;
        nivelMod = nivel;
    }

    public void handleDuplicarEsp(ActionEvent event) {


        try {
            Especialidades especialidades = especialidadesArrayList.parallelStream().filter(esp -> esp.getId() == tablaEspecialidades.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarEspecialidad.fxml"));
            Parent proot = loader.load();

            DuplicarEspecialidadController controller = loader.getController();
            controller.pasarDatos(obraM, empresaconstructoraMod, zonasModel, objetosMod, nivelMod, especialidades);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleImportarEsp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarEspecialidad.fxml"));
            Parent proot = loader.load();

            importarEspecialidadController controller = loader.getController();
            controller.pasarDatos(obraM, empresaconstructoraMod, zonasModel, objetosMod, nivelMod);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
