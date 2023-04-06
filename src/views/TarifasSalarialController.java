package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class TarifasSalarialController implements Initializable {

    public ObservableList<TarConceptos> tarConceptosList;
    public ObservableList<TarConceptos> tarConceptosListGrupo2;
    public ObservableList<TarConceptos> tarConceptosListGrupo3;
    public ObservableList<GrupoEscalaConceptos> grupoEscalaConceptos;
    @FXML
    public JFXTextField vacaciones;
    TarifasSalarialController tarifasSalarialController;
    @FXML
    private JFXTextField codeTarifa;
    @FXML
    private JFXTextArea tarDescripcion;
    @FXML
    private JFXTextField moneda;
    @FXML
    private JFXTextField grupo;
    @FXML
    private JFXTextField valor;
    @FXML
    private TableView<GruposEscalas> tableGroup;
    @FXML
    private TableColumn<GruposEscalas, String> grupCode;
    @FXML
    private TableColumn<GruposEscalas, Double> tarifa;
    @FXML
    private TableColumn<GruposEscalas, Double> grupValor;
    @FXML
    private JFXTextField aEspecial;
    @FXML
    private JFXTextField antiguedad;
    @FXML
    private JFXTextField nomina;
    @FXML
    private JFXTextField social;
    @FXML
    private JFXTextField valIni;
    @FXML
    private JFXButton btnUpdate;
    private TarifaSalarialRepository tarifaSalarialRepository = TarifaSalarialRepository.getInstance();
    private GrupoEscalaRepository grupoEscalaRepository = GrupoEscalaRepository.getInstance();
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private String falg;
    private GruposEscalas selectedGrupo;
    private int grupoIndex;
    private ConfiguracionController configuracion;
    @FXML
    private JFXTextField meses;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        falg = "Save";
        social.setText("0.0");
        aEspecial.setText("0.0");
        antiguedad.setText("0.0");
        nomina.setText("0.0");
        vacaciones.setText("0.0");
        meses.setText("0");
        tarifasSalarialController = this;

    }

    public void addElementsToListConcepts(ObservableList<TarConceptos> list) {
        tarConceptosList = FXCollections.observableArrayList();
        tarConceptosList.addAll(list);
    }

    public void addDataToFieldsForm(double val, ObservableList<TarConceptos> listGeneral1, ObservableList<TarConceptos> listGeneral2) {
        tarConceptosListGrupo2 = FXCollections.observableArrayList();
        tarConceptosListGrupo3 = FXCollections.observableArrayList();
        tarConceptosListGrupo2.addAll(listGeneral1);
        tarConceptosListGrupo3.addAll(listGeneral2);
        valIni.setText(String.valueOf(new BigDecimal(String.format("%.2f", val)).doubleValue()));
    }

    public void passDataToConfiguracion(ConfiguracionController configuracionController) {
        configuracion = configuracionController;
    }

    public void createNewTarifaAction(ActionEvent event) {
        if (!codeTarifa.getText().isEmpty() && !moneda.getText().isEmpty() && !tarDescripcion.getText().isEmpty()) {
            TarifaSalarial tarifaSalarial = new TarifaSalarial();
            tarifaSalarial.setCode(codeTarifa.getText());
            tarifaSalarial.setDescripcion(tarDescripcion.getText());
            tarifaSalarial.setMoneda(moneda.getText());
            tarifaSalarial.setNomina(Double.parseDouble(nomina.getText()));
            tarifaSalarial.setAntiguedad(Double.parseDouble(antiguedad.getText()));
            tarifaSalarial.setAutEspecial(Double.parseDouble(aEspecial.getText()));
            tarifaSalarial.setVacaciones(Double.parseDouble(vacaciones.getText()));
            tarifaSalarial.setSegSocial(Double.parseDouble(social.getText()));
            //  tarifaSalarial.setHoras(Double.parseDouble(meses.getText()));

            int idTarifa = tarifaSalarialRepository.addNuevaTarifa(tarifaSalarial);
            createListOfGrupos(idTarifa);
            //  createListOfConceptos(idTarifa, tarConceptosList);
            utilCalcSingelton.showAlert("Tarifa: " + codeTarifa.getText() + " creado correctamente!!!", 1);

            configuracion.datosSalarios();
            configuracion.showGruposEscalasAction(tarifaSalarialRepository.getTarifaById(idTarifa));
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();

        } else {
            utilCalcSingelton.showAlert("Complete los campos del fomulario para crear la nueva Tarifa Salarial!!!", 2);
        }
    }

    private void createListOfConceptos(int idTarifa, ObservableList<TarConceptos> tarConceptosList) {
        List<TarConceptos> tarConceptos = new ArrayList<>();
        for (TarConceptos conceptos : tarConceptosList) {
            conceptos.setTarifaId(idTarifa);
            tarConceptos.add(conceptos);
        }
        tarifaSalarialRepository.saveAllConceptsCalc(tarConceptosList);
    }

    private void loadData() {
        grupCode.setCellValueFactory(new PropertyValueFactory<GruposEscalas, String>("grupo"));
        tarifa.setCellValueFactory(new PropertyValueFactory<GruposEscalas, Double>("valorBase"));
        grupValor.setCellValueFactory(new PropertyValueFactory<GruposEscalas, Double>("valor"));
    }

    public void addGrupoToList(ActionEvent event) {
        if (!grupo.getText().isEmpty() && !valor.getText().isEmpty() && !valIni.getText().isEmpty()) {
            if (falg.equals("Save")) {
                saveInTable(grupo.getText(), valIni.getText(), valor.getText());
                loadData();
            } else if (falg.equals("Update")) {
                updateIntable(grupo.getText(), valIni.getText(), valor.getText());
                loadData();
            }
        } else {
            utilCalcSingelton.showAlert("Complete los campos del fomulario para crear los grupos de la tarifa salarial!!!", 2);
        }
    }

    private double calcValorTarifa(double valorTar) {
        double calculo = 0.0;
        double porAE = 0.0;
        double porAnt = 0.0;
        double porVac = 0.0;
        double porNom = 0.0;
        double porSeg = 0.0;
        if (!aEspecial.getText().equals("0.0")) {
            porAE = Double.parseDouble(aEspecial.getText()) / 100 * valorTar;
        }
        if (!antiguedad.getText().equals("0.0")) {
            porAnt = Double.parseDouble(antiguedad.getText()) / 100 * valorTar;
        }
        if (!vacaciones.getText().equals("0.0")) {
            porVac = Double.parseDouble(vacaciones.getText()) / 100 * valorTar;
        }
        if (nomina.getText().equals("0.0")) {
            porNom = Double.parseDouble(nomina.getText()) / 100 * valorTar;
        }
        if (social.getText().equals("0.0")) {
            porSeg = Double.parseDouble(social.getText()) / 100 * valorTar;
        }
        if (!meses.getText().equals("0")) {
            double v1 = valorTar + porAE + porAnt + porVac + porNom + porSeg;
            calculo = v1 / Double.parseDouble(meses.getText());
        } else {
            calculo = valorTar + porAE + porAnt + porVac + porNom + porSeg;
        }
        return new BigDecimal(String.format("%.2f", calculo)).doubleValue();
    }

    private double sumOthersConpts(double val) {
        double valFinal = 0.0;
        for (TarConceptos tarConceptos : tarConceptosList) {
            valFinal += tarConceptos.getValor();
        }
        return valFinal;
    }


    public void calcCostoTarifa(ActionEvent event) {
        System.out.println(valIni.getText());
        double val = calcValorTarifa(Double.parseDouble(valIni.getText()));
        valor.setText(String.valueOf(val));
    }

    private void saveInTable(String group, String ini, String value) {
        GruposEscalas gruposEscalas = new GruposEscalas();
        gruposEscalas.setGrupo(group);
        gruposEscalas.setValorBase(Double.parseDouble(ini));
        gruposEscalas.setValor(Double.parseDouble(value));
        int index = tableGroup.getItems().size() + 1;
        //  gruposEscalas.setTarConceptosCollection(createListGrupo(index));
        gruposEscalas.setTarifaId(1);
        tableGroup.getItems().add(gruposEscalas);
        grupo.clear();
        valor.clear();
        valIni.clear();

    }

    private Collection<GrupoEscalaConceptos> createListGrupo(int index) {
        List<GrupoEscalaConceptos> tempListG = new ArrayList<>();
        tempListG.addAll(createListGrupoConceptos(index, tarConceptosListGrupo2, 2));
        tempListG.addAll(createListGrupoConceptos(index, tarConceptosListGrupo3, 3));
        tarConceptosListGrupo2 = FXCollections.observableArrayList();
        tarConceptosListGrupo3 = FXCollections.observableArrayList();
        return tempListG;
    }

    private List<GrupoEscalaConceptos> createListGrupoConceptos(int index, List<TarConceptos> tarConceptos, int type) {
        List<GrupoEscalaConceptos> tempList = new ArrayList<>();
        for (TarConceptos tarConce : tarConceptos) {
            GrupoEscalaConceptos grupoEscalaConceptos = new GrupoEscalaConceptos();
            grupoEscalaConceptos.setGrupoId(index);
            grupoEscalaConceptos.setConceptoId(tarConce.getId());
            grupoEscalaConceptos.setValor(tarConce.getValor());
            grupoEscalaConceptos.setTipo(type);
            tempList.add(grupoEscalaConceptos);
        }
        return tempList;
    }

    private void updateIntable(String grupDes, String ini, String value) {
        var select = tableGroup.getItems().get(grupoIndex);
        select.setGrupo(grupDes);
        select.setValorBase(Double.parseDouble(ini));
        select.setValor(Double.parseDouble(value));
        // select.getTarConceptosCollection().clear();
        // select.setTarConceptosCollection(createListGrupo(grupoIndex));
        tableGroup.getItems().set(grupoIndex, select);
        grupo.clear();
        valor.clear();
        valIni.clear();
        falg = "Save";
    }

    public void updateGrupoIntable(ActionEvent event) {
        falg = "Update";
        selectedGrupo = tableGroup.getSelectionModel().getSelectedItem();
        grupoIndex = tableGroup.getSelectionModel().getSelectedIndex();
        grupo.setText(selectedGrupo.getGrupo());
        valIni.setText(String.valueOf(selectedGrupo.getValorBase()));
        valor.setText(String.valueOf(selectedGrupo.getValor()));
    }

    public void handleRemoveToTable(ActionEvent event) {
        selectedGrupo = tableGroup.getSelectionModel().getSelectedItem();
        tableGroup.getItems().remove(selectedGrupo);
        loadData();
    }

    private void createListOfGrupos(int idTarifa) {
        List<GruposEscalas> gruposEscalasList = new ArrayList<>();
        for (GruposEscalas item : tableGroup.getItems()) {
            item.setTarifaId(idTarifa);
            gruposEscalasList.add(item);
        }
        tarifaSalarialRepository.saveAllEscalas(gruposEscalasList);
    }

    private Collection<? extends GrupoEscalaConceptos> updateGruposEscalasConceptos(int idGrupo, Collection<GrupoEscalaConceptos> tarConceptosCollection) {
        List<GrupoEscalaConceptos> modifiedGruposEscalasList = new ArrayList<>();
        for (GrupoEscalaConceptos escalaConceptos : tarConceptosCollection) {
            escalaConceptos.setGrupoId(idGrupo);
            modifiedGruposEscalasList.add(escalaConceptos);
        }
        return modifiedGruposEscalasList;
    }

    public void actionShowConceptsList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConceptosTarifa.fxml"));
            Parent proot = loader.load();

            ConceptTarifasSalarialController controller = (ConceptTarifasSalarialController) loader.getController();
            controller.passDataToConfiguracion(tarifasSalarialController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private double getValTarPais() {
        return tarConceptosList.parallelStream().map(TarConceptos::getValor).reduce(0.0, Double::sum);

    }

    public void actionShowGrupoConceptsList(ActionEvent event) {
        if (tarConceptosList != null) {
            double valor = getValTarPais();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ConceptosTarifaBase.fxml"));
                Parent proot = loader.load();

                ConceptTarifasSalarialBaseController controller = (ConceptTarifasSalarialBaseController) loader.getController();
                if (falg.equals("Save")) {
                    controller.passDataToConfiguracion(valor, tarifasSalarialController, null);
                } else if (falg.equals("Update")) {
                    controller.passDataToConfiguracion(valor, tarifasSalarialController, selectedGrupo);
                }

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            utilCalcSingelton.showAlert("Complete los datos de los costos para el país objetivo", 2);
        }

    }


}
