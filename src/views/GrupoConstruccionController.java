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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.GrupoContruccionView;
import models.Grupoconstruccion;
import models.Usuarios;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GrupoConstruccionController implements Initializable {

    private static SessionFactory sf;
    public GrupoConstruccionController grupoConstruccionController;
    public Grupoconstruccion grupoconstruccionToUpdate;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<GrupoContruccionView> dataTable_grupo;
    @FXML
    private TableColumn<GrupoContruccionView, StringProperty> code;
    @FXML
    private TableColumn<GrupoContruccionView, StringProperty> descrip;
    @FXML
    private TableColumn<GrupoContruccionView, StringProperty> brigag;
    @FXML
    private TableColumn<GrupoContruccionView, StringProperty> empresa;
    @FXML
    private JFXButton btn_add;
    private ArrayList<Grupoconstruccion> listGrupos;
    private ObservableList<GrupoContruccionView> observableGrupoContruccionViewsList;
    private GrupoContruccionView grupoContruccionView;
    private int idGrupo;

    @FXML
    private MenuItem option1;

    @FXML
    private MenuItem option2;

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<GrupoContruccionView> getGrupoCont() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listGrupos = new ArrayList<Grupoconstruccion>();
            observableGrupoContruccionViewsList = FXCollections.observableArrayList();
            listGrupos = (ArrayList<Grupoconstruccion>) session.createQuery("FROM Grupoconstruccion ").list();
            for (Grupoconstruccion grupo : listGrupos) {
                observableGrupoContruccionViewsList.add(new GrupoContruccionView(grupo.getId(), grupo.getCodigo(), grupo.getDescripcion(), grupo.getBrigadaconstruccionByBrigadaconstruccionId().getDescripcion(), grupo.getBrigadaconstruccionByBrigadaconstruccionId().getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion()));
            }

            tx.commit();
            session.close();
            return observableGrupoContruccionViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /* Method to DELETE an Grupo de Contruccion from the records */
    public void deleteGrupo(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Grupoconstruccion grupoconstruccion = session.get(Grupoconstruccion.class, id);
            // for (Cuadrillaconstruccion cuadrillaconstruccion : grupoconstruccion.getCuadrillaconstruccionsById()){
            // session.delete(cuadrillaconstruccion);
            // }
            session.delete(grupoconstruccion);
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
    void handleButtonAction(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoGrupoConstruccion.fxml"));
            Parent proot = loader.load();

            NuevoGrupoConstruccionController controller = loader.getController();
            controller.sendController(grupoConstruccionController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void loadData() {
        code.setCellValueFactory(new PropertyValueFactory<GrupoContruccionView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<GrupoContruccionView, StringProperty>("descripcion"));
        descrip.setPrefWidth(250);
        brigag.setCellValueFactory(new PropertyValueFactory<GrupoContruccionView, StringProperty>("brigada"));
        brigag.setPrefWidth(250);
        empresa.setCellValueFactory(new PropertyValueFactory<GrupoContruccionView, StringProperty>("empresa"));
        empresa.setPrefWidth(250);

        ObservableList<GrupoContruccionView> datos = FXCollections.observableArrayList();
        datos = getGrupoCont();

        FilteredList<GrupoContruccionView> filteredData = new FilteredList<GrupoContruccionView>(datos, p -> true);

        SortedList<GrupoContruccionView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable_grupo.comparatorProperty());

        dataTable_grupo.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(grupoContruccionView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = grupoContruccionView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadData();

        grupoConstruccionController = this;


    }

    public void handleGrupoUpdateAction(ActionEvent event) {


        grupoconstruccionToUpdate = listGrupos.parallelStream().filter(gc -> gc.getDescripcion().equals(dataTable_grupo.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarGrupoConstruccion.fxml"));
            Parent proot = loader.load();
            ActualizarGrupoConstruccionController actualizarGrupoConstruccionController = loader.getController();
            actualizarGrupoConstruccionController.pasarParametros(grupoConstruccionController, grupoconstruccionToUpdate);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            grupoconstruccionToUpdate = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleGrupoDeleteAction(ActionEvent event) {


        grupoconstruccionToUpdate = listGrupos.parallelStream().filter(gc -> gc.getDescripcion().equals(dataTable_grupo.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);
        deleteGrupo(grupoconstruccionToUpdate.getId());
        loadData();
        grupoconstruccionToUpdate = null;

    }

    public void checkUsuario(Usuarios usuarios) {

        if (usuarios.getGruposId() == 6) {
            btn_add.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);

        }
    }


    public void handleCambiarOperaciones(ActionEvent event) {


        grupoconstruccionToUpdate = listGrupos.parallelStream().filter(gc -> gc.getDescripcion().equals(dataTable_grupo.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MoverCertificacionGrupoConstruccion.fxml"));
            Parent proot = loader.load();
            MoverCertificacionGrupoConstruccionController controller = loader.getController();
            controller.pasarParametros(grupoconstruccionToUpdate, grupoconstruccionToUpdate.getBrigadaconstruccionByBrigadaconstruccionId().getEmpresaconstructoraId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            grupoconstruccionToUpdate = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
