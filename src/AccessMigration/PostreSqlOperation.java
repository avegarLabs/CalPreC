package AccessMigration;

import AccessMigration.model.*;
import javafx.scene.control.Alert;
import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import views.CertificaRecursosRenglonesThread;
import views.PlanificarRecursosRenglonesThread;

import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostreSqlOperation {

    private static PostreSqlOperation instancia = null;
    public ArrayList<Renglonvariante> renglonvarianteArrayList;
    public List<Renglonnivelespecifico> nivelespecificoList;
    List<Renglonnivelespecifico> renglonnivelespecificoList;
    private Tipoobra tipoobra;
    private Zonas zonas;
    private Integer idObjeto;
    private Especialidades especialidades;
    private Nivel nivel;
    private Subespecialidades subespecialidades;
    private ArrayList<Empresaconstructora> empresaconstructoraArrayList;
    private ArrayList<Zonas> zonasArrayList;
    private ArrayList<Objetos> objetosArrayList;
    private ArrayList<Nivel> nivelArrayList;
    private ArrayList<Especialidades> especialidadesArrayList;
    private ArrayList<Subespecialidades> subespecialidadesArrayList;
    private Integer idOb;
    private int batchSize = 5;
    private int count;
    private int batchSizeOb = 5;
    private int countOb;
    private int batchSizeNiv = 10;
    private int countNiv;
    private EstructUOCalP uoCalPList;
    private Map<Especialidades, List<Subespecialidades>> especialidadesListMap;
    private int batchSub = 10;
    private int countsub;
    private int batchSizeEO = 5;
    private int countEO;
    private int batchSizeSal = 5;
    private int countSal;
    private ArrayList<Unidadobra> unidadobraList;
    private double costTotal;
    private double costUnitario;
    private ArrayList<Nivelespecifico> nivelespecificoArrayList;
    private int batchSizeUO = 500;
    private int countUO;
    private int batchSizeES = 50;
    private int countES;
    private List<Unidadobrarenglon> unidadobrarenglonArrayList;
    private int batchSizeUOR = 50;
    private int countUOR;
    private int batchSizeNER = 50;
    private int countNER;
    private int batchSizeRec = 200;
    private int countrec;
    private int idSub;
    private Integer idReng;
    private Recursos rec;
    private Suministrossemielaborados recSuministrossemielaborados;
    private Juegoproducto juegoproducto;
    private ArrayList<Unidadobra> unidadobraArrayList;
    private ArrayList<Nivelespecifico> nivelespecificoArray;
    private ArrayList<CalPrecSuminist> calPrecSuministArrayList;
    private List<Recursos> recursosList;
    private int batchSizeSE = 200;
    private int countsem;
    private Integer idRc;
    private ArrayList<CompoSemiModel> compoSemiModels;
    private Integer idInsumo;
    private List<Semielaboradosrecursos> semielaboradosrecursosList;
    private double total;
    private int batchrs = 5;
    private int countrs;
    private int batchbajo = 500;
    private int countbajo;
    private int batchbajorv = 50;
    private int countbajorv;
    private certificaRenglonSingelton renglonSingelton;
    private planificaRenglonSingleton planificaRenglonSingleton;

    private PostreSqlOperation() {


    }

    public static PostreSqlOperation getInstance() {

        if (instancia == null) {
            instancia = new PostreSqlOperation();
        }

        return instancia;
    }

    public Tipoobra getTipoobra(String code) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            tipoobra = (Tipoobra) session.createQuery(" FROM Tipoobra WHERE codigo =: cod").setParameter("cod", code).getSingleResult();

            tx.commit();
            session.close();
            return tipoobra;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Tipoobra();
    }

    public ArrayList<Zonas> getZonasArrayList() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            zonasArrayList = new ArrayList<>();
            zonasArrayList = (ArrayList<Zonas>) session.createQuery("FROM Zonas ").getResultList();


            tx.commit();
            session.close();
            return zonasArrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<Objetos> getObjetosArrayList() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            objetosArrayList = new ArrayList<>();
            objetosArrayList = (ArrayList<Objetos>) session.createQuery(" FROM Objetos ").getResultList();


            tx.commit();
            session.close();
            return objetosArrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<Especialidades> getEspecialidadesArrayList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidadesArrayList = new ArrayList<>();
            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("FROM Especialidades ").getResultList();

            tx.commit();
            session.close();
            return especialidadesArrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<Subespecialidades> getSubespecialidades() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            subespecialidadesArrayList = new ArrayList<>();
            subespecialidadesArrayList = (ArrayList<Subespecialidades>) session.createQuery("FROM Subespecialidades ").getResultList();

            tx.commit();
            session.close();
            return subespecialidadesArrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<Nivel> getNivelArrayList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            nivelArrayList = new ArrayList<>();
            nivelArrayList = (ArrayList<Nivel>) session.createQuery("FROM Nivel").getResultList();

            tx.commit();
            session.close();
            return nivelArrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public Integer createObra(ObraPCW obraPCW, int entId, int invId, int salId) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            idOb = null;

            Obra obra = new Obra();
            obra.setCodigo(obraPCW.getCode());
            obra.setDescripion(obraPCW.getDescrip());
            obra.setTipo(obraPCW.getClasif());
            obra.setTipoobraId(getTipoobra(obraPCW.getTipo()).getId());
            obra.setEntidadId(entId);
            obra.setInversionistaId(invId);
            obra.setSalarioId(salId);
            obra.setDefcostmat(0);

            idOb = (Integer) session.save(obra);

            tx.commit();
            session.close();
            return idOb;


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear la obra en PostgreSQL");
            alert.showAndWait();
        }
        return idOb;
    }

    public void createZonas(int idObra, List<ZonasPCW> zonasPCWArrayList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = 0;
            for (ZonasPCW zon : zonasPCWArrayList) {
                count++;
                if (count > 0 && count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }

                Zonas zonas = new Zonas();
                zonas.setCodigo(zon.getCode());
                zonas.setDesripcion(zon.getDescrip());
                zonas.setObraId(idObra);

                session.persist(zonas);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear las zonas en PostgreSQL");
            alert.showAndWait();
        }

    }

    public void createObjetos(int idObr, List<ObjetosPCW> objetosPCWList) {

        zonasArrayList = new ArrayList<>();
        zonasArrayList = getZonasArrayList();

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countOb = 0;
            for (ObjetosPCW obj : objetosPCWList) {
                countOb++;
                if (countOb > 0 && countOb % batchSizeOb == 0) {
                    session.flush();
                    session.clear();
                }

                Objetos objetos = new Objetos();
                objetos.setCodigo(obj.getCode());
                objetos.setDescripcion(obj.getDescrip());
                zonas = zonasArrayList.parallelStream().filter(z -> z.getCodigo().equals(obj.getZona()) && z.getObraId() == idOb).findFirst().orElse(null);
                objetos.setZonasId(zonas.getId());
                session.persist(objetos);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Objetos en PostgreSQL");
            alert.showAndWait();
        }

    }

    public void createNiveles(Integer idOb, List<NivelPCW> nivelPCWList) {

        objetosArrayList = new ArrayList<>();
        objetosArrayList = getObjetosArrayList();
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countNiv = 0;
            for (NivelPCW niv : nivelPCWList) {
                countNiv++;
                if (countNiv > 0 && countNiv % batchSizeNiv == 0) {
                    session.flush();
                    session.clear();
                }

                Nivel nivel = new Nivel();
                nivel.setCodigo(niv.getCode());
                nivel.setDescripcion(niv.getDescrip());
                Objetos objetos = objetosArrayList.parallelStream().filter(ob -> ob.getCodigo().equals(niv.getObj()) && ob.getZonasByZonasId().getCodigo().equals(niv.getZona()) && ob.getZonasByZonasId().getObraId() == idOb).findFirst().orElse(null);
                nivel.setObjetosId(objetos.getId());

                session.persist(nivel);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Niveles en PostgreSQL");
            alert.showAndWait();
        }

    }

    public EstructUOCalP getEstructUOCalPS(EstructuraPCW est) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Object[]> datoEstruct = session.createQuery("SELECT zon.id, zon.codigo, obj.id, obj.codigo, niv.id, niv.codigo FROM Zonas zon LEFT JOIN Objetos obj ON zon.id = obj.zonasId AND obj.codigo =: codeOb LEFT JOIN Nivel niv ON obj.id = niv.objetosId AND niv.codigo =: codeNi WHERE zon.codigo =: codeZ").setParameter("codeZ", est.getZona()).setParameter("codeOb", est.getObjeto()).setParameter("codeNi", est.getNivel()).getResultList();
            for (Object[] row : datoEstruct) {
                uoCalPList = new EstructUOCalP(Integer.parseInt(row[0].toString()), row[1].toString(), Integer.parseInt(row[2].toString()), row[3].toString(), Integer.parseInt(row[4].toString()), row[5].toString());
            }
            tx.commit();
            session.close();
            return uoCalPList;


        } catch (Exception ex) {
            ex.printStackTrace();
            /*
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al identificar la estructura en el CalPreC");
            alert.showAndWait();
            */

        }

        return uoCalPList;
    }

    public Map<Especialidades, List<Subespecialidades>> getEstructUOCalPSEtoS() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidadesArrayList = new ArrayList<>();
            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("FROM Especialidades ").getResultList();
            especialidadesListMap = new HashMap<>();
            for (Especialidades especialidades : especialidadesArrayList) {
                Especialidades esp = (Especialidades) session.createQuery("FROM Especialidades WHERE id =: idE").setParameter("idE", especialidades.getId()).getSingleResult();
                esp.getSubespecialidadesById().size();
                especialidadesListMap.put(esp, (List<Subespecialidades>) esp.getSubespecialidadesById());
            }

            tx.commit();
            session.close();
            return especialidadesListMap;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new HashMap<>();
    }

    public ArrayList<Especialidades> getEspecialidades() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidadesArrayList = new ArrayList<>();
            especialidadesArrayList = (ArrayList<Especialidades>) session.createQuery("FROM Especialidades ").getResultList();

            tx.commit();
            session.close();
            return especialidadesArrayList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void createSubespecialidadList(List<Subespecialidades> subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countsub = 0;
            for (Subespecialidades sub : subespecialidadesList) {
                countsub++;
                if (countsub > 0 && countsub % batchSub == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(sub);

            }

            tx.commit();
            session.close();
        } catch (Exception e) {

        }
    }

    public void createSubespecialidad(Subespecialidades subespecialidadesList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            session.persist(subespecialidadesList);

            tx.commit();
            session.close();

        } catch (Exception e) {

        }
    }

    public Integer createEspecialidad(Especialidades especialidades) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idEsp = null;
        try {
            tx = session.beginTransaction();

            idEsp = (Integer) session.save(especialidades);

            tx.commit();
            session.close();
            return idEsp;

        } catch (Exception e) {

        }

        return idEsp;

    }

    public void createEmpresaObra(List<Empresaobra> empresaobraList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countEO = 0;
            for (Empresaobra eo : empresaobraList) {
                countEO++;

                if (countEO > 0 && countEO % batchSizeEO == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }

            tx.commit();
            session.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void createEmpresaObraSalario(List<Empresaobrasalario> empresaobrasalariosList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countSal = 0;
            for (Empresaobrasalario eos : empresaobrasalariosList) {
                countSal++;

                if (countSal > 0 && countSal % batchSizeSal == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eos);
            }

            tx.commit();
            session.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public Especialidades getEspecialidad(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            especialidades = new Especialidades();
            especialidades = (Especialidades) session.createQuery("FROM Especialidades WHERE codigo=: cod").setParameter("cod", code).getSingleResult();

            tx.commit();
            session.close();
            return especialidades;
        } catch (Exception e) {

        }

        return new Especialidades();
    }

    ReportProjectStructureSingelton reportProjectStructureSingelton = ReportProjectStructureSingelton.getInstance();

    public int getIdEspecialidad(String codeEsp) {
        return especialidadesArrayList.parallelStream().filter(esp -> esp.getCodigo().equals(codeEsp.trim())).findFirst().map(Especialidades::getId).orElse(0);
    }

    public int getIdSub(String codeEsp, String codesub) {
        return subespecialidadesArrayList.parallelStream().filter(subespecialidades1 -> subespecialidades1.getEspecialidadesByEspecialidadesId().getCodigo().equals(codeEsp.trim()) && subespecialidades1.getCodigo().trim().endsWith(codesub.trim().substring(1))).map(Subespecialidades::getId).findFirst().orElse(0);
    }

    UtilCalcSingelton utilCalcSingelton = UtilCalcSingelton.getInstance();

    public ArrayList<Unidadobra> getUnidadobraList(Integer idO, CalPrecIdComponentes componentes, List<UnidadesObraPCW> unidadesObraPCWList) {
        Obra obra = reportProjectStructureSingelton.getObra(idO);
        unidadobraList = new ArrayList<>();
        for (UnidadesObraPCW uo : unidadesObraPCWList) {
            costTotal = 0.0;
            costUnitario = 0.0;
            Unidadobra unidadobra = new Unidadobra();
            //Estructura de la UO
            unidadobra.setObraId(obra.getId());
            unidadobra.setEmpresaconstructoraId(componentes.getEmpId());
            unidadobra.setZonasId(componentes.getZonaId());
            unidadobra.setObjetosId(componentes.getObjetoId());
            unidadobra.setNivelId(componentes.getNivId());
            unidadobra.setEspecialidadesId(componentes.getEspId());
            unidadobra.setSubespecialidadesId(componentes.getSubId());

            //Valores UO
            unidadobra.setCodigo(uo.getCode());
            unidadobra.setDescripcion(uo.getDescrip());
            unidadobra.setUm(uo.getUm());
            unidadobra.setRenglonbase(uo.getRb());
            unidadobra.setCantidad(uo.getCant());
            unidadobra.setCostoMaterial(uo.getCostMaterial());
            unidadobra.setCostomano(uo.getCostMano());
            unidadobra.setCostoequipo(uo.getCostEquipo());
            unidadobra.setSalario(uo.getSalario());
            unidadobra.setSalariocuc(uo.getSalarioCUC());
            costTotal = uo.getCostMaterial() + uo.getCostMano() + uo.getCostEquipo();
            costUnitario = costTotal / uo.getCant();
            unidadobra.setCostototal(costTotal);
            unidadobra.setCostounitario(costUnitario);
            unidadobra.setGrupoejecucionId(1);
            unidadobra.setIdResolucion(obra.getSalarioId());

            unidadobraList.add(unidadobra);
        }

        return unidadobraList;
    }

    public ArrayList<Unidadobra> getUnidadobraListImpor(Integer idO, int idEmpresa, int idZona, int idObjetos, int idNivel, int idEspecialidad, int idSubespecialidad, ArrayList<UnidadObraPCW> unidadesObraPCWList) {
        unidadobraList = new ArrayList<>();
        Obra obra = reportProjectStructureSingelton.getObra(idO);
        for (UnidadObraPCW uo : unidadesObraPCWList) {
            costTotal = 0.0;
            costUnitario = 0.0;
            Unidadobra unidadobra = new Unidadobra();
            //Estructura de la UO
            unidadobra.setObraId(obra.getId());
            unidadobra.setEmpresaconstructoraId(idEmpresa);
            unidadobra.setZonasId(idZona);
            unidadobra.setObjetosId(idObjetos);
            unidadobra.setNivelId(idNivel);
            unidadobra.setEspecialidadesId(idEspecialidad);
            unidadobra.setSubespecialidadesId(idSubespecialidad);

            //Valores UO
            unidadobra.setCodigo(uo.getCode());
            unidadobra.setDescripcion(uo.getDescrip());
            unidadobra.setUm(uo.getUm());
            unidadobra.setRenglonbase(uo.getRb());
            unidadobra.setCantidad(uo.getCantidad());
            unidadobra.setCostoMaterial(uo.getCostMaterial());
            unidadobra.setCostomano(uo.getCostMano());
            unidadobra.setCostoequipo(uo.getCostEquipo());
            unidadobra.setSalario(uo.getSalario());
            unidadobra.setSalariocuc(uo.getSalariocuc());
            costTotal = uo.getCostMaterial() + uo.getCostMano() + uo.getCostEquipo();
            costUnitario = costTotal / uo.getCantidad();
            unidadobra.setCostototal(costTotal);
            unidadobra.setCostounitario(costUnitario);
            unidadobra.setGrupoejecucionId(1);
            unidadobra.setIdResolucion(obra.getSalarioId());
            unidadobraList.add(unidadobra);
        }

        return unidadobraList;
    }

    private int getIdNivel(String codeN, int idOb) {
        return nivelArrayList.parallelStream().filter(nivel1 -> nivel1.getObjetosId() == idOb && nivel1.getCodigo().trim().equals(codeN.trim())).findFirst().map(Nivel::getId).orElse(null);
    }

    public ArrayList<Unidadobra> getUnidadobraListImporGeneral(Integer idO, int idEmpresa, int idZona, int idObjetos, int idNivel, List<UnidadObraPCW> unidadesObraPCWList) {
        unidadobraList = new ArrayList<>();
        Obra obra = reportProjectStructureSingelton.getObra(idO);
        for (UnidadObraPCW uo : unidadesObraPCWList) {
            costTotal = 0.0;
            costUnitario = 0.0;
            Unidadobra unidadobra = new Unidadobra();
            //Estructura de la UO
            unidadobra.setObraId(obra.getId());
            unidadobra.setEmpresaconstructoraId(idEmpresa);
            unidadobra.setZonasId(idZona);
            unidadobra.setObjetosId(idObjetos);
            unidadobra.setNivelId(idNivel);
            unidadobra.setEspecialidadesId(getIdEspecialidad(uo.getEspecialidad()));
            unidadobra.setSubespecialidadesId(getIdSub(uo.getEspecialidad(), uo.getSubEspecialidad()));

            //Valores UO
            unidadobra.setCodigo(uo.getCode().trim());
            unidadobra.setDescripcion(uo.getDescrip().trim());
            unidadobra.setUm(uo.getUm().trim());
            unidadobra.setRenglonbase(uo.getRb());
            unidadobra.setCantidad(uo.getCantidad());
            unidadobra.setCostoMaterial(uo.getCostMaterial());
            unidadobra.setCostomano(uo.getCostMano());
            unidadobra.setCostoequipo(uo.getCostEquipo());
            unidadobra.setSalario(uo.getSalario());
            unidadobra.setSalariocuc(uo.getSalariocuc());
            costTotal = uo.getCostMaterial() + uo.getCostMano() + uo.getCostEquipo();
            costUnitario = costTotal / uo.getCantidad();
            unidadobra.setCostototal(costTotal);
            unidadobra.setCostounitario(costUnitario);
            unidadobra.setGrupoejecucionId(1);
            unidadobra.setIdResolucion(obra.getSalarioId());
            unidadobraList.add(unidadobra);
        }

        return unidadobraList;
    }

    public ArrayList<Nivelespecifico> getNivelEspecificoListImpor(Integer idO, int idEmpresa, int idZona, int idObjetos, int idNivel, int idEspecialidad, int idSubespecialidad, ArrayList<UnidadObraPCW> unidadesObraPCWList) {
        nivelespecificoArrayList = new ArrayList<>();
        for (UnidadObraPCW uo : unidadesObraPCWList) {
            costTotal = 0.0;
            Nivelespecifico unidadobra = new Nivelespecifico();
            //Estructura de la UO
            unidadobra.setObraId(idO);
            unidadobra.setEmpresaconstructoraId(idEmpresa);
            unidadobra.setZonasId(idZona);
            unidadobra.setObjetosId(idObjetos);
            unidadobra.setNivelId(idNivel);
            unidadobra.setEspecialidadesId(idEspecialidad);
            unidadobra.setSubespecialidadesId(idSubespecialidad);

            //Valores UO
            unidadobra.setCodigo(uo.getCode());
            unidadobra.setDescripcion(uo.getDescrip());
            unidadobra.setCostomaterial(uo.getCostMaterial());
            unidadobra.setCostomano(uo.getCostMano());
            unidadobra.setCostoequipo(uo.getCostEquipo());
            unidadobra.setSalario(uo.getSalario());
            nivelespecificoArrayList.add(unidadobra);
        }

        return nivelespecificoArrayList;
    }

    public void createUnidadesObra(List<Unidadobra> unidadesObraPCWList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUO = 0;

            for (Unidadobra uo : unidadesObraPCWList) {
                countUO++;
                if (countUO > 0 && countUO % batchSizeUO == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(uo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void createNivelEspecifico(List<Nivelespecifico> unidadesObraPCWList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countES = 0;

            for (Nivelespecifico uo : unidadesObraPCWList) {
                countES++;
                if (countES > 0 && countES % batchSizeES == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(uo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public ArrayList<Unidadobra> getUnidadobraListImporGeneralObjeto(Integer idO, int idEmpresa, int idZona, int idObjetos, List<UnidadObraPCW> unidadesObraPCWList) {
        unidadobraList = new ArrayList<>();
        Obra obra = reportProjectStructureSingelton.getObra(idO);
        for (UnidadObraPCW uo : unidadesObraPCWList) {
            costTotal = 0.0;
            costUnitario = 0.0;
            Unidadobra unidadobra = new Unidadobra();
            //Estructura de la UO
            unidadobra.setObraId(obra.getId());
            unidadobra.setEmpresaconstructoraId(idEmpresa);
            unidadobra.setZonasId(idZona);
            unidadobra.setObjetosId(idObjetos);
            unidadobra.setNivelId(getIdNivel(uo.getNivel(), idObjetos));
            unidadobra.setEspecialidadesId(getIdEspecialidad(uo.getEspecialidad()));
            unidadobra.setSubespecialidadesId(getIdSub(uo.getEspecialidad(), uo.getSubEspecialidad()));

            //Valores UO
            unidadobra.setCodigo(uo.getCode().trim());
            unidadobra.setDescripcion(uo.getDescrip().trim());
            unidadobra.setUm(uo.getUm().trim());
            unidadobra.setRenglonbase(uo.getRb());
            unidadobra.setCantidad(uo.getCantidad());
            unidadobra.setCostoMaterial(uo.getCostMaterial());
            unidadobra.setCostomano(uo.getCostMano());
            unidadobra.setCostoequipo(uo.getCostEquipo());
            unidadobra.setSalario(uo.getSalario());
            unidadobra.setSalariocuc(uo.getSalariocuc());
            costTotal = uo.getCostMaterial() + uo.getCostMano() + uo.getCostEquipo();
            costUnitario = costTotal / uo.getCantidad();
            unidadobra.setCostototal(costTotal);
            unidadobra.setCostounitario(costUnitario);
            unidadobra.setGrupoejecucionId(1);
            unidadobra.setIdResolucion(obra.getSalarioId());
            unidadobraList.add(unidadobra);
        }

        return unidadobraList;
    }

    public List<Unidadobrarenglon> getUnidadobrarenglonArrayList(Unidadobra unidadobra, List<UnidadRnglonesPCW> unidadesObraRnglonesPCWListPCWList) {

        unidadobrarenglonArrayList = new ArrayList<>();
        for (UnidadRnglonesPCW uo : unidadesObraRnglonesPCWListPCWList) {
            Unidadobrarenglon unidadobrarenglon = new Unidadobrarenglon();
            unidadobrarenglon.setUnidadobraId(unidadobra.getId());
            Renglonvariante renglon = rvToSugestion(uo.getRv(), unidadobra.getObraByObraId().getSalarioId());
            double costoMano = renglon.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * utilCalcSingelton.getValorCosto(renglonrecursos.getRecursosByRecursosId().getGrupoescala())).reduce(0.0, Double::sum);
            double costoEq = renglon.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            unidadobrarenglon.setRenglonvarianteId(renglon.getId());
            unidadobrarenglon.setCantRv(uo.getCant());
            unidadobrarenglon.setCostMat(uo.getCmate());
            unidadobrarenglon.setCostMano(uo.getCant() * costoMano);
            unidadobrarenglon.setCostEquip(uo.getCant() * costoEq);
            double salario = utilCalcSingelton.calcSalarioRV(renglon);
            unidadobrarenglon.setSalariomn(salario);
            unidadobrarenglon.setSalariocuc(uo.getSalcuc());
            unidadobrarenglon.setConMat("0");
            unidadobrarenglonArrayList.add(unidadobrarenglon);
        }
        return unidadobrarenglonArrayList;
    }

    public List<Unidadobrarenglon> getUnidadobrarenglonToImportArrayList(Unidadobra unidadobra, List<RenglonVariantePCW> unidadesObraRnglonesPCWListPCWList) {

        unidadobrarenglonArrayList = new ArrayList<>();
        for (RenglonVariantePCW uo : unidadesObraRnglonesPCWListPCWList) {
            Unidadobrarenglon unidadobrarenglon = new Unidadobrarenglon();
            unidadobrarenglon.setUnidadobraId(unidadobra.getId());
            Empresaobrasalario empresaobrasalario = utilCalcSingelton.getEmpresaobrasalario(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());
            Renglonvariante renglon = rvToSugestion(uo.getCodeRV(), unidadobra.getObraByObraId().getSalarioId());
            double costoMano = renglon.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * utilCalcSingelton.getValorCosto(renglonrecursos.getRecursosByRecursosId().getGrupoescala())).reduce(0.0, Double::sum);
            double costoEq = renglon.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            unidadobrarenglon.setRenglonvarianteId(renglon.getId());
            unidadobrarenglon.setCantRv(uo.getCantidad());
            unidadobrarenglon.setCostMat(uo.getCostMaterial());
            unidadobrarenglon.setCostMano(uo.getCantidad() * costoMano);
            unidadobrarenglon.setCostEquip(uo.getCantidad() * costoEq);
            double salario = utilCalcSingelton.calcSalarioRV(renglon);
            unidadobrarenglon.setSalariomn(salario);
            unidadobrarenglon.setSalariocuc(uo.getSalariocuc());
            unidadobrarenglon.setConMat("0");
            if (unidadobrarenglon.getRenglonvarianteId() != 0) {
                unidadobrarenglonArrayList.add(unidadobrarenglon);
            }

        }
        return unidadobrarenglonArrayList;
    }

    public void createUnidadesObraRenglones(List<Unidadobrarenglon> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Unidadobrarenglon unidadobrarenglon : unidadobrarenglonArray) {
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

    public void createNivelEspecificoRenglones(List<Renglonnivelespecifico> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countNER = 0;

            for (Renglonnivelespecifico unidadobrarenglon : unidadobrarenglonArray) {
                countNER++;
                if (countNER > 0 && countNER % batchSizeNER == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(unidadobrarenglon);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            /*
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear la asociación unidad de obra RV");
            alert.showAndWait();
            */

        }

    }

    public void createRecursos(ArrayList<Recursos> recursosArrayList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countrec = 0;

            for (Recursos recursos : recursosArrayList) {
                countrec++;
                if (countrec > 0 && countrec % batchSizeRec == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(recursos);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los recursos");
            alert.showAndWait();
        }

    }

    public List<Renglonnivelespecifico> getNiUnidadobrarenglonList(Nivelespecifico unidadobra, List<RenglonVariantePCW> unidadesObraRnglonesPCWListPCWList) {

        nivelespecificoList = new ArrayList<>();
        for (RenglonVariantePCW uo : unidadesObraRnglonesPCWListPCWList) {
            Renglonnivelespecifico unidadobrarenglon = new Renglonnivelespecifico();
            unidadobrarenglon.setNivelespecificoId(unidadobra.getId());
            Empresaobrasalario empresaobrasalario = utilCalcSingelton.getEmpresaobrasalario(unidadobra.getObraId(), unidadobra.getEmpresaconstructoraId());
            Renglonvariante renglon = rvToSugestion(uo.getCodeRV(), unidadobra.getObraByObraId().getSalarioId());
            double costoMano = renglon.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("2")).map(renglonrecursos -> renglonrecursos.getCantidas() * utilCalcSingelton.getValorCosto(renglonrecursos.getRecursosByRecursosId().getGrupoescala())).reduce(0.0, Double::sum);
            double costoEq = renglon.getRenglonrecursosById().parallelStream().filter(renglonrecursos -> renglonrecursos.getRecursosByRecursosId().getTipo().trim().equals("3")).map(renglonrecursos -> renglonrecursos.getCantidas() * renglonrecursos.getRecursosByRecursosId().getPreciomn()).reduce(0.0, Double::sum);
            unidadobrarenglon.setRenglonvarianteId(renglon.getId());
            unidadobrarenglon.setCantidad(uo.getCantidad());
            unidadobrarenglon.setCostmaterial(uo.getCostMaterial());
            unidadobrarenglon.setCostmano(uo.getCantidad() * costoMano);
            unidadobrarenglon.setCostequipo(uo.getCantidad() * costoEq);
            double salario = utilCalcSingelton.calcSalarioRV(renglon);
            unidadobrarenglon.setSalario(salario);
            unidadobrarenglon.setSalariocuc(uo.getSalariocuc());
            unidadobrarenglon.setConmat("0");
            nivelespecificoList.add(unidadobrarenglon);
        }
        return nivelespecificoList;
    }

    public Renglonvariante rvToSugestion(String code, int tag) {
        return utilCalcSingelton.renglonvarianteList.parallelStream().filter(renglon -> renglon.getCodigo().trim().equals(code) && renglon.getRs() == tag).findFirst().orElse(null);
    }


    public RVCreateModel getRenModel(String cod) {

        DatabaseEstructure databaseEstructure = DatabaseEstructure.getInstance();
        ArrayList<RVCreateModel> createModelArrayList = databaseEstructure.getRvCreateModelArrayList();

        RVCreateModel createModel = createModelArrayList.parallelStream().filter(rvCreateModel -> rvCreateModel.getRV().equals(cod)).findFirst().orElse(null);
        return createModel;
    }

    public int getsubGrupoId(String codeS) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            idSub = 0;
            Subgrupo subgrupo = (Subgrupo) session.createQuery("from Sobregrupo WHERE codigo =: c").setParameter("c", codeS).getSingleResult();
            if (subgrupo != null) {
                idSub = subgrupo.getId();
            }


            tx.commit();
            session.close();
            return idSub;


        } catch (Exception ex) {

        }

        return idSub;
    }

    public Integer createRnglonVarianteFromPCWin(String codRV, int norma) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;

        RVCreateModel rng = getRenModel(codRV);

        try {
            tx = session.beginTransaction();

            idReng = null;

            Renglonvariante renglonvariante = new Renglonvariante();
            renglonvariante.setCodigo(rng.getRV());
            renglonvariante.setUm(rng.UM);
            renglonvariante.setDescripcion(rng.getDescripn());
            renglonvariante.setRs(norma);
            renglonvariante.setCostomat(Double.parseDouble(rng.getCostoMat()));
            renglonvariante.setCostmano(Double.parseDouble(rng.getCostoMan()));
            renglonvariante.setCostequip(Double.parseDouble(rng.getCostoEq()));
            renglonvariante.setHh(Double.parseDouble(rng.getHH()));
            renglonvariante.setHa(Double.parseDouble(rng.getHA()));
            renglonvariante.setHe(Double.parseDouble(rng.getHE()));
            renglonvariante.setHo(Double.parseDouble(rng.getHO()));
            renglonvariante.setCemento(Double.parseDouble(rng.getCemento()));
            renglonvariante.setArido(Double.parseDouble(rng.getArido()));
            renglonvariante.setPeso(Double.parseDouble(rng.getPeso()));
            renglonvariante.setMermamanip(0.0);
            renglonvariante.setMermaprod(0.0);
            renglonvariante.setMermatrans(0.0);
            renglonvariante.setOtrasmermas(0.0);
            renglonvariante.setAsfalto(0.0);
            renglonvariante.setCarga(0.0);
            renglonvariante.setPrefab(0.0);
            renglonvariante.setSubgrupoId(getsubGrupoId(rng.getSubGrp()));
            renglonvariante.setPreciomn(0.0);
            renglonvariante.setPreciomlc(0.0);

            idReng = (Integer) session.save(renglonvariante);
            if (idReng != null) {
                createEstructureRV(idReng, rng.getRvCompositionArrayList());
                renglonvarianteArrayList.add(renglonvariante);
            }

            tx.commit();
            session.close();


        } catch (Exception ex) {

        }

        return idReng;
    }

    public Recursos getRecursos(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            rec = new Recursos();
            rec = (Recursos) session.createQuery("FROM Recursos WHERE codigo =: rc").setParameter("rc", code).getSingleResult();

            tx.commit();
            session.close();
            return rec;


        } catch (Exception ex) {

        }

        return new Recursos();
    }

    public Unidadobra getUnidadobra(int idUni) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Unidadobra unidadobra = null;
        try {
            tx = session.beginTransaction();

            unidadobra = session.get(Unidadobra.class, idUni);

            tx.commit();
            session.close();
            return unidadobra;


        } catch (Exception ex) {

        }
        return unidadobra;

    }

    public Suministrossemielaborados getRecSuministrossemielaborados(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            recSuministrossemielaborados = new Suministrossemielaborados();
            recSuministrossemielaborados = (Suministrossemielaborados) session.createQuery("FROM Suministrossemielaborados WHERE codigo =: rc").setParameter("rc", code).getSingleResult();

            tx.commit();
            session.close();
            return recSuministrossemielaborados;


        } catch (Exception ex) {

        }

        return new Suministrossemielaborados();
    }

    public Juegoproducto getJuegoproducto(String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            juegoproducto = new Juegoproducto();
            juegoproducto = (Juegoproducto) session.createQuery("FROM Juegoproducto WHERE codigo =: rc").setParameter("rc", code).getSingleResult();

            tx.commit();
            session.close();
            return juegoproducto;


        } catch (Exception ex) {

        }

        return new Juegoproducto();
    }

    public void createEstructureRV(int idR, ArrayList<rvComposition> compositionArrayList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (rvComposition rvComposition : compositionArrayList) {

                if (rvComposition.getTip().equals(1) || rvComposition.getTip().equals("2") || rvComposition.equals("3")) {
                    Renglonrecursos renglonrecursos = new Renglonrecursos();
                    renglonrecursos.setRenglonvarianteId(idR);
                    renglonrecursos.setRecursosId(getRecursos(rvComposition.getInsumo()).getId());
                    renglonrecursos.setCantidas(rvComposition.getNorma());
                    renglonrecursos.setUsos(rvComposition.getUso());
                    session.persist(renglonrecursos);

                } else if (rvComposition.getTip().equals("S")) {
                    Renglonsemielaborados renglonsemielaborados = new Renglonsemielaborados();
                    renglonsemielaborados.setRenglonvarianteId(idR);
                    renglonsemielaborados.setSuministrossemielaboradosId(getRecSuministrossemielaborados(rvComposition.getInsumo()).getId());
                    renglonsemielaborados.setCantidad(rvComposition.getNorma());
                    renglonsemielaborados.setUsos(rvComposition.getUso());
                    session.persist(renglonsemielaborados);

                } else if (rvComposition.getTip().equals("J")) {
                    Renglonjuego renglonjuego = new Renglonjuego();
                    renglonjuego.setRenglonvarianteId(idR);
                    renglonjuego.setJuegoproductoId(getJuegoproducto(rvComposition.getInsumo()).getId());
                    renglonjuego.setCantidad(rvComposition.getNorma());
                    renglonjuego.setUsos(rvComposition.getUso());
                    session.persist(renglonjuego);
                }

            }
            tx.commit();
            session.close();


        } catch (Exception ex) {

        }

    }

    public ArrayList<Unidadobra> getUnidadobraArrayList() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            unidadobraArrayList = new ArrayList<>();
            unidadobraArrayList = (ArrayList<Unidadobra>) session.createQuery("FROM Unidadobra ").getResultList();

            tx.commit();
            session.close();
            return unidadobraArrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<Nivelespecifico> getNivelespecificoArrayList() {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelespecificoArray = new ArrayList<>();
            nivelespecificoArray = (ArrayList<Nivelespecifico>) session.createQuery("FROM Nivelespecifico ").getResultList();

            tx.commit();
            session.close();
            return nivelespecificoArray;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<Renglonvariante> getRenglonvarianteArrayList(String norma) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            renglonvarianteArrayList = new ArrayList<>();
            renglonvarianteArrayList = (ArrayList<Renglonvariante>) session.createQuery("FROM Renglonvariante WHERE rs =: no").setParameter("no", norma).getResultList();

            tx.commit();
            session.close();
            return renglonvarianteArrayList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList<CalPrecSuminist> getCalPrecSuministArrayList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            calPrecSuministArrayList = new ArrayList<>();
            List<Recursos> recursosList = session.createQuery("FROM Recursos WHERE tipo = '1'").getResultList();
            for (Recursos recursos : recursosList) {
                calPrecSuministArrayList.add(new CalPrecSuminist(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getTipo(), recursos.getPreciomn(), 0.0, recursos.getPeso()));
            }

            List<Juegoproducto> juegoproductoList = session.createQuery("FROM Juegoproducto ").getResultList();
            for (Juegoproducto juegoproducto1 : juegoproductoList) {
                calPrecSuministArrayList.add(new CalPrecSuminist(juegoproducto1.getId(), juegoproducto1.getCodigo(), juegoproducto1.getDescripcion(), juegoproducto1.getUm(), "J", juegoproducto1.getPreciomn(), 0.0, juegoproducto1.getPeso()));
            }

            List<Suministrossemielaborados> suministrossemielaboradosList = session.createQuery("FROM Suministrossemielaborados ").getResultList();
            for (Suministrossemielaborados suministrossemielaborados : suministrossemielaboradosList) {
                calPrecSuministArrayList.add(new CalPrecSuminist(suministrossemielaborados.getId(), suministrossemielaborados.getCodigo(), suministrossemielaborados.getDescripcion(), suministrossemielaborados.getUm(), "S", suministrossemielaborados.getPreciomn(), 0.0, suministrossemielaborados.getPeso()));
            }

            tx.commit();
            session.close();
            return calPrecSuministArrayList;

        } catch (Exception ex) {

        }
        return new ArrayList<>();
    }

    public List<Recursos> getFullRecursosArrayList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            recursosList = new ArrayList<>();
            recursosList = session.createQuery("FROM Recursos").getResultList();

            tx.commit();
            session.close();
            return recursosList;

        } catch (Exception ex) {

        }
        return new ArrayList<>();
    }

    public void createSuministrosSemielaborados(List<Suministrossemielaborados> suministrossemielaboradosArrayList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countsem = 0;
            for (Suministrossemielaborados recursos : suministrossemielaboradosArrayList) {
                countsem++;
                if (countsem > 0 && countsem % batchSizeSE == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(recursos);
            }


            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los semielaborados");
            alert.showAndWait();
        }

    }

    public Integer createSingleREC(SuminstrosPCW suminstrosPCW) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            idRc = null;
            Recursos sumuni = new Recursos();
            sumuni.setCodigo(suminstrosPCW.getCodeSuministro());
            sumuni.setDescripcion(suminstrosPCW.getDescrip());
            sumuni.setUm(suminstrosPCW.getUm());
            sumuni.setPeso(suminstrosPCW.getPeso());
            sumuni.setPreciomn(suminstrosPCW.getMn());
            sumuni.setPreciomlc(suminstrosPCW.getMlc());
            sumuni.setMermaprod(0.0);
            sumuni.setMermatrans(0.0);
            sumuni.setMermamanip(0.0);
            sumuni.setOtrasmermas(0.0);
            sumuni.setPertenece(String.valueOf(idOb));
            sumuni.setTipo(suminstrosPCW.getTipo());
            sumuni.setGrupoescala("I");
            sumuni.setCostomat(0.0);
            sumuni.setCostmano(0.0);
            sumuni.setCostequip(0.0);
            sumuni.setHa(0.0);
            sumuni.setHe(0.0);
            sumuni.setHo(0.0);
            sumuni.setCemento(0.0);
            sumuni.setArido(0.0);
            sumuni.setAsfalto(0.0);
            sumuni.setCarga(0.0);
            sumuni.setPrefab(0.0);
            sumuni.setCet(0.0);
            sumuni.setCpo(0.0);
            sumuni.setCpe(0.0);
            sumuni.setOtra(0.0);

            idRc = (Integer) session.save(sumuni);
            tx.commit();
            session.close();

        } catch (Exception e) {

        }
        recursosList = new ArrayList<>();
        recursosList = getFullRecursosArrayList();
        return idRc;

    }

    public void createComposicionSemiPCW(List<Suministrossemielaborados> recursos) {

        semielaboradosrecursosList = new ArrayList<>();
        for (Suministrossemielaborados s : recursos) {
            System.out.println(s.getCodigo());
            compoSemiModels = new ArrayList<>();
            compoSemiModels = DatabaseEstructure.getInstance().getCompoSemiModelArrayList(s.getCodigo());
            Suministrossemielaborados suministrossemielaborados = getRecSuministrossemielaborados(s.getCodigo());
            for (CompoSemiModel compoSemiModel : compoSemiModels) {
                idInsumo = null;
                Recursos r = recursosList.parallelStream().filter(rec -> rec.getCodigo().equals(compoSemiModel.getInsumo())).findFirst().orElse(null);
                if (r == null) {
                    SuminstrosPCW sum = DatabaseEstructure.getInstance().getRecurso(compoSemiModel.getInsumo());
                    idInsumo = createSingleREC(sum);
                } else if (r != null) {
                    idInsumo = r.getId();
                }

                Semielaboradosrecursos semielaboradosrecursos = new Semielaboradosrecursos();
                semielaboradosrecursos.setSuministrossemielaboradosId(suministrossemielaborados.getId());
                semielaboradosrecursos.setRecursosId(idInsumo);
                semielaboradosrecursos.setCantidad(compoSemiModel.getCantidad());
                semielaboradosrecursos.setUsos(compoSemiModel.getUsos());

                semielaboradosrecursosList.add(semielaboradosrecursos);
            }
        }

        persistListRescursosInSemielaborados(semielaboradosrecursosList);

    }

    private Double getCantidadCertificada(int idUnidad) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery(" SELECT SUM(cantidad) FROM Certificacion  WHERE unidadobraId =: id").setParameter("id", idUnidad);
            if (query.getResultList().get(0) == null) {
                total = 0.0;
            } else {
                total = (Double) query.getResultList().get(0);
            }

            tx.commit();
            session.close();
            return total;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0.0;
    }

    private Double getCantidadCertificadaUOTime(int idUnidad, Date desde, Date hasta) {

        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Certificacion> list = session.createQuery(" FROM Certificacion ").getResultList();
            total = list.parallelStream().filter(certificacion -> certificacion.getUnidadobraId() == idUnidad && certificacion.getDesde().equals(desde) && certificacion.getHasta().equals(hasta)).map(Certificacion::getCantidad).reduce(0.0, Double::sum);

            tx.commit();
            session.close();
            return total;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }

        return 0.0;
    }

    private void persistListRescursosInSemielaborados(List<Semielaboradosrecursos> semielaboradosrecursosList) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countrs = 0;

            for (Semielaboradosrecursos recursos : semielaboradosrecursosList) {
                countrs++;
                if (countrs > 0 && countrs % batchrs == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(recursos);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los semielaborados y sus recursos");
            alert.showAndWait();
        }

    }

    public List<Renglonnivelespecifico> getRenglonnivelespecificoList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            renglonnivelespecificoList = new ArrayList<>();
            renglonnivelespecificoList = session.createQuery("FROM Renglonnivelespecifico ").getResultList();

            tx.commit();
            session.close();
            return renglonnivelespecificoList;

        } catch (Exception e) {

        }

        return new ArrayList<>();
    }

    public void createBajoespecificacion(List<Bajoespecificacion> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajo = 0;

            for (Bajoespecificacion bajoespecificacion : bajoespecificacionList) {
                countbajo++;
                if (countbajo > 0 && countbajo % batchbajo == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }

    }

    public void createBajoespecificacionRV(List<Bajoespecificacionrv> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajorv = 0;

            for (Bajoespecificacionrv bajoespecificacion : bajoespecificacionList) {
                countbajorv++;
                if (countbajorv > 0 && countbajorv % batchbajorv == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(bajoespecificacion);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear los Bajo Especificación");
            alert.showAndWait();
        }

    }

    public Integer createObraRV(ObraPCW obraPCW, int entId, int invId, int salId) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {

            tx = session.beginTransaction();
            idOb = null;

            Obra obra = new Obra();
            obra.setCodigo(obraPCW.getCode());
            obra.setDescripion(obraPCW.getDescrip());
            obra.setTipo(obraPCW.getClasif());
            obra.setTipoobraId(getTipoobra(obraPCW.getTipo()).getId());
            obra.setEntidadId(entId);
            obra.setInversionistaId(invId);
            obra.setSalarioId(salId);
            obra.setDefcostmat(0);

            idOb = (Integer) session.save(obra);

            tx.commit();
            session.close();
            return idOb;


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la obra");
            alert.setContentText("Error al crear la obra en PostgreSQL");
            alert.showAndWait();
        }
        return idOb;
    }

    public void createCertificacion(Certificacion certificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idcert = null;
        renglonSingelton = certificaRenglonSingelton.getInstance();

        Unidadobra unid = getUnidadobra(certificacion.getUnidadobraId());
        double calc = getCantidadCertificada(certificacion.getUnidadobraId());
        double valToPlan = unid.getCantidad() - calc;
        try {
            tx = session.beginTransaction();
            idcert = (Integer) session.save(certificacion);

            renglonSingelton.cargarDatos(certificacion.getUnidadobraId(), idcert, certificacion.getDesde(), certificacion.getHasta(), certificacion.getCantidad(), valToPlan);

            Thread t2 = new CertificaRecursosRenglonesThread(certificacion.getUnidadobraId(), idcert, certificacion.getDesde(), certificacion.getHasta(), certificacion.getCantidad(), valToPlan);
            t2.start();


            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            /*
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la certificaciíon");
            alert.setContentText("Error al crear la certificación");
            alert.showAndWait(); 
            */

        }

    }

    public void createPlanificacion(Planificacion planificacion) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idcert = null;
        planificaRenglonSingleton = models.planificaRenglonSingleton.getInstance();
        Unidadobra unid = getUnidadobra(planificacion.getUnidadobraId());
        double calc = getCantidadCertificadaUOTime(planificacion.getUnidadobraId(), planificacion.getDesde(), planificacion.getHasta());
        double valToPlan = unid.getCantidad() - calc;
        try {
            tx = session.beginTransaction();

            idcert = (Integer) session.save(planificacion);
            planificaRenglonSingleton.cargarDatos(planificacion.getUnidadobraId(), idcert, planificacion.getDesde(), planificacion.getHasta(), planificacion.getCantidad(), valToPlan);

/*
            Thread t1 = new PlanificarUORenglonesThread(planificacion.getUnidadobraId(), idcert, planificacion.getDesde(), planificacion.getHasta(), planificacion.getCantidad(), valToPlan);
            t1.start();
*/
            Thread t2 = new PlanificarRecursosRenglonesThread(planificacion.getUnidadobraId(), idcert, planificacion.getDesde(), planificacion.getHasta(), planificacion.getCantidad(), valToPlan);
            t2.start();


            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            /*
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error al crear la planificación");
            alert.setContentText("Error al crear la planificación");
            alert.showAndWait();
            */

        }

    }
}




