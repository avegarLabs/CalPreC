package models;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelsImportViewSingelton {
    public static ModelsImportViewSingelton instancia = null;

    public ArrayList<ObraImpModel> obraArrayList;
    public ArrayList<ZonaImpModel> zonasArrayList;
    public ArrayList<ObjetoImpModel> objetosArrayList;
    public ArrayList<NivelImpModel> nivelArrayList;
    public ArrayList<EspecialidadImpModel> especialidadesArrayList;
    public ArrayList<SubespecialidadImpModel> subespecialidadesArrayList;
    public ArrayList<EmpImpModel> empresaconstructoraArrayList;


    public ObservableList<ObjetosView> objetosViewObservableList;
    public ObservableList<NivelView> nivelViewObservableList;
    public ObservableList<EspecialidadesView> especialidadesViewObservableList;
    public ObservableList<SubDuplicateModel> subDuplicateModelObservableList;
    private int batchSizeNiv = 10;
    private int countNiv;
    private int batchSizeUOR = 10;
    private int countUOR;
    private int batchbajo = 10;
    private int countbajo;
    private int batchPlanrec = 20;
    private int countplan;
    private int batchEmpresaObra = 10;
    private int countEO;
    private int batchEmpresaObraConcep = 20;
    private int countEOC;
    private ReportProjectStructureSingelton structureSingelton = ReportProjectStructureSingelton.getInstance();
    private boolean chekUni;
    private ReportProjectStructureSingelton projectStructureSingelton = ReportProjectStructureSingelton.getInstance();
    private ArrayList<Recursos> recursosArrayList;
    private ArrayList<Suministrossemielaborados> suministrossemielaboradosArrayList;
    private ArrayList<Juegoproducto> juegoproductoArrayList;
    private ObservableList<RecursosToChangeObserve> toChangeObserveObservableList;

    private ModelsImportViewSingelton() {

    }

    public static ModelsImportViewSingelton getInstance() {
        if (instancia == null) {
            instancia = new ModelsImportViewSingelton();
        }

        return instancia;
    }

    public List<ObraImpModel> getObrasList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obraArrayList = new ArrayList<>();
            List<Obra> listOb = session.createQuery("FROM Obra WHERE tipo = 'UO'").getResultList();
            for (Obra obra : listOb) {
                obraArrayList.add(new ObraImpModel(obra.getId(), obra.getCodigo() + " - " + obra.getDescripion()));
            }
            tx.commit();
            session.close();
            return obraArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<ObraImpModel> getObrasListRV() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            obraArrayList = new ArrayList<>();
            List<Obra> listOb = session.createQuery("FROM Obra WHERE tipo = 'RV'").getResultList();
            for (Obra obra : listOb) {
                obraArrayList.add(new ObraImpModel(obra.getId(), obra.getCodigo() + " - " + obra.getDescripion()));
            }
            tx.commit();
            session.close();
            return obraArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<ZonaImpModel> getZonasList(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            zonasArrayList = new ArrayList<>();
            zonasArrayList.add(new ZonaImpModel(0, "Todas"));
            List<Zonas> list = session.createQuery("FROM Zonas WHERE obraId =: idObra").setParameter("idObra", idObra).getResultList();
            for (Zonas zonas : list) {
                zonasArrayList.add(new ZonaImpModel(zonas.getId(), zonas.getCodigo() + " - " + zonas.getDesripcion()));
            }
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

    public List<Zonas> getZonasListFull(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Zonas> list = new ArrayList<>();
            list = session.createQuery("FROM Zonas WHERE obraId =: idObra").setParameter("idObra", idObra).getResultList();

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

    public List<EmpImpModel> getEmpresaList(int idObra) {
        System.out.println(" -->:" + idObra);
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            empresaconstructoraArrayList = new ArrayList<>();
            List<Tuple> resultList = session.createQuery("SELECT emp.id, emp.codigo, emp.descripcion  FROM Empresaobra eo INNER JOIN Empresaconstructora emp ON eo.empresaconstructoraId = emp.id WHERE eo.obraId =: idObra", Tuple.class).setParameter("idObra", idObra).getResultList();
            for (Tuple row : resultList) {
                System.out.println(row.get(1).toString());
                empresaconstructoraArrayList.add(new EmpImpModel(Integer.parseInt(row.get(0).toString()), row.get(1).toString() + " - " + row.get(2).toString()));
            }
            tx.commit();
            session.close();
            return empresaconstructoraArrayList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<ObjetoImpModel> getObjetosList(int idZona) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosArrayList = new ArrayList<>();
            objetosArrayList.add(new ObjetoImpModel(0, "Todos"));
            List<Objetos> list = session.createQuery("FROM Objetos WHERE zonasId =: idZona").setParameter("idZona", idZona).getResultList();
            for (Objetos obj : list) {
                objetosArrayList.add(new ObjetoImpModel(obj.getId(), obj.getCodigo() + " - " + obj.getDescripcion()));
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

    public List<Objetos> getObjetosListFull(int idObr) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Objetos> objetosList = new ArrayList<>();
            List<Tuple> results = session.createQuery("SELECT obj.id, obj.codigo, obj.descripcion, obj.zonasId FROM Objetos obj INNER JOIN Zonas zon ON obj.zonasId = zon.id WHERE zon.obraId =: idOb", Tuple.class).setParameter("idOb", idObr).getResultList();
            for (Tuple result : results) {
                Objetos obj = new Objetos();
                obj.setId(Integer.parseInt(result.get(0).toString()));
                obj.setCodigo(result.get(1).toString());
                obj.setDescripcion(result.get(2).toString());
                obj.setZonasId(Integer.parseInt(result.get(3).toString()));
                objetosList.add(obj);
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
        return new ArrayList<>();
    }

    public List<NivelImpModel> getNivelList(int idObjeto) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelArrayList = new ArrayList<>();
            nivelArrayList.add(new NivelImpModel(0, "Todos"));
            List<Nivel> list = session.createQuery("FROM Nivel WHERE objetosId =: idObjeto").setParameter("idObjeto", idObjeto).getResultList();
            for (Nivel nivel : list) {
                nivelArrayList.add(new NivelImpModel(nivel.getId(), nivel.getCodigo() + " - " + nivel.getDescripcion()));
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

    public List<EspecialidadImpModel> getEspecialidadesList() {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesArrayList = new ArrayList<>();
            especialidadesArrayList.add(new EspecialidadImpModel(0, "Todas"));
            List<Especialidades> list = session.createQuery("FROM Especialidades ").getResultList();
            for (Especialidades esp : list) {
                especialidadesArrayList.add(new EspecialidadImpModel(esp.getId(), esp.getCodigo() + " " + esp.getDescripcion()));
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

    public List<SubespecialidadImpModel> getSubespecialidadesObservableList(int idEspec) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subespecialidadesArrayList = new ArrayList<>();
            subespecialidadesArrayList.add(new SubespecialidadImpModel(0, "Todas"));
            List<Subespecialidades> list = session.createQuery("FROM Subespecialidades WHERE especialidadesId =: idEsp").setParameter("idEsp", idEspec).getResultList();
            for (Subespecialidades subespecialidades : list) {
                subespecialidadesArrayList.add(new SubespecialidadImpModel(subespecialidades.getId(), subespecialidades.getCodigo() + " - " + subespecialidades.getDescripcion()));
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

    public ObservableList<ObjetosView> getObjetosViewObservableList(int idZona) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            objetosViewObservableList = FXCollections.observableArrayList();
            Zonas zonas = session.get(Zonas.class, idZona);
            zonas.getObjetosById().size();
            for (Objetos objetos : zonas.getObjetosById()) {
                objetosViewObservableList.add(new ObjetosView(objetos.getId(), objetos.getCodigo(), objetos.getDescripcion()));
            }

            tx.commit();
            session.close();
            return objetosViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    public ObservableList<NivelView> getNivelViewObservableList(int idObj) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            nivelViewObservableList = FXCollections.observableArrayList();
            Objetos objetos = session.get(Objetos.class, idObj);
            objetos.getNivelsById().size();
            for (Nivel nivel : objetos.getNivelsById()) {
                nivelViewObservableList.add(new NivelView(nivel.getId(), nivel.getCodigo(), nivel.getDescripcion()));
            }

            tx.commit();
            session.close();
            return nivelViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList();
    }

    public ObservableList<EspecialidadesView> getEspecialidadView(int idOb, int idEmp, int idZon, int idObj, int idNiv, String type) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            especialidadesViewObservableList = FXCollections.observableArrayList();
            if (type.equals("UO")) {
                List<Tuple> results = session.createQuery("SELECT DISTINCT esp.id, esp.codigo, esp.descripcion FROM Unidadobra uo INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idO AND uo.empresaconstructoraId =: idEm AND uo.zonasId =: idZ AND uo.objetosId =: idOb AND uo.nivelId =: idN ", Tuple.class).setParameter("idO", idOb).setParameter("idEm", idEmp).setParameter("idZ", idZon).setParameter("idOb", idObj).setParameter("idN", idNiv).getResultList();
                for (Tuple result : results) {
                    especialidadesViewObservableList.add(new EspecialidadesView(Integer.parseInt(result.get(0).toString()), result.get(1).toString(), result.get(2).toString()));
                }
            } else {
                List<Tuple> results = session.createQuery("SELECT DISTINCT esp.id, esp.codigo, esp.descripcion FROM Nivelespecifico uo INNER JOIN Especialidades esp ON uo.especialidadesId = esp.id WHERE uo.obraId =: idO AND uo.empresaconstructoraId =: idEm AND uo.zonasId =: idZ AND uo.objetosId =: idOb AND uo.nivelId =: idN ", Tuple.class).setParameter("idO", idOb).setParameter("idEm", idEmp).setParameter("idZ", idZon).setParameter("idOb", idObj).setParameter("idN", idNiv).getResultList();
                for (Tuple result : results) {
                    especialidadesViewObservableList.add(new EspecialidadesView(Integer.parseInt(result.get(0).toString()), result.get(1).toString(), result.get(2).toString()));
                }
            }


            tx.commit();
            session.close();
            return especialidadesViewObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    private List<Unidadobra> checkUnidadObra(List<Unidadobra> unidadobraList, int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp) {
        return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idOb && unidadobra.getEmpresaconstructoraId() == idEmp && unidadobra.getZonasId() == idZon && unidadobra.getObjetosId() == idObj && unidadobra.getNivelId() == idNiv && unidadobra.getEspecialidadesId() == idEsp).collect(Collectors.toList());
    }

    private List<Nivelespecifico> checkNivelespecificoList(List<Nivelespecifico> unidadobraList, int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp) {
        return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idOb && unidadobra.getEmpresaconstructoraId() == idEmp && unidadobra.getZonasId() == idZon && unidadobra.getObjetosId() == idObj && unidadobra.getNivelId() == idNiv && unidadobra.getEspecialidadesId() == idEsp).collect(Collectors.toList());
    }

    public ObservableList<SubDuplicateModel> getSubEspView(int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Obra obra = structureSingelton.getObra(idOb);
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            subDuplicateModelObservableList = FXCollections.observableArrayList();
            List<Subespecialidades> subespecialidadesList = new ArrayList<>();
            subespecialidadesList = session.createQuery(" FROM Subespecialidades WHERE especialidadesId =: id").setParameter("id", idEsp).getResultList();
            for (Subespecialidades subespecialidades : subespecialidadesList) {
                if (obra.getTipo().trim().equals("UO")) {
                    if (checkUnidadObra(subespecialidades.getUnidadobrasById().stream().collect(Collectors.toList()), idOb, idEmp, idZon, idObj, idNiv, idEsp).size() > 0) {
                        JFXCheckBox checkBox = new JFXCheckBox(subespecialidades.getCodigo());
                        checkBox.setSelected(true);
                        subDuplicateModelObservableList.add(new SubDuplicateModel(subespecialidades.getId(), checkBox, subespecialidades.getDescripcion()));
                    }
                } else {
                    if (checkNivelespecificoList(subespecialidades.getNivelespecificosById().stream().collect(Collectors.toList()), idOb, idEmp, idZon, idObj, idNiv, idEsp).size() > 0) {
                        JFXCheckBox checkBox = new JFXCheckBox(subespecialidades.getCodigo());
                        checkBox.setSelected(true);
                        subDuplicateModelObservableList.add(new SubDuplicateModel(subespecialidades.getId(), checkBox, subespecialidades.getDescripcion()));
                    }
                }
            }

            tx.commit();
            session.close();
            return subDuplicateModelObservableList;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public List<Unidadobra> getUnidadesToCheck(int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp, int idS) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobra> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery(" FROM Unidadobra ").getResultList();
            tx.commit();
            session.close();
            return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idOb && unidadobra.getEmpresaconstructoraId() == idEmp && unidadobra.getZonasId() == idZon && unidadobra.getObjetosId() == idObj && unidadobra.getNivelId() == idNiv && unidadobra.getEspecialidadesId() == idEsp && unidadobra.getSubespecialidadesId() == idS).collect(Collectors.toList());
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }

    public boolean getUnidadesToCheckUO(int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp, int idSu, String code) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            chekUni = false;
            Obra ob = projectStructureSingelton.getObra(idOb);
            Unidadobra uo = ob.getUnidadobrasById().parallelStream().filter(unidadobra -> unidadobra.getEmpresaconstructoraId() == idEmp && unidadobra.getZonasId() == idZon && unidadobra.getObjetosId() == idObj && unidadobra.getNivelId() == idNiv && unidadobra.getEspecialidadesId() == idEsp && unidadobra.getSubespecialidadesId() == idSu && unidadobra.getCodigo().trim().equals(code.trim())).findFirst().orElse(null);
            if (uo != null) {
                chekUni = true;
            }
            tx.commit();
            session.close();
            return chekUni;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return chekUni;
    }

    public List<Nivelespecifico> getNivelToCheck(int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp, int idS) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery(" FROM Nivelespecifico ").getResultList();
            tx.commit();
            session.close();
            return unidadobraList.parallelStream().filter(unidadobra -> unidadobra.getObraId() == idOb && unidadobra.getEmpresaconstructoraId() == idEmp && unidadobra.getZonasId() == idZon && unidadobra.getObjetosId() == idObj && unidadobra.getNivelId() == idNiv && unidadobra.getEspecialidadesId() == idEsp && unidadobra.getSubespecialidadesId() == idS).collect(Collectors.toList());
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();

    }

    public Integer createZona(Zonas zonas) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(zonas);
            tx.commit();
            session.close();
            return id;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public Integer createObjetos(Objetos objetos) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(objetos);
            tx.commit();
            session.close();
            return id;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public Integer createNivel(Nivel nivel) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(nivel);
            tx.commit();
            session.close();
            return id;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public void createNiveles(List<Nivel> nivelList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countNiv = 0;
            for (Nivel niv : nivelList) {
                countNiv++;
                if (countNiv > 0 && countNiv % batchSizeNiv == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(niv);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Error al importar el Nivel");
            alert.showAndWait();
        }
    }

    public List<Unidadobra> getUnidadobraList(int idOb, int idEmp, int idZon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobra> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Unidadobra WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).getResultList();

            tx.commit();
            session.close();
            return unidadobraList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Nivelespecifico> getNivelespecificoList(int idOb, int idEmp, int idZon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> nivelespecificoList = new ArrayList<>();
            nivelespecificoList = session.createQuery("FROM Nivelespecifico WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).getResultList();

            tx.commit();
            session.close();
            return nivelespecificoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Nivelespecifico> getNivelespecificoByObjectsList(int idOb, int idEmp, int idZon, int idObj) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> nivelespecificoArrayList = new ArrayList<>();
            nivelespecificoArrayList = session.createQuery("FROM Nivelespecifico WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo AND objetosId =: idObje").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).setParameter("idObje", idObj).getResultList();

            tx.commit();
            session.close();
            return nivelespecificoArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Unidadobra> getUnidadobraByObjetosList(int idOb, int idEmp, int idZon, int idObj) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobra> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Unidadobra WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo AND objetosId =: idObje").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).setParameter("idObje", idObj).getResultList();

            tx.commit();
            session.close();
            return unidadobraList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Unidadobra> getUnidadobraByNivelesList(int idOb, int idEmp, int idZon, int idObj, int idNiv) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobra> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Unidadobra WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo AND objetosId =: idObje AND nivelId =: idn").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).setParameter("idObje", idObj).setParameter("idn", idNiv).getResultList();


            tx.commit();
            session.close();
            return unidadobraList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Unidadobra> getUnidadobraByEspecialidadList(int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Unidadobra> unidadobraList = new ArrayList<>();
            unidadobraList = session.createQuery("FROM Unidadobra WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo AND objetosId =: idObje AND nivelId =: idn AND especialidadesId =: idES").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).setParameter("idObje", idObj).setParameter("idn", idNiv).setParameter("idES", idEsp).getResultList();


            tx.commit();
            session.close();
            return unidadobraList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Nivelespecifico> getNivelEspecificoByNivelesList(int idOb, int idEmp, int idZon, int idObj, int idNiv) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> nivelespecificoList = new ArrayList<>();
            nivelespecificoList = session.createQuery("FROM Nivelespecifico WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo AND objetosId =: idObje AND nivelId =: idn").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).setParameter("idObje", idObj).setParameter("idn", idNiv).getResultList();

            tx.commit();
            session.close();
            return nivelespecificoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Nivelespecifico> getNivelEspecificoByEspecialidadList(int idOb, int idEmp, int idZon, int idObj, int idNiv, int idEsp) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List<Nivelespecifico> nivelespecificoList = new ArrayList<>();
            nivelespecificoList = session.createQuery("FROM Nivelespecifico WHERE obraId =: idO AND empresaconstructoraId =: idE AND zonasId =: idZo AND objetosId =: idObje AND nivelId =: idn AND especialidadesId =: idES").setParameter("idO", idOb).setParameter("idE", idEmp).setParameter("idZo", idZon).setParameter("idObje", idObj).setParameter("idn", idNiv).setParameter("idES", idEsp).getResultList();

            tx.commit();
            session.close();
            return nivelespecificoList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Capturar datos de las estructura de la obra creada
     */

    public List<Objetos> getObjetosbyZona(int idZon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Zonas z = session.get(Zonas.class, idZon);
            z.getObjetosById().size();

            tx.commit();
            session.close();
            return z.getObjetosById().stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Nivel> getNivelesByObjectsInZona(int idZon) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Nivel> nivelList = new ArrayList<>();
            List<Tuple> results = session.createQuery("SELECT niv.id, niv.codigo, niv.descripcion, niv.objetosId FROM Nivel niv INNER JOIN Objetos obj ON niv.objetosId = obj.id WHERE obj.zonasId =: idZ", Tuple.class).setParameter("idZ", idZon).getResultList();
            for (Tuple result : results) {
                Nivel nivel = new Nivel();
                nivel.setId(Integer.parseInt(result.get(0).toString()));
                nivel.setCodigo(result.get(1).toString());
                nivel.setDescripcion(result.get(2).toString());
                nivel.setObjetosId(Integer.parseInt(result.get(3).toString()));
                nivelList.add(nivel);
            }

            tx.commit();
            session.close();
            return nivelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Nivel> getNivelesByObra(int idOb) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Nivel> nivelList = new ArrayList<>();
            List<Tuple> results = session.createQuery("SELECT niv.id, niv.codigo, niv.descripcion, niv.objetosId FROM Nivel niv INNER JOIN Objetos obj ON niv.objetosId = obj.id INNER JOIN Zonas zon ON  obj.zonasId = zon.id WHERE zon.obraId =: idZ", Tuple.class).setParameter("idZ", idOb).getResultList();
            for (Tuple result : results) {
                Nivel nivel = new Nivel();
                nivel.setId(Integer.parseInt(result.get(0).toString()));
                nivel.setCodigo(result.get(1).toString());
                nivel.setDescripcion(result.get(2).toString());
                nivel.setObjetosId(Integer.parseInt(result.get(3).toString()));
                nivelList.add(nivel);
            }

            tx.commit();
            session.close();
            return nivelList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Nivel> getNivelbyOb(int idObj) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Objetos objetos = session.get(Objetos.class, idObj);
            objetos.getNivelsById().size();

            tx.commit();
            session.close();
            return objetos.getNivelsById().stream().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Integer createUnidadObra(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(unidadobra);
            tx.commit();
            session.close();
            return id;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }

    public Integer createNivelEspecifico(Nivelespecifico nivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idN = null;
        try {
            tx = session.beginTransaction();
            idN = (Integer) session.save(nivelespecifico);
            tx.commit();
            session.close();
            return idN;
        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return idN;
    }

    public List<Unidadobrarenglon> getUnidadobrarenglonArrayList(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Unidadobrarenglon> unidadobrarenglonList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Unidadobra unidadobra1 = session.get(Unidadobra.class, unidadobra.getId());
            unidadobra1.getUnidadobrarenglonsById().size();
            unidadobrarenglonList.addAll(unidadobra1.getUnidadobrarenglonsById());

            tx.commit();
            session.close();
            return unidadobrarenglonList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Renglonnivelespecifico> getRenglonNiRenglonnivelespecificoList(Nivelespecifico nivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Renglonnivelespecifico> renglonnivelespecificoList = new ArrayList<>();
            Nivelespecifico nivelespecifico1 = session.get(Nivelespecifico.class, nivelespecifico.getId());
            nivelespecifico1.getRenglonnivelespecificosById().size();
            renglonnivelespecificoList.addAll(nivelespecifico1.getRenglonnivelespecificosById());

            tx.commit();
            session.close();
            return renglonnivelespecificoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
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

    public void createRenglonnivelEspecificoList(List<Renglonnivelespecifico> unidadobrarenglonArray) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countUOR = 0;

            for (Renglonnivelespecifico unidadobrarenglon : unidadobrarenglonArray) {
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

    public List<Bajoespecificacion> getBajoespecificacionList(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Bajoespecificacion> list = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            list = session.createQuery(" FROM Bajoespecificacion WHERE unidadobraId =: idUn ").setParameter("idUn", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return list;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Bajoespecificacionrv> getBajoespecificacionListBajoespecificacionrvs(Nivelespecifico nivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Bajoespecificacionrv> list = new ArrayList<>();
            list = session.createQuery(" FROM Bajoespecificacionrv WHERE nivelespecificoId =: idUn ").setParameter("idUn", nivelespecifico.getId()).getResultList();

            tx.commit();
            session.close();
            return list;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
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

    public void createBajoespecificacionrvList(List<Bajoespecificacionrv> bajoespecificacionList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countbajo = 0;

            for (Bajoespecificacionrv bajoespecificacion : bajoespecificacionList) {
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

    public List<Planificacion> getUnidaPlanificacions(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Planificacion> planificacionList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planificacionList = session.createQuery("FROM Planificacion WHERE unidadobraId=: idUn").setParameter("idUn", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return planificacionList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Planrenglonvariante> getPlanrenglonvarianteList(Nivelespecifico nivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Planrenglonvariante> planificacionList = new ArrayList<>();
            planificacionList = session.createQuery("FROM Planrenglonvariante WHERE nivelespecificoId=: idUn").setParameter("idUn", nivelespecifico.getId()).getResultList();

            tx.commit();
            session.close();
            return planificacionList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Planrecuo> getPlanrecuoList(Unidadobra unidadobra, Planificacion plan) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Planrecuo> planrecuoList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            planrecuoList = session.createQuery("FROM Planrecuo WHERE unidadobraId =: idUn AND planId =: idPl ").setParameter("idUn", unidadobra.getId()).setParameter("idPl", plan.getId()).getResultList();

            tx.commit();
            session.close();
            return planrecuoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Planrecrv> getPlanrecrvList(Nivelespecifico nivelespecifico, Planrenglonvariante plan) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Planrecrv> planrecuoList = new ArrayList<>();
            planrecuoList = session.createQuery("FROM Planrecrv WHERE nivelespId =: idUn AND planId =: idPl ").setParameter("idUn", nivelespecifico.getId()).setParameter("idPl", plan.getId()).getResultList();

            tx.commit();
            session.close();
            return planrecuoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public int createCertificacionUO(Certificacion certificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();

            idUni = (Integer) session.save(certificacion);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return idUni;
    }

    public int createCertificacionRV(CertificacionRenglonVariante certificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();

            idUni = (Integer) session.save(certificacion);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return idUni;
    }

    public int createPlanificacion(Planificacion planificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();

            idUni = (Integer) session.save(planificacion);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return idUni;
    }

    public int createPlanificacionRV(Planrenglonvariante planificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        Integer idUni = null;
        try {
            tx = session.beginTransaction();

            idUni = (Integer) session.save(planificacion);

            tx.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return idUni;
    }

    public void createPlanrecuo(List<Planrecuo> planrecuoList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Planrecuo planrecuo : planrecuoList) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(planrecuo);
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

    public void createPlanrecrv(List<Planrecrv> planrecuoList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Planrecrv planrecuo : planrecuoList) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(planrecuo);
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

    public List<Certificacion> getUnidaCertificacions(Unidadobra unidadobra) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Certificacion> certificacionList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionList = session.createQuery(" FROM Certificacion WHERE unidadobraId=: idUn").setParameter("idUn", unidadobra.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<CertificacionRenglonVariante> getCertificacionRenglonVarianteList(Nivelespecifico nivelespecifico) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<CertificacionRenglonVariante> certificacionList = new ArrayList<>();
            certificacionList = session.createQuery(" FROM CertificacionRenglonVariante WHERE nivelespecificoId=: idUn").setParameter("idUn", nivelespecifico.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Certificacionrecuo> getCertificacionrecuoList(Unidadobra unidadobra, Certificacion certificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();
        List<Certificacionrecuo> certificacionrecuoList = new ArrayList<>();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            certificacionrecuoList = session.createQuery("FROM Certificacionrecuo WHERE unidadobraId =: idUn AND certificacionId =: idPl ").setParameter("idUn", unidadobra.getId()).setParameter("idPl", certificacion.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionrecuoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public List<Certificacionrecrv> getCertificacionrecrvList(Nivelespecifico nivelespecifico, CertificacionRenglonVariante certificacion) {
        Session session = ConnectionModel.createAppConnection().openSession();

        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List<Certificacionrecrv> certificacionrecuoList = new ArrayList<>();
            certificacionrecuoList = session.createQuery("FROM Certificacionrecrv WHERE nivelespId =: idUn AND certificacionId =: idPl ").setParameter("idUn", nivelespecifico.getId()).setParameter("idPl", certificacion.getId()).getResultList();

            tx.commit();
            session.close();
            return certificacionrecuoList;
        } catch (Exception he) {
            he.printStackTrace();
        } finally {
            session.close();
        }
        return new ArrayList<>();
    }

    public void createCertifrecuo(List<Certificacionrecuo> certificacionrecuos) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Certificacionrecuo certificacionrecuo : certificacionrecuos) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(certificacionrecuo);
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

    public void createCertificacionRV(List<Certificacionrecrv> certificacionrecuos) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countplan = 0;

            for (Certificacionrecrv certificacionrecuo : certificacionrecuos) {
                countplan++;
                if (countplan > 0 && countplan % batchPlanrec == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(certificacionrecuo);
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

    public Integer addNuevaObra(Obra obra) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            obId = (Integer) obsession.save(obra);
            trx.commit();
            obsession.close();
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return obId;
    }

    public List<Empresaobrasalario> empresaobrasalarioList(Obra obra) {

        Session obsession = ConnectionModel.createAppConnection().openSession();
        Transaction trx = null;
        Integer obId = null;

        try {
            trx = obsession.beginTransaction();
            List<Empresaobrasalario> empresaobrasalarioList = new ArrayList<>();
            empresaobrasalarioList = obsession.createQuery(" FROM Empresaobrasalario WHERE obraId =: idO").setParameter("idO", obra.getId()).getResultList();

            trx.commit();
            obsession.close();
            return empresaobrasalarioList;
        } catch (HibernateException he) {
            if (trx != null) trx.rollback();
            he.printStackTrace();
        } finally {
            obsession.close();
        }
        return new ArrayList<>();
    }

    public void caddNuevaObraEmpresa(List<Empresaobra> empresaobraList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countEO = 0;

            for (Empresaobra eo : empresaobraList) {
                countEO++;
                if (countEO > 0 && countEO % batchEmpresaObra == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }

    }

    public void caddNuevaEmpresaObraSalario(List<Empresaobrasalario> empresaobrasalarioList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countEO = 0;

            for (Empresaobrasalario eo : empresaobrasalarioList) {
                countEO++;
                if (countEO > 0 && countEO % batchEmpresaObra == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }

    }

    public void caddNuevaEmpresObraConceptos(List<Empresaobraconcepto> empresaobraconceptoList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countEOC = 0;

            for (Empresaobraconcepto eo : empresaobraconceptoList) {
                countEOC++;
                if (countEOC > 0 && countEOC % batchEmpresaObraConcep == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }

    }

    public void caddNuevoCoeficienteEquipo(List<Coeficientesequipos> coeficientesequiposList) {

        Session session = ConnectionModel.createAppConnection().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            countEOC = 0;

            for (Coeficientesequipos eo : coeficientesequiposList) {
                countEOC++;
                if (countEOC > 0 && countEOC % batchEmpresaObraConcep == 0) {
                    session.flush();
                    session.clear();
                }

                session.persist(eo);
            }
            tx.commit();
            session.close();
        } catch (Exception e) {
        }

    }

    public ObservableList<RecursosToChangeObserve> listToSugestionSuministros(int idObra) {
        Session session = ConnectionModel.createAppConnection().openSession();


        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            toChangeObserveObservableList = FXCollections.observableArrayList();
            recursosArrayList = new ArrayList<>();
            recursosArrayList = (ArrayList<Recursos>) session.createQuery("FROM Recursos WHERE tipo = '1'").getResultList();
            toChangeObserveObservableList.addAll(recursosArrayList.parallelStream().map(recursos -> {
                RecursosToChangeObserve recursosToChangeObserve = new RecursosToChangeObserve(recursos.getId(), recursos.getCodigo(), recursos.getDescripcion(), recursos.getUm(), recursos.getPreciomn(), "1");
                return recursosToChangeObserve;
            }).collect(Collectors.toList()));

            juegoproductoArrayList = new ArrayList<>();
            juegoproductoArrayList = (ArrayList<Juegoproducto>) session.createQuery("FROM Juegoproducto ").list();
            toChangeObserveObservableList.addAll(juegoproductoArrayList.parallelStream().map(juegoproducto -> {
                RecursosToChangeObserve recursosToChangeObserve = new RecursosToChangeObserve(juegoproducto.getId(), juegoproducto.getCodigo(), juegoproducto.getDescripcion(), juegoproducto.getUm(), juegoproducto.getPreciomn(), "J");
                return recursosToChangeObserve;
            }).collect(Collectors.toList()));

            suministrossemielaboradosArrayList = new ArrayList<>();
            suministrossemielaboradosArrayList = (ArrayList<Suministrossemielaborados>) session.createQuery("FROM Suministrossemielaborados ").list();
            toChangeObserveObservableList.addAll(suministrossemielaboradosArrayList.parallelStream().map(suministrossemielaborados -> {
                RecursosToChangeObserve recursosToChangeObserve = new RecursosToChangeObserve(suministrossemielaborados.getId(), suministrossemielaborados.getCodigo(), suministrossemielaborados.getDescripcion(), suministrossemielaborados.getUm(), suministrossemielaborados.getPreciomn(), "S");
                return recursosToChangeObserve;
            }).collect(Collectors.toList()));

            tx.commit();
            session.close();
            return toChangeObserveObservableList;

        } catch (HibernateException he) {
            if (tx != null) tx.rollback();
            he.printStackTrace();
        } finally {
            session.close();
        }
        return FXCollections.emptyObservableList();
    }


}
