package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.ResourceBundle;

public class ComposicionRenglonVarianteController implements Initializable {
    private static SessionFactory sf;
    private static double valor;
    double costoMaterial;
    double costoMano;
    double costoEquipo;
    double hh;
    double he;
    @FXML
    private Label label_title;
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
    private Label labelTotal;
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
    private Label labelHH;
    @FXML
    private Label labelHE;
    @FXML
    private Label labelHO;
    @FXML
    private Label labelHA;
    @FXML
    private Label labelPeso;
    private Renglonvariante renglon;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ArrayList<Renglonsemielaborados> renglonsemielaboradosArrayList;
    private ArrayList<Renglonjuego> renglonjuegoArrayList;
    @FXML
    private TableView<ComponentesRV> dataTable;
    private ArrayList<Recursos> arraySuministrosList;
    private ArrayList<Suministrossemielaborados> arraySuministrossemielaboradosList;
    private ArrayList<Juegoproducto> arrayJuegoproductoArrayList;
    @FXML
    private TableColumn<ComponentesRV, String> code;
    @FXML
    private TableColumn<ComponentesRV, Double> cantidad;
    private boolean flag;
    private int index;
    private Boolean check;
    @FXML
    private MenuItem nuevoOP;
    @FXML
    private MenuItem modificarOP;
    @FXML
    private MenuItem eliminarOP;
    @FXML
    private TableColumn<ComponentesRV, Double> usos;
    private ObservableList<ComponentesRV> suministrosRenglonViews;
    private ComponentesRV componente;
    private ObservableList<ComponentesRV> observableGlobalList;
    private ObservableList<ComponentesRV> datos;
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

    /**
     * Metodos para obtener la composicion del suministro compuesto
     */
    //Este metodo devuelve todos los suminitros
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
                    componente = new ComponentesRV(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getUm(), juegoproducto.getPreciomlc(), renglonjuego.getCantidad(), renglonjuego.getUsos(), "precons", "J", juegoproducto.getPeso(), "I");
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

    public ObservableList<ComponentesRV> getAllSuminitros(int idSalario) {
        observableGlobalList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (idSalario == 1) {
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
            } else if (idSalario >= 2) {
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
        return observableGlobalList;
    }

    public void loadComponetes() {
        code.setCellValueFactory(new PropertyValueFactory<ComponentesRV, String>("codigo"));
        code.setPrefWidth(100);
        cantidad.setCellValueFactory(new PropertyValueFactory<ComponentesRV, Double>("cantidad"));
        cantidad.setPrefWidth(80);
        usos.setCellValueFactory(new PropertyValueFactory<ComponentesRV, Double>("uso"));
        usos.setPrefWidth(50);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadComponetes();


        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                insumodescrip.setText(dataTable.getSelectionModel().getSelectedItem().getDescripcion());
                insumoum.setText(dataTable.getSelectionModel().getSelectedItem().getUm());
                if (dataTable.getSelectionModel().getSelectedItem().getTipo().trim().equals("2")) {
                    insumomn.setText(String.valueOf(getTarifaResol(dataTable.getSelectionModel().getSelectedItem().getTarifa())));
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
        labelCemento.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getCemento())).doubleValue()));
        labelAridos.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getArido())).doubleValue()));
        labelAsfalto.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getAsfalto())).doubleValue()));
        labelPrefab.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getPrefab())).doubleValue()));
        labelCarga.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getCarga())).doubleValue()));
        labelHH.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getHh())).doubleValue()));
        labelHE.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getHe())).doubleValue()));
        labelHO.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getHo())).doubleValue()));
        labelHA.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getHa())).doubleValue()));
        labelPeso.setText(Double.toString(new BigDecimal(String.format("%.2f", renglon.getPeso())).doubleValue()));

        datos = FXCollections.observableArrayList();
        datos = getComponentesRenglon(renglon.getId());
        dataTable.getItems().setAll(datos);

        getAllSuminitros(salario.getId());
        ArrayList<String> sugestion = new ArrayList<String>();
        observableGlobalList.forEach(global -> {
            sugestion.add(global.getCodigo() + "/" + global.getDescripcion());
        });
        TextFields.bindAutoCompletion(fieldCodigo, sugestion);

        if (usuarios != null) {
            if (usuarios.getGruposId() != 1) {
                nuevoOP.setDisable(true);
                modificarOP.setDisable(true);
                eliminarOP.setDisable(true);
            }
        }

    }

    public Boolean myFlag(ComponentesRV suministrosCompuestosView) {
        flag = false;
        dataTable.getItems().forEach(dat -> {
            if (dat.getCodigo().contentEquals(fieldCodigo.getText())) {
                flag = true;
            }
        });

        return flag;
    }

    public void deleteComponenteRVInsumos(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonrecursos renglonrecursos = (Renglonrecursos) session.createQuery("FROM Renglonrecursos WHERE recursosId =: idR AND renglonvarianteId =: idRVa ").setParameter("idR", componente.getId()).setParameter("idRVa", idRV).getResultList().get(0);
            if (renglonrecursos != null) {
                session.delete(renglonrecursos);
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

    public void deleteComponenteRVJuego(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonjuego renglonrecursos = (Renglonjuego) session.createQuery("FROM Renglonjuego WHERE juegoproductoId =: idR AND renglonvarianteId =: idRVa ").setParameter("idR", componente.getId()).setParameter("idRVa", idRV).getResultList().get(0);
            if (renglonrecursos != null) {
                session.delete(renglonrecursos);
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

    public void deleteComponenteRVSemielaborado(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonsemielaborados renglonrecursos = (Renglonsemielaborados) session.createQuery("FROM Renglonsemielaborados WHERE suministrossemielaboradosId =: idR AND renglonvarianteId =: idRVa ").setParameter("idR", componente.getId()).setParameter("idRVa", idRV).getResultList().get(0);
            if (renglonrecursos != null) {
                session.delete(renglonrecursos);
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

    public void handleNuevoComponent(ActionEvent event) {

        componente = new ComponentesRV(tempSuminitro.getId(), tempSuminitro.getCodigo(), tempSuminitro.getDescripcion(), tempSuminitro.getUm(), tempSuminitro.getPreciomn(), Double.parseDouble(fieldCantidad.getText()), Double.parseDouble(fieldUsos.getText()), tempSuminitro.getTag(), tempSuminitro.getTipo(), tempSuminitro.getPeso(), tempSuminitro.getTarifa());
        check = myFlag(componente);

        if (check == false) {
            datos.add(componente);
            dataTable.getItems().add(componente);
            updateComponenteinRV(componente);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("El recurso " + componente.getDescripcion() + " ya es parte del Renglon Variante");
            alert.showAndWait();

        }

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();

        calculaParametrosinRV(dataTable.getItems());

    }

    public void updateComponenteRVInsumos(Integer idRV, ComponentesRV componente) {
        Session session = ConnectionModel.createAppConnection().openSession();
        System.out.println(componente.getId() + " **** " + "  888888888888 ");
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Renglonrecursos renrec = renglon.getRenglonrecursosById().parallelStream().filter(item -> item.getRenglonvarianteId() == idRV && item.getRecursosId() == componente.getId()).findFirst().orElse(null);
            if (renrec != null) {
                renrec.setCantidas(componente.getCantidad());
                renrec.setUsos(componente.getUso());
                session.update(renrec);
            } else {
                Renglonrecursos renglonrecursos = new Renglonrecursos();
                renglonrecursos.setRenglonvarianteId(idRV);
                renglonrecursos.setRecursosId(componente.getId());
                renglonrecursos.setCantidas(componente.getCantidad());
                renglonrecursos.setUsos(componente.getUso());
                session.save(renglonrecursos);
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

    public void updateComponenteRVJuego(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonjuego renrec = renglon.getRenglonjuegosById().parallelStream().filter(item -> item.getRenglonvarianteId() == idRV && item.getJuegoproductoId() == componente.getId()).findFirst().orElse(null);
            if (renrec != null) {
                renrec.setCantidad(componente.getCantidad());
                renrec.setUsos(componente.getUso());
                session.update(renrec);
            } else {
                Renglonjuego renglonrecursos1 = new Renglonjuego();
                renglonrecursos1.setRenglonvarianteId(idRV);
                renglonrecursos1.setJuegoproductoId(componente.getId());
                renglonrecursos1.setCantidad(componente.getCantidad());
                renglonrecursos1.setUsos(componente.getUso());
                session.save(renglonrecursos1);
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

    public void updateComponenteRVSemielaborado(Integer idRV, ComponentesRV componente) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonsemielaborados renrec = renglon.getRenglonsemielaboradosById().parallelStream().filter(item -> item.getRenglonvarianteId() == idRV && item.getSuministrossemielaboradosId() == componente.getId()).findFirst().orElse(null);
            if (renrec != null) {
                renrec.setCantidad(componente.getCantidad());
                renrec.setUsos(componente.getUso());
                session.update(renrec);
            } else {
                Renglonsemielaborados renglonrecursos1 = new Renglonsemielaborados();
                renglonrecursos1.setRenglonvarianteId(idRV);
                renglonrecursos1.setSuministrossemielaboradosId(componente.getId());
                renglonrecursos1.setCantidad(componente.getCantidad());
                renglonrecursos1.setUsos(componente.getUso());
                session.save(renglonrecursos1);
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
        datos.set(index, componente);
        dataTable.getItems().set(index, componente);

        updateComponenteinRV(componente);

        fieldCodigo.clear();
        textDescrip.clear();
        fieldCantidad.clear();
        fieldUsos.clear();

        calculaParametrosinRV(dataTable.getItems());
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

        labelCostoMat.setText(String.valueOf(costoMaterial));
        labelCostoMano.setText(String.valueOf(costoMano));
        labelCostoEquipo.setText(String.valueOf(costoEquipo));
        labelHH.setText(String.valueOf(hh));
        labelHE.setText(String.valueOf(he));

        labelTotal.setText(String.valueOf(costoMaterial + costoMano + costoEquipo));
    }


    public void handleEliminarMenu(ActionEvent event) {
        componente = dataTable.getSelectionModel().getSelectedItem();
        datos.remove(componente);
        dataTable.getItems().remove(componente);

        if (componente.getTipo().contentEquals("1") || componente.getTipo().contentEquals("2") || componente.getTipo().contentEquals("3")) {
            deleteComponenteRVInsumos(renglon.getId(), componente);
        }

        if (componente.getTipo().contentEquals("J")) {
            deleteComponenteRVJuego(renglon.getId(), componente);
        }

        if (componente.getTipo().contentEquals("S")) {
            deleteComponenteRVSemielaborado(renglon.getId(), componente);
        }
    }

    public void handleHidePanel(ActionEvent event) {
        paneForm.setVisible(false);
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
            rv.setHa(Double.parseDouble(labelHA.getText()));
            rv.setHe(Double.parseDouble(labelHE.getText()));
            rv.setHo(Double.parseDouble(labelHO.getText()));

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


}
