package views;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GruposEscalasEmpresaObraController implements Initializable {

    @FXML
    private Label tarifaLabel;
    @FXML
    private JFXTextField grupo;

    @FXML
    private JFXTextField valor;

    @FXML
    private TableView<Empresaobratarifa> tableGroup;

    @FXML
    private TableColumn<Empresaobratarifa, String> grupCode;

    @FXML
    private TableColumn<Empresaobratarifa, Double> tarifaV;

    @FXML
    private TableColumn<Empresaobratarifa, Double> grupValor;

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
    private JFXTextField porIncremento;
    private UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    @FXML
    private JFXTextField valIni;

    private String falg;
    private Empresaobratarifa selectedGrupo;
    private int grupoIndex;
    private TarifaSalarial tarifaSalarial;
    private int idObra;
    private int idEmp;
    private Obra obraModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        falg = "Save";
    }

    public void passDataToObra(Obra obra, Empresaconstructora empresaconstructora, TarifaSalarial tarifa) {
        idObra = obra.getId();
        idEmp = empresaconstructora.getId();
        tarifaSalarial = tarifa;
        obraModel = obra;
        tarifaLabel.setText("Tarifas de la Empresa: " + empresaconstructora.getCodigo());
        ObservableList<Empresaobratarifa> empresaobratarifaObservableList = FXCollections.observableArrayList();
        empresaobratarifaObservableList.addAll(utilCalcSingelton.getEmpresaobratarifaObservableList(obra.getId(), empresaconstructora.getId(), tarifa.getId()));
        tableGroup.setItems(empresaobratarifaObservableList);
        loadData();

        antiguedad.setText(String.valueOf(tarifa.getAntiguedad()));
        nomina.setText(String.valueOf(tarifa.getNomina()));
        vacaciones.setText(String.valueOf(tarifa.getVacaciones()));
        aEspecial.setText(String.valueOf(tarifa.getAutEspecial()));
        social.setText(String.valueOf(tarifa.getSegSocial()));
        if (empresaobratarifaObservableList.size() > 0) {
            Empresaobratarifa eot = empresaobratarifaObservableList.stream().collect(Collectors.toList()).get(0);
            if (eot.getIncremento() == null) {
                porIncremento.setText("0.0");
            } else {
                porIncremento.setText(String.valueOf(eot.getIncremento()));
            }
        } else {
            porIncremento.setText("0.0");
        }
    }

    private void loadData() {
        grupCode.setCellValueFactory(new PropertyValueFactory<Empresaobratarifa, String>("grupo"));
        tarifaV.setCellValueFactory(new PropertyValueFactory<Empresaobratarifa, Double>("escala"));
        grupValor.setCellValueFactory(new PropertyValueFactory<Empresaobratarifa, Double>("tarifa"));
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
        if (!porIncremento.getText().equals("0.0")) {
            double finalval = valTarifaIncrementada(valorTar);
            double porAE = Double.parseDouble(aEspecial.getText()) / 100 * finalval;
            double porAnt = Double.parseDouble(antiguedad.getText()) / 100 * finalval;
            double porVac = Double.parseDouble(vacaciones.getText()) / 100 * finalval;
            double porNom = Double.parseDouble(nomina.getText()) / 100 * finalval;
            double porSeg = Double.parseDouble(social.getText()) / 100 * finalval;
            double calculo = finalval + porAE + porAnt + porVac + porNom + porSeg;
            return new BigDecimal(String.format("%.2f", calculo)).doubleValue();
        } else {
            double porAE = Double.parseDouble(aEspecial.getText()) / 100 * valorTar;
            double porAnt = Double.parseDouble(antiguedad.getText()) / 100 * valorTar;
            double porVac = Double.parseDouble(vacaciones.getText()) / 100 * valorTar;
            double porNom = Double.parseDouble(nomina.getText()) / 100 * valorTar;
            double porSeg = Double.parseDouble(social.getText()) / 100 * valorTar;
            double calculo = valorTar + porAE + porAnt + porVac + porNom + porSeg;
            return new BigDecimal(String.format("%.2f", calculo)).doubleValue();
        }
    }

    public double valTarifaIncrementada(double tarval) {
        double val = 0.0;
        if (tarval > 0.0) {
            double porIncrementado = Double.parseDouble(porIncremento.getText()) / 100 * tarval;
            val = tarval + porIncrementado;
        }
        return new BigDecimal(String.format("%.2f", val)).doubleValue();
    }

    public void calcCostoTarifa(ActionEvent event) {
        double val = calcValorTarifa(Double.parseDouble(valIni.getText()));
        valor.setText(String.valueOf(val));
    }

    private double calcTarifaBaseInventoR15(double valor) {
        double porAE = Double.parseDouble(aEspecial.getText()) / 100;
        double porAnt = Double.parseDouble(antiguedad.getText()) / 100;
        double porVac = Double.parseDouble(vacaciones.getText()) / 100;
        double porNom = Double.parseDouble(nomina.getText()) / 100;
        double porSeg = Double.parseDouble(social.getText()) / 100;
        double sumatoria = porAE + porAnt + porVac + porNom + porSeg;
        double calcR = 1 + sumatoria;
        double calc = valor / calcR;
        return new BigDecimal(String.format("%.2f", calc)).doubleValue();
    }

    private void saveInTable(String group, String ini, String value) {
        Empresaobratarifa empresaobratarifa = new Empresaobratarifa();
        empresaobratarifa.setEmpresaconstructoraId(idEmp);
        empresaobratarifa.setObraId(idObra);
        empresaobratarifa.setTarifaId(tarifaSalarial.getId());
        empresaobratarifa.setGrupo(group);
        empresaobratarifa.setEscala(Double.parseDouble(ini));
        empresaobratarifa.setTarifa(Double.parseDouble(value));
        saveEmpresaObraTarifa(empresaobratarifa);
        empresaobratarifa.setIncremento(Double.parseDouble(porIncremento.getText()));
        tableGroup.getItems().add(empresaobratarifa);
        grupo.clear();
        valor.clear();
        valIni.clear();

    }

    private void updateIntable(String grupDes, String ini, String value) {
        var select = tableGroup.getItems().get(grupoIndex);
        select.setGrupo(grupDes);
        select.setEscala(Double.parseDouble(ini));
        select.setTarifa(Double.parseDouble(value));
        UpdateEmpresaObraTarifa(select);
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
        valIni.setText(String.valueOf(selectedGrupo.getEscala()));
        valor.setText(String.valueOf(selectedGrupo.getTarifa()));
    }

    public void handleRemoveToTable(ActionEvent event) {
        selectedGrupo = tableGroup.getSelectionModel().getSelectedItem();
        deleteEmpresaObraTarifa(selectedGrupo);
        tableGroup.getItems().remove(selectedGrupo);
        loadData();
    }

    public void saveEmpresaObraTarifa(Empresaobratarifa bajo) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(bajo);
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void UpdateEmpresaObraTarifa(Empresaobratarifa bajo) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(bajo);
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteEmpresaObraTarifa(Empresaobratarifa bajo) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bajo);
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void refreshEmpresaObraTarifa(ActionEvent event) {
        if (tableGroup.getColumns().size() > 0) {
            utilCalcSingelton.clearTarifaEmpresaObra(idObra, idEmp);
        }
        List<Empresaobratarifa> temp = new ArrayList<>();
        if (porIncremento.getText().equals("0.0")) {
            for (GruposEscalas gruposEscalas : tarifaSalarial.getGruposEscalasCollection()) {
                Empresaobratarifa empresaobratarifa = new Empresaobratarifa();
                empresaobratarifa.setEmpresaconstructoraId(idEmp);
                empresaobratarifa.setObraId(idObra);
                empresaobratarifa.setTarifaId(tarifaSalarial.getId());
                empresaobratarifa.setGrupo(gruposEscalas.getGrupo());
                empresaobratarifa.setEscala(gruposEscalas.getValorBase());
                if (obraModel.getSalarioBySalarioId().getTag().equals("T15")) {
                    double tarBaseReal = calcTarifaBaseInventoR15(gruposEscalas.getValor());
                    empresaobratarifa.setTarifa(calcValorTarifa(tarBaseReal));
                } else {
                    empresaobratarifa.setTarifa(calcValorTarifa(gruposEscalas.getValorBase()));
                }
                empresaobratarifa.setIncremento(Double.parseDouble(porIncremento.getText()));
                empresaobratarifa.setTarifaInc(gruposEscalas.getValorBase());
                temp.add(empresaobratarifa);
            }
        } else {
            for (GruposEscalas gruposEscalas : tarifaSalarial.getGruposEscalasCollection()) {
                Empresaobratarifa empresaobratarifa = new Empresaobratarifa();
                empresaobratarifa.setEmpresaconstructoraId(idEmp);
                empresaobratarifa.setObraId(idObra);
                empresaobratarifa.setTarifaId(tarifaSalarial.getId());
                empresaobratarifa.setGrupo(gruposEscalas.getGrupo());
                empresaobratarifa.setEscala(gruposEscalas.getValorBase());
                if (obraModel.getSalarioBySalarioId().getTag().equals("T15")) {
                    double tarBaseReal = calcTarifaBaseInventoR15(gruposEscalas.getValor());
                    empresaobratarifa.setTarifa(calcValorTarifa(tarBaseReal));
                } else {
                    empresaobratarifa.setTarifa(calcValorTarifa(gruposEscalas.getValorBase()));
                }
                empresaobratarifa.setIncremento(Double.parseDouble(porIncremento.getText()));
                empresaobratarifa.setTarifaInc(valTarifaIncrementada(gruposEscalas.getValorBase()));
                temp.add(empresaobratarifa);
            }
        }
        utilCalcSingelton.persistListEmpresaObraTarifa(temp);
        ObservableList<Empresaobratarifa> empresaobratarifaObservableList = FXCollections.observableArrayList();
        empresaobratarifaObservableList.addAll(utilCalcSingelton.getEmpresaobratarifaObservableList(idObra, idEmp, tarifaSalarial.getId()));
        tableGroup.setItems(empresaobratarifaObservableList);
        loadData();
    }


}
