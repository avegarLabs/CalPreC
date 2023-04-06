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
import models.CuadrillaConstruccionView;
import models.Cuadrillaconstruccion;
import models.Usuarios;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CuadrillaContructoraController implements Initializable {


    private static SessionFactory sf;
    public CuadrillaContructoraController cuadrillaContructoraController;
    public Cuadrillaconstruccion cuadrillaconstruccionToUpdate;
    public int IdCuadrilla;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<CuadrillaConstruccionView> dataTable_cuadrilla;
    @FXML
    private TableColumn<CuadrillaConstruccionView, StringProperty> code;
    @FXML
    private TableColumn<CuadrillaConstruccionView, StringProperty> descrip;
    @FXML
    private TableColumn<CuadrillaConstruccionView, StringProperty> grupo;
    @FXML
    private TableColumn<CuadrillaConstruccionView, StringProperty> brigada;
    @FXML
    private TableColumn<CuadrillaConstruccionView, StringProperty> empresa;
    @FXML
    private JFXButton btn_add;
    @FXML
    private MenuItem option1;
    @FXML
    private MenuItem option2;
    private ArrayList<Cuadrillaconstruccion> cuadrillaconstruccionArrayList;
    private ObservableList<CuadrillaConstruccionView> observableCuadrillaConstruccionViews;
    private CuadrillaConstruccionView cuadrillaConstruccionView;

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */

    public ObservableList<CuadrillaConstruccionView> getCuadrillasCont() {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            cuadrillaconstruccionArrayList = new ArrayList<Cuadrillaconstruccion>();
            observableCuadrillaConstruccionViews = FXCollections.observableArrayList();
            cuadrillaconstruccionArrayList = (ArrayList<Cuadrillaconstruccion>) session.createQuery("FROM Cuadrillaconstruccion ").list();
            for (Cuadrillaconstruccion cuadrilla : cuadrillaconstruccionArrayList) {
                observableCuadrillaConstruccionViews.add(new CuadrillaConstruccionView(cuadrilla.getId(), cuadrilla.getCodigo(), cuadrilla.getDescripcion(), cuadrilla.getGrupoconstruccionByGrupoconstruccionId().getDescripcion(), cuadrilla.getGrupoconstruccionByGrupoconstruccionId().getBrigadaconstruccionByBrigadaconstruccionId().getDescripcion(), cuadrilla.getGrupoconstruccionByGrupoconstruccionId().getBrigadaconstruccionByBrigadaconstruccionId().getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion()));
            }

            tx.commit();
            session.close();
            return observableCuadrillaConstruccionViews;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void deleteCuadrilla(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Cuadrillaconstruccion cuadrillaconstruccion = session.get(Cuadrillaconstruccion.class, id);
            session.delete(cuadrillaconstruccion);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaCuadrilla.fxml"));
            Parent proot = loader.load();

            NuevaCuadrillaController controller = loader.getController();
            controller.sentController(cuadrillaContructoraController);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void loadData() {

        code.setCellValueFactory(new PropertyValueFactory<CuadrillaConstruccionView, StringProperty>("codigo"));
        code.setPrefWidth(100);
        descrip.setCellValueFactory(new PropertyValueFactory<CuadrillaConstruccionView, StringProperty>("descripcion"));
        descrip.setPrefWidth(250);
        grupo.setCellValueFactory(new PropertyValueFactory<CuadrillaConstruccionView, StringProperty>("grupo"));
        grupo.setPrefWidth(150);
        brigada.setCellValueFactory(new PropertyValueFactory<CuadrillaConstruccionView, StringProperty>("brigada"));
        brigada.setPrefWidth(150);
        empresa.setCellValueFactory(new PropertyValueFactory<CuadrillaConstruccionView, StringProperty>("empresa"));
        empresa.setPrefWidth(150);


        ObservableList<CuadrillaConstruccionView> datos = FXCollections.observableArrayList();
        datos = getCuadrillasCont();

        FilteredList<CuadrillaConstruccionView> filteredData = new FilteredList<CuadrillaConstruccionView>(datos, p -> true);

        SortedList<CuadrillaConstruccionView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable_cuadrilla.comparatorProperty());

        dataTable_cuadrilla.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(cuadrillaConstruccionView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = cuadrillaConstruccionView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cuadrillaContructoraController = this;

        loadData();

    }


    public void handleCuadrillaUpdateAction(ActionEvent event) {


        cuadrillaconstruccionToUpdate = cuadrillaconstruccionArrayList.parallelStream().filter(cua -> cua.getDescripcion().equals(dataTable_cuadrilla.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateCuadrilla.fxml"));
            Parent proot = loader.load();


            ActualizarCuadrillaController actualizarCuadrillaController = loader.getController();
            actualizarCuadrillaController.pasarParametros(cuadrillaContructoraController, cuadrillaconstruccionToUpdate);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();
            cuadrillaconstruccionToUpdate = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleCuadrilaDeleteAction(ActionEvent event) {

        cuadrillaconstruccionToUpdate = cuadrillaconstruccionArrayList.parallelStream().filter(cua -> cua.getDescripcion().equals(dataTable_cuadrilla.getSelectionModel().getSelectedItem().getDescripcion())).findFirst().orElse(null);
        deleteCuadrilla(cuadrillaconstruccionToUpdate.getId());
        cuadrillaconstruccionToUpdate = null;

    }

    public void checkUser(Usuarios usuarios) {
        if (usuarios.getGruposId() == 6) {

            btn_add.setDisable(true);
            option1.setDisable(true);
            option2.setDisable(true);

        }

    }


}
