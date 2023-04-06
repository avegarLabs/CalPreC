package views;

import Reports.BuildDynamicReport;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.EntityExistsException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class
RenglonesController implements Initializable {

    private static SessionFactory sf;
    Logger logger = Logger.getLogger(RenglonesController.class.getName());
    List<Renglonvariante> R15List;
    @FXML
    private Label label_title;
    @FXML
    private JFXTextField filter;
    @FXML
    private Pane content_pane;
    @FXML
    private TableView<RenglonVarianteView> dataTable;
    @FXML
    private TableColumn<RenglonVarianteView, StringProperty> code;
    @FXML
    private TableColumn<RenglonVarianteView, StringProperty> descrip;
    @FXML
    private TableColumn<RenglonVarianteView, StringProperty> um;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> costomat;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> costomano;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> costoEqui;
    @FXML
    private TableColumn<RenglonVarianteView, DoubleProperty> salario;
    @FXML
    private JFXComboBox<String> comboSobregrupo;
    @FXML
    private JFXComboBox<String> comboGrupo;
    @FXML
    private JFXComboBox<String> comboSubgrupo;
    @FXML
    private JFXCheckBox checkPrecons;
    @FXML
    private JFXCheckBox checkMariel;
    private ArrayList<Sobregrupo> sobregrupoArrayList;
    private ObservableList<String> sobregrupoArrayListString;
    private Sobregrupo sobregrupo;
    private List<Grupo> grupoArrayList;
    private ObservableList<String> grupoArrayListString;
    private Grupo grupo;
    private List<Subgrupo> subgrupoArrayList;
    private List<Subgrupo> subgrupoArrayListFull;
    private ObservableList<String> subgrupoArrayListString;
    private Subgrupo subgrupo;
    private List<Renglonvariante> renglonvarianteArrayList;
    private ObservableList<RenglonVarianteView> renglonVarianteViewObservableList;
    private RenglonVarianteView renglonVarianteView;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ObservableList<ManoObraRenglonVariante> manoobraList;
    private double importe;
    private double valGrupo;
    private double sal;
    private List<Renglonvariante> reglones15List;
    private int batchSizeUOR = 5;
    private int countUOR;
    private Subgrupo getSubgrupo;
    private String tipoSalario;
    @FXML
    private JFXButton btnagregar;
    private Usuarios user;
    @FXML
    private JFXButton btnNNormas;
    private String path;
    private File[] files;
    private int countr;
    private double batchSizer = 100;
    private int countrec;
    private double batchSizerec = 100;
    private int countsem;
    private double batchSizesem = 100;
    private List<Renglonvariante> renglonvariantNewNormaseList;
    private List<Renglonrecursos> renglonrecursosList;
    private List<Renglonsemielaborados> renglonsemielaboradosList;
    private List<Renglonjuego> renglonjuegoList;
    Task tarea;
    private List<Juegoproducto> juegoproductoList;

    private static List<Salario> salarioList;
    UtilCalcSingelton reportProjectStructureSingelton = UtilCalcSingelton.getInstance();
    private ObservableList<String> resolucionesList;
    @FXML
    private JFXComboBox<String> combNormas;
    private Salario valSal;

    private RenglonesController renglonesController;

    private static List<Salario> getSalarios() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            salarioList = new ArrayList<>();
            salarioList = session.createQuery("FROM Salario ORDER BY id ASC ").getResultList();

            tx.commit();
            session.close();
            return salarioList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private void deleteRV(int idRen) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Integer op1 = session.createQuery(" DELETE FROM Renglonrecursos WHERE renglonvarianteId =: idR ").setParameter("idR", idRen).executeUpdate();
            Integer op2 = session.createQuery(" DELETE FROM Renglonjuego WHERE renglonvarianteId =: idR ").setParameter("idR", idRen).executeUpdate();
            Integer op3 = session.createQuery(" DELETE FROM Renglonsemielaborados WHERE renglonvarianteId =: idR ").setParameter("idR", idRen).executeUpdate();
            Integer op4 = session.createQuery(" DELETE FROM Renglonvariante WHERE id =: idR ").setParameter("idR", idRen).executeUpdate();

            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    private List<Subgrupo> getSubgruposFull() {

        Session session = ConnectionModel.createAppConnection().openSession();
        List<Subgrupo> lisTmp = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            lisTmp = session.createQuery("FROM Subgrupo ").getResultList();

            tx.commit();
            session.close();
            return lisTmp;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    /**
     * Bloques para las funciones de trabajo con la base de datos
     */
    //Llena el combobox de los sobregrupos
    public ObservableList<String> getSobregrupos() {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            sobregrupoArrayListString = FXCollections.observableArrayList();
            sobregrupoArrayList = new ArrayList<>();
            sobregrupoArrayList = (ArrayList<Sobregrupo>) session.createQuery("FROM Sobregrupo ").getResultList();
            sobregrupoArrayListString.addAll(sobregrupoArrayList.parallelStream().map(sobregrupo -> sobregrupo.getCodigo().trim() + " - " + sobregrupo.getDescipcion().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return sobregrupoArrayListString;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    private void createComponentesInRV(Integer idR, int id) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Renglonrecursos> listRec = new ArrayList<>();
            listRec = session.createQuery(" FROM Renglonrecursos WHERE  renglonvarianteId =: idReng").setParameter("idReng", id).getResultList();
            List<Renglonsemielaborados> listSemi = new ArrayList<>();
            listSemi = session.createQuery(" FROM Renglonsemielaborados WHERE  renglonvarianteId =: idReng").setParameter("idReng", id).getResultList();
            List<Renglonjuego> listJuego = new ArrayList<>();
            listJuego = session.createQuery(" FROM Renglonjuego WHERE  renglonvarianteId =: idReng").setParameter("idReng", id).getResultList();


            if (listRec.size() > 0) {
                List<Renglonrecursos> datos = new ArrayList<>();
                for (Renglonrecursos rvc : listRec) {
                    Renglonrecursos recRen = new Renglonrecursos();
                    recRen.setRenglonvarianteId(idR);
                    recRen.setCantidas(cal30porcientoInNorma(rvc.getCantidas()));
                    recRen.setRecursosId(rvc.getRecursosId());
                    recRen.setUsos(rvc.getUsos());
                    datos.add(recRen);

                }

                saveRenglonRecursosList(datos);
            }

            if (listSemi.size() > 0) {
                List<Renglonsemielaborados> renglonsemielaborados = new ArrayList<>();
                for (Renglonsemielaborados rvc : listSemi) {
                    Renglonsemielaborados recRen = new Renglonsemielaborados();
                    recRen.setRenglonvarianteId(idR);
                    recRen.setCantidad(cal30porcientoInNorma(rvc.getCantidad()));
                    recRen.setSuministrossemielaboradosId(rvc.getSuministrossemielaboradosId());
                    recRen.setUsos(rvc.getUsos());
                    renglonsemielaborados.add(recRen);
                }

                saveRenglonSemiList(renglonsemielaborados);
            }

            if (listJuego.size() > 0) {
                List<Renglonjuego> renglonjuegos = new ArrayList<>();
                for (Renglonjuego rvc : listJuego) {
                    Renglonjuego recRen = new Renglonjuego();
                    recRen.setRenglonvarianteId(idR);
                    recRen.setCantidad(cal30porcientoInNorma(rvc.getCantidad()));
                    recRen.setJuegoproductoId(rvc.getJuegoproductoId());
                    recRen.setUsos(rvc.getUsos());
                    renglonjuegos.add(recRen);
                }

                saveRenglonJuegoList(renglonjuegos);
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

    public void saveRenglonRecursosList(List<Renglonrecursos> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Renglonrecursos unidadobrarenglon : unidadobrarenglonArray) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveRenglonSemiList(List<Renglonsemielaborados> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Renglonsemielaborados unidadobrarenglon : unidadobrarenglonArray) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveRenglonJuegoList(List<Renglonjuego> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Renglonjuego unidadobrarenglon : unidadobrarenglonArray) {
                countUOR++;
                if (countUOR > 0 && countUOR % batchSizeUOR == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateValoresRenglones(Integer idRVNew) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonvariante renglon = session.get(Renglonvariante.class, idRVNew);
            List<Renglonrecursos> listRec = new ArrayList<>();
            listRec = session.createQuery(" FROM Renglonrecursos WHERE  renglonvarianteId =: idReng").setParameter("idReng", renglon.getId()).getResultList();
            List<Renglonsemielaborados> listSemi = new ArrayList<>();
            listSemi = session.createQuery(" FROM Renglonsemielaborados WHERE  renglonvarianteId =: idReng").setParameter("idReng", renglon.getId()).getResultList();
            List<Renglonjuego> listJuego = new ArrayList<>();
            listJuego = session.createQuery(" FROM Renglonjuego WHERE  renglonvarianteId =: idReng").setParameter("idReng", renglon.getId()).getResultList();


            double costMano = listRec.parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            double costEquipo = listRec.parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            double costMatRec = listRec.parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().equals("1")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            double costMatJueg = listJuego.parallelStream().map(renglonjuego -> renglonjuego.getCantidad() * renglonjuego.getJuegoproductoByJuegoproductoId().getPreciomn()).reduce(0.0, Double::sum);
            double costMatSemi = listSemi.parallelStream().map(renglonsemielaborados -> renglonsemielaborados.getCantidad() * renglonsemielaborados.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn()).reduce(0.0, Double::sum);
            double salario = listRec.parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * getImporteEscala(renglonrecursos.getRecursosByRecursosId().getGrupoescala(), renglon.getRs())).reduce(0.0, Double::sum);

            renglon.setCostequip(costEquipo);
            renglon.setCostmano(costMano);
            renglon.setCostomat(costMatRec + costMatJueg + costMatSemi);
            renglon.setSalario(salario);

            session.update(renglon);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
    }

    //Llena el combobox de los grupos apartir del sobregrupo seleccionado
    public ObservableList<String> getGrupos(Sobregrupo sobregrupo) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            grupoArrayListString = FXCollections.observableArrayList();
            grupoArrayList = new LinkedList<>();

            grupoArrayList = (ArrayList<Grupo>) session.createQuery("FROM Grupo WHERE sobregrupoId =: id").setParameter("id", sobregrupo.getId()).getResultList();
            grupoArrayListString.addAll(grupoArrayList.parallelStream().map(grupo -> grupo.getCodigog().trim() + " - " + grupo.getDescripciong().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return grupoArrayListString;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    @FXML
    public void handleLLenaGrupoList(ActionEvent event) {
        comboGrupo.getItems().clear();
        String[] code = comboSobregrupo.getValue().split(" - ");
        var sobreg = sobregrupoArrayList.stream().filter(sobre -> sobre.getCodigo().equals(code[0])).findFirst().orElse(null);
        comboGrupo.setItems(getGrupos(sobreg));
        comboGrupo.setEditable(true);
        TextFields.bindAutoCompletion(comboGrupo.getEditor(), comboGrupo.getItems()).setPrefWidth(comboGrupo.getPrefWidth());
    }

    @FXML
    public void handleLlenaSubGrupoList(ActionEvent event) {
        comboSubgrupo.getItems().clear();
        String[] codeg = comboGrupo.getValue().split(" - ");
        var grupo = grupoArrayList.stream().filter(gr -> gr.getCodigog().equals(codeg[0])).findFirst().orElse(null);
        comboSubgrupo.setItems(getSubgrupos(grupo));
        comboSubgrupo.setEditable(true);
        TextFields.bindAutoCompletion(comboSubgrupo.getEditor(), comboSubgrupo.getItems()).setPrefWidth(comboSubgrupo.getPrefWidth());
    }

    private double getImporteEscala(String grupo, int idSal) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Salario salario = session.get(Salario.class, idSal);
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

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();

        }
        return importe;
    }

    private ObservableList<String> getSubgrupos(Grupo grupo) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subgrupoArrayListString = FXCollections.observableArrayList();
            subgrupoArrayList = new LinkedList<>();
            subgrupoArrayList = (ArrayList<Subgrupo>) session.createQuery("FROM Subgrupo WHERE grupoId =: id").setParameter("id", grupo.getId()).getResultList();
            subgrupoArrayListString.addAll(subgrupoArrayList.parallelStream().map(sub -> sub.getCodigosub().trim() + " - " + sub.getDescripcionsub().trim()).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return subgrupoArrayListString;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private Integer habiliteRVinR15(Integer idRV) {
        Integer idR = null;
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Renglonvariante rv = session.get(Renglonvariante.class, idRV);

            Renglonvariante renglonvariante = new Renglonvariante();
            renglonvariante.setCodigo(rv.getCodigo());
            renglonvariante.setDescripcion(rv.getDescripcion().concat(" (PRECONS MODIFICADO)"));
            renglonvariante.setUm(rv.getUm());
            renglonvariante.setCostomat(rv.getCostomat());
            renglonvariante.setCostmano(rv.getCostmano());
            renglonvariante.setCostequip(rv.getCostequip());

            renglonvariante.setHh(rv.getHh());
            renglonvariante.setHa(rv.getHa());
            renglonvariante.setHe(rv.getHe());
            renglonvariante.setHo(rv.getHo());

            renglonvariante.setPeso(rv.getPeso());
            renglonvariante.setCarga(rv.getCarga());
            renglonvariante.setCemento(rv.getCemento());
            renglonvariante.setAsfalto(rv.getAsfalto());
            renglonvariante.setArido(rv.getArido());
            renglonvariante.setPrefab(rv.getPrefab());
            renglonvariante.setPreciomlc(0.0);
            renglonvariante.setPreciomn(0.0);
            renglonvariante.setRs(2);
            renglonvariante.setSalario(0.0);
            renglonvariante.setSubgrupoId(getSubgrupo.getId());

            idR = (Integer) session.save(renglonvariante);


            tx.commit();
            session.close();
            return idR;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }


        return idR;
    }

    private DecimalFormat formatoDecimal;

    private Double cal30porcientoInNorma(double norma) {
        double opreration = norma * 0.3;
        double result = norma - opreration;
        String val = formatoDecimal.format(result);
        return Double.parseDouble(val);

    }

    private void loadDatos() {

        code.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, StringProperty>("codigo"));
        descrip.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, StringProperty>("um"));
        costomat.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("costomat"));
        costomano.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("costmano"));
        costoEqui.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("costequip"));
        salario.setCellValueFactory(new PropertyValueFactory<RenglonVarianteView, DoubleProperty>("salario"));

    }


    private ObservableList<RenglonVarianteView> getRenglonesVariantes(int idSub, int norm) {
        renglonVarianteViewObservableList = FXCollections.observableArrayList();
        List<Renglonvariante> listRenglones = reportProjectStructureSingelton.renglonvarianteList.parallelStream().filter(renglonvariante -> renglonvariante.getSubgrupoId() == idSub && renglonvariante.getRs() == norm).collect(Collectors.toList());
        for (Renglonvariante rv : listRenglones) {
            double salario = getSalrioInRV(rv);
            BigDecimal salarioFormater = new BigDecimal(String.format("%.2f", salario));
            BigDecimal costMaterFormater = new BigDecimal(String.format("%.2f", rv.getCostomat()));
            BigDecimal costManoFormater = new BigDecimal(String.format("%.2f", rv.getCostmano()));
            BigDecimal costEquipFormater = new BigDecimal(String.format("%.2f", rv.getCostequip()));
            renglonVarianteViewObservableList.add(new RenglonVarianteView(rv.getId(), rv.getCodigo(), rv.getDescripcion().toUpperCase(), rv.getUm(), costMaterFormater.doubleValue(), costManoFormater.doubleValue(), costEquipFormater.doubleValue(), salarioFormater.doubleValue()));
        }
        return renglonVarianteViewObservableList;
    }

    private double getSalrioInRV(Renglonvariante renglonvariante) {
        double salary = 0.0;
        for (Renglonrecursos renglonrecursos : renglonvariante.getRenglonrecursosById()) {
            if (renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")) {
                salary += renglonrecursos.getCantidas() * getImporteEscala(renglonrecursos.getRecursosByRecursosId().getGrupoescala(), renglonvariante.getRs());
            }
        }
        return salary;
    }

    @FXML
    public void handleLlenaTablaRenglonVariantes(ActionEvent event) {
        String[] codeSub = comboSubgrupo.getValue().split(" - ");
        getSubgrupo = subgrupoArrayList.stream().filter(sbg -> sbg.getCodigosub().equals(codeSub[0])).findFirst().orElse(null);
        valSal = salarioList.parallelStream().filter(sal -> sal.getDescripcion().trim().equals(combNormas.getValue().trim())).findFirst().get();

        loadDatos();
        renglonVarianteViewObservableList = FXCollections.observableArrayList();
        renglonVarianteViewObservableList = getRenglonesVariantes(getSubgrupo.getId(), valSal.getId());
        renglonVarianteViewObservableList.size();

        FilteredList<RenglonVarianteView> filteredData = new FilteredList<RenglonVarianteView>(renglonVarianteViewObservableList, p -> true);

        SortedList<RenglonVarianteView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

        dataTable.setItems(sortedData);

        filter.textProperty().addListener((prop, old, text) -> {
            filteredData.setPredicate(renglonVarianteView -> {
                if (text == null || text.isEmpty()) {
                    return true;
                }

                String descrip = renglonVarianteView.getDescripcion().toLowerCase();
                return descrip.contains(text.toLowerCase());
            });
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renglonesController = this;
        addDatotoComboNormas();

        comboSobregrupo.setItems(getSobregrupos());
        comboSobregrupo.setEditable(true);
        TextFields.bindAutoCompletion(comboSobregrupo.getEditor(), comboSobregrupo.getItems()).setPrefWidth(comboSobregrupo.getPrefWidth());
        subgrupoArrayListFull = new ArrayList<>();
        subgrupoArrayListFull = getSubgruposFull();

        combNormas.setOnAction(event -> {
            if (comboSobregrupo.getValue() != null && comboGrupo.getValue() != null && comboSubgrupo.getValue() != null) {
                handleLlenaTablaRenglonVariantes(event);
            }
        });
    }

    public void addDatotoComboNormas() {
        resolucionesList = FXCollections.observableArrayList();
        List<Salario> listSalarios = getSalarios();
        resolucionesList.addAll(listSalarios.parallelStream().map(Salario::getDescripcion).collect(Collectors.toList()));
        combNormas.setItems(resolucionesList);
        combNormas.getSelectionModel().select(resolucionesList.get(0));
    }

    public void handleViewComposicionRenglonVariante(ActionEvent event) {
        renglonVarianteView = dataTable.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ComposicionRVModificado.fxml"));
            Parent proot = loader.load();
            ComposicionRVModifController composicionRenglonVarianteController = loader.getController();
            if (valSal.getId() > 3) {
                valSal = salarioList.parallelStream().filter(sal -> sal.getId() == 3).findFirst().get();
            }
            composicionRenglonVarianteController.LlenarDatos(renglonVarianteView, user, valSal);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.setOnCloseRequest(event1 -> {
                reportProjectStructureSingelton.getAllRenglones();
                handleLlenaTablaRenglonVariantes(event);
            });

            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleViewHabilitarRenglonVariante15(ActionEvent event) {
        if (combNormas.getValue().trim().equals("Resolución 30")) {
            int idR = getidRenglonVariante(dataTable.getSelectionModel().getSelectedItem().getCodigo());

            if (idR == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(" Las normas del PRECONS II se pueden utilizar con un 30% de economía aprobada en la obra por el inversionista y el constructor ");

                ButtonType buttonAgregar = new ButtonType("Aceptar");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == buttonAgregar) {

                    Integer id = habiliteRVinR15(dataTable.getSelectionModel().getSelectedItem().getId());

                    if (id != null) {
                        createComponentesInRV(id, dataTable.getSelectionModel().getSelectedItem().getId());
                        updateValoresRenglones(id);


                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setHeaderText("Renglón Variante habilitado para la resolución 15");
                        alert1.showAndWait();
                    }


                    handleLlenaTablaRenglonVariantes(event);
                }


            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso!");
                alert.setHeaderText("El renglon variante esta habilitado con las normas de la resolución 15");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso!");
            alert.setHeaderText("Esta funcionalidad es solo aplicable en las normas del PRECONS II");
            alert.showAndWait();
        }

    }

    public void handleDeleteAcation(ActionEvent event) {
        try {
            deleteRV(dataTable.getSelectionModel().getSelectedItem().getId());
            List<Renglonvariante> list = new ArrayList<>();
            list = reportProjectStructureSingelton.getAllRenglones();
            list.size();
            handleLlenaTablaRenglonVariantes(event);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Error al eliminar el renglon variante: " + dataTable.getSelectionModel().getSelectedItem().getCodigo());
            alert.showAndWait();
        }
    }

    public void checkUsuario(Usuarios usuar) {
        user = usuar;
        if (usuar.getGruposId() != 1) {
            btnagregar.setDisable(true);
            btnNNormas.setDisable(true);
        }
    }

    // private List<Recursos> getRecursosList;
    private List<Suministrossemielaborados> suministrossemielaboradosList;
    private ProgressDialog stage;
    private int count;
    private int batchSize = 100;

    public void handlegetExelNormas(ActionEvent event) {
        if (combNormas.getValue().trim().equals("Resolución 266")) {
            List<Renglonrecursos> recursosList = getFRecursosList(1);
            recursosList.size();
        } else if (combNormas.getValue().trim().equals("Resolución 86")) {
            List<Renglonrecursos> recursosList = getFRecursosList(2);
            recursosList.size();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Esta actualización es solo para las resoluciones 266 y 86");
            alert.showAndWait();
        }


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione el fichero con las normas");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                tarea = createTimeMesage();
                ProgressDialog dialog = new ProgressDialog(tarea);
                dialog.setContentText("Analizando el fichero, por favor espere...");
                dialog.setTitle("Espere...");
                new Thread(tarea).start();
                dialog.showAndWait();

                FileInputStream inputStream = null;

                try {
                    inputStream = new FileInputStream(new File(file.getAbsolutePath()));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }

                Workbook workbook = null;
                try {
                    workbook = new XSSFWorkbook(inputStream);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                try {
                    Sheet firstSheet = workbook.getSheetAt(0);
                    Iterator<Row> iterator = firstSheet.iterator();
                    List<Renglonrecursos> renglonrecursosInsertList = new ArrayList<>();
                    List<Renglonrecursos> renglonrecursosDeleteList = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Row nextRow = iterator.next();
                        Renglonrecursos rrec = getRenglonrecursos(Double.valueOf(nextRow.getCell(0).toString()).intValue(), Double.valueOf(nextRow.getCell(1).toString()).intValue());
                        if (rrec == null) {
                            Renglonrecursos renglonrecursos = new Renglonrecursos();
                            renglonrecursos.setRenglonvarianteId(Double.valueOf(nextRow.getCell(0).toString()).intValue());
                            renglonrecursos.setRecursosId(Double.valueOf(nextRow.getCell(1).toString()).intValue());
                            renglonrecursos.setCantidas(Double.valueOf(nextRow.getCell(2).toString()));
                            renglonrecursos.setUsos(Double.valueOf(nextRow.getCell(3).toString()));
                            renglonrecursosInsertList.add(renglonrecursos);
                        } else if (rrec != null) {
                            renglonrecursosDeleteList.add(rrec);
                            Renglonrecursos renrec = new Renglonrecursos();
                            renrec.setRenglonvarianteId(Double.valueOf(nextRow.getCell(0).toString()).intValue());
                            renrec.setRecursosId(Double.valueOf(nextRow.getCell(1).toString()).intValue());
                            double normaToIn = Double.valueOf(nextRow.getCell(2).toString());
                            double normaOk = rrec.getCantidas() + normaToIn;
                            renrec.setCantidas(new BigDecimal(String.format("%.4f", normaOk)).doubleValue());
                            renrec.setUsos(Double.valueOf(nextRow.getCell(3).toString()));
                            renglonrecursosInsertList.add(renrec);
                        }
                    }
                    deleteRenglonRecursos(renglonrecursosDeleteList);
                    addRenglonRecursos(renglonrecursosInsertList);

                    tarea = createTime(Math.toIntExact(50));
                    stage = new ProgressDialog(tarea);
                    stage.setContentText("Contabilizando Renglones actualizados...");
                    stage.setTitle("Espere...");
                    new Thread(tarea).start();
                    stage.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } finally {

            }
        }
    }

    public void addRenglonRecursos(List<Renglonrecursos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Renglonrecursos recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                try {
                    session.persist(recursos);
                } catch (EntityExistsException eex) {
                    System.out.println(recursos.getRenglonvarianteId() + " -- " + recursos.getRecursosId() + " -- " + recursos.getCantidas());
                }

            }
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Suministros");
            alert.setContentText("Ha ocurrido un error al importar los recursos,  posiblemente este importando datos que ya esten en la base de datos ");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }

    public void deleteRenglonRecursos(List<Renglonrecursos> recursosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (Renglonrecursos recursos : recursosArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
                session.delete(recursos);
            }
            tx.commit();
            session.close();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error Importando Suministros");
            alert.setContentText("Ha ocurrido un error al importar los recursos,  posiblemente este importando datos que ya esten en la base de datos ");
            alert.showAndWait();
        } finally {
            session.close();
        }

    }


    public Task createTime(Integer val) {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                for (int i = 0; i < val; i++) {
                    Thread.sleep(val / 2);
                    updateProgress(i + 1, val);
                }
                return true;
            }
        };
    }

    public Task createTimeMesage() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(2000);
                return true;
            }
        };
    }

    private Renglonrecursos getRenglonrecursos(int idRV, int idRec) {
        return renglonrecursosList.parallelStream().filter(item -> item.getRenglonvarianteId() == idRV && item.getRecursosId() == idRec).findFirst().orElse(null);
    }


    ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();

    /*
    private int getidInsumo(String code) {
        return getRecursosList.parallelStream().filter(renglonvariante -> renglonvariante.getCodigo().contains(code)).findFirst().map(Recursos::getId).orElse(0);
    }

    private int getSuministrosSemi(String codeS) {
        return suministrossemielaboradosList.parallelStream().filter(suministrossemielaborados -> suministrossemielaborados.getCodigo().contains(codeS)).findFirst().map(Suministrossemielaborados::getId).orElse(0);

    }

    private int getJuegoProducto(String code) {
        return juegoproductoList.parallelStream().filter(juegoproducto -> juegoproducto.getCodigo().contains(code)).findFirst().map(Juegoproducto::getId).orElse(0);

    }
*/
    private List<Renglonrecursos> getFRecursosList(int val) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            renglonrecursosList = new ArrayList<>();
            List<Renglonvariante> datos = new ArrayList<>();
            if (val == 1) {
                datos = session.createQuery(" FROM Renglonvariante WHERE rs = 3 ").getResultList();
            } else if (val == 2) {
                datos = session.createQuery(" FROM Renglonvariante WHERE rs = 2 ").getResultList();
            }
            for (Renglonvariante dato : datos) {
                System.out.println(dato.getRs());
                renglonrecursosList.addAll(dato.getRenglonrecursosById());
            }
            tx.commit();
            session.close();
            return renglonrecursosList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    List<RCreportModel> rCreportModelList = new ArrayList<>();

    private int getidRenglonVariante(String code) {
        return reportProjectStructureSingelton.renglonvarianteList.parallelStream().filter(renglonvariante -> renglonvariante.getCodigo().contains(code) && renglonvariante.getRs() == 2).findFirst().map(Renglonvariante::getId).orElse(0);
    }

    private BuildDynamicReport bdr;

    public void handleNewRenglon(ActionEvent event) {
        String[] codeSub = comboSubgrupo.getValue().split(" - ");
        var subg = subgrupoArrayList.stream().filter(sbg -> sbg.getCodigosub().equals(codeSub[0])).findFirst().orElse(null);


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoRVModif.fxml"));
            Parent proot = loader.load();

            NuevoRenglonVarianteModifController controller = loader.getController();
            controller.LlenarDatos(subg.getId(), valSal);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                reportProjectStructureSingelton.getAllRenglones();
                handleLlenaTablaRenglonVariantes(event);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void printRenglonReport(ActionEvent event) {
        bdr = new BuildDynamicReport();
        Renglonvariante renglon = reportProjectStructureSingelton.renglonvarianteList.parallelStream().filter(item -> item.getId() == dataTable.getSelectionModel().getSelectedItem().getId()).findFirst().get();
        TarifaSalarial tarifaSalarial = reportProjectStructureSingelton.getTarifaById(renglon.getRs());

        rCreportModelList = new ArrayList<>();
        rCreportModelList = createListTorepotT(renglon.getRenglonrecursosById(), renglon.getRenglonsemielaboradosById(), renglon.getRenglonjuegosById());
        var total = rCreportModelList.parallelStream().map(RCreportModel::getCosto).reduce(0.0, Double::sum);
        Map parametros = new HashMap();
        parametros.put("empresa", structureSingelton.getEmpresa().getNombre().trim());
        parametros.put("image", "templete/logoReport.jpg");
        parametros.put("reportName", "RC: " + renglon.getCodigo().trim() + "   " + renglon.getDescripcion().trim());
        parametros.put("unidad", "Costo: " + total + " " + tarifaSalarial.getMoneda() + "/" + renglon.getUm().trim());
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
    }

    private List<RCreportModel> createListTorepotT(List<Renglonrecursos> renglonrecursosById, List<Renglonsemielaborados> renglonsemielaboradosById, List<Renglonjuego> renglonjuegosById) {
        List<RCreportModel> temp = new ArrayList<>();
        for (Renglonrecursos item : renglonrecursosById) {
            if (item.getRecursosByRecursosId().getTipo().trim().equals("1")) {
                temp.add(new RCreportModel("Materiales", item.getRecursosByRecursosId().getCodigo().trim(), item.getRecursosByRecursosId().getDescripcion().trim(), item.getRecursosByRecursosId().getUm(), item.getCantidas(), item.getRecursosByRecursosId().getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidas() * item.getRecursosByRecursosId().getPreciomn())).doubleValue()));
            } else if (item.getRecursosByRecursosId().getTipo().trim().equals("2")) {
                double val = reportProjectStructureSingelton.getValorManoDeObraInRV(item.getRenglonvarianteByRenglonvarianteId(), item.getRecursosByRecursosId());
                temp.add(new RCreportModel("Mano de Obra", item.getRecursosByRecursosId().getCodigo().trim(), item.getRecursosByRecursosId().getDescripcion().trim(), item.getRecursosByRecursosId().getUm(), item.getCantidas(), val, new BigDecimal(String.format("%.2f", item.getCantidas() * val)).doubleValue()));
            } else if (item.getRecursosByRecursosId().getTipo().trim().equals("3")) {
                temp.add(new RCreportModel("Equipos", item.getRecursosByRecursosId().getCodigo().trim(), item.getRecursosByRecursosId().getDescripcion().trim(), item.getRecursosByRecursosId().getUm(), item.getCantidas(), item.getRecursosByRecursosId().getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidas() * item.getRecursosByRecursosId().getPreciomn())).doubleValue()));
            }
        }
        if (renglonsemielaboradosById.size() > 0) {
            for (Renglonsemielaborados item : renglonsemielaboradosById) {
                temp.add(new RCreportModel("Materiales", item.getSuministrossemielaboradosBySuministrossemielaboradosId().getCodigo().trim(), item.getSuministrossemielaboradosBySuministrossemielaboradosId().getDescripcion().trim(), item.getSuministrossemielaboradosBySuministrossemielaboradosId().getUm(), item.getCantidad(), item.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidad() * item.getSuministrossemielaboradosBySuministrossemielaboradosId().getPreciomn())).doubleValue()));
            }
        }
        if (renglonrecursosById.size() > 0) {
            for (Renglonjuego item : renglonjuegosById) {
                temp.add(new RCreportModel("Materiales", item.getJuegoproductoByJuegoproductoId().getCodigo().trim(), item.getJuegoproductoByJuegoproductoId().getDescripcion().trim(), item.getJuegoproductoByJuegoproductoId().getUm(), item.getCantidad(), item.getJuegoproductoByJuegoproductoId().getPreciomn(), new BigDecimal(String.format("%.2f", item.getCantidad() * item.getJuegoproductoByJuegoproductoId().getPreciomn())).doubleValue()));
            }
        }
        return temp;
    }

/*
    public void handlerActionAddCatalogo(ActionEvent e) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearCatalogo.fxml"));
            Parent proot = loader.load();

            NuevoCatalogoController controller = (NuevoCatalogoController) loader.getController();
            controller.addDataFromview(renglonesController);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
/*
    public void handlerTransferirCatalogo(ActionEvent e) {
        var renglon = reportProjectStructureSingelton.renglonvarianteList.parallelStream().filter(item -> item.getId() == dataTable.getSelectionModel().getSelectedItem().getId()).findFirst().get();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TransferirCatalogo.fxml"));
            Parent proot = loader.load();

            TranferirCatalogoController controller = (TranferirCatalogoController) loader.getController();
            controller.addDataFromRenglones(renglon);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
*/
}
