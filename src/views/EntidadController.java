package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EntidadController implements Initializable {

    private static SessionFactory sf;
    public EntidadController entidadController;
    @FXML
    private Label label_title;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private MenuItem option1;
    @FXML
    private MenuItem option2;
    @FXML
    private TableView<EntidadView> dataTable_entidad;
    @FXML
    private TableColumn<EntidadView, StringProperty> code;
    @FXML
    private TableColumn<EntidadView, StringProperty> descripcion;
    private ArrayList<Entidad> entidadsList;
    private ObservableList<EntidadView> observableEntidadViewsList;
    private EntidadView entidadView;
    private Entidad entidadToChange;
    private int idEntidad;

    private ArrayList<Nomencladorempresa> nomencladorempresaArrayList;
    private ObservableList<String> listeEmpresa;

    public ObservableList<String> getListaEmpresa() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            listeEmpresa = FXCollections.observableArrayList();
            tx = session.beginTransaction();
            nomencladorempresaArrayList = (ArrayList<Nomencladorempresa>) session.createQuery("FROM Nomencladorempresa ").list();
            listeEmpresa.addAll(nomencladorempresaArrayList.parallelStream().map(me -> me.getReup() + " - " + me.getDescripcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return listeEmpresa;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<EntidadView> getEntidades() {

        entidadsList = new ArrayList<Entidad>();
        observableEntidadViewsList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            entidadsList = (ArrayList<Entidad>) session.createQuery("FROM Entidad ").getResultList();
            for (Entidad entidad : entidadsList) {
                observableEntidadViewsList.add(new EntidadView(entidad.getId(), entidad.getCodigo(), entidad.getDescripcion()));
            }

            tx.commit();
            session.close();
            return observableEntidadViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    private boolean checkEntidad(Entidad entidad) {

        return observableEntidadViewsList.stream().filter(o -> o.getCodigo().equals(entidad.getCodigo())).findFirst().isPresent();
    }


    /**
     * Metodo para agregar una nueva entidad
     */

    public Integer newEntidad(Entidad entidad) {

        Session entsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer ent = 0;

        try {
            trx = entsession.beginTransaction();


            boolean res = checkEntidad(entidad);
            if (res == false) {
                ent = (Integer) entsession.save(entidad);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código de la entidad especificada ya existe!!! ");
                alert.showAndWait();
            }

            trx.commit();
            entsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            entsession.close();
        }
        return ent;
    }

    public void deleteEntidad(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Entidad entidad = session.get(Entidad.class, id);
            session.delete(entidad);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    public void loadData() {
        code.setCellValueFactory(new PropertyValueFactory<EntidadView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descripcion.setCellValueFactory(new PropertyValueFactory<EntidadView, StringProperty>("descripcion"));
        descripcion.setPrefWidth(250);

        ObservableList<EntidadView> datos = FXCollections.observableArrayList();
        datos = getEntidades();


        FilteredList<EntidadView> filteredData = new FilteredList<EntidadView>(datos, p -> true);

        SortedList<EntidadView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable_entidad.comparatorProperty());

        dataTable_entidad.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(entidadView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = entidadView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        entidadController = this;

        loadData();

        getListaEmpresa();
        TextFields.bindAutoCompletion(field_codigo, listeEmpresa);


    }

    public void handleDefineEmpresa() {
        String code = field_codigo.getText().substring(0, 5);
        field_codigo.clear();
        field_codigo.setText(code);


        for (Nomencladorempresa ne : nomencladorempresaArrayList) {
            if (ne.getReup().contentEquals(code)) {
                field_descripcion.setText(ne.getDescripcion());
            }
        }

    }


    public void addNewEntidad(ActionEvent event) {

        String cod = field_codigo.getText();
        String descrip = field_descripcion.getText();

        if (!cod.isEmpty() && !descrip.isEmpty()) {

            Entidad ent = new Entidad();
            ent.setCodigo(cod);
            ent.setDescripcion(descrip);

            Integer id = newEntidad(ent);

            if (id != null) {
                loadData();
                clearField();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Complete los campos del formulario");
            alert.showAndWait();
        }
    }


    public void clearField() {
        field_codigo.clear();
        field_descripcion.clear();
    }


    public void handleEntidadUpdateAction(ActionEvent event) {

        entidadView = dataTable_entidad.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarEntidad.fxml"));
            Parent proot = loader.load();
            ActualizarEntidadController actualizarEntidadController = loader.getController();
            actualizarEntidadController.pasarParametros(entidadController, entidadView);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadData();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleEntidadDeleteAction(ActionEvent event) {

        entidadView = dataTable_entidad.getSelectionModel().getSelectedItem();
        deleteEntidad(entidadView.getId());

        loadData();
    }


    public void checkUser(Usuarios usuarios) {

        if (usuarios.getGruposId() == 6) {

            field_codigo.setDisable(true);
            field_descripcion.setDisable(true);
            btn_add.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);
        }
    }


}
