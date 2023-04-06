package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class RenVarianteToRenglonObraController implements Initializable {

    private static SessionFactory sf;
    private static String codeSum;
    public RenVarianteToRenglonObraController renVarianteToRenglonObraController;
    public RenglonVarianteObraController renglonVarianteObraController;
    public Integer idObra;
    public double valorEscala;
    public double salarioCalc;
    public double coeficienteEquipo;
    public double coeficienteMano;
    public List<Empresaobratarifa> empresaobratarifaList;
    public List<Renglonrecursos> listRecursos;
    public Integer Tnorma;
    public Nivelespecifico unidadobraO;
    public JFXButton btn_close;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField fieldCant;
    @FXML
    private JFXButton btn_add;
    @FXML
    private JFXButton btn_exit;
    @FXML
    private Label labelUoCode;
    @FXML
    private TextArea textDedscripcion;
    @FXML
    private Label labelValue;
    @FXML
    private JFXButton btnSum;
    @FXML
    private JFXButton btnHoja;
    @FXML
    private JFXButton btnHelp;
    @FXML
    private JFXCheckBox checkTipoCosto;
    private ArrayList<Renglonvariante> renglonvarianteArrayList;
    private ObservableList<String> listRenglonVariante;
    private double costMat;
    private double costMano;
    private double costeEquip;
    private double costTotal;
    private double salario;
    private Renglonnivelespecifico nivelrenglon;
    private Integer idUO;
    private Integer idRV;
    private double cant;
    private String pattern;
    private String umRev;
    private Integer idbajo;
    private Integer tipoC;
    private ArrayList<Bajoespecificacionrv> bajoespecificacionArrayList;
    private Bajoespecificacionrv bajoespecificacion;
    private Renglonvariante renglonvariante;
    private int inputLength = 6;
    private double val;
    private Double calcTotal;
    private ObservableList<SuministrosView> listSuminitros;
    private int countsub;
    private int batchSub = 50;

    public static String getCodigo(int idR) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            codeSum = null;

            Recursos rec = session.get(Recursos.class, idR);
            if (rec != null) {
                codeSum = rec.getCodigo();
            } else {
                Suministrossemielaborados sum = session.get(Suministrossemielaborados.class, idR);
                codeSum = sum.getCodigo();
            }

            tx.commit();
            session.close();
            return codeSum;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return codeSum;
    }

    public Integer saveNewRNE(Renglonnivelespecifico rn) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idRN = null;
        try {
            tx = session.beginTransaction();
            idRN = (Integer) session.save(rn);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idRN;
    }

    public List<Renglonrecursos> getRecursosinRV(int idR) {

        listRecursos = new LinkedList<>();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglonvariante = session.get(Renglonvariante.class, idR);
            listRecursos = renglonvariante.getRenglonrecursosById();
            listRecursos.size();

            tx.commit();
            session.close();
            return listRecursos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();

    }

    public Renglonvariante rvToSugestion(String code, int tag) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglon -> renglon.getCodigo().trim().equals(code) && renglon.getRs() == tag).findFirst().orElse(null);
    }

    public void addRVtoNivelEspecifico(Renglonnivelespecifico renglonnivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        try {
            trx = session.beginTransaction();
            session.persist(renglonnivelespecifico);
            trx.commit();
        } catch (PersistenceException pe) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" El Renglón Variante " + field_codigo.getText() + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");

            ButtonType buttonAgregar = new ButtonType("Sumar");
            ButtonType buttonSobre = new ButtonType("Sobreescribir");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonAgregar) {

                sumarCantidadesRV(renglonnivelespecifico);
                renglonVarianteObraController.addElementTableRV(idUO);

            } else if (result.get() == buttonSobre) {

                sobreEscribirRenglonUnidad(renglonnivelespecifico);
                renglonVarianteObraController.addElementTableRV(idUO);
            }
        } finally {
            session.close();
        }

    }

    public void sobreEscribirRenglonUnidad(Renglonnivelespecifico unidadobrarenglon) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: idUni AND renglonvarianteId =: idRv").setParameter("idUni", unidadobrarenglon.getNivelespecificoId()).setParameter("idRv", unidadobrarenglon.getRenglonvarianteId());
            Renglonnivelespecifico unidadobrareng = (Renglonnivelespecifico) query.getSingleResult();
            double oldCant = unidadobrareng.getCantidad();
            if (unidadobrareng != null) {
                unidadobrareng.setCantidad(unidadobrarenglon.getCantidad());
                unidadobrareng.setCostmaterial(unidadobrarenglon.getCostmaterial());
                unidadobrareng.setCostmano(unidadobrarenglon.getCostmano());
                unidadobrareng.setCostequipo(unidadobrarenglon.getCostequipo());
                unidadobrareng.setSalario(unidadobrarenglon.getSalario());
                unidadobrareng.setSalariocuc(unidadobrarenglon.getSalario());
                unidadobrareng.setConmat(unidadobrarenglon.getConmat());
                session.update(unidadobrareng);
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

    private void sumarCantidadesRV(Renglonnivelespecifico renglonnivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Renglonnivelespecifico rn = new Renglonnivelespecifico();
            Query query = session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: idUni AND renglonvarianteId =: idRv").setParameter("idUni", renglonnivelespecifico.getNivelespecificoId()).setParameter("idRv", renglonnivelespecifico.getRenglonvarianteId());
            Renglonnivelespecifico unidadobrareng = (Renglonnivelespecifico) query.getSingleResult();
            double cantidad = unidadobrareng.getCantidad() + renglonnivelespecifico.getCantidad();
            unidadobrareng.setCantidad(cantidad);
            if (renglonnivelespecifico.getConmat().trim().equals("0")) {
                rn.setCostmaterial(0.0);
            } else if (renglonnivelespecifico.getConmat().trim().equals("1")) {
                rn.setCostmaterial(unidadobrareng.getRenglonvarianteByRenglonvarianteId().getCostomat() * cantidad);
            }
            unidadobrareng.setCostmano(unidadobrareng.getRenglonvarianteByRenglonvarianteId().getCostmano() * cantidad);
            unidadobrareng.setCostequipo(unidadobrareng.getRenglonvarianteByRenglonvarianteId().getCostequip() * cantidad);
            unidadobrareng.setSalario(utilCalcSingelton.calcSalarioRV(unidadobrareng.getRenglonvarianteByRenglonvarianteId()) * cantidad);
            unidadobrareng.setConmat(renglonnivelespecifico.getConmat());
            session.update(unidadobrareng);
            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        renVarianteToRenglonObraController = this;


    }


    public void clearField() {
        field_codigo.clear();
        fieldCant.clear();
        labelValue.setText(" ");
        textDedscripcion.clear();
    }

    public void handleSumRVView(ActionEvent event) {

        if (fieldCant.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Debe especificar la cantidad de Renglón Variante a utilizar!");
            alert.showAndWait();
        } else {
            cant = Double.valueOf(fieldCant.getText());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("suministrosInRVtoRV.fxml"));
                Parent proot = loader.load();

                SuministrosInRVtoRVController controller = loader.getController();
                controller.datosRV(renVarianteToRenglonObraController, renglonvariante.getId(), cant, idObra);


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setResizable(false);
                stage.toFront();
                stage.show();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handleAyudaView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonesVariantesRV.fxml"));
            Parent proot = loader.load();

            RenglonesRVController controller = loader.getController();
            controller.pasarDatos(renVarianteToRenglonObraController, Tnorma);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleHojadeCalculo(ActionEvent event) {
        if (!field_codigo.getText().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HojadeCalculo.fxml"));
                Parent proot = loader.load();
                HojadeCalcauloController controller = loader.getController();
                controller.LlenarDatosRV(renglonvariante, unidadobraO.getObraByObraId(), unidadobraO.getEmpresaconstructoraId());


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Debe especificar el código del Renglón Variante!");
            alert.showAndWait();
        }
    }

    public void addUnidadObra(RenglonVarianteObraController unidadesObraO, Nivelespecifico unidadobra) {

        unidadobraO = unidadobra;
        idObra = structureSingelton.getObra(unidadobra.getObraId()).getId();
        labelUoCode.setText("Nivel: " + unidadobra.getCodigo());
        idUO = unidadobra.getId();

        coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);

        tipoC = unidadobra.getObraByObraId().getDefcostmat();
        Tnorma = unidadobra.getObraByObraId().getSalarioId();


        checkTipoCosto.setSelected(true);

        renglonVarianteObraController = unidadesObraO;

        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId(), unidadobra.getObraByObraId().getTarifaId());

        listSuminitros = FXCollections.observableArrayList();
        listSuminitros = utilCalcSingelton.getSuministrosViewObservableList(unidadobra.getObraByObraId().getSalarioBySalarioId().getTag());

    }


    public void handleAddRenglonVariante(ActionEvent event) {

        if (fieldCant.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Información");
            alert.setContentText("Debe indicar la cantidad del renglón variante!");
            alert.showAndWait();
        } else {
            nivelrenglon = new Renglonnivelespecifico();
            nivelrenglon.setNivelespecificoId(idUO);
            nivelrenglon.setRenglonvarianteId(renglonvariante.getId());
            nivelrenglon.setCantidad(Double.parseDouble(fieldCant.getText()));

            double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvariante);
            double costoEq = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMater = renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterSemi = renglonvariante.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterJueg = renglonvariante.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);

            if (checkTipoCosto.isSelected() == true) {
                nivelrenglon.setConmat("0 ");
                nivelrenglon.setCostmaterial(0.0);
            } else {
                //double mat = new BigDecimal(String.format("%.2f", costMater + costMaterSemi + costMaterJueg)).doubleValue();
                double mat = costMater + costMaterSemi + costMaterJueg;
                nivelrenglon.setConmat("1 ");
                nivelrenglon.setCostmaterial(mat * Double.parseDouble(fieldCant.getText()));
            }
            nivelrenglon.setCostmano(costoMano * Double.parseDouble(fieldCant.getText()) * coeficienteMano);
            nivelrenglon.setCostequipo(costoEq * Double.parseDouble(fieldCant.getText()) * coeficienteEquipo);
            nivelrenglon.setSalario(salario * Double.parseDouble(fieldCant.getText()));
            nivelrenglon.setSalariocuc(0.0);
            addRVtoNivelEspecifico(nivelrenglon);


            if (checkTipoCosto.isSelected() && bajoespecificacionArrayList != null) {
                for (Bajoespecificacionrv bajo : bajoespecificacionArrayList) {
                    idbajo = addBajoEspecificacion(bajo);
                }
                bajoespecificacionArrayList.clear();
            }

            clearField();
            renglonVarianteObraController.addElementTableRV(idUO);
            renglonVarianteObraController.addElementTableSuministros(idUO, renglonvariante.getId());
            renglonVarianteObraController.completeValuesNivelEspecifico(idUO);
            renglonVarianteObraController.calElementNivelEspecifico();
            field_codigo.requestFocus();


        }
    }

    public void getRenglonVarianteFronHelp(Renglonvariante renglonvariantehelp) {

        renglonvariante = null;
        renglonvariante = renglonvariantehelp;
        field_codigo.clear();
        field_codigo.setText(renglonvariante.getCodigo());
        textDedscripcion.setText(renglonvariante.getDescripcion());
        costTotal = renglonvariante.getCostomat() + renglonvariante.getCostmano() + renglonvariante.getCostequip();
        salario = utilCalcSingelton.calcSalarioRV(renglonvariante);

        if (checkTipoCosto.isSelected() == false) {
            labelValue.setText(costTotal + " / " + renglonvariante.getUm());
        } else if (checkTipoCosto.isSelected() == true) {
            calcTotal = null;
            calcTotal = renglonvariante.getCostmano() + renglonvariante.getCostequip();
            labelValue.setText(calcTotal + " / " + renglonvariante.getUm());
        }

    }

    public Integer addBajoEspecificacion(Bajoespecificacionrv bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idBe = null;
        Bajoespecificacionrv bajo = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: unidad AND idsuministro =: suministro AND renglonvarianteId =: renv").setParameter("unidad", bajoespecificacion.getNivelespecificoId()).setParameter("suministro", bajoespecificacion.getIdsuministro()).setParameter("renv", bajoespecificacion.getRenglonvarianteId());

            try {
                bajo = (Bajoespecificacionrv) query.getSingleResult();
            } catch (NoResultException nre) {
            }
            if (bajo != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" El Suministro " + getCodigo(bajo.getIdsuministro()) + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");

                ButtonType buttonAgregar = new ButtonType("Sumar");
                ButtonType buttonSobre = new ButtonType("Sobreescribir");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonAgregar) {

                    sumarCantidadesSuministro(bajoespecificacion);
                    renglonVarianteObraController.addElementTableSuministros(bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getRenglonvarianteId());

                } else if (result.get() == buttonSobre) {

                    sobreEscribirSuministroUnidad(bajoespecificacion);
                    renglonVarianteObraController.addElementTableSuministros(bajoespecificacion.getNivelespecificoId(), bajoespecificacion.getRenglonvarianteId());

                }

            } else {
                idBe = (Integer) session.save(bajoespecificacion);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return idBe;

    }

    private void sobreEscribirSuministroUnidad(Bajoespecificacionrv bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUni AND idsuministro =: idSum AND renglonvarianteId =: idRen");
            query.setParameter("idUni", bajoespecificacion.getNivelespecificoId());
            query.setParameter("idSum", bajoespecificacion.getIdsuministro());
            query.setParameter("idRen", bajoespecificacion.getRenglonvarianteId());

            Bajoespecificacionrv bajo = (Bajoespecificacionrv) query.getSingleResult();
            if (bajo != null) {
                bajo.setCantidad(bajoespecificacion.getCantidad());
                bajo.setCosto(bajoespecificacion.getCosto());
                bajo.setTipo(bajoespecificacion.getTipo());
                session.update(bajo);
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void sumarCantidadesSuministro(Bajoespecificacionrv bajoespecificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUni AND idsuministro =: idSum AND renglonvarianteId =: idReng");
            query.setParameter("idUni", bajoespecificacion.getNivelespecificoId());
            query.setParameter("idSum", bajoespecificacion.getIdsuministro());
            query.setParameter("idReng", bajoespecificacion.getRenglonvarianteId());

            if (bajoespecificacion.getTipo().trim().equals("1")) {
                Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdsuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            } else if (bajoespecificacion.getTipo().trim().equals("S")) {
                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdsuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            } else if (bajoespecificacion.getTipo().trim().equals("J")) {
                Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdsuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            }
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void createBajoespecificacionList(ArrayList<SumRVView> listOfSuminitrso) {

        bajoespecificacionArrayList = new ArrayList<Bajoespecificacionrv>();
        if (listOfSuminitrso != null) {
            for (SumRVView sumRVView : listOfSuminitrso) {
                bajoespecificacion = new Bajoespecificacionrv();
                bajoespecificacion.setNivelespecificoId(idUO);
                bajoespecificacion.setIdsuministro(sumRVView.getId());
                bajoespecificacion.setRenglonvarianteId(renglonvariante.getId());
                bajoespecificacion.setTipo(sumRVView.getTipo());
                bajoespecificacion.setCantidad(sumRVView.getCantidad());
                bajoespecificacion.setCosto(Math.round(sumRVView.getCosto() * sumRVView.getCantidad() * 100d) / 100d);
                bajoespecificacionArrayList.add(bajoespecificacion);
            }
        }
    }

    public void handleSearhRenglones(ActionEvent event) {
        pattern = field_codigo.getText();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhRVChangeRV.fxml"));
            Parent proot = loader.load();
            SearchRVChangeRVController controller = loader.getController();
            controller.setRVDatos(renVarianteToRenglonObraController, pattern, unidadobraO.getObraByObraId().getSalarioId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handleCloseRVtoUO(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            Tnorma = unidadobraO.getObraByObraId().getSalarioId();
            renglonvariante = rvToSugestion(field_codigo.getText(), unidadobraO.getObraByObraId().getSalarioId());
            if (renglonvariante == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setHeaderText("Renglón Variante no encontrado!!");
                alert.showAndWait();
            }
            textDedscripcion.setText(renglonvariante.getDescripcion());
            if (checkTipoCosto.isSelected() == true) {
                costTotal = Math.round(renglonvariante.getCostmano() * 100d) / 100d + Math.round(renglonvariante.getCostequip() * 100d) / 100d;
                labelValue.setText(costTotal + " / " + renglonvariante.getUm().trim());
            } else {
                costTotal = Math.round(renglonvariante.getCostomat() * 100d) / 100d + Math.round(renglonvariante.getCostmano() * 100d) / 100d + Math.round(renglonvariante.getCostequip() * 100d) / 100d;
                labelValue.setText(costTotal + " / " + renglonvariante.getUm().trim());
            }
            salario = utilCalcSingelton.calcSalarioRV(renglonvariante);
            labelValue.setText(Math.round(costTotal * 100d) / 100d + " / " + renglonvariante.getUm().trim());
            fieldCant.requestFocus();
        }
    }

    public void handleTipoCosto(ActionEvent event) {
        val = 0.0;
        if (checkTipoCosto.isSelected() == true) {
            costTotal = renglonvariante.getCostmano() + renglonvariante.getCostequip();
            val = costTotal;
            labelValue.setText(val + " / " + renglonvariante.getUm().trim());
        } else if (checkTipoCosto.isSelected() == false) {
            if (renglonvariante != null) {
                costTotal = renglonvariante.getCostomat() + renglonvariante.getCostmano() + renglonvariante.getCostequip();
                val = costTotal;
                labelValue.setText(val + " / " + renglonvariante.getUm().trim());
            }
        }
    }

    private void racalcVolumenesOfBajoEspecificacion(Integer idUO, Integer idRV, Double cantidad, double cantidadOll) {
        List<Bajoespecificacionrv> bajoespecificacionrvs = utilCalcSingelton.getBajoespecificacionrvList(idUO, idRV);
        List<Bajoespecificacionrv> updateList = new ArrayList<>();
        List<Bajoespecificacionrv> toDeleteTemp = new ArrayList<>();
        for (Bajoespecificacionrv bajoespecificacionrv : bajoespecificacionrvs) {
            SuministrosView suministrosView = getSuministrosToUpdate(bajoespecificacionrv.getIdsuministro());
            if (suministrosView == null) {
                toDeleteTemp.add(bajoespecificacionrv);
            } else {
                double cant = calcVolSuminitro(cantidadOll, bajoespecificacionrv.getCantidad(), cantidad);
                System.out.println("cant --> " + cantidadOll + " -- " + bajoespecificacionrv.getCantidad() + " *** " + cantidad);
                double costo = suministrosView.getPreciomn() * cant;
                if (bajoespecificacionrv.getCantidad() != cant) {
                    bajoespecificacionrv.setCantidad(cant);
                    bajoespecificacionrv.setCosto(costo);
                    updateList.add(bajoespecificacionrv);
                }
            }
        }
        if (toDeleteTemp.size() > 0) {
            utilCalcSingelton.deleteBajoEspecificacionRenglonList(toDeleteTemp);
        }
        if (updateList.size() > 0) {
            updateAll(updateList);
        }

    }

    private double calcVolSuminitro(double volIniRV, double volRecIni, double volFinRV) {
        double mult = volRecIni * volFinRV;
        double finalCant = mult / volIniRV;

        return new BigDecimal(String.format("%.4f", finalCant)).doubleValue();
    }

    private SuministrosView getSuministrosToUpdate(int idRec) {
        return listSuminitros.parallelStream().filter(item -> item.getId() == idRec).findFirst().orElse(null);
    }

    private void updateAll(List<Bajoespecificacionrv> datos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countsub = 0;
            for (Bajoespecificacionrv items : datos) {
                countsub++;
                if (countsub > 0 && countsub % batchSub == 0) {
                    session.flush();
                    session.clear();
                }
                session.update(items);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {

        }
    }


}
