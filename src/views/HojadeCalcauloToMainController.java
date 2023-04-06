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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HojadeCalcauloToMainController implements Initializable {

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

    @FXML
    private JFXCheckBox t30;

    @FXML
    private JFXCheckBox t15;

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


    private ObservableList<String> listJuegoSug;
    private ObservableList<String> listSumSemiSug;

    private double importe;
    private double valGrupo;
    private double sal;
    private double costo;
    private Integer codeSize = 6;

    /**
     * Metodo para obtener todos los datos del renglon variante especificado
     *
     * @param code
     * @return
     */

    public Renglonvariante getRenglonVariante(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            if (t30.isSelected() == true) {
                Query query = session.createQuery("FROM Renglonvariante WHERE codigo =: codeRV AND rs = 3");
                query.setParameter("codeRV", code);

                if (query.getResultList().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("El renglón variante no existe");
                    alert.showAndWait();
                } else {
                    renglon = (Renglonvariante) query.getResultList().get(0);

                }
            }

            if (t15.isSelected() == true) {
                Query query = session.createQuery("FROM Renglonvariante WHERE codigo =: codeRV AND rs = 2");
                query.setParameter("codeRV", code);

                if (query.getResultList().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setContentText("El renglón variante no existe");
                    alert.showAndWait();
                } else {
                    renglon = (Renglonvariante) query.getResultList().get(0);

                }
            }
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();

        } finally {
            session.close();
        }
        return renglon;
    }

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
    public ObservableList<ComponentesSuministrosCompuestosView> getComponentesRenglon(int id) {


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
                // System.out.println(sum.getCodigo());
                componente = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), 0.0, suministro.getCantidas(), suministro.getUsos(), "precons", sum.getTipo(), sum.getPeso());
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
                    //  System.out.println("semis" + sumSemi.getCodigo());
                    componente = new ComponentesSuministrosCompuestosView(sumSemi.getId(), sumSemi.getCodigo(), sumSemi.getDescripcion(), sumSemi.getUm(), sumSemi.getPreciomn(), sumSemi.getPreciomlc(), semielab.getCantidad(), semielab.getUsos(), "precons", "S", sumSemi.getPeso());
                    suministrosRenglonViews.add(componente);


                });
            }

            Query query2 = session.createQuery("FROM Renglonjuego WHERE renglonvarianteId = :id");
            query2.setParameter("id", id);
            renglonjuegoArrayList = (ArrayList<Renglonjuego>) ((org.hibernate.query.Query) query2).list();
            if (renglonjuegoArrayList != null) {
                renglonjuegoArrayList.forEach(renglonjuego -> {
                    Juegoproducto juegoproducto = session.find(Juegoproducto.class, renglonjuego.getJuegoproductoId());
                    // System.out.println(juegoproducto.getCodigo());
                    componente = new ComponentesSuministrosCompuestosView(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getUm(), juegoproducto.getPreciomn(), juegoproducto.getPreciomlc(), renglonjuego.getCantidad(), renglonjuego.getUsos(), "precons", "J", juegoproducto.getPeso());
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

    public ObservableList<ComponentesSuministrosCompuestosView> getManodeObra(ObservableList<ComponentesSuministrosCompuestosView> recursoList) {
        manoObraObservableList = FXCollections.observableArrayList();
        recursoList.forEach(recurso -> {
            if (recurso.getTipo().contentEquals("2")) {
                manoObraObservableList.add(recurso);

            }
        });

        return manoObraObservableList;
    }

    /**
     * para el cálculo de salario
     */
    private double getImporteEscala(String grupo) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (t30.isSelected() == true) {
                Salario salario = session.get(Salario.class, 1);
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
            }

            if (t15.isSelected() == true) {
                Salario salario = session.get(Salario.class, 2);
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
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return importe;
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
        manoObra.forEach(mano -> {
            recursos = getrecursoMano(mano);
            valGrupo = getImporteEscala(recursos.getGrupoescala());
            importe = mano.getCantidad() * valGrupo;
            sal = Math.round(importe * 100d) / 100d;
        });

        return sal;
    }

    private Salario getValOfEscala() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            if (t30.isSelected() == true) {
                tx = session.beginTransaction();
                salario = session.get(Salario.class, 1);
            }
            if (t15.isSelected() == true) {
                tx = session.beginTransaction();
                salario = session.get(Salario.class, 2);
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return salario;
    }

    /**
     * para la estructura de los juegos de productos
     */
    //Este metodo devuelve todos los suminitros
    public ArrayList<Juegoproducto> getAllJuegos() {


        juegoproductoArrayList = new ArrayList<Juegoproducto>();
        listJuegoSug = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("from Juegoproducto ").list();
            juegoproductoArrayList.forEach(juegoproducto -> {
                listJuegoSug.add(juegoproducto.getCodigo() + " - " + juegoproducto.getDescripcion());
            });

            tx.commit();
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


        suministrosJuego = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Juegorecursos WHERE juegoproductoId = :id");
            query.setParameter("id", id);
            suministrosJuegosArrayList = (ArrayList<Juegorecursos>) ((org.hibernate.query.Query) query).list();
            suministrosJuegosArrayList.forEach(suministro -> {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());
                componente = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), sum.getPreciomlc(), suministro.getCantidad(), 0.0, "precons", sum.getTipo(), sum.getPeso());
                suministrosJuego.add(componente);


            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministrosJuego;
    }

    /**
     * para la estructura del los semielaborados
     */

    public ArrayList<Suministrossemielaborados> getAllSuministros() {


        suministrossemielaboradosArrayList = new ArrayList<Suministrossemielaborados>();
        listSumSemiSug = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("from Suministrossemielaborados ").list();

            suministrossemielaboradosArrayList.forEach(suministrossemielaborados -> {
                listSumSemiSug.add(suministrossemielaborados.getCodigo() + " - " + suministrossemielaborados.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministrossemielaboradosArrayList;
    }

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
    public ObservableList<ComponentesSuministrosCompuestosView> getComponetesSemielaborados(int id) {


        suministrosSemielaborados = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Semielaboradosrecursos WHERE suministrossemielaboradosId = :id");
            query.setParameter("id", id);
            suministrosSemielaboradosArrayList = (ArrayList<Semielaboradosrecursos>) ((org.hibernate.query.Query) query).list();
            suministrosSemielaboradosArrayList.forEach(suministro -> {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());

                componente = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), sum.getPreciomlc(), suministro.getCantidad(), suministro.getUsos(), "precons", sum.getTipo(), sum.getPeso());

                suministrosSemielaborados.add(componente);


            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return suministrosSemielaborados;
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
                labelPrecio.setText(String.valueOf(tableCompRV.getSelectionModel().getSelectedItem().getPreciomn()));
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
                labelPrecioSumJuego.setText(String.valueOf(tableCompJuego.getSelectionModel().getSelectedItem().getPreciomn()));
                labelPesoSumJuego.setText(String.valueOf(tableCompJuego.getSelectionModel().getSelectedItem().getPeso()));

            }
        });

        tableCompoSemi.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                textDescripSemi.setText(tableCompoSemi.getSelectionModel().getSelectedItem().getDescripcion());
                labelUMSumSemi.setText(tableCompoSemi.getSelectionModel().getSelectedItem().getUm());
                labelPrecioSumSemi.setText(String.valueOf(tableCompoSemi.getSelectionModel().getSelectedItem().getPreciomn()));
                labelSumSemiPeso.setText(String.valueOf(tableCompoSemi.getSelectionModel().getSelectedItem().getPeso()));

            }
        });


        salario = getValOfEscala();
        labelgrupo2.setText(String.valueOf(salario.getIi()));
        labelgrupo3.setText(String.valueOf(salario.getIii()));
        labelgrupo4.setText(String.valueOf(salario.getIv()));
        labelgrupo5.setText(String.valueOf(salario.getV()));
        labelgrupo6.setText(String.valueOf(salario.getVi()));
        labelgrupo7.setText(String.valueOf(salario.getVii()));
        labelgrupo8.setText(String.valueOf(salario.getViii()));
        labelgrupo9.setText(String.valueOf(salario.getIx()));
        labelgrupo10.setText(String.valueOf(salario.getX()));
        labelgrupo11.setText(String.valueOf(salario.getXi()));
        labelgrupo12.setText(String.valueOf(salario.getXii()));


        TextFields.bindAutoCompletion(fieldCodigoJuego, listJuegoSug);


        TextFields.bindAutoCompletion(fieldCodigoSemi, listSumSemiSug);

        t30.setOnMouseClicked(event -> {
            if (t30.isSelected() == true) {
                t15.setSelected(false);

                salario = getValOfEscala();
                labelgrupo2.setText(String.valueOf(salario.getIi()));
                labelgrupo3.setText(String.valueOf(salario.getIii()));
                labelgrupo4.setText(String.valueOf(salario.getIv()));
                labelgrupo5.setText(String.valueOf(salario.getV()));
                labelgrupo6.setText(String.valueOf(salario.getVi()));
                labelgrupo7.setText(String.valueOf(salario.getVii()));
                labelgrupo8.setText(String.valueOf(salario.getViii()));
                labelgrupo9.setText(String.valueOf(salario.getIx()));
                labelgrupo10.setText(String.valueOf(salario.getX()));
                labelgrupo11.setText(String.valueOf(salario.getXi()));
                labelgrupo12.setText(String.valueOf(salario.getXii()));

                fieldCodigo.clear();
                textDescripRV.clear();
                labelCostMat.setText(" ");
                labelMano.setText(" ");
                labelEquipos.setText(" ");
                tableCompRV.getItems().clear();
                labelCostotal.setText(" ");
                labelPesoRV.setText(" ");
                labelHH.setText(" ");
                labelHA.setText(" ");
                labelHO.setText(" ");
                labelHE.setText(" ");
                labelCemento.setText(" ");
                labelAridos.setText(" ");
                labelAsfalto.setText(" ");
                labelPrefab.setText(" ");
                labelCarga.setText(" ");
                tableSalario.getItems().clear();
                labelSalario.setText(" ");
                labelUM.setText(" ");
            }
        });

        t15.setOnMouseClicked(event -> {
            if (t15.isSelected() == true) {
                t30.setSelected(false);

                salario = getValOfEscala();
                labelgrupo2.setText(String.valueOf(salario.getIi()));
                labelgrupo3.setText(String.valueOf(salario.getIii()));
                labelgrupo4.setText(String.valueOf(salario.getIv()));
                labelgrupo5.setText(String.valueOf(salario.getV()));
                labelgrupo6.setText(String.valueOf(salario.getVi()));
                labelgrupo7.setText(String.valueOf(salario.getVii()));
                labelgrupo8.setText(String.valueOf(salario.getViii()));
                labelgrupo9.setText(String.valueOf(salario.getIx()));
                labelgrupo10.setText(String.valueOf(salario.getX()));
                labelgrupo11.setText(String.valueOf(salario.getXi()));
                labelgrupo12.setText(String.valueOf(salario.getXii()));

                fieldCodigo.clear();
                textDescripRV.clear();
                labelCostMat.setText(" ");
                labelMano.setText(" ");
                labelEquipos.setText(" ");
                tableCompRV.getItems().clear();
                labelCostotal.setText(" ");
                labelPesoRV.setText(" ");
                labelHH.setText(" ");
                labelHA.setText(" ");
                labelHO.setText(" ");
                labelHE.setText(" ");
                labelCemento.setText(" ");
                labelAridos.setText(" ");
                labelAsfalto.setText(" ");
                labelPrefab.setText(" ");
                labelCarga.setText(" ");
                tableSalario.getItems().clear();
                labelSalario.setText(" ");
                labelUM.setText(" ");
            }
        });

    }

    public void LlenarDatosRV(KeyEvent event) {

        if (fieldCodigo.getText().length() == codeSize) {
            String code = fieldCodigo.getText();
            renglon = getRenglonVariante(code);
            costo = renglon.getCostomat() + renglon.getCostmano() + renglon.getCostequip();
            fieldCodigo.setText(renglon.getCodigo());
            textDescripRV.setText(renglon.getDescripcion());
            labelUM.setText(renglon.getUm());
            labelCostMat.setText(String.valueOf(renglon.getCostomat()));
            labelMano.setText(String.valueOf(renglon.getCostmano()));
            labelEquipos.setText(String.valueOf(Math.round(renglon.getCostequip() * 100d) / 100d));
            labelCostotal.setText(String.valueOf(costo));
            labelCemento.setText(String.valueOf(renglon.getCemento()));
            labelAridos.setText(String.valueOf(renglon.getArido()));
            labelAsfalto.setText(String.valueOf(renglon.getAsfalto()));
            labelPrefab.setText(String.valueOf(renglon.getPrefab()));
            labelCarga.setText(String.valueOf(renglon.getCarga()));
            labelHH.setText(String.valueOf(renglon.getHh()));
            labelHE.setText(String.valueOf(renglon.getHe()));
            labelHO.setText(String.valueOf(renglon.getHo()));
            labelHA.setText(String.valueOf(renglon.getHa()));
            labelPesoRV.setText(String.valueOf(renglon.getPeso()));

            datos = FXCollections.observableArrayList();
            datos = getComponentesRenglon(renglon.getId());
            tableCompRV.getItems().setAll(datos);

            datosMano = FXCollections.observableArrayList();
            datosMano = getManodeObra(datos);
            tableSalario.getItems().setAll(datosMano);

            sal = calcSalRV(datosMano);
            labelSalario.setText(String.valueOf(sal));


        }

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
        juegoproductoArrayList.forEach(juegoproducto -> {
            if (juegoproducto.getCodigo().contentEquals(codejuego)) {
                datosJuego = getComponetesJuego(juegoproducto.getId());
                textDescripJuego.setText(juegoproducto.getDescripcion());
                labelUMJuego.setText(juegoproducto.getUm());
                labelPrecioJuego.setText(String.valueOf(juegoproducto.getPreciomn()));
                labelPesoJuego.setText(String.valueOf(juegoproducto.getPeso()));
            }
        });
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

        suministrossemielaboradosArrayList.forEach(suministrossemielaborados -> {
            if (suministrossemielaborados.getCodigo().contentEquals(codeSE)) {
                datosSemielaborado = getComponetesSemielaborados(suministrossemielaborados.getId());
                labelUMsemi.setText(suministrossemielaborados.getUm());
                labelPrecioSemi.setText(String.valueOf(suministrossemielaborados.getPreciomn()));
                textDescripSemielab.setText(String.valueOf(suministrossemielaborados.getDescripcion()));
                labelPesoSemi.setText(String.valueOf(suministrossemielaborados.getPeso()));
            }
        });

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
