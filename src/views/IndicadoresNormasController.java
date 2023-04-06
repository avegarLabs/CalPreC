package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class IndicadoresNormasController implements Initializable {


    @FXML
    private Labeled label_title;

    @FXML
    private JFXTextField field_codigo;

    @FXML
    private TableView<TableIndicadorNorma> tableCodes;

    @FXML
    private TableColumn<TableIndicadorNorma, String> codeRV;

    @FXML
    private TableColumn<TableIndicadorNorma, String> descripRV;

    @FXML
    private TableColumn<TableIndicadorNorma, String> umRV;

    @FXML
    private JFXTextField field_code;

    @FXML
    private JFXComboBox<String> listResolt;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    @FXML
    private JFXButton btn_add;
    private int pertenece;
    private int batchSize = 25;
    private int count;
    private int inputLength = 6;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(utilCalcSingelton.getSalarios().parallelStream().filter(salario -> !salario.getDescripcion().equals("Resolución 30")).map(Salario::getDescripcion).collect(Collectors.toList()));
        listResolt.setItems(observableList);
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_code.getText().length() == inputLength) {
            codeRV.setCellValueFactory(new PropertyValueFactory<TableIndicadorNorma, String>("code"));
            descripRV.setCellValueFactory(new PropertyValueFactory<TableIndicadorNorma, String>("descrip"));
            umRV.setCellValueFactory(new PropertyValueFactory<TableIndicadorNorma, String>("um"));
            pertenece = 0;
            if (listResolt.getValue().equals("Resolución 86")) {
                pertenece = 2;
            } else if (listResolt.getValue().equals("Resolución 266")) {
                pertenece = 3;
            }
            Renglonvariante renglonvariante = utilCalcSingelton.renglonvarianteList.parallelStream().filter(item -> item.getCodigo().trim().equals(field_code.getText().trim()) && item.getRs() == pertenece).findFirst().get();
            if (renglonvariante != null) {
                tableCodes.getItems().add(new TableIndicadorNorma(renglonvariante.getCodigo(), renglonvariante.getDescripcion(), renglonvariante.getUm()));
                clearField();
            }
        }
    }

    public void clearField() {
        field_code.setText("");
    }


    public void addIndicador(ActionEvent event) {

        try {
            IndicadorGrup indicadorGrup = new IndicadorGrup();
            indicadorGrup.setDescipcion(field_codigo.getText());
            indicadorGrup.setPertenece(pertenece);

            int idIndicador = saveIndicador(indicadorGrup);

            List<NormasInd> normasIndList = new ArrayList<>();
            for (TableIndicadorNorma item : tableCodes.getItems()) {
                NormasInd normasInd = new NormasInd();
                normasInd.setCodenorna(item.getCode());
                normasInd.setIndcadorId(idIndicador);
                normasIndList.add(normasInd);
            }
            addCodesToIndicador(normasIndList);

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Indicadore creado satisfactoriamente!");
            alert.showAndWait();

            Stage stage = (Stage) btn_add.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear el indicadore");
            alert.showAndWait();
        }

    }

    public int saveIndicador(IndicadorGrup indicadorGrup) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idIndicador = null;
        try {
            tx = session.beginTransaction();
            idIndicador = (Integer) session.save(indicadorGrup);
            tx.commit();
            session.close();
            return idIndicador;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idIndicador;
    }

    public void addCodesToIndicador(List<NormasInd> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (NormasInd recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(recursos);
            }
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando los codigos");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }

    public void handleListIndicadores(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IndicadoresNormasLista.fxml"));
            Parent proot = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
