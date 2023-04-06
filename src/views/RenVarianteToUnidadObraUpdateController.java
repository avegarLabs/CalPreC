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
import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class RenVarianteToUnidadObraUpdateController implements Initializable {

    public static List<Empresaobratarifa> empresaobratarifaList;
    private static SessionFactory sf;
    private static String codeSum;
    public double valorEscala;
    public double salarioCalc;
    public double coeficienteEquipo;
    public double coeficienteMano;
    public List<Renglonrecursos> listRecursos;
    public Integer Tnorma;
    public Unidadobra unidadobraO;
    public Runtime garbache;
    RenVarianteToUnidadObraUpdateController renVarianteToUnidadObraUpdateController;
    UnidadesObraObraController unidadesObraObraController;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    ReportProjectStructureSingelton util = ReportProjectStructureSingelton.getInstance();
    @FXML
    private JFXTextField field_codigo;
    @FXML
    private JFXTextField fieldCant;
    @FXML
    private JFXButton btn_add;
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
    private Unidadobrarenglon unidadobrarenglon;
    private Unidadobrarenglon unidadobrarenglonIni;
    private Integer idUO;
    private Integer idRV;
    private double cant;
    private double valor;
    private String pattern;
    private double salario;
    private String umRev;
    private ArrayList<Bajoespecificacion> bajoespecificacionArrayList;
    private Bajoespecificacion bajoespecificacion;
    private Integer idbajo;
    private Integer idObra;
    private RVparaRUOoRNE renglonvariante;
    private Renglonvariante renglonvarianteok;
    @FXML
    private JFXButton btn_exit;
    private int inputLength = 6;
    private double val;
    private boolean flag;
    private ObservableList<SuministrosView> listSuminitros;

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

    public RVparaRUOoRNE getRenglonvariante(int idUO, int idRen) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> datosRenglon = session.createQuery("SELECT rv.codigo, rv.descripcion, rv.um, rv.costomat, rv.costmano, rv.costequip, uor.cantRv, uor.conMat FROM Unidadobrarenglon uor INNER JOIN  Renglonvariante rv ON uor.renglonvarianteId = rv.id WHERE uor.unidadobraId =: idUO AND uor.renglonvarianteId =: idR").setParameter("idUO", idUO).setParameter("idR", idRen).getResultList();

            for (Object[] row : datosRenglon) {
                renglonvariante = new RVparaRUOoRNE(row[0].toString(), row[1].toString(), row[2].toString(), Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString()), Double.parseDouble(row[5].toString()), Double.parseDouble(row[6].toString()), row[7].toString());
            }

            tx.commit();
            session.close();
            return renglonvariante;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return renglonvariante;
    }

    public void UpdateRVtoUnidadObra(Unidadobrarenglon unidadobrareng) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            if (unidadobrarenglonIni.getUnidadobraId() == unidadobrareng.getUnidadobraId() && unidadobrarenglonIni.getRenglonvarianteId() == unidadobrareng.getRenglonvarianteId()) {
                unidadobrarenglon = (Unidadobrarenglon) session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId =: idU AND renglonvarianteId =: idR").setParameter("idU", unidadobrareng.getUnidadobraId()).setParameter("idR", unidadobrareng.getRenglonvarianteId()).getSingleResult();
                unidadobrarenglon.setCantRv(unidadobrareng.getCantRv());
                unidadobrarenglon.setConMat(unidadobrareng.getConMat());
                unidadobrarenglon.setCostMat(unidadobrareng.getCostMat());
                unidadobrarenglon.setCostMano(unidadobrareng.getCostMano());
                unidadobrarenglon.setCostEquip(unidadobrareng.getCostEquip());
                unidadobrarenglon.setSalariomn(unidadobrareng.getSalariomn());
                unidadobrarenglon.setSalariocuc(unidadobrareng.getSalariocuc());
                session.update(unidadobrarenglon);

                if (unidadobraO.getRenglonbase() != null) {
                    if (unidadobraO.getRenglonbase().trim().equals(unidadobrarenglon.getRenglonvarianteByRenglonvarianteId().getCodigo())) {
                        updateCantUnidadObra(unidadobraO.getId(), unidadobrareng.getCantRv());
                    }
                } else {
                    updateCantUnidadObra(unidadobraO.getId(), unidadobrareng.getCantRv());
                }
            } else {

                session.delete(unidadobrarenglonIni);
                session.persist(unidadobrareng);

            }

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private void updateCantUnidadObra(int id, Double cantRv) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            Unidadobra unidadobra = session.get(Unidadobra.class, id);
            unidadobra.setCantidad(cantRv);
            session.update(unidadobra);

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Unidadobrarenglon getUnidadobrarenglon(int idUo, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();
            unidadobrarenglon = (Unidadobrarenglon) session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId = : idU AND renglonvarianteId =: idR ").setParameter("idU", idUo).setParameter("idR", idRV).getSingleResult();
            trx.commit();
            session.close();
            return unidadobrarenglon;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobrarenglon;

    }

    public Renglonvariante rvToSugestion(String code, int tag) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglon -> renglon.getCodigo().trim().equals(code) && renglon.getRs() == tag).findFirst().orElse(null);
    }

    public void handleTipoCosto(ActionEvent event) {
        val = 0.0;
        costMat = 0.0;
        if (checkTipoCosto.isSelected() == true) {
            costTotal = renglonvarianteok.getCostmano() + renglonvarianteok.getCostequip();
            val = Math.round(costTotal * 100d) / 100d;
            labelValue.setText(val + " / " + renglonvariante.getUm());
        } else if (checkTipoCosto.isSelected() == false) {
            if (renglonvarianteok != null) {
                costMat = renglonvarianteok.getCostomat();
                costTotal = renglonvarianteok.getCostomat() + renglonvarianteok.getCostmano() + renglonvarianteok.getCostequip();
                val = Math.round(costTotal * 100d) / 100d;
                labelValue.setText(val + " / " + renglonvariante.getUm());
            }
        }
    }

    public void keyTypeCode(KeyEvent event) {
        if (field_codigo.getText().length() == inputLength) {
            Tnorma = unidadobraO.getObraByObraId().getSalarioId();
            renglonvarianteok = null;
            renglonvarianteok = rvToSugestion(field_codigo.getText(), unidadobraO.getObraByObraId().getSalarioId());
            if (renglonvarianteok == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error");
                alert.setHeaderText("Renglón Variante no encontrado!!");
                alert.showAndWait();
            }
            double costoMano = renglonvarianteok.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * utilCalcSingelton.getValorCosto(renglonrecursos.getRecursosByRecursosId().getGrupoescala()) / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costoEq = renglonvarianteok.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMater = renglonvarianteok.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterSemi = renglonvarianteok.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
            double costMaterJueg = renglonvarianteok.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);

            double mat = costMater + costMaterSemi + costMaterJueg;

            textDedscripcion.setText(renglonvarianteok.getDescripcion());
            if (checkTipoCosto.isSelected() == true) {
                costTotal = Math.round(costoMano * 100d) / 100d + Math.round(costoEq * 100d) / 100d;
                labelValue.setText(costTotal + " / " + renglonvariante.getUm());
            } else {
                costTotal = Math.round(mat * 100d) / 100d + Math.round(costoMano * 100d) / 100d + Math.round(costoEq * 100d) / 100d;
                labelValue.setText(costTotal + " / " + renglonvariante.getUm());
            }
            salario = utilCalcSingelton.calcSalarioRV(renglonvarianteok);
            labelValue.setText(Math.round(costTotal * 100d) / 100d + " / " + renglonvariante.getUm());
            fieldCant.requestFocus();

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renVarianteToUnidadObraUpdateController = this;
        fieldCant.requestFocus();
    }

    public void addUpdateRvToUnidadO(UnidadesObraObraController unidadesObra, Unidadobra unidadobra, Integer idR, boolean action) {
        flag = action;
        labelUoCode.setText("UO: " + unidadobra.getCodigo());
        idUO = unidadobra.getId();
        idRV = 0;
        costMat = 0.0;
        costMano = 0.0;
        costeEquip = 0.0;
        valor = 0.0;
        idObra = unidadobra.getObraId();
        unidadobrarenglonIni = getUnidadobrarenglon(idUO, idR);
        renglonvariante = getRenglonvariante(idUO, idR);
        renglonvarianteok = rvToSugestion(renglonvariante.getCode(), unidadobra.getObraByObraId().getSalarioId());

        idRV = idR;
        unidadobraO = unidadobra;

        coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == unidadobra.getObraId() && eocc.getEmpresaconstructoraId() == unidadobra.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);

        field_codigo.setText(renglonvariante.getCode());
        textDedscripcion.setText(renglonvariante.getDescripcion());
        costMano = Math.round(renglonvariante.getMano() * 100d) / 100d;
        costeEquip = Math.round(renglonvariante.getEquipos() * 100d) / 100d;

        if (renglonvariante.getTipoCosto().trim().equals("0")) {
            checkTipoCosto.setSelected(true);
            costMat = 0.0;
        }
        if (renglonvariante.getTipoCosto().trim().equals("1")) {
            checkTipoCosto.setSelected(false);
            costMat = Math.round(renglonvariante.getMateriales() * 100d) / 100d;
        }

        costTotal = costMat + costMano + costeEquip;
        labelValue.setText(String.valueOf(costTotal));
        fieldCant.setText(String.valueOf(renglonvariante.getCantDecla()));

        unidadesObraObraController = unidadesObra;
        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId(), unidadobra.getObraByObraId().getTarifaId());

        listSuminitros = FXCollections.observableArrayList();
        listSuminitros = utilCalcSingelton.getSuministrosViewObservableList(unidadobra.getObraByObraId().getSalarioBySalarioId().getTag());

    }

    public void handleUpdateRenglonVariante(ActionEvent event) {
        double cantRV = Double.parseDouble(fieldCant.getText());
        unidadobrarenglon = new Unidadobrarenglon();
        unidadobrarenglon.setUnidadobraId(idUO);
        unidadobrarenglon.setRenglonvarianteId(renglonvarianteok.getId());
        unidadobrarenglon.setCantRv(cantRV);

        // double costoMano = new BigDecimal(String.format("%.2f", utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvariante))).doubleValue();
        double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvarianteok);
        // double costoEq = new BigDecimal(String.format("%.2f", renglonvariante.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum))).doubleValue();
        double costoEq = renglonvarianteok.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
        double costMater = renglonvarianteok.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
        double costMaterSemi = renglonvarianteok.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
        double costMaterJueg = renglonvarianteok.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);


        if (checkTipoCosto.isSelected() == true) {
            unidadobrarenglon.setConMat("0 ");
            unidadobrarenglon.setCostMat(0.0);
        } else {
            // double mat = new BigDecimal(String.format("%.2f", costMater + costMaterSemi + costMaterJueg)).doubleValue();
            double mat = costMater + costMaterSemi + costMaterJueg;
            unidadobrarenglon.setConMat("1 ");
            unidadobrarenglon.setCostMat(mat * cantRV);
        }
        unidadobrarenglon.setCostMano(costoMano * cantRV * coeficienteMano);
        unidadobrarenglon.setCostEquip(costoEq * cantRV * coeficienteEquipo);
        if (salario == 0.0) {
            salario = utilCalcSingelton.calcSalarioRV(renglonvarianteok);
        }
        //unidadobrarenglon.setSalariomn(new BigDecimal(String.format("%.2f", salario)).doubleValue() * cantRV);
        unidadobrarenglon.setSalariomn(salario * cantRV);

        unidadobrarenglon.setSalariocuc(0.0);

        UpdateRVtoUnidadObra(unidadobrarenglon);

        if (checkTipoCosto.isSelected() && bajoespecificacionArrayList != null) {
            for (Bajoespecificacion bajo : bajoespecificacionArrayList) {
                idbajo = addBajoEspecificacion(bajo);
            }
            bajoespecificacionArrayList.clear();

        }

        /*
        if (flag == true) {
            reclacValuesOfUO(unidadobraO.getCantidad(), unidadobraO, cantRV);
        }*/

        unidadesObraObraController.addElementTableRV(unidadobraO);
        unidadesObraObraController.addElementTableSuministros(idUO);

        Stage stage = (Stage) btn_add.getScene().getWindow();
        unidadesObraObraController.calcularElementosUO();
        stage.close();
    }

    //private void reclacValuesOfUO(double cantIni, Unidadobra unidad, double cant) {
    //calcAllUORenglonList(cantIni, unidadobraO.getUnidadobrarenglonsById().parallelStream().filter(item -> !item.getRenglonvarianteByRenglonvarianteId().getCodigo().trim().equals(unidad.getRenglonbase())).collect(Collectors.toSet()), cant);
    //calcAllBajoEsp(cantIni, unidadobraO.getUnidadobrabajoespecificacionById(), cant);
    //}

    private void calcAllBajoEsp(double cantIni, Collection<Bajoespecificacion> unidadobrabajoespecificacionById, double cantF) {
        List<Bajoespecificacion> bajoespecificacionListUpdates = new ArrayList<>();
        for (Bajoespecificacion bajoespecificacion : unidadobrabajoespecificacionById) {
            SuministrosView suministrosView = getSuministrosToUpdate(bajoespecificacion.getIdSuministro());
            double cant = calcVolSuminitro(cantIni, bajoespecificacion.getCantidad(), cantF);
            double costo = suministrosView.getPreciomn() * cant;
            if (bajoespecificacion.getCantidad() != cant) {
                bajoespecificacion.setCantidad(cant);
                bajoespecificacion.setCosto(costo);
                bajoespecificacionListUpdates.add(bajoespecificacion);
            }
        }
        if (bajoespecificacionListUpdates.size() > 0) {
            utilCalcSingelton.updateRecurBajoUOList(bajoespecificacionListUpdates);
        }
    }

    private SuministrosView getSuministrosToUpdate(int idRec) {
        return listSuminitros.parallelStream().filter(item -> item.getId() == idRec).findFirst().get();
    }

    private void calcAllUORenglonList(double cantIni, Collection<Unidadobrarenglon> unidadobrarenglonsById, double cant) {
        List<Unidadobrarenglon> unidadobrarenglonListUpdates = new ArrayList<>();
        for (Unidadobrarenglon ur : unidadobrarenglonsById) {
            double cantCal = calcVolRenglon(cantIni, ur.getCantRv(), cant);

            if (ur.getCantRv() != cantCal) {
                double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(ur.getRenglonvarianteByRenglonvarianteId());
                double costoEq = ur.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costMater = ur.getRenglonvarianteByRenglonvarianteId().getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costMaterSemi = ur.getRenglonvarianteByRenglonvarianteId().getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
                double costMaterJueg = ur.getRenglonvarianteByRenglonvarianteId().getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);


                ur.setCantRv(cantCal);
                if (ur.getConMat().trim().equals("0")) {
                    ur.setConMat("0 ");
                    ur.setCostMat(0.0);
                } else {
                    double mat = costMater + costMaterSemi + costMaterJueg;
                    ur.setConMat("1 ");
                    ur.setCostMat(mat * cantCal);
                }
                ur.setCostMano(costoMano * cantCal);
                ur.setCostEquip(costoEq * cantCal);
                if (salario == 0.0) {
                    salario = utilCalcSingelton.calcSalarioRV(ur.getRenglonvarianteByRenglonvarianteId());
                }
                ur.setSalariomn(salario * cantCal);
            }
            unidadobrarenglonListUpdates.add(ur);
        }

        if (unidadobrarenglonListUpdates.size() > 0) {
            utilCalcSingelton.updatesUnidadesObraRenglones(unidadobrarenglonListUpdates);
        }
    }

    private double calcVolRenglon(double volIniUO, double volRenIni, double volFinUO) {
        double mult = volRenIni * volFinUO;
        double finalCant = mult / volIniUO;
        return new BigDecimal(String.format("%.4f", finalCant)).doubleValue();
    }

    private double calcVolSuminitro(double volIniUO, double volRecIni, double volFinUO) {
        double mult = volRecIni * volFinUO;
        double finalCant = mult / volIniUO;
        return new BigDecimal(String.format("%.4f", finalCant)).doubleValue();
    }


    public void clearField() {
        field_codigo.clear();
        fieldCant.clear();
        labelValue.setText(" ");
        checkTipoCosto.setSelected(false);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("suministrosInRVUpdate.fxml"));
                Parent proot = loader.load();
                SuministrosInRVUpdateController controller = loader.getController();
                controller.datosRV(renVarianteToUnidadObraUpdateController, idRV, cant, idObra);


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


    public void handleHojadeCalculo(ActionEvent event) {
        if (!field_codigo.getText().isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HojadeCalculo.fxml"));
                Parent proot = loader.load();
                HojadeCalcauloController controller = (HojadeCalcauloController) loader.getController();
                controller.LlenarDatosRV(renglonvarianteok, unidadobraO.getObraByObraId(), unidadobraO.getEmpresaconstructoraId());

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

    public Integer addBajoEspecificacion(Bajoespecificacion bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        Integer idBe = null;
        Bajoespecificacion bajo = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: unidad AND idSuministro =: suministro").setParameter("unidad", bajoespecificacion.getUnidadobraId()).setParameter("suministro", bajoespecificacion.getIdSuministro());

            try {
                bajo = (Bajoespecificacion) query.getSingleResult();
            } catch (NoResultException nre) {
            }
            if (bajo != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" El Suministro " + getCodigo(bajo.getIdSuministro()) + " es parte de la unidad de obra, escoja Sumar si desea incrementar los valores o Sobreescribir si desea desechar los valores anteriores  ");

                ButtonType buttonAgregar = new ButtonType("Sumar");
                ButtonType buttonSobre = new ButtonType("Sobreescribir");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonAgregar, buttonSobre, buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonAgregar) {
                    sumarCantidadesSuministro(bajoespecificacion);
                    unidadesObraObraController.addElementTableSuministros(bajoespecificacion.getUnidadobraId());
                } else if (result.get() == buttonSobre) {
                    sobreEscribirSuministroUnidad(bajoespecificacion);
                    unidadesObraObraController.addElementTableSuministros(bajoespecificacion.getUnidadobraId());
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

    private void sobreEscribirSuministroUnidad(Bajoespecificacion bajoespecificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv");
            query.setParameter("idUni", bajoespecificacion.getUnidadobraId());
            query.setParameter("idRv", bajoespecificacion.getIdSuministro());

            Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
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

    private void sumarCantidadesSuministro(Bajoespecificacion bajoespecificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUni AND idSuministro =: idRv");
            query.setParameter("idUni", bajoespecificacion.getUnidadobraId());
            query.setParameter("idRv", bajoespecificacion.getIdSuministro());

            if (bajoespecificacion.getTipo().trim().equals("1")) {
                Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdSuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            } else if (bajoespecificacion.getTipo().trim().equals("S")) {
                Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdSuministro());
                Bajoespecificacion bajo = (Bajoespecificacion) query.getSingleResult();
                if (bajo != null) {
                    double cantidad = bajo.getCantidad() + bajoespecificacion.getCantidad();
                    bajo.setCantidad(cantidad);
                    bajo.setCosto(Math.round(recursos.getPreciomn() * 100d) / 100d * cantidad);
                    bajo.setTipo(bajoespecificacion.getTipo());
                    session.update(bajo);
                }
            } else if (bajoespecificacion.getTipo().trim().equals("J")) {
                Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdSuministro());
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


    public void createBajoespecificacionListUpdate(ArrayList<SumRVView> listOfSuminitrso) {
        bajoespecificacionArrayList = new ArrayList<Bajoespecificacion>();
        if (listOfSuminitrso != null) {
            for (SumRVView sumRVView : listOfSuminitrso) {
                bajoespecificacion = new Bajoespecificacion();
                bajoespecificacion.setUnidadobraId(idUO);
                bajoespecificacion.setIdSuministro(sumRVView.getId());
                bajoespecificacion.setTipo(sumRVView.getTipo());
                bajoespecificacion.setCantidad(sumRVView.getCantidad());
                bajoespecificacion.setCosto(Math.round(sumRVView.getCosto() * sumRVView.getCantidad() * 100d) / 100d);
                bajoespecificacion.setSumrenglon(0);
                bajoespecificacion.setRenglonvarianteId(0);

                bajoespecificacionArrayList.add(bajoespecificacion);

            }
        }


    }

    public void getRenglonVarianteFronHelp(Renglonvariante renglonvariante) {
        field_codigo.clear();
        renglonvarianteok = null;
        renglonvarianteok = renglonvariante;
        idRV = 0;
        idRV = renglonvariante.getId();
        field_codigo.setText(renglonvariante.getCodigo());
        textDedscripcion.setText(renglonvariante.getDescripcion());

        if (checkTipoCosto.isSelected() == false) {
            costTotal = renglonvarianteok.getCostomat() + renglonvarianteok.getCostmano() + renglonvarianteok.getCostequip();
            labelValue.setText(Math.round(costTotal * 100d) / 100d + " / " + renglonvarianteok.getUm());
            costMat = Math.round(renglonvarianteok.getCostomat() * 100d) / 100d;
            costMano = Math.round(renglonvarianteok.getCostmano() * 100d) / 100d;
            costeEquip = Math.round(renglonvarianteok.getCostequip() * 100d) / 100d;
            salario = utilCalcSingelton.calcSalarioRV(renglonvariante);
        } else {
            costTotal = renglonvarianteok.getCostmano() + renglonvarianteok.getCostequip();
            labelValue.setText(Math.round(costTotal * 100d) / 100d + " / " + renglonvarianteok.getUm());
            costMano = Math.round(renglonvarianteok.getCostmano() * 100d) / 100d;
            costeEquip = Math.round(renglonvarianteok.getCostequip() * 100d) / 100d;
            salario = utilCalcSingelton.calcSalarioRV(renglonvariante);
        }
    }

    public void handleSearhRenglones(ActionEvent event) {
        pattern = field_codigo.getText();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhRVUOUpdate.fxml"));
            Parent proot = loader.load();
            SearchRVUOUpdateController controller = loader.getController();
            controller.setRVDatos(renVarianteToUnidadObraUpdateController, pattern, unidadobraO.getObraByObraId().getSalarioId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.toFront();
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleCloseRVtoUO(ActionEvent event) {

        unidadesObraObraController.calcularElementosUO();
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }


}
