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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Despachoscl;
import models.Recursos;
import models.VerDespachosModel;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VerDespachosController implements Initializable {
    private static SessionFactory sf;
    private static Recursos recursos;
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXButton btn_close;

    @FXML
    private TableView<VerDespachosModel> tableSum;

    @FXML
    private TableColumn<VerDespachosModel, String> code;

    @FXML
    private TableColumn<VerDespachosModel, Double> cant;

    @FXML
    private TableColumn<VerDespachosModel, String> date;

    @FXML
    private TableColumn<VerDespachosModel, String> user;

    @FXML
    private TableColumn<VerDespachosModel, JFXCheckBox> state;

    private ObservableList<VerDespachosModel> validarDespachosModels;
    private List<Despachoscl> despachosclList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void closeWindow(ActionEvent event) {
        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    public void cargarDespacho(List<Despachoscl> list) {
        despachosclList = new ArrayList<>();
        despachosclList = list;
        validarDespachosModels = FXCollections.observableArrayList();
        for (Despachoscl despachoscl : despachosclList) {
            if (despachoscl.getValido().equals("0")) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setSelected(false);
                validarDespachosModels.add(new VerDespachosModel(despachoscl.getId(), despachoscl.getVale(), despachoscl.getCantidad(), despachoscl.getFecha().toString(), despachoscl.getUsuario(), checkBox));
            } else if (despachoscl.getValido().equals("1")) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.setSelected(true);
                validarDespachosModels.add(new VerDespachosModel(despachoscl.getId(), despachoscl.getVale(), despachoscl.getCantidad(), despachoscl.getFecha().toString(), despachoscl.getUsuario(), checkBox));
            }
        }
        code.setCellValueFactory(new PropertyValueFactory<VerDespachosModel, String>("codigo"));
        cant.setCellValueFactory(new PropertyValueFactory<VerDespachosModel, Double>("cant"));
        user.setCellValueFactory(new PropertyValueFactory<VerDespachosModel, String>("usuario"));
        date.setCellValueFactory(new PropertyValueFactory<VerDespachosModel, String>("fecha"));
        state.setCellValueFactory(new PropertyValueFactory<VerDespachosModel, JFXCheckBox>("estado"));
        tableSum.getItems().setAll(validarDespachosModels);

    }


    public void tranferirDespachosHandle(ActionEvent event) {
        Despachoscl des = despachosclList.parallelStream().filter(despachoscl -> despachoscl.getId() == tableSum.getSelectionModel().getSelectedItem().getId()).findFirst().orElse(null);
        des.getId();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TransferirDespacho.fxml"));
            Parent proot = loader.load();
            TransferirCantidadController controller = loader.getController();
            controller.pasarDatos(des);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                despachosclList.removeIf(x -> x.getId() == des.getId());
                tableSum.getItems().remove(tableSum.getSelectionModel().getSelectedItem());
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}



