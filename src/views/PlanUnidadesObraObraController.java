package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class PlanUnidadesObraObraController implements Initializable {

    private static SessionFactory sf;
    @FXML
    private Label label_title;
    @FXML
    private JFXComboBox<String> coboEmpresas;
    @FXML
    private JFXComboBox<String> comboZonas;
    @FXML
    private JFXComboBox<String> comboObjetos;
    @FXML
    private JFXComboBox<String> comboNivel;
    @FXML
    private JFXComboBox<String> comboEspecialidad;
    @FXML
    private JFXComboBox<String> comboSubEspecialidad;
    @FXML
    private TextArea textDescrp;
    @FXML
    private JFXButton addZonas;
    @FXML
    private JFXButton addObjetos;
    @FXML
    private JFXButton addNivel;
    @FXML
    private JFXButton addEspecialidad;
    @FXML
    private JFXButton addSubespecialidad;
    @FXML
    private JFXButton btnHide;
    private double disponi;
    private double cantPlan;
    private Planificacion planificacion;
    @FXML
    private TableView<SubespecialidadView> dataTable;
    @FXML
    private TableColumn<SubespecialidadView, StringProperty> code;
    @FXML
    private TableColumn<SubespecialidadView, StringProperty> descripcion;
    @FXML
    private Pane paneCalc;
    @FXML
    private TextArea labelUodescrip;
    @FXML
    private Label labelCode;
    @FXML
    private Label labelUM;
    @FXML
    private Label labelCant;
    @FXML
    private Label labelcostU;
    @FXML
    private Label labelCosT;
    @FXML
    private Label labelSal;
    @FXML
    private TableView<PlanView> tablePlanes;
    @FXML
    private TableColumn<PlanView, StringProperty> codeUO;
    @FXML
    private TableColumn<PlanView, StringProperty> descripcionUO;
    @FXML
    private TableColumn<PlanView, StringProperty> um;
    @FXML
    private TableColumn<PlanView, DoubleProperty> cantidad;
    @FXML
    private TableColumn<PlanView, DoubleProperty> costMat;
    @FXML
    private TableColumn<PlanView, DoubleProperty> costMan;
    @FXML
    private TableColumn<PlanView, DoubleProperty> costEquip;
    @FXML
    private TableColumn<PlanView, DoubleProperty> disponible;
    private ArrayList<Planificacion> planificacionArrayList;
    private ObservableList<PlanView> planViewObservableList;
    private PlanView planView;
    @FXML
    private JFXComboBox<String> comboRenglones;
    @FXML
    private Label labelTipoCost;
    @FXML
    private Label labelRVCant;
    @FXML
    private Label labelPeso;
    @FXML
    private Label labelRVum;
    private ArrayList<Empresaobra> empresaobraArrayList;
    private ArrayList<Empresaconstructora> empresaArrayList;
    private ObservableList<String> empresasList;
    private Empresaconstructora empresaconstructoraModel;
    private ArrayList<Zonas> zonasArrayList;
    private ObservableList<String> zonaList;
    private Zonas zonasModel;
    private ArrayList<Objetos> objetosArrayList;
    private ObservableList<String> objetosList;
    private Objetos objetosModel;
    private ArrayList<Nivel> nivelArrayList;
    private ObservableList<String> nivelesList;
    private Nivel nivelModel;
    private ArrayList<Especialidades> especialidadesArrayList;
    private ObservableList<String> especialidadesList;
    private Especialidades especialidadesModel;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private ObservableList<String> subEspecialiadesList;
    private Subespecialidades subespecialidadesModel;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ObservableList<UniddaObraView> unidadObraViewObservableList;
    private UniddaObraView unidadObraView;
    private int IdObra;
    private int IdRV;
    private ObservableList<UniddaObraView> datos;

    private boolean falg;
    private boolean falg1;

    private ArrayList<Unidadobrarenglon> unidadobrarenglonArrayList;
    private double costosRV;
    private double CostoTotal;
    private double sumCostoTotal;

    private double costoMaterial;
    private double costo;


    private ObservableList<UORVTableView> uorvTableViewsList;
    private UORVTableView uorvTableView;
    private ObservableList<UORVTableView> datosRV;

    private String codeRB;

    private ObservableList<String> codesList;

    private Unidadobra unidadobra;

    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ObservableList<ManoObraRenglonVariante> manoobraList;
    private double importe;
    private double cantUO;
    private double valGrupo;
    private double sal;

    private ArrayList<Bajoespecificacion> bajoespecificacionArrayList;
    private ObservableList<BajoEspecificacionView> bajoEspecificacionViewObservableList;
    private BajoEspecificacionView bajoEspecificacionView;
    private ObservableList<BajoEspecificacionView> datosBE;

    private Unidadobrarenglon unidadobrarenglon;


    private ObservableList<SubespecialidadView> subespecialidadViewObservableList;
    private SubespecialidadView subespecialidadView;
    private Double cantcert;

    @FXML
    private JFXButton recalc;


    /**
     * Funcionalidades para llenar los elementos de la vista
     */

    public ObservableList<String> getListEmpresas(int Id) {


        empresasList = FXCollections.observableArrayList();
        empresaArrayList = new ArrayList<Empresaconstructora>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Empresaobra where obraId = :id ");
            query.setParameter("id", Id);
            empresaobraArrayList = (ArrayList<Empresaobra>) ((org.hibernate.query.Query) query).list();
            empresaobraArrayList.forEach(empresaobra -> {
                Empresaconstructora empresaconstructora = session.find(Empresaconstructora.class, empresaobra.getEmpresaconstructoraId());
                empresaArrayList.add(empresaconstructora);
                empresasList.add(empresaconstructora.getCodigo() + " - " + empresaconstructora.getDescripcion());
            });
            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return empresasList;
    }

    public ObservableList<String> getListZonas(int Id) {


        zonaList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Zonas where obraId = :id ");
            query.setParameter("id", Id);
            zonasArrayList = (ArrayList<Zonas>) ((org.hibernate.query.Query) query).list();
            zonasArrayList.forEach(zonas -> {
                zonaList.add(zonas.getCodigo() + " - " + zonas.getDesripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return zonaList;
    }

    public Double getUoCostoTotal(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            costosRV = 0.0;
            CostoTotal = 0.0;
            sumCostoTotal = 0.0;
            Query query = session.createQuery("FROM Unidadobrarenglon where unidadobraId = :id ");
            query.setParameter("id", idUO);
            unidadobrarenglonArrayList = (ArrayList<Unidadobrarenglon>) ((org.hibernate.query.Query) query).list();
            unidadobrarenglonArrayList.forEach(unidadobrarenglon -> {
                costosRV = unidadobrarenglon.getCostMat() + unidadobrarenglon.getCostMano() + unidadobrarenglon.getCostEquip();
                CostoTotal = unidadobrarenglon.getCantRv() * costosRV;
                sumCostoTotal += CostoTotal;
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return sumCostoTotal;
    }

    /**
     * para el calculo del salario de los rv
     */

    private double getImporteEscala(String grupo) {


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
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

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return importe;
    }

    private ObservableList<ManoObraRenglonVariante> getManoObraRenglon(int idRv) {

        manoobraList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId = :id");
            query.setParameter("id", idRv);
            renglonrecursosArrayList = (ArrayList<Renglonrecursos>) ((org.hibernate.query.Query) query).list();
            renglonrecursosArrayList.forEach(renglonrecursos -> {
                Recursos recursos = session.get(Recursos.class, renglonrecursos.getRecursosId());
                if (recursos.getTipo().contentEquals("2")) {

                    manoobraList.add(new ManoObraRenglonVariante(recursos.getCodigo(), recursos.getGrupoescala(), renglonrecursos.getCantidas(), recursos.getDescripcion()));
                }
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return manoobraList;

    }

    public double calcSalRV(ObservableList<ManoObraRenglonVariante> manoObra) {
        manoObra.forEach(mano -> {
            valGrupo = getImporteEscala(mano.getEscala());
            importe = mano.getNorma() * valGrupo;
            sal = Math.round(importe * 100d) / 100d;
        });

        return sal;
    }


    private ObservableList<String> getListObjetos(int idZona) {

        objetosList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Objetos where zonasId = :id ");
            query.setParameter("id", idZona);
            objetosArrayList = (ArrayList<Objetos>) ((org.hibernate.query.Query) query).list();
            objetosArrayList.forEach(objetos -> {
                objetosList.add(objetos.getCodigo() + " - " + objetos.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return objetosList;

    }

    private ObservableList<String> getNivelList(int id) {
        nivelesList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Nivel where objetosId = :id ");
            query.setParameter("id", id);
            nivelArrayList = (ArrayList<Nivel>) ((org.hibernate.query.Query) query).list();
            nivelArrayList.forEach(nivel -> {
                nivelesList.add(nivel.getCodigo() + " - " + nivel.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return nivelesList;
    }

    private ObservableList<String> getEspecialidades() {
        especialidadesList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("from Especialidades ").list();
            especialidadesArrayList.forEach(especialiades -> {
                especialidadesList.add(especialiades.getCodigo() + " - " + especialiades.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return especialidadesList;
    }

    private ObservableList<SubespecialidadView> getListSubEspecialidad(int id) {

        subespecialidadViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Subespecialidades where especialidadesId = :id ");
            query.setParameter("id", id);
            subespecialidadesArrayList = (ArrayList<Subespecialidades>) ((org.hibernate.query.Query) query).list();
            subespecialidadesArrayList.forEach(subespecialidades -> {
                System.out.println(subespecialidades.getCodigo());
                subespecialidadView = new SubespecialidadView(subespecialidades.getId(), subespecialidades.getCodigo(), subespecialidades.getDescripcion());
                subespecialidadViewObservableList.add(subespecialidadView);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return subespecialidadViewObservableList;
    }


    public ArrayList<Planificacion> getPlanificaciones() {
        planificacionArrayList = new ArrayList<Planificacion>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planificacionArrayList = (ArrayList<Planificacion>) session.createQuery("FROM Planificacion ").list();


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return planificacionArrayList;
    }

    /**
     * Para determinar la cantidad certificad
     */

    public Double getCantCertificada(int idUnidadObra) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cantPlan = 0.0;


            Query query = session.createQuery("SELECT SUM(cantidad) FROM Certificacion WHERE unidadobraId =: id");
            query.setParameter("id", idUnidadObra);


            if (query.getSingleResult() != null) {
                cantPlan = (Double) query.getSingleResult();
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantPlan;
    }

    private Double getCantcert(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        planViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            cantcert = 0.0;
            Query query = session.createQuery("SELECT SUM(cr.cantidad) FROM Unidadobra uo INNER JOIN Certificacion cr ON uo.id = cr.unidadobraId WHERE uo.obraId = :idObra and uo.empresaconstructoraId =:idEmp AND uo.zonasId = :idZona and uo.objetosId =: idObjeto and uo.nivelId =: idNivel and uo.especialidadesId =: idEspecilidad and uo.subespecialidadesId =:idSub");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);

            if (query.getResultList().isEmpty()) {
                cantcert = 0.0;

            } else {
                cantcert = (Double) query.getResultList().get(0);
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return cantcert;
    }


    private ObservableList<PlanView> AllPlanificaciones(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        planViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("SELECT uo.id, uo.codigo, uo.cantidad,  uo.um,  uo.descripcion, SUM(pl.cantidad), SUM(pl.costomaterial), SUM(pl.costomano), SUM(pl.costoequipo), SUM(pl.costosalario), SUM(pl.costsalariocuc) FROM Unidadobra uo INNER JOIN Planificacion pl ON uo.id = pl.unidadobraId WHERE uo.obraId = :idObra and uo.empresaconstructoraId =:idEmp AND uo.zonasId = :idZona and uo.objetosId =: idObjeto and uo.nivelId =: idNivel and uo.especialidadesId =: idEspecilidad and uo.subespecialidadesId =:idSub GROUP BY  uo.id, uo.codigo, uo.cantidad,  uo.um,  uo.descripcion");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);


            cantPlan = 0.0;
            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                Integer iduo = Integer.parseInt(row[0].toString());
                String code = row[1].toString();
                double cantUO = Double.parseDouble(row[2].toString());
                String umUO = row[3].toString();
                String descrip = row[4].toString();
                double planificado = Double.parseDouble(row[5].toString());
                double costMaterial = Double.parseDouble(row[6].toString());
                double costMano = Double.parseDouble(row[7].toString());
                double costEquipo = Double.parseDouble(row[8].toString());
                double sal = Double.parseDouble(row[9].toString());
                double salcuc = Double.parseDouble(row[10].toString());
                double cant = getCantCertificada(iduo);
                // double valCant = getCantCertificada(iduo);

                cantPlan = cantUO - cant;

                planView = new PlanView(1, code, descrip, umUO, planificado, costMaterial, costMano, costEquipo, sal, salcuc, "", "", cantPlan);
                planViewObservableList.add(planView);

            }


            /*
            unidadobraArrayList = (ArrayList<Unidadobra>) ((org.hibernate.query.Query) query).list();
            unidadobraArrayList.forEach(unidadobra -> {
                unidadObraView = new UniddaObraView(unidadobra.getId(), unidadobra.getCodigo(), unidadobra.getDescripcion());
                unidadObraViewObservableList.add(unidadObraView);
            });
            */

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return planViewObservableList;
    }


    public void handleLenaObjetosList(ActionEvent event) {
        String codeZona = comboZonas.getValue().substring(0, 3);

        zonasArrayList.forEach(zonas -> {
            if (zonas.getCodigo().contentEquals(codeZona)) {
                comboObjetos.setItems(getListObjetos(zonas.getId()));
            }
        });
    }


    public void handleLlenarNivelList(ActionEvent event) {
        String codeObjeto = comboObjetos.getValue().substring(0, 2);

        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(codeObjeto)) {
                comboNivel.setItems(getNivelList(objetos.getId()));
            }
        });
    }

    public void datatableData() {
        code.setCellValueFactory(new PropertyValueFactory<SubespecialidadView, StringProperty>("codigo"));
        descripcion.setCellValueFactory(new PropertyValueFactory<SubespecialidadView, StringProperty>("descripcion"));
    }

    public void handlrLlenarSubsepecialidad(ActionEvent event) {
        String codeEsp = comboEspecialidad.getValue().substring(0, 2);

        especialidadesArrayList.forEach(especialidades -> {
            if (especialidades.getCodigo().contentEquals(codeEsp)) {
                subespecialidadViewObservableList = FXCollections.observableArrayList();
                subespecialidadViewObservableList = getListSubEspecialidad(especialidades.getId());
                dataTable.setVisible(true);
                datatableData();
                dataTable.getItems().setAll(subespecialidadViewObservableList);
                especialidadesModel = especialidades;
            }
        });
    }


    public void handleCrearZonas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Zonas.fxml"));
            Parent proot = loader.load();
            ZonaController controller = loader.getController();
            // controller.pasarZona(IdObra);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboZonas.setItems(getListZonas(IdObra));
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleAgregarObjetos(ActionEvent event) {
        String codeZona = comboZonas.getValue().substring(0, 3);
        zonasArrayList.forEach(zonas -> {
            if (zonas.getCodigo().contentEquals(codeZona)) {
                zonasModel = zonas;
            }
        });
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Objetos.fxml"));
            Parent proot = loader.load();
            ObjetosController controller = loader.getController();
            //  controller.pasarZona(zonasModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboObjetos.setItems(getListObjetos(zonasModel.getId()));
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleAgregarNivel(ActionEvent event) {
        String codeObjeto = comboObjetos.getValue().substring(0, 2);
        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(codeObjeto)) {
                objetosModel = objetos;
            }
        });
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Niveles.fxml"));
            Parent proot = loader.load();
            NivelesController controller = loader.getController();
            //   controller.pasarObjeto(objetosModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboNivel.setItems(getNivelList(objetosModel.getId()));
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadPlanificaciones() {

        codeUO.setCellValueFactory(new PropertyValueFactory<PlanView, StringProperty>("codeUo"));
        descripcionUO.setCellValueFactory(new PropertyValueFactory<PlanView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<PlanView, StringProperty>("um"));
        cantidad.setCellValueFactory(new PropertyValueFactory<PlanView, DoubleProperty>("cantidad"));
        costMat.setCellValueFactory(new PropertyValueFactory<PlanView, DoubleProperty>("costmaterial"));
        costMan.setCellValueFactory(new PropertyValueFactory<PlanView, DoubleProperty>("costmano"));
        costEquip.setCellValueFactory(new PropertyValueFactory<PlanView, DoubleProperty>("costequipo"));
        disponible.setCellValueFactory(new PropertyValueFactory<PlanView, DoubleProperty>("disponible"));


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboEspecialidad.setItems(getEspecialidades());

        dataTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && dataTable.getSelectionModel() != null) {
                subespecialidadView = dataTable.getSelectionModel().getSelectedItem();
                paneCalc.setVisible(true);

                String[] codeEmp = coboEmpresas.getValue().split(" - ");
                String codeObjeto = comboObjetos.getValue().substring(0, 2);
                String codeNivel = comboNivel.getValue().substring(0, 2);
                String codeZona = comboZonas.getValue().substring(0, 3);

                String codeEspecialidad = comboEspecialidad.getValue().substring(0, 2);
                empresaArrayList.forEach(empresaconstructora -> {
                    if (empresaconstructora.getCodigo().contentEquals(codeEmp[0])) {
                        empresaconstructoraModel = empresaconstructora;

                    }
                });
                objetosArrayList.forEach(objetos -> {
                    if (objetos.getCodigo().contentEquals(codeObjeto)) {
                        objetosModel = objetos;
                    }
                });

                nivelArrayList.forEach(nivel -> {
                    if (nivel.getCodigo().contentEquals(codeNivel)) {
                        nivelModel = nivel;
                    }
                });

                zonasArrayList.forEach(zonas -> {
                    if (zonas.getCodigo().contentEquals(codeZona)) {
                        zonasModel = zonas;
                    }
                });

                especialidadesArrayList.forEach(especialidades -> {
                    if (especialidades.getCodigo().contentEquals(codeEspecialidad)) {
                        especialidadesModel = especialidades;
                    }
                });
                loadPlanificaciones();
                //unidadObraViewObservableList = FXCollections.observableArrayList();
                //unidadObraViewObservableList = getUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());

                planViewObservableList = FXCollections.observableArrayList();
                planViewObservableList = AllPlanificaciones(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());
                tablePlanes.getItems().setAll(planViewObservableList);


            }
        });


        btnHide.setOnAction(event -> {

            paneCalc.setVisible(false);

        });

    }


    public void definirObra(ObrasView obra) {
        IdObra = obra.getId();
        System.out.println(IdObra);
        label_title.setText("Plan del mes obra " + obra.getCodigo() + " - " + obra.getDescripion());

        coboEmpresas.setItems(getListEmpresas(IdObra));
        comboZonas.setItems(getListZonas(IdObra));

    }

    public void handleNewPlan(ActionEvent event) {

        planView = tablePlanes.getSelectionModel().getSelectedItem();

        String[] codeEmp = coboEmpresas.getValue().split(" - ");
        // String codeSub = comboSubEspecialidad.getValue().substring(0, 3);
        String codeObjeto = comboObjetos.getValue().substring(0, 2);
        String codeNivel = comboNivel.getValue().substring(0, 2);
        String codeZona = comboZonas.getValue().substring(0, 3);
        String codeEspecialidad = comboEspecialidad.getValue().substring(0, 2);

        empresaArrayList.forEach(empresaconstructora -> {
            if (empresaconstructora.getCodigo().contentEquals(codeEmp[0])) {
                empresaconstructoraModel = empresaconstructora;

            }
        });

        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(codeObjeto)) {
                objetosModel = objetos;
            }
        });

        nivelArrayList.forEach(nivel -> {
            if (nivel.getCodigo().contentEquals(codeNivel)) {
                nivelModel = nivel;
            }
        });

        zonasArrayList.forEach(zonas -> {
            if (zonas.getCodigo().contentEquals(codeZona)) {
                zonasModel = zonas;
            }
        });

        especialidadesArrayList.forEach(especialidades -> {
            if (especialidades.getCodigo().contentEquals(codeEspecialidad)) {
                especialidadesModel = especialidades;
            }
        });


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevoPlan.fxml"));
            Parent proot = loader.load();
            NuevoPlanController controller = loader.getController();
            controller.cargarUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                tablePlanes.getItems().clear();
                loadPlanificaciones();

                planViewObservableList = FXCollections.observableArrayList();
                planViewObservableList = AllPlanificaciones(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());
                tablePlanes.getItems().setAll(planViewObservableList);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleAcatualizarPlan(ActionEvent event) {
        planView = tablePlanes.getSelectionModel().getSelectedItem();

        /*
        planificacionArrayList.forEach(certif -> {
            if (certif.getId() == planView.getId()) {
                planificacion = certif;
            }
        });

        String codeEmp = coboEmpresas.getValue().substring(0, 4);
        String codeObjeto = comboObjetos.getValue().substring(0, 2);
        String codeNivel = comboNivel.getValue().substring(0, 2);
        String codeZona = comboZonas.getValue().substring(0, 3);
        String codeEspecialidad = comboEspecialidad.getValue().substring(0, 2);

        empresaArrayList.forEach(empresaconstructora -> {
            if (empresaconstructora.getCodigo().contentEquals(codeEmp)) {
                empresaconstructoraModel = empresaconstructora;

            }
        });

        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(codeObjeto)) {
                objetosModel = objetos;
            }
        });

        nivelArrayList.forEach(nivel -> {
            if (nivel.getCodigo().contentEquals(codeNivel)) {
                nivelModel = nivel;
            }
        });

        zonasArrayList.forEach(zonas -> {
            if (zonas.getCodigo().contentEquals(codeZona)) {
                zonasModel = zonas;
            }
        });

        especialidadesArrayList.forEach(especialidades -> {
            if (especialidades.getCodigo().contentEquals(codeEspecialidad)) {
                especialidadesModel = especialidades;
            }
        });
*/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetallePlanificarUnidaddeObra.fxml"));
            Parent proot = loader.load();
            DetallePlanificacionUnidadObra controller = loader.getController();
            controller.passData(planView.getCodeUo());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                tablePlanes.getItems().clear();
                loadPlanificaciones();
                //unidadObraViewObservableList = FXCollections.observableArrayList();
                //unidadObraViewObservableList = getUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());

                planViewObservableList = FXCollections.observableArrayList();
                planViewObservableList = AllPlanificaciones(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());
                tablePlanes.getItems().setAll(planViewObservableList);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualizarPlan.fxml"));
            Parent proot = (Parent) loader.load();
            ActualizarPlanController controller = (ActualizarPlanController) loader.getController();
            controller.cargarUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId(), planificacion);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                tablePlanes.getItems().clear();
                loadPlanificaciones();
                //unidadObraViewObservableList = FXCollections.observableArrayList();
                //unidadObraViewObservableList = getUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());

                planViewObservableList = FXCollections.observableArrayList();
                planViewObservableList = AllPlanificaciones(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());
                tablePlanes.getItems().setAll(planViewObservableList);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        */
    }


}





