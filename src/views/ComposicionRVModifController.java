package views;

import Reports.BuildDynamicReport;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ComposicionRVModifController implements Initializable {

    private static SessionFactory sf;
    private static double valor;
    private static int inputLength = 6;
    UtilCalcSingelton reportProjectStructureSingelton = UtilCalcSingelton.getInstance();
    TarifaSalarialRepository tarifaSalarialRepository = TarifaSalarialRepository.getInstance();
    ReportProjectStructureSingelton reportProjectStructure = ReportProjectStructureSingelton.getInstance();
    @FXML
    private Label label_title;
    @FXML
    private TableView<ComponentesRV> dataTable;
    @FXML
    private TableColumn<ComponentesRV, String> code;
    @FXML
    private Label insumodescrip;
    @FXML
    private Label insumoum;
    @FXML
    private Label insumomn;
    @FXML
    private Label isumomlc;
    @FXML
    private JFXTextField labelUM;
    @FXML
    private JFXTextField labelCostoMat;
    @FXML
    private JFXTextField labelCostoMano;
    @FXML
    private JFXTextField labelCostoEquipo;
    @FXML
    private JFXTextArea labelDescrip;
    @FXML
    private JFXTextField fieldCodeJP;
    @FXML
    private Pane paneForm;
    @FXML
    private JFXTextField fieldCodigo;
    @FXML
    private JFXTextArea textDescrip;
    @FXML
    private JFXTextField fieldCantidad;
    @FXML
    private JFXButton btn_hecho;
    @FXML
    private JFXButton btn_update;
    @FXML
    private JFXTextField fieldUsos;
    @FXML
    private JFXTextField labelTotal;
    @FXML
    private JFXTextField labelHH;
    @FXML
    private JFXTextField labelHE;
    @FXML
    private Renglonvariante renglon;
    @FXML
    private Label valorInsumo;
    @FXML
    private JFXTextArea especifi;
    @FXML
    private JFXButton btn_print;
    @FXML
    private JFXComboBox<String> comboTarifas;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ArrayList<Renglonsemielaborados> renglonsemielaboradosArrayList;
    private ArrayList<Renglonjuego> renglonjuegoArrayList;
    @FXML
    private TableColumn<ComponentesRV, Double> cantidad;
    @FXML
    private TableColumn<ComponentesRV, Double> usos;
    private ObservableList<ComponentesRV> suministrosRenglonViews;
    private ArrayList<Recursos> arraySuministrosList;
    private ArrayList<Suministrossemielaborados> arraySuministrossemielaboradosList;
    private ArrayList<Juegoproducto> arrayJuegoproductoArrayList;
    private ComponentesRV componente;
    private ObservableList<ComponentesRV> observableGlobalList;
    private boolean flag;
    private int index;
    private Boolean check;
    private ObservableList<ComponentesRV> datos;
    private Integer subGrupo;
    private Double costoMaterial;
    private Double costoEquipo;
    private Double costoMano;
    private Double hh;
    private Double he;
    private Integer idRenglon;
    private List<RCreportModel> rCreportModelList;
    private ComponentesRV tempSuminitro;
    private Salario RS;
    private ObservableList<TarifaSalarial> tarifasList = FXCollections.observableArrayList();
    private ObservableList<String> tarifasCodeList = FXCollections.observableArrayList();
    private TarifaSalarial tarifaSalarial;
    private BuildDynamicReport bdr;
    @FXML
    private ContextMenu opciones;

    /**
     * Metodo para obtener todos los datos del renglon variante especificado
     *
     * @param id
     * @return
     */

    public Renglonvariante getRenglonVariante(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            renglon = session.get(Renglonvariante.class, id);

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return renglon;
    }

    public void handleLLenarTextarea(ActionEvent event) {
        String[] part = fieldCodigo.getText().split(" / ");
        ComponentesRV suministro = observableGlobalList.parallelStream().filter(item -> item.getCodigo().trim().equals(part[0].trim())).findFirst().get();
        fieldCodigo.clear();
        fieldCodigo.setText(part[0]);
        textDescrip.setText(suministro.getDescripcion());
        if (suministro.getTipo().trim().equals("2")) {
            double valor = getValosManoByTarifa(suministro.getTarifa());
            valorInsumo.setText(String.valueOf(valor) + " / " + suministro.getUm());
        } else {
            valorInsumo.setText(String.valueOf(suministro.getPreciomn()) + " / " + suministro.getUm());
        }
        tempSuminitro = suministro;

    }

    public void deleteComponentRC(int idRv, int idC) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            int opt1 = session.createQuery("DELETE FROM Renglonrecursos WHERE renglonvarianteId =:idR AND recursosId =:idRC").setParameter("idR", idRv).setParameter("idRC", idC).executeUpdate();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void deleteComponentRJ(int idRv, int idC) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            int opt1 = session.createQuery("DELETE FROM Renglonjuego WHERE renglonvarianteId =:idR AND juegoproductoId =:idRC").setParameter("idR", idRv).setParameter("idRC", idC).executeUpdate();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void deleteComponentRS(int idRv, int idC) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            int opt1 = session.createQuery("DELETE FROM Renglonsemielaborados WHERE renglonvarianteId =:idR AND suministrossemielaboradosId=:idRC").setParameter("idR", idRv).setParameter("idRC", idC).executeUpdate();

            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    private double getValosManoByTarifa(String tarifa) {
        double valorTarifa = 0.0;
        try {
            valorTarifa = tarifaSalarial.getGruposEscalasCollection().parallelStream().filter(item -> item.getGrupo().trim().equals(tarifa.trim())).findFirst().map(GruposEscalas::getValor).get();
        } catch (Exception e) {
            reportProjectStructureSingelton.showAlert("No existe valor para el grupo escala seleccionado en la tarifa: " + comboTarifas.getValue(), 2);
        }
        return valorTarifa;
    }

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    public ObservableList<ComponentesRV> getComponentesRenglon(Renglonvariante renglonvariante) {
        suministrosRenglonViews = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            /**
             * Capturo los recursos que componen al renglon variante
             */
            for (Renglonrecursos renglonrecursos : renglonvariante.getRenglonrecursosById()) {
                suministrosRenglonViews.add(new ComponentesRV(renglonrecursos.getRecursosByRecursosId().getId(), renglonrecursos.getRecursosByRecursosId().getCodigo(), renglonrecursos.getRecursosByRecursosId().getDescripcion(), renglonrecursos.getRecursosByRecursosId().getUm(), renglonrecursos.getRecursosByRecursosId().getPreciomn(), renglonrecursos.getCantidas(), renglonrecursos.getUsos(), "precons", renglonrecursos.getRecursosByRecursosId().getTipo(), renglonrecursos.getRecursosByRecursosId().getPeso(), renglonrecursos.getRecursosByRecursosId().getGrupoescala()));
            }
            /**
             * Capturo los suministros compuestos que componen al renglon variante
             *
             */
            for (Renglonsemielaborados renglonsemielaborados : renglonvariante.getRenglonsemielaboradosById()) {
                suministrosRenglonViews.add(new ComponentesRV(renglonsemielaborados.getSuministrossemielaboradosBySuministrossemielaboradosId().getId(), renglonsemielaborados.getSuministrossemielaboradosBySuministrossemielaboradosId().getCodigo(), renglonsemielaborados.getSuministrossemielaboradosBySuministrossemielaboradosId().getDescripcion(), renglonsemielaborados.getSuministrossemielaboradosBySuministrossemielaboradosId().getUm(), renglonsemielaborados.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn(), renglonsemielaborados.getCantidad(), renglonsemielaborados.getUsos(), "precons", "S", renglonsemielaborados.getSuministrossemielaboradosBySuministrossemielaboradosId().getPeso(), "I"));
            }

            for (Renglonjuego renglonjuego : renglonvariante.getRenglonjuegosById()) {
                suministrosRenglonViews.add(new ComponentesRV(renglonjuego.getJuegoproductoByJuegoproductoId().getId(), renglonjuego.getJuegoproductoByJuegoproductoId().getCodigo(), renglonjuego.getJuegoproductoByJuegoproductoId().getDescripcion(), renglonjuego.getJuegoproductoByJuegoproductoId().getUm(), renglonjuego.getJuegoproductoByJuegoproductoId().getPreciomn(), renglonjuego.getCantidad(), renglonjuego.getUsos(), "precons", "J", renglonjuego.getJuegoproductoByJuegoproductoId().getPeso(), "I"));
            }

            tx.commit();
            session.close();
            return suministrosRenglonViews;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void loadComponetes() {

        code.setCellValueFactory(new PropertyValueFactory<ComponentesRV, String>("codigo"));
        code.setPrefWidth(100);
        cantidad.setCellValueFactory(new PropertyValueFactory<ComponentesRV, Double>("cantidad"));
        cantidad.setPrefWidth(80);
        usos.setCellValueFactory(new PropertyValueFactory<ComponentesRV, Double>("uso"));
        usos.setPrefWidth(50);

    }

    public ObservableList<ComponentesRV> getAllSuminitros(int salario) {
        observableGlobalList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (salario == 1) {
                arraySuministrosList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece !='R266'").list();
                for (Recursos rec : arraySuministrosList) {
                    observableGlobalList.add(new ComponentesRV(rec.getId(), rec.getCodigo(), rec.getDescripcion(), rec.getUm(), rec.getPreciomn(), 0.0, 0.0, "precons", rec.getTipo(), rec.getPeso(), rec.getGrupoescala()));
                }
                arraySuministrossemielaboradosList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados WHERE pertenec !='R266' ").list();
                for (Suministrossemielaborados sum1 : arraySuministrossemielaboradosList) {
                    observableGlobalList.add(new ComponentesRV(sum1.getId(), sum1.getCodigo(), sum1.getDescripcion(), sum1.getUm(), sum1.getPreciomn(), 0.0, 0.0, "precons", "S", sum1.getPeso(), "I"));
                }
                arrayJuegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto WHERE pertenece !='R266'").list();
                for (Juegoproducto juegoproducto : arrayJuegoproductoArrayList) {
                    observableGlobalList.add(new ComponentesRV(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getUm(), juegoproducto.getPreciomn(), 0.0, 0.0, "precons", "J", juegoproducto.getPeso(), "I"));
                }
            } else if (salario >= 2) {
                arraySuministrosList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE pertenece !='I'").list();
                for (Recursos rec : arraySuministrosList) {
                    observableGlobalList.add(new ComponentesRV(rec.getId(), rec.getCodigo(), rec.getDescripcion(), rec.getUm(), rec.getPreciomn(), 0.0, 0.0, "precons", rec.getTipo(), rec.getPeso(), rec.getGrupoescala()));
                }
                arraySuministrossemielaboradosList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM  Suministrossemielaborados WHERE pertenec !='I' ").list();
                for (Suministrossemielaborados sum1 : arraySuministrossemielaboradosList) {
                    observableGlobalList.add(new ComponentesRV(sum1.getId(), sum1.getCodigo(), sum1.getDescripcion(), sum1.getUm(), sum1.getPreciomn(), 0.0, 0.0, "precons", "S", sum1.getPeso(), "I"));
                }
                arrayJuegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto  WHERE pertenece !='I'").list();
                for (Juegoproducto juegoproducto : arrayJuegoproductoArrayList) {
                    observableGlobalList.add(new ComponentesRV(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getUm(), juegoproducto.getPreciomn(), 0.0, 0.0, "precons", "J", juegoproducto.getPeso(), "I"));
                }
            }
            tx.commit();
            session.close();
            return observableGlobalList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void keyTypeCode(KeyEvent event) {
        if (fieldCodeJP.getText().length() == inputLength) {
            labelDescrip.requestFocus();
        }
    }

    public void showFormToInsert(ActionEvent event) {
        paneForm.setVisible(true);
        btn_update.setDisable(true);
        btn_hecho.setDisable(false);

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();
        valorInsumo.setText(" ");
    }

    public void showFormToUpdate() {
        paneForm.setVisible(true);
        btn_hecho.setDisable(true);
        btn_update.setDisable(false);

        index = dataTable.getSelectionModel().getSelectedIndex();
        componente = dataTable.getSelectionModel().getSelectedItem();
        fieldCodigo.setText(componente.getCodigo());
        textDescrip.setText(componente.getDescripcion());
        fieldCantidad.setText(String.valueOf(componente.getCantidad()));
        fieldUsos.setText(String.valueOf(componente.getUso()));
        if (componente.getTipo().trim().equals("2")) {
            valorInsumo.setText(String.valueOf(getValosManoByTarifa(componente.getTarifa())) + " / " + componente.getUm());
        } else {
            valorInsumo.setText(String.valueOf(componente.getPreciomn()) + " / " + componente.getUm());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                insumodescrip.setText(dataTable.getSelectionModel().getSelectedItem().getDescripcion());
                insumoum.setText(dataTable.getSelectionModel().getSelectedItem().getUm());
                if (dataTable.getSelectionModel().getSelectedItem().getTipo().trim().equals("2")) {
                    insumomn.setText(String.valueOf(getValosManoByTarifa(dataTable.getSelectionModel().getSelectedItem().getTarifa().trim())));
                } else {
                    insumomn.setText(String.valueOf(dataTable.getSelectionModel().getSelectedItem().getPreciomn()));
                }
            }
        });
    }

    public void LlenarDatos(RenglonVarianteView renglonvariante, Usuarios usuarios, Salario salario) {

        RS = salario;
        renglon = getRenglonVariante(renglonvariante.getId());
        Double costo = renglon.getCostomat() + renglon.getCostmano() + renglon.getCostequip();
        fieldCodeJP.setText(renglon.getCodigo());
        labelDescrip.setText(renglon.getDescripcion());
        labelUM.setText(renglon.getUm());
        labelCostoMat.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getCostomat())).doubleValue()));
        labelCostoMano.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getCostmano())).doubleValue()));
        labelCostoEquipo.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getCostequip())).doubleValue()));
        labelTotal.setText(Double.toString(new BigDecimal(String.format("%.2f", costo)).doubleValue()));
        labelHH.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getHh())).doubleValue()));
        labelHE.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getHe())).doubleValue()));
        if (renglon.getEspecificaciones() != null) {
            especifi.setText(renglon.getEspecificaciones());
        } else {
            especifi.setText(" ");
        }

        datos = FXCollections.observableArrayList();
        datos = getComponentesRenglon(renglon);
        dataTable.getItems().setAll(datos);

        loadComponetes();
        getAllSuminitros(salario.getId());
        ArrayList<String> sugestion = new ArrayList<String>();
        observableGlobalList.size();
        observableGlobalList.forEach(global -> {
            sugestion.add(global.getCodigo().trim() + " / " + global.getDescripcion().trim());
        });
        TextFields.bindAutoCompletion(fieldCodigo, sugestion).setPrefWidth(450);

        if (usuarios != null) {
            if (usuarios.getGruposId() != 1) {
                for (MenuItem item : opciones.getItems()) {
                    item.setDisable(true);
                }
            }
        }
        sugestion.size();
        if (renglon.getRs() > 3) {
            llenaComboTarifas(3);
        } else {
            llenaComboTarifas(renglon.getRs());
        }
    }

    private void llenaComboTarifas(int salario) {
        if (tarifaSalarialRepository.tarifaSalarialObservableList == null) {
            tarifasList = FXCollections.observableArrayList();
            tarifasList = tarifaSalarialRepository.getAllTarifasSalarial();
        }
        tarifasCodeList.addAll(tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().map(tarifaSalarial -> tarifaSalarial.getCode()).collect(Collectors.toList()));
        comboTarifas.setItems(tarifasCodeList);
        String codetarifa = tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(iten -> iten.getId() == salario).map(TarifaSalarial::getCode).findFirst().get();
        comboTarifas.getSelectionModel().select(codetarifa);
        tarifaSalarial = tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(item -> item.getId() == salario).findFirst().get();
    }

    public void handleCreateTarifaByPrecio(ActionEvent event) {
        tarifaSalarial = new TarifaSalarial();
        tarifaSalarial = tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(item -> item.getCode().trim().equals(comboTarifas.getValue().trim())).findFirst().get();

        if (dataTable.getItems().size() > 0) {
            calculaParametrosinRV(dataTable.getItems());
        }
    }

    public Boolean myFlag(ComponentesRV suministrosCompuestosView) {
        flag = false;
        dataTable.getItems().forEach(dat -> {
            if (dat.getCodigo().trim().equals(fieldCodigo.getText())) {
                flag = true;
            }
        });

        return flag;
    }

    public void handleNuevoComponent(ActionEvent event) {
        if (tempSuminitro.getTipo().trim().equals("2")) {
            componente = new ComponentesRV(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), getValosManoByTarifa(tempSuminitro.getTarifa()), new BigDecimal(String.format("%.4f", Double.valueOf(fieldCantidad.getText()))).doubleValue(), new BigDecimal(String.format("%.2f", Double.valueOf(fieldUsos.getText()))).doubleValue(), tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso(), tempSuminitro.getTarifa());
        } else {
            componente = new ComponentesRV(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), tempSuminitro.getPreciomn(), new BigDecimal(String.format("%.4f", Double.valueOf(fieldCantidad.getText()))).doubleValue(), new BigDecimal(String.format("%.2f", Double.valueOf(fieldUsos.getText()))).doubleValue(), tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso(), tempSuminitro.getTarifa());
        }
        check = myFlag(componente);

        if (check == false) {
            dataTable.getItems().add(componente);
            updateComponenteinRV(componente);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("El recurso " + componente.getDescripcion() + " ya es parte del Renglon Variante");
            alert.showAndWait();
        }
        calculaParametrosinRV(dataTable.getItems());
        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();
        valorInsumo.setText(" ");
        btn_print.setDisable(false);
    }

    private void calculaParametrosinRV(ObservableList<ComponentesRV> items) {
        costoMaterial = 0.0;
        costoMano = 0.0;
        costoEquipo = 0.0;
        hh = 0.0;
        he = 0.0;
        for (ComponentesRV comp : items) {
            if (comp.getTipo().trim().equals("1") || comp.getTipo().trim().equals("S") || comp.getTipo().trim().equals("J")) {
                costoMaterial += comp.getCantidad() * comp.getPreciomn();
            }
            if (comp.getTipo().trim().equals("2")) {
                costoMano += comp.getCantidad() * getValosManoByTarifa(comp.getTarifa());
                hh += comp.getCantidad();
            }
            if (comp.getTipo().trim().equals("3")) {
                costoEquipo += comp.getCantidad() * comp.getPreciomn();
                he += comp.getCantidad();
            }
        }

        labelCostoMat.setText(Double.toString(new BigDecimal(String.format("%.2f", costoMaterial)).doubleValue()));
        labelCostoMano.setText(Double.toString(new BigDecimal(String.format("%.2f", costoMano)).doubleValue()));
        labelCostoEquipo.setText(Double.toString(new BigDecimal(String.format("%.2f", costoEquipo)).doubleValue()));
        labelHH.setText(Double.toString(new BigDecimal(String.format("%.4f", hh)).doubleValue()));
        labelHE.setText(Double.toString(new BigDecimal(String.format("%.4f", he)).doubleValue()));
        labelTotal.setText(Double.toString(new BigDecimal(String.format("%.2f", costoMaterial + costoMano + costoEquipo)).doubleValue()));

        insumodescrip.setText(" ");
        insumoum.setText(" ");
        insumomn.setText(" ");
    }

    public void updateComponenteRVInsumos(Integer idRV, ComponentesRV componente) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Renglonrecursos renrec = renglon.getRenglonrecursosById().parallelStream().filter(item -> item.getRenglonvarianteId() == idRV && item.getRecursosId() == componente.getId()).findFirst().orElse(null);
            if (renrec != null) {
                deleteComponentRC(renrec.getRenglonvarianteId(), renrec.getRecursosId());
                createRRecurso(idRV, componente);
            } else {
                createRRecurso(idRV, componente);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void createRRecurso(Integer idRV, ComponentesRV componente) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Renglonrecursos renglonrecursos = new Renglonrecursos();
            renglonrecursos.setRenglonvarianteId(idRV);
            renglonrecursos.setRecursosId(componente.getId());
            renglonrecursos.setCantidas(componente.getCantidad());
            renglonrecursos.setUsos(componente.getUso());
            session.save(renglonrecursos);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updateComponenteRVJuego(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonjuego renrec = renglon.getRenglonjuegosById().parallelStream().filter(item -> item.getRenglonvarianteId() == idRV && item.getJuegoproductoId() == componente.getId()).findFirst().orElse(null);
            if (renrec != null) {
                deleteComponentRJ(renrec.getRenglonvarianteId(), renrec.getJuegoproductoId());
                createComponenteRVJuego(idRV, componente);
            } else {
                createComponenteRVJuego(idRV, componente);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void createComponenteRVJuego(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Renglonjuego renglonrecursos1 = new Renglonjuego();
            renglonrecursos1.setRenglonvarianteId(idRV);
            renglonrecursos1.setJuegoproductoId(componente.getId());
            renglonrecursos1.setCantidad(componente.getCantidad());
            renglonrecursos1.setUsos(componente.getUso());
            session.save(renglonrecursos1);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void updateComponenteRVSemielaborado(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonsemielaborados renrec = renglon.getRenglonsemielaboradosById().parallelStream().filter(item -> item.getRenglonvarianteId() == idRV && item.getSuministrossemielaboradosId() == componente.getId()).findFirst().orElse(null);
            if (renrec != null) {
                deleteComponentRS(renrec.getRenglonvarianteId(), renrec.getSuministrossemielaboradosId());
                createdComponenteRVSemielaborado(idRV, componente);
            } else {
                createdComponenteRVSemielaborado(idRV, componente);
            }
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void createdComponenteRVSemielaborado(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Renglonsemielaborados renglonrecursos1 = new Renglonsemielaborados();
            renglonrecursos1.setRenglonvarianteId(idRV);
            renglonrecursos1.setSuministrossemielaboradosId(componente.getId());
            renglonrecursos1.setCantidad(componente.getCantidad());
            renglonrecursos1.setUsos(componente.getUso());
            session.save(renglonrecursos1);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void updateComponenteinRV(ComponentesRV componente) {

        if (componente.getTipo().trim().equals("1") || componente.getTipo().trim().equals("2") || componente.getTipo().trim().equals("3")) {
            updateComponenteRVInsumos(renglon.getId(), componente);
        }
        if (componente.getTipo().trim().equals("J")) {
            updateComponenteRVJuego(renglon.getId(), componente);
        }
        if (componente.getTipo().trim().equals("S")) {
            updateComponenteRVSemielaborado(renglon.getId(), componente);
        }

    }

    public void handleModificarMenu(ActionEvent event) {

        componente.setCantidad(Double.parseDouble(fieldCantidad.getText()));
        componente.setUso(Double.parseDouble(fieldUsos.getText()));
        // datos.set(index, componente);
        dataTable.getItems().set(index, componente);

        updateComponenteinRV(componente);

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();
        valorInsumo.setText(" ");
        calculaParametrosinRV(dataTable.getItems());
    }

    public void handleEliminarMenu(ActionEvent event) {
        componente = dataTable.getSelectionModel().getSelectedItem();
        if (componente.getTipo().equals("1") || componente.getTipo().equals("2") || componente.getTipo().equals("3")) {
            deleteComponentRC(renglon.getId(), componente.getId());
        } else if (componente.getTipo().equals("S")) {
            deleteComponentRS(renglon.getId(), componente.getId());
        } else if (componente.getTipo().equals("J")) {
            deleteComponentRJ(renglon.getId(), componente.getId());
        }
        dataTable.getItems().remove(componente);
        resetButtons();
        calculaParametrosinRV(dataTable.getItems());
    }

    public void resetButtons() {
        btn_update.setDisable(true);
        btn_hecho.setDisable(false);
    }

    public void handleNewRenglonVariante(ActionEvent event) {

        try {
            updateRenglonVariante(renglon.getId());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Error al actualizar el renglón variante");
            alert.showAndWait();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Renglón Variante Insertado Satisfactoriamente");
        alert.showAndWait();
    }

    private void updateRenglonVariante(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonvariante rv = session.get(Renglonvariante.class, id);
            rv.setCodigo(fieldCodeJP.getText());
            rv.setDescripcion(labelDescrip.getText());
            rv.setUm(labelUM.getText());
            rv.setCostomat(Double.parseDouble(labelCostoMat.getText()));
            rv.setCostmano(Double.parseDouble(labelCostoMano.getText()));
            rv.setCostequip(Double.parseDouble(labelCostoEquipo.getText()));
            rv.setHh(Double.parseDouble(labelHH.getText()));
            rv.setHa(0.0);
            rv.setHe(Double.parseDouble(labelHE.getText()));
            rv.setHo(0.0);
            rv.setEspecificaciones(especifi.getText());
            // rv.setTarifaId(tarifaSalarial.getId());

            session.update(rv);

            tx.commit();
            session.close();
        } catch (
                HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void handleclosePane(ActionEvent event) {
        paneForm.setVisible(false);
    }

    public void printRenglonReport(ActionEvent event) {
        bdr = new BuildDynamicReport();
        if (!especifi.getText().isEmpty() && !fieldCodeJP.getText().isEmpty() && !labelDescrip.getText().isEmpty() && !labelUM.getText().isEmpty() && dataTable.getItems().size() > 0) {
            rCreportModelList = new ArrayList<>();
            rCreportModelList = createListTorepot(dataTable.getItems());
            var total = rCreportModelList.parallelStream().map(RCreportModel::getCosto).reduce(0.0, Double::sum);
            Map parametros = new HashMap();
            parametros.put("empresa", reportProjectStructure.getEmpresa().getNombre());
            parametros.put("image", "templete/logoReport.jpg");
            parametros.put("reportName", "RC: " + fieldCodeJP.getText().trim() + "   " + labelDescrip.getText().trim());
            parametros.put("unidad", "Costo: " + total + " " + tarifaSalarial.getMoneda() + "/" + labelUM.getText().trim());
            if (renglon.getEspecificaciones() == null) {
                parametros.put("reportDetail", "Sin especificaciones");
            } else {
                parametros.put("reportDetail", renglon.getEspecificaciones().trim());
            }
            try {
                DynamicReport dr = bdr.createreportRCDetail(tarifaSalarial);
                JRDataSource ds = new JRBeanCollectionDataSource(rCreportModelList);
                JasperReport jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
                JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);
                JasperViewer.viewReport(jp, false);
            } catch (Exception ex) {
                ex.printStackTrace();
                reportProjectStructureSingelton.showAlert(" Ocurrio un error al mostrar el reporte!!!", 2);
            }
        } else {
            reportProjectStructureSingelton.showAlert("Complete los campos del formulario para mostar el reporte", 2);
            especifi.setFocusColor(Color.RED);
            labelDescrip.setFocusColor(Color.RED);
            labelUM.setFocusColor(Color.RED);
            fieldCodeJP.setFocusColor(Color.RED);
        }
    }

    private List<RCreportModel> createListTorepot(ObservableList<ComponentesRV> items) {
        List<RCreportModel> temp = new ArrayList<>();
        for (ComponentesRV item : items) {
            if (item.getTipo().trim().equals("1") || item.getTipo().trim().equals("J") || item.getTipo().trim().equals("S")) {
                temp.add(new RCreportModel("Materiales", item.getCodigo(), item.getDescripcion().trim(), item.getUm(), item.getCantidad(), item.getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidad() * item.getPreciomn())).doubleValue()));
            } else if (item.getTipo().trim().equals("2")) {
                double val = reportProjectStructureSingelton.getValorManoDeObraInRVUsinTarifa(tarifaSalarial, item.getTarifa());
                temp.add(new RCreportModel("Mano de Obra", item.getCodigo(), item.getDescripcion().trim(), item.getUm(), item.getCantidad(), val, new BigDecimal(String.format("%.2f", item.getCantidad() * val)).doubleValue()));
            } else if (item.getTipo().trim().equals("3")) {
                temp.add(new RCreportModel("Equipos", item.getCodigo(), item.getDescripcion().trim(), item.getUm(), item.getCantidad(), item.getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidad() * item.getPreciomn())).doubleValue()));
            }
        }
        return temp;
    }


}
