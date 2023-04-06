package views;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RecalculraRVController implements Initializable {

    private static List<Especialidades> especialidadesList;
    private static List<Empresaconstructora> empresaconstructoraList;

    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    @FXML
    private JFXComboBox<String> listEspecialidades;
    @FXML
    private JFXComboBox<String> empresasList;
    @FXML
    private JFXComboBox<String> zonaslist;
    @FXML
    private JFXComboBox<String> objetoList;
    private ObservableList<String> especialidadesString;
    private ObservableList<String> empresacontObservableList;
    private ObservableList<String> zonacomboList;
    private ObservableList<String> objetoscomboList;
    private Obra obra;
    private Zonas zonas;

    public void pasarDatos(Obra obraSend) {

        obra = obraSend;
        empresaconstructoraList = new ArrayList<>();
        empresaconstructoraList.addAll(obra.getEmpresaobrasById().parallelStream().map(Empresaobra::getEmpresaconstructoraByEmpresaconstructoraId).collect(Collectors.toList()));

        empresacontObservableList = FXCollections.observableArrayList();
        empresacontObservableList.addAll(empresaconstructoraList.parallelStream().map(empresaconstructora -> empresaconstructora.getCodigo() + " - " + empresaconstructora.getDescripcion()).collect(Collectors.toList()));
        empresasList.setItems(empresacontObservableList);

        especialidadesList = new ArrayList<>();
        especialidadesList = utilCalcSingelton.getAllEspecialidades();

        zonacomboList = FXCollections.observableArrayList();
        zonacomboList.addAll(obra.getZonasById().parallelStream().map(item -> item.getCodigo() + " - " + item.getDesripcion()).collect(Collectors.toList()));
        zonaslist.setItems(zonacomboList);

        especialidadesString = FXCollections.observableArrayList();
        especialidadesString.addAll(especialidadesList.parallelStream().map(item -> item.getCodigo() + " - " + item.getDescripcion()).collect(Collectors.toList()));
        listEspecialidades.setItems(especialidadesString);
    }

    public void handleLLenarObjetos(ActionEvent event) {
        String[] partZonas = zonaslist.getValue().split(" - ");
        zonas = obra.getZonasById().parallelStream().filter(item -> item.getCodigo().trim().equals(partZonas[0])).findFirst().get();

        objetoscomboList = FXCollections.observableArrayList();
        objetoscomboList.addAll(zonas.getObjetosById().parallelStream().map(item -> item.getCodigo() + " - " + item.getDescripcion()).collect(Collectors.toList()));
        objetoList.setItems(objetoscomboList);
    }

    public void handleCalcular(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recalcular Obra");
        alert.setContentText("CalPreC recalculará los valores para la obra: " + obra.getDescripion());
        alert.showAndWait();
        try {

            /*
            String[] partSub = listEspecialidades.getValue().split(" - ");
            int idEsp = especialidadesList.parallelStream().filter(item -> item.getCodigo().trim().equals(partSub[0].trim())).map(Especialidades::getId).findFirst().get();
*/
            String[] partEmp = empresasList.getValue().split(" - ");
            int idEmp = empresaconstructoraList.parallelStream().filter(empresaconstructora -> empresaconstructora.getCodigo().trim().equals(partEmp[0].trim())).findFirst().map(Empresaconstructora::getId).get();

            /*
            String partObjetos[] = objetoList.getValue().split(" - ");
            int idObj = zonas.getObjetosById().parallelStream().filter(item -> item.getCodigo().trim().equals(partObjetos[0].trim())).findFirst().map(Objetos::getId).get();
         */
            // utilCalcSingelton.setUpdateValoresRenglones(obra.getNivelespecificosById().parallelStream().filter(item -> item.getEspecialidadesId() == idEsp && item.getEmpresaconstructoraId() == idEmp && item.getZonasId() == zonas.getId() && item.getObjetosId() == idObj).collect(Collectors.toList()));
            utilCalcSingelton.setUpdateValoresRenglones(obra.getNivelespecificosById().parallelStream().filter(item -> item.getEmpresaconstructoraId() == idEmp).collect(Collectors.toList()));


            /*
            List<Certificacionrecuo> list = new ArrayList<>();
            list = utilCalcSingelton.getAllCertificacionesRec(obra.getId());
            list.size();

            List<Planrecuo> listPlan = new ArrayList<>();
            listPlan = utilCalcSingelton.getAllPlanifuoRv(obra.getId());
            listPlan.size();

            List<Unidadobra> unidadobraList = utilCalcSingelton.getUnidadobrasList(obra.getId(), idEmp);
            utilCalcSingelton.setUpdateValoresCertPlan(unidadobraList.parallelStream().filter(item -> item.getEspecialidadesId() == idEsp && item.getZonasId() == zonas.getId() && item.getObjetosId() == idObj).collect(Collectors.toList()));
*/
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Recalcular Obra");
            alert.setContentText("Valores de la obra: " + obra.getDescripion() + " calculados");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al recalcular la obra");
            alert.setContentText("Error : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
