package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.ConnectionModel;
import models.Despachoscl;
import models.Recursos;
import models.ValidarDespachosModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ValidarDespachosController implements Initializable {
    private static SessionFactory sf;
    private static Recursos recursos;
    @FXML
    public TableView<ValidarDespachosModel> tableSum;
    public ValidarDespachosController validarDespachosController;
    Despachoscl actual;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXButton btn_close;
    @FXML
    private TableColumn<ValidarDespachosModel, String> code;
    @FXML
    private TableColumn<ValidarDespachosModel, String> descrip;
    @FXML
    private TableColumn<ValidarDespachosModel, Double> cant;
    @FXML
    private TableColumn<ValidarDespachosModel, JFXCheckBox> state;
    private ObservableList<ValidarDespachosModel> validarDespachosModels;
    private List<Despachoscl> despachosclList;
    private List<Despachoscl> filterList;
    private int countsub = 0;
    private int batchSub = 50;
    private CartaLimiteController controllerCL;

    private static Recursos getRecursoById(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recursos = new Recursos();
            recursos = session.get(Recursos.class, id);
            tx.commit();
            session.close();
            return recursos;
        } catch (Exception e) {

        }
        return new Recursos();

    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    public void handleCargarTable(ActionEvent event) {

        ObservableList<ValidarDespachosModel> validarDespachosModelObservableList = FXCollections.observableArrayList();
        validarDespachosModelObservableList = getValidarDespachosModels(field_codigo.getText());
        code.setCellValueFactory(new PropertyValueFactory<ValidarDespachosModel, String>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<ValidarDespachosModel, String>("descripcion"));
        cant.setCellValueFactory(new PropertyValueFactory<ValidarDespachosModel, Double>("cant"));
        state.setCellValueFactory(new PropertyValueFactory<ValidarDespachosModel, JFXCheckBox>("estado"));
        tableSum.getItems().setAll(validarDespachosModelObservableList);

    }

    public void handleValidarTodos(ActionEvent event) {
        List<Despachoscl> okList = new ArrayList<>();
        for (Despachoscl despachoscl : filterList) {
            despachoscl.setValido("1");
            okList.add(despachoscl);
        }

        updateDespachos(okList);
        handleCargarTable(event);

    }

    public void handleRechazarUnik(ActionEvent event) {
        Despachoscl despachoscl = filterList.parallelStream().filter(d -> d.getId() == tableSum.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);
        despachoscl.setValido("1");
        List<Despachoscl> okList = new ArrayList<>();
        okList.add(despachoscl);
        updateDespachos(okList);
        handleCargarTable(event);
    }

    private ObservableList<ValidarDespachosModel> getValidarDespachosModels(String noVale) {
        filterList = new ArrayList<>();
        filterList.addAll(despachosclList.parallelStream().filter(des -> des.getVale().trim().equals(noVale.trim())).collect(Collectors.toList()));
        validarDespachosModels = FXCollections.observableArrayList();
        for (Despachoscl despachoscl : filterList) {
            if (despachoscl.getValido().equals("0")) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setSelected(false);
                Recursos rec = getRecursoById(despachoscl.getSuministroId());
                validarDespachosModels.add(new ValidarDespachosModel(despachoscl.getId(), rec.getCodigo(), rec.getDescripcion(), despachoscl.getCantidad(), checkBox));
            } else if (despachoscl.getValido().equals("1")) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setSelected(true);
                Recursos rec = getRecursoById(despachoscl.getSuministroId());
                validarDespachosModels.add(new ValidarDespachosModel(despachoscl.getId(), rec.getCodigo(), rec.getDescripcion(), despachoscl.getCantidad(), checkBox));

            }
        }

        return validarDespachosModels;
    }

    private void updateDespachos(List<Despachoscl> despachosclList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countsub = 0;
            for (Despachoscl sub : despachosclList) {
                countsub++;
                if (countsub > 0 && countsub % batchSub == 0) {
                    session.flush();
                    session.clear();
                }
                session.update(sub);
            }

            tx.commit();
            session.close();
        } catch (Exception e) {

        }
    }

    private void deleteDespachos(Despachoscl despachoscl) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            int op = session.createQuery("DELETE FROM Despachoscl WHERE id =: idDe").setParameter("idDe", despachoscl.getId()).executeUpdate();

            tx.commit();
            session.close();
        } catch (Exception e) {

        }
    }

    public void cargarDespacho(CartaLimiteController cartaController, List<Despachoscl> list) {
        controllerCL = cartaController;
        despachosclList = new ArrayList<>();
        despachosclList = list;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validarDespachosController = this;
    }

    public void handleModificarCantidad(ActionEvent event) {
        actual = filterList.parallelStream().filter(d -> d.getId() == tableSum.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cantidad.fxml"));
            Parent proot = loader.load();
            CantidadController controller = loader.getController();
            controller.pasarDatos(validarDespachosController, actual, tableSum.getSelectionModel().getSelectedIndex());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                handleCargarTable(event);
                controllerCL.handleGetMateriales(event);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handleEliminar(ActionEvent event) {
        Despachoscl despachoscl = filterList.parallelStream().filter(d -> d.getId() == tableSum.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);

        if (despachoscl.getValido().trim().equals("0")) {
            try {
                deleteDespachos(despachoscl);
                filterList.removeIf(x -> x.getId() == despachoscl.getId());
                tableSum.getItems().removeIf(x -> x.getId() == despachoscl.getId());
                controllerCL.handleGetMateriales(event);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!!");
                alert.setContentText(" Error al eliminar el despacho ");
                alert.showAndWait();
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!!");
            alert.setContentText("No se puede modificar las cantidades de los despachos confirmados");
            alert.showAndWait();
        }

    }

}



