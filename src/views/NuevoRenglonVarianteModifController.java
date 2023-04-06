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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class NuevoRenglonVarianteModifController implements Initializable {

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

    private double getValosManoByTarifa(String tarifa) {
        double valorTarifa = tarifaSalarial.getGruposEscalasCollection().parallelStream().filter(item -> item.getGrupo().trim().equals(tarifa.trim())).findFirst().map(GruposEscalas::getValor).get();
        return valorTarifa;
    }

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    public ObservableList<ComponentesRV> getComponentesRenglon(int id) {


        suministrosRenglonViews = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            /**
             * Capturo los recursos que componen al renglon variante
             */
            Query query = session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId = :id");
            query.setParameter("id", id);
            renglonrecursosArrayList = (ArrayList<Renglonrecursos>) ((org.hibernate.query.Query) query).list();
            renglonrecursosArrayList.forEach(suministro -> {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());
                componente = new ComponentesRV(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), suministro.getCantidas(), suministro.getUsos(), "precons", sum.getTipo(), sum.getPeso(), sum.getGrupoescala());

                suministrosRenglonViews.add(componente);


            });

            /**
             * Capturo los suministros compuestos que componen al renglon variante
             *
             */
            Query query1 = session.createQuery("FROM Renglonsemielaborados WHERE renglonvarianteId = :id");
            query1.setParameter("id", id);
            renglonsemielaboradosArrayList = (ArrayList<Renglonsemielaborados>) ((org.hibernate.query.Query) query1).list();
            if (renglonsemielaboradosArrayList != null) {
                renglonsemielaboradosArrayList.forEach(semielab -> {
                    Suministrossemielaborados sumSemi = session.find(Suministrossemielaborados.class, semielab.getSuministrossemielaboradosId());
                    componente = new ComponentesRV(sumSemi.getId(), sumSemi.getCodigo(), sumSemi.getDescripcion(), sumSemi.getUm(), sumSemi.getPreciomn(), semielab.getCantidad(), semielab.getUsos(), "precons", "S", sumSemi.getPeso(), "I");
                    suministrosRenglonViews.add(componente);
                });
            }
            Query query2 = session.createQuery("FROM Renglonjuego WHERE renglonvarianteId = :id");
            query2.setParameter("id", id);
            renglonjuegoArrayList = (ArrayList<Renglonjuego>) ((org.hibernate.query.Query) query2).list();
            if (renglonjuegoArrayList != null) {
                renglonjuegoArrayList.forEach(renglonjuego -> {
                    Juegoproducto juegoproducto = session.find(Juegoproducto.class, renglonjuego.getJuegoproductoId());
                    componente = new ComponentesRV(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getUm(), juegoproducto.getPreciomn(), renglonjuego.getCantidad(), renglonjuego.getUsos(), "precons", "J", juegoproducto.getPeso(), "I");
                    suministrosRenglonViews.add(componente);

                });
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministrosRenglonViews;
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

    public void LlenarDatos(Integer subgId, Salario salario) {
        btn_print.setDisable(true);
        RS = salario;
        subGrupo = subgId;
        labelCostoMano.setText(String.valueOf(0.0));
        labelCostoMat.setText(String.valueOf(0.0));
        labelCostoEquipo.setText(String.valueOf(0.0));
        labelTotal.setText(String.valueOf(0.0));
        labelHH.setText(String.valueOf(0.0));
        labelHE.setText(String.valueOf(0.0));

        loadComponetes();
        getAllSuminitros(RS.getId());

        ArrayList<String> sugestion = new ArrayList<String>();
        observableGlobalList.size();
        observableGlobalList.forEach(global -> {
            sugestion.add(global.getCodigo().trim() + " / " + global.getDescripcion().trim());
        });
        TextFields.bindAutoCompletion(fieldCodigo, sugestion).setPrefWidth(450);
        sugestion.size();
        llenaComboTarifas();
    }

    private void llenaComboTarifas() {
        if (tarifaSalarialRepository.tarifaSalarialObservableList == null) {
            tarifasList = FXCollections.observableArrayList();
            tarifasList = tarifaSalarialRepository.getAllTarifasSalarial();
        }
        tarifasCodeList.addAll(tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().map(tarifaSalarial -> tarifaSalarial.getCode()).collect(Collectors.toList()));
        comboTarifas.setItems(tarifasCodeList);
        comboTarifas.getSelectionModel().select(0);
        tarifaSalarial = tarifaSalarialRepository.tarifaSalarialObservableList.parallelStream().filter(item -> item.getCode().trim().equals(tarifasCodeList.get(0).trim())).findFirst().get();
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

            Renglonrecursos renglonrecursos1 = new Renglonrecursos();
            renglonrecursos1.setRenglonvarianteId(idRV);
            renglonrecursos1.setRecursosId(componente.getId());
            renglonrecursos1.setCantidas(componente.getCantidad());
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

    public void updateComponenteRVJuego(Integer idRV, ComponentesRV componente) {

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

    public void handleModificarMenu(ActionEvent event) {

        componente.setCantidad(Double.parseDouble(fieldCantidad.getText()));
        componente.setUso(Double.parseDouble(fieldUsos.getText()));
        //   datos.set(index, componente);
        dataTable.getItems().set(index, componente);

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();
    }

    public void handleEliminarMenu(ActionEvent event) {
        componente = dataTable.getSelectionModel().getSelectedItem();
        dataTable.getItems().remove(componente);

    }

    public void updateComponenteRVSemielaborado(Integer idRV, ComponentesRV componente) {

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

    public void handleNewRenglonVariante(ActionEvent event) {

        Renglonvariante rv = new Renglonvariante();
        rv.setCodigo(fieldCodeJP.getText());
        rv.setDescripcion(labelDescrip.getText());
        rv.setUm(labelUM.getText());
        rv.setCostomat(Double.parseDouble(labelCostoMat.getText().trim()));
        rv.setCostmano(Double.parseDouble(labelCostoMano.getText().trim()));
        rv.setCostequip(Double.parseDouble(labelCostoEquipo.getText().trim()));

        rv.setHh(Double.parseDouble(labelHH.getText()));
        rv.setHa(0.0);
        rv.setHe(Double.parseDouble(labelHE.getText()));
        rv.setHo(0.0);

        rv.setPeso(0.0);
        rv.setCarga(0.0);
        rv.setCemento(0.0);
        rv.setAsfalto(0.0);
        rv.setArido(0.0);
        rv.setPrefab(0.0);
        rv.setPreciomlc(0.0);
        rv.setPreciomn(0.0);
        rv.setRs(RS.getId());
        rv.setSalario(0.0);
        rv.setSubgrupoId(subGrupo);
        rv.setEspecificaciones(especifi.getText().trim());
        // rv.setTar(tarifaSalarial.getId());
        idRenglon = addRenglonV(rv);

        if (idRenglon != null) {
            for (ComponentesRV comp : dataTable.getItems()) {
                if (comp.getTipo().contentEquals("1") || comp.getTipo().contentEquals("2") || comp.getTipo().contentEquals("3")) {
                    updateComponenteRVInsumos(idRenglon, comp);
                }
                if (comp.getTipo().contentEquals("J")) {
                    updateComponenteRVJuego(idRenglon, comp);
                }
                if (comp.getTipo().contentEquals("S")) {
                    updateComponenteRVSemielaborado(idRenglon, comp);
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Renglón Variante Insertado Satisfactoriamente");
            alert.showAndWait();

            List<Renglonvariante> list = new ArrayList<>();
            list = reportProjectStructureSingelton.getAllRenglones();
            list.size();

            clearFilds();
        }

    }

    private void clearFilds() {
        fieldCodeJP.clear();
        labelDescrip.clear();
        labelUM.clear();
        labelCostoMat.clear();
        labelCostoMano.clear();
        labelCostoEquipo.clear();
        labelTotal.clear();
        labelHH.clear();
        labelHE.clear();
        especifi.clear();
        dataTable.getItems().clear();

    }

    public Integer addRenglonV(Renglonvariante renglonvariante) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Integer idRV = null;

            idRenglon = (Integer) session.save(renglonvariante);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return idRenglon;
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

    public void handleclosePane(ActionEvent event) {
        paneForm.setVisible(false);
    }

    public void printRenglonReport(ActionEvent event) {
        bdr = new BuildDynamicReport();
        if (!especifi.getText().isEmpty() && !fieldCodeJP.getText().isEmpty() && !labelDescrip.getText().isEmpty() && !labelUM.getText().isEmpty() && dataTable.getItems().size() > 0) {
            Map parametros = new HashMap();
            parametros.put("image", "templete/logoReport.jpg");
            parametros.put("reportName", fieldCodeJP.getText().trim() + "   " + labelDescrip.getText().trim() + "   " + labelTotal.getText() + "/" + labelUM.getText().trim());
            parametros.put("reportDetail", especifi.getText().trim());
            parametros.put("empresa", reportProjectStructure.getEmpresa().getNombre());
            rCreportModelList = new ArrayList<>();
            rCreportModelList = createListTorepot(dataTable.getItems());

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
                temp.add(new RCreportModel("Mano de Obra", item.getCodigo(), item.getDescripcion().trim(), item.getUm(), item.getCantidad(), item.getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidad() * item.getPreciomn())).doubleValue()));
            } else if (item.getTipo().trim().equals("3")) {
                temp.add(new RCreportModel("Equipos", item.getCodigo(), item.getDescripcion().trim(), item.getUm(), item.getCantidad(), item.getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidad() * item.getPreciomn())).doubleValue()));
            }
        }
        return temp;
    }


}
