package views;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
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
import java.util.stream.Collectors;

public class SearchRVUOController implements Initializable {
    private static SessionFactory sf;
    RenVarianteToUnidadObraController renVarianteToRenglonObraController;
    @FXML
    private VBox box;
    @FXML
    private TableView<RenglonVarianteSearchView> dataTable;
    @FXML
    private TableColumn<RenglonVarianteSearchView, StringProperty> code;
    @FXML
    private TableColumn<RenglonVarianteSearchView, StringProperty> descrip;
    @FXML
    private TableColumn<RenglonVarianteSearchView, StringProperty> um;
    @FXML
    private TableColumn<RenglonVarianteSearchView, DoubleProperty> costoMate;
    @FXML
    private TableColumn<RenglonVarianteSearchView, DoubleProperty> costoMano;
    @FXML
    private TableColumn<RenglonVarianteSearchView, DoubleProperty> costEquip;
    @FXML
    private TableColumn<RenglonVarianteSearchView, JFXButton> refere;

    private List<Renglonvariante> renglonvarianteArrayList;
    private ObservableList<RenglonVarianteSearchView> renglonVarianteViewObservableList;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private double importe;

    public ObservableList<RenglonVarianteSearchView> searchRenglonPattern(String pattern, int norma) {
        List<Renglonvariante> list = utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglon -> renglon.getRs() == norma).collect(Collectors.toList());
        renglonvarianteArrayList = new ArrayList<>();
        renglonvarianteArrayList = list.parallelStream().filter(renglon -> renglon.getCodigo().trim().contains(pattern) || renglon.getDescripcion().trim().contains(pattern)).collect(Collectors.toList());
        for (Renglonvariante renglon : renglonvarianteArrayList) {
            double salario = getSalrioInRV(renglon);
            JFXButton button = new JFXButton("Info");
            button.setStyle("-fx-border-color:  #286090; -fx-border-radius: 5px;");
            button.setOnMouseClicked(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Datos del Renglon Variante");
                alert.setHeaderText("RV: " + renglon.getCodigo());
                alert.setContentText("SOBREGRUPO: " + renglon.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion() + "\n" +
                        "GRUPO: " + renglon.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong() + "\n" +
                        "SUBGRUPO: " + renglon.getSubgrupoBySubgrupoId().getDescripcionsub());
                alert.showAndWait();
            });
            renglonVarianteViewObservableList.add(new RenglonVarianteSearchView(renglon.getId(), renglon.getCodigo(), renglon.getDescripcion(), renglon.getUm(), Math.round(renglon.getCostomat() * 100d) / 100d, Math.round(renglon.getCostmano() * 100d) / 100d, Math.round(renglon.getCostequip() * 100d) / 100d, Math.round(salario * 100d) / 100d, button));
        }
        return renglonVarianteViewObservableList;
    }

    private Renglonvariante getRenglonVariante(int idR) {
        return renglonvarianteArrayList.parallelStream().filter(renglonvariante -> renglonvariante.getId() == idR).findFirst().orElse(null);
    }

    private double getSalrioInRV(Renglonvariante renglonvariante) {
        double salary = 0.0;
        for (Renglonrecursos renglonrecursos : renglonvariante.getRenglonrecursosById()) {
            if (renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")) {
                salary += renglonrecursos.getCantidas() * getImporteEscala(renglonrecursos.getRecursosByRecursosId().getGrupoescala(), renglonvariante.getRs());
            }
        }
        return salary;
    }

    private double getImporteEscala(String grupo, int idSal) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Salario salario = session.get(Salario.class, idSal);
            if (grupo.contentEquals("II")) {
                importe = salario.getIi();
            }
            if (grupo.contentEquals("III")) {
                importe = salario.getIii();
            }
            if (grupo.contentEquals("IV")) {
                importe = salario.getIv();
            }
            if (grupo.contentEquals("V")) {
                importe = salario.getV();
            }
            if (grupo.contentEquals("VI")) {
                importe = salario.getVi();
            }
            if (grupo.contentEquals("VII")) {
                importe = salario.getVii();
            }
            if (grupo.contentEquals("VIII")) {
                importe = salario.getViii();
            }
            if (grupo.contentEquals("IX")) {
                importe = salario.getIx();
            }
            if (grupo.contentEquals("X")) {
                importe = salario.getX();
            }
            if (grupo.contentEquals("XI")) {
                importe = salario.getXi();
            }
            if (grupo.contentEquals("XII")) {
                importe = salario.getXii();
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();

        }
        return importe;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dataTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && dataTable.getSelectionModel() != null) {
                Renglonvariante renglonvariante = getRenglonVariante(dataTable.getSelectionModel().getSelectedItem().getId());
                renVarianteToRenglonObraController.getRenglonVarianteFronHelp(renglonvariante);
                Stage stage = (Stage) box.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void loadDataRenglones() {
        code.setCellValueFactory(new PropertyValueFactory<RenglonVarianteSearchView, StringProperty>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<RenglonVarianteSearchView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<RenglonVarianteSearchView, StringProperty>("um"));
        costoMate.setCellValueFactory(new PropertyValueFactory<RenglonVarianteSearchView, DoubleProperty>("costomat"));
        costoMano.setCellValueFactory(new PropertyValueFactory<RenglonVarianteSearchView, DoubleProperty>("costmano"));
        costEquip.setCellValueFactory(new PropertyValueFactory<RenglonVarianteSearchView, DoubleProperty>("costequip"));
        refere.setCellValueFactory(new PropertyValueFactory<RenglonVarianteSearchView, JFXButton>("view"));

    }

    public void setRVDatos(RenVarianteToUnidadObraController renglonObraController, String pattern, int idObra) {
        renVarianteToRenglonObraController = renglonObraController;
        loadDataRenglones();
        renglonVarianteViewObservableList = FXCollections.observableArrayList();
        renglonVarianteViewObservableList = searchRenglonPattern(pattern, idObra);
        dataTable.getItems().setAll(renglonVarianteViewObservableList);

    }


}
