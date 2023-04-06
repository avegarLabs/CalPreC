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
import models.ConnectionModel;
import models.GrupoEjecucionView;
import models.Grupoejecucion;
import models.Usuarios;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GrupoEjecucionController implements Initializable {


    private static SessionFactory sf;
    public GrupoEjecucionController grupoEjecucionController;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField field_descripcion;
    @FXML
    private TableView<GrupoEjecucionView> dataTable_grupoejecucion;
    @FXML
    private TableColumn<GrupoEjecucionView, StringProperty> code;
    @FXML
    private TableColumn<GrupoEjecucionView, StringProperty> descrip;
    @FXML
    private JFXButton btn_add;
    private ArrayList<Grupoejecucion> grupoejecucionArrayList;
    private ObservableList<GrupoEjecucionView> observableGrupoEjecucionViewsList;
    private GrupoEjecucionView grupoEjecucionView;
    private Grupoejecucion grupoejecucionToChange;
    private int idgrupo;


    @FXML
    private MenuItem option1;

    @FXML
    private MenuItem option2;


    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<GrupoEjecucionView> getGrupoEjecucion() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoejecucionArrayList = new ArrayList<Grupoejecucion>();
            observableGrupoEjecucionViewsList = FXCollections.observableArrayList();
            grupoejecucionArrayList = (ArrayList<Grupoejecucion>) session.createQuery("FROM Grupoejecucion ").list();
            for (Grupoejecucion grupoE : grupoejecucionArrayList) {
                observableGrupoEjecucionViewsList.add(new GrupoEjecucionView(grupoE.getId(), grupoE.getCodigo(), grupoE.getDescripcion()));
            }

            tx.commit();
            session.close();
            return observableGrupoEjecucionViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


    private boolean checkGrupoConstruccion(Grupoejecucion grupoejecucion) {

        return observableGrupoEjecucionViewsList.stream().filter(o -> o.getCodigo().equals(grupoejecucion.getCodigo())).findFirst().isPresent();
    }


    /**
     * Metodo para agregar un nuevo grupo de ejecución
     */
    public Integer AddGrupoEjecicion(Grupoejecucion grupoejecucion) {

        Session grpsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer ge = 0;

        try {
            trx = grpsession.beginTransaction();

            boolean res = checkGrupoConstruccion(grupoejecucion);
            if (res == false) {
                ge = (Integer) grpsession.save(grupoejecucion);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("El código del grupo de ejecución especificado ya existe en este!!! ");
                alert.showAndWait();
            }

            trx.commit();
            grpsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            grpsession.close();
        }
        return ge;
    }

    /* Method to DELETE an Grupo de ejecusion from the records */
    public void deleteGrupoEjecucion(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Grupoejecucion grupoejecucion = session.get(Grupoejecucion.class, id);
            session.delete(grupoejecucion);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @FXML
    void newGrupoEjecucionAction(ActionEvent event) {
        String cod = field_codigo.getText();
        String desc = field_descripcion.getText();

        if (!cod.isEmpty() && !desc.isEmpty()) {
            Grupoejecucion grupoejecucion = new Grupoejecucion();
            grupoejecucion.setCodigo(cod);
            grupoejecucion.setDescripcion(desc);

            Integer geId = AddGrupoEjecicion(grupoejecucion);

            if (geId != null) {
                clearField();
            }

            loadData();
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


    public void loadData() {

        code.setCellValueFactory(new PropertyValueFactory<GrupoEjecucionView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<GrupoEjecucionView, StringProperty>("descripcion"));
        descrip.setPrefWidth(250);

        ObservableList<GrupoEjecucionView> datos = FXCollections.observableArrayList();
        datos = getGrupoEjecucion();

        FilteredList<GrupoEjecucionView> filteredData = new FilteredList<GrupoEjecucionView>(datos, p -> true);

        SortedList<GrupoEjecucionView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable_grupoejecucion.comparatorProperty());

        dataTable_grupoejecucion.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(empresaConstructoraView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = empresaConstructoraView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();

        grupoEjecucionController = this;


    }


    public void handleGrupoEjecucionUpdateAction(ActionEvent event) {
        grupoejecucionToChange = grupoejecucionArrayList.parallelStream().filter(ge -> ge.getDescripcion().equals(dataTable_grupoejecucion.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarGrupoEjecucion.fxml"));
            Parent proot = loader.load();
            ActualizarGrupoEjecucionController actualizarGrupoEjecucionController = loader.getController();
            actualizarGrupoEjecucionController.pasarParametros(grupoEjecucionController, grupoejecucionToChange);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
            grupoejecucionToChange = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleGrupoEjecucionDeleteAction(ActionEvent event) {

        grupoejecucionToChange = grupoejecucionArrayList.parallelStream().filter(ge -> ge.getDescripcion().equals(dataTable_grupoejecucion.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);
        deleteGrupoEjecucion(grupoejecucionToChange.getId());
        loadData();

    }

    public void checkUsuario(Usuarios usuarios) {

        if (usuarios.getGruposId() == 6) {

            btn_add.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);

        }
    }


}
