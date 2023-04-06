package MProject;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import net.sf.mpxj.*;
import net.sf.mpxj.common.NumberHelper;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.mspdi.SaveVersion;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Tuple;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ExportacionProjectController implements Initializable {

    private static SessionFactory sf;
    javafx.concurrent.Task tarea;
    @FXML
    private Label label_title;
    @FXML
    private Pane boxPane;
    @FXML
    private JFXCheckBox checkUnidad;
    @FXML
    private JFXCheckBox checkUMC;
    @FXML
    private JFXCheckBox checkRV;
    @FXML
    private JFXCheckBox checkPresup;
    @FXML
    private JFXCheckBox checkPlan;
    @FXML
    private JFXCheckBox checkNormPrec;
    @FXML
    private JFXCheckBox checkSalario;
    @FXML
    private JFXCheckBox checkMateriales;
    @FXML
    private JFXCheckBox checkMano;
    @FXML
    private JFXCheckBox checkEquipo;
    @FXML
    private JFXComboBox<String> comboObras;
    @FXML
    private JFXComboBox<String> comboZonas;
    @FXML
    private JFXComboBox<String> comboObjetos;
    @FXML
    private JFXComboBox<String> comboNivel;
    @FXML
    private JFXComboBox<String> comboEspecialidades;
    @FXML
    private JFXComboBox<String> comboSubespecialidades;
    @FXML
    private JFXComboBox<String> comboEmpresas;
    @FXML
    private JFXDatePicker pickerDesde;
    @FXML
    private JFXDatePicker pickerHasta;
    @FXML
    private JFXComboBox<String> comboConfecionado;
    @FXML
    private JFXComboBox<String> comboRevisado;
    @FXML
    private JFXComboBox<String> comboAprovado;
    @FXML
    private JFXCheckBox tipoUO;
    @FXML
    private JFXCheckBox tipoRV;
    @FXML
    private JFXTextField fieldMO;
    @FXML
    private JFXTextField fieldEquipo;
    private ArrayList<Obra> obraArrayList;
    private ArrayList<Zonas> zonasArrayList;
    private ArrayList<Objetos> objetosArrayList;
    private ArrayList<Nivel> nivelArrayList;
    private ArrayList<Especialidades> especialidadesArrayList;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private ArrayList<Empresaconstructora> empresaconstructoraArrayList;
    private String[] partObras;
    private String[] partZonas;
    private String[] partObj;
    private String[] partNiv;
    private String[] partEsp;
    private String[] partSub;
    private String[] partEmp;
    private Empresa empresa;
    private ArrayList<net.sf.mpxj.Resource> resoucesArraylist;
    private ProjectFile project;
    private net.sf.mpxj.Resource resource;
    private net.sf.mpxj.Task rootTask;
    private net.sf.mpxj.Task empresaTask;
    private net.sf.mpxj.Task zonasTask;
    private net.sf.mpxj.Task objetoTask;
    private net.sf.mpxj.Task nivelTask;
    private net.sf.mpxj.Task especialidadTask;
    private net.sf.mpxj.Task subespTask;
    private net.sf.mpxj.Task unidadtask;
    private Task renglonTask;
    private Double volumen;
    private Double ejecutado;
    private Double calc;
    private Double porci;
    private Double avance;
    @FXML
    private JFXButton btn_close;
    @FXML
    private JFXComboBox<String> mspselect;
    private ReportProjectStructureSingelton projectStructureSingelton;
    private ArrayList<Task> taskArrayList;
    private Double valHoras;
    private Double total;
    private ArrayList<Task> renglonesTaskArray;
    private StringBuilder resouStringBuilder;
    private ProgressDialog stage;
    private MSPDIWriter mspdiWriter;
    private boolean myFlag;
    private StringBuilder zonaTaskBuider;
    private StringBuilder objTaskBuider;
    private StringBuilder nivTaskBuider;
    private StringBuilder espTaskBuider;
    private StringBuilder subTaskBuider;
    private List<DataResourcesToProject> allResourcesList;
    private List<Resource> resourceList;
    private Obra obra;
    private Empresaconstructora empresaconstructora;
    private Zonas zona;
    private Objetos objeto;
    private double coefMano;
    private double coefEquip;
    private Nivel nivel;
    private Especialidades especialidades;
    private Subespecialidades subespecialidades;

    private static Recursos getRecurso(int idR) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Recursos rec = session.get(Recursos.class, idR);

            tx.commit();
            session.close();
            return rec;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Recursos();
    }

    private static Suministrossemielaborados getSuministrossemielaborados(int idR) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Suministrossemielaborados rec = session.get(Suministrossemielaborados.class, idR);

            tx.commit();
            session.close();
            return rec;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new Suministrossemielaborados();
    }

    private static List<Empresaobra> getEmpresaobras(int idOb) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Empresaobra> empresaobraList = session.createQuery(" from Empresaobra WHERE obraId =: idO ").setParameter("idO", idOb).getResultList();

            tx.commit();
            session.close();
            return empresaobraList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectStructureSingelton = ReportProjectStructureSingelton.getInstance();

        empresa = projectStructureSingelton.getEmpresa();

        comboObras.getItems().setAll(projectStructureSingelton.getObrasList());
        // comboObras.setEditable(true);
        TextFields.bindAutoCompletion(comboObras.getEditor(), comboObras.getItems()).setPrefWidth(comboObras.getPrefWidth());
        comboEspecialidades.getItems().setAll(projectStructureSingelton.getEspecialidadesList());
        // comboEspecialidades.setEditable(true);
        TextFields.bindAutoCompletion(comboEspecialidades.getEditor(), comboEspecialidades.getItems()).setPrefWidth(comboEspecialidades.getPrefWidth());

        fieldMO.setText("1");
        fieldEquipo.setText("1");

        checkMano.setSelected(true);
        checkEquipo.setSelected(true);
        checkMateriales.setSelected(false);


        checkUnidad.setSelected(true);
        checkPresup.setSelected(true);
        checkNormPrec.setSelected(true);

        checkRV.setOnMouseClicked(event -> {
            if (checkRV.isSelected() == true) {

                comboObras.getItems().clear();
                comboObras.getItems().setAll(projectStructureSingelton.getObrasListRV());
                //comboObras.setEditable(true);
                TextFields.bindAutoCompletion(comboObras.getEditor(), comboObras.getItems()).setPrefWidth(comboObras.getPrefWidth());

                checkUnidad.setSelected(false);
                tipoUO.setDisable(true);
            }
        });

        checkUnidad.setOnMouseClicked(event -> {
            if (checkUnidad.isSelected() == true) {

                comboObras.getItems().clear();
                comboObras.getItems().setAll(projectStructureSingelton.getObrasList());

                checkRV.setSelected(false);
                tipoUO.setDisable(false);
            }
        });

        ObservableList<String> versionsList = FXCollections.observableArrayList();
        versionsList.add("MSProject2013");
        versionsList.add("MSProject2016");
        mspselect.setItems(versionsList);


    }

    public ArrayList<Zonas> getZonasToTasks(int idObra, int idEmp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonasArrayList = new ArrayList<Zonas>();
            List<Object[]> listZonas = session.createQuery("SELECT zon.id, zon.codigo, zon.desripcion, zon.obraId FROM Unidadobra uo INNER JOIN Zonas zon ON uo.zonasId = zon.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp GROUP BY zon.id, zon.codigo, zon.desripcion, zon.obraId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).getResultList();
            zonasArrayList.addAll(listZonas.parallelStream().map(row -> {
                Zonas zonas = new Zonas();
                zonas.setId(Integer.parseInt(row[0].toString().trim()));
                zonas.setCodigo(row[1].toString().trim());
                zonas.setDesripcion(row[2].toString().trim());
                zonas.setObraId(Integer.parseInt(row[3].toString().trim()));
                return zonas;
            }).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return zonasArrayList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Zonas> getZonasToTasksNE(int idObra, int idEmp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonasArrayList = new ArrayList<>();
            List<Object[]> listZonas = session.createQuery("SELECT zon.id, zon.codigo, zon.desripcion, zon.obraId FROM Nivelespecifico uo INNER JOIN Zonas zon ON uo.zonasId = zon.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp GROUP BY zon.id, zon.codigo, zon.desripcion, zon.obraId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).getResultList();
            zonasArrayList.addAll(listZonas.parallelStream().map(row -> {
                Zonas zonas = new Zonas();
                zonas.setId(Integer.parseInt(row[0].toString().trim()));
                zonas.setCodigo(row[1].toString().trim());
                zonas.setDesripcion(row[2].toString().trim());
                zonas.setObraId(Integer.parseInt(row[3].toString().trim()));
                return zonas;
            }).collect(Collectors.toList()));
            tx.commit();
            session.close();
            return zonasArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Objetos> getObjetosToTask(int idObra, int idEmp, int idZona) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosArrayList = new ArrayList<Objetos>();
            List<Object[]> objectList = session.createQuery("SELECT obj.id, obj.codigo, obj.descripcion, obj.zonasId FROM Unidadobra uo INNER JOIN Objetos obj ON uo.objetosId = obj.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon GROUP BY obj.id, obj.codigo, obj.descripcion, obj.zonasId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).getResultList();
            for (Object[] row : objectList) {
                Objetos objetos = new Objetos();
                objetos.setId(Integer.parseInt(row[0].toString().trim()));
                objetos.setCodigo(row[1].toString().trim());
                objetos.setDescripcion(row[2].toString().trim());
                objetos.setZonasId(Integer.parseInt(row[3].toString().trim()));

                objetosArrayList.add(objetos);

            }

            tx.commit();
            session.close();
            return objetosArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Objetos> getObjetosToTaskNE(int idObra, int idEmp, int idZona) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosArrayList = new ArrayList<Objetos>();
            List<Object[]> objectList = session.createQuery("SELECT obj.id, obj.codigo, obj.descripcion, obj.zonasId FROM Nivelespecifico uo INNER JOIN Objetos obj ON uo.objetosId = obj.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon GROUP BY obj.id, obj.codigo, obj.descripcion, obj.zonasId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).getResultList();
            for (Object[] row : objectList) {
                Objetos objetos = new Objetos();
                objetos.setId(Integer.parseInt(row[0].toString().trim()));
                objetos.setCodigo(row[1].toString().trim());
                objetos.setDescripcion(row[2].toString().trim());
                objetos.setZonasId(Integer.parseInt(row[3].toString().trim()));

                objetosArrayList.add(objetos);

            }

            tx.commit();
            session.close();
            return objetosArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Nivel> getNivelToTask(int idObra, int idEmp, int idZona, int idObj) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelArrayList = new ArrayList<Nivel>();
            List<Object[]> niveList = session.createQuery("SELECT niv.id, niv.codigo, niv.descripcion, niv.objetosId FROM Unidadobra uo INNER JOIN Nivel niv ON uo.nivelId = niv.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj GROUP BY niv.id, niv.codigo, niv.descripcion, niv.objetosId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).setParameter("idObj", idObj).getResultList();

            for (Object[] row : niveList) {
                Nivel nivel = new Nivel();
                nivel.setId(Integer.parseInt(row[0].toString().trim()));
                nivel.setCodigo(row[1].toString().trim());
                nivel.setDescripcion(row[2].toString().trim());
                nivel.setObjetosId(Integer.parseInt(row[3].toString().trim()));

                nivelArrayList.add(nivel);

            }

            tx.commit();
            session.close();
            return nivelArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Nivel> getNivelToTaskNE(int idObra, int idEmp, int idZona, int idObj) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelArrayList = new ArrayList<Nivel>();
            List<Object[]> niveList = session.createQuery("SELECT niv.id, niv.codigo, niv.descripcion, niv.objetosId FROM Nivelespecifico uo INNER JOIN Nivel niv ON uo.nivelId = niv.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj GROUP BY niv.id, niv.codigo, niv.descripcion, niv.objetosId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).setParameter("idObj", idObj).getResultList();
            for (Object[] row : niveList) {
                nivelArrayList.add((Nivel) row[0]);
            }
            tx.commit();
            session.close();
            return nivelArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Especialidades> getEspecialidadToTask(int idObra, int idEmp, int idZona, int idObj, int idNivel) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesArrayList = new ArrayList<Especialidades>();
            List<Object[]> resultList = session.createQuery("SELECT esp.id, esp.codigo, esp.descripcion FROM Unidadobra uo INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv GROUP BY esp.id, esp.codigo, esp.descripcion").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).setParameter("idObj", idObj).setParameter("idNiv", idNivel).getResultList();
            for (Object[] row : resultList) {
                Especialidades especialidades = new Especialidades();
                especialidades.setId(Integer.parseInt(row[0].toString().trim()));
                especialidades.setCodigo(row[1].toString().trim());
                especialidades.setDescripcion(row[2].toString().trim());
                especialidadesArrayList.add(especialidades);
            }

            tx.commit();
            session.close();
            return especialidadesArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Especialidades> getEspecialidadToTaskNE(int idObra, int idEmp, int idZona, int idObj, int idNivel) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesArrayList = new ArrayList<Especialidades>();
            List<Object[]> resultList = session.createQuery("SELECT esp.id, esp.codigo, esp.descripcion FROM Nivelespecifico uo INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv GROUP BY esp.id, esp.codigo, esp.descripcion").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).setParameter("idObj", idObj).setParameter("idNiv", idNivel).getResultList();
            for (Object[] row : resultList) {
                Especialidades especialidades = new Especialidades();
                especialidades.setId(Integer.parseInt(row[0].toString().trim()));
                especialidades.setCodigo(row[1].toString().trim());
                especialidades.setDescripcion(row[2].toString().trim());
                especialidadesArrayList.add(especialidades);
            }
            tx.commit();
            session.close();
            return especialidadesArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Subespecialidades> getSubEspecialidadToTask(int idObra, int idEmp, int idZona, int idObj, int idNivel, int idEsp) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subespecialidadesArrayList = new ArrayList<Subespecialidades>();
            List<Object[]> suObjects = session.createQuery("SELECT sub.id, sub.codigo, sub.descripcion, sub.especialidadesId FROM Unidadobra uo INNER JOIN Subespecialidades sub ON uo.subespecialidadesId = sub.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND uo.especialidadesId =: idEs GROUP BY sub.id, sub.codigo, sub.descripcion, sub.especialidadesId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).setParameter("idObj", idObj).setParameter("idNiv", idNivel).setParameter("idEs", idEsp).getResultList();

            for (Object[] row : suObjects) {
                Subespecialidades subespecialidades = new Subespecialidades();
                subespecialidades.setId(Integer.parseInt(row[0].toString().trim()));
                subespecialidades.setCodigo(row[1].toString().trim());
                subespecialidades.setDescripcion(row[2].toString().trim());
                subespecialidades.setEspecialidadesId(Integer.parseInt(row[3].toString().trim()));
                subespecialidadesArrayList.add(subespecialidades);

            }

            tx.commit();
            session.close();
            return subespecialidadesArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public ArrayList<Subespecialidades> getSubEspecialidadToTaskNE(int idObra, int idEmp, int idZona, int idObj, int idNivel, int idEsp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subespecialidadesArrayList = new ArrayList<Subespecialidades>();
            List<Object[]> suObjects = session.createQuery("SELECT sub.id, sub.codigo, sub.descripcion, sub.especialidadesId FROM Nivelespecifico uo INNER JOIN Subespecialidades sub ON uo.subespecialidadesId = sub.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND uo.especialidadesId =: idEs GROUP BY sub.id, sub.codigo, sub.descripcion, sub.especialidadesId").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idZona).setParameter("idObj", idObj).setParameter("idNiv", idNivel).setParameter("idEs", idEsp).getResultList();
            for (Object[] row : suObjects) {
                subespecialidadesArrayList.add((Subespecialidades) row[0]);
            }
            tx.commit();
            session.close();
            return subespecialidadesArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void getZonasTaskResoucesArrayNE(Task task, int idObra, int idEmp, int idzona) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            total = null;
            taskArrayList = new ArrayList<>();
            List<Object[]> listElemnts = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, SUM(uor.cantidad * rvr.cantidas), uo.costomaterial, uo.costomano, uo.costoequipo FROM Nivelespecifico uo INNER JOIN  Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.costomaterial, uo.costomano, uo.costoequipo").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).getResultList();
            for (Object[] row : listElemnts) {
                total = Double.parseDouble(row[4].toString().trim()) + Double.parseDouble(row[5].toString().trim()) + Double.parseDouble(row[6].toString().trim());
                unidadtask = task.addTask();
                unidadtask.setName(row[2].toString().trim());
                // valHoras = Double.parseDouble(row[5].toString().trim());
                // unidadtask.setDuration(Duration.getInstance(Math.toIntExact(Math.round(valHoras)), TimeUnit.DAYS));
                unidadtask.setCost(1, Math.round(Double.parseDouble(row[4].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(2, Math.round(Double.parseDouble(row[5].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(3, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(4, Math.round(total * 100d) / 100d);
                unidadtask.setText(1, row[2].toString().trim());
                getRebglonesToUnidadNE(unidadtask, Integer.parseInt(row[0].toString().trim()));
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

    public void getObjetosTaskResoucesArrayNE(Task task, int idObra, int idEmp, int idzona, int idObj) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            taskArrayList = new ArrayList<>();
            List<Object[]> datosList = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion,  SUM(uor.cantidad * rvr.cantidas), uo.costomaterial, uo.costomano, uo.costoequipo FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.costomaterial, uo.costomano, uo.costoequipo").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).getResultList();
            for (Object[] row : datosList) {
                total = Double.parseDouble(row[4].toString().trim()) + Double.parseDouble(row[5].toString().trim()) + Double.parseDouble(row[6].toString().trim());
                unidadtask = task.addTask();
                unidadtask.setName(row[2].toString().trim());
                valHoras = Double.parseDouble(row[5].toString().trim());
                //  unidadtask.setDuration(Duration.getInstance(Math.toIntExact(Math.round(valHoras)), TimeUnit.DAYS));
                unidadtask.setCost(1, Math.round(Double.parseDouble(row[4].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(2, Math.round(Double.parseDouble(row[5].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(3, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(Math.round(total * 100d) / 100d);
                unidadtask.setText(1, row[2].toString().trim());

                getRebglonesToUnidadNE(unidadtask, Integer.parseInt(row[0].toString().trim()));


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

    private Double getcantCertfificada(int idUn) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Certificacion> certificacionList = session.createQuery("from Certificacion where unidadobraId=: idU").setParameter("idU", idUn).getResultList();

            tx.commit();
            session.close();
            return certificacionList.parallelStream().map(Certificacion::getCantidad).reduce(0.0, Double::sum);
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return 0.0;
    }

    public void getNivelTaskResourceArrayNE(Task task, int idObra, int idEmp, int idzona, int idObj, int idNiv) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            taskArrayList = new ArrayList<>();
            List<Object[]> listData = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, SUM(uor.cantidad * rvr.cantidas), uo.costomaterial, uo.costomano, uo.costoequipo FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion,  uo.costomaterial, uo.costomano, uo.costoequipo").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).setParameter("idNiv", idNiv).getResultList();
            for (Object[] row : listData) {

                total = Double.parseDouble(row[4].toString().trim()) + Double.parseDouble(row[5].toString().trim()) + Double.parseDouble(row[6].toString().trim());
                unidadtask = task.addTask();
                unidadtask.setName(row[2].toString().trim());
                valHoras = Double.parseDouble(row[5].toString().trim());
                // unidadtask.setDuration(Duration.getInstance(Math.toIntExact(Math.round(valHoras)), TimeUnit.DAYS));
                unidadtask.setCost(1, Math.round(Double.parseDouble(row[4].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(2, Math.round(Double.parseDouble(row[5].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(3, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(Math.round(total * 100d) / 100d);
                unidadtask.setText(1, row[2].toString().trim());

                getRebglonesToUnidadNE(unidadtask, Integer.parseInt(row[0].toString().trim()));


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

    public void getEspecialidadTaskResourcesArrayNE(Task task, int idObra, int idEmp, int idzona, int idObj, int idNiv, int idEsp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            taskArrayList = new ArrayList<>();
            List<Object[]> listObjects = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, SUM(uor.cantidad * rvr.cantidas), uo.costomaterial, uo.costomano, uo.costoequipo FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND uo.especialidadesId =: idEsp AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.costomaterial, uo.costomano, uo.costoequipo").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).setParameter("idNiv", idNiv).setParameter("idEsp", idEsp).getResultList();
            for (Object[] row : listObjects) {
                total = Double.parseDouble(row[4].toString().trim()) + Double.parseDouble(row[5].toString().trim()) + Double.parseDouble(row[6].toString().trim());
                unidadtask = task.addTask();
                unidadtask.setName(row[2].toString().trim());
                valHoras = Double.parseDouble(row[5].toString().trim());
                //  unidadtask.setDuration(Duration.getInstance(Math.toIntExact(Math.round(valHoras)), TimeUnit.DAYS));
                unidadtask.setCost(1, Math.round(Double.parseDouble(row[4].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(2, Math.round(Double.parseDouble(row[5].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(3, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(Math.round(total * 100d) / 100d);
                unidadtask.setText(1, row[2].toString().trim());


                getRebglonesToUnidadNE(unidadtask, Integer.parseInt(row[0].toString().trim()));

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

    public void getSubEspecialidadTaskResourcesArrayNE(Task task, int idObra, int idEmp, int idzona, int idObj, int idNiv, int idEsp, int idSub) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            taskArrayList = new ArrayList<>();
            List<Object[]> listData = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, SUM(uor.cantidad * rvr.cantidas), uo.costomaterial, uo.costomano, uo.costoequipo FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND uo.especialidadesId =: idEsp AND uo.subespecialidadesId =: idSub AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.costomaterial, uo.costomano, uo.costoequipo").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).setParameter("idNiv", idNiv).setParameter("idEsp", idEsp).setParameter("idSub", idSub).getResultList();
            for (Object[] row : listData) {
                total = Double.parseDouble(row[4].toString().trim()) + Double.parseDouble(row[5].toString().trim()) + Double.parseDouble(row[6].toString().trim());
                unidadtask = task.addTask();
                unidadtask.setName(row[2].toString().trim());
                valHoras = Double.parseDouble(row[5].toString().trim());
                //  unidadtask.setDuration(Duration.getInstance(Math.toIntExact(Math.round(valHoras)), TimeUnit.DAYS));
                unidadtask.setCost(1, Math.round(Double.parseDouble(row[4].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(2, Math.round(Double.parseDouble(row[5].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(3, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(Math.round(total * 100d) / 100d);
                unidadtask.setText(1, row[2].toString().trim());

                getRebglonesToUnidadNE(unidadtask, Integer.parseInt(row[0].toString().trim()));

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

    public void getObjetosTaskResoucesArray(Task task, int idObra, int idEmp, int idzona, int idObj, Boolean recursos) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            volumen = null;
            ejecutado = null;
            calc = null;
            porci = null;
            List<Object[]> listObjects = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, SUM(uor.cantRv * rvr.cantidas), uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal, SUM(cert.cantidad) FROM Unidadobra uo LEFT JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId  LEFT JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId LEFT JOIN Recursos rec ON rvr.recursosId = rec.id  LEFT JOIN Certificacion cert ON uo.id = cert.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).getResultList();
            for (Object[] row : listObjects) {
                unidadtask = task.addTask();
                unidadtask.setName(row[2].toString().trim());

                if (row[3] != null) {

                    volumen = getcantCertfificada(Integer.parseInt(row[0].toString()));

                    ejecutado = Double.parseDouble(row[9].toString().trim()) * volumen / Double.parseDouble(row[4].toString().trim());
                    calc = volumen / Double.parseDouble(row[4].toString().trim());
                    porci = calc * 100;

                    valHoras = Double.parseDouble(row[5].toString().trim());
                    unidadtask.setCost(1, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(2, Math.round(Double.parseDouble(row[7].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(3, Math.round(Double.parseDouble(row[8].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(4, Math.round(Double.parseDouble(row[9].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(5, ejecutado);
                    unidadtask.setNumber(1, Double.parseDouble(row[4].toString().trim()));
                    unidadtask.setNumber(2, volumen);
                    unidadtask.setNumber(3, porci);
                    unidadtask.setText(1, row[1].toString().trim());
                    unidadtask.setText(2, row[2].toString().trim());

                    if (recursos == true) {
                        setResourcesToUnidada(unidadtask, Math.toIntExact(Math.round(valHoras)), Integer.parseInt(row[0].toString().trim()));
                    } else if (recursos == false) {
                        getRebglonesToUnidad(unidadtask, Integer.parseInt(row[0].toString().trim()));

                    }


                }

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

    public void getNivelTaskResourceArray(Task nTask, int idObra, int idEmp, int idzona, int idObj, int idNiv, Boolean recursos) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            volumen = null;
            ejecutado = null;
            calc = null;
            porci = null;
            taskArrayList = new ArrayList<>();
            List<Object[]> listDtaos = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, SUM(uor.cantRv * rvr.cantidas), uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal, SUM(cert.cantidad) FROM Unidadobra uo  LEFT JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId LEFT JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId LEFT JOIN Recursos rec ON rvr.recursosId = rec.id LEFT JOIN Certificacion cert ON uo.id = cert.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).setParameter("idNiv", idNiv).getResultList();
            for (Object[] row : listDtaos) {
                unidadtask = nTask.addTask();
                unidadtask.setName(row[2].toString().trim());

                if (row[3] != null) {

                    volumen = getcantCertfificada(Integer.parseInt(row[0].toString()));

                    ejecutado = Double.parseDouble(row[9].toString().trim()) * volumen / Double.parseDouble(row[4].toString().trim());
                    calc = volumen / Double.parseDouble(row[4].toString().trim());
                    porci = calc * 100;

                    valHoras = Double.parseDouble(row[5].toString().trim());
                    unidadtask.setCost(1, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(2, Math.round(Double.parseDouble(row[7].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(3, Math.round(Double.parseDouble(row[8].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(4, Math.round(Double.parseDouble(row[9].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(5, ejecutado);
                    unidadtask.setNumber(1, Double.parseDouble(row[4].toString().trim()));
                    unidadtask.setNumber(2, volumen);
                    unidadtask.setNumber(3, porci);
                    unidadtask.setText(1, row[1].toString().trim());
                    unidadtask.setText(2, row[3].toString().trim());

                    if (recursos == true) {
                        setResourcesToUnidada(unidadtask, Math.toIntExact(Math.round(valHoras)), Integer.parseInt(row[0].toString().trim()));
                    } else if (recursos == false) {
                        getRebglonesToUnidad(unidadtask, Integer.parseInt(row[0].toString().trim()));
                    }

                }
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

    public void getEspecialidadTaskResourcesArray(Task espTask, int idObra, int idEmp, int idzona, int idObj, int idNiv, int idEsp, Boolean recursos) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            volumen = null;
            ejecutado = null;
            calc = null;
            porci = null;
            taskArrayList = new ArrayList<>();
            List<Object[]> listUnidad = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, SUM(uor.cantRv * rvr.cantidas), uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal, SUM(cert.cantidad) FROM Unidadobra uo LEFT JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId LEFT JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId LEFT JOIN Recursos rec ON rvr.recursosId = rec.id LEFT JOIN Certificacion cert on uo.id = cert.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND uo.especialidadesId =: idEsp AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).setParameter("idNiv", idNiv).setParameter("idEsp", idEsp).getResultList();
            for (Object[] row : listUnidad) {
                unidadtask = espTask.addTask();
                unidadtask.setName(row[2].toString().trim());

                if (row[3] != null) {
                    volumen = getcantCertfificada(Integer.parseInt(row[0].toString()));

                    ejecutado = Double.parseDouble(row[9].toString().trim()) * volumen / Double.parseDouble(row[4].toString().trim());
                    calc = volumen / Double.parseDouble(row[4].toString().trim());
                    porci = calc * 100;

                    valHoras = Double.parseDouble(row[5].toString().trim());
                    unidadtask.setCost(1, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(2, Math.round(Double.parseDouble(row[7].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(3, Math.round(Double.parseDouble(row[8].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(4, Math.round(Double.parseDouble(row[9].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(5, ejecutado);
                    unidadtask.setNumber(1, Double.parseDouble(row[4].toString().trim()));
                    unidadtask.setNumber(2, volumen);
                    unidadtask.setNumber(3, porci);
                    unidadtask.setText(1, row[1].toString().trim());
                    unidadtask.setText(2, row[3].toString().trim());
                    if (recursos == true) {
                        setResourcesToUnidada(unidadtask, Math.toIntExact(Math.round(valHoras)), Integer.parseInt(row[0].toString().trim()));
                    } else if (recursos == false) {
                        getRebglonesToUnidad(unidadtask, Integer.parseInt(row[0].toString().trim()));

                    }

                }
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

    public void getSubEspTaskResourcesArray(Task subespTask, int idObra, int idEmp, int idzona, int idObj, int idNiv, int idEsp, int idSub, Boolean recursos) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            avance = null;
            ejecutado = null;
            calc = null;
            porci = null;
            taskArrayList = new ArrayList<>();
            List<Object[]> listObject = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, SUM(uor.cantRv * rvr.cantidas), uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal, SUM(cert.cantidad) FROM Unidadobra uo LEFT JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId LEFT JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId LEFT JOIN Recursos rec ON rvr.recursosId = rec.id LEFT JOIN Certificacion cert ON uo.id = cert.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND uo.objetosId =: idObj AND uo.nivelId =: idNiv AND uo.especialidadesId =: idEsp AND uo.subespecialidadesId =: idSub AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).setParameter("idObj", idObj).setParameter("idNiv", idNiv).setParameter("idEsp", idEsp).setParameter("idSub", idSub).getResultList();
            for (Object[] row : listObject) {
                unidadtask = subespTask.addTask();
                unidadtask.setName(row[2].toString().trim());

                if (row[3] != null) {
                    volumen = getcantCertfificada(Integer.parseInt(row[0].toString()));

                    ejecutado = Double.parseDouble(row[9].toString().trim()) * volumen / Double.parseDouble(row[4].toString().trim());
                    calc = volumen / Double.parseDouble(row[4].toString().trim());
                    porci = calc * 100;

                    unidadtask.setCost(1, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(2, Math.round(Double.parseDouble(row[7].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(3, Math.round(Double.parseDouble(row[8].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(4, Math.round(Double.parseDouble(row[9].toString().trim()) * 100d) / 100d);
                    unidadtask.setCost(5, ejecutado);
                    unidadtask.setNumber(1, Double.parseDouble(row[4].toString().trim()));
                    unidadtask.setNumber(2, volumen);
                    unidadtask.setNumber(3, porci);
                    unidadtask.setText(1, row[1].toString().trim());
                    unidadtask.setText(2, row[3].toString().trim());

                    if (recursos == true) {
                        setResourcesToUnidada(unidadtask, 0.0, Integer.parseInt(row[0].toString().trim()));
                    } else if (recursos == false) {
                        getRebglonesToUnidad(unidadtask, Integer.parseInt(row[0].toString().trim()));

                    }


                }
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

    private Double getcantCertfificadaRV(int idUn, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Certificacionrecuo> certificacionList = session.createQuery("from Certificacionrecuo where unidadobraId=: idU AND renglonId =: idR").setParameter("idU", idUn).setParameter("idR", idRV).getResultList();

            tx.commit();
            session.close();
            return certificacionList.parallelStream().map(Certificacionrecuo::getCantidad).reduce(0.0, Double::sum);
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return 0.0;
    }

    public void getRebglonesToUnidad(Task unidTask, Integer idUni) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            total = null;
            avance = null;
            ejecutado = null;
            calc = null;
            porci = null;
            renglonesTaskArray = new ArrayList<>();
            List<Object[]> objectsList = session.createQuery("SELECT rv.id, rv.codigo, rv.descripcion, rv.um, uor.cantRv, SUM(uor.cantRv * rvr.cantidas), uor.costEquip, uor.costMano, uor.costEquip, SUM(cert.cantidad) FROM Unidadobra uo LEFT JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId LEFT JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId LEFT JOIN Renglonvariante rv ON uor.renglonvarianteId = rv.id LEFT JOIN Recursos rec ON rvr.recursosId = rec.id LEFT JOIN Certificacionrecuo cert ON uor.unidadobraId = cert.unidadobraId AND uor.renglonvarianteId = cert.renglonId WHERE uo.id =: id AND rec.tipo != '1' GROUP BY rv.id, rv.codigo, rv.descripcion, rv.um, uor.cantRv, uor.costMat, uor.costMano, uor.costEquip").setParameter("id", idUni).getResultList();
            for (Object[] row : objectsList) {
                renglonTask = unidTask.addTask();
                if (row[2].toString().trim().contains("\n")) {
                    String[] partDesc = row[2].toString().trim().split("\n");
                    renglonTask.setName(partDesc[0] + " " + partDesc[1]);
                } else {
                    renglonTask.setName(row[2].toString().trim());
                }

                total = Double.parseDouble(row[6].toString().trim()) + Double.parseDouble(row[7].toString().trim()) + Double.parseDouble(row[8].toString().trim());

                if (row[9] != null) {
                    volumen = getcantCertfificadaRV(idUni, Integer.parseInt(row[0].toString()));
                }

                ejecutado = total * volumen / Double.parseDouble(row[4].toString().trim());
                calc = volumen / Double.parseDouble(row[4].toString().trim());
                porci = calc * 100;
                valHoras = Double.parseDouble(row[5].toString().trim());

                renglonTask.setCost(1, 0.0);
                renglonTask.setCost(2, Math.round(Double.parseDouble(row[7].toString().trim()) * 100d) / 100d);
                renglonTask.setCost(3, Math.round(Double.parseDouble(row[8].toString().trim()) * 100d) / 100d);
                renglonTask.setCost(4, (Math.round(total * 100d) / 100d));
                renglonTask.setCost(5, ejecutado);

                renglonTask.setNumber(1, Double.parseDouble(row[4].toString().trim()));
                renglonTask.setNumber(2, volumen);
                renglonTask.setNumber(3, porci);
                renglonTask.setText(1, row[1].toString().trim());
                renglonTask.setText(2, row[3].toString().trim());
                renglonTask.setPercentageComplete(Math.round(calc));
                renglonTask.setPercentageWorkComplete(Math.round(calc));

                getResourcesRV(renglonTask, idUni, Integer.parseInt(row[0].toString().trim()));

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

    public void getRebglonesToUnidadNE(Task task, int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            renglonesTaskArray = new ArrayList<>();
            List<Object[]> objectsList = session.createQuery("SELECT rv.id, rv.codigo, rv.descripcion, rv.um, uor.cantidad, SUM(uor.cantidad * rvr.cantidas), rv.costomat, rv.costmano, rv.costequip, SUM(cert.cantidad) FROM Nivelespecifico uo LEFT JOIN  Renglonnivelespecifico uor ON uo.id = uor.nivelespecificoId LEFT JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId LEFT JOIN Renglonvariante rv ON uor.renglonvarianteId = rv.id LEFT JOIN Recursos rec ON rvr.recursosId = rec.id  LEFT JOIN Certificacionrecrv cert ON uo.id = cert.nivelespId AND rv.id = cert.renglonId WHERE uor.nivelespecificoId =: idUo AND rec.tipo != '1' GROUP BY rv.id, rv.codigo, rv.descripcion, rv.um, uor.cantidad, rv.costomat, rv.costmano, rv.costequip").setParameter("idUo", idUO).getResultList();
            for (Object[] row : objectsList) {
                renglonTask = task.addTask();
                renglonTask.setName(row[2].toString().trim());

                total = Double.parseDouble(row[6].toString().trim()) + Double.parseDouble(row[6].toString().trim()) + Double.parseDouble(row[8].toString().trim());
                if (row[9] != null) {
                    volumen = Double.parseDouble(row[9].toString().trim());
                } else {
                    volumen = 0.0;
                }
                ejecutado = total * volumen / Double.parseDouble(row[4].toString().trim());
                calc = volumen / Double.parseDouble(row[4].toString().trim());
                porci = calc * 100;

                valHoras = Double.parseDouble(row[5].toString().trim());
                //  renglonTask.setDuration(Duration.getInstance(Math.toIntExact(Math.round(valHoras)), TimeUnit.DAYS));
                renglonTask.setCost(1, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                renglonTask.setCost(2, Math.round(Double.parseDouble(row[7].toString().trim()) * 100d) / 100d);
                renglonTask.setCost(3, Math.round(Double.parseDouble(row[8].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(4, (Math.round(total * 100d) / 100d));
                unidadtask.setCost(5, ejecutado);

                renglonTask.setNumber(1, Double.parseDouble(row[4].toString().trim()));
                renglonTask.setNumber(2, volumen);
                renglonTask.setNumber(3, porci);
                renglonTask.setText(1, row[1].toString().trim());
                renglonTask.setText(2, row[3].toString().trim());
                renglonTask.setPercentageComplete(NumberHelper.getDouble(calc));

                getResourcesRVinRV(renglonTask, idUO, Integer.parseInt(row[0].toString().trim()));

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

    public void getZonasTaskResoucesArray(Task taskOwner, int idObra, int idEmp, int idzona, Boolean recursos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> resultList = session.createQuery("SELECT uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, SUM(uor.cantRv * rvr.cantidas), uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal FROM Unidadobra uo LEFT JOIN  Unidadobrarenglon uor ON uo.id = uor.unidadobraId LEFT JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId LEFT JOIN Recursos rec ON rvr.recursosId = rec.id LEFT JOIN Certificacion cert ON uo.id = cert.unidadobraId WHERE uo.obraId =: idOb AND uo.empresaconstructoraId =: idEmp AND uo.zonasId =: idZon AND rec.tipo != '1' GROUP BY uo.id, uo.codigo, uo.descripcion, uo.um, uo.cantidad, uo.costoMaterial, uo.costomano, uo.costoequipo, uo.costototal").setParameter("idOb", idObra).setParameter("idEmp", idEmp).setParameter("idZon", idzona).getResultList();
            volumen = null;
            ejecutado = null;
            calc = null;
            porci = null;
            for (Object[] row : resultList) {
                unidadtask = taskOwner.addTask();
                unidadtask.setName(row[2].toString().trim());

                if (row[3] != null) {
                    volumen = getcantCertfificada(Integer.parseInt(row[0].toString()));
                }
                ejecutado = Double.parseDouble(row[9].toString().trim()) * volumen / Double.parseDouble(row[4].toString().trim());
                //ejecutado = 0.0;
                calc = volumen / Double.parseDouble(row[4].toString().trim());

                valHoras = Double.parseDouble(row[5].toString().trim());
                unidadtask.setCost(1, Math.round(Double.parseDouble(row[6].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(2, Math.round(Double.parseDouble(row[7].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(3, Math.round(Double.parseDouble(row[8].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(4, Math.round(Double.parseDouble(row[9].toString().trim()) * 100d) / 100d);
                unidadtask.setCost(5, ejecutado);
                unidadtask.setNumber(1, Double.parseDouble(row[4].toString().trim()));
                unidadtask.setNumber(2, volumen);
                unidadtask.setNumber(3, calc * 100);
                unidadtask.setText(1, row[1].toString().trim());
                unidadtask.setText(2, row[3].toString().trim());

                if (recursos == true) {
                    setResourcesToUnidada(unidadtask, 0.0, Integer.parseInt(row[0].toString().trim()));
                } else if (recursos == false) {
                    getRebglonesToUnidad(unidadtask, Integer.parseInt(row[0].toString().trim()));
                }


            }


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        // return taskArrayList;
    }

    /**
     * Para las obras por Unidad de obra
     *
     * @param idObra
     * @return
     */
    private List<DataResourcesToProject> getResorcesInObra(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<DataResourcesToProject> resourceArrayList = new ArrayList<>();
            List<Tuple> cuantitativaGenral = session.createQuery("SELECT rec.codigo, rec.descripcion, rec.preciomn, rec.tipo FROM Unidadobra uo INNER JOIN Unidadobrarenglon our ON uo.id = our.unidadobraId INNER JOIN Renglonrecursos rvr ON our.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id  WHERE rec.tipo != '1' AND uo.obraId =: idO  GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo", Tuple.class).setParameter("idO", idObra).getResultList();
            for (Tuple tuple : cuantitativaGenral) {
                if (tuple.get(3).toString().trim().equals("2")) {
                    resourceArrayList.add(new DataResourcesToProject(tuple.get(0).toString().trim(), tuple.get(1).toString().trim(), Double.parseDouble(tuple.get(2).toString()), "Mano de Obra"));
                } else if (tuple.get(3).toString().trim().equals("3")) {
                    resourceArrayList.add(new DataResourcesToProject(tuple.get(0).toString().trim(), tuple.get(1).toString().trim(), Double.parseDouble(tuple.get(2).toString()), "Equipos"));

                }
            }

            if (checkMateriales.isSelected()) {
                List<Tuple> cuantitativaRecursos = session.createQuery("SELECT bajo.idSuministro, bajo.tipo from Bajoespecificacion bajo INNER JOIN Unidadobra  uo ON uo.id = bajo.unidadobraId  WHERE uo.obraId =: unId GROUP BY bajo.idSuministro, bajo.tipo", Tuple.class).setParameter("unId", idObra).getResultList();
                for (Tuple tuple : cuantitativaRecursos) {
                    if (tuple.get(1).toString().trim().equals("1")) {
                        Recursos recursos = getRecurso(Integer.parseInt(tuple.get(0).toString()));
                        resourceArrayList.add(new DataResourcesToProject(recursos.getCodigo().trim(), recursos.getDescripcion().trim(), recursos.getPreciomn(), "Materiales"));
                    } else if (tuple.get(1).toString().trim().equals("S")) {
                        Suministrossemielaborados recursos = getSuministrossemielaborados(Integer.parseInt(tuple.get(0).toString()));
                        resourceArrayList.add(new DataResourcesToProject(recursos.getCodigo().trim(), recursos.getDescripcion().trim(), recursos.getPreciomn(), "Materiales"));

                    }

                }
            }

            tx.commit();
            session.close();
            return resourceArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }

    /**
     * Para las obras por Renglones Variantes
     *
     * @param idObra
     * @return
     */
    private List<DataResourcesToProject> getResorcesInObraRV(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<DataResourcesToProject> resourceArrayList = new ArrayList<>();
            List<Tuple> cuantitativaGenral = session.createQuery("SELECT rec.codigo, rec.descripcion, rec.preciomn, rec.tipo FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico our ON uo.id = our.nivelespecificoId INNER JOIN Renglonrecursos rvr ON our.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id  WHERE rec.tipo != '1' AND uo.obraId =: idO  GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo", Tuple.class).setParameter("idO", idObra).getResultList();
            for (Tuple tuple : cuantitativaGenral) {
                if (tuple.get(3).toString().trim().equals("2")) {
                    resourceArrayList.add(new DataResourcesToProject(tuple.get(0).toString().trim(), tuple.get(1).toString().trim(), Double.parseDouble(tuple.get(2).toString()), "Mano de Obra"));
                } else if (tuple.get(3).toString().trim().equals("3")) {
                    resourceArrayList.add(new DataResourcesToProject(tuple.get(0).toString().trim(), tuple.get(1).toString().trim(), Double.parseDouble(tuple.get(2).toString()), "Equipos"));

                }
            }

            if (checkMateriales.isSelected()) {
                List<Tuple> cuantitativaRecursos = session.createQuery("SELECT bajo.idsuministro, bajo.tipo from Bajoespecificacionrv bajo INNER JOIN Nivelespecifico  uo ON uo.id = bajo.nivelespecificoId  WHERE uo.obraId =: unId GROUP BY bajo.idsuministro, bajo.tipo", Tuple.class).setParameter("unId", idObra).getResultList();
                for (Tuple tuple : cuantitativaRecursos) {
                    if (tuple.get(1).toString().trim().equals("1")) {
                        Recursos recursos = getRecurso(Integer.parseInt(tuple.get(0).toString()));
                        resourceArrayList.add(new DataResourcesToProject(recursos.getCodigo().trim(), recursos.getDescripcion().trim(), recursos.getPreciomn(), "Materiales"));
                    } else if (tuple.get(1).toString().trim().equals("S")) {
                        Suministrossemielaborados recursos = getSuministrossemielaborados(Integer.parseInt(tuple.get(0).toString()));
                        resourceArrayList.add(new DataResourcesToProject(recursos.getCodigo().trim(), recursos.getDescripcion().trim(), recursos.getPreciomn(), "Materiales"));

                    }

                }
            }

            tx.commit();
            session.close();
            return resourceArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return new ArrayList<>();
    }

    public javafx.concurrent.Task createTime(Integer val) {
        return new javafx.concurrent.Task() {
            @Override
            protected Object call() throws Exception {

                for (int i = 0; i < val; i++) {
                    Thread.sleep(val * 5);
                    updateProgress(i + 1, val);
                }
                return true;
            }
        };
    }

    public javafx.concurrent.Task createTimeMesage() {
        return new javafx.concurrent.Task() {
            @Override
            protected Object call() throws Exception {

                //for (int i = 0; i < ; i++) {
                Thread.sleep(2000);
                //  updateProgress(i + 1, val);
                //}
                return true;
            }
        };
    }

    /**
     * Coregir
     *
     * @param task
     * @param duration
     * @param idRV
     */
    public void getResourcesRV(Task task, int duration, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> listObjects = session.createQuery("SELECT rec.codigo, rec.descripcion, SUM(our.cantRv * rvr.cantidas), rec.preciomn, rec.tipo  FROM  Renglonrecursos rvr INNER JOIN Recursos rec ON rvr.recursosId = rec.id INNER JOIN Unidadobrarenglon our ON rvr.renglonvarianteId = our.renglonvarianteId  WHERE rec.tipo != '1' AND our.renglonvarianteId =: idRv AND our.unidadobraId =: idU GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo").setParameter("idRv", idRV).setParameter("idU", duration).getResultList();
            for (Object[] row : listObjects) {
                if (getResourceByName(row[1].toString().trim()) == null) {
                    resource = createResources(row[0].toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    if (row[4].toString().trim().equals("3")) {
                        double val = Double.parseDouble(row[2].toString()) * coefEquip;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    } else if (row[4].toString().trim().equals("2")) {
                        double val = Double.parseDouble(row[2].toString()) * coefMano;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    }
                    resoucesArraylist.add(resource);
                } else {
                    resource = getResourceByName(row[1].toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    if (row[4].toString().trim().equals("3")) {
                        double val = Double.parseDouble(row[2].toString()) * coefEquip;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    } else if (row[4].toString().trim().equals("2")) {
                        double val = Double.parseDouble(row[2].toString()) * coefMano;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    }
                }
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

    public void getResourcesRVinRV(Task task, int duration, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> listObjects = session.createQuery("SELECT rec.codigo, rec.descripcion, SUM(our.cantidad * rvr.cantidas), rec.preciomn, rec.tipo  FROM  Renglonrecursos rvr INNER JOIN Recursos rec ON rvr.recursosId = rec.id INNER JOIN Renglonnivelespecifico our ON rvr.renglonvarianteId = our.renglonvarianteId  WHERE rec.tipo != '1' AND our.renglonvarianteId =: idRv AND our.nivelespecificoId =: idU GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo").setParameter("idRv", idRV).setParameter("idU", duration).getResultList();
            for (Object[] row : listObjects) {
                if (getResourceByName(row[1].toString().trim()) == null) {
                    resource = createResources(row[0].toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(row[2].toString()) * coefMano;
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resoucesArraylist.add(resource);
                } else {
                    resource = getResourceByName(row[1].toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(row[2].toString()) * coefMano;
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                }
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

    public void getResourcesInZonasGeneralRV(Task task, int idZ) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> listObjects = session.createQuery("SELECT rec.codigo, rec.descripcion, SUM(our.cantidad * rvr.cantidas), rec.preciomn, rec.tipo  FROM  Renglonrecursos rvr INNER JOIN Recursos rec ON rvr.recursosId = rec.id INNER JOIN Renglonnivelespecifico our ON rvr.renglonvarianteId = our.renglonvarianteId INNER JOIN Nivelespecifico niv ON our.nivelespecificoId = niv.id WHERE rec.tipo != '1' AND niv.zonasId =: idU GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo", Tuple.class).setParameter("idU", idZ).getResultList();
            for (Tuple t : listObjects) {
                if (getResourceByName(t.get(0).toString().trim()) == null) {
                    resource = createResources(t.get(0).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resoucesArraylist.add(resource);
                } else {
                    resource = getResourceByName(t.get(1).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                }
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

    public void getResourcesInObjectsGeneralRV(Task task, int idZ) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> listObjects = session.createQuery("SELECT rec.codigo, rec.descripcion, SUM(our.cantidad * rvr.cantidas), rec.preciomn, rec.tipo  FROM  Renglonrecursos rvr INNER JOIN Recursos rec ON rvr.recursosId = rec.id INNER JOIN Renglonnivelespecifico our ON rvr.renglonvarianteId = our.renglonvarianteId INNER JOIN Nivelespecifico niv ON our.nivelespecificoId = niv.id WHERE rec.tipo != '1' AND niv.objetosId =: idU GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo", Tuple.class).setParameter("idU", idZ).getResultList();
            for (Tuple t : listObjects) {
                if (getResourceByName(t.get(0).toString().trim()) == null) {
                    resource = createResources(t.get(0).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resoucesArraylist.add(resource);
                } else {
                    resource = getResourceByName(t.get(1).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                }
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

    public void getResourcesEspecialidadINGeneralRV(Task task, int idO, int idZ, int idOB, int idN, int idE, int idES) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> listObjects = session.createQuery("SELECT rec.codigo, rec.descripcion, SUM(our.cantidad * rvr.cantidas), rec.preciomn, rec.tipo  FROM  Renglonrecursos rvr INNER JOIN Recursos rec ON rvr.recursosId = rec.id INNER JOIN Renglonnivelespecifico our ON rvr.renglonvarianteId = our.renglonvarianteId INNER JOIN Nivelespecifico niv ON our.nivelespecificoId = niv.id WHERE rec.tipo != '1' AND niv.obraId =: id1 AND  niv.zonasId =: id2 AND  niv.objetosId =: id3 AND  niv.nivelId =: id4 AND  niv.empresaconstructoraId =: id5  AND  niv.especialidadesId =: id6 GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo", Tuple.class).setParameter("id1", idO).setParameter("id2", idZ).setParameter("id3", idOB).setParameter("id4", idN).setParameter("id5", idE).setParameter("id6", idES).getResultList();
            for (Tuple t : listObjects) {
                if (getResourceByName(t.get(0).toString().trim()) == null) {
                    resource = createResources(t.get(0).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resoucesArraylist.add(resource);
                } else {
                    resource = getResourceByName(t.get(1).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                }
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

    public void getResourcesInNivelGeneralRV(Task task, int idZ) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Tuple> listObjects = session.createQuery("SELECT rec.codigo, rec.descripcion, SUM(our.cantidad * rvr.cantidas), rec.preciomn, rec.tipo  FROM  Renglonrecursos rvr INNER JOIN Recursos rec ON rvr.recursosId = rec.id INNER JOIN Renglonnivelespecifico our ON rvr.renglonvarianteId = our.renglonvarianteId INNER JOIN Nivelespecifico niv ON our.nivelespecificoId = niv.id WHERE rec.tipo != '1' AND niv.nivelId =: idU GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo", Tuple.class).setParameter("idU", idZ).getResultList();
            for (Tuple t : listObjects) {
                if (getResourceByName(t.get(0).toString().trim()) == null) {
                    resource = createResources(t.get(0).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resoucesArraylist.add(resource);
                } else {
                    resource = getResourceByName(t.get(1).toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    double val = Double.parseDouble(t.get(2).toString());
                    resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                    resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                }
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

    private Resource getResourceByName(String code) {
        return resoucesArraylist.parallelStream().filter(res -> res.getName().trim().equals(code.trim())).findFirst().orElse(null);
    }

    private Resource createResources(String code) {
        DataResourcesToProject data = allResourcesList.parallelStream().filter(item -> item.getCode().trim().equals(code.trim())).findFirst().orElse(null);
        Resource resource = project.addResource();
        String desc = data.getDescrip().trim();
        resource.setText(1, data.getCode());
        resource.setName(desc);
        resource.setInitials(String.valueOf(desc.charAt(0)));
        Rate rate = new Rate(data.getPrecio() / 8, null);
        resource.setStandardRate(rate);
        resource.setMaxUnits(1 * 100);
        resource.setPeakUnits(1 * 100);
        resource.setGroup(data.getTipo());
        resource.setAccrueAt(AccrueType.getInstance(3));
        return resource;
    }

    private void setResourcesToUnidada(Task task, double duration, int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Object[]> listObjects = session.createQuery("SELECT rec.codigo, rec.descripcion,  SUM(uor.cantRv * rvr.cantidas), rec.preciomn, rec.tipo  FROM Unidadobra uo INNER JOIN Unidadobrarenglon uor ON uo.id = uor.unidadobraId INNER JOIN Renglonrecursos rvr ON uor.renglonvarianteId = rvr.renglonvarianteId INNER JOIN Recursos rec ON rvr.recursosId = rec.id WHERE rec.tipo != '1' AND uo.id =: idUO  GROUP BY rec.codigo, rec.descripcion, rec.preciomn, rec.tipo").setParameter("idUO", idUO).getResultList();
            for (Object[] row : listObjects) {
                if (getResourceByName(row[1].toString().trim()) == null) {
                    resource = createResources(row[0].toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    if (row[4].toString().trim().equals("3")) {
                        double val = Double.parseDouble(row[2].toString()) * coefEquip;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    } else if (row[4].toString().trim().equals("2")) {
                        double val = Double.parseDouble(row[2].toString()) * coefMano;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    }
                    resoucesArraylist.add(resource);
                } else {
                    resource = getResourceByName(row[1].toString().trim());
                    ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                    if (row[4].toString().trim().equals("3")) {
                        double val = Double.parseDouble(row[2].toString()) * coefEquip;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    } else if (row[4].toString().trim().equals("2")) {
                        double val = Double.parseDouble(row[2].toString()) * coefMano;
                        resourceAssignment.setRemainingWork(Duration.getInstance(val, TimeUnit.HOURS));
                        resourceAssignment.setWork(Duration.getInstance(val, TimeUnit.HOURS));
                    }
                }
            }

            if (checkMateriales.isSelected()) {
                List<Bajoespecificacion> bajoespecificacions = session.createQuery(" from Bajoespecificacion WHERE unidadobraId =: unId ").setParameter("unId", idUO).getResultList();
                for (Bajoespecificacion bajoespecificacion : bajoespecificacions) {
                    if (bajoespecificacion.getTipo().trim().equals("1")) {
                        Recursos recursos = getRecurso(bajoespecificacion.getIdSuministro());
                        if (getResourceByName(recursos.getDescripcion()) == null) {
                            resource = createResources(recursos.getCodigo());
                            ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                            resoucesArraylist.add(resource);
                        } else {
                            resource = getResourceByName(recursos.getCodigo());
                            ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                        }
                    } else if (bajoespecificacion.getTipo().trim().equals("S")) {
                        Suministrossemielaborados recursos = getSuministrossemielaborados(bajoespecificacion.getIdSuministro());
                        if (getResourceByName(recursos.getDescripcion()) == null) {
                            resource = createResources(recursos.getCodigo());
                            ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                            resoucesArraylist.add(resource);
                        } else {
                            resource = getResourceByName(recursos.getCodigo());
                            ResourceAssignment resourceAssignment = task.addResourceAssignment(resource);
                        }
                    }
                }

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

    public void createProjectTemplate(ActionEvent event) {
        coefMano = Double.parseDouble(fieldMO.getText().trim());
        coefEquip = Double.parseDouble(fieldEquipo.getText().trim());
        if (checkUnidad.isSelected() == true && checkNormPrec.isSelected() == true && tipoUO.isSelected() == true && tipoRV.isSelected() == false && !comboEmpresas.getValue().equals("Todas")) {
            myFlag = true;
            createProjectUOSimple();
        } else if (checkUnidad.isSelected() == true && checkNormPrec.isSelected() == true && tipoUO.isSelected() == true && tipoRV.isSelected() == false && comboEmpresas.getValue().equals("Todas")) {
            myFlag = true;
            createProjectUOMultiple();
        }
        if (checkUnidad.isSelected() == true && checkNormPrec.isSelected() == true && tipoUO.isSelected() == true && tipoRV.isSelected() == true && !comboEmpresas.getValue().equals("Todas")) {
            myFlag = true;
            createProjectUORVSimple();
        } else if (checkUnidad.isSelected() == true && checkNormPrec.isSelected() == true && tipoUO.isSelected() == true && tipoRV.isSelected() == true && comboEmpresas.getValue().equals("Todas")) {
            myFlag = true;
            createProjectUORVMultiple();
        }

        //if (checkRV.isSelected() == true && checkNormPrec.isSelected() == true && tipoUO.isSelected() == false && tipoRV.isSelected() == true && !comboEmpresas.getValue().equals("Todas")) {
        if (checkRV.isSelected() == true && checkNormPrec.isSelected() == true && tipoUO.isSelected() == false && !comboEmpresas.getValue().equals("Todas")) {
            myFlag = true;
            createRVXMLtoProjectSimple();
        } else if (checkRV.isSelected() == true && checkNormPrec.isSelected() == true && tipoUO.isSelected() == false && tipoRV.isSelected() == true && comboEmpresas.getValue().equals("Todas")) {
            myFlag = true;
            createRVXMLtoProjectMultiple();
        }


    }

    public void handleObjetos(ActionEvent event) {
        if (!comboZonas.getValue().contentEquals("Todas")) {
            Zonas zona = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

            comboObjetos.getItems().setAll(projectStructureSingelton.getObjetosList(zona.getId()));
        } else if (comboZonas.getValue().contentEquals("Todas")) {
            comboObjetos.getItems().add("Todos");
        }
        // comboObjetos.setEditable(true);
        TextFields.bindAutoCompletion(comboObjetos.getEditor(), comboObjetos.getItems()).setPrefWidth(comboObjetos.getPrefWidth());
    }

    public void handleNivel(ActionEvent event) {
        if (!comboObjetos.getValue().contentEquals("Todos")) {
            Objetos objeto = projectStructureSingelton.objetosArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();
            comboNivel.getItems().setAll(projectStructureSingelton.getNivelList(objeto.getId()));
        } else if (comboObjetos.getValue().contentEquals("Todos")) {
            comboNivel.getItems().add("Todos");
        }
        //comboNivel.setEditable(true);
        TextFields.bindAutoCompletion(comboNivel.getEditor(), comboNivel.getItems()).setPrefWidth(comboNivel.getPrefWidth());
    }

    public void handleSubespecilaidad(ActionEvent event) {
        if (!comboEspecialidades.getValue().contentEquals("Todas")) {
            Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();
            comboSubespecialidades.getItems().setAll(projectStructureSingelton.getSubespecialidadesObservableList(especialidades.getId()));
        } else if (comboEspecialidades.getValue().contentEquals("Todas")) {
            comboSubespecialidades.getItems().add("Todas");
        }
        //comboSubespecialidades.setEditable(true);
        TextFields.bindAutoCompletion(comboSubespecialidades.getEditor(), comboSubespecialidades.getItems()).setPrefWidth(comboSubespecialidades.getPrefWidth());
    }

    public void handleZonasEmpresa(ActionEvent event) {
        Obra obra = projectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue())).findFirst().get();
        comboZonas.getItems().setAll(projectStructureSingelton.getZonasList(obra.getId()));
        //comboZonas.setEditable(true);
        TextFields.bindAutoCompletion(comboZonas.getEditor(), comboZonas.getItems()).setPrefWidth(comboZonas.getPrefWidth());
        ObservableList<String> datos = FXCollections.observableArrayList();
        datos.add("Todas");
        for (String s : projectStructureSingelton.getEmpresaList(obra.getId())) {
            datos.add(s);
        }
        comboEmpresas.getItems().setAll(datos);
        //comboEmpresas.setEditable(true);
        TextFields.bindAutoCompletion(comboEmpresas.getEditor(), comboEmpresas.getItems()).setPrefWidth(comboEmpresas.getPrefWidth());
    }

    private void createRVXMLtoProjectMultiple() {
        myFlag = false;
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Procesando petición del usuario...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        mspdiWriter = new MSPDIWriter();
        project = new ProjectFile();
        project.addCalendar().remove();

        CustomFieldContainer customFields = project.getCustomFields();

        CustomField field = customFields.getCustomField(TaskField.TEXT1);
        field.setAlias("Código");

        CustomField field1 = customFields.getCustomField(TaskField.TEXT2);
        field1.setAlias("U/M");

        CustomField fieldCost1 = customFields.getCustomField(TaskField.COST1);
        fieldCost1.setAlias("Costo Material");

        CustomField fieldCost2 = customFields.getCustomField(TaskField.COST2);
        fieldCost2.setAlias("Costo Mano de Obra");

        CustomField fieldCost3 = customFields.getCustomField(TaskField.COST3);
        fieldCost3.setAlias("Costo de Equipos");

        CustomField fieldCost4 = customFields.getCustomField(TaskField.COST4);
        fieldCost4.setAlias("Costo Total");

        CustomField fieldCost5 = customFields.getCustomField(TaskField.COST5);
        fieldCost5.setAlias("Costo Ejecutado");

        CustomField fieldNumber = customFields.getCustomField(TaskField.NUMBER1);
        fieldNumber.setAlias("Cantidad");

        CustomField fieldNumber1 = customFields.getCustomField(TaskField.NUMBER2);
        fieldNumber1.setAlias("Volumen Ejecutado");

        CustomField fieldNumber2 = customFields.getCustomField(TaskField.NUMBER3);
        fieldNumber2.setAlias("Porciento de Ejecución");

        if (comboObras.getValue() != null) {
            obra = projectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue())).findFirst().get();

        }

        allResourcesList = new ArrayList<>();
        allResourcesList = getResorcesInObraRV(Integer.parseInt(partObras[0].toString()));
        allResourcesList.size();
        resoucesArraylist = new ArrayList<>();

        rootTask = project.addTask();
        rootTask.setName(obra.getDescripion().trim());

        if (comboZonas.getValue() != null && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasksNE(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    getZonasTaskResoucesArrayNE(zonasTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


        } else if (comboZonas.getValue() != null && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {

                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                getZonasTaskResoucesArrayNE(zonasTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId());

            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }
        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTaskNE(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());
                    getObjetosTaskResoucesArrayNE(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId());
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());

                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                getObjetosTaskResoucesArrayNE(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId());
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());

                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());

                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        getObjetosTaskResoucesArrayNE(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                    }

                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }

        if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                nivelArrayList = new ArrayList<>();
                nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId());
                for (Nivel nivel : nivelArrayList) {

                    nivelTask = objetoTask.addTask();
                    nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                    nivelTask.setName(nivTaskBuider.toString().trim());
                    getNivelTaskResourceArrayNE(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId());
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = zonasArrayList.stream().filter(z -> z.getDesripcion().equals(partZonas[1])).findFirst().orElse(null);

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = objetosArrayList.stream().filter(obj -> obj.getDescripcion().equals(partObj[1])).findFirst().orElse(null);

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = nivelArrayList.stream().filter(niv -> niv.getDescripcion().equals(partNiv[1])).findFirst().orElse(null);

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                getNivelTaskResourceArrayNE(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId());
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {

                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());
                            getNivelTaskResourceArrayNE(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId());
                        }
                    }

                }
            }

            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }
        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() == null) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                especialidadesArrayList = new ArrayList<>();
                especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId());
                for (Especialidades especialidades : especialidadesArrayList) {

                    especialidadTask = nivelTask.addTask();
                    espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                    especialidadTask.setName(espTaskBuider.toString().trim());

                    getEspecialidadTaskResourcesArrayNE(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());


                getEspecialidadTaskResourcesArrayNE(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());
                                getEspecialidadTaskResourcesArrayNE(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                            }

                        }
                    }
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }
        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                subespecialidadesArrayList = new ArrayList<>();
                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                    subespTask = especialidadTask.addTask();
                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                    subespTask.setName(subTaskBuider.toString().trim());

                    getSubEspecialidadTaskResourcesArrayNE(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId());
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && !comboSubespecialidades.getValue().equals("Todas")) {
            myFlag = true;
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                partSub = comboSubespecialidades.getValue().split(" - ");
                Subespecialidades subespecialidades = projectStructureSingelton.subespecialidadesArrayList.stream().filter(esp -> esp.getDescripcion().equals(partSub[1])).findFirst().orElse(null);

                subespTask = especialidadTask.addTask();
                subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                subespTask.setName(subTaskBuider.toString().trim());

                getSubEspecialidadTaskResourcesArrayNE(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId());
            }

            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
            List<Empresaobra> empresaobraList = new ArrayList<>();
            empresaobraList = getEmpresaobras(obra.getId());
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());

                                subespecialidadesArrayList = new ArrayList<>();
                                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                                    subespTask = especialidadTask.addTask();
                                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                                    subespTask.setName(subTaskBuider.toString().trim());
                                    getSubEspecialidadTaskResourcesArrayNE(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId());
                                }
                            }

                        }
                    }
                }
            }
        }


        try {
            if (mspselect.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setContentText("Seleccine la versión de Microsoft Project a exportar");
                alert.showAndWait();
            } else if (mspselect.getValue().endsWith("2013")) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar exportación");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                    mspdiWriter.write(project, file.getAbsolutePath());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Exportación terminada!");
                    alert.showAndWait();
                }
            } else if (mspselect.getValue().endsWith("2016")) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar exportación");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                    mspdiWriter.write(project, file.getAbsolutePath());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Exportación terminada!");
                    alert.showAndWait();
                }
            }


        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(ex.getMessage());
        }
    }

    private void createRVXMLtoProjectSimple() {
        myFlag = false;
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Procesando petición del usuario...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        mspdiWriter = new MSPDIWriter();
        project = new ProjectFile();
        project.getBaselineCalendar().remove();

        CustomFieldContainer customFields = project.getCustomFields();

        CustomField field = customFields.getCustomField(TaskField.TEXT1);
        field.setAlias("Código");

        CustomField field1 = customFields.getCustomField(TaskField.TEXT2);
        field1.setAlias("U/M");

        CustomField fieldCost1 = customFields.getCustomField(TaskField.COST1);
        fieldCost1.setAlias("Costo Material");

        CustomField fieldCost2 = customFields.getCustomField(TaskField.COST2);
        fieldCost2.setAlias("Costo Mano de Obra");

        CustomField fieldCost3 = customFields.getCustomField(TaskField.COST3);
        fieldCost3.setAlias("Costo de Equipos");

        CustomField fieldCost4 = customFields.getCustomField(TaskField.COST4);
        fieldCost4.setAlias("Costo Total");

        CustomField fieldCost5 = customFields.getCustomField(TaskField.COST5);
        fieldCost5.setAlias("Costo Ejecutado");

        CustomField fieldNumber = customFields.getCustomField(TaskField.NUMBER1);
        fieldNumber.setAlias("Cantidad");

        CustomField fieldNumber1 = customFields.getCustomField(TaskField.NUMBER2);
        fieldNumber1.setAlias("Volumen Ejecutado");

        CustomField fieldNumber2 = customFields.getCustomField(TaskField.NUMBER3);
        fieldNumber2.setAlias("Porciento de Ejecución");

        if (comboObras.getValue() != null) {
            obra = projectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue())).findFirst().get();

        }
        allResourcesList = new ArrayList<>();
        allResourcesList = getResorcesInObraRV(obra.getId());
        allResourcesList.size();
        resoucesArraylist = new ArrayList<>();

        if (comboEmpresas.getValue() != null) {
            Empresaconstructora empresaconstructora = projectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEmpresas.getValue().trim())).findFirst().get();
            rootTask = project.addTask();
            rootTask.setName(obra.getDescripion().trim());
            net.sf.mpxj.Task empresaTask = rootTask.addTask();
            empresaTask.setName(empresaconstructora.getDescripcion().trim());
            if (comboZonas.getValue() != null && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasksNE(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    if (tipoRV.isSelected()) {
                        getZonasTaskResoucesArrayNE(zonasTask, obra.getId(), empresaconstructora.getId(), zonas.getId());
                    } else {
                        completeZonaAsGeneralTask(zonasTask, zonas);
                    }
                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue() != null && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                if (tipoRV.isSelected()) {
                    getZonasTaskResoucesArrayNE(zonasTask, obra.getId(), empresaconstructora.getId(), zon.getId());
                } else {
                    completeZonaAsGeneralTask(zonasTask, zon);
                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }

            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                completeZonaAsGeneralTask(zonasTask, zon);

                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());
                    if (tipoRV.isSelected()) {
                        getObjetosTaskResoucesArrayNE(objetoTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                    } else {
                        completeObjectAsGeneralTask(objetoTask, objetos, zon);
                    }
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                completeZonaAsGeneralTask(zonasTask, zon);

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                if (tipoRV.isSelected()) {
                    getObjetosTaskResoucesArrayNE(objetoTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                } else {
                    completeObjectAsGeneralTask(objetoTask, objetos, zon);
                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasksNE(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.getId() == zonas.getId()).findFirst().get();

                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    completeZonaAsGeneralTask(zonasTask, zonas);

                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        if (tipoRV.isSelected()) {
                            getObjetosTaskResoucesArrayNE(objetoTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        } else {
                            completeObjectAsGeneralTask(objetoTask, objetos, zon);
                        }
                    }

                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            }
            if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                System.out.println("<<<Aqui entro yo...");
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                completeZonaAsGeneralTask(zonasTask, zon);

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();
                completeObjectAsGeneralTask(objetoTask, objetos, zon);
                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                nivelArrayList = new ArrayList<>();
                nivelArrayList = getNivelToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                for (Nivel nivel : nivelArrayList) {
                    nivelTask = objetoTask.addTask();
                    nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                    nivelTask.setName(nivTaskBuider.toString().trim());
                    if (tipoRV.isSelected()) {
                        getNivelTaskResourceArrayNE(nivelTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                    } else {
                        completeNivelsAsGeneralTask(nivelTask, nivel, objetos);
                    }
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                completeZonaAsGeneralTask(zonasTask, zon);

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                completeObjectAsGeneralTask(objetoTask, objetos, zon);
                Nivel nivel = projectStructureSingelton.nivelArrayList.stream().filter(niv -> niv.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());
                if (tipoRV.isSelected()) {
                    getNivelTaskResourceArrayNE(nivelTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                } else {
                    completeNivelsAsGeneralTask(nivelTask, nivel, objetos);
                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.getId() == zonas.getId()).findFirst().get();
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    completeZonaAsGeneralTask(zonasTask, zonas);
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        Objetos objt = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.getId() == objetos.getId()).findFirst().get();
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        completeObjectAsGeneralTask(objetoTask, objetos, zon);
                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());
                            if (tipoRV.isSelected()) {
                                getNivelTaskResourceArrayNE(nivelTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId());
                            } else {
                                completeNivelsAsGeneralTask(nivelTask, nivel, objt);
                            }
                        }
                    }

                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }
            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                completeZonaAsGeneralTask(zonasTask, zon);

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                completeObjectAsGeneralTask(objetoTask, objetos, zon);

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());
                completeNivelsAsGeneralTask(nivelTask, nivel, objetos);

                especialidadesArrayList = new ArrayList<>();
                especialidadesArrayList = getEspecialidadToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                for (Especialidades especialidades : especialidadesArrayList) {

                    especialidadTask = nivelTask.addTask();
                    espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                    especialidadTask.setName(espTaskBuider.toString().trim());
                    if (tipoRV.isSelected()) {
                        getEspecialidadTaskResourcesArrayNE(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                    } else {
                        List<Nivelespecifico> basicList = getDatoNivelEspecialidades(obra.getId(), zon.getId(), objetos.getId(), nivel.getId(), empresaconstructora.getId(), especialidades.getId());
                        completeEspecialidadsAsGeneralTask(especialidadTask, basicList, obra.getId(), zon.getId(), objetos.getId(), nivel.getId(), empresaconstructora.getId(), especialidades.getId());
                    }
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                completeZonaAsGeneralTask(zonasTask, zon);

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                completeObjectAsGeneralTask(objetoTask, objetos, zon);

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());
                completeNivelsAsGeneralTask(nivelTask, nivel, objetos);

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                if (tipoRV.isSelected()) {
                    getEspecialidadTaskResourcesArrayNE(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                } else {
                    List<Nivelespecifico> basicList = getDatoNivelEspecialidades(obra.getId(), zon.getId(), objetos.getId(), nivel.getId(), empresaconstructora.getId(), especialidades.getId());
                    completeEspecialidadsAsGeneralTask(especialidadTask, basicList, obra.getId(), zon.getId(), objetos.getId(), nivel.getId(), empresaconstructora.getId(), especialidades.getId());
                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {

                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasksNE(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.getId() == zonas.getId()).findFirst().get();
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    completeZonaAsGeneralTask(zonasTask, zon);

                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId());

                    for (Objetos objetos : objetosArrayList) {
                        Objetos objt = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.getId() == objetos.getId()).findFirst().get();

                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        completeObjectAsGeneralTask(objetoTask, objt, zon);

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            Nivel niv = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.getId() == nivel.getId()).findFirst().get();

                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());
                            completeNivelsAsGeneralTask(nivelTask, niv, objt);

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());
                                if (tipoRV.isSelected()) {
                                    getEspecialidadTaskResourcesArrayNE(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                                } else {
                                    List<Nivelespecifico> basicList = getDatoNivelEspecialidades(obra.getId(), zon.getId(), objetos.getId(), nivel.getId(), empresaconstructora.getId(), especialidades.getId());
                                    completeEspecialidadsAsGeneralTask(especialidadTask, basicList, obra.getId(), zon.getId(), objetos.getId(), nivel.getId(), empresaconstructora.getId(), especialidades.getId());
                                }
                            }
                        }
                    }
                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (!comboZonas.getValue().contentEquals("Todas") && comboZonas.getValue() != null && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());

                    nivelArrayList = new ArrayList<>();
                    nivelArrayList = getNivelToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                    for (Nivel nivel : nivelArrayList) {
                        nivelTask = objetoTask.addTask();
                        nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                        nivelTask.setName(nivTaskBuider.toString().trim());

                        especialidadesArrayList = new ArrayList<>();
                        especialidadesArrayList = getEspecialidadToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                        for (Especialidades especialidades : especialidadesArrayList) {
                            especialidadTask = nivelTask.addTask();
                            espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                            especialidadTask.setName(espTaskBuider.toString().trim());
                            getEspecialidadTaskResourcesArrayNE(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                        }

                    }
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            }
            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                subespecialidadesArrayList = new ArrayList<>();
                subespecialidadesArrayList = getSubEspecialidadToTaskNE(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                    subespTask = especialidadTask.addTask();
                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                    subespTask.setName(subTaskBuider.toString().trim());

                    getSubEspecialidadTaskResourcesArrayNE(subespTask, obra.getId(), empresaconstructora.getId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId());
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && !comboSubespecialidades.getValue().equals("Todas")) {
                myFlag = true;

                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                partSub = comboSubespecialidades.getValue().split(" - ");
                Subespecialidades subespecialidades = projectStructureSingelton.subespecialidadesArrayList.stream().filter(esp -> esp.getDescripcion().equals(partSub[1])).findFirst().orElse(null);

                subespTask = especialidadTask.addTask();
                subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                subespTask.setName(subTaskBuider.toString().trim());

                getSubEspecialidadTaskResourcesArrayNE(subespTask, obra.getId(), empresaconstructora.getId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId());


                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasksNE(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());

                                subespecialidadesArrayList = new ArrayList<>();
                                subespecialidadesArrayList = getSubEspecialidadToTaskNE(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                                    subespTask = especialidadTask.addTask();
                                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                                    subespTask.setName(subTaskBuider.toString().trim());
                                    getSubEspecialidadTaskResourcesArrayNE(subespTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId());
                                }
                            }

                        }
                    }

                }
            }

            try {
                if (mspselect.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Seleccine la versión de Microsoft Project a exportar");
                    alert.showAndWait();
                } else if (mspselect.getValue().endsWith("2013")) {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Salvar exportación");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                        mspdiWriter.write(project, file.getAbsolutePath());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información");
                        alert.setContentText("Exportación terminada!");
                        alert.showAndWait();
                    }
                } else if (mspselect.getValue().endsWith("2016")) {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Salvar exportación");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                        mspdiWriter.write(project, file.getAbsolutePath());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información");
                        alert.setContentText("Exportación terminada!");
                        alert.showAndWait();
                    }
                }


            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText(ex.getMessage());
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar una empresa");
            alert.showAndWait();
        }
    }

    private void completeEspecialidadsAsGeneralTask(Task especialidadTask, List<Nivelespecifico> basicList, int id, int zonId, int objetosId, int nivelId, int empresaconstructoraId, int especialidadesId) {
        double costoMat = basicList.parallelStream().map(Nivelespecifico::getCostomaterial).reduce(0.0, Double::sum);
        double costoEqip = basicList.parallelStream().map(Nivelespecifico::getCostoequipo).reduce(0.0, Double::sum);
        double costoMano = basicList.parallelStream().map(Nivelespecifico::getCostomano).reduce(0.0, Double::sum);
        double total = costoMat + costoEqip + costoMano;
        double calcExecutedCost = getExcutedCostInEspecialidadfulNivel(basicList);
        double execut = total - calcExecutedCost;
        double porc = calcExecutedCost / total;

        especialidadTask.setText(1, especialidadTask.getName());
        especialidadTask.setCost(1, Math.round(costoMat * 100d) / 100d);
        especialidadTask.setCost(2, Math.round(costoMano * 100d) / 100d);
        especialidadTask.setCost(3, Math.round(costoEqip * 100d) / 100d);
        especialidadTask.setCost(4, Math.round(total * 100d) / 100d);
        especialidadTask.setCost(5, execut);
        especialidadTask.setNumber(3, Math.round(porc * 100d));
        especialidadTask.setPercentageComplete(NumberHelper.getDouble(porc));
        getResourcesEspecialidadINGeneralRV(especialidadTask, id, zonId, objetosId, nivelId, empresaconstructoraId, especialidadesId);
    }

    private List<Nivelespecifico> getDatoNivelEspecialidades(int idO, int idZ, int idOB, int idN, int idE, int idES) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Nivelespecifico> list = new ArrayList<>();
            list = session.createQuery(" FROM Nivelespecifico WHERE obraId =: id1 AND zonasId =: id2 AND objetosId =: id3 AND nivelId =: id4 AND empresaconstructoraId =: id5  AND especialidadesId =: id6 ").setParameter("id1", idO).setParameter("id2", idZ).setParameter("id3", idOB).setParameter("id4", idN).setParameter("id5", idE).setParameter("id6", idES).getResultList();

            tx.commit();
            session.close();
            return list;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    private void completeNivelsAsGeneralTask(Task nivelTask, Nivel nivel, Objetos objetos) {
        Nivel fullNivel = objetos.getNivelsById().parallelStream().filter(item -> item.getId() == nivel.getId()).findFirst().get();
        double costoMat = fullNivel.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostomaterial).reduce(0.0, Double::sum);
        double costoEqip = fullNivel.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostoequipo).reduce(0.0, Double::sum);
        double costoMano = fullNivel.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostomano).reduce(0.0, Double::sum);
        double total = costoMat + costoEqip + costoMano;
        double calcExecutedCost = getExcutedCostInNivelfulNivel(fullNivel);
        double execut = total - calcExecutedCost;
        double porc = calcExecutedCost / total;

        nivelTask.setText(1, fullNivel.getCodigo());
        nivelTask.setCost(1, Math.round(costoMat * 100d) / 100d);
        nivelTask.setCost(2, Math.round(costoMano * 100d) / 100d);
        nivelTask.setCost(3, Math.round(costoEqip * 100d) / 100d);
        nivelTask.setCost(4, Math.round(total * 100d) / 100d);
        nivelTask.setCost(5, execut);
        nivelTask.setNumber(3, Math.round(porc * 100d));
        nivelTask.setPercentageComplete(NumberHelper.getDouble(porc));
        getResourcesInNivelGeneralRV(nivelTask, fullNivel.getId());
    }

    private double getExcutedCostInNivelfulNivel(Nivel fullNivel) {
        List<Nivelespecifico> nivelespecificoList = fullNivel.getNivelespecificosById().stream().collect(Collectors.toList());
        double materialCertif = 0.0;
        double manoCertif = 0.0;
        double equipCertif = 0.0;
        for (Nivelespecifico nivelespecifico : nivelespecificoList) {
            if (nivelespecifico.getCertificacionglonvariantesById().size() > 0) {
                materialCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomaterial).reduce(0.0, Double::sum);
                manoCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomano).reduce(0.0, Double::sum);
                equipCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostoequipo).reduce(0.0, Double::sum);
            }
        }
        return materialCertif + manoCertif + equipCertif;
    }

    private double getExcutedCostInEspecialidadfulNivel(List<Nivelespecifico> fullList) {
        double materialCertif = 0.0;
        double manoCertif = 0.0;
        double equipCertif = 0.0;
        for (Nivelespecifico nivelespecifico : fullList) {
            if (nivelespecifico.getCertificacionglonvariantesById().size() > 0) {
                materialCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomaterial).reduce(0.0, Double::sum);
                manoCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomano).reduce(0.0, Double::sum);
                equipCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostoequipo).reduce(0.0, Double::sum);
            }
        }
        return materialCertif + manoCertif + equipCertif;
    }

    private void completeObjectAsGeneralTask(Task objetoTask, Objetos ob, Zonas zon) {
        Objetos fullObject = zon.getObjetosById().parallelStream().filter(item -> item.getId() == ob.getId()).findFirst().get();
        double costoMat = fullObject.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostomaterial).reduce(0.0, Double::sum);
        double costoEqip = fullObject.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostoequipo).reduce(0.0, Double::sum);
        double costoMano = fullObject.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostomano).reduce(0.0, Double::sum);
        double total = costoMat + costoEqip + costoMano;
        double calcExecutedCost = getExcutedCostInObjectfullObject(fullObject);
        double execut = total - calcExecutedCost;
        double porc = calcExecutedCost / total;

        objetoTask.setText(1, fullObject.getCodigo());
        objetoTask.setCost(1, Math.round(costoMat * 100d) / 100d);
        objetoTask.setCost(2, Math.round(costoMano * 100d) / 100d);
        objetoTask.setCost(3, Math.round(costoEqip * 100d) / 100d);
        objetoTask.setCost(4, Math.round(total * 100d) / 100d);
        objetoTask.setCost(5, execut);
        objetoTask.setNumber(3, Math.round(porc * 100d));
        objetoTask.setPercentageComplete(NumberHelper.getDouble(porc));
        getResourcesInObjectsGeneralRV(objetoTask, fullObject.getId());
    }


    private double getExcutedCostInObjectfullObject(Objetos fullObject) {
        List<Nivelespecifico> nivelespecificoList = fullObject.getNivelespecificosById().stream().collect(Collectors.toList());
        double materialCertif = 0.0;
        double manoCertif = 0.0;
        double equipCertif = 0.0;
        for (Nivelespecifico nivelespecifico : nivelespecificoList) {
            if (nivelespecifico.getCertificacionglonvariantesById().size() > 0) {
                materialCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomaterial).reduce(0.0, Double::sum);
                manoCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomano).reduce(0.0, Double::sum);
                equipCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostoequipo).reduce(0.0, Double::sum);
            }
        }
        return materialCertif + manoCertif + equipCertif;
    }

    private void completeZonaAsGeneralTask(Task zonasTask, Zonas zonas) {
        Zonas fullZona = obra.getZonasById().parallelStream().filter(item -> item.getId() == zonas.getId()).findFirst().get();
        double costoMat = fullZona.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostomaterial).reduce(0.0, Double::sum);
        double costoEqip = fullZona.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostoequipo).reduce(0.0, Double::sum);
        double costoMano = fullZona.getNivelespecificosById().parallelStream().map(Nivelespecifico::getCostomano).reduce(0.0, Double::sum);
        double total = costoMat + costoEqip + costoMano;
        double calcExecutedCost = getExcutedCostInZona(fullZona);
        double execut = total - calcExecutedCost;
        double porc = calcExecutedCost / total;

        zonasTask.setText(1, fullZona.getCodigo());
        zonasTask.setCost(1, Math.round(costoMat * 100d) / 100d);
        zonasTask.setCost(2, Math.round(costoMano * 100d) / 100d);
        zonasTask.setCost(3, Math.round(costoEqip * 100d) / 100d);
        zonasTask.setCost(4, Math.round(total * 100d) / 100d);
        zonasTask.setCost(5, execut);
        zonasTask.setNumber(3, Math.round(porc * 100d));
        zonasTask.setPercentageComplete(NumberHelper.getDouble(porc));
        getResourcesInZonasGeneralRV(zonasTask, fullZona.getId());
    }

    private double getExcutedCostInZona(Zonas fullZona) {
        List<Nivelespecifico> nivelespecificoList = fullZona.getNivelespecificosById().stream().collect(Collectors.toList());
        double materialCertif = 0.0;
        double manoCertif = 0.0;
        double equipCertif = 0.0;
        for (Nivelespecifico nivelespecifico : nivelespecificoList) {
            if (nivelespecifico.getCertificacionglonvariantesById().size() > 0) {
                materialCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomaterial).reduce(0.0, Double::sum);
                manoCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostomano).reduce(0.0, Double::sum);
                equipCertif += nivelespecifico.getCertificacionglonvariantesById().parallelStream().map(CertificacionRenglonVariante::getCostoequipo).reduce(0.0, Double::sum);
            }
        }
        return materialCertif + manoCertif + equipCertif;
    }

    public void createProjectUOSimple() {
        myFlag = false;
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Procesando petición del usuario...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        mspdiWriter = new MSPDIWriter();
        project = new ProjectFile();
        project.addCalendar().remove();

        CustomFieldContainer customFields = project.getCustomFields();

        CustomField field = customFields.getCustomField(TaskField.TEXT1);
        field.setAlias("Código");

        CustomField field1 = customFields.getCustomField(TaskField.TEXT2);
        field1.setAlias("U/M");

        CustomField fieldCost1 = customFields.getCustomField(TaskField.COST1);
        fieldCost1.setAlias("Costo Material");

        CustomField fieldCost2 = customFields.getCustomField(TaskField.COST2);
        fieldCost2.setAlias("Costo Mano de Obra");

        CustomField fieldCost3 = customFields.getCustomField(TaskField.COST3);
        fieldCost3.setAlias("Costo de Equipos");

        CustomField fieldCost4 = customFields.getCustomField(TaskField.COST4);
        fieldCost4.setAlias("Costo Total");

        CustomField fieldCost5 = customFields.getCustomField(TaskField.COST5);
        fieldCost5.setAlias("Costo Ejecutado");

        CustomField fieldNumber = customFields.getCustomField(TaskField.NUMBER1);
        fieldNumber.setAlias("Cantidad");

        CustomField fieldNumber1 = customFields.getCustomField(TaskField.NUMBER2);
        fieldNumber1.setAlias("Volumen Ejecutado");

        CustomField fieldNumber2 = customFields.getCustomField(TaskField.NUMBER3);
        fieldNumber2.setAlias("Porciento de Ejecución");

        if (comboObras.getValue() != null) {
            obra = projectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue())).findFirst().get();

        }

        allResourcesList = new ArrayList<>();
        allResourcesList = getResorcesInObra(obra.getId());
        allResourcesList.size();
        resoucesArraylist = new ArrayList<>();

        if (comboEmpresas.getValue() != null) {
            Empresaconstructora empresaconstructora = projectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEmpresas.getValue().trim())).findFirst().get();
            rootTask = project.addTask();
            rootTask.setName(obra.getDescripion().trim());

            net.sf.mpxj.Task empresaTask = rootTask.addTask();
            empresaTask.setName(empresaconstructora.getDescripcion().trim());

            if (comboZonas.getValue() != null && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    getZonasTaskResoucesArray(zonasTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), true);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();


            } else if (comboZonas.getValue() != null && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                getZonasTaskResoucesArray(zonasTask, obra.getId(), empresaconstructora.getId(), zon.getId(), true);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }

            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());
                    getObjetosTaskResoucesArray(objetoTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), true);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                getObjetosTaskResoucesArray(objetoTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), true);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {

                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());

                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        getObjetosTaskResoucesArray(objetoTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), true);
                    }

                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }

            if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                nivelArrayList = new ArrayList<>();
                nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                for (Nivel nivel : nivelArrayList) {

                    nivelTask = objetoTask.addTask();
                    nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                    nivelTask.setName(nivTaskBuider.toString().trim());
                    getNivelTaskResourceArray(nivelTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), true);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                getNivelTaskResourceArray(nivelTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), true);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {

                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());
                            getNivelTaskResourceArray(nivelTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), true);
                        }
                    }

                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }
            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();


                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                especialidadesArrayList = new ArrayList<>();
                especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                for (Especialidades especialidades : especialidadesArrayList) {

                    especialidadTask = nivelTask.addTask();
                    espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                    especialidadTask.setName(espTaskBuider.toString().trim());

                    getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());


                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());
                                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
                            }

                        }
                    }

                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());

                    nivelArrayList = new ArrayList<>();
                    nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                    for (Nivel nivel : nivelArrayList) {
                        nivelTask = objetoTask.addTask();
                        nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                        nivelTask.setName(nivTaskBuider.toString().trim());

                        especialidadesArrayList = new ArrayList<>();
                        especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                        for (Especialidades especialidades : especialidadesArrayList) {
                            especialidadTask = nivelTask.addTask();
                            espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                            especialidadTask.setName(espTaskBuider.toString().trim());
                            getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
                        }

                    }
                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                nivelArrayList = new ArrayList<>();
                nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                for (Nivel nivel : nivelArrayList) {
                    nivelTask = objetoTask.addTask();
                    nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                    nivelTask.setName(nivTaskBuider.toString().trim());

                    especialidadesArrayList = new ArrayList<>();
                    especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                    for (Especialidades especialidades : especialidadesArrayList) {
                        especialidadTask = nivelTask.addTask();
                        espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                        especialidadTask.setName(espTaskBuider.toString().trim());
                        getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
                    }

                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                especialidadesArrayList = new ArrayList<>();
                especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                for (Especialidades especialidades : especialidadesArrayList) {
                    especialidadTask = nivelTask.addTask();
                    espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                    especialidadTask.setName(espTaskBuider.toString().trim());
                    getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            }
            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                subespecialidadesArrayList = new ArrayList<>();
                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                    subespTask = especialidadTask.addTask();
                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                    subespTask.setName(subTaskBuider.toString().trim());

                    getSubEspTaskResourcesArray(subespTask, obra.getId(), empresaconstructora.getId(), zon.getId(), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), true);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && !comboSubespecialidades.getValue().equals("Todas")) {
                myFlag = true;

                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                partSub = comboSubespecialidades.getValue().split(" - ");

                Subespecialidades subespecialidades = projectStructureSingelton.subespecialidadesArrayList.stream().filter(esp -> esp.toString().trim().equals(comboSubespecialidades.getValue().trim())).findFirst().get();

                subespTask = especialidadTask.addTask();
                subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                subespTask.setName(subTaskBuider.toString().trim());

                getSubEspTaskResourcesArray(subespTask, obra.getId(), empresaconstructora.getId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), true);


                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());

                                subespecialidadesArrayList = new ArrayList<>();
                                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                                    subespTask = especialidadTask.addTask();
                                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                                    subespTask.setName(subTaskBuider.toString().trim());
                                    getSubEspTaskResourcesArray(subespTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId(), true);
                                }
                            }

                        }
                    }

                }
            }
            try {
                if (mspselect.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Seleccine la versión de Microsoft Project a exportar");
                    alert.showAndWait();
                } else if (mspselect.getValue().endsWith("2013")) {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Salvar exportación");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                        mspdiWriter.write(project, file.getAbsolutePath());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información");
                        alert.setContentText("Exportación terminada!");
                        alert.showAndWait();
                    }
                } else if (mspselect.getValue().endsWith("2016")) {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Salvar exportación");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                        mspdiWriter.write(project, file.getAbsolutePath());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información");
                        alert.setContentText("Exportación terminada!");
                        alert.showAndWait();
                    }
                }


            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText(ex.getMessage());
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar una empresa");
            alert.showAndWait();
        }

        if (myFlag == false) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" La estructura de reporte declarada no es correcta");
            alert.showAndWait();
        }

    }


    public void createProjectUOMultiple() {
        myFlag = false;
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Procesando petición del usuario...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        mspdiWriter = new MSPDIWriter();
        project = new ProjectFile();
        project.addCalendar().remove();

        CustomFieldContainer customFields = project.getCustomFields();

        CustomField field = customFields.getCustomField(TaskField.TEXT1);
        field.setAlias("Código");

        CustomField field1 = customFields.getCustomField(TaskField.TEXT2);
        field1.setAlias("U/M");

        CustomField fieldCost1 = customFields.getCustomField(TaskField.COST1);
        fieldCost1.setAlias("Costo Material");

        CustomField fieldCost2 = customFields.getCustomField(TaskField.COST2);
        fieldCost2.setAlias("Costo Mano de Obra");

        CustomField fieldCost3 = customFields.getCustomField(TaskField.COST3);
        fieldCost3.setAlias("Costo de Equipos");

        CustomField fieldCost4 = customFields.getCustomField(TaskField.COST4);
        fieldCost4.setAlias("Costo Total");

        CustomField fieldCost5 = customFields.getCustomField(TaskField.COST5);
        fieldCost5.setAlias("Costo Ejecutado");

        CustomField fieldNumber = customFields.getCustomField(TaskField.NUMBER1);
        fieldNumber.setAlias("Cantidad");

        CustomField fieldNumber1 = customFields.getCustomField(TaskField.NUMBER2);
        fieldNumber1.setAlias("Volumen Ejecutado");

        CustomField fieldNumber2 = customFields.getCustomField(TaskField.NUMBER3);
        fieldNumber2.setAlias("Porciento de Ejecución");

        if (comboObras.getValue() != null) {
            obra = projectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue())).findFirst().get();
        }

        allResourcesList = new ArrayList<>();
        allResourcesList = getResorcesInObra(obra.getId());
        allResourcesList.size();
        resoucesArraylist = new ArrayList<>();


        rootTask = project.addTask();
        rootTask.setName(obra.getDescripion().trim());

        List<Empresaobra> empresaobraList = new ArrayList<>();
        empresaobraList = getEmpresaobras(obra.getId());

        if (comboZonas.getValue() != null && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    getZonasTaskResoucesArray(zonasTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), true);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


        } else if (comboZonas.getValue() != null && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                getZonasTaskResoucesArray(zonasTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), true);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }

        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            myFlag = true;

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());
                    getObjetosTaskResoucesArray(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), true);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            myFlag = true;

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                getObjetosTaskResoucesArray(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), true);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());

                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        getObjetosTaskResoucesArray(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), true);
                    }

                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }

        if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            myFlag = true;

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                nivelArrayList = new ArrayList<>();
                nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId());
                for (Nivel nivel : nivelArrayList) {

                    nivelTask = objetoTask.addTask();
                    nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                    nivelTask.setName(nivTaskBuider.toString().trim());
                    getNivelTaskResourceArray(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), true);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            myFlag = true;

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = zonasArrayList.stream().filter(z -> z.getDesripcion().equals(partZonas[1])).findFirst().orElse(null);

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = objetosArrayList.stream().filter(obj -> obj.getDescripcion().equals(partObj[1])).findFirst().orElse(null);

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = nivelArrayList.stream().filter(niv -> niv.getDescripcion().equals(partNiv[1])).findFirst().orElse(null);

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                getNivelTaskResourceArray(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), true);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {

                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());
                            getNivelTaskResourceArray(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), true);
                        }
                    }

                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }
        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() == null) {
            myFlag = true;

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                especialidadesArrayList = new ArrayList<>();
                especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId());
                for (Especialidades especialidades : especialidadesArrayList) {

                    especialidadTask = nivelTask.addTask();
                    espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                    especialidadTask.setName(espTaskBuider.toString().trim());

                    getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
            myFlag = true;

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());


                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());
                                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), true);
                            }

                        }
                    }

                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }
        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                subespecialidadesArrayList = new ArrayList<>();
                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                    subespTask = especialidadTask.addTask();
                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                    subespTask.setName(subTaskBuider.toString().trim());

                    getSubEspTaskResourcesArray(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), true);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && !comboSubespecialidades.getValue().equals("Todas")) {
            myFlag = true;

            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                partSub = comboSubespecialidades.getValue().split(" - ");
                Subespecialidades subespecialidades = projectStructureSingelton.subespecialidadesArrayList.stream().filter(esp -> esp.getDescripcion().equals(partSub[1])).findFirst().orElse(null);

                subespTask = especialidadTask.addTask();
                subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                subespTask.setName(subTaskBuider.toString().trim());

                getSubEspTaskResourcesArray(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), true);
            }

            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());

                                subespecialidadesArrayList = new ArrayList<>();
                                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                                    subespTask = especialidadTask.addTask();
                                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                                    subespTask.setName(subTaskBuider.toString().trim());
                                    getSubEspTaskResourcesArray(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId(), true);
                                }
                            }

                        }
                    }

                }
            }
        }
        try {
            if (mspselect.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setContentText("Seleccine la versión de Microsoft Project a exportar");
                alert.showAndWait();
            } else if (mspselect.getValue().endsWith("2013")) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar exportación");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                    mspdiWriter.write(project, file.getAbsolutePath());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Exportación terminada!");
                    alert.showAndWait();
                }
            } else if (mspselect.getValue().endsWith("2016")) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar exportación");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                    mspdiWriter.write(project, file.getAbsolutePath());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Exportación terminada!");
                    alert.showAndWait();
                }
            }


        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(ex.getMessage());
        }
    }

    public void createProjectUORVSimple() {
        myFlag = false;
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Procesando petición del usuario...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        mspdiWriter = new MSPDIWriter();
        project = new ProjectFile();
        project.getBaselineCalendar().remove();

        CustomFieldContainer customFields = project.getCustomFields();

        CustomField field = customFields.getCustomField(TaskField.TEXT1);
        field.setAlias("Código");

        CustomField field1 = customFields.getCustomField(TaskField.TEXT2);
        field1.setAlias("U/M");

        CustomField fieldCost1 = customFields.getCustomField(TaskField.COST1);
        fieldCost1.setAlias("Costo Material");

        CustomField fieldCost2 = customFields.getCustomField(TaskField.COST2);
        fieldCost2.setAlias("Costo Mano de Obra");

        CustomField fieldCost3 = customFields.getCustomField(TaskField.COST3);
        fieldCost3.setAlias("Costo de Equipos");

        CustomField fieldCost4 = customFields.getCustomField(TaskField.COST4);
        fieldCost4.setAlias("Costo Total");

        CustomField fieldCost5 = customFields.getCustomField(TaskField.COST5);
        fieldCost5.setAlias("Costo Ejecutado");

        CustomField fieldNumber = customFields.getCustomField(TaskField.NUMBER1);
        fieldNumber.setAlias("Cantidad");

        CustomField fieldNumber1 = customFields.getCustomField(TaskField.NUMBER2);
        fieldNumber1.setAlias("Volumen Ejecutado");

        CustomField fieldNumber2 = customFields.getCustomField(TaskField.NUMBER3);
        fieldNumber2.setAlias("Porciento de Ejecución");

        if (comboObras.getValue() != null) {
            obra = projectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue())).findFirst().get();

        }

        allResourcesList = new ArrayList<>();
        allResourcesList = getResorcesInObra(obra.getId());
        allResourcesList.size();
        resoucesArraylist = new ArrayList<>();

        if (comboEmpresas.getValue() != null) {
            Empresaconstructora empresaconstructora = projectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEmpresas.getValue().trim())).findFirst().get();

            empresaconstructora = projectStructureSingelton.empresaconstructoraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEmpresas.getValue().trim())).findFirst().get();

            rootTask = project.addTask();
            rootTask.setName(obra.getDescripion().trim());

            net.sf.mpxj.Task empresaTask = rootTask.addTask();
            empresaTask.setName(empresaconstructora.getDescripcion().trim());

            if (comboZonas.getValue() != null && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    getZonasTaskResoucesArray(zonasTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), false);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();


            } else if (comboZonas.getValue() != null && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                getZonasTaskResoucesArray(zonasTask, obra.getId(), empresaconstructora.getId(), zon.getId(), false);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }

            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());
                    getObjetosTaskResoucesArray(objetoTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), false);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                getObjetosTaskResoucesArray(objetoTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), false);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {

                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());

                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        getObjetosTaskResoucesArray(objetoTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), false);
                    }

                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }

            if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                System.out.println("<<<Aqui entro yo...");
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                nivelArrayList = new ArrayList<>();
                nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                for (Nivel nivel : nivelArrayList) {

                    nivelTask = objetoTask.addTask();
                    nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                    nivelTask.setName(nivTaskBuider.toString().trim());
                    getNivelTaskResourceArray(nivelTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), false);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.stream().filter(niv -> niv.getDescripcion().equals(partNiv[1])).findFirst().orElse(null);

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                getNivelTaskResourceArray(nivelTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), false);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {

                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());
                            getNivelTaskResourceArray(nivelTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), false);
                        }
                    }

                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            }
            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                especialidadesArrayList = new ArrayList<>();
                especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                for (Especialidades especialidades : especialidadesArrayList) {

                    especialidadTask = nivelTask.addTask();
                    espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                    especialidadTask.setName(espTaskBuider.toString().trim());

                    getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), false);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());


                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), false);

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                System.out.println("imprimeindo datos!!!!!!");
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());
                                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), false);
                            }

                        }
                    }

                }
                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (!comboZonas.getValue().contentEquals("Todas") && comboZonas.getValue() != null && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());

                    nivelArrayList = new ArrayList<>();
                    nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId());
                    for (Nivel nivel : nivelArrayList) {
                        nivelTask = objetoTask.addTask();
                        nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                        nivelTask.setName(nivTaskBuider.toString().trim());

                        especialidadesArrayList = new ArrayList<>();
                        especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId());
                        for (Especialidades especialidades : especialidadesArrayList) {
                            especialidadTask = nivelTask.addTask();
                            espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                            especialidadTask.setName(espTaskBuider.toString().trim());
                            getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), false);
                        }

                    }
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            }
            if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
                myFlag = true;
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                subespecialidadesArrayList = new ArrayList<>();
                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                    subespTask = especialidadTask.addTask();
                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                    subespTask.setName(subTaskBuider.toString().trim());

                    getSubEspTaskResourcesArray(subespTask, obra.getId(), empresaconstructora.getId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), false);
                }

                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();

            } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && !comboSubespecialidades.getValue().equals("Todas")) {
                myFlag = true;

                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                partSub = comboSubespecialidades.getValue().split(" - ");
                Subespecialidades subespecialidades = projectStructureSingelton.subespecialidadesArrayList.stream().filter(esp -> esp.getDescripcion().equals(partSub[1])).findFirst().orElse(null);

                subespTask = especialidadTask.addTask();
                subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                subespTask.setName(subTaskBuider.toString().trim());

                getSubEspTaskResourcesArray(subespTask, obra.getId(), empresaconstructora.getId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), false);


                tarea = createTime(20);
                stage = new ProgressDialog(tarea);
                stage.setContentText("Preparando fichero xml...");
                stage.setTitle("Espere...");
                new Thread(tarea).start();
                stage.showAndWait();
            } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), empresaconstructora.getId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), empresaconstructora.getId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());

                                subespecialidadesArrayList = new ArrayList<>();
                                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                                    subespTask = especialidadTask.addTask();
                                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                                    subespTask.setName(subTaskBuider.toString().trim());
                                    getSubEspTaskResourcesArray(subespTask, obra.getId(), empresaconstructora.getId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId(), false);
                                }
                            }

                        }
                    }

                }
            }

            try {
                if (mspselect.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Seleccine la versión de Microsoft Project a exportar");
                    alert.showAndWait();
                } else if (mspselect.getValue().endsWith("2013")) {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Salvar exportación");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                        mspdiWriter.write(project, file.getAbsolutePath());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información");
                        alert.setContentText("Exportación terminada!");
                        alert.showAndWait();
                    }
                } else if (mspselect.getValue().endsWith("2016")) {

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Salvar exportación");
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);

                    //Show save file dialog
                    File file = fileChooser.showSaveDialog(null);
                    if (file != null) {
                        mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                        mspdiWriter.write(project, file.getAbsolutePath());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Información");
                        alert.setContentText("Exportación terminada!");
                        alert.showAndWait();
                    }
                }


            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setContentText(ex.getMessage());
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar una empresa");
            alert.showAndWait();
        }

    }

    public void createProjectUORVMultiple() {
        myFlag = false;
        tarea = createTimeMesage();
        ProgressDialog dialog = new ProgressDialog(tarea);
        dialog.setContentText("Procesando petición del usuario...");
        dialog.setTitle("Espere...");
        new Thread(tarea).start();
        dialog.showAndWait();

        mspdiWriter = new MSPDIWriter();
        project = new ProjectFile();
        project.addCalendar().remove();

        CustomFieldContainer customFields = project.getCustomFields();

        CustomField field = customFields.getCustomField(TaskField.TEXT1);
        field.setAlias("Código");

        CustomField field1 = customFields.getCustomField(TaskField.TEXT2);
        field1.setAlias("U/M");

        CustomField fieldCost1 = customFields.getCustomField(TaskField.COST1);
        fieldCost1.setAlias("Costo Material");

        CustomField fieldCost2 = customFields.getCustomField(TaskField.COST2);
        fieldCost2.setAlias("Costo Mano de Obra");

        CustomField fieldCost3 = customFields.getCustomField(TaskField.COST3);
        fieldCost3.setAlias("Costo de Equipos");

        CustomField fieldCost4 = customFields.getCustomField(TaskField.COST4);
        fieldCost4.setAlias("Costo Total");

        CustomField fieldCost5 = customFields.getCustomField(TaskField.COST5);
        fieldCost5.setAlias("Costo Ejecutado");

        CustomField fieldNumber = customFields.getCustomField(TaskField.NUMBER1);
        fieldNumber.setAlias("Cantidad");

        CustomField fieldNumber1 = customFields.getCustomField(TaskField.NUMBER2);
        fieldNumber1.setAlias("Volumen Ejecutado");

        CustomField fieldNumber2 = customFields.getCustomField(TaskField.NUMBER3);
        fieldNumber2.setAlias("Porciento de Ejecución");

        if (comboObras.getValue() != null) {
            obra = projectStructureSingelton.obraArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboObras.getValue())).findFirst().get();

        }

        allResourcesList = new ArrayList<>();
        allResourcesList = getResorcesInObra(obra.getId());
        allResourcesList.size();
        resoucesArraylist = new ArrayList<>();

        List<Empresaobra> empresaobraList = new ArrayList<>();
        empresaobraList = getEmpresaobras(obra.getId());
        rootTask = project.addTask();
        rootTask.setName(obra.getDescripion().trim());

        if (comboZonas.getValue() != null && comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    getZonasTaskResoucesArray(zonasTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), false);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();


        } else if (comboZonas.getValue() != null && !comboZonas.getValue().equals("Todas") && comboObjetos.getValue() == null) {
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();
                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                getZonasTaskResoucesArray(zonasTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), false);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }

        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());
                objetosArrayList = new ArrayList<>();
                objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId());

                for (Objetos objetos : objetosArrayList) {
                    objetoTask = zonasTask.addTask();
                    objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                    objetoTask.setName(objTaskBuider.toString().trim());
                    getObjetosTaskResoucesArray(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), false);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                getObjetosTaskResoucesArray(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), true);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());

                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());
                        getObjetosTaskResoucesArray(objetoTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), false);
                    }

                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }

        if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());
                nivelArrayList = new ArrayList<>();
                nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId());
                for (Nivel nivel : nivelArrayList) {

                    nivelTask = objetoTask.addTask();
                    nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                    nivelTask.setName(nivTaskBuider.toString().trim());
                    getNivelTaskResourceArray(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), false);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = zonasArrayList.stream().filter(z -> z.getDesripcion().equals(partZonas[1])).findFirst().orElse(null);

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = objetosArrayList.stream().filter(obj -> obj.getDescripcion().equals(partObj[1])).findFirst().orElse(null);

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = nivelArrayList.stream().filter(niv -> niv.getDescripcion().equals(partNiv[1])).findFirst().orElse(null);

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                getNivelTaskResourceArray(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), false);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {

                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());
                            getNivelTaskResourceArray(nivelTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), false);
                        }
                    }
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }
        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                especialidadesArrayList = new ArrayList<>();
                especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId());
                for (Especialidades especialidades : especialidadesArrayList) {

                    especialidadTask = nivelTask.addTask();
                    espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                    especialidadTask.setName(espTaskBuider.toString().trim());

                    getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), false);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());


                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), false);
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().contentEquals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().contentEquals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().contentEquals("Todas") && comboSubespecialidades.getValue() == null) {
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());
                                getEspecialidadTaskResourcesArray(especialidadTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), false);
                            }

                        }
                    }

                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        }
        if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().equals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                subespecialidadesArrayList = new ArrayList<>();
                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zon.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                    subespTask = especialidadTask.addTask();
                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                    subespTask.setName(subTaskBuider.toString().trim());

                    getSubEspTaskResourcesArray(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), false);
                }
            }
            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();

        } else if (!comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && !comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && !comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && !comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && !comboSubespecialidades.getValue().equals("Todas")) {
            myFlag = true;
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                partZonas = comboZonas.getValue().split(" - ");
                Zonas zon = projectStructureSingelton.zonasArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboZonas.getValue().trim())).findFirst().get();

                zonasTask = empresaTask.addTask();
                zonaTaskBuider = new StringBuilder().append(zon.getCodigo()).append(" ").append(zon.getDesripcion());
                zonasTask.setName(zonaTaskBuider.toString().trim());

                partObj = comboObjetos.getValue().split(" - ");
                Objetos objetos = projectStructureSingelton.objetosArrayList.stream().filter(obj -> obj.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().get();

                objetoTask = zonasTask.addTask();
                objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                objetoTask.setName(objTaskBuider.toString().trim());

                partNiv = comboNivel.getValue().split(" - ");
                Nivel nivel = projectStructureSingelton.nivelArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboNivel.getValue().trim())).findFirst().get();

                nivelTask = objetoTask.addTask();
                nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                nivelTask.setName(nivTaskBuider.toString().trim());

                partEsp = comboEspecialidades.getValue().split(" - ");
                Especialidades especialidades = projectStructureSingelton.especialidadesArrayList.parallelStream().filter(item -> item.toString().trim().equals(comboEspecialidades.getValue().trim())).findFirst().get();

                especialidadTask = nivelTask.addTask();
                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                especialidadTask.setName(espTaskBuider.toString().trim());

                partSub = comboSubespecialidades.getValue().split(" - ");
                Subespecialidades subespecialidades = projectStructureSingelton.subespecialidadesArrayList.stream().filter(esp -> esp.getDescripcion().equals(partSub[1])).findFirst().orElse(null);

                subespTask = especialidadTask.addTask();
                subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                subespTask.setName(subTaskBuider.toString().trim());

                getSubEspTaskResourcesArray(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), Integer.parseInt(partZonas[0]), Integer.parseInt(partObj[0]), Integer.parseInt(partNiv[0]), Integer.parseInt(partEsp[0]), subespecialidades.getId(), false);
            }

            tarea = createTime(20);
            stage = new ProgressDialog(tarea);
            stage.setContentText("Preparando fichero xml...");
            stage.setTitle("Espere...");
            new Thread(tarea).start();
            stage.showAndWait();
        } else if (comboZonas.getValue().equals("Todas") && comboObjetos.getValue() != null && comboObjetos.getValue().equals("Todos") && comboNivel.getValue() != null && comboNivel.getValue().contentEquals("Todos") && comboEspecialidades.getValue() != null && comboEspecialidades.getValue().equals("Todas") && comboSubespecialidades.getValue() != null && comboSubespecialidades.getValue().equals("Todas")) {
            for (Empresaobra partEmp : empresaobraList) {
                net.sf.mpxj.Task empresaTask = rootTask.addTask();
                empresaTask.setName(partEmp.getEmpresaconstructoraByEmpresaconstructoraId().getDescripcion());
                zonasArrayList = new ArrayList<>();
                zonasArrayList = getZonasToTasks(obra.getId(), partEmp.getEmpresaconstructoraId());
                for (Zonas zonas : zonasArrayList) {
                    zonaTaskBuider = new StringBuilder().append(zonas.getCodigo()).append(" ").append(zonas.getDesripcion());
                    zonasTask = empresaTask.addTask();
                    zonasTask.setName(zonaTaskBuider.toString().trim());
                    objetosArrayList = new ArrayList<>();
                    objetosArrayList = getObjetosToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId());

                    for (Objetos objetos : objetosArrayList) {
                        objetoTask = zonasTask.addTask();
                        objTaskBuider = new StringBuilder().append(objetos.getCodigo()).append(" ").append(objetos.getDescripcion());
                        objetoTask.setName(objTaskBuider.toString().trim());

                        nivelArrayList = new ArrayList<>();
                        nivelArrayList = getNivelToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId());
                        for (Nivel nivel : nivelArrayList) {
                            nivelTask = objetoTask.addTask();
                            nivTaskBuider = new StringBuilder().append(nivel.getCodigo()).append(" ").append(nivel.getDescripcion());
                            nivelTask.setName(nivTaskBuider.toString().trim());

                            especialidadesArrayList = new ArrayList<>();
                            especialidadesArrayList = getEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId());
                            for (Especialidades especialidades : especialidadesArrayList) {
                                especialidadTask = nivelTask.addTask();
                                espTaskBuider = new StringBuilder().append(especialidades.getCodigo()).append(" ").append(especialidades.getDescripcion());
                                especialidadTask.setName(espTaskBuider.toString().trim());

                                subespecialidadesArrayList = new ArrayList<>();
                                subespecialidadesArrayList = getSubEspecialidadToTask(obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId());
                                for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                                    subespTask = especialidadTask.addTask();
                                    subTaskBuider = new StringBuilder().append(subespecialidades.getCodigo()).append(" ").append(subespecialidades.getDescripcion());
                                    subespTask.setName(subTaskBuider.toString().trim());
                                    getSubEspTaskResourcesArray(subespTask, obra.getId(), partEmp.getEmpresaconstructoraId(), zonas.getId(), objetos.getId(), nivel.getId(), especialidades.getId(), subespecialidades.getId(), false);
                                }
                            }

                        }
                    }
                }
            }
        }


        try {
            if (mspselect.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setContentText("Seleccine la versión de Microsoft Project a exportar");
                alert.showAndWait();
            } else if (mspselect.getValue().endsWith("2013")) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar exportación");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                    mspdiWriter.write(project, file.getAbsolutePath());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Exportación terminada!");
                    alert.showAndWait();
                }
            } else if (mspselect.getValue().endsWith("2016")) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar exportación");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Proyecto (*.xml)", "*.xml");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    mspdiWriter.setSaveVersion(SaveVersion.Project2010);
                    mspdiWriter.write(project, file.getAbsolutePath());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setContentText("Exportación terminada!");
                    alert.showAndWait();
                }
            }


        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(ex.getMessage());
        }
    }


    public void handleCloseWindows(ActionEvent event) {

        Stage stage = (Stage) btn_close.getScene().getWindow();
        stage.close();
    }
}










