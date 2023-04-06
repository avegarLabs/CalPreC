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
import javafx.scene.control.*;
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

public class CertificacionUnidadesObraObraController implements Initializable {

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
    private double disponible;
    private double cantCert;
    @FXML
    private TableView<CertificacionView> dateCertificaciones;
    @FXML
    private TableColumn<CertificacionView, StringProperty> codeUO;
    @FXML
    private TableColumn<CertificacionView, StringProperty> descripcionUO;
    @FXML
    private TableColumn<CertificacionView, StringProperty> um;
    @FXML
    private TableColumn<CertificacionView, DoubleProperty> cantidad;
    @FXML
    private TableColumn<CertificacionView, DoubleProperty> costMat;
    @FXML
    private TableColumn<CertificacionView, DoubleProperty> costMan;
    @FXML
    private TableColumn<CertificacionView, DoubleProperty> costEquip;
    @FXML
    private TableColumn<CertificacionView, DoubleProperty> disponib;
    private ArrayList<Certificacion> certificacionArrayList;
    private ObservableList<CertificacionView> certificacionViewObservableList;
    private ObservableList<CertificacionView> datosTable;
    private CertificacionView certificacionView;
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


    private Unidadobrarenglon unidadobrarenglon;


    private ObservableList<SubespecialidadView> subespecialidadViewObservableList;
    private SubespecialidadView subespecialidadView;

    private Certificacion certificacion;

    @FXML
    private ContextMenu menuOptiosn;
    private UsuariosDAO usuariosDAO;

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
            for (Empresaobra empresaobra : empresaobraArrayList) {
                Empresaconstructora empresaconstructora = session.find(Empresaconstructora.class, empresaobra.getEmpresaconstructoraId());
                empresaArrayList.add(empresaconstructora);
                empresasList.add(empresaconstructora.getCodigo() + " - " + empresaconstructora.getDescripcion());
            }
            tx.commit();
            session.close();
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
            for (Zonas zonas : zonasArrayList) {
                zonaList.add(zonas.getCodigo() + " - " + zonas.getDesripcion());
            }

            tx.commit();
            session.close();
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

    /**
     * Metodos para mostrar lis materiales suminitrados bajo especificacion a la unida de obra
     */

    public ObservableList<BajoEspecificacionView> getSumBajoEspecificacion(int idUO) {

        bajoEspecificacionViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            System.out.println(idUO);

            Query query = session.createQuery("FROM Bajoespecificacion where unidadobraId = :id ");
            query.setParameter("id", idUO);
            bajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) ((org.hibernate.query.Query) query).list();
            bajoespecificacionArrayList.forEach(bajoespecificacion -> {

                if (bajoespecificacion.getTipo().contentEquals("1")) {
                    Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdSuministro());
                    bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
                    bajoEspecificacionViewObservableList.add(bajoEspecificacionView);
                }
                if (bajoespecificacion.getTipo().contentEquals("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdSuministro());
                    bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
                    bajoEspecificacionViewObservableList.add(bajoEspecificacionView);
                }
                if (bajoespecificacion.getTipo().contentEquals("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdSuministro());
                    bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getUnidadobraId(), bajoespecificacion.getIdSuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
                    bajoEspecificacionViewObservableList.add(bajoEspecificacionView);
                }

            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return bajoEspecificacionViewObservableList;
    }

    public Double getCostoMaterial(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        costo = 0.0;
        costoMaterial = 0.0;

        try {
            tx = session.beginTransaction();
            System.out.println(idUO);

            Query query = session.createQuery("FROM Bajoespecificacion where unidadobraId = :id ");
            query.setParameter("id", idUO);
            bajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) ((org.hibernate.query.Query) query).list();
            bajoespecificacionArrayList.forEach(bajoespecificacion -> {
                costo = bajoespecificacion.getCosto();
                costoMaterial += Math.round(costo * 100d) / 100d;

            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return costoMaterial;
    }

    /**
     * Metodo pare mostrar los RV que componen a unida de obra
     *
     * @param idUO
     * @return
     */

    public ObservableList<UORVTableView> getUoRVRelation(int idUO) {

        uorvTableViewsList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobrarenglon where unidadobraId = :id ");
            query.setParameter("id", idUO);
            unidadobrarenglonArrayList = (ArrayList<Unidadobrarenglon>) ((org.hibernate.query.Query) query).list();
            unidadobrarenglonArrayList.forEach(unidadobrarenglon -> {
                manoobraList = getManoObraRenglon(unidadobrarenglon.getRenglonvarianteId());
                sal = unidadobrarenglon.getSalariomn();
                costosRV = unidadobrarenglon.getCostMat() + unidadobrarenglon.getCostMano() + unidadobrarenglon.getCostEquip();
                CostoTotal = unidadobrarenglon.getCantRv() * costosRV;
                Renglonvariante renglonvariante = session.get(Renglonvariante.class, unidadobrarenglon.getRenglonvarianteId());
                uorvTableView = new UORVTableView(renglonvariante.getId(), renglonvariante.getCodigo(), unidadobrarenglon.getCantRv(), Math.round(CostoTotal * 100d) / 100d, renglonvariante.getDescripcion(), renglonvariante.getPeso(), renglonvariante.getUm(), costosRV, "1", sal, unidadobrarenglon.getSalariocuc(), unidadobrarenglon.getConMat(), renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonvariante.getSubgrupoBySubgrupoId().getDescripcionsub());
                uorvTableViewsList.add(uorvTableView);


            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return uorvTableViewsList;
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

    private ObservableList<UniddaObraView> getUnidadesObra(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        unidadObraViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobra where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);
            unidadobraArrayList = (ArrayList<Unidadobra>) ((org.hibernate.query.Query) query).list();
            unidadobraArrayList.forEach(unidadobra -> {
                unidadObraView = new UniddaObraView(unidadobra.getId(), unidadobra.getCodigo(), unidadobra.getDescripcion(), unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());
                unidadObraViewObservableList.add(unidadObraView);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadObraViewObservableList;
    }

    public boolean deleteUnidadObra(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Unidadobra unidadobra = session.get(Unidadobra.class, id);
            if (unidadobra != null) {

                Query query = session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId = : idUO");
                query.setParameter("idUO", id);

                unidadobrarenglonArrayList = (ArrayList<Unidadobrarenglon>) ((org.hibernate.query.Query) query).list();
                unidadobrarenglonArrayList.forEach(unidadobrarenglon1 -> {
                    deleteUnidadObreRenglon(unidadobrarenglon1.getUnidadobraId(), unidadobrarenglon1.getRenglonvarianteId());
                });

                Query query1 = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idUO");
                query1.setParameter("idUO", id);

                bajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) ((org.hibernate.query.Query) query1).list();
                bajoespecificacionArrayList.forEach(bajoespecificacion -> {
                    deleteBajoEspecificacion(bajoespecificacion.getId());
                });

                session.delete(unidadobra);
                falg = true;
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return falg;
    }

    public void deleteRelacionesUORV(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("from Unidadobrarenglon where unidadobraId = : id");
            query.setParameter("id", id);
            unidadobrarenglonArrayList = (ArrayList<Unidadobrarenglon>) ((org.hibernate.query.Query) query).list();
            unidadobrarenglonArrayList.forEach(unidadobrarenglon -> {
                session.delete(unidadobrarenglon);
            });

            Query query1 = session.createQuery("FROM Bajoespecificacion WHERE unidadobraId =: idU");
            query1.setParameter("idU", id);
            bajoespecificacionArrayList = (ArrayList<Bajoespecificacion>) ((org.hibernate.query.Query) query1).list();
            bajoespecificacionArrayList.forEach(bajoespecificacion -> {
                session.delete(bajoespecificacion);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public Unidadobra initializeUnidadObra(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            unidadobra = session.get(Unidadobra.class, id);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return unidadobra;
    }

    /*
        public boolean AddRenglonBase(int idUO, int idReglon) {
            falg = false;
            Session session = ConnectionModel.createAppConnection().openSession();

            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Unidadobra unidadobra = (Unidadobra) session.get(Unidadobra.class, idUO);
                if (unidadobra.getRenglonbase() == null) {
                    unidadobra.setRenglonbase(idReglon);
                    session.update(unidadobra);
                    falg = true;
                } else if (unidadobra.getRenglonbase() != idReglon) {
                    unidadobra.setRenglonbase(idReglon);
                    session.update(unidadobra);
                    falg = true;
                }


                tx.commit();
            } catch (HibernateException he) {
                if (tx != null) tx.rollback();
                he.printStackTrace();
            } finally {
                session.close();
            }
            return falg;
        }
    */
    public boolean completeValuesUO(int idUO, String um, double cantidad, double costoTotal, double costoUnitario, double salario, double costoMaterial) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Unidadobra unidadobra = session.get(Unidadobra.class, idUO);
            if (unidadobra != null) {
                unidadobra.setUm(um);
                unidadobra.setCantidad(cantidad);
                unidadobra.setCostototal(costoTotal);
                unidadobra.setCostounitario(costoUnitario);
                unidadobra.setSalario(salario);
                unidadobra.setCostoMaterial(costoMaterial);
                session.update(unidadobra);
                falg = true;
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return falg;
    }

    public void deleteUnidadObreRenglon(int idUo, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            Query query = session.createQuery("FROM Unidadobrarenglon WHERE unidadobraId = : idU AND renglonvarianteId =: idR ");
            query.setParameter("idU", idUo);
            query.setParameter("idR", idRV);

            unidadobrarenglon = (Unidadobrarenglon) query.getSingleResult();
            session.delete(unidadobrarenglon);


            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }


    }

    private void deleteBajoEspecificacion(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            Bajoespecificacion bajoespecificacion = session.get(Bajoespecificacion.class, id);
            session.delete(bajoespecificacion);


            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

    }

    public ArrayList<Certificacion> getCertificaciones() {
        certificacionArrayList = new ArrayList<Certificacion>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionArrayList = (ArrayList<Certificacion>) session.createQuery("FROM Certificacion ").list();


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certificacionArrayList;
    }

    public ObservableList<CertificacionView> Allcertificaciones(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {
        certificacionViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();


            Query query = session.createQuery("SELECT uo.id, uo.codigo, uo.cantidad,  uo.um,  uo.descripcion, SUM(cr.cantidad), SUM(cr.costmaterial), SUM(cr.costmano), SUM(cr.costequipo), SUM(cr.salario), SUM(cr.salario) FROM Unidadobra uo INNER JOIN Certificacion cr ON uo.id = cr.unidadobraId WHERE uo.obraId = :idObra and uo.empresaconstructoraId =:idEmp AND uo.zonasId = :idZona and uo.objetosId =: idObjeto and uo.nivelId =: idNivel and uo.especialidadesId =: idEspecilidad and uo.subespecialidadesId =:idSub GROUP BY  uo.id, uo.codigo, uo.cantidad,  uo.um,  uo.descripcion");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);

            for (Iterator it = ((org.hibernate.query.Query) query).iterate(); it.hasNext(); ) {
                Object[] row = (Object[]) it.next();
                Integer iduo = Integer.parseInt(row[0].toString());
                String code = row[1].toString();
                double cantUO = Double.parseDouble(row[2].toString());
                String umUO = row[3].toString();
                String descrip = row[4].toString();
                double certificado = Double.parseDouble(row[5].toString());
                double costMaterial = Double.parseDouble(row[6].toString());
                double costMano = Double.parseDouble(row[7].toString());
                double costEquipo = Double.parseDouble(row[8].toString());
                double sal = Double.parseDouble(row[9].toString());
                double salcuc = Double.parseDouble(row[10].toString());

                certificacionView = new CertificacionView(1, code, descrip, umUO, certificado, costMaterial, costMano, costEquipo, sal, salcuc, "", " ", cantUO - certificado);
                certificacionViewObservableList.add(certificacionView);

            }


            /*
            disponible = 0.0;
            cantCert = 0.0;
            certificacionArrayList = getCertificaciones();
            arrayList.forEach(unidad -> {
                certificacionArrayList.forEach(certif -> {
                    if (certif.getUnidadobraId() == unidad.getId()) {
                        cantCert += certif.getCantidad();
                        disponible = unidad.getCantidad() - cantCert;
                        certificacionView = new CertificacionView(certif.getId(), unidad.getCodigo(), unidad.getDescripcion(), unidad.getUm(), certif.getCantidad(), certif.getCostmaterial(), certif.getCostmano(), certif.getCostequipo(), certif.getSalario(), certif.getCucsalario(), certif.getDesde().toString(), certif.getHasta().toString(), Math.round(disponible * 100d) / 100d);
                        certificacionViewObservableList.add(certificacionView);
                    }

                });

            });
*/

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return certificacionViewObservableList;
    }

    public void handleLenaObjetosList(ActionEvent event) {
        String codeZona = comboZonas.getValue().substring(0, 3);

        zonasArrayList.forEach(zonas -> {
            if (zonas.getCodigo().contentEquals(codeZona)) {
                comboObjetos.setItems(getListObjetos(zonas.getId()));
                zonasModel = zonas;
            }
        });
    }

    public void handleLlenarNivelList(ActionEvent event) {
        String codeObjeto = comboObjetos.getValue().substring(0, 2);

        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(codeObjeto)) {
                comboNivel.setItems(getNivelList(objetos.getId()));
                objetosModel = objetos;

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
            //  controller.pasarZona(IdObra);


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
            // controller.pasarZona(zonasModel);


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
            // controller.pasarObjeto(objetosModel);


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

    public void loadCertificaciones() {

        codeUO.setCellValueFactory(new PropertyValueFactory<CertificacionView, StringProperty>("codeUo"));
        descripcionUO.setCellValueFactory(new PropertyValueFactory<CertificacionView, StringProperty>("descripcion"));
        um.setCellValueFactory(new PropertyValueFactory<CertificacionView, StringProperty>("um"));
        cantidad.setCellValueFactory(new PropertyValueFactory<CertificacionView, DoubleProperty>("cantidad"));
        costMat.setCellValueFactory(new PropertyValueFactory<CertificacionView, DoubleProperty>("costmaterial"));
        costMan.setCellValueFactory(new PropertyValueFactory<CertificacionView, DoubleProperty>("costmano"));
        costEquip.setCellValueFactory(new PropertyValueFactory<CertificacionView, DoubleProperty>("costequipo"));
        disponib.setCellValueFactory(new PropertyValueFactory<CertificacionView, DoubleProperty>("disponible"));

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
                loadCertificaciones();
                datosTable = FXCollections.observableArrayList();
                datosTable = Allcertificaciones(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());


                dateCertificaciones.getItems().setAll(datosTable);


            }
        });


        btnHide.setOnAction(event -> {

            paneCalc.setVisible(false);

        });


    }

    public void definirObra(ObrasView obra) {
        usuariosDAO = UsuariosDAO.getInstance();
        IdObra = obra.getId();
        System.out.println(IdObra);
        label_title.setText("Certificaciones en obra " + obra.getCodigo() + " - " + obra.getDescripion());

        coboEmpresas.setItems(getListEmpresas(IdObra));
        comboZonas.setItems(getListZonas(IdObra));

        if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : menuOptiosn.getItems()) {
                item.setDisable(true);
            }
        }

    }

    public void handleNewPlan(ActionEvent event) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NuevaCertificion.fxml"));
            Parent proot = loader.load();
            NuevaCertificacionController controller = loader.getController();
            controller.cargarUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadCertificaciones();
                datosTable = FXCollections.observableArrayList();
                datosTable = Allcertificaciones(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());


                dateCertificaciones.getItems().setAll(datosTable);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleAcatualizarPlan(ActionEvent event) {
        certificacionView = dateCertificaciones.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetallecertificacionUnidaddeObra.fxml"));
            Parent proot = loader.load();
            DetalleCertificacionUnidadObra controller = loader.getController();
            controller.passData(certificacionView.getCodeUo());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                loadCertificaciones();
                datosTable = FXCollections.observableArrayList();
                datosTable = Allcertificaciones(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadView.getId());


                dateCertificaciones.getItems().setAll(datosTable);
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}





