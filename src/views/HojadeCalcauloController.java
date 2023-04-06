package views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HojadeCalcauloController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private Label label_title;
    @FXML
    private Label labelCostMat;
    @FXML
    private Label labelMano;
    @FXML
    private Label labelEquipos;
    @FXML
    private JFXTextField fieldCodigo;
    @FXML
    private Label labelUM;
    @FXML
    private TextArea textDescripRV;
    @FXML
    private TableView<ComponentesSuministrosCompuestosView> tableCompRV;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, StringProperty> sumRVCode;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> sumRVCant;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> sumRVUsos;
    @FXML
    private Label labelCostotal;
    @FXML
    private Label labelPesoRV;
    @FXML
    private Label labelHH;
    @FXML
    private Label labelHO;
    @FXML
    private Label labelHA;
    @FXML
    private Label labelHE;
    @FXML
    private Label labelCemento;
    @FXML
    private Label labelAridos;
    @FXML
    private Label labelAsfalto;
    @FXML
    private Label labelPrefab;
    @FXML
    private Label labelCarga;
    @FXML
    private TextArea textdescripSum;
    @FXML
    private Label labelUMSum;
    @FXML
    private Label labelPrecio;
    @FXML
    private Label labelSumPeso;
    @FXML
    private Label labelMano1;
    @FXML
    private Label labelEquipos1;
    @FXML
    private JFXTextField fieldCodigoEnSalario;
    @FXML
    private Label labelUMEnSalario;
    @FXML
    private TextArea textDescripEnSalario;
    @FXML
    private TableView<ComponentesSuministrosCompuestosView> tableSalario;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, StringProperty> codeMano;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> manoNorma;
    @FXML
    private Label labelSalario;
    @FXML
    private Label labelgrupo2;
    @FXML
    private Label labelgrupo3;
    @FXML
    private Label labelgrupo4;
    @FXML
    private Label labelgrupo5;
    @FXML
    private Label labelgrupo6;
    @FXML
    private Label labelgrupo7;
    @FXML
    private Label labelgrupo8;
    @FXML
    private Label labelgrupo9;
    @FXML
    private Label labelgrupo10;
    @FXML
    private Label labelgrupo11;
    @FXML
    private Label labelgrupo12;
    @FXML
    private TextArea textDescripMano;
    @FXML
    private Label labelUMMano;
    @FXML
    private Label labelPrecioJuego;
    @FXML
    private Label labelPrecioSumJuego;
    @FXML
    private Label labelPesoJuego;
    @FXML
    private JFXTextField fieldCodigoJuego;
    @FXML
    private Label labelUMJuego;
    @FXML
    private TextArea textDescripJuego;
    @FXML
    private TableView<ComponentesSuministrosCompuestosView> tableCompJuego;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, StringProperty> juegoSumCode;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> juegoSumCant;
    @FXML
    private TextArea textDescripSumJuego;
    @FXML
    private Label labelUMSumJuego;
    @FXML
    private Label labelPesoSumJuego;
    @FXML
    private Label labelPrecioSemi;
    @FXML
    private Label labelPesoSemi;
    @FXML
    private JFXTextField fieldCodigoSemi;
    @FXML
    private Label labelUMsemi;
    @FXML
    private TextArea textDescripSemielab;
    @FXML
    private TableView<ComponentesSuministrosCompuestosView> tableCompoSemi;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, StringProperty> semiSumCode;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> semiSumCant;
    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> semiSumUsos;
    @FXML
    private TextArea textDescripSemi;
    @FXML
    private Label labelUMSumSemi;
    @FXML
    private Label labelPrecioSumSemi;
    @FXML
    private Label labelSumSemiPeso;
    @FXML
    private JFXCheckBox checkConSum;
    @FXML
    private JFXCheckBox checkSinSum;
    private Renglonvariante renglon;
    private Recursos recursos;
    private Salario salario;

    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ArrayList<Renglonsemielaborados> renglonsemielaboradosArrayList;
    private ArrayList<Renglonjuego> renglonjuegoArrayList;
    private ArrayList<Juegoproducto> juegoproductoArrayList;
    private ArrayList<Juegorecursos> suministrosJuegosArrayList;
    private ArrayList<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private ArrayList<Semielaboradosrecursos> suministrosSemielaboradosArrayList;
    private ObservableList<ComponentesSuministrosCompuestosView> suministrosRenglonViews;
    private ObservableList<ComponentesSuministrosCompuestosView> suministrosJuego;
    private ObservableList<ComponentesSuministrosCompuestosView> suministrosSemielaborados;
    private ObservableList<ComponentesSuministrosCompuestosView> manoObraObservableList;
    private ComponentesSuministrosCompuestosView componente;
    private ObservableList<ComponentesSuministrosCompuestosView> datos;
    private ObservableList<ComponentesSuministrosCompuestosView> datosMano;
    private ObservableList<ComponentesSuministrosCompuestosView> datosJuego;
    private ObservableList<ComponentesSuministrosCompuestosView> datosSemielaborado;
    private UtilCalcSingelton util = UtilCalcSingelton.getInstance();
    private List<Empresaobratarifa> empresaobratarifaList;


    private ObservableList<String> listJuegoSug;
    private ObservableList<String> listSumSemiSug;

    private double importe;
    private double valGrupo;
    private double sal;
    private double costo;


    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
    public ObservableList<ComponentesSuministrosCompuestosView> getComponentesRenglon(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            /**
             * Capturo los recursos que componen al renglon variante
             */
            suministrosRenglonViews = FXCollections.observableArrayList();
            renglonrecursosArrayList = (ArrayList<Renglonrecursos>) session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId = :id").setParameter("id", id).getResultList();
            for (Renglonrecursos suministro : renglonrecursosArrayList) {
                suministrosRenglonViews.add(new ComponentesSuministrosCompuestosView(suministro.getRecursosByRecursosId().getId(), suministro.getRecursosByRecursosId().getCodigo(), suministro.getRecursosByRecursosId().getDescripcion(), suministro.getRecursosByRecursosId().getUm(), suministro.getRecursosByRecursosId().getPreciomn(), 0.0, new BigDecimal(String.format("%.4f", suministro.getCantidas())).doubleValue(), suministro.getUsos(), suministro.getRecursosByRecursosId().getGrupoescala(), suministro.getRecursosByRecursosId().getTipo(), suministro.getRecursosByRecursosId().getPeso()));
            }

            /**
             * Capturo los suministros compuestos que componen al renglon variante
             *
             */

            renglonsemielaboradosArrayList = (ArrayList<Renglonsemielaborados>) session.createQuery("FROM Renglonsemielaborados WHERE renglonvarianteId = :id").setParameter("id", id).getResultList();
            if (renglonsemielaboradosArrayList != null) {
                for (Renglonsemielaborados sumSemi : renglonsemielaboradosArrayList) {
                    suministrosRenglonViews.add(new ComponentesSuministrosCompuestosView(sumSemi.getSuministrossemielaboradosBySuministrossemielaboradosId().getId(), sumSemi.getSuministrossemielaboradosBySuministrossemielaboradosId().getCodigo(), sumSemi.getSuministrossemielaboradosBySuministrossemielaboradosId().getDescripcion(), sumSemi.getSuministrossemielaboradosBySuministrossemielaboradosId().getUm(), sumSemi.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn(), sumSemi.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomlc(), new BigDecimal(String.format("%.4f", sumSemi.getCantidad())).doubleValue(), sumSemi.getUsos(), "precons", "S", sumSemi.getSuministrossemielaboradosBySuministrossemielaboradosId().getPeso()));
                }
            }

            renglonjuegoArrayList = (ArrayList<Renglonjuego>) session.createQuery("FROM Renglonjuego WHERE renglonvarianteId = :id").setParameter("id", id).getResultList();
            if (renglonjuegoArrayList != null) {
                for (Renglonjuego renglonjuego : renglonjuegoArrayList) {
                    suministrosRenglonViews.add(new ComponentesSuministrosCompuestosView(renglonjuego.getJuegoproductoByJuegoproductoId().getId(), renglonjuego.getJuegoproductoByJuegoproductoId().getCodigo(), renglonjuego.getJuegoproductoByJuegoproductoId().getDescripcion(), renglonjuego.getJuegoproductoByJuegoproductoId().getUm(), renglonjuego.getJuegoproductoByJuegoproductoId().getPreciomn(), renglonjuego.getJuegoproductoByJuegoproductoId().getPreciomlc(), new BigDecimal(String.format("%.4f", renglonjuego.getCantidad())).doubleValue(), renglonjuego.getUsos(), "precons", "J", renglonjuego.getJuegoproductoByJuegoproductoId().getPeso()));
                }
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

    public ObservableList<ComponentesSuministrosCompuestosView> getManodeObra(ObservableList<ComponentesSuministrosCompuestosView> recursoList) {
        manoObraObservableList = FXCollections.observableArrayList();
        manoObraObservableList.setAll(recursoList.parallelStream().filter(r -> r.getTipo().equals("2")).collect(Collectors.toList()));
        return manoObraObservableList;
    }

    private Recursos getrecursoMano(ComponentesSuministrosCompuestosView componente) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recursos = session.get(Recursos.class, componente.getId());

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return recursos;
    }


    public double calcSalRV(ObservableList<ComponentesSuministrosCompuestosView> manoObra) {
        for (ComponentesSuministrosCompuestosView mano : manoObra) {
            recursos = getrecursoMano(mano);
            valGrupo = getValOfEscala(recursos.getGrupoescala());
            importe = mano.getCantidad() * valGrupo;
            sal = Math.round(importe * 100d) / 100d;
        }

        return sal;
    }

    private double getValOfEscala(String scala) {
        return empresaobratarifaList.parallelStream().filter(item -> item.getGrupo().trim().equals(scala.trim())).findFirst().map(Empresaobratarifa::getEscala).orElse(0.0);
    }

    private double getValOfTarifa(String scala) {
        System.out.println(scala);
        return empresaobratarifaList.parallelStream().filter(item -> item.getGrupo().trim().equals(scala.trim())).findFirst().map(Empresaobratarifa::getTarifa).orElse(0.0);
    }

    /**
     * para la estructura de los juegos de productos
     */
    //Este metodo devuelve todos los suminitros
    public ArrayList<Juegoproducto> getAllJuegos() {


        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            juegoproductoArrayList = new ArrayList<Juegoproducto>();
            listJuegoSug = FXCollections.observableArrayList();
            juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("from Juegoproducto ").getResultList();
            listJuegoSug.setAll(juegoproductoArrayList.parallelStream().map(j -> j.getCodigo() + " - " + j.getDescripcion()).collect(Collectors.toSet()));
            tx.commit();
            session.close();
            return juegoproductoArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return juegoproductoArrayList;
    }

    /**
     * Metodos para obtener la composicion del juego
     */
    public ObservableList<ComponentesSuministrosCompuestosView> getComponetesJuego(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            suministrosJuego = FXCollections.observableArrayList();
            suministrosJuegosArrayList = (ArrayList<Juegorecursos>) session.createQuery("FROM Juegorecursos WHERE juegoproductoId = :id").setParameter("id", id).getResultList();
            for (Juegorecursos sum : suministrosJuegosArrayList) {
                suministrosJuego.add(new ComponentesSuministrosCompuestosView(sum.getRecursosByRecursosId().getId(), sum.getRecursosByRecursosId().getCodigo(), sum.getRecursosByRecursosId().getDescripcion(), sum.getRecursosByRecursosId().getUm(), sum.getRecursosByRecursosId().getPreciomn(), sum.getRecursosByRecursosId().getPreciomlc(), new BigDecimal(String.format("%.4f", sum.getCantidad())).doubleValue(), 0.0, "precons", sum.getRecursosByRecursosId().getTipo(), sum.getRecursosByRecursosId().getPeso()));
            }
            tx.commit();
            session.close();
            return suministrosJuego;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            if (tx != null) tx.rollback();
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    /**
     * para la estructura del los semielaborados
     */

    public ArrayList<Suministrossemielaborados> getAllSuministros() {

        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            suministrossemielaboradosArrayList = new ArrayList<Suministrossemielaborados>();
            listSumSemiSug = FXCollections.observableArrayList();
            suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("from Suministrossemielaborados ").getResultList();
            listSumSemiSug.setAll(suministrossemielaboradosArrayList.parallelStream().map(sum -> sum.getCodigo() + " - " + sum.getDescripcion()).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return suministrossemielaboradosArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
    public ObservableList<ComponentesSuministrosCompuestosView> getComponetesSemielaborados(int id) {


        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            suministrosSemielaborados = FXCollections.observableArrayList();
            suministrosSemielaboradosArrayList = (ArrayList<Semielaboradosrecursos>) session.createQuery("FROM Semielaboradosrecursos WHERE suministrossemielaboradosId = :id").setParameter("id", id).getResultList();
            for (Semielaboradosrecursos sum : suministrosSemielaboradosArrayList) {
                suministrosSemielaborados.add(new ComponentesSuministrosCompuestosView(sum.getRecursosByRecursosId().getId(), sum.getRecursosByRecursosId().getCodigo(), sum.getRecursosByRecursosId().getDescripcion(), sum.getRecursosByRecursosId().getUm(), sum.getRecursosByRecursosId().getPreciomn(), sum.getRecursosByRecursosId().getPreciomlc(), new BigDecimal(String.format("%.4f", sum.getCantidad())).doubleValue(), sum.getUsos(), sum.getRecursosByRecursosId().getGrupoescala(), sum.getRecursosByRecursosId().getTipo(), sum.getRecursosByRecursosId().getPeso()));
            }

            tx.commit();
            session.close();
            return suministrosSemielaborados;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public void loadComponetesRV() {

        sumRVCode.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, StringProperty>("codigo"));
        sumRVCant.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
        sumRVUsos.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("uso"));


    }

    public void loadComponentsSalario() {
        codeMano.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, StringProperty>("codigo"));
        manoNorma.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComponetesRV();
        loadComponentsSalario();
        getAllJuegos();
        getAllSuministros();

        checkConSum.setSelected(true);

        tableCompRV.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textdescripSum.setText(tableCompRV.getSelectionModel().getSelectedItem().getDescripcion());
                labelUMSum.setText(tableCompRV.getSelectionModel().getSelectedItem().getUm());
                if (tableCompRV.getSelectionModel().getSelectedItem().getTipo().trim().equals("2")) {
                    labelPrecio.setText(String.valueOf(getValOfTarifa(tableCompRV.getSelectionModel().getSelectedItem().getTag())));
                } else {
                    labelPrecio.setText(String.valueOf(tableCompRV.getSelectionModel().getSelectedItem().getPreciomn()));
                }
                labelSumPeso.setText(String.valueOf(tableCompRV.getSelectionModel().getSelectedItem().getPeso()));

            }
        });

        tableSalario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textDescripMano.setText(tableSalario.getSelectionModel().getSelectedItem().getDescripcion());
                labelUMMano.setText(tableSalario.getSelectionModel().getSelectedItem().getUm());


            }
        });

        tableCompJuego.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textDescripSumJuego.setText(tableCompJuego.getSelectionModel().getSelectedItem().getDescripcion());
                labelUMSumJuego.setText(tableCompJuego.getSelectionModel().getSelectedItem().getUm());
                if (tableCompJuego.getSelectionModel().getSelectedItem().getTipo().trim().equals("2")) {
                    labelPrecioSumJuego.setText(String.valueOf(getValOfTarifa(tableCompJuego.getSelectionModel().getSelectedItem().getTag())));
                } else {
                    labelPrecioSumJuego.setText(String.valueOf(tableCompJuego.getSelectionModel().getSelectedItem().getPreciomn()));
                }
                labelPesoSumJuego.setText(String.valueOf(tableCompJuego.getSelectionModel().getSelectedItem().getPeso()));

            }
        });

        tableCompoSemi.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textDescripSemi.setText(tableCompoSemi.getSelectionModel().getSelectedItem().getDescripcion());
                labelUMSumSemi.setText(tableCompoSemi.getSelectionModel().getSelectedItem().getUm());
                if (tableCompoSemi.getSelectionModel().getSelectedItem().getTipo().trim().equals("2")) {
                    labelPrecioSumSemi.setText(String.valueOf(getValOfTarifa(tableCompoSemi.getSelectionModel().getSelectedItem().getTag())));
                } else {
                    labelPrecioSumSemi.setText(String.valueOf(tableCompoSemi.getSelectionModel().getSelectedItem().getPreciomn()));
                }
                labelSumSemiPeso.setText(String.valueOf(tableCompoSemi.getSelectionModel().getSelectedItem().getPeso()));

            }
        });

        TextFields.bindAutoCompletion(fieldCodigoJuego, listJuegoSug);
        TextFields.bindAutoCompletion(fieldCodigoSemi, listSumSemiSug);

    }

    public void LlenarDatosRV(Renglonvariante renglonvariante, Obra obra, int idEmpresa) {
        System.out.println(obra.getId() + " -- " + idEmpresa + " ***** " + obra.getTarifaId());

        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = util.getEmpresaobratarifaObservableList(obra.getId(), idEmpresa, obra.getTarifaId());

        renglon = renglonvariante;
        double costoMano = calCostomanoInRV(renglon);
        costo = renglon.getCostomat() + costoMano + renglon.getCostequip();
        fieldCodigo.setText(renglon.getCodigo());
        textDescripRV.setText(renglon.getDescripcion());
        labelUM.setText(renglon.getUm());
        labelCostMat.setText(String.valueOf(renglon.getCostomat()));
        labelMano.setText(String.valueOf(costoMano));
        labelEquipos.setText(String.valueOf(Math.round(renglon.getCostequip() * 100d) / 100d));
        labelCostotal.setText(String.valueOf(costo));
        labelCemento.setText(String.valueOf(renglon.getCemento()));
        labelAridos.setText(String.valueOf(renglon.getArido()));
        labelAsfalto.setText(String.valueOf(renglon.getAsfalto()));
        labelPrefab.setText(String.valueOf(renglon.getPrefab()));
        labelCarga.setText(String.valueOf(renglon.getCarga()));
        labelHH.setText(String.valueOf(new BigDecimal(String.format("%.4f", renglon.getHh())).doubleValue()));
        labelHE.setText(String.valueOf(new BigDecimal(String.format("%.4f", renglon.getHe())).doubleValue()));
        labelHO.setText(String.valueOf(new BigDecimal(String.format("%.4f", renglon.getHo())).doubleValue()));
        labelHA.setText(String.valueOf(new BigDecimal(String.format("%.4f", renglon.getHa())).doubleValue()));
        labelPesoRV.setText(String.valueOf(new BigDecimal(String.format("%.4f", renglon.getPeso())).doubleValue()));

        datos = FXCollections.observableArrayList();
        datos = getComponentesRenglon(renglon.getId());
        tableCompRV.getItems().setAll(datos);

        datosMano = FXCollections.observableArrayList();
        datosMano = getManodeObra(datos);
        tableSalario.getItems().setAll(datosMano);

        sal = calcSalRV(datosMano);
        labelSalario.setText(String.valueOf(sal));

        labelgrupo2.setText(String.valueOf(getValOfEscala("II")));
        labelgrupo3.setText(String.valueOf(getValOfEscala("III")));
        labelgrupo4.setText(String.valueOf(getValOfEscala("IV")));
        labelgrupo5.setText(String.valueOf(getValOfEscala("V")));
        labelgrupo6.setText(String.valueOf(getValOfEscala("VI")));
        labelgrupo7.setText(String.valueOf(getValOfEscala("VII")));
        labelgrupo8.setText(String.valueOf(getValOfEscala("VIII")));
        labelgrupo9.setText(String.valueOf(getValOfEscala("IX")));
        labelgrupo10.setText(String.valueOf(getValOfEscala("X")));
        labelgrupo11.setText(String.valueOf(getValOfEscala("XI")));
        labelgrupo12.setText(String.valueOf(getValOfEscala("XII")));

    }

    private double calCostomanoInRV(Renglonvariante renglon) {
        List<Renglonrecursos> temp = renglon.getRenglonrecursosById().parallelStream().filter(item -> item.getRecursosByRecursosId().getTipo().trim().equals("2")).collect(Collectors.toList());
        double val = 0.0;
        if (temp.size() > 0) {
            for (Renglonrecursos renglonrecursos : temp) {
                val += renglonrecursos.getCantidas() * getValOfTarifa(renglonrecursos.getRecursosByRecursosId().getGrupoescala());
            }
        }
        return new BigDecimal(String.format("%.2f", val)).doubleValue();
    }

    public void loadJuegoComponent() {
        juegoSumCode.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, StringProperty>("codigo"));
        juegoSumCant.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
    }

    public void handleGetJuego(ActionEvent event) {
        String codejuego = fieldCodigoJuego.getText().substring(0, 10);
        fieldCodigoJuego.clear();
        fieldCodigoJuego.setText(codejuego);

        datosJuego = FXCollections.observableArrayList();
        var juegoproducto = juegoproductoArrayList.parallelStream().filter(jp -> jp.getCodigo().equals(codejuego)).findFirst().orElse(null);

        datosJuego = getComponetesJuego(juegoproducto.getId());
        textDescripJuego.setText(juegoproducto.getDescripcion());
        labelUMJuego.setText(juegoproducto.getUm());
        labelPrecioJuego.setText(String.valueOf(juegoproducto.getPreciomn()));
        labelPesoJuego.setText(String.valueOf(juegoproducto.getPeso()));

        loadJuegoComponent();
        tableCompJuego.getItems().setAll(datosJuego);
    }


    public void loadComponentSemielaborado() {
        semiSumCode.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, StringProperty>("codigo"));
        semiSumCant.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
        semiSumUsos.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("uso"));
    }

    public void handleSemielaboradoComponent(ActionEvent event) {
        String codeSE = fieldCodigoSemi.getText().substring(0, 10);
        fieldCodigoSemi.clear();
        fieldCodigoSemi.setText(codeSE);
        datosSemielaborado = FXCollections.observableArrayList();

        var suministrossemielaborados = suministrossemielaboradosArrayList.parallelStream().filter(sse -> sse.getCodigo().equals(codeSE)).findFirst().orElse(null);
        datosSemielaborado = getComponetesSemielaborados(suministrossemielaborados.getId());
        labelUMsemi.setText(suministrossemielaborados.getUm());
        labelPrecioSemi.setText(String.valueOf(suministrossemielaborados.getPreciomn()));
        textDescripSemielab.setText(String.valueOf(suministrossemielaborados.getDescripcion()));
        labelPesoSemi.setText(String.valueOf(suministrossemielaborados.getPeso()));


        loadComponentSemielaborado();
        tableCompoSemi.getItems().setAll(datosSemielaborado);

    }

    public void handleSimSum(ActionEvent event) {
        double cMat = Double.parseDouble(labelCostMat.getText());
        if (checkSinSum.isSelected() == true) {
            checkConSum.setSelected(false);
            double value = costo - cMat;
            labelCostotal.setText(String.valueOf(Math.round(value * 100d) / 100d));
        }
    }

    public void handleConMat(ActionEvent event) {

        if (checkConSum.isSelected() == true) {
            checkSinSum.setSelected(false);
            labelCostotal.setText(String.valueOf(costo));
        }

    }

}
