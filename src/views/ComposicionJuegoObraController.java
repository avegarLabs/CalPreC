package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
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
import javafx.scene.layout.Pane;
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

public class ComposicionJuegoObraController implements Initializable {


    private int idSuministro;
    private ObservableList<ComponentesSuministrosCompuestosView> observableList;
    private ComponentesSuministrosCompuestosView compuestosView;
    private ArrayList<Recursos> arraySuministrosList;
    private ArrayList<Juegorecursos> suministrosArrayList;
    private ComponentesSuministrosCompuestosView tempSuminitro;
    private ObservableList<ComponentesSuministrosCompuestosView> observableGlobalList;


    /*


    private ArrayList<Composicionjuegopropiosumprecons> arrayCompPreconsList;
    private ArrayList<Composicionjuegopropio> arrayJuegoPropioList;

    private ArrayList<Suministrospropios> arrayListSuministrospropiosList;


    private Juegoproductopropios juegoproductopropios;
    private Suministros sumComponent;
    private Suministrospropios sumpropComponent;
    private Composicionjuegopropiosumprecons composicionjuegopropio;
    private Composicionjuegopropio composicionjuegosumpropio;
    private ArrayList<Composicionjuegopropiosumprecons> allComponetes;
*/
    private boolean flag;
    private int index;

    private SessionFactory sf;

    @FXML
    private Label label_title;

    @FXML
    private Pane content_pane;

    @FXML
    private TableView<ComponentesSuministrosCompuestosView> dataTable;

    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, String> code;

    @FXML
    private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> cantidad;

    @FXML
    private Label insumodescrip;

    @FXML
    private Label insumoum;

    @FXML
    private Label insumomn;

    @FXML
    private Label isumomlc;

    @FXML
    private JFXButton btn_modificar;

    @FXML
    private JFXTextField labelUM;

    @FXML
    private JFXTextField labelMN;

    @FXML
    private JFXTextField labelMlC;

    @FXML
    private JFXTextField labelpeso;

    @FXML
    private JFXTextArea labelDescrip;

    @FXML
    private JFXTextField fieldCodigo;

    @FXML
    private JFXTextArea textDescrip;

    @FXML
    private JFXTextField fieldCantidad;

    @FXML
    private JFXTextField fieldUsos;

    @FXML
    private JFXButton btn_hecho;

    @FXML
    private JFXButton btn_update;


    @FXML
    private JFXTextField fieldCodeJP;

    private int idObra;


    private Juegoproducto juegoproducto;
    private Juegorecursos juegorecursos;

    private ObservableList<ComponentesSuministrosCompuestosView> datos;
    private double valnull = 0.0;
    private int idjuego;
    private Boolean check;

    @FXML
    private Pane paneForm;

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
    public ObservableList<ComponentesSuministrosCompuestosView> getComponetesJuego(int id) {


        observableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Juegorecursos WHERE juegoproductoId = :id");
            query.setParameter("id", id);
            suministrosArrayList = (ArrayList<Juegorecursos>) ((org.hibernate.query.Query) query).list();
            suministrosArrayList.forEach(suministro -> {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());
                compuestosView = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), sum.getPreciomlc(), suministro.getCantidad(), 0.0, "precons", sum.getTipo(), sum.getPeso());
                observableList.add(compuestosView);


            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return observableList;
    }

    //Este metodo devuelve todos los elemntos de la table suminitros para llenar el
    public ObservableList<ComponentesSuministrosCompuestosView> getAllSuminitros() {
        observableGlobalList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            arraySuministrosList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE tipo = '1'").list();
            arraySuministrosList.forEach(sum1 -> {
                compuestosView = new ComponentesSuministrosCompuestosView(sum1.getId(), sum1.getCodigo(), sum1.getDescripcion(), sum1.getUm(), sum1.getPreciomn(), sum1.getPreciomlc(), 0.0, 0.0, "precons", sum1.getTipo(), sum1.getPeso());
                observableGlobalList.add(compuestosView);
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return observableGlobalList;
    }


    public Integer addJuegoProductoPropio(Juegoproducto juegoproducto) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer newJuego = null;

        try {
            tx = session.beginTransaction();
            newJuego = (Integer) session.save(juegoproducto);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return newJuego;
    }


    /**
     * Agregar la composición a pertir de suministros
     */
    public void addComposucionJuegoProductoPrecons(Juegorecursos juegorecursos) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.persist(juegorecursos);
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }


    public void loadComponetes() {
        code.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, String>("codigo"));
        code.setPrefWidth(100);
        cantidad.setCellValueFactory(new PropertyValueFactory<ComponentesSuministrosCompuestosView, DoubleProperty>("cantidad"));
        cantidad.setPrefWidth(80);


    }

    public void handleLLenarTextarea(ActionEvent event) {

        String[] partCode = fieldCodigo.getText().split(" - ");

        observableGlobalList.forEach(suministros -> {
            if (suministros.getCodigo().contentEquals(partCode[0])) {
                fieldCodigo.clear();
                fieldCodigo.setText(partCode[0]);
                textDescrip.setText(suministros.getDescripcion());
                tempSuminitro = suministros;
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        loadComponetes();
        getAllSuminitros();
        labelUM.setText("jg");

        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                insumodescrip.setText(dataTable.getSelectionModel().getSelectedItem().getDescripcion());
                insumoum.setText(dataTable.getSelectionModel().getSelectedItem().getUm());
                insumomn.setText(String.valueOf(dataTable.getSelectionModel().getSelectedItem().getPreciomn()));
                isumomlc.setText(String.valueOf(dataTable.getSelectionModel().getSelectedItem().getPreciomlc()));
            }
        });
        ArrayList<String> sugestion = new ArrayList<String>();
        observableGlobalList.forEach(global -> {
            sugestion.add(global.getCodigo() + " - " + global.getDescripcion());
        });
        TextFields.bindAutoCompletion(fieldCodigo, sugestion);

    }


    /**
     * private int idSuministro;
     * <p>
     * private ObservableList<ComponentesSuministrosCompuestosView> observableList;
     * private ObservableList<ComponentesSuministrosCompuestosView> observableGlobalList;
     * private ArrayList<Composicionjuego> suministrosArrayList;
     * private ArrayList<Composicionjuegopropiosumprecons> arrayCompPreconsList;
     * private ArrayList<Composicionjuegopropio> arrayJuegoPropioList;
     * private ArrayList<Suministros> arraySuministrosList;
     * private ArrayList<Suministrospropios> arrayListSuministrospropiosList;
     * private ComponentesSuministrosCompuestosView compuestosView;
     * private ComponentesSuministrosCompuestosView tempSuminitro;
     * private Juegoproductopropios juegoproductopropios;
     * private Suministros sumComponent;
     * private Suministrospropios sumpropComponent;
     * private Composicionjuegopropiosumprecons composicionjuegopropio;
     * private Composicionjuegopropio composicionjuegosumpropio;
     * private ArrayList<Composicionjuegopropiosumprecons> allComponetes;
     * <p>
     * private boolean flag;
     * private int index;
     * <p>
     * private SessionFactory sf;
     *
     * @FXML private Label label_title;
     * @FXML private Pane content_pane;
     * @FXML private TableView<ComponentesSuministrosCompuestosView> dataTable;
     * @FXML private TableColumn<ComponentesSuministrosCompuestosView, String> code;
     * @FXML private TableColumn<ComponentesSuministrosCompuestosView, DoubleProperty> cantidad;
     * @FXML private Label insumodescrip;
     * @FXML private Label insumoum;
     * @FXML private Label insumomn;
     * @FXML private Label isumomlc;
     * @FXML private JFXButton btn_modificar;
     * @FXML private JFXTextField labelUM;
     * @FXML private JFXTextField labelMN;
     * @FXML private JFXTextField labelMlC;
     * @FXML private JFXTextField labelpeso;
     * @FXML private JFXTextArea labelDescrip;
     * @FXML private JFXTextField fieldCodigo;
     * @FXML private JFXTextArea textDescrip;
     * @FXML private JFXTextField fieldCantidad;
     * @FXML private JFXTextField fieldUsos;
     * @FXML private JFXButton btn_hecho;
     * @FXML private JFXTextField fieldCodeJP;
     * <p>
     * private ObservableList<ComponentesSuministrosCompuestosView> datos;
     * private double valnull = 0.0;
     * private int idjuego;
     * <p>
     * /**
     * Metodos para obtener la composicion del suministro compuesto
     * <p>
     * //Este metodo devuelve todos los suminitros
     * public ObservableList<ComponentesSuministrosCompuestosView> getComponetesJuego(int id) {
     * <p>
     * suministrosArrayList = new ArrayList<Composicionjuego>();
     * observableList = FXCollections.observableArrayList();
     * <p>
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * DecimalFormat df = new DecimalFormat("0.00");
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * try {
     * tx = session.beginTransaction();
     * suministrosArrayList = (ArrayList<Composicionjuego>) session.createQuery("FROM Composicionjuego ").list();
     * suministrosArrayList.forEach(suministro -> {
     * if (suministro.getJuegoproductoId() == id) {
     * Suministros sum = (Suministros) session.find(Suministros.class, suministro.getSuministrosId());
     * compuestosView = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), sum.getPreciomlc(), suministro.getCantidad(), 0.0, "precons");
     * observableList.add(compuestosView);
     * }
     * <p>
     * });
     * <p>
     * arrayCompPreconsList = (ArrayList<Composicionjuegopropiosumprecons>) session.createQuery("FROM Composicionjuegopropiosumprecons ").list();
     * arrayCompPreconsList.forEach(suministro -> {
     * if (suministro.getJuegoproductopropiosId() == id) {
     * Suministros sum = (Suministros) session.find(Suministros.class, suministro.getSuministrosId());
     * compuestosView = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), sum.getPreciomlc(), suministro.getCantidad(), 0.0, "precons");
     * observableList.add(compuestosView);
     * }
     * <p>
     * <p>
     * });
     * <p>
     * arrayJuegoPropioList = (ArrayList<Composicionjuegopropio>) session.createQuery("FROM Composicionjuegopropio ").list();
     * arrayJuegoPropioList.forEach(suministro -> {
     * if (suministro.getJuegoproductopropiosId() == id) {
     * Suministrospropios sum = (Suministrospropios) session.find(Suministrospropios.class, suministro.getSuministrospropiosId());
     * compuestosView = new ComponentesSuministrosCompuestosView(sum.getId(), sum.getCodigo(), sum.getDescripcion(), sum.getUm(), sum.getPreciomn(), sum.getPreciomlc(), suministro.getCantidad(), 0.0, "propio");
     * observableList.add(compuestosView);
     * }
     * <p>
     * <p>
     * });
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * return observableList;
     * }
     * <p>
     * <p>
     * <p>
     * <p>
     * public Integer addJuegoProductoPropio(Juegoproductopropios juegoproductopropios) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * Integer newJuego = null;
     * <p>
     * <p>
     * try {
     * tx = session.beginTransaction();
     * newJuego = (Integer) session.save(juegoproductopropios);
     * <p>
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * return newJuego;
     * }
     * <p>
     * /**
     * Para obtener el juego de producto Insertado
     * <p>
     * public Juegoproductopropios getJuegoproductopropios(int id) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * <p>
     * try {
     * tx = session.beginTransaction();
     * juegoproductopropios = (Juegoproductopropios) session.find(Juegoproductopropios.class, id);
     * <p>
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * return juegoproductopropios;
     * }
     * <p>
     * /**
     * Componetes que son de suministros
     * <p>
     * public Suministros getSuministrosComoComponetes(ComponentesSuministrosCompuestosView componentesSuministros) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * <p>
     * try {
     * tx = session.beginTransaction();
     * sumComponent = (Suministros) session.find(Suministros.class, componentesSuministros.getId());
     * <p>
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * return sumComponent;
     * }
     * <p>
     * /**
     * Componetes que son de suministros
     * <p>
     * public Suministrospropios getSuministrosPropiosComoComponetes(ComponentesSuministrosCompuestosView componentesSuministros) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * <p>
     * try {
     * tx = session.beginTransaction();
     * sumpropComponent = (Suministrospropios) session.find(Suministrospropios.class, componentesSuministros.getId());
     * <p>
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * return sumpropComponent;
     * }
     * <p>
     * /**
     * Agregar la composición a pertir de suministros
     * <p>
     * public void addComposucionJuegoProductoPrecons(Composicionjuegopropiosumprecons composicionjuegopropio) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * <p>
     * <p>
     * try {
     * tx = session.beginTransaction();
     * session.persist(composicionjuegopropio);
     * <p>
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * <p>
     * }
     * <p>
     * /**
     * Agregar la composición a partir de suministros propios
     * <p>
     * public void addComposucionJuegoProductoSuministrosPropios(Composicionjuegopropio composicionjuegopropio) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * <p>
     * <p>
     * try {
     * tx = session.beginTransaction();
     * session.persist(composicionjuegopropio);
     * <p>
     * tx.commit();
     * } catch (HibernateException he) {
     * if (tx != null) tx.rollback();
     * he.printStackTrace();
     * } finally {
     * session.close();
     * }
     * <p>
     * }
     * <p>
     * /**
     * Method to UPDATE JuegoProductoPropio
     * <p>
     * public void updateJuegoProductoPropio(int id) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * <p>
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * <p>
     * <p>
     * try {
     * tx = session.beginTransaction();
     * Juegoproductopropios juegoproductopropios = (Juegoproductopropios) session.get(Juegoproductopropios.class, id);
     * juegoproductopropios.setCodigo(fieldCodeJP.getText());
     * juegoproductopropios.setDescripcion(labelDescrip.getText());
     * juegoproductopropios.setUm("jg");
     * if (labelpeso.getText() != null) {
     * juegoproductopropios.setPeso(Double.parseDouble(labelpeso.getText()));
     * } else {
     * juegoproductopropios.setPeso(valnull);
     * }
     * <p>
     * juegoproductopropios.setPreciomn(Double.parseDouble(labelMN.getText()));
     * if (labelMlC.getText() != null) {
     * juegoproductopropios.setPreciomlc(Double.parseDouble(labelMlC.getText()));
     * } else {
     * juegoproductopropios.setPreciomlc(valnull);
     * }
     * <p>
     * juegoproductopropios.setMermaprod(0.0);
     * juegoproductopropios.setMermamanip(0.0);
     * juegoproductopropios.setMermatrans(0.0);
     * juegoproductopropios.setOtrasmermas(0.0);
     * juegoproductopropios.setCarga(0.0);
     * juegoproductopropios.setCemento(0.0);
     * juegoproductopropios.setArido(0.0);
     * juegoproductopropios.setAsfalto(0.0);
     * juegoproductopropios.setCostequip(0.0);
     * juegoproductopropios.setCostmano(0.0);
     * juegoproductopropios.setCostomat(0.0);
     * juegoproductopropios.setHa(0.0);
     * juegoproductopropios.setHo(0.0);
     * juegoproductopropios.setHe(0.0);
     * session.update(juegoproductopropios);
     * tx.commit();
     * } catch (HibernateException e) {
     * if (tx != null) tx.rollback();
     * e.printStackTrace();
     * } finally {
     * session.close();
     * }
     * }
     * <p>
     * /**
     * Method to UPDATE composición de un juego
     * <p>
     * public void updateComposicionJuegoProductoPropio(int idjuegopropio) {
     * try {
     * * } catch (Throwable ex) {
     * System.err.println("Failed to create sessionFactory object." + ex);
     * throw new ExceptionInInitializerError(ex);
     * }
     * <p>
     * SessionSys session = sf.openSession();
     * Transaction tx = null;
     * <p>
     * <p>
     * try {
     * tx = session.beginTransaction();
     * CriteriaBuilder builder = session.getCriteriaBuilder();
     * CriteriaQuery<Composicionjuegopropiosumprecons> criteria = builder.createQuery(Composicionjuegopropiosumprecons.class);
     * Root<Composicionjuegopropiosumprecons> root = criteria.from(Composicionjuegopropiosumprecons.class);
     * criteria.select(root);
     * criteria.where(builder.equal(root.get("juegoproductopropiosId"), idjuegopropio));
     * arrayCompPreconsList = (ArrayList<Composicionjuegopropiosumprecons>) session.createQuery(criteria).list();
     * arrayCompPreconsList.forEach(dat -> {
     * System.out.println(dat.getJuegoproductopropiosId() + "--" + dat.getSuministrosId() + "--" + dat.getCantidad());
     * <p>
     * });
     * <p>
     * /*
     * arrayCompPreconsList = (ArrayList<Composicionjuegopropiosumprecons>) session.createQuery("FROM Composicionjuegopropiosumprecons ").list();
     * arrayCompPreconsList.forEach(ajpcl -> {
     * if (ajpcl.getJuegoproductopropiosId() == idjuegopropio) {
     * datos.forEach(componentes -> {
     * if (componentes.getTag().contentEquals("precons")) {
     * Composicionjuegopropiosumprecons compo = ajpcl;
     * compo.setSuministrosId(componentes.getId());
     * compo.setCantidad(componentes.getCantidad());
     * session.update(compo);
     * }
     * });
     * }
     * });
     * <p>
     * <p>
     * arrayJuegoPropioList = (ArrayList<Composicionjuegopropio>) session.createQuery("FROM Composicionjuegopropio ").list();
     * arrayJuegoPropioList.forEach(ajpl -> {
     * if (ajpl.getJuegoproductopropiosId() == idjuegopropio) {
     * datos.forEach(componetes -> {
     * if (componetes.getTag().contentEquals("propio")) {
     * Composicionjuegopropio compo = ajpl;
     * compo.setSuministrospropiosId(componetes.getId());
     * compo.setCantidad(componetes.getCantidad());
     * session.update(compo);
     * }
     * });
     * }
     * });
     * <p>
     * <p>
     * tx.commit();
     * } catch (HibernateException e) {
     * if (tx != null) tx.rollback();
     * e.printStackTrace();
     * } finally {
     * session.close();
     * }
     * }
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * ArrayList<String> sugestion = new ArrayList<String>();
     * observableGlobalList.forEach(global -> {
     * sugestion.add(global.getCodigo() + "/" + global.getDescripcion());
     * });
     * TextFields.bindAutoCompletion(fieldCodigo, sugestion);
     * <p>
     * }
     * <p>
     * public void handleLLenarTextarea(ActionEvent event) {
     * observableGlobalList.forEach(suministros -> {
     * if (suministros.getCodigo().contentEquals(fieldCodigo.getText())) {
     * textDescrip.setText(suministros.getDescripcion());
     * tempSuminitro = suministros;
     * }
     * });
     * }
     * <p>
     * public void pasarParametrosJP(Juegoproductopropios juegoproducto) {
     * DecimalFormat df = new DecimalFormat("0.00");
     * if (juegoproducto != null) {
     * idSuministro = juegoproducto.getId();
     * fieldCodeJP.setText(juegoproducto.getCodigo());
     * labelDescrip.setText(juegoproducto.getDescripcion());
     * labelMN.setText(String.valueOf(df.format(juegoproducto.getPreciomn())));
     * labelMlC.setText(String.valueOf(df.format(juegoproducto.getPreciomlc())));
     * labelpeso.setText(String.valueOf(df.format(juegoproducto.getPeso())));
     * datos = FXCollections.observableArrayList();
     * datos = getComponetesJuego(idSuministro);
     * dataTable.getItems().setAll(datos);
     * btn_modificar.setText("   Actualizar");
     * }
     * <p>
     * }
     */
    public void pasarParametros(JuegoProductoView juegoproducto, int id) {

        if (juegoproducto != null) {
            idSuministro = juegoproducto.getId();
            fieldCodeJP.setText(juegoproducto.getCodigo());
            labelDescrip.setText(juegoproducto.getDescripcion());
            labelMN.setText(String.valueOf(juegoproducto.getPreciomn()));
            labelMlC.setText(String.valueOf(juegoproducto.getPreciomlc()));
            labelpeso.setText(String.valueOf(juegoproducto.getPeso()));

            datos = FXCollections.observableArrayList();
            datos = getComponetesJuego(idSuministro);
            dataTable.getItems().setAll(datos);
            idObra = id;
        }
    }


    public Boolean myFlag(ComponentesSuministrosCompuestosView suministrosCompuestosView) {
        flag = false;
        dataTable.getItems().forEach(dat -> {
            if (dat.getCodigo().contentEquals(fieldCodigo.getText())) {
                flag = true;
            }
        });

        return flag;
    }

    public void showFormToInsert(ActionEvent event) {
        paneForm.setVisible(true);
        btn_hecho.setDisable(false);
        btn_update.setDisable(true);


    }


    public void showFormToUpdate() {
        paneForm.setVisible(true);
        btn_update.setDisable(false);
        btn_hecho.setDisable(true);
        index = dataTable.getSelectionModel().getSelectedIndex();
        compuestosView = dataTable.getSelectionModel().getSelectedItem();
        fieldCodigo.setText(compuestosView.getCodigo());
        textDescrip.setText(compuestosView.getDescripcion());
        fieldCantidad.setText(String.valueOf(compuestosView.getCantidad()));
    }

    public void handleNuevoComponent(ActionEvent event) {

        compuestosView = new ComponentesSuministrosCompuestosView(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), tempSuminitro.getPreciomn(), tempSuminitro.getPreciomlc(), Double.parseDouble(fieldCantidad.getText()), 0.0, tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso());
        check = myFlag(compuestosView);

        if (check == false) {
            //datos.add(compuestosView);
            dataTable.getItems().add(compuestosView);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("El recurso " + compuestosView.getDescripcion() + " ya es parte del juego");
            alert.showAndWait();

        }

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();

    }


    public void handleModificarMenu(ActionEvent event) {
        compuestosView.setCantidad(Double.parseDouble(fieldCantidad.getText()));
        dataTable.getItems().set(index, compuestosView);

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
    }

    public void handleEliminarMenu(ActionEvent event) {
        compuestosView = dataTable.getSelectionModel().getSelectedItem();

        dataTable.getItems().remove(compuestosView);

    }


    public void closeFormToInsert(ActionEvent event) {
        paneForm.setVisible(false);
    }

    public void handleActualizarJuego(ActionEvent event) {


        juegoproducto = new Juegoproducto();
        juegoproducto.setCodigo(fieldCodeJP.getText());
        juegoproducto.setDescripcion(labelDescrip.getText());
        juegoproducto.setUm("jg");
        if (labelpeso.getText().isEmpty()) {

            juegoproducto.setPeso(0.0);

        } else {
            juegoproducto.setPeso(Double.parseDouble(labelpeso.getText()));
        }
        juegoproducto.setPreciomn(Double.parseDouble(labelMN.getText()));
        if (labelMlC.getText().isEmpty()) {
            juegoproducto.setPreciomlc(0.0);

        } else {
            juegoproducto.setPreciomlc(Double.parseDouble(labelMlC.getText()));
        }

        juegoproducto.setMermaprod(0.0);
        juegoproducto.setMermamanip(0.0);
        juegoproducto.setMermatrans(0.0);
        juegoproducto.setOtrasmermas(0.0);
        juegoproducto.setCarga(0.0);
        juegoproducto.setCemento(0.0);
        juegoproducto.setArido(0.0);
        juegoproducto.setAsfalto(0.0);
        juegoproducto.setCostequip(0.0);
        juegoproducto.setCostmano(0.0);
        juegoproducto.setCostomat(0.0);
        juegoproducto.setHa(0.0);
        juegoproducto.setHo(0.0);
        juegoproducto.setHe(0.0);
        juegoproducto.setPertenece(String.valueOf(idObra));

        Integer inserted = addJuegoProductoPropio(juegoproducto);

        if (inserted != null) {

            juegorecursos = new Juegorecursos();
            juegorecursos.setJuegoproductoId(inserted);

            dataTable.getItems().forEach(datos -> {

                juegorecursos.setRecursosId(datos.getId());
                juegorecursos.setCantidad(datos.getCantidad());

                addComposucionJuegoProductoPrecons(juegorecursos);
            });

        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Juego de Producto  creado correctamente");
        alert.showAndWait();

        labelDescrip.setText("");
        labelMlC.setText("");
        labelMN.setText("");
        labelpeso.setText("");
        fieldCodeJP.setText("");
        dataTable.getItems().clear();
    }
}
/**
 * public void handleNewJuego(ActionEvent event) {
 * <p>
 * if (btn_modificar.getText().contentEquals("   Actualizar")) {
 * updateJuegoProductoPropio(idSuministro);
 * updateComposicionJuegoProductoPropio(idSuministro);
 * <p>
 * } else {
 * juegoproductopropios = new Juegoproductopropios();
 * juegoproductopropios.setCodigo(fieldCodeJP.getText());
 * juegoproductopropios.setDescripcion(labelDescrip.getText());
 * juegoproductopropios.setUm("jg");
 * if (labelpeso.getText() != null) {
 * juegoproductopropios.setPeso(Double.parseDouble(labelpeso.getText()));
 * } else {
 * juegoproductopropios.setPeso(valnull);
 * }
 * <p>
 * juegoproductopropios.setPreciomn(Double.parseDouble(labelMN.getText()));
 * if (labelMlC.getText() != null) {
 * juegoproductopropios.setPreciomlc(Double.parseDouble(labelMlC.getText()));
 * } else {
 * juegoproductopropios.setPreciomlc(valnull);
 * }
 * <p>
 * juegoproductopropios.setMermaprod(0.0);
 * juegoproductopropios.setMermamanip(0.0);
 * juegoproductopropios.setMermatrans(0.0);
 * juegoproductopropios.setOtrasmermas(0.0);
 * juegoproductopropios.setCarga(0.0);
 * juegoproductopropios.setCemento(0.0);
 * juegoproductopropios.setArido(0.0);
 * juegoproductopropios.setAsfalto(0.0);
 * juegoproductopropios.setCostequip(0.0);
 * juegoproductopropios.setCostmano(0.0);
 * juegoproductopropios.setCostomat(0.0);
 * juegoproductopropios.setHa(0.0);
 * juegoproductopropios.setHo(0.0);
 * juegoproductopropios.setHe(0.0);
 * Integer inserted = addJuegoProductoPropio(juegoproductopropios);
 * <p>
 * datos.forEach(dat -> {
 * if (dat.getTag().contentEquals("precons")) {
 * composicionjuegopropio = new Composicionjuegopropiosumprecons();
 * Juegoproductopropios p1 = getJuegoproductopropios(juegoproductopropios.getId());
 * composicionjuegopropio.setJuegoproductopropiosId(p1.getId());
 * sumComponent = getSuministrosComoComponetes(dat);
 * composicionjuegopropio.setSuministrosId(sumComponent.getId());
 * composicionjuegopropio.setCantidad(dat.getCantidad());
 * <p>
 * addComposucionJuegoProductoPrecons(composicionjuegopropio);
 * <p>
 * <p>
 * } else {
 * composicionjuegosumpropio = new Composicionjuegopropio();
 * Juegoproductopropios p1 = getJuegoproductopropios(juegoproductopropios.getId());
 * composicionjuegosumpropio.setJuegoproductopropiosId(p1.getId());
 * sumpropComponent = getSuministrosPropiosComoComponetes(dat);
 * composicionjuegosumpropio.setSuministrospropiosId(sumpropComponent.getId());
 * composicionjuegosumpropio.setCantidad(dat.getCantidad());
 * addComposucionJuegoProductoSuministrosPropios(composicionjuegosumpropio);
 * <p>
 * }
 * });
 * Alert alert = new Alert(Alert.AlertType.INFORMATION);
 * alert.setHeaderText("Información");
 * alert.setContentText("Juego de Producto " + juegoproductopropios.getCodigo() + " creado correctamente");
 * <p>
 * alert.showAndWait();
 * <p>
 * labelDescrip.setText("");
 * labelMlC.setText("");
 * labelMN.setText("");
 * labelpeso.setText("");
 * fieldCodeJP.setText("");
 * datos.clear();
 * dataTable.getItems().clear();
 * }
 * }
 * }
 */