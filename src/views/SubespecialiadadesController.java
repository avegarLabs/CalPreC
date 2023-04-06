package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import models.ConnectionModel;
import models.Especialidades;
import models.SubespecialidadView;
import models.Subespecialidades;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubespecialiadadesController implements Initializable {

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
    private ArrayList<Especialidades> objetosArrayList;
    private ObservableList<String> listObjetos;
    private Especialidades especialidadesModel;
    private int idObjeto;
    private Runtime runtime;
    @FXML
    private TableView<SubespecialidadView> tableSubespecialidad;
    @FXML
    private TableColumn<SubespecialidadView, StringProperty> codeNivel;
    @FXML
    private TableColumn<SubespecialidadView, StringProperty> descripNivel;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private ObservableList<SubespecialidadView> subespecialidadViewObservableList;
    private SubespecialidadView subespecialidadView;
    private Subespecialidades newSubespecialidades;
    private Integer inputLength = 3;

    public ObservableList<String> getListObjetos() {


        listObjetos = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            objetosArrayList = (ArrayList<Especialidades>) session.createQuery("FROM Especialidades ").list();
            for (Especialidades objetos : objetosArrayList) {
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

    public boolean checkEsABoolean(Subespecialidades subespecialidades) {
        return subespecialidadViewObservableList.stream().filter(o -> o.getCodigo().equals(subespecialidades.getCodigo())).findFirst().isPresent();

    }

    public Integer AddSubEspecialidad(Subespecialidades subespecialidades) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer nivId = null;

        try {
            tx = session.beginTransaction();

            boolean res = checkEsABoolean(subespecialidades);

            if (res == false) {
                nivId = (Integer) session.save(subespecialidades);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código de la subespecialidad especificada ya existe!!! ");
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

    public ObservableList<SubespecialidadView> getSubespecialidadViewObservableList(int Id) {

        subespecialidadViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            subespecialidadesArrayList = (ArrayList<Subespecialidades>) session.createQuery("FROM Subespecialidades where especialidadesId = :id ").setParameter("id", Id).getResultList();
            for (Subespecialidades nivel : subespecialidadesArrayList) {
                subespecialidadView = new SubespecialidadView(nivel.getId(), nivel.getCodigo(), nivel.getDescripcion());
                subespecialidadViewObservableList.add(subespecialidadView);
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return subespecialidadViewObservableList;
    }

    private void editSubespecialidad(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            newSubespecialidades = session.get(Subespecialidades.class, id);
            if (newSubespecialidades != null) {
                newSubespecialidades.setCodigo(field_codigo.getText());
                newSubespecialidades.setDescripcion(field_descripcion.getText());
                if (newSubespecialidades.getEspecialidadesId() != idObjeto) {
                    newSubespecialidades.setEspecialidadesId(especialidadesModel.getId());
                }
                session.update(newSubespecialidades);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void deleteSubespecialidad(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            newSubespecialidades = session.get(Subespecialidades.class, id);
            if (newSubespecialidades != null) {

                session.delete(newSubespecialidades);
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

        tableSubespecialidad.getItems().clear();
        codeNivel.setCellValueFactory(new PropertyValueFactory<SubespecialidadView, StringProperty>("codigo"));
        descripNivel.setCellValueFactory(new PropertyValueFactory<SubespecialidadView, StringProperty>("descripcion"));


        subespecialidadViewObservableList = FXCollections.observableArrayList();
        subespecialidadViewObservableList = getSubespecialidadViewObservableList(idObjeto);
        tableSubespecialidad.getItems().setAll(subespecialidadViewObservableList);
    }

    public void pasarEspecialidad(Especialidades especialidad) {
        idObjeto = especialidad.getId();
        comboListObjetos.setItems(getListObjetos());
        String selectedObjeto = especialidad.getCodigo() + " - " + especialidad.getDescripcion();
        comboListObjetos.getSelectionModel().select(selectedObjeto);

        datosNiveles();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void addNewNivel(ActionEvent event) {

        newSubespecialidades = new Subespecialidades();
        String code = comboListObjetos.getValue().substring(0, 2);

        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(code)) {
                especialidadesModel = objetos;
            }
        });
        if (especialidadesModel.getId() == idObjeto) {
            newSubespecialidades.setEspecialidadesId(idObjeto);
        } else {
            newSubespecialidades.setEspecialidadesId(especialidadesModel.getId());
        }

        if (btn_add.getText().contentEquals("Aceptar")) {

            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {

                newSubespecialidades.setCodigo(field_codigo.getText());
                newSubespecialidades.setDescripcion(field_descripcion.getText());


                Integer id = AddSubEspecialidad(newSubespecialidades);

                if (id != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Información");
                    alert.setContentText("Subespecialidad creada satisfactoriamente!");
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
                alert.setContentText("Error al declarar la Subespecialidad use el patrón: 'Subespecialidad + No', para la descripción 2 digitos");
                alert.showAndWait();
            }
        }
        if (btn_add.getText().contentEquals("Editar")) {


            if (field_codigo.getText().length() == 2 || field_descripcion.getText().length() > 3) {

                editSubespecialidad(subespecialidadView.getId());


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Información");
                alert.setContentText("Datos actualizados!");
                alert.showAndWait();

                runtime = Runtime.getRuntime();
                runtime.gc();

                field_codigo.clear();
                field_descripcion.clear();

                tableSubespecialidad.getItems().clear();
                datosNiveles();


            } else if (field_codigo.getText().length() < 2 || field_descripcion.getText().length() < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Información");
                alert.setContentText("Error al declarar la Subespecialidad use el patrón: 'Subespecialidad + No', para la descripción 2 digitos");
                alert.showAndWait();
            }

        }
    }

    public void handleModificar(ActionEvent event) {

        subespecialidadView = tableSubespecialidad.getSelectionModel().getSelectedItem();

        field_codigo.setText(subespecialidadView.getCodigo());
        field_descripcion.setText(subespecialidadView.getDescripcion());

        btn_add.setText("Editar");

    }

    public void handleEliminar(ActionEvent event) {

        subespecialidadView = tableSubespecialidad.getSelectionModel().getSelectedItem();

        deleteSubespecialidad(subespecialidadView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("NivelPCW Eliminado!");
        alert.showAndWait();

        tableSubespecialidad.getItems().clear();
        datosNiveles();
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            field_descripcion.requestFocus();
        }
    }
}


