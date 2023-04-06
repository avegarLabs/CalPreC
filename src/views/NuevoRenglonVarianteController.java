package views;

import com.jfoenix.controls.JFXButton;
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
import models.*;
import org.controlsfx.control.textfield.TextFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NuevoRenglonVarianteController implements Initializable {

    private static SessionFactory sf;
    private static double valor;
    private static int inputLength = 6;
    UtilCalcSingelton reportProjectStructureSingelton = UtilCalcSingelton.getInstance();
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
    private JFXTextField labelCemento;
    @FXML
    private JFXTextField labelAridos;
    @FXML
    private JFXTextField labelAsfalto;
    @FXML
    private JFXTextField labelPrefab;
    @FXML
    private JFXTextField labelCarga;
    @FXML
    private JFXTextField labelHH;
    @FXML
    private JFXTextField labelHE;
    @FXML
    private JFXTextField labelHO;
    @FXML
    private JFXTextField labelHA;
    @FXML
    private JFXTextField labelPeso;
    private Renglonvariante renglon;
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
    private ComponentesRV tempSuminitro;
    private Salario RS;

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
        String[] part = fieldCodigo.getText().split("/");
        observableGlobalList.forEach(suministros -> {
            if (suministros.getCodigo().contentEquals(part[0])) {
                fieldCodigo.clear();
                fieldCodigo.setText(part[0]);
                textDescrip.setText(suministros.getDescripcion());
                tempSuminitro = suministros;
            }
        });
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
    }


    public void showFormToUpdate() {
        paneForm.setVisible(true);
        btn_hecho.setDisable(true);

        index = dataTable.getSelectionModel().getSelectedIndex();
        componente = dataTable.getSelectionModel().getSelectedItem();
        fieldCodigo.setText(componente.getCodigo());
        textDescrip.setText(componente.getDescripcion());
        fieldCantidad.setText(String.valueOf(componente.getCantidad()));
        fieldUsos.setText(String.valueOf(componente.getUso()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                insumodescrip.setText(dataTable.getSelectionModel().getSelectedItem().getDescripcion());
                insumoum.setText(dataTable.getSelectionModel().getSelectedItem().getUm());
                insumomn.setText(String.valueOf(getTarifaResol(dataTable.getSelectionModel().getSelectedItem().getTarifa())));

            }
        });

    }

    public void LlenarDatos(Integer subgId, Salario salario) {
        RS = salario;
        subGrupo = subgId;
        labelCostoMano.setText(String.valueOf(0.0));
        labelCostoMat.setText(String.valueOf(0.0));
        labelCostoEquipo.setText(String.valueOf(0.0));
        labelTotal.setText(String.valueOf(0.0));
        labelCemento.setText(String.valueOf(0.0));
        labelAridos.setText(String.valueOf(0.0));
        labelAsfalto.setText(String.valueOf(0.0));
        labelPrefab.setText(String.valueOf(0.0));
        labelCarga.setText(String.valueOf(0.0));
        labelHH.setText(String.valueOf(0.0));
        labelHE.setText(String.valueOf(0.0));
        labelHO.setText(String.valueOf(0.0));
        labelHA.setText(String.valueOf(0.0));
        labelPeso.setText(String.valueOf(0.0));

        loadComponetes();
        getAllSuminitros(RS.getId());

        ArrayList<String> sugestion = new ArrayList<String>();
        observableGlobalList.size();
        observableGlobalList.forEach(global -> {
            sugestion.add(global.getCodigo().trim() + " / " + global.getDescripcion().trim());
        });
        TextFields.bindAutoCompletion(fieldCodigo, sugestion).setPrefWidth(450);
        sugestion.size();

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
        componente = new ComponentesRV(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), tempSuminitro.getPreciomn(), new BigDecimal(String.format("%.4f", Double.valueOf(fieldCantidad.getText()))).doubleValue(), new BigDecimal(String.format("%.2f", Double.valueOf(fieldUsos.getText()))).doubleValue(), tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso(), tempSuminitro.getTarifa());
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
    }

    private double getTarifaResol(String grpoScala) {
        valor = 0.0;
        if (grpoScala.trim().equals("II")) {
            valor = RS.getGtii();
        } else if (grpoScala.trim().equals("III")) {
            valor = RS.getGtiii();
        } else if (grpoScala.trim().equals("IV")) {
            valor = RS.getGtiv();
        } else if (grpoScala.trim().equals("V")) {
            valor = RS.getGtv();
        } else if (grpoScala.trim().equals("VI")) {
            valor = RS.getGtvi();
        } else if (grpoScala.trim().equals("VII")) {
            valor = RS.getGtii();
        } else if (grpoScala.trim().equals("VIII")) {
            valor = RS.getGtiii();
        } else if (grpoScala.trim().equals("IX")) {
            valor = RS.getGtix();
        } else if (grpoScala.trim().equals("X")) {
            valor = RS.getGtx();
        } else if (grpoScala.trim().equals("XI")) {
            valor = RS.getGtxi();
        }
        return valor;
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
                costoMano += comp.getCantidad() * getTarifaResol(comp.getTarifa());
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
        datos.set(index, componente);
        dataTable.getItems().set(index, componente);

        //  updateComponenteinRV(componente);


        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();
    }

    public void handleEliminarMenu(ActionEvent event) {
        componente = dataTable.getSelectionModel().getSelectedItem();
        datos.remove(componente);
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
        rv.setHa(Double.parseDouble(labelHA.getText()));
        rv.setHe(Double.parseDouble(labelHE.getText()));
        rv.setHo(Double.parseDouble(labelHO.getText()));

        rv.setPeso(Double.parseDouble(labelPeso.getText()));
        rv.setCarga(Double.parseDouble(labelCarga.getText()));
        rv.setCemento(Double.parseDouble(labelCemento.getText()));
        rv.setAsfalto(Double.parseDouble(labelAsfalto.getText()));
        rv.setArido(Double.parseDouble(labelAridos.getText()));
        rv.setPrefab(Double.parseDouble(labelPrefab.getText()));
        rv.setPreciomlc(0.0);
        rv.setPreciomn(0.0);
        rv.setRs(RS.getId());
        rv.setSalario(0.0);
        rv.setSubgrupoId(subGrupo);

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
        }


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


    public void handleclosePane(ActionEvent event) {
        paneForm.setVisible(false);
    }


}
