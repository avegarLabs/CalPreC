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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class RenglonVarianteObraController implements Initializable {

    @FXML
    public Label label_title;
    @FXML
    public TableView<UniddaObraView> dataTable;
    @FXML
    public TableColumn<UniddaObraView, StringProperty> code;
    @FXML
    public TableColumn<UniddaObraView, StringProperty> descripcion;
    @FXML
    public TableView<UORVTableView> tableRenglones;
    @FXML
    public TableColumn<UORVTableView, String> RVcode;
    @FXML
    public TableColumn<UORVTableView, DoubleProperty> RVcant;
    @FXML
    public TableColumn<UORVTableView, DoubleProperty> RVCosto;
    @FXML
    public TableView<BajoEspecificacionView> tableSuministros;
    @FXML
    public TableColumn<BajoEspecificacionView, StringProperty> SumCode;
    @FXML
    public TableColumn<BajoEspecificacionView, DoubleProperty> SumCant;
    @FXML
    public TableColumn<BajoEspecificacionView, DoubleProperty> SumCost;
    public ArrayList<Empresagastos> empresagastosArrayList;
    public ArrayList<Empresaobraconcepto> empresaobraconceptoArrayList;
    public ObservableList<UORVTableView> uorvTableViewsList;
    RenglonVarianteObraController renglonVarianteObraController;
    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();
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
    private Obra obrasView;
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
    private Double costomatBe;
    private double costoMano;
    private double costoEquipo;
    private double salario;
    private double costMateNivel;
    private double costMaterialBajo;
    private double costo;
    private double costRV;
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
    private CreateLogFile createLogFile;
    private Obra obram;
    private ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    @FXML
    private ContextMenu nivMenu;
    @FXML
    private JFXButton btnadd;
    @FXML
    private ContextMenu rvMenu;
    @FXML
    private ContextMenu sumMenu;
    @FXML
    private JFXButton btnPlan;
    @FXML
    private JFXButton btncertf;
    @FXML
    private JFXButton btnAfc;
    @FXML
    private JFXButton btnpcw;
    @FXML
    private JFXButton btnimpCal;
    private int batchSize = 5;
    private int count;
    private UsuariosDAO usuariosDAO;
    private ObrasView viewObras;

    /**
     * Funcionalidades para llenar los elementos de la vista
     */

    public ObservableList<String> getListEmpresas(int Id) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresasList = FXCollections.observableArrayList();
            empresaArrayList = new ArrayList<Empresaconstructora>();
            empresaobraArrayList = (ArrayList<Empresaobra>) session.createQuery("FROM Empresaobra em where obraId = :id ").setParameter("id", Id).getResultList();
            for (Empresaobra empresaobra : empresaobraArrayList) {
                empresaArrayList.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId());
                var unit = empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().getNivelespecificosById().parallelStream().filter(u -> u.getObraId() == Id).findFirst().orElse(null);
                if (unit != null) {
                    empresasList.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim() + " ");
                } else {
                    empresasList.add(empresaobra.getEmpresaconstructoraByEmpresaconstructoraId().toString().trim());
                }
            }
            tx.commit();
            session.close();
            return empresasList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public ObservableList<String> getListZonas(Obra obra, int emp) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonaList = FXCollections.observableArrayList();
            zonasArrayList = (ArrayList<Zonas>) session.createQuery("FROM Zonas where obraId = :id ").setParameter("id", obra.getId()).getResultList();
            for (Zonas zon : zonasArrayList) {
                var zonUnit = zon.getNivelespecificosById().parallelStream().filter(u -> u.getObraId() == obra.getId() && u.getEmpresaconstructoraId() == emp).findFirst().orElse(null);
                if (zonUnit != null) {
                    zonaList.add(zon.toString().trim() + " ");
                } else {
                    zonaList.add(zon.toString().trim());
                }
            }
            tx.commit();
            session.close();
            return zonaList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return zonaList;
    }

    public List<CertificacionRenglonVariante> getListCertificacionRenglonVariantes(int idNivel, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<CertificacionRenglonVariante> recCertificacionRenglonVariantes = session.createQuery(" FROM CertificacionRenglonVariante WHERE nivelespecificoId =: idN AND renglonvarianteId =: idR").setParameter("idN", idNivel).setParameter("idR", idRV).getResultList();
            tx.commit();
            session.close();
            return recCertificacionRenglonVariantes;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public Double getUoCostoTotal(int idUO) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            costosRV = 0.0;
            CostoTotal = 0.0;
            sumCostoTotal = 0.0;
            List<Object[]> datosCalc = session.createQuery("SELECT SUM(cantidad * costmaterial), SUM(cantidad*costmano), SUM(cantidad*costequipo) FROM Renglonnivelespecifico where nivelespecificoId = :id ").setParameter("id", idUO).getResultList();
            for (Object[] row : datosCalc) {
                costosRV = Double.parseDouble(row[0].toString()) + Double.parseDouble(row[1].toString()) + Double.parseDouble(row[2].toString());
                CostoTotal = Math.round(costosRV * 100d) / 100d;
                // sumCostoTotal += CostoTotal;
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return CostoTotal;
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
            Query query = session.createQuery("SELECT SUM(costmaterial) FROM Renglonnivelespecifico where nivelespecificoId = :id ").setParameter("id", idUO);

            if (query.getResultList().size() > 0) {
                costoMatRV = (Double) query.getResultList().get(0);
            }

            costomatBe = (Double) session.createQuery(" SELECT SUM(costo) FROM Bajoespecificacionrv where nivelespecificoId = :id ").setParameter("id", idUO).getSingleResult();
            if (costomatBe == null) {
                costomatBe = 0.0;
            }

            costoMaterial = Math.round(costoMatRV + costomatBe * 100d) / 100d;
            tx.commit();
            session.close();
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

            costoMano = (Double) session.createQuery("SELECT SUM(costmano) FROM Renglonnivelespecifico where nivelespecificoId = :id ").setParameter("id", idUO).getSingleResult();

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return Math.round(costoMano * 100d) / 100d;
    }

    private ObservableList<String> getListObjetos(int idZona, int emp) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosList = FXCollections.observableArrayList();
            objetosArrayList = new ArrayList<>();
            objetosArrayList = (ArrayList<Objetos>) session.createQuery("FROM Objetos where zonasId = :id ").setParameter("id", idZona).getResultList();
            for (Objetos objetos : objetosArrayList) {
                var unitObj = objetos.getNivelespecificosById().parallelStream().filter(u -> u.getObraId() == obrasView.getId() && u.getEmpresaconstructoraId() == emp).findFirst().orElse(null);
                if (unitObj != null) {
                    objetosList.add(objetos.toString().trim() + "   ");
                } else {
                    objetosList.add(objetos.toString().trim());
                }
            }
            tx.commit();
            session.close();
            return objetosList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return FXCollections.emptyObservableList();

    }

    private ObservableList<String> getNivelList(int id, int emp, int zon) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelesList = FXCollections.observableArrayList();
            nivelArrayList = (ArrayList<Nivel>) session.createQuery("FROM Nivel where objetosId = :id ").setParameter("id", id).getResultList();
            for (Nivel nivel : nivelArrayList) {
                var nivUnit = nivel.getNivelespecificosById().parallelStream().filter(u -> u.getObraId() == obrasView.getId() && u.getEmpresaconstructoraId() == emp && u.getZonasId() == zon).findFirst().orElse(null);
                if (nivUnit != null) {
                    nivelesList.add(nivel.toString().trim() + "  ");
                } else {
                    nivelesList.add(nivel.toString().trim());
                }
            }
            tx.commit();
            session.close();
            return nivelesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return FXCollections.emptyObservableList();
    }

    private ObservableList<String> getEspecialidades(int emp, int zon, int obj, int niv) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesList = FXCollections.observableArrayList();
            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("from Especialidades ").getResultList();
            for (Especialidades especialiades : especialidadesArrayList) {
                var espUnit = especialiades.getNivelespecificosById().parallelStream().filter(u -> u.getObraId() == obrasView.getId() && u.getEmpresaconstructoraId() == emp && u.getZonasId() == zon && u.getObjetosId() == obj && u.getNivelId() == niv && u.getEspecialidadesId() == especialiades.getId()).findFirst().orElse(null);
                if (espUnit != null) {
                    especialidadesList.add(especialiades.toString().trim() + "  ");
                } else {
                    especialidadesList.add(especialiades.toString().trim());
                }
            }
            tx.commit();
            session.close();
            System.out.println(especialidadesList.size() + " === ");
            return especialidadesList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private List<Nivelespecifico> checkUnidadObra(List<Nivelespecifico> unidadobraList, int idOb, int idEmp, int idZon, int idObj, int idNiv) {
        return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idOb && unidadobra.getEmpresaconstructoraId() == idEmp && unidadobra.getZonasId() == idZon && unidadobra.getObjetosId() == idObj && unidadobra.getNivelId() == idNiv).collect(Collectors.toList());
    }

    private ObservableList<String> getListSubEspecialidad(int id, int empID, int zonID, int objID, int nivID) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subEspecialiadesList = FXCollections.observableArrayList();
            subespecialidadesArrayList = new ArrayList<>();
            subespecialidadesArrayList = (ArrayList<Subespecialidades>) session.createQuery("FROM Subespecialidades where especialidadesId = :id ").setParameter("id", id).getResultList();
            for (Subespecialidades subespecialidades : subespecialidadesArrayList) {
                if (checkUnidadObra(subespecialidades.getNivelespecificosById().stream().collect(Collectors.toList()), obrasView.getId(), empID, zonID, objID, nivID).size() > 0) {
                    subEspecialiadesList.add(subespecialidades.toString().trim().concat(" "));
                } else {
                    subEspecialiadesList.add(subespecialidades.toString().trim());
                }
            }

            tx.commit();
            session.close();
            return subEspecialiadesList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();

    }

    private ObservableList<UniddaObraView> getNivelEspecificos(int idObra, int idEmp, int idZona, int idObj, int idNiv, int idEsp, int idSub) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadObraViewObservableList = FXCollections.observableArrayList();
            nivelespecificoArrayList = (ArrayList<Nivelespecifico>) session.createQuery("FROM Nivelespecifico where obraId = :idObra and empresaconstructoraId =:idEmp AND zonasId = :idZona and objetosId =: idObjeto and nivelId =: idNivel and especialidadesId =: idEspecilidad and subespecialidadesId =:idSub").setParameter("idObra", idObra).setParameter("idEmp", idEmp).setParameter("idZona", idZona).setParameter("idObjeto", idObj).setParameter("idNivel", idNiv).setParameter("idEspecilidad", idEsp).setParameter("idSub", idSub).getResultList();
            for (Nivelespecifico nivel : nivelespecificoArrayList) {
                unidadObraViewObservableList.add(new UniddaObraView(nivel.getId(), nivel.getCodigo(), nivel.getDescripcion(), nivel.getObraId(), nivel.getEmpresaconstructoraId()));
            }
            tx.commit();
            session.close();
            return unidadObraViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return FXCollections.emptyObservableList();
    }

    public Nivelespecifico initializeNivel(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            nivel = session.get(Nivelespecifico.class, id);

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return nivel;
    }

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

            List<Object[]> datosList = session.createQuery("SELECT SUM(ur.costmaterial), SUM(ur.costmano), SUM(ur.costequipo), SUM(ur.salario) FROM Nivelespecifico uo INNER JOIN Renglonnivelespecifico ur ON uo.id = ur.nivelespecificoId WHERE uo.id =: unidad").setParameter("unidad", id).getResultList();
            for (Object[] row : datosList) {

                if (row[0] != null) {
                    //  System.out.println("materiales" + row[0].toString());
                    costoMatRV = Math.round(Double.parseDouble(row[0].toString()) * 100d) / 100d;
                }
                if (row[1] != null) {
                    //System.out.println(row[1].toString());
                    costoMano = Math.round(Double.parseDouble(row[1].toString()) * 100d) / 100d;
                }
                if (row[2] != null) {
                    //System.out.println(row[2].toString());
                    costoEquipo = Math.round(Double.parseDouble(row[2].toString()) * 100d) / 100d;
                }
                if (row[3] != null) {
                    //System.out.println(row[3].toString());
                    salario = Math.round(Double.parseDouble(row[3].toString()) * 100d) / 100d;
                }
            }

            costMaterialBajo = getCostoMaterialBajoEspecificacion(id);
            costoMaterial = costoMatRV + costMaterialBajo;

            Nivelespecifico nivelespecifico = session.get(Nivelespecifico.class, id);
            nivelespecifico.setCostomaterial(costoMaterial);
            nivelespecifico.setCostomano(costoMano);
            nivelespecifico.setCostoequipo(costoEquipo);
            nivelespecifico.setSalario(salario);

            session.update(nivelespecifico);
            falg = true;


            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return falg;

    }

    private double getCostoMaterialBajoEspecificacion(int id) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            costMaterialBajo = 0.0;
            Query query = session.createQuery("SELECT SUM(bajo.costo) FROM Bajoespecificacionrv bajo INNER JOIN Renglonnivelespecifico re ON bajo.nivelespecificoId = re.nivelespecificoId AND bajo.renglonvarianteId = re.renglonvarianteId WHERE bajo.nivelespecificoId = : idNivel").setParameter("idNivel", id);

            if (query.getResultList().get(0) != null) {
                costMaterialBajo = (Double) query.getResultList().get(0);
            } else {
                costMaterialBajo = 0.0;
            }

            tx.commit();
            session.close();
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
            Integer op122 = (Integer) session.createQuery("DELETE FROM Bajoespecificacionrv WHERE nivelespecificoId = : idU AND renglonvarianteId =: idR ").setParameter("idU", idUo).setParameter("idR", idRV).executeUpdate();
            Integer op12 = (Integer) session.createQuery("DELETE FROM Renglonnivelespecifico WHERE nivelespecificoId = : idU AND renglonvarianteId =: idR ").setParameter("idU", idUo).setParameter("idR", idRV).executeUpdate();

            trx.commit();
            session.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        session.close();
    }

    public ObservableList<UORVTableView> getNivelRVRelation(int idUO) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            uorvTableViewsList = FXCollections.observableArrayList();
            renglonnivelespecificoArrayList = (ArrayList<Renglonnivelespecifico>) session.createQuery("FROM Renglonnivelespecifico where nivelespecificoId = :id ").setParameter("id", idUO).getResultList();
            for (Renglonnivelespecifico renglonnivelespecifico : renglonnivelespecificoArrayList) {
                if (renglonnivelespecifico.getConmat().trim().equals("1")) {
                    double total = renglonnivelespecifico.getCostmaterial() + renglonnivelespecifico.getCostmano() + renglonnivelespecifico.getCostequipo();
                    uorvTableViewsList.add(new UORVTableView(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getId(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo().trim().concat("  "), new BigDecimal(String.format("%.4f", renglonnivelespecifico.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", total)).doubleValue(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getPeso(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getUm(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostomat() + renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostmano() + renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostequip(), "1", sal, 0.0, renglonnivelespecifico.getConmat(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getDescripcionsub()));
                } else if (renglonnivelespecifico.getConmat().trim().equals("0")) {
                    double total = renglonnivelespecifico.getCostmaterial() + renglonnivelespecifico.getCostmano() + renglonnivelespecifico.getCostequipo();
                    uorvTableViewsList.add(new UORVTableView(renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getId(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCodigo().trim(), new BigDecimal(String.format("%.4f", renglonnivelespecifico.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", total)).doubleValue(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getDescripcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getPeso(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getUm(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostmano() + renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getCostequip(), "1", sal, 0.0, renglonnivelespecifico.getConmat(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getSobregrupoBySobregrupoId().getDescipcion(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getGrupoByGrupoId().getDescripciong(), renglonnivelespecifico.getRenglonvarianteByRenglonvarianteId().getSubgrupoBySubgrupoId().getDescripcionsub()));

                }
            }
            uorvTableViewsList.sort(Comparator.comparing(UORVTableView::getCodeRV));
            tx.commit();
            session.close();
            return uorvTableViewsList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public boolean deleteNivelEspecifico(int id) {
        falg = false;
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Nivelespecifico unidadobra = session.get(Nivelespecifico.class, id);
            if (unidadobra != null) {

                int op1 = session.createQuery("DELETE FROM Renglonnivelespecifico WHERE nivelespecificoId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                int op2 = session.createQuery("DELETE FROM Bajoespecificacionrv WHERE nivelespecificoId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                int op3 = session.createQuery("DELETE FROM Planrenglonvariante WHERE nivelespecificoId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                int op4 = session.createQuery("DELETE FROM CertificacionRenglonVariante WHERE nivelespecificoId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                int op5 = session.createQuery("DELETE FROM Memoriadescriptivarv WHERE nivelespecificoId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                int op6 = session.createQuery("DELETE FROM Certificacionrecrv WHERE nivelespId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();
                int op7 = session.createQuery("DELETE FROM Planrecrv WHERE nivelespId =: idU").setParameter("idU", unidadobra.getId()).executeUpdate();

                session.delete(unidadobra);
                falg = true;
            }

            tx.commit();
            session.close();
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
        return falg;
    }

    private void deleteBajoEspecificacion(List<Bajoespecificacionrv> bajoespecificacions) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;

        try {
            trx = session.beginTransaction();

            count = 0;
            for (Bajoespecificacionrv unid : bajoespecificacions) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                session.delete(unid);
            }

            trx.commit();
            session.close();

        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        session.close();
    }

    public void handleLenaObjetosList(ActionEvent event) {
        zonasModel = zonasArrayList.parallelStream().filter(z -> z.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        comboObjetos.getSelectionModel().clearSelection();

        comboObjetos.setItems(getListObjetos(zonasModel.getId(), empresaconstructoraModel.getId()));
    }

    public void handleLlenarNivelList(ActionEvent event) {
        comboNivel.getSelectionModel().clearSelection();
        comboEspecialidad.getSelectionModel().clearSelection();
        comboSubEspecialidad.getSelectionModel().clearSelection();

        dataTable.getItems().clear();
        dataTable.setVisible(false);

        objetosModel = objetosArrayList.parallelStream().filter(o -> o.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        comboNivel.setItems(getNivelList(objetosModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId()));
    }

    public void handleLlenarEspecialidad(ActionEvent event) {
        comboEspecialidad.getSelectionModel().clearSelection();
        System.out.println(nivelArrayList.size() + " ******* ");

        dataTable.getItems().clear();
        dataTable.setVisible(false);
        nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
        System.out.println(nivelModel.getCodigo() + " ------ ");
        comboEspecialidad.setItems(getEspecialidades(empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
    }

    public void handlrLlenarSubsepecialidad(ActionEvent event) {
        comboSubEspecialidad.getSelectionModel().clearSelection();

        dataTable.getItems().clear();
        dataTable.setVisible(false);

        especialidadesModel = especialidadesArrayList.parallelStream().filter(e -> e.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);
        comboSubEspecialidad.setItems(getListSubEspecialidad(especialidadesModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
    }

    public void handleCrearZonas(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Zonas.fxml"));
            Parent proot = loader.load();
            ZonaController controller = loader.getController();
            controller.pasarZona(obrasView, empresaconstructoraModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboZonas.setItems(getListZonas(obrasView, empresaconstructoraModel.getId()));
                if (zonaList.size() > 0) {
                    comboZonas.getSelectionModel().select(zonaList.get(0));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleAgregarObjetos(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Objetos.fxml"));
            Parent proot = loader.load();
            ObjetosController controller = loader.getController();
            controller.pasarZona(zonasModel, empresaconstructoraModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                String[] codeEmpresa = coboEmpresas.getValue().split(" - ");

                comboNivel.getSelectionModel().clearSelection();
                comboEspecialidad.getSelectionModel().clearSelection();
                comboSubEspecialidad.getSelectionModel().clearSelection();
                comboObjetos.setItems(getListObjetos(zonasModel.getId(), empresaconstructoraModel.getId()));
                if (objetosList.size() > 0) {
                    comboObjetos.getSelectionModel().select(objetosList.get(0));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleAgregarNivel(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(empresaconstructora -> empresaconstructora.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Niveles.fxml"));
            Parent proot = loader.load();
            NivelesController controller = loader.getController();
            controller.pasarObjeto(objetosModel, empresaconstructoraModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboNivel.setItems(getNivelList(objetosModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId()));
                if (nivelesList.size() > 0) {
                    comboNivel.getSelectionModel().select(nivelesList.get(0));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleAgregarEspecialidad(ActionEvent event) {

        try {
            obram = structureSingelton.getObra(IdObra);
            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("Especialidades.fxml"));
            Parent proot = loader.load();

            EspacialidadesController controller = loader.getController();
            controller.pasarDatos(obram, empresaconstructoraModel, zonasModel, objetosModel, nivelModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboEspecialidad.setItems(getEspecialidades(empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
                comboEspecialidad.getSelectionModel().select(especialidadesList.get(0));
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleAgregarSubespecialidad(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Subespecialidades.fxml"));
            Parent proot = loader.load();
            SubespecialiadadesController controller = loader.getController();
            controller.pasarEspecialidad(especialidadesModel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                comboSubEspecialidad.setItems(getListSubEspecialidad(especialidadesModel.getId(), empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId()));
                comboSubEspecialidad.getSelectionModel().select(subEspecialiadesList.get(0));
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addElementTableRV(int idUO) {

        RVcode.setCellValueFactory(new PropertyValueFactory<UORVTableView, String>("codeRV"));
        RVcant.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("cant"));
        RVCosto.setCellValueFactory(new PropertyValueFactory<UORVTableView, DoubleProperty>("costoTotal"));
        datosRV = FXCollections.observableArrayList();
        datosRV = getNivelRVRelation(idUO);


        tableRenglones.getItems().clear();
        tableRenglones.getItems().addAll(datosRV);

        RVcode.setCellFactory(new Callback<TableColumn<UORVTableView, String>, TableCell<UORVTableView, String>>() {
            @Override
            public TableCell<UORVTableView, String> call(TableColumn<UORVTableView, String> param) {
                return new TableCell<UORVTableView, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            if (item.endsWith("  ")) {
                                this.setTextFill(Color.DARKRED);
                                setStyle("-fx-font-size:12");
                                setStyle("-fx-font-weight:900");
                                setText(item.toString());
                            } else {
                                setText(item.toString());
                            }
                        }
                    }
                };
            }
        });

    }

    public void addElementTableSuministros(int idUO, int idRV) {
        SumCode.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, StringProperty>("codigo"));
        SumCant.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("cantidadBe"));
        SumCost.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("costoBe"));
        tableSuministros.setItems(getSumBajoEspecificacion(idUO, idRV));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuariosDAO = UsuariosDAO.getInstance();

        TableView.TableViewSelectionModel selectionModel = dataTable.getSelectionModel();
        TableView.TableViewSelectionModel selectionModel2 = tableSuministros.getSelectionModel();
        TableView.TableViewSelectionModel selectionModel3 = tableRenglones.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel2.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel3.setSelectionMode(SelectionMode.MULTIPLE);

        if (usuariosDAO.usuario.getGruposId() == 6) {
            for (MenuItem item : nivMenu.getItems()) {
                item.setDisable(true);
            }
            for (MenuItem item : rvMenu.getItems()) {
                item.setDisable(true);
            }

            for (MenuItem item : sumMenu.getItems()) {
                item.setDisable(true);
            }

            addEspecialidad.setDisable(true);
            addZonas.setDisable(true);
            addObjetos.setDisable(true);
            addNivel.setDisable(true);
            addSubespecialidad.setDisable(true);
            btnadd.setDisable(true);
            btnAfc.setDisable(true);
            btncertf.setDisable(true);
            btnPlan.setDisable(true);
            btnpcw.setDisable(true);
            btnimpCal.setDisable(true);
        } else if (usuariosDAO.usuario.getGruposId() == 7) {
            for (MenuItem item : nivMenu.getItems()) {
                item.setDisable(true);
            }
            for (MenuItem item : rvMenu.getItems()) {
                item.setDisable(true);
            }

            for (MenuItem item : sumMenu.getItems()) {
                item.setDisable(true);
            }

            addEspecialidad.setDisable(true);
            addZonas.setDisable(true);
            addObjetos.setDisable(true);
            addNivel.setDisable(true);
            addSubespecialidad.setDisable(true);
            btnadd.setDisable(true);
            btnAfc.setDisable(true);
            btnpcw.setDisable(true);
            btnimpCal.setDisable(true);
        }

        renglonVarianteObraController = this;
        if (dataTable.getItems() != null) {
            dataTable.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    paneCalc.setVisible(true);
                    clearUp();
                    unidadObraView = dataTable.getSelectionModel().getSelectedItem();
                    nivel = initializeNivel(unidadObraView.getId());
                    tableRenglones.getItems().clear();
                    addElementTableRV(unidadObraView.getId());
                    tableSuministros.getItems().clear();
                }
            });
        }
        btnHide.setOnAction(event -> {
            if (unidadObraView != null) {
                falg = completeValuesNivelEspecifico(unidadObraView.getId());
                paneCalc.setVisible(false);
            } else {
                paneCalc.setVisible(false);
            }

        });


        tableRenglones.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();
                SumCode.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, StringProperty>("codigo"));
                SumCant.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("cantidadBe"));
                SumCost.setCellValueFactory(new PropertyValueFactory<BajoEspecificacionView, DoubleProperty>("costoBe"));

                addElementTableSuministros(unidadObraView.getId(), uorvTableView.getId());

                if (newSelection.getConMat().trim().equals("1")) {
                    labelTipoCost.setText("SI");
                    labelRVCant.setText(String.valueOf(Math.round(tableRenglones.getSelectionModel().getSelectedItem().getCostoTotal() / tableRenglones.getSelectionModel().getSelectedItem().getCant() * 100d) / 100d));
                    labelPeso.setText(String.valueOf(Math.round(tableRenglones.getSelectionModel().getSelectedItem().getPeso() * 100d) / 100d));
                    labelRVum.setText(tableRenglones.getSelectionModel().getSelectedItem().getUm());
                    textDescrp.setText(tableRenglones.getSelectionModel().getSelectedItem().getSobreG() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getGrupo() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getSubGrupo() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getDescripcion());
                } else if (newSelection.getConMat().trim().equals("0")) {
                    labelTipoCost.setText("NO");
                    labelRVCant.setText(String.valueOf(Math.round(tableRenglones.getSelectionModel().getSelectedItem().getCostoTotal() / tableRenglones.getSelectionModel().getSelectedItem().getCant() * 100d) / 100d));
                    labelPeso.setText(String.valueOf(Math.round(tableRenglones.getSelectionModel().getSelectedItem().getPeso() * 100d) / 100d));
                    labelRVum.setText(tableRenglones.getSelectionModel().getSelectedItem().getUm());
                    textDescrp.setText(tableRenglones.getSelectionModel().getSelectedItem().getSobreG() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getGrupo() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getSubGrupo() + "\n" + tableRenglones.getSelectionModel().getSelectedItem().getDescripcion());
                }


            }
        });

        try {
            tableSuministros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (tableSuministros.getSelectionModel().getSelectedItem() != null) {
                    labelRVCant.setText(String.valueOf(Math.round(tableSuministros.getSelectionModel().getSelectedItem().getPrecio() * 100d) / 100d));
                    labelPeso.setText(String.valueOf(Math.round(tableSuministros.getSelectionModel().getSelectedItem().getPeso() * 100d) / 100d));
                    labelRVum.setText(tableSuministros.getSelectionModel().getSelectedItem().getUm());
                    textDescrp.setText(tableSuministros.getSelectionModel().getSelectedItem().getDescripcion());
                }
            });
        } catch (Exception ex) {

        }

        coboEmpresas.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith(" ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboZonas.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith(" ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboObjetos.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("  ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboNivel.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("  ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboEspecialidad.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith("  ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }
                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        comboSubEspecialidad.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        final ListCell<String> cell = new ListCell<String>() {
                            {
                                // super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item,
                                                   boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    setText(item);
                                    if (item.endsWith(" ")) {
                                        setTextFill(Color.DARKRED);
                                        setStyle("-fx-font-size:14");
                                        setStyle("-fx-font-weight:900");
                                    } else {
                                        setTextFill(Color.BLACK);
                                    }

                                } else {
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }

                });

        tableRenglones.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ActionEvent event1 = new ActionEvent();
                handleActualizarRV(event1);
            }
        });

        tableSuministros.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ActionEvent event1 = new ActionEvent();
                haddleUpadteBajoEspecificacion(event1);
            }
        });

    }

    public void clearUp() {
        labelTipoCost.setText(" ");
        labelPeso.setText(" ");
        labelRVum.setText(" ");
        textDescrp.setText(" ");
    }

    public void handleSelectEmpresaAction(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        comboZonas.setItems(getListZonas(obrasView, empresaconstructoraModel.getId()));
    }

    public void definirObra(ObrasView obra) {
        viewObras = obra;
        IdObra = obra.getId();
        obrasView = structureSingelton.getObra(IdObra);

        label_title.setText(obra.getCodigo() + " - " + obra.getDescripion());
        coboEmpresas.setItems(getListEmpresas(IdObra));
    }

    public void handleCargardatos(ActionEvent event) {

        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
        objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
        especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

        code.setCellValueFactory(new PropertyValueFactory<UniddaObraView, StringProperty>("codigo"));
        descripcion.setCellValueFactory(new PropertyValueFactory<UniddaObraView, StringProperty>("descripcion"));


        datos = FXCollections.observableArrayList();
        datos = getNivelEspecificos(IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());
        dataTable.getItems().setAll(datos);

        if (dataTable.getItems() != null) {
            dataTable.setVisible(true);
        }
    }

    public void handleAgregarUO(ActionEvent event) {

        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
        objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
        especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearNivelEspecifico.fxml"));
            Parent proot = loader.load();
            NuevoNivelEspecificoController controller = loader.getController();
            controller.pasarParametros(renglonVarianteObraController, IdObra, empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void modificarNivel(ActionEvent event) {
        Nivelespecifico nivelespec = initializeNivel(dataTable.getSelectionModel().getSelectedItem().getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModificarNivelEspecifico.fxml"));
            Parent proot = loader.load();
            ModificarNivelEspecificoController controller = loader.getController();
            controller.pasarParametros(renglonVarianteObraController, nivelespec);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void handleDeleteUo(ActionEvent event) {
        ObservableList<UniddaObraView> list = FXCollections.observableArrayList();
        list = this.dataTable.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro de eliminar los niveles especificos seleccionados ");
        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(new ButtonType[]{buttonAgregar, buttonTypeCancel});
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonAgregar) {
            for (UniddaObraView uniddaObraView : list) {
                boolean mssg = deleteNivelEspecifico(uniddaObraView.getId());
                handleCargardatos(event);
                if (!mssg) {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("Información");
                    alert.setContentText("Error al eliminar el nivel especifico: " + uniddaObraView.getDescripcion());
                    alert.showAndWait();
                }
            }
        }
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
            controller.addUnidadObra(renglonVarianteObraController, nivel);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


            stage.setOnCloseRequest(event1 -> {
                calElementNivelEspecifico();
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodos para mostrar lis materiales suminitrados bajo especificacion a la unida de obra
     */

    public ObservableList<BajoEspecificacionView> getSumBajoEspecificacion(int idUO, int idRV) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            bajoEspecificacionViewObservableList = FXCollections.observableArrayList();
            bajoespecificacionrvArrayList = (ArrayList<Bajoespecificacionrv>) session.createQuery("FROM Bajoespecificacionrv where nivelespecificoId = :id  AND  renglonvarianteId =: idR").setParameter("id", idUO).setParameter("idR", idRV).getResultList();
            for (Bajoespecificacionrv bajoespecificacion : bajoespecificacionrvArrayList) {
                if (bajoespecificacion.getTipo().trim().equals("1")) {
                    Recursos recursos = session.get(Recursos.class, bajoespecificacion.getIdsuministro());
                    bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(bajoespecificacion.getId(), idUO, bajoespecificacion.getIdsuministro(), idRV, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), new BigDecimal(String.format("%.4f", bajoespecificacion.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", bajoespecificacion.getCosto())).doubleValue(), bajoespecificacion.getTipo()));
                } else if (bajoespecificacion.getTipo().trim().equals("J")) {
                    Juegoproducto recursos = session.get(Juegoproducto.class, bajoespecificacion.getIdsuministro());
                    bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(bajoespecificacion.getId(), idUO, bajoespecificacion.getIdsuministro(), idRV, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), new BigDecimal(String.format("%.4f", bajoespecificacion.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", bajoespecificacion.getCosto())).doubleValue(), bajoespecificacion.getTipo()));
                } else if (bajoespecificacion.getTipo().trim().equals("S")) {
                    Suministrossemielaborados recursos = session.get(Suministrossemielaborados.class, bajoespecificacion.getIdsuministro());
                    bajoEspecificacionViewObservableList.add(new BajoEspecificacionView(bajoespecificacion.getId(), idUO, bajoespecificacion.getIdsuministro(), idRV, recursos.getCodigo(), recursos.getDescripcion(), recursos.getPreciomn(), recursos.getUm(), recursos.getPeso(), new BigDecimal(String.format("%.4f", bajoespecificacion.getCantidad())).doubleValue(), new BigDecimal(String.format("%.2f", bajoespecificacion.getCosto())).doubleValue(), bajoespecificacion.getTipo()));
                }
            }
            bajoEspecificacionViewObservableList.sort(Comparator.comparing(BajoEspecificacionView::getCodigo));
            tx.commit();
            session.close();
            return bajoEspecificacionViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    /**
     * para definir los suministros bajo especificacion
     */
    public void haddleBajoEspecificacion(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();


        if (uorvTableView == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Debe seleccionar un renglón variante");
            alert.showAndWait();

        } else if (uorvTableView.getConMat().trim().equals("1")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("No puede agregar suministros bajo especificación en renglones variantes con el costo de material ");
            alert.showAndWait();
        } else if (uorvTableView.getConMat().trim().equals("0")) {

            if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Error");
                alert.setContentText("No se puede modificar suministros en RV con certificaciones");
                alert.showAndWait();
            } else if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() == 0) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("SuministroBajoEspecificacionRenglon.fxml"));
                    Parent proot = loader.load();
                    BajoEspecificacionRenglonController controller = loader.getController();
                    controller.pasarUnidaddeObra(renglonVarianteObraController, unidadObraView, uorvTableView);


                    Stage stage = new Stage();
                    stage.setScene(new Scene(proot));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.toFront();
                    stage.show();


                    stage.setOnCloseRequest(event1 -> {
                        calElementNivelEspecifico();
                    });


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void clearFields() {
        labelRVCant.setText(" ");
        labelPeso.setText(" ");
        labelRVum.setText(" ");
        textDescrp.setText(" ");
    }


    public void handleActualizarRV(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        uorvTableView = tableRenglones.getSelectionModel().getSelectedItem();
        if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("No se puede actualizar renglones con certificaciones");
            alert.showAndWait();
        } else if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() == 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RenglonVToRenglonObraUpdate.fxml"));
                Parent proot = loader.load();
                RenVarianteToRenglonObraUpdateController controller = loader.getController();
                controller.addUpdateRvToUnidadO(renglonVarianteObraController, nivel, uorvTableView.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handleDeleteRV(ActionEvent event) {
        ObservableList<UORVTableView> list = FXCollections.observableArrayList();
        ObservableList<UORVTableView> okListtoRemove = FXCollections.observableArrayList();
        list = this.tableRenglones.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(" Esta seguro de eliminar los renglones variantes seleccionados ");
        ButtonType buttonAgregar = new ButtonType("Aceptar");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(new ButtonType[]{buttonAgregar, buttonTypeCancel});
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonAgregar) {
            for (UORVTableView tableView : list) {
                if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableView.getId()).size() > 0) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error");
                    alert.setContentText("No se puede eliminar renglones con certificaciones");
                    alert.showAndWait();
                } else if (getListCertificacionRenglonVariantes(nivel.getNivelId(), tableView.getId()).size() == 0) {
                    okListtoRemove.add(tableView);
                }
            }
            if (okListtoRemove.size() > 0) {
                for (UORVTableView tableView : okListtoRemove) {
                    deleteNivelEspecificoRenglon(dataTable.getSelectionModel().getSelectedItem().getId(), tableView.getId());
                    this.tableRenglones.getItems().removeAll(okListtoRemove);
                }
                labelRVCant.setText(" ");
                labelPeso.setText(" ");
                labelRVum.setText(" ");
                textDescrp.setText(" ");
                costoMaterial = getCostoMaterial(unidadObraView.getId());
                costo = getUoCostoTotal(unidadObraView.getId());
                sumCostoTotal = costo + costoMaterial;
                addElementTableRV(unidadObraView.getId());
                calElementNivelEspecifico();
            }
        }
    }

    private Bajoespecificacionrv getBajoEspecificacion(int id) {
        return bajoespecificacionrvArrayList.parallelStream().filter(bajoespecificacion -> bajoespecificacion.getId() == id).findFirst().orElse(null);
    }

    public void haddleUpadteBajoEspecificacion(ActionEvent event) {
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        bajoEspecificacionView = tableSuministros.getSelectionModel().getSelectedItem();

        if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Error");
            alert.setContentText("No se puede modificar suministros en RV con certificaciones");
            alert.showAndWait();
        } else if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() == 0) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateSuministroBajoEspecificacionRenglon.fxml"));
                Parent proot = loader.load();
                UpdateBajoEspecificacionRenglonController controller = loader.getController();
                controller.pasarUnidaddeObra(renglonVarianteObraController, unidadObraView, bajoEspecificacionView.getId());


                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

                stage.setOnCloseRequest(event1 -> {
                    calElementNivelEspecifico();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handleMemoriaView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MemoriaDescriptivaRV.fxml"));
            Parent proot = loader.load();
            MemoriaDescriptivaRVController controller = loader.getController();
            controller.definirUOToMemoria(unidadObraView);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void handleNewCertificacion(ActionEvent event) {
        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
        objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
        especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CertificarRenglonVariantes.fxml"));
            Parent proot = loader.load();
            CertificarRenglonVarianteController controller = (CertificarRenglonVarianteController) loader.getController();
            controller.cargarUnidadeObra(unidadObraView.getId(), empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId(), IdObra);


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleNewPlan(ActionEvent event) {

        empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
        zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
        subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
        objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
        nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
        especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlanificarRenglonVariantes.fxml"));
            Parent proot = loader.load();
            PlanificarRenglonVarianteController controller = loader.getController();
            controller.cargarUnidadeObra(unidadObraView.getId(), empresaconstructoraModel.getId(), zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void calElementNivelEspecifico() {
        completeValuesNivelEspecifico(unidadObraView.getId());
        Thread t1 = new CalForRenglonVariante(renglonVarianteObraController, empresaconstructoraModel, obrasView);
        t1.start();
    }


    public void handleActionImport(ActionEvent event) {

        if (coboEmpresas.getValue() != null && comboSubEspecialidad.getValue() != null && comboObjetos.getValue() != null && comboNivel.getValue() != null && comboZonas.getValue() != null && comboEspecialidad.getValue() != null) {

            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
            especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarRVtoPCwin.fxml"));
                Parent proot = loader.load();

                ImportarRVtoPCwinController controller = loader.getController();
                controller.cargarDatos(renglonVarianteObraController, IdObra, empresaconstructoraModel, zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Información");
            alert.setContentText("Complete la estructura de la obra");
            alert.showAndWait();
        }
    }

    public void handleActionImportInCalPreC(ActionEvent event) {

        if (coboEmpresas.getValue() != null && comboSubEspecialidad.getValue() != null && comboObjetos.getValue() != null && comboNivel.getValue() != null && comboZonas.getValue() != null && comboEspecialidad.getValue() != null) {

            empresaconstructoraModel = empresaArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(coboEmpresas.getValue().trim())).findFirst().orElse(null);
            zonasModel = zonasArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboZonas.getValue().trim())).findFirst().orElse(null);
            subespecialidadesModel = subespecialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboSubEspecialidad.getValue().trim())).findFirst().orElse(null);
            objetosModel = objetosArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboObjetos.getValue().trim())).findFirst().orElse(null);
            nivelModel = nivelArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboNivel.getValue().trim())).findFirst().orElse(null);
            especialidadesModel = especialidadesArrayList.parallelStream().filter(emp -> emp.toString().trim().equals(comboEspecialidad.getValue().trim())).findFirst().orElse(null);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportarNIVInCalperC.fxml"));
                Parent proot = loader.load();

                ImportarNIVInCalPreCController controller = loader.getController();
                controller.cargarDatos(renglonVarianteObraController, obrasView, empresaconstructoraModel, zonasModel.getId(), objetosModel.getId(), nivelModel.getId(), especialidadesModel.getId(), subespecialidadesModel.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(proot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.toFront();
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Información");
            alert.setContentText("Complete la estructura de la obra");
            alert.showAndWait();
        }
    }

    public void actionHandleDuplica(ActionEvent event) {

        nivel = initializeNivel(dataTable.getSelectionModel().getSelectedItem().getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DuplicarNivelEspecifico.fxml"));
            Parent proot = loader.load();
            DuplicarNivelEspecificoController controller = loader.getController();
            controller.llenaLabels(nivel);

            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.toFront();
            stage.show();

            stage.setOnCloseRequest(event1 -> {
                handleCargardatos(event);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void keyEventRefresh(KeyEvent event) {
        if (event.getCode() == KeyCode.F5) {
            definirObra(viewObras);
            comboZonas.getSelectionModel().clearSelection();
            comboObjetos.getSelectionModel().clearSelection();
            comboNivel.getSelectionModel().clearSelection();
            comboEspecialidad.getSelectionModel().clearSelection();
            comboSubEspecialidad.getSelectionModel().clearSelection();
            dataTable.setVisible(false);
        }
    }

    public void handleCargarHojaCalculo(ActionEvent event) {
        Renglonvariante renglonvariante = renglonnivelespecificoArrayList.parallelStream().filter(item -> item.getRenglonvarianteByRenglonvarianteId().getId() == tableRenglones.getSelectionModel().getSelectedItem().getId()).findFirst().map(item -> item.getRenglonvarianteByRenglonvarianteId()).get();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HojadeCalculo.fxml"));
            Parent proot = loader.load();
            HojadeCalcauloController controller = loader.getController();
            controller.LlenarDatosRV(renglonvariante, obrasView, empresaconstructoraModel.getId());


            Stage stage = new Stage();
            stage.setScene(new Scene(proot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleDeleteSumistro(ActionEvent event) {
        ObservableList<BajoEspecificacionView> list = FXCollections.observableArrayList();
        list = tableSuministros.getSelectionModel().getSelectedItems();
        unidadObraView = dataTable.getSelectionModel().getSelectedItem();
        if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Error");
            alert.setContentText("No se puede eliminar suministros en RV con certificaciones");
            alert.showAndWait();
        } else if (getListCertificacionRenglonVariantes(dataTable.getSelectionModel().getSelectedItem().getId(), tableRenglones.getSelectionModel().getSelectedItem().getId()).size() == 0) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" Esta seguro de eliminar los suministros seleccionados ");
            ButtonType buttonAgregar = new ButtonType("Aceptar");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(new ButtonType[]{buttonAgregar, buttonTypeCancel});
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonAgregar) {
                List<Bajoespecificacionrv> bajoespecificacions = new ArrayList<>();
                for (BajoEspecificacionView especificacionView : list) {
                    bajoespecificacions.add(getBajoEspecificacion(especificacionView.getId()));
                }
                deleteBajoEspecificacion(bajoespecificacions);
            }
            calElementNivelEspecifico();
            costoMaterial = getCostoMaterial(unidadObraView.getId());
            costo = getUoCostoTotal(unidadObraView.getId());
            sumCostoTotal = costo + costoMaterial;
            addElementTableSuministros(unidadObraView.getId(), uorvTableView.getId());
            labelRVCant.setText(" ");
            labelPeso.setText(" ");
            labelRVum.setText(" ");
            textDescrp.setText(" ");
        }
    }

    public void handleDesglozarAction(ActionEvent event) {
        var recurso = tableSuministros.getSelectionModel().getSelectedItem();
        System.out.println(recurso.getIdRV());
        if (recurso.getTipo().trim().equals("S") || recurso.getTipo().trim().equals("J")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" Esta seguro de desglozar el recurso:  " + recurso.getCodigo() + ". Esta opción agregará a la unidad de obra como suministros bajo especificación los insumos que componen el recurso selccionado, tenga cuidado porque se obvian la mano de obra y los equipos ");
            ButtonType buttonAgregar = new ButtonType("Aceptar");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonAgregar, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonAgregar) {
                if (recurso.getTipo().trim().equals("S")) {
                    var semielaborado = utilCalcSingelton.getSuministroSemielaboradoById(recurso.getIdSum());
                    createListOfComponentesInSemielaborado(semielaborado.getSemielaboradosrecursosById().parallelStream().filter(item -> item.getRecursosByRecursosId().getTipo().trim().equals("1")).collect(Collectors.toList()), recurso);

                } else if (recurso.getTipo().trim().equals("J")) {
                    var juego = utilCalcSingelton.getJuegoProductoById(recurso.getIdSum());
                    createListOfComponentesInJuego(juego.getJuegorecursosById(), recurso);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setContentText("No se permite desglozar insumos");
            alert.showAndWait();
        }
    }

    private void createListOfComponentesInJuego(Collection<Juegorecursos> juegorecursosById, BajoEspecificacionView recurso) {
        int idUO = recurso.getIdUo();
        int idR = recurso.getIdRV();
        List<Bajoespecificacionrv> listOfRecursosDelete = new ArrayList<>();
        listOfRecursosDelete.add(getBajoEspecificacion(recurso.getId()));
        for (Juegorecursos semielaboradosrecursos : juegorecursosById) {
            Bajoespecificacionrv bajo = new Bajoespecificacionrv();
            bajo.setNivelespecificoId(idUO);
            bajo.setIdsuministro(semielaboradosrecursos.getRecursosId());
            bajo.setRenglonvarianteId(idR);
            double cant = recurso.getCantidadBe() * semielaboradosrecursos.getCantidad();
            bajo.setCantidad(cant);
            bajo.setCosto(cant * semielaboradosrecursos.getRecursosByRecursosId().getPreciomn());
            bajo.setTipo(semielaboradosrecursos.getRecursosByRecursosId().getTipo().trim());
            utilCalcSingelton.addBajoEspecificacionRV(bajo);
        }
        deleteBajoEspecificacion(listOfRecursosDelete);
        addElementTableSuministros(idUO, idR);

    }


    private void createListOfComponentesInSemielaborado(List<Semielaboradosrecursos> collect, BajoEspecificacionView recurso) {
        System.out.println("IdRV: " + recurso.getIdRV());
        int idUO = recurso.getIdUo();
        int idR = recurso.getIdRV();
        List<Bajoespecificacionrv> listOfRecursosDelete = new ArrayList<>();
        listOfRecursosDelete.add(getBajoEspecificacion(recurso.getId()));
        for (Semielaboradosrecursos semielaboradosrecursos : collect) {
            Bajoespecificacionrv bajo = new Bajoespecificacionrv();
            bajo.setNivelespecificoId(idUO);
            bajo.setIdsuministro(semielaboradosrecursos.getRecursosId());
            bajo.setRenglonvarianteId(idR);
            double cant = recurso.getCantidadBe() * semielaboradosrecursos.getCantidad();
            bajo.setCantidad(cant);
            bajo.setCosto(cant * semielaboradosrecursos.getRecursosByRecursosId().getPreciomn());
            bajo.setTipo(semielaboradosrecursos.getRecursosByRecursosId().getTipo().trim());
            utilCalcSingelton.addBajoEspecificacionRV(bajo);
        }
        deleteBajoEspecificacion(listOfRecursosDelete);
        addElementTableSuministros(idUO, idR);
    }


}
