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

public class RenVarianteToRenglonObraUpdateController implements Initializable {

    private static SessionFactory sf;
    private static String codeSum;
    public double valorEscala;
    public double salarioCalc;
    public double coeficienteEquipo;
    public double coeficienteMano;
    public List<Empresaobratarifa> empresaobratarifaList;
    public List<Renglonrecursos> listRecursos;
    public Integer Tnorma;
    public Nivelespecifico unidadobraO;
    public int inputLength = 6;
    RenVarianteToRenglonObraUpdateController renVarianteToUnidadObraUpdateController;
    RenglonVarianteObraController renglonVarianteObraController;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
    ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
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
    private Renglonnivelespecifico unidadobrarenglon;
    private Renglonnivelespecifico unidadobrarenglonIni;
    private Integer idUO;
    private Integer idRV;
    private double cant;
    private double valor;
    private double salario;
    private String umRev;
    private String pattern;
    private ArrayList<Bajoespecificacionrv> bajoespecificacionArrayList;
    private List<Bajoespecificacionrv> bajoespecificacionInsertArrayList;
    private Bajoespecificacionrv bajoespecificacion;
    private Integer idbajo;
    private int idObra;
    private RVparaRUOoRNE renglonvariante;
    private Renglonvariante renglonvarianteok;
    private double val;
    private int countsub;
    private int batchSub = 10;
    private ObservableList<SuministrosView> listSuminitros;
    private double cantOllRV;

    public static List<Bajoespecificacionrv> getBajoespecificacionrvList(int idUO, int idRen) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacionrv> renglonDatos = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUO AND renglonvarianteId =: idR").setParameter("idUO", idUO).setParameter("idR", idRen).getResultList();

            tx.commit();
            session.close();
            return renglonDatos;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

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

    public List<Renglonrecursos> getRecursosinRV(int idR) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            listRecursos = new LinkedList<>();
            renglonvarianteok = session.get(Renglonvariante.class, idR);
            listRecursos = renglonvarianteok.getRenglonrecursosById();
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

    public RVparaRUOoRNE getRenglonvariante(int idUO, int idRen) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> renglonDatos = session.createQuery("SELECT rv.codigo, rv.descripcion, rv.um, rv.costomat, rv.costmano, rv.costequip, uor.cantidad, uor.conmat FROM Renglonnivelespecifico uor INNER JOIN  Renglonvariante rv ON uor.renglonvarianteId = rv.id WHERE uor.nivelespecificoId =: idUO AND uor.renglonvarianteId =: idR").setParameter("idUO", idUO).setParameter("idR", idRen).getResultList();
            for (Object[] row : renglonDatos) {
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

    public Integer getIdObra(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Nivelespecifico unidadobra = session.get(Nivelespecifico.class, idUO);

            tx.commit();
            session.close();
            return unidadobra.getId();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idObra;
    }

    public Renglonnivelespecifico getUnidadobrarenglon(int idUo, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            unidadobrarenglon = (Renglonnivelespecifico) session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId = : idU AND renglonvarianteId =: idR ").setParameter("idU", idUo).setParameter("idR", idRV).getSingleResult();

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

    public void handleTipoCosto(ActionEvent event) {
        val = 0.0;
        costMat = 0.0;
        if (checkTipoCosto.isSelected() == true) {
            costTotal = renglonvarianteok.getCostmano() + renglonvarianteok.getCostequip();
            val = costTotal;
            labelValue.setText(val + " / " + renglonvarianteok.getUm());
        } else if (checkTipoCosto.isSelected() == false) {
            if (renglonvariante != null) {
                costMat = renglonvarianteok.getCostomat();
                costTotal = renglonvarianteok.getCostomat() + renglonvarianteok.getCostmano() + renglonvarianteok.getCostequip();
                val = costTotal;
                labelValue.setText(val + " / " + renglonvarianteok.getUm());
            }
        }
    }

    public void UpdateRVtoUnidadObra(Renglonnivelespecifico unidadobrareng) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();
            session.delete(unidadobrarenglonIni);
            session.persist(unidadobrareng);

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renVarianteToUnidadObraUpdateController = this;
    }

    public void addUpdateRvToUnidadO(RenglonVarianteObraController controller, Nivelespecifico nivelespecifico, Integer idR) {
        cantOllRV = 0.0;
        renglonVarianteObraController = controller;
        labelUoCode.setText("Nivel Especifico: " + nivelespecifico.getCodigo());
        idUO = nivelespecifico.getId();
        idRV = 0;
        costMat = 0.0;
        costMano = 0.0;
        costeEquip = 0.0;
        valor = 0.0;
        idObra = structureSingelton.getObra(nivelespecifico.getObraId()).getId();
        unidadobrarenglonIni = getUnidadobrarenglon(idUO, idR);
        cantOllRV = unidadobrarenglonIni.getCantidad();
        renglonvariante = getRenglonvariante(idUO, idR);
        renglonvarianteok = rvToSugestion(renglonvariante.getCode(), nivelespecifico.getObraByObraId().getSalarioId());
        bajoespecificacionInsertArrayList = new ArrayList<>();
        bajoespecificacionInsertArrayList = getBajoespecificacionrvList(idUO, idR);
        idRV = idR;

        unidadobraO = nivelespecifico;

        Tnorma = nivelespecifico.getObraByObraId().getSalarioId();

        coeficienteMano = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == nivelespecifico.getObraId() && eocc.getEmpresaconstructoraId() == nivelespecifico.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 2).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);
        coeficienteEquipo = structureSingelton.getValCoeficienteList().parallelStream().filter(eocc -> eocc.getObraId() == nivelespecifico.getObraId() && eocc.getEmpresaconstructoraId() == nivelespecifico.getEmpresaconstructoraId() && eocc.getConceptosgastoId() == 3).map(Empresaobraconceptoscoeficientes::getCoeficiente).findFirst().orElse(1.0);

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

        renglonVarianteObraController = controller;

        empresaobratarifaList = new ArrayList<>();
        empresaobratarifaList = utilCalcSingelton.getEmpresaobratarifaList(nivelespecifico.getObraId(), nivelespecifico.getEmpresaconstructoraId(), nivelespecifico.getObraByObraId().getTarifaId());

        listSuminitros = FXCollections.observableArrayList();
        listSuminitros = utilCalcSingelton.getSuministrosViewObservableList(nivelespecifico.getObraByObraId().getSalarioBySalarioId().getTag());
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
                labelValue.setText(costTotal + " / " + renglonvarianteok.getUm());
            } else {
                costTotal = Math.round(mat * 100d) / 100d + Math.round(costoMano * 100d) / 100d + Math.round(costoEq * 100d) / 100d;
                labelValue.setText(costTotal + " / " + renglonvarianteok.getUm());
            }
            salario = utilCalcSingelton.calcSalarioRV(renglonvarianteok);
            labelValue.setText(Math.round(costTotal * 100d) / 100d + " / " + renglonvarianteok.getUm());
            fieldCant.requestFocus();

        }
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("suministrosInRVUpdateRV.fxml"));
                Parent proot = loader.load();
                SuministrosInRVUpdateRVController controller = loader.getController();
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
                HojadeCalcauloController controller = loader.getController();
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


    public void createBajoespecificacionListUpdate(ArrayList<SumRVView> listOfSuminitrso) {
        bajoespecificacionArrayList = new ArrayList<Bajoespecificacionrv>();
        if (listOfSuminitrso != null) {
            for (SumRVView sumRVView : listOfSuminitrso) {
                bajoespecificacion = new Bajoespecificacionrv();
                bajoespecificacion.setNivelespecificoId(idUO);
                bajoespecificacion.setIdsuministro(sumRVView.getId());
                bajoespecificacion.setRenglonvarianteId(idRV);
                bajoespecificacion.setTipo(sumRVView.getTipo());
                bajoespecificacion.setCantidad(sumRVView.getCantidad());
                bajoespecificacion.setCosto(Math.round(sumRVView.getCosto() * sumRVView.getCantidad() * 100d) / 100d);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searhRVChangeRVUpdate.fxml"));
            Parent proot = loader.load();
            SearchRVChangeRVUpdateController controller = loader.getController();
            controller.setRVDatos(renVarianteToUnidadObraUpdateController, pattern, unidadobraO.getObraByObraId().getSalarioId());

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.toFront();
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void updateRecuros(int idRenglon, List<Bajoespecificacionrv> list) {
        List<Bajoespecificacionrv> datos = new ArrayList<>();
        for (Bajoespecificacionrv bajoespecificacionrv : list) {
            bajoespecificacionrv.setRenglonvarianteId(idRenglon);
            datos.add(bajoespecificacionrv);
        }

        updateAll(datos);
    }

    public void handleUpdateRenglonVariante(ActionEvent event) {
        double cantRV = Double.parseDouble(fieldCant.getText());
        unidadobrarenglon = new Renglonnivelespecifico();
        unidadobrarenglon.setNivelespecificoId(idUO);
        unidadobrarenglon.setRenglonvarianteId(renglonvarianteok.getId());
        unidadobrarenglon.setCantidad(cantRV);

        double costoMano = utilCalcSingelton.calcCostoManoRVinEmpresaObra(renglonvarianteok);
        double costoEq = renglonvarianteok.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
        double costMater = renglonvarianteok.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
        double costMaterSemi = renglonvarianteok.getRenglonsemielaboradosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);
        double costMaterJueg = renglonvarianteok.getRenglonjuegosById().parallelStream().map(renglonrecursos -> renglonrecursos.getCantidad() * renglonrecursos.getJuegoproductoByJuegoproductoId().getPreciomn() / renglonrecursos.getUsos()).reduce(0.0, Double::sum);


        if (checkTipoCosto.isSelected() == true) {
            unidadobrarenglon.setConmat("0 ");
            unidadobrarenglon.setCostmaterial(0.0);
        } else {
            double mat = costMater + costMaterSemi + costMaterJueg;
            unidadobrarenglon.setConmat("1 ");
            unidadobrarenglon.setCostmaterial(mat * cantRV);
        }
        unidadobrarenglon.setCostmano(costoMano * cantRV * coeficienteMano);
        unidadobrarenglon.setCostequipo(costoEq * cantRV * coeficienteEquipo);
        if (salario == 0.0) {
            salario = utilCalcSingelton.calcSalarioRV(renglonvarianteok);
        }
        unidadobrarenglon.setSalario(salario * cantRV);
        unidadobrarenglon.setSalariocuc(0.0);

        UpdateRVtoUnidadObra(unidadobrarenglon);
        if (bajoespecificacionInsertArrayList.size() > 0) {
            updateRecuros(unidadobrarenglon.getRenglonvarianteId(), bajoespecificacionInsertArrayList);
        }
        if (checkTipoCosto.isSelected() && bajoespecificacionArrayList != null) {
            for (Bajoespecificacionrv bajo : bajoespecificacionArrayList) {
                idbajo = addBajoEspecificacion(bajo);
            }
            bajoespecificacionArrayList.clear();
        }

        racalcVolumenesOfBajoEspecificacion(idUO, idRV, cantRV, cantOllRV);

        clearField();
        renglonVarianteObraController.addElementTableRV(idUO);
        renglonVarianteObraController.addElementTableSuministros(idUO, idRV);
        renglonVarianteObraController.completeValuesNivelEspecifico(idUO);
        renglonVarianteObraController.calElementNivelEspecifico();
    }

    private void racalcVolumenesOfBajoEspecificacion(Integer idUO, Integer idRV, Double cantidad, double cantidadOll) {
        List<Bajoespecificacionrv> bajoespecificacionrvs = getBajoespecificacionrvList(idUO, idRV);
        if (bajoespecificacionrvs.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("El renglón variante tiene suministros asociados bajo especificación, desea reaclacular los volumenes de estos recursos en función del volumen del renglón variante");

            ButtonType buttonAgregar = new ButtonType("Recalcular");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonAgregar) {
                List<Bajoespecificacionrv> updateList = new ArrayList<>();
                List<Bajoespecificacionrv> toDeleteTemp = new ArrayList<>();
                for (Bajoespecificacionrv bajoespecificacionrv : bajoespecificacionrvs) {
                    SuministrosView suministrosView = getSuministrosToUpdate(bajoespecificacionrv.getIdsuministro());
                    if (suministrosView == null) {
                        toDeleteTemp.add(bajoespecificacionrv);
                    } else {
                        double cant = calcVolSuminitro(cantidadOll, bajoespecificacionrv.getCantidad(), cantidad);
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


}
