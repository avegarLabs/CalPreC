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
import java.util.ResourceBundle;

public class SalarioUnidadesObraObraController implements Initializable {

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
    private TableView<UniddaObraView> dataTable;
    @FXML
    private TableColumn<UniddaObraView, StringProperty> code;
    @FXML
    private TableColumn<UniddaObraView, StringProperty> descripcion;
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
    private TableView<UORVTableView> tableRenglones;
    @FXML
    private TableColumn<UORVTableView, StringProperty> RVcode;
    @FXML
    private TableColumn<UORVTableView, DoubleProperty> RVcant;
    @FXML
    private TableColumn<UORVTableView, DoubleProperty> RVCosto;
    @FXML
    private TableView<ManoObraRenglonVariante> tableSuministros;
    @FXML
    private TableColumn<ManoObraRenglonVariante, StringProperty> SumCode;
    @FXML
    private TableColumn<ManoObraRenglonVariante, DoubleProperty> SumCant;
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
                sal = Math.round(unidadobrarenglon.getCantRv() * calcSalRV(manoobraList) * 100d) / 100d;
                costosRV = unidadobrarenglon.getCostMat() + unidadobrarenglon.getCostMano() + unidadobrarenglon.getCostEquip();
                CostoTotal = unidadobrarenglon.getCantRv() * costosRV;
                Renglonvariante renglonvariante = session.get(Renglonvariante.class, unidadobrarenglon.getRenglonvarianteId());
                uorvTableView = new UORVTableView(renglonvariante.getId(), renglonvariante.getCodigo(), unidadobrarenglon.getCantRv(), Math.round(CostoTotal * 100d) / 100d, renglonvariante.getDescripcion(), renglonvariante.getPeso(), renglonvariante.getUm(), costosRV, "1", sal, 0.0, unidadobrarenglon.getConMat(), renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonvariante.getSubgrupoBySubgrupoId().getDescripcionsub());
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

    private ObservableList<String> getListSubEspecialidad(int id) {

        subEspecialiadesList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Subespecialidades where especialidadesId = :id ");
            query.setParameter("id", id);
            subespecialidadesArrayList = (ArrayList<Subespecialidades>) ((org.hibernate.query.Query) query).list();
            subespecialidadesArrayList.forEach(subespecialidades -> {
                subEspecialiadesList.add(subespecialidades.getCodigo() + " - " + subespecialidades.getDescripcion());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return subEspecialiadesList;
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


    public void handlrLlenarSubsepecialidad(ActionEvent event) {
        String codeEsp = comboEspecialidad.getValue().substring(0, 2);

        especialidadesArrayList.forEach(especialidades -> {
            if (especialidades.getCodigo().contentEquals(codeEsp)) {
                comboSubEspecialidad.setItems(getListSubEspecialidad(especialidades.getId()));
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


    public void addElementTableRV(int idUO) {

        RVcode.setCellValueFactory(new PropertyValueFactory<UORVTableView, StringProperty>("codeRV"));
        RVcant.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("cant"));
        RVCosto.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("salario"));
        datosRV = FXCollections.observableArrayList();
        datosRV = getUoRVRelation(idUO);

        tableRenglones.getItems().setAll(datosRV);


    }


    public void addElementTableSuministros() {

        SumCode.setCellValueFactory(new PropertyValueFactory<ManoObraRenglonVariante, StringProperty>("codigo"));
        SumCant.setCellValueFactory(new PropertyValueFactory<ManoObraRenglonVariante, DoubleProperty>("norma"));

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboEspecialidad.setItems(getEspecialidades());
        codesList = FXCollections.observableArrayList();
        if (dataTable.getItems() != null) {
            dataTable.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    paneCalc.setVisible(true);
                    unidadObraView = dataTable.getSelectionModel().getSelectedItem();
                    labelCode.setText(unidadObraView.getCodigo());
                    labelUodescrip.setText(unidadObraView.getDescripcion());
                    unidadobra = initializeUnidadObra(unidadObraView.getId());
                    if (unidadobra.getRenglonbase() != null) {
                        addElementTableRV(unidadObraView.getId());
                        //addElementTableSuministros(unidadObraView.getId());
                        uorvTableViewsList.forEach(table -> {
                            /*
                            if (table.getId() == unidadobra.getRenglonbase()) {
                                codesList.add(table.getCodeRV());
                                comboRenglones.setItems(codesList);
                                comboRenglones.getSelectionModel().select(String.valueOf(table.getCodeRV()));
                                codeRB = String.valueOf(unidadobra.getRenglonbase());
                            }
                            */

                        });

                        if (unidadobra != null && unidadobra.getCantidad() != 0.0 && unidadobra.getCostounitario() != 0.0 && unidadobra.getCostototal() != 0.0) {
                            labelCant.setText(String.valueOf(unidadobra.getCantidad()));
                            labelUM.setText(unidadobra.getUm());
                            labelcostU.setText(String.valueOf(Math.round(unidadobra.getCostounitario() * 100d) / 100d));
                            labelCosT.setText(String.valueOf(Math.round(unidadobra.getCostototal() * 100d) / 100d));
                            sumCostoTotal = Math.round(unidadobra.getCostototal() * 100d) / 100d;
                            labelSal.setText(String.valueOf(Math.round(unidadobra.getSalario() * 100d) / 100d));
                        }

                    } else {

                        deleteRelacionesUORV(unidadObraView.getId());

                    }
                }
            });
        }

        btnHide.setOnAction(event -> {

            String code = comboRenglones.getValue();
            if (code == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("ERROR");
                alert.setContentText("Debe seleccionar un renglón base para la unida de obra!");
                alert.showAndWait();
            } else {
                if (codeRB != code || codeRB == null) {
                    tableRenglones.getItems().forEach(rv -> {
                        if (rv.getCodeRV().equals(code)) {

                            //falg = AddRenglonBase(unidadObraView.getId(), rv.getCodeRV());
                            if (falg == true) {
                                falg1 = completeValuesUO(unidadObraView.getId(), rv.getUm(), rv.getCant(), Double.parseDouble(labelCosT.getText()), Double.parseDouble(labelcostU.getText()), importe, costoMaterial);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText("Información");
                                alert.setContentText(rv.getCodeRV() + " definido como renglón base en la unidad de obra");
                                alert.showAndWait();
                            }
                        }
                    });
                }
                if (unidadobra.getUm() != labelUM.getText() || unidadobra.getCantidad() != Double.parseDouble(labelCant.getText()) || unidadobra.getCostounitario() != Double.parseDouble(labelcostU.getText()) || unidadobra.getCostoMaterial() != costoMaterial || unidadobra.getCostototal() != Double.parseDouble(labelCosT.getText())) {
                    tableRenglones.getItems().forEach(rv -> {
                        if (rv.getCodeRV().equals(code)) {
                            falg = completeValuesUO(unidadObraView.getId(), rv.getUm(), rv.getCant(), Double.parseDouble(labelCosT.getText()), Double.parseDouble(labelcostU.getText()), importe, costoMaterial);
                        }

                        if (falg == true) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Información");
                            alert.setContentText("Cambios en la unidad de obra relizados correctamente!");
                            alert.showAndWait();
                        }

                    });
                }
                sumCostoTotal = 0.0;
                codesList.clear();
                paneCalc.setVisible(false);
            }
        });
        tableRenglones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            manoobraList = FXCollections.observableArrayList();
            manoobraList = getManoObraRenglon(tableRenglones.getSelectionModel().getSelectedItem().getId());
            sal = calcSalRV(manoobraList);
            labelRVCant.setText(String.valueOf(Math.round(sal * 100d) / 100d));
            tableSuministros.setVisible(true);
            addElementTableSuministros();
            tableSuministros.getItems().setAll(manoobraList);
            labelPeso.setText(tableRenglones.getSelectionModel().getSelectedItem().getUm());
            //labelRVum.setText(tableRenglones.getSelectionModel().getSelectedItem().getUm());
            textDescrp.setText(tableRenglones.getSelectionModel().getSelectedItem().getDescripcion());

        });

        tableSuministros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            sal = getImporteEscala(tableSuministros.getSelectionModel().getSelectedItem().getEscala());
            labelRVCant.setText(String.valueOf(sal));
            labelPeso.setText("h/h");
            //labelRVum.setText(tableSuministros.getSelectionModel().getSelectedItem().getUm());
            textDescrp.setText(tableSuministros.getSelectionModel().getSelectedItem().getDescripcion());

        });
    }


    public void definirObra(ObrasView obra) {
        IdObra = obra.getId();
        System.out.println(IdObra);
        label_title.setText("Salario ObraPCW: " + obra.getCodigo() + " - " + obra.getDescripion());

        coboEmpresas.setItems(getListEmpresas(IdObra));
        comboZonas.setItems(getListZonas(IdObra));

    }


    public void addElementosTable() {
        String[] codeEmp = coboEmpresas.getValue().split(" - ");
        String codeSub = comboSubEspecialidad.getValue().substring(0, 3);
        String codeObjeto = comboObjetos.getValue().substring(0, 2);
        String codeNivel = comboNivel.getValue().substring(0, 2);
        String codeZona = comboZonas.getValue().substring(0, 3);
        String codeEspecialidad = comboEspecialidad.getValue().substring(0, 2);

        empresaArrayList.forEach(empresaconstructora -> {
            if (empresaconstructora.getCodigo().contentEquals(codeEmp[0])) {
                empresaconstructoraModel = empresaconstructora;

            }
        });

        subespecialidadesArrayList.forEach(subespecialidades -> {
            if (subespecialidades.getCodigo().contentEquals(codeSub)) {
                subespecialidadesModel = subespecialidades;

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

        code.setCellValueFactory(new PropertyValueFactory<UniddaObraView, StringProperty>("codigo"));
        descripcion.setCellValueFactory(new PropertyValueFactory<UniddaObraView, StringProperty>("descripcion"));


        datos = FXCollections.observableArrayList();
        datos = getUnidadesObra(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());
        dataTable.getItems().setAll(datos);

    }

    public void handleCargardatos(ActionEvent event) {

        addElementosTable();
        if (dataTable.getItems().size() > 0) {
            dataTable.setVisible(true);
        }
    }

    public void handleDeleteUo(ActionEvent event) {
        Integer id = dataTable.getSelectionModel().getSelectedItem().getId();

        boolean mssg = deleteUnidadObra(id);

        if (mssg = true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText(dataTable.getSelectionModel().getSelectedItem().getDescripcion() + " eliminada satisfactoriamente!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Información");
            alert.setContentText("Error al eliminar satisfactoriamente!");
            alert.showAndWait();
        }

        addElementosTable();
        if (dataTable.getItems().size() == 0) {
            dataTable.setVisible(false);
        }

    }


    public void handleNewRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        String codeRB = comboRenglones.getValue();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToUnidadObra.fxml"));
            Parent proot = loader.load();
            RenVarianteToUnidadObraController controller = loader.getController();
            // controller.addUnidadObra( , unidadObraView);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                costoMaterial = 0.0;
                costoMaterial = getCostoMaterial(unidadObraView.getId());
                System.out.println("C1 " + costoMaterial);

                costo = 0.0;
                costo = getUoCostoTotal(unidadObraView.getId());
                System.out.println("C1 " + costo);

                sumCostoTotal = 0.0;
                sumCostoTotal = costo + costoMaterial;
                System.out.println("C1 " + sumCostoTotal);
                if (sumCostoTotal != 0.0) {
                    labelCosT.setText(" ");
                    labelCosT.setText(String.valueOf(Math.round(sumCostoTotal * 100d) / 100d));
                    tableRenglones.getItems().clear();
                    addElementTableRV(unidadObraView.getId());
                }

                if (codeRB != null) {
                    tableRenglones.getItems().forEach(datos -> {
                        importe = 0.0;
                        importe += datos.getSalario();
                        labelSal.setText(String.valueOf(Math.round(importe * 100d) / 100d));

                    });
                    labelcostU.setText(" ");
                    labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / Double.parseDouble(labelCant.getText()) * 100d) / 100d));
                }


                tableRenglones.getItems().forEach(rv -> {
                    codesList.add(rv.getCodeRV());
                    comboRenglones.setItems(codesList);
                });
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * para definir el renglon base y calcular los demas parametros
     *
     * @param event
     */
    public void handleDefinirRenglonBase(ActionEvent event) {

        String code = comboRenglones.getValue();


        costoMaterial = 0.0;
        costoMaterial = getCostoMaterial(unidadObraView.getId());
        System.out.println("C1 " + costoMaterial);

        costo = 0.0;
        costo = getUoCostoTotal(unidadObraView.getId());
        System.out.println("C1 " + costo);

        sumCostoTotal = 0.0;
        sumCostoTotal = costo + costoMaterial;
        System.out.println("C1 " + sumCostoTotal);
        importe = 0.0;
        tableRenglones.getItems().forEach(datos -> {
            // System.out.println(datos.getSalario());

            importe += datos.getSalario();


            labelSal.setText(String.valueOf(Math.round(importe * 100d) / 100d));
            if (datos.getCodeRV().equals(code)) {
                labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / datos.getCant() * 100d) / 100d));
                labelUM.setText(datos.getUm());
                labelCant.setText(String.valueOf(datos.getCant()));
            }
        });
    }

    /**
     * para definir los suministros bajo especificacion
     */
    public void haddleBajoEspecificacion(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministroBajoEspecificacion.fxml"));
            Parent proot = loader.load();
            BajoEspecificacionController controller = loader.getController();
            // controller.pasarUnidaddeObra(unidadObraView);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();


            stage.setOnCloseRequest(event1 -> {


                // addElementTableSuministros(unidadObraView.getId());
                costoMaterial = 0.0;
                costoMaterial = getCostoMaterial(unidadObraView.getId());
                System.out.println("C2 " + costoMaterial);

                costo = 0.0;
                costo = getUoCostoTotal(unidadObraView.getId());
                System.out.println("C2 " + costo);

                sumCostoTotal = 0.0;
                sumCostoTotal = costo + costoMaterial;
                System.out.println("C2 " + sumCostoTotal);
                labelCosT.setText(" ");
                labelCosT.setText(String.valueOf(Math.round(sumCostoTotal * 100d) / 100d));

                String code = comboRenglones.getValue();


                tableRenglones.getItems().forEach(datos -> {
                    if (datos.getCodeRV().equals(code)) {
                        //para calcular el costo unitario
                        cantUO = Double.parseDouble(labelCant.getText());

                        labelcostU.setText(" ");
                        labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / cantUO * 100d) / 100d));

                    }
                });


            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void haddleUpadteBajoEspecificacion(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        //  bajoEspecificacionView = tableSuministros.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSuministroBajoEspecificacion.fxml"));
            Parent proot = loader.load();
            UpdateBajoEspecificacionController controller = loader.getController();
            //  controller.pasarUnidaddeObra(unidadObraView, bajoEspecificacionView.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();


            stage.setOnCloseRequest(event1 -> {


                //   addElementTableSuministros(unidadObraView.getId());
                costoMaterial = 0.0;
                costoMaterial = getCostoMaterial(unidadObraView.getId());


                costo = 0.0;
                costo = getUoCostoTotal(unidadObraView.getId());


                sumCostoTotal = costo + costoMaterial;
                labelCosT.setText(" ");
                labelCosT.setText(String.valueOf(Math.round(sumCostoTotal * 100d) / 100d));

                String code = comboRenglones.getValue();


                tableRenglones.getItems().forEach(datos -> {
                    if (datos.getCodeRV().equals(code)) {
                        //para calcular el costo unitario
                        cantUO = Double.parseDouble(labelCant.getText());
                        labelcostU.setText(" ");
                        labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / cantUO * 100d) / 100d));

                    }
                });


            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void handleActualizarRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToUnidadObraUpdate.fxml"));
            Parent proot = loader.load();
            RenVarianteToUnidadObraUpdateController controller = loader.getController();
            //controller.addUpdateRvToUnidadO(unidadObraView, uorvTableView.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                costoMaterial = 0.0;
                costoMaterial = getCostoMaterial(unidadObraView.getId());
                System.out.println("C1 " + costoMaterial);

                costo = 0.0;
                costo = getUoCostoTotal(unidadObraView.getId());
                System.out.println("C1 " + costo);

                sumCostoTotal = 0.0;
                sumCostoTotal = costo + costoMaterial;
                System.out.println("C1 " + sumCostoTotal);
                if (sumCostoTotal != 0.0) {
                    labelCosT.setText(" ");
                    labelCosT.setText(String.valueOf(Math.round(sumCostoTotal * 100d) / 100d));
                    tableRenglones.getItems().clear();
                    addElementTableRV(unidadObraView.getId());
                }

                String code = comboRenglones.getValue();

                importe = 0.0;
                tableRenglones.getItems().forEach(datos -> {
                    importe += datos.getSalario();
                    labelSal.setText(" ");
                    labelSal.setText(String.valueOf(Math.round(importe * 100d) / 100d));
                    if (datos.getCodeRV().equals(code)) {
                        //para calcular el costo unitario
                        cantUO = Double.parseDouble(labelCant.getText());
                        labelcostU.setText(" ");
                        labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / cantUO * 100d) / 100d));


                    }


                });

/**Por terminar
 codesList.forEach(list -> {
 tableRenglones.getItems().forEach(rv -> {
 if (list.contentEquals(rv.getCodeRV())) {
 System.out.println("---");

 } else {
 codesList.add(rv.getCodeRV());
 comboRenglones.setItems(codesList);
 }
 });
 });
 */
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleDeleteRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();

        deleteUnidadObreRenglon(unidadObraView.getId(), uorvTableView.getId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Renglón Variante eliminado de la unidad de ObraPCW!");
        alert.showAndWait();

        costoMaterial = 0.0;
        costoMaterial = getCostoMaterial(unidadObraView.getId());
        System.out.println("C1 " + costoMaterial);

        costo = 0.0;
        costo = getUoCostoTotal(unidadObraView.getId());
        System.out.println("C1 " + costo);

        sumCostoTotal = 0.0;
        sumCostoTotal = costo + costoMaterial;
        System.out.println("C1 " + sumCostoTotal);
        if (sumCostoTotal != 0.0) {
            labelCosT.setText(" ");
            labelCosT.setText(String.valueOf(Math.round(sumCostoTotal * 100d) / 100d));
            tableRenglones.getItems().clear();
            addElementTableRV(unidadObraView.getId());

        }

        String code = comboRenglones.getValue();

        importe = 0.0;
        tableRenglones.getItems().forEach(datos -> {
            importe += datos.getSalario();
            labelSal.setText(" ");
            labelSal.setText(String.valueOf(Math.round(importe * 100d) / 100d));
            if (datos.getCodeRV().equals(code)) {
                //para calcular el costo unitario
                cantUO = Double.parseDouble(labelCant.getText());
                labelcostU.setText(" ");
                labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / cantUO * 100d) / 100d));


            }


        });


        //

        tableRenglones.getItems().forEach(rv -> {
            codesList.forEach(list -> {
                if (!list.contentEquals(rv.getCodeRV())) {
                    codesList.remove(list);
                }
            });

        });


    }

    public void handleDeleteSumistro(ActionEvent event) {
        //bajoEspecificacionView = tableSuministros.getSelectionModel().getSelectedItem();
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        deleteBajoEspecificacion(bajoEspecificacionView.getId());


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Suministro eliminado de la unidad de ObraPCW!");
        alert.showAndWait();


        //  addElementTableSuministros(unidadObraView.getId());
        costoMaterial = 0.0;
        costoMaterial = getCostoMaterial(unidadObraView.getId());

        costo = 0.0;
        costo = getUoCostoTotal(unidadObraView.getId());

        sumCostoTotal = 0.0;
        sumCostoTotal = costo + costoMaterial;
        labelCosT.setText(" ");
        labelCosT.setText(String.valueOf(Math.round(sumCostoTotal * 100d) / 100d));

        String code = comboRenglones.getValue();


        tableRenglones.getItems().forEach(datos -> {
            if (datos.getCodeRV().equals(code)) {
                //para calcular el costo unitario
                cantUO = Double.parseDouble(labelCant.getText());
                labelcostU.setText(" ");
                labelcostU.setText(String.valueOf(Math.round(sumCostoTotal / cantUO * 100d) / 100d));

            }
        });
    }


}








