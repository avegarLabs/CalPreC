package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TarifasSalarialUpdateController implements Initializable {
    public TarifaSalarial tarifa;

    @FXML
    private Label title;

    @FXML
    private JFXTextField codeTarifa;

    @FXML
    private JFXTextArea tarDescripcion;

    @FXML
    private JFXTextField moneda;

    @FXML
    private JFXTextField aEspecial;

    @FXML
    private JFXTextField antiguedad;

    @FXML
    private JFXTextField vacaciones;

    @FXML
    private JFXTextField nomina;

    @FXML
    private JFXTextField social;

    @FXML
    private JFXButton btnUpdate;
    private TarifasSalarialUpdateController tarifasSalarialUpdateController;

    private TarifaSalarialRepository tarifaSalarialRepository = TarifaSalarialRepository.getInstance();
    @FXML
    private JFXTextField meses;

    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    private ConfiguracionController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tarifasSalarialUpdateController = this;
    }

    public void setDatoToView(TarifaSalarial tarifaSalarial, ConfiguracionController configuracionController) {
        controller = configuracionController;
        tarifa = tarifaSalarial;
        title.setText("Actualizar tarifa: " + tarifaSalarial.getCode());
        codeTarifa.setText(tarifaSalarial.getCode());
        tarDescripcion.setText(tarifaSalarial.getDescripcion());
        moneda.setText(tarifaSalarial.getMoneda());
        aEspecial.setText(String.valueOf(tarifaSalarial.getAutEspecial()));
        nomina.setText(String.valueOf(tarifaSalarial.getNomina()));
        vacaciones.setText(String.valueOf(tarifaSalarial.getVacaciones()));
        antiguedad.setText(String.valueOf(tarifaSalarial.getAntiguedad()));
        social.setText(String.valueOf(tarifaSalarial.getSegSocial()));
//        meses.setText(String.valueOf(tarifaSalarial.getHoras()));
    }

    public void updateTarifaValues(ActionEvent event) {
        try {
            tarifa.setCode(codeTarifa.getText());
            tarifa.setDescripcion(tarDescripcion.getText());
            tarifa.setMoneda(moneda.getText());
            tarifa.setNomina(Double.parseDouble(nomina.getText()));
            tarifa.setAntiguedad(Double.parseDouble(antiguedad.getText()));
            tarifa.setAutEspecial(Double.parseDouble(aEspecial.getText()));
            tarifa.setVacaciones(Double.parseDouble(vacaciones.getText()));
            tarifa.setSegSocial(Double.parseDouble(social.getText()));
            //tarifa.setHoras(Double.parseDouble(meses.getText()));
            tarifaSalarialRepository.updateTarifa(tarifa);
            // tarifaSalarialRepository.updateTarConceptosTarifa(tarifa.getTarConceptosCollection().stream().collect(Collectors.toList()));
            reclalcularCosto(tarifa.getGruposEscalasCollection().stream().collect(Collectors.toList()));
            utilCalcSingelton.showAlert("Datos Actualizados Correctamente!!!", 1);

            controller.datosSalarios();
            controller.showGruposEscalasAction(tarifa);
            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            utilCalcSingelton.showAlert("Ocurrio un error al actualizar los datos, por favor revise!!!", 2);
        }
    }

    private void reclalcularCosto(List<GruposEscalas> gruposEscalasList) {
        List<GruposEscalas> calulateList = new ArrayList<>();
        for (GruposEscalas gruposEscalas : gruposEscalasList) {
            double newCosto = calcValorTarifa(gruposEscalas);
            gruposEscalas.setValorBase(newCosto);
            //     double tarifaHH = new BigDecimal(String.format("%.2f", newCosto / tarifa.getHoras())).doubleValue();
            //      gruposEscalas.setValor(tarifaHH);
            calulateList.add(gruposEscalas);
        }
        tarifaSalarialRepository.updateAllEscalas(gruposEscalasList);
    }

    private double calcValorTarifa(GruposEscalas gruposEscalas) {
        double costoTarifa = tarifa.getTarConceptosCollection().parallelStream().map(TarConceptos::getValor).reduce(0.0, Double::sum);
        //   double costoConceptsGrupo = gruposEscalas.getTarConceptosCollection().parallelStream().map(GrupoEscalaConceptos::getValor).reduce(0.0, Double::sum);
        //  return new BigDecimal(String.format("%.2f", costoTarifa + costoConceptsGrupo)).doubleValue();
        /*
        double porAE = Double.parseDouble(aEspecial.getText()) / 100 * valorTar;
        double porAnt = Double.parseDouble(antiguedad.getText()) / 100 * valorTar;
        double porVac = Double.parseDouble(vacaciones.getText()) / 100 * valorTar;
        double porNom = Double.parseDouble(nomina.getText()) / 100 * valorTar;
        double porSeg = Double.parseDouble(social.getText()) / 100 * valorTar;
        double calculo = valorTar + porAE + porAnt + porVac + porNom + porSeg;
        return new BigDecimal(String.format("%.2f", calculo)).doubleValue();
        */

        return 0.0;

    }

    public void actionShowConceptsList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConceptosTarifaUpdate.fxml"));
            Parent proot = loader.load();

            ConceptTarifasSalarialUpdateController controller = (ConceptTarifasSalarialUpdateController) loader.getController();
            controller.passDataToConfiguracion(tarifasSalarialUpdateController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addElementsToListConcepts(ObservableList<TarConceptos> list) {
        tarifa.getTarConceptosCollection().clear();
        tarifa.setTarConceptosCollection(list);
    }


}
