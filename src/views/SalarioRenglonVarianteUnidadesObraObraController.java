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
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SalarioRenglonVarianteUnidadesObraObraController implements Initializable {

    private static SessionFactory sf;
    public ArrayList<Empresagastos> empresagastosArrayList;
    public ArrayList<Empresaobraconcepto> empresaobraconceptoArrayList;
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

    // @FXML
    // private TableColumn<UORVTableView, DoubleProperty> RVCosto;
    @FXML
    private TableView<UORVTableView> tableRenglones;
    @FXML
    private TableColumn<UORVTableView, StringProperty> RVcode;
    @FXML
    private TableColumn<UORVTableView, DoubleProperty> RVcant;
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
    private String formulaCalc;
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
    private ArrayList<Nivelespecifico> nivelespecificoArrayList;
    private ObservableList<UniddaObraView> unidadObraViewObservableList;
    private UniddaObraView unidadObraView;
    private int IdObra;
    private int IdRV;
    private ObservableList<UniddaObraView> datos;
    private boolean falg;
    private boolean falg1;
    private ArrayList<Unidadobrarenglon> unidadobrarenglonArrayList;
    private ArrayList<Renglonnivelespecifico> renglonnivelespecificoArrayList;
    private double costosRV;
    private double CostoTotal;
    private double sumCostoTotal;
    private double costoMaterial;
    private double costoMatRV;
    private double costomatBe;
    private double costoMano;
    private double costoEquipo;
    private double salario;
    private double costMateNivel;
    private double costMaterialBajo;
    private double costo;
    private double costRV;
    private ObservableList<UORVTableView> uorvTableViewsList;
    private UORVTableView uorvTableView;
    private ObservableList<UORVTableView> datosRV;
    private String codeRB;
    private ObservableList<String> codesList;
    private Nivelespecifico nivel;
    private ArrayList<Renglonrecursos> renglonrecursosArrayList;
    private ArrayList<Renglonsemielaborados> renglonsemielaboradosArrayList;
    private ArrayList<Renglonjuego> renglonjuegoArrayList;
    private ObservableList<BajoEspecificacionView> componetesRVObservableList;
    private ObservableList<ManoObraRenglonVariante> manoobraList;
    private double importe;
    private double cantUO;
    private double valGrupo;
    private double sal;
    private double costSuministrosInRenglon;
    private ArrayList<Bajoespecificacion> bajoespecificacionArrayList;
    private ArrayList<Bajoespecificacionrv> bajoespecificacionrvArrayList;
    private ObservableList<BajoEspecificacionView> bajoEspecificacionViewObservableList;
    private BajoEspecificacionView bajoEspecificacionView;
    private ObservableList<BajoEspecificacionView> datosBE;
    private Unidadobrarenglon unidadobrarenglon;
    private Empresaobraconcepto empresaobraconcepto;

    private Renglonvariante renglonvariante;
    private ObraEmpresaNiveleEspecificosCostos datosCostos;

    private String changeFormula;


    private double concepto1;
    private double concepto2;
    private double concepto3;
    private double concepto4;
    private double concepto5;
    private double concepto6;
    private double concepto7;
    private double concepto8;
    private double concepto9;
    private double concepto10;
    private double concepto11;
    private double concepto12;
    private double concepto13;
    private double concepto14;
    private double concepto15;
    private double concepto16;
    private double concepto17;
    private double concepto18;
    private double concepto19;
    private double concepto20;


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
            Query query = session.createQuery("FROM Renglonnivelespecifico where nivelespecificoId = :id ");
            query.setParameter("id", idUO);
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
            renglonnivelespecificoArrayList.forEach(renglonnivelespecifico -> {
                costosRV = renglonnivelespecifico.getCostmaterial() * renglonnivelespecifico.getCantidad() + renglonnivelespecifico.getCostmano() * renglonnivelespecifico.getCantidad() + renglonnivelespecifico.getCostequipo() * renglonnivelespecifico.getCantidad();
                CostoTotal = Math.round(costosRV * 100d) / 100d;
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

    public ObservableList<BajoEspecificacionView> getSumBajoEspecificacion(int idUO, int idRV) {

        bajoEspecificacionViewObservableList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            System.out.println(idUO);

            Query query = session.createQuery("FROM Bajoespecificacionrv where nivelespecificoId = :id  AND  renglonvarianteId =: idR");
            query.setParameter("id", idUO);
            query.setParameter("idR", idRV);
            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) ((org.hibernate.query.Query) query).list();
            bajoespecificacionrvArrayList.forEach(bajoespecificacion -> {

                if (bajoespecificacion.getTipo().contentEquals("1")) {
                    Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdsuministro());
                    bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getIdsuministro(), bajoespecificacion.getIdsuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
                    bajoEspecificacionViewObservableList.add(bajoEspecificacionView);
                }
                if (bajoespecificacion.getTipo().contentEquals("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdsuministro());
                    bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getIdsuministro(), bajoespecificacion.getIdsuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
                    bajoEspecificacionViewObservableList.add(bajoEspecificacionView);
                }
                if (bajoespecificacion.getTipo().contentEquals("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdsuministro());
                    bajoEspecificacionView = new BajoEspecificacionView(bajoespecificacion.getId(), bajoespecificacion.getIdsuministro(), bajoespecificacion.getIdsuministro(), 0, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), bajoespecificacion.getCantidad(), Math.round(bajoespecificacion.getCosto() * 100d) / 100d, bajoespecificacion.getTipo());
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

    /**
     * Calculo del costo material de la unida de obra
     *
     * @param idUO
     * @return
     */

    public Double getCostoMaterial(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        costo = 0.0;
        costRV = 0.0;
        costoMaterial = 0.0;
        costoMatRV = 0.0;
        costomatBe = 0.0;

        try {
            tx = session.beginTransaction();
            Query query1 = session.createQuery("FROM Renglonnivelespecifico where nivelespecificoId = :id ");
            query1.setParameter("id", idUO);
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query1).list();
            renglonnivelespecificoArrayList.forEach(unidadobrarenglon -> {
                costosRV = unidadobrarenglon.getCostmaterial();
                costoMatRV += Math.round(costo * 100d) / 100d;

            });


            Query query = session.createQuery("FROM Bajoespecificacionrv where nivelespecificoId = :id ");
            query.setParameter("id", idUO);
            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) ((org.hibernate.query.Query) query).list();
            bajoespecificacionrvArrayList.forEach(bajoespecificacion -> {
                costo = bajoespecificacion.getCosto();
                costomatBe += Math.round(costo * 100d) / 100d;

            });

            costoMaterial = Math.round(costoMatRV + costomatBe * 100d) / 100d;
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
     * Calculo del costo de la mano de obra
     *
     * @param idUO
     * @return
     */
    public Double getCostoMano(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        costo = 0.0;
        costoMano = 0.0;

        try {
            tx = session.beginTransaction();
            System.out.println(idUO);

            Query query = session.createQuery("FROM Renglonnivelespecifico where nivelespecificoId = :id ");
            query.setParameter("id", idUO);
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
            renglonnivelespecificoArrayList.forEach(unidadobrarenglon -> {
                costo = unidadobrarenglon.getCostmano();
                costoMano += Math.round(costo * 100d) / 100d;

            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return costoMano;
    }

    /**
     * Claculo del costo de equipo
     *
     * @param idUO
     * @return
     */
    public Double getCostoEquipo(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        costo = 0.0;
        costoEquipo = 0.0;

        try {
            tx = session.beginTransaction();
            System.out.println(idUO);

            Query query = session.createQuery("FROM Renglonnivelespecifico where nivelespecificoId = :id ");
            query.setParameter("id", idUO);
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
            renglonnivelespecificoArrayList.forEach(unidadobrarenglon -> {
                costo = unidadobrarenglon.getCostequipo();
                costoEquipo += Math.round(costo * 100d) / 100d;

            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return costoEquipo;
    }


    /**
     * Metodo pare mostrar los RV que componen a unida de obra
     *
     * @param idUO
     * @return
     */

    public ObservableList<UORVTableView> getNivelRVRelation(int idUO) {

        uorvTableViewsList = FXCollections.observableArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Renglonnivelespecifico where nivelespecificoId = :id ");
            query.setParameter("id", idUO);
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
            renglonnivelespecificoArrayList.forEach(renglonnivelespecifico -> {
                costosRV = renglonnivelespecifico.getCostmaterial() * renglonnivelespecifico.getCantidad() + renglonnivelespecifico.getCostmano() * renglonnivelespecifico.getCantidad() + renglonnivelespecifico.getCostequipo() * renglonnivelespecifico.getCantidad();
                CostoTotal = Math.round(costosRV * 100d) / 100d;
                Renglonvariante renglonvariante = session.get(Renglonvariante.class, renglonnivelespecifico.getRenglonvarianteId());
                uorvTableView = new UORVTableView(renglonvariante.getId(), renglonvariante.getCodigo(), renglonnivelespecifico.getCantidad(), CostoTotal, renglonvariante.getDescripcion(), renglonvariante.getPeso(), renglonvariante.getUm(), costosRV, "1", sal, 0.0, renglonnivelespecifico.getConmat(), renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglonvariante.getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonvariante.getSubgrupoBySubgrupoId().getDescripcionsub());
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
        session.close();
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
        session.close();
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
        session.close();
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
        session.close();
        return subEspecialiadesList;
    }

    private ObservableList<UniddaObraView> getNivelEspecificos(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        unidadObraViewObservableList = FXCollections.observableArrayList();


        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Nivelespecifico where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub");
            query.setParameter("idObra", idObra);
            query.setParameter("idEmp", idEmp);
            query.setParameter("idZona", idZona);
            query.setParameter("idObjeto", idObj);
            query.setParameter("idNivel", idNiv);
            query.setParameter("idEspecilidad", idEsp);
            query.setParameter("idSub", idSub);
            nivelespecificoArrayList = (ArrayList<Nivelespecifico>) ((org.hibernate.query.Query) query).list();
            nivelespecificoArrayList.forEach(nivel -> {
                unidadObraView = new UniddaObraView(nivel.getId(), nivel.getCodigo(), nivel.getDescripcion(), nivel.getObraId(), nivel.getEmpresaconstructoraId());
                unidadObraViewObservableList.add(unidadObraView);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return unidadObraViewObservableList;
    }

    public boolean deleteNivelEspecifico(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Nivelespecifico nivelespecifico = session.get(Nivelespecifico.class, id);
            if (nivelespecifico != null) {

                Query query = session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId = : idUO");
                query.setParameter("idUO", id);

                renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
                renglonnivelespecificoArrayList.forEach(unidadobrarenglon1 -> {
                    deleteNivelEspecificoRenglon(unidadobrarenglon1.getNivelespecificoId(), unidadobrarenglon1.getRenglonvarianteId());
                });

                Query query1 = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUO");
                query1.setParameter("idUO", id);

                bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) ((org.hibernate.query.Query) query1).list();
                bajoespecificacionrvArrayList.forEach(bajoespecificacion -> {
                    deleteBajoEspecificacionRV(bajoespecificacion.getId());
                });

                session.delete(nivelespecifico);
                falg = true;
            }


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return falg;
    }

    public void deleteRelacionesUORV(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("from Renglonnivelespecifico where nivelespecificoId = : id");
            query.setParameter("id", id);
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
            renglonnivelespecificoArrayList.forEach(unidadobrarenglon -> {
                session.delete(unidadobrarenglon);
            });

            Query query1 = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId =: idU");
            query1.setParameter("idU", id);
            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) ((org.hibernate.query.Query) query1).list();
            bajoespecificacionrvArrayList.forEach(bajoespecificacion -> {
                session.delete(bajoespecificacion);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
    }


    public ArrayList<Empresagastos> listConceptos(Empresaconstructora empresaconstructora) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Empresagastos  WHERE empresaconstructoraId =: idEmp");
            query.setParameter("idEmp", empresaconstructora.getId());
            empresagastosArrayList = (ArrayList<Empresagastos>) ((org.hibernate.query.Query) query).list();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return empresagastosArrayList;
    }

    private ObservableList<BajoEspecificacionView> getSuminitrosRenglon(int id) {
        componetesRVObservableList = FXCollections.observableArrayList();
        //  bajoEspecificacionViewObservableList = FXCollections.observableArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            /**
             * Capturo los recursos que componen al renglon variante
             */
            Query query = session.createQuery("FROM Renglonrecursos WHERE renglonvarianteId = :id");
            query.setParameter("id", id);
            costSuministrosInRenglon = 0.0;


            renglonrecursosArrayList = (ArrayList<Renglonrecursos>) ((org.hibernate.query.Query) query).list();
            renglonrecursosArrayList.forEach(suministro -> {
                Recursos sum = session.find(Recursos.class, suministro.getRecursosId());
                // System.out.println(sum.getCodigo());
                if (sum.getTipo().contentEquals("1")) {
                    costSuministrosInRenglon = Math.round(sum.getPreciomn() * suministro.getCantidas() * 100d) / 100d;
                    bajoEspecificacionView = new BajoEspecificacionView(sum.getId(), 1, sum.getId(), suministro.getRenglonvarianteId(), sum.getCodigo(), sum.getDescripcion(), Math.round(sum.getPreciomn() + sum.getPreciomlc() * 100d) / 100d, sum.getUm(), sum.getPeso(), suministro.getCantidas(), costSuministrosInRenglon, sum.getTipo());

                    componetesRVObservableList.add(bajoEspecificacionView);


                }


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
                    //  System.out.println("semis" + sumSemi.getCodigo());
                    costSuministrosInRenglon = Math.round(sumSemi.getPreciomn() * semielab.getCantidad() * 100d) / 100d;
                    bajoEspecificacionView = new BajoEspecificacionView(sumSemi.getId(), 1, sumSemi.getId(), semielab.getRenglonvarianteId(), sumSemi.getCodigo(), sumSemi.getDescripcion(), Math.round(sumSemi.getPreciomn() + sumSemi.getPreciomlc() * 100d) / 100d, sumSemi.getUm(), sumSemi.getPeso(), semielab.getCantidad(), costSuministrosInRenglon, "S");
                    componetesRVObservableList.add(bajoEspecificacionView);

                });
            }

            Query query2 = session.createQuery("FROM Renglonjuego WHERE renglonvarianteId = :id");
            query2.setParameter("id", id);
            renglonjuegoArrayList = (ArrayList<Renglonjuego>) ((org.hibernate.query.Query) query2).list();
            if (renglonjuegoArrayList != null) {
                renglonjuegoArrayList.forEach(renglonjuego -> {
                    Juegoproducto juegoproducto = session.find(Juegoproducto.class, renglonjuego.getJuegoproductoId());
                    // System.out.println(juegoproducto.getCodigo());
                    costSuministrosInRenglon = Math.round(juegoproducto.getPreciomn() * renglonjuego.getCantidad() * 100d) / 100d;
                    bajoEspecificacionView = new BajoEspecificacionView(juegoproducto.getId(), 1, juegoproducto.getId(), renglonjuego.getRenglonvarianteId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), Math.round(juegoproducto.getPreciomn() + juegoproducto.getPreciomlc() * 100d) / 100d, juegoproducto.getUm(), juegoproducto.getPeso(), renglonjuego.getCantidad(), costSuministrosInRenglon, "J");
                    componetesRVObservableList.add(bajoEspecificacionView);

                });
            }

            componetesRVObservableList.forEach(data2 -> {
                System.out.println("Sum " + data2.getCodigo());
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return componetesRVObservableList;
    }


    private ArrayList<Empresagastos> getAllGastosValores(int idEmp) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Empresagastos WHERE empresaconstructoraId =: empId ORDER BY  conceptosgastoId");
            query.setParameter("empId", idEmp);

            empresagastosArrayList = (ArrayList<Empresagastos>) ((org.hibernate.query.Query) query).list();

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return empresagastosArrayList;
    }

    public void insertValorestoGastos(Empresaobraconcepto empresaobraconcepto) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.saveOrUpdate(empresaobraconcepto);


            tx.commit();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        session.close();

    }

    public void updateValorestoGastos(Empresaobraconcepto empresaobrac) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Empresaobraconcepto WHERE empresaconstructoraId =: empId AND conceptosgastoId =: conceptoId  AND obraId =: obraId");
            query.setParameter("empId", empresaobrac.getEmpresaconstructoraId());
            query.setParameter("conceptoId", empresaobrac.getConceptosgastoId());
            query.setParameter("obraId", empresaobrac.getObraId());
            Empresaobraconcepto empresaobraconcepto = (Empresaobraconcepto) query.getSingleResult();

            if (empresaobraconcepto.getValor() != empresaobrac.getValor()) {
                empresaobraconcepto.setValor(empresaobrac.getValor());
                session.update(empresaobraconcepto);
            }


            tx.commit();

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();

    }


    public Nivelespecifico initializeNivel(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            nivel = session.get(Nivelespecifico.class, id);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return nivel;
    }


    public Renglonvariante getRenglonVariante(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglonvariante = session.get(Renglonvariante.class, id);

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return renglonvariante;
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
            session.close();
            return falg;
        }
    */
    public boolean completeValuesNivelEspecifico(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            costoMaterial = 0.0;
            costoMano = 0.0;
            costoEquipo = 0.0;
            salario = 0.0;

            costMaterialBajo = 0.0;
            costoMatRV = 0.0;
            Nivelespecifico nivelespecifico = session.get(Nivelespecifico.class, id);
            if (nivelespecifico != null) {
                Query query = session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId =: idNivel");
                query.setParameter("idNivel", nivelespecifico.getId());
                renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) ((org.hibernate.query.Query) query).list();
                renglonnivelespecificoArrayList.forEach(renglonnivelespecifico -> {

                    costoMatRV += renglonnivelespecifico.getCostmaterial() * renglonnivelespecifico.getCantidad();
                    costoMano += renglonnivelespecifico.getCostmano() * renglonnivelespecifico.getCantidad();
                    costoEquipo += renglonnivelespecifico.getCostequipo() * renglonnivelespecifico.getCantidad();
                    salario += renglonnivelespecifico.getSalario();
                });

                costMaterialBajo = getCostoMaterialBajoEspecificacion(nivelespecifico.getId());

                costoMaterial = costoMatRV + costMaterialBajo;

                nivelespecifico.setCostomaterial(costoMaterial);
                nivelespecifico.setCostomano(costoMano);
                nivelespecifico.setCostoequipo(costoEquipo);
                nivelespecifico.setSalario(salario);

                session.update(nivelespecifico);
                falg = true;
            }

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return falg;

    }

    private double getCostoMaterialBajoEspecificacion(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            costMaterialBajo = 0.0;
            Query query = session.createQuery("FROM Bajoespecificacionrv WHERE nivelespecificoId = : idNivel");
            query.setParameter("idNivel", id);

            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) ((org.hibernate.query.Query) query).list();
            bajoespecificacionrvArrayList.forEach(bajoespecificacionrv -> {
                costMaterialBajo += bajoespecificacionrv.getCosto();
            });


            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();

        return costMaterialBajo;

    }

    public void deleteNivelEspecificoRenglon(int idUo, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            Query query = session.createQuery("FROM Renglonnivelespecifico WHERE nivelespecificoId = : idU AND renglonvarianteId =: idR ");
            query.setParameter("idU", idUo);
            query.setParameter("idR", idRV);

            Renglonnivelespecifico renglonnivelespecifico = (Renglonnivelespecifico) query.getSingleResult();
            session.delete(renglonnivelespecifico);


            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        session.close();
    }

    private void deleteBajoEspecificacionRV(int id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            Bajoespecificacionrv bajoespecificacion = session.get(Bajoespecificacionrv.class, id);
            session.delete(bajoespecificacion);


            trx.commit();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
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
            //controller.pasarZona(zonasModel);


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
            //controller.pasarObjeto(objetosModel);


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

    private ObraEmpresaNiveleEspecificosCostos getDatosConceptos(int idO, int idEmp) {

        nivelespecificoArrayList = new ArrayList<Nivelespecifico>();

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery("FROM Nivelespecifico WHERE obraId =: idO AND empresaconstructoraId =: idE");
            query.setParameter("idO", idO);
            query.setParameter("idE", idEmp);
            costoMaterial = 0.0;
            costoMano = 0.0;
            costoEquipo = 0.0;
            nivelespecificoArrayList = (ArrayList<Nivelespecifico>) ((org.hibernate.query.Query) query).list();
            nivelespecificoArrayList.forEach(nivelespecifico -> {
                costoMaterial += Math.round(nivelespecifico.getCostomaterial() * 100d) / 100d;
                costoMano += Math.round(nivelespecifico.getCostomano() * 100d) / 100d;
                costoEquipo += Math.round(nivelespecifico.getCostoequipo() * 100d) / 100d;
                datosCostos = new ObraEmpresaNiveleEspecificosCostos(idO, idEmp, costoMaterial, costoMano, costoEquipo);
            });

            tx.commit();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();

        return datosCostos;

    }


    public void addElementTableRV(int idUO) {

        RVcode.setCellValueFactory(new PropertyValueFactory<UORVTableView, StringProperty>("codeRV"));
        RVcant.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("cant"));
        // RVCosto.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("costoTotal"));
        datosRV = FXCollections.observableArrayList();
        datosRV = getNivelRVRelation(idUO);

        tableRenglones.getItems().setAll(datosRV);


    }
/*

    public void addElementTableSuministros(int idUO, int idRV) {

        SumCode.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, StringProperty>("codigo"));
        SumCant.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("cantidadBe"));
        SumCost.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("costoBe"));

        datosBE = FXCollections.observableArrayList();
        datosBE = getSumBajoEspecificacion(idUO, idRV);

        tableSuministros.getItems().setAll(datosBE);

    }
*/

    public Double calcStringFormula(int idConcept, String formula) {
        Object result;
        StringBuilder conceptEstruct = new StringBuilder();


        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("js");

        if (idConcept < 10) {
            for (int i = 0; i < formula.length() - 1; ++i) {
                char val = formula.charAt(i);
                if (val == '1') {
                    conceptEstruct.append(concepto1);
                } else if (val == '2') {
                    conceptEstruct.append(concepto2);
                } else if (val == '3') {
                    conceptEstruct.append(concepto3);
                } else if (val == '4') {
                    conceptEstruct.append(concepto4);
                } else if (val == '5') {
                    conceptEstruct.append(concepto5);
                } else if (val == '6') {
                    conceptEstruct.append(concepto6);
                } else if (val == '7') {
                    conceptEstruct.append(concepto7);
                } else if (val == '8') {
                    conceptEstruct.append(concepto8);
                } else if (val == '9') {
                    conceptEstruct.append(concepto9);

                } else {
                    conceptEstruct.append(val);
                }

            }
        }
        if (idConcept > 9) {

            if (formula.contains("*")) {

                String[] part = formula.split(Pattern.quote("*"));
                conceptEstruct.append(part[0]).append("*");
                for (int j = 0; j < part[1].length(); ++j) {
                    char val1 = part[1].charAt(j);
                    if (Character.isDigit(val1) || !Character.isDigit(val1)) {
                        if (val1 == '1') {
                            conceptEstruct.append(concepto1);
                        } else if (val1 == '2') {
                            conceptEstruct.append(concepto2);
                        } else if (val1 == '3') {
                            conceptEstruct.append(concepto3);
                        } else if (val1 == '4') {
                            conceptEstruct.append(concepto4);
                        } else if (val1 == '5') {
                            conceptEstruct.append(concepto5);
                        } else if (val1 == '6') {
                            conceptEstruct.append(concepto6);
                        } else if (val1 == '7') {
                            conceptEstruct.append(concepto7);
                        } else if (val1 == '8') {
                            conceptEstruct.append(concepto8);
                        } else if (val1 == '9') {
                            conceptEstruct.append(concepto9);

                        } else {
                            conceptEstruct.append(val1);
                        }
                    }
                }

            } else {

                for (int i = 0; i < formula.length() - 1; ++i) {
                    char val = formula.charAt(i);
                    char va1 = formula.charAt(i + 1);

                    if (Character.isDigit(val) && Character.isDigit(va1) || Character.isDigit(val) && !Character.isDigit(va1)) {
                        formulaCalc = Character.toString(val) + va1;
                        if (formulaCalc.contentEquals("09")) {
                            conceptEstruct.append(concepto9);
                        } else if (formulaCalc.contentEquals("10")) {
                            conceptEstruct.append(concepto10);
                        } else if (formulaCalc.contentEquals("11")) {
                            conceptEstruct.append(concepto11);
                        } else if (formulaCalc.contentEquals("12")) {
                            conceptEstruct.append(concepto12);
                        } else if (formulaCalc.contentEquals("13")) {
                            conceptEstruct.append(concepto13);
                        } else if (formulaCalc.contentEquals("14")) {
                            conceptEstruct.append(concepto14);
                        } else if (formulaCalc.contentEquals("15")) {
                            conceptEstruct.append(concepto15);
                        } else if (formulaCalc.contentEquals("16")) {
                            conceptEstruct.append(concepto16);
                        } else if (formulaCalc.contentEquals("17")) {
                            conceptEstruct.append(concepto17);
                        } else if (formulaCalc.contentEquals("18")) {
                            conceptEstruct.append(concepto18);
                        } else if (formulaCalc.contentEquals("19")) {
                            conceptEstruct.append(concepto19);
                        }
                    } else if (!Character.isDigit(val)) {
                        conceptEstruct.append(val);
                    }

                }
            }

        }
        System.out.println(idConcept);
        System.out.println(conceptEstruct.toString());
        try {

            result = engine.eval(conceptEstruct.toString());
            formulaCalc = String.valueOf(result);


        } catch (Exception e) {

        }

        return Math.round(Double.parseDouble(formulaCalc) * 100d) / 100d;
        // return 0.0;

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

                    nivel = initializeNivel(unidadObraView.getId());
                    addElementTableRV(unidadObraView.getId());
                }
            });
        }
        btnHide.setOnAction(event -> {


            paneCalc.setVisible(false);

        });


        tableRenglones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            manoobraList = FXCollections.observableArrayList();
            manoobraList = getManoObraRenglon(tableRenglones.getSelectionModel().getSelectedItem().getId());
            sal = calcSalRV(manoobraList);
            labelRVCant.setText(String.valueOf(Math.round(sal * 100d) / 100d));
            tableSuministros.setVisible(true);
            addElementTableSuministros();
            tableSuministros.getItems().setAll(manoobraList);
            labelRVum.setText(tableRenglones.getSelectionModel().getSelectedItem().getUm());
            //labelRVum.setText(tableRenglones.getSelectionModel().getSelectedItem().getUm());
            textDescrp.setText(tableRenglones.getSelectionModel().getSelectedItem().getDescripcion());

        });

        tableSuministros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            sal = getImporteEscala(tableSuministros.getSelectionModel().getSelectedItem().getEscala());
            labelRVCant.setText(String.valueOf(sal));
            labelRVum.setText("h/h");
            //labelRVum.setText(tableSuministros.getSelectionModel().getSelectedItem().getUm());
            textDescrp.setText(tableSuministros.getSelectionModel().getSelectedItem().getDescripcion());

        });
    }

    public void definirObra(ObrasView obra) {
        IdObra = obra.getId();
        System.out.println(IdObra);
        label_title.setText(obra.getCodigo() + " - " + obra.getDescripion());

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
                System.out.println(" ID emp " + empresaconstructoraModel.getId());

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
        datos = getNivelEspecificos(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());
        dataTable.getItems().setAll(datos);

    }

    public void handleCargardatos(ActionEvent event) {

        addElementosTable();
        if (dataTable.getItems() != null) {
            dataTable.setVisible(true);
        }
    }

    public void handleAgregarUO(ActionEvent event) {

        String[] codeEmp = coboEmpresas.getValue().split(" - ");
        String codeSub = comboSubEspecialidad.getValue().substring(0, 3);
        String codeObjeto = comboObjetos.getValue().substring(0, 2);
        String codeNivel = comboNivel.getValue().substring(0, 2);
        String codeZona = comboZonas.getValue().substring(0, 3);
        String codeEspecialidad = comboEspecialidad.getValue().substring(0, 2);

        empresaArrayList.forEach(empresaconstructora -> {
            if (empresaconstructora.getCodigo().contentEquals(codeEmp[0])) {
                empresaconstructoraModel = empresaconstructora;
                System.out.println("Emp C" + empresaconstructoraModel.getId());

            }
        });

        subespecialidadesArrayList.forEach(subespecialidades -> {
            if (subespecialidades.getCodigo().contentEquals(codeSub)) {
                subespecialidadesModel = subespecialidades;
                System.out.println("SE " + subespecialidadesModel.getId());

            }

        });

        objetosArrayList.forEach(objetos -> {
            if (objetos.getCodigo().contentEquals(codeObjeto)) {
                objetosModel = objetos;
                System.out.println("OBJ " + objetosModel.getId());
            }
        });

        nivelArrayList.forEach(nivel -> {
            if (nivel.getCodigo().contentEquals(codeNivel)) {
                nivelModel = nivel;
                System.out.println("NIV " + nivelModel.getId());
            }
        });

        zonasArrayList.forEach(zonas -> {
            if (zonas.getCodigo().contentEquals(codeZona)) {
                zonasModel = zonas;
                System.out.println("ZON " + zonasModel.getId());
            }
        });

        especialidadesArrayList.forEach(especialidades -> {
            if (especialidades.getCodigo().contentEquals(codeEspecialidad)) {
                especialidadesModel = especialidades;
                System.out.println("ESP " + especialidadesModel.getId());
            }
        });


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearNivelEspecifico.fxml"));
            Parent proot = loader.load();
            NuevoNivelEspecificoController controller = loader.getController();
            //controller.pasarParametros( IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                addElementosTable();
                if (dataTable.getItems().size() > 0) {
                    dataTable.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    public void handleDeleteUo(ActionEvent event) {
        Integer id = dataTable.getSelectionModel().getSelectedItem().getId();

        boolean mssg = deleteNivelEspecifico(id);

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

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToRenglonVarianteObra.fxml"));
            Parent proot = loader.load();
            RenVarianteToRenglonObraController controller = loader.getController();
            // controller.addUnidadObra(unidadObraView);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                tableRenglones.getItems().clear();
                addElementTableRV(unidadObraView.getId());

                costoMaterial = 0.0;
                costoMaterial = getCostoMaterial(unidadObraView.getId());
                System.out.println("C1 " + costoMaterial);

                costo = 0.0;
                costo = getUoCostoTotal(unidadObraView.getId());
                System.out.println("C1 " + costo);

                sumCostoTotal = 0.0;
                sumCostoTotal = costo + costoMaterial;
                System.out.println("C1 " + sumCostoTotal);


            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * para definir el renglon base y calcular los demas parametros
     *
     * @param event
     *
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
     */
    /**
     * para definir los suministros bajo especificacion
     * <p>
     * public void haddleBajoEspecificacion(ActionEvent event) {
     * unidadObraView = dataTable.getSelectionModel().getSelectedItem();
     * uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();
     * <p>
     * <p>
     * renglonvariante = getRenglonVariante(uorvTableView.getId());
     * if (renglonvariante.getCostomat() == 0) {
     * Alert alert = new Alert(Alert.AlertType.ERROR);
     * alert.setHeaderText("Error");
     * alert.setContentText("El renglon variante seleccionado no incurre en el costo material de la obra!");
     * alert.showAndWait();
     * <p>
     * } else {
     * <p>
     * try {
     * FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministroBajoEspecificacionRenglon.fxml"));
     * Parent proot = (Parent) loader.load();
     * BajoEspecificacionRenglonController controller = (BajoEspecificacionRenglonController) loader.getController();
     * controller.pasarUnidaddeObra(unidadObraView, uorvTableView);
     * <p>
     * <p>
     * Stage stage = new Stage();
     * stage.setScene(new Scene(proot));
     * stage.show();
     * <p>
     * <p>
     * stage.setOnCloseRequest(event1 -> {
     * addElementTableSuministros(unidadObraView.getId(), uorvTableView.getId());
     * <p>
     * <p>
     * costoMaterial = 0.0;
     * costoMaterial = getCostoMaterial(unidadObraView.getId());
     * System.out.println("C2 " + costoMaterial);
     * <p>
     * costo = 0.0;
     * costo = getUoCostoTotal(unidadObraView.getId());
     * System.out.println("C2 " + costo);
     * <p>
     * sumCostoTotal = 0.0;
     * sumCostoTotal = costo + costoMaterial;
     * System.out.println("C2 " + sumCostoTotal);
     * <p>
     * <p>
     * });
     * <p>
     * <p>
     * } catch (Exception ex) {
     * ex.printStackTrace();
     * }
     * }
     * }
     * <p>
     * public void haddleBajoEspecificacionCambiar(ActionEvent event) {
     * unidadObraView = dataTable.getSelectionModel().getSelectedItem();
     * uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();
     * bajoEspecificacionView = tableSuministros.getSelectionModel().getSelectedItem();
     * <p>
     * try {
     * FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministroBajoEspecificacionCambiar.fxml"));
     * Parent proot = (Parent) loader.load();
     * BajoEspecificacionRenglonCambiarController controller = (BajoEspecificacionRenglonCambiarController) loader.getController();
     * controller.pasarUnidaddeObra(unidadObraView, uorvTableView, bajoEspecificacionView);
     * <p>
     * <p>
     * Stage stage = new Stage();
     * stage.setScene(new Scene(proot));
     * stage.show();
     * <p>
     * <p>
     * stage.setOnCloseRequest(event1 -> {
     * <p>
     * <p>
     * addElementTableSuministros(unidadObraView.getId(), uorvTableView.getId());
     * costoMaterial = 0.0;
     * costoMaterial = getCostoMaterial(unidadObraView.getId());
     * System.out.println("C2 " + costoMaterial);
     * <p>
     * costo = 0.0;
     * costo = getUoCostoTotal(unidadObraView.getId());
     * System.out.println("C2 " + costo);
     * <p>
     * sumCostoTotal = 0.0;
     * sumCostoTotal = costo + costoMaterial;
     * System.out.println("C2 " + sumCostoTotal);
     * <p>
     * <p>
     * });
     * <p>
     * <p>
     * } catch (Exception ex) {
     * ex.printStackTrace();
     * }
     * }
     * <p>
     * public void haddleUpadteBajoEspecificacion(ActionEvent event) {
     * unidadObraView = dataTable.getSelectionModel().getSelectedItem();
     * bajoEspecificacionView = tableSuministros.getSelectionModel().getSelectedItem();
     * <p>
     * try {
     * FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSuministroBajoEspecificacion.fxml"));
     * Parent proot = (Parent) loader.load();
     * UpdateBajoEspecificacionController controller = (UpdateBajoEspecificacionController) loader.getController();
     * controller.pasarUnidaddeObra(unidadObraView, bajoEspecificacionView.getId());
     * <p>
     * <p>
     * Stage stage = new Stage();
     * stage.setScene(new Scene(proot));
     * stage.show();
     * <p>
     * <p>
     * stage.setOnCloseRequest(event1 -> {
     * <p>
     * <p>
     * addElementTableSuministros(unidadObraView.getId(), uorvTableView.getId());
     * costoMaterial = 0.0;
     * costoMaterial = getCostoMaterial(unidadObraView.getId());
     * <p>
     * <p>
     * costo = 0.0;
     * costo = getUoCostoTotal(unidadObraView.getId());
     * <p>
     * <p>
     * sumCostoTotal = costo + costoMaterial;
     * <p>
     * });
     * <p>
     * <p>
     * } catch (Exception ex) {
     * ex.printStackTrace();
     * }
     * }
     */

    public void handleActualizarRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToRenglonObraUpdate.fxml"));
            Parent proot = loader.load();
            RenVarianteToRenglonObraUpdateController controller = loader.getController();
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

                tableRenglones.getItems().clear();
                addElementTableRV(unidadObraView.getId());


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


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleDeleteRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();

        deleteNivelEspecificoRenglon(unidadObraView.getId(), uorvTableView.getId());

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

    }

    public void handleDeleteSumistro(ActionEvent event) {
        // bajoEspecificacionView = tableSuministros.getSelectionModel().getSelectedItem();
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        deleteBajoEspecificacionRV(bajoEspecificacionView.getId());


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Información");
        alert.setContentText("Suministro eliminado de la unidad de ObraPCW!");
        alert.showAndWait();


        // addElementTableSuministros(unidadObraView.getId());
        costoMaterial = 0.0;
        costoMaterial = getCostoMaterial(unidadObraView.getId());

        costo = 0.0;
        costo = getUoCostoTotal(unidadObraView.getId());

        sumCostoTotal = 0.0;
        sumCostoTotal = costo + costoMaterial;


    }

    public void handleMemoriaView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MemoriaDescriptivaRV.fxml"));
            Parent proot = loader.load();
            MemoriaDescriptivaRVController controller = loader.getController();
            controller.definirUOToMemoria(unidadObraView);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}





