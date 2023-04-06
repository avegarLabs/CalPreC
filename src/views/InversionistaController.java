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


public class InversionistaController implements Initializable {

    private static SessionFactory sf;
    public InversionistaController inversionistaController;
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
    private TableView<InversionistaView> dataTable_inversionista;
    @FXML
    private TableColumn<InversionistaView, StringProperty> code;
    @FXML
    private TableColumn<InversionistaView, StringProperty> descripcion;
    private ArrayList<Inversionista> inversionistasList;
    private ObservableList<InversionistaView> observableInversionistaViewsList;
    private InversionistaView inversionistaView;
    private Inversionista inversionistaToChange;
    private int idInversionista;

    @FXML
    private MenuItem option1;

    @FXML
    private MenuItem option2;

    private ArrayList<Nomencladorempresa> nomencladorempresaArrayList;
    private ObservableList<String> listeEmpresa;


    public ObservableList<String> getListaEmpresa() {
        listeEmpresa = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
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
        return listeEmpresa;
    }


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<InversionistaView> getInversionistas() {

        inversionistasList = new ArrayList<Inversionista>();
        observableInversionistaViewsList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            inversionistasList = (ArrayList<Inversionista>) session.createQuery("FROM Inversionista ").list();
            for (Inversionista inversionista : inversionistasList) {
                observableInversionistaViewsList.add(new InversionistaView(inversionista.getId(), inversionista.getCodigo(), inversionista.getDescripcion()));
            }


            tx.commit();
            session.close();
            return observableInversionistaViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private boolean checkInversionista(Inversionista inversionista) {

        return observableInversionistaViewsList.stream().filter(o -> o.getCodigo().equals(inversionista.getCodigo())).findFirst().isPresent();
    }


    /**
     * Metodo para agregar un nuevo inversionista
     */
    public Integer AddInversionista(Inversionista inversionista) {
        Session invsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer inv = 0;

        try {
            trx = invsession.beginTransaction();


            boolean res = checkInversionista(inversionista);
            if (res == false) {
                inv = (Integer) invsession.save(inversionista);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código del inversionista especificado ya existe!!! ");
                alert.showAndWait();
            }


            trx.commit();
            invsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            invsession.close();
        }
        return inv;
    }

    /**
     * Delete Inversionista
     *
     * @param id
     */
    public void deleteInversionista(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Inversionista inversionista = session.get(Inversionista.class, id);
            session.delete(inversionista);
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
        code.setCellValueFactory(new PropertyValueFactory<InversionistaView, StringProperty>("codigo"));
        code.setPrefWidth(100);

        descripcion.setCellValueFactory(new PropertyValueFactory<InversionistaView, StringProperty>("descripcion"));
        descripcion.setPrefWidth(100);

        ObservableList<InversionistaView> datos = FXCollections.observableArrayList();
        datos = getInversionistas();


        FilteredList<InversionistaView> filteredData = new FilteredList<InversionistaView>(datos, p -> true);

        SortedList<InversionistaView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable_inversionista.comparatorProperty());

        dataTable_inversionista.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(inversionistaView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = inversionistaView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadData();

        inversionistaController = this;

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

    public void newInversionistaAction(ActionEvent event) {

        String cod = field_codigo.getText();
        String desc = field_descripcion.getText();

        if (!cod.isEmpty() && !desc.isEmpty()) {
            Inversionista inversionista = new Inversionista();
            inversionista.setCodigo(cod);
            inversionista.setDescripcion(desc);

            int invId = AddInversionista(inversionista);

            loadData();
            clearData();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Complete los campos del formulario");
            alert.showAndWait();
        }

    }

    public void handleInversionistaUpdateAction(ActionEvent event) {

        inversionistaView = dataTable_inversionista.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarInversionista.fxml"));
            Parent proot = loader.load();
            ActualizarInversionistaController actualizarInversionistaController = loader.getController();
            actualizarInversionistaController.pasarParametros(inversionistaController, inversionistaView);

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

    public void handleInversionistaDeleteAction(ActionEvent event) {

        inversionistaView = dataTable_inversionista.getSelectionModel().getSelectedItem();
        deleteInversionista(inversionistaView.getId());
        loadData();
    }

    public void clearData() {
        field_codigo.clear();
        field_descripcion.clear();
    }


    public void chckUser(Usuarios usuarios) {

        if (usuarios.getGruposId() == 6) {

            field_codigo.setDisable(true);
            field_descripcion.setDisable(true);
            btn_add.setDisable(true);

            option1.setDisable(true);
            option2.setDisable(true);

        }

    }


}
